package oracle.retail.sim.client.screen.transfer;

import java.util.ArrayList;
import java.util.List;

import extra.retail.sim.webservice.shipTrailer.client.DMShipTrailerCaptureClient;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.table.SimTableResetFocusException;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferLineItem;
import oracle.retail.sim.common.transfer.TransferMessageText;
import oracle.retail.sim.common.transfer.TransferProperty;
import oracle.retail.sim.common.transfer.TransferPropertyModifiableRule;
import oracle.retail.sim.common.transfer.TransferSerialNumber;
import oracle.retail.sim.common.transfer.TransferStatus;
import oracle.retail.sim.common.transfer.TransferValidateUinCommand;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Transfer Dispatch Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferDispatchModel extends TransferDetailModel {

    /****************************************************************************************************
     * Basic Load Data Methods
     ***************************************************************************************************/

    public void loadTransfer() {
        transfer = (Transfer) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_TRANSFER);
    }

    /****************************************************************************************************
     * Basic Validate Methods
     ***************************************************************************************************/

    public boolean isLineItemsModifiable() {
        return TransferPropertyModifiableRule.isPropertyModifiable(transfer, TransferProperty.LINE_ITEM);
    }

    public boolean isRequestedQuantityDisplayable() {
        return transfer != null && hasRequestedQuantity();
    }

    public boolean isApprovedQuantityDisplayable() {
        return transfer != null && hasRequestedQuantity();
    }

    public boolean isShippedQuantityDisplayable() {
        return transfer != null && transfer.getStatus() == TransferStatus.IN_PROGRESS;
    }

    public boolean isEmptyTransfer() {
        return transfer.getId() != null && transfer.getLineItems().isEmpty();
    }

    public boolean isContextTypePromotional() {
        ContextType contextType = transfer.getContextType();
        return contextType != null && contextType.isPromotion();
    }

    /****************************************************************************************************
     * SCANNER METHODS
     ***************************************************************************************************/

    public boolean isScannerAvailable() {
        return isLineItemsModifiable();
    }

    public void updateExistingLineItem(TransferLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        if (barcodeItem.isSerialNumberRequired()) {
            updateExistingLineItemUin(wrapper, barcodeItem);
        } else {
            updateExistingLineItemQty(wrapper, barcodeItem);
        }
    }

    private void updateExistingLineItemQty(TransferLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        if (barcodeItem.getQuantity().isZero()) {
            throw new UIException(CommonMessageText.NO_QUANTITY_APPLIED, RErrorSeverity.WARNING);
        }
        try {
            if (wrapper.isCasesMode() && wrapper.isEachesStandardUnitOfMeasure()) {
                wrapper.setTransferQuantity(wrapper.getTransferQuantityOrZero().add(barcodeItem.getQuantity().multiply(wrapper.getCaseSize())));
            } else {
                wrapper.setTransferQuantity(wrapper.getTransferQuantityOrZero().add(barcodeItem.getQuantity()));
            }
        } catch (SimTableResetFocusException exception) {
            UILog.debug(getClass(), exception);
        }
    }

    private void updateExistingLineItemUin(TransferLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        String uinLabel = barcodeItem.getStockItem().getUINLabel();
        if (StringHelper.isNullOrEmpty(barcodeItem.getUin())) {
            throw new UIException(CommonMessageText.NO_UIN_CAPTURED, uinLabel);
        }

        SerialNumberValue serialNumber = ClientServiceFactory.getUINServices().findSerialNumberValue(barcodeItem.getId(), barcodeItem.getUin());
        if (serialNumber == null) {
            Object[] values = new Object[3];
            values[0] = uinLabel;
            values[1] = barcodeItem.getUin();
            values[2] = barcodeItem.getId();
            throw new BusinessException(UINMessageText.UIN_NOT_FOUND_FOR_ITEM, values);
        }
        for (TransferSerialNumber testSerialNumber : wrapper.getRemovedSerialNumbers()) {
            if (testSerialNumber.getUin().equals(serialNumber.getUin())) {
                wrapper.addSerialNumber(testSerialNumber);
                wrapper.setShippedQtyBasedOnSerialNumbers();
                return;
            }
        }
        if (isDuplicateSerialNumber(barcodeItem.getId(), serialNumber)) {
            throw new BusinessException(UINMessageText.UIN_ALREADY_ENTERED, uinLabel);
        }

        TransferValidateUinCommand command = new TransferValidateUinCommand();
        command.setFunctionalArea(FunctionalArea.DISPATCH_TRANSFER);
        command.setStoreId(getStoreId());
        command.setUINLabel(uinLabel);
        command.setNewOnTransaction(true);
        command.setSerialNumber(serialNumber);
        command.execute();

        wrapper.addSerialNumber(serialNumber);
        wrapper.setShippedQtyBasedOnSerialNumbers();
    }

    private boolean isDuplicateSerialNumber(String itemId, SerialNumberValue newSerialNumber) {
        for (TransferLineItem lineItem : transfer.getLineItems()) {
            if (lineItem.getStockItem().getId().equals(itemId)) {
                for (TransferSerialNumber existingSerialNumber : lineItem.getSerialNumbers()) {
                    if (existingSerialNumber.getUinId().equals(newSerialNumber.getUinId())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /****************************************************************************************************
     * Line Item Methods
     ***************************************************************************************************/

    public boolean removeLineItem(TransferLineItemWrapper wrapper) throws BusinessException {
        TransferLineItem lineItem = wrapper.getLineItem();
        Quantity requestedQuantity = lineItem.getRequestedQuantity();
        List<String> serialNumbers = new ArrayList<String>();
        for (TransferSerialNumber serialNumber : lineItem.getSerialNumbers()) {
            serialNumbers.add(serialNumber.getUin());
        }
        for (int i = 0; i < serialNumbers.size(); i++) {
            lineItem.removeSerialNumber(serialNumbers.get(i));
        }
        if (requestedQuantity == null || requestedQuantity.isZero()) {
            transfer.removeLineItem(lineItem);
            return true;
        }
        lineItem.doSetTransferQuantity(Quantity.ZERO);
        lineItem.doSetDirty();
        return false;
    }

    public void assignDefaultShippedQuantities() {
        for (TransferLineItem lineItem : transfer.getLineItems()) {
            lineItem.doSetTransferQuantity(lineItem.getApprovedQuantity());
            lineItem.doSetDirty();
        }
    }

    /****************************************************************************************************
     * Update Transfer
     ***************************************************************************************************/

    public void updateTransfer() throws Exception {
        transfer.validateIsCoherent();
        transfer.defaultMissingTransferQuantityToZero();
        ClientServiceFactory.getTransferServices().updateTransfer(transfer);
        clearTransferLock();
        RepositoryManager.addStateObject(SimClientStateKey.TRANSFER_DETAIL_MODIFIED, Boolean.TRUE);
    }

    /****************************************************************************************************
     * Cancel Transfer
     ***************************************************************************************************/

    public void cancelTransfer() throws Exception {
        ClientServiceFactory.getTransferServices().cancelTransfer(getStoreId(), transfer.getId());
        RepositoryManager.addStateObject(SimClientStateKey.TRANSFER_DETAIL_MODIFIED, Boolean.TRUE);
    }

    /****************************************************************************************************
     * Validates if atleast one line item has non zero quantity
     ***************************************************************************************************/

    public boolean validateHasQuantity() throws Exception {
        boolean hasNoQuantity = true;
        transfer.defaultMissingTransferQuantityToZero();
        for (TransferLineItem lineItem : transfer.getLineItems()) {
            if (!lineItem.getTransferQuantityOrZero().isZero()) {
                hasNoQuantity = false;
                break;
            }
        }
        if (hasNoQuantity && transfer.isRequestedBySendingStore()) {
            throw new BusinessException(TransferMessageText.AT_LEAST_ONE_NONZERO_QUANTITY_REQUIRED);
        }
        return true;
    }

    /*******************************************************************
     * helper methods
     * *****************************************************************/

    public boolean hasDispatchedMoreThanRequested() {
        return transfer.isFulfillmentOrderRelated() && transfer.isShipQtyLargerThanRequested();
    }

    public boolean hasDispatchPartialQuantities() {
        return transfer.isFulfillmentOrderRelated() && transfer.hasShipPartialOrderQuantities();
    }

    public void validateDispatchAllowed() throws BusinessException {
        transfer.validateDispatchAllowed(isDispatchSubmitRequired());
    }

    public boolean isValidForSubmit() throws BusinessException {
        if (transfer.isCoherent()) {
            TransferStatus status = transfer.getStatus();
            return status != TransferStatus.DISPATCHED && status != TransferStatus.CANCELED_TRANSFER && status != TransferStatus.SUBMITTED;
        }
        return false;
    }

    public boolean isValidForCancelSubmit() {
        return transfer.getStatus() == TransferStatus.SUBMITTED;
    }

	public boolean hasShipTrailer() {
		return !isShipTrailerEnabled() || DMShipTrailerCaptureClient.getInstance().hasShipTrailer(transfer.getId());
	}

	public boolean isShipTrailerEnabled() {
		return DMShipTrailerCaptureClient.getInstance().isShipTrailerEnabled(getStoreId());
	}
}
