## Report: OracleIntegrationServices / oracle-base / oms-common — Core Gateway Layer

### 1. Full Inventory

**All 26 `@WebService`/`@WebServiceClient`-annotated classes in `OracleIntegrationServices`** (all under `OracleIntegrationServices/src/`). Every one is a JAX-WS RI 2.1.5 **generated** artifact — none contain hand-written business logic.

| # | File | Class | Annotation | Namespace |
|---|---|---|---|---|
| 1 | `com/oracle/retail/rms/integration/services/fulfillorderservice/v1/FulfillOrderPortType.java` | `FulfillOrderPortType` | `@WebService` (SEI) | `.../rms/integration/services/FulfillOrderService/v1` |
| 2 | `.../fulfillorderservice/v1/FulfillOrderService.java` | `FulfillOrderService` | `@WebServiceClient` (factory) | same |
| 3 | `com/oracle/retail/rms/integration/services/inventorybackorderservice/v1/InventoryBackOrderPortType.java` | `InventoryBackOrderPortType` | `@WebService` | `.../rms/integration/services/InventoryBackOrderService/v1` |
| 4 | `.../inventorybackorderservice/v1/InventoryBackOrderService.java` | `InventoryBackOrderService` | `@WebServiceClient` | same |
| 5 | `com/oracle/retail/rwms/integration/services/pendingreturnsservice/v1/PendingReturnsPortType.java` | `PendingReturnsPortType` | `@WebService` | `.../rwms/integration/services/PendingReturnsService/v1` |
| 6 | `.../pendingreturnsservice/v1/PendingReturnsService.java` | `PendingReturnsService` | `@WebServiceClient` | same |
| 7 | `com/oracle/retail/sim/integration/services/fulfillmentorderdeliveryservice/v1/FulfillmentOrderDeliveryPortType.java` | `FulfillmentOrderDeliveryPortType` | `@WebService` | `.../sim/.../FulfillmentOrderDeliveryService/v1` |
| 8 | `.../fulfillmentorderdeliveryservice/v1/FulfillmentOrderDeliveryService.java` | `FulfillmentOrderDeliveryService` | `@WebServiceClient` | same |
| 9 | `com/oracle/retail/sim/integration/services/fulfillmentorderreversepickservice/v1/FulfillmentOrderReversePickPortType.java` | `FulfillmentOrderReversePickPortType` | `@WebService` | `.../sim/.../FulfillmentOrderReversePickService/v1` |
| 10 | `.../fulfillmentorderreversepickservice/v1/FulfillmentOrderReversePickService.java` | `FulfillmentOrderReversePickService` | `@WebServiceClient` | same |
| 11 | `com/oracle/retail/sim/integration/services/inventoryadjustmentservice/v1/InventoryAdjustmentPortType.java` | `InventoryAdjustmentPortType` | `@WebService` | `.../sim/.../InventoryAdjustmentService/v1` |
| 12 | `.../inventoryadjustmentservice/v1/InventoryAdjustmentService.java` | `InventoryAdjustmentService` | `@WebServiceClient` | same |
| 13 | `com/oracle/retail/sim/integration/services/postransactionservice/v1/POSTransactionPortType.java` | `POSTransactionPortType` | `@WebService` | `.../sim/.../POSTransactionService/v1` |
| 14 | `.../postransactionservice/v1/POSTransactionService.java` | `POSTransactionService` | `@WebServiceClient` | same |
| 15 | `com/oracle/retail/sim/integration/services/storefulfillmentorderservice/v1/StoreFulfillmentOrderPortType.java` | `StoreFulfillmentOrderPortType` | `@WebService` | `.../sim/.../StoreFulfillmentOrderService/v1` |
| 16 | `.../storefulfillmentorderservice/v1/StoreFulfillmentOrderService.java` | `StoreFulfillmentOrderService` | `@WebServiceClient` | same |
| 17 | `com/oracle/retail/sim/integration/services/storeinventoryservice/v1/StoreInventoryPortType.java` | `StoreInventoryPortType` | `@WebService` | `.../sim/.../StoreInventoryService/v1` |
| 18 | `.../storeinventoryservice/v1/StoreInventoryService.java` | `StoreInventoryService` | `@WebServiceClient` | same |
| 19 | `com/oracle/retail/sim/integration/services/storetostoretransferservice/v1/StoreToStoreTransferPortType.java` | `StoreToStoreTransferPortType` | `@WebService` | `.../sim/.../StoreToStoreTransferService/v1` |
| 20 | `.../storetostoretransferservice/v1/StoreToStoreTransferService.java` | `StoreToStoreTransferService` | `@WebServiceClient` | same |
| 21 | `retail/siebel/com/integration/InboundordercreationbpelprocessClientEp.java` | `InboundordercreationbpelprocessClientEp` | `@WebServiceClient` (extends `Service`) | `http://com.siebel.retail/integration/` |
| 22 | `retail/siebel/com/integration/OmsorderupdatebpelprocessClientEp.java` | `OmsorderupdatebpelprocessClientEp` | `@WebServiceClient` (extends `Service`) | `http://com.siebel.retail/integration/` |
| 23 | `retail/siebel/com/integration/SiebelOrderFeedWebservice.java` | `SiebelOrderFeedWebservice` | `@WebService` (SEI, BARE binding) | `http://com.siebel.retail/integration/` |
| 24 | `retail/siebel/com/integration/SiebelStatusUpdateWebService.java` | `SiebelStatusUpdateWebService` | `@WebService` (SEI, BARE binding) | `http://com.siebel.retail/integration/` |
| 25 | `retail/siebel/com/soaintegration/IStatus.java` | `IStatus` | `@WebService` (SEI) | `http://www.extra.com/Services/OmsStatus` |
| 26 | `retail/siebel/com/soaintegration/Status.java` | `Status` | `@WebServiceClient` (extends `Service`) | `http://www.extra.com/Services/OmsStatus` |

