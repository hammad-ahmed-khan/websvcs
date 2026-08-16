# Order Management Cluster — API Reference Report

Modules analyzed: `CustomerOrderService`, `CustomerOrderBeanPOS`, `OMSCustomerOrder`, `oms-customer-order`, `oms-core`, `OMSUtil`.

## Module roles (context)

- **CustomerOrderService** and **CustomerOrderBeanPOS** are two separate WAR deployments of the *same* legacy JAX-WS RPC/literal SOAP service (`com.oracle.retail.oms.integration.services.customerorderservice.v1.CustomerOrderPortTypeImpl`, servlet path `/CustomerOrderService`) that receives orders/pickups/returns/cancellations pushed **from ORPOS (in-store POS)**. The two source trees are ~90% identical; `CustomerOrderBeanPOS` is the newer copy — it adds `IBDSClient` (Feign REST client to `PUT /bds/booking`, "Booking Delivery Service") and `PersistRequestResponse`, not present in `CustomerOrderService`.
- **OMSCustomerOrder** is a second JAX-WS SOAP service (`OMSCustomerOrderWebServiceImpl`, servlet path `/OMSCustomerOrderWebService`) that receives **e‑commerce/Hybris/NOON customer orders** and drives RMS/SIM reservation, Siebel status notification, and same-day "On-Demand Delivery" slot booking.
- **oms-customer-order** is a Spring MVC REST module for e-commerce order intake (currently a stub) and the **back-order batch processor**.
- **oms-core** is the internal operations/admin REST API (fulfilment management, order search/address-verification, e-invoicing reports, login) used by an internal SPA — JWT-secured under `/api/*`.
- **OMSUtil** is a shared library supplying: (a) `OMSUtilSessionEJB`/`OMSUtilSessionEJBBean`, a stateless EJB3 exposing ~1400 finder/persist/merge methods over a JPA `EntityManager` (persistence unit `omsUtilPersistence`, JTA datasource `jdbc/oms`, EclipseLink) mapping ~190 `OMS_*`/`ORPOS_*` entity classes; (b) `OMSUtil.java`/`OracleBaseAPIUtil.java` — JNDI/DB-connection helpers used for raw JDBC/`CallableStatement` calls that bypass JPA; (c) the Siebel order-status-update SOAP client wrapper (`OmsStatusUpdateForReturnPickupCancellation`, `OmsOrderStatusUpdateHeader`) plus generated WSDL client stubs for SIM/RMS/Siebel BPEL services. Imported by every other module in this cluster.

---

## SOAP service: CustomerOrderService (ORPOS integration)

- **Module**: CustomerOrderService / CustomerOrderBeanPOS (duplicate deployment)
- **Endpoint class**: `com.oracle.retail.oms.integration.services.customerorderservice.v1.CustomerOrderPortTypeImpl`
- **WSDL/namespace**: `CustomerOrderService/public_html/WEB-INF/wsdl/CustomerOrderService.wsdl`; `targetNamespace=http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1`, `serviceName=CustomerOrderService`, `portName=CustomerOrderPort`
- **web.xml**: servlet mapping `/CustomerOrderService`, protected by `oracle.security.jps.ee.http.JpsFilter` (anonymous enabled)

### requestNewCustomerOrderId
- **Entry**: `CustomerOrderPortTypeImpl.requestNewCustomerOrderId` (`:89-102`)
- **Call chain**: `GenerateNewOrderId.generateCustomerOrderId()` (`:61-97`)
- **DB**: raw JDBC, `select oms_cust_id_seq.nextval from dual` via `jdbc/oms` datasource looked up through `OMSUtil.getInstance().getInitialContext()`, with a WebLogic-JNDI fallback hard-coded to `http://exvm-tstadfapp01.extrastores.com:7511` — a test-environment URL baked into production code.
- **Request/response**: input `Nothing` (empty); output `CustOrderRef.orderId` (sequence value as string).

### cancelNewCustomerOrderId
- `CustomerOrderPortTypeImpl.cancelNewCustomerOrderId` (`:107-116`) — **empty method body**, no-op stub.

### createCustomerOrder
- **Entry**: `CustomerOrderPortTypeImpl.createCustomerOrder(Holder<CustOrderDesc>)` (`:121-213`), INOUT parameter.
- **Call chain**: `CustOrdCreateBean` (1672 lines):
  1. `checkCustomerOrderStatus(custOrderDesc1)` → dedup check
  2. `saveCreatCustOrd(custOrderDesc1)` (`:94`) — persists header/items/tenders/discounts/tax/customer/address via JPA entity setters through `OMSUtilSessionEJB`
  3. `checkOrderStatus(...)` → FILLED/CANCELED
  4. on FILLED: `persistRTLogTable`, `reconcilationCreateOrder`
  5. on error/CANCELED: `callRollBackMethod` + `reduceBOSrcQty` (rolls back RMS/SIM fulfilment & back-order source-qty reservations)
