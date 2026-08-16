package oracle.retail.sim.client.screen.fulfillmentorderpick;

import java.math.BigDecimal;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.table.SimTableResetFocusException;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderLineItem;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderBin;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPick;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickLineItem;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickProperty;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickStatus;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.lineitem.StockLineItemWrapper;
import oracle.retail.sim.common.rules.core.CaseSizeAndQtyValidForUomRule;
import oracle.retail.sim.common.tolerance.ToleranceAdmin;

/********************************************************************************************************
 * Fulfillment Order Pick Line Item Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderPickLineItemWrapper extends StockLineItemWrapper {
    private FulfillmentOrderPick pick;
    private FulfillmentOrderPickLineItem lineItem;
    private FulfillmentOrder fulfillmentOrder;
    private String primaryLocation;
    private ToleranceAdmin toleranceAdmin;
    private BigDecimal preferredUomConversionFactor;

    public FulfillmentOrderPickLineItemWrapper(FulfillmentOrderPick pick, FulfillmentOrderPickLineItem lineItem, FulfillmentOrder fulfillmentOrder, ToleranceAdmin toleranceAdmin,
            BigDecimal preferredUomConversionFactor) {
        this.pick = pick;
        this.lineItem = lineItem;
        this.fulfillmentOrder = fulfillmentOrder;
        this.toleranceAdmin = toleranceAdmin;
        this.preferredUomConversionFactor = preferredUomConversionFactor;
    }

    /**
     * Returns the FulfillmentOrderPickLineItem for this wrapper.
     * @return The FulfillmentOrderPickLineItem for this wrapper.
     */
    public FulfillmentOrderPickLineItem getLineItem() {
        return lineItem;
    }

    /**
     * Returns the Stock Item on this wrapper.
     * @return The Stock Item on this wrapper.
     */
    public StockItem getStockItem() {
        return lineItem.getStockItem();
    }

    /**
     * Returns the Item Id of the Pick Line Item for the wrapper.
     * @return The Item Id of the Pick Line Item for the wrapper.
     */
    public String getItemId() {
        return lineItem.getStockItem().getId();
    }

    /**
     * Returns the item description for the Pick Line Item for this wrapper.
     * @return The item description for the Pick Line Item for this wrapper.
     */
    public String getItemDescription() {
        return lineItem.getStockItem().getItemDescription();
    }

    /**
     * Sets the primary location at which to pick this line item.
     * @param primaryLocation The primary location at which to pick this line item.
     */
    public void setPrimaryLocation(String primaryLocation) {
        this.primaryLocation = primaryLocation;
    }

    /**
     * Returns the primary location at which to pick this line item.
     * @return Returns the primary location of where to pick this line item.
     */
    public String getPrimaryLocation() {
        return primaryLocation;
    }

    /**
     * Returns the identifier of the Fulfillment Order that the pick on this wrapper is for.
     * @return The identifier of the Fulfillment Order that the pick on this wrapper is for.
     */
    public Long getSimCustomerOrderId() {
        return fulfillmentOrder.getId();
    }

    /**
     * Returns the user or system defined Bin Id for the FulfillmentOrderBin of this wrapper.
     * @return The user or system defined Bin Id for the FulfillmentOrderBin of this wrapper.
     */
    public String getBinId() {
        FulfillmentOrderBin bin = lineItem.getBin();
        return bin != null ? bin.getBinId() : null;
    }

    /**
     * Returns the external id of the FulfillmentOrder of this wrapper.
     * @return The external id of the FulfillmentOrder of this wrapper.
     */
    public String getFulfillmentId() {
        return fulfillmentOrder.getExternalId();
    }

    /**
     * Returns the FulfillmentOrderPick of this wrapper.
     * @return The FulfillmentOrderPick of this wrapper.
     */
    public FulfillmentOrderPick getPick() {
        return pick;
    }

    /**
     * Returns the ToleranceAdmin of this wrapper.
     * @return The ToleranceAdmin of this wrapper.
     */
    public ToleranceAdmin getToleranceAdmin() {
        return toleranceAdmin;
    }

    public Quantity getQuantityOrZero() {
        return lineItem.getQuantityOrZero();
    }

    public void setCaseSize(Quantity caseSize) throws BusinessException {
        if (lineItem != null && isCasesMode()) {
            CaseSizeAndQtyValidForUomRule.execute(this, caseSize, lineItem.getQuantityOrZero());
            lineItem.setCaseSize(caseSize);
        }
    }

    public Quantity getCaseSize() {
        if (lineItem != null && isCasesMode()) {
            return lineItem.getCaseSize();
        }
        return Quantity.ONE;
    }

    public Quantity getQuantity() {
        if (!getSubstitutes()) {
            return lineItem.getQuantity();
        }
        Quantity quantity = lineItem.getQuantity();
        for (FulfillmentOrderPickLineItem pickLineItem : pick.getLineItems()) {
            if (pickLineItem.isSubstitute() && pickLineItem.getSubstituteLineItemId().equals(lineItem.getId())) {
                quantity = quantity.add(pickLineItem.getQuantityOrZero());
            }
        }
        return quantity;
    }

    public Quantity getQuantityBasedOnUom() {
        return rationalizeQuantityBasedOnUom(getQuantity());
    }

    public Quantity getSuggestedQuantity() {
        return lineItem.getSuggestedQuantity();
    }

    public Quantity getSuggestedQuantityBasedOnUom() {
        return rationalizeQuantityBasedOnUom(lineItem.getSuggestedQuantity());
    }

    public Quantity getDiscrepancyQty() {
        Quantity remainingQuantity = fulfillmentOrder.getRemainingPickQty(lineItem.getFulfillmentOrderLineItemId());
        if (!lineItem.getSuggestedQuantity().subtract(remainingQuantity).isPositive()) {
            return null;
        }
        return remainingQuantity;
    }

    public Quantity getDiscrepancyQtyBasedOnUom() {
        Quantity remainingQuantity = fulfillmentOrder.getRemainingPickQty(lineItem.getFulfillmentOrderLineItemId());
        if (!lineItem.getSuggestedQuantity().subtract(remainingQuantity).isPositive()) {
            return null;
        }
        return rationalizeQuantityBasedOnUom(remainingQuantity);
    }

    public String getPreferredUnitOfMeasure() {
        return lineItem.getPreferredUom();
    }

    public BigDecimal getPreferredUomConversionFactor() {
        return preferredUomConversionFactor;
    }

    /* This method is standard access to setting quantity. Ignores current line item mode. */
    public void setQuantity(Quantity quantity) throws Exception {
        if (lineItem != null && quantity != null) {
            doSetQuantity(quantity);
        }
    }

    /* This method is standard access to setting quantity. It uses current line item mode to modified quantity. */
    public void setQuantityModifiedByUom(Quantity quantity) throws Exception {
        if (lineItem != null && quantity != null) {
            if (isCasesMode()) {
                quantity = quantity.multiply(getCaseSize());
            }
            if (isPreferredMode() && isPreferredUomConversionAvailable()) {
                quantity = quantity.divide(getPreferredUomConversionFactor());
            }
            doSetQuantity(quantity);
        }
    }

    private void doSetQuantity(Quantity quantity) throws BusinessException {
        //Gather this item and any substitutes it has and add their quantities to the input for the total
        Quantity totalQuantity = quantity;
        for (FulfillmentOrderPickLineItem pickLineItem : pick.getLineItems()) {
            if (pickLineItem.isSubstitute() && pickLineItem.getSubstituteLineItemId().equals(lineItem.getId())) {
                if (isCasesMode()) {
                    totalQuantity = totalQuantity.add(pickLineItem.getQuantityOrZero().multiply(pickLineItem.getCaseSize()));
                } else {
                    totalQuantity = totalQuantity.add(pickLineItem.getQuantityOrZero());
                }
            }
        }

        //Get the suggested quantity, if this line item is a substitute, get it from the original
        Quantity suggestedQuantity = lineItem.getSuggestedQuantity();
        if (lineItem.isSubstitute()) {
            for (FulfillmentOrderPickLineItem pickLineItem : pick.getLineItems()) {
                if (lineItem.getSubstituteLineItemId().equals(pickLineItem.getId())) {
                    suggestedQuantity = pickLineItem.getSuggestedQuantity();
                    break;
                }
            }
        }

        //If the Item is an Each and the user enters more than the suggested quantity
        if (lineItem.getStockItem().isEachesUom()) {
            if (totalQuantity.subtract(suggestedQuantity).isPositive()) {
                throw new BusinessException(FulfillmentOrderMessageText.ACTUAL_CANNOT_EXCEED_SUGGESTED);
            }
        } else {
            //Otherwise, check the tolerances for going over
            if (toleranceAdmin.getVarianceCount() != null) {
                if (totalQuantity.subtract(suggestedQuantity.add(toleranceAdmin.getVarianceCount())).isPositive()) {
                    throw new BusinessException(FulfillmentOrderMessageText.TOLERANCE_VALUES_EXCEEDED);
                }
            }
            if (toleranceAdmin.getVariancePercent() != null) {
                if (totalQuantity.subtract(suggestedQuantity.add(suggestedQuantity.multiply(toleranceAdmin.getVariancePercent().movePointLeft(2)))).isPositive()) {
                    throw new BusinessException(FulfillmentOrderMessageText.TOLERANCE_VALUES_EXCEEDED);
                }
            }
        }

        lineItem.setQuantity(quantity);
    }

    /* This method is called by table only! */
    public void setQuantityBasedOnUom(Quantity quantity) throws Exception {
        try {
            setQuantityModifiedByUom(quantity);
        } catch (BusinessException exception) {
            UIStatusUtility.displayWarning(this, exception.getPrimaryMessageText());
            throw new SimTableResetFocusException();
        }
    }

    public boolean isSubstituteAllowed() {
        //If this is one we added as a substitute
        if (lineItem.isSubstitute()) {
            return false;
        }
        for (FulfillmentOrderLineItem orderLineItem : fulfillmentOrder.getLineItems()) {
            if (orderLineItem.getId().equals(lineItem.getFulfillmentOrderLineItemId())) {
                return orderLineItem.isSubstituteAllowed();
            }
        }
        return false;
    }

    /**
     * Returns whether this line item has any items substituted for it.
     * @return True if this line item has any items substituted for it, otherwise false.
     */
    public boolean getSubstitutes() {
        //If this line item is a substitute, we cannot have a substitute for it
        if (lineItem.isSubstitute()) {
            return false;
        }
        for (FulfillmentOrderPickLineItem pickLineItem : pick.getLineItems()) {
            if (pickLineItem.isSubstitute() && lineItem.getId().equals(pickLineItem.getSubstituteLineItemId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isPropertyModifiable(String property) {
        if (pick.getStatus() == FulfillmentOrderPickStatus.CANCELED || pick.getStatus() == FulfillmentOrderPickStatus.COMPLETED) {
            return false;
        }
        if (FulfillmentOrderPickProperty.UOM_MODE.equals(property)) {
            return true;
        }
        if (FulfillmentOrderPickProperty.CASE_SIZE.equals(property)) {
            return lineItem != null && (pick.getStatus() == FulfillmentOrderPickStatus.IN_PROGRESS || pick.getStatus() == FulfillmentOrderPickStatus.NEW) && isCasesMode()
                    && !SimConfigManager.getBoolean(SimConfigManager.DISABLE_CASE_SIZE);
        }
        if (FulfillmentOrderPickProperty.QUANTITY.equals(property)) {
            return !getSubstitutes();
        }
        return false;
    }
}
