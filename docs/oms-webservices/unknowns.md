# Unknowns / Requires Environment or Database Verification

This section separates **confirmed from source code**, **inferred** (a reasonable conclusion from code + naming + context, but not literally proven), and **requires runtime/database verification** (cannot be determined from this repository at all). Every item below was flagged as such by the research that produced `categories/*.md`, `plsql-packages.md`, `api-plsql-matrix.md`, or `business-flows.md`.

## PL/SQL packages referenced but not present in this repository

The following packages/procedures are called from code that **is** in this repository, but their own source is not:

| Package.Procedure | Called from | What's known | What's not determinable |
|---|---|---|---|
| `OMS_RECEIPT_CO_SIM_WS_INVOKER.f_REC_SIM_CO_RESERVE` | `OMSSUB_RECEIVING` (PL/SQL) | Called with `(message_id, cust_ord_no)`, return value logged only | Internal logic, SIM operation invoked, error handling |
| `OMS_RECEIPT_CO_WS_INVOKER.f_REC_RMS_CO_RESERVE` | `OMSSUB_RECEIVING` (PL/SQL) | Called with `(message_id, cust_ord_no)` | Internal logic, RMS operation invoked |
| `OMS_RECEIPT_SIM_INVADJ_INVOKER.f_REC_SIM_INVADJ` | `OMSSUB_RECEIVING` (PL/SQL) | Called with `(message_id, response OUT)` | Internal logic |
| `OMS_WS_INVOKER.generic_oms_soap_call` | `OMS_MSG_WS_PUBLISHER` (PL/SQL) | Distinct from the four `OMS_*_WS_INVOKER` packages present in this repo; presumably a shared/common version of the same `UTL_HTTP` pattern | Whether it's truly shared code or a copy-paste sibling |
| `rms14.xx_tsf_cre_carera_sql.xx_dc_tsf_cre` | `CarreraTransferCreation` (Java) | Called with item/qty array + src/dest/ref params, returns tsf_no | Internal logic, RMS transfer tables touched |
| `XX_CREATE_LABEL` | `label_Package_Creation` (Java) | Takes XML in, returns label CLOB + status | Internal logic |
| `XX_HB_MAKE_LABEL` | `CR_17_ExtraAsCourier` (Java) | Takes userId + XML in, returns label CLOB + AWB number + error | Internal logic; a superseded `XX_MAKE_LABEL` name appears commented out in the caller |
| `XX_IS_CANCELLABLE` | `oms-cancellation` (Java) | Takes seqId + omsCustOrdNo, returns status/message/refundAmount | Internal cancellation-eligibility rules |
| `OMS_SHIP_CLASSIFICATION` (function) | Order Management cluster (multiple modules) | Takes item/channel/classification, returns a classification string | Internal classification rules |
| `OMS_INVADJ_STATUS_UNAVIALINV` | Order Management, Cancellation, Payment/Finance clusters | 9-param signature confirmed identically across ~6 call sites | Internal RMS inventory-status logic |
| `XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN` (function) | Order Management, Cancellation clusters | 4-param signature confirmed identically across 5 call sites | Internal transfer-approval logic |
| `XX_JOOD_*` family (`XX_JOOD_MEM_TRANSACTION.XX_JOOD_TRANSACTION_PROCESS`, `XX_JOOD_ELIGIBILITY_CHECK`, `XX_JOOD_POS_ELIGIBILITY_CHECK`, `XX_JOOD_CB_ELIG_CHECK`, `XX_JOOD_CB_TRAN_HISTORY_V1`, `XX_JOOD_CB_STATUS`) | jood, oms-cancellation, OMSRMAGeneration | Full parameter lists confirmed at each call site (up to 21+ positional params) | Internal loyalty/cashback calculation logic |
| `XX_APPLE_ON_PRICING_SQL.APPLE_MPN_REFRESH` / `POST_PUBLISH_UPDATE` / `POST_REPUBLISH_UPDATE` | apple-pricing/boot-apple-pricing | Parameter signatures confirmed | Internal pricing logic |
| `XTRA_TSF_CRE_SQL.XTRA_XTSF_CRE` | NOON/transferCreation | Parameter signature confirmed | Internal transfer logic |
| `XX_IMEI_SN_UPLOAD_VALIDATE.IMEI_SN_UPLOAD_VALIDATE` (incl. `@rmsdb` DB-link variant) | imei-validation, extra-imei-capture | Called with item/imei/location/source | Internal validation logic |
| `XX_IMEI_SN_CONFIG_VALIDATE.XX_IMEI_SN_CONF_VAL` | extra-imei-capture | Called signature confirmed | Internal logic |
| `INACTIVE_TRACKING_ECOMM_ORDERS` | inactive_Track_id | Called with 2 params | Internal logic |
| `GET_CLASSIFICATION_BO_TEST` (function) | OmsOrposInventoryCheckNew | Takes an Oracle array of item/classification pairs, returns array of structs | Internal logic |
| `XXHDB_CORE_PKG.reserve_booking_small` / `cancel_booking_small` / `BOOK_ORDER` (procedure-name variants) | OMSCustomerOrder, oms-cancellation, bds-service | These names do not appear verbatim in the `XXHDB_CORE_PKG`/`_BODY` source checked into this repository, which only defines `get_schedule`, `reserve_booking`, `create_booking`, `confirm_booking`, `cancel_booking` | Whether the live database has additional overloaded/suffixed procedures beyond this repo's copy of the package body — **flagged explicitly for DB verification** |

