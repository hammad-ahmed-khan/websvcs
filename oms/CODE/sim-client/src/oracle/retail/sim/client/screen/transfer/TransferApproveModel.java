package oracle.retail.sim.client.screen.transfer;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferLineItem;
import oracle.retail.sim.common.transfer.TransferProperty;
import oracle.retail.sim.common.transfer.TransferPropertyModifiableRule;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Transfer Approve Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferApproveModel extends TransferDetailModel {

    public void loadTransfer() {
        transfer = (Transfer) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_TRANSFER);
    }

    /****************************************************************************************************
     * Line Item Methods
     ***************************************************************************************************/

    public boolean isLineItemsModifiable() {
        return TransferPropertyModifiableRule.isPropertyModifiable(transfer, TransferProperty.LINE_ITEM);
    }

    public void assignDefaultApprovedQuantities() {
        for (TransferLineItem lineItem : transfer.getLineItems()) {
            lineItem.doSetApprovedQuantity(lineItem.getRequestedQuantity());
            lineItem.doSetDirty();
        }
    }

    public boolean hasNoApprovedQuantities() {
        for (TransferLineItem lineItem : transfer.getLineItems()) {
            Quantity approvedQuantity = lineItem.getApprovedQuantity();
            if (approvedQuantity != null && approvedQuantity.isPositive()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasApprovedMoreThanRequested() {
        return transfer.isFulfillmentOrderRelated() && transfer.hasApprovedMoreThanOrderRequested();
    }

    public boolean hasApprovedPartialQuantities() {
        return transfer.isFulfillmentOrderRelated() && transfer.hasApprovePartialOrderQuantity();
    }

    /****************************************************************************************************
     * Process Transfer Methods
     ***************************************************************************************************/

    public void updateTransfer() throws Exception {
        ClientServiceFactory.getTransferServices().updateTransfer(transfer);
        RepositoryManager.addStateObject(SimClientStateKey.TRANSFER_DETAIL_MODIFIED, Boolean.TRUE);
    }

    public void rejectTransfer() throws Exception {
        transfer.validateRejectAllowed();
        ClientServiceFactory.getTransferServices().rejectTransfer(transfer.getId());
        RepositoryManager.addStateObject(SimClientStateKey.TRANSFER_DETAIL_MODIFIED, Boolean.TRUE);
    }

    public void approveTransfer() throws Exception {
        transfer.validateIsCoherent();
        transfer.validateApproveAllowed();
        ClientServiceFactory.getTransferServices().approveTransfer(transfer);
        RepositoryManager.addStateObject(SimClientStateKey.TRANSFER_DETAIL_MODIFIED, Boolean.TRUE);
    }

}