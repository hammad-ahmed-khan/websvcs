package oracle.retail.sim.client.screen.returns;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.screen.uin.SerialNumberWrapper;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.stockreturn.ReturnStatus;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINType;
import oracle.retail.sim.common.uin.UINUserAction;

/********************************************************************************************************
 * The business logic model for the Return UIN dialog.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ReturnUinDialogModel extends SimScreenModel {

    private ReturnLineItemWrapper lineItemWrapper;

    public void setLineItemWrapper(ReturnLineItemWrapper wrapper) {
        lineItemWrapper = wrapper;
    }

    public String getItemId() {
        if (lineItemWrapper != null) {
            return lineItemWrapper.getStockItem().getId();
        }
        return null;
    }

    public boolean isUseUnavailable() {
        return lineItemWrapper.getUseUnavailable();
    }

    public String getItemDescription() {
        if (lineItemWrapper != null) {
            if (SimConfigManager.isItemShortDescription()) {
                return lineItemWrapper.getStockItem().getShortDescription();
            }
            return lineItemWrapper.getStockItem().getLongDescription();
        }
        return null;
    }

    public String getNonSellableTypeDescription() throws Exception {
        if (lineItemWrapper != null && isNonSellableTypesActive()) {
            return lineItemWrapper.getNonSellableTypeDescription();
        }
        return null;
    }

    public Quantity getInventory() {
        if (lineItemWrapper != null) {
            return lineItemWrapper.getInventoryBasedOnUom();
        }
        return null;
    }

    public List<SerialNumberWrapper> getSerialNumberWrappers() {
        List<SerialNumberWrapper> wrappers = new ArrayList<>();
        for (SerialNumberValue value : lineItemWrapper.getSerialNumbers()) {
            SerialNumberWrapper wrapper = createSerialNumberWrapper();
            wrapper.setSerialNumberValue(value);
            wrapper.setDefaultAction();
            wrapper.setValidated();

            wrappers.add(wrapper);
        }
        for (SerialNumberValue value : lineItemWrapper.getRemovedSerialNumbers()) {
            SerialNumberWrapper wrapper = createSerialNumberWrapper();
            wrapper.setSerialNumberValue(value);
            wrapper.setUserAction(UINUserAction.REMOVED);
            wrapper.setValidated();

            wrappers.add(wrapper);
        }
        if (wrappers.isEmpty()) {
            wrappers.add(createSerialNumberWrapper());
        }
        return wrappers;
    }

    public SerialNumberWrapper createSerialNumberWrapper() {
        return ClientWrapperFactory.createSerialNumberWrapper(FunctionalArea.CREATE_RETURN, lineItemWrapper.getStockItem().getId(), getUINType(), getUINLabel());
    }

    public UINType getUINType() {
        if (lineItemWrapper != null) {
            return lineItemWrapper.getStockItem().getUINType();
        }
        return UINType.SERIAL;
    }

    public String getUINLabel() {
        if (lineItemWrapper != null) {
            return lineItemWrapper.getStockItem().getUINLabel();
        }
        return null;
    }

    public boolean isSerialNumbersEditable() {
        return lineItemWrapper.getStockReturn().getStatus() != ReturnStatus.DISPATCHED;
    }

    public void validateSerialNumber(SerialNumberValue serialNumber) throws Exception {
        ReturnIsValidSerialNumberRule.execute(lineItemWrapper.getStockReturn(), lineItemWrapper, serialNumber);
    }

    public void saveSerialNumbers(List<SerialNumberWrapper> wrappers) throws Exception {
        for (SerialNumberWrapper wrapper : wrappers) {
            SerialNumberValue serialNumber = wrapper.getSerialNumberValue();
            if (serialNumber != null) {
                if (wrapper.isDeleted()) {
                    lineItemWrapper.removeSerialNumber(serialNumber);
                }
                if (wrapper.isAdded()) {
                    validateSerialNumber(wrapper.getSerialNumberValue());
                    serialNumber.setDamaged(wrapper.isDamaged());
                    lineItemWrapper.addSerialNumber(serialNumber);
                }
            }
        }
        lineItemWrapper.setQuantitiesBasedOnSerialNumbers();
    }
}
