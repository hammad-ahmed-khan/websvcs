
# Payment / Finance / Pricing / E-Invoicing Cluster — API Reference

Modules covered: `PaymentConfirmation`, `VASContract` (incl. `VASContract/SIMDeliveryDetail`), `apple-pricing`, `boot-apple-pricing`, `oms-discount`, `oms-finance`, `e-invoice-util`, `einvoicing`, `oms-einvoicingxml`.

All file paths are relative to `/home/user/websvcs/oms/CODE/`.

**Note on secrets**: several `application.properties` files in this cluster contain plaintext production credentials (DB passwords, ZATCA API binary token/private key/secret, BIP report password, email-service password, SFTP password). Their presence is noted below; the literal secret values are intentionally not reproduced in this report.

**Note on `XX_REFUND_REQUEST_PKG` / `OMSSUB_ASNOUT` / `OMSSUB_RECEIVING` / `WS_INVOKER`**: a repo-wide grep (`grep -rli` for these package names and for "refund") across all nine modules in this cluster returned no matches. None of these modules call the refund PL/SQL package or the ASN/receiving/WS-invoker packages — refund creation is out of scope for this cluster (it is driven from the cancellation module documented elsewhere).

---

### PaymentConfirmation — `processPaymentConf`

- **Module**: `PaymentConfirmation` (legacy JAX-WS WAR, package `com.logicinfo.oms.model`)

- **Type**: SOAP 1.2, JAX-WS (`javax.jws.WebService`), doc/literal BARE, schema-validated (`@SchemaValidation`)

- **Endpoint**: `/PaymentConfirmationWebService` — `public_html/WEB-INF/web.xml:6-13`

- **WSDL**: `public_html/WEB-INF/wsdl/PaymentConfirmationWebservice.wsdl`, namespace `http://com.logicinfo.oms/model/`

- **Entry point**: `PaymentConfirmationWebServiceImpl.processPaymentConf(CoPaymentConf)` — `src/com/logicinfo/oms/model/PaymentConfirmationWebServiceImpl.java:61`

**Call chain**

- `PaymentConfirmationWebServiceImpl.processPaymentConf` (`PaymentConfirmationWebServiceImpl.java:61`)

- `OMSCustomerOrderBean.getOmsCustOrdNo` — resolves `oms_cust_ord_no` from `customer_order_no`/`customer_sub_order_no`/`application_Id` via EJB session finder `OMSUtilSessionEJB.getOmsCustOrdHeadFindByExternalCustOrdNo` (`OMSCustomerOrderBean.java:53-70`)

- `PersistPaymentRequest.checkPaymentProgress` / `persistInPaymentAuditTable` — raw JDBC against `Oms_PaymentConf_Audit` (dedup/lock table), datasource `jdbc/oms` (`PersistPaymentRequest.java:23-52`, `75-98`)

- `OMSCustomerOrderBean.validate` — re-validates the header lookup (`OMSCustomerOrderBean.java:213-233`)

- `OMSCustomerOrderBean.persistOmsCustOrdReserve` → `OMSPersistence.persistOmsCustOrdReserve` — for every `OmsCustOrdReserve` row of the order: sets `conf_ts=now`, `payment_status='S'`, `resv_status='CNF'`, then `session.mergeOmsCustOrdReserve` (JPA merge) (`OMSPersistence.java:27-69`)

- `OMSCustomerOrderBean.persistOmsPaymentSync` — inserts one `OmsPaymentSync` JPA entity per reserved item/line (`OMSCustomerOrderBean.java:297-324`)

- `OMSCustomerOrderBean.unreserveQuantities`:
`RMSPackage.rmsPackageCall` — for each reserve row not at a store/supplier location, calls **`{call OMS_INVADJ_STATUS_UNAVIALINV(?,?,?,?,?,?,?,?,?)}`** (item, `i_inv_status` from `OMS_SYSTEM_PARAMETERS('RESV_INV_STATUS','OMS_SYSTEM_OPTION')`, loc-type `S`/`W`, loc, negative qty, reason-code from `OMS_SYSTEM_PARAMETERS('REASON_CODE','OMS_SYSTEM_OPTION')`, `cust_order_no+"_PY"`, OUT status, OUT err_msg) on datasource `OMSConstants.DS_OMS_STRING`, retried up to 10x, raises SOAP fault `TABLE_LOCKED` on repeated lock (`RMSPackage.java:154-272`)

- `InterfacePersistence.adjustInventoryByItemLocation` (`InterfacePersistence.java:695`)

- `OMSCustomerOrderBean.processPaymentConfirmation` → `ExtSystemUpdate.processPaymentConfirmation` — calls RMS `FulfillOrderService`/SIM `StoreFulfillmentOrderService` SOAP ports to confirm fulfillment (WSDLs under `classes/com/oracle/retail/...`); on failure calls `rollbackForTimeout` (`OMSCustomerOrderBean.java:236-281`)

- `OMSCustomerOrderBean.callSiebelOrderFeed` → `InterfacePersistence.callSeibelWebservice` — builds `CustomerOrderHeaderLevel` (address, items, fulfillment, **tender details**) from `OmsCustOrdHead`/`OmsCustOrdAddress`/`OmsCustOrdItem`/`OmsCoFulfillDetail`/`OmsCustOrdTender`, then calls `InboundordercreationbpelprocessClientEp.getSiebelOrderFeedWebservicePt().publishToSiebel(...)`; only invoked when `OMS_SYSTEM_PARAMETERS('CALL_SIEBEL','OMS_SYSTEM_OPTION')='Y'` and `application_Id != 'SIEBEL_CRM'`; on failure persists a retry row to `OmsRepublishData` with `webServiceId` looked up via `session.getOmsWebserviceUriDetailFindByWebserviceName("SIEBEL_ORDER_FEED")` (`InterfacePersistence.java:387-690`, called from `OMSCustomerOrderBean.java:401-420`)

- `OMSCustomerOrderBean.createResponse` → `ResponseProcessing.createResponse` builds `CoPaymentConfResponse`

- `DCtoDCTransfer.updateTsfNoandFulFillOrdNo` — post-processing for DC-to-DC transfer scenario (`PaymentConfirmationWebServiceImpl.java:155-157`)

- `PersistPaymentRequest.deletePaymentAuditTable` — clears the audit/lock row on success (`PersistPaymentRequest.java:53-74`)

**Error/rollback path**: on `SOAPException` or any `Exception`, checks `OMSCustomerOrderBean.checkOmsCustOrdHeadPaymentStatus()`; if payment is not already confirmed, calls `rollbackForTimeout(omsCustOrdNo, customerOrderNo)` which: sets `OmsCustOrdReserve.paymentStatus='P'`/`resvStatus='RES'` for every reserve row (JPA merge), sets `OmsRtlogPublishLog.publishedInd='F'` for related publish-log rows, and calls `OMSUtilCommons.rollback(customerOrderNo)` (`OMSCustomerOrderBean.java:581-634`). Rollback of RMS reservation itself (`RMSPackage.rollbackRmsPackageCall`, same `OMS_INVADJ_STATUS_UNAVIALINV` procedure with positive qty) and RMS/SIM order cancellation (`InterfacePersistence.callRMSCancellationWebservice` → RMS `FulfillOrderService.cancelFulfilOrdColRef`; `InterfacePersistence.callSIMCancelllationWS` → SIM `StoreFulfillmentOrderService.cancelFulfillmentOrderDetail`) are invoked from `OMSCustomerOrderBean.rollback`/`rollbackUnreservation` (`OMSCustomerOrderBean.java:348-577`, `InterfacePersistence.java:199-386`).

**Request fields** (`CoPaymentConf`, `src/com/logicinfo/oms/model/CoPaymentConf.java:76-101`): `entity_id` (required, ≤30), `application_Id` (required, enum `SIEBEL_CRM`), `comments` (optional, ≤240), `request_datetimestamp` (required, dateTime), `customer_order_no` (required, ≤48), `customer_sub_order_no` (optional, ≤3, defaults to `"1"` in code), `payment_status` (required, enum `S`/`D` — not read by the impl beyond validation), `payment_datetime` (required, date).

**Response fields** (`CoPaymentConfResponse.java:97-120`): `entity_id`, `application_id`, `request_datetimestamp`, `response_datetimestamp`, `customer_order_no` (required), `oms_customer_ord_no` (nillable Long), `customer_order_response_items` (nillable list — per-line `order_qty_suom`, `fulfill_qty_suom`, `status_message`, `line_no`, and nested fulfillment detail: `tsf_no`, `po_no`, `source_loc(/type)`, `fulfill_loc(/type)`, `RMS_resv_qty/loc/loc_type`, `backorder_qty`, `fulfill_order_no` — see `CustomerOrderResponseItems.java:124-133`, `CustomerOrderResponseItemFulfillment.java:148-172`), `message_code`, `message_desc`, `message_status`.

