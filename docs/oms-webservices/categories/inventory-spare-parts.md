## Report: Inventory / Spare Parts / Stock Business Cluster

### 1. Stockcheck REST — `POST /inventory` (lookUpAvailableInventory)

**Module**: Stockcheck (Spring MVC, non-JAX-WS). **Endpoint**: `POST /inventory`, defined via `@RestController`, mapped by `DispatcherServlet` at `/` per `Stockcheck/src/main/webapp/WEB-INF/web.xml:7-15`, config `Stockcheck/src/main/webapp/WEB-INF/spring-servlet.xml`.

- **Entry point**: `StockcheckController.lookUpAvailableInventory` — `Stockcheck/src/main/java/com/extra/restservice/controller/StockcheckController.java:22-41`. Validates `request.getItems()`/`request.getLocations()` non-empty, else returns `Status{code=F}`.
- **Service**: `LookUpInventoryServiceImpl.lookupInventory` — `.../controller/LookUpInventoryServiceImpl.java:37-108`. Buckets request items/locations by physical-stock flag, pack-item flag, and store(S)/warehouse(W) type, then dispatches to 4 sub-methods.
- **DB access** (all via `NamedParameterJdbcTemplate`, three JNDI datasources wired in `spring-servlet.xml:24-46`: `omsJdbcTemplate`→`jdbc/oms`, `dasJdbcTemplate`→`jdbc/xtradas` (unused in code), `simJdbcTemplate`→`jdbc/sim`):
  - `checkStoreInventoryQuantity` (line 110-150): `SELECT ITEM_ID, STORE_ID, SIM_AVAIL_TO_SELL AVAIL_QTY FROM XX_OMS_INV WHERE ITEM_ID IN (:items) AND STORE_ID IN (:locations) AND SOURCE='E-COMMERCE'` on `simJdbcTemplate`; then per-row calls `getUnFulFilledQtyFromOmsBackOrderDTLTable` (line 280) which runs `SELECT SUM(SOURCE_QTY-FULFILL_QTY) ... FROM OMS_BACK_ORDER_DTL WHERE BACKORDER_STATUS='N' AND SOURCE_QTY>FULFILL_QTY AND ITEM=... AND SOURCE_LOC=...` on `omsJdbcTemplate`, and subtracts unfulfilled backorder qty from available qty (floor 0).
  - `checkWHInventoryQuantity` (line 152-179): `SELECT ITEM, LOC, LOC_TYPE, AVAIL_QTY_AFTER_BACK_ORDER FROM XX_OMS_INVAVAIL_V_BACK WHERE ITEM IN (:items) AND LOC IN (:locations)` on `omsJdbcTemplate`.
  - `getPackItemInventoryQuantity` (line 188-233): `SELECT * FROM GET_PACK_QTY_V WHERE PACK_NO IN (:itm) AND LOC IN (:location)` on `omsJdbcTemplate`.
  - `callFutureInventoryQuery` (line 235-278): `SELECT ITEM, LOCATION, LOC_TYPE, BACK_ORDER_IND, AVAIL_QTY FROM V_CUST_FUTURE_INV_ECOM_FEED_T WHERE ITEM IN (:itm) AND LOCATION IN (:location)`; qty only returned when `BACK_ORDER_IND='Y'`.
- **Response fields** (`ItemAvailability`): `item`, `location`, `locationType` (S/W), `channel_id` (warehouse only), `availableQuantity`, `unitOfMeasure` (hardcoded "EA"), `packCalculateIndicator`.
- **Request fields** (`RealTimeInventoryRequest`): `items[]` (code, physicalStockInd, packItemInd), `locations[]` (code, type, channelId, physicalStockInd, packItemInd), `storePickup` (unused). Items and locations lists mandatory.
- **Business rules**: ATS reduced by unfulfilled backorder quantity for store items; qty floored at zero; future-inventory items only surfaced when back-ordered; separate query paths for pack (bundle) items vs single SKUs.
- **External systems**: RMS/OMS custom views via direct JDBC — no outbound web-service calls.
- **Transactions**: none explicit; read-only.
- **Dead/vestigial code**: Axis 1.4-generated JAX-RPC skeletons `InventoryDetailPortBindingImpl`/`StoreInventoryPortBindingImpl` — every method returns null; non-functional stubs, not live endpoints.

---

### 2. Stock_Feed REST — `POST /physicaldeltafeed`, `POST /futureinventoryfeed`, `POST /packitemfeed`

