package oracle.retail.sim.client.screen.fulfillmentorder;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Fulfillment Order List Screen
 * <p>
 * The customer order list screen can will display the customer orders present in database.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FulfillmentOrderListScreen extends SimScreen {

    private static final long serialVersionUID = -3949291361988007896L;

    private FulfillmentOrderListPanel panel = new FulfillmentOrderListPanel();

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public FulfillmentOrderListScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Customer Order List";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() {
        showMenu();
        panel.start();
    }

    public void resume() {
        showMenu();
        if (RepositoryManager.getStateObject(SimClientStateKey.FULFILLMENT_ORDER_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.FULFILLMENT_ORDER_MODIFIED);
            panel.start();
        }
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.FULFILLMENT_ORDER_FILTER);
    }

    /****************************************************************************************************
     * Navigation Methods
     ***************************************************************************************************/
    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.DELIVERY)) {
                panel.handleDelivery();
            }
            if (command.equals(SimNavigation.REVERSE_PICK)) {
                panel.handleReversePick();
            }
            if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrint();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
