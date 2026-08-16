package oracle.retail.sim.client.screen.directdelivery;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Purchase Order Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class PurchaseOrderDetailScreen extends SimScreen {
    private static final long serialVersionUID = -428549206523483113L;

    private PurchaseOrderDetailPanel panel = new PurchaseOrderDetailPanel();

    public PurchaseOrderDetailScreen() {
        add(panel);
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Purchase Order Detail";
    }

    public void start() throws Exception {
        panel.start();
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
            if (command.equals(SimNavigation.CREATE_DELIVERY)) {
                handleCreateDelivery(event);
            }
        } catch (Throwable t) {
            displayException(panel, event, t);
        }
    }

    private void handleCreateDelivery(NavigationEvent event) throws Exception {
        if (!panel.handleCreateDelivery()) {
            event.consume();
            return;
        }
        removeFromScreenHistory();
    }
}