**Module**: Stock_Feed (Spring MVC). Root-mapped `DispatcherServlet`.

- **Entry point**: `StockFeedController` — `Stock_Feed/src/main/java/org/logicinfo/stockfeed/controller/StockFeedController.java`.
  - `getPhysicalStockDeltaFeedResponse` (23-38): validates `physicalStockInd='Y'`, `locType` in {WH,ST}, calls service, always persists request/response.
  - `getFutureInventoryFeedResponse` (40-56): validates `physicalStockInd='N'`, calls service, persists.
  - `getPackItemFeedData` (58-61): requires `packItemInd='Y'`.
- **DAO**: `StockFeedDaoImpl` (`.../daoImpl/StockFeedDaoImpl.java`). Raw JDBC via JNDI: `jdbc/xtradas`, `jdbc/sim`, `jdbc/oms`.
  - Physical delta (WH): `select ITEM,LOC location,AVAIL_QTY from XX_TEST_INVAVAIL_V where GREATEST(LAST_UPDATE_DATETIME, NVL(SOH_UPDATE_DATETIME,LAST_UPDATE_DATETIME)) > (SELECT MAX(CREATE_DATETIME) FROM STOCK_FEED WHERE STATUS='Success' AND REQUEST_TYPE='PhysicalDeltaFeed-WH') and loc NOT IN (1088,1098) and loc_type='W'`.
  - Physical delta (ST): `select ITEM_ID ITEM,location,AVAIL_QTY from TEST_ITEM_STOCK_DTL_V WHERE LAST_UPDATE_DATETIME >= (SELECT MAX(CREATE_DATETIME) FROM STOCK_FEED@rmsdb WHERE STATUS='Success' AND REQUEST_TYPE='PhysicalDeltaFeed-ST') AND LOCATION NOT IN (19008,19009)` — cross-DB link `@rmsdb`.
  - Future inventory: `select * from V_CUST_FUTURE_INV_ECOM_FEED_B where location NOT IN (1098,19008,19009)`.
  - Pack item: `SELECT * FROM GET_PACK_QTY_V`.
  - Every call persists to `STOCK_FEED (id,response,CREATE_DATETIME,STATUS,Request_Type)` on OMS connection.
- **Response fields** (`StockFeedResponseModel`): `status`, `error`, `feed[]{productCode, stock[]{location, quantity}}`.
- **Business rules**: delta feeds only return rows changed since last successful run of the same feed type (incremental watermark); specific test/dummy locations hard-excluded.
- **Transactions**: each call opens/closes own connection; failures caught/logged, no rollback.

---

### 3. OmsOrposInventoryCheck — SOAP `checkInventory` (legacy)

**Module**: OmsOrposInventoryCheck. JAX-WS `@WebService`, WSDL `OmsOrposInventoryCheckWebservice.wsdl`, URL pattern `/OmsOrposInventoryCheckWebService`.

- **Entry point**: `OmsOrposInventoryCheckWebServiceImpl.checkInventory` (`:31-39`) → `InventoryCheckBean.checkInventory` (`:36-551`).
- Looks up item dept/inventory-indicator via EJB (`OMSUtilSessionEJBBean`), resolves fulfillment "combination id", iterates priority locations from `OMS_FULFILL_MATRIX_EXT_DETAIL` (`FindNextFulfillLoc.processFulfillmentMatrix`).
- `InterfacePersistence.callSIMStoreInventory` — outbound SOAP to SIM `StoreInventoryService.lookupInventoryInStore`.
- `InterfacePersistence.findWHInventory` → `OMSUtilCommons.checkSOHForWH`: `select item, sum(AVAIL_QTY_AFTER_BACK_ORDER) from XX_OMS_INVAVAIL_V_BACK where item=... and loc IN(...) group by item` (E-COMMERCE) or `XX_OMS_POS_INVAVAIL_V` (POS).
- `InterfacePersistence.backOrder` → `FindNextFulfillLoc.findFutInvDateAndQty`: `select * from V_CUST_FUTURE_INV_POSITION where item=? and location=? order by expected_date` on DAS schema.
- `FindNextFulfillLoc.findItemStatus`: `select status from item_loc where item=? and loc=?`.
- **Business rules**: allocates SOH cumulatively across priority-ordered fulfillment locations; supplier (SU) location triggers item-status check + back-order/future-availability lookup; throws `SOAPFaultException("UNAVL_INV")` when no more fulfillment locations exist.

---

