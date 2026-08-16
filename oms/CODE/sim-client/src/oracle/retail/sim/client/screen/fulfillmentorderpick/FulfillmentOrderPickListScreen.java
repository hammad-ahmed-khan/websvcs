package oracle.retail.sim.client.screen.fulfillmentorderpick;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Fulfillment Order Pick List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderPickListScreen extends SimScreen {

    private static final long serialVersionUID = 6469343106600355708L;

    private FulfillmentOrderPickListPanel panel = new FulfillmentOrderPickListPanel();

    public FulfillmentOrderPickListScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Customer Order Pick List";
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
        if (RepositoryManager.getStateObject(SimClientStateKey.CUSTOMER_ORDER_PICK_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.CUSTOMER_ORDER_PICK_MODIFIED);
            panel.start();
        }
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.FULFILLMENT_ORDER_PICK_FILTER);
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
                panel.handleCreatePick();
            }
            if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrint();
            }
            if (command.equals(SimNavigation.DELETE)) {
                panel.handleCancelPick();
            }
            if (command.equals(SimNavigation.REFRESH)) {
                panel.handleRefresh();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }


}