**Business rules / validation** (`OMSCustomerOrderBean.getOmsCustOrdNo`, `OMSCustomerOrderBean.java:53-211`):

- Order must exist in `OMS_CUST_ORD_HEAD` for the given `customer_order_no`+sub-order+`application_Id`; otherwise `customerOrderNotExist`/`paymentCannotConfirm` fault (localized via `OMSUtilCommons.formErrorDescription`).

- Only a header with `status='S'` is eligible; if the only matches are `status<>'S'` with reserve rows in `RES`, or no reserve rows at all → `paymentCannotConfirm` fault.

- If `ord_payment_status='S'` already → `paymentAlreadyConfirmed` fault (idempotency guard against double payment confirmation).

- If `ord_payment_status='R'` (rejected) → `paymentRejected` fault.

- Concurrent-call guard via `Oms_PaymentConf_Audit` — a second call for the same `customer_order_no`+`oms_cust_ord_no` while one is in flight returns `messageCode="PaymentInProgress"`, `messageStatus="F"` (`PersistPaymentRequest.checkPaymentProgress`/`returnTenderStatusResponse`).

- `ValidatePayment.getTenderStatus`/`updateOmsTenderPaymentStatus` (present in code, currently commented out of the main flow at `PaymentConfirmationWebServiceImpl.java:82-88`) model tender-level payment status transitions `P` (pending) → `A` (in progress) → confirmed, against `OmsCustOrdTender.paymentStatusInd`.

- Tender/amount fields carried through to Siebel: `TenderDetails` (`cc_auth_no`, `cc_auth_src`, `cc_cardholder_verf`, `cc_entry_mode`, `cc_exp_date`, `cc_no`, `cc_spec_cond`, `cc_term_id`, `tender_amt`, `tender_type_group`, `tender_seq_no`, `tender_type_id`) sourced from `OmsCustOrdTender` (`InterfacePersistence.java:600-639`).

**External systems**: RMS `FulfillOrderService` (SOAP), SIM `StoreFulfillmentOrderService` (SOAP), Siebel `SiebelOrderFeedWebservice` via `InboundordercreationbpelprocessClientEp` (SOAP/BPEL), all WSDL-generated clients under `classes/com/oracle/...` and `classes/retail/siebel/com/integration/...`; target endpoints resolved from the generated JAX-WS client config, and for Siebel additionally validated against `OMS_WEBSERVICE_URI_DETAIL` (`session.getOmsWebserviceUriDetailFindByWebserviceName`).

**Transactions**: no explicit JTA/`@Transactional` boundary is visible; persistence is a mix of direct JDBC (`PersistPaymentRequest`, `RMSPackage`) with manual connection open/close in `finally`, and container-managed JPA `EntityManager.merge()` calls through the `OMSUtilSessionEJB` stateless session bean (commit semantics are whatever the EJB container applies per method — not explicit in this module). Rollback of business state is handled by explicit compensating calls (`rollbackForTimeout`, `rollback`, `rollbackUnreservation`), not a DB transaction rollback.

---

### VASContract — `processVASContract`

- **Module**: `VASContract` (legacy JAX-WS WAR)

- **Type**: SOAP 1.2, JAX-WS BARE

- **Endpoint**: `/VASContractWebService` — `public_html/WEB-INF/web.xml:20-28` (behind `oracle.security.jps.ee.http.JpsFilter`, anonymous access enabled)

- **WSDL**: `public_html/WEB-INF/wsdl/VASContractWebService.wsdl`, namespace `http://com.logicinfo.oms/model/`

- **Entry point**: `VASContractWebServiceImpl.processVASContract(VASContract)` — `src/com/logicinfo/oms/model/VASContractWebServiceImpl.java:38`

**Call chain**: single-hop — the servlet impl directly opens a JDBC connection (`OMSUtil.getDBConnection("jdbc/extradev")`) and executes one `INSERT` (`VASContractWebServiceImpl.java:46-163`). No service/DAO layer is used even though a JPA-backed EJB (`VASContractEJBSessionBean`, `src/com/logicinfo/oms/vascontract/VASContractEJBSessionBean.java`) exists in the same module with `persistVasContractsEcom`/`mergeVasContractsEcom`/`getVasContractsEcomFindAll` methods — it is dead code from this endpoint's perspective (not called by `VASContractWebServiceImpl`).

**SQL**: `INSERT INTO VAS_CONTRACTS_ECOM(SRV_ID, SERVICE_PACKAGE_NAME, OPER_UNIT_ID, CONTRACT_NO, ORG_ID, ITEM_SKU, ITEM_SERIAL_1, ITEM_SERIAL_2, SRV_SKU, SRV_PRICE, SRV_START_DATE, SRV_END_DATE, NO_OF_YEARS, STATUS_DESC, SRV_INV_NO, SRV_INV_SOURCE, ITEM_INV_SOURCE, EXTRA_ITEM, MOBILE, DP_YEAR, SALESMAN_ID, SRV_INV_COM, ITEM_INV_COM, ITEM_INV_DATE, ON_SITE, SRV_INVOICE_LINE_NO, ITEM_INVOICE_LINE_NO, TOTAL_VISITS, FREE_LABOR, ALLOW_LOANER, NUM_OF_RE_INSTALLATION_AVAIL, NUM_OF_PREV_MAINTENANCE_AVAIL, ON_SITE_VISIT_FLAG, REPLACEMENT_GUARANTEE, NUMBER_OF_CLEANING_VISITS, NUMBER_OF_OTHER_VISITS, LAST_UPDATE_DATE, VAS_GROUP, VAS_TYPE, FIRST_NAME, LAST_NAME, COVERED_PRODUCT, DIVISION_STORE_NAME, COUNTRY, VAS_CREATED_DATE, SERVICE_STATUS, RETURNED_BSN_DATE) VALUES(47 bind params)` — `VASContractWebServiceImpl.java:48-163`. `SERVICE_STATUS` is hardcoded to the literal string `"null"` (bug: not SQL NULL).

**Request fields** (`VASContract.java:411-515`, all `required=true` unless noted): `srv_id`, `service_package_name`, `oper_unit_id` (not `required=true` per annotation but no null-check), `contract_no`, `org_id`, `item_sku`, `item_serial_1`, `item_serial_2`, `srv_sku`, `srv_price`, `srv_start_date`, `srv_end_date`, `no_of_years`, `status_desc`, `srv_inv_no`, `srv_inv_source`, `item_inv_source`, `extra_item`, `return_reason`, `first_brand`, `no_of_visits`, `salesman_id`, `srv_inv_com`, `item_inv_com`, `item_inv_date`, `on_site`, `srv_invoice_line_no`, `item_invoice_line_no`, `software_key`, `total_visits`, `free_labor`, `allow_loaner`, `compensation_type_text`, `free_spare_parts_text`, `num_of_re_installation_avail`, `num_of_prev_installation_avail`, `on_site_visit_flag`, `replacement_guarantee`, `number_of_cleaning_visits`, `number_of_other_visits`, `vas_group`, `first_name`, `last_name`, `covered_product`, `division_store_name`, `vas_created_date`, `service_type`, `dp_year`, `vas_type`. Several of these (`return_reason`, `first_brand`, `no_of_visits`, `software_key`, `compensation_type_text`, `free_spare_parts_text`) are accepted but **not** persisted by the current INSERT.

**Response fields** (`VASContractResponse`): `XRequestorId` (= request `org_id`), `XApplicationId` (hardcoded `"E-COMMERCE"`), `XResponseDatetime` (now), `statusCode` (`200` success / `400` failure), `status` (`Success`/`Failed`), `message`.

**Business rules**: none beyond straight persistence — no dedup, no state-machine, no VAT logic in this endpoint. On exception, `statusCode=400`, `status="Failed"`, generic message `"Client specific issue (invalid input, invalid resource, unauthorized etc.)"` (`VASContractWebServiceImpl.java:174-184`).

**Transaction**: single auto-commit JDBC `executeUpdate()`; connection closed in `finally` via `OMSUtil.closeDBConnection`. No explicit commit/rollback statements.

---

### SIMDeliveryDetail — `processNewOrder`

- **Module**: `VASContract/SIMDeliveryDetail` (sub-WAR)

- **Type**: SOAP, JAX-WS BARE

- **Endpoint**: `/SIMDeliveryDetailWebService` — `SIMDeliveryDetail/public_html/WEB-INF/web.xml:20-27`

- **WSDL**: `SIMDeliveryDetail/public_html/WEB-INF/wsdl/SIMDeliveryDetail.wsdl`, namespace `http://com.logicinfo.oms/model/`

