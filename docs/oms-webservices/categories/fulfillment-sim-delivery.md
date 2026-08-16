# Fulfillment / SIM / Delivery Cluster — API Reference

Scope: `SIMDeliveryDetail`, `sim-dispatch`, `DeliveryUpdate`, `CarreraTransferCreation`, `label_Package_Creation` (endpoint-exposing modules). `sim-client`, `extra-sim-client`, `extra-sim-common`, `extra-sim-server`, `extra-sim-ejb` are SDK/vendor code (see note at end).

### processNewOrder (SIMDeliveryDetailWebService)

1. **API**: SOAP operation `processNewOrder`. Module `SIMDeliveryDetail`. WSDL: `SIMDeliveryDetail/public_html/WEB-INF/wsdl/SIMDeliveryDetail.wsdl`, `targetNamespace="http://com.logicinfo.oms/model/"`, doc/literal. Servlet mapping `/SIMDeliveryDetailWebService`.
2. **Entry point**: `oms.logicinfo.com.model.SIMDeliveryDetailWebServiceImpl.processNewOrder` — `:27-43`.
3. **Call chain**:
   - `processNewOrder` → `SIMDeliveryDetailBean.validateInput` (no-op, just logs) → `SIMDeliveryDetailBean.processOrdersDetails` (`:33-70`).
   - Branch on `orderStatus`: `READY_TO_SHIP` → `generateResponse` (`:77-410`) → `SIMDeliveryDetailCommon.processReadyToShipDetails` (`:23-180`); `UNDELIVERED` → `generateResponseForUndelivered` (`:412-681`) → `SIMDeliveryDetailCommon.processUndeliveredDetails` (`:183-335`); other/blank → `throw new Exception("Request Contains Incorrect Data")`.
   - Both generate* methods also use `OMSUtilSessionEJB` (JPA-based EJB, module `OMSUtil`) for header/address/tender/item lookups, and `OMSUtilCommons.getExpressDelvdetails`.
   - No sim-client SDK calls — SIM reached only as a raw JDBC schema (`jdbc/sim`), not the SIM SOAP API.
4. **Database access**:
   - `select * from SIM_DEL_DETAILS_RTS where rownum <= ?` on `jdbc/sim`.
   - `select * from SIM_DEL_DETAILS where rownum <=?` on `jdbc/sim`.
   - `UPDATE ordcust SET BILL_PHONE = (...) WHERE CUSTOMER_ORDER_NO = ? AND BILL_PHONE IS NULL` on `jdbc/oms`.
   - `SELECT CARRIER_CITY FROM xx_vxref_carrier_city WHERE lower(EXTRA_CITY)=? AND lower(CARRIER_CODE)=?` on `jdbc/sim`.
   - `UPDATE Ful_Ord_Dlv SET update_date = sysdate WHERE ID IN (?)` on `jdbc/sim`.
   - `OMSUtilSessionEJB` JPA named queries against `OMS_CUST_ORD_HEAD`, `OMS_CUST_ORD_ADDRESS`, `OMS_CUST_ORD_TENDER`, `ITEM_SUPP_COUNTRY_DIM`, `ITEM_MASTER`, `ORDCUST`.
   - `OMSUtilCommons.getExpressDelvdetails`: `SELECT COUNT(1) COUNT FROM OMS_CUST_ORD_ITEM OI, OMS_CUST_ORD_HEAD OH, ITEM_MASTER IM ...`.
5. **Request fields** (`SIMDeliveryDetail.java`): `numberOfOrders` (required), `orderStatus` (required, enum `READY_TO_SHIP|UNDELIVERED|ALL` — only first two handled), `orderSource` (required, logged only), `retrieveCustomerDetails`/`retrieveProductDetails` (required, True/False).
   **Response fields**: list of `Order` — `OrderNo`, `OrderValue`, `IsCod`, `IsExpressDelivery`, `Carrier` (Aramex/Smsa/Fetchr/Ups/Dhl), `store_id`, `shipment_id`, address/item sub-objects.
6. **Business rules**: COD flag set when tender type id == 106; Carrier `ELITE` gets leading `00` stripped from delivery mobile; missing billing email defaults to `dummy@extra.com` for Oman orders (store id starting `3`), else empty; carrier-specific city lookup via `xx_vxref_carrier_city` with fallback to OMS delivery city; backfills NULL `BILL_PHONE` before reading it back.
7. **External systems**: none via SOAP — only direct JDBC to `jdbc/sim`/`jdbc/oms`. Contains an unused dead-code helper wrapping SIM SOAP stubs, not referenced by the active flow.
8. **Transactions**: plain JDBC, default autocommit; both generate* methods swallow most exceptions and log rather than propagate.
9. `XX_DLV_ADDRESS_UPDATE`: not called in this module.