**All 13 WSDL files**, with `soap:address` (a snapshot of a real remote endpoint at generation time) and operations (from generated PortType `@WebMethod`s):

| WSDL | Service | soap:address | Operations |
|---|---|---|---|
| `.../fulfillorderservice/v1/FulfillOrderService.wsdl` | FulfillOrderService | `http://licrpap14-extra.logicindia.com:17040/FulfillOrderBean/FulfillOrderService` | `createFulfilOrdColDesc`, `cancelFulfilOrdColRef`, `ping` |
| `.../inventorybackorderservice/v1/InventoryBackOrderService.wsdl` | InventoryBackOrderService | `...:17040/InventoryBackOrderBean/InventoryBackOrderService` | `createInvBackOrdColDesc`, `ping` |
| `.../pendingreturnsservice/v1/PendingReturnsService.wsdl` | PendingReturnsService | `...:17053/PendingReturnsBean/PendingReturnsService` | `pendReturnDtlCreate`, `pendReturnDtlModify`, `pendReturnDtlDelete`, `ping` |
| `.../fulfillmentorderdeliveryservice/v1/FulfillmentOrderDeliveryService.wsdl` | FulfillmentOrderDeliveryService | `...:17019/FulfillmentOrderDeliveryBean/FulfillmentOrderDeliveryService` | `createFulfillmentOrderDelivery`, `submitFulfillmentOrderDelivery`, `dispatchFulfillmentOrderDelivery`, `updateFulfillmentOrderDelivery`, `cancelFulfillmentOrderDelivery`, `cancelFulfillmentOrderDeliverySubmission`, `lookupFulfillmentOrderDeliveryHeaders`, `readFulfillmentOrderDeliveryDetail`, `ping` |
| `.../fulfillmentorderreversepickservice/v1/FulfillmentOrderReversePickService.wsdl` | FulfillmentOrderReversePickService | `...:17019/FulfillmentOrderReversePickBean/...` | `createReversePick`, `updateFulfillmentOrderReversePick`, `deleteReversePick`, `confirmReversePick`, `lookupReversePickHeaders`, `readReversePickDetail`, `ping` |
| `.../inventoryadjustmentservice/v1/InventoryAdjustmentService.wsdl` | InventoryAdjustmentService | `...:17019/InventoryAdjustmentBean/...` | `saveInventoryAdjustment`, `saveAndConfirmInventoryAdjustment`, `confirmInventoryAdjustment`, `cancelInventoryAdjustment`, `lookupInventoryAdjustmentReason`, `lookupNonSellableQuantityType`, `lookupInventoryAdjustmentTemplateHeader`, `readInventoryAdjustmentTemplateDetail`, `lookupInventoryAdjustmentHeader`, `readInventoryAdjustmentDetail`, `ping` |
| `.../postransactionservice/v1/POSTransactionService.wsdl` | POSTransactionService | `http://exvm-qaadfapp01.extrastores.com:7511/POSTransactionBean/...` | `processPOSTransactions`, `ping` |
| `.../storefulfillmentorderservice/v1/StoreFulfillmentOrderService.wsdl` | StoreFulfillmentOrderService | `...:17019/StoreFulfillmentOrderBean/...` | `createFulfillmentOrderDetail`, `cancelFulfillmentOrderDetail`, `readFulfillmentOrderDetail`, `lookupFulfillmentOrderHeaders`, `ping` |
| `.../storeinventoryservice/v1/StoreInventoryService.wsdl` | StoreInventoryService | `...:17019/StoreInventoryBean/...` | `lookupAvailableInventory`, `lookupInventoryInStore`, `lookupInventoryInTransferZone`, `lookupInventoryForBuddyStores`, `ping` |
| `.../storetostoretransferservice/v1/StoreToStoreTransferService.wsdl` | StoreToStoreTransferService | `...:17019/StoreToStoreTransferBean/...` | `requestTransfer`, `savePendingTransferRequest`, `saveTransferRequest`, `approveTransfer`, `submitTransfer`, `dispatchTransfer`, `receiveTransfer`, `rejectTransfer`, `cancelTransfer`, `cancelTransferSubmission`, `saveInProgressTransfer`, `saveInReceivingTransfer`, `lookupTransferHeader`, `readTransferDetail`, `lookupBillOfLadingMotives`, `ping` |
| `retail/siebel/com/integration/omsorderupdatebpelprocess_client_ep.wsdl` | omsorderupdatebpelprocess_client_ep | `http://ex-uat-mw1.extrastores.com:8001/soa-infra/services/IBT_Development/OrderStatusUpdate/...` | `processSiebelStatusUpdate` (via `SiebelStatusUpdateWebService` port) |
| `retail/siebel/com/integration/omsorderupdatebpelprocess_client_epRuntimeFault.wsdl` | (fault/abstract import for above) | n/a | n/a |
| `retail/siebel/com/soaintegration/Status.wsdl` | Status | `http://192.168.40.89/eXtra.Services.Oms/Status.svc` | `updateOrderStatus` (via `IStatus` port) |

