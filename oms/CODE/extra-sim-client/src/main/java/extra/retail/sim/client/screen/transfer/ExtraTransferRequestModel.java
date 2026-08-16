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
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.shipment.BillOfLadingMotive;
import oracle.retail.sim.common.shipment.ShipmentCarrier;
import oracle.retail.sim.common.shipment.ShipmentCarrierRole;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferLineItem;
import oracle.retail.sim.common.transfer.TransferProperty;
import oracle.retail.sim.common.transfer.TransferPropertyModifiableRule;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Transfer Request Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraTransferRequestModel extends ExtraTransferDetailModel {

	    /****************************************************************************************************
     * Basic Load Data Methods
     * @throws Exception 
     ***************************************************************************************************/

    public void loadTransfer() throws Exception {
        transfer = (Transfer) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_TRANSFER);

        if (transfer == null) {
            transfer = BOFactory.createTransfer();
            transfer.doSetReceivingStore(getStore());
            transfer.doSetCreateStoreId(getStoreId());
            transfer.doSetCreateUser(getUserName());
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
    
    private String getWeightUom() {
        return SimConfigManager.getStoreString(StoreConfigKeys.MANIFEST_WEIGHT_UOM, getStoreId());
    }

    private ShipmentCarrierRole getDefaultCarrierRole() {
        return ShipmentCarrierRole.toValue(SimConfigManager.getStoreString(StoreConfigKeys.STORE_TO_STORE_CARRIER_DEFAULT, getStoreId()));
    }

    private ShipmentCarrier getOtherCarrier() throws Exception {
        return ClientServiceFactory.getShipmentServices().findCarrier(ShipmentCarrier.OTHER_CARRIER_CODE);
    }

    public boolean isSendingStoreModifiable() {
        return transfer.getLineItems().isEmpty();
    }

    /****************************************************************************************************
     * Line Item Methods
     ***************************************************************************************************/

    public void removeLineItem(TransferLineItemWrapper wrapper) throws BusinessException {
        TransferLineItem lineItem = wrapper.getLineItem();
        if (lineItem != null) {
            transfer.removeLineItem(lineItem);
        }
    }

    public boolean isLineItemsModifiable() {
        return TransferPropertyModifiableRule.isPropertyModifiable(transfer, TransferProperty.LINE_ITEM);
    }

    public boolean isAddLineAvailable() {
        return hasPermission(PermissionKey.PC_ADD_ITEM_TRANSFER_REQUEST);
    }

    /****************************************************************************************************
     * Scanner Methods
     ***************************************************************************************************/

    public boolean isScannerAvailable() {
        return true;
    }

    public void updateExistingLineItem(TransferLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        if (wrapper.getStockItem() == null || barcodeItem.getQuantity().isZero()) {
            throw new UIException(CommonMessageText.NO_QUANTITY_APPLIED, RErrorSeverity.WARNING);
        }
        try {
            if (wrapper.isCasesMode() && wrapper.isEachesStandardUnitOfMeasure()) {
                wrapper.setRequestedQuantity(wrapper.getRequestedQuantityOrZero().add(barcodeItem.getQuantity().multiply(wrapper.getCaseSize())));
            } else {
                wrapper.setRequestedQuantity(wrapper.getRequestedQuantityOrZero().add(barcodeItem.getQuantity()));
            }
        } catch (SimTableResetFocusException exception) {
            UILog.debug(getClass(), exception);
        }
    }

    /****************************************************************************************************
     * Update Transfer
     ***************************************************************************************************/

    public void updateTransfer() throws Exception {
        transfer.validateIsCoherent();
        if (transfer.isNew()) {
            ClientServiceFactory.getTransferServices().insertTransfer(transfer);
        } else {
            ClientServiceFactory.getTransferServices().updateTransfer(transfer);
        }
        RepositoryManager.addStateObject(SimClientStateKey.TRANSFER_DETAIL_MODIFIED, Boolean.TRUE);
    }

    /****************************************************************************************************
     * Request Transfer
     ***************************************************************************************************/

    public void requestTransfer() throws Exception {
        transfer.validateRequestAllowed();
        ClientServiceFactory.getTransferServices().requestTransfer(transfer);
        clearTransferLock();
        RepositoryManager.addStateObject(SimClientStateKey.TRANSFER_DETAIL_MODIFIED, Boolean.TRUE);
    }
}
