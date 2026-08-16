# Integration / Notification / Miscellaneous Cluster — Technical Trace Report

All paths relative to `/home/user/websvcs/oms/CODE/`. All webapps are Spring MVC (XML-less, `AnnotationConfigWebApplicationContext`) WARs deployed with their own `web.xml`, mapped at context root `/`. Batch modules are plain `main()` jars invoked by shell/cron.

## System identification (evidence-based)

- **BDS / bds-service** — "Home Delivery Booking" front door for the Hybris storefront. Every operation calls PL/SQL package `XXHDB_CORE_PKG` (ground truth) for slot search, reservation, confirmation, cancellation, POS-checkout booking. `BDS/*` is an older set of 4 separate WARs (`slotBookingAvailability`, `bookingCreation`, `bookingConfimation`, `bookingDeletion`); `bds-service` is a newer consolidated WAR exposing the same capability set plus delivery-charge calc.
- **NOON** — noon.com marketplace integration. `ExtraServiceRest` receives noon.com return notifications and posts a `RETURN` POS transaction into Oracle Retail SIM via `POSTransactionService` SOAP. `transferCreation`/`transferReceive` handle stock transfers to/from a NOON fulfilment location, the latter publishing receipts into RIB's `ReceivingPublishingService` SOAP endpoint.
- **Jood (jood, jood-membership-sync, home-maintenance-sync)** — eXtra's loyalty/cashback/VIP-membership platform, hosted externally on MuleSoft CloudHub. `jood` is the **inbound** REST facade RMS/OMS exposes so the loyalty engine can check eligibility/cashback and post transactions (backed by `XX_JOOD_*` PL/SQL packages). `jood-membership-sync`/`home-maintenance-sync` are **outbound** batch jobs pushing new membership/subscription rows to the Jood cloud API via Feign.
- **AWB (AwbCustomerNotification, AwbOrder-Notification, Order-Notification, inactive_Track_id)** — "Air Waybill" courier tracking-number ingestion/notification/cleanup pipeline. `AwbCustomerNotification` is inbound REST intake for SIM/WMS to register a tracking number; `AwbOrder-Notification`/`Order-Notification` are batch jobs emailing/SMS-ing tracking info via the MuleSoft "communication" API; `inactive_Track_id` invalidates stale tracking numbers via an external tracking-hub DELETE API.
- **cross-channel-service** — read-only lookup letting e-commerce/Hybris see a customer's POS purchase history and cross-channel VAS eligibility by mobile number, from reconciled views `XX_CC_ALL_TRAN_DETAILS`/`XX_CC_XCM_SERVICES_V`.
- **CR_17_ExtraAsCourier / label_Package_Creation** — eXtra acting as its own last-mile courier: SOAP/REST endpoints calling WMS PL/SQL packages (`XX_HB_MAKE_LABEL`, `XX_CREATE_LABEL`) to generate a shipping label + self-issued airway-bill number.
- **extra-imei-capture, imei-validation** — Oracle Retail SIM plugin + standalone REST service for capturing/validating IMEI/serial numbers during store fulfilment, backed by `XX_IMEI_SN_*` PL/SQL (some invoked over DB link `@rmsdb` from SIM into RMS).
- **ecom-reconcilliation** — reconciles RMS/RPM item, price, promotion data against Hybris catalog via the MuleSoft "reconciliation-data-api".
- **notification** — newer, generic, config-table-driven (`ESU_DETAIL`/`ESU_CONFIG`) multi-threaded replacement for the AWB/Order notification batches, additionally cross-referencing SIM for delivery pick status.
- **proxy-api** — a generic outbound HTTP relay (JSON/XML, GET/POST/PUT) forwarding to a caller-supplied target URL.

---

## `XXHDB_CORE_PKG` and named PL/SQL package call sites

| PL/SQL package.procedure | Caller class.method | File : line |
|---|---|---|
| `XXHDB_CORE_PKG.get_schedule` | `BookingDAO.getAvailablity` | `bds-service/.../dao/BookingDAO.java:194` |
| `XXHDB_CORE_PKG.RESERVE_BOOKING` | `BookingDAO.reservation` | `bds-service/.../dao/BookingDAO.java:368` |
| `XXHDB_CORE_PKG.CONFIRM_BOOKING` | `BookingDAO.confirmBooking` | `bds-service/.../dao/BookingDAO.java:431` |
| `XXHDB_CORE_PKG.CANCEL_BOOKING` | `BookingDAO.cancelBooking` | `bds-service/.../dao/BookingDAO.java:480` |
| `XXHDB_CORE_PKG.BOOK_ORDER` | `BookingDAO.booking` | `bds-service/.../dao/BookingDAO.java:595` |
| `XXHDB_CORE_PKG` (same 4 ops) | `SlotsAvailCheckDAO`, `BookingCreationDAO`, `BookingConfirmationDAO`, `BookingDeletionDAO` | legacy `BDS/*/…/controller/*DAO.java` (duplicate/older implementation) |
| `XX_SHIPPING_CHARGE.CALCULATESHIPPINGCHARGES` (function) | `BookingDAO.getDeliveryCharges` | `bds-service/.../dao/BookingDAO.java:913` |
| `OMSSUB_ASNOUT`, `OMSSUB_RECEIVING`, `OMS_ASNOUT_WS_INVOKER`, `OMS_RECEIPT_WS_INVOKER`, `OMS_SOS_WS_INVOKER`, `OMS_PENDRETURN_WS_INVOKER`, `OMS_MSG_WS_PUBLISHER` | — | **Not referenced from any Java source in this cluster** (grep across all 16 modules returns nothing) |
| `XX_DLV_ADDRESS_UPDATE` | `DeliveryService` | `DeliveryUpdate` module — **outside this cluster** |
| `XX_REFUND_REQUEST_PKG` | `HybrisCancellationServImpl` | `Hybris_Cancellation` module — **outside this cluster** |

