package oracle.retail.sim.client.screen.fulfillmentorderdelivery;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Fulfillment Order Delivery List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderDeliveryListScreen extends SimScreen {
    private static final long serialVersionUID = 2887037556785456919L;

    private FulfillmentOrderDeliveryListPanel panel = new FulfillmentOrderDeliveryListPanel();

    public FulfillmentOrderDeliveryListScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Customer Order Delivery List";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() {
        showMenu();
        panel.start();
        validateButtons();
    }

    public void resume() {
        showMenu();
        if (RepositoryManager.getStateObject(SimClientStateKey.CUSTOMER_ORDER_DELIVERY_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.CUSTOMER_ORDER_DELIVERY_MODIFIED);
            panel.start();
        }
        validateButtons();
    }

    private void validateButtons() {
        if (!panel.isCreateFunctionAvailable()) {
            removeNavButton(SimNavigation.CREATE);
        }
        if (!panel.isCancelFunctionAvailable()) {
            removeNavButton(SimNavigation.DELETE);
        }
    }

    /****************************************************************************************************
     * Navigation Methods
     ***************************************************************************************************/
    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.CREATE)) {
                panel.handleCreateDelivery();
            } else if (command.equals(SimNavigation.DELETE)) {
                panel.handleCancelDelivery();
            } else if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrint();
            } else if (command.equals(SimNavigation.NOTES)) {
                panel.handleNotes();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
