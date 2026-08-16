# API ↔ PL/SQL Mapping Matrix

Consolidated from the eight business-category reports in `categories/`. Every API found in the repository appears exactly once. Where an API invokes multiple PL/SQL packages/procedures or touches multiple tables, all are listed (not just the first). "Purpose" is a one-line summary — see the linked category file for full request/response fields, business rules, and file:line citations.

Legend for **PL/SQL or SQL** column: package/procedure names in `CODE` are real stored-procedure calls (`CallableStatement`/`{call ...}`); "raw SQL" means the Java layer performs SELECT/INSERT/UPDATE/DELETE directly with no stored-procedure call; "JPA" means persistence goes through the shared `OMSUtilSessionEJB` entity-bean layer with no visible SQL text in the calling module.

## 1. Order Management — [full detail](categories/order-management.md)

| API | Method/Operation | Java Entry Point | Java Service | DAO/DB Layer | PL/SQL or SQL | Main Tables | Purpose |
|---|---|---|---|---|---|---|---|
| CustomerOrderService | SOAP `requestNewCustomerOrderId` | `CustomerOrderPortTypeImpl:89` | `GenerateNewOrderId` | raw JDBC | `select oms_cust_id_seq.nextval from dual` | (sequence) | Issue new OMS order ID for ORPOS |
| CustomerOrderService | SOAP `cancelNewCustomerOrderId` | `CustomerOrderPortTypeImpl:107` | — | — | — | — | Stub, no-op |
| CustomerOrderService | SOAP `createCustomerOrder` | `CustomerOrderPortTypeImpl:121` | `CustOrdCreateBean` | JPA (`OMSUtilSessionEJB`) | none | `OMS_CUST_ORD_HEAD/ITEM/TENDER`, `OMS_ORPOS_*` | Persist ORPOS-created customer order |
| CustomerOrderService | SOAP `queryCustomerOrder` | `CustomerOrderPortTypeImpl:219` | `QueryCustOrdBean` | JPA | none | `OMS_ORPOS_CUST_ORDER_HEAD/ITM` | Query order by ID/customer/card token |
| CustomerOrderService | SOAP `pickupCustomerOrderItems` | `CustomerOrderPortTypeImpl:260` | `PickCustOrdItemBean` | JPA | none | `OMS_ORPOS_CUST_ORDER_PICKUP/ITM_PICKUP`, `OMS_CO_FULFILL_DETAIL` | In-store pickup and cancellation |
| CustomerOrderService | SOAP `returnCustomerOrderItems` | `CustomerOrderPortTypeImpl:311` | `ReturnCustOrdBean` | JPA | none | `OMS_ORPOS_CUST_ORDER_RTN/ITM_RT` | In-store return recording |
| CustomerOrderService | SOAP `updateReceipt`, `ping` | `CustomerOrderPortTypeImpl:341,354` | — | — | — | — | Stubs, unimplemented |
| OMSCustomerOrder | SOAP `processNewOrder` | `OMSCustomerOrderWebServiceImpl:95` | `OMSCustomerOrderBean` | JPA + raw JDBC (failed-order audit) | `XXHDB_CORE_PKG.reserve_booking_small` (ODD only) | `OMS_CUST_ORD_HEAD/ITEM/ADDRESS`, `OMS_RTLOG_PUBLISH_LOG`, `oms_order_create_response_head/item` | E-commerce/Hybris/NOON order intake, RMS/SIM reservation, Siebel notify, ODD slot booking |
| OMSCustomerOrder | SOAP `processBackOrder` | `OMSCustomerOrderWebServiceImpl:221` | `BackOrder` | raw JDBC/JPA | `OMS_INVADJ_STATUS_UNAVIALINV` | `OMS_BACK_ORDER_DTL`, batch progress table | Trigger back-order batch matching |
| oms-customer-order | REST `POST /createNewOrder` | `EcomOrderController:18` | `EComOrderServiceImpl` | none | none | none | **Non-functional stub** — validates but never persists, always returns success |
| oms-customer-order | REST `GET /back-order` | `BackOrderController:33` | — | in-memory stats | none | none | Batch status |
| oms-customer-order | REST `POST /back-order` | `BackOrderController:45` | `BackOrderService`/`BackOrderProcess` | `BackOrderDAO` | `OMS_INVADJ_STATUS_UNAVIALINV`, `XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN` | `OMS_BACK_ORDER_DTL`, `OMS_BACK_ORDER_BATCH_CHECK`, `OMS_CUST_ORD_RESERVE`, `OMS_CO_FULFILL_DETAIL` | Back-order batch: stock match, RMS/SIM/Carrera reservation |
| oms-core | REST `GET/PUT/POST /fulfilment` | `FulfilmentController` | `FulfilmentService` | `FulfilmentDAO` | `XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN` | `OMS_CO_FULFILL_DETAIL` | Manual fulfilment search/create/cancel |
| oms-core | REST `POST/PUT/GET /bulkfulfilment` | `BulkFulfilmentController` | `FulfilmentService` | `FulfilmentDAO` | (same, per-row) | same | Excel-driven bulk fulfilment |
| oms-core | REST `GET /order*`, `POST /order/address` | `OrderController` | `OrderService` | `OrderDAO` | raw SQL | `OMS_CUST_ORD_HEAD/ITEM/ADDRESS`, `OMS_CO_CANCEL_*`, `OMS_RMA_REQ*`, `XX_DLVRY_BOOKING*` | Order search/detail/address-verify/cancel/return/booking views |
| oms-core | REST `GET /user`, `POST /login` | `UserController`/`LoginController` | `UserService`/`JWTService` | `UserDAO` | raw SQL | `OMS_USER_INFO`, `OMS_ROLE_INFO` | Internal SPA auth (JWT) |
| oms-core | REST `GET /util/*` | `UtilController` | `UtilService` | `UtilDAO` | raw SQL | `STORE`, `WH`, `SUPS` | Reference-data lookups |
| oms-core | REST `GET /einvoice*`, `/ebs`, `/b2bpos` | `ReportController`/`EBSController`/`B2BPOSController` | `EInvoicingService` | `EInvoicingDAO` | raw SQL | `XX_EINV_XML_GAZT_REP`, `XX_B2B_EBS_EINV_REP`, `XX_POS_B2B_EINV` | E-invoicing (ZATCA) reporting |