`XXHDB_CORE_PKG`'s caller is confirmed to be **BDS / bds-service**.

---

### `POST /booking`, `PUT /booking`, `DELETE /booking` — bds-service (Home Delivery Booking)
- **Entry point**: `BookingController` — `bds-service/.../controller/BookingController.java:26-39`.
- **Chain**: `BookingController` → `BookingService.slotBooking/confirmBooking/cancelBooking` → `BookingDAO.reservation|booking / confirmBooking / cancelBooking`.
- **DB**: XXHDB_CORE_PKG calls above; DAO also resolves city/area/region IDs from `XX_DLVRY_REGIONS_V`.
- **Persistence of the exchange**: every call first inserts the raw JSON request into `OMS_ORDER_BOOKING_HEAD_INFO` (seq `XX_BOOK_SEQ`) and later updates the same row with the JSON response — request/response audit log, not the booking itself.
- **Validation**: order number, customer details, delivery-id, booking reservation-id/group-id/window/date/items all mandatory (error codes E-0001…E-0010); `hasACInstallMismatch` rejects carts mixing AC (dept 402) items with/without matching installation SKUs (E-705); `validateCityArea` rejects unknown city/area combos (400).
- **Response codes**: E-505 invalid item, E-615 missing source/fulfil location, E-905 no delivery slot available.
- **Request fields**: mandatory `orderNo`, `customerDetails`, `deliveries[].deliveryId`; booking calls additionally require `deliveries[].bookings[].reservationId/groupId/windowCode/date/items[].itemCode`. Optional `orderType` (`POS_CO` triggers item-grouping + `BOOK_ORDER` instead of `RESERVE_BOOKING`/`CONFIRM_BOOKING`), `deliveryChargeInd`.
- **Transactions**: no `@Transactional`; each `CallableStatement`/update auto-commits independently — failure mid-way can leave an orphaned `OMS_ORDER_BOOKING_HEAD_INFO` row with `STATUS='N'`.
- **Legacy equivalents**: `BDS/slotBookingAvailability` `POST /schedule`, `BDS/bookingCreation` `POST /booking`, `BDS/bookingConfimation` `PUT /booking/{orderNo}`, `BDS/bookingDeletion` `DELETE /booking/{orderNo}` — same PL/SQL targets, superseded by `bds-service`.

### `POST /available`, `POST /available/order` — bds-service (slot availability)
- **Entry point**: `AvailablityController.getItemsAvailablity`/`getAvailablityByOrder` — `bds-service/.../controller/AvailablityController.java:15-23`.
- **Chain**: `BookingService.getItemsAvailablity`/`getAvailablityByOrder` → for `POS_CO` order type: `BookingDAO.updateRequestLocation` (source/fulfilment-location resolution against `ITEM_LOC_CFA_EXT`, `OMS_FULFILL_MATRIX_EXT_HEAD/DETAIL`, `XX_OMS_INVAVAIL_V`, remote DB-link `XX_OMS_INV@SIMDB`) — else `BookingDAO.getAvailablity` (`XXHDB_CORE_PKG.get_schedule`).
- **Business rule**: for `getAvailablityByOrder`, if item's `ShipClassification` resolves to `SMALL`, request rejected (E-505 "Shipclassifcation is SMALL") — small items are delivery-charge-only, not slot-bookable.
- **DB**: `ITEM_MASTER` (dept 2005/402/411 install-flag lookup), `OMS_SYSTEM_PARAMETERS` (`SHIPPING_CHARGE`/Express Delivery param).

### `POST /membership/eligibility` — jood
- **Entry point**: `MembershipController.validateEligibility` — `jood/.../controller/MembershipController.java:29-46`.
- **Chain**: `JOODService.validateEligibility` → `JOODDAO.checkEligibility`, branches on `transaction.cashbackCheck`: `Y` → `validateCashBackEligibility` → `{ call XX_JOOD_CB_ELIG_CHECK(...) }` (array `XX_JOOD_CB_ELIG_DET_TBL`, result `XX_JOOD_CB_RESULT_DET_TBL`); otherwise → `validateEligibility` → `{ call XX_JOOD_POS_ELIGIBILITY_CHECK(...) }` (channel ORPOS) or `{ call XX_JOOD_ELIGIBILITY_CHECK(...) }` (other channels), Oracle object array types in/out.
- **Request** (`MembershipInfo`/`TransactionInfo`): mandatory `activeMembershipID`, `membershipTypeID`, `transaction.{transactionNumber,orderNumber,channel,date,location,country,customer.{...},totalRetailPrice,totalRetailDiscount,totalJoodDiscount,totalSellingPrice,transactionLineItems[]}`; `lineNumber` required only when `channel=ORPOS`. Optional `cashbackCheck`, `renewUpgradeDetails`.
- **Response**: status/respCode/message, per-line `CappingCheckResult`, aggregate cashback totals, `isFirstPurchaseAvail`, `membershipProgram`.
- **Transaction**: `@Transactional` — single PL/SQL call per request.