### 4. OmsOrposInventoryCheckNew — SOAP `checkInventory` / `refreshParams` (rewrite)

**Module**: OmsOrposInventoryCheckNew. Same WSDL contract/namespace/URL pattern.

- `checkInventory` (:42-61) delegates to `InventoryCheckService.getStockAvailability` (rewritten, batched, no per-item roundtrips); catches all exceptions and returns `OMS_ORPOS_ERROR_102` response rather than faulting.
- `refreshParams` (:63-70) hot-refreshes cached system-parameter thresholds via `OmsSysParameterUtil.reload`.
- **Service**: `InventoryCheckService.getStockAvailability` (`:43-417`) — computes country code from first digit of `initiateLocId`, item classification (SMALL/BIG/PRE ORDER) via `DataAccessDAO.getShipClassification`, wall-bracket linked-SKU overrides, promise-delivery dates (14:00 cutoff, Friday-aware), applies per-classification/location-type stock thresholds.
- **DAO** (`DataAccessDAO`, raw JDBC): `SELECT ITEM,DEPT,STATUS,INVENTORY_IND FROM ITEM_MASTER`; `GET_CLASSIFICATION_BO_TEST(?,?)` stored function via `SELECT ... FROM DUAL` with Oracle array/struct types; `OMS_CUST_ORDER_DELV_DATE`, `OMS_CUST_ORDER_DLT`; `OMS_FULFILL_MATRIX_EXT_HEAD/DETAIL`; `XX_OMS_POS_INV_V` (SIM schema, ATS-minus-unfulfilled-BO); `XX_OMS_POS_INVAVAIL_V`; `V_CUST_FUTURE_INV_ECOM_FEED_P`; `XX_WALL_BRACKET_SKU`; `ITEM_LOC_TRAITS`; `WH`; `V_CUST_FUTURE_INV_POSITION` (DAS); `OMS_BACK_ORDER_DTL`.
- **Business rules**: threshold-based ATS netting per classification/location type using country-and-classification-keyed system parameters; pre-order items draw from separate future-inventory pool and set `inTransitQty=1`; express delivery cutoff logic skips Fridays.
- **External systems**: pure DB-side computation — no outbound SOAP in this rewritten version (unlike legacy).

---

### 5. spareParts-stockRequest REST — 4 endpoints under `/api/*`

**Module**: spareParts-stockRequest. Controller `StockRequestController`.

| Path | Method | Service method |
|---|---|---|
| `/api/stock-return-request` | POST | `SparePartsService.processRequest` |
| `/api/stock-receiving` | POST | `SparePartsService.processReceiving` |
| `/api/stock-deducting` | POST | `SparePartsService.stockDeducting` |
| `/api/stock-cancellation` | POST | `SparePartsService.stockCancellation` |

- `processRequest`: logs raw JSON to `XX_SP_LOG_INFO`, branches Stock Request vs Return, compares available vs requested qty, persists via `xx_sp_stock_req_ret[_item]`.
- `processReceiving`: checks/creates `xx_sp_tsf_req_rec` row, calls SIM inventory adjustment twice (non-sellable→available, then available→tech sub-bucket).
- `stockDeducting`: persists `xx_spare_parts_hdr/item`, checks tech-bucket stock (`AVAIL_TO_TECH ≥` requested), two SIM adjustment calls.
- `stockCancellation`: branches on cancel status (P/C-null/N/R), calls one of 3 SIM adjustment variants.
- All SIM calls via Feign SOAP client `IOracleSIMClient.saveAndConfirmInventoryAdjustment`, target `sim.oralce.base.api.url` = `http://prodsimapp.extrastores.com:7511`.
- **DAO** (`SparePartsDAO`): `XX_OMS_INVAVAIL_V UNION GET_PACK_QTY_V` for stock-request check; `XX_TECH_SUB_AVAIL_V@simdb` for return check; `xx_sp_stock_req_ret_item`, `xx_spare_parts_hdr/item`, `XX_SP_LOG_INFO`, `XX_TECH_REAS_CODE_V@simdb`.
- **Business rules**: stock request approved only if WH-style ATS ≥ requested; return approved only if store tech-sub-bucket stock ≥ requested AND requested+unapproved-pending ≤ available; deduction requires tech-bucket qty ≥ requested; cancellation maps 4 status codes to distinct SIM adjustment call pairs.
- **Transactions**: several DAO methods `@Transactional` (real `DataSourceTransactionManager` configured) but no rollback of SIM-side effects if a later DB step fails.

