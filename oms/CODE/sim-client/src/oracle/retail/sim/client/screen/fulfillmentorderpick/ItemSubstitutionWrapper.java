package oracle.retail.sim.client.screen.fulfillmentorderpick;

import java.math.BigDecimal;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.table.SimTableResetFocusException;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickLineItem;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickProperty;
import oracle.retail.sim.common.item.RelatedItem;
import oracle.retail.sim.common.lineitem.UnitOfMeasureWrapper;

/********************************************************************************************************
 * Item Substitution Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class ItemSubstitutionWrapper extends UnitOfMeasureWrapper {
    private FulfillmentOrderPickLineItem originalItem;
    private RelatedItem relatedItem;
    private Quantity caseSize;
    private Quantity actualQuantity = Quantity.ZERO;
    private BigDecimal preferredUomConversionFactor;

    public ItemSubstitutionWrapper(FulfillmentOrderPickLineItem originalItem, RelatedItem relatedItem, BigDecimal preferredUomConversionFactor) {
        this.originalItem = originalItem;
        this.relatedItem = relatedItem;
        this.preferredUomConversionFactor = preferredUomConversionFactor;
        if (originalItem.getCaseSize() != null) {
            caseSize = originalItem.getCaseSize();
        } else {
            caseSize = relatedItem.getCaseSize();
        }
    }

    public RelatedItem getRelatedItem() {
        return relatedItem;
    }

    public Quantity getCaseSize() {
        if (isCasesMode()) {
            return caseSize != null ? caseSize : relatedItem.getCaseSize();
        }
        return Quantity.ONE;
    }

    public void setCaseSize(Quantity caseSize) {
        this.caseSize = caseSize;
    }

    public Quantity getActualQuantity() {
        return actualQuantity;
    }

    public Quantity getActualQuantityBasedOnUom() {
        return rationalizeQuantityBasedOnUom(actualQuantity);
    }

    public void setActualQuantity(Quantity actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    /* This method is called by table only! */
    public void setActualQuantityBasedOnUom(Quantity quantity) throws Exception {
        try {
            setActualQuantityModifiedByUom(quantity);
        } catch (BusinessException exception) {
            UIStatusUtility.displayWarning(this, exception.getPrimaryMessageText());
            throw new SimTableResetFocusException();
        }
    }

    /* This method is standard access to setting quantity */
    public void setActualQuantityModifiedByUom(Quantity quantity) throws Exception {
        if (isCasesMode()) {
            quantity = quantity.multiply(getCaseSize());
        }
        if (isPreferredMode() && isPreferredUomConversionAvailable()) {
            quantity = quantity.divide(getPreferredUomConversionFactor());
        }
        actualQuantity = quantity;
    }

    public Quantity getAvailableStockOnHand() {
        return relatedItem.getAvailableStockOnHand();
    }

    public Quantity getAvailableStockOnHandBasedOnUom() {
        return rationalizeQuantityBasedOnUom(relatedItem.getAvailableStockOnHand());
    }

    public String getItemId() {
        return relatedItem.getId();
    }

    public String getItemDescription() {
        return relatedItem.getDescription();
    }

    public String getDiff1() {
        return relatedItem.getDiff1Description();
    }

    public String getDiff2() {
        return relatedItem.getDiff2Description();
    }

    public String getDiff3() {
        return relatedItem.getDiff3Description();
    }

    public String getDiff4() {
        return relatedItem.getDiff4Description();
    }

    public String getStandardUnitOfMeasure() {
        return relatedItem.getUnitOfMeasure();
    }

    public String getPreferredUnitOfMeasure() {
        return originalItem.getPreferredUom();
    }

    public BigDecimal getPreferredUomConversionFactor() {
        return preferredUomConversionFactor;
    }

    public boolean isPropertyModifiable(String property) {
        if (FulfillmentOrderPickProperty.ACTUAL_QTY_UOM.equals(property)) {
            return true;
        }
        if (FulfillmentOrderPickProperty.UOM_MODE.equals(property)) {
            return true;
        }
        if (FulfillmentOrderPickProperty.CASE_SIZE.equals(property)) {
            return relatedItem != null && isCasesMode() && !SimConfigManager.getBoolean(SimConfigManager.DISABLE_CASE_SIZE);
        }
        return false;
    }
}