### `POST /transaction` — jood
- **Entry point**: `TransactionController.saveTransactionDetail` — `jood/.../controller/TransactionController.java:29-48`.
- **Chain**: `JOODService.saveTransactionDetail` → `JOODDAO.saveTransactionDetail` → `{ CALL XX_JOOD_MEM_TRANSACTION.XX_JOOD_TRANSACTION_PROCESS(?×16, OUT status, message, joodTranId, joodTranSeqNo, errorMessage) }`, line items as `XX_JOOD_TRANDTL_TBL` Oracle array.
- **Mandatory fields**: `activeMembershipID`, `channel`, `transactionType`, `date`, `orderNumber`, `transactionNumber`, totals, `transactionLineItems[].*`, `membershipProgram`.
- **Business rule**: throws if PL/SQL `status` ≠ "Success"; controller returns `resCode=ENTRY_FAILED`.

### `POST /cb/history` — jood
- `MembershipCbHistoryController.checkCashBackHistory` → `JOODService.getCbHistoryTransaction` → `JOODDAO.getCbHistoryTransaction` → `{ call XX_JOOD_CB_TRAN_HISTORY_V1(...) }` — paginated (`offSet`/`size`).

### `POST /cbstatus` — jood
- `MembershipCbStatusController.checkCashBackStatus` → `JOODService.getTransactionCbStatus` → `JOODDAO.getTransactionCbStatus` → `{ call XX_JOOD_CB_STATUS(...) }`.
- **Code defect noted**: parameter 6 (transaction date) is hardcoded to literal string `"16-09-2025"` instead of `request.getTransaction().getDate()` (`JOODDAO.java:447`) — caller-supplied date silently ignored.

### `jood-membership-sync` — VIP membership publish batch
- **Entry point**: `JoodSyncApplication.main`.
- **Chain**: `JoodSyncService.updateJoodMembership` → `JoodDetailsDAO.getJoodMemberShipDetails` — `SELECT M.*, T.* FROM XX_JOOD_MEMBERSHIP M JOIN XX_JOOD_MEMBERSHIP_TYPE T ... WHERE M.STATUS='A' AND M.PUBLISH_IND='N'` → per row, `JoodAPI.joodVipmembership` (Feign `POST /vipmembership`, CloudHub `customer-profile-v3`, basic auth, TLS verification **disabled**) → on `"S"` response, `updatePublishInd` sets `PUBLISH_IND='S'`.
- **No retry/backoff**: failed rows stay `PUBLISH_IND='N'` and retry indefinitely (no retry-count column, unlike `home-maintenance-sync`).

### `home-maintenance-sync` — Home Maintenance subscription publish batch
- **Entry point**: `HomeMaintenanceSyncApplication.main`; scheduled via `run_homemaintenance_sync.ksh`.
- **Chain**: `HomeMaintenanceSyncService.updateHomeMaintenance` → `HomeMaintenanceDetailsDAO.getHomeMaintenanceMemberShipDetails` — `SELECT ... FROM XX_SUBSCRIPTION_DETAILS M JOIN XX_SUBSCRIPTION_TYPE T ... WHERE M.STATUS='A' AND M.PUBLISH_IND='N' AND M.RETRY_COUNT < 6` → `HomeMaintenanceAPI.homemaintenanceVipmembership` (Feign `POST /jood/api/homemaintenance/vip/subscription/sync`, CloudHub `jood-api`) → success → `updatePublishInd`; failure → `updateFailure` (`RETRY_COUNT+1`), capped at 6.

### `GET /info`, `POST /customer` — NOON ExtraServiceRest (return-to-store)
- **Entry point**: `ExtraRestService` — `NOON/ExtraServiceRest/.../controller/ExtraRestService.java:29-71`.
- **Chain**: `POST /customer` → `ExtraReturnStoreDao.insertRequestData` (conditional insert into `noon_return_request`, gated by system parameter `NOON_RETURN`/`REQUEST_INSERT`) → `checkCustomerData` (validates `OMS_CUST_ORD_HEAD.STATUS='S' AND ORD_PAYMENT_STATUS='S'`, then delivered/returned quantities; rejects if `delQty=0` or `qty>delQty` or `qty>ordQty`, or non-marketplace already fully returned) → on success: `insertCustomerData` (`OMS_RMA_REQ`), `insertItemData` (`OMS_RMA_REQ_ITEM`), `insertOMSDetails` (`OMS_RMA_RCV_DTL`) → `ExtraReturnStoreService.callWebservice`: builds SIM `PosTrnColDesc`/`PosTrnDesc`/`PosTrnItm` (transaction_code `RETURN`) and invokes **SOAP `POSTransactionService.processPOSTransactions`**.
- **Transaction behaviour**: each DAO method opens its own connection — multi-step write is **not atomic**; failure after `insertCustomerData` leaves a partial RMA with no rollback.
- **Request fields**: mandatory `customerOrderNo`, `itemCode`, `lineNo`, `orderQty`, `refundAmount`, `returnLocId`; optional `returnReqId`, `marketPlaceInd`.