---

### 6. SparePartsRequest — SOAP `SparePartsRequestOperation`

**Module**: SparePartsRequest. JAX-WS, `SparePartsRequestService`/`SparePartsRequestPort`.

- `performBasicValidation`: rejects duplicate `(ServiceRequestId, SequenceId)` in `OMS_SPARE_PART_HEADER`; validates item status; resolves fulfillment combination id for `SPARE`/`C`; simulates cumulative SOH across priority locations, optionally seeded by technician sub-bucket qty (`SELECT AVAIL_TECH_SUB FROM XX_TECH_SUB_AVAIL_V WHERE ITEM_ID=? AND STORE_ID=? AND TECH_SUB_BUCKET=?` on SIM connection).
- `createSparePartsHeaderEntry`: EJB persist → `OMS_SPARE_PART_HEADER`.
- `processSparePartsFulfillment`: priority-1 location → SIM inventory-adjustment (reservation); subsequent priorities → SIM store-to-store-transfer request, optionally auto-confirmed. Persists `OMS_SPARE_PART_FULFILL`, `OMS_SPARE_PART_AUDIT` each iteration. On `SOAPException`, `SparePartsRequestReversalBean.reverseSuccessfulTransactions` performs compensating-transaction rollback (re-adjusts inventory / cancels SIM transfer, zeroes reserved qty, writes `QUANTITY_REJECTED` audit).
- **Business rules**: greedy fulfillment across priority-ordered locations; sub-bucket stock consumed first.

---

### 7. SparePartsConfirmation — SOAP `SparePartsConfirmationOperation`

**Module**: SparePartsConfirmation. JAX-WS, `SparePartsConfirmationService`/`SparePartsConfirmationPort`.

- `performBasicValidation`: rejects duplicate `(ServiceRequestId, ServiceConfirmationId)`.
- `validateGetReasonCodeId`: `SELECT id FROM INV_ADJUST_REASON WHERE code=?` on SIM schema.
- `createSparePartsConfirmationHeader`: inserts `OMS_SPARE_PART_CONFIRM_HDR` (status `CL`).
- `processAndCreateSparePartsConfirmationDetails` → `updateSIMandSparePartsRequestTable`: persists `OMS_SPARE_PART_CONFIRM_DTL`; calls SIM inventory adjustment twice (un-reserve, then deduct). On SIM failure, inserts async-retry record into `OMS_REPUBLISH_DATA` (web service id resolved via `OMS_WEBSERVICE_URI_DETAIL WHERE WEB_SERVICE_NAME='SIM_INVENTORY_ADJ'`) rather than failing outright. Updates `OMS_SPARE_PART_FULFILL`/`OMS_SPARE_PART_HEADER` (decrements pending qty, closes header at zero).
- **Reversal**: `SparePartsConfirmationReversalBean` — compensating SIM calls + audit row on later DB-step failure.
- **Business rules**: confirmation qty cannot exceed pending or cumulative-reserved qty; only fulfill records for the confirming store are updated; header auto-closes at pending qty = 0.

---

### Cross-cutting notes

- None of the 7 modules call `OMS_RECEIPT_CO_SIM_WS_INVOKER`, `OMS_RECEIPT_CO_WS_INVOKER`, `OMS_RECEIPT_SIM_INVADJ_INVOKER`, `OMSSUB_ASNOUT`, or `OMSSUB_RECEIVING` (zero grep hits) — those belong to the RIB-subscriber layer; the only shared touchpoint is the `OMS_SPARE_PART_*` tables.
- "ORPOS" = the literal `applicationId="ORPOS"` string passed to shared SOH/backorder lookups to select POS-branch views vs e-commerce-branch views — identifies the in-store Oracle Retail Point-of-Service system as caller.
- Legacy SOAP modules (OmsOrposInventoryCheck, SparePartsRequest, SparePartsConfirmation) share `OMSUtilSessionEJBBean` (JPA) as their DB layer; `OmsOrposInventoryCheckNew` and `spareParts-stockRequest` diverge with hand-rolled JDBC DAOs.

---

### Summary Table

