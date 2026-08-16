package oracle.retail.sim.client.screen.transfer;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.screen.uin.SerialNumberWrapper;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.transfer.TransferMessageText;
import oracle.retail.sim.common.transfer.TransferSerialNumber;
import oracle.retail.sim.common.transfer.TransferStatus;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINStatus;
import oracle.retail.sim.common.uin.UINType;
import oracle.retail.sim.common.uin.UINUserAction;

/********************************************************************************************************
 * The business logic model for the Transfer UIN Dialog.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferUinDialogModel extends SimScreenModel {

    private TransferLineItemWrapper lineItemWrapper;
    private FunctionalArea functionalArea;
    private MessageText message;
    private Object[] messageValues;

    public void setLineItemWrapper(TransferLineItemWrapper wrapper) {
        lineItemWrapper = wrapper;
    }

    public void setFunctionalArea(FunctionalArea functionalArea) {
        this.functionalArea = functionalArea;
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
        List<TransferSerialNumber> serialNumbers = new ArrayList<>();
        if (isReceivingMode()) {
            serialNumbers.addAll(lineItemWrapper.getReceivedSerialNumbers());
            serialNumbers.addAll(lineItemWrapper.getDamagedSerialNumbers());
        } else {
            serialNumbers.addAll(lineItemWrapper.getSerialNumbers());
        }
        List<SerialNumberWrapper> wrappers = new ArrayList<>();
        for (TransferSerialNumber serialNumber : serialNumbers) {
            SerialNumberWrapper wrapper = createSerialNumber();
            wrapper.setSerialNumberValue(serialNumber.toSerialNumberValue());
            wrapper.setDefaultAction();
            wrapper.setValidated();

            wrappers.add(wrapper);
        }
        for (TransferSerialNumber serialNumber : lineItemWrapper.getRemovedSerialNumbers()) {
            SerialNumberWrapper wrapper = createSerialNumber();
            wrapper.setSerialNumberValue(serialNumber.toSerialNumberValue());
            wrapper.setUserAction(UINUserAction.REMOVED);
            wrapper.setValidated();

            wrappers.add(wrapper);
        }
        return wrappers;
    }

    public SerialNumberWrapper createSerialNumber() {
        return ClientWrapperFactory.createSerialNumberWrapper(functionalArea, lineItemWrapper.getStockItem().getId(), getUINType(), getUINLabel());
    }

    private UINType getUINType() {
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
        TransferStatus status = lineItemWrapper.getStatus();
        if (isDispatchMode() && status == TransferStatus.IN_PROGRESS) {
            return lineItemWrapper.getTransfer().getSendingStore().equals(SimRepository.getStore());
        }
        if (isReceivingMode() && (status == TransferStatus.DISPATCHED || status == TransferStatus.RECEIVING)) {
            if (lineItemWrapper.getTransfer().getReceivingStore().equals(SimRepository.getStore())) {
                if (lineItemWrapper.getStockItem().isAgsnEnabled()) {
                    return lineItemWrapper.getShippedSerialNumbers().size() > 0;
                }
                return true;
            }
        }
        return false;
    }

    public void removeSerialNumber(SerialNumberWrapper wrapper) throws BusinessException {
        if (lineItemWrapper.getTransfer().isAdjustReceivedTransferMode()) {
            if (!UINStatus.getValidSetForTransfersReceipt().contains(wrapper.getSerialNumberValue().getStatus()) && !isAllowUnexpectedUINs()) {
                createUINStatusError(wrapper, TransferMessageText.UIN_CANNOT_BE_REMOVED);
                throw new BusinessException(message, messageValues);
            }
        }
        wrapper.setUserAction(UINUserAction.REMOVED);
    }

    public void saveSerialNumbers(List<SerialNumberWrapper> wrappers) throws Exception {
        List<SerialNumberWrapper> validatedWrappers = new ArrayList<>();
        for (SerialNumberWrapper wrapper : wrappers) {
            if (wrapper != null && wrapper.getSerialNumberValue() != null) {
                validatedWrappers.add(wrapper);
            }
        }
        if (isDispatchMode()) {
            lineItemWrapper.updateShippedSerialNumbers(validatedWrappers);
        } else if (isReceivingMode()) {
            lineItemWrapper.updateReceivedSerialNumbers(validatedWrappers);
        }
    }

    public TransferMessageText getSerialNumberCountInvalidMessage() {
        if (isReceivingMode()) {
            Quantity shippedQty = lineItemWrapper.getTransferQuantity();
            if (shippedQty != null && shippedQty.isPositive()) {
                int receivedSerialNumberCount = lineItemWrapper.getSerialNumberCount();
                if (lineItemWrapper.getReceivedQuantity() != null) {
                    if (receivedSerialNumberCount == lineItemWrapper.getReceivedQuantity().intValue()) {
                        return null;
                    }
                }
                if (receivedSerialNumberCount != shippedQty.intValue()) {
                    return TransferMessageText.UIN_RECEIVE_QTY_MISMATCH;
                }
            }
        }
        if (isDispatchMode()) {
            Quantity approvedQty = lineItemWrapper.getApprovedQuantity();
            if (approvedQty != null && approvedQty.isPositive()) {
                if (lineItemWrapper.getSerialNumberCount() != approvedQty.intValue()) {
                    return TransferMessageText.UIN_DISPATCH_QTY_MISMATCH;
                }
            }
        }
        return null;
    }

    public boolean isSerialNumberShipped(SerialNumberValue serialNumber) {
        for (TransferSerialNumber transferSerialNumber : lineItemWrapper.getShippedSerialNumbers()) {
            if (transferSerialNumber.getUin().equals(serialNumber.getUin())) {
                return true;
            }
        }
        return false;
    }

    public void updateLineItemQuantites() throws Exception {
        if (isDispatchMode()) {
            lineItemWrapper.setShippedQtyBasedOnSerialNumbers();
        } else if (isReceivingMode()) {
            lineItemWrapper.setReceivedQtyBasedOnSerialNumbers();
        }
    }

    public String[] getConfirmValues() {
        String[] values = new String[3];
        values[0] = getUINLabel();
        values[1] = lineItemWrapper.getStockItem().getId() + " - " + lineItemWrapper.getDescription();
        values[2] = LocaleManager.getIntegerFormatter().format(lineItemWrapper.getSerialNumberCount());
        return values;
    }

    public boolean isUnexpectedSerialNumberAllowed() {
        return SimConfigManager.getBoolean(SimConfigManager.ALLOW_UNEXPECTED_UINS);
    }

    public FunctionalArea getFunctionalArea() {
        return functionalArea;
    }

    public boolean isReceivingMode() {
        return functionalArea == FunctionalArea.RECEIVE_TRANSFER;
    }

    public boolean isDispatchMode() {
        return functionalArea == FunctionalArea.CREATE_TRANSFER || functionalArea == FunctionalArea.DISPATCH_TRANSFER;
    }

    private void createUINStatusError(SerialNumberWrapper wrapper, MessageText error) {
        message = error;
        messageValues = new Object[2];
        messageValues[0] = wrapper.getSerialNumberValue().getUin();
        messageValues[1] = wrapper.getSerialNumberValue().getStatus().toString();
    }
}