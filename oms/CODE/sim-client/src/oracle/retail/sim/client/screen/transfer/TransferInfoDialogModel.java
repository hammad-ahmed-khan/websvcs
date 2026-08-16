package oracle.retail.sim.client.screen.transfer;

import java.util.Date;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.transfer.Transfer;

/********************************************************************************************************
 * Transfer Info Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class TransferInfoDialogModel extends SimScreenModel {
    private Transfer transfer;

    public Transfer getTransfer() {
        return transfer;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }

    public String getSubmitUser() {
        return transfer.getSubmitUser();

    }

    public Date getSubmitDate() {
        return transfer.getSubmitDate();

    }

    public String getCustomerOrderExternalId() {
        return transfer.getCustomerOrderExternalId();
    }

    public String getFulfillOrderExternalId() {
        return transfer.getFulfillmentOrderExternalId();
    }

}
