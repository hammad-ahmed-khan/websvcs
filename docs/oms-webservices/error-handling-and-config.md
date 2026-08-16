# Error Handling & Configuration

## Error Handling

### PL/SQL layer
- The custom subscriber/invoker packages (`OMSSUB_ASNOUT`, `OMSSUB_RECEIVING`, the four `*_WS_INVOKER` packages) follow the standard RMS `API_LIBRARY.HANDLE_ERRORS` / `SQL_LIB.CREATE_MSG` pattern: a `PROGRAM_ERROR` exception is raised internally and translated into an `O_status_code`/`O_error_message` pair returned to the RIB caller, rather than propagating a raw Oracle exception. `P_INS_ERROR_DTL` (a custom error-logging procedure, referenced throughout but not itself in this repository) is called from several `WHEN OTHERS` handlers to persist error detail rows before continuing or returning.
- `XX_DLV_ADDRESS_UPDATE`, `XX_REFUND_REQUEST_PKG`, and `XXHDB_CORE_PKG` each use their own `WHEN OTHERS`/named-exception handlers that catch `SQLERRM`, `ROLLBACK`, and return a status/message pair through `OUT` parameters — none of the ten packages raises an unhandled Oracle exception back to its Java or RIB caller under normal failure conditions; failures are always translated into a status code + message.
- `UTL_HTTP`-based outbound calls (inside `generic_oms_soap_call` in each `*_WS_INVOKER` package) specifically catch `UTL_HTTP.transfer_timeout`, ORA-29273 (HTTP request failure, via `PRAGMA EXCEPTION_INIT`), and `UTL_HTTP.end_of_body`, returning a canned SOAP-fault XML body in each case rather than raising.

### Java layer — SOAP services
- The legacy JAX-WS services (CustomerOrderService, OMSCustomerOrder, PaymentConfirmation, VASContract, the RMA services, SparePartsRequest/Confirmation/Cancel) generally catch business-rule violations as custom exceptions and translate them into `SOAPFaultException`s with a defined fault code (e.g. `INVALID_RMA_ID`, `RMA_MOD_GT_QTY`, `ITEM_ALRDY_CLD`, `UNAVL_INV`, `TABLE_LOCKED`), or into a structured response object with a status/message field rather than a fault, depending on the module's convention — there is no single consistent error-response shape across all SOAP services; each module defines its own response DTO with its own status/message/code fields (e.g. `message_status`/`message_desc`, `Status`/`message_code`, `messageStatus`/`messageCode`/`messageDesc`).
- Several modules (SparePartsRequest, SparePartsConfirmation, OMSCOCancellation via payment-related flows) implement **compensating-transaction rollback** on mid-operation SOAP failure — a dedicated `*ReversalBean` re-issues offsetting SIM/RMS calls and writes an audit row (`QUANTITY_REJECTED`/`QUANTITY_RECEIVED` event types) rather than relying on any database transaction rollback, since the affected state spans multiple systems (OMS DB, SIM, RMS) that cannot share a single transaction.
- `SparePartsConfirmation` additionally supports **async retry via `OMS_REPUBLISH_DATA`** as an alternative to synchronous compensating rollback — on a SIM adjustment failure, it queues a full SOAP-envelope XML for later resend rather than attempting an immediate reversal, a different resilience strategy from its sibling `SparePartsRequest`.

