package oracle.retail.sim.client.screen.directdelivery;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.directdelivery.DirectDelivery;
import oracle.retail.sim.common.directdelivery.DirectDeliveryVO;
import oracle.retail.sim.common.directdelivery.PurchaseOrder;
import oracle.retail.sim.common.directdelivery.PurchaseOrderQueryFilter;
import oracle.retail.sim.common.directdelivery.PurchaseOrderStatus;
import oracle.retail.sim.common.directdelivery.PurchaseOrderVO;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Purchase Order List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class PurchaseOrderListModel extends SimScreenModel {
    public List<PurchaseOrderVO> getPurchaseOrders() throws Exception {
        return ClientServiceFactory.getDirectDeliveryServices().findPurchaseOrderVOs(getFilter());
    }

    public PurchaseOrderQueryFilter getFilter() {
        PurchaseOrderQueryFilter filter = (PurchaseOrderQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.PURCHASE_ORDER_FILTER);
        if (filter == null) {
            filter = BOFactory.createPurchaseOrderQueryFilter();
            filter.doSetStatus(PurchaseOrderStatus.ACTIVE);
            filter.doSetStoreId(getStoreId());
            RepositoryManager.addStateObject(SimClientStateKey.PURCHASE_ORDER_FILTER, filter);
        }
        return filter;
    }

    public boolean isCreateDeliveryAllowed() {
        if (!hasPermission(PermissionKey.PC_EDIT_DIRECT_DELIVERY)) {
            return false;
        }
        if (!isCreateDeliveryWithAsnAllowed() && !isCreateDeliveryWithoutAsnAllowed()) {
            return false;
        }
        return true;
    }

    public boolean isCreateDeliveryWithAsnAllowed() {
        return hasPermission(PermissionKey.PC_CREATE_DIRECT_DELIVERY_WITH_PO_WITH_ASN);
    }

    public boolean isCreateDeliveryWithoutAsnAllowed() {
        return hasPermission(PermissionKey.PC_CREATE_DIRECT_DELIVERY_WITH_PO_WITHOUT_ASN);
    }

    public boolean isPurchaseOrderClosed(PurchaseOrderVO purchaseOrder) {
        return PurchaseOrderStatus.getClosedSet().contains(purchaseOrder.getStatus());
    }

    public PurchaseOrder getPurchaseOrder(Long purchaseOrderId) throws Exception {
        return ClientServiceFactory.getDirectDeliveryServices().readPurchaseOrder(purchaseOrderId);
    }

    public void storePurchaseOrder(PurchaseOrder purchaseOrder) {
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_PURCHASE_ORDER, purchaseOrder);
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

    public boolean obtainLock(DirectDeliveryVO deliveryVO) throws Exception {
        return obtainLock(ActivityLockType.DIRECT_DELIVERY, deliveryVO.getId().toString());
    }

	public boolean obtainLock(PurchaseOrderVO purchaseOrderVO) throws Exception {
		return obtainLock(ActivityLockType.PURCHASE_ORDER, purchaseOrderVO.getExternalId().toString() + getStoreId());
	}

    public void storeDelivery(DirectDelivery delivery) {
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_DIRECT_DELIVERY, delivery);
    }

    public void storeOpenAsnsDeliveryVOs(List<DirectDeliveryVO> deliveryVOs) {
        RepositoryManager.addStateObject(SimClientStateKey.ASNS_FOR_SELECTION, deliveryVOs);
    }

    public void clearState() {
        RepositoryManager.removeStateObject(SimClientStateKey.ASNS_FOR_SELECTION);
        RepositoryManager.removeStateObject(SimClientStateKey.DIRECT_DELIVERY_CANCELED);
        RepositoryManager.removeStateObject(SimClientStateKey.PURCHASE_ORDER_FILTER);
        RepositoryManager.removeStateObject(SimClientStateKey.PURCHASE_ORDER_FILTER_MODIFIED);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_DIRECT_DELIVERY);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_PURCHASE_ORDER);
    }

    public Map<String, String> getDescriptionMap() {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        PurchaseOrderQueryFilter filter = getFilter();
        if (filter.getFromDate() != null) {
            descriptionMap.put("From Date", LocaleManager.getShortDateFormatter().format(filter.getFromDate()));
        }
        if (filter.getToDate() != null) {
            descriptionMap.put("To Date", LocaleManager.getShortDateFormatter().format(filter.getToDate()));
        }
        if (filter.getExternalId() != null) {
            descriptionMap.put("PO Number", filter.getExternalId());
        }
        if (filter.getSupplierId() != null) {
            descriptionMap.put("Supplier", filter.getSupplierId());
        }
        if (filter.getStatus() != null) {
            descriptionMap.put("Status", filter.getStatus().toString());
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
        return descriptionMap;
    }
}
