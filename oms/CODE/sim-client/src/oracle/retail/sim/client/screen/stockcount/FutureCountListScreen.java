package oracle.retail.sim.client.screen.stockcount;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Future Count List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FutureCountListScreen extends SimScreen {
    private static final long serialVersionUID = -1431356876414088712L;

    private FutureCountListPanel panel = new FutureCountListPanel();

    public FutureCountListScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Future Stock Count List";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public boolean isStartable() {
        return panel.isStartable();
    }

    public void start() throws Exception {
        panel.start();
        showMenu();
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.FUTURE_COUNT_FILTER);
    }

    /****************************************************************************************************
     * Button Actions
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.REFRESH)) {
                panel.handleRefresh();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
