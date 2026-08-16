package oracle.retail.sim.client.screen.warehousedelivery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderVO;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.warehousedelivery.WarehouseDelivery;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCarton;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryMessageText;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryQueryFilter;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryStatus;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Warehouse Delivery List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class WarehouseDeliveryListModel extends SimScreenModel {
    public List<WarehouseDeliveryVO> getDeliveries() throws Exception {
        return ClientServiceFactory.getWarehouseDeliveryServices().findWarehouseDeliveryVOs(getFilter());
    }

    public WarehouseDeliveryQueryFilter getFilter() {
        WarehouseDeliveryQueryFilter filter = (WarehouseDeliveryQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_FILTER);
        if (filter == null) {
            filter = BOFactory.createWarehouseDeliveryQueryFilter();
            filter.doSetStoreId(getStoreId());
            filter.doSetStatus(WarehouseDeliveryStatus.ACTIVE);
            RepositoryManager.addStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_FILTER, filter);
        }
        return filter;
    }

    public void printDeliveries(List<WarehouseDeliveryVO> deliveryVOs) throws Exception {
        Long storeId = getStoreId();
        List<RetailStoreFormatPrinter> formatPrinters = SimClientPrintUtility.selectFormatPrinter(storeId, ReportFormat.WAREHOUSE_DELIVERY);
        if (formatPrinters.isEmpty()) {
            return;
        }
        List<ReportRequest> reportRequests = new ArrayList<ReportRequest>();
        for (WarehouseDeliveryVO deliveryVO : deliveryVOs) {
            reportRequests.add(BOFactory.createWarehouseDeliveryReportRequest(deliveryVO.getId()));
        }
        SimClientPrintUtility.printReportRequests(reportRequests, formatPrinters, WarehouseDeliveryMessageText.WAREHOUSE_DELIVERY_PRINTED);
    }

    public boolean isCanceled() {
        if (RepositoryManager.getStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_CANCELED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_CANCELED);
            return true;
        }
        return false;
    }

    public boolean isDeliveryEditAllowed() {
        return hasPermission(PermissionKey.PC_EDIT_WAREHOUSE_DELIVERY);
    }

    public boolean isDeliveryClosed(WarehouseDelivery delivery) {
        return WarehouseDeliveryStatus.getClosedSet().contains(delivery.getStatus());
    }

    public boolean obtainLock(WarehouseDelivery delivery) throws Exception {
        return obtainLock(ActivityLockType.WAREHOUSE_DELIVERY, delivery.getId().toString());
    }

    public WarehouseDelivery getDelivery(Long deliveryId) throws Exception {
        return ClientServiceFactory.getWarehouseDeliveryServices().readWarehouseDelivery(deliveryId);
    }

    public List<WarehouseDeliveryCartonWrapper> getCartonWrappers(WarehouseDelivery delivery) {
        List<WarehouseDeliveryCarton> cartons = delivery.getCartons();
        List<WarehouseDeliveryCartonWrapper> wrappers = new ArrayList<WarehouseDeliveryCartonWrapper>(cartons.size());
        for (WarehouseDeliveryCarton carton : cartons) {
            wrappers.add(ClientWrapperFactory.createWarehouseDeliveryCartonWrapper(carton));
        }
        return wrappers;
    }

    public void markNewInProgress(WarehouseDelivery delivery) throws Exception {
        if (delivery.getStatus() == WarehouseDeliveryStatus.NEW) {
            delivery.markInProgress();
        }
    }

    public List<FulfillmentOrderVO> getFulfillmentOrderVOs(Long deliveryId) throws Exception {
        return ClientServiceFactory.getFulfillmentOrderServices().findFulfillmentOrderVOsForWarehouseDelivery(deliveryId);
    }

    public void storeFulfillmentOrderVOs(List<FulfillmentOrderVO> fulfillmentOrderVOs) {
        RepositoryManager.addStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_FULFILLMENT_ORDERS, fulfillmentOrderVOs);
    }

    public void storeDelivery(WarehouseDelivery delivery) {
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_WAREHOUSE_DELIVERY, delivery);
    }

    public void storeCartonWrappers(List<WarehouseDeliveryCartonWrapper> cartonWrappers) {
        RepositoryManager.addStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_FULFILLMENT_ORDER_CARTONS, cartonWrappers);
    }

    public void storeViewOnly() {
        RepositoryManager.addStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_VIEW_ONLY, true);
    }

    public void clearSelectedState() {
        RepositoryManager.removeStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_CANCELED);
        RepositoryManager.removeStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_VIEW_ONLY);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_WAREHOUSE_DELIVERY);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_CARTON);
        RepositoryManager.removeStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_WORKING_DELIVERY);
        RepositoryManager.removeStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_FULFILLMENT_ORDERS);
        RepositoryManager.removeStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_FULFILLMENT_ORDER_LINE_ITEMS);
    }

    public void clearState() {
        clearSelectedState();
        RepositoryManager.removeStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_FILTER);
        RepositoryManager.removeStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_FILTER_MODIFIED);
    }

    public Map<String, String> getDescriptionMap() {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        WarehouseDeliveryQueryFilter filter = getFilter();
        if (filter.getFromDate() != null) {
            descriptionMap.put("From Date", LocaleManager.getShortDateFormatter().format(filter.getFromDate()));
        }
        if (filter.getToDate() != null) {
            descriptionMap.put("To Date", LocaleManager.getShortDateFormatter().format(filter.getToDate()));
        }
        if (filter.getAsnId() != null) {
            descriptionMap.put("ASN", filter.getAsnId());
        }
        if (filter.isAnyFulfillmentOrder() && filter.getCustomerOrderId() == null && filter.getFulfillmentOrderExternalId() == null) {
            descriptionMap.put("Customer Orders", String.valueOf(filter.isAnyFulfillmentOrder()));
        } else {
            if (filter.getCustomerOrderId() != null) {
                descriptionMap.put("Customer Order", filter.getCustomerOrderId());
            }
            if (filter.getFulfillmentOrderExternalId() != null) {
                descriptionMap.put("Fulfillment Order", filter.getFulfillmentOrderExternalId());
            }
        }
        if (filter.getStatus() != null) {
            descriptionMap.put("Status", Translator.getText(filter.getStatus().toString()));
        }
        if (filter.getSourceId() != null) {
            if (filter.getSourceType() == SourceType.FINISHER) {
                descriptionMap.put("From Finisher", filter.getSourceId());
            } else {
                descriptionMap.put("From Warehouse", filter.getSourceId());
            }
        }
        if (filter.getContextType() != null) {
            descriptionMap.put("Context Type", filter.getContextType().getName());
        }
        if (filter.getContextValue() != null) {
            descriptionMap.put("Context Value", filter.getContextValue());
        }
        return descriptionMap;
    }
}