`InboundordercreationbpelprocessClientEp` (order-creation feed to Siebel, via `SiebelOrderFeedWebservice.publishToSiebel`) has no dedicated `.wsdl` checked in — operation `publishToSiebel(CustomerOrderHeaderLevel) -> CustomerOrderHeaderLevelResponse`, addressed at `http://ex-uat-mw1.extrastores.com:8001/soa-infra/services/IBT_Development/OMSToSiebelOrderCreation/inboundordercreationbpelprocess_client_ep`.

---

### 2. Business-function groups and end-to-end traces

**Critical scope finding**: `OracleIntegrationServices`, `oracle-base`, `oms-common` contain **zero** `CallableStatement`/`DriverManager`/`DataSource`/`persistence.xml`/`EntityManager` usage (verified by grep) and **zero** references to any of the ten ground-truth PL/SQL packages. These three modules stop at the SOAP/REST client boundary — there is no DAO/DB layer inside them. Actual DB access/business rules live in sibling modules (`oms-core`, `oms-cancellation`, `oms-customer-order`, `sim-dispatch`, `spareParts-stockRequest`, `OMSUtil`, `CustomerOrderBeanPOS`), outside this module set.

**RMS Integration Services (FulfillOrder / InventoryBackOrder)**: Client SEI duplicated in `oracle-base`. Hand-written Feign contract `oms-common/.../IOracleRMSClient.java:20` — `createFulfilOrdColDesc(CreateFulfilOrdColDesc)` → `POST /FulfillOrderBean/FulfillOrderService`. DTO `FulfilOrdColDesc` wraps a list of `FulfilOrdDesc` (customer_order_no, fulfill_order_no, source/fulfill loc type+id, delivery type/carrier, consumer_delivery_date, comments, nested customer desc + line items). Config: `${rms.oralce.base.api.url}` Spring property (typo "oralce" consistent across modules), e.g. `oms-core/.../application.properties:23` prod value `http://prodrms-app1.extrastores.com:7117/FulfillOrderBean/FulfillOrderService`. Legacy variant in `OracleIntegrationServices` resolves WSDL location dynamically via `OMSUtilCommons.getWebServiceURL("RMS_FULFIL_ORDER")` → EJB lookup on `OMS_WEBSERVICE_URI_DETAIL` (same table the PL/SQL WS_INVOKER packages read, reached here via Java/EJB instead of UTL_HTTP).