- **DB layer**: all via `OMSUtilSessionEJB`/JPA (EclipseLink, `jdbc/oms`) — no direct SQL/CallableStatement in this bean.
- **Transaction**: no explicit `@Transactional`/`commit()`; relies on container-managed EJB transactions. Rollback is application-level (`callRollBackMethod`), not a DB transaction rollback.

### queryCustomerOrder
- **Entry**: `CustomerOrderPortTypeImpl.queryCustomerOrder(CustOrderCriVo)` (`:219-254`)
- **Call chain**: `QueryCustOrdBean.queryCustomerOrder` (`:138-256`) branches on `searchType`: `ORDERID` → `ResponseProcessing.createResponse`; `CUSTOMER` → `createResponseByCustomerId/FirstName/LastName/PhoneNo/...` (`:259-352`); `CHARGECARD` → `createResponseByCardToken`.
- **DB layer**: entirely via JPA finder methods on `OMSUtilSessionEJB`.
- **Response**: `CustOrderColDesc` (collection of order descriptors with items, fulfillment, tax/discount/payment lines).

### pickupCustomerOrderItems
- **Entry**: `CustomerOrderPortTypeImpl.pickupCustomerOrderItems(CustOrderPicVo)` (`:260-305`)
- **Call chain**: `PickCustOrdItemBean` (5798 lines) — handles **both pickup and in-store cancellation**:
  1. `getCustomerOrderandTransactionNo` (`:167`)
  2. `saveCustOrderPicVo` (`:686`) — audit persist
  3. `findPickUpOrCancel` (`:360`) — branches per line item into pickup (`updateOmsCustOrdItem :1227`, `updateOmsCoFulfillDetail :1313`, `persistOmsCustOrdLogPickup :1427`, `notifySiebelForpickUp :1561`) or cancellation (`processCancellation :2136`, RMA/dummy-cancel handling `filterForDummyCancellation :4078`, refund-tender persistence `persistOmsOrposRefundTenderMethod :4409`)
  4. `reconcilePickCancel` (best-effort, exceptions swallowed) — writes reconciliation-batch audit rows
- **External systems**: `notifySiebelForpickUp`/`notifySiebel` (`:1561`, `:3556`) call the Siebel order-status-update SOAP service.
- **Response**: `PickupCustomerOrderItemDetailsRef`.

### returnCustomerOrderItems
- **Entry**: `CustomerOrderPortTypeImpl.returnCustomerOrderItems(CustOrderRtnColVo)` (`:311-335`)
- **Call chain**: `ReturnCustOrdBean.createResponse` (`:841`) → `persistIntoOmsOrposCustOrdHead` (`:749`), `saveCustOrderRtnColVo`/`saveCustOrdItmRtColVo` (`:174,:205`), `update_OmsOrposCustOrderHead/Item/DiscntLine/TaxLine` (`:276,410,594,665`) — all JPA. On success calls `reconcilationReturnOrder` (`:1002`).
- **Response**: `CustOrderColRef`.

### updateReceipt, ping
- Both **stubs, return `null`** unconditionally (`:341-349`, `:354-356`).

### Cross-cutting: RMS/SIM ship-classification lookup
- `FindNextfulfillLoc.findShipmentClassification(item, store, shippingClassification)` (`CustomerOrderService/.../FindNextfulfillLoc.java:130-159`)
- **PL/SQL**: `{?=call OMS_SHIP_CLASSIFICATION(?,?,?)}` (function; IN: item, channel_id, shipping_classification; RETURN: VARCHAR ship classification) — raw `CallableStatement` via `OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING)`, **not** through JPA. Same function called (4-param variant) from `OMSUtil/src/com/logicinfo/oms/beans/OMSUtilCommons.java:454`.

### Cross-cutting: Carrera (warehouse-to-warehouse) transfer rollback
- `InterfacePersistence.java:2876` (CustomerOrderBeanPOS) — `{ ? = call XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN(?, ?, ?, ?) }` (2 OUT status/message VARCHAR + IN tsf_no NUMBER, item VARCHAR); on success follows with raw `UPDATE ORDCUST_DETAIL SET QTY_CANCELLED_SUOM = ? WHERE ordcust_no = (select ordcust_no from ordcust where tsf_no = ?)` (`:2892`).

---

## SOAP service: OMSCustomerOrderWebService (e‑commerce/Hybris/NOON order intake)

