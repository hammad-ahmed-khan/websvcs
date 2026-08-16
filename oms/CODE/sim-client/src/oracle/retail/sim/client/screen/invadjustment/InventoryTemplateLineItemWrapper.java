package oracle.retail.sim.client.screen.invadjustment;

import java.math.BigDecimal;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentMessageText;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplate;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateLineItem;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateProperty;
import oracle.retail.sim.common.item.InventoryDisposition;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.lineitem.StockLineItemWrapper;
import oracle.retail.sim.common.rules.core.CaseSizeAndQtyValidForUomRule;

public class InventoryTemplateLineItemWrapper extends StockLineItemWrapper {

    private InventoryAdjustmentTemplate template;
    private InventoryAdjustmentTemplateLineItem lineItem;
    private InventoryAdjustmentReason defaultReason;

    public InventoryTemplateLineItemWrapper(InventoryAdjustmentTemplate template) {
        this.template = template;
    }

    public InventoryTemplateLineItemWrapper(InventoryAdjustmentTemplate template, InventoryAdjustmentTemplateLineItem lineItem) {
        this.template = template;
        this.lineItem = lineItem;
    }

    public InventoryAdjustmentTemplateLineItem getLineItem() {
        return lineItem;
    }

    public StockItem getStockItem() {
        return lineItem != null ? lineItem.getStockItem() : null;
    }

    public void setStockItem(StockItem stockItem) throws BusinessException {
        if (stockItem != null) {
            if (stockItem.isSerialNumberRequired()) {
                throw new BusinessException(InventoryAdjustmentMessageText.TEMPLATE_UIN_ITEM_INVALID);
            }
            if (lineItem == null) {
                lineItem = template.createLineItem(stockItem);
            } else if (!stockItem.equals(lineItem.getStockItem())) {
                template.removeLineItem(lineItem);
                lineItem = null;
                lineItem = template.createLineItem(stockItem);
            }
            if (defaultReason != null) {
                lineItem.setReason(defaultReason);
            }
        }
    }

    public String getItemDescription() {
        return lineItem != null ? lineItem.getStockItem().getItemDescription() : null;
    }

    public void setDefaultReason(InventoryAdjustmentReason defaultReason) {
        this.defaultReason = defaultReason;
    }

    public InventoryAdjustmentReason getReason() {
        return lineItem != null ? lineItem.getReason() : null;
    }

    public void setReason(InventoryAdjustmentReason reason) throws BusinessException {
        lineItem.setReason(reason);
    }

    public InventoryDisposition getDisposition() {
        InventoryAdjustmentReason reason = getReason();
        return reason != null ? reason.getDisposition() : null;
    }

    public Quantity getCaseSize() {
        if (isCasesMode() && lineItem != null) {
            return lineItem.getCaseSize();
        }
        return Quantity.ONE;
    }

    public void setCaseSize(Quantity caseSize) throws BusinessException {
        if (isCasesMode()) {
            CaseSizeAndQtyValidForUomRule.execute(this, caseSize, lineItem.getQuantityOrZero());
            lineItem.setCaseSize(caseSize);
        }
    }

    public Quantity getQuantityBasedOnUOM() {
        return lineItem != null ? rationalizeQuantityBasedOnUom(lineItem.getQuantityOrZero()) : null;
    }

    public void setQuantityBasedOnUOM(Quantity quantity) throws BusinessException {
        if (quantity != null && isCasesMode()) {
            quantity = quantity.multiply(getCaseSize());
        }
        lineItem.setQuantity(quantity);
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
        if (InventoryAdjustmentTemplateProperty.STOCK_ITEM.equals(property)) {
            return getStockItem() == null;
        }
        if (InventoryAdjustmentTemplateProperty.REASON.equals(property)) {
            return lineItem != null;
        }
        if (InventoryAdjustmentTemplateProperty.CASE_SIZE.equals(property)) {
            return isCasesMode() && !SimConfigManager.getBoolean(SimConfigManager.DISABLE_CASE_SIZE);
        }
        if (InventoryAdjustmentTemplateProperty.QUANTITY_BASED_ON_UOM.equals(property)) {
            return lineItem != null;
        }
        return true;
    }
}