**SIM Integration Services** (Store Fulfillment/Delivery/Reverse Pick/Inventory Adjustment/POS/Store-to-Store Transfer/Store Inventory): 8 PortTypes. Hand-written Feign contract `oms-common/.../IOracleSIMClient.java:44-96` — 16 operations behind one Feign target. `StoreInventoryService` (lookup-only) is the one SIM WSDL not represented in `IOracleSIMClient`. Config `${sim.oralce.base.api.url}`.

**RWMS Pending Returns**: `PendingReturnsPortType` — `pendReturnDtlCreate`/`Modify`/`Delete`. **Not wrapped by any Feign interface in `oms-common`** — no consumer found anywhere in the repo outside `OracleIntegrationServices`/`oracle-base` themselves. Appears generated but currently unused/orphaned, matching the ground-truth `OMS_PENDRETURN_WS_INVOKER` package having no confirmed Java-side caller either.

**Siebel/SOA-BPEL order feed and status update**: `InboundordercreationbpelprocessClientEp`/`SiebelOrderFeedWebservice.publishToSiebel` — outbound order-creation feed to Siebel BPEL (`OMSToSiebelOrderCreation`), static WSDL location baked in. `OmsorderupdatebpelprocessClientEp`/`SiebelStatusUpdateWebService.processSiebelStatusUpdate` — outbound order-status-update feed, endpoint resolved dynamically via `OMSUtilCommons.getWebServiceURL("SIEBEL_STATUS_UPDATE")`. Neither has any implementing class — pure JAX-WS client-side PortType interfaces.