- **Module**: OMSCustomerOrder
- **Endpoint class**: `com.logicinfo.oms.model.OMSCustomerOrderWebServiceImpl`
- **WSDL/namespace**: `targetNamespace=http://com.logicinfo.oms/model/`
- **web.xml**: servlet path `/OMSCustomerOrderWebService`, `JpsFilter` (anonymous enabled), plus `com.logicinfo.oms.listener.OracleBaseAPIConfigListener`.
- **Persistence unit**: separate copy of the `omsUtilPersistence`-style unit, `jdbc/oms`, EclipseLink.

### processNewOrder
- **Entry**: `OMSCustomerOrderWebServiceImpl.processNewOrder(CustomerOrder)` (`:95-217`)
- **Call chain** (`OMSCustomerOrderBean`):
  1. `validateInput(input)` (`:53-240`) — mandatory-field validation
  2. `persistData(input)` (`:263`) → `OMSPersistence.omsPersist(input)` (persists header/items/address/tenders via JPA)
  3. `findSourceLocation(input)` (`:242`) → `SourceLocationIdentifier.OrderPickUpModule(...)` — determines fulfillment source (store/WH/supplier), calls RMS `FulfillOrderService`/SIM `StoreFulfillmentOrderService`/`StoreInventoryService`/RMS `InventoryBackOrderService` SOAP ports; returns `ItemUnavailabilityStatus` (status `FAIL` short-circuits with `UNAVL_INV` response)
  4. `checkCreateOrReserveOrder(input)` (`:268`) — calls RMS fulfillment-order creation or SIM reservation
  5. `persistRTLog(input)` (`:316`) — inserts `OmsRtlogPublishLog` rows (RMS RTLog republish queue) when `payInStoreInd='N'` and `orderCreateReserveInd='C'`
  6. `splitTender(input)` (`:306`) → `TenderSplit.tenderSplit(custOrdHeadSeqNo)` for VOUCH tender type
  7. `notifySiebel(input)` (`:338`)
  8. If `deliveryModeType=="ODDSMALL"` and `deliveryType=="S"`: `bookingODDSlot(input)` (`:356`) → `oddPackageCall` — **XXHDB_CORE_PKG** call
  9. On any exception: `oMSUtilCommons.rollback(input.getCustomerOrderNo())` then `generateResponse(...)`
- **Response**: `CustomerOrderResponse`. On failure for orders whose `customerOrderNo` contains `"WEB"` or `"NSA"` (NOON), and only if system parameter `FAILED_ORDERS_REQ_RESP` = `'Y'`, `updateRequestResponseDetails` (`:327-409`) does raw JDBC `INSERT INTO oms_order_create_response_head(...)` / `oms_order_create_response_item(...)`.
- **Business rules** (`validateInput`, `:53-240`): `orderType='B2B'` requires `customerSubOrderNo`; `deliveryType` in {S,SS,SC} requires `customerOrderAddress`; `deliveryType` in {C,CC} requires `pickLoc`; every tender's `tenderType` must exist in code table `TENT`; tender type CHECK/GCARD/VOUCH requires `tenderRefId`; CCARD/DCARD requires `ccNo`, `ccAuthNo`, `ccAuthSrc`, `ccCardholderVerf`, `ccEntryMode`, `ccExpDate`, `ccSpecCond`, `ccTermId`.
- **Transaction**: no explicit JTA transaction management; failure path calls `OMSUtilCommons.rollback(orderNo)` (compensating rollback, not a DB transaction rollback).

### processBackOrder
- **Entry**: `OMSCustomerOrderWebServiceImpl.processBackOrder(BackOrderRequest)` (`:221-269`)
- **Call chain**: `BackOrder` bean — `getBatchProcessingIndicator()`, `insertBackOrderBachProgress()`, `findBackOrder()` (calls RMS inventory-adjustment PL/SQL), `updateBackOrderBatchStatus(...)`.
- **DB**: `{call OMS_INVADJ_STATUS_UNAVIALINV(?,?,?,?,?,?,?,?,?)}` at `BackOrder.java:412` and `:506` (item, inv_status, loc_type, loc, qty, reason_code, user_id, status OUT, message OUT).
- **Response**: `BackOrderResponse.message` (`"COMPLETED"`/`"FAILED"`/"Another back order batch is in Progress..").

