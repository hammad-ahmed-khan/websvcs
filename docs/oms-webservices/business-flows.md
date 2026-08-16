# End-to-End Business Flows

Each flow below is built only from what the eight category reports and the PL/SQL reference actually confirmed. Where a step could not be traced further (e.g. into standard RMS internals or into a PL/SQL procedure whose source isn't in this repository), that boundary is marked explicitly rather than assumed. File:line citations live in the linked category docs — this file gives the cross-module narrative.

---

## 1. Order Creation — e-commerce / Hybris / NOON

```text
Hybris / NOON storefront
    │  SOAP request
    ▼
OMSCustomerOrder web service — SOAP operation "processNewOrder"
    │  OMSCustomerOrderWebServiceImpl.processNewOrder
    ▼
OMSCustomerOrderBean
    ├─ validateInput()            — mandatory-field / tender-type rules
    ├─ persistData() → OMSPersistence.omsPersist()   — JPA: OMS_CUST_ORD_HEAD/ITEM/ADDRESS, tenders
    ├─ findSourceLocation() → SourceLocationIdentifier.OrderPickUpModule()
    │       │  SOAP calls (via generated JAX-WS clients in oracle-base / OracleIntegrationServices)
    │       ├─ RMS FulfillOrderService
    │       ├─ SIM StoreFulfillmentOrderService / StoreInventoryService
    │       └─ RMS InventoryBackOrderService
    │       (returns ItemUnavailabilityStatus — FAIL short-circuits the whole order with UNAVL_INV)
    ├─ checkCreateOrReserveOrder() — RMS fulfillment-order creation or SIM reservation
    ├─ persistRTLog()             — OMS_RTLOG_PUBLISH_LOG (RMS RTLog republish queue), when pay-in-store='N' and create/reserve='C'
    ├─ splitTender()              — TenderSplit.tenderSplit() for VOUCH tender type
    ├─ notifySiebel()             — gated by system param CALL_SIEBEL
    │       │  builds UpdateOrderStatus SOAP payload (IStatus/Status client, same contract as the
    │       │  PL/SQL WS_INVOKER packages target)
    │       ├─ success → sent
    │       └─ failure → OMSUtil.OmsStatusUpdateForReturnPickupCancellation.insertRecordIntoRepublishData()
    │              → INSERT OMS_REPUBLISH_DATA (webServiceId resolved from OMS_WEBSERVICE_URI_DETAIL,
    │                key 'SOA_CO_STATUS_UPDATE') — the SAME failure-audit table the PL/SQL
    │                OMS_MSG_WS_PUBLISHER batch drains, though this Java path writes directly to
    │                OMS_REPUBLISH_DATA rather than going through OMS_PUBLISH_WS_DATA
    └─ if deliveryModeType='ODDSMALL' and deliveryType='S':
            bookingODDSlot() → oddPackageCall()
            └─ CallableStatement {call XXHDB_CORE_PKG.reserve_booking_small(...)}  ← PL/SQL, same-day slot reservation
               (failure here is only logged, never surfaced to the caller)

On any exception at any step: OMSUtilCommons.rollback(customerOrderNo) — a compensating
application-level rollback (reverses RMS/SIM reservations, does not use a DB transaction rollback).

Response: CustomerOrderResponse (messageStatus/messageCode/messageDesc + per-item status).
For NOON ("NSA") / web ("WEB") orders that fail, and only if system param
FAILED_ORDERS_REQ_RESP='Y': the full request+response XML is additionally archived to
oms_order_create_response_head/item (raw JDBC insert) for support/audit purposes.
```

**Downstream, asynchronously, via RIB (not directly triggered by this flow, but consuming the RMS-side effects of it)**: once RMS physically ships or the order is received against a PO, `OMSSUB_ASNOUT`/`OMSSUB_RECEIVING` (PL/SQL RIB subscribers, §1–2 of `plsql-packages.md`) pick up the resulting shipment/receiving RIB messages and progress the order's fulfillment/delivery state — see Flow 5 and 6 below.

**Note — parallel, non-functional path**: `oms-customer-order`'s REST `POST /createNewOrder` (`EcomOrderController`/`EComOrderServiceImpl`) reimplements a subset of the same validation rules but is confirmed **non-functional** — it never persists anything, never calls RMS/SIM, and always returns a hardcoded success regardless of validation outcome. It is not a working alternative entry point to this flow.

---

## 2. Order Creation — ORPOS (in-store POS)

```text
ORPOS (in-store point of sale)
    │  SOAP: requestNewCustomerOrderId  → GenerateNewOrderId (oms_cust_id_seq.nextval)
    │  SOAP: createCustomerOrder
    ▼
CustomerOrderService / CustomerOrderBeanPOS — CustomerOrderPortTypeImpl.createCustomerOrder
    ▼
CustOrdCreateBean
    ├─ checkCustomerOrderStatus()  — dedup
    ├─ saveCreatCustOrd()          — JPA persist via OMSUtilSessionEJB (no raw SQL)
    ├─ checkOrderStatus()          — FILLED / CANCELED
    ├─ FILLED  → persistRTLogTable(), reconcilationCreateOrder()
    └─ CANCELED/error → callRollBackMethod() + reduceBOSrcQty()
           (rolls back RMS/SIM reservations and back-order source-quantity holds —
            application-level compensating rollback, not a DB transaction rollback)
```

Subsequent in-store customer actions on the same order flow through the same SOAP service: `pickupCustomerOrderItems` (pickup **and** in-store cancellation share one operation, `PickCustOrdItemBean`), `returnCustomerOrderItems` (`ReturnCustOrdBean`). Both notify Siebel via the same `OmsStatusUpdateForReturnPickupCancellation` path as Flow 1.

---

## 3. Order Cancellation

Two independently-built, functionally-overlapping cancellation code paths exist against the same schema. `oms-cancellation` is the active one — `OMSCOCancellation` (legacy SOAP/EJB) is not called by anything else in the repository and appears to be superseded.

```text
Caller: Hybris (via Hybris_Cancellation), oms-cancellation-job (scheduled sweep), or a direct API client
    ▼
POST /omscancellation  (oms-cancellation, OrderCancelController → OrderCancellationService)
    ├─ CancelOrderDAO.validateAndGetDetail()      — dedup + line-eligibility checks (raw SQL)
    ├─ CancelOrderDAO.saveCancellationRequest()   — INSERT XX_ORD_CANCELLATION_REQ
    ├─ CancelOrderDAO.callCancelPackage()
    │       └─ CallableStatement {call XX_IS_CANCELLABLE(...)}   ← PL/SQL eligibility + refund-amount calc
    ├─ saveOmsCancelHead()  — INSERT OMS_CO_CANCEL_HEAD, OMS_CUST_ORD_LOG (event 'CA')
    ├─ branch on (orderCreateReserveInd='R' and ordPaymentStatus='P'):
    │    RESERVATION-CANCEL PATH (cancelReservation):
    │       ├─ warehouse reserves → {call OMS_INVADJ_STATUS_UNAVIALINV(...)} per item
    │       └─ store reserves     → BaseAPIService.reverseInventoryAdjustment()
    │                                → SIM/RMS SOAP InventoryAdjustmentPortType.saveAndConfirmInventoryAdjustment
    │                                (SOAP failures queued to OMS_REPUBLISH_DATA for republish)
    │    NORMAL CANCEL PATH (processCancellation):
    │       ├─ WH↔WH "Carera" fulfilments → {? = call XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN(...)}
    │       ├─ else → RMS FulfillOrderPortType.cancelFulfilOrdColRef and/or
    │       │         SIM StoreFulfillmentOrderPortType.cancelFulfillmentOrderDetail (SOAP)
    │       ├─ in-store pickup reversal → SIM FulfillmentOrderDeliveryPortType /
    │       │         FulfillmentOrderReversePickPortType (SOAP)
    │       └─ inventory back-order reversal → RMS InventoryBackOrderPortType.createInvBackOrdColDesc
    ├─ CancelOrderDAO.updateCancellationDetail()  — batched UPDATE/INSERT across OMS_CO_CANCEL_ITEM,
    │       OMS_CUST_ORD_ITEM.QTY_CANCELLED, OMS_CO_FULFILL_DETAIL.FULFILL_CANCEL_QTY, OMS_CO_FO_CANCEL,
    │       OMS_CUST_ORD_LOG_ITEM, OMS_CUST_ORD_RESERVE, OMS_BACK_ORDER_DTL; closes the order header and
    │       rejects tender when fully processed
    ├─ if deliveryMode='ODDSMALL': {call XXHDB_CORE_PKG.cancel_booking_small(...)}    ← PL/SQL
    ├─ saveJoodTransactionDetail() → {call XX_JOOD_MEM_TRANSACTION.XX_JOOD_TRANSACTION_PROCESS(...)}  ← PL/SQL (loyalty reversal)
    └─ notifyHybris() (unless comments='HybrisCancellation'):
            ├─ e-invoicing conditions met → saveEInvoicingReq() — INSERT XX_EINV_CAN_RET_OMS
            └─ always → builds Siebel UpdateOrderStatus SOAP XML → INSERT OMS_REPUBLISH_DATA
                   (webServiceId from OMS_WEBSERVICE_URI_DETAIL, key 'SOA_CO_STATUS_UPDATE')
```

**Hybris-originated cancel/refund flow** (separate entry point that calls into the above):
```text
Hybris storefront
    ▼
POST /createCancellation  (Hybris_Cancellation, HybrisCancellationController)
    ├─ checkCustomerOrder(), checkRefundValue(), checkvalidation()   — raw JDBC validation
    ├─ insertRequestInHybrisTables() — INSERT ext_cancel_head/item/tender (own connection, no shared tx)
    ├─ RefundOnly path  → insertDetailInMuleTableProcedure() directly
    └─ CancelAndRefund path:
           processingCencellation() → raw HttpURLConnection POST to
               http://<host>/omscancellation/omscancellation  (i.e. calls the flow above,
               with comments="HybrisCancellation" to suppress its Siebel/e-invoicing notify)
           on success → insertDetailInMuleTableProcedure()
                 └─ CallableStatement {call XX_REFUND_REQUEST_PKG.create_xrr_refund_request(...×28)}
                        ← PL/SQL — creates the channel-specific refund record (SADAD/Payfort/
                          Tasheel/Benefit/AFS/COD), keyed to ext_cancel_head.CANCEL_HEAD_SEQ_ID
```

**Scheduled sweep**: `oms-cancellation-job` (`ServiceOrderCancelApplication`) polls `XX_CC_SERVICE_CANCEL_DTL` for pending service-order cancellations and replays each through the same `POST /omscancellation` endpoint via a Feign client — a third entry point into the same core flow.

---

## 4. Order Fulfillment — Manual / Bulk (internal ops)

```text
Internal ops SPA (oms-core, JWT-authenticated)
    ▼
POST /fulfilment  (FulfilmentController → FulfilmentService.createWithCancelFulfilment, @Transactional)
    ├─ FulfilmentDAO.getStockAvailablity()  — stock check, throws STOCK_NOT_AVAILABLE if insufficient
    ├─ optional prior cancel (same cancel logic as createFulfilment's cancel path)
    ├─ WH↔WH → createCarreraFulfilment()  — Feign TransferCreationRequest/Response (Carrera JSON API)
    └─ else  → createRMSFulfilment()  — RMS SOAP FulfillOrderService.createFulfilOrdColDesc
               → if store=store also createSIMFulfilment() — SIM SOAP createFulfillmentOrderDetail
                     on SIM failure → rollbackRMSFulfilment() (RMS cancelFulfilOrdColRef) — automatic
                     cross-system compensation within the same request
               → FulfilmentDAO.createOmsFulfilment()  — persists OMS-side fulfilment record
```
`PUT /fulfilment` (cancel) and the Excel-driven `POST/PUT/GET /bulkfulfilment` endpoints reuse the same `FulfilmentService`/`FulfilmentDAO` logic per row; bulk processing runs on a raw per-request `Thread` with in-memory per-user result queues (not clustered-safe — see `error-handling-config.md`).

---

## 5. Store / Warehouse Fulfillment — Shipment (ASN-out RIB subscriber)

```text
Oracle Retail RMS 14  (standard RMS shipment/transfer processing — internals not in this repo)
    │  RIB message: RIB_ASNOutDesc_REC, type "asnoutcre"
    ▼
OMSSUB_ASNOUT.CONSUME  (PL/SQL, invoked by RMS's RIB dequeue framework)
    ├─ API_LIBRARY.INIT() — standard RMS subscriber init
    ├─ filter: only proceeds for consumer-direct / non-stockholding / spare-parts-linked shipments
    ├─ INSERT OMS_ASNOUT_DESC / DISTRO / CTN / ITEM  — stage the full RIB message
    ├─ if TO_STOCKHOLDING_IND='N' (true customer delivery):
    │     for each affected OMS_CUST_ORD_HEAD / OMS_CO_FULFILL_DETAIL row:
    │       UPDATE OMS_CUST_ORD_ITEM.CUM_QTY_DELIVERED, OMS_CO_FULFILL_DETAIL.FULFILL_DELIVER_QTY
    │       (auto-closes OMS_CUST_ORD_HEAD when all lines fully delivered/cancelled)
    │       INSERT OMS_RTLOG_PUBLISH_LOG  (feeds RMS's own RTLog republish mechanism)
    │       INSERT OMS_CUST_ORD_LOG / LOG_ITEM  (event DL=delivered or SH=shipped/reserved)
    │       [handles non-inventory / shipping-charge lines separately, auto-marking them delivered]
    ├─ spare-parts branch: if transfer is linked to OMS_SPARE_PART_FULFILL, updates
    │       quantity_shipped / CUM_QUANTITY_SHIPPED, INSERT OMS_SPARE_PART_AUDIT
    └─ every step mirrored into CUST_ORDER_TRACKING (audit trail)

(No commit inside this package — relies on the RIB framework's post-CONSUME commit.
 No explicit call in the current source to OMS_ASNOUT_WS_INVOKER's outbound send —
 the Siebel-notify leg described in plsql-packages.md is staged-only via OMS_PUBLISH_WS_DATA,
 sent later by the OMS_MSG_WS_PUBLISHER batch job — see Flow 7.)
```

---

## 6. Warehouse Receiving (RIB subscriber → SIM/RMS reservation)

```text
Oracle Retail RMS 14 (or SIM, on receipt confirmation)
    │  RIB message: RIB_ReceiptDesc_REC, type "receiptcre" (non-order) or "receiptordcre" (order-linked)
    ▼
OMSSUB_RECEIVING.CONSUME
    ├─ INSERT OMS_RECEIPT_DESC / RECEIPT / RECEIPT_DTL / RECEIPT_CARTONDTL / RECEIPT_OVERAGE(DTL)
    ├─ if type = receiptordcre  (order-linked receipt):
    │     for each affected customer order:
    │       OMS_RECEIPT_CO_SIM_WS_INVOKER.f_REC_SIM_CO_RESERVE(...)   ← reserve in SIM
    │           (package source NOT in this repository — Requires DB verification)
    │       OMS_RECEIPT_CO_WS_INVOKER.f_REC_RMS_CO_RESERVE(...)       ← reserve in RMS
    │           (package source NOT in this repository — Requires DB verification)
    │       [Siebel status-update call via OMS_RECEIPT_WS_INVOKER is present in source but
    │        COMMENTED OUT — currently disabled]
    │     INSERT OMS_CO_FULFILL_DETAIL (source_loc_type='ST'), OMS_CUST_ORD_LOG/LOG_ITEM (event RE)
    └─ else if spare-parts-linked transfer (document type 'T'):
          OMS_RECEIPT_SIM_INVADJ_INVOKER.f_REC_SIM_INVADJ(...)   ← SIM inventory adjustment
              (package source NOT in this repository — Requires DB verification)
          UPDATE OMS_SPARE_PART_HEADER (cum received/reserved), INSERT OMS_SPARE_PART_FULFILL (RSV),
          INSERT OMS_SPARE_PART_AUDIT, flags READY_TO_NOTIFY_FLAG when fully reserved
```

---

## 7. Outbound Siebel Order-Status Notification (dual implementation)

This is the one integration implemented **twice, independently** — once in PL/SQL (staged/async) and once in Java (both synchronous-attempt-then-async-fallback):

```text
PL/SQL path (staging only — no live send in the current source):
  OMSSUB_ASNOUT / OMSSUB_RECEIVING
      → OMS_ASNOUT_WS_INVOKER / OMS_RECEIPT_WS_INVOKER . f_..._Siebel_COStatusUpdate
          → builds UpdateOrderStatus SOAP XML
          → INSERT OMS_PUBLISH_WS_DATA (queued; the actual generic_oms_soap_call/UTL_HTTP send is
            commented out at this layer)
                    │
                    ▼  (async, external cron: xtra_oms_ws_republish.ksh)
      OMS_MSG_WS_PUBLISHER.publish_Messages
          → SELECT ... FROM OMS_PUBLISH_WS_DATA WHERE REPUBLISH_STATUS='N' ...
          → OMS_WS_INVOKER.generic_oms_soap_call(...)   [package not in this repo]
          → UTL_HTTP POST to the Siebel/SOA-fronted UpdateOrderStatus endpoint
                (URL from OMS_WEBSERVICE_URI_DETAIL)
          → success: OMS_PUBLISH_WS_DATA_LOG ; failure: OMS_REPUBLISH_DATA

Java path (synchronous attempt with async fallback):
  OMSCustomerOrderBean.notifySiebel / PickCustOrdItemBean.notifySiebel(ForPickUp) /
  BaseAPIService (oms-cancellation) / PaymentConfirmation's InterfacePersistence
      → OmsStatusUpdateForReturnPickupCancellation.callOmsStatusUpdateWebserviceForReturnAndCancellationAndPickup
          → JAX-WS client IStatus/Status.updateOrderStatus(...)  (generated in oracle-base,
            endpoint resolved dynamically — historically a .NET/WCF service, later migrated to a
            MuleSoft CloudHub-fronted endpoint, same SOAP contract)
          → on failure or non-"Success"/non-"ORPOS" result:
                INSERT OMS_REPUBLISH_DATA directly (webServiceId via OMS_WEBSERVICE_URI_DETAIL,
                key 'SOA_CO_STATUS_UPDATE') — bypassing OMS_PUBLISH_WS_DATA/OMS_MSG_WS_PUBLISHER entirely
```

The PL/SQL-side order-creation feed (`InboundordercreationbpelprocessClientEp`/`SiebelOrderFeedWebservice.publishToSiebel`, a separate BPEL process from `UpdateOrderStatus`) is also called from `InterfacePersistence.callSeibelWebservice` in `PaymentConfirmation` — see `categories/payment-finance-pricing-einvoicing.md`.

---

## 8. Returns / RMA

```text
Caller (Siebel CRM, e-commerce, or ORPOS-style requestor per entity_id/application_id)
    ▼
SOAP generateNewRMA  (OMSRMAGeneration)
    ├─ RMAGenerationBean.validate() — dup check, order/line existence, return-date-in-future,
    │       return qty ≤ delivered qty (and, for ORPOS orders, ≤ not-already-returned qty)
    ├─ saveRMA() / saveRMAItems()   — JPA persist OMS_RMA_REQ / OMS_RMA_REQ_ITEM
    ├─ callRWMSWebService() → RWMS PendingReturnsService.pendReturnDtlCreate (SOAP)
    │       (skipped for non-inventory items; URL from OMS_WEBSERVICE_URI_DETAIL key RWMS_PENDING_RETURNS;
    │        retries up to 2x on transient SOAP faults, else marks OMS_RMA_REQ.STATUS='F')
    └─ saveJoodTranscation() → {call XX_JOOD_MEM_TRANSACTION.XX_JOOD_TRANSACTION_PROCESS(...)}  ← PL/SQL

SOAP modifyRMA  (OMSRMAModification)
    └─ validate (mod-id not already used, RMA exists, qty within delivered/received bounds)
       → JPA persist OMS_RMA_MOD_HEAD/DETAIL → RWMS pendReturnDtlModify (SOAP)

SOAP deleteRMA  (OMSRMADeletion)
    └─ validate RMA exists → JPA persist OMS_RMA_DEL_HEAD/DETAIL, reverses
       OMS_CUST_ORD_ITEM.QTY_RETURNED → RWMS pendReturnDtlDelete (SOAP), per line item
```
None of the three RMA modules call `OMS_PENDRETURN_WS_INVOKER` or `XX_REFUND_REQUEST_PKG` — the PL/SQL "pending return" Siebel-status-update leg (Flow 6/7's `OMS_PENDRETURN_WS_INVOKER`) is a **separate, RIB-triggered path** with no confirmed Java caller, distinct from this Java-driven RMA-to-RWMS flow.

**In-store return** (ORPOS): `CustomerOrderService.returnCustomerOrderItems` — see Flow 2.
**noon.com marketplace return**: `NOON/ExtraServiceRest POST /customer` — validates delivered/returned quantities directly against `OMS_CUST_ORD_HEAD`/`ITEM`, inserts `OMS_RMA_REQ(_ITEM)`/`OMS_RMA_RCV_DTL` directly (raw JDBC, not via the SOAP RMA services above), then posts a `RETURN` transaction to SIM's `POSTransactionService` (SOAP) — a fourth, independently-implemented return code path.

---

## 9. Inventory Inquiry

Three independent implementations exist, none of which call each other:

```text
E-commerce ATS check:
  POST /inventory (Stockcheck) → LookUpInventoryServiceImpl
     → raw SQL against XX_OMS_INV (store), XX_OMS_INVAVAIL_V_BACK (warehouse),
       GET_PACK_QTY_V (pack items), V_CUST_FUTURE_INV_ECOM_FEED_T (future/back-ordered),
       netted against OMS_BACK_ORDER_DTL unfulfilled quantity

ORPOS inventory check (two parallel implementations — legacy and rewritten):
  SOAP checkInventory (OmsOrposInventoryCheck, legacy)
     → InventoryCheckBean: per-item priority-location loop, SIM StoreInventoryService SOAP call
       for store SOH, raw SQL for warehouse/future/back-order lookups
  SOAP checkInventory (OmsOrposInventoryCheckNew, rewrite)
     → InventoryCheckService: fully batched, no SOAP call — pure DB computation with
       classification/threshold/promise-date logic (GET_CLASSIFICATION_BO_TEST stored function)

Delta/feed-style inventory (for downstream consumers, not request/response):
  POST /physicaldeltafeed, /futureinventoryfeed, /packitemfeed (Stock_Feed)
     → incremental-watermark queries against XX_TEST_INVAVAIL_V / TEST_ITEM_STOCK_DTL_V /
       V_CUST_FUTURE_INV_ECOM_FEED_B / GET_PACK_QTY_V, logged to STOCK_FEED
```

## 10. Inventory Reservation / Adjustment

Reservation and unreservation of inventory is not a single API — it is a **side effect** performed inside the order-creation, payment-confirmation, and cancellation flows (Flows 1, and the payment-confirmation flow in `categories/payment-finance-pricing-einvoicing.md`), always via the same two mechanisms:
- PL/SQL: `{call OMS_INVADJ_STATUS_UNAVIALINV(...)}` (warehouse-side reservation status).
- SOAP: RMS/SIM `InventoryAdjustmentPortType.saveAndConfirmInventoryAdjustment` (store-side reservation), reached through `IOracleSIMClient`/`IOracleInvAdjClient` (Feign) or the legacy JAX-WS ports depending on which module is calling.

Spare-parts inventory reservation follows a separate, dedicated pair of SOAP services — `SparePartsRequest` (reserve, compensating rollback via `SparePartsRequestReversalBean` on failure) and `SparePartsConfirmation` (confirm/deduct, with an async-republish fallback via `OMS_REPUBLISH_DATA` on SIM failure rather than synchronous rollback) — see `categories/inventory-spare-parts.md`.

## 11. Order Status Inquiry

`CustomerOrderService.queryCustomerOrder` (ORPOS) and `oms-core`'s `GET /order`, `/order/detail/{omsCustomerNo}`, `/order/cancel/{customerNo}`, `/order/return/{customerNo}`, `/order/booking/{customerNo}` (internal ops SPA) are the two read-side entry points; both are pure lookups against `OMS_CUST_ORD_*`/`OMS_CO_CANCEL_*`/`OMS_RMA_*`/`XX_DLVRY_BOOKING*` with no PL/SQL involvement. There is no single unified "order status" API consumed by the e-commerce front end in this repository — Hybris/NOON order status is understood to be driven by the outbound Siebel `UpdateOrderStatus` push (Flow 7), not a pull API.

---

## Flows explicitly NOT supported by the code (do not imply otherwise)

- There is no single "Update Order" / general order-modification SOAP or REST API distinct from cancellation, RMA modification, address update, and payment confirmation — order changes are handled as a set of narrow, purpose-specific operations, not a generic PATCH/update endpoint.
- `oms-customer-order`'s `POST /createNewOrder` is present in the WSDL/REST surface but is confirmed non-functional (see Flow 1 note) — it must not be documented as a working e-commerce order-creation path.
- The three PL/SQL packages referenced by `OMSSUB_RECEIVING` for SIM/RMS reservation and inventory adjustment (`OMS_RECEIPT_CO_SIM_WS_INVOKER`, `OMS_RECEIPT_CO_WS_INVOKER`, `OMS_RECEIPT_SIM_INVADJ_INVOKER`) have no source in this repository — their internal logic cannot be documented beyond the fact that they are called and with what parameters (see Flow 6).
