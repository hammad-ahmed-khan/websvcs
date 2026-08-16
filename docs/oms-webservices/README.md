# Oracle Retail RMS 14 Custom Web Services & OMS — Technical Architecture

**Scope**: this documents the custom Order Management System (OMS) and web-services layer built around Oracle Retail Merchandising System (RMS) 14 for the retailer "eXtra" (Saudi Arabia / Gulf region — SADAD, Payfort, Tasheel Wallet, Benefit, AFS payment channels; ZATCA e-invoicing). It was produced by tracing the actual Java and PL/SQL source in this repository — controller/endpoint → service → DAO → SQL/PL·SQL → tables — not by summarizing file names.

**How this documentation set is organized**: this file covers the cross-cutting material (sections 1–4, 13–14, 19–25 of the requested structure). Business-category API detail (sections 5–12, 15, 17) lives in `categories/*.md`, one file per category, each independently thorough with file:line citations. The consolidated mapping matrix (sections 7–8) is in `api-plsql-matrix.md`. Full PL/SQL package documentation (part of section 18) is in `plsql-packages.md`. End-to-end flows (section 16) are in `business-flows.md`. Error handling and configuration (sections 20–21) are in `error-handling-and-config.md`. Gaps and unknowns (section 22) are in `unknowns.md`.

| Section | Where |
|---|---|
| 1–4. Executive Summary, Repository Overview, Architecture, Module Structure | this file |
| 5–6. API Catalog, Categorization | this file (catalog) + `categories/*.md` (detail) |
| 7. API → Java → PL/SQL Mapping Matrix | `api-plsql-matrix.md` |
| 8. PL/SQL → API Reverse Mapping | `api-plsql-matrix.md` |
| 9. Java Class/Method Call Chains | `categories/*.md` (per API, "Call chain" subsections) |
| 10. Request/Response Documentation | `categories/*.md` (per API) |
| 11. Error Handling | `error-handling-and-config.md` |
| 12. Configuration & External Integrations | `error-handling-and-config.md` |
| 13. Architecture Diagram | this file |
| 14. Gaps and Unknowns | `unknowns.md` |
| 15. Final structure / cross-references | this file (table above) |
| 16. End-to-End Business Flows | `business-flows.md` |
| 17. Java Class Dependency Mapping / shared classes | this file (Shared Infrastructure) + `categories/*.md` |
| 18. Database and PL/SQL Dependencies | `plsql-packages.md` + `api-plsql-matrix.md` |
| 19. Oracle RMS Standard vs Custom Components | this file |
| 20–21. Error Handling, Configuration | `error-handling-and-config.md` |
| 22. Unknowns / Requires Verification | `unknowns.md` |
| 23. Complete API Inventory | this file |
| 24. Complete PL/SQL Inventory | `plsql-packages.md` (custom) + `unknowns.md` (referenced-but-absent) |
| 25. Key Findings and Architectural Observations | this file |

---

## 1. Executive Summary

This repository (`oms/`) contains the complete custom Java and PL/SQL source for eXtra's Order Management System (OMS) built on top of Oracle Retail RMS 14 — **not** a fork or modification of RMS itself, but a large (~65 module) layer of custom web services, batch jobs, and PL/SQL that sits alongside RMS, SIM (Store Inventory Management), RWMS (Warehouse Management), and an external Siebel CRM, mediating orders that originate from e-commerce (Hybris), a marketplace (noon.com), and in-store POS (ORPOS).

The system is not architecturally uniform: it spans roughly a decade of development across at least two clear generations — legacy JAX-WS/Apache-Axis SOAP services backed by an EJB3/JPA session bean (`OMSUtilSessionEJB`), and newer Spring MVC REST services backed by direct JDBC (`NamedParameterJdbcTemplate`) — often with **both generations implementing the same business capability side-by-side against the same schema** (order cancellation, delivery-address update, and Apple pricing all have two parallel implementations; see §25).

Orders flow in from three channels (e-commerce/Hybris, noon.com marketplace, ORPOS in-store), are validated and reserved against RMS/SIM inventory via SOAP, persisted into a custom `OMS_*` schema, and their fulfillment progress is driven both by RMS's own RIB (Retail Integration Bus) shipment/receiving events — consumed by two custom PL/SQL subscriber packages — and by direct Java-to-RMS/SIM SOAP calls at order-creation, payment-confirmation, and cancellation time. Order status is pushed outward to Siebel CRM via a SOAP `UpdateOrderStatus` contract that is, notably, implemented **twice independently** — once as async-staged PL/SQL (via `UTL_HTTP`) and once as a direct-attempt-then-async-fallback Java client — both writing to the same failure-audit table.

