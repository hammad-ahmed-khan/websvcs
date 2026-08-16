package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.Displayer;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferStatus;
import oracle.retail.sim.common.transfer.TransferVO;

/********************************************************************************************************
 * Transfer Status Displayer
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferVOStatusDisplayer implements Displayer {

    public String getDisplayText(Object object) {
        return getDisplayText(object, null);
    }

    public String getDisplayText(Object value, Object model) {
        if (value instanceof TransferStatus) {
            Long receivingStoreId = null;
            if (model instanceof TransferVO) {
                receivingStoreId = ((TransferVO) model).getReceivingStoreId();
            }
            if (model instanceof Transfer) {
                receivingStoreId = ((Transfer) model).getReceivingStore().getId();
            }
            TransferStatus status = (TransferStatus) value;
            if (SimRepository.getStore().getId().equals(receivingStoreId)) {
                return Translator.getText(status.getReceivingDescription());
            }
            return Translator.getText(status.getSendingDescription());
        }
        return StringConstants.EMPTY;
    }
}