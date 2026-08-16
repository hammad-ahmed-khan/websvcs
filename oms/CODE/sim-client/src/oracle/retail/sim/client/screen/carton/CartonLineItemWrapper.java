package oracle.retail.sim.client.screen.carton;

import java.math.BigDecimal;
import java.util.List;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.lineitem.SerialNumberLineItemWrapper;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryLineItem;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryProperty;

/********************************************************************************************************
 * Carton Line Item Wrapper For The Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CartonLineItemWrapper extends SerialNumberLineItemWrapper {
    private WarehouseDeliveryLineItem lineItem;

    public CartonLineItemWrapper(WarehouseDeliveryLineItem lineItem) {
        this.lineItem = lineItem;
    }

    public WarehouseDeliveryLineItem getLineItem() {
        return lineItem;
    }

    public StockItem getStockItem() {
        return lineItem.getStockItem();
    }

    public String getItemId() {
        return lineItem.getStockItem().getId();
    }

    public Quantity getCaseSize() {
        return isCasesMode() ? lineItem.getCaseSize() : Quantity.ONE;
    }

    public Quantity getQuantityExpectedBasedOnUom() {
        return rationalizeQuantityBasedOnUom(lineItem.getQuantityExpected());
    }

    public Quantity getQuantityReceivedBasedOnUom() {
        return rationalizeQuantityBasedOnUom(lineItem.getQuantityReceived());
    }

    public Quantity getQuantityDamagedBasedOnUom() {
        return rationalizeQuantityBasedOnUom(lineItem.getQuantityDamaged());
    }

    public Quantity getQuantityVarianceBasedOnUom() {
        Quantity received = lineItem.getQuantityReceivedOrZero();
        Quantity damaged = lineItem.getQuantityDamagedOrZero();
        if (!received.isPositive() && !damaged.isPositive()) {
            return null;
        }
        return received.add(damaged).subtract(lineItem.getQuantityExpectedOrZero());
    }

    public List<SerialNumberValue> getSerialNumbers() {
        return lineItem.getSerialNumbers();
    }

    public Integer getSerialNumberCount() {
        return lineItem.getSerialNumbers().size();
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

    public boolean isPropertyModifiable(String name) throws Exception {
        if (WarehouseDeliveryProperty.SERIAL_NUMBER_COUNT.equals(name)) {
            return isSerialNumberRequired();
        }
        if (WarehouseDeliveryProperty.UOM_MODE.equals(name)) {
            return lineItem.getCaseSize().isPositive() || isPreferredUomConversionAvailable();
        }
        return false;
    }
}
