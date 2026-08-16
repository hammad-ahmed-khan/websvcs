package oracle.retail.sim.client.screen.fulfillmentorder;

import java.math.BigDecimal;
import java.util.Date;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderLineItem;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderProperty;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.lineitem.StockLineItemWrapper;

/********************************************************************************************************
 * Wrapper for a Fulfillment Order Line Item to assist display in the table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FulfillmentOrderLineItemWrapper extends StockLineItemWrapper {

    private FulfillmentOrder order;
    private FulfillmentOrderLineItem lineItem;
    private BigDecimal uomConversionFactor;

    public FulfillmentOrderLineItemWrapper(FulfillmentOrder order, FulfillmentOrderLineItem lineItem, BigDecimal uomConversionFactor) {
        this.order = order;
        this.lineItem = lineItem;
        this.uomConversionFactor = uomConversionFactor;
    }

    /**
     * Returns the line item identifier.
     * @return The line item identifier.
     */
    public Long getId() {
        return lineItem.getId();
    }

    /**
     * Returns the item id of the line item.
     * @return the item id of the line item.
     */
    public String getItemId() {
        return lineItem.getItemId();
    }

    /**
     * The item description of the line item.
     * @return The item description of the line item.
     */
    public String getItemDescription() {
        return lineItem.getItemDescription();
    }

    /**
     * Returns the external comments on the line item.
     * @return The external comments on the line item.
     */
    public String getComments() {
        return lineItem.getComments();
    }

    /**
     * Returns the item id of the FulfillmentOrderLineItem that this FulfillmentOrderLineItem
     * was substituted for.
     * @return The item id of the FulfillmentOrderLineItem that this FulfillmentOrderLineItem
     * was substituted for, or null of this line item was not a substitute.
     */
    public String getSubstituteItemId() {
        Long substituteId = lineItem.getSubstituteLineItemId();
        if (substituteId == null) {
            return null;
        }
        for (FulfillmentOrderLineItem lineItem : order.getLineItems()) {
            if (lineItem.getId().equals(substituteId)) {
                return lineItem.getItemId();
            }
        }
        return null;
    }

    public Date getCreatedDate() {
        return lineItem.getCreatedDate();
    }

    public Date getUpdatedDate() {
        return lineItem.getUpdatedDate();
    }

    /**
     * Returns the quantity delivered.
     * @return The quantity delivered of the FulfillmentOrderLineItem.
     */
    public Quantity getDeliveredQty() throws Exception {
        return rationalizeQuantityBasedOnUom(lineItem.getDeliveredQuantity());
    }

    /**
     * Returns the quantity ordered.
     * @return The quantity ordered of the FulfillmentOrderLineItem.
     */
    public Quantity getOrderedQty() throws Exception {
        return rationalizeQuantityBasedOnUom(lineItem.getOrderedQuantity());
    }

    /**
     * Returns the picked quantity.
     * @return The picked quantity of the FulfillmentOrderLineItem.
     */
    public Quantity getPickedQty() {
        return rationalizeQuantityBasedOnUom(lineItem.getPickedQuantity());
    }

    /**
     * Returns the remaining quantity to be delivered before the FulfillmentOrderLineItem
     * is considered to be fully delivered.
     * @return The remaining quantity to be delivered before the FulfillmentOrderLineItem
     * is considered to be fully delivered.
     */
    public Quantity getRemainingQty() {
        return rationalizeQuantityBasedOnUom(order.getRemainingQty(lineItem.getId()));
    }

    /**
     * Returns the canceled quantity of the FulfillmentOrderLineItem.
     * @return The canceled quantity of the FulfillmentOrderLineItem.
     */
    public Quantity getCanceledQty() {
        return rationalizeQuantityBasedOnUom(lineItem.getCanceledQuantity());
    }

    public StockItem getStockItem() {
        return lineItem.getStockItem();
    }

    public Quantity getCaseSize() {
        if (lineItem != null && isCasesMode()) {
            return lineItem.getStockItem().getDefaultCaseSize();
        }
        return Quantity.ONE;
    }

    public String getPreferredUnitOfMeasure() {
        return lineItem.getPreferredUom();
    }

    public BigDecimal getPreferredUomConversionFactor() {
        return uomConversionFactor;
    }

    public boolean isPropertyModifiable(String property) {
        if (FulfillmentOrderProperty.UOM_MODE.equals(property)) {
            return true;
        }
        return false;
    }
}
