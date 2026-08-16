package oracle.retail.sim.client.screen.invadjustment;

import java.math.BigDecimal;
import java.util.List;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.table.SimTableResetFocusException;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.invadjustment.InventoryAdjustment;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentLineItem;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentMessageText;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentProperty;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentStatus;
import oracle.retail.sim.common.item.InventoryDisposition;
import oracle.retail.sim.common.item.ItemStatus;
import oracle.retail.sim.common.item.NonSellableQtyType;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.lineitem.SerialNumberLineItemWrapper;
import oracle.retail.sim.common.rules.core.CaseSizeAndQtyValidForUomRule;
import oracle.retail.sim.common.rules.core.QuantityMustBePositiveRule;
import oracle.retail.sim.common.rules.core.QuantityMustConformToUOMRule;
import oracle.retail.sim.common.uin.SerialNumberValue;

public class InventoryAdjustmentLineItemWrapper extends SerialNumberLineItemWrapper {

    private InventoryAdjustment adjustment;
    private InventoryAdjustmentLineItem lineItem;
    private InventoryAdjustmentReason defaultReason;
    private boolean isLineItemQtyValidated = false;

    public InventoryAdjustmentLineItemWrapper(InventoryAdjustment adjustment, InventoryAdjustmentLineItem lineItem) {
        this.adjustment = adjustment;
        this.lineItem = lineItem;
    }

    public InventoryAdjustmentLineItemWrapper(InventoryAdjustment adjustment, InventoryAdjustmentReason defaultReason) {
        this.adjustment = adjustment;
        this.defaultReason = defaultReason;
    }

    public InventoryAdjustment getAdjustment() {
        return adjustment;
    }

    public InventoryAdjustmentLineItem getLineItem() {
        return lineItem;
    }

    public StockItem getStockItem() {
        return lineItem != null ? lineItem.getStockItem() : null;
    }

    public String getItemDescription() {
        StockItem stockItem = getStockItem();
        return stockItem != null ? stockItem.getItemDescription() : null;
    }

    public void setDefaultReason(InventoryAdjustmentReason reason) {
        if (defaultReason == null) {
            defaultReason = reason;
        }
    }

    public boolean isValidatedQty() {
		return isLineItemQtyValidated;
	}

	public void setValidatedQty(boolean isValidatedQty) {
		this.isLineItemQtyValidated = isValidatedQty;
	}

    public void setStockItem(StockItem stockItem) throws BusinessException {
        if (stockItem == null) {
            return;
        }
        ItemStatus status = stockItem.getStatus();
        if (status == ItemStatus.INACTIVE || status == ItemStatus.DELETED || status == ItemStatus.DISCONTINUED) {
            if (!RConfirmUtility.confirm("Inventory Adjustment", CommonMessageText.NON_ACTIVE_ITEM_CONFIRM, status.toString())) {
                return;
            }
        }
        for (InventoryAdjustmentLineItem tempLineItem : adjustment.getLineItems()) {
            if (stockItem.equals(tempLineItem.getStockItem())) {
                if (tempLineItem != lineItem) {
                    throw new BusinessException(CommonMessageText.ITEM_ALREADY_EXISTS);
                }
            }
        }
        if (lineItem == null) {
            lineItem = adjustment.createLineItem(stockItem);
        } else if (!stockItem.equals(lineItem.getStockItem())) {
            adjustment.removeLineItem(lineItem);
            lineItem = null;
            lineItem = adjustment.createLineItem(stockItem);
        }
        if (defaultReason != null) {
            setReason(defaultReason);
        }
    }

    public InventoryAdjustmentStatus getStatus() {
        return adjustment.getStatus();
    }

    public InventoryAdjustmentReason getReason() {
        return lineItem != null ? lineItem.getReason() : null;
    }

    public InventoryDisposition getDisposition() {
        InventoryAdjustmentReason reason = getReason();
        return reason != null ? reason.getDisposition() : null;
    }

    public void setReason(InventoryAdjustmentReason reason) throws BusinessException {
        lineItem.setReason(reason);
    }

    public void clearReason() {
        lineItem.doSetReason(null);
        lineItem.doSetDirty();
    }

