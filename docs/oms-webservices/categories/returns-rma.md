# RMA Generation / Modification / Deletion — Technical Trace Report

Scope: `/home/user/websvcs/oms/CODE/OMSRMAGeneration`, `OMSRMAModification`, `OMSRMADeletion`. All three are legacy JAX-WS SOAP servlets (`javax.jws.WebService`, `@BindingType(SOAP12HTTP_BINDING)`, `parameterStyle = BARE`), each deployed as its own WAR with a single servlet mapping in `web.xml`. Business logic is delegated to a shared remote stateless EJB, `OMSUtilSessionEJB` (project `OMSUtil`, package `com.logicinfo.oms.ejb`), looked up via WebLogic JNDI (`OMSUtil-OMSUtil-OMSUtilSessionEJB`). All persistence through that EJB is JPA (EclipseLink `EntityManager.persist/merge/createNamedQuery`) — **no PL/SQL stored procedures are called for RMA header/detail persistence**; the only raw-SQL/PL/SQL activity found is two direct-JDBC helpers described below. Outbound integration is exclusively to the Oracle Retail **RWMS** (Warehouse Management, not RMS) `PendingReturnsService` SOAP web service; no Siebel/SOA status-update call, `OMS_PENDRETURN_WS_INVOKER`, or `XX_REFUND_REQUEST_PKG` is invoked from any of these three modules (see item 10).

---

### RMA Generation — `generateNewRMA`

- **Module**: OMSRMAGeneration. **SOAP operation**: `generateNewRMA`. **Endpoint path**: `/OMSRMAGenerationWebService` (`OMSRMAGeneration/public_html/WEB-INF/web.xml:20-28`). **WSDL**: `OMSRMAGeneration/public_html/WEB-INF/wsdl/OMSRMAGenerationWebservice.wsdl`, SOAP 1.2, namespace `http://com.logicinfo.oms/model/`, `soap12:address location=""` (unbound placeholder, filled by container at deploy).
- **Entry point**: `com.logicinfo.oms.model.RMAGenerationWebServiceImpl.generateNewRMA(CustomerOrderRMA)` — `OMSRMAGeneration/src/com/logicinfo/oms/model/RMAGenerationWebServiceImpl.java:47-71`.
- **Call chain**:
  1. Servlet endpoint (above) → `new RMAGenerationBean()` → `rmsGenerationBean.validate(input)` (`RMAGenerationBean.java:98`)
  2. `rmsGenerationBean.checkDuplicate(input)` (`RMAGenerationBean.java:68`)
  3. `rmsGenerationBean.saveRMA(input)` (`RMAGenerationBean.java:282`)
  4. `rmsGenerationBean.saveRMAItems(input)` (`RMAGenerationBean.java:315`)
  5. `rmsGenerationBean.callRWMSWebService(input, requestStoreId)` (`RMAGenerationBean.java:361`), which also calls `saveJoodTranscation` (`RMAGenerationBean.java:587`)
  6. `rmsGenerationBean.createResponse(...)` (`RMAGenerationBean.java:705`)
