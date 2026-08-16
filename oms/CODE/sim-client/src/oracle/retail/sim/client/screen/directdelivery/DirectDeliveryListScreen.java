package oracle.retail.sim.client.screen.directdelivery;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Direct Delivery List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class DirectDeliveryListScreen extends SimScreen {
    private static final long serialVersionUID = 2465928578673666229L;

    private DirectDeliveryListPanel panel = new DirectDeliveryListPanel();

    public DirectDeliveryListScreen() {
        add(panel);
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Direct Delivery List";
    }

    public void start() throws Exception {
        panel.start();
        displayMenu();
    }

    public void resume() throws Exception {
        panel.resume();
        displayMenu();
    }

    public void stop() {
        panel.stop();
    }

    private void displayMenu() {
        showMenu();
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.REFRESH)) {
                panel.handleRefresh();
            } else if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrint();
            }
        } catch (Throwable t) {
            displayException(panel, event, t);
        }
    }
}
