package oracle.retail.sim.client.screen.transfer;

import java.util.List;
import oracle.retail.sim.common.transfer.TransferSerialNumber;

/********************************************************************************************************
 * The business logic model for the Transfer UIN Dialog.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferViewUinDialogModel {

    private TransferLineItemWrapper lineItemWrapper;

    public void setLineItemWrapper(TransferLineItemWrapper wrapper) {
        lineItemWrapper = wrapper;
    }

    public String getUINLabel() {
        return lineItemWrapper.getStockItem().getUINLabel();
    }

    public List<TransferSerialNumber> findTransferSerialNumbers() throws Exception {
        return lineItemWrapper.getLineItem().getSerialNumbers();
    }
}