- **DB access** (all via remote EJB `OMSUtilSessionEJB`, JPA, no stored procs):
  - `session.getOmsRmaReqFindRmaId` → JPQL named query `OmsRmaReq.findRmaId` on `OMS_RMA_REQ` (`OMSUtilSessionEJBBean.java:1311`)
  - `session.getVWhFindPhyWH` → `VWh.findPhyWH` (`:1352`)
  - `session.getOmsCustOrdHeadFindByExternalCustOrdNo` (`:68`), `getOmsCustOrdHeadFindByOmsCustOrdNo` (`:702`)
  - `session.getOmsCustOrdItemFindByItem` (`:616`), `getOmsCustOrdItemFindDeilverdQuantity` (`:1339`)
  - `session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId` (`:2295`), `getOmsOrposCustOrdItmRtnFindByOmsOrposCustOrdIdAndLineItemNo` (`:2971`)
  - `session.persistOmsRmaReq` → `em.persist` into `OMS_RMA_REQ` (`:1289`); `session.persistOmsRmaReqItem` → `em.persist` into `OMS_RMA_REQ_ITEM` (`:1259`)
  - `session.getOmsSystemParametersFindByParameterId("RMA_REQ")` (`:461`) — reads `REASON_CODE`/`REASON_DESCRIPTION`/`ACTION_CODE` config
  - `session.mergeOmsRmaReq` (`:1294`), `session.mergeOmsRmaReqItem` (`:1264`), `session.mergeOmsCustOrdItem` (`:589`)
  - **Direct JDBC, non-EJB**: `RMAGenerationBean.getInventoryInd(item)` — `SELECT inventory_ind FROM item_master WHERE item = ?` over `OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING)` i.e. JNDI datasource `jdbc/oms` (`RMAGenerationBean.java:769-808`, query at `:773`).
  - **PL/SQL stored procedure call** (only one in this module): `RMAGenerationBean.saveJoodTranscation(input, reqStoreId)` (`RMAGenerationBean.java:587-703`) — first runs a raw SELECT (`SELECT_JOOD_ORD_TRAN_SQL`, constant `RMAGenerationBean.java:59-64`) against `XX_JOOD_TRANSACTIONS_HEAD`/`XX_JOOD_TRANSACTIONS_DTL`/`XX_JOOD_MEMBERSHIP`, then calls `{ CALL XX_JOOD_MEM_TRANSACTION.XX_JOOD_TRANSACTION_PROCESS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }` (21 params: memberShipId, applicationId, "RETURN", returnDate, custOrderNo×3, retail, discount, joodDiscount, selling, totalRewardsCb, totalRedeemedCb, `XX_JOOD_TRANDTL_TBL` array, "N", joodProgram, 5 OUT params) — `RMAGenerationBean.java:663-694`. This is a JOOD loyalty-program transaction feed, unrelated to RMS/RWMS RMA processing. (An earlier, now commented-out, 16-param version of the same call exists at `:494-585`.)
- **External system**: Oracle Retail RWMS `PendingReturnsService`, operation `pendReturnDtlCreate` (`PendingReturnsPortType.pendReturnDtlCreate(PendRtrnDesc)`, called `RMAGenerationBean.java:425`) — skipped when the item's `inventory_ind = 'N'` (non-inventory item). Target WSDL/endpoint URL resolved dynamically at runtime by `OMSUtilCommons.getWebServiceURL("RWMS_PENDING_RETURNS")` (`OMSUtil/src/.../PendingReturnsService.java:35`) → `session.getOmsWebserviceUriDetailFindByWebserviceName` → JPA query on `OMS_WEBSERVICE_URI_DETAIL` (`OMSUtilSessionEJBBean.java:1234-1236`); a hardcoded fallback dev URL `http://licrpap14-extra.logicindia.com:17053/PendingReturnsBean/PendingReturnsService?wsdl` appears in the generated stub's `@WebServiceClient` annotation and in `jax-ws-catalog.xml`. On `IllegalArgumentWSFaultException`/`IllegalStateWSFaultException`/`ValidationWSFaultException` the bean retries up to 2 times (`maxNoOfRetry`), else marks `OMS_RMA_REQ.STATUS='F'` (`RMAGenerationBean.java:458-491`).
- **Request fields (mandatory unless noted, from `OMSRMAGenerationV1.xsd`)**: `entity_id` (1-30 chars), `application_id` (enum SIEBEL_CRM/E-COMMERCE/ORPOS), `comments` (optional, ≤240), `request_datetimestamp`, `customer_order_no` (≤46), `sub_customer_order_no` (optional, ≤3, defaults to "1" in code), `rma_request_id` (1-16), `physical_wh` (unsigned int &gt;0), `return_date`, `refund_preference` (enum CLEARING/ORPOS), `refund_amount` (decimal), `reason_code` (optional int, default 0), `reason` (optional string), `CustomerOrderRMAItem[1..100]` each with `item` (1-25), `line_no`, `return_qty_suom` (decimal &gt;0), `item_comments` (optional ≤200).
- **Response fields**: `entity_id`, `application_id`, `customer_order_no`, `comments`, `message_status` (S/F/E, 1 char), `message_desc`, `request_datetimestamp`, `response_datetimestamp`, `rma_request_id`, `RMA_no`, `response_message` (SUCCESS/FAILED).
- **Business rules/validation** (`RMAGenerationBean.java`): duplicate RMA request check by `(omsCustOrdNo, rmaRequestId)` (`:68-96`); physical warehouse must exist in `VWh` (`:107-126`); customer order must exist for `(custOrderNo, subCustOrderNo, applicationId)` (`:127-142`); return date must be strictly future (`:147-161`); each item/line must exist on the order (`:186-205`); return qty must be `&gt;0` and `&lt;=` delivered qty (`:210-224`); if the order originated from ORPOS, already-returned qty (summed from `OmsOrposCustOrdItmRtn`) must be `&lt; deliveredQty` else `ITEM_ALDY_RETUNED` (`:238-273`). Error strings are built via `OMSUtilCommons.formErrorDescription`, which itself does a raw SQL `SELECT oms_err_lang_desc FROM oms_error_codes WHERE oms_error_code=? AND lang_code=?` (`OMSUtil/src/.../OMSUtilCommons.java:306-319`).
- **Transaction behavior**: `OMSUtilSessionEJBBean` is `@Stateless` with no `@TransactionAttribute` overrides found anywhere in the file, so each remote call (`persistOmsRmaReq`, then separately `persistOmsRmaReqItem` per item, then separately the merges inside `callRWMSWebService`) is its own container-managed transaction, auto-committed on method return — the multi-step `saveRMA` → `saveRMAItems` → `callRWMSWebService` sequence is **not atomic**; a mid-sequence failure leaves partially-committed rows (mitigated only by the later `status='F'` update). The direct-JDBC `getInventoryInd` and `saveJoodTranscation` connections are closed via `OMSUtil.closeDBConnection` with no explicit `commit()`/`setAutoCommit()` calls, so behavior follows the `jdbc/oms` datasource's default autocommit setting (not visible in this repo).
- **Config**: `web.xml` above; `classes/app.properties` — `ServerUrl=prodrib-app1.extrastores.com`, `port=7701` (used to build the WebLogic `t3`/`http` JNDI provider URL in `OMSUtil.getInitialContext`, `OMSUtil.java:67-91`); `src/META-INF/persistence.xml` declares a local `persistence-unit "persistenceUnit"` against `java:/app/jdbc/jdbc/OMSDS`, but nothing in the module's Java code uses a local `EntityManager` — this appears to be an unused leftover artifact (only `OMSRMAGeneration` of the three modules ships one).

