package oracle.retail.sim.client.screen.security;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * User Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UserDetailScreen extends SimScreen {
    private static final long serialVersionUID = 4063351561925160520L;

    private UserDetailPanel panel = new UserDetailPanel();

    public UserDetailScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "User Detail";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        panel.start();
        displayMenu();
    }

    public void resume() throws Exception {
        displayMenu();
    }

    public void stop() {
        panel.stop();
    }

    private void displayMenu() throws Exception {
        showMenu();
        if (panel.isUserReadOnly()) {
            removeNavButton(SimNavigation.ASSIGN_PASSWORD);
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.CANCEL);
        } else {
            removeNavButton(SimNavigation.BACK);
        }
        if (!panel.isUserNew()) {
            removeNavButton(SimNavigation.COPY_ASSIGNMENTS);
        }
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                panel.handleSave();
            } else if (command.equals(SimNavigation.ASSIGN_PASSWORD)) {
                panel.handleAssignPassword();
            } else if (command.equals(SimNavigation.ASSIGN_STORES)) {
                panel.handleAssignStores();
            } else if (command.equals(SimNavigation.ASSIGN_ROLES)) {
                panel.handleAssignRoles();
            } else if (command.equals(SimNavigation.COPY_ASSIGNMENTS)) {
                panel.handleCopyAssignments();
            }
        } catch (Throwable t) {
            displayException(panel, event, t);
        }
    }
}
