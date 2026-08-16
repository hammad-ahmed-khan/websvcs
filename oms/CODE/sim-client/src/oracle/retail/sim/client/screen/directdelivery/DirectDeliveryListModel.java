package oracle.retail.sim.client.screen.directdelivery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.directdelivery.DirectDelivery;
import oracle.retail.sim.common.directdelivery.DirectDeliveryInvoiceEntryType;
import oracle.retail.sim.common.directdelivery.DirectDeliveryMessageText;
import oracle.retail.sim.common.directdelivery.DirectDeliveryQueryFilter;
import oracle.retail.sim.common.directdelivery.DirectDeliveryStatus;
import oracle.retail.sim.common.directdelivery.DirectDeliveryVO;
import oracle.retail.sim.common.directdelivery.PurchaseOrderVO;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Direct Delivery List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class DirectDeliveryListModel extends SimScreenModel {
    public List<DirectDeliveryVO> getDeliveries() throws Exception {
        return ClientServiceFactory.getDirectDeliveryServices().findDirectDeliveryVOs(getFilter(), true);
    }

    public DirectDeliveryQueryFilter getFilter() {
        DirectDeliveryQueryFilter filter = (DirectDeliveryQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.DIRECT_DELIVERY_FILTER);
        if (filter == null) {
            filter = BOFactory.createDirectDeliveryQueryFilter();
            filter.doSetStoreId(getStoreId());
            filter.doSetStatus(DirectDeliveryStatus.ACTIVE);
            RepositoryManager.addStateObject(SimClientStateKey.DIRECT_DELIVERY_FILTER, filter);
        }
        return filter;
    }

    public void printDeliveries(List<DirectDeliveryVO> deliveryVOs) throws Exception {
        Long storeId = getStoreId();
        List<RetailStoreFormatPrinter> formatPrinters = SimClientPrintUtility.selectFormatPrinter(storeId, ReportFormat.DIRECT_DELIVERY);
        if (formatPrinters.isEmpty()) {
            return;
        }
        List<ReportRequest> reportRequests = new ArrayList<ReportRequest>();
        for (DirectDeliveryVO deliveryVO : deliveryVOs) {
            reportRequests.add(BOFactory.createDirectDeliveryReportRequest(deliveryVO.getId()));
        }
        SimClientPrintUtility.printReportRequests(reportRequests, formatPrinters, DirectDeliveryMessageText.DIRECT_DELIVERY_PRINTED);
    }

    public boolean isCanceled() {
        if (RepositoryManager.getStateObject(SimClientStateKey.DIRECT_DELIVERY_CANCELED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.DIRECT_DELIVERY_CANCELED);
            return true;
        }
        return false;
    }

    public boolean isDeliveryEditAllowed() {
        return hasPermission(PermissionKey.PC_EDIT_DIRECT_DELIVERY);
    }

    public boolean isCreateDeliveryWithAsnAllowed() {
        return hasPermission(PermissionKey.PC_CREATE_DIRECT_DELIVERY_WITH_PO_WITH_ASN);
    }

    public boolean isCreateDeliveryWithoutAsnAllowed() {
        return hasPermission(PermissionKey.PC_CREATE_DIRECT_DELIVERY_WITH_PO_WITHOUT_ASN);
    }    
    
    public boolean isDeliveryNew(DirectDelivery delivery) {
        return delivery.getStatus().equals(DirectDeliveryStatus.NEW);
    }
    
    public List<DirectDeliveryVO> findOpenAsnDirectDeliveryVOs(Long purchaseOrderId) throws Exception {
        return ClientServiceFactory.getDirectDeliveryServices().findOpenAsnDirectDeliveryVOs(purchaseOrderId, true);
    }

    public DirectDelivery createDeliveryForPurchaseOrder(Long purchaseOrderId) throws Exception {
        return ClientServiceFactory.getDirectDeliveryServices().createDirectDeliveryForPurchaseOrder(purchaseOrderId);
    }

    public DirectDelivery prepareAsnForPurchaseOrder(Long deliveryId) throws Exception {
        return ClientServiceFactory.getDirectDeliveryServices().prepareDirectDeliveryAsnForPurchaseOrder(deliveryId);
    }

    public boolean isDeliveryClosed(DirectDelivery delivery) {
        return DirectDeliveryStatus.getClosedSet().contains(delivery.getStatus());
    }

    public boolean obtainLock(DirectDelivery delivery) throws Exception {
        return obtainLock(ActivityLockType.DIRECT_DELIVERY, delivery.getId().toString());
    }

    public boolean obtainLock(DirectDeliveryVO deliveryVO) throws Exception {
        return obtainLock(ActivityLockType.DIRECT_DELIVERY, deliveryVO.getId().toString());
    }

    public boolean obtainLock(PurchaseOrderVO purchaseOrderVO) throws Exception {
		return obtainLock(ActivityLockType.PURCHASE_ORDER, purchaseOrderVO.getExternalId().toString() + getStoreId());
	}

    public DirectDelivery getDelivery(Long deliveryId) throws Exception {
        return ClientServiceFactory.getDirectDeliveryServices().readDirectDelivery(deliveryId);
    }

    public void markNewInProgress(DirectDelivery delivery) throws Exception {
        switch (delivery.getStatus()) {
            case NEW:
            case DEXNEX:
                delivery.markInProgress();
        }
    }

    public boolean isInvoiceEntryEnabled() {
        DirectDeliveryInvoiceEntryType invoiceEntryType = null;
        Integer value = SimConfigManager.getStoreInteger(StoreConfigKeys.DIRECT_DELIVERY_INVOICE_ENTRY, getStoreId());
        if (value != null) {
            invoiceEntryType = DirectDeliveryInvoiceEntryType.toValue(value);
        }
        return invoiceEntryType != DirectDeliveryInvoiceEntryType.DISABLED;
    }

    public void storeDelivery(DirectDelivery delivery) {
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_DIRECT_DELIVERY, delivery);
    }

    public void storeViewOnly() {
        RepositoryManager.addStateObject(SimClientStateKey.DIRECT_DELIVERY_VIEW_ONLY, true);
    }

    public void storeOpenAsnsDeliveryVOs(List<DirectDeliveryVO> deliveryVOs) {
        RepositoryManager.addStateObject(SimClientStateKey.ASNS_FOR_SELECTION, deliveryVOs);
    }

    public void clearState() {
        RepositoryManager.removeStateObject(SimClientStateKey.ASNS_FOR_SELECTION);
        RepositoryManager.removeStateObject(SimClientStateKey.DIRECT_DELIVERY_CANCELED);
        RepositoryManager.removeStateObject(SimClientStateKey.DIRECT_DELIVERY_FILTER);
        RepositoryManager.removeStateObject(SimClientStateKey.DIRECT_DELIVERY_FILTER_MODIFIED);
        RepositoryManager.removeStateObject(SimClientStateKey.DIRECT_DELIVERY_VIEW_ONLY);
        RepositoryManager.removeStateObject(SimClientStateKey.PURCHASE_ORDER_FILTER);
        RepositoryManager.removeStateObject(SimClientStateKey.PURCHASE_ORDER_FILTER_MODIFIED);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_DIRECT_DELIVERY);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_PURCHASE_ORDER);
    }

    public Map<String, String> getDescriptionMap() {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        DirectDeliveryQueryFilter filter = getFilter();
        if (filter.getFromDate() != null) {
            descriptionMap.put("From Date", LocaleManager.getShortDateFormatter().format(filter.getFromDate()));
        }
        if (filter.getToDate() != null) {
            descriptionMap.put("To Date", LocaleManager.getShortDateFormatter().format(filter.getToDate()));
        }
        if (filter.getPurchaseOrderExternalId() != null) {
            descriptionMap.put("PO Number", filter.getPurchaseOrderExternalId());
        }
        if (filter.getInvoiceNumber() != null) {
            descriptionMap.put("Invoice Number", filter.getInvoiceNumber());
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
        if (filter.getSupplierId() != null) {
            descriptionMap.put("Supplier", filter.getSupplierId());
        }
        if (filter.getStatus() != null) {
            descriptionMap.put("Status", filter.getStatus().toString());
        }
        if (filter.getUserId() != null) {
            descriptionMap.put("User", filter.getUserId());
        }
        return descriptionMap;
    }
}
