package oracle.retail.sim.client.screen.shelfreplenishment;

import java.math.BigDecimal;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.lineitem.SerialNumberLineItemWrapper;
import oracle.retail.sim.common.rules.core.CaseSizeAndQtyValidForUomRule;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishment;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentFromArea;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentLineItem;

/********************************************************************************************************
 * Shelf Replenishment List Line Item Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ShelfReplenishmentLineItemWrapper extends SerialNumberLineItemWrapper {

    private ShelfReplenishment shelfReplenishment;
    private ShelfReplenishmentLineItem lineItem;

    public ShelfReplenishmentLineItemWrapper(ShelfReplenishment shelfReplenishment, ShelfReplenishmentLineItem lineItem) {
        this.shelfReplenishment = shelfReplenishment;
        this.lineItem = lineItem;
    }

    public StockItem getStockItem() {
        return lineItem.getStockItem();
    }

    public String getItemDescription() {
        return lineItem.getStockItem().getItemDescription();
    }

    public Quantity getCaseSize() {
        return isCasesMode() ? lineItem.getCaseSize() : Quantity.ONE;
    }

    public void setCaseSize(Quantity caseSize) throws BusinessException {
        checkForNullParameter("Case Size", caseSize);
        CaseSizeAndQtyValidForUomRule.execute(this, caseSize, getActualQuantity());
        executeRule("setCaseSize", caseSize);
        lineItem.setCaseSize(caseSize);
    }

    public ShelfReplenishmentFromArea getFromArea() {
        return lineItem.getFromArea();
    }

    public void setFromArea(ShelfReplenishmentFromArea fromArea) throws BusinessException {
        lineItem.setFromArea(fromArea);
    }

    public Quantity getRequestedQuantityBasedOnUom() {
        return rationalizeQuantityBasedOnUom(lineItem.getRequestedQuantity());
    }

    public void setRequestedQuantityBasedOnUom(Quantity quantity) throws BusinessException {
        checkForNullParameter("Requested Quantity", quantity);
        executeRule("setRequestedQuantityBasedOnUom", quantity);
        lineItem.setActualQuantity(quantity.multiply(getCaseSize()));
    }

    public Quantity getActualQuantity() {
        return lineItem.getActualQuantity();
    }
    
    public Quantity getActualQuantityOrZero() {
        return lineItem.getActualQuantity() != null ? lineItem.getActualQuantity() : Quantity.ZERO;
    }

    public void setActualQuantity(Quantity quantity) throws BusinessException {
        lineItem.setActualQuantity(quantity);
    }

    public Quantity getActualQuantityBasedOnUom() {
        return rationalizeQuantityBasedOnUom(lineItem.getActualQuantity());
    }

    public void setActualQuantityBasedOnUom(Quantity quantity) throws BusinessException {
        checkForNullParameter("Actual Quantity", quantity);
        executeRule("setActualQuantityBasedOnUom", quantity);
        lineItem.setActualQuantity(quantity.multiply(getCaseSize()));
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

    public boolean isPropertyModifiable(String propertyName) throws Exception {
        if (lineItem != null) {
            return lineItem.isPropertyModifiable(propertyName, shelfReplenishment.getStatus(), shelfReplenishment.getStatusDate());
        }
        return false;
    }
}