**eXtra `OmsStatus`/`UpdateOrderStatus`** (Java-side counterpart of the contract the PL/SQL `WS_INVOKER` packages target via UTL_HTTP — confirmed client, not server): SEI `retail.siebel.com.soaintegration.IStatus`, duplicated as `com.extra.services.omsstatus.IStatus` in `oracle-base` and re-exported by `oms-common`. Operation `updateOrderStatus(OrderStatus) -> String` (action `http://www.extra.com/Services/OmsStatus/IStatus/UpdateOrderStatus`). Request DTO `OrderStatus`: `EnitityId` (mandatory, note upstream typo), `ApplicationId`, `OrderId`, `SubOrderId` (optional), `OmsOrderId`, `DeliveryDate`, `UpdateDate`, `OrderDetailStatuses` array (each item: `OrderDetailId`, `ProductSku`, `Quantity`, `SourceType`, `SourceId`, `FulfillType`, `FulfillId`, `EventId`, `EventComment`, `EventReferenceId`, `UpdateDate` — all mandatory). Endpoint config diverges: `OracleIntegrationServices` dynamic lookup `OMSUtilCommons.getWebServiceURL("SOA_CO_STATUS_UPDATE")`, fallback `http://192.168.40.89/eXtra.Services.Oms/Status.svc?singleWsdl` (a **.NET/WCF** service); `oracle-base` hardcodes `wsdlLocation = "http://qa-online-order-processing-v1.uk-e1.cloudhub.io/status/UpdateOrderStatus?wsdl"` (a **MuleSoft CloudHub**-hosted endpoint) — i.e. by the time `oracle-base` was cut, this integration had migrated from the on-prem WCF service to a Mule-fronted service, same SOAP contract. Confirmed real callers (outside scope, cited for continuity): `oms-cancellation/.../BaseAPIService.java:223,294` and `OMSUtil/.../OmsStatusUpdateForReturnPickupCancellation.java:79,131-162`.

**Carrera transfer creation & Mule address lookup** (JSON/REST, no WSDL): `oms-common/.../ICarreraClient.java:15-17` (duplicate `oracle-base/.../ICarreraTransfer.java:10-12`) — `getOrderItemsResponse` → `POST /CarreraTransferCreation/NewTransferCreation`. `IMuleAddressClient.java:10-16` — `getAddressByGeocode`/`getAddressByShortAddress` against a Mule-hosted address-resolution API — likely the client counterpart used by the address-update flow referenced by `XX_DLV_ADDRESS_UPDATE` in the ground truth, though no direct call site from these modules to that PL/SQL package was found.

**SOAP transport plumbing**: `oms-common/.../feign/soap/{SOAPEncoder,SOAPDecoder,SOAPErrorDecoder}.java` — a locally-vendored fork of `feign-soap`, marshals/unmarshals JAXB DTOs into/out of SOAP 1.1 envelopes for every `IOracleRMSClient`/`IOracleSIMClient`/`IOracleInvAdjClient` call; unwraps `SOAPFaultException` on a SOAP Fault even when HTTP status is 200.

**BDS (Booking/home-delivery) DTOs**: `oms-common/.../com/extra/bds/bean/*.java` — plain POJOs consistent with the `XXHDB_CORE_PKG` "home delivery booking" ground truth, but no Feign interface/caller present in these three modules — Not determinable from these modules alone.

---

### 3. Architectural determination: client or server?

**All three assigned modules are pure client/DTO libraries. None implement a server side.** Evidence:
1. No implementation classes for any of the 13 SEIs anywhere in the three modules (`implements <PortType>` grep returns zero).
2. No deployment descriptor/endpoint publisher (no `web.xml`, `sun-jaxws.xml`, `Endpoint.publish(...)`, `@WebServiceProvider`).
3. `@WebServiceClient`-annotated factory classes dominate, each extending `javax.xml.ws.Service` and resolving a remote `wsdlLocation` — the shape of `wsimport`-generated client stubs.
4. `oracle-base/pom.xml` self-describes as "Base API for calling oracle base web services" and uses `jaxws-maven-plugin` `wsimport` against externally-hosted QA WSDL URLs to regenerate these stubs at build time.
5. `OracleIntegrationServices/pom.xml` states "This POM is only for import in eclipse, not for build" — not referenced by any other module's build; near-identical to current `oracle-base` copies — a legacy/orphaned earlier generation superseded by `oracle-base`.
6. Real consumers (`oms-core`, `oms-cancellation`, `OMSUtil`) build Feign clients or Spring-XML-wired port `Service` classes and always call these outbound, never receiving inbound calls.

