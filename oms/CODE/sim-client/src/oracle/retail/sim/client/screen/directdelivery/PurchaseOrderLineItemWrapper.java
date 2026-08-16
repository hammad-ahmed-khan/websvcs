package oracle.retail.sim.client.screen.directdelivery;

import java.math.BigDecimal;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.directdelivery.PurchaseOrderLineItem;
import oracle.retail.sim.common.directdelivery.PurchaseOrderProperty;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.item.SupplierItem;
import oracle.retail.sim.common.lineitem.StockLineItemWrapper;
import oracle.retail.sim.common.lineitem.UOMMode;

/********************************************************************************************************
 * Purchase Order Line Item Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class PurchaseOrderLineItemWrapper extends StockLineItemWrapper {
    private PurchaseOrderLineItem purchaseOrderLineItem;
    private StockItem stockItem;
    private BigDecimal preferredUomConversionFactor;

    public PurchaseOrderLineItemWrapper(PurchaseOrderLineItem purchaseOrderLineItem) {
        this.purchaseOrderLineItem = purchaseOrderLineItem;
    }

    public PurchaseOrderLineItem getLineItem() {
        return purchaseOrderLineItem;
    }

    public StockItem getStockItem() {
        return stockItem;
    }

    public void setStockItem(StockItem stockItem) {
        if (this.stockItem != null) {
            throw new IllegalArgumentException("Value is immutable");
        }
        this.stockItem = stockItem;
    }

    public SupplierItem getSupplierItem() {
        return purchaseOrderLineItem != null ? purchaseOrderLineItem.getSupplierItem() : null;
    }

    public String getShortDescription() {
        return stockItem != null ? stockItem.getShortDescription() : null;
    }

    public String getLongDescription() {
        return stockItem != null ? stockItem.getLongDescription() : null;
    }

    public boolean isExpected() {
        return purchaseOrderLineItem != null ? purchaseOrderLineItem.isExpected() : false;
    }

    public Quantity getQuantityExpectedOrZero() {
        return purchaseOrderLineItem != null ? purchaseOrderLineItem.getQuantityExpected() : Quantity.ZERO;
    }

    public Quantity getQuantityExpectedBasedOnUOM() {
        return purchaseOrderLineItem != null ? rationalizeQuantityBasedOnUom(purchaseOrderLineItem.getQuantityExpected()) : null;
    }

    public Quantity getQuantityReceivedOrZero() {
        return purchaseOrderLineItem != null ? purchaseOrderLineItem.getQuantityReceived() : Quantity.ZERO;
    }

    public Quantity getQuantityReceivedBasedOnUOM() {
        return purchaseOrderLineItem != null ? rationalizeQuantityBasedOnUom(purchaseOrderLineItem.getQuantityReceived()) : null;
    }

    public Quantity getQuantityOrderedBasedOnUOM() {
        return purchaseOrderLineItem != null ? rationalizeQuantityBasedOnUom(purchaseOrderLineItem.getQuantityOrdered()) : null;
    }

    public Quantity getQuantityOverageOrZero() {
        if (purchaseOrderLineItem == null) {
            return Quantity.ZERO;
        }
        return purchaseOrderLineItem.getQuantityReceived().subtract(purchaseOrderLineItem.getQuantityExpected());
    }

    public UOMMode getUnitOfMeasureMode() {
        return purchaseOrderLineItem != null ? super.getUnitOfMeasureMode() : getDefaultUomMode();
    }

    public Quantity getCaseSize() {
        if (purchaseOrderLineItem == null || !isCasesMode()) {
            return Quantity.ONE;
        }
        return purchaseOrderLineItem.getCaseSize();
    }

    public SimMoney getUnitCost() {
        return purchaseOrderLineItem != null ? purchaseOrderLineItem.getUnitCost() : null;
    }

    public String getPreferredUnitOfMeasure() {
        return purchaseOrderLineItem != null ? purchaseOrderLineItem.getPreferredUom() : null;
    }

    public BigDecimal getPreferredUomConversionFactor() {
        return preferredUomConversionFactor;
    }

    public void setPreferredUomConversionFactor(BigDecimal preferredUomConversionFactor) {
        if (this.preferredUomConversionFactor != null) {
            throw new IllegalArgumentException("Value is immutable");
        }
        this.preferredUomConversionFactor = preferredUomConversionFactor;
    }

    public boolean isPropertyModifiable(String name) throws Exception {
        if (purchaseOrderLineItem == null) {
            return false;
        }
        if (PurchaseOrderProperty.UOM_MODE.equals(name)) {
            return purchaseOrderLineItem.getCaseSize().isPositive() || isPreferredUomConversionAvailable();
        }
        return false;
    }
}