| API | Method/Operation | Java Entry Point | Java Service | DAO/DB Layer | PL/SQL or SQL | Main Tables | Purpose |
|---|---|---|---|---|---|---|---|
| Stockcheck | POST `/inventory` | `StockcheckController.lookUpAvailableInventory` | `LookUpInventoryServiceImpl.lookupInventory` | Same class, `NamedParameterJdbcTemplate` | Raw SQL SELECTs | `XX_OMS_INV`, `XX_OMS_INVAVAIL_V_BACK`, `GET_PACK_QTY_V`, `V_CUST_FUTURE_INV_ECOM_FEED_T`, `OMS_BACK_ORDER_DTL` | Real-time ATS check for e-commerce |
| Stock_Feed | POST `/physicaldeltafeed`, `/futureinventoryfeed`, `/packitemfeed` | `StockFeedController` | `StockFeedServiceImpl` | `StockFeedDaoImpl` (raw JDBC) | Raw SQL | `TEST_ITEM_STOCK_DTL_V`, `XX_TEST_INVAVAIL_V`, `V_CUST_FUTURE_INV_ECOM_FEED_B`, `GET_PACK_QTY_V`, `STOCK_FEED` | Delta/future/pack-item stock feed to downstream consumers |
| OmsOrposInventoryCheck | SOAP `checkInventory` | `OmsOrposInventoryCheckWebServiceImpl.checkInventory` | `InventoryCheckBean.checkInventory` | `InterfacePersistence`/`FindNextFulfillLoc` + shared EJB (JPA) | JPA + raw JDBC + SOAP to SIM `StoreInventoryService` | `OMS_FULFILL_MATRIX_EXT_DETAIL`, `XX_OMS_INVAVAIL_V_BACK`, `XX_OMS_POS_INVAVAIL_V`, `V_CUST_FUTURE_INV_POSITION`, `ITEM_LOC` | ORPOS inventory availability check (legacy) |
| OmsOrposInventoryCheckNew | SOAP `checkInventory`, `refreshParams` | `OmsOrposInventoryCheckWebServiceImpl` | `InventoryCheckService.getStockAvailability` | `DataAccessDAO` (raw JDBC, batched) | Raw SQL + Oracle stored function `GET_CLASSIFICATION_BO_TEST` | `ITEM_MASTER`, `OMS_FULFILL_MATRIX_EXT_HEAD/DETAIL`, `XX_OMS_POS_INV_V`, `XX_OMS_POS_INVAVAIL_V`, `V_CUST_FUTURE_INV_ECOM_FEED_P`, `V_CUST_FUTURE_INV_POSITION`, `OMS_BACK_ORDER_DTL`, `XX_WALL_BRACKET_SKU` | Rewritten batched ORPOS inventory check with classification/threshold logic |
| spareParts-stockRequest | POST `/api/stock-return-request`, `/stock-receiving`, `/stock-deducting`, `/stock-cancellation` | `StockRequestController` | `SparePartsService` | `SparePartsDAO` (`NamedParameterJdbcTemplate`) | Raw SQL + SOAP via Feign `IOracleSIMClient` | `xx_sp_stock_req_ret[_item]`, `xx_spare_parts_hdr/item/cancel`, `XX_TECH_SUB_AVAIL_V@simdb`, `XX_TECH_REAS_CODE_V@simdb`, `XX_SP_LOG_INFO` | Technician spare-parts stock request/return/receive/deduct/cancel |
| SparePartsRequest | SOAP `SparePartsRequestOperation` | `SparePartsRequestImpl.sparePartsRequestOperation` | `SparePartsRequestBean` | Shared EJB (JPA) + raw JDBC | JPA + SOAP to SIM `InventoryAdjustmentService`/`StoreToStoreTransferService` | `OMS_SPARE_PART_HEADER`, `OMS_SPARE_PART_FULFILL`, `OMS_SPARE_PART_AUDIT`, `OMS_FULFILL_MATRIX_EXT_DETAIL`, `XX_TECH_SUB_AVAIL_V` | Reserve/transfer inventory for a spare-parts service request |
| SparePartsConfirmation | SOAP `SparePartsConfirmationOperation` | `SparePartsConfirmationPortTypeImpl.sparePartsConfirmationOperation` | `SparePartsConfirmationBean` | Shared EJB (JPA) + raw JDBC | JPA + SOAP to SIM `InventoryAdjustmentService` (+ async republish) | `OMS_SPARE_PART_CONFIRM_HDR/DTL`, `OMS_SPARE_PART_HEADER`, `OMS_SPARE_PART_FULFILL`, `OMS_SPARE_PART_AUDIT`, `OMS_REPUBLISH_DATA`, `INV_ADJUST_REASON` | Confirm/deduct reserved spare-parts inventory once consumed |
