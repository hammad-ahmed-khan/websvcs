package oracle.retail.sim.client.screen.mps;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * MPS Staged Message Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class MpsStagedMessageScreen extends SimScreen {
    private static final long serialVersionUID = -6317894459425285231L;

    private MpsStagedMessagePanel panel = new MpsStagedMessagePanel();

    public MpsStagedMessageScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "MPS Staged Message Lookup";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        try {
            switch (event.getCommand()) {
                case SimNavigation.REFRESH:
                    panel.handleRefresh();
                    break;
                case SimNavigation.RESET:
                    panel.handleReset();
                    break;
                case SimNavigation.DELETE:
                    panel.handleDelete();
                    break;
            }
        } catch (Throwable t) {
            displayException(panel, event, t);
        }
    }
}
