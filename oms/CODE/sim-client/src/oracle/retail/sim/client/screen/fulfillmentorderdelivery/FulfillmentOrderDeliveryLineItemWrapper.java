package oracle.retail.sim.client.screen.fulfillmentorderdelivery;

import java.math.BigDecimal;
import java.util.List;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.table.SimTableResetFocusException;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderLineItem;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderProperty;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryLineItem;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryStatus;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.lineitem.StockLineItemWrapper;
import oracle.retail.sim.common.rules.core.CaseSizeAndQtyValidForUomRule;
import oracle.retail.sim.common.uin.SerialNumberValue;

/********************************************************************************************************
 * Fulfillment Order Delivery Line Item Wrapper
 * <p>
 * Wraps a FulfillmentOrderDeliveryLineItem, the FulfillmentOrderDelivery it is on, the FulfillmentOrder
 * the delivery was created for, and the FulfillmentOrderLineItem associated to the delivery line item into
 * a class for presentation on the PC client.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderDeliveryLineItemWrapper extends StockLineItemWrapper {

    private FulfillmentOrderDelivery delivery;
    private FulfillmentOrderDeliveryLineItem lineItem;
    private FulfillmentOrder fulfillmentOrder;
    private FulfillmentOrderLineItem orderLineItem;
    private BigDecimal preferredUomConversionFactor;

    public FulfillmentOrderDeliveryLineItemWrapper(FulfillmentOrderDelivery delivery, FulfillmentOrderDeliveryLineItem lineItem, FulfillmentOrder fulfillmentOrder,
            FulfillmentOrderLineItem orderLineItem, BigDecimal preferredUomConversionFactor) {
        this.delivery = delivery;
        this.lineItem = lineItem;
        this.fulfillmentOrder = fulfillmentOrder;
        this.orderLineItem = orderLineItem;
        this.preferredUomConversionFactor = preferredUomConversionFactor;
    }

    /**
     * Returns the FulfillmentOrderDelivery of which this line item is a part.
     * @return The FulfillmentOrderDelivery of which this line item is a part.
     */
    public FulfillmentOrderDelivery getDelivery() {
        return delivery;
    }

    /**
     * Returns the StockItem associated to this line item.
     * @return The StockItem associated to this line item.
     */
    public StockItem getStockItem() {
        return orderLineItem.getStockItem();
    }

    /**
     * Returns the item id of the FulfillmentOrderLineItem associated to the delivery line item.
     * @return The item id of the FulfillmentOrderLineItem associated to the delivery line item.
     */
    public String getItemId() {
        return orderLineItem.getItemId();
    }

    /**
     * Returns the item description of the FulfillmentOrderLineItem associated to the delivery line item.
     * @return The item description of the FulfillmentOrderLineItem associated to the delivery line item.
     */
    public String getItemDescription() {
        return orderLineItem.getItemDescription();
    }

    /**
     * Returns the status of the FulfillmentOrderDelivery.
     * @return The current status of the FulfillmentOrderDelivery.
     */
    public FulfillmentOrderDeliveryStatus getStatus() {
        return delivery.getStatus();
    }

    /**
     * Returns the case size of the delivery line item.
     * @return The case size of the delivery line item.
     */
    public Quantity getCaseSize() {
        if (lineItem != null && isCasesMode()) {
            return lineItem.getCaseSize();
        }
        return Quantity.ONE;
    }

    /**
     * Sets the case size of the delivery line item.
     * @param caseSize The quantity of the case size of the delivery line item.
     */
    public void setCaseSize(Quantity caseSize) throws BusinessException {
        if (lineItem != null && isCasesMode()) {
            CaseSizeAndQtyValidForUomRule.execute(this, caseSize, getQuantityOrZero());
            lineItem.setCaseSize(caseSize);
        }
    }

    /**
     * Returns the preferred UOM for the FulfillmentOrderLineItem associated to the delivery line item.
     * @return the preferred UOM for the FulfillmentOrderLineItem associated to the delivery line item.
     */
    public String getPreferredUnitOfMeasure() {
        return orderLineItem.getPreferredUom();
    }

    /**
     * Returns the remaining quantity that needs to be delivered before the FulfillmentOrderLineItem is
     * fully delivered.
     * @return The remaining quantity that needs to be delivered before the FulfillmentOrderLineItem is
     * considered fully delivered.
     */
    public Quantity getRemainingQty() {
        return fulfillmentOrder.getRemainingQty(orderLineItem.getId());
    }

    public Quantity getRemainingQtyBasedOnUom() {
        return rationalizeQuantityBasedOnUom(fulfillmentOrder.getRemainingQty(orderLineItem.getId()));
    }

    /**
     * Returns the ordered quantity of the FulfillmentOrderLineItem.
     * @return The ordered quantity of the FulfillmentOrderLineItem.
     */
    public Quantity getOrderedQty() {
        return orderLineItem.getOrderedQuantity();
    }

    public Quantity getOrderedQtyBasedOnUom() {
        return rationalizeQuantityBasedOnUom(orderLineItem.getOrderedQuantity());
    }

    /**
     * Returns the picked quantity of the FulfillmentOrderLineItem.
     * @return The picked quantity of the FulfillmentOrderLineItem.
     */
    public Quantity getPickedQty() {
        return orderLineItem.getPickedQuantity();
    }

    public Quantity getPickedQtyBasedOnUom() {
        return rationalizeQuantityBasedOnUom(orderLineItem.getPickedQuantity());
    }

    /**
     * Returns the delivered quantity of the FulfillmentOrderLineItem.
     * @return The delivered quantity of the FulfillmentOrderLineItem.
     */
    public Quantity getDeliveredQty() {
        return orderLineItem.getDeliveredQuantity();
    }

    public Quantity getDeliveredQtyBasedOnUom() {
        return rationalizeQuantityBasedOnUom(orderLineItem.getDeliveredQuantity());
    }

    /**
     * Returns the canceled quantity of the FulfillmentOrderLineItem.
     * @return The canceled quantity of the FulfillmentOrderLineItem.
     */
    public Quantity getCanceledQty() {
        return orderLineItem.getCanceledQuantity();
    }

    public Quantity getCanceledQtyBasedOnUom() {
        return rationalizeQuantityBasedOnUom(orderLineItem.getCanceledQuantity());
    }

    /**
     * Returns the current delivery quantity of the delivery line item.
     * @return The current delivery quantity of the delivery line item.
     */
    public Quantity getQuantity() {
        return lineItem.getQuantity();
    }

    /**
     * Returns the current delivery quantity of the delivery line item, or zero quantity if it was null.
     * @return The current delivery quantity of the delivery line item, or zero quantity if it was null.
     */
    public Quantity getQuantityOrZero() {
        Quantity quantity = getQuantity();
        return quantity != null ? quantity : Quantity.ZERO;
    }

    public Quantity getQuantityBasedOnUom() {
        return rationalizeQuantityBasedOnUom(lineItem.getQuantity());
    }

    public void setQuantity(Quantity quantity) throws Exception {
        if (lineItem != null && quantity != null) {
            doSetQuantity(quantity);
        }
    }

    /* This method should be called by table only! */
    public void setQuantityBasedOnUom(Quantity quantity) throws Exception {
        try {
            setQuantityModifiedByUom(quantity);
        } catch (BusinessException exception) {
            UIStatusUtility.displayWarning(this, exception.getPrimaryMessageText());
            throw new SimTableResetFocusException();
        }
    }

    /* This method is standard access to setting quantity */
    public void setQuantityModifiedByUom(Quantity quantity) throws Exception {
        if (lineItem != null && quantity != null) {
            if (isCasesMode()) {
                quantity = quantity.multiply(getCaseSize());
            }
            if (isPreferredMode() && isPreferredUomConversionAvailable()) {
                quantity = quantity.divide(getPreferredUomConversionFactor());
            }
            if (doSetQuantity(quantity)) {
                return;
            }
            throw new SimTableResetFocusException();
        }
    }

    private boolean doSetQuantity(Quantity quantity) throws BusinessException {
        Quantity oldQuantity = lineItem.getQuantity();
        if (SimConfigManager.getStoreBoolean(StoreConfigKeys.PICKING_REQUIRED_FOR_CUSTOMER_ORDERS, delivery.getStoreId())) {
            if (quantity.subtract(getPickedQty().subtract(getDeliveredQty())).isPositive()) {
                lineItem.doSetQuantity(oldQuantity);
                throw new BusinessException(FulfillmentOrderMessageText.QUANTITY_CANNOT_EXCEED_PICKED);
            }
        } else {
            if (getPickedQty().subtract(getOrderedQty()).isPositive()) {
                if (quantity.subtract(getPickedQty().subtract(getDeliveredQty())).isPositive()) {
                    lineItem.doSetQuantity(oldQuantity);
                    throw new BusinessException(FulfillmentOrderMessageText.QUANTITY_CANNOT_EXCEED_PICKED);
                }
            } else {
                if (quantity.subtract(getRemainingQty()).isPositive()) {
                    lineItem.doSetQuantity(oldQuantity);
                    throw new BusinessException(FulfillmentOrderMessageText.QUANTITY_CANNOT_EXCEED_REMAINING);
                }
            }
        }
        boolean assignQuantity = true;
        //We need to subtract reserved and prompt if they are going over it and touching available SOH
        if (quantity.subtract(orderLineItem.getReservedQuantity()).isPositive()) {
            if (orderLineItem.getStockItem().isQtyGreaterThanAvailableStockOnHand(quantity.subtract(orderLineItem.getReservedQuantity()))) {
                assignQuantity = RConfirmUtility.confirm("Quantity Confirmation", FulfillmentOrderMessageText.CONFIRM_DELIVERY_QUANTITY);
            }
        }
        if (assignQuantity) {
            lineItem.setQuantity(quantity);
            return true;
        }
        return false;
    }

    /**
     * Adds the input serial number to the delivery line item.
     * @param value The input serial number to add to the delivery line item.
     */
    public void addSerialNumber(SerialNumberValue value) throws BusinessException {
        lineItem.addSerialNumber(value);
    }

    /**
     * Removes the input serial number from the delivery line item.
     * @param value The input serial number to remove from the delivery line item.
     */
    public void removeSerialNumber(SerialNumberValue value) throws BusinessException {
        if (value != null) {
            lineItem.removeSerialNumber(value.getUin());
        }
    }

    /**
     * Returns the number of serial numbers currently on the delivery line item.
     * @return The number of serial numbers currently on the delivery line item.
     */
    public Integer getSerialNumberCount() {
        return lineItem != null ? lineItem.getSerialNumbers().size() : 0;
    }

    /**
     * Returns whether or not the FulfillmentOrderLineItem associated to the delivery line item
     * requires the user of serial numbers.
     * @return True if the use of serial numbers is required, otherwise false.
     */
    public boolean isSerialNumberRequired() {
        return orderLineItem.isUINRequired();
    }

    /**
     * Returns a List of SerialNumberValues attached to the delivery line item.
     * @return A List of SerialNumberValues attached to the delivery line item.
     */
    public List<SerialNumberValue> getSerialNumbers() {
        return lineItem.getSerialNumbers();
    }

    /**
     * Returns a List of removed SerialNumberValues attached to the delivery line item.
     * @return A List of removed SerialNumberValues attached to the delivery line item.
     */
    public List<SerialNumberValue> getRemovedSerialNumbers() {
        return lineItem.getRemovedSerialNumbers();
    }

    /**
     * Sets the current delivery quantity to the number of serial numbers currently attached
     * to the current delivery line item.
     */
    public void setQtyBasedOnSerialNumbers() throws BusinessException {
        lineItem.setQuantity(new Quantity(getSerialNumbers().size()));
    }

    /**
     * Returns the Item Id of the FulfillmentOrderLineItem that the FulfillmentOrderLineItem
     * associated to the current delivery line item was substituted for.
     * @return The Item Id of the FulfillmentOrderLineItem that the FulfillmentOrderLineItem
     * associated to the current delivery line item was substituted for.
     */
    public String getSubstituteId() {
        Long substituteId = orderLineItem.getSubstituteLineItemId();
        for (FulfillmentOrderLineItem tmpLineItem : fulfillmentOrder.getLineItems()) {
            if (tmpLineItem.getId().equals(substituteId)) {
                return tmpLineItem.getItemId();
            }
        }
        return null;
    }

    public BigDecimal getPreferredUomConversionFactor() {
        return preferredUomConversionFactor;
    }

    public boolean isPropertyModifiable(String property) {
        if (property.equals(FulfillmentOrderProperty.SERIAL_NUMBER_COUNT)) {
            return lineItem != null && isSerialNumberRequired();
        }
        if (FulfillmentOrderProperty.UOM_MODE.equals(property)) {
            return true;
        }
        if (delivery.getStatus() != FulfillmentOrderDeliveryStatus.IN_PROGRESS) {
            return false;
        }
        if (FulfillmentOrderProperty.QUANTITY_BASED_ON_UOM.equals(property)) {
            return lineItem != null && !isSerialNumberRequired();
        }
        if (property.equals(FulfillmentOrderProperty.CASE_SIZE)) {
            return lineItem != null && delivery.getStatus() == FulfillmentOrderDeliveryStatus.IN_PROGRESS && isCasesMode() && !SimConfigManager.getBoolean(SimConfigManager.DISABLE_CASE_SIZE);
        }
        return false;
    }
}