---

### POST /dispatch (sim-service / sim-dispatch)

1. **API**: REST `POST /api/dispatch`. Module `sim-dispatch`. Pure Spring REST, JSON.
2. **Entry point**: `com.extra.oms.sim.DispatchController.dispatchItem` — `:28-41`.
3. **Call chain**: `DispatchController.dispatchItem` → `DispatcherService.dispatchOrder(Request)` (`:29-44`) → `SIMDispatchDAO.getFulfilmentDetails(request)` (`:29-54`) to resolve SIM fulfillment order/line ids → `IOracleSIMClient.createFulfillmentOrderDelivery(...)` (Feign SOAP client in `oms-common`, `POST /FulfillmentOrderDeliveryBean/FulfillmentOrderDeliveryService`) → then `dispatchFulfillmentOrderDelivery(...)` using the returned `deliveryId`.
4. **Database access**: `SELECT O.ID FULFILL_ORDER_ID, I.ID FULFILL_ORDER_LINE_ID FROM FUL_ORD_LINE_ITEM I, FUL_ORD O WHERE O.ID = I.FUL_ORD_ID AND O.EXTERNAL_ID = :extNo AND CUST_ORDER_ID = :ordNo AND I.ITEM_ID = :itemId` against SIM schema (`jdbc/sim`).
5. **Request fields** (`Request.java`): `orderNo`, `fulfilNo`, `item`, `qty`. **Response** (`Response.java`): `code` (SUCCESS/ERROR), `message`.
6. **Business rules**: order must resolve to exactly one `FUL_ORD`/`FUL_ORD_LINE_ITEM` row, else `BaseException`. Dispatch is a strict two-step SIM sequence: create delivery, then dispatch it.
7. **External systems**: Oracle Retail SIM `FulfillmentOrderDeliveryService` via Feign+SOAPEncoder, target `sim.oralce.base.api.url`.
8. **Transactions**: `PlatformTransactionManager` bean declared but no `@Transactional` annotation used; single read, no writes.
9. `XX_DLV_ADDRESS_UPDATE`: not called.

---

### POST /update (DeliveryUpdate)

1. **API**: REST `POST /update`. Module `DeliveryUpdate`. Plain JSON, no WSDL.
2. **Entry point**: `org.extra.deliveryUpdate.DeliveryController.updateDeliveryDetails` — `:21-40`.
3. **Call chain**: `updateDeliveryDetails` → `validateDeliveryRequest` (`:42-72`, in-controller) → `DeliveryService.updateDeliveryService(request)` (`:43-147`) — gets raw JDBC connection from `jdbc/oms` and calls the PL/SQL package directly.
4. **Database access — THE ground-truth `XX_DLV_ADDRESS_UPDATE` call site**: `CallableStatement` — `{?= call XX_DLV_ADDRESS_UPDATE.VALIDATE_UPDATE_REQUEST(?, ?, ?, ?, ?, ?, ?, ?, ?) }` (`DeliveryService.java:70`, executed at `:120`). Params: (1) OUT status VARCHAR, (2) IN orderNo, (3) IN omsCustOrdNo (int), (4) IN classification, (5) IN firstName, (6) IN lastName, (7) IN address, (8) IN mobileNo (or NULL), (9) OUT VARCHAR (unused), (10) OUT message VARCHAR. **This is the only Java call site for `XX_DLV_ADDRESS_UPDATE` found anywhere in the repository.**
5. **Request fields** (`DeliveryRequest.java`): `orderNo` (required), `omsCustOrdNo` (int, required — `0` treated as missing; note lookup-by-orderNo helper always returns 0, i.e. dead code), `classification` (required), `firstName`/`lastName`/`address`/`mobileNo` (all optional — an "at least one of" rule is commented out/disabled).
   **Response** (`DeliveryResponse.java`): `status`, `message` from the package's two OUT params.
6. **Business rules**: controller-level required-field checks only; actual address-change eligibility (WMS pick / SIM booking / `XX_DLVRY_BOOKING` checks per ground truth) lives entirely inside the PL/SQL package.
7. **External systems**: none directly in Java.
8. **Transactions**: single `CallableStatement.execute()`, default autocommit.
9. `XX_DLV_ADDRESS_UPDATE`: called by `DeliveryService.updateDeliveryService`, `DeliveryService.java:70,120`.