### `POST /NewTransferCreation` — NOON transferCreation
- **Entry point**: `TransferCreationController.getOrderItemsResponse` — `NOON/transferCreation/.../controller/TransferCreationController.java:23-106`.
- **Chain/validation**: `TsfCreationDAO.transferExists` (dedup by `TSFHEAD.COMMENT_DESC = refNo`) → `findIncorrectItem`/`findInvalidItem` (item-exists/active-status checks) → `findStockPerLoc` (`XX_OMS_INVAVAIL_V.AVAIL_QTY` sufficiency check) → `getResponse` → `{? = call XTRA_TSF_CRE_SQL.XTRA_XTSF_CRE(OUT tsf_no, item/qty array[XXTSF_TBL_TYPE], src_id, dest_id, refNo) }` → `saveRequestandResponse` (`OMS_NOON_TRANSFER_SERVICES` audit log).
- **Response error codes**: `invalid_items`, `inactive_items`, `insufficient_qty`.

### `POST /receive/cust` — NOON transferReceive
- **Entry point**: `TransferReceiveController.saveDOA` — `NOON/transferReceive/.../controller/TransferReceiveController.java:27-154`.
- **Chain**: `checktransferNoExist` (`TSFDETAIL`), `checkInvalidItems`, `checkInvalidQty` (vs `SHIP_QTY`), `checkReceivedQty` (vs already received), `lockReceiveRequest` (`XX_TRANSFER_LOCK_INFO` app-level advisory lock keyed by PO) → `tsfReceive` → `getBol`(`SHIPMENT`/`SHIPSKU`), `getItem` → `callwebserv`: hand-crafted **RIB SOAP envelope** POSTed to `wsURL` (RIB `ReceivingPublishingService.publishReceiptCreateUsingReceiptDesc`) with per-item `ReceiptDtl`.
- **Concurrency**: in-memory `ConcurrentHashMap` plus DB-row lock both guard against duplicate processing; lock always released in `finally`.

### `GET /awb/info`, `POST /awb/create` — AwbCustomerNotification
- **Entry point**: `AwbController` — `AwbCustomerNotification/.../controller/AwbController.java:19-61`.
- **Chain**: `AwbTrackingService.addAwb` → `AwbTrackingDAO.validateInputRequest` (dedup: `SELECT COUNT(1) FROM OMS_AWB_HEADER_INFO WHERE COURIER_NAME=? AND TRACKING_ID=? AND SOURCE=? AND STATUS='S'`) → `AwbTrackingDAOImpl.addAwb`: resolves tracking record from `XX_CARRIER_TRACKING_SIM_V` or `XX_CARRIER_TRACKING_WMS_V` (by `source=SIM|WMS`) → resolves `OMS_CUST_ORD_HEAD`→`OMS_CUST_ORD_ADDRESS` → INSERT `OMS_AWB_HEADER_INFO` → per line, resolves `OMS_CO_FULFILL_DETAIL`, unit-retail (`OMS_CUST_ORD_ITEM`/`_DISC`), distributed quantity (`XX_CARRIER_TRACKING_QTY_WMS_V`/`_SIM_V`) → batch INSERT `OMS_AWB_DETAIL_INFO` → UPDATE header `STATUS='S'|'F'`.
- **Concurrency defect**: `AwbTrackingDAOImpl` is a singleton `@Repository` storing per-request state (`awbheaderId`, `omsCustOrdNo`) in **instance fields** — concurrent calls can corrupt each other's in-flight state.
- **Transaction behaviour**: no `@Transactional`; each statement auto-commits; failure paths manually flip header status to `'F'` rather than rolling back.
- **Mandatory request fields** (`AwbRequest`): `trackingId`, `source` (SIM|WMS), `courier`.

### AWB / order notification batches (email + SMS)
- **`AwbOrder-Notification`** — `AwbNotificationOrder.main(args[0]=NORMAL|REMINDER)`: `EmailNotificationDAO.getOrderEmailInfo/getReminderEmailInfo` build bilingual (EN/AR) HTML with item/price/tracking-URL substitutions → `EmailNotification.sendMail`/`SmsNotification.sendsms` POST to `${ORDER.NOTIFICATION.ORDER.EMAIL.API}`/`_SMS.API}` (MuleSoft CloudHub `communication-v1`) using a **hardcoded prod Basic-Auth token embedded in source** → `updateEmailFlag`/`updateSmsFlag` mark header status `1` (failed) or `2` (sent).
- **`Order-Notification`** — `StoreManagerNotification.main`: opens **two** DB connections (SIM14 and OMS) and notifies store managers via the same communication API; REMINDER runs flip `SMS_PROCESS_IND`/`EMAIL_PROCESS_IND`.
- **`inactive_Track_id`** — `InActiveCancellationImpl.main` (decompiled, no original source): `getInactiveOrderListFromSim` calls `{call INACTIVE_TRACKING_ECOMM_ORDERS(?,?)}` then reads `INACTIVE_ORDER_TRACKING_STATUS WHERE PROCESS_IND='N' AND COUNT<5`; `getInactiveOrderListFromWms` reads `xx_awb_status_upload WHERE UPLOAD_STATUS='N' AND EVENT_CODE='Cancel' AND RETRY_COUNT<5` → for each, `ShipmentCancellationClient.callInactiveOrdrdelete` issues **HTTP DELETE** to `sim.invalide.tracking.id.update.api.url` with Basic auth → on 200, marks `PROCESS_IND='Y'`/`UPLOAD_STATUS='Y'`; else increments retry counter (capped at 5).