Beyond core order/fulfillment/returns/inventory, the system integrates: a home-delivery slot-booking subsystem (`XXHDB_CORE_PKG` + `bds-service`), a loyalty/cashback platform ("Jood", hosted on MuleSoft CloudHub), noon.com marketplace order/return/transfer sync, courier tracking-number ("AWB") capture and customer notification, Apple product pricing sync, ZATCA (Saudi tax authority) e-invoicing, IMEI/serial-number capture for SIM, and eXtra acting as its own last-mile courier (self-issued shipping labels).

**A material, unsolicited finding from this exercise**: the repository has at least 27 configuration files with plaintext production database and third-party API credentials committed to source control, several outbound HTTP clients with TLS certificate/hostname validation disabled, and one module (`proxy-api`) that functions as an open, unauthenticated outbound HTTP relay. See `error-handling-and-config.md` for the full list. These are documented here because they were discovered in the course of tracing configuration for API endpoints — this is not a security audit and no further security testing was performed.

## 2. Repository Overview

```text
oms/
├── CODE/            65+ Java modules (Maven and legacy Eclipse/WebLogic WAR layouts mixed)
├── PACKAGE/         10 custom Oracle PL/SQL package .sql/.txt files (the ONLY PL/SQL source in this repo)
└── SCRIPT/          2 shell (.ksh) scripts — republish batch driver, a one-off item-fix script
```

- **Java**: ~4,700 `.java` files. Web-service styles found: **60** JAX-WS `@WebService` classes (SOAP), **48** Spring `@RestController`/`@Controller` classes (REST), **4** files with a `.jws` extension (these are actually JDeveloper workspace descriptors for JAX-WS servlets in this codebase, not genuine Apache Axis 1.x RPC artifacts — confirmed by inspection). **48** separate `web.xml` deployment descriptors, reflecting ~40+ independently-deployed WAR modules alongside newer consolidated Spring/Maven services.
- **PL/SQL**: 10 custom packages (see `plsql-packages.md`). This is deliberately small — the bulk of "PL/SQL logic" that Java calls into (`OMS_INVADJ_STATUS_UNAVIALINV`, `XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN`, `XX_IS_CANCELLABLE`, the `XX_JOOD_*` family, `XX_APPLE_ON_PRICING_SQL`, `XX_CREATE_LABEL`, `XX_HB_MAKE_LABEL`, and others) is **not included in this repository** — only its call sites and signatures are visible from the Java side (see `unknowns.md`).
- **XML/WSDL/XSD**: 65 WSDL, 363 XSD files — mostly generated JAX-WS artifacts (request/response schemas) plus a handful of hand-authored contracts.
- No standard Oracle Retail RMS 14 source (Java or PL/SQL) is present in this repository — RMS itself is treated as an external system reached via its standard SOAP integration services (`FulfillOrderService`, `InventoryBackOrderService`) and RIB.

## 3. System Architecture

