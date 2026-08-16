package oracle.retail.sim.client.screen.fulfillmentorderreversepick;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.table.SimTableResetFocusException;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderLineItem;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickProperty;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePick;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickLineItem;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickProperty;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.lineitem.StockLineItemWrapper;

/********************************************************************************************************
 * Fulfillment Order Reverse Pick Line Item Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderReversePickLineItemWrapper extends StockLineItemWrapper {

    private FulfillmentOrderReversePick reversePick;
    private FulfillmentOrderReversePickLineItem reversePickLineItem;
    private FulfillmentOrder fulfillmentOrder;
    private FulfillmentOrderLineItem fulfillmentOrderLineItem;
    private Map<Long, FulfillmentOrderLineItem> orderLineItemMap = new HashMap<Long, FulfillmentOrderLineItem>();
    private BigDecimal preferredUomConversionFactor;

    public FulfillmentOrderReversePickLineItemWrapper(FulfillmentOrderReversePick reversePick, FulfillmentOrderReversePickLineItem reversePickLineItem) {
        this.reversePick = reversePick;
        this.reversePickLineItem = reversePickLineItem;
    }

    public void setFulfillmentOrder(FulfillmentOrder fulfillmentOrder) {
        this.fulfillmentOrder = fulfillmentOrder;
        for (FulfillmentOrderLineItem orderLineItem : fulfillmentOrder.getLineItems()) {
            orderLineItemMap.put(orderLineItem.getId(), orderLineItem);
        }
    }

    public void setFulfillmentOrderLineItem(FulfillmentOrderLineItem fulfillmentOrderLineItem) {
        this.fulfillmentOrderLineItem = fulfillmentOrderLineItem;
    }

    public FulfillmentOrderReversePick getReversePick() {
        return reversePick;
    }

    public FulfillmentOrderReversePickLineItem getReversePickLineItem() {
        return reversePickLineItem;
    }

    public StockItem getStockItem() {
        return fulfillmentOrderLineItem.getStockItem();
    }

    public String getItemId() {
        return fulfillmentOrderLineItem.getStockItem().getId();
    }

    public String getItemDescription() {
        return fulfillmentOrderLineItem.getStockItem().getItemDescription();
    }

    public Quantity getSuggestedQty() {
        return reversePickLineItem != null ? reversePickLineItem.getSuggestedQty() : null;
    }

    public Quantity getSuggestedQtyBasedOnUom() {
        return reversePickLineItem != null ? rationalizeQuantityBasedOnUom(reversePickLineItem.getSuggestedQty()) : null;
    }

    public Quantity getRemainingQty() {
        return fulfillmentOrder.getRemainingQty(fulfillmentOrderLineItem.getId());
    }

    public Quantity getRemainingQtyBasedOnUom() {
        return rationalizeQuantityBasedOnUom(fulfillmentOrder.getRemainingQty(fulfillmentOrderLineItem.getId()));
    }

    public Quantity getOrderedQtyBasedOnUom() {
        return rationalizeQuantityBasedOnUom(fulfillmentOrderLineItem.getOrderedQuantity());
    }

    public Quantity getPickedQtyBasedOnUom() {
        return rationalizeQuantityBasedOnUom(fulfillmentOrderLineItem.getPickedQuantity());
    }

    public Quantity getDeliveredQtyBasedOnUom() {
        return rationalizeQuantityBasedOnUom(fulfillmentOrderLineItem.getDeliveredQuantity());
    }

    public Quantity getCanceledQtyBasedOnUom() {
        return rationalizeQuantityBasedOnUom(fulfillmentOrderLineItem.getCanceledQuantity());
    }

    public String getPreferredUnitOfMeasure() {
        return fulfillmentOrderLineItem.getPreferredUom();
    }

    public void setPreferredUomConversionFactor(BigDecimal preferredUomConversionFactor) {
        this.preferredUomConversionFactor = preferredUomConversionFactor;
    }

    public BigDecimal getPreferredUomConversionFactor() {
        return preferredUomConversionFactor;
    }

    private FulfillmentOrderReversePickLineItem getReversePickLineItemForOrderLineItem(FulfillmentOrderLineItem orderLineItem) {
        for (FulfillmentOrderReversePickLineItem lineItem : reversePick.getLineItems()) {
            if (lineItem.getFulfillmentOrderLineItemId().equals(orderLineItem.getId())) {
                return lineItem;
            }
        }
        return null;

    }

    public void setCaseSize(Quantity caseSize) throws BusinessException {
        if (reversePickLineItem != null && isCasesMode()) {
            reversePickLineItem.setCaseSize(caseSize);
        }
    }

    public Quantity getCaseSize() {
        if (reversePickLineItem != null && isCasesMode()) {
            return reversePickLineItem.getCaseSize();
        }
        return Quantity.ONE;
    }

    /* This method ignores the current mode. */
    public void setQuantity(Quantity quantity) throws Exception {
        if (reversePickLineItem != null && quantity != null) {
            if (quantity.isNegative()) {
                throw new BusinessException(CommonMessageText.QUANTITY_INVALID_NEGATIVE);
            }
            doSetQuantity(quantity);
        }
    }

    /* This method should be called by table only! */
    public void setQuantityBasedOnUom(Quantity quantity) throws Exception {
        try {
            setQuantityModifiedByUom(quantity);
        } catch (BusinessException exception) {
            UIStatusUtility.displayWarning(this, exception.getPrimaryMessageText());
            if (!reversePick.isSetDefaultQuantities()) {
                throw new SimTableResetFocusException();
            }
            reversePick.setSetDefaultQuantities(false);
        }
    }

    public void setQuantityModifiedByUom(Quantity quantity) throws Exception {
        if (reversePickLineItem != null && quantity != null) {
            if (quantity.isNegative()) {
                throw new BusinessException(CommonMessageText.QUANTITY_INVALID_NEGATIVE);
            }
            if (isCasesMode()) {
                quantity = quantity.multiply(getCaseSize());
            }
            if (isPreferredMode() && isPreferredUomConversionAvailable()) {
                quantity = quantity.divide(getPreferredUomConversionFactor());
            }
            doSetQuantity(quantity);
        }
    }

    private void doSetQuantity(Quantity quantity) throws Exception {
        Quantity oldQuantity = reversePickLineItem.getQuantity();
        FulfillmentOrderLineItem ordLineItem = orderLineItemMap.get(reversePickLineItem.getFulfillmentOrderLineItemId());
        Quantity physicalPickedQuantity = ordLineItem.getPickedQuantity().subtract(ordLineItem.getDeliveredQuantity());
        Quantity remainingQty = fulfillmentOrder.getRemainingQty(ordLineItem.getId());

        //If the reverse pick was created by OMS cancellation
        if (reversePick.isExternallyInitiated()) {
            //If PPQ > Remaining, let them reverse pick up to PPQ
            if (physicalPickedQuantity.subtract(remainingQty).isPositive()) {
                if (quantity.subtract(physicalPickedQuantity).isPositive()) {
                    reversePickLineItem.doSetQuantity(oldQuantity);
                    if (quantity.subtract(physicalPickedQuantity).isPositive()) {
                        throw new BusinessException(FulfillmentOrderMessageText.ACTUAL_REVERSE_PICK_CANNOT_EXCEED_PHYSICALLY_PICKED);
                    }
                }
            } else {
                if (quantity.subtract(remainingQty).isPositive()) {
                    reversePickLineItem.doSetQuantity(oldQuantity);
                    throw new BusinessException(FulfillmentOrderMessageText.ACTUAL_REVERSE_PICK_CANNOT_EXCEED_REMAINING);
                } else {
                    reversePickLineItem.setQuantity(quantity);
                    //Only validate suggested in the case of EA UOM
                    if (ordLineItem.getStockItem().isEachesUom() && isTotalRevPickQuantityMoreThanSuggested(reversePickLineItem)) {
                        reversePickLineItem.doSetQuantity(oldQuantity);
                        throw new BusinessException(FulfillmentOrderMessageText.ACTUAL_REVERSE_PICK_CANNOT_EXCEED_SUGGESTED);
                    }
                }
            }

        } else {
            //Otherwise if this reverse pick was manually created
            if (quantity.subtract(physicalPickedQuantity).isPositive()) {
                throw new BusinessException(FulfillmentOrderMessageText.ACTUAL_REVERSE_PICK_CANNOT_EXCEED_PHYSICALLY_PICKED);
            }
        }
        reversePickLineItem.setQuantity(quantity);
    }

    private boolean isTotalRevPickQuantityMoreThanSuggested(FulfillmentOrderReversePickLineItem revPickLineItem) {
        FulfillmentOrderLineItem orderLineItem = orderLineItemMap.get(revPickLineItem.getFulfillmentOrderLineItemId());
        FulfillmentOrderLineItem originalOrderLineItem = null;
        originalOrderLineItem = orderLineItem.isSubstitute() ? orderLineItemMap.get(orderLineItem.getSubstituteLineItemId()) : orderLineItem;
        FulfillmentOrderReversePickLineItem originalRevPickLineItem = getReversePickLineItemForOrderLineItem(originalOrderLineItem);
        Quantity suggestedQty = originalRevPickLineItem.getSuggestedQty();
        Quantity revPickQty = Quantity.ZERO;
        for (FulfillmentOrderLineItem ordLineItem : fulfillmentOrder.getLineItems()) {
            if (originalOrderLineItem.getId().equals(ordLineItem.getId())
                    || ((ordLineItem.getSubstituteLineItemId() != null) && (ordLineItem.getSubstituteLineItemId().equals(originalOrderLineItem.getId())))) {
                revPickQty = revPickQty.add(getReversePickLineItemForOrderLineItem(ordLineItem).getQuantityOrZero());
            }
        }
        return revPickQty.subtract(suggestedQty).isPositive();
    }

    public Quantity getQuantity() {
        return reversePickLineItem != null ? reversePickLineItem.getQuantity() : null;
    }

    public Quantity getQuantityOrZero() {
        return reversePickLineItem != null ? reversePickLineItem.getQuantityOrZero() : Quantity.ZERO;
    }

    public Quantity getQuantityBasedOnUom() {
        return rationalizeQuantityBasedOnUom(getQuantity());
    }

    /**
     * Returns the Item Id of the FulfillmentOrderLineItem that the FulfillmentOrderLineItem
     * associated to the current reverse pick line item was substituted for.
     * @return The Item Id of the FulfillmentOrderLineItem that the FulfillmentOrderLineItem
     * associated to the current reverse pick line item was substituted for.
     */
    public String getSubstituteId() {
        Long substituteId = fulfillmentOrderLineItem.getSubstituteLineItemId();
        for (FulfillmentOrderLineItem tmpLineItem : fulfillmentOrder.getLineItems()) {
            if (tmpLineItem.getId().equals(substituteId)) {
                return tmpLineItem.getItemId();
            }
        }
        return null;
    }

    public boolean isPropertyModifiable(String property) {
        if (FulfillmentOrderPickProperty.UOM_MODE.equals(property)) {
            return true;
        }
        if (FulfillmentOrderPickProperty.CASE_SIZE.equals(property)) {
            return true;
        }
        if (FulfillmentOrderPickProperty.QUANTITY.equals(property)) {
            return true;
        }
        return false;
    }
}