## Standard Oracle Retail RMS 14 package internals

This repository contains **zero** standard RMS 14 PL/SQL source (no `RMSSUB_*`, `RMSCPI_*`, or core RMS package bodies beyond the custom `OMSSUB_*` subscribers that plug into RMS's subscriber framework via `API_LIBRARY`, `API_CODES`, `SQL_LIB`). Every reference in this documentation to "standard RMS behavior" (e.g. what happens inside RMS after `FulfillOrderService.createFulfilOrdColDesc` is called, or what RMS itself does with an ASN-out RIB message before publishing it) is based on the **Oracle Retail RMS 14 product documentation's known public behavior**, not on source in this repository, and should be verified against the actual RMS 14 installation if precise internal behavior matters.

## Runtime / deployment configuration

- **Production endpoint URLs for RMS/SIM/RWMS/Siebel web services**: some are hardcoded as fallback values in generated JAX-WS client stubs (e.g. `oracle-base`'s WSDL-cached `soap:address` values, mostly pointing at a vendor dev host `licrpap14-extra.logicindia.com`), others are resolved at runtime from the `OMS_WEBSERVICE_URI_DETAIL` table (contents not in this repository), and others come from `application.properties` files that contain a mix of QA and commented-out production values. **The actual production endpoint topology is not fully reconstructable from source alone** — it requires either DB access to `OMS_WEBSERVICE_URI_DETAIL` or the live application-server deployment configuration.
- **WebLogic deployment descriptors** (`weblogic.xml`, `weblogic-ejb-jar.xml`) are present for only some of the ~40 legacy WAR/EJB modules; where absent (e.g. `OMSRMAModification`, `OMSRMADeletion`, `OMSSparePartsCancel`), the deployed context root/JNDI bindings are not determinable from source.
- **JMS/RIB queue and topic configuration**: this repository contains the PL/SQL side of RIB message *consumption* (`OMSSUB_ASNOUT`, `OMSSUB_RECEIVING`) but no RIB adapter configuration, AQ queue definitions, or JMS connection factory setup — those live in the RMS/RIB middleware tier, not in this Java+PL/SQL repository.
- **Scheduling for batch jobs**: shell scripts exist for `OMS_MSG_WS_PUBLISHER` (`xtra_oms_ws_republish.ksh`) and one non-inventory-item script, but the actual cron/scheduler entries that invoke the various Java batch `main()` classes (`oms-cancellation-job`, `jood-membership-sync`, `home-maintenance-sync`, `AwbOrder-Notification`, `Order-Notification`, `inactive_Track_id`, `notification`) are **not present in this repository** — their invocation frequency/timing is not determinable from source.
- **`OMS_WEBSERVICE_URI_DETAIL` table contents**: referenced by both PL/SQL and Java as the runtime lookup for several web-service URLs, but the table's actual row contents are runtime data, not schema/source — not determinable without DB access.
- **`OMS_SYSTEM_PARAMETERS` values**: dozens of behavioral toggles throughout the system (`CALL_SIEBEL`, `REPUBLISH_STOP_COUNT`, `SIEBEL_WS_REPUB_IND`, `SHIPPING_CHARGE_DEPT`, `RESV_INV_STATUS`, `REASON_CODE`, `FAILED_ORDERS_REQ_RESP`, threshold parameters in `OmsOrposInventoryCheckNew`, etc.) are read by key at runtime; their actual configured values are runtime data, not determinable from source.

## Database objects referenced but not conclusively confirmed to exist as named

- `XXHDB_CORE_PKG`'s Java-visible `_small`-suffixed and `BOOK_ORDER` procedure variants (see table above).
- Several views referenced only by name in Java raw SQL (e.g. `XX_OMS_INVAVAIL_V_BACK`, `XX_OMS_POS_INV_V`, `V_CUST_FUTURE_INV_ECOM_FEED_T/B/P`, `XXHDB_SCHEDULE_V`, `GET_PACK_QTY_V`) — their column lists and underlying definitions are not in this repository (they are database view objects, not files under version control here).
- Standard RMS base tables referenced throughout (`ITEM_MASTER`, `ITEM_LOC`, `WH`, `STORE`, `TSFHEAD`/`TSFDETAIL`, `ORDCUST`, `SHIPMENT`/`SHIPSKU`, `ITEM_LOC_TRAITS`, etc.) are assumed to be the standard RMS 14 schema based on naming convention and are not further documented here — see standard Oracle Retail RMS 14 data model documentation.

## External systems whose internal behavior is out of scope

- **Oracle Retail SIM 14** (Store Inventory Management) — this repository only contains the SOAP client contracts (WSDL) and Java code that calls them; SIM's own server-side behavior is external.
- **Oracle Retail RWMS** (Warehouse Management) — same: only the `PendingReturnsService` client contract is present.
- **Siebel CRM** — only the outbound feed contracts (`UpdateOrderStatus`, order-creation BPEL) are visible; Siebel's own processing is external. Endpoint appears to have migrated over time from an on-prem WCF `.svc` host to a MuleSoft CloudHub-fronted URL (both forms found in different generated client copies) — the current live endpoint is a deployment-time fact, not determinable from source.
- **MuleSoft CloudHub integration layer** — numerous modules (jood, ecom-reconcilliation, AWB/order-notification, address validation) call CloudHub-hosted APIs (`customer-profile-v3`, `jood-api`, `reconciliation-data-api-v1`, `communication-v1`, an address-management API). Only the client-side contracts and (in several cases, concerningly) embedded credentials are visible; Mule's own integration logic and its connections to further downstream systems are out of scope.
- **ZATCA (Saudi tax authority) e-invoicing APIs** — the `einvoicing` module calls ZATCA's clearance/reporting/compliance APIs and bundles a ZATCA signing SDK; ZATCA's own service behavior is external and governed by Saudi regulatory specifications, not this codebase.
- **Payment gateways** (SADAD, Payfort, Tasheel Wallet, Benefit, AFS/FSS) — `XX_REFUND_REQUEST_PKG` and the cancellation flow model refund *requests* to these channels, but no code in this repository calls out to the gateways themselves — that integration (and the callback that eventually calls `update_xrr_processing_status`/`update_xrr_Sadad_status`) is not present in this repository.
- **Apple Pricing API** — only the `apple-pricing`/`boot-apple-pricing` client side is present.

## Explicitly confirmed absences (not just "not found" — actively verified negative)

- `OMSSUB_ASNOUT`, `OMSSUB_RECEIVING`, `OMS_ASNOUT_WS_INVOKER`, `OMS_RECEIPT_WS_INVOKER`, `OMS_SOS_WS_INVOKER`, `OMS_PENDRETURN_WS_INVOKER`, `OMS_MSG_WS_PUBLISHER`: **zero Java call sites anywhere in the ~4,700-file Java codebase**, confirmed independently by all eight cluster research passes. These seven packages are exclusively RIB/cron-triggered PL/SQL with no Java integration point.
- `XX_DLV_ADDRESS_UPDATE`: **exactly one** Java call site (`DeliveryUpdate` module), confirmed by three independent research passes that each grepped for it and found nothing in their own assigned modules.
- `XX_REFUND_REQUEST_PKG.create_xrr_refund_request`: **exactly one** Java call site (`Hybris_Cancellation` module), confirmed by three independent research passes.
- `OMS_SOS_WS_INVOKER` and `OMS_PENDRETURN_WS_INVOKER`: no caller found even *within the other 9 PL/SQL packages present in this repository* — their caller is genuinely external to this repository's checked-in source (most likely RMS-side or RIB-adjacent PL/SQL not included here).

## Modules noted as confirmed non-functional or dead code (not "unknown" — verified from source)

These are not gaps in research; they are things the source code itself proves don't work, included here so they aren't mistaken for working integrations elsewhere in this documentation:
- `oms-customer-order`'s `POST /createNewOrder` — validates but never persists, always returns hardcoded success.
- `VASContract` module's `VASContractEJBSessionBean` — a fully-built JPA persistence class that the module's only SOAP endpoint never calls (the endpoint does its own direct-JDBC insert instead).
- Several Axis 1.4 JAX-RPC skeleton stubs in `Stockcheck` (`InventoryDetailPortBindingImpl`, `StoreInventoryPortBindingImpl`) — every method body is `return null;`.
- `OMSRMADeletion`'s duplicate-delete-guard (`RMA_ALRDY_CLD` check) — wrapped in a try/catch whose catch block is empty, so it never actually blocks a duplicate delete.
- `RWMS PendingReturnsService`'s Java client stub in `OracleIntegrationServices`/`oracle-base` — generated but has no confirmed caller anywhere in the repository (orphaned, matching `OMS_PENDRETURN_WS_INVOKER`'s equally-orphaned status on the PL/SQL side).