---

### POST /NewTransferCreation (CarreraTransferCreation)

1. **API**: REST `POST /NewTransferCreation` (JSON). Module `CarreraTransferCreation`.
2. **Entry point**: `org.logicInfo.oms.transferCreation.Controller.TransferCreationController.getOrderItemsResponse` — `:25-66`.
3. **Call chain**: validates line items, calls `TsfCreationDAO.getResponse(...)`, implemented by `TransferCreationDAO.getResponse` (`@Repository("TsfCreationDAO")`, `@Transactional`, `:32-120`). No SIM SOAP/sim-client calls — pure RMS DB write.
4. **Database access**: `CallableStatement` — `{? = call rms14.xx_tsf_cre_carera_sql.xx_dc_tsf_cre(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }` (`:57`). Params: (1) OUT VARCHAR, (2) IN Oracle array `RMS14.XXTSF_TBL_TYPE` (item/qty pairs), (3) src_id, (4) dest_id, (5) refNo, (6) singleitem, (7) singleqty, (8) src_loc, (9) ful_loc, (10) cust_ord_no, (11) ful_ord_no. **`rms14.xx_tsf_cre_carera_sql` is not one of the 10 ground-truth packages and not present in `oms/PACKAGE/` — Requires DB verification / not in repository.**
5. **Request fields**: `src_id`, `dest_id`, `ref_no`, `src_loc`, `ful_loc`, `ful_ord_no`, `cust_ord_no` (required, no null checks besides implicit NPE risk), `customerItems: [{item, qty}]` (item non-empty, qty > 0, else `500`/"Invalid Item/Qty Value").
   **Response**: `code` (200/500), `success`, `tsf_No`, `message`.
6. **Business rules**: item/qty array lengths must match. Retry-on-lock loop: executes up to 10 times if the returned string contains "locked" (transient RMS locking); result treated as failure if it contains "transfer" or "ORA" (fragile substring check — false-positives on legitimate transfer numbers containing "transfer").
7. **External systems**: none besides direct Oracle RMS DB access (`jdbc/oms`).
8. **Transactions**: `@Transactional` (default propagation), no manual commit/rollback.
9. `XX_DLV_ADDRESS_UPDATE`: not called.

---

### GET /ping and POST /createLablePackage (label_Package_Creation)

1. **API**: REST. `GET /ping` (health check), `POST /createLablePackage` (raw XML body in, plain string out). Module `label_Package_Creation`.
2. **Entry point**: `org.logicinfo.label.creation.controller.LabelCreationController.getServiceResponse`/`getLabelCreationResponse` (`:20-26`, `:28-38`).
3. **Call chain**: `getLabelCreationResponse` → `LableCreationServImpl.getlablePackageResponse(String xml)` (`@Repository("lableService")`, `@Transactional`, `:35-71`). No sim-client/SIM SOAP call — direct PL/SQL only. Model classes exist but are unused/vestigial — controller passes the raw body straight through as `String`.
4. **Database access**: `CallableStatement` — `{call XX_CREATE_LABEL(?,?,?)}` (`:40`). Params: (1) IN CLOB (input XML), (2) OUT CLOB (label data), (3) OUT VARCHAR (status/error). **`XX_CREATE_LABEL` is not one of the 10 ground-truth packages and not present in `oms/PACKAGE/` — Requires DB verification / not in repository.**
5. **Request**: unstructured — entire HTTP body passed as raw XML string, no Java-side schema validation.
   **Response**: plain string — either the CLOB label content or an error message.
6. **Business rules**: if OUT status == "SUCCESS", return label CLOB; else fall back to result string, else error message — response selection fully delegated to the PL/SQL procedure.
7. **External systems**: connects **directly** to the SIM Oracle database using `DriverManagerDataSource` with plaintext credentials in `application.properties` (`url=jdbc:oracle:thin:@exvm-qarmsdb01.extrastores.com:1521/SIMQA.extrastores.com`, PROD/RMS alternates commented) — a direct DB connection, not a JNDI pool, unlike other modules in this cluster.
8. **Transactions**: `@Transactional`, no manual commit; connection from `jdbcTemplate.getDataSource().getConnection()`.
9. `XX_DLV_ADDRESS_UPDATE`: not called.

---

### Note on sim-client and the extra-sim-* modules

