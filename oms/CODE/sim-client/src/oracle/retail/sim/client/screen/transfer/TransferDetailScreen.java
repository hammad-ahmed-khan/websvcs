package oracle.retail.sim.client.screen.transfer;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.common.business.CommonMessageText;

/********************************************************************************************************
 * Transfer Detail Screen. This is the abstract class that all transfer screens should extend.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class TransferDetailScreen extends SimScreen {
    private static final long serialVersionUID = 1074152998141820545L;

    public String getScreenName() {
        return "Transfer Detail";
    }

    /**
     * This will display the standard lock error and then consume the event so the event
     * action will not take place. Finally, it attempts to navigate to the previous
     * screen on the stack.
     */
    void processLockError(NavigationEvent event) {
        displayException(CommonMessageText.LOCK_TAKEN_OVER);
        if (event != null) {
            event.consume();
        }
        navigate(SimNavigation.PREVIOUS_SCREEN);
    }
}