---

### RMA Modification — `modifyRMA`

- **Module**: OMSRMAModification. **SOAP operation**: `modifyRMA`. **Endpoint path**: `/OMSRMAModifyWebService` (`OMSRMAModification/public_html/WEB-INF/web.xml:5-13`). **WSDL**: `OMSRMAModifyWebservice.wsdl`, SOAP 1.2, same namespace `http://com.logicinfo.oms/model/`.
- **Entry point**: `com.logicinfo.oms.service.RMAModifyWebServiceImpl.modifyRMA(RMAModifyRequest)` — `OMSRMAModification/src/com/logicinfo/oms/service/RMAModifyWebServiceImpl.java:36-56`.
- **Call chain**:
  1. Endpoint → `new OMSRMAModificationBean()` → `.validate(input)` (`OMSRMAModificationBean.java:36`)
  2. `.persistOmsRmaMod(input)` (`OMSRMAModificationBean.java:29`) → `OMSPersistence.persistOMSRMAModifyHead` (`OMSPersistence.java:27`) and `.peristOMSRmaModDetail` (`OMSPersistence.java:39`)
  3. `.callRWMSWebservice(input)` (`OMSRMAModificationBean.java:113`) → `InterfacePersistence.callRWMSWebservice` (`InterfacePersistence.java:44`)
  4. `.createResponse(...)` (`OMSRMAModificationBean.java:121`) → `ResponseProcessing.createResponse` (`ResponseProcessing.java:23`)
- **DB access** (all JPA via `OMSUtilSessionEJB`, no stored procs):
  - `session.getOmsRmaReqFindByRmaId` on `OMS_RMA_REQ` (`OMSUtilSessionEJBBean.java:1325`)
  - `session.getOmsCustOrdHeadFindLanguage` (`:115`)
  - `session.getOmsRmaModHeadFindByRmaModReqId` (`OMS_RMA_MOD_HEAD`, throws `SOAPException("No Record")` when absent) (`:1753-1759`)
  - `session.getOmsCustOrdItemFindDeilverdQuantity` (`:1339`), `session.getOmsRmaReqItemFindByRmaIdAndItem` (`OMS_RMA_REQ_ITEM`, `:1281`)
  - `session.persistOmsRmaModHead` → `em.persist` into `OMS_RMA_MOD_HEAD` (`:1733`); `session.persistOmsRmaModDetail` → `em.persist` into `OMS_RMA_MOD_DETAIL` (`:1707`)
  - `session.getOmsSystemParametersFindByParameterId("RMA_REQ")` (`:461`); `session.mergeOmsRmaReqItem` (`:1264`)
  - `session.getOmsRmaModDetailFindByRmaModReqId` (`:1729`), used in response building