**Conclusion**: `OracleIntegrationServices` (legacy/orphaned) and `oracle-base` (current, Maven-managed) are outbound **client-stub libraries** for RMS/SIM/RWMS Integration Services, Siebel SOA-BPEL processes, and the `eXtra.Services.Oms` order-status webservice. `oms-common` layers hand-written Feign interfaces, a SOAP codec, and shared DTOs on top. RMS/RIB's inbound path into the OMS DB is handled entirely on the PL/SQL side (`OMSSUB_ASNOUT`/`OMSSUB_RECEIVING` RIB subscribers), architecturally separate from these three Java modules.

---

### 4. PL/SQL package call-site grep results

All 10 named packages (`OMSSUB_ASNOUT`, `OMSSUB_RECEIVING`, `OMS_ASNOUT_WS_INVOKER`, `OMS_RECEIPT_WS_INVOKER`, `OMS_SOS_WS_INVOKER`, `OMS_PENDRETURN_WS_INVOKER`, `OMS_MSG_WS_PUBLISHER`, `XX_DLV_ADDRESS_UPDATE`, `XX_REFUND_REQUEST_PKG`, `XXHDB_CORE_PKG`): **zero hits** in all three assigned modules — consistent with the architectural finding that these modules contain no JDBC/`CallableStatement` code at all.

---

### 5. Configuration inventory

- No `web.xml`, `applicationContext*.xml`, or `persistence.xml` in any of the three modules — consistent with "client library, not a deployed service."
- Feign target URLs (`${...oralce.base.api.url}` etc.) live in *consumer* modules' `application.properties` (`oms-core`, `oms-cancellation`, `sim-dispatch`, `spareParts-stockRequest`, `oms-customer-order`) — genuine QA/prod hostnames found (e.g. `exvm-qaadfapp01.extrastores.com`, `prodrms-app1.extrastores.com`, `prodadf-app2.extrastores.com`, `prodsimapp.extrastores.com`), not placeholders.
- Two endpoint-config strategies: (a) legacy dynamic lookup via `OMSUtilCommons.getWebServiceURL(<key>)` → EJB finder on `OMS_WEBSERVICE_URI_DETAIL` (keys: `RMS_FULFIL_ORDER`, `RMS_BACK_ORDER`, `RWMS_PENDING_RETURNS`, `SIM_FULFILL_ORD_DLV`, `SIM_FULFILL_REVERSE_PICK`, `SIM_INVENTORY_ADJ`, `POS_TRANSACTION`, `SIM_STORE_FULFILL_ORDER`, `SIM_STORE_INVENTORY`, `SIM_STR_TO_STR_TSF`, `SIEBEL_STATUS_UPDATE`, `SOA_CO_STATUS_UPDATE` — found in `OracleIntegrationServices` only); (b) `oracle-base`'s regenerated copies hardcode the `wsimport`-time WSDL URL as fallback and rely on Feign's externally-supplied base URL for actual calls.

---

### 6. Summary table