    public String getNonSellableTypeDescription() throws Exception {
        InventoryAdjustmentReason reason = getReason();
        if (reason != null) {
            NonSellableQtyType toType = getNonSellableQtyType(reason.getToNonSellableQtyTypeId());
            NonSellableQtyType fromType = getNonSellableQtyType(reason.getFromNonSellableQtyTypeId());
            if (fromType != null && toType != null) {
                return "-" + Translator.getText(fromType.getDescription()) + " && +" + Translator.getText(toType.getDescription());
            }
            if (fromType != null) {
                return "-" + Translator.getText(fromType.getDescription());
            }
            if (toType != null) {
                return "+" + Translator.getText(toType.getDescription());
            }
        }
        return StringConstants.EMPTY;
    }

    private NonSellableQtyType getNonSellableQtyType(Long typeId) throws Exception {
        if (lineItem != null && lineItem.getReason() != null && typeId != null) {
            if (SimConfigManager.getBoolean(SimConfigManager.ENABLE_SUB_BUCKETS)) {
                for (NonSellableQtyType nonSellableType : ClientDataCacheUtility.getNonSellableQtyTypes()) {
                    if (nonSellableType.getId().equals(typeId)) {
                        return nonSellableType;
                    }
                }
            }
        }
        return null;
    }

    public Quantity getInventoryBasedOnUom() {
        if (lineItem == null) {
            return null;
        }
        StockItem stockItem = lineItem.getStockItem();
        InventoryAdjustmentReason reason = lineItem.getReason();
        if (stockItem == null || reason == null || reason.getDisposition() == null) {
            return null;
        }
        switch (reason.getDisposition()) {
            case AVAILABLE_SOH_TO_UNAVAILABLE_SOH:
            case AVAILABLE_TO_OUT:
            case AVAILABLE_TO_CUSTOMER_RESERVED:
                return rationalizeQuantityBasedOnUom(stockItem.getAvailableStockOnHand());
            case UNAVAILABLE_TO_UNAVAILABLE:
            case UNAVAILABLE_SOH_TO_AVAILABLE_SOH:
            case UNAVAILABLE_TO_OUT:
                if (SimConfigManager.getBoolean(SimConfigManager.ENABLE_SUB_BUCKETS)) {
                    return rationalizeQuantityBasedOnUom(stockItem.getNonSellableTypeQty(reason.getFromNonSellableQtyTypeId()));
                }
                return rationalizeQuantityBasedOnUom(stockItem.getNonSellableQty());
            case CUSTOMER_RESERVED_TO_AVAILABLE:
                return rationalizeQuantityBasedOnUom(stockItem.getCustomerReservedQty());
            default:
                return null;
        }
    }

    public Quantity getCaseSize() {
        if (lineItem != null && isCasesMode()) {
            return lineItem.getCaseSize();
        }
        return Quantity.ONE;
    }

    public void setCaseSize(Quantity caseSize) throws BusinessException {
        if (isCasesMode()) {
            CaseSizeAndQtyValidForUomRule.execute(this, caseSize, getQuantity());
            lineItem.setCaseSize(caseSize);
        }
    }

    public Quantity getQuantity() {
        return lineItem != null ? lineItem.getQuantityOrZero() : null;
    }

    public Quantity getQuantityBasedOnUom() {
        return lineItem != null ? rationalizeQuantityBasedOnUom(lineItem.getQuantityOrZero()) : null;
    }

    public void setQuantity(Quantity quantity) throws Exception {
        checkForNullParameter("Quantity", quantity);
        QuantityMustBePositiveRule.execute(quantity);
        QuantityMustConformToUOMRule.execute(this, quantity);
        doSetQuantity(quantity);
    }

    public void setQuantityBasedOnUom(Quantity quantity) throws Exception {
        checkForNullParameter("Quantity", quantity);
        QuantityMustBePositiveRule.execute(quantity);
        QuantityMustConformToUOMRule.execute(this, quantity);
        if (isCasesMode()) {
            doSetQuantity(quantity.multiply(getCaseSize()));
        } else {
            doSetQuantity(quantity);
        }
    }

