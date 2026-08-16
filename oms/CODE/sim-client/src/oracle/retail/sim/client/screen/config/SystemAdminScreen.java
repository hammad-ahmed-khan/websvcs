package oracle.retail.sim.client.screen.config;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * System Admin Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SystemAdminScreen extends SimScreen {
    private static final long serialVersionUID = -1396870188765223670L;

    private SystemAdminPanel panel = new SystemAdminPanel();

    public SystemAdminScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "System Admin";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
    }

    public void stop() {
        panel.stop();
    }

    /****************************************************************************************************
     * Handle Navigation Methods
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            }
        } catch (Throwable t) {
            displayException(panel, event, t);
        }
    }

    private void handleSave(NavigationEvent event) throws Exception {
        if (panel.handleSave()) {
            return;
        }
        event.consume();
    }
}
