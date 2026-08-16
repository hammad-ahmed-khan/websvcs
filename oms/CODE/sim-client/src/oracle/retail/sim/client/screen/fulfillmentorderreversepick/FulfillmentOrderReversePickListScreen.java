package oracle.retail.sim.client.screen.fulfillmentorderreversepick;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Fulfillment Order Reverse Pick List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderReversePickListScreen extends SimScreen {
    private static final long serialVersionUID = -244631497295758354L;

    private FulfillmentOrderReversePickListPanel panel = new FulfillmentOrderReversePickListPanel();

    public FulfillmentOrderReversePickListScreen() {
        add(panel);
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public String getScreenName() {
        return "Customer Order Reverse Pick List";
    }

    public void start() throws Exception {
        panel.start();
        displayMenu();
    }

    private void displayMenu() {
        showMenu();
        if (!panel.isCreateFunctionAvailable()) {
            removeNavButton(SimNavigation.CREATE);
        }
        if (!panel.isCancelFunctionAvailable()) {
            removeNavButton(SimNavigation.DELETE);
        }
    }

    public void resume() throws Exception {
        panel.start();
        displayMenu();
    }

    /****************************************************************************************************
     * Navigation Methods
     ***************************************************************************************************/
    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.CREATE)) {
                panel.handleCreatePick();
            } else if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrint();
            } else if (command.equals(SimNavigation.DELETE)) {
                panel.handleCancelPick();
            } else if (command.equals(SimNavigation.NOTES)) {
                panel.handleNotes();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