### Siebel integration (used by OMSCustomerOrder create flow and CustomerOrderService pickup/return flows)
- `OMSCustomerOrderBean.notifySiebel` (`:338-354`), gated by system parameter `CALL_SIEBEL`, and `PickCustOrdItemBean.notifySiebelForpickUp`/`notifySiebel` call `OmsStatusUpdateForReturnPickupCancellation.callOmsStatusUpdateWebserviceForReturnAndCancellationAndPickup` (`OMSUtil/.../OmsStatusUpdateForReturnPickupCancellation.java:34-104`).
- Invokes generated JAX-WS client `retail.siebel.com.soaintegration.Status`/`IStatus.updateOrderStatus(...)` (stub defined in `OracleIntegrationServices`) — same Siebel `UpdateOrderStatus` SOAP operation that the PL/SQL `OMS_SOS_WS_INVOKER`/`OMS_RECEIPT_WS_INVOKER`/etc. packages call via `UTL_HTTP`. **So this operation is called from both the PL/SQL layer and directly from this Java layer.**
- On failure, `insertRecordIntoRepublishData` (`:107-128`) persists a row into `OMS_REPUBLISH_DATA` with `webServiceId` resolved from `OMS_WEBSERVICE_URI_DETAIL` (key `"SOA_CO_STATUS_UPDATE"`) — **the same `OMS_REPUBLISH_DATA`/`OMS_WEBSERVICE_URI_DETAIL` table pair used by the ground-truth `OMS_MSG_WS_PUBLISHER` PL/SQL republish batch job.**

### XXHDB_CORE_PKG (On-Demand-Delivery slot booking) — only hit for a ground-truth-named package in this cluster
- **Caller**: `OMSCustomerOrderBean.oddPackageCall` (`:381-430`), invoked from `bookingODDSlot` (`:356`), invoked from `processNewOrder` only when `deliveryModeType.equals("ODDSMALL")` and `deliveryType.equals("S")`.
- **Call**: `{call XXHDB_CORE_PKG.reserve_booking_small(?,?,?,?,?,?,?,?)}` (`:397`), params: (1) `omsCustOrdNo` int IN, (2) `customerOrderNo` String IN, (3) current timestamp IN, (4) delivery `window` (first 2 chars of `consumerDeliverySlot`) IN, (5) `sourceLoc` int IN, (6) `customerPhoneNo` IN, (7) status OUT VARCHAR, (8) message OUT VARCHAR. Failure is only logged, never surfaced to the caller/response.

---

## REST module: oms-customer-order

- **web.xml**: Spring `DispatcherServlet` mapped to `/*`.
- **Config**: JNDI datasource `db.jndi.oms`; Feign SOAP clients `oracleRMSClient`/`oracleSIMClient`; Feign JSON client `carreraClient`; `backOrderProcessor` thread pool (5–20 threads, queue 20000).

### POST /createNewOrder
- **Entry**: `EcomOrderController.processNewOrder` (`:18-32`)
- **Call chain**: `EComOrderServiceImpl.validateInput(request)` (`:20-132`) — partial reimplementation of `OMSCustomerOrderBean.validateInput` rules. **Note**: never persists anything and never calls a DAO/RMS/SIM/DB layer — the controller always returns a hardcoded success (`messageCode=500`, `messageStatus="success"`) regardless of validation outcome, and the tender-type-code lookup list is `Collections.emptyList()` so that branch can never match. **Incomplete/non-functional relative to its SOAP counterpart.**

### GET /back-order
- **Entry**: `BackOrderController.getProcessStatus` (`:33-43`) — returns thread-pool stats only, no DB access.

### POST /back-order
- **Entry**: `BackOrderController.processBackOrders` (`:45-57`)
- **Call chain**: `BackOrderService.processBackOrders()` (`:38-72`):
  1. `backOrderDAO.aquireLock()` (`:81-94`) — `LOCK TABLE OMS_BACK_ORDER_BATCH_CHECK IN EXCLUSIVE MODE WAIT 5`, checks `IN_PROGRESS`, else `INSERT ... BACK_ORDER_BATCH_SEQ.NEXTVAL, SYSTIMESTAMP, 'IN_PROGRESS'`.
  2. `getStockAvailablity()` — `UNION` over `XX_OMS_INVAVAIL_V` (warehouse) and `XX_OMS_INV@SIMDB` (store, DB link `SIMDB`).
  3. `getMaxFulfilOrderNo()`, `getSystemParams()` (reads `RESV_INV_STATUS`, `REASON_CODE`, `OMS_PARTIAL_DLV_IND`, `INV_RESV_CODE`, `INV_UNRESV_CODE` from `OMS_SYSTEM_PARAMETERS`).
  4. `getBOOrders(stockMap)` — join across `OMS_BACK_ORDER_DTL`, `OMS_CUST_ORD_HEAD`, `OMS_CUST_ORD_ITEM`, `OMS_CUST_ORD_ADDRESS`, `WH`, then in-memory allocation.
  5. per-order parallel processing via `BackOrderProcess` (`Callable`, 748 lines — builds RMS/SIM reservation/adjustment requests, WH↔WH combos call `carreraTransfer`), executed via `backOrderExecutor.invokeAll`.
  6. RMS SOAP `InventoryBackOrderService.createInvBackOrdColDesc` via `oracleRMSClient`.
  7. `finally`: `releaseLock` → `UPDATE OMS_BACK_ORDER_BATCH_CHECK SET STATUS=..., BATCH_END_TIME=SYSTIMESTAMP`.
