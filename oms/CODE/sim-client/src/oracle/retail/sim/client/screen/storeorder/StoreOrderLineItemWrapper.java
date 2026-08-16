package oracle.retail.sim.client.screen.storeorder;

import java.math.BigDecimal;
import java.util.List;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.ItemStatus;
import oracle.retail.sim.common.item.ItemType;
import oracle.retail.sim.common.item.OrderItem;
import oracle.retail.sim.common.item.SupplierItem;
import oracle.retail.sim.common.itemrequest.ItemRequestMessageText;
import oracle.retail.sim.common.lineitem.OrderLineItemWrapper;
import oracle.retail.sim.common.rules.core.CaseSizeAndQtyValidForUomRule;
import oracle.retail.sim.common.storeorder.StoreOrder;
import oracle.retail.sim.common.storeorder.StoreOrderLineItem;
import oracle.retail.sim.common.storeorder.StoreOrderMessageText;
import oracle.retail.sim.common.storeorder.StoreOrderProperty;
import oracle.retail.sim.common.storeorder.StoreOrderType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Store Order Line Item Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class StoreOrderLineItemWrapper extends OrderLineItemWrapper {

    private StoreOrder storeOrder;
    private StoreOrderLineItem lineItem;

    /**
     * Creates an empty line item. This is used so that the user can add a row (with no real backing line
     * item) and fill it in later.
     */
    public StoreOrderLineItemWrapper(StoreOrderLineItem lineItem, StoreOrder storeOrder) {
        this.lineItem = lineItem;
        this.storeOrder = storeOrder;
    }

    public void setOrderItem(OrderItem orderItem) throws Exception {
        if (orderItem == null) {
            return;
        }
        if (orderItem.getItemType() == ItemType.CONSIGNMENT) {
            throw new BusinessException(CommonMessageText.CONSIGNMENT_ITEM_ERROR);
        }
        if (orderItem.getItemType() == ItemType.CONCESSION) {
            throw new BusinessException(CommonMessageText.CONCESSION_ITEM_ERROR);
        }
        if (orderItem.getItemType() == ItemType.NON_INVENTORY) {
            throw new BusinessException(CommonMessageText.NON_INVENTORY_ITEM_ERROR);
        }
        if (!orderItem.isOrderable()) {
            throw new BusinessException(CommonMessageText.NON_ORDERABLE_ITEM_ERROR);
        }
        if (SimConfigManager.getBoolean(SimConfigManager.RESTRICT_STORE_ORDERABLE_ITEMS)) {
            if (!orderItem.isStoreOrderReplenishmentType()) {
                throw new BusinessException(ItemMessageText.ITEM_NOT_STORE_ORDERABLE);
            }
        }

        ItemStatus status = orderItem.getStatus();
        if (status == ItemStatus.DELETED || status == ItemStatus.INACTIVE || status == ItemStatus.DISCONTINUED) {
            throw new BusinessException(ItemRequestMessageText.INVALID_ITEM_STATE, status.toString());
        }

        List<SupplierItem> supplierItems = null;
        SupplierItem supplierItem = null;

        if (storeOrder.getType() == StoreOrderType.PURCHASE_ORDER) {
            supplierItems = ClientServiceFactory.getItemServices().findSupplierItems(orderItem.getId(), storeOrder.getFromLocation().getId());
            if (supplierItems == null || supplierItems.size() == 0) {
                throw new BusinessException(ItemMessageText.ITEM_NOT_AVAILABLE_FROM_SUPPLIER);
            }
            for (SupplierItem tempSupplierItem : supplierItems) {
                if (tempSupplierItem.getCountryCode().equals(storeOrder.getToLocation().getCountry())) {
                    supplierItem = tempSupplierItem;
                    break;
                }
            }
        }

        if (storeOrder.getType() == StoreOrderType.TRANSFER) {
            if (!ClientServiceFactory.getItemServices().isItemShippedByWarehouse(orderItem.getId(), storeOrder.getFromLocation().getId())) {
                throw new BusinessException(StoreOrderMessageText.NO_SOURCE_FOR_ITEM);
            }
        }

        if (lineItem != null) {
            storeOrder.removeStoreOrderLineItem(lineItem);
        }

        if (storeOrder.getLineItems().size() > 0) {
            for (StoreOrderLineItem lineItem : storeOrder.getLineItems()) {
                OrderItem lineOrderItem = lineItem.getOrderItem();
                if (lineOrderItem != null && orderItem != null && orderItem.getId().equals(lineOrderItem.getId())) {
                    throw new BusinessException(StoreOrderMessageText.ITEM_ALREADY_EXISTS);
                }
            }
        }

        lineItem = BOFactory.createStoreOrderLineItem(orderItem);

        if (storeOrder.getType() == StoreOrderType.PURCHASE_ORDER) {
            if (supplierItem == null) {
                supplierItem = supplierItems.get(0);
            }
            lineItem.setOriginCountryId(supplierItem.getCountryCode());
            lineItem.setUnitCost(supplierItem.getUnitCost());
            lineItem.setCaseSize(supplierItem.getCaseSize());
        }

        storeOrder.addLineItem(lineItem);
    }

    public Quantity getCaseSize() {
        if (lineItem.getOrderItem() == null) {
            return null;
        }
        return isCasesMode() ? lineItem.getCaseSize() : Quantity.ONE;
    }

    public void setCaseSize(Quantity caseSize) throws BusinessException {
        CaseSizeAndQtyValidForUomRule.execute(this, caseSize, getQuantity());
        lineItem.setCaseSize(caseSize);
    }

    public SimMoney getUnitCost() {
        return lineItem.getOrderItem() != null ? lineItem.getUnitCost() : null;
    }

    public void setUnitCost(SimMoney money) throws BusinessException {
        lineItem.setUnitCost(money);
    }

    public String getDescription() {
        if (lineItem.getOrderItem() == null) {
            return null;
        }
        if (SimConfigManager.isItemShortDescription()) {
            return lineItem.getOrderItem().getShortDescription();
        }
        return lineItem.getOrderItem().getLongDescription();
    }

    public String getStandardUnitOfMeasure() {
        return lineItem.getOrderItem() != null ? lineItem.getOrderItem().getUnitOfMeasure() : null;
    }

    public boolean isPreferredUomConversionAvailable() {
        return false;
    }

    public String getPreferredUnitOfMeasure() {
        return null;
    }

    public BigDecimal getPreferredUomConversionFactor() {
        return null;
    }

    public Quantity getQuantity() {
        return lineItem.getQuantity();
    }

    public Quantity getQuantityOrZero() {
        Quantity quantity = lineItem.getQuantity();
        return quantity != null ? quantity : Quantity.ZERO;
    }

    public void setQuantity(Quantity quantity) throws BusinessException {
        lineItem.setQuantity(quantity);
    }

    public Quantity getQuantityBasedOnUomOrZero() {
        Quantity quantity = getQuantityBasedOnUom();
        return quantity != null ? quantity : Quantity.ZERO;
    }

    public Quantity getQuantityBasedOnUom() {
        return rationalizeQuantityBasedOnUom(lineItem.getQuantity());
    }

    public void setQuantityBasedOnUom(Quantity quantity) throws BusinessException {
        executeRule("setQuantityBasedOnUom", quantity);
        lineItem.setQuantity(quantity.multiply(getCaseSize()));
    }

    public boolean isNewWrapper() {
        return lineItem == null;
    }

    public StoreOrderLineItem getLineItem() {
        return lineItem;
    }

    public OrderItem getOrderItem() {
        return lineItem != null ? lineItem.getOrderItem() : null;
    }

    public void cancelLineItem() {
        if (lineItem != null) {
            storeOrder.removeStoreOrderLineItem(lineItem);
        }
    }

    /**
     * Determines if the given property is modifiable. If this returns false, then this property is not modifiable.
     */
    public boolean isPropertyModifiable(String propertyName) {
        if (StoreOrderProperty.ORDER_ITEM.equals(propertyName)) {
            return true;
        }
        if (StoreOrderProperty.CASE_SIZE.equals(propertyName)) {
            if (isStandardMode()) {
                return false;
            }
        }
        if (lineItem != null) {
            return lineItem.isPropertyModifiable(propertyName);
        }
        return false;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        StoreOrderLineItemWrapper other = (StoreOrderLineItemWrapper) object;
        if (lineItem == null && other.lineItem == null) {
            return true;
        }
        if (lineItem == null || other.lineItem == null) {
            return false;
        }
        return lineItem.equals(other.lineItem);
    }

    public int hashCode() {
        return super.hashCode();
    }
}