- **Entry point**: `SIMDeliveryDetailWebServiceImpl.processNewOrder(SIMDeliveryDetail)` — `SIMDeliveryDetail/src/oms/logicinfo/com/model/SIMDeliveryDetailWebServiceImpl.java:29`

**Call chain**:

- `SIMDeliveryDetailWebServiceImpl.processNewOrder` → `SIMDeliveryDetailBean.validateInput` (no-op logging only, `SIMDeliveryDetailBean.java:30-32`) → `SIMDeliveryDetailBean.processOrdersDetails` (`SIMDeliveryDetailBean.java:34-77`)

- Branches on `orderStatus`:
`READY_TO_SHIP` → `SIMDeliveryDetailBean.generateResponse` (`SIMDeliveryDetailBean.java:84-424`), which first calls `SIMDeliveryDetailCommon.processReadyToShipDetails(numberOfOrders)` (`SIMDeliveryDetailCommon.java:31-231`)

- `UNDELIVERED` → `SIMDeliveryDetailBean.generateResponseForUndelivered` (`SIMDeliveryDetailBean.java:427-712`), first calling `SIMDeliveryDetailCommon.processUndeliveredDetails(numberOfOrders)` (`SIMDeliveryDetailCommon.java:235+`)

- anything else → `Exception("Request Contains Incorrect Data")`

- Both `SIMDeliveryDetailCommon` methods run a large cross-schema SQL join over a **DB link (`@rmsdb`)** from the SIM schema, e.g. (ready-to-ship, `SIMDeliveryDetailCommon.java:46-71`):```
select * from (
  select Orders.Store_Id, Delivery.Id as DeliveryId, Orders.Cust_Order_Id, FulfillDetail.line_no, OrderLines.Item_Id, DeliveryLines.Quantity,
         OrderLines.UNIT_COST_VALUE, OrderLines.UNIT_COST_CURRENCY, Carrier.Code, Carrier.Description, Tracking_Number,
         Delivery.CREATE_DATE, Delivery.UPDATE_DATE
  from Ful_Ord_Dlv Delivery, Ful_Ord Orders, Shipment_Bol Shipment, Ful_Ord_Dlv_Line_Item DeliveryLines,
       Shipment_Carrier Carrier, Ful_Ord_Line_Item OrderLines, OMS_CUST_ORD_HEAD@rmsdb OmsHead,
       oms_co_fulfill_detail@rmsdb FulfillDetail, oms_cust_ord_item@rmsdb OrdItem
  where Delivery.Ful_Ord_Id = Orders.Id
    and Orders.Cust_Order_Id = OmsHead.cust_order_no and OmsHead.Status = 'S'
    and OrderLines.Ful_Ord_Id = Orders.Id and Shipment.Id = Delivery.Shipment_Bol_Id
    and Carrier.Id(+) = Shipment.Ship_Carrier_Id and Delivery.Id = DeliveryLines.Ful_Ord_Dlv_Id
    and OrderLines.Id = DeliveryLines.Ful_Ord_Line_Item_Id
    and Delivery.Status = 1 and Shipment.Tracking_Number is null and Delivery.Shipment_Bol_Id is not null
    and DeliveryLines.Quantity > '0' and Orders.Store_Id in (select store from store@rmsdb)
    and Carrier.Code is not null
    and OrderLines.item_id = FulfillDetail.item and Orders.external_id = FulfillDetail.fulfill_order_no
    and FulfillDetail.item = OrdItem.item and OmsHead.cust_order_no = Orders.cust_order_id
    and OmsHead.oms_cust_ord_no = FulfillDetail.oms_cust_ord_no and OmsHead.oms_cust_ord_no = OrdItem.oms_cust_ord_no
    and FulfillDetail.line_no = OrdItem.line_no and OmsHead.DELIVERY_TYPE = 'S'
  order by Delivery.UPDATE_DATE asc)
where rownum <= ?

```

(Undelivered variant is analogous, `SIMDeliveryDetailCommon.java:250-265`, additionally selecting `Shipment_Id`.)

- Back in `SIMDeliveryDetailBean`, per order: EJB finders `session.getOmsCustOrdHeadFindOmsCustOrdNo`, `getOmsCustOrdTenderFindTenderTypeIdByOmsCustOrdNo`, `getOmsCustOrdTenderSumOfTenderAmt`, `getOmsCustOrdAddressFindByOmsCustOrdNo`, `getItemSuppCountryDimFindByItemId`, `getItemMasterFindItemDesc`; plus two direct-JDBC `UPDATE`s:
`UPDATE ordcust SET BILL_PHONE = (SELECT BILL_PHONE FROM ordcust WHERE CUSTOMER_ORDER_NO = '<order>' AND rownum=1 AND BILL_PHONE IS NOT NULL) WHERE CUSTOMER_ORDER_NO = '<order>' AND BILL_PHONE IS NULL` on `jdbc/oms` (both ready-to-ship and undelivered paths — string-concatenated, not parameterized: `SIMDeliveryDetailBean.java:264-276`, `596-607`)

- `UPDATE Ful_Ord_Dlv SET update_date = sysdate WHERE ID IN ('<deliveryId>')` on SIM connection, undelivered path only (`SIMDeliveryDetailBean.java:558-568`)

- `SELECT CARRIER_CITY FROM xx_vxref_carrier_city WHERE EXTRA_CITY=? AND CARRIER_CODE=?` (parameterized) used to map OMS delivery city → carrier-specific city name (`SIMDeliveryDetailBean.java:343-364`, `645-663`)

**Request fields** (`SIMDeliveryDetail.java` XSD comment): `numberOfOrders` (unsignedInt, ≤4 digits), `orderStatus` (enum `READY_TO_SHIP`/`UNDELIVERED`/`ALL`), `orderSource` (enum `E-COMMERCE`/`ORPOS`/`ALL`, read but not filtered on in code), `retrieveCustomerDetails` (`True`/`False`), `retrieveProductDetails` (`True`/`False`).

**Response fields** (`Order`/`CustomerOrderResponseAddress`/`CustomerOrderResponseItems`): order no, store id, carrier, order value (= sum of tender amounts), `isCod` (`Y` when `tender_type_id = 106`), `isExpressDelivery`, shipment id, per-item SKU/name/qty/line no/delivery id/value/currency/weight, and (if requested) customer address block (name, phone, email, city — resolved via the carrier-city cross-reference table, district, shipping address, country, language).

**Business rules**: `isCod` flag derives from a hardcoded `tender_type_id == 106` check (`SIMDeliveryDetailBean.java:170-181`); "express delivery" flag comes from `OMSUtilCommons.getExpressDelvdetails(orderNo)` (outside this module).

**Transactions**: raw JDBC, autocommit, connections closed in `finally`.

---

### apple-pricing / boot-apple-pricing — `GET/POST /pricing`

Two near-identical deployments of the same logic: `apple-pricing` is a Spring MVC WAR (WebLogic, `web.xml` + Spring `DispatcherServlet`), `boot-apple-pricing` is the Spring Boot repackage of the same code (embedded servlet, `@SpringBootApplication`). Business logic, DAO, and SQL are byte-for-byte equivalent except for logging framework (`log4j` vs `slf4j`) and one JDBC unwrap idiom.

- **Type**: Spring REST (`@RestController`)

- **Base path**: `/pricing` — `PricingController.java:18` (`apple-pricing/src/main/java/com/extra/apple/pricing/controller/PricingController.java`, mirrored in `boot-apple-pricing`)

- **Servlet mapping**: `apple-pricing/src/main/webapp/WEB-INF/web.xml:31-34` maps `SpringDispatcher` to `/*`; `boot-apple-pricing` uses embedded Tomcat with default root context.

#### `GET /pricing` — `updatePricingDetail`

- **Entry point**: `PricingController.updatePricingDetail()` — `controller/PricingController.java:26-34`