## 2. Cancellation — [full detail](categories/cancellation.md)

| API | Method/Operation | Java Entry Point | Java Service | DAO/DB Layer | PL/SQL or SQL | Main Tables | Purpose |
|---|---|---|---|---|---|---|---|
| OMSCOCancellationWebService | SOAP `processCancellationOrder` | `OMSCOCancellationWebServiceImpl:57` | `COCancellationBean` | EJB (JPA) + raw JDBC | raw SQL (`XX_EINV_CAN_RET_OMS`) | `OMS_CO_CANCEL_HEAD/ITEM`, `OMS_CUST_ORD_HEAD/ITEM/TENDER` | Legacy SOAP order-cancellation; superseded by oms-cancellation |
| oms-cancellation | REST `POST /omscancellation` | `OrderCancelController:31` | `OrderCancellationService` | `CancelOrderDAO` | `XX_IS_CANCELLABLE`, `OMS_INVADJ_STATUS_UNAVIALINV`, `XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN`, `XXHDB_CORE_PKG.cancel_booking_small`, `XX_JOOD_MEM_TRANSACTION.XX_JOOD_TRANSACTION_PROCESS` | `XX_ORD_CANCELLATION_REQ`, `OMS_CO_CANCEL_HEAD/ITEM/FO_CANCEL`, `OMS_CUST_ORD_*`, `OMS_BACK_ORDER_DTL`, `OMS_REPUBLISH_DATA` | **Current/active** order-cancellation API — drives RMS/SIM SOAP + PL/SQL |
| Hybris_Cancellation | REST `POST /createCancellation` | `HybrisCancellationController:35` | `HybrisCancellationServImpl` | raw JDBC | `XX_REFUND_REQUEST_PKG.create_xrr_refund_request` | `ext_cancel_head/item/tender`, `oms_cust_ord_head/tender` | Hybris cancel/refund intake; forwards cancel to oms-cancellation |
| oms-cancellation-job | Batch (`main()`) | `ServiceOrderCancelApplication.main` | `OrderCancelService` | `ServiceOrderDetailsDAO` | none (Feign to oms-cancellation) | `XX_CC_SERVICE_CANCEL_DTL` | Scheduled sweep of pending service-order cancellations |
| SparePartsCancelRequestService | SOAP `SparePartsCancelRequestOperation` | `SparePartsCancelRequestImpl:32` | `SparePartsCancelBean` | EJB (JPA) + `SparePartDAO` | none (raw SQL insert/select) | `OMS_SPARE_PART_HEADER/CANCEL_HDR/FULFILL/AUDIT`, `OMS_SIEBEL_CANCELLATION` | Cancel spare-parts fulfilment, reverse SIM inventory/transfer |

## 3. Returns / RMA — [full detail](categories/returns-rma.md)

