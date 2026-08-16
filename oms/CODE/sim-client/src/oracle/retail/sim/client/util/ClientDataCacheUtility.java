package oracle.retail.sim.client.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.security.PermissionManager;
import oracle.retail.sim.common.configutil.CommonConfigManager;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.deliverytimeslot.DeliveryTimeSlot;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.item.NonSellableQtyType;
import oracle.retail.sim.common.itemprice.PriceInfo;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;
import oracle.retail.sim.common.itemticket.TicketTypeId;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.shipment.ShipmentCarrierService;
import oracle.retail.sim.common.shipment.ShipmentCartonType;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.source.Warehouse;
import oracle.retail.sim.common.stockreturn.ReturnReason;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.common.uda.UDADetail;
import oracle.retail.sim.common.uda.UDAType;
import oracle.retail.sim.common.uda.UDAValue;
import oracle.retail.sim.common.util.CacheRefreshStrategy;
import oracle.retail.sim.common.util.SystemTimestampProvider;
import oracle.retail.sim.common.util.TimedCacheRefreshStrategy;
import oracle.retail.sim.common.util.TimestampProvider;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Utility that holds caches of basic data that are timed at a refresh rate.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class ClientDataCacheUtility {
    private static TimestampProvider timestampProvider = new SystemTimestampProvider();

    private static CacheRefreshStrategy adjustmentReasonsCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_ACTIVE_INV_ADJ_REASON, 3_600_000L);
    private static CacheRefreshStrategy agsnTicketFormatCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_AGSN_TICKET_FORMAT, 3_600_000L);
    private static CacheRefreshStrategy carrierServiceCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_CARRIER_SERVICE, 3_600_000L);
    private static CacheRefreshStrategy cartonTypeCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_CARTON_TYPE, 3_600_000L);
    private static CacheRefreshStrategy contextTypeCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_CONTEXT_TYPE, 3_600_000L);
    private static CacheRefreshStrategy deliveryTimeSlotCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_DELIVERY_TIMESLOT, 3_600_000L);
    private static CacheRefreshStrategy finisherReturnReasonCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_FINISHER_RETURN_REASON, 3_600_000L);
    private static CacheRefreshStrategy itemTicketFormatCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_ITEM_TICKET_FORMAT, 3_600_000L);
    private static CacheRefreshStrategy itemTicketLabelFormatCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_ITEM_TICKET_FORMAT, 3_600_000L);

    private static CacheRefreshStrategy nonSellableQtyTypeCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_NONSELLABLE_QTY_TYPE, 3_600_000L);
    private static CacheRefreshStrategy priceHistoryCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_PRICE_HISTORY, 3_600_000L);
    private static CacheRefreshStrategy shelfLabelFormatCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_SHELF_LABEL_FORMAT, 3_600_000L);
    private static CacheRefreshStrategy storeCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_STORE, 3_600_000L);
    private static CacheRefreshStrategy supplierCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_SUPPLIER, 3_600_000L);
    private static CacheRefreshStrategy supplierReturnReasonCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_SUPPLIER_RETURN_REASON, 3_600_000L);
    private static CacheRefreshStrategy udaDetailsCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_UDA_DETAILS, 3_600_000L);
    private static CacheRefreshStrategy udaValuesCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_UDA_LOV, 3_600_000L);
    private static CacheRefreshStrategy uomConversionCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_UOM_CONVERSION, 3_600_000L);
    private static CacheRefreshStrategy warehouseCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_WAREHOUSE, 3_600_000L);
    private static CacheRefreshStrategy warehouseReturnReasonCacheTimer = new TimedCacheRefreshStrategy(timestampProvider, CommonConfigManager.REFRESH_RATE_WAREHOUSE_RETURN_REASON, 3_600_000L);

    private static List<InventoryAdjustmentReason> inventoryAdjustmentReasons = new ArrayList<>();
    private static List<ContextType> contextTypes = new ArrayList<>();
    private static List<DeliveryTimeSlot> deliveryTimeSlots = new ArrayList<>();
    private static List<UDADetail> udaDetails = new ArrayList<>();
    private static List<NonSellableQtyType> nonSellableQtyTypes = new ArrayList<>();
    private static List<ShipmentCarrierService> carrierServices = new ArrayList<>();

    private static List<ReturnReason> warehouseReturnReasons = new ArrayList<>();
    private static List<ReturnReason> supplierReturnReasons = new ArrayList<>();
    private static List<ReturnReason> finisherReturnReasons = new ArrayList<>();

    private static Map<Long, List<TicketTypeFormat>> itemTicketFormatMap = new HashMap<>();
    private static Map<Long, List<TicketTypeFormat>> shelfLabelFormatMap = new HashMap<>();
    private static Map<Long, List<TicketTypeFormat>> agsnTicketFormatMap = new HashMap<>();
    private static Map<Long, List<TicketTypeFormat>> itemTicketLabelFormatMap = new HashMap<>();

    private static Map<Long, Store> storeMap = new HashMap<>();
    private static Map<String, Warehouse> warehouseMap = new HashMap<>();
    private static Map<Long, Map<String, List<Supplier>>> storeSupplierMap = new HashMap<>();
    private static Map<Long, Map<String, List<PriceInfo>>> storePriceHistoryMap = new HashMap<>();
    private static Map<Long, List<ShipmentCartonType>> cartonTypeMap = new HashMap<>();
    private static Map<Long, List<UDAValue>> udaValueMap = new HashMap<>();
    private static Map<String, BigDecimal> uomConversionMap = new HashMap<>();

    private ClientDataCacheUtility() {
    }

    public static List<InventoryAdjustmentReason> getInventoryAdjustmentReasons() throws Exception {
        if (inventoryAdjustmentReasons.isEmpty() || adjustmentReasonsCacheTimer.isCacheStale()) {
            inventoryAdjustmentReasons = ClientServiceFactory.getInventoryAdjustmentServices().findAllInventoryAdjustmentReasons();
        }
        return inventoryAdjustmentReasons;
    }

    public static List<InventoryAdjustmentReason> getDisplayableInventoryAdjustmentReasons() throws Exception {
        List<InventoryAdjustmentReason> displayableReasons = new ArrayList<>();
        for (InventoryAdjustmentReason reason : getInventoryAdjustmentReasons()) {
            if (reason.isDisplayable()) {
                displayableReasons.add(reason);
            }
        }
        return displayableReasons;
    }

    public static List<ContextType> getContextTypes() throws Exception {
        if (contextTypes.isEmpty() || contextTypeCacheTimer.isCacheStale()) {
            contextTypes = ClientServiceFactory.getSourceServices().findAllContextTypes();
        }
        return contextTypes;
    }

    public static List<DeliveryTimeSlot> getDeliveryTimeSlots() throws Exception {
        if (deliveryTimeSlots.isEmpty() || deliveryTimeSlotCacheTimer.isCacheStale()) {
            deliveryTimeSlots = new ArrayList<>();
            List<DeliveryTimeSlot> allTimeslots = ClientServiceFactory.getItemRequestServices().findDeliveryTimeSlots();
            for (DeliveryTimeSlot timeslot : allTimeslots) {
                if (PermissionManager.hasDataPermission(PermissionKey.DATA_ITEM_REQUEST_DELIVERY_TIMESLOT, timeslot.getId())) {
                    deliveryTimeSlots.add(timeslot);
                }
            }
        }
        return deliveryTimeSlots;
    }

    public static List<UDADetail> getUDADetails() throws Exception {
        if (udaDetails.isEmpty() || udaDetailsCacheTimer.isCacheStale()) {
            udaDetails = ClientServiceFactory.getUDAServices().findAllUDADetails();
        }
        return udaDetails;
    }

    public static List<UDAValue> getUDAValues(UDADetail udaDetail) throws Exception {
        if (udaDetail == null) {
            return Collections.emptyList();
        }
        if (udaDetail.getType() != UDAType.VALUE) {
            return Collections.emptyList();
        }
        List<UDAValue> udaValues = udaValueMap.get(udaDetail.getId());
        if (udaValues == null || udaValues.isEmpty() || udaValuesCacheTimer.isCacheStale()) {
            udaValues = ClientServiceFactory.getUDAServices().findUDAValues(udaDetail.getId());
            udaValueMap.put(udaDetail.getId(), udaValues);
        }
        return udaValues;
    }

    public static List<ReturnReason> getWarehouseReturnReasons() throws Exception {
        if (warehouseReturnReasons.isEmpty() || warehouseReturnReasonCacheTimer.isCacheStale()) {
            warehouseReturnReasons = ClientServiceFactory.getReturnServices().findReturnReasons(SourceType.WAREHOUSE);
        }
        return warehouseReturnReasons;
    }

    public static List<ReturnReason> getSupplierReturnReasons() throws Exception {
        if (supplierReturnReasons.isEmpty() || supplierReturnReasonCacheTimer.isCacheStale()) {
            supplierReturnReasons = ClientServiceFactory.getReturnServices().findReturnReasons(SourceType.SUPPLIER);
        }
        return supplierReturnReasons;
    }

    public static List<ReturnReason> getFinisherReturnReasons() throws Exception {
        if (finisherReturnReasons.isEmpty() || finisherReturnReasonCacheTimer.isCacheStale()) {
            finisherReturnReasons = ClientServiceFactory.getReturnServices().findReturnReasons(SourceType.FINISHER);
        }
        return finisherReturnReasons;
    }

    public static List<TicketTypeFormat> getItemTicketFormats() throws Exception {
        List<TicketTypeFormat> itemTicketFormats = itemTicketFormatMap.get(SimRepository.getStoreId());
        if (itemTicketFormats == null || itemTicketFormats.isEmpty() || itemTicketFormatCacheTimer.isCacheStale()) {
            itemTicketFormats = ClientServiceFactory.getItemTicketServices().findTicketTypeFormats(SimRepository.getStoreId(), TicketTypeId.ITEM_TICKET_ID);
            itemTicketFormatMap.put(SimRepository.getStoreId(), itemTicketFormats);
        }
        return itemTicketFormats;
    }

    public static List<TicketTypeFormat> getShelfLabelFormats() throws Exception {
        List<TicketTypeFormat> shelfLabelFormats = shelfLabelFormatMap.get(SimRepository.getStoreId());
        if (shelfLabelFormats == null || shelfLabelFormats.isEmpty() || shelfLabelFormatCacheTimer.isCacheStale()) {
            shelfLabelFormats = ClientServiceFactory.getItemTicketServices().findTicketTypeFormats(SimRepository.getStoreId(), TicketTypeId.SHELF_LABEL_ID);
            shelfLabelFormatMap.put(SimRepository.getStoreId(), shelfLabelFormats);
        }
        return shelfLabelFormats;
    }

    public static List<TicketTypeFormat> getAGSNTicketFormats() throws Exception {
        List<TicketTypeFormat> agsnTicketFormats = agsnTicketFormatMap.get(SimRepository.getStoreId());
        if (agsnTicketFormats == null || agsnTicketFormats.isEmpty() || agsnTicketFormatCacheTimer.isCacheStale()) {
            agsnTicketFormats = ClientServiceFactory.getItemTicketServices().findTicketTypeFormats(SimRepository.getStoreId(), TicketTypeId.AGSN_ID);
            agsnTicketFormatMap.put(SimRepository.getStoreId(), agsnTicketFormats);
        }
        return agsnTicketFormats;
    }

    public static List<TicketTypeFormat> getItemTicketLabelFormats() throws Exception {
        List<TicketTypeFormat> itemTicketLabelFormats = itemTicketFormatMap.get(SimRepository.getStoreId());
        if (itemTicketLabelFormats == null || itemTicketLabelFormats.isEmpty() || itemTicketLabelFormatCacheTimer.isCacheStale()) {
            itemTicketLabelFormats = ClientServiceFactory.getItemTicketServices().findAllTicketTypeFormats(SimRepository.getStoreId());
            itemTicketFormatMap.put(SimRepository.getStoreId(), itemTicketLabelFormats);
        }
        return itemTicketLabelFormats;
    }

    public static List<NonSellableQtyType> getNonSellableQtyTypes() throws Exception {
        if (nonSellableQtyTypes.isEmpty() || nonSellableQtyTypeCacheTimer.isCacheStale()) {
            nonSellableQtyTypes = ClientServiceFactory.getInventoryAdjustmentServices().findNonSellableQtyTypes();
        }
        return nonSellableQtyTypes;
    }

    public static List<PriceInfo> getPriceHistory(String itemId, Long storeId) throws Exception {
        Map<String, List<PriceInfo>> itemPriceHistoryMap = storePriceHistoryMap.get(storeId);
        if (itemPriceHistoryMap == null || itemPriceHistoryMap.size() > 5 || priceHistoryCacheTimer.isCacheStale()) {
            itemPriceHistoryMap = new HashMap<>();
            storePriceHistoryMap.put(storeId, itemPriceHistoryMap);
        }
        List<PriceInfo> priceList = itemPriceHistoryMap.get(itemId);
        if (priceList == null) {
            priceList = ClientServiceFactory.getItemPriceServices().findItemPriceHistory(storeId, itemId, SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
            itemPriceHistoryMap.put(itemId, priceList);
        }
        return priceList;
    }

    public static List<Supplier> getSuppliersForItem(String itemId, Long storeId) throws Exception {
        Map<String, List<Supplier>> itemSupplierMap = storeSupplierMap.get(storeId);
        if (itemSupplierMap == null || itemSupplierMap.size() > 10 || supplierCacheTimer.isCacheStale()) {
            itemSupplierMap = new HashMap<>();
            storeSupplierMap.put(storeId, itemSupplierMap);
        }
        List<Supplier> suppliers = itemSupplierMap.get(itemId);
        if (suppliers == null) {
            suppliers = ClientServiceFactory.getSourceServices().findSuppliers(itemId, storeId);
            itemSupplierMap.put(itemId, suppliers);
        }
        return suppliers;
    }

    public static Map<String, Warehouse> getAllWarehouses() throws Exception {
        if (warehouseMap.isEmpty() || warehouseCacheTimer.isCacheStale()) {
            List<Warehouse> warehouses = ClientServiceFactory.getSourceServices().findAllWarehouses();
            warehouseMap = new HashMap<>(warehouses.size());
            for (Warehouse warehouse : warehouses) {
                warehouseMap.put(warehouse.getId(), warehouse);
            }
        }
        return warehouseMap;
    }

    public static List<ShipmentCarrierService> getAllCarrierServices() throws Exception {
        if (carrierServices.isEmpty() || carrierServiceCacheTimer.isCacheStale()) {
            carrierServices = ClientServiceFactory.getShipmentServices().findAllCarrierServices();
        }
        return carrierServices;
    }

    public static List<ShipmentCartonType> getCartonTypes(Long storeId) throws Exception {
        if (storeId == null) {
            return Collections.emptyList();
        }
        List<ShipmentCartonType> cartonTypes = cartonTypeMap.get(storeId);
        if (cartonTypes == null || cartonTypes.isEmpty() || cartonTypeCacheTimer.isCacheStale()) {
            cartonTypes = ClientServiceFactory.getShipmentServices().findCartonTypes(storeId);
            cartonTypeMap.put(storeId, cartonTypes);
        }
        return cartonTypes;
    }

    public static Store getStore(Long storeId) throws Exception {
        if (storeCacheTimer.isCacheStale()) {
            storeMap = new HashMap<>();
        }
        Store store = storeMap.get(storeId);
        if (store == null) {
            store = ClientServiceFactory.getStoreServices().readStore(storeId);
            if (store != null) {
                storeMap.put(storeId, store);
            }
        }
        return store;
    }

    public static BigDecimal getUomConversionFactor(String fromUOM, String toUOM) throws Exception {
        if (uomConversionCacheTimer.isCacheStale()) {
            uomConversionMap = new HashMap<>();
        }
        String cacheKey = fromUOM + "-" + toUOM;
        BigDecimal factor = uomConversionMap.get(cacheKey);
        if (factor == null) {
            factor = ClientServiceFactory.getUOMServices().findUOMConversionFactor(fromUOM, toUOM);
            if (factor != null) {
                uomConversionMap.put(cacheKey, factor);
            }
        }
        return factor;
    }

    public static void clearCache() {
        contextTypes.clear();
        deliveryTimeSlots.clear();
        inventoryAdjustmentReasons.clear();
        warehouseReturnReasons.clear();
        supplierReturnReasons.clear();
        finisherReturnReasons.clear();
        itemTicketFormatMap.clear();
        shelfLabelFormatMap.clear();
        agsnTicketFormatMap.clear();
        storePriceHistoryMap.clear();
        storeSupplierMap.clear();
        warehouseMap.clear();
        storeMap.clear();
        udaDetails.clear();
    }
}
