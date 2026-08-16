package oracle.retail.sim.client.screen.stockcount;

import java.math.BigDecimal;
import java.util.List;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.lineitem.UnitOfMeasureWrapper;
import oracle.retail.sim.common.stockcount.StockCountLineItem;
import oracle.retail.sim.common.stockcount.StockCountMessageText;
import oracle.retail.sim.common.stockcount.StockCountPhase;
import oracle.retail.sim.common.stockcount.StockCountProperty;
import oracle.retail.sim.common.stockcount.StockCountSerialNumber;
import oracle.retail.sim.common.storesequence.StoreSequenceAreaType;
import oracle.retail.sim.common.storesequence.StoreSequenceConstants;
import oracle.retail.sim.common.uin.UINType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/********************************************************************************************************
 * Stock Count Line Item Wrapper - Wraps a StockCountLineItem object specifically to handle the
 * functionality of authorization on the PC UI client side.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountLineItemWrapper extends UnitOfMeasureWrapper {

    private StockCountLineItem lineItem;
    private String serialNumberLabel;
    private UOMMode uomMode = UOMMode.STANDARD;
    private boolean isAuthorizeMode;

    public StockCountLineItemWrapper(StockCountLineItem lineItem) {
        this(lineItem, false);
    }

    public StockCountLineItemWrapper(StockCountLineItem lineItem, boolean isAuthorizeMode) {
        this.lineItem = lineItem;
        this.isAuthorizeMode = isAuthorizeMode;
    }

    public StockCountLineItem getLineItem() {
        return lineItem;
    }

    public Long getSequence() {
        return lineItem.getId();
    }

    public String getItemId() {
        return lineItem.getItemId();
    }

    public String getShortDescription() {
        return lineItem.getShortDescription();
    }

    public String getLongDescription() {
        return lineItem.getLongDescription();
    }

    public UOMMode getUnitOfMeasureMode() {
        return uomMode;
    }

    public boolean isCasesMode() {
        return getUnitOfMeasureMode() == UOMMode.CASES;
    }

    public boolean isStandardMode() {
        return getUnitOfMeasureMode() == UOMMode.STANDARD;
    }

    public String getStandardUnitOfMeasure() {
        return lineItem.getUnitOfMeasure();
    }

    public Quantity getCaseSize() {
        return lineItem.getCaseSize();
    }

    public StoreSequenceAreaType getArea() {
        return lineItem.getLocationArea();
    }

    public String getLocation() {
        String description = lineItem.getLocationDescription();
        if (description == null) {
            return StringConstants.EMPTY;
        }
        if (StoreSequenceConstants.NO_SEQUENCE_DESCRIPTION.equals(description)) {
            return Translator.getText(description);
        }
        StringHelper helper = StringHelper.getInstance(LocaleManager.getLanguageLocale());
        StringBuilder buffer = new StringBuilder(helper.replace(description, ":::", " - "));
        StoreSequenceAreaType area = lineItem.getLocationArea();
        if (area != null) {
            buffer.append(" - ");
            buffer.append(Translator.getText(area.toString()));
        }
        return buffer.toString();
    }

    public String getUINLabel() {
        return serialNumberLabel;
    }

    public UINType getUINType() {
        return lineItem.getStockCountItem().getUINType();
    }

    public boolean isMultiLocated() {
        return lineItem.isMultiLocated();
    }

    public boolean isSerialNumberRequired() {
        return lineItem.isSerialNumberRequired();
    }

    public List<StockCountSerialNumber> getSerialNumbers() {
        return lineItem.getSerialNumbers();
    }

    public Integer getSerialNumberTotal() {
        if (isAuthorizeMode) {
            Quantity quantity = lineItem.getStockApproved();
            if (quantity == null) {
                quantity = lineItem.getStockRecountedTotal();
            }
            if (quantity == null) {
                quantity = lineItem.getStockRecounted();
            }
            if (quantity == null) {
                quantity = lineItem.getStockCountedTotal();
            }
            if (quantity == null) {
                quantity = lineItem.getStockCounted();
            }
            if (quantity == null) {
                return null;
            }
            return quantity.intValue();
        }
        return lineItem.getSerialNumbers().size();
    }

    public Quantity getStockCounted() {
        return lineItem.getStockCounted();
    }

    public Quantity getStockCountedOrZero() {
        return lineItem.getStockCounted() != null ? lineItem.getStockCounted() : Quantity.ZERO;
    }

    public Quantity getStockCountedBasedOnUom() {
        return lineItem != null ? rationalizeQuantityBasedOnUom(lineItem.getStockCounted()) : null;
    }

    public Quantity getStockRecounted() {
        return lineItem.getStockRecounted();
    }

    public Quantity getStockRecountedOrZero() {
        return lineItem.getStockRecounted() != null ? lineItem.getStockRecounted() : Quantity.ZERO;
    }

    public Quantity getStockRecountedBasedOnUom() {
        return lineItem != null ? rationalizeQuantityBasedOnUom(lineItem.getStockRecounted()) : null;
    }

    public Quantity getStockApproved() {
        return lineItem.getStockApproved();
    }

    public Quantity getStockCountedTotal() {
        return lineItem.getStockCountedTotal();
    }

    public Quantity getStockRecountedTotal() {
        return lineItem.getStockRecountedTotal();
    }

    public Quantity getSnapshot() {
        return lineItem.getRecountSnapshot() != null ? lineItem.getRecountSnapshot() : lineItem.getCountSnapshot();
    }

    /**
     * Gets the variance in the number of stock counted. Null if not counted.
     */
    public Quantity getStockCountedVariance() {
        Quantity countQty = lineItem.getStockCountedTotal();
        Quantity countSnapshot = lineItem.getCountSnapshot();
        if (countQty == null || countSnapshot == null) {
            return null;
        }
        return countQty.subtract(lineItem.getCountSnapshot());
    }

    /**
     * Gets the variance in the number of stock re-counted. Null if not re-counted.
     */
    public Quantity getStockRecountedVariance() {
        Quantity recountQty = lineItem.getStockRecountedTotal();
        if (recountQty == null) {
            return null;
        }
        Quantity snapshotQty = lineItem.getRecountSnapshot();
        if (snapshotQty == null) {
            snapshotQty = lineItem.getCountSnapshot();
        }
        if (snapshotQty == null) {
            return null;
        }
        return recountQty.subtract(snapshotQty);
    }

    /**
     * Gets the variance in the number of stock counted. Null if not counted.
     * Percentage calculations run from 0 -> 1.
     */
    public Quantity getStockCountedPercent() {
        Quantity variance = getStockCountedVariance();
        if (variance == null) {
            return null;
        }
        Quantity snapshot = getSnapshot();
        if (snapshot.equals(Quantity.ZERO)) {
            snapshot = Quantity.ONE;
        }
        return variance.divide(snapshot).abs();
    }

    /**
     * Gets the variance in the number of stock re-counted. Null if not re-counted.
     * Percentage calculations run from 0 -> 1.
     */
    public Quantity getStockRecountedPercent() {
        Quantity variance = getStockRecountedVariance();
        if (variance == null) {
            return null;
        }
        Quantity snapshot = getSnapshot();
        if (snapshot.equals(Quantity.ZERO)) {
            snapshot = Quantity.ONE;
        }
        return variance.divide(snapshot).abs();
    }

    /**
     * Assigns the unit of measure for processing. This method does NOT allow null values.
     */
    public void setUnitOfMeasureMode(UOMMode uomMode) throws BusinessException {
        executeRule("setUnitOfMeasureMode", uomMode);
        if (uomMode != null) {
            this.uomMode = uomMode;
        }
    }

    /**
     * Sets the stock counted in standard unit of measure.
     * @param quantity The quantity of stock counted. Provide null if not counted.
     */
    public void setStockCounted(Quantity quantity) throws BusinessException {
        executeRule("setStockCounted", quantity);
        doSetStockCounted(quantity);
    }

    /**
     * Sets the stock counted based on current unit of measure mode.
     * @param quantity The quantity of stock counted. Provide null if not counted.
     */
    public void setStockCountedBasedOnUom(Quantity quantity) throws BusinessException {
        executeRule("setStockCountedBasedOnUom", quantity);
        if (isCasesMode()) {
            doSetStockCounted(quantity.multiply(getCaseSize()));
        } else {
            doSetStockCounted(quantity);
        }
    }

    /**
     * Sets the stock recounted in standard unit of measure.
     * @param quantity The quantity of stock counted. Provide null if not counted.
     */
    public void setStockRecounted(Quantity quantity) throws BusinessException {
        executeRule("setStockRecountedBasedOnUom", quantity);
        doSetStockRecounted(quantity);
    }

    /**
     * Sets the stock recounted based on unit of measure mode.
     * @param quantity The quantity of stock counted. Provide null if not counted.
     */
    public void setStockRecountedBasedOnUom(Quantity quantity) throws BusinessException {
        executeRule("setStockRecountedBasedOnUom", quantity);
        if (isCasesMode()) {
            doSetStockRecounted(quantity.multiply(getCaseSize()));
        } else {
            doSetStockRecounted(quantity);
        }
    }

    /**
     * Helper method to set stock counted
     */
    private void doSetStockCounted(Quantity quantity) throws BusinessException {
        if (lineItem.isSerialNumberRequired() && quantity.isPositive()) {
            if (SimConfigManager.getStoreBoolean(StoreConfigKeys.UIN_PROCESSING_ENABLED, SimRepository.getStoreId())) {
                throw new BusinessException(StockCountMessageText.QUANTITY_NOT_ALLOWED);
            }
        }
        if (lineItem.getStockCounted() == null) {
            lineItem.setPhysicalTimestamp(SimDateUtil.getCurrentDate());
        }
        lineItem.setStockCounted(quantity);
    }

    /**
     * Helper method to set stock recounted
     */
    private void doSetStockRecounted(Quantity quantity) throws BusinessException {
        if (lineItem.isSerialNumberRequired() && quantity.isPositive()) {
            if (SimConfigManager.getStoreBoolean(StoreConfigKeys.UIN_PROCESSING_ENABLED, SimRepository.getStoreId())) {
                throw new BusinessException(StockCountMessageText.QUANTITY_NOT_ALLOWED);
            }
        }
        if (lineItem.getStockRecounted() == null) {
            lineItem.setPhysicalTimestamp(SimDateUtil.getCurrentDate());
        }
        lineItem.setStockRecounted(quantity);
    }

    /**
     * Sets the stock approved.
     * @param quantity The quantity of stock approved.
     */
    public void setStockApproved(Quantity quantity) throws BusinessException {
        executeRule("setStockApproved", quantity);
        lineItem.setStockApproved(quantity);
    }

    /**
     * Assigns serial numbers to the line item.
     */
    public void setSerialNumbers(List<StockCountSerialNumber> serialNumbers, StockCountPhase phase) throws BusinessException {
        lineItem.setSerialNumbers(serialNumbers, phase);
    }

    public void removeSerialNumber(String serialNumber, StockCountPhase phase) throws BusinessException {
        lineItem.removeSerialNumber(serialNumber, phase);
    }

    public void setSerialNumberLabel(String label) {
        serialNumberLabel = label;
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

    public boolean isPropertyModifiable(String property) {
        if (StockCountProperty.SERIAL_NUMBER_TOTAL.equals(property)) {
            return isSerialNumberRequired();
        }
        if (StockCountProperty.COUNTED_QTY.equals(property)) {
            return !isSerialNumberRequired();
        }
        if (StockCountProperty.RECOUNTED_QTY.equals(property)) {
            return !isSerialNumberRequired();
        }
        if (StockCountProperty.AUTHORIZED_QTY.equals(property)) {
            return !isSerialNumberRequired();
        }
        return true;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        StockCountLineItemWrapper that = (StockCountLineItemWrapper) object;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(getLineItem(), that.getLineItem());
        return builder.isEquals();
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getLineItem());
        return builder.toHashCode();
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder("StockCountLineItemWrapper[");
        buffer.append("StockCountLineItem=").append(getLineItem());
        return buffer.toString();
    }
}
