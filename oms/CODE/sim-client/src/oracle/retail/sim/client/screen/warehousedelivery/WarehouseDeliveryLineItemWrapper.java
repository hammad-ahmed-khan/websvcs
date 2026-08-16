package oracle.retail.sim.client.screen.warehousedelivery;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.item.ItemStatus;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.lineitem.SerialNumberLineItemWrapper;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.rules.core.CaseSizeAndQtyValidForUomRule;
import oracle.retail.sim.common.rules.core.QuantityCannotBeNegativeRule;
import oracle.retail.sim.common.rules.core.QuantityMustConformToUOMRule;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCarton;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryLineItem;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryProperty;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliverySimpleLineItem;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliverySimpleLineItemComparator;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryStatus;

/********************************************************************************************************
 * Warehouse Delivery Line Item Wrapper for Warehouse Delivery GUI
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class WarehouseDeliveryLineItemWrapper extends SerialNumberLineItemWrapper {
    private WarehouseDeliveryCarton carton;
    private WarehouseDeliveryLineItem lineItem;
    private String preferredUom;
    private BigDecimal preferredUomConversionFactor;

    public WarehouseDeliveryLineItemWrapper(WarehouseDeliveryCarton carton) {
        this.carton = carton;
    }

    public WarehouseDeliveryCarton getCarton() {
        return carton;
    }

    public WarehouseDeliveryLineItem getLineItem() {
        return lineItem;
    }

    public void setLineItem(WarehouseDeliveryLineItem lineItem) {
        //Used to construct existing line item
        if (this.lineItem != null) {
            throw new IllegalArgumentException("Value is immutable");
        }
        this.lineItem = lineItem;
    }

    public boolean isNew() {
        return lineItem == null;
    }

    public boolean isDeleteAllowed() {
        //Empty line item
        if (lineItem == null) {
            return true;
        }
        //Expected line item
        if (lineItem.isExpected()) {
            return false;
        }
        //New unsaved unexpected line item
        if (lineItem.isNew()) {
            return true;
        }
        //Saved unexpected line item being adjusted after confirmation
        if (carton.getDelivery().isAllowAdjustment()) {
            return false;
        }
        //Saved unexpected line item not been confirmed
        return true;
    }

    public StockItem getStockItem() {
        return lineItem != null ? lineItem.getStockItem() : null;
    }

    public void setStockItem(StockItem stockItem) throws Exception {
        if (stockItem == null) {
            return;
        }
        if (lineItem != null && (stockItem.equals(lineItem.getStockItem()) || !isDeleteAllowed())) {
            return;
        }
        ItemStatus status = stockItem.getStatus();
        if (status == ItemStatus.DELETED || status == ItemStatus.INACTIVE || status == ItemStatus.DISCONTINUED) {
            if (!RConfirmUtility.confirm("Warehouse Delivery", CommonMessageText.NON_ACTIVE_ITEM_CONFIRM, status.toString())) {
                return;
            }
        }
        if (lineItem != null) {
            removeLineItem();
        }
        try {
            //Create new delivery line item
            lineItem = createLineItem(stockItem);
            carton.addLineItem(lineItem);
        } catch (Exception e) {
            lineItem = null;
            throw e;
        }
    }

    private WarehouseDeliveryLineItem createLineItem(StockItem stockItem) throws BusinessException {
        WarehouseDeliverySimpleLineItem newLineItem = BOFactory.createWarehouseDeliverySimpleLineItem(stockItem);
        newLineItem.setQuantityExpected(Quantity.ZERO);
        //Add line item to most recent distribution associated with this carton
        List<WarehouseDeliverySimpleLineItem> simpleLineItems = carton.getSimpleLineItems();
        if (!simpleLineItems.isEmpty()) {
            if (simpleLineItems.size() > 1) {
                Collections.sort(simpleLineItems, new WarehouseDeliverySimpleLineItemComparator());
            }
            WarehouseDeliverySimpleLineItem simpleLineItem = simpleLineItems.get(simpleLineItems.size() - 1);
            newLineItem.doSetReceiptDocumentId(simpleLineItem.getReceiptDocumentId());
            newLineItem.doSetReceiptParentDocumentId(simpleLineItem.getReceiptParentDocumentId());
            newLineItem.doSetReceiptDocumentType(simpleLineItem.getReceiptDocumentType());
        }
        return newLineItem;
    }

    public void removeLineItem() throws BusinessException {
        if (lineItem != null) {
            carton.removeLineItem(lineItem);
            lineItem = null;
        }
    }

    public boolean isExpected() {
        return lineItem != null && lineItem.isExpected();
    }

    public Quantity getQuantityExpectedOrZero() {
        return lineItem != null ? lineItem.getQuantityExpectedOrZero() : Quantity.ZERO;
    }

    public Quantity getQuantityExpectedBasedOnUom() {
        return lineItem != null ? rationalizeQuantityBasedOnUom(lineItem.getQuantityExpected()) : null;
    }

    public Quantity getQuantityReceivedOrZero() {
        return lineItem != null ? lineItem.getQuantityReceivedOrZero() : Quantity.ZERO;
    }

    public Quantity getQuantityReceivedBasedOnUom() {
        return lineItem != null ? rationalizeQuantityBasedOnUom(lineItem.getQuantityReceived()) : null;
    }

    public void setQuantityReceived(Quantity quantity) throws BusinessException {
        if (lineItem != null) {
            checkForNullParameter("Quantity Received", quantity);
            QuantityCannotBeNegativeRule.execute(quantity);
            QuantityMustConformToUOMRule.execute(this, quantity);
            executeRule("setQuantityReceived", quantity);
            doSetQuantityReceived(quantity);
        }
    }

    public void setQuantityReceivedBasedOnUom(Quantity quantity) throws BusinessException {
        if (lineItem != null) {
            checkForNullParameter("Quantity Received Based On UOM", quantity);
            QuantityCannotBeNegativeRule.execute(quantity);
            QuantityMustConformToUOMRule.execute(this, quantity);
            executeRule("setQuantityReceivedBasedOnUom", quantity);
            doSetQuantityReceived(quantity.multiply(getCaseSize()));
        }
    }

    private void doSetQuantityReceived(Quantity quantity) throws BusinessException {
        if (isAGSNRequired()) {
            //Check AGSN quantity limit
            if (quantity.compareTo(SimConfigManager.MAX_AGSN_QTY) > 0) {
                throw new BusinessException(CommonMessageText.NUMBER_NOT_1_TO_999);
            }
            if (carton.getDelivery().isAllowAdjustment()) {
                //Check if new quantity requires user to remove existing UINs
                int received = 0;
                for (SerialNumberValue serialNumber : lineItem.getSerialNumbers()) {
                    if (!serialNumber.isDamaged()) {
                        received++;
                    }
                }
                if (new Quantity(received).compareTo(quantity) > 0) {
                    String[] values = new String[2];
                    values[0] = lineItem.getStockItem().getUINType().toString();
                    values[1] = getItemIdDescription();
                    throw new BusinessException(CommonMessageText.UIN_RECEIVE_QUANTITY_MISMATCH, values);
                }
            }
        }
        lineItem.setQuantityReceived(quantity);
    }

    public Quantity getQuantityDamagedOrZero() {
        return lineItem != null ? lineItem.getQuantityDamagedOrZero() : Quantity.ZERO;
    }

    public Quantity getQuantityDamagedBasedOnUom() {
        return lineItem != null ? rationalizeQuantityBasedOnUom(lineItem.getQuantityDamaged()) : null;
    }

    public void setQuantityDamaged(Quantity quantity) throws BusinessException {
        if (lineItem != null) {
            checkForNullParameter("Quantity Damaged", quantity);
            QuantityCannotBeNegativeRule.execute(quantity);
            QuantityMustConformToUOMRule.execute(this, quantity);
            executeRule("setQuantityDamaged", quantity);
            doSetQuantityDamaged(quantity);
        }
    }

    public void setQuantityDamagedBasedOnUom(Quantity quantity) throws BusinessException {
        if (lineItem != null) {
            checkForNullParameter("Quantity Damaged Based On UOM", quantity);
            QuantityCannotBeNegativeRule.execute(quantity);
            QuantityMustConformToUOMRule.execute(this, quantity);
            executeRule("setQuantityDamagedBasedOnUom", quantity);
            doSetQuantityDamaged(quantity.multiply(getCaseSize()));
        }
    }

    private void doSetQuantityDamaged(Quantity quantity) throws BusinessException {
        if (isAGSNRequired()) {
            //Check AGSN quantity limit
            if (quantity.compareTo(SimConfigManager.MAX_AGSN_QTY) > 0) {
                throw new BusinessException(CommonMessageText.NUMBER_NOT_1_TO_999);
            }
            //Check if new quantity requires user to remove existing confirmed UINs
            if (carton.getDelivery().isAllowAdjustment()) {
                int damaged = 0;
                for (SerialNumberValue serialNumber : lineItem.getSerialNumbers()) {
                    if (serialNumber.isDamaged()) {
                        damaged++;
                    }
                }
                if (new Quantity(damaged).compareTo(quantity) > 0) {
                    String[] values = new String[2];
                    values[0] = lineItem.getStockItem().getUINType().toString();
                    values[1] = getItemIdDescription();
                    throw new BusinessException(CommonMessageText.UIN_RECEIVE_QUANTITY_MISMATCH, values);
                }
            }
        }
        lineItem.setQuantityDamaged(quantity);
    }

    public UOMMode getUnitOfMeasureMode() {
        return lineItem != null ? super.getUnitOfMeasureMode() : getDefaultUomMode();
    }

    public Quantity getCaseSize() {
        if (lineItem == null || !isCasesMode()) {
            return Quantity.ONE;
        }
        return lineItem.getCaseSize();
    }

    public void setCaseSize(Quantity caseSize) throws BusinessException {
        if (lineItem != null && isCasesMode()) {
            CaseSizeAndQtyValidForUomRule.execute(this, caseSize, getQuantityExpectedOrZero());
            CaseSizeAndQtyValidForUomRule.execute(this, caseSize, getQuantityReceivedOrZero());
            CaseSizeAndQtyValidForUomRule.execute(this, caseSize, getQuantityDamagedOrZero());
            lineItem.setCaseSize(caseSize);
        }
    }

    public SimMoney getUnitCost() {
        return lineItem != null ? lineItem.getUnitCost() : null;
    }

    public void addSerialNumber(SerialNumberValue value) throws BusinessException {
        if (lineItem != null) {
            lineItem.addSerialNumber(value);
        }
    }

    public void removeSerialNumber(SerialNumberValue value) throws BusinessException {
        if (lineItem != null) {
            lineItem.removeSerialNumber(value.getUin());
        }
    }

    public void setQuantitiesBasedOnSerialNumbers() throws BusinessException {
        if (lineItem != null) {
            lineItem.resetSerialNumberQuantities();
        }
    }

    public List<SerialNumberValue> getSerialNumbers() {
        if (lineItem == null) {
            return Collections.emptyList();
        }
        return lineItem.getSerialNumbers();
    }

    public Integer getSerialNumberCount() {
        return lineItem != null ? lineItem.getSerialNumbers().size() : 0;
    }

    private boolean isAGSNRequired() {
        if (lineItem == null) {
            return false;
        }
        StockItem stockItem = lineItem.getStockItem();
        return stockItem.isSerialNumberRequired() && stockItem.isAgsnEnabled() && !carton.getDelivery().isFinisherDelivery();
    }

    public boolean isMissingRequiredUins() {
        if (lineItem == null) {
            return false;
        }
        if (!isSerialNumberRequired() || isAGSNRequired()) {
            return false;
        }
        Quantity quantity = lineItem.getQuantityReceivedOrZero().add(lineItem.getQuantityDamagedOrZero());
        return quantity.intValue() != lineItem.getSerialNumbers().size();
    }

    public String getItemIdDescription() {
        return getItemIdDescription(SimConfigManager.isItemShortDescription());
    }

    public String getItemIdDescription(boolean shortDescription) {
        if (lineItem == null) {
            return null;
        }
        return lineItem.getStockItem().getId() + " - " + getStockItemDescription(shortDescription);
    }

    public String getPreferredUnitOfMeasure() {
        return preferredUom;
    }

    public void setPreferredUom(String preferredUom) {
        //Used to construct existing line item
        if (this.preferredUom != null) {
            throw new IllegalArgumentException("Value is immutable");
        }
        this.preferredUom = preferredUom;
    }

    public BigDecimal getPreferredUomConversionFactor() {
        return preferredUomConversionFactor;
    }

    public void setPreferredUomConversionFactor(BigDecimal preferredUomConversionFactor) {
        //Used to construct existing line item
        if (this.preferredUomConversionFactor != null) {
            throw new IllegalArgumentException("Value is immutable");
        }
        this.preferredUomConversionFactor = preferredUomConversionFactor;
    }

    public boolean isPropertyModifiable(String name) throws Exception {
        if (WarehouseDeliveryProperty.SERIAL_NUMBER_COUNT.equals(name)) {
            if (isNew() || !isSerialNumberRequired()) {
                return false;
            }
            if (carton.getDelivery().isAllowAdjustment() || WarehouseDeliveryStatus.getClosedSet().contains(carton.getDelivery().getStatus())) {
                //enable so you can see which uins were created
                return true;
            }
            return !isAGSNRequired();
        }
        if (WarehouseDeliveryProperty.UOM_MODE.equals(name)) {
            return !isNew() && (lineItem.getCaseSize().isPositive() || isPreferredUomConversionAvailable());
        }
        if (isViewOnly() || WarehouseDeliveryStatus.getClosedSet().contains(carton.getDelivery().getStatus())) {
            return false;
        }
        if (WarehouseDeliveryProperty.STOCK_ITEM.equals(name)) {
            return isNew() || isDeleteAllowed();
        }
        if (isNew()) {
            return false;
        }
        if (WarehouseDeliveryProperty.QUANTITY_RECEIVED_UOM.equals(name)) {
            return !isSerialNumberRequired() || isAGSNRequired();
        }
        if (WarehouseDeliveryProperty.QUANTITY_DAMAGED_UOM.equals(name)) {
            if (SimConfigManager.getBoolean(SimConfigManager.DISABLE_DAMAGES)) {
                return false;
            }
            return !isSerialNumberRequired() || isAGSNRequired();
        }
        //Case size only editable for unexpected line items
        if (WarehouseDeliveryProperty.CASE_SIZE.equals(name)) {
            if (SimConfigManager.getBoolean(SimConfigManager.DISABLE_CASE_SIZE)) {
                return false;
            }
            return isCasesMode() && !isExpected();
        }
        return false;
    }
}
