package extra.retail.sim.client.screen.transfer;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.screen.transfer.TransferLineItemWrapper;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.table.SimTableResetFocusException;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.shipment.BillOfLadingMotive;
import oracle.retail.sim.common.shipment.ShipmentCarrier;
import oracle.retail.sim.common.shipment.ShipmentCarrierRole;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferLineItem;
import oracle.retail.sim.common.transfer.TransferProperty;
import oracle.retail.sim.common.transfer.TransferPropertyModifiableRule;
import oracle.retail.sim.common.transfer.TransferSerialNumber;
import oracle.retail.sim.common.transfer.TransferStatus;
import oracle.retail.sim.common.transfer.TransferValidateUinCommand;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.service.core.ClientServiceFactory;

import extra.retail.sim.client.screen.shipTrailer.DMShipTrailer;
import extra.retail.sim.webservice.shipTrailer.client.DMShipTrailerCaptureClient;

/********************************************************************************************************
 * Transfer Create Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraTransferCreateModel extends ExtraTransferDetailModel {

    /****************************************************************************************************
     * Basic Load Data Methods
     * @throws Exception 
     * @throws BusinessException 
     ***************************************************************************************************/

    public void loadTransfer() throws Exception {
        transfer = (Transfer) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_TRANSFER);
        if (transfer == null) {
            transfer = BOFactory.createTransfer();
            transfer.doSetSendingStore(getStore());
            transfer.doSetCreateStoreId(getStoreId());
            transfer.doSetCreateUser(getUserName());
            transfer.doSetStatus(TransferStatus.IN_PROGRESS);
            ShipmentCarrierRole carrierRole = getDefaultCarrierRole();
            transfer.getBillOfLading().doSetCarrierRole(carrierRole);
            if (carrierRole == ShipmentCarrierRole.THIRD_PARTY) {
                transfer.getBillOfLading().doSetCarrier(getOtherCarrier());
            }
            transfer.getBillOfLading().doSetMotiveId(BillOfLadingMotive.TRANSFER_MOTIVE_ID);
            transfer.getBillOfLading().doSetWeightUom(getWeightUom());
            transfer.getBillOfLading().doSetDirty();
            RepositoryManager.addStateObject(SimClientStateKey.SELECTED_TRANSFER, transfer);
        }
    }

    private ShipmentCarrierRole getDefaultCarrierRole() {
        return ShipmentCarrierRole.toValue(SimConfigManager.getStoreString(StoreConfigKeys.STORE_TO_STORE_CARRIER_DEFAULT, getStoreId()));
    }

    private ShipmentCarrier getOtherCarrier() throws Exception {
        return ClientServiceFactory.getShipmentServices().findCarrier(ShipmentCarrier.OTHER_CARRIER_CODE);
    }

    private String getWeightUom() {
        return SimConfigManager.getStoreString(StoreConfigKeys.MANIFEST_WEIGHT_UOM, getStoreId());
    }

    /****************************************************************************************************
     * Basic Validate Methods
     ***************************************************************************************************/

    public boolean isReceivingStoreModifiable() {
        return transfer.getLineItems().isEmpty();
    }

    public boolean isLineItemsModifiable() {
        return TransferPropertyModifiableRule.isPropertyModifiable(transfer, TransferProperty.LINE_ITEM);
    }

    public boolean isAddLineAvailable() {
        return hasPermission(PermissionKey.PC_ADD_ITEM_TRANSFER);
    }

    public boolean isEmptyTransfer() {
        return transfer.getId() != null && transfer.getLineItems().isEmpty();
    }

    /****************************************************************************************************
     * SCANNER METHODS
     ***************************************************************************************************/

    public boolean isScannerAvailable() {
        return true;
    }

    public void updateExistingLineItem(TransferLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        if (wrapper.getStockItem() != null) {
            if (barcodeItem.isSerialNumberRequired()) {
                updateExistingLineItemUin(wrapper, barcodeItem);
            } else {
                updateExistingLineItemQty(wrapper, barcodeItem);
            }
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
            throw new BusinessException(CommonMessageText.NO_UIN_CAPTURED, uinLabel);
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
        command.setFunctionalArea(FunctionalArea.CREATE_TRANSFER);
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

    public void removeLineItem(TransferLineItemWrapper wrapper) throws BusinessException {
        transfer.removeLineItem(wrapper.getLineItem());
    }

    /****************************************************************************************************
     * Cancel Transfer
     ***************************************************************************************************/

    public void cancelTransfer() throws Exception {
        if (transfer.isNew()) {
            return;
        }
        ClientServiceFactory.getTransferServices().cancelTransfer(getStoreId(), transfer.getId());
    }

    /****************************************************************************************************
     * Return true if the transfer contains no quantities - specialized method for creation.
     ***************************************************************************************************/

    public boolean containsNoTransferQuantities() {
    	transfer.defaultMissingTransferQuantityToZero();
        for (TransferLineItem lineItem : transfer.getLineItems()) {
            if (!lineItem.getTransferQuantityOrZero().isZero()) {
                return false;
            }
        }
        return true;
    }

    /****************************************************************************************************
     * Update Transfer
     ***************************************************************************************************/
    public void updateTransfer() throws Exception {
        transfer.validateIsCoherent();
        transfer.defaultMissingTransferQuantityToZero();
        if (transfer.isNew()) {
        	Long transferId = ClientServiceFactory.getTransferServices().insertTransfer(transfer);
            DMShipTrailer shipTrailer = (DMShipTrailer) RepositoryManager.getStateObject(SimClientStateKey.SHIP_TRAILER_OBJECT);
            if (shipTrailer != null) {
            	shipTrailer.setTransferReturnId(transferId);
            	shipTrailer.setTransfer(true);
            	shipTrailer.setCreatedUser(getUser().getId().toString());
            	DMShipTrailerCaptureClient.getInstance().savaShipTrailer(shipTrailer);
            	RepositoryManager.removeStateObject(SimClientStateKey.SHIP_TRAILER_OBJECT);
            }
        } else {
            ClientServiceFactory.getTransferServices().updateTransfer(transfer);
        }
        RepositoryManager.addStateObject(SimClientStateKey.TRANSFER_DETAIL_MODIFIED, Boolean.TRUE);
    }

	public boolean validateShipTrailer() {
		return !isShipTrailerEnabled() || transfer.getId() != null || RepositoryManager.getStateObject(SimClientStateKey.SHIP_TRAILER_OBJECT) != null;
	}

	public boolean isShipTrailerEnabled() {
		return DMShipTrailerCaptureClient.getInstance().isShipTrailerEnabled(getStoreId());
	}
}