| API | Method/Operation | Java Entry Point | Java Service | DAO/DB Layer | PL/SQL or SQL | Main Tables | Purpose |
|---|---|---|---|---|---|---|---|
| RMA Generation | SOAP `generateNewRMA` | `RMAGenerationWebServiceImpl:47` | `RMAGenerationBean` | `OMSUtilSessionEJB` (JPA) + raw JDBC | `XX_JOOD_MEM_TRANSACTION.XX_JOOD_TRANSACTION_PROCESS` | `OMS_RMA_REQ/REQ_ITEM`, `OMS_CUST_ORD_ITEM`, `ITEM_MASTER` | Create RMA header+lines, notify RWMS |
| RMA Modification | SOAP `modifyRMA` | `RMAModifyWebServiceImpl:36` | `OMSRMAModificationBean` | `OMSUtilSessionEJB` (JPA) | none | `OMS_RMA_MOD_HEAD/DETAIL`, `OMS_RMA_REQ/REQ_ITEM` | Modify RMA quantity, notify RWMS |
| RMA Deletion | SOAP `deleteRMA` | `RMADeletionWebServiceImpl:39` | `RMADeletetionBean` | `OMSUtilSessionEJB` (JPA) | none | `OMS_RMA_DEL_HEAD/DETAIL`, `OMS_RMA_REQ/REQ_ITEM` | Delete/cancel RMA line(s), notify RWMS |

All three call RWMS `PendingReturnsService` (`pendReturnDtlCreate`/`Modify`/`Delete`), URL resolved from `OMS_WEBSERVICE_URI_DETAIL` — none call `OMS_PENDRETURN_WS_INVOKER` or `XX_REFUND_REQUEST_PKG`.

## 4. Fulfillment / SIM / Delivery — [full detail](categories/fulfillment-sim-delivery.md)

| API | Method/Operation | Java Entry Point | Java Service | DAO/DB Layer | PL/SQL or SQL | Main Tables | Purpose |
|---|---|---|---|---|---|---|---|
| SIMDeliveryDetail | SOAP `processNewOrder` | `SIMDeliveryDetailWebServiceImpl:27` | `SIMDeliveryDetailBean` | raw JDBC + JPA | none | `SIM_DEL_DETAILS_RTS`, `SIM_DEL_DETAILS`, `ordcust`, `Ful_Ord_Dlv` | Fetch ready-to-ship/undelivered SIM order+delivery details |
| sim-dispatch | REST `POST /dispatch` | `DispatchController:28` | `DispatcherService` | `SIMDispatchDAO` | raw SQL + SIM SOAP `createFulfillmentOrderDelivery`/`dispatchFulfillmentOrderDelivery` | `FUL_ORD`, `FUL_ORD_LINE_ITEM` | Create+dispatch a SIM fulfillment order delivery |
| DeliveryUpdate | REST `POST /update` | `DeliveryController:21` | `DeliveryService` | raw JDBC `CallableStatement` | **`XX_DLV_ADDRESS_UPDATE.VALIDATE_UPDATE_REQUEST`** | (via package: `tsfdetail`/`tsfhead`, `ful_ord@simdb`, `XX_DLVRY_BOOKING`) | Validate/update order delivery address |
| CarreraTransferCreation | REST `POST /NewTransferCreation` | `TransferCreationController:25` | `TransferCreationDAO` | raw JDBC `CallableStatement` | `rms14.xx_tsf_cre_carera_sql.xx_dc_tsf_cre` (not in repo) | RMS transfer tables | Create a Carrera store/DC transfer |
| label_Package_Creation | REST `POST /createLablePackage`, `GET /ping` | `LabelCreationController` | `LableCreationServImpl` | raw JDBC `CallableStatement` (direct DB connection) | `XX_CREATE_LABEL` (not in repo) | none visible | Generate shipping label CLOB from XML |

## 5. Inventory / Spare Parts / Stock — [full detail](categories/inventory-spare-parts.md)