```text
                         ┌─────────────────────────────────────────────────┐
                         │              External Channels                   │
                         │  Hybris (e-commerce)   noon.com   ORPOS (in-store)│
                         └───────────────┬───────────────────┬─────────────┘
                                          │ SOAP/REST              │ SOAP
                                          ▼                        ▼
        ┌─────────────────────────────────────────────────────────────────────┐
        │                    Custom OMS Web Services  (oms/CODE)               │
        │                                                                       │
        │  Order Mgmt        Cancellation      Returns/RMA     Fulfillment/SIM  │
        │  (OMSCustomerOrder, (oms-cancellation, (OMSRMA*)      (SIMDeliveryDetail,│
        │   CustomerOrderSvc,  Hybris_Cancel-                   sim-dispatch,     │
        │   oms-core)          lation, OMSCO-                   DeliveryUpdate,   │
        │                      Cancellation)                    CarreraTransfer)  │
        │                                                                       │
        │  Inventory          Payment/Finance/   Integration/Notification       │
        │  (Stockcheck,        Pricing/E-Invoice  (bds-service, jood, NOON,     │
        │   Stock_Feed,        (PaymentConfirm-    AWB*, notification,          │
        │   OmsOrpos*,         ation, apple-       ecom-reconcilliation,        │
        │   SpareParts*)       pricing, einvoicing) cross-channel-service,      │
        │                                           imei-validation, proxy-api) │
        │                                                                       │
        │  Shared infra: OMSUtil (JPA/EJB DAO layer, ~190 entities),            │
        │                oms-common (Feign SOAP/REST client wrappers, DTOs),    │
        │                oracle-base (generated JAX-WS client stubs)            │
        └───────────┬─────────────────────┬───────────────────┬───────────────┘
                     │ JDBC/JPA            │ SOAP (client)      │ REST (Feign, CloudHub)
                     ▼                     ▼                    ▼
        ┌─────────────────────┐  ┌──────────────────────┐  ┌───────────────────────┐
        │  Custom OMS schema   │  │ RMS / SIM / RWMS      │  │ MuleSoft CloudHub      │
        │  (OMS_*, XX_*, EXT_* │  │ standard SOAP          │  │ (Jood loyalty, address │
        │  tables — this repo's│  │ integration services   │  │ validation, reconcil., │
        │  10 custom PL/SQL    │  │ (FulfillOrderService,   │  │ AWB email/SMS)         │
        │  packages live here) │  │  StoreFulfillmentOrder- │  └───────────────────────┘
        └──────────┬───────────┘  │  Service, Inventory-    │
                   │              │  AdjustmentService,      │  ┌───────────────────────┐
                   │              │  PendingReturnsService)  │  │ Siebel CRM             │
                   │              └───────────┬──────────────┘  │ (UpdateOrderStatus SOAP,│
                   │                          │                 │  order-creation BPEL)   │
         RIB (shipment/receiving              │ RIB (standard   └───────────────────────┘
         events, consumed by                  │  Oracle Retail          ▲
         OMSSUB_ASNOUT/RECEIVING)              │  Integration Bus)      │ SOAP (UTL_HTTP from
                   │                          ▼                        │ PL/SQL, OR direct
                   ▼              ┌──────────────────────┐             │ JAX-WS from Java —
        ┌─────────────────────┐  │  Oracle Retail RMS 14  │             │ BOTH paths exist)
        │ 10 Custom PL/SQL     │◄─┤  (external, standard   │─────────────┘
        │ packages (this repo):│  │  product — not in this │
        │ OMSSUB_ASNOUT/       │  │  repository)            │
        │ RECEIVING, 4×        │  └──────────────────────┘
        │ *_WS_INVOKER,        │
        │ OMS_MSG_WS_PUBLISHER,│           ┌──────────────────────┐
        │ XX_DLV_ADDRESS_      │           │ ZATCA (Saudi tax      │
        │ UPDATE, XX_REFUND_   │           │ authority e-invoicing)│
        │ REQUEST_PKG,         │◄──────────┤ via einvoicing module │
        │ XXHDB_CORE_PKG       │           └──────────────────────┘
        └─────────────────────┘
                                            ┌──────────────────────┐
                                            │ Apple Pricing API     │
                                            │ via apple-pricing/    │
                                            │ boot-apple-pricing    │
                                            └──────────────────────┘
```

