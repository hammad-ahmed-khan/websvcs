package oracle.retail.sim.client.screen.mps;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * MPS Worker Type Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class MpsWorkerTypeScreen extends SimScreen {
    private static final long serialVersionUID = 6378580734694941286L;

    private MpsWorkerTypePanel panel = new MpsWorkerTypePanel();

    public MpsWorkerTypeScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "MPS Worker Types";
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
                case SimNavigation.START:
                    panel.handleStart();
                    break;
                case SimNavigation.STOP:
                    panel.handleStop();
                    break;
            }
        } catch (Throwable t) {
            displayException(panel, event, t);
        }
    }
}