### `notification` — generic ESU-driven notification batch
- **Entry point**: `NotificationUtil.main(args[0]=NORMAL|REMAINDER)`.
- **Chain**: `NotificationService.processNotification` → `NotificationTemplateDAO.getNotificationMessages` — joins `ESU_DETAIL`/`ESU_CONFIG`/`XXK_STORE_CONTACT_V` filtered by status codes; for REMAINDER runs, also opens a **second DB connection to the SIM database** and queries `FUL_ORD.STATUS` to suppress reminders for already-picked/delivered orders — **the SIM DB password is logged in clear text at INFO level** (`NotificationTemplateDAO.java:367`).
- Fanned out across `Runtime.getRuntime().availableProcessors()` threads; each posts to `NOTIFICATION_GATEWAY_EMAIL_URL`/`_SMS_URL` (same MuleSoft communication pattern).
- `updateNotificationStatus` batches `UPDATE ESU_DETAIL SET ... WHERE SEQ_NO=?`.

### `POST /product`, `POST /price`, `POST /promotion` — ecom-reconcilliation
- **Entry point**: `ReconciliationController` — `ecom-reconcilliation/.../ReconciliationController.java:23-75`.
- **Chain**: `RPMReconciliationService.reConcileItem/Price/Promotion` → `RPMReconiliationDAO.getItemDetails/getPriceDetails/getPromotionDetails` — `SELECT ROWID,... FROM XX_ITEM_RMS_HYBRIS_RECON|XX_ITEM_PRICE_HYBRIS_STAGE|XX_PROMO_INFO_HYBRIS_STAGE WHERE PICK_STATUS='N'` batched 200 rows → `IReconciliationAPI.reConcileProduct/Price/Promotion` (Feign, CloudHub `reconciliation-data-api-v1`, TLS verification disabled) → DAO marks `PICK_STATUS='Y'`, inserts results into `XX_ITEM_RMS_HYBRIS_RECON_VALID`/`XX_ITEM_PRICE_HYBRIS_DATA`/`XX_PROMO_INFO_HYBRIS_DATA` → `updateResponse` logs API result to `XX_RETEK_API_LOG`.

### `POST /offlineorders`, `POST /servicesoffered` — cross-channel-service
- **Entry point**: `CorssChannelController` — `cross-channel-service/.../controller/CorssChannelController.java:29-59`.
- **Chain**: `CrossChannelService.processCrossChannel/processCrossChannelServicesoffered` → `CrossChannelDAO.getOfflineOrders` — `SELECT * FROM OMSDEV.XX_CC_ALL_TRAN_DETAILS WHERE CHANNEL='POS' AND TRAN_TYPE='SALE' AND QTY>0 AND MOBILENUMBER=:mobileNumber [AND ORDERNO=:orderNo] ORDER BY ORDERDATE DESC`; `getServicesOffered` — similar against `XX_CC_XCM_SERVICES_V`.
- **Read-only**; no external calls, no PL/SQL. `mobileNo` mandatory; `orderNo`/`emailId` optional filters.

### Generic HTTP proxy — proxy-api
- **Entry point**: `ProxyController` (POST/PUT/GET, JSON & XML) — mapped at root `/`.
- **Chain**: `ProxyService.postRequest/putRequest/getRequest` — reads target URL from header `apiURL`, forwards headers (minus `Host`) and an optional `auth` header (renamed `Authorization`) via OkHttp, with **no host allowlist**, TLS trust-all/hostname-verification bypassed, and **zero timeouts**.
- **Security note**: functions as an open outbound relay/SSRF surface — any caller reaching this service can make it issue arbitrary HTTP(S) calls (with attacker-supplied `Authorization`) to any host reachable from the app-tier network.

### `POST /validate` — imei-validation
- **Entry point**: `IMEIController.validateIMEI` — `imei-validation/.../controller/IMEIController.java:28-32`.
- **Chain**: `IMEIService.validateIMEI` (mandatory `item`/`imei`) → `IMEIDao.validateIMEI` → `{ CALL XX_IMEI_SN_UPLOAD_VALIDATE.IMEI_SN_UPLOAD_VALIDATE(item, imei, location, source, 'Y', OUT valid) }`.
- **Response**: `status` boolean + message, derived from `valid='Y'`.