- **Other DB writes** (`updateBackOrderDetails`, `:288-427`): batched `UPDATE OMS_BACK_ORDER_DTL`, `UPDATE`/`INSERT OMS_CUST_ORD_RESERVE`, `INSERT OMS_CO_FULFILL_DETAIL`, `UPDATE OMS_CUST_ORD_ITEM`, `INSERT OMS_UNAPPROVED_TRANSFERS`, `UPDATE OMS_CO_FULFILL_DETAIL...FROM ORDCUST`.
- **PL/SQL**: `callWHAdjustment`/`rbWHAdjustment` → `{ CALL OMS_INVADJ_STATUS_UNAVIALINV(...) }` (`:447,539`); `rbCarreraTransfers` → `{? = call XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN(?, ?, ?, ?)}` (`:498`).

---

## REST module: oms-core (internal operations API)

- **web.xml**: Spring `DispatcherServlet` on `/anonymous/*` and `/api/*`; `CORSFilter` on `/anonymous/*`; `AuthFilter` (JWT) on `/api/*`.
- **Auth**: `AuthFilter` requires header `X-AUTH-TOKEN` on every `/api/*` call except `/oms/api/login` and `/oms/api/order/validate`; validated by `JWTService.verifyToken` (HS384, secret from property `jwt.secret` — plaintext in checked-in properties file, not reproduced here).
- **Config**: datasource `db.jndi.oms`; a **second, independent** JDBC datasource `rmsJndiTemplate` built directly from `DriverManagerDataSource` using `rms.datasource.jdbc-url/username/password` (plaintext Oracle credentials in `application.properties`) — used only by `EInvoicingDAO.getInvoiceXML`/`getError`. Feign clients `oracleRMSClient`/`oracleSIMClient`, `carreraTransfer`, `IMuleAddressClient` (Mule/CloudHub address-validation API — **TLS hostname verification and certificate validation disabled** via a trust-all `X509TrustManager`/`HostnameVerifier` at `MvcConfiguration.java:174-232`, a security-relevant finding observed directly in code).

### GET /fulfilment
- `FulfilmentController.searchFulfilments` → `FulfilmentService.searchFulfilments` → `FulfilmentDAO.searchFulfilments` (parameterized dynamic search query).

### PUT /fulfilment (cancel fulfilment)
- `FulfilmentController.cancelFulfilment` → `FulfilmentService.cancelFulfilmentReq` (`@Transactional(rollbackFor=BaseException.class)`) → `FulfilmentDAO.insertFulfilmentRequest` (audit insert) → `cancelFulfilment` → `FulfilmentDAO.validateAndLockFulfilment` (row lock), then:
  - WH→WH cross-warehouse: `cancelCareraFulfilment` → `{? = call XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN(?, ?, ?, ?) }`, throws `BaseException` unless returns `"Success"`.
  - otherwise: `cancelRMSFulfilment` → RMS SOAP `FulfillOrderService.cancelFulfilOrdColRef`; if source=store=fulfil-store also `cancelSIMFulfilment` → SIM SOAP `StoreFulfillmentOrderService.cancelFulfillmentOrderDetail`.
  - `updateOMSFulfiment` persists cancelled quantity; `finally` always calls `updateFulfilmentRequest` to close audit row.
- **Business rules**: `requestQty <= (cancelledQty+deliveryQty)` → `INVALID_QUANTITY_ON_CANCEL`; new cumulative cancel exceeding request qty → `CANCEL_QUANTITY_MAX_REACHED`.

### POST /fulfilment (create, optionally cancel-and-create)
- `FulfilmentService.createWithCancelFulfilment` (`@Transactional`) — stock check (throws `STOCK_NOT_AVAILABLE`), optional prior cancel, then `createFulfilment`: WH→WH → `createCarreraFulfilment` (Feign); else `createRMSFulfilment` (RMS SOAP `createFulfilOrdColDesc`) then, if store=store, `createSIMFulfilment` (SIM SOAP `createFulfillmentOrderDetail`) with automatic **RMS rollback** (`rollbackRMSFulfilment`) if the SIM call fails; finally `createOmsFulfilment` persists the OMS-side record.

### GET /fulfilment/stock
- `getStockAvailablity` → `FulfilmentDAO.getStockAvailablity`.

### POST/PUT/GET /bulkfulfilment, GET /bulkfulfilment/template
- `POST` parses uploaded Excel (Apache POI), validates rows (`validateFulfilments`); `PUT` processes rows asynchronously on a raw `new Thread()` per request, queuing per-user results in an in-memory `bulkProcessQueue` map — **not distributed/clustered-safe**; `GET` polls/drains queue; `GET /template` streams generated `.xlsx`.