| API | Method/Operation | Java Entry Point | Java Service | DAO/DB Layer | PL/SQL or SQL | Main Tables | Purpose |
|---|---|---|---|---|---|---|---|
| Stockcheck | REST `POST /inventory` | `StockcheckController:22` | `LookUpInventoryServiceImpl` | `NamedParameterJdbcTemplate` | raw SQL | `XX_OMS_INV`, `XX_OMS_INVAVAIL_V_BACK`, `GET_PACK_QTY_V`, `OMS_BACK_ORDER_DTL` | Real-time ATS check for e-commerce |
| Stock_Feed | REST `POST /physicaldeltafeed`,`/futureinventoryfeed`,`/packitemfeed` | `StockFeedController` | `StockFeedServiceImpl` | `StockFeedDaoImpl` | raw SQL | `TEST_ITEM_STOCK_DTL_V`, `XX_TEST_INVAVAIL_V`, `STOCK_FEED` | Delta/future/pack-item stock feed |
| OmsOrposInventoryCheck | SOAP `checkInventory` | `OmsOrposInventoryCheckWebServiceImpl:31` | `InventoryCheckBean` | `OMSUtilSessionEJB` (JPA) + raw JDBC | JPA + SOAP to SIM `StoreInventoryService` | `OMS_FULFILL_MATRIX_EXT_DETAIL`, `XX_OMS_INVAVAIL_V_BACK` | ORPOS inventory availability check (legacy) |
| OmsOrposInventoryCheckNew | SOAP `checkInventory`,`refreshParams` | `OmsOrposInventoryCheckWebServiceImpl:42,65` | `InventoryCheckService` | `DataAccessDAO` | raw SQL + `GET_CLASSIFICATION_BO_TEST` (function) | `ITEM_MASTER`, `OMS_FULFILL_MATRIX_EXT_HEAD/DETAIL`, `XX_OMS_POS_INV_V` | Rewritten batched ORPOS inventory check |
| spareParts-stockRequest | REST `POST /api/stock-return-request`,`/stock-receiving`,`/stock-deducting`,`/stock-cancellation` | `StockRequestController` | `SparePartsService` | `SparePartsDAO` | raw SQL + SOAP `IOracleSIMClient` | `xx_sp_stock_req_ret[_item]`, `xx_spare_parts_hdr/item/cancel` | Technician spare-parts stock workflow |
| SparePartsRequest | SOAP `SparePartsRequestOperation` | `SparePartsRequestImpl:37` | `SparePartsRequestBean` | `OMSUtilSessionEJB` (JPA) + raw JDBC | JPA + SOAP to SIM `InventoryAdjustmentService`/`StoreToStoreTransferService` | `OMS_SPARE_PART_HEADER/FULFILL/AUDIT` | Reserve/transfer inventory for spare-parts request |
| SparePartsConfirmation | SOAP `SparePartsConfirmationOperation` | `SparePartsConfirmationPortTypeImpl:44` | `SparePartsConfirmationBean` | `OMSUtilSessionEJB` (JPA) + raw JDBC | JPA + SOAP to SIM `InventoryAdjustmentService` (+ async republish) | `OMS_SPARE_PART_CONFIRM_HDR/DTL`, `OMS_REPUBLISH_DATA` | Confirm/deduct reserved spare-parts inventory |

## 6. Payment / Finance / Pricing / E-Invoicing — [full detail](categories/payment-finance-pricing-einvoicing.md)

