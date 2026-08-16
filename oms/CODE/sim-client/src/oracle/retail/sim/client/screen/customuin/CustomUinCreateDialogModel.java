package oracle.retail.sim.client.screen.customuin;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.screen.uin.SerialNumberWrapper;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINType;

/********************************************************************************************************
 * The business logic model for the Custom UIN Create Dialog.
 * <p>
 * This is future work-in-progress code for future SIM 14.0 or later release.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CustomUinCreateDialogModel extends SimScreenModel {
    private CustomUINCreateWrapper lineItemWrapper;

    public void setWrapper(CustomUINCreateWrapper wrapper) {
        lineItemWrapper = wrapper;
    }

    public String getItemId() {
        if (lineItemWrapper != null) {
            return lineItemWrapper.getStockItem().getId();
        }
        return null;
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

    public List<SerialNumberWrapper> getSerialNumberWrappers() {
        List<SerialNumberWrapper> wrappers = new ArrayList<>();
        for (SerialNumberValue value : lineItemWrapper.getSerialNumbers()) {
            SerialNumberWrapper wrapper = createSerialNumberWrapper();
            wrapper.setSerialNumberValue(value);
            wrapper.setDefaultAction();
            wrapper.setValidated();

            wrappers.add(wrapper);
        }
        if (wrappers.isEmpty()) {
            wrappers.add(createSerialNumberWrapper());
        }
        return wrappers;
    }

    public SerialNumberWrapper createSerialNumberWrapper() {
        return ClientWrapperFactory.createSerialNumberWrapper(FunctionalArea.MANUAL, lineItemWrapper.getStockItem().getId(), getUINType(), getUINLabel());
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

    public void saveSerialNumbers(List<SerialNumberWrapper> wrappers) {
        List<SerialNumberValue> serialNumbers = new ArrayList<>();
        for (SerialNumberWrapper wrapper : wrappers) {
            SerialNumberValue value = wrapper.getSerialNumberValue();
            if (value != null) {
                serialNumbers.add(value);
            }
        }
        lineItemWrapper.setSerialNumbers(serialNumbers);
    }
}