### GET /order, /order/validate/{type}/{orderNo}, /order/detail/{omsCustomerNo}, /order/address/{omsCustomerNo}, POST /order/address, GET /order/cancel/{customerNo}, /order/return/{customerNo}, /order/booking/{customerNo}
- `OrderController` → `OrderService` → `OrderDAO`. Read endpoints are hand-built SQL SELECTs against `OMS_CUST_ORD_HEAD`, `OMS_CUST_ORD_ITEM`, `OMS_CO_FULFILL_DETAIL`, `OMS_CUST_ORD_TENDER`, `OMS_CUST_ORD_ADDRESS`, `OMS_CO_CANCEL_HEAD/ITEM`, `OMS_CANCEL_REQ_AUDIT`, `USER_ATTRIB`, `OMS_RMA_REQ/REQ_ITEM/RCV_DTL`, `OMS_ORPOS_MASTER_AUDIT`, `XX_DLVRY_BOOKING`/`XX_DLVRY_BOOKING_V`, `STORE`, `WH`, `SUPS`, `XX_CARRIER_TRACKING_V`, `XX_POS_ADDR_VER_PU`.
- `POST /order/address` → `OrderService.updateAddress` calls **`IMuleAddressClient`** (`getAddressByShortAddress`/`getAddressByGeocode`) to geocode/validate the address, then compares returned city against `OrderDAO.getCustomerCity`; on match, `OrderDAO.updateCustomerAddress` (`@Transactional`) does raw `UPDATE OMS_CUST_ORD_HEAD SET ADDR_VERIFIED_IND=...`, `UPDATE ORDCUST SET DELIVER_ADD1/2/3=...`, `UPDATE OMS_CUST_ORD_ADDRESS SET DELIVER_ADD_1/2/3, LATITUDE, LONGITUDE, DELV_SHORT_ADDR=...`, `UPDATE XX_DLVRY_BOOKING SET LONGITUDE, LATITUDE, CUSTOMER_ADDRESS=...`; for `type != "order"` instead `INSERT`s into `XX_POS_ADDR_VER_PU`. **This is functionally the same "verify/update customer delivery address" capability described for the `XX_DLV_ADDRESS_UPDATE` PL/SQL package in the ground truth, but this Java code path performs the updates directly via raw JDBC and never calls that package.**
- **Business rule**: address update only committed if `type=="invoice"` or geocoded city matches customer's existing delivery city on the order.

### GET /user, POST /login, GET /util/*
- `UserController.getUserInfo` echoes request attribute — no DB call.
- `LoginController.validateUser` → `UserService.validateUser` → `UserDAO.getUserByUserName` (`SELECT U.USER_ID, U.USER_NAME, U.PASSWORD, R.ROLES FROM OMS_USER_INFO U, OMS_ROLE_INFO R WHERE U.ROLE_ID=R.ROLE_ID AND USER_NAME=:userName`); password check via `PooledStringDigester.matches` (salted/hashed digest comparison, not plaintext); success issues JWT via `JWTService.generateAuthToken`, returned in header `X-AUTH-TOKEN`.
- `UtilController` (`GET /util/store|wh|supplier|virtual`) → `UtilService`/`UtilDAO` — reference-data lookups.

### GET /einvoice, /einvoice/xml/{invoiceNumber}, /einvoice/error/{invoiceNumber}; GET /ebs; GET /b2bpos
- `ReportController`/`EBSController`/`B2BPOSController` → `EInvoicingService` → `EInvoicingDAO` — read-only SELECTs against `XX_EINV_XML_GAZT_REP` (KSA ZATCA e-invoice XML/report table), `XX_B2B_EBS_EINV_REP`, `XX_POS_B2B_EINV`. `getInvoiceXML`/`getError` use the separate `rmsJndiTemplate` datasource.

---

## PL/SQL package cross-reference (ground-truth packages) — search results

| Package | Call sites found in this cluster |
|---|---|
| `OMSSUB_ASNOUT` | none |
| `OMSSUB_RECEIVING` | none |
| `OMS_ASNOUT_WS_INVOKER` | none |
| `OMS_RECEIPT_WS_INVOKER` | none |
| `OMS_SOS_WS_INVOKER` | none |
| `OMS_PENDRETURN_WS_INVOKER` | none |
| `OMS_MSG_WS_PUBLISHER` | none directly called, but this cluster **writes to `OMS_REPUBLISH_DATA`/reads `OMS_WEBSERVICE_URI_DETAIL`** (`OmsStatusUpdateForReturnPickupCancellation.java:107-128`) — the same table pair that job republishes from — an indirect, table-level integration point |
| `XX_DLV_ADDRESS_UPDATE` | none — `OrderDAO.updateCustomerAddress` (`oms-core`) implements equivalent address-update logic directly in raw SQL instead of calling this package |
| `XX_REFUND_REQUEST_PKG` | none |
| `XXHDB_CORE_PKG` | **one call**: `OMSCustomerOrderBean.oddPackageCall` → `XXHDB_CORE_PKG.reserve_booking_small` — `OMSCustomerOrder/src/com/logicinfo/oms/model/OMSCustomerOrderBean.java:397` |

