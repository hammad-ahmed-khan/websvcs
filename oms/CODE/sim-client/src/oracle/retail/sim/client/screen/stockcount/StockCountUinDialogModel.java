package oracle.retail.sim.client.screen.stockcount;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.stockcount.StockCountPhase;
import oracle.retail.sim.common.stockcount.StockCountSerialNumber;
import oracle.retail.sim.common.uin.UINType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * The business logic model for the UIN Stock Count Dialog.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountUinDialogModel extends SimScreenModel {
    private StockCountWrapper stockCountWrapper;
    private StockCountLineItemWrapper lineItemWrapper;
    private List<String> removedSerialNumbers = new ArrayList<>();

    public void setLineItemWrapper(StockCountLineItemWrapper wrapper) {
        stockCountWrapper = (StockCountWrapper) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_STOCK_COUNT);
        lineItemWrapper = wrapper;
    }

    public String getItemId() {
        if (lineItemWrapper != null) {
            return lineItemWrapper.getItemId();
        }
        return null;
    }

    public Object getItemDescription() {
        if (lineItemWrapper != null) {
            if (SimConfigManager.isItemShortDescription()) {
                return lineItemWrapper.getShortDescription();
            }
            return lineItemWrapper.getLongDescription();
        }
        return null;
    }

    public String getSerialNumberLabel() {
        String uinLabel = UINType.SERIAL.toString();
        if (lineItemWrapper != null) {
            uinLabel = lineItemWrapper.getUINLabel();

            if (StringUtility.isNullOrEmpty(uinLabel)) {
                try {
                    uinLabel = ClientServiceFactory.getUINServices().readUINLabel(lineItemWrapper.getItemId(), getStoreId());
                    lineItemWrapper.setSerialNumberLabel(uinLabel);
                } catch (Exception e) {
                    LogService.error(this, "No Serial Label!", e);
                    uinLabel = UINType.SERIAL.toString();
                }
            }
        }
        return uinLabel;
    }

    public List<StockCountUinWrapper> getStockCountSerialNumberWrappers() {
        List<StockCountUinWrapper> wrappers = new ArrayList<>();
        for (StockCountSerialNumber serialNumber : lineItemWrapper.getSerialNumbers()) {
            wrappers.add(createNewWrapper(serialNumber));
        }
        return wrappers;
    }

    private StockCountUinWrapper createNewWrapper(StockCountSerialNumber serialNumber) {
        StockCountUinWrapper wrapper = createNewWrapper();
        wrapper.setSerialNumber(serialNumber);
        return wrapper;
    }

    public StockCountUinWrapper createNewWrapper() {
        StockCountUinWrapper wrapper = ClientWrapperFactory.createStockCountUinWrapper();
        wrapper.setItemId(lineItemWrapper.getItemId());
        wrapper.setType(lineItemWrapper.getUINType());
        wrapper.setLabel(lineItemWrapper.getUINLabel());
        if (stockCountWrapper.getLocationPhase() == StockCountPhase.RECOUNT) {
            wrapper.setFunctionalArea(FunctionalArea.STOCK_RECOUNT);
        } else {
            wrapper.setFunctionalArea(FunctionalArea.STOCK_COUNT);
        }
        return wrapper;
    }

    public boolean removeSerialNumber(StockCountUinWrapper wrapper) {
        StockCountSerialNumber serialNumber = wrapper.getSerialNumber();
        if (serialNumber != null) {
            if (serialNumber.getSerialNumber() != null) {
                removedSerialNumbers.add(serialNumber.getSerialNumber());
            }
        }
        return true;
    }

    public void saveSerialNumbers(List<StockCountUinWrapper> wrappers) throws BusinessException {
        for (String serialNumber : removedSerialNumbers) {
            lineItemWrapper.removeSerialNumber(serialNumber, stockCountWrapper.getLocationPhase());
        }
        List<StockCountSerialNumber> uinValues = new ArrayList<>(wrappers.size());
        for (StockCountUinWrapper wrapper : wrappers) {
            if (wrapper.getSerialNumber() != null) {
                uinValues.add(wrapper.getSerialNumber());
            }
        }
        lineItemWrapper.setSerialNumbers(uinValues, stockCountWrapper.getLocationPhase());
    }
}