`sim-client` (1825 files) is the **Oracle Retail SIM store-client Swing desktop application** — vendor SIM client framework plus eXtra's custom extensions (IMEI/serial-number capture, ship-trailer capture screens). It is a UI/desktop codebase, not a service consumed at runtime by the REST/SOAP endpoints in this cluster. Confirmed by grep: none of the 5 endpoint modules import from `oracle.retail.sim.client`, `oracle.retail.sim.common`, or `extra.retail.sim.client`. The endpoint that does call the Oracle Retail SIM **web services** (only `sim-dispatch`) does so through a separate JAX-WS-generated stub SDK whose source lives in `OracleIntegrationServices` (outside this cluster), consumed via the Feign-based `IOracleSIMClient` in `oms-common`. `extra-sim-common`/`extra-sim-server`/`extra-sim-ejb` are further eXtra customizations on the SIM *server* side; no assigned endpoint module references them.

---

## Summary Table

| API | Method/Operation | Java Entry Point | Java Service | DAO/DB Layer | PL/SQL or SQL | Main Tables | Purpose |
|---|---|---|---|---|---|---|---|
| processNewOrder | SOAP (doc/literal) | `SIMDeliveryDetailWebServiceImpl.processNewOrder` (SIMDeliveryDetail) | `SIMDeliveryDetailBean.processOrdersDetails/generateResponse/generateResponseForUndelivered` | Raw JDBC + shared `OMSUtilSessionEJB` | Raw SQL only | `SIM_DEL_DETAILS_RTS`, `SIM_DEL_DETAILS`, `ordcust`, `Ful_Ord_Dlv`, `xx_vxref_carrier_city`, `OMS_CUST_ORD_HEAD/ADDRESS/TENDER`, `ITEM_MASTER`, `ITEM_SUPP_COUNTRY_DIM` | Fetch ready-to-ship/undelivered SIM order+delivery details for downstream carrier/label systems |
| POST /dispatch | REST POST | `DispatchController.dispatchItem` (sim-dispatch) | `DispatcherService.dispatchOrder` | `SIMDispatchDAO.getFulfilmentDetails` + `IOracleSIMClient` (Feign SOAP) | Raw SQL (SELECT) + SIM SOAP `createFulfillmentOrderDelivery`/`dispatchFulfillmentOrderDelivery` | `FUL_ORD`, `FUL_ORD_LINE_ITEM` (SIM schema) | Trigger SIM to create and dispatch a fulfillment order delivery |
| POST /update | REST POST | `DeliveryController.updateDeliveryDetails` (DeliveryUpdate) | `DeliveryService.updateDeliveryService` | Raw JDBC `CallableStatement` | `XX_DLV_ADDRESS_UPDATE.VALIDATE_UPDATE_REQUEST` | (inside package: `tsfdetail`/`tsfhead`, `ful_ord@simdb`, `XX_DLVRY_BOOKING`, per ground truth) | Validate/update a customer order's delivery address |
| POST /NewTransferCreation | REST POST | `TransferCreationController.getOrderItemsResponse` (CarreraTransferCreation) | `TransferCreationDAO.getResponse` | Raw JDBC `CallableStatement` with Oracle array type | `rms14.xx_tsf_cre_carera_sql.xx_dc_tsf_cre` (not in repo) | RMS transfer tables (via package) | Create a Carrera store/DC transfer for order fulfillment items |
| GET /ping | REST GET | `LabelCreationController.getServiceResponse` (label_Package_Creation) | none | none | none | none | Liveness check |
| POST /createLablePackage | REST POST | `LabelCreationController.getLabelCreationResponse` | `LableCreationServImpl.getlablePackageResponse` | Raw JDBC `CallableStatement` (direct `DriverManagerDataSource`) | `XX_CREATE_LABEL` (not in repo) | none directly visible | Generate shipping/package label data (CLOB) from an XML payload |

**Key file paths**: `SIMDeliveryDetail/src/oms/logicinfo/com/model/{SIMDeliveryDetailWebServiceImpl,SIMDeliveryDetailBean,SIMDeliveryDetailCommon}.java`; `sim-dispatch/src/main/java/com/extra/oms/sim/{DispatchController,service/DispatcherService,dao/SIMDispatchDAO,config/MvcConfiguration}.java`; `DeliveryUpdate/src/main/java/org/extra/deliveryUpdate/{DeliveryController,DeliveryService}.java`; `CarreraTransferCreation/src/main/java/org/logicInfo/oms/transferCreation/Controller/{TransferCreationController,TransferCreationDAO}.java`; `label_Package_Creation/src/main/java/org/logicinfo/label/creation/{controller/LabelCreationController,serviceImpl/LableCreationServImpl}.java`; `oms-common/src/main/java/com/extra/oms/service/client/IOracleSIMClient.java`.