Other PL/SQL packages/functions called from Java in this cluster (not in ground-truth list):

| Package/function | Call sites |
|---|---|
| `OMS_SHIP_CLASSIFICATION` (function) | `CustomerOrderService/.../FindNextfulfillLoc.java:142`, `CustomerOrderBeanPOS/.../FindNextfulfillLoc.java:133`, `OMSCustomerOrder/.../FindNextfulfillLoc.java:148`, `OMSUtil/.../OMSUtilCommons.java:454` |
| `OMS_INVADJ_STATUS_UNAVIALINV` | `OMSCustomerOrder/.../RmsPackage.java:157,253`, `OMSCustomerOrder/.../BackOrder.java:412,506`, `oms-customer-order/.../BackOrderDAO.java:447,539` |
| `XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN` (function) | `CustomerOrderBeanPOS/.../InterfacePersistence.java:2876`, `OMSCustomerOrder/.../NonSADADPayment.java:1457`, `oms-customer-order/.../BackOrderDAO.java:498`, `oms-core/.../FulfilmentDAO.java:381` |

---

## Summary table

| API | Method/Operation | Java Entry Point | Java Service | DAO/DB Layer | PL/SQL or SQL | Main Tables | Purpose |
|---|---|---|---|---|---|---|---|
| CustomerOrderService | SOAP `requestNewCustomerOrderId` | `CustomerOrderPortTypeImpl.java:89` | `GenerateNewOrderId` | raw JDBC | `select oms_cust_id_seq.nextval from dual` | (sequence) | Issue new OMS order ID for ORPOS |
| CustomerOrderService | SOAP `cancelNewCustomerOrderId` | `CustomerOrderPortTypeImpl.java:107` | — | — | — | — | Stub, no-op |
| CustomerOrderService | SOAP `createCustomerOrder` | `CustomerOrderPortTypeImpl.java:121` | `CustOrdCreateBean` | JPA via `OMSUtilSessionEJB` | none (JPA) | `OMS_CUST_ORD_HEAD/ITEM/TENDER`, `OMS_ORPOS_*` | Persist ORPOS-created customer order |
| CustomerOrderService | SOAP `queryCustomerOrder` | `CustomerOrderPortTypeImpl.java:219` | `QueryCustOrdBean`, `ResponseProcessing` | JPA via `OMSUtilSessionEJB` | none (JPA) | `OMS_ORPOS_CUST_ORDER_HEAD/ITM`, etc. | Query order by ID/customer/card token |
| CustomerOrderService | SOAP `pickupCustomerOrderItems` | `CustomerOrderPortTypeImpl.java:260` | `PickCustOrdItemBean` | JPA via `OMSUtilSessionEJB` | none (JPA) | `OMS_ORPOS_CUST_ORDER_PICKUP/ITM_PICKUP`, `OMS_CO_FULFILL_DETAIL` | In-store pickup and cancellation |
| CustomerOrderService | SOAP `returnCustomerOrderItems` | `CustomerOrderPortTypeImpl.java:311` | `ReturnCustOrdBean` | JPA via `OMSUtilSessionEJB` | none (JPA) | `OMS_ORPOS_CUST_ORDER_RTN/ITM_RT` | In-store return recording |
| CustomerOrderService | SOAP `updateReceipt`, `ping` | `CustomerOrderPortTypeImpl.java:341,354` | — | — | — | — | Stubs, unimplemented |
| OMSCustomerOrder | SOAP `processNewOrder` | `OMSCustomerOrderWebServiceImpl.java:95` | `OMSCustomerOrderBean`, `OMSPersistence`, `SourceLocationIdentifier` | JPA via `OMSUtilSessionEJB`; raw JDBC insert for failed-order audit | `XXHDB_CORE_PKG.reserve_booking_small` (ODD only); raw `INSERT INTO oms_order_create_response_head/item` | `OMS_CUST_ORD_HEAD/ITEM/ADDRESS`, `OMS_RTLOG_PUBLISH_LOG`, `oms_order_create_response_head/item` | E-commerce/Hybris/NOON order intake, RMS/SIM reservation, Siebel notify, ODD slot booking |
| OMSCustomerOrder | SOAP `processBackOrder` | `OMSCustomerOrderWebServiceImpl.java:221` | `BackOrder` | raw JDBC/JPA mix | `OMS_INVADJ_STATUS_UNAVIALINV` | `OMS_BACK_ORDER_DTL`, batch progress table | Trigger back-order batch matching |
| oms-customer-order | `POST /createNewOrder` | `EcomOrderController.java:18` | `EComOrderServiceImpl` | none | none | none | Validation-only stub; does not persist |
| oms-customer-order | `GET /back-order` | `BackOrderController.java:33` | — | in-memory executor stats | none | none | Batch status |
| oms-customer-order | `POST /back-order` | `BackOrderController.java:45` | `BackOrderService`, `BackOrderProcess`, `InventoryBackOrderService` | `BackOrderDAO` (`NamedParameterJdbcTemplate`) | `OMS_INVADJ_STATUS_UNAVIALINV`, `XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN` | `OMS_BACK_ORDER_DTL`, `OMS_BACK_ORDER_BATCH_CHECK`, `OMS_CUST_ORD_RESERVE`, `OMS_CO_FULFILL_DETAIL`, `XX_OMS_INVAVAIL_V`, `XX_OMS_INV@SIMDB` | Back-order batch: stock match, RMS/SIM/Carrera reservation |
| oms-core | `GET/PUT/POST /fulfilment` | `FulfilmentController.java` | `FulfilmentService` | `FulfilmentDAO` | `XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN` | `OMS_CO_FULFILL_DETAIL`, related fulfilment tables | Manual fulfilment search/create/cancel |
| oms-core | `POST/PUT/GET /bulkfulfilment` | `BulkFulfilmentController.java` | `FulfilmentService` | `FulfilmentDAO` | (same as above, per-row) | same | Excel-driven bulk fulfilment processing |
| oms-core | `GET /order*`, `POST /order/address` | `OrderController.java` | `OrderService` | `OrderDAO` | none (raw SQL) | `OMS_CUST_ORD_HEAD/ITEM/ADDRESS`, `OMS_CO_CANCEL_*`, `OMS_RMA_REQ*`, `XX_DLVRY_BOOKING*` | Order search/detail/address-verification/cancel/return/booking views |
| oms-core | `GET /user`, `POST /login` | `UserController.java`, `LoginController.java` | `UserService`, `JWTService` | `UserDAO` | none | `OMS_USER_INFO`, `OMS_ROLE_INFO` | Internal SPA auth (JWT) |
| oms-core | `GET /util/*` | `UtilController.java` | `UtilService` | `UtilDAO` | none | `STORE`, `WH`, `SUPS`, virtual-store tables | Reference-data lookups |
| oms-core | `GET /einvoice*`, `/ebs`, `/b2bpos` | `ReportController.java`, `EBSController.java`, `B2BPOSController.java` | `EInvoicingService` | `EInvoicingDAO` | none | `XX_EINV_XML_GAZT_REP`, `XX_B2B_EBS_EINV_REP`, `XX_POS_B2B_EINV` | E-invoicing (ZATCA) reporting |

