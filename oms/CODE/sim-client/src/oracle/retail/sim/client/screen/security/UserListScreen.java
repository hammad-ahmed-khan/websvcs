package oracle.retail.sim.client.screen.security;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * User List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UserListScreen extends SimScreen {
    private static final long serialVersionUID = -4533136172755952445L;

    private UserListPanel panel = new UserListPanel();

    public UserListScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "User List";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
    }

    public void resume() throws Exception {
        showMenu();
        if (RepositoryManager.getStateObject(SimClientStateKey.USER_DETAIL_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.USER_DETAIL_MODIFIED);
            panel.populateScreen();
        }
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.USER_FILTER);
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.DELETE)) {
                panel.handleDelete();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
