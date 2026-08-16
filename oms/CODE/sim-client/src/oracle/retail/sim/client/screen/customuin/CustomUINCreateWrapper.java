package oracle.retail.sim.client.screen.customuin;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;

/********************************************************************************************************
 * Wrapper for single entry row in the serial number table.
 * <p>
 * This is future work-in-progress code for future SIM 14.0 or later release.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CustomUINCreateWrapper {
    public static final String STOCK_ITEM_PROPERTY = "stockItem";
    public static final String DESCRIPTION_PROPERTY = "description";
    public static final String UIN_COUNT_PROPERTY = "serialNumberCount";

    private StockItem stockItem;
    private List<SerialNumberValue> serialNumbers = new ArrayList<>();

    public StockItem getStockItem() {
        return stockItem;
    }

    public String getDescription() {
        if (stockItem == null) {
            return StringConstants.EMPTY;
        }
        if (SimConfigManager.isItemShortDescription()) {
            return stockItem.getShortDescription();
        }
        return stockItem.getLongDescription();
    }

    public void setStockItem(StockItem stockItem) throws BusinessException {
        if (stockItem != null && !stockItem.isSerialNumberRequired()) {
            throw new BusinessException(UINMessageText.ITEM_NOT_UIN_ENABLED);
        }
        this.stockItem = stockItem;
    }

    public Integer getSerialNumberCount() {
        return serialNumbers.size();
    }

    public List<SerialNumberValue> getSerialNumbers() {
        return serialNumbers;
    }

    public void setSerialNumbers(List<SerialNumberValue> serialNumbers) {
        if (serialNumbers != null) {
            this.serialNumbers = serialNumbers;
        }
    }

    public boolean isPropertyModifiable(String property) {
        if (property.equals(STOCK_ITEM_PROPERTY)) {
            return stockItem == null;
        }
        if (property.equals(UIN_COUNT_PROPERTY)) {
            return true;
        }
        return false;
    }
}
