package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferStatus;

/********************************************************************************************************
 * Transfer Status Displayer
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferStatusDisplayer extends AbstractDisplayer {

    private Transfer transfer;

    public TransferStatusDisplayer(Transfer transfer) {
        this.transfer = transfer;
    }

    public String getDisplayText(Object object) {
        return Translator.getText(getDescription(object));
    }

    private String getDescription(Object object) {
        if (object instanceof TransferStatus) {
            Store receivingStore = transfer.getReceivingStore();
            if (receivingStore != null) {
                if (receivingStore.equals(SimRepository.getStore())) {
                    return ((TransferStatus) object).getReceivingDescription();
                } else {
                    return ((TransferStatus) object).getSendingDescription();
                }
            }
            Store sendingStore = transfer.getSendingStore();
            if (sendingStore != null) {
                if (sendingStore.equals(SimRepository.getStore())) {
                    return ((TransferStatus) object).getSendingDescription();
                } else {
                    return ((TransferStatus) object).getReceivingDescription();
                }
            }
        }
        return StringConstants.EMPTY;
    }
}