### Java layer — REST services
- Spring `@RestController` modules mostly return a `200 OK` with a body-level status field (`success`/`code`/`message`, or `status`/`error`) even for business-logic failures — HTTP status codes are used inconsistently across modules (some return `500` for validation failures — e.g. `TransferCreationController`'s "Invalid Item/Qty Value" — others return `200` with a failure payload).
- `oms-core`'s `BaseController.handleException` (referenced by the Payment/Finance cluster research for `apple-pricing`) is one of the few centralized exception-handling patterns found; most other REST modules handle exceptions locally per-controller-method with try/catch.
- `oms-cancellation` and `Hybris_Cancellation` both mark methods `@Transactional` with **no transaction manager configured** in their Spring context (`cancellationAPI-servlet.xml`, `dispatcher-servlet.xml`) — meaning the annotation is inert and multi-statement operations are **not atomic**. A failure partway through (e.g. `updateCancellationDetail`'s 6+ batched updates, or the Hybris head/item/tender insert sequence) leaves partial writes committed with no rollback. This is a genuine correctness gap present in the shipped code, not a documentation limitation.

### Database error propagation to the API consumer
For a typical stored-procedure-backed API (e.g. `DeliveryUpdate`'s `XX_DLV_ADDRESS_UPDATE` call, or `apple-pricing`'s `XX_APPLE_ON_PRICING_SQL` calls): an Oracle exception inside the procedure is caught by the procedure's own `WHEN OTHERS` handler, converted to a status/message OUT parameter pair, read back by the Java `CallableStatement` caller, and surfaced in the REST/SOAP response body — the original Oracle error code/`SQLERRM` text is sometimes embedded verbatim in the message field (e.g. `XX_DLV_ADDRESS_UPDATE`'s `'Cannot update delivery address - ' || SQLERRM`), meaning raw Oracle error text can reach an external caller in several of these APIs.

### Logging
Logging conventions vary by module vintage: legacy JAX-WS modules mostly use `DBMS_OUTPUT.PUT_LINE` (PL/SQL, visible only via a DB trace/session, not surfaced anywhere else) or `System.out`/basic `Logger` calls; newer Spring modules use `log4j`/`slf4j`. One concrete logging-related finding: `notification`'s `NotificationTemplateDAO` logs the SIM database password at INFO level when opening its second (SIM) connection — a security issue, not merely a style inconsistency (see `unknowns.md` and the Integration/Notification category doc for detail).

---

## Configuration & Integration Dependencies

### Database connections
Every module falls into one of three patterns:
1. **JNDI datasource lookup** (most common) — `InitialContext().lookup("jdbc/oms")` or similar, JNDI names seen: `jdbc/oms`, `jdbc/sim`, `jdbc/rms`, `jdbc/wms`, `jdbc/xtradas`, `jdbc/extradev`. Resolved by the application server (WebLogic) at deploy time; the actual connection-pool target is server configuration, not in this repository.
2. **Direct `DriverManagerDataSource`** with a JDBC URL + credentials in `application.properties` — used by newer Spring Boot-style modules (`oms-cancellation-job`, `oms-core`'s secondary `rmsJndiTemplate`, `label_Package_Creation`, several batch jobs). **These files contain plaintext production database credentials** — see the Security Findings section below.
3. **Cross-schema DB links** — `@rmsdb` (SIM→RMS), `@simdb` (RMS/OMS→SIM), used in several raw SQL queries (`SIMDeliveryDetail`, `Stock_Feed`, `spareParts-stockRequest`, `oms-customer-order`'s `XX_OMS_INV@SIMDB`, `XX_DLV_ADDRESS_UPDATE`'s `ful_ord@simdb`). These confirm the system spans at least three separate Oracle schemas/databases (OMS, RMS, SIM) that are cross-queried directly rather than only integrated via web services.

### RMS / SIM / RWMS / Siebel connection
Reached exclusively via generated JAX-WS SOAP clients (`oracle-base`, legacy duplicate in `OracleIntegrationServices`) or hand-written Feign wrappers (`oms-common`'s `IOracleRMSClient`, `IOracleSIMClient`, `IOracleInvAdjClient`). Target URLs come from `${rms.oralce.base.api.url}` / `${sim.oralce.base.api.url}` (note the consistent "oralce" typo in the property key across every module that uses it) Spring properties, or from the dynamic `OMS_WEBSERVICE_URI_DETAIL` DB lookup for the legacy JAX-WS client path. See `categories/core-gateway-oracleintegrationservices.md` for the full endpoint/operation inventory.

### JMS / RIB
No RIB adapter, JMS connection factory, or AQ configuration is present in this repository. The custom PL/SQL subscriber packages (`OMSSUB_ASNOUT`, `OMSSUB_RECEIVING`) plug into RMS's own RIB dequeue framework via the standard `API_LIBRARY`/`RIB_OBJECT` calling convention — RIB itself (queues, topics, adapters) is Oracle Retail middleware external to this codebase.

### External services (MuleSoft CloudHub, ZATCA, Apple, payment gateways)
All reached via REST/SOAP Feign or OkHttp/`HttpURLConnection` clients with target URLs and (frequently) credentials in per-module `application.properties`. See `categories/*.md` for the specific CloudHub API paths (`customer-profile-v3`, `jood-api`, `reconciliation-data-api-v1`, `communication-v1`, an address-management API) and `categories/payment-finance-pricing-einvoicing.md` for the ZATCA and Apple Pricing API integrations.

### Web service deployment
Two generations of deployment coexist:
- **Legacy**: standalone WAR modules with `public_html/WEB-INF/web.xml` + a checked-in WSDL, deployed individually to WebLogic (evidenced by `weblogic.xml`/`weblogic-ejb-jar.xml` where present, and JNDI-based EJB lookups via `t3://` provider URLs).
- **Modern**: Maven/Spring modules (`oms-core`, `oms-cancellation`, `bds-service`, `jood`, etc.) using Spring's `DispatcherServlet` and either WAR (`web.xml` + `webapp/WEB-INF`) or, for a few (`boot-apple-pricing`), Spring Boot's embedded servlet container with no `web.xml` at all.

Both generations run side-by-side in production — e.g. `apple-pricing` (WAR) and `boot-apple-pricing` (Spring Boot repackage of the identical logic) both exist in the repository simultaneously, and `OMSCOCancellation` (legacy SOAP) coexists with `oms-cancellation` (modern REST) implementing overlapping functionality against the same schema.

---

## Security Findings (surfaced during this documentation effort — not part of the requested scope, but material enough to record)

These were discovered incidentally while tracing configuration for the documentation above. They are **not** claims about production state (which config file is actually deployed cannot be verified from source alone) — they are observations about what is committed to this repository.

1. **Plaintext production credentials committed to source control** — at least 27 `application.properties`/`db.properties`/`persistence.xml`-adjacent files across modules including `oms-core`, `Hybris_Cancellation`, `oms-cancellation-job`, `bds-service`, `label_Package_Creation`, `AwbOrder-Notification`, `NOON/*`, `jood-membership-sync`, `ecom-reconcilliation`, `home-maintenance-sync`, `einvoicing`, `apple-pricing`/`boot-apple-pricing`, `imei-validation`, `oms-discount`, `oms-einvoicingxml`, `extra-imei-capture`, `notification`'s SIM DB connection, contain plaintext Oracle database passwords, CloudHub API client secrets, JWT signing secrets, and (in `einvoicing`) ZATCA production private-key/certificate/secret material. Values are not reproduced anywhere in this documentation set.
2. **TLS validation disabled on multiple outbound clients** — `oms-core` (Mule address API), `jood-membership-sync`, `ecom-reconcilliation`, `home-maintenance-sync`, and `apple-pricing` all install a trust-all `X509TrustManager`/permissive `HostnameVerifier` on their Feign/OkHttp clients to CloudHub-hosted or Apple's endpoints.
3. **`proxy-api` is an unauthenticated, unrestricted outbound HTTP relay** — no host allowlist, TLS validation bypassed, zero timeouts, forwards a caller-supplied `Authorization` header to any URL the caller specifies via a request header. This is a functional SSRF primitive as shipped.
4. **Hardcoded production credentials in Java source** (not config) — `AwbOrder-Notification`'s `EmailNotification`/`SmsNotification` classes embed a literal Basic-Auth token string directly in the `.java` file.
5. **Concurrency bug with security-adjacent impact** — `AwbCustomerNotification`'s `AwbTrackingDAOImpl` is a singleton Spring bean storing per-request state (`awbheaderId`, `omsCustOrdNo`) in instance fields; concurrent requests can cross-contaminate each other's tracking-number/order associations.
6. **SQL built by string concatenation** in specific queries within `NOON/ExtraServiceRest` and `NOON/transferCreation` (rather than bind parameters) — a SQL-injection risk localized to those queries, not a system-wide pattern (the large majority of the codebase's raw SQL uses bind parameters correctly).

**Recommendation** (outside the scope of the documentation task, noted for completeness): rotate all credentials found in committed properties files, move secrets to a vault/environment-variable mechanism, and remove `proxy-api` from any externally-reachable deployment or add an allowlist + auth check before it is exposed further.