- **Call chain**: `PricingController` → `PricingService.updatePricingDetail()` (`service/PricingService.java:37-64`) → `PricingDAO.getCurrentPriceIDs()` (SQL) → for each price-sheet id, async `AsyncAPIService.getPriceSheetTemplate(id)` → `AppleAPIService.getPriceSheetTemplate` (Feign `GET /price-sheets/{priceSheetId}/template` against Apple's pricing API) → `PricingDAO.updateMPN(lob, activeMPNs)` (stored proc)

- **SQL**: `SELECT PRICE_SHEET_ID, APPLE_ID FROM XX_APPLE_STORE_PS_DETAIL WHERE APPLE_ID = :appleId` (`dao/PricingDAO.java:59`)

- **Stored procedure**: `{ CALL XX_APPLE_ON_PRICING_SQL.APPLE_MPN_REFRESH(?, ?, ?, ?) }` — params: `p1` = Oracle `XXMPNS_TBL_TYPE` array of active MPN strings, `p2` = product/LOB name (`String`), OUT `p3`=`status` (VARCHAR), OUT `p4`=`message` (VARCHAR); throws when `status != 'Y'` (`dao/PricingDAO.java:83-98`)

#### `POST /pricing` — `publishPricingDetail`

- **Entry point**: `PricingController.publishPricingDetail()` — `controller/PricingController.java:36-42`

- **Call chain**: `PricingService.publishPricingDetail()` (`service/PricingService.java:66-90`):
`PricingDAO.getUpdatedPriceSheetIds()` — `SELECT PRICE_SHEET_ID FROM XX_APPLE_STORE_PS_DETAIL WHERE PUBLISH_IND = 'N'`

- for each id, `AsyncAPIService.getPriceSheetTemplate(id)` (Feign, fire-and-forget refresh)

- `PricingDAO.getUpdatedPriceSheet()` — joins `XX_APPLE_STORE_ITEM_PRICES P, XX_APPLE_STORE_PS_DETAIL S WHERE S.STORE=P.STORE AND S.PRODUCT_CODE=P.PRODUCT_CODE AND P.PUBLISH_IND='N'`, builds `PriceSheet`/`PricePlan`/`PricePlanOffer` objects (`dao/PricingDAO.java:101-150`)

- per sheet, `AsyncAPIService.updateAndPublishPriceSheet(sheet)` → `AppleAPIService.instantPriceSheet` (Feign `PUT /instant-pricing/mpns`)

- `PricingDAO.publishPriceSheet(sheet)` — `{ CALL XX_APPLE_ON_PRICING_SQL.POST_PUBLISH_UPDATE(?, ?, ?) }`, params: `priceSheetId` (Long), OUT `status`, OUT `message`; throws when not `'Y'` (`dao/PricingDAO.java:152-178`)

- Also present but not wired to a controller path: `PricingDAO.rePublishPriceSheet` → `{ CALL XX_APPLE_ON_PRICING.POST_REPUBLISH_UPDATE(?, ?, ?, ?, ?, ?) }` (priceSheetId, publishId, startDate, endDate, OUT status, OUT message) and `PricingDAO.getPriceSheetIDs()` (`SELECT PRICE_SHEET_ID, APPLE_ID FROM XX_APPLE_STORE_PRICE_SHEET WHERE REPUBLISH_IND = 'N'`) — dead/unused code paths (`dao/PricingDAO.java:180-234`).

**Request/response fields**: both endpoints take no request body; response is `Response{status: "Y"/"N", message}` (`model/Response.java`, exception path handled by `BaseController.handleException`, `controller/BaseController.java:21-28`).

**Item/price fields modeled**: `PriceSheet{priceSheetId, lob, stores[], priceSheetDescriptions[]}` → `PriceSheetDescription{mpns[], properties(consumerOffer, valueProposition, partnerTC), pricePlans[]}` → `PricePlan{planName, pricePlanOffers[]}` → `PricePlanOffer{priceOfferTitle, offerNumericPrice, offerTerm, qualifyingDescriptionTitle, qualifyingDescription, offerRelation}` (`model/PriceSheet.java`, `PriceSheetDescription.java`, `PricePlan.java`, `PricePlanOffer.java`). DB columns sourced: `PLAN_NAME`, `OFFER_NAME`, `SELLING_PRICE`, `OFFER_TERM`, `QUAL_DESC_TITLE`, `QUAL_DESC`, `OFFER_RELATION`, `CONSUMER_OFFER`, `VALUE_PROPOSITION`, `DEPT` (iPad department codes excluded from `properties`, `dao/PricingDAO.java:122-131`).

**Business rule**: iPad department codes (config `iPadDeptIds`, e.g. `{'626','606'}`) are excluded from setting `consumerOffer`/`valueProposition` — Apple's iPad program forbids reseller-set consumer offer text (`dao/PricingDAO.java:124-130`).

**External system**: Apple Pricing API v2, base URL from `apple.pricing.url.v2` (`application.properties`, currently `https://dc-pricing-api.apple.com/pricing-api/api/v2`), Feign client built in `config/MvcConfiguration.java:74-79` (apple-pricing) / `config/FeignConfiguration.java` (boot-apple-pricing) with a custom `HeaderInterceptor` (`common/interceptor/HeaderInterceptor.java`): every request gets `API_CLIENT_ID`, `TIMESTAMP`, and `AUTHORIZATION` = Base64(SHA-256(AES-ECB-encrypt(timestamp, apiSecretKey))) — `apple.pricing.clientid`/`apple.pricing.secret` from config. The apple-pricing (non-boot) variant also installs a trust-all `SSLSocketFactory`/`X509TrustManager` for the Apple TLS connection (`config/MvcConfiguration.java:81-129`) — certificate validation is disabled for that outbound call.

**Config present**: `application.properties` in both modules define `apple.id`, `apple.pricing.clientid`, `apple.pricing.secret`, `apple.pricing.url.v2`, `iPadDeptIds`; `boot-apple-pricing/src/main/resources/application.properties` additionally hardcodes production Oracle datasource URL/username/password (RMSPRD) in plaintext.

**Transactions**: no explicit transaction demarcation; each DAO call is a single Spring `NamedParameterJdbcTemplate` statement/call, autocommit.

---

### oms-discount — `POST /api/discount`

- **Module**: `oms-discount` (Spring MVC WAR)

- **Type**: Spring REST

- **Endpoint**: `web.xml` maps dispatcher to `/api/*` (`src/main/webapp/WEB-INF/web.xml:32-35`); controller path `/discount` (`controller/DiscountController.java:19`) → effective URL `/api/discount`

- **Entry point**: `DiscountController.saveDiscountInfo(DiscountInfo)` — `src/main/java/com/extra/oms/discount/controller/DiscountController.java:27-42`

- **Call chain**: `DiscountController` → `DiscountService.saveDiscountInfo` (`service/DiscountService.java:19-21`) → `DiscountDAO.saveDiscountInfo` (`dao/DiscountDAO.java:30-55`)

- **SQL** (batch insert, one row per `DiscountItem`): ```
INSERT INTO XX_PMP_INV_DETAILS(STORE, BUSINESS_DATE, INVOICE_NO, TRAN_DATE, TRAN_TYPE, ITEM, QTY, UNIT_RETAIL,
  TAX_AMOUNT, ORIGINAL_TRAN_NO, ORIG_TRAN_ITEM, ORIG_TRAN_ITEM_LINE_NO, CUST_ORDER_NO, PROCESS_FLAG,
  OMS_CUST_ORD_NO, EXTD_UNIT_PRICE, PMP_AMT)
VALUES(:storeId, :businessDate, :invoiceNo, :tranDate, :tranType, :item, :quantity, :unitRetail, :taxAmount,
  :origTranNo, :origTranItem, :origLineNo, :custOrdNo, 'N',
  (SELECT OMS_CUST_ORD_NO FROM OMS_CUST_ORD_HEAD WHERE CUST_ORDER_NO = :custOrdNo AND STATUS = 'S'),
  :extendUnitPrice, :pmpAmount)

```

(`dao/DiscountDAO.java:53-54`) — `PROCESS_FLAG` hardcoded `'N'`, `OMS_CUST_ORD_NO` resolved inline by correlated subquery to the successful (`status='S'`) order header.

**Request fields** (`model/DiscountInfo.java`, `model/DiscountItem.java`): header — `store` (Integer), `business_date` (Date, `dd-MM-yyyy`), `invoice_no`, `tran_date` (Date, `dd-MM-yyyy`), `tran_type`, `cust_order_no`, `items[]`; item — `item`, `qty`, `unit_retail`, `extd_unit_price`, `tax_amount`, `pmp_amt`, `original_tran_no`, `orig_tran_item`, `orig_tran_item_line_no`. No fields are marked required by Bean Validation annotations — everything is optional at the framework level, and a missing `items` list would NPE in the DAO loop.

**Response**: `Response{status: "Success"/"Failed", message}` (`model/Response.java`).

**Business rule**: this is a PMP (price-match-promise / promotional-margin-protection) discount recording endpoint — no VAT or pricing computation in code; it is a pass-through persistence of pre-computed discount amounts (`pmp_amt`, `tax_amount`) from an upstream POS/PMP system, keyed to an OMS order that must already be in successful (`status='S'`) state.

**Transactions**: single `NamedParameterJdbcTemplate.batchUpdate` call, autocommit; exception is caught only at the controller (`response.setStatus("Failed")`), not rolled back explicitly (batch itself may partially apply depending on driver batching mode — not controlled here).

---

### oms-finance — scheduled batch: contract-file/OMS order reconciliation

- **Module**: `oms-finance` — plain Spring context app (no web layer), started via `Application.main` (`src/main/java/com/extra/finance/Application.java:17-27`) using `AnnotationConfigApplicationContext(AppConfig.class)`.

- **Not a callable API** — a `@Scheduled(fixedDelay = 1000)` batch job: `OrderService.checkOrderStatus()` (`service/OrderService.java:27-39`).

**Call chain**:

- `OrderService.checkOrderStatus` → `OrderDAO.getContractOrders()` — `SELECT DISTINCT CUST_ORDER_NO FROM OMS_CUST_ORD_HEAD WHERE CONTRACT_FLAG = 'N'` (`dao/OrderDAO.java:27-35`)

- `FTPService.validateContractForOrders(ordNos)` — for each order, SFTP `lstat` on `<ssh.remote-path>/contract-<orderNo>.pdf` via `net.schmizz.sshj` `SFTPClient` (`service/FTPService.java:29-47`); orders whose contract PDF is missing are removed from the list.

- `OrderDAO.updateOrderStatus(remainingOrdNos)` — `UPDATE OMS_CUST_ORD_HEAD SET CONTRACT_FLAG = 'Y' WHERE CUST_ORDER_NO IN (:orderNos) AND STATUS = 'S'` (`dao/OrderDAO.java:23-25`)

**Business rule**: an order is only marked `CONTRACT_FLAG='Y'` once its signed contract PDF is confirmed present on the SFTP server — this gates finance/VAS-contract orders from downstream processing until proof-of-contract exists.

**External system**: SFTP server, host/port/user/password/remote-path from `application.properties` (`ssh.host=sftp2.procco.net`, `ssh.port=28822`, `ssh.remote-path=/ExtraEgypt-UATContracts`) with hardcoded fallback defaults also present in `@Value` annotations in `config/AppConfig.java:37` pointing at a different host/port/credentials — i.e. two different hardcoded SFTP credential sets exist (one in code defaults, one in properties), both plaintext.

**Config present**: `application.properties` — `ssh.host/port/username/password/remote-path`, `db.url/username/password` (points at `RMSQA`). `AppConfig.java` additionally hardcodes SSH host-key verification to always return `true` (`return true` in `HostKeyVerifier.verify`, `config/AppConfig.java:42`) — host key checking is effectively disabled.

**Transactions**: no explicit transaction management; two independent `NamedParameterJdbcTemplate` statements, autocommit.

---

### e-invoice-util — shared library (no endpoint)

`e-invoice-util` is a Spring Boot–flavored module (`EInvoiceApplication` has `@SpringBootApplication`) but ships **no controller, no `@Scheduled` method, and no `application.properties`** — it is a shared JAR of ZATCA UBL invoice-building helpers and Feign client interfaces consumed by other modules, not an independently deployed service in this repo:

- `util/InvoiceUtil.java` — builds a ZATCA-style UBL `InvoiceType` from a JDBC `ResultSet` (`createEComInvoice`, `InvoiceUtil.java:210-286`): sets `ProfileID="reporting:1.0"`, invoice type code `388` (tax invoice) for `SALE` vs `381` (credit note) otherwise with a `BillingReferenceType` back-reference to `ORIGINAL_ORDER`, currency `SAR`, ICV/PIH placeholder document references, party CRN `344233` hardcoded (`InvoiceUtil.java:281`).

- `service/client/IZatcaInterfaceAPI.java` — Feign client, `POST /invoices/clearance/single` and `POST /invoices/reporting/single` with a `Clearance-Status` header, base URL `${zatca.api.url}` (`FeignZatcaConfiguration`).

- `service/client/IZatcaCertificateAPI.java` — Feign client for ZATCA onboarding: `POST /compliance` (CSR + OTP → CSID), `POST /compliance/invoices` (compliance check), `POST /production/csids` (production CSID exchange), `POST /invoices/reporting/single`.

- `config/FeignZatcaConfiguration.java` — HTTP Basic auth interceptor using `zatca.api.binary-token`/`zatca.api.secret`, forces headers `Accept-Version: V2`, `Accept-Language: en`.

This module's classes are superseded/duplicated by near-identical classes inside `einvoicing` (see below) — `einvoicing` is the actually-scheduled production service; `e-invoice-util` looks like an earlier or shared iteration of the same utility code.

---

### einvoicing — scheduled ZATCA e-invoicing batch service

- **Module**: `einvoicing` (Spring Boot, `EinvoicingApplication`, `@EnableScheduling @EnableFeignClients`)

- **Not a REST API** — six independent `@Scheduled(cron=...)` jobs on `EInvoicingService` (`src/main/java/com/extra/einvoicing/service/EInvoicingService.java`), each cron expression externalized to `application.properties` (`schedule.ebs.cron`, `schedule.pos.cron`, `schedule.canret.cron`, `schedule.report.cron`, `schedule.siebel.cron`, `schedule.email.cron` — all currently `0 */1 * * NOV THU` except email `0 */1 * * * *`, i.e. effectively disabled/mistuned cron values in this environment).

#### Job 1 — `generateEBSInvoice()` (B2B, EBS-sourced AP/AR invoices → ZATCA clearance)

- `EInvoicingService.generateEBSInvoice` (`EInvoicingService.java:29-38`) → `B2BInvoicingService.processEBSInvoice()` (`service/B2BInvoicingService.java:37-51`)

- `B2BInvoicingDAO.getEBSInvoices()` — `SELECT * FROM XX_B2B_EBS_EINV_REP WHERE XML_REPORT_FLAG = 'N'` (`dao/B2BInvoicingDAO.java:127-129`), grouped into `InvoiceInfo` via `InvoiceUtil.createEBSInvoice`/`createEBSLineItem`

- Partitioned by CPU-core count (`Partition`, `util/Partition.java`) and dispatched to `B2BInvoicingProcess.processInvocies` (`@Async`, `service/B2BInvoicingProcess.java:31-42`), which submits each invoice to a `ThreadPoolExecutor` running `B2BInvoiceProcessor` (`service/B2BInvoiceProcessor.java`):
`HashingService.getInvoiceHash` — computes ICV (`XX_EINVOICE_COUNTER_SEQ.NEXTVAL`, `dao/BaseDAO.java:71-73`) and PIH (previous invoice hash chained from `XX_EINV_XML_GAZT_REP.HASH_VALUE`, `dao/BaseDAO.java:75-81`) and embeds them into the UBL doc's ICV/PIH `AdditionalDocumentReference` (`service/HashingService.java:36-69`)

- `InvoiceUtil.serializeToXml` — JAXB-marshal the UBL invoice

- Optional schema/schematron validation via `resourceBean.getValidationProcessor()` (ZATCA SDK `ApiValidationProcessorImpl`), gated by `zatca.xml.validate`

- `resourceBean.getZatcaInterfaceAPI().clearanceApi(request, 1)` — Feign `POST /invoices/clearance/single` with `Clearance-Status: 1`, body `{uuid, invoiceHash, invoice(Base64 XML)}`

- `B2BInvoicingDAO.saveClearedInvoice` — `INSERT INTO XX_EINV_XML_GAZT_REP(INVOICE_NUM, INVOICE_ORIG_SYS, INVOICE_TYPE, BUSINESS_DATE, XML_GEN, PROCESS_DATE, GAZT_RPT_FLAG, INVOICE_DESC, HASH_VALUE) VALUES(..., 'B2B', ...)` (`dao/B2BInvoicingDAO.java:147-161`)

- `B2BInvoicingDAO.updateInvoiceStatus` — `UPDATE XX_B2B_EBS_EINV_REP SET XML_REPORT_FLAG=:status, XML_PROCESS_DATE=SYSDATE, ERROR_MSG=:message, ICV=:icv WHERE INVOICE_ID=:invoiceId` (`dao/B2BInvoicingDAO.java:131-145`)

- On clearance success, generates a BIP PDF report (`IBIPReportService.runReport`, SOAP-over-HTTP via Feign, `report.bip.url`), embeds the cleared XML as a PDF attachment (PDFBox), and emails it via `IEmailService.sendEmail` (Feign `POST /email` to `email.config.url`) to `invoice.customerEmail` and to a per-type CC address from `report.bip.email-cc`/`email-to` maps (`ReportUtil.getConfigToEmail`)

- `B2BInvoicingDAO.updateEBSEmailStatus` — `UPDATE XX_B2B_EBS_EINV_REP SET EMAIL_STATUS=:eMailStatus WHERE INVOICE_ID=:invoiceId`

- On DB error mid-scan, `B2BInvoicingDAO`'s `EBS_INVOICE_RESULT_SET` extractor marks the invoice failed inline: `UPDATE XX_B2B_EBS_EINV_REP SET ICV=:icv, XML_REPORT_FLAG='F', ERROR_MSG=:message WHERE INVOICE_ID=:invoiceId` (`dao/B2BInvoicingDAO.java:69-81`)

#### Job 2 — `generateB2BPOSInvoice()` (B2B POS transactions → ZATCA clearance)

- `EInvoicingService.generateB2BPOSInvoice` → `B2BInvoicingService.processPOSInvoice()` → `B2BInvoicingDAO.getB2BInvoices()`

- **SQL**: joins `XX_POS_B2B_EINV T, SA_TRAN_HEAD H, SA_TRAN_ITEM I, SA_TRAN_IGTAX TX, SA_TRAN_DISC D, ITEM_MASTER IM` filtered on `T.PROCESS_FLAG = 'N'`, pulling receipt/tran/item/tax/discount detail plus a correlated CRN lookup `(SELECT CRN FROM XX_EINV_STORE_CRN WHERE STORE = TO_CHAR(T.STORE))` (`dao/B2BInvoicingDAO.java:39-44`)

- Same clearance/email/status pipeline as Job 1, using `updatePOSInvoice` (`UPDATE XX_POS_B2B_EINV SET PROCESS_FLAG=..., ICV=... WHERE TRAN_SEQ_NO=:invoiceNum`) and `updatePOSEmailStatus` instead of the EBS variants (`dao/B2BInvoicingDAO.java:163-176`, `195-206`).

#### Job 3 — `generateB2CXml()` (B2C cancellation/return credit-note XML generation, not cleared through ZATCA — signed locally)

- `EInvoicingService.generateB2CXml` → `B2CXmlGenerationService.generateB2CXml()` → `B2CInvoicingDAO.getCancelReturnDetails()`

- **SQL** (UNION ALL of cancellation and RMA views, `dao/B2CInvoicingDAO.java:35-43`):```
SELECT V.UNIQUE_INV_ID, V.CUST_ORDER_NO, V.CAN_RET_ID, V.OMS_CUST_ORD_NO, NULL RESTOCK_AMOUNT, V.SRC_LOC_CRN CRN, ...
  FROM XX_EINV_CAN_DETAILS_CLR_V V, ITEM_MASTER I WHERE PROCESS_FLAG = 'N' AND I.ITEM = V.ITEM
UNION ALL
SELECT V.UNIQUE_INV_ID, ..., V.RESTOCK_AMOUNT, ..., 'R' TYPE, ...
  FROM XX_EINV_RMA_DETAILS_CLR_V V, ITEM_MASTER I WHERE PROCESS_FLAG = 'N' AND I.ITEM = V.ITEM

```

- `B2CInvoicingProcess.generateXML` (`@Async`, `service/B2CInvoicingProcess.java:64-91`): `HashingService.signB2CXml` → `B2CSigningService.signXml` (ZATCA SDK `SigningServiceImpl`, EC private key parsed from `zatca.api.private-key`, certificate from `zatca.api.binary-token`) — this path **signs locally rather than clearing via the ZATCA API**, consistent with simplified/B2C invoices which are reported, not cleared.

- `B2CInvoicingDAO.updateOMSXMLGeneration` — batch `UPDATE XX_EINV_CAN_RET_OMS SET PROCESS_FLAG=..., ERROR_MSG=..., ICV=... WHERE UNIQUE_INV_ID=:invoiceNum`, plus batch `INSERT INTO XX_EINV_XML_GAZT_REP(..., INVOICE_ORIG_SYS='OMS', INVOICE_TYPE='B2C', ..., VENDOR_ID='extra')` (`dao/B2CInvoicingDAO.java:81-113`)

#### Job 4 — `reportB2CInvoice()` (B2C reporting to ZATCA, with in-process VAT/amount validation)

- `EInvoicingService.reportB2CInvoice` → `B2CReportingService.reportInvoice()` → `B2CInvoicingDAO.getSignedInvoices()` — `SELECT INVOICE_NUM, BUSINESS_DATE, XML_GEN, STORE, INVOICE_ORIG_SYS, INVOICE_TYPE, ORDER_NO, VALIDATE_XML FROM XX_EINV_XML_GAZT_REP WHERE GAZT_RPT_FLAG='N' AND RESA_VLD_FLAG='Y' AND VENDOR_ID='extra'`

- `B2CInvoicingProcess.reportXML` (`service/B2CInvoicingProcess.java:93-179`): deserializes the stored XML, optionally calls `validateInvoice` (see business rules below), computes `InvoiceUtil.getHashFromXml`, then `IZatcaInterfaceAPI.reportingApi(request, 0)` (Feign `POST /invoices/reporting/single`, `Clearance-Status: 0`) via a bounded `ThreadPoolExecutor` (10 threads); success = `response.reportingStatus == "REPORTED"`.

- `B2CInvoicingDAO.updateXMLReporting` — `UPDATE XX_EINV_XML_GAZT_REP SET GAZT_RPT_FLAG=:status, PROCESS_DATE=SYSDATE, ERROR_FLAG=:message WHERE INVOICE_NUM=:invoiceNum AND GAZT_RPT_FLAG='N'`

- `BaseDAO.persistInvForRecon` (`@Transactional`) — batch-inserts `XX_EINV_RECONCIL_HEAD`/`XX_EINV_RECONCIL_DETAIL` for every reported invoice (`dao/BaseDAO.java:25-69`) — **the only `@Transactional` boundary found anywhere in this cluster**.

**Business rule — VAT/amount cross-check before ZATCA reporting** (`B2CInvoicingProcess.validateInvoice`, `service/B2CInvoicingProcess.java:181-241`):

- Fetches authoritative item VAT rates: `SELECT MAX(VAT_RATE) VAT_RATE, A.ITEM FROM VAT_ITEM A WHERE VAT_REGION = 1101 AND A.ITEM IN (:items) AND ACTIVE_DATE = (SELECT MAX(ACTIVE_DATE) FROM VAT_ITEM WHERE ITEM=A.ITEM AND VAT_REGION=A.VAT_REGION) GROUP BY A.ITEM` (`dao/B2CInvoicingDAO.java:49-51`, region `1101` hardcoded).

- For every invoice line: line VAT % in the XML must equal the DB `VAT_RATE` (unless tax amount is 0) — else `SoftException` "Vat rate is not matching...".

- Recomputed `lineAmount` (qty × unit price, divided by `baseQuantity` if set) must match XML `LineExtensionAmount` within ±0.25 — else "Net amount without vat is not matching...".

- Recomputed VAT amount (`lineAmount × vatRate/100`) must match XML tax amount within ±0.25 — else "Vat amount is not matching...".

- Recomputed line total (net+VAT) must match XML `RoundingAmount` within ±0.25 — else "Total amount is not matching...".

- Invoice-level totals (`LegalMonetaryTotal.LineExtensionAmount`, `TaxTotal.TaxAmount`, `LegalMonetaryTotal.PayableAmount`) must match the sums of the above within ±0.25 each — else corresponding `SoftException`.

- A `±0.25` currency-unit tolerance band is used uniformly for all of the above float-based comparisons (`service/B2CInvoicingProcess.java:206,214,221,228,233,238`).

#### Job 5 — `generateSiebelInvoice()` (B2C spare-parts/service invoices from Siebel staging)

- `EInvoicingService.generateSiebelInvoice` → `B2CXmlGenerationService.processSiebelInvoice()` → `B2CInvoicingDAO.getSiebelInvoices()`

- **SQL**: `SELECT INVOICE_CREATION_DATE, UNIQUE_INVOICE_NO, INVOICE_LINE_NO, SKU_DESCR ITEM, QTY, PRICE_EXCL_VAT, PRICE_INC_VAT, VAT_AMT, TOTAL_LINE_EXCL_VAT, TOTAL_LINE, VAT_PERCENTAGE, TRAN_TYPE, FIRST_NAME||' '||LAST_NAME VENDOR_NAME, ADDRESS1, PHONE, EMAIL, ORIGINAL_INVOICE_NO, (SELECT CRN FROM XX_EINV_STORE_CRN WHERE STORE = ORGANIZATION_ID) CRN, SKU FROM XX_EINV_SPARE_PART_STG WHERE PROCESS_IND = 'N'` (`dao/B2CInvoicingDAO.java:45-47`)

- Same `B2CInvoicingProcess.generateXML` signing pipeline as Job 3, but persisted via `B2CInvoicingDAO.updateSiebelXML` (`UPDATE XX_EINV_SPARE_PART_STG SET PROCESS_IND=..., ERROR_MSG=..., ICV=... WHERE UNIQUE_INVOICE_NO=:invoiceNum`, plus `INSERT ... INVOICE_ORIG_SYS='SEIBEL' ...`) (`dao/B2CInvoicingDAO.java:115-147`).

- Return lines (`TRAN_TYPE='RETURN'`) get quantity sign flipped to negative (`InvoiceUtil.createSeibelLineItem(..., TRAN_TYPE.equals("RETURN") ? -1 : 1)`, `service/B2CInvoicingProcess` caller at DAO line 197).

#### Job 6 — `resendEmail()` (retry of failed B2B invoice emails)

- `EInvoicingService.resendEmail` → `B2BInvoicingService.processResendEmail()` — pulls `B2BInvoicingDAO.getEBSEmailDetails()` (`XX_B2B_EBS_EINV_REP`/`XX_EINV_XML_GAZT_REP` join on `EMAIL_STATUS='F' AND GAZT_RPT_FLAG='Y'`) and `getPOSEmailDetails()` (analogous on `XX_POS_B2B_EINV`), then `B2BInvoicingProcess.resendEmails` submits each to `B2BEmailSender` (Callable) on the shared `ThreadPoolExecutor`.

**Request/response fields (invoice/tax)**: UBL `InvoiceType` fields set across `InvoiceUtil` for B2B/B2C/POS/Siebel invoices — `ID`, `UUID`, `IssueDate`/`IssueTime`, `InvoiceTypeCode` (`388`=tax invoice/sale, `381`=credit note/return with `BillingReferenceType`→original invoice), `DocumentCurrencyCode`/`TaxCurrencyCode` (`SAR`), `LineCountNumeric`, ICV/PIH `AdditionalDocumentReference`s, `AccountingSupplierParty`/`AccountingCustomerParty` (VAT/TIN via `PartyIdentificationType schemeID=TIN`, CRN via `schemeID=CRN`), and per line: item SKU/description, quantity, unit price, `TaxCategoryType` (`classifiedTaxCategory.id = "S"` standard-rated when VAT rate > 0, else `"Z"` zero-rated — `util/InvoiceUtil.java:255-263`), line tax amount, line rounding (gross) amount.

**External systems**:

- **ZATCA e-invoicing platform** — `zatca.api.url` (`application.properties`, currently `https://gw-apic-gov.gazt.gov.sa/e-invoicing/core`, commented-out `simulation` alternative), HTTP Basic auth from `zatca.api.binary-token`/`zatca.api.secret` (`config/FeignZatcaAuthConfiguration.java`/used by `IZatcaInterfaceAPI`).

- **BIP (Oracle BI Publisher) report service** — `report.bip.url` (SOAP over Feign, `config/FeignSOAPConfiguration.java`), basic-auth `report.bip.username`/`report.bip.password`.

- **Email microservice** — `email.config.url`, basic-auth `email.config.username`/`email.config.password`, custom headers `X-Request-ID` (`email.config.header.req-id`), `X-Application-ID` (`email.config.header.application-id`) (`config/FeignMessageAuthConfig.java`).

- **ZATCA local hashing/signing SDKs** — `com.gazt.einvoicing.hashing.generation.service.HashingGenerationServiceImpl` and `com.gazt.einvoicing.signing.service.impl.SigningServiceImpl` (bundled JARs under `einvoicing/src/main/resources/zatca-einvoicing-sdk-*.jar`), used for B2C local signing (`B2CSigningService`) and hash generation (`HashingService`) — not a network call, in-process crypto.

- **RMS/OMS Oracle DB** — datasource from `spring.datasource.*` (points at `RMSPRD`).

**Config present**: `einvoicing/src/main/resources/application.properties` contains plaintext production DB credentials, ZATCA production certificate binary token / private key / API secret, BIP report password, and email-service password — all noted above without reproducing the literal secret values. Also defines seller identity constants (`zatca.api.seller.*`: street, building/additional numbers, city, postal code, country `SA`, VAT number, registered name, per-brand CRN map `{Extra: '2051029841', UCFS: '2051224103'}`) and `b2b.pos.stores`/`XX_POS_B2B_EINV`-scoped store id filter.

**Transaction/commit behavior**: almost everything is auto-commit `NamedParameterJdbcTemplate` single statements or batch updates; the sole explicit `@Transactional` is `BaseDAO.persistInvForRecon` (reconciliation header+detail insert). Async job dispatch uses a dedicated `ThreadPoolExecutor` bean (`config/ApplicationConfiguration.java:39-42`, 5 core/max threads) plus a Spring `ThreadPoolTaskExecutor` (`asyncTaskExecutor`, 10 core threads) for the `@Async` service-layer methods — errors inside a per-invoice `Callable`/`Future` are caught and logged, not propagated to fail the whole scheduled run.

---

### oms-einvoicingxml — `POST /api/hybristranxml`

- **Module**: `oms-einvoicingxml` (Spring MVC WAR)

- **Type**: Spring REST

- **Endpoint**: dispatcher mapped to `/api/*` (`src/main/webapp/WEB-INF/web.xml:32-35`), controller path `/hybristranxml` (`controller/XmlController.java:15`) → effective URL `/api/hybristranxml`

- **Entry point**: `XmlController.saveXmlInfo(HybrisOmsXmlRequest)` — `src/main/java/com/extra/oms/einvoicingxml/controller/XmlController.java:23-39`

- **Call chain**: `XmlController` → `HybrisOmsXmlService.saveXMLInfo` (`service/HybrisOmsXmlService.java:15-17`) → `HybrisOMSXmlDAO.saveXmlInfo` (`dao/HybrisOMSXmlDAO.java:23-45`)

- **SQL**: `INSERT INTO XX_EINV_XML_GAZT_REP(INVOICE_NUM, STORE, INVOICE_ORIG_SYS, INVOICE_TYPE, BUSINESS_DATE, XML_GEN, PROCESS_DATE, GAZT_RPT_FLAG, INVOICE_DESC, ERROR_FLAG, ORDER_NO) VALUES(:invoicenumber, :orderRequestorid, 'ECOM', 'B2C', :transactiondate, :generatedxml, null, 'N', :transactiontype, null, :ordNo)` (`dao/HybrisOMSXmlDAO.java:36-37`) — `INVOICE_ORIG_SYS` hardcoded `'ECOM'`, `INVOICE_TYPE` hardcoded `'B2C'`, `GAZT_RPT_FLAG` inserted as `'N'` (not-yet-reported, same downstream table `einvoicing`'s Job 4 later polls).

**Request fields** (`model/HybrisOmsXmlRequest.java`): `order_req_id` (Long, → `STORE` column, despite the name), `invoice_no`, `order_Id` (→ `ORDER_NO`), `tran_type` (→ `INVOICE_DESC`), `tran_date` (`dd-MM-yyyy`, → `BUSINESS_DATE`), `generated_xml` (Base64-encoded XML string, decoded via `Base64Utils.decodeFromString` before insert into `XML_GEN`). No field is annotated required; the DAO will NPE/throw on missing values since there's no null-guarding before `Base64Utils.decodeFromString`.

**Response**: `Response{status: "Success"/"Failed", message}` (`model/Response.java`).

**Business rule**: this is the ingestion point for pre-built ZATCA B2C invoice XML generated by the external Hybris/e-commerce storefront (it does not build or validate UBL XML itself — that's done upstream in Hybris and handed over as a base64 blob) — it lands the XML in the same `XX_EINV_XML_GAZT_REP` staging table that `einvoicing`'s `B2CReportingService`/`B2CInvoicingProcess.reportXML` job later polls (`GAZT_RPT_FLAG='N'`) and reports to ZATCA.

**Config present**: `application.properties` — `rms.datasource.url/username/password` (points at `RMSPRD`, plaintext password), with a commented-out QA alternative.

**Transactions**: single `NamedParameterJdbcTemplate.update`, autocommit; exceptions rethrown from DAO to service to controller, where they're caught and turned into `status="Failed"`.

---

## Cross-module observations

- **No refund package usage in this cluster.** `XX_REFUND_REQUEST_PKG`, `OMSSUB_ASNOUT`, `OMSSUB_RECEIVING`, and `WS_INVOKER` are not referenced anywhere under `PaymentConfirmation`, `VASContract`, `apple-pricing`, `boot-apple-pricing`, `oms-discount`, `oms-finance`, `e-invoice-util`, `einvoicing`, or `oms-einvoicingxml` (verified by repo-wide case-insensitive grep). Payment confirmation in this cluster only ever advances an order to paid/reserved state or rolls back a reservation — it never creates a refund record.

- **Two generations of architecture side by side**: `PaymentConfirmation`/`VASContract` are legacy JAX-WS SOAP WARs on stateless-session-bean/JPA persistence with direct JDBC scattered in; `apple-pricing`/`oms-discount`/`oms-finance`/`oms-einvoicingxml` are Spring MVC/JDBC-template WARs; `boot-apple-pricing`/`einvoicing`/`e-invoice-util` are Spring Boot with Feign clients — a visible chronological migration path within the same business domain.

- **E-invoicing staging-table convergence**: three different producers (`einvoicing`'s B2C/Siebel jobs, `oms-einvoicingxml`'s Hybris ingestion endpoint) write into the same `XX_EINV_XML_GAZT_REP` table, which `einvoicing`'s `reportB2CInvoice` job later drains — this is the de facto integration contract between the OMS/Hybris side and the ZATCA reporting side.

- **Widespread plaintext secrets in `application.properties`/`@Value` defaults** across `apple-pricing`/`boot-apple-pricing` (DB creds, Apple API secret), `oms-finance` (SFTP creds — two different hardcoded sets), `einvoicing` (DB creds, ZATCA production private key/certificate/secret, BIP password, email-service password), and `oms-einvoicingxml` (DB creds). None of the literal values are reproduced in this report.

---

## Summary table

| 

API 
| Method/Operation 
| Java Entry Point 
| Java Service 
| DAO/DB Layer 
| PL/SQL or SQL 
| Main Tables 
| Purpose 
|

| PaymentConfirmation 
| SOAP `processPaymentConf` 
| `PaymentConfirmationWebServiceImpl.processPaymentConf` 
| `OMSCustomerOrderBean`, `ExtSystemUpdate`, `InterfacePersistence`, `RMSPackage` 
| JPA via `OMSUtilSessionEJB`; raw JDBC in `PersistPaymentRequest`/`RMSPackage`/`OMSPersistence` 
| `{call OMS_INVADJ_STATUS_UNAVIALINV(...)}` + JPA merges 
| `OMS_CUST_ORD_HEAD`, `OMS_CUST_ORD_RESERVE`, `OMS_CUST_ORD_ITEM`, `OMS_CUST_ORD_TENDER`, `Oms_PaymentConf_Audit`, `OmsPaymentSync`, `OmsRtlogPublishLog` 
| Confirm payment for a reserved order, unreserve/adjust RMS inventory, notify RMS/SIM/Siebel 
|

| VASContract 
| SOAP `processVASContract` 
| `VASContractWebServiceImpl.processVASContract` 
| (none — inline) 
| direct JDBC 
| `INSERT INTO VAS_CONTRACTS_ECOM(...)` 
| `VAS_CONTRACTS_ECOM` 
| Record a VAS/warranty service contract sold with an item 
|

| SIMDeliveryDetail 
| SOAP `processNewOrder` 
| `SIMDeliveryDetailWebServiceImpl.processNewOrder` 
| `SIMDeliveryDetailBean`, `SIMDeliveryDetailCommon` 
| raw JDBC (SIM + OMS connections, `@rmsdb` DB link) 
| ready-to-ship/undelivered SELECT joins, `ordcust`/`Ful_Ord_Dlv` UPDATEs 
| `Ful_Ord`, `Ful_Ord_Dlv`, `Ful_Ord_Line_Item`, `OMS_CUST_ORD_HEAD@rmsdb`, `oms_co_fulfill_detail@rmsdb`, `ordcust`, `xx_vxref_carrier_city` 
| Feed SIM with ready-to-ship/undelivered order+delivery detail 
|

| apple-pricing / boot-apple-pricing 
| REST `GET /pricing` 
| `PricingController.updatePricingDetail` 
| `PricingService`, `AsyncAPIService`, `AppleAPIService` (Feign) 
| `PricingDAO` (`NamedParameterJdbcTemplate`) 
| `{ CALL XX_APPLE_ON_PRICING_SQL.APPLE_MPN_REFRESH(?,?,?,?) }` 
| `XX_APPLE_STORE_PS_DETAIL` 
| Refresh active Apple MPN list from Apple pricing feed 
|

| apple-pricing / boot-apple-pricing 
| REST `POST /pricing` 
| `PricingController.publishPricingDetail` 
| `PricingService`, `AsyncAPIService`, `AppleAPIService` (Feign) 
| `PricingDAO` 
| `{ CALL XX_APPLE_ON_PRICING_SQL.POST_PUBLISH_UPDATE(?,?,?) }` 
| `XX_APPLE_STORE_ITEM_PRICES`, `XX_APPLE_STORE_PS_DETAIL` 
| Publish updated Apple price sheets to Apple and mark them published 
|

| oms-discount 
| REST `POST /api/discount` 
| `DiscountController.saveDiscountInfo` 
| `DiscountService` 
| `DiscountDAO` (`NamedParameterJdbcTemplate`, batch insert) 
| `INSERT INTO XX_PMP_INV_DETAILS(...)` 
| `XX_PMP_INV_DETAILS`, `OMS_CUST_ORD_HEAD` 
| Persist PMP/promotional discount detail against an OMS order 
|

| oms-finance 
| scheduled job (`checkOrderStatus`, `fixedDelay=1000`) 
| `OrderService.checkOrderStatus` 
| `OrderService`, `FTPService` 
| `OrderDAO` 
| `SELECT ... WHERE CONTRACT_FLAG='N'` / `UPDATE ... SET CONTRACT_FLAG='Y'` 
| `OMS_CUST_ORD_HEAD` 
| Mark orders as contract-confirmed once signed PDF is found on SFTP 
|

| einvoicing 
| scheduled job `generateEBSInvoice` (cron `schedule.ebs.cron`) 
| `EInvoicingService.generateEBSInvoice` 
| `B2BInvoicingService`→`B2BInvoicingProcess`→`B2BInvoiceProcessor` 
| `B2BInvoicingDAO` 
| select/insert/update on report tables + Feign `IZatcaInterfaceAPI.clearanceApi` 
| `XX_B2B_EBS_EINV_REP`, `XX_EINV_XML_GAZT_REP` 
| Clear EBS-sourced B2B tax invoices/credit notes through ZATCA and email PDF 
|

| einvoicing 
| scheduled job `generateB2BPOSInvoice` (cron `schedule.pos.cron`) 
| `EInvoicingService.generateB2BPOSInvoice` 
| `B2BInvoicingService`→`B2BInvoicingProcess`→`B2BInvoiceProcessor` 
| `B2BInvoicingDAO` 
| POS join SELECT + `IZatcaInterfaceAPI.clearanceApi` 
| `XX_POS_B2B_EINV`, `SA_TRAN_HEAD/ITEM/IGTAX/DISC`, `ITEM_MASTER` 
| Clear POS-sourced B2B invoices through ZATCA and email PDF 
|

| einvoicing 
| scheduled job `generateB2CXml` (cron `schedule.canret.cron`) 
| `EInvoicingService.generateB2CXml` 
| `B2CXmlGenerationService`→`B2CInvoicingProcess` 
| `B2CInvoicingDAO` 
| UNION SELECT + local ZATCA signing (`B2CSigningService`) 
| `XX_EINV_CAN_DETAILS_CLR_V`, `XX_EINV_RMA_DETAILS_CLR_V`, `XX_EINV_CAN_RET_OMS`, `XX_EINV_XML_GAZT_REP` 
| Build & sign B2C cancellation/return credit-note XML 
|

| einvoicing 
| scheduled job `reportB2CInvoice` (cron `schedule.report.cron`) 
| `EInvoicingService.reportB2CInvoice` 
| `B2CReportingService`→`B2CInvoicingProcess` 
| `B2CInvoicingDAO`, `BaseDAO` (`@Transactional` recon insert) 
| SELECT signed XML + VAT cross-check + `IZatcaInterfaceAPI.reportingApi` 
| `XX_EINV_XML_GAZT_REP`, `VAT_ITEM`, `XX_EINV_RECONCIL_HEAD/DETAIL` 
| Report signed B2C invoices to ZATCA with VAT/amount validation 
|

| einvoicing 
| scheduled job `generateSiebelInvoice` (cron `schedule.siebel.cron`) 
| `EInvoicingService.generateSiebelInvoice` 
| `B2CXmlGenerationService`→`B2CInvoicingProcess` 
| `B2CInvoicingDAO` 
| SELECT + local ZATCA signing 
| `XX_EINV_SPARE_PART_STG`, `XX_EINV_STORE_CRN`, `XX_EINV_XML_GAZT_REP` 
| Build & sign B2C spare-parts/service invoices from Siebel 
|

| einvoicing 
| scheduled job `resendEmail` (cron `schedule.email.cron`) 
| `EInvoicingService.resendEmail` 
| `B2BInvoicingService`→`B2BInvoicingProcess`→`B2BEmailSender` 
| `B2BInvoicingDAO` 
| SELECT failed-email invoices + `IEmailService.sendEmail` 
| `XX_B2B_EBS_EINV_REP`, `XX_POS_B2B_EINV`, `XX_EINV_XML_GAZT_REP` 
| Retry sending cleared-invoice PDF emails 
|

| oms-einvoicingxml 
| REST `POST /api/hybristranxml` 
| `XmlController.saveXmlInfo` 
| `HybrisOmsXmlService` 
| `HybrisOMSXmlDAO` (`NamedParameterJdbcTemplate`) 
| `INSERT INTO XX_EINV_XML_GAZT_REP(...)` 
| `XX_EINV_XML_GAZT_REP` 
| Ingest pre-built ZATCA B2C invoice XML from Hybris storefront for later ZATCA reporting 
|