    private void doSetQuantity(Quantity quantity) throws Exception {
        if (isQtyGreaterThanAvailableStockOnHand(quantity)) {
        	setValidatedQty(false);
            if (!RConfirmUtility.confirm("Quantity Confirmation", InventoryAdjustmentMessageText.AVAILABLE_QTY_CONFIRM)) {
                throw new SimTableResetFocusException();
            }
        }
        if (isQtyGreaterThanNonSellable(quantity)) {
        	setValidatedQty(false);
            UIStatusUtility.displayWarning(this, InventoryAdjustmentMessageText.UNAVAILABLE_QTY_ERROR);
            throw new SimTableResetFocusException();
        }
        lineItem.setQuantity(quantity);
    	setValidatedQty(true);
    }

    public boolean isQtyGreaterThanAvailableStockOnHand(Quantity quantity) {
        InventoryAdjustmentReason reason = getReason();
        InventoryDisposition disposition = reason.getDisposition();
        if (disposition == InventoryDisposition.AVAILABLE_TO_OUT || disposition == InventoryDisposition.AVAILABLE_SOH_TO_UNAVAILABLE_SOH) {
            return getStockItem().isQtyGreaterThanAvailableStockOnHand(quantity);
        }
        return false;
    }

    public boolean isQtyGreaterThanNonSellable(Quantity quantity) throws Exception {
        InventoryAdjustmentReason reason = getReason();
        InventoryDisposition disposition = reason.getDisposition();
        if (disposition == InventoryDisposition.UNAVAILABLE_TO_OUT || disposition == InventoryDisposition.UNAVAILABLE_SOH_TO_AVAILABLE_SOH
        		|| disposition == InventoryDisposition.UNAVAILABLE_TO_UNAVAILABLE) {
            return getStockItem().isQtyGreaterThanNonSellable(quantity, getNonSellableQtyType(reason.getFromNonSellableQtyTypeId()));
        }
        return false;
    }

    public void addSerialNumber(SerialNumberValue value) throws BusinessException {
        lineItem.addSerialNumber(value);
    }

    public void removeSerialNumber(SerialNumberValue value) throws BusinessException {
        lineItem.removeSerialNumber(value.getUin());
    }

    public List<SerialNumberValue> getSerialNumbers() {
        return lineItem.getSerialNumbers();
    }

    public Integer getSerialNumberCount() {
        return lineItem != null ? lineItem.getSerialNumbers().size() : 0;
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

    public boolean isCoherent() throws BusinessException {
        if (lineItem == null) {
            throw new BusinessException(CommonMessageText.LINE_ITEM_NO_ITEM);
        }
        return lineItem.isCoherent();
    }

    public boolean isPropertyModifiable(String property) {
        boolean isInProgress = adjustment.getStatus() == InventoryAdjustmentStatus.IN_PROGRESS;
        if (InventoryAdjustmentProperty.STOCK_ITEM.equals(property)) {
            return isInProgress;
        }
        if (InventoryAdjustmentProperty.ITEM_DESCRIPTION.equals(property)) {
            return false;
        }
        if (InventoryAdjustmentProperty.REASON.equals(property)) {
            return lineItem != null && isInProgress && lineItem.getQuantityOrZero().isZero();
        }
        if (InventoryAdjustmentProperty.DISPOSITION.equals(property)) {
            return false;
        }
        if (InventoryAdjustmentProperty.UOM_MODE.equals(property)) {
            return true;
        }
        if (InventoryAdjustmentProperty.CASE_SIZE.equals(property)) {
            return lineItem != null && isInProgress && isCasesMode() && !SimConfigManager.getBoolean(SimConfigManager.DISABLE_CASE_SIZE);
        }
        if (InventoryAdjustmentProperty.INVENTORY_BASED_ON_UOM.equals(property)) {
            return false;
        }
        if (InventoryAdjustmentProperty.QUANTITY_BASED_ON_UOM.equals(property)) {
            if (lineItem != null && isInProgress && lineItem.getReason() != null) {
                if (lineItem.getStockItem().isSerialNumberRequired()) {
                    if (lineItem.getStockItem().isAgsnEnabled()) {
                        return lineItem.getReason().getDisposition() == InventoryDisposition.OUT_TO_AVAILABLE;
                    }
                    return false;
                }
                return true;
            }
            return false;
        }
        if (property.equals(InventoryAdjustmentProperty.SERIAL_NUMBER_COUNT)) {
            return lineItem != null && isSerialNumberRequired();
        }
        return false;
    }
}