| API / Group | Method / Operation | Java Entry Point | Java Service (Feign contract) | DAO/DB Layer | PL/SQL or SQL | Main Tables | Purpose |
|---|---|---|---|---|---|---|---|
| RMS FulfillOrderService | `createFulfilOrdColDesc` | `FulfillOrderPortType` (generated SEI) | `IOracleRMSClient.createFulfilOrdColDesc` | None in scope | Not determinable | n/a | Send new fulfillment-order collection to RMS |
| RMS FulfillOrderService | `cancelFulfilOrdColRef` | `FulfillOrderPortType` | `IOracleRMSClient.cancelFulfilOrdColRef` | None in scope | Not determinable | n/a | Cancel a fulfillment order in RMS |
| RMS InventoryBackOrderService | `createInvBackOrdColDesc` | `InventoryBackOrderPortType` | `IOracleRMSClient.createInvBackOrdColDesc` | None in scope | Not determinable | n/a | Register back-order collection in RMS |
| SIM StoreFulfillmentOrderService | `createFulfillmentOrderDetail` | `StoreFulfillmentOrderPortType` | `IOracleSIMClient.createFulfillmentOrderDetail` | None in scope | Not determinable | n/a | Create store fulfillment order in SIM |
| SIM FulfillmentOrderDeliveryService | `dispatchFulfillmentOrderDelivery` | `FulfillmentOrderDeliveryPortType` | `IOracleSIMClient.dispatchFulfillmentOrderDelivery` | None in scope | Not determinable | n/a | Mark delivery as dispatched in SIM |
| SIM POSTransactionService | `processPOSTransactions` | `POSTransactionPortType` | `IOracleSIMClient.processPOSTransactions` | None in scope | Not determinable | n/a | Push POS transaction data to SIM |
| SIM InventoryAdjustmentService | `saveAndConfirmInventoryAdjustment` | `InventoryAdjustmentPortType` | `IOracleSIMClient.saveAndConfirmInventoryAdjustment` / `IOracleInvAdjClient` | None in scope | Not determinable | n/a | Post/confirm store inventory adjustment |
| SIM StoreToStoreTransferService | `approveTransfer`/`savePendingTransferRequest` | `StoreToStoreTransferPortType` | `IOracleSIMClient.approveTransfer`/`savePendingTransferRequest` | None in scope | Not determinable | n/a | Store-to-store stock transfer approval |
| SIM StoreInventoryService | `lookupAvailableInventory` | `StoreInventoryPortType` | No Feign interface (generated stub only) | None in scope | Not determinable | n/a | ATS/store-inventory lookup |
| RWMS PendingReturnsService | `pendReturnDtlCreate` | `PendingReturnsPortType` | None (no consumer found anywhere in repo) | None in scope | Not determinable | n/a | Register a pending warehouse return (appears orphaned) |
| Siebel BPEL — order creation | `publishToSiebel` | `SiebelOrderFeedWebservice` | None (direct JAX-WS client) | None in scope | Not determinable | n/a | Push new customer order to Siebel |
| Siebel BPEL — status update | `processSiebelStatusUpdate` | `SiebelStatusUpdateWebService` | None (direct JAX-WS client) | None in scope | Not determinable | n/a | Push order-status change to Siebel |
| eXtra OmsStatus service | `updateOrderStatus` | `IStatus` (via `Status`), consumed at `oms-cancellation/.../BaseAPIService.java:223,294` and `OMSUtil/.../OmsStatusUpdateForReturnPickupCancellation.java:79,131` | None (direct JAX-WS client) | None in scope | Not determinable | n/a | Java-side equivalent of the PL/SQL WS_INVOKER → `UpdateOrderStatus` SOAP call |
| Carrera transfer | `getOrderItemsResponse` | `ICarreraClient`/`ICarreraTransfer` | Feign REST/JSON | None in scope | Not determinable | n/a | Create a Carrera stock transfer |
| Mule address service | `getAddressByGeocode`/`getAddressByShortAddress` | `IMuleAddressClient` | Feign REST/JSON | None in scope | Not determinable | n/a | Address resolution/geocoding lookup |

### Key file paths
- `OracleIntegrationServices/pom.xml`, `OracleIntegrationServices/src/**` (26 classes, 13 WSDLs above)
- `oracle-base/pom.xml`, `oracle-base/src/main/java/com/oracle/retail/**`, `oracle-base/src/main/java/com/extra/{services/omsstatus,oms/carrera}/**`
- `oms-common/src/main/java/com/extra/oms/service/client/*.java`, `oms-common/src/main/java/feign/soap/*.java`, `oms-common/src/main/java/com/extra/bds/bean/**`
- Out-of-scope cross-references: `oms-core/src/main/java/com/extra/oms/core/{config/MvcConfiguration,service/FulfilmentService}.java`, `oms-cancellation/src/main/java/com/extra/oms/service/BaseAPIService.java`, `oms-cancellation/src/main/webapp/WEB-INF/cancellationAPI-servlet.xml`, `OMSUtil/src/com/logicinfo/oms/beans/OMSUtilCommons.java`, `OMSUtil/src/com/logicinfo/oms/util/OmsStatusUpdateForReturnPickupCancellation.java`.
