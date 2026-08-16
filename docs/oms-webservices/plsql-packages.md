# Custom PL/SQL Package Reference

Source: `oms/PACKAGE/*.sql` and `*.txt` (10 files — the complete set of custom PL/SQL package source checked into this repository). All packages below were read in full from the repository; every signature, table name, and behavior described is **confirmed from source**, not inferred. Java call sites are cross-referenced from the eight business-category reports in `categories/`.

This is a small, tightly-scoped set of custom packages. The vast majority of the OMS/RMS business logic in this system lives either in **standard Oracle Retail RMS 14 PL/SQL** (not present in this repository — see `unknowns.md`) or in **Java** (JPA/JDBC in the ~65 Java modules under `oms/CODE/`). These 10 packages are the custom PL/SQL layer that bridges RMS's RIB (Retail Integration Bus) subscriber framework to the custom `OMS_*` schema, plus a handful of narrowly-scoped custom procedures called directly from Java.

---

## 1. `OMSSUB_ASNOUT` / `OMSSUB_ASNOUT_BODY`

**Classification**: Custom RIB subscriber package (follows RMS's standard `RMSSUB_*`/`API_LIBRARY` subscriber conventions — `API_LIBRARY.INIT`, `API_CODES.SUCCESS`, `SQL_LIB.CREATE_MSG` are all standard RMS framework calls).

**Purpose**: Subscribes to the RIB **ASN-out / shipment** message (`RIB_ASNOutDesc_REC`, message type `asnoutcre`), published by RMS when a shipment/transfer/reservation event occurs. Populates the custom `OMS_ASNOUT_*` staging tables and then updates customer-order fulfillment state.

**Public interface**:
```sql
PROCEDURE CONSUME(O_status_code OUT VARCHAR2, O_error_message OUT VARCHAR2,
                   I_message IN RIB_OBJECT, I_message_type IN VARCHAR2);
PROCEDURE CONSUME_SHIPMENT(O_status_code OUT VARCHAR2, O_error_message OUT VARCHAR2,
                            I_message IN RIB_OBJECT, I_message_type IN VARCHAR2,
                            I_check_l10n_ind IN VARCHAR2);
FUNCTION CONSUME_SHIPMENT(O_error_message OUT VARCHAR2, IO_L10N_RIB_REC IN OUT L10N_OBJ) RETURN BOOLEAN;
```
`CONSUME` is the entry point RMS's RIB dequeue framework calls. `CONSUME_SHIPMENT` (function form) is invoked through RMS's L10N (localization) wrapper layer, and is also documented as callable from `notify_asnout_message`, `ribapi_aq_reprocess_sql` (RIB error-retry), and RMS's own `fm_asnout_consume_sql.consume`/`fm_auto_shipping_sql.consume` — i.e. this package plugs into RMS's standard shipment-consume extension point.

**Core logic** (private `CONSUME_SHIPMENT_CORE`):
1. Filters: only processes when `TO_STOCKHOLDING_IND='N'` or `TO_LOCATION IS NULL` (customer/consumer-direct delivery), or when the distro's `consumer_direct='Y'`, or when the transfer number matches an open `OMS_SPARE_PART_FULFILL` record (spare-parts context). Non-matching messages are ignored (no error — this is filtering, not validation).
2. Inserts the full message tree into `OMS_ASNOUT_DESC` (header), `OMS_ASNOUT_DISTRO` (per-distro), `OMS_ASNOUT_CTN` (carton), `OMS_ASNOUT_ITEM` (item/qty).
3. When `TO_STOCKHOLDING_IND='N'` (true consumer delivery): for every matching `OMS_CUST_ORD_HEAD`/`OMS_CO_FULFILL_DETAIL` row, updates `OMS_CUST_ORD_ITEM.CUM_QTY_DELIVERED`, `OMS_CO_FULFILL_DETAIL.FULFILL_DELIVER_QTY`, closes `OMS_CUST_ORD_HEAD.CLOSE_DATETIME` when all lines are fully delivered/cancelled, inserts `OMS_RTLOG_PUBLISH_LOG` (RMS RTLog republish queue) and, for consumer-direct lines, `OMS_CUST_ORD_LOG`/`OMS_CUST_ORD_LOG_ITEM` (event `DL`=delivered or `SH`=shipped/reserved). Also separately handles non-inventory/shipping-charge line items (matched via `OMS_SYSTEM_PARAMETERS('SHIPPING_CHARGE_DEPT')`) which are auto-marked delivered without a physical ASN line.
4. Spare-parts branch: if the transfer is linked to `OMS_SPARE_PART_FULFILL` (via `tran_id`), updates `quantity_shipped`/`CUM_QUANTITY_SHIPPED` and inserts an `OMS_SPARE_PART_AUDIT` row.
5. Every state change is mirrored into `CUST_ORDER_TRACKING` (a lightweight step-by-step audit trail table, one row per intermediate step — 9 distinct tracking events).

**Tables**: reads/writes `OMS_ASNOUT_DESC/DISTRO/CTN/ITEM`, `OMS_CUST_ORD_HEAD/ITEM`, `OMS_CO_FULFILL_DETAIL`, `OMS_CUST_ORD_LOG/LOG_ITEM`, `OMS_RTLOG_PUBLISH_LOG`, `OMS_SPARE_PART_FULFILL/HEADER/AUDIT`, `CUST_ORDER_TRACKING`, `ITEM_MASTER`, `OMS_SYSTEM_PARAMETERS`. Standard RMS `RIB_ASNOutDesc_REC`/`RIB_ASNOutDistro_REC`/`RIB_ASNOutCtn_REC`/`RIB_ASNOutItem_REC` object types (RMS-standard, not custom) are the message payload shape.

**Transaction/error behavior**: standard RMS `API_LIBRARY.HANDLE_ERRORS` pattern — no explicit `COMMIT` in the body shown (commit is expected to be issued by the RIB framework/caller after `CONSUME` returns success, per RMS convention). Errors from mid-procedure failures are captured via `P_INS_ERROR_DTL` (custom error-logging procedure, referenced but not itself in this package) rather than raising, in several branches — meaning a partial failure can still leave prior INSERTs in the same transaction pending commit by the caller.

**Java callers**: none found anywhere in the repo. This package is purely RIB-triggered PL/SQL — RMS's queue framework invokes `CONSUME` directly; there is no Java code path that calls it.

---

## 2. `OMSSUB_RECEIVING`

**Classification**: Custom RIB subscriber package (same RMS subscriber convention).

**Purpose**: Subscribes to the RIB **Receiving** message (`RIB_ReceiptDesc_REC`, message types `receiptcre` / `receiptordcre`). Per Oracle Retail's documented behavior (confirmed in the code comments), SIM publishes `ReceiptCre` for receipts unrelated to a customer order and `ReceiptOrdCre` when the receipt fulfills a customer order; RMS treats both identically for its own processing, but only `ReceiptOrdCre` triggers the OMS-side logic in this package.

**Public interface**:
```sql
PROCEDURE CONSUME(O_status_code OUT VARCHAR2, O_error_message OUT VARCHAR2,
                   I_message IN RIB_OBJECT, I_message_type IN VARCHAR2,
                   O_rib_otbdesc_rec OUT RIB_OBJECT, O_rib_error_tbl OUT RIB_ERROR_TBL);
```

**Core logic**:
1. Persists the receipt into `OMS_RECEIPT_DESC` (header), `OMS_RECEIPT` (per-PO), `OMS_RECEIPT_DTL` (item detail), `OMS_RECEIPT_CARTONDTL`, `OMS_RECEIPT_OVERAGE`/`OVERAGEDTL`.
2. If the message type is `receiptordcre` (customer-order-linked, document type `'T'` transfer for spare parts is additionally checked against `OMS_SPARE_PART_FULFILL`): for each affected customer order, calls two other custom packages **not present in this repository**:
   - `OMS_RECEIPT_CO_SIM_WS_INVOKER.f_REC_SIM_CO_RESERVE(message_id, cust_ord_no)` — reserves the received item in SIM.
   - `OMS_RECEIPT_CO_WS_INVOKER.f_REC_RMS_CO_RESERVE(message_id, cust_ord_no)` — reserves the received item in RMS.
   (A third referenced-but-absent package, `OMS_RECEIPT_SIM_INVADJ_INVOKER.f_REC_SIM_INVADJ`, is called in the spare-parts branch below for SIM inventory adjustment.)
   Then conditionally (only if the order has at least one non-`ORPOS` application with `STATUS='S'` and `ORD_PAYMENT_STATUS='S'`) calls `OMS_RECEIPT_WS_INVOKER.f_REC_Siebel_COStatusUpdate` (present in this repo — see §5) — **this call is commented out** in the current source (`--   L_seibel_out :=OMS_RECEIPT_WS_INVOKER.f_REC_Siebel_COStatusUpdate(...)`), so the Siebel status-update leg of the receiving flow is currently disabled in this codebase, even though the tracking rows around it still fire.
   Inserts `OMS_CO_FULFILL_DETAIL` (new fulfillment record sourced from the store, `SOURCE_LOC_TYPE='ST'`), `OMS_CUST_ORD_LOG`/`LOG_ITEM` (event `RE`/"RECEIVING").
3. Else (document type `'T'` transfer tied to a spare-parts service request, but not order-linked): reserves at the destination service-centre via `OMS_RECEIPT_SIM_INVADJ_INVOKER.f_REC_SIM_INVADJ` (SIM inventory adjustment for spare parts), then updates `OMS_SPARE_PART_HEADER.CUM_QUANTITY_RECEIVED/CUM_QUANTITY_RESERVED`, inserts a new `OMS_SPARE_PART_FULFILL` row (`TRAN_TYPE='RSV'`) and an `OMS_SPARE_PART_AUDIT` row, and flags `OMS_SPARE_PART_HEADER.READY_TO_NOTIFY_FLAG='Y'` once cumulative reserved-minus-unreserved equals the pending quantity.
4. Every step is mirrored into `CUST_ORDER_TRACKING` (10 distinct events).

**Tables**: `OMS_RECEIPT_DESC/RECEIPT/RECEIPT_DTL/RECEIPT_CARTONDTL/RECEIPT_OVERAGE(DTL)`, `OMS_CO_FULFILL_DETAIL`, `OMS_CUST_ORD_LOG/LOG_ITEM`, `OMS_SPARE_PART_HEADER/FULFILL/AUDIT`, `CUST_ORDER_TRACKING`.

**Transaction/error behavior**: same `API_LIBRARY`/`SQL_LIB` pattern as `OMSSUB_ASNOUT`; no explicit commit in the body — relies on the RIB framework. `INV_MESSAGE_TYPE`/`PROGRAM_ERROR` exceptions map to `O_status_code='E'`.

**Java callers**: none. Purely RIB-triggered.

---

## 3–6. Outbound Siebel status-update invokers: `OMS_ASNOUT_WS_INVOKER`, `OMS_RECEIPT_WS_INVOKER`, `OMS_SOS_WS_INVOKER`, `OMS_PENDRETURN_WS_INVOKER`

**Classification**: Custom outbound SOAP-client packages, all following an identical structural pattern (near-duplicate code across the four).

**Purpose**: Each builds a `UpdateOrderStatus` SOAP request (from OMS staging-table data for a specific event — ASN-out/shipment, receiving, "SOS" i.e. a generic in-memory record variant, or pending-return/RMA-receipt) and POSTs it via `UTL_HTTP` to an external order-status-update web service (contract `IStatus.UpdateOrderStatus`, namespace `http://www.extra.com/Services/OmsStatus`, action `http://www.extra.com/Services/OmsStatus/IStatus/UpdateOrderStatus`). This is the **Siebel CRM order-status feed** — the same SOAP contract the Java modules also call directly via a JAX-WS client (see `categories/order-management.md` and `categories/core-gateway-oracleintegrationservices.md`), confirming this integration is implemented **twice, independently, in both PL/SQL and Java**.

**Common structure** (all four packages):
- `lsv_WS_Name CONSTANT := 'SOA_CO_STATUS_UPDATE'` — the lookup key into `OMS_WEBSERVICE_URI_DETAIL`.
- `get_WS_URL_Details(p_WS_Name)` — `SELECT * FROM OMS_WEBSERVICE_URI_DETAIL WHERE web_service_name = p_WS_name`; raises `TOO_MANY_ROWS`/`NO_DATA_FOUND` if not exactly one row.
- `generic_oms_soap_call(p_payload, p_target_url, p_soap_action, p_soap_envelope OUT)` — wraps the payload in a `<soap:Envelope>` if not already wrapped, issues the HTTP POST via `UTL_HTTP.begin_request/set_header/write_text/get_response`, sets a 100000ms transfer timeout, and returns the response body extracted from `/S:Envelope/S:Body`. On any failure (`UTL_HTTP.transfer_timeout`, ORA-29273 HTTP failure, `end_of_body`, or `OTHERS`), returns a canned SOAP-fault XML instead of propagating the exception, and on `OTHERS` also calls `P_INS_ERROR_DTL` + `COMMIT`.
- `f_..._Siebel_COStatusUpdate(...)` — the main entry function. Builds the header XML (`get_Header_XML_COStatusUpdate`) and per-line detail XML (`get_Details_XML_COStatusUpdate`), concatenates them into a full `UpdateOrderStatus` payload, **but the actual `generic_oms_soap_call` invocation is commented out in all four packages** (`--  l_response_payload := generic_oms_soap_call(...)`). Instead, every call site **only inserts the fully-formed outbound SOAP envelope into `OMS_PUBLISH_WS_DATA`** (queued for asynchronous delivery) rather than calling the web service synchronously. This means, as currently shipped, none of these four packages actually perform a live outbound HTTP call themselves — they only stage messages for the republish job (`OMS_MSG_WS_PUBLISHER`, §7) to send.

Differences between the four:

| Package | Data source | Event mapping |
|---|---|---|
| `OMS_ASNOUT_WS_INVOKER` | `OMS_ASNOUT_DESC/DISTRO`, `OMS_CUST_ORD_HEAD/ITEM`, `OMS_CO_FULFILL_DETAIL` | `EventId` = `DL` (delivered, when `TO_LOCATION` is null or non-stockholding) or `SH` (shipped/reserved) |
| `OMS_RECEIPT_WS_INVOKER` | `OMS_RECEIPT`/`RECEIPT_DTL`, `OMS_CO_FULFILL_DETAIL` | `EventId='RE'` ("RECEIVING") |
| `OMS_SOS_WS_INVOKER` | in-memory PL/SQL record/table types (`rec_seibel_status_head`/`_item`) passed directly by the caller rather than queried by message ID — the only one of the four that takes structured parameters instead of a message ID, implying it's meant to be called with pre-assembled data from another PL/SQL context | caller-supplied `EventId` |
| `OMS_PENDRETURN_WS_INVOKER` | `OMS_RMA_RCV_DTL`, `OMS_PENDRT_DESC` | `EventId='RTN'`/`'RT'` ("RETURNED") — fulfil location hardcoded to `'WH'`/`OMS_PENDRT_DESC.physical_wh`, source hardcoded to store `'19010'` |

All four insert into `OMS_PUBLISH_WS_DATA` (columns: `SEQ_NO` from `oms_republish_dataseq.nextval`, `APPLICATION_ID`, `FIRST_ATTEMPT_DATETIME`, `TRANSACTION_KEY` = customer order number, `XML_MSG` = full soap envelope, `WEB_SERVICE_ID`, `ATTEMPT_CNT=0`, `REPUBLISH_STATUS`), filtering source rows to `OMS_CUST_ORD_HEAD.STATUS='S'` and `ORDER_REQUESTOR_ID IN ('19010','20010','30010')` (i.e. only e-commerce/specific-channel orders, never `ORPOS`/in-store).

**Tables**: `OMS_WEBSERVICE_URI_DETAIL`, `OMS_PUBLISH_WS_DATA`, plus each package's specific source tables above.

**Transaction behavior**: no explicit commit inside the invoker functions themselves (the `INSERT INTO OMS_PUBLISH_WS_DATA` is left uncommitted, to be committed by the calling context — e.g. `OMSSUB_ASNOUT`/`OMSSUB_RECEIVING`, which don't explicitly commit either, so ultimately by the RIB framework's post-`CONSUME` commit).

**Java callers**: none found anywhere in the Java codebase for any of the four (`OMS_ASNOUT_WS_INVOKER`, `OMS_RECEIPT_WS_INVOKER`, `OMS_SOS_WS_INVOKER`, `OMS_PENDRETURN_WS_INVOKER` — zero grep hits across all ~65 modules). All four are purely PL/SQL-internal, invoked from `OMSSUB_ASNOUT`/`OMSSUB_RECEIVING` (ASN and receipt variants) or from other RMS/OMS RIB-side PL/SQL not included in this repository (SOS and pending-return variants — no caller for these two was found even within the 10 packages present here, so their caller is **not determinable from this repository**).

---

## 7. `OMS_MSG_WS_PUBLISHER`

**Classification**: Custom batch/republish package — the consumer side of the `OMS_PUBLISH_WS_DATA` queue populated by §3–6.

**Purpose**: Periodically (invoked by the shell script `oms/SCRIPT/xtra_oms_ws_republish.ksh`, which runs `sqlplus` and calls this package) drains queued outbound SOAP messages from `OMS_PUBLISH_WS_DATA` and actually sends them via `UTL_HTTP` (through the shared `OMS_WS_INVOKER.generic_oms_soap_call` — note: this references a package named plain `OMS_WS_INVOKER`, distinct from the four `OMS_*_WS_INVOKER` packages above; its source is **not present in this repository**).

**Public interface**:
```sql
PROCEDURE publish_Messages;
FUNCTION  get_Rebublish_Stop_Count RETURN OMS_SYSTEM_PARAMETERS.PARAMETER_VALUE%TYPE;
FUNCTION  get_Unpublished_Data(isv_republish_stop_count IN ..., ltv_unpublished_msgs IN OUT Unpublished_Data_Coll) RETURN BOOLEAN;
PROCEDURE delete_Publish_Data;
PROCEDURE update_Republish_Data(inv_seq_no IN ..., isv_Republish_Status IN ..., isv_error_msg IN ...);
```

**Core logic** (`publish_Messages`):
1. Reads the stop-count threshold from `OMS_SYSTEM_PARAMETERS` (`REPUBLISH_STOP_COUNT`).
2. `get_Unpublished_Data` bulk-collects (limit 5000) rows from `OMS_PUBLISH_WS_DATA` joined to `OMS_WEBSERVICE_URI_DETAIL`, filtered to `REPUBLISH_STATUS='N'`, `attempt_cnt < stop_count`, and `WEB_SERVICE_NAME IN ('SIEBEL_ORDER_CANCEL','SIEBEL_ORDER_FEED','SIEBEL_STATUS_UPDATE','SOA_CO_STATUS_UPDATE','RECEIPT_SIEBEL_CO_STATUS_UPDATE','ASNOUT_SIEBEL_CO_STATUS_UPDATE')` — i.e. this single republish job handles **all** of the Siebel-bound message families in the system, not just the ones staged by §3–6. It also joins in an `OMS_SYSTEM_PARAMETERS('SIEBEL_WS_REPUB_IND')` flag (`FLAG` column) that gates whether the send is actually attempted (`IF lrt_republish_record.FLAG = 'Y'`).
3. For each row where the flag is `Y`, calls `OMS_WS_INVOKER.generic_oms_soap_call` and extracts `UpdateOrderStatusResponse/UpdateOrderStatusResult` from the response.
4. On a `Success`/`ORPOS` result → `update_Republish_Data(seq_no, 'P', NULL)` (mark published). On any other non-null response, a SOAP fault, or an empty response → `update_Republish_Data(seq_no, 'F', <error>)` (mark failed).
5. `update_Republish_Data`: on `'F'` inserts the row into `OMS_REPUBLISH_DATA` (a separate, permanent failure-audit table — distinct from `OMS_PUBLISH_WS_DATA`, the working queue) and deletes it from the queue; on `'P'` inserts into `OMS_PUBLISH_WS_DATA_LOG` (success-audit table) and deletes from the queue. Both branches `COMMIT` explicitly.
6. `delete_Publish_Data` (present but not called from `publish_Messages` in the current source — its invocation is commented out) would purge already-published rows.

**Tables**: `OMS_PUBLISH_WS_DATA` (queue, read+delete), `OMS_PUBLISH_WS_DATA_LOG` (success audit, insert), `OMS_REPUBLISH_DATA` (failure audit, insert — this is the **same table** the Java layer writes to directly in several modules, e.g. `OmsStatusUpdateForReturnPickupCancellation.insertRecordIntoRepublishData` in `OMSUtil`, and `CancelOrderDAO.saveWSPublishReq` in `oms-cancellation` — confirming Java and this PL/SQL job share the same failure-audit table, though Java writes to `OMS_REPUBLISH_DATA` directly rather than through `OMS_PUBLISH_WS_DATA`/this package), `OMS_WEBSERVICE_URI_DETAIL`, `OMS_SYSTEM_PARAMETERS`.

**Transaction behavior**: explicit `COMMIT` at the end of the main loop in `publish_Messages`, and explicit `COMMIT` inside `update_Republish_Data` for both branches; `ROLLBACK` on `OTHERS` in the exception handlers of `publish_Messages` and `delete_Publish_Data`.

**Java callers**: none — invoked exclusively via the `xtra_oms_ws_republish.ksh` shell script (external cron/scheduler, not visible in this repository beyond the script itself).

---

## 8. `XX_DLV_ADDRESS_UPDATE`

**Classification**: Custom standalone validation/update procedure, narrowly scoped to one Java caller.

**Purpose**: Validates whether a customer order's delivery address can still be safely changed (i.e. no part of the order has begun physical fulfillment yet), and if so, updates it.

**Public interface**:
```sql
FUNCTION VALIDATE_UPDATE_REQUEST(
  P_Orderno IN Oms_Cust_Ord_Head.Cust_Order_No%Type,
  P_OmsCustOrdNo IN Oms_Cust_Ord_Head.Oms_Cust_Ord_No%Type,
  P_Classification IN Oms_Cust_Ord_Item.Ship_Classification%Type,
  P_First_Name IN Oms_Cust_Ord_Address.Deliver_First_Name%Type,
  P_Last_Name IN Oms_Cust_Ord_Address.Deliver_Last_Name%Type,
  P_Address IN Oms_Cust_Ord_Address.Deliver_Add_1%Type,
  P_Mobile IN Oms_Cust_Ord_Address.Deliver_Phone_No%Type,
  P_Response IN OUT VARCHAR2,
  P_Response_Error IN OUT Rtk_Errors.Rtk_Text%Type
) RETURN VARCHAR2;
```

**Logic**:
1. Logs every call (regardless of outcome) into `XX_DLV_ADDRESS_UPDATE_LOG`.
2. If no fulfillment record exists yet for the order (`OMS_CO_FULFILL_DETAIL` empty) → updates `OMS_CUST_ORD_ADDRESS` directly (name/address/phone, `NVL`-coalesced so only supplied fields overwrite) and returns success immediately — no further checks needed since nothing has started.
3. Else, if **any** quantity has already been delivered (`SUM(CUM_QTY_DELIVERED) > 0`) → reject with "Item already delivered".
4. Else branches on `Ship_Classification`:
   - `SMALL`: for each distinct source/fulfil-location-type combination on the order, checks whether the item has been picked in the **warehouse** transfer system (`tsfdetail`/`tsfhead` where `TSF_TYPE='CO'` and a matching `SELECTED_QTY`/`DISTRO_QTY` &gt; 0) or in **SIM** (`ful_ord@simdb`/`ful_ord_line_item@simdb` where `QUANTITY_PICKED` &gt; 0, via **database link `simdb`**) — either match rejects the address change ("Item is already picked in the Warehouse"/"in SIM"). If neither is picked, updates `OMS_CUST_ORD_ADDRESS`, `ORDCUST` (RMS's own customer-order table — **directly**, not just the OMS shadow table), and (commented out) would also update `address@simdb`.
   - `MIXED`/`BIG`: same WH/SIM pick checks, **plus** a booking-system check against `XX_DLVRY_BOOKING`/`XX_DLVRY_BOOKING_LINES` (`BOOK_STATUS='Confirmed'`) — if a confirmed booking's `REQUEST_DATE` is today or in the past, rejects ("Booking is less or equal to Current Date"); if in the future, proceeds and additionally updates `XX_DLVRY_BOOKING` (customer name/address/mobile) alongside `OMS_CUST_ORD_ADDRESS`/`ORDCUST`.
5. On any unhandled exception: `ROLLBACK`, returns failure with the SQL error message appended.

**Tables**: `OMS_CO_FULFILL_DETAIL`, `OMS_CUST_ORD_ITEM`, `OMS_CUST_ORD_ADDRESS`, `ORDCUST` (RMS core table), `tsfdetail`/`tsfhead` (RMS transfer tables), `ful_ord`/`ful_ord_line_item` **via DB link `simdb`**, `XX_DLVRY_BOOKING`/`XX_DLVRY_BOOKING_LINES`, `XX_DLV_ADDRESS_UPDATE_LOG`.

**Transaction behavior**: explicit `COMMIT` after each successful update branch; explicit `ROLLBACK` in the `WHEN OTHERS` handler.

**Java caller — confirmed, single call site**: `DeliveryUpdate` module, `DeliveryService.updateDeliveryService` (`DeliveryUpdate/src/main/java/org/extra/deliveryUpdate/DeliveryService.java:70,120`), exposed as REST `POST /update`. This is the **only** place in the entire ~4,700-file Java codebase that calls this package (confirmed by repo-wide grep from three independent research passes). Notably, `oms-core`'s `OrderDAO.updateCustomerAddress` (`POST /order/address`) implements a **separate, parallel** address-update capability directly in raw JDBC against the same tables (`OMS_CUST_ORD_HEAD`, `ORDCUST`, `OMS_CUST_ORD_ADDRESS`, `XX_DLVRY_BOOKING`) without going through this package — i.e. there are two independently-maintained Java-reachable address-update code paths in the system, one PL/SQL-backed (`DeliveryUpdate`) and one pure-Java (`oms-core`), with different validation rules.

---

## 9. `XX_REFUND_REQUEST_PKG` / `XX_REFUND_REQUEST_PKG_BODY`

**Classification**: Custom refund-orchestration package, central to the multi-channel refund flow.

**Purpose**: Creates and tracks refund requests across the system's payment channels (SADAD, Payfort, Tasheel Wallet, Benefit, AFS/FSS, COD), tied back to order cancellations.

**Public interface**:
```sql
PROCEDURE create_xrr_refund_request(P_CANCEL_HEAD_SEQ_ID, P_CANCEL_TENDER_SEQ_ID, P_REFUND_CHANNEL, P_TENDER_TYPE,
  P_TENDER_SUBTYPE, P_DECIMAL_ADJUSTMENT, P_REFUND_AMOUNT, P_COUNTRY, P_CURRENCY, P_REFUNDABLE,
  P_SADAD_BANKID, P_SADAD_UID, P_SADAD_SPTN, P_PAYFORT_FORTID, P_PAYFORT_MERCHANT_REFERENCE,
  P_TASHEEL_WALLET_CIVILID, P_TASHEEL_WALLET_REFNUMBER, P_TASHEEL_CARDNO,
  P_REQ_ADDITIONAL_ATTRIBUTE1/2/3, P_PROCESSED_BY, P_REFUND_METHOD, p_cust_order_no,
  X_REFUND_ID OUT, X_STATUS_CODE OUT, X_STATUS_MSG OUT);
PROCEDURE update_xrr_processing_status(P_REFUND_ID, P_PROCESSED_FLAG, P_PROCESSED_BY, p_refund_status,
  p_refund_date, P_ERR_CODE, P_ERR_MSG, p_RES_ADDITIONAL_ATTRIBUTE1, X_STATUS_CODE OUT, X_STATUS_MSG OUT);
PROCEDURE update_xrr_Sadad_status(P_SADAD_REFUND_ID, p_refund_status, p_refund_date,
  p_RES_ADDITIONAL_ATTRIBUTE1, p_RES_ADDITIONAL_ATTRIBUTE2);
PROCEDURE update_gl_accounting(p_Prefund_method, p_PRES_ADDITIONAL_ATTRIBUTE1, p_PRES_ADDITIONAL_ATTRIBUTE2);
```

**`create_xrr_refund_request` logic**:
1. Logs the full raw parameter set into `xx_refund_request_parameters` (audit/replay table), commits.
2. Duplicate guard: rejects (`X_STATUS_CODE='F'`) if a row already exists in `XX_REFUND_REQUEST` for the same `CUST_ORDER_NO` + `REFUND_AMOUNT`.
3. Looks up `EXT_CANCEL_HEAD.TRANSACTION_TYPE` for the given `CANCEL_HEAD_SEQ_ID` — this is the table populated by the Java `Hybris_Cancellation` module (`ext_cancel_head`, lowercase in Java's raw SQL, same table). If `TRANSACTION_TYPE='RefundOnly'` or the caller passed `P_REQ_ADDITIONAL_ATTRIBUTE1='ReadyForRefund'`, sets `ready_for_refund='Y'`.
4. Generates a UUID-style `SADAD_UID` (formatted GUID) and, for `P_REFUND_CHANNEL='SADAD'`, a `SADAD_REFUND_ID` from `XX_RR_SADAD_REFUND_ID_SEQ` prefixed with literal `134`.
5. Looks up `xx_refund_channel_config` for a `MANUAL_PROCESSING_FLAG` keyed by channel+country.
6. Inserts the full row into `XX_REFUND_REQUEST` (`refund_id` from `XX_RR_REFUND_ID_SEQ`), with `refund_status` defaulted via `decode(p_refund_channel,'COD','NA','Inprogress')`.
7. Commits; returns `X_REFUND_ID`, `X_STATUS_CODE='S'`, success message.

**`update_xrr_processing_status` logic**: logs to `xx_refund_request_param_log`; resolves the target row either directly by `P_REFUND_ID` or (when `P_REFUND_ID=0`) by a 3-second `DBMS_LOCK.sleep` then a lookup by `RES_ADDITIONAL_ATTRIBUTE1` (a wait-and-retry pattern to handle a race where the caller doesn't yet have the generated ID). Updates `processed_flag`, `refund_status`, `res_additional_attribute1`, and derives `refund_method` from a `CASE` on `(refund_channel, country, tender_subtype)` — e.g. Payfort+SA+subtype 3000/3010 → `'Payfort - CC - V. M.'`, subtype 3020 → `'AMEX'`, subtype 8000 → `'Payfort - DC - MADA'`; Payfort+OM → `'Payfort'`; FSS+BH → `'AFS'`. Sets `manual_processing_flag='Y'` when `p_refund_status='Rejected'`. On success (`processed_flag='Y'` and status in `Accepted`/`Processed`), additionally sets `refund_ref_number` (SADAD → the SADAD refund id; Payfort → merchant reference + refund id; else → the customer order number) and `refunded_amount = refund_amount`.

**`update_xrr_Sadad_status`**: SADAD-specific webhook-style callback — updates `refund_status`, `refund_date` (parsed from a `T`-separated ISO-ish string), two response-attribute columns, and sets `manual_processing_flag='Y'` when status is `'Expired'`, matched by `sadad_refund_id`.

**`update_gl_accounting`**: updates `refund_method`/`res_additional_attribute2` on rows matched by `RES_ADDITIONAL_ATTRIBUTE1` where `REFUND_STATUS='Processed'` — a GL/accounting-system feedback hook.

**Tables**: `xx_refund_request_parameters` (raw-input audit), `XX_REFUND_REQUEST` (main refund record), `xx_refund_request_param_log` (status-update audit), `xx_refund_channel_config` (manual-processing-flag lookup by channel/country), `EXT_CANCEL_HEAD` (read-only, transaction-type lookup).

**Transaction behavior**: explicit `COMMIT` after each logical unit of work in every procedure; no explicit `ROLLBACK` except implicitly via the `WHEN others` handler in `create_xrr_refund_request` (which just sets an error status/message and returns — no `ROLLBACK` statement, so the earlier `INSERT INTO xx_refund_request_parameters` + its `COMMIT` at step 1 persists even if the rest of the procedure subsequently fails).

**Java caller — confirmed, single call site**: `Hybris_Cancellation` module, `HybrisCancellationServImpl.insertDetailInMuleTableProcedure` (`Hybris_Cancellation/src/main/java/org/logicinfo/hybriscancellation/serviceImpl/HybrisCancellationServImpl.java:633-634`), called once per refund line from `POST /createCancellation`. Confirmed by three independent research passes (cancellation, RMA, and payment/finance clusters) that this is the **only** Java call site for `create_xrr_refund_request` in the repository; the other three procedures (`update_xrr_processing_status`, `update_xrr_Sadad_status`, `update_gl_accounting`) have **no Java caller anywhere in this repository** — they are presumably invoked from RMS-side PL/SQL callbacks or an external payment-gateway/GL integration not included in this codebase (see `unknowns.md`).

---

## 10. `XXHDB_CORE_PKG`

**Classification**: Custom scheduling/reservation package for the Hybris e-commerce "Home Delivery Booking" (HDB) feature — same-day/scheduled delivery slot management.

**Purpose**: Computes available delivery time-slots per region/item-group, and manages a two-phase reserve→confirm booking workflow (plus cancellation), for large/bulky/installation-requiring items ordered through the Hybris storefront.

**Public interface**:
```sql
PROCEDURE get_schedule(p_item_list VARCHAR2, p_city VARCHAR2,
  o_groups OUT ref_cursor, o_items OUT ref_cursor, o_schedule OUT ref_cursor);
PROCEDURE reserve_booking(p_reservation_id, p_Hybris_Cust_nbr, p_group, p_order_lines IN xxhdb_tbl_type,
  p_request_date, p_window, p_slots, p_region, p_cust_city, p_cust_area, p_cust_name, p_cust_addr,
  p_cust_mobile, p_cust_lat, p_cust_long, p_status OUT, p_msg OUT);
PROCEDURE create_booking(p_reservation_id, p_Hybris_Cust_nbr, p_cust_nbr, p_group, p_order_lines,
  p_request_date, p_window, p_slots, p_region, p_cust_city, p_cust_area, p_cust_name, p_cust_addr,
  p_cust_mobile, p_cust_lat, p_cust_long, p_booking_id OUT, p_status OUT, p_msg OUT);
PROCEDURE confirm_booking(p_reservation_id, p_Hybris_Cust_nbr, p_cust_order_no,
  p_booking_id OUT, p_status OUT, p_msg OUT);
PROCEDURE cancel_booking(p_reservation_id, p_Hybris_Cust_nbr, p_status OUT, p_msg OUT);
```
Note: `bds-service`'s Java DAO (see `categories/integration-notification-other.md`) calls procedures named `RESERVE_BOOKING`, `CONFIRM_BOOKING`, `CANCEL_BOOKING`, and `BOOK_ORDER` — the Java-visible names track this package's core operations closely (case-insensitive match on the first three), but `BOOK_ORDER` does not appear in this package's source; either it is an additional overload/entry point added in a version of this package not captured verbatim in this checkout, or Java calls a related-but-different package for the `POS_CO` order-type path. **Flagged as not fully reconcilable from the two sources examined; requires DB verification.** `OMSCustomerOrder`'s Java caller (see `categories/order-management.md`) calls `reserve_booking_small` — again a variant name not present verbatim in this package body, suggesting **overloaded or size-qualified variants of these procedures exist in the deployed database that are not present in this repository's copy of the source.**

**`get_schedule` logic**: resolves the delivery region from `p_city` via `xx_dlvry_cities`/`xx_dlvry_regions_v`; opens the caller-supplied `p_item_list` as a ref cursor (dynamic SQL — the caller supplies a literal `SELECT` fragment, embedded into a larger dynamically-built query, `q'[...]' || v_item_list || q'[...]']'`); computes each line's fulfillment-group (`XXHDB_GROUPS_MERCH_MATRIX_V`, keyed by department/class/install-service-flag) and lead-time (`xx_dlvry_dc_lead_time`) for the requested source/fulfil-location pair; flags `SEEC`-restricted items (`xx_seec_items`, always excluded by the current `1=2` guard — effectively disabled) and unmapped/"missing location" combinations (`v_rpt_locations`); builds and executes dynamic SQL against `XXHDB_SCHEDULE_V` (a view, not a base table — the actual slot/capacity data source) per group/region/window/date, applying an `available` flag derived from `xx_dlvry_dc_lead_time_f` (a function, not shown in this package) and hardcoded city-specific day-of-week logic (city `Abqiq` only bookable on Wednesdays). Every call is logged to `xx_dlvry_log`.

**`reserve_booking` logic**: logs to `xx_dlvry_resrv_log`; validates the city/area combination exists in `xx_dlvry_regions_v`; rejects duplicate `reservation_id` (`XXhdb_EC_RESVE`); rejects past request dates; inserts one `XXHDB_EC_RESVE` row per order line (`status='PENDING'`), then recomputes each line's `group_id` (re-deriving from `XXHDB_GROUPS_MERCH_MATRIX_V`/`item_master`, with a fallback re-derivation using `install_srv_flag='N'` when the item is AC-related per `xx_dlvry_ac_ins_items_v` but no group-3 rows exist) and recomputes `slots` per group (group 3/9 = sum of qty excluding AC items; other groups = `ceil(qty*2/3)`, floored at 1; AC-only lines fall back to their own summed qty). For each distinct group/date/window combination, calls `xx_dlvry_pkg.XX_DLVRY_CALENDAR` (external function, not in this package) to re-check slot availability against the requested `p_slots`, rejecting if insufficient. On success, flips all `PENDING` rows for the reservation to `RESERVED`. On any failure, deletes any partially-created `xx_dlvry_booking`/`xx_dlvry_booking_lines` rows, marks the `XXHDB_EC_RESVE` rows `ERROR`, and logs the message.

**`create_booking` logic**: re-derives slot counts for any stale `PROCESSED`/`RESERVED` rows with `slots=0`; normalizes the customer mobile number (`xx_dlvry_pkg.mobile_format`) and falls back to `OMS_CUST_ORD_HEAD` for phone/name if not supplied; validates the request date is not in the past and that the customer order (`OMS_CUST_ORD_HEAD` by `cust_order_no`, `STATUS='S'`) exists; creates one `xx_dlvry_booking` header row (`BOOK_STATUS='Confirmed'` immediately — no separate "pending booking" state at this level, `store_code` hardcoded `'ESK'`, `src='E-COMMERCE'`, `source_booking='HYBRIS'`) and one `xx_dlvry_booking_lines` row per order line (validated to exist on the real order via `oms_cust_ord_item`, and deduped against any other non-cancelled booking already covering the same order/line).

**`confirm_booking` logic**: for each distinct group/date/window/etc. combination under the reservation, re-collects its `RESERVED` lines into an in-memory `xxhdb_tbl_type` array and calls `create_booking` for that group; on success, updates the corresponding `XXHDB_EC_RESVE` rows to `PROCESSED` with the resulting `BOOK_ID` and the now-known `CUST_ORDER_NBR`.

**`cancel_booking` logic**: sets `xx_dlvry_booking.BOOK_STATUS='Cancelled'` for every non-cancelled booking linked (via `xx_dlvry_booking_lines`) to the reservation+Hybris-customer-number, and sets `XXHDB_EC_RESVE.STATUS='Cancelled'`.

**Tables**: `xx_dlvry_cities/regions(_v)/areas/zones/groups`, `XXHDB_GROUPS_MERCH_MATRIX_V`, `xx_dlvry_dc_lead_time`, `xx_seec_items`, `v_rpt_locations`, `XXHDB_SCHEDULE_V`, `xx_dlvry_log`, `xx_dlvry_resrv_log`, `XXHDB_EC_RESVE`, `xx_dlvry_ac_ins_items_v`, `XX_DLVRY_AC_ITEMS`, `xx_dlvry_booking`/`xx_dlvry_booking_lines`, `oms_cust_ord_head`/`item`, `xx_item_details`, `rms14.item_master`.

**Transaction behavior**: explicit `COMMIT` after most logging/status-update steps (frequent, fine-grained commits rather than one commit per public procedure call); explicit `ROLLBACK` in the `WHEN v_exp`/`WHEN others` exception handlers of `reserve_booking`/`create_booking`, paired with manual cleanup deletes before the rollback (belt-and-suspenders — the deletes are largely redundant with the rollback for anything not yet committed, but necessary for anything committed by an earlier fine-grained `COMMIT` in the same call).

**Java callers**: this is the **most Java-integrated** of the 10 packages — confirmed callers in three different modules across two business clusters:
- `bds-service` (`BookingDAO.java`, Integration/Notification cluster) — the primary, current integration: `get_schedule`, `RESERVE_BOOKING`/`BOOK_ORDER`, `CONFIRM_BOOKING`/`BOOK_ORDER`, `CANCEL_BOOKING`, plus the legacy duplicate WARs `BDS/{slotBookingAvailability,bookingCreation,bookingConfimation,bookingDeletion}`.
- `oms-cancellation` (`CancelOrderDAO.cancelOddBooking`, Cancellation cluster) — calls `XXHDB_CORE_PKG.cancel_booking_small` (again, a `_small`-suffixed variant not present verbatim in this package body) when `deliveryMode='ODDSMALL'`.
- `OMSCustomerOrder` (`OMSCustomerOrderBean.oddPackageCall`, Order Management cluster) — calls `XXHDB_CORE_PKG.reserve_booking_small` (same naming caveat) during `processNewOrder` when `deliveryModeType='ODDSMALL'` and `deliveryType='S'`.

The consistent appearance of a `_small`/`BOOK_ORDER` naming variant across three independent Java call sites, none of which is present in this package body, strongly suggests the live database has additional overloaded procedures in `XXHDB_CORE_PKG` for a "small item" on-demand-delivery (ODD) booking path that supplements the "large item scheduled slot" path documented above from the source actually present in this repository. **This is flagged in `unknowns.md` as requiring database verification.**
