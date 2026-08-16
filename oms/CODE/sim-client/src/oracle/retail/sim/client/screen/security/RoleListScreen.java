package oracle.retail.sim.client.screen.security;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Role List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RoleListScreen extends SimScreen {
    private static final long serialVersionUID = -3868804770353176365L;

    private RoleListPanel panel = new RoleListPanel();

    public RoleListScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Role List";
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
        if (RepositoryManager.getStateObject(SimClientStateKey.ROLE_DETAIL_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.ROLE_DETAIL_MODIFIED);
            panel.populateScreen();
        }
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.ROLE_FILTER);
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
        } catch (Throwable t) {
            displayException(panel, event, t);
        }
    }
}
