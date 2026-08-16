package oracle.retail.sim.client.screen.security;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Role Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RoleDetailScreen extends SimScreen {
    private static final long serialVersionUID = 358691946580554168L;

    private RoleDetailPanel panel = new RoleDetailPanel();

    public RoleDetailScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Role Detail";
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
        panel.resume();
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_ROLE);
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.DATA_PERMISSION)) {
                panel.updateRoleInformation();
            } else if (command.equals(SimNavigation.SAVE)) {
                panel.handleSave();
            }
        } catch (Throwable t) {
            displayException(panel, event, t);
        }
    }
}
