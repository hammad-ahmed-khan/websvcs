package oracle.retail.sim.client.screen.mps;

import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.mps.MpsStagedMessage;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Staged Message Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class MpsStagedMessageDialogModel extends SimScreenModel {
    private MpsStagedMessage stagedMessage;

    public void setStagedMessage(MpsStagedMessage stagedMessage) {
        this.stagedMessage = stagedMessage;
    }

    public String getMessageData() {
        return stagedMessage.getMessageData();
    }

    public String getMessageError() {
        return stagedMessage.getMessageError();
    }

    public boolean saveMessageData(String messageData) throws Exception {
        if (getMessageData().equals(messageData)) {
            LogService.debug(this, "Ignoring unchanged message data.");
            return false;
        }
        LogService.debug(this, "Updating changed message data.");
        ClientServiceFactory.getMpsServices().updateStagedMessageData(stagedMessage.getId(), messageData);
        return true;
    }
}
