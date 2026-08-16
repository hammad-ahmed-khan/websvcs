package oracle.retail.sim.client.screen.transfer;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.item.BarcodeItem;
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
import oracle.retail.sim.common.uin.UINStatus;
import oracle.retail.sim.common.uin.UINType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Transfer Receive Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferReceiveModel extends TransferDetailModel {

    private int expectedLineCount = -1;
    private int receivedLineCount = -1;
    private int damagedLineCount = -1;
    private int discrepantLineCount = -1;

    /****************************************************************************************************
     * Basic Load Data Methods
     ***************************************************************************************************/

    public void loadTransfer() throws BusinessException {
        transfer = (Transfer) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_TRANSFER);
        transfer.setStatus(TransferStatus.RECEIVING);
    }

    /****************************************************************************************************
     * Basic Validate Methods
     ***************************************************************************************************/

    public boolean isReceiptAdjustmentMode() {
        return transfer.isAdjustReceivedTransferMode();
    }

    public boolean isAddItemAvailable() {
        return SimConfigManager.getBoolean(SimConfigManager.ADD_ITEM_TO_TRANSFER_ON_RECEIVE);
    }

    public boolean isLineItemsModifiable() {
        return TransferPropertyModifiableRule.isPropertyModifiable(transfer, TransferProperty.LINE_ITEM);
    }

    public boolean isReceiveEntireTransferOnly() {
        return SimConfigManager.getBoolean(SimConfigManager.RECEIVE_ENTIRE_TRANSFER);
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
        if (barcodeItem.isDamaged()) {
            if (wrapper.isCasesMode() && wrapper.isEachesStandardUnitOfMeasure()) {
                wrapper.setDamagedQuantity(wrapper.getDamagedQuantityOrZero().add(barcodeItem.getQuantity().multiply(wrapper.getCaseSize())));
            } else {
                wrapper.setDamagedQuantity(wrapper.getDamagedQuantityOrZero().add(barcodeItem.getQuantity()));
            }
        } else {
            if (wrapper.isCasesMode() && wrapper.isEachesStandardUnitOfMeasure()) {
                wrapper.setReceivedQuantity(wrapper.getReceivedQuantityOrZero().add(barcodeItem.getQuantity().multiply(wrapper.getCaseSize())));
            } else {
                wrapper.setReceivedQuantity(wrapper.getReceivedQuantityOrZero().add(barcodeItem.getQuantity()));
            }
        }
    }

    private void updateExistingLineItemUin(TransferLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        String uin = barcodeItem.getUin();
        String uinLabel = barcodeItem.getStockItem().getUINLabel();
        UINType uinType = barcodeItem.getStockItem().getUINType();
        String itemId = barcodeItem.getId();

        if (StringHelper.isNullOrEmpty(uin)) {
            if (uinType == UINType.AGSN && wrapper.getShippedSerialNumbers().isEmpty()) {
                updateExistingLineItemQty(wrapper, barcodeItem);
                return;
            }
            throw new UIException(CommonMessageText.NO_UIN_CAPTURED, uinLabel);
        }

        SerialNumberValue serialNumber = ClientServiceFactory.getUINServices().findSerialNumberValue(barcodeItem.getId(), uin);
        if (serialNumber == null) {
            serialNumber = ClientServiceFactory.getUINServices().findSerialNumberValueOrCreate(getStoreId(), itemId, uin, uinType, FunctionalArea.RECEIVE_TRANSFER);
        }
        if (serialNumber == null) {
            Object[] values = new Object[] { uinLabel, uin, itemId };
            throw new BusinessException(UINMessageText.UIN_NOT_FOUND_FOR_ITEM, values);
        }

        // Basic Validation
        TransferValidateUinCommand command = new TransferValidateUinCommand();
        command.setFunctionalArea(FunctionalArea.RECEIVE_TRANSFER);
        command.setStoreId(getStoreId());
        command.setUINLabel(uinLabel);
        command.setNewOnTransaction(true);
        command.setSerialNumber(serialNumber);
        command.execute();

        // Expected On Transfer
        TransferSerialNumber transferSerialNumber = findExistingSerialNumbers(wrapper, serialNumber);
        if (transferSerialNumber != null) {
            if (transferSerialNumber.isReceived() && !barcodeItem.isDamaged()) {
                throw new BusinessException(UINMessageText.UIN_ALREADY_ENTERED, uinLabel);
            }
            if (transferSerialNumber.isDamaged() && barcodeItem.isDamaged()) {
                throw new BusinessException(UINMessageText.UIN_ALREADY_ENTERED, uinLabel);
            }
            transferSerialNumber.setReceived(true);
            transferSerialNumber.setDamaged(barcodeItem.isDamaged());
            wrapper.setReceivedQtyBasedOnSerialNumbers();
            return;
        }

        // Shipped to Another store
        if (serialNumber.getStatus() == UINStatus.SHIPPED_TO_STORE) {
            throw new BusinessException(TransferMessageText.SHIPPED_TO_OTHER_STORE, uinLabel);
        }

        // In Stock At Another Store
        if (serialNumber.getStatus() == UINStatus.IN_STOCK) {
            if (serialNumber.getStoreId().equals(getStoreId())) {
                Object[] values = new String[] { uinLabel, serialNumber.getUin(), serialNumber.getStatus().toString() };
                throw new BusinessException(UINMessageText.UIN_CANNOT_BE_RECEIVED, values);
            }
            if (!SimConfigManager.getBoolean(SimConfigManager.ALLOW_UNEXPECTED_UINS)) {
                throw new BusinessException(UINMessageText.UIN_UNEXPECTED);
            }
            if (!RConfirmUtility.confirm("Confirmation", UINMessageText.MOVE_STORE_CONFIRM, uinLabel)) {
                return;
            }
        }

        // Create The Transfer Serial Number
        transferSerialNumber = new TransferSerialNumber();
        transferSerialNumber.doSetUin(serialNumber.getUin());
        transferSerialNumber.doSetStatus(serialNumber.getStatus());
        transferSerialNumber.doSetNonSellableQtyTypeId(serialNumber.getNonSellableQtyTypeId());
        transferSerialNumber.doSetStoreId(serialNumber.getStoreId());
        transferSerialNumber.doSetUinId(serialNumber.getUinId());
        transferSerialNumber.setShipped(false);
        transferSerialNumber.setReceived(true);
        transferSerialNumber.setDamaged(barcodeItem.isDamaged());

        wrapper.addSerialNumber(transferSerialNumber);
        wrapper.setReceivedQtyBasedOnSerialNumbers();
    }

    /** Serial both shipped and current received serial numbers for an existing serial number */
    private TransferSerialNumber findExistingSerialNumbers(TransferLineItemWrapper wrapper, SerialNumberValue newSerialNumber) {
        for (TransferSerialNumber existingSerialNumber : wrapper.getLineItem().getShippedSerialNumbers()) {
            if (existingSerialNumber.getUinId().equals(newSerialNumber.getUinId())) {
                return existingSerialNumber;
            }
        }
        for (TransferSerialNumber existingSerialNumber : wrapper.getLineItem().getSerialNumbers()) {
            if (existingSerialNumber.getUinId().equals(newSerialNumber.getUinId())) {
                return existingSerialNumber;
            }
        }
        return null;
    }

    /****************************************************************************************************
     * Line Count Summary Information
     ***************************************************************************************************/

    public Integer getExpectedLineCount() {
        if (expectedLineCount == -1) {
            calculateLineCounts();
        }
        return expectedLineCount;
    }

    public Integer getReceivedLineCount() {
        if (receivedLineCount == -1) {
            calculateLineCounts();
        }
        return receivedLineCount;
    }

    public Integer getDamagedLineCount() {
        if (damagedLineCount == -1) {
            calculateLineCounts();
        }
        return damagedLineCount;
    }

    public Integer getDiscrepantLineCount() {
        if (discrepantLineCount == -1) {
            calculateLineCounts();
        }
        return discrepantLineCount;
    }

    private void calculateLineCounts() {
        expectedLineCount = 0;
        receivedLineCount = 0;
        damagedLineCount = 0;
        discrepantLineCount = 0;

        for (TransferLineItem lineItem : transfer.getLineItems()) {
            if (lineItem.getTransferQuantityOrZero().doubleValue() > 0.0) {
                expectedLineCount++;
            }
            if (lineItem.getReceivedQuantityOrZero().doubleValue() > 0.0) {
                receivedLineCount++;
            }
            if (lineItem.getDamagedQuantityOrZero().doubleValue() > 0.0) {
                damagedLineCount++;
            }
            if (lineItem.getReceivedQuantity() != null || lineItem.getDamagedQuantity() != null) {
                if (lineItem.getTransferQuantityOrZero().compareTo(lineItem.getReceivedQuantityOrZero().add(lineItem.getDamagedQuantityOrZero())) != 0) {
                    discrepantLineCount++;
                }
            }
        }
    }

    /****************************************************************************************************
     * Basic Validation Methods
     ***************************************************************************************************/

    public boolean isRequestedQuantityDisplayable() {
        return transfer != null && hasRequestedQuantity();
    }

    public boolean isApprovedQuantityDisplayable() {
        return transfer != null && hasRequestedQuantity();
    }

    public boolean isDamagedQuantityDisplayable() {
        return transfer != null && !isReceiveEntireTransferOnly();
    }

    public void validateAddLineItemAllowed(List<TransferLineItemWrapper> wrappers) throws BusinessException {
        for (TransferLineItemWrapper wrapper : wrappers) {
            if (wrapper.getLineItem().getId() == null) {
                if (wrapper.getReceivedQuantity() == null) {
                    if (wrapper.getDamagedQuantity() == null) {
                        throw new BusinessException(TransferMessageText.MISSING_RECEIVED_QUANTITY);
                    }
                }
            }
        }
    }

    /****************************************************************************************************
     * Remove Line Item
     ***************************************************************************************************/

    public boolean removeLineItem(TransferLineItemWrapper wrapper) throws BusinessException {
        TransferLineItem lineItem = wrapper.getLineItem();
        if (lineItem == null) {
            return true;
        }
        if (lineItem.getStockItem().isSerialNumberRequired()) {
            List<TransferSerialNumber> serialNumbers = new ArrayList<>(lineItem.getSerialNumbers());
            for (TransferSerialNumber serialNumber : serialNumbers) {
                if (wrapper.getTransfer().isAdjustReceivedTransferMode()) {
                    if (!UINStatus.getValidSetForTransfersReceipt().contains(serialNumber.getStatus()) && !isAllowUnexpectedUINs()) {
                        Object[] errorValues = { serialNumber.getUin(), serialNumber.getStatus().toString() };
                        throw new BusinessException(TransferMessageText.UIN_CANNOT_BE_REMOVED, errorValues);
                    }
                }
                lineItem.removeSerialNumber(serialNumber.getUin());
            }
        }
        lineItem.doSetReceivedQuantity(Quantity.ZERO);
        lineItem.doSetDamagedQuantity(Quantity.ZERO);
        lineItem.doSetDirty();

        if (lineItem.getTransferQuantityOrZero().isZero()) {
            transfer.removeLineItem(lineItem);
            return true;
        }
        return false;
    }

    /****************************************************************************************************
     * Update Transfer
     ***************************************************************************************************/

    public void updateTransfer() throws Exception {
        transfer.defaultMissingReceivedQuantityToZero();
        transfer.validateIsCoherent();
        ClientServiceFactory.getTransferServices().updateTransfer(transfer);
        RepositoryManager.addStateObject(SimClientStateKey.TRANSFER_DETAIL_MODIFIED, Boolean.TRUE);
    }

    /****************************************************************************************************
     * Receive Transfer
     ***************************************************************************************************/

    public void receiveTransfer() throws Exception {
        transfer.defaultMissingReceivedQuantityToZero();
        transfer.validateIsCoherent();
        ClientServiceFactory.getTransferServices().receiveTransfer(transfer);
        RepositoryManager.addStateObject(SimClientStateKey.TRANSFER_DETAIL_MODIFIED, Boolean.TRUE);
    }
}
