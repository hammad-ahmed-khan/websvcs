package oracle.retail.sim.client.screen.uda;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * UDA Print Setup Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UDAPrintSetupScreen extends SimScreen {
    private static final long serialVersionUID = -6475219601912340509L;

    private UDAPrintSetupPanel panel = new UDAPrintSetupPanel();

    public UDAPrintSetupScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "UDA Print Setup";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu(SimNavigation.SAVE);
        panel.start();
    }

    public void resume() {
        showMenu(SimNavigation.SAVE);
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                panel.handleSave();
            } else if (command.equals(SimNavigation.CANCEL)) {
                panel.handleCancel();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