**Where each standard Oracle Retail component fits**:
- **RMS 14**: system of record for items, locations, inventory, transfers, purchase orders — reached from Java via its standard SOAP integration services (`FulfillOrderService`, `InventoryBackOrderService`), and reached *from* by the two custom RIB subscriber packages consuming its ASN-out/shipment and receiving events. Also queried directly via cross-schema SQL in several places (`ITEM_MASTER`, `ORDCUST`, `TSFHEAD`/`TSFDETAIL`, `WH`, `STORE`).
- **SIM 14**: store-level inventory/fulfillment execution — reached via its standard SOAP integration services (`StoreFulfillmentOrderService`, `StoreInventoryService`, `InventoryAdjustmentService`, `StoreToStoreTransferService`, `FulfillmentOrderDeliveryService`, `FulfillmentOrderReversePickService`, `POSTransactionService`) and via direct cross-schema SQL (multiple modules query the SIM schema directly, including across a `@simdb` DB link from OMS/RMS). `extra-imei-capture` is an actual SIM-side plugin module (`com.extra.sim` package).
- **RWMS**: reached only for pending-return processing (`PendingReturnsService`), by the three RMA modules.
- **RIB**: standard Oracle Retail Integration Bus — the transport for `OMSSUB_ASNOUT`/`OMSSUB_RECEIVING`'s inbound messages; RIB's own configuration (queues, adapters) is not in this repository.
- **JMS**: implicit in the RIB transport layer; no JMS configuration is present in this repository.
- **WMS**: eXtra's own warehouse management (distinct from RWMS) is referenced as the source of `XX_HB_MAKE_LABEL`/`XX_CREATE_LABEL` shipping-label generation (`CR_17_ExtraAsCourier`, `label_Package_Creation`, connecting via JNDI datasource `jdbc/wms`) — eXtra self-issuing its own last-mile shipping labels/AWB numbers rather than using a third-party courier's API for this function.
- **RPM** (Retail Price Management): referenced only as a data source name in `ecom-reconcilliation`'s reconciliation of "RMS/RPM" catalog and pricing data against Hybris — no direct RPM integration code is present; the reconciliation reads from OMS-side staging views/tables that are presumably populated by an RPM feed external to this repository.
- **SIM/RMS/RWMS/Siebel are all external systems** from this repository's point of view — only their SOAP client contracts and this codebase's calls into them are documented here.

## 4. Application / Module Structure

The ~65 modules fall into these functional groups (full detail in the linked category doc):

| Group | Modules | Detail |
|---|---|---|
| Order Management | `CustomerOrderService`, `CustomerOrderBeanPOS`, `OMSCustomerOrder`, `oms-customer-order`, `oms-core`, `OMSUtil` (shared) | `categories/order-management.md` |
| Cancellation | `OMSCOCancellation`, `Hybris_Cancellation`, `oms-cancellation`, `oms-cancellation-job`, `OMSSparePartsCancel` | `categories/cancellation.md` |
| Returns / RMA | `OMSRMAGeneration`, `OMSRMAModification`, `OMSRMADeletion` | `categories/returns-rma.md` |
| Fulfillment / SIM / Delivery | `SIMDeliveryDetail`, `sim-client`, `sim-dispatch`, `extra-sim-client/common/server/ejb`, `DeliveryUpdate`, `CarreraTransferCreation`, `label_Package_Creation` | `categories/fulfillment-sim-delivery.md` |
| Inventory / Spare Parts | `Stockcheck`, `Stock_Feed`, `OmsOrposInventoryCheck`, `OmsOrposInventoryCheckNew`, `spareParts-stockRequest`, `SparePartsRequest`, `SparePartsConfirmation` | `categories/inventory-spare-parts.md` |
| Payment / Finance / Pricing / E-Invoicing | `PaymentConfirmation`, `VASContract` (+ its `SIMDeliveryDetail` sub-WAR), `apple-pricing`, `boot-apple-pricing`, `oms-discount`, `oms-finance`, `e-invoice-util`, `einvoicing`, `oms-einvoicingxml` | `categories/payment-finance-pricing-einvoicing.md` |
| Integration / Notification / Other | `BDS` (4 legacy WARs), `bds-service`, `jood`, `jood-membership-sync`, `home-maintenance-sync`, `NOON` (3 sub-modules), `AwbCustomerNotification`, `AwbOrder-Notification`, `Order-Notification`, `inactive_Track_id`, `notification`, `ecom-reconcilliation`, `cross-channel-service`, `proxy-api`, `imei-validation`, `extra-imei-capture`, `CR_17_ExtraAsCourier` | `categories/integration-notification-other.md` |
| Core gateway / shared client libraries | `OracleIntegrationServices` (legacy, orphaned from build), `oracle-base` (current), `oms-common` | `categories/core-gateway-oracleintegrationservices.md` |
| Not independently documented | `oms-parent` (Maven reactor POM only), `oms-build`, `oms-admin-template`, `oms-map-address-template` (front-end scaffolding, 0 Java files) | — |

## 5–6. API Catalog & Categorization

See `api-plsql-matrix.md` for the complete, single-table catalog of every API found in the repository (SOAP operation or REST endpoint), organized by the same business categories as the module table above. Every row links back to its full documentation in `categories/*.md`.