- **External system**: RWMS `PendingReturnsService`, operation `pendReturnDtlModify` (`InterfacePersistence.java:121`, `pendingReturnsPortType.pendReturnDtlModify(pendRtrnDesc)`). Same dynamic-URL resolution mechanism as Generation module (`OMS_WEBSERVICE_URI_DETAIL` via `OMSUtilCommons.getWebServiceURL`).
- **Note — Siebel artifacts present but unused**: `OMSRMAModification/src/retail/siebel/com/integration/SiebelStatusUpdateWebService.wsdl`, `SiebelStatusUpdateWebService1.xsd`, `SiebelStatusUpdateWebServiceProxy.proxy` exist in the source tree, but no Java class in this module references a `SiebelStatusUpdate*` type or generated client — no call site found. The Siebel order-status update is performed by the PL/SQL `WS_INVOKER` packages, not from this Java module.
- **Request fields (`OMSRMAModify.xsd`)**: header — `rma_id` (mandatory, unsigned int &gt;0), `rma_mod_req_id` (mandatory, ≤20); `RMAModifyDetail[1..100]` — `item` (1-25), `line_no`, `qty` (unsigned int &gt;0). **Response**: `rma_mod_req_id`, `status`, `message_code`, `message_desc`, `RMAModifyDetailResponse[0..100]` (`line_no`, `item`).
- **Business rules/validation** (`OMSRMAModificationBean.java:36-111`): a modification record must **not already exist** for `rma_mod_req_id` — if `getOmsRmaModHeadFindByRmaModReqId` returns a row, throws `INVALID_RMA_MOD_ID` (`:53-57`; catches the bean's internal "No Record" SOAPException to mean "OK to proceed", `:59-72`); the RMA (`rma_id`) must exist (`INVALID_RMA_ID` otherwise, `:73-82`); per line item, `qty` must be `&lt;=` delivered qty (`RMA_MOD_GT_QTY`, `:87-96`) and `&gt;=` already-received qty from `OMS_RMA_REQ_ITEM.RECEIVED_QTY` (`RMA_MOD_LT_QTY`, `:97-108`).
- **Transaction behavior**: same CMT-per-EJB-call model as Generation — `persistOMSRMAModifyHead`, `peristOMSRmaModDetail` (looped per item), and the later `mergeOmsRmaReqItem` calls inside `callRWMSWebservice` are separate transactions; no explicit rollback coordination across steps.
- **Config**: `web.xml` above; `classes/app.properties` (`ServerUrl=prodrib-app1.extrastores.com`, `port=7701`) — identical pattern to Generation. No `persistence.xml`/`weblogic-ejb-jar.xml` present in this module (only Generation ships those).

---

### RMA Deletion — `deleteRMA`

- **Module**: OMSRMADeletion. **SOAP operation**: `deleteRMA`. **Endpoint path**: `/OMSRMADeletionWebService` (`OMSRMADeletion/public_html/WEB-INF/web.xml:5-13`). **WSDL**: `OMSRMADeleteWebservice.wsdl`, SOAP 1.2, same namespace.
- **Entry point**: `com.logicinfo.oms.service.RMADeletionWebServiceImpl.deleteRMA(RMADeleteRequest)` — `OMSRMADeletion/src/com/logicinfo/oms/service/RMADeletionWebServiceImpl.java:39-60`.
- **Call chain**:
  1. Endpoint → `new RMADeletetionBean()` → `.validate(input)` (`RMADeletetionBean.java:36`)
  2. `.persistData(input)` (`RMADeletetionBean.java:75`) → `OMSPersistence.persistOmsRMADelReq` (`OMSPersistence.java:23`) (the sibling `persistOmsRMADelDetail`, `OMSPersistence.java:33`, exists but its call is **commented out** at `RMADeletetionBean.java:79`)
  3. `.callRWMSWebservice(input)` (`RMADeletetionBean.java:83`), looping `RMADeleteDetail` items, each calling `InterfacePersistence.callRWMSWebservice(input, lineNo)` (`InterfacePersistence.java:26`)
  4. `.createResponse(...)` (`RMADeletetionBean.java:124`) → `ResponseProcessing.createResponse` (`ResponseProcessing.java:19`)
- **DB access** (all JPA via `OMSUtilSessionEJB`, no stored procs):
  - `session.getOmsRmaReqFindByRmaId` (`OMS_RMA_REQ`, `OMSUtilSessionEJBBean.java:1325`)
  - `session.getOmsRmaDelHeadFindByRmaId(rmaId, "S")` (`OMS_RMA_DEL_HEAD`, `:1703`)
  - `session.persistOmsRmaDelHead` → `em.persist` into `OMS_RMA_DEL_HEAD` (`:1679`)
  - Per item: `session.getOmsRmaReqItemFindByRmaIdAndItem` (`OMS_RMA_REQ_ITEM`, `:1281`); `session.getOmsRmaDelHeadFindByRmaDelReqId` (`:1699`); `session.mergeOmsRmaDelHead` (`:1684`); `session.getOmsCustOrdItemFindByItem` (`:616`); `session.mergeOmsCustOrdItem` (`:589`, subtracts returned qty back onto `OMS_CUST_ORD_ITEM.QTY_RETURNED`); `session.persistOmsRmaDelDetail` → `em.persist` into `OMS_RMA_DEL_DETAIL` (`:1653`)
  - `session.getOmsRmaDelDetailFindByRmaDelReqId` (`:1675`) used in response building
- **External system**: RWMS `PendingReturnsService`, operation `pendReturnDtlDelete` (`InterfacePersistence.java:40`, `pendingReturnsPortType.pendReturnDtlDelete(pendRtrnRef)`), one call per line item with `physicalWh`, `rmaNbr`, `lineItemNbr`. Same dynamic-URL `OMS_WEBSERVICE_URI_DETAIL` resolution as the other two modules.
- **Request fields (`OMSRMADelete.xsd`)**: header — `rma_id` (mandatory, unsigned int &gt;0), `rma_del_req_id` (mandatory, ≤12); `RMADeleteDetail[1..100]` — `line_no`, `item` (1-25). **Response**: `rma_del_req_id`, `message_status`, `RMADeleteDetailResponse[0..100]` (`line_no`, `item`, `message_code`, `message_desc`).
- **Business rules/validation** (`RMADeletetionBean.java:36-73`): RMA must exist for `rma_id`, else `INVALID_RMA_ID` (`:42-52`); if an `OMS_RMA_DEL_HEAD` row already exists with `STATUS='S'` for the RMA, throws `RMA_ALRDY_CLD` — but this check is wrapped in a `try/catch` whose catch block is **empty/commented** (`:66-70`), so any lookup failure (e.g., no prior delete row — the normal case) silently passes validation rather than surfacing an error; per-item failures inside `callRWMSWebservice` are caught and recorded as `status='F'`/`errorCode='SYSTEM_ERROR'` on the (unpersisted, since `persistOmsRmaReqItem`/detail persist path only records success in the header merge) detail object rather than aborting the whole request (`:107-115`).
- **Transaction behavior**: same CMT-per-call model; each per-item block does `mergeOmsRmaDelHead` and then (regardless of the just-caught exception) unconditionally continues to `getOmsCustOrdItemFindByItem` / `mergeOmsCustOrdItem` / `persistOmsRmaDelDetail` (`:116-121`) even on the RWMS-call failure path — meaning `OMS_CUST_ORD_ITEM.QTY_RETURNED` is decremented and a detail row persisted even if the external RWMS delete call failed. No explicit rollback across the loop or across validate/persist/callRWMS steps.
- **Config**: `web.xml` above; `classes/app.properties` (`ServerUrl=prodrib-app1.extrastores.com`, `port=7701`). No `persistence.xml`/`weblogic-ejb-jar.xml` in this module.

---

### Item 10 — explicit grep for the named PL/SQL packages

- `OMS_PENDRETURN_WS_INVOKER`: **no match** anywhere under `/home/user/websvcs/oms/CODE` (Java or otherwise) — confirms it is invoked purely from PL/SQL (`OMSSUB_RECEIVING`/`OMSSUB_ASNOUT`), never from Java.
- `XX_REFUND_REQUEST_PKG`: **one match**, not in the assigned modules — `Hybris_Cancellation/src/main/java/org/logicinfo/hybriscancellation/serviceImpl/HybrisCancellationServImpl.java:633`, `{call XX_REFUND_REQUEST_PKG.create_xrr_refund_request(?×28)}` via `OracleCallableStatement`. This confirms refund requests are triggered from the Hybris cancellation flow, not from RMA generation/modification/deletion.
- `OMSSUB_ASNOUT`, `OMSSUB_RECEIVING`, `OMS_MSG_WS_PUBLISHER`: **no matches** in Java anywhere in the repo — consistent with these being PL/SQL-native RIB subscriber/publisher jobs with no Java call sites.
- Conclusion: none of the three assigned modules touch refund-request creation or the pending-return WS invoker; their only outbound integration is the RWMS `PendingReturnsService` SOAP client described above.

---

## Summary Table

| API | Method/Operation | Java Entry Point | Java Service | DAO/DB Layer | PL/SQL or SQL | Main Tables | Purpose |
|---|---|---|---|---|---|---|---|
| RMA Generation | `generateNewRMA` | `RMAGenerationWebServiceImpl.generateNewRMA` (`OMSRMAGeneration/src/com/logicinfo/oms/model/RMAGenerationWebServiceImpl.java:47`) | `RMAGenerationBean` (`.../RMAGenerationBean.java`) | `OMSUtilSessionEJB` (JPA persist/merge/named-query) + direct JDBC in `getInventoryInd`/`saveJoodTranscation` | JPA only for RMA rows; raw SQL `SELECT inventory_ind FROM item_master WHERE item=?` (`:773`); `{call XX_JOOD_MEM_TRANSACTION.XX_JOOD_TRANSACTION_PROCESS(...)}` (`:663`) | `OMS_RMA_REQ`, `OMS_RMA_REQ_ITEM`, `OMS_CUST_ORD_ITEM`, `ITEM_MASTER`, `XX_JOOD_TRANSACTIONS_HEAD/DTL`, `XX_JOOD_MEMBERSHIP` | Create a new RMA header + line items for a customer order and notify RWMS |
| RMA Modification | `modifyRMA` | `RMAModifyWebServiceImpl.modifyRMA` (`OMSRMAModification/src/.../RMAModifyWebServiceImpl.java:36`) | `OMSRMAModificationBean` + `OMSPersistence`/`InterfacePersistence`/`ResponseProcessing` | `OMSUtilSessionEJB` (JPA persist/merge/named-query) | none — pure JPA | `OMS_RMA_MOD_HEAD`, `OMS_RMA_MOD_DETAIL`, `OMS_RMA_REQ`, `OMS_RMA_REQ_ITEM` | Record a quantity modification against an existing RMA and notify RWMS |
| RMA Deletion | `deleteRMA` | `RMADeletionWebServiceImpl.deleteRMA` (`OMSRMADeletion/src/.../RMADeletionWebServiceImpl.java:39`) | `RMADeletetionBean` + `OMSPersistence`/`InterfacePersistence`/`ResponseProcessing` | `OMSUtilSessionEJB` (JPA persist/merge/named-query) | none — pure JPA | `OMS_RMA_DEL_HEAD`, `OMS_RMA_DEL_DETAIL`, `OMS_RMA_REQ`, `OMS_RMA_REQ_ITEM`, `OMS_CUST_ORD_ITEM` | Delete/cancel line item(s) from an existing RMA and notify RWMS |

All three call the same external system — Oracle Retail RWMS `PendingReturnsService` (`pendReturnDtlCreate` / `pendReturnDtlModify` / `pendReturnDtlDelete`), URL resolved at runtime from `OMS_WEBSERVICE_URI_DETAIL` row `RWMS_PENDING_RETURNS` via `OMSUtilCommons.getWebServiceURL` → `OMSUtilSessionEJBBean.getOmsWebserviceUriDetailFindByWebserviceName` (`OMSUtil/src/com/logicinfo/oms/ejb/OMSUtilSessionEJBBean.java:1234`). None call `OMS_PENDRETURN_WS_INVOKER` or `XX_REFUND_REQUEST_PKG` directly.
