package oracle.retail.sim.client.screen.fulfillmentorderdelivery;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.screen.uin.SerialNumberWrapper;
import oracle.retail.sim.common.business.ClientCommandFactory;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryLineItem;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryStatus;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryValidateUINCommand;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINType;
import oracle.retail.sim.common.uin.UINUserAction;

/********************************************************************************************************
 * Fulfillment Order Serial Number Dialog Model
 * <p>
 * The business logic model for the Customer Order UIN Dialog.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FulfillmentOrderUinDialogModel extends SimScreenModel {

    private FulfillmentOrderDeliveryLineItemWrapper lineItemWrapper;

    /**
     * Sets the current delivery line item to the input wrapper.
     * @param wrapper The new current delivery line item.
     */
    public void setLineItemWrapper(FulfillmentOrderDeliveryLineItemWrapper wrapper) {
        lineItemWrapper = wrapper;
    }

    /**
     * Returns the CustomerOrderDeliveryLineItemWrapper representing the current
     * delivery line item.
     * @return The CustomerOrderDeliveryLineItemWrapper representing the current
     * delivery line item.
     */
    public FulfillmentOrderDeliveryLineItemWrapper getLineItemWrapper() {
        return lineItemWrapper;
    }

    /**
     * Returns the FulfillmentOrderDelivery on which serial numbers are being processed.
     * @return The FulfillmentOrderDelivery on which serial numbers are being processed.
     */
    public FulfillmentOrderDelivery getDelivery() {
        return lineItemWrapper.getDelivery();
    }

    /**
     * Returns the item id of the current line item.
     * @return The item id of the current line item.
     */
    public String getItemId() {
        if (lineItemWrapper != null) {
            return lineItemWrapper.getItemId();
        }
        return null;
    }

    /**
     * Returns the item description of the current line item.
     * @return The item description of the current line item.
     */
    public String getItemDescription() {
        return lineItemWrapper.getItemDescription();
    }

    /**
     * Returns a List of SerialNumberWrappers representing all the serial numbers currently
     * added to the current line item.
     * @return A List of SerialNumberWrappers representing all the serial numbers currently
     * added to the current line item.
     */
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

    /**
     * Creates a new Serial Number Wrapper for the current line item.
     * @return A new Serial Number Wrapper for the current line item.
     */
    public SerialNumberWrapper createSerialNumberWrapper() {
        return ClientWrapperFactory.createSerialNumberWrapper(FunctionalArea.CUSTOMER_ORDER, lineItemWrapper.getItemId(), getUINType(), getUINLabel());
    }

    /**
     * Returns the UIN Type needed for the current line item.
     * @return The UIN Type needed for the current line item.
     */
    private UINType getUINType() {
        return lineItemWrapper.getStockItem().getUINType();
    }

    /**
     * Returns the UIN label of the current line item.
     * @return The UIN label of the current line item.
     */
    public String getUINLabel() {
        if (lineItemWrapper != null) {
            return lineItemWrapper.getStockItem().getUINLabel();
        }
        return StringConstants.EMPTY;
    }

    /**
     * Returns whether or not UINs can be added to the current line item.
     * @return True if UINs can be added to the current line item, otherwise false.
     */
    public boolean isSerialNumbersEditable() {
        return lineItemWrapper.getStatus() == FulfillmentOrderDeliveryStatus.IN_PROGRESS;
    }

    /**
     * Returns whether or not the input serial number is already present on the current line item.
     * @param newSerialNumber The serial number to check if it is already present on the current line item.
     * @return True if the input serial number is already present on the current line item, otherwise false.
     */
    public boolean isDuplicateSerialNumber(SerialNumberValue newSerialNumber) {
        for (FulfillmentOrderDeliveryLineItem lineItem : lineItemWrapper.getDelivery().getLineItems()) {
            for (SerialNumberValue existingSerialNumber : lineItem.getSerialNumbers()) {
                if (existingSerialNumber.getUin().equals(newSerialNumber.getUin())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns whether the number of serial numbers of the current line item differs
     * from the current quantity on the line item.
     * @return True if the number of serial numbers and the quantity of the current
     * line item differ, otherwise false if they are equal.
     */
    public boolean isSerialNumberCountInvalid() {
        Quantity quantity = lineItemWrapper.getQuantity();
        if (quantity != null && quantity.isPositive()) {
            return lineItemWrapper.getSerialNumberCount() != quantity.intValue();
        }
        return false;
    }

    /**
     * Returns the String parameters for the confirm dialog.
     * @return The String parameters for the confirm dialog.
     */
    public String[] getConfirmValues() {
        String[] values = new String[3];
        values[0] = getUINLabel();
        values[1] = lineItemWrapper.getItemId() + " - " + lineItemWrapper.getItemDescription();
        values[2] = LocaleManager.getIntegerFormatter().format(lineItemWrapper.getSerialNumberCount());
        return values;
    }

    /**
     * Updates the quantity on the current line item to match the number of serial numbers.
     */
    public void updateLineItemQuantites() throws Exception {
        lineItemWrapper.setQtyBasedOnSerialNumbers();
    }

    /**
     * Adds and removes the input serial numbers from the current line item.
     * @param wrappers The input serial numbers to add or remove from the current line item.
     */
    public void saveSerialNumbers(List<SerialNumberWrapper> wrappers) throws Exception {
        for (SerialNumberWrapper wrapper : wrappers) {
            SerialNumberValue serialNumber = wrapper.getSerialNumberValue();
            if (serialNumber != null) {
                if (wrapper.isDeleted()) {
                    lineItemWrapper.removeSerialNumber(serialNumber);
                } else {
                    lineItemWrapper.addSerialNumber(serialNumber);
                }
            }
        }
    }

    /**
     * Validates that the input serial numbers are in a valid state to be on the current line item.
     * @param wrappers The serial numbers to validate.
     */
    public void validateSerialNumbers(List<SerialNumberWrapper> wrappers) throws Exception {
        for (SerialNumberWrapper wrapper : wrappers) {
            SerialNumberValue serialNumber = wrapper.getSerialNumberValue();
            if (serialNumber == null || wrapper.isDeleted()) {
                continue;
            }
            FulfillmentOrderDeliveryValidateUINCommand command = ClientCommandFactory.createFulfillmentOrderDeliveryValidateUINCommand();
            command.setSerialNumber(serialNumber);
            command.setDelivery(getDelivery());
            command.setUINLabel(wrapper.getUINLabel());
            if (wrapper.isAdded()) {
                command.setNewOnTransaction(true);
            } else {
                command.setNewOnTransaction(false);
            }
            command.execute();
        }
    }
}
