package oracle.retail.sim.client.screen.directdelivery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.uom.UomUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.directdelivery.DirectDelivery;
import oracle.retail.sim.common.directdelivery.DirectDeliveryVO;
import oracle.retail.sim.common.directdelivery.PurchaseOrder;
import oracle.retail.sim.common.directdelivery.PurchaseOrderLineItem;
import oracle.retail.sim.common.directdelivery.PurchaseOrderStatus;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Direct Delivery Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class PurchaseOrderDetailModel extends SimScreenModel {
    private PurchaseOrder purchaseOrder;

    public void loadState() {
        purchaseOrder = (PurchaseOrder) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_PURCHASE_ORDER);
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public List<PurchaseOrderLineItemWrapper> getLineItemWrappers() throws Exception {
        List<PurchaseOrderLineItem> lineItems = purchaseOrder.getLineItems();
        if (lineItems.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, StockItem> itemMap = loadStockItems(lineItems);
        List<PurchaseOrderLineItemWrapper> wrappers = new ArrayList<PurchaseOrderLineItemWrapper>(lineItems.size());
        for (PurchaseOrderLineItem lineItem : lineItems) {
            StockItem stockItem = itemMap.get(lineItem.getSupplierItem().getItemId());
            PurchaseOrderLineItemWrapper wrapper = ClientWrapperFactory.createPurchaseOrderLineItemWrapper(lineItem);
            wrapper.setStockItem(stockItem);
            wrapper.setPreferredUomConversionFactor(UomUtility.getStandardUomToTargetUom(stockItem, lineItem.getPreferredUom()));
            wrappers.add(wrapper);
        }
        return wrappers;
    }

    private Map<String, StockItem> loadStockItems(List<PurchaseOrderLineItem> lineItems) throws Exception {
        if (lineItems.isEmpty()) {
            return Collections.emptyMap();
        }
        Set<String> itemIds = new HashSet<String>(lineItems.size());
        for (PurchaseOrderLineItem lineItem : lineItems) {
            itemIds.add(lineItem.getSupplierItem().getItemId());
        }
        return ClientServiceFactory.getItemServices().readStockItems(itemIds, getStoreId());
    }

    public boolean isCreateDeliveryAllowed() {
        if (!hasPermission(PermissionKey.PC_EDIT_DIRECT_DELIVERY)) {
            return false;
        }
        if (!isCreateDeliveryWithAsnAllowed() && !isCreateDeliveryWithoutAsnAllowed()) {
            return false;
        }
        if (PurchaseOrderStatus.getClosedSet().contains(purchaseOrder.getStatus())) {
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

    public List<DirectDeliveryVO> findOpenAsnDirectDeliveryVOs() throws Exception {
        return ClientServiceFactory.getDirectDeliveryServices().findOpenAsnDirectDeliveryVOs(purchaseOrder.getId(), true);
    }

    public DirectDelivery createDeliveryForPurchaseOrder() throws Exception {
        return ClientServiceFactory.getDirectDeliveryServices().createDirectDeliveryForPurchaseOrder(purchaseOrder.getId());
    }

    public DirectDelivery prepareAsnForPurchaseOrder(Long deliveryId) throws Exception {
        return ClientServiceFactory.getDirectDeliveryServices().prepareDirectDeliveryAsnForPurchaseOrder(deliveryId);
    }

    public boolean obtainLock(DirectDeliveryVO deliveryVO) throws Exception {
        return obtainLock(ActivityLockType.DIRECT_DELIVERY, deliveryVO.getId().toString());
    }

	public boolean obtainLock(PurchaseOrder purchaceOrder) throws Exception {
		return obtainLock(ActivityLockType.PURCHASE_ORDER, purchaceOrder.getExternalId().toString()  + getStoreId());
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
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_DIRECT_DELIVERY);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_PURCHASE_ORDER);
    }
}