| API | Method/Operation | Java Entry Point | Java Service | DAO/DB Layer | PL/SQL or SQL | Main Tables | Purpose |
|---|---|---|---|---|---|---|---|
| PaymentConfirmation | SOAP `processPaymentConf` | `PaymentConfirmationWebServiceImpl:61` | `OMSCustomerOrderBean` | JPA + raw JDBC (`PersistPaymentRequest`, `RMSPackage`) | `OMS_INVADJ_STATUS_UNAVIALINV` | `Oms_PaymentConf_Audit`, `OMS_CUST_ORD_RESERVE`, `OMS_CUST_ORD_TENDER` | Confirm payment, unreserve/confirm RMS+SIM fulfillment, notify Siebel |
| VASContract | SOAP `processVASContract` | `VASContractWebServiceImpl:38` | (none — direct JDBC) | direct JDBC | raw SQL insert | `VAS_CONTRACTS_ECOM` | Persist a value-added-service contract record |
| VASContract/SIMDeliveryDetail | SOAP `processNewOrder` | `SIMDeliveryDetailWebServiceImpl:29` | `SIMDeliveryDetailBean` | raw JDBC (cross-schema, DB link `@rmsdb`) | none | `Ful_Ord_Dlv`, `Ful_Ord`, `OMS_CUST_ORD_HEAD@rmsdb` | Fetch SIM delivery detail (duplicate module of #4 above) |
| apple-pricing / boot-apple-pricing | REST `GET /pricing` | `PricingController.updatePricingDetail` | `PricingService` | `PricingDAO` | `XX_APPLE_ON_PRICING_SQL.APPLE_MPN_REFRESH` | `XX_APPLE_STORE_PS_DETAIL` | Refresh active Apple MPNs from Apple pricing API |
| apple-pricing / boot-apple-pricing | REST `POST /pricing` | `PricingController.publishPricingDetail` | `PricingService` | `PricingDAO` | `XX_APPLE_ON_PRICING_SQL.POST_PUBLISH_UPDATE` | `XX_APPLE_STORE_ITEM_PRICES`, `XX_APPLE_STORE_PS_DETAIL` | Push updated Apple price sheets |
| oms-discount | REST (see category doc) | — | — | — | raw SQL | discount tables | Discount rule evaluation |
| oms-finance | scheduled job | — | — | — | raw SQL | contract-reconciliation tables | Financial contract reconciliation batch |
| einvoicing | scheduled jobs (`generateB2CXml`,`reportB2CInvoice`,`generateSiebelInvoice`,`resendEmail`, +2 more) | `EInvoicingService.*` | `B2C*Service`/`B2CInvoicingProcess`, `B2B*Service` | `B2CInvoicingDAO`,`B2BInvoicingDAO`,`BaseDAO` | raw SQL + local ZATCA signing + `IZatcaInterfaceAPI` | `XX_EINV_XML_GAZT_REP`, `XX_EINV_CAN_DETAILS_CLR_V`, `XX_EINV_RMA_DETAILS_CLR_V`, `XX_EINV_RECONCIL_HEAD/DETAIL`, `XX_B2B_EBS_EINV_REP` | ZATCA (Saudi e-invoicing) B2C/B2B clearance, reporting, email |
| oms-einvoicingxml | REST `POST /api/hybristranxml` | `XmlController.saveXmlInfo` | `HybrisOmsXmlService` | `HybrisOMSXmlDAO` | raw SQL insert | `XX_EINV_XML_GAZT_REP` | Ingest pre-built ZATCA invoice XML from Hybris |

## 7. Integration / Notification / Other — [full detail](categories/integration-notification-other.md)

| API | Method/Operation | Java Entry Point | Java Service | DAO/DB Layer | PL/SQL or SQL | Main Tables | Purpose |
|---|---|---|---|---|---|---|---|
| bds-service | REST `POST/PUT/DELETE /booking` | `BookingController` | `BookingService` | `BookingDAO` | **`XXHDB_CORE_PKG.RESERVE_BOOKING`/`CONFIRM_BOOKING`/`CANCEL_BOOKING`/`BOOK_ORDER`** | `OMS_ORDER_BOOKING_HEAD_INFO`, `XX_DLVRY_REGIONS_V` | Home delivery slot reservation/confirm/cancel |
| bds-service | REST `POST /available`,`/available/order` | `AvailablityController` | `BookingService` | `BookingDAO` | **`XXHDB_CORE_PKG.get_schedule`** | `ITEM_MASTER`, `OMS_FULFILL_MATRIX_EXT_*` | Delivery slot search |
| BDS (legacy) | REST (4 separate WARs) | `Slots/Booking*Controller` | `*DAO` | same | same `XXHDB_CORE_PKG` procs | same | Superseded duplicate of bds-service |
| jood | REST `POST /membership/eligibility` | `MembershipController` | `JOODService` | `JOODDAO` | `XX_JOOD_ELIGIBILITY_CHECK`/`XX_JOOD_POS_ELIGIBILITY_CHECK`/`XX_JOOD_CB_ELIG_CHECK` | `XX_JOOD_*` | Jood loyalty eligibility/capping check |
| jood | REST `POST /transaction` | `TransactionController` | `JOODService` | `JOODDAO` | `XX_JOOD_MEM_TRANSACTION.XX_JOOD_TRANSACTION_PROCESS` | — | Record Jood cashback transaction |
| jood | REST `POST /cb/history`,`/cbstatus` | `MembershipCb*Controller` | `JOODService` | `JOODDAO` | `XX_JOOD_CB_TRAN_HISTORY_V1`, `XX_JOOD_CB_STATUS` | — | Cashback history/status |
| jood-membership-sync | batch | `JoodSyncApplication.main` | `JoodSyncService` | `JoodDetailsDAO` + Feign | raw SQL | `XX_JOOD_MEMBERSHIP(_TYPE)` | Publish new VIP members to Jood CloudHub |
| home-maintenance-sync | batch | `HomeMaintenanceSyncApplication.main` | `HomeMaintenanceSyncService` | `HomeMaintenanceDetailsDAO` + Feign | raw SQL | `XX_SUBSCRIPTION_DETAILS(_TYPE)` | Publish maintenance subscriptions to Jood CloudHub |
| NOON ExtraServiceRest | REST `POST /customer` | `ExtraRestService` | `ExtraReturnStoreService` | `ExtraReturnStoreDaoImpl` | raw SQL + SIM SOAP `processPOSTransactions` | `OMS_RMA_REQ(_ITEM)`, `OMS_RMA_RCV_DTL` | Ingest noon.com return, post RETURN txn to SIM |
| NOON transferCreation | REST `POST /NewTransferCreation` | `TransferCreationController` | `TsfCreationDAO` | `TransferCreationDAO` | `XTRA_TSF_CRE_SQL.XTRA_XTSF_CRE` | `TSFHEAD`, `ITEM_LOC` | Create NOON stock transfer |
| NOON transferReceive | REST `POST /receive/cust` | `TransferReceiveController` | `TransferReceiveServiceImpl` | `TransferReceiveDaoImpl` | raw SQL + RIB SOAP `publishReceiptCreateUsingReceiptDesc` | `TSFDETAIL`, `SHIPSKU`, `SHIPMENT` | Receive NOON transfer, publish receipt to RIB |
| AwbCustomerNotification | REST `POST /awb/create` | `AwbController` | `AwbTrackingServiceImpl` | `AwbTrackingDAOImpl` | raw SQL | `OMS_AWB_HEADER_INFO/DETAIL_INFO` | Register courier tracking number for order |
| AwbOrder-Notification / Order-Notification | batch | `AwbNotificationOrder.main`/`StoreManagerNotification.main` | inline | `EmailNotificationDAO`/`SmsNotificationDAO` | raw SQL | `OMS_AWB_HEADER_INFO` | Email/SMS shipment tracking notifications |
| notification | batch | `NotificationUtil.main` | `NotificationService` | `NotificationTemplateDAO` | raw SQL | `ESU_DETAIL`, `ESU_CONFIG`, SIM `FUL_ORD` | Generic config-driven notification engine |
| inactive_Track_id | batch | `InActiveCancellationImpl.main` | inline | `TrackingOrderList` | `INACTIVE_TRACKING_ECOMM_ORDERS` | `INACTIVE_ORDER_TRACKING_STATUS`, `xx_awb_status_upload` | Invalidate stale AWB tracking numbers |
| ecom-reconcilliation | REST `POST /product`,`/price`,`/promotion` | `ReconciliationController` | `RPMReconciliationService` | `RPMReconiliationDAO` + Feign | raw SQL | `XX_ITEM_RMS_HYBRIS_RECON`, `XX_ITEM_PRICE_HYBRIS_STAGE`, `XX_PROMO_INFO_HYBRIS_STAGE` | Reconcile RMS/RPM catalog vs Hybris |
| cross-channel-service | REST `POST /offlineorders`,`/servicesoffered` | `CorssChannelController` | `CrossChannelService` | `CrossChannelDAO` | raw SQL (view) | `XX_CC_ALL_TRAN_DETAILS`, `XX_CC_XCM_SERVICES_V` | POS purchase history / cross-channel VAS lookup |
| proxy-api | REST `GET/POST/PUT` (generic) | `ProxyController` | `ProxyService` | — (relay) | none | none | Outbound HTTP relay (⚠ SSRF surface, see security notes) |
| imei-validation | REST `POST /validate` | `IMEIController` | `IMEIService` | `IMEIDao` | `XX_IMEI_SN_UPLOAD_VALIDATE.IMEI_SN_UPLOAD_VALIDATE` | — | Validate item↔IMEI pairing |
| extra-imei-capture | REST (11 SIM-plugin ops) | `UniqueSerialNumberController` | `UniqueSerialNumberService` | `UniqueSerialNumberImpl` | `XX_IMEI_SN_UPLOAD_VALIDATE...@rmsdb`, `XX_IMEI_SN_CONFIG_VALIDATE.XX_IMEI_SN_CONF_VAL` | `XX_FUL_ORD_DLV_LINE_ITEM_UIN` | SIM plugin: capture/validate serials at fulfilment |
| CR_17_ExtraAsCourier | SOAP `CreateShipment` | `ExtraCourierWebServiceImpl` | `CreateLabelCall` | JNDI `jdbc/wms` | `XX_HB_MAKE_LABEL` | — | Self-issue AWB/shipping label from WMS |
| label_Package_Creation | REST `POST /createLablePackage` | `LabelCreationController` | `LableCreationServImpl` | same | `XX_CREATE_LABEL` | — | REST label creation (alt. to CR_17) |

## 8. Core Gateway (client-stub libraries, no server side) — [full detail](categories/core-gateway-oracleintegrationservices.md)

`OracleIntegrationServices` (legacy, orphaned from the build), `oracle-base` (current, Maven-managed), and `oms-common` (Feign wrappers + shared DTOs) contain **zero DB access and zero PL/SQL references** — they are outbound SOAP/REST client-stub libraries consumed by the modules in sections 1–7 above. See the "Architectural determination" section of the linked doc for full evidence. Their operations are already reflected in the "PL/SQL or SQL" / external-system columns of the tables above wherever a consuming module uses them (e.g. `IOracleRMSClient`, `IOracleSIMClient`, `ICarreraClient`, `IMuleAddressClient`, the `IStatus`/`Status` Siebel `UpdateOrderStatus` client).

---

# PL/SQL → API Reverse Mapping

Every custom PL/SQL package documented in `plsql-packages.md`, with its confirmed Java callers (or explicit confirmation of none) and the business function it serves.

| PL/SQL Package | Procedure/Function | Called By Java Class | Called By API | Business Function | Tables |
|---|---|---|---|---|---|
| `OMSSUB_ASNOUT` | `CONSUME` / `CONSUME_SHIPMENT` | **none** (RIB-triggered only) | n/a — RMS RIB dequeue framework | Consume ASN-out/shipment RIB message, update fulfillment/delivery state | `OMS_ASNOUT_*`, `OMS_CUST_ORD_HEAD/ITEM`, `OMS_CO_FULFILL_DETAIL`, `OMS_CUST_ORD_LOG*`, `OMS_RTLOG_PUBLISH_LOG`, `OMS_SPARE_PART_*` |
| `OMSSUB_RECEIVING` | `CONSUME` | **none** (RIB-triggered only) | n/a — RMS RIB dequeue framework | Consume Receiving RIB message, reserve received stock for order/spare-part | `OMS_RECEIPT*`, `OMS_CO_FULFILL_DETAIL`, `OMS_CUST_ORD_LOG*`, `OMS_SPARE_PART_*` |
| `OMS_ASNOUT_WS_INVOKER` | `f_ASNOUT_Siebel_COStatusUpdate` | **none** | Called from `OMSSUB_ASNOUT` (PL/SQL→PL/SQL) | Stage Siebel order-status-update SOAP payload for ASN/shipment events | `OMS_WEBSERVICE_URI_DETAIL`, `OMS_PUBLISH_WS_DATA` |
| `OMS_RECEIPT_WS_INVOKER` | `f_REC_Siebel_COStatusUpdate` | **none** | Referenced (call commented out) in `OMSSUB_RECEIVING` | Stage Siebel order-status-update SOAP payload for receiving events | same |
| `OMS_SOS_WS_INVOKER` | `f_REC_Siebel_COStatusUpdate` (record/table param variant) | **none found** | Not determinable — no caller present in this repository | Stage Siebel status update from caller-supplied in-memory data | same |
| `OMS_PENDRETURN_WS_INVOKER` | `f_REC_Siebel_COStatusUpdate` | **none found** | Not determinable — no caller present in this repository (RMA modules confirmed *not* to call it) | Stage Siebel status update for pending-return/RMA-receipt events | `OMS_RMA_RCV_DTL`, `OMS_PENDRT_DESC`, plus common tables above |
| `OMS_MSG_WS_PUBLISHER` | `publish_Messages` | **none** (shell-script/cron triggered: `xtra_oms_ws_republish.ksh`) | n/a — scheduled batch | Drain `OMS_PUBLISH_WS_DATA` queue, actually send the Siebel SOAP calls via `UTL_HTTP` | `OMS_PUBLISH_WS_DATA`, `OMS_PUBLISH_WS_DATA_LOG`, `OMS_REPUBLISH_DATA` |
| `XX_DLV_ADDRESS_UPDATE` | `VALIDATE_UPDATE_REQUEST` | `DeliveryService` (`DeliveryUpdate` module) | REST `POST /update` | Validate + apply a delivery-address change, gated on fulfillment/pick status | `OMS_CO_FULFILL_DETAIL`, `OMS_CUST_ORD_ADDRESS`, `ORDCUST`, `tsfdetail/head`, `ful_ord@simdb`, `XX_DLVRY_BOOKING*` |
| `XX_REFUND_REQUEST_PKG` | `create_xrr_refund_request` | `HybrisCancellationServImpl` (`Hybris_Cancellation` module) | REST `POST /createCancellation` | Create a channel-specific refund request tied to a cancellation | `XX_REFUND_REQUEST`, `xx_refund_request_parameters`, `EXT_CANCEL_HEAD` |
| `XX_REFUND_REQUEST_PKG` | `update_xrr_processing_status`, `update_xrr_Sadad_status`, `update_gl_accounting` | **none found** | Not determinable — presumably called from an external payment-gateway/GL callback not in this repository | Track refund processing/settlement status | `XX_REFUND_REQUEST`, `xx_refund_request_param_log` |
| `XXHDB_CORE_PKG` | `get_schedule` | `BookingDAO` (`bds-service`, + legacy `BDS/*`) | REST `POST /available`, `/available/order` | Compute delivery-slot availability by region/item-group | `XXHDB_SCHEDULE_V`, `XXHDB_GROUPS_MERCH_MATRIX_V`, `xx_dlvry_*` |
| `XXHDB_CORE_PKG` | `reserve_booking` / `RESERVE_BOOKING` | `BookingDAO` (`bds-service`) | REST `POST /booking` | Phase 1: reserve a delivery slot | `XXHDB_EC_RESVE`, `xx_dlvry_resrv_log` |
| `XXHDB_CORE_PKG` | `create_booking`/`confirm_booking` / `CONFIRM_BOOKING` | `BookingDAO` (`bds-service`) | REST `PUT /booking` | Phase 2: confirm the reservation into a real booking | `xx_dlvry_booking(_lines)`, `XXHDB_EC_RESVE` |
| `XXHDB_CORE_PKG` | `cancel_booking` / `CANCEL_BOOKING` | `BookingDAO` (`bds-service`) | REST `DELETE /booking` | Cancel a booking/reservation | `xx_dlvry_booking(_lines)`, `XXHDB_EC_RESVE` |
| `XXHDB_CORE_PKG` | `reserve_booking_small` (variant, not in repo source) | `OMSCustomerOrderBean.oddPackageCall` | SOAP `processNewOrder` (`OMSCustomerOrder`) | Same-day "ODD small item" slot reservation at order-creation time | not determinable (variant not in repo) |
| `XXHDB_CORE_PKG` | `cancel_booking_small` (variant, not in repo source) | `CancelOrderDAO.cancelOddBooking` | REST `POST /omscancellation` (`oms-cancellation`) | Cancel a same-day "ODD small item" booking on order cancellation | not determinable (variant not in repo) |

**Custom PL/SQL called from Java that is *not* one of the 10 ground-truth packages** (discovered during tracing, listed here because Java treats it with equal importance — source not in this repository, so full internals are `Not determinable from repository`):

| Package.Procedure | Called by | Purpose (inferred from call site) |
|---|---|---|
| `OMS_SHIP_CLASSIFICATION` (function) | `FindNextfulfillLoc` (CustomerOrderService/CustomerOrderBeanPOS/OMSCustomerOrder), `OMSUtilCommons` | Classify an item/order line's shipment size (SMALL/BIG/MIXED) for fulfillment routing |
| `OMS_INVADJ_STATUS_UNAVIALINV` | `RmsPackage`/`BackOrder` (OMSCustomerOrder), `BackOrderDAO` (oms-customer-order), `CancelOrderDAO` (oms-cancellation), `RMSPackage` (PaymentConfirmation) | Adjust RMS inventory status for a reservation/unreservation |
| `XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN` (function) | `InterfacePersistence` (CustomerOrderBeanPOS), `NonSADADPayment` (OMSCustomerOrder), `BackOrderDAO` (oms-customer-order), `FulfilmentDAO` (oms-core), `CancelOrderDAO` (oms-cancellation) | Approve/cancel a "Carera" (warehouse-to-warehouse) transfer |
| `XX_IS_CANCELLABLE` | `CancelOrderDAO` (oms-cancellation) | Determine whether an order/line is eligible for cancellation, compute refund amount |
| `XX_JOOD_MEM_TRANSACTION.XX_JOOD_TRANSACTION_PROCESS` | `CancelOrderDAO` (oms-cancellation), `RMAGenerationBean` (OMSRMAGeneration), `JOODDAO` (jood) | Record a Jood loyalty transaction (purchase/return/cancel) |
| `XX_JOOD_ELIGIBILITY_CHECK` / `XX_JOOD_POS_ELIGIBILITY_CHECK` / `XX_JOOD_CB_ELIG_CHECK` | `JOODDAO` (jood) | Jood loyalty eligibility/cashback-capping check |
| `XX_JOOD_CB_TRAN_HISTORY_V1` / `XX_JOOD_CB_STATUS` | `JOODDAO` (jood) | Jood cashback history/status lookup |
| `XX_APPLE_ON_PRICING_SQL.APPLE_MPN_REFRESH` / `POST_PUBLISH_UPDATE` | `PricingDAO` (apple-pricing/boot-apple-pricing) | Apple product-pricing refresh/publish |
| `rms14.xx_tsf_cre_carera_sql.xx_dc_tsf_cre` | `TransferCreationDAO` (CarreraTransferCreation) | Create a Carrera DC transfer (distinct from `XTRA_OMS_TSF_CAN_SQL` above — creation vs. cancellation) |
| `XX_CREATE_LABEL` | `LableCreationServImpl` (label_Package_Creation) | Generate a shipping label CLOB |
| `XX_HB_MAKE_LABEL` | `CreateLabelCall` (CR_17_ExtraAsCourier) | Generate a shipping label + self-issued AWB number |
| `XTRA_TSF_CRE_SQL.XTRA_XTSF_CRE` | `TransferCreationDAO` (NOON/transferCreation) | Create a NOON marketplace stock transfer |
| `XX_IMEI_SN_UPLOAD_VALIDATE.IMEI_SN_UPLOAD_VALIDATE` (incl. `@rmsdb` variant) | `IMEIDao` (imei-validation), `UniqueSerialNumberImpl` (extra-imei-capture) | Validate an item/IMEI pairing |
| `XX_IMEI_SN_CONFIG_VALIDATE.XX_IMEI_SN_CONF_VAL` | `UniqueSerialNumberImpl` (extra-imei-capture) | Validate IMEI/serial-capture configuration for a store |
| `INACTIVE_TRACKING_ECOMM_ORDERS` | `TrackingOrderList` (inactive_Track_id) | Identify e-commerce orders with stale/inactive tracking numbers |
| `GET_CLASSIFICATION_BO_TEST` (function) | `DataAccessDAO` (OmsOrposInventoryCheckNew) | Batched item shipment-classification lookup |
