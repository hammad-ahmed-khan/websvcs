package oracle.retail.sim.client.screen.warehousedelivery;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Warehouse Delivery List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class WarehouseDeliveryListScreen extends SimScreen {
    private static final long serialVersionUID = -5965205422696355329L;

    private WarehouseDeliveryListPanel panel = new WarehouseDeliveryListPanel();

    public WarehouseDeliveryListScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Warehouse Delivery List";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    /****************************************************************************************************
     * State Management
     ***************************************************************************************************/

    public void start() throws Exception {
        showMenu();
        panel.start();
    }

    public void resume() throws Exception {
        showMenu();
        panel.resume();
    }

    public void stop() {
        panel.stop();
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
            } else if (command.equals(SimNavigation.CUSTOMER_ORDERS)) {
                handleFulfillmentOrders(event);
            }
        } catch (Throwable t) {
            displayException(panel, event, t);
        }
    }

    private void handleFulfillmentOrders(NavigationEvent event) throws Exception {
        if (!panel.handleFulfillmentOrders()) {
            event.consume();
        }
    }
}