**Counts**: ~70 distinct externally-callable operations across ~40 deployable modules (a module may expose one operation — most SOAP WARs — or several — most REST controllers). Styles: SOAP (JAX-WS) ≈ 30 operations across ~20 modules; REST ≈ 40 endpoints across ~20 controllers; a handful of batch-only `main()` entry points with no network-facing API at all (counted in the matrix for completeness since they're "APIs" in the sense of externally-triggerable OMS entry points, even though triggered by cron rather than HTTP).

## 13. Architecture Diagram

See §3 above.

## 14. Gaps and Unknowns

See `unknowns.md` for the full, categorized list (confirmed / inferred / requires verification).

## 17. Shared Infrastructure / Java Class Dependency Highlights

Three pieces of shared code are reused across most of the legacy SOAP modules and are worth calling out because they are **not owned by any single business category**:

- **`OMSUtil` module** — the shared DAO/utility library for the legacy JAX-WS generation of services. Its centerpiece is `OMSUtilSessionEJB`/`OMSUtilSessionEJBBean`, a stateless EJB3 session bean exposing roughly **1,400 JPA finder/persist/merge methods** over ~190 entity classes mapping the `OMS_*`/`ORPOS_*` schema — this is the de facto DB access layer for `CustomerOrderService`, `CustomerOrderBeanPOS`, `OMSCustomerOrder`, `PaymentConfirmation`, the three RMA modules, `SparePartsRequest`/`Confirmation`/`Cancel`, `OmsOrposInventoryCheck` (legacy), and others. `OMSUtil` also supplies `OMSUtilCommons` (business-rule helpers, some of which call raw JDBC/PL/SQL directly — e.g. `OMS_SHIP_CLASSIFICATION`), `OMSUtil.java`/`OracleBaseAPIUtil.java` (JNDI/connection helpers), and `OmsStatusUpdateForReturnPickupCancellation` (the Java-side Siebel `UpdateOrderStatus` client wrapper used by multiple modules).
- **`oms-common` module** — the shared library for the newer Spring/Feign generation of services. Supplies `IOracleRMSClient`/`IOracleSIMClient`/`IOracleInvAdjClient` (Feign SOAP contracts wrapping the RMS/SIM integration services), `ICarreraClient`, `IMuleAddressClient`, a vendored `feign-soap` codec fork, and shared request/response DTOs (including a `com.extra.bds.bean.*` package matching the home-delivery-booking domain, though no caller of it was found within `oms-common` itself — its consumer is `bds-service`, documented separately).
- **`oracle-base`** (superseding the orphaned `OracleIntegrationServices`) — the generated JAX-WS client-stub library for every standard RMS/SIM/RWMS integration service plus the Siebel `UpdateOrderStatus`/BPEL contracts, rebuilt via `wsimport` against externally-hosted QA WSDL URLs (per its `pom.xml`).

No module in this repository implements a **server-side** JAX-WS/SOAP endpoint for any of the RMS/SIM/RWMS/Siebel contracts — every one of these three shared libraries is exclusively a client. See `categories/core-gateway-oracleintegrationservices.md` for the full evidence trail.

## 19. Oracle RMS Standard vs. Custom Components

| Component | Standard RMS/SIM/RWMS | Custom (this repo) |
|---|---|---|
| Order/inventory/item/location master data | ✅ RMS 14 (external) | — |
| Store inventory execution | ✅ SIM 14 (external) | `extra-imei-capture` plugin adds IMEI/serial capture + ship-trailer capture on top of SIM |
| Warehouse returns processing | ✅ RWMS (external) | RMA modules are custom Java wrappers around RWMS's `PendingReturnsService` |
| RIB shipment/receiving message transport | ✅ standard RIB | `OMSSUB_ASNOUT`/`OMSSUB_RECEIVING` are custom **subscribers** plugged into RMS's standard subscriber framework (`API_LIBRARY`), not a modification of RIB itself |
| Order-status feed to CRM | — (not an RMS/SIM concept) | 100% custom — both the PL/SQL `*_WS_INVOKER`/`OMS_MSG_WS_PUBLISHER` chain and the Java `OmsStatusUpdateForReturnPickupCancellation` path |
| Delivery-address change validation | — | 100% custom (`XX_DLV_ADDRESS_UPDATE`), plus a second, independent Java-only implementation in `oms-core` |
| Refund orchestration | — | 100% custom (`XX_REFUND_REQUEST_PKG`), multi-channel (SADAD/Payfort/Tasheel/Benefit/AFS/COD) |
| Home-delivery slot booking | — | 100% custom (`XXHDB_CORE_PKG` + `bds-service`) |
| Loyalty/cashback ("Jood") | — | 100% custom, backed by an external CloudHub-hosted loyalty platform |
| Order cancellation business rules | — | 100% custom (`OMSCOCancellation`/`oms-cancellation`'s `XX_IS_CANCELLABLE`, `OMS_INVADJ_STATUS_UNAVIALINV`) |
| Spare-parts service-order fulfillment | — | 100% custom (`OMS_SPARE_PART_*` tables/flows), reuses SIM's `InventoryAdjustmentService`/`StoreToStoreTransferService` for the physical stock movement |
| Marketplace (noon.com) sync | — | 100% custom (`NOON/*` modules) |
| ZATCA e-invoicing | — | 100% custom (`einvoicing`, `oms-einvoicingxml`, `e-invoice-util`), reuses standard Oracle BI Publisher for report generation per the payment/finance category doc |
| Apple product pricing sync | — | 100% custom |
| eXtra-as-courier (self-issued AWB/labels) | — | 100% custom (`XX_HB_MAKE_LABEL`/`XX_CREATE_LABEL`, WMS-schema-backed) |

**Why the custom layer exists** (inferred from the above, consistent across every category): RMS/SIM/RWMS provide merchandising, store-execution, and warehouse-management primitives, but have no native concept of a unified customer order spanning multiple fulfillment types (ship-from-store, ship-from-warehouse, in-store pickup, spare-parts service), no native loyalty/cashback engine, no native home-delivery-slot scheduling, no native Saudi e-invoicing compliance, and no native CRM order-status feed. The custom OMS layer's `OMS_*`/`XX_*` schema and ~65 Java modules exist specifically to unify these concerns across RMS+SIM+RWMS and bridge them to the CRM (Siebel), the e-commerce platform (Hybris), the marketplace (noon.com), and third-party services (Apple, ZATCA, Jood/CloudHub).

## 23. Complete API Inventory

The complete inventory is the union of every row in every table in `api-plsql-matrix.md` — approximately 70 operations. Rather than duplicate that table here, refer to it directly; it is organized by business category and is the authoritative single-page inventory.

## 24. Complete PL/SQL Inventory

**Custom PL/SQL fully documented from source (10 packages)**: see `plsql-packages.md` — `OMSSUB_ASNOUT`, `OMSSUB_RECEIVING`, `OMS_ASNOUT_WS_INVOKER`, `OMS_RECEIPT_WS_INVOKER`, `OMS_SOS_WS_INVOKER`, `OMS_PENDRETURN_WS_INVOKER`, `OMS_MSG_WS_PUBLISHER`, `XX_DLV_ADDRESS_UPDATE`, `XX_REFUND_REQUEST_PKG`, `XXHDB_CORE_PKG`.

**Custom PL/SQL referenced from Java but not present in this repository (~20 packages/functions)**: see the table in `unknowns.md` — includes `OMS_SHIP_CLASSIFICATION`, `OMS_INVADJ_STATUS_UNAVIALINV`, `XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN`, `XX_IS_CANCELLABLE`, the `XX_JOOD_*` family, `XX_APPLE_ON_PRICING_SQL.*`, `XX_CREATE_LABEL`, `XX_HB_MAKE_LABEL`, `XTRA_TSF_CRE_SQL.XTRA_XTSF_CRE`, `XX_IMEI_SN_UPLOAD_VALIDATE.*`, `XX_IMEI_SN_CONFIG_VALIDATE.*`, `INACTIVE_TRACKING_ECOMM_ORDERS`, `GET_CLASSIFICATION_BO_TEST`, `rms14.xx_tsf_cre_carera_sql.xx_dc_tsf_cre`, and the unresolved `XXHDB_CORE_PKG` naming variants (`reserve_booking_small`, `cancel_booking_small`, `BOOK_ORDER`).

**Standard Oracle Retail RMS 14 PL/SQL**: not present in this repository at all — out of scope by definition (this is a third-party product).

## 25. Key Findings and Architectural Observations

1. **Two implementation generations coexist for the same business capability, more than once.** Order cancellation (`OMSCOCancellation` SOAP/EJB vs. `oms-cancellation` REST/JDBC), delivery-address update (`XX_DLV_ADDRESS_UPDATE`-backed `DeliveryUpdate` vs. `oms-core`'s independent raw-JDBC `OrderDAO.updateCustomerAddress`), and Apple pricing (`apple-pricing` WAR vs. `boot-apple-pricing` Spring Boot repackage, byte-for-byte equivalent logic) all have two parallel, independently-maintained implementations against the same schema. `Hybris_Cancellation` and `oms-cancellation-job` both call the *newer* `oms-cancellation` REST endpoint, and no caller of the legacy `OMSCOCancellation` SOAP service was found anywhere in the repository — suggesting it is superseded but not removed.
2. **The Siebel order-status feed is implemented twice, independently, in two different languages.** PL/SQL's `*_WS_INVOKER` chain stages messages to `OMS_PUBLISH_WS_DATA` for the `OMS_MSG_WS_PUBLISHER` cron job to send via `UTL_HTTP`; Java's `OmsStatusUpdateForReturnPickupCancellation` attempts a synchronous JAX-WS call and falls back to writing directly into the shared `OMS_REPUBLISH_DATA` failure-audit table on error. Both target the same `UpdateOrderStatus` SOAP contract and the same `OMS_WEBSERVICE_URI_DETAIL` configuration table, but neither path is aware of the other's queue.
3. **The outbound send in the current PL/SQL invoker chain is dormant.** All four `*_WS_INVOKER` packages have their actual `generic_oms_soap_call`/`UTL_HTTP` invocation commented out — they only ever stage to `OMS_PUBLISH_WS_DATA`, relying entirely on `OMS_MSG_WS_PUBLISHER` to perform the real send. `OMSSUB_RECEIVING`'s Siebel-notify call is similarly commented out at its call site. This means the receiving/ASN-out flows' Siebel notification currently depends entirely on the cron-scheduled republish job, not on any synchronous path.
4. **`XXHDB_CORE_PKG` has more surface area in production than in this repository's copy of its source.** Three independent Java call sites (in three different business categories) call procedure names (`reserve_booking_small`, `cancel_booking_small`, `BOOK_ORDER`) that don't appear verbatim in the package body checked into this repo, which only defines `get_schedule`/`reserve_booking`/`create_booking`/`confirm_booking`/`cancel_booking`. This is flagged for explicit database verification rather than assumed to be a naming mismatch in the Java code.
5. **Three PL/SQL packages are genuinely orphaned on both sides.** `OMS_SOS_WS_INVOKER` and `OMS_PENDRETURN_WS_INVOKER` have no caller anywhere in this repository (Java or the other 9 PL/SQL packages) — their caller is entirely external to this checked-in source. `RWMS PendingReturnsService`'s generated Java client stub is similarly uncalled from anywhere in the Java codebase, mirroring `OMS_PENDRETURN_WS_INVOKER`'s orphaned status — both halves of the "pending returns to RWMS" integration appear to be unused dead code as shipped, even though the three live RMA modules do call RWMS's `PendingReturnsService` through their own, separate direct SOAP client code.
6. **At least one confirmed non-functional endpoint is live in the deployment surface.** `oms-customer-order`'s `POST /createNewOrder` validates its input using a partial reimplementation of the real order-creation rules, but never persists, never calls RMS/SIM, and always returns a hardcoded success message regardless of what it found — it cannot be relied on as an e-commerce order-intake path despite matching that description by name and location.
7. **The custom OMS schema (`OMS_*`, `XX_*`, `EXT_*` prefixes) is the true system of record for order lifecycle state**, cross-referenced constantly against RMS's own `ORDCUST`/`ITEM_MASTER`/transfer tables and SIM's `FUL_ORD*` tables — often via direct cross-schema SQL over DB links (`@rmsdb`, `@simdb`) rather than exclusively through the web-service layer. This means RMS/SIM database schema changes have a wider blast radius on this OMS layer than the SOAP contracts alone would suggest.
8. **Compensating-transaction rollback, not database transactions, is the primary consistency mechanism** for any operation that spans OMS DB + RMS + SIM (order creation, payment confirmation, cancellation, spare-parts reservation) — because no single database transaction can span these systems. Several modules implement this correctly with dedicated `*ReversalBean` classes; at least two modules (`oms-cancellation`, `Hybris_Cancellation`) have `@Transactional`-annotated methods with no transaction manager configured, meaning even their local, single-database multi-statement operations are not atomic.