### `extra-imei-capture` — Oracle Retail SIM plugin (IMEI/serial + ship-trailer)
- **Entry points** (`UniqueSerialNumberController`): `POST /ping`, `/sim/imei` (save), `/sim/cancel-imei`, `/sim/lookup-imei`, `/sim/update-imei-indicator`, `/sim/lookup-uin-enabled`, `/sim/lookup-imei-qty`, `/sim/delete-imei`, `/sim/delete-qty-reverted-imei`, `/sim/retreive-imei`, `/sim/check-bol`.
- **DB**: `UniqueSerialNumberImpl` reads/writes `XX_FUL_ORD_DLV_LINE_ITEM_UIN`, `XX_STORE_IMEI_SN_CONF_INFO`, `SHIPMENT_BOL`, `FUL_ORD_PICK_LINE_ITEM`; calls **`{ CALL XX_IMEI_SN_UPLOAD_VALIDATE.IMEI_SN_UPLOAD_VALIDATE@rmsdb(...) }`** — via DB link, fired from SIM database out to RMS database — and `{? = call XX_IMEI_SN_CONFIG_VALIDATE.XX_IMEI_SN_CONF_VAL(...) }`.
- **`ShipTrailerController`** (`/ship-trailer`, "Dead On Arrival"): `GET`/`POST`/`PUT /ship-trailer[...]` → `ShipTrailerService`/`ShipTrailerDAO` (DM/damaged-merchandise ship-trailer config, not fully traced given time budget).
- **`UtilController`**: `GET /util/baselv?listCode=...` — generic SIM lookup-value retrieval.

### `CreateShipment` SOAP operation — CR_17_ExtraAsCourier
- **Endpoint**: JAX-WS/SOAP 1.2, servlet `ExtraCourierWebService` mapped to `/ExtraCourierWebService`, WSDL `ExtraCourierService.wsdl`, namespace `http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto`.
- **Entry point**: `ExtraCourierWebServiceImpl.createShipment(Shipment) → ShipmentResponse` (operation `CreateShipment`).
- **Chain**: marshals `Shipment` back to XML (JAXB) → `CreateLabelCall.callWebService(xmlString, "SIMUSER")` — obtains connection from JNDI `jdbc/wms` → `{ call XX_HB_MAKE_LABEL(userId IN, xmlInput IN, labelData OUT CLOB, airwayBillNo OUT VARCHAR, errorMessage OUT VARCHAR) }`.
- **Response**: `ShipmentResponse{AirwayBillNo, LabelData, ErrorMessage}`.
- **Purpose**: eXtra self-issuing its own AWB/shipping label from WMS — acting as its own last-mile carrier. A previous procedure name `XX_MAKE_LABEL` is present commented-out — historical migration to `XX_HB_MAKE_LABEL`.

### `POST /createLablePackage`, `GET /ping` — label_Package_Creation
- **Entry point**: `LabelCreationController`.
- **Chain**: `LableCreationServImpl.getlablePackageResponse(String xml)` → `{ call XX_CREATE_LABEL(xmlInput IN, labelData OUT CLOB, errorMsg OUT VARCHAR) }`.
- **Relation to CR_17_ExtraAsCourier**: functionally adjacent (both produce a shipping label from XML against a WMS/RMS package) but targets a **different** PL/SQL package (`XX_CREATE_LABEL` vs `XX_HB_MAKE_LABEL`) and is REST (raw XML string body) rather than SOAP — an alternate/simpler REST-facing label-creation path for a different caller.

---

## Config/security observations worth flagging in the documentation

- Multiple `application.properties`/`db.properties` files across this cluster (`bds-service`, `jood-membership-sync`, `ecom-reconcilliation`, `home-maintenance-sync`, `AwbOrder-Notification`, `NOON/ExtraServiceRest`) store **plaintext Oracle DB credentials and CloudHub API client-secret/passwords** for both QA and PROD.
- `EmailNotification.java`/`SmsNotification.java` (AwbOrder-Notification) embed a **hardcoded production Basic-Auth token literal** in source rather than reading it from properties.
- `jood-membership-sync`, `ecom-reconcilliation`, `home-maintenance-sync` all disable TLS hostname verification and certificate validation on outbound Feign/OkHttp clients to CloudHub (trust-all `X509TrustManager`).
- `proxy-api` is an unauthenticated, unrestricted outbound HTTP relay (SSRF surface).
- `notification` module logs the SIM database password at INFO level.
- `AwbTrackingDAOImpl` keeps per-request state in singleton-bean instance fields — concurrency bug under multi-threaded load.
- Several DAOs (`NOON/ExtraServiceRest`, `NOON/transferCreation`) build SQL by string concatenation of caller-supplied values in some code paths — SQL-injection risk for those specific queries.

---

## Summary table

