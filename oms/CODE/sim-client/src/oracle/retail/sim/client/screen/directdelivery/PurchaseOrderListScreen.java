package oracle.retail.sim.client.screen.directdelivery;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Purchase Order List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class PurchaseOrderListScreen extends SimScreen {
    private static final long serialVersionUID = -5349908316694736648L;

    private PurchaseOrderListPanel panel = new PurchaseOrderListPanel();

    public PurchaseOrderListScreen() {
        add(panel);
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Purchase Order List";
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

        if (!panel.isCreateDeliveryAllowed()) {
            removeNavButton(SimNavigation.CREATE_DELIVERY);
        }
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.REFRESH)) {
                panel.handleRefresh();
            } else if (command.equals(SimNavigation.CREATE_DELIVERY)) {
                panel.handleCreateDelivery();
            }
        } catch (Throwable t) {
            displayException(panel, event, t);
        }
    }
}
