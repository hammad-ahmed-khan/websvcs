package oracle.retail.sim.client.screen.returns;

import java.math.BigDecimal;
import java.util.List;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.table.SimTableResetFocusException;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.item.NonSellableQtyType;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.lineitem.SerialNumberLineItemWrapper;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.rules.core.CaseSizeAndQtyValidForUomRule;
import oracle.retail.sim.common.rules.core.QuantityMustBePositiveRule;
import oracle.retail.sim.common.rules.core.QuantityMustConformToUOMRule;
import oracle.retail.sim.common.stockreturn.Return;
import oracle.retail.sim.common.stockreturn.ReturnLineItem;
import oracle.retail.sim.common.stockreturn.ReturnMessageText;
import oracle.retail.sim.common.stockreturn.ReturnProperty;
import oracle.retail.sim.common.stockreturn.ReturnReason;
import oracle.retail.sim.common.stockreturn.ReturnStatus;
import oracle.retail.sim.common.uin.SerialNumberValue;

/**
 * Wraps the Return Line Item for the PC UI. Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class ReturnLineItemWrapper extends SerialNumberLineItemWrapper {
    private Return stockReturn;
    private ReturnLineItem lineItem;
    private ReturnReason defaultReason;

    public ReturnLineItemWrapper(Return stockReturn) {
        this.stockReturn = stockReturn;
    }

    public ReturnLineItemWrapper(Return stockReturn, ReturnLineItem lineItem) {
        this.stockReturn = stockReturn;
        this.lineItem = lineItem;
    }

    public ReturnLineItemWrapper(Return stockReturn, ReturnReason defaultReason) {
        this.stockReturn = stockReturn;
        this.defaultReason = defaultReason;
    }

    public Return getStockReturn() {
        return stockReturn;
    }

    public ReturnLineItem getLineItem() {
        return lineItem;
    }

    public StockItem getStockItem() {
        return lineItem != null ? lineItem.getStockItem() : null;
    }

    public String getDescription() {
        if (lineItem == null) {
            return null;
        }
        if (SimConfigManager.isItemShortDescription()) {
            return lineItem.getStockItem().getShortDescription();
        }
        return lineItem.getStockItem().getLongDescription();
    }

    public boolean isNewWrapper() {
        return lineItem == null;
    }

    public Quantity getInventoryBasedOnUom() {
        if (lineItem == null) {
            return null;
        }
        StockItem stockItem = lineItem.getStockItem();
        ReturnReason returnReason = lineItem.getReason();
        if (stockItem == null || returnReason == null) {
            return null;
        }
        if (returnReason.isUseAvailable()) {
            return rationalizeQuantityBasedOnUom(stockItem.getAvailableStockOnHand());
        }
        if (SimConfigManager.getBoolean(SimConfigManager.ENABLE_SUB_BUCKETS)) {
            return rationalizeQuantityBasedOnUom(stockItem.getNonSellableTypeQty(returnReason.getNonSellableQtyTypeId()));
        }
        return rationalizeQuantityBasedOnUom(stockItem.getNonSellableQty());
    }

    public Quantity getCaseSize() {
        return isCasesMode() && lineItem != null ? lineItem.getCaseSize() : Quantity.ONE;
    }

    public ReturnReason getReason() {
        return lineItem != null ? lineItem.getReason() : null;
    }

    public String getNonSellableTypeDescription() throws Exception {
        NonSellableQtyType type = getNonSellableQtyType();
        if (type != null) {
            return "-" + Translator.getText(type.getDescription());
        }
        return StringConstants.EMPTY;
    }

    private NonSellableQtyType getNonSellableQtyType() throws Exception {
        if (lineItem != null && lineItem.getReason() != null) {
            if (SimConfigManager.getBoolean(SimConfigManager.ENABLE_SUB_BUCKETS)) {
                Long typeId = lineItem.getReason().getNonSellableQtyTypeId();
                if (typeId != null) {
                    for (NonSellableQtyType nonSellableType : ClientDataCacheUtility.getNonSellableQtyTypes()) {
                        if (nonSellableType.getId().equals(typeId)) {
                            return nonSellableType;
                        }
                    }
                }
            }
        }
        return null;
    }

    public UOMMode getUnitOfMeasureMode() {
        return lineItem != null ? super.getUnitOfMeasureMode() : getDefaultUomMode();
    }

    public Quantity getQuantityOrZero() {
        return lineItem != null ? lineItem.getQuantityOrZero() : Quantity.ZERO;
    }

    public Quantity getQuantityBasedOnUom() {
        return lineItem != null ? rationalizeQuantityBasedOnUom(lineItem.getQuantityOrZero()) : null;
    }

    public Quantity getQuantityRequestedBasedOnUom() {
        return lineItem != null ? rationalizeQuantityBasedOnUom(lineItem.getQuantityRequestedOrZero()) : null;
    }

    public void setStockItem(StockItem stockItem) throws BusinessException {
        if (stockItem != null) {
            if (lineItem == null) {
                lineItem = stockReturn.createLineItem(stockItem);
            } else if (!stockItem.equals(lineItem.getStockItem())) {
                stockReturn.removeLineItem(lineItem);
                lineItem = null;
                lineItem = stockReturn.createLineItem(stockItem);
            }
            if (defaultReason != null) {
                setReason(defaultReason);
            }
        }
    }

    // Always called with standard unit of measure
    public void setQuantity(Quantity quantity) throws Exception {
        checkForNullParameter("Quantity", quantity);
        QuantityMustBePositiveRule.execute(lineItem.getQuantity(), quantity);
        QuantityMustConformToUOMRule.execute(this, quantity);
        executeRule("setQuantity", quantity);
        doSetQuantity(quantity);
    }

    // Assumes it is in the current unit of measure
    public void setQuantityBasedOnUom(Quantity quantity) throws Exception {
        checkForNullParameter("Quantity", quantity);
        QuantityMustBePositiveRule.execute(lineItem.getQuantity(), quantity);
        QuantityMustConformToUOMRule.execute(this, quantity);
        executeRule("setQuantityBasedOnUom", quantity);
        if (isCasesMode()) {
            doSetQuantity(quantity.multiply(getCaseSize()));
        } else {
            doSetQuantity(quantity);
        }
    }

    private void doSetQuantity(Quantity quantity) throws Exception {
        Quantity oldQuantity = lineItem.getQuantity(); // Used to Be Based On UOM
        Quantity newQuantity = quantity;
        if (newQuantity.equals(oldQuantity)) {
            return;
        }
        StockItem stockItem = getStockItem();
        Quantity validateQuantity = newQuantity.subtract(lineItem.getOriginalQuantityOrZero());
        for (ReturnLineItem tmpLineItem : stockReturn.getLineItems()) {
            if (tmpLineItem.getStockItem().getId().equalsIgnoreCase(lineItem.getStockItem().getId())) {
                if (!tmpLineItem.getReason().getId().equals(lineItem.getReason().getId())) {
                    if (lineItem.isUseAvailable()) {
                        validateQuantity = validateQuantity.add(tmpLineItem.getQuantityOrZero()).subtract(tmpLineItem.getOriginalQuantityOrZero());
                    } else if (SimConfigManager.getBoolean(SimConfigManager.ENABLE_SUB_BUCKETS)) {
                        if (tmpLineItem.isUseUnavailable() && tmpLineItem.getReason().getNonSellableQtyTypeId().equals(lineItem.getReason().getNonSellableQtyTypeId())) {
                            validateQuantity = validateQuantity.add(tmpLineItem.getQuantityOrZero()).subtract(tmpLineItem.getOriginalQuantityOrZero());
                        }
                    }
                }
            }
        }
        if (lineItem.isUseUnavailable() && stockItem.isQtyGreaterThanNonSellable(validateQuantity, getNonSellableQtyType())) {
            UIStatusUtility.displayWarning(this, ReturnMessageText.QUANTITY_EXCEEDS_UNAVAILABLE);
            throw new SimTableResetFocusException();
        }
        if (!lineItem.isUseUnavailable() && stockItem.isQtyGreaterThanAvailableStockOnHand(validateQuantity)) {
            if (!RConfirmUtility.confirm("Quantity Confirmation", ReturnMessageText.QUANTITY_CONFIRM)) {
                throw new SimTableResetFocusException();
            }
        }
        lineItem.setQuantity(newQuantity);
    }

    public void setCaseSize(Quantity caseSize) throws BusinessException {
        CaseSizeAndQtyValidForUomRule.execute(this, caseSize, lineItem.getQuantityRequestedOrZero());
        CaseSizeAndQtyValidForUomRule.execute(this, caseSize, lineItem.getQuantityOrZero());
        executeRule("setCaseSize", caseSize);
        lineItem.setCaseSize(caseSize);
    }

    public void setReason(ReturnReason returnReason) throws BusinessException {
        if (returnReason != null && !returnReason.equals(lineItem.getReason())) {
            if (stockReturn.isLineItemOnReturn(lineItem.getStockItem().getId(), returnReason)) {
                throw new BusinessException(ReturnMessageText.ITEM_REASON_ALREADY_EXISTS);
            }
            lineItem.setReason(returnReason);
        }
    }

    public void setDefaultReason(ReturnReason defaultReason) {
        this.defaultReason = defaultReason;
    }

    public List<SerialNumberValue> getSerialNumbers() {
        return lineItem.getSerialNumbers();
    }

    public List<SerialNumberValue> getRemovedSerialNumbers() {
        return lineItem.getRemovedSerialNumbers();
    }

    public void addSerialNumber(SerialNumberValue value) throws BusinessException {
        lineItem.addSerialNumber(value);
    }

    public void removeSerialNumber(SerialNumberValue value) throws BusinessException {
        lineItem.removeSerialNumber(value.getUin());
    }

    public Integer getSerialNumberCount() {
        return lineItem != null ? lineItem.getSerialNumbers().size() : 0;
    }

    public Boolean getUseUnavailable() {
        return lineItem != null ? lineItem.isUseUnavailable() : Boolean.FALSE;
    }

    public boolean isMissingRequiredUins() {
        if (lineItem == null) {
            return false;
        }
        return lineItem.getStockItem().isSerialNumberRequired() ? lineItem.getQuantityOrZero().intValue() == 0 : false;
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

    /**
     * See if we can add or remove line items since this is really what this wrapper will do if we try to
     * set the stockable property. See the setStockable() method for more info.
     */
    public boolean isPropertyModifiable(String propertyName) throws Exception {
        if (propertyName.equals(ReturnProperty.SERIAL_NUMBER_COUNT)) {
            return isSerialNumberRequired();
        }
        ReturnStatus status = stockReturn.getStatus();
        if (status == ReturnStatus.CANCELED || status == ReturnStatus.DISPATCHED) {
            return false;
        }
        if (propertyName.equals(ReturnProperty.STOCK_ITEM)) {
            return stockReturn.isPropertyModifiable(ReturnProperty.RETURN_LINE_ITEM);
        }
        if (propertyName.equals(ReturnProperty.REASON)) {
            return lineItem.getQuantity() == null;
        }
        if (propertyName.equals(ReturnProperty.CASE_SIZE) && isStandardMode()) {
            return false;
        }
        if (propertyName.equals(ReturnProperty.QUANTITY_BASED_ON_UOM)) {
            if (isSerialNumberRequired()) {
                return false;
            }
            return lineItem.getReason() != null;
        }
        if (propertyName.equals(ReturnProperty.USE_UNAVAILABLE_QTY)) {
            return false;
        }
        if (lineItem != null) {
            return lineItem.isPropertyModifiable(propertyName, stockReturn);
        }
        return false;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        ReturnLineItemWrapper other = (ReturnLineItemWrapper) object;
        if (stockReturn != null && other.stockReturn != null) {
            if (!stockReturn.equals(other.stockReturn)) {
                return false;
            }
        }
        if (lineItem == null && other.lineItem == null) {
            return true;
        }
        if (lineItem == null || other.lineItem == null) {
            return false;
        }
        return lineItem.equals(other.lineItem);
    }

    public int hashCode() {
        return super.hashCode();
    }
}