| API | Method/Operation | Java Entry Point | Java Service | DAO/DB Layer | PL/SQL or SQL | Main Tables | Purpose |
|---|---|---|---|---|---|---|---|
| Slot booking | `POST /booking` | `BookingController.slotBooking` (bds-service) | `BookingService.slotBooking` | `BookingDAO.reservation`/`booking` | `XXHDB_CORE_PKG.RESERVE_BOOKING`/`BOOK_ORDER` | `OMS_ORDER_BOOKING_HEAD_INFO`, `XX_DLVRY_REGIONS_V` | Home delivery slot reservation |
| Booking confirm | `PUT /booking` | `BookingController.confirmBooking` | `BookingService.confirmBooking` | `BookingDAO.confirmBooking`/`booking` | `XXHDB_CORE_PKG.CONFIRM_BOOKING`/`BOOK_ORDER` | same | Confirm reserved slot |
| Booking cancel | `DELETE /booking` | `BookingController.cancelBooking` | `BookingService.cancelBooking` | `BookingDAO.cancelBooking` | `XXHDB_CORE_PKG.CANCEL_BOOKING` | same | Cancel booking |
| Item availability | `POST /available` | `AvailablityController.getItemsAvailablity` | `BookingService.getItemsAvailablity` | `BookingDAO.getAvailablity`/`updateRequestLocation` | `XXHDB_CORE_PKG.get_schedule` | `ITEM_MASTER`, `OMS_FULFILL_MATRIX_EXT_*`, `XX_OMS_INVAVAIL_V` | Delivery slot search |
| Availability by order | `POST /available/order` | `AvailablityController.getAvailablityByOrder` | `BookingService.getAvailablityByOrder` | `BookingDAO.getOrderDetail`, `getAvailablity` | `XXHDB_CORE_PKG.get_schedule` | `OMS_CUST_ORD_HEAD/ITEM/ADDRESS`, `OMS_CO_FULFILL_DETAIL` | Slot search for existing order |
| Membership eligibility | `POST /membership/eligibility` | `MembershipController.validateEligibility` (jood) | `JOODService.validateEligibility` | `JOODDAO.validateEligibility`/`validateCashBackEligibility` | `XX_JOOD_ELIGIBILITY_CHECK`/`XX_JOOD_POS_ELIGIBILITY_CHECK`/`XX_JOOD_CB_ELIG_CHECK` | `XX_JOOD_*` types | Jood loyalty eligibility/capping check |
| Transaction post | `POST /transaction` | `TransactionController.saveTransactionDetail` | `JOODService.saveTransactionDetail` | `JOODDAO.saveTransactionDetail` | `XX_JOOD_MEM_TRANSACTION.XX_JOOD_TRANSACTION_PROCESS` | — | Record Jood cashback transaction |
| CB history | `POST /cb/history` | `MembershipCbHistoryController` | `JOODService.getCbHistoryTransaction` | `JOODDAO.getCbHistoryTransaction` | `XX_JOOD_CB_TRAN_HISTORY_V1` | — | Cashback transaction history |
| CB status | `POST /cbstatus` | `MembershipCbStatusController` | `JOODService.getTransactionCbStatus` | `JOODDAO.getTransactionCbStatus` | `XX_JOOD_CB_STATUS` | — | Cashback balance/status |
| Jood membership sync | batch | `JoodSyncApplication.main` | `JoodSyncService.updateJoodMembership` | `JoodDetailsDAO` + Feign `JoodAPI` | SQL only | `XX_JOOD_MEMBERSHIP(_TYPE)` | Publish new VIP members to Jood CloudHub |
| Home maintenance sync | batch | `HomeMaintenanceSyncApplication.main` | `HomeMaintenanceSyncService.updateHomeMaintenance` | `HomeMaintenanceDetailsDAO` + Feign `HomeMaintenanceAPI` | SQL only | `XX_SUBSCRIPTION_DETAILS(_TYPE)` | Publish maintenance subscriptions to Jood CloudHub |
| NOON return intake | `POST /customer` | `ExtraRestService.customerInput` | `ExtraReturnStoreService.callWebservice` | `ExtraReturnStoreDaoImpl` | raw SQL | `OMS_RMA_REQ(_ITEM)`, `OMS_RMA_RCV_DTL`, `OMS_CUST_ORD_*` | Ingest noon.com return, post RETURN txn to SIM (SOAP) |
| Transfer creation | `POST /NewTransferCreation` | `TransferCreationController` | `TsfCreationDAO` | `TransferCreationDAO` | `XTRA_TSF_CRE_SQL.XTRA_XTSF_CRE` | `TSFHEAD`, `ITEM_LOC`, `XX_OMS_INVAVAIL_V` | Create NOON stock transfer |
| Transfer receive | `POST /receive/cust` | `TransferReceiveController.saveDOA` | `TransferReceiveServiceImpl` | `TransferReceiveDaoImpl` | raw SQL + RIB SOAP `publishReceiptCreateUsingReceiptDesc` | `TSFDETAIL`, `SHIPSKU`, `SHIPMENT`, `XX_TRANSFER_LOCK_INFO` | Receive transfer, publish receipt to RIB |
| AWB create | `POST /awb/create` | `AwbController.createAwb` | `AwbTrackingServiceImpl.addAwb` | `AwbTrackingDAOImpl` | raw SQL | `OMS_AWB_HEADER_INFO`, `OMS_AWB_DETAIL_INFO` | Register courier tracking number for an order |
| AWB order notify | batch (NORMAL/REMINDER) | `AwbNotificationOrder.main` | inline | `EmailNotificationDAO`/`SmsNotificationDAO` | raw SQL | `OMS_AWB_HEADER_INFO` | Email/SMS customer shipment tracking |
| Store manager notify | batch (NORMAL/REMINDER) | `StoreManagerNotification.main` | inline | `EmailNotificationDAO`/`SmsNotificationDAO` (SIM14+OMS) | raw SQL | SIM14 + OMS tables | Email/SMS store manager order alerts |
| Generic notification | batch (NORMAL/REMAINDER) | `NotificationUtil.main` | `NotificationService.processNotification` | `NotificationTemplateDAO` | raw SQL | `ESU_DETAIL`, `ESU_CONFIG`, SIM `FUL_ORD` | Multi-threaded config-driven email/SMS engine |
| Inactive tracking cleanup | batch | `InActiveCancellationImpl.main` | inline | `TrackingOrderList` | `INACTIVE_TRACKING_ECOMM_ORDERS` | `INACTIVE_ORDER_TRACKING_STATUS`, `xx_awb_status_upload` | Invalidate stale AWB tracking numbers externally |
| Ecom item/price/promo reconciliation | `POST /product`,`/price`,`/promotion` | `ReconciliationController` | `RPMReconciliationService` | `RPMReconiliationDAO` + Feign `IReconciliationAPI` | raw SQL | `XX_ITEM_RMS_HYBRIS_RECON`, `XX_ITEM_PRICE_HYBRIS_STAGE`, `XX_PROMO_INFO_HYBRIS_STAGE` | Reconcile RMS/RPM catalog vs Hybris |
| Cross-channel offline orders | `POST /offlineorders` | `CorssChannelController.crossChannel` | `CrossChannelService.processCrossChannel` | `CrossChannelDAO.getOfflineOrders` | raw SQL (view) | `XX_CC_ALL_TRAN_DETAILS` | POS purchase history lookup by mobile |
| Cross-channel services offered | `POST /servicesoffered` | `CorssChannelController.crossChannelServicesoffered` | `CrossChannelService.processCrossChannelServicesoffered` | `CrossChannelDAO.getServicesOffered` | raw SQL (view) | `XX_CC_XCM_SERVICES_V` | Cross-channel VAS eligibility lookup |
| Generic HTTP proxy | GET/POST/PUT (JSON/XML) | `ProxyController` | `ProxyService` | — (OkHttp relay) | none | none | Outbound HTTP relay to caller-supplied URL |
| IMEI validation | `POST /validate` | `IMEIController.validateIMEI` | `IMEIService.validateIMEI` | `IMEIDao.validateIMEI` | `XX_IMEI_SN_UPLOAD_VALIDATE.IMEI_SN_UPLOAD_VALIDATE` | — | Validate item↔IMEI pairing |
| SIM IMEI capture | `POST /sim/imei` etc. (11 ops) | `UniqueSerialNumberController` | `UniqueSerialNumberService` | `UniqueSerialNumberImpl` | `XX_IMEI_SN_UPLOAD_VALIDATE...@rmsdb`, `XX_IMEI_SN_CONFIG_VALIDATE.XX_IMEI_SN_CONF_VAL` | `XX_FUL_ORD_DLV_LINE_ITEM_UIN`, `XX_STORE_IMEI_SN_CONF_INFO` | SIM plugin: capture/validate serials at fulfilment |
| Ship trailer | `GET/POST/PUT /ship-trailer` | `ShipTrailerController` | `ShipTrailerService` | `ShipTrailerDAO` | not fully traced | not fully traced | DOA/damaged-merchandise ship-trailer config |
| eXtra as courier | SOAP `CreateShipment` | `ExtraCourierWebServiceImpl.createShipment` | `CreateLabelCall.callWebService` | JNDI `jdbc/wms` | `XX_HB_MAKE_LABEL` | — | Self-issue AWB/shipping label from WMS |
| Label package creation | `POST /createLablePackage` | `LabelCreationController` | `LableCreationServImpl` | same class | `XX_CREATE_LABEL` | — | REST label creation (alt. to CR_17) |

