package oracle.retail.sim.client.screen.transfer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferLineItem;
import oracle.retail.sim.common.transfer.TransferStatus;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Transfer View Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferViewModel extends TransferDetailModel {
    public void loadTransfer() {
        transfer = (Transfer) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_TRANSFER);
    }

    public List<TransferLineItemWrapper> getLineItemWrappers() {
        List<TransferLineItemWrapper> transferItems = new ArrayList<>();
        for (TransferLineItem lineItem : transfer.getLineItems()) {
            transferItems.add(ClientWrapperFactory.createTransferLineItemWrapper(transfer, lineItem, true));
        }
        return transferItems;
    }

    public boolean isRequestedQuantityDisplayable() {
        if (transfer == null) {
            return false;
        }
        if (transfer.getStatus() == TransferStatus.NEW) {
            return true;
        }
        return hasRequestedQuantity();
    }

    public boolean isApprovedQuantityDisplayable() {
        return transfer != null && hasRequestedQuantity();
    }

    public boolean isShippedQuantityDisplayable() {
        return transfer != null && transfer.getStatus().getCode() > TransferStatus.IN_PROGRESS.getCode();
    }

    public boolean isReceivedQuantityDisplayable() {
        return transfer != null && transfer.getStatus().getCode() > TransferStatus.DISPATCHED.getCode();
    }

    public boolean isDamagedQuantityDisplayable() {
        return transfer != null && transfer.getStatus().getCode() > TransferStatus.DISPATCHED.getCode();
    }

    public boolean isAdjustmentAllowed() {
        if (transfer.getStatus() != TransferStatus.RECEIVED) {
            return false;
        }
        if (!transfer.getReceivingStore().getId().equals(getStoreId())) {
            return false;
        }
        Integer daysAllowed = SimConfigManager.getInteger(SimConfigManager.DAYS_ALLOWED_FOR_ADJUSTMENTS_TO_TRANSFERS);
        if (daysAllowed == null || daysAllowed <= 0) {
            return false;
        }
        TimeZone timeZone = transfer.getReceivingStore().getTimeZone();
        Date deadlineDate = SimDateUtil.getDateAtEndOfDay(timeZone, transfer.getReceiveDate());
        if (daysAllowed > 1) {
            deadlineDate = SimDateUtil.addDays(timeZone, deadlineDate, daysAllowed - 1);
        }
        return SimDateUtil.getCurrentDateAtStartOfDay(timeZone).before(deadlineDate);
    }

    public boolean isDispatchAllowed() {
        return transfer.getStatus() == TransferStatus.SUBMITTED;
    }
    
    /****************************************************************************************************
     * Cancel Transfer
     ***************************************************************************************************/

    public void cancelTransfer() throws Exception {
        ClientServiceFactory.getTransferServices().cancelTransfer(getStoreId(), transfer.getId());
        RepositoryManager.addStateObject(SimClientStateKey.TRANSFER_DETAIL_MODIFIED, Boolean.TRUE);
    }
}