## Key files referenced
- `CustomerOrderService/src/com/oracle/retail/oms/integration/services/customerorderservice/v1/CustomerOrderPortTypeImpl.java`
- `CustomerOrderService/src/com/logicinfo/oms/beans/{CustOrdCreateBean,QueryCustOrdBean,PickCustOrdItemBean,ReturnCustOrdBean,FindNextfulfillLoc}.java`
- `CustomerOrderService/src/com/oracle/retail/oms/integration/services/customerorderservice/v1/GenerateNewOrderId.java`
- `CustomerOrderBeanPOS/src/com/logicinfo/oms/beans/InterfacePersistence.java`
- `OMSCustomerOrder/src/com/logicinfo/oms/model/{OMSCustomerOrderWebServiceImpl,OMSCustomerOrderBean,BackOrder,RmsPackage,NonSADADPayment}.java`
- `OMSUtil/src/com/logicinfo/oms/ejb/{OMSUtilSessionEJB,OMSUtilSessionEJBBean,OmsRepublishData,OmsWebserviceUriDetail}.java`
- `OMSUtil/src/com/logicinfo/oms/util/{OMSUtil,OmsStatusUpdateForReturnPickupCancellation}.java`
- `oms-customer-order/src/main/java/com/extra/oms/custOrder/{controller/*,service/*,dao/BackOrderDAO.java,config/MvcConfiguration.java}`
- `oms-core/src/main/java/com/extra/oms/core/{controller/*,service/*,dao/*,config/MvcConfiguration.java}`
- `oms-core/src/main/java/com/extra/oms/common/AuthFilter.java`
- `oms-core/src/main/java/com/extra/oms/einvoice/{controller/*,service/*,dao/*}`
