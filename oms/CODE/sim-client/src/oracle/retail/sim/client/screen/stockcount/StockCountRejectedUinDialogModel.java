package oracle.retail.sim.client.screen.stockcount;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.stockcount.StockCountRejectedLineItem;
import oracle.retail.sim.common.stockcount.StockCountSerialNumber;
import oracle.retail.sim.common.uin.UINType;

/********************************************************************************************************
 * The business logic model for the Stock Count Rejected UIN Dialog.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountRejectedUinDialogModel extends SimScreenModel {

    private StockCountRejectedLineItem lineItem;

    public void setLineItemWrapper(StockCountRejectedLineItem lineItem) throws Exception {
        this.lineItem = lineItem;
    }

    public String getItemId() {
        if (lineItem != null) {
            return lineItem.getItemId();
        }
        return null;
    }

    public Object getItemDescription() {
        if (lineItem != null) {
            if (SimConfigManager.isItemShortDescription()) {
                return lineItem.getShortDescription();
            }
            return lineItem.getLongDescription();
        }
        return null;
    }

    public String getUINLabel() {
        if (lineItem != null) {
            StockItem stockItem = lineItem.getStockItem();
            if (stockItem != null) {
                return stockItem.getUINLabel();
            }
        }
        return UINType.SERIAL.toString();
    }

    public List<StockCountUinWrapper> getSerialNumberWrappers() {
        List<StockCountUinWrapper> wrappers = new ArrayList<>();
        for (StockCountSerialNumber serialNumber : lineItem.getSerialNumbers()) {
            wrappers.add(createNewWrapper(serialNumber));
        }
        if (wrappers.isEmpty()) {
            wrappers.add(createNewWrapper());
        }
        return wrappers;
    }

    private StockCountUinWrapper createNewWrapper(StockCountSerialNumber serialNumber) {
        StockCountUinWrapper wrapper = createNewWrapper();
        wrapper.setSerialNumber(serialNumber);
        return wrapper;
    }

    public StockCountUinWrapper createNewWrapper() {
        StockItem stockItem = lineItem.getStockItem();
        StockCountUinWrapper wrapper = ClientWrapperFactory.createStockCountUinWrapper();
        wrapper.setItemId(stockItem.getId());
        wrapper.setType(stockItem.getUINType());
        wrapper.setLabel(stockItem.getUINLabel());
        wrapper.setFunctionalArea(FunctionalArea.STOCK_COUNT);
        return wrapper;
    }

    public void saveSerialNumbers(List<StockCountUinWrapper> wrappers) throws BusinessException {
        List<StockCountSerialNumber> uinValues = new ArrayList<>(wrappers.size());
        for (StockCountUinWrapper wrapper : wrappers) {
            if (wrapper.getSerialNumber() != null) {
                uinValues.add(wrapper.getSerialNumber());
            }
        }
        lineItem.setSerialNumbers(uinValues);
    }
}
