package oracle.retail.sim.client.screen.itemrequest;

import java.math.BigDecimal;
import oracle.retail.sim.client.security.PermissionManager;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.deliverytimeslot.DeliveryTimeSlot;
import oracle.retail.sim.common.item.ItemStatus;
import oracle.retail.sim.common.item.OrderItem;
import oracle.retail.sim.common.itemrequest.ItemRequest;
import oracle.retail.sim.common.itemrequest.ItemRequestLineItem;
import oracle.retail.sim.common.itemrequest.ItemRequestMessageText;
import oracle.retail.sim.common.itemrequest.ItemRequestProperty;
import oracle.retail.sim.common.lineitem.OrderLineItemWrapper;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.rules.core.CaseSizeAndQtyValidForUomRule;
import oracle.retail.sim.common.rules.core.QuantityCannotBeNegativeRule;
import oracle.retail.sim.common.rules.core.QuantityMustBePositiveRule;
import oracle.retail.sim.common.rules.core.QuantityMustConformToUOMRule;
import oracle.retail.sim.common.security.PermissionKey;

/********************************************************************************************************
 * Item Request Line Item Wrapper. This is used specifically in the PC UI only.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemRequestLineItemWrapper extends OrderLineItemWrapper {
    private ItemRequest itemRequest;
    private ItemRequestLineItem lineItem;

    public ItemRequestLineItemWrapper(ItemRequest itemRequest) {
        this(itemRequest, null);
    }

    public ItemRequestLineItemWrapper(ItemRequest itemRequest, ItemRequestLineItem lineItem) {
        if (itemRequest == null) {
            throw new IllegalArgumentException("Item Request can NOT be null!");
        }
        this.itemRequest = itemRequest;
        this.lineItem = lineItem;
    }

    public ItemRequest getItemRequest() {
        return itemRequest;
    }

    public ItemRequestLineItem getLineItem() {
        return lineItem;
    }

    public OrderItem getOrderItem() {
        return lineItem != null ? lineItem.getOrderItem() : null;
    }

    public String getDescription() {
        if (lineItem == null) {
            return null;
        }
        if (SimConfigManager.isItemShortDescription()) {
            return lineItem.getOrderItem().getShortDescription();
        }
        return lineItem.getOrderItem().getLongDescription();
    }

    public Quantity getCaseSize() {
        if (lineItem != null) {
            return isCasesMode() ? lineItem.getCaseSize() : Quantity.ONE;
        }
        return null;
    }

    public String getStandardUnitOfMeasure() {
        if (lineItem != null && lineItem.getOrderItem() != null) {
            return lineItem.getOrderItem().getUnitOfMeasure();
        }
        return null;
    }

    public UOMMode getUnitOfMeasureMode() {
        if (lineItem != null) {
            OrderItem orderItem = lineItem.getOrderItem();
            if (orderItem.isPack() && !orderItem.isSimple() && !orderItem.isSellable()) {
                setCasesMode();
            }
            return super.getUnitOfMeasureMode();
        }
        return getDefaultUomMode();
    }

    public Quantity getQuantityOrZero() {
        if (lineItem != null) {
            return lineItem.getQuantity() != null ? lineItem.getQuantity() : Quantity.ZERO;
        }
        return Quantity.ZERO;
    }

    public Quantity getQuantityBasedOnUom() {
        return lineItem != null ? rationalizeQuantityBasedOnUom(lineItem.getQuantity()) : null;
    }

    public Quantity getAvailableSOH() {
        return lineItem != null ? rationalizeQuantityBasedOnUom(getOrderItem().getAvailableStockOnHand()) : null;
    }

    public Quantity getInTransitQuantity() {
        if (lineItem != null) {
            Quantity inTransitQuantity = getOrderItem().getInTransitQty();
            if (inTransitQuantity != null) {
                return rationalizeQuantityBasedOnUom(inTransitQuantity);
            }
            return Quantity.ZERO;
        }
        return null;
    }

    public void setOrderItem(OrderItem orderItem) throws BusinessException {
        if (orderItem == null) {
            return;
        }
        ItemStatus status = orderItem.getStatus();
        if (status == ItemStatus.INACTIVE) {
            throw new BusinessException(ItemRequestMessageText.ITEM_REQ_INACTIVE_ITEM);
        }
        if (status == ItemStatus.DELETED) {
            throw new BusinessException(ItemRequestMessageText.ITEM_REQ_DELETED_ITEM);
        }
        if (status == ItemStatus.DISCONTINUED) {
            if (!RConfirmUtility.confirm("Item Request", CommonMessageText.NON_ACTIVE_ITEM_CONFIRM, status.toString())) {
                return;
            }
        }
        if (lineItem == null) {
            lineItem = itemRequest.createLineItem(orderItem);
            return;
        }
        if (!orderItem.equals(lineItem.getOrderItem())) {
            itemRequest.removeLineItem(lineItem);
            lineItem = null;
            lineItem = itemRequest.createLineItem(orderItem);
            setDeliveryTimeslot(itemRequest.getDeliveryTimeSlot());
        }
    }

    public void setCaseSize(Quantity caseSize) throws BusinessException {
        CaseSizeAndQtyValidForUomRule.execute(this, caseSize, getQuantityOrZero());
        lineItem.setCaseSize(caseSize);
    }

    public void setQuantity(Quantity quantity) throws BusinessException {
        QuantityMustBePositiveRule.execute(quantity);
        QuantityCannotBeNegativeRule.execute(quantity);
        QuantityMustConformToUOMRule.execute(this, quantity);
        executeRule("setQuantity", quantity);
        lineItem.setQuantity(quantity);
    }

    public void setQuantityBasedOnUom(Quantity quantity) throws BusinessException {
        QuantityCannotBeNegativeRule.execute(quantity);
        QuantityMustConformToUOMRule.execute(this, quantity);
        executeRule("setQtyBasedOnUOM", quantity);
        lineItem.setQuantity(quantity.multiply(getCaseSize()));
    }

    public DeliveryTimeSlot getDeliveryTimeslot() {
        return lineItem != null ? lineItem.getDeliveryTimeSlot() : null;
    }

    public void setDeliveryTimeslot(DeliveryTimeSlot timeslot) throws BusinessException {
        if (lineItem != null && lineItem.isStoreOrderReplenishmentType() && lineItem.isMultipleDeliveryAllowed()) {
            lineItem.setDeliveryTimeSlot(timeslot);
        }
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

    public boolean isPropertyModifiable(String propertyName) {
        if (getDeliveryTimeslot() != null && !PermissionManager.hasDataPermission(PermissionKey.DATA_ITEM_REQUEST_DELIVERY_TIMESLOT, getDeliveryTimeslot().getId())) {
            return false;
        }
        if (propertyName.equals(ItemRequestProperty.ORDER_ITEM)) {
            return itemRequest.isPropertyModifiable(ItemRequestProperty.ITEM_REQUEST_LINE_ITEM);
        }
        if (propertyName.equals(ItemRequestProperty.CASE_SIZE) && isStandardMode()) {
            return false;
        }
        if (lineItem != null) {
            return lineItem.isPropertyModifiable(propertyName, itemRequest);
        }
        return propertyName.equals(ItemRequestProperty.ORDER_ITEM);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        ItemRequestLineItemWrapper other = (ItemRequestLineItemWrapper) object;
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

    public String toString() {
        StringBuilder buffer = new StringBuilder("ItemRequestLineItemWrapper=[");
        buffer.append(lineItem.toString());
        buffer.append("]");
        return buffer.toString();
    }
}