## Key file references
- BDS/Home Delivery Booking: `bds-service/src/main/java/com/extra/bds/{controller,service,dao}/*.java`; legacy `BDS/{slotBookingAvailability,bookingCreation,bookingConfimation,bookingDeletion}/src/main/java/**/controller/*.java`
- Jood: `jood/src/main/java/com/extra/jood/{controller,service,dao}/*.java`, `jood-membership-sync/src/main/java/com/extra/jood/**`, `home-maintenance-sync/src/main/java/com/extra/homemaintenance/**`
- NOON: `NOON/ExtraServiceRest/src/main/java/com/extra/restservice/**`, `NOON/transferCreation/src/main/java/org/logicInfo/oms/transferCreation/Controller/**`, `NOON/transferReceive/src/main/java/com/logicinfo/transfer/receive/**`
- AWB: `AwbCustomerNotification/src/main/java/com/logicinfo/awb/**`, `AwbOrder-Notification/src/main/java/com/order/notification/**`, `Order-Notification/src/main/java/com/order/notification/**`, `inactive_Track_id/inactive_Track_id/src/main/java/com/logicinfo/extra/store/**`
- `notification/src/main/java/com/extra/notification/**`
- `ecom-reconcilliation/src/main/java/com/extra/ecom/reconcilliation/**`
- `cross-channel-service/src/main/java/com/extra/oms/crossChannel/**`
- `proxy-api/src/main/java/com/extra/proxy/**`
- `imei-validation/src/main/java/com/extra/imei/**`, `extra-imei-capture/src/main/java/com/extra/sim/**`
- `CR_17_ExtraAsCourier/ExtraCourierBean/src/org/datacontract/schemas/_2004/_07/extra_services_shipping/**`
- `label_Package_Creation/src/main/java/org/logicinfo/label/creation/**`

Items not determinable from the repository: `ShipTrailerDAO`'s exact SQL/PL/SQL calls (not opened, time-bounded); `OMSSUB_ASNOUT`, `OMSSUB_RECEIVING`, `OMS_*_WS_INVOKER`, `OMS_MSG_WS_PUBLISHER` have no Java call sites anywhere in this cluster; the exact deployed WebLogic context roots are set at deploy time and not present in the repo.
