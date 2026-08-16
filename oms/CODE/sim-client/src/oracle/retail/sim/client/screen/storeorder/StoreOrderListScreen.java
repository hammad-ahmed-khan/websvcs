package oracle.retail.sim.client.screen.storeorder;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Store Orders Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreOrderListScreen extends SimScreen {
    private static final long serialVersionUID = 6978855199841064123L;

    private StoreOrderListPanel panel = new StoreOrderListPanel();

    public StoreOrderListScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Store Orders";
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
        if (RepositoryManager.getStateObject(SimClientStateKey.STORE_ORDER_DETAIL) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.STORE_ORDER_IN_PROGRESS);
            RepositoryManager.removeStateObject(SimClientStateKey.STORE_ORDER_FILTER);
            navigate(SimNavigation.PREVIOUS_SCREEN);
            return;
        }
        if (RepositoryManager.getStateObject(SimClientStateKey.STORE_ORDER_DETAIL_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.STORE_ORDER_DETAIL_MODIFIED);
            panel.start();
        }
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.STORE_ORDER_FILTER);
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.CREATE)) {
                handleCreateOrder(event);
            } else if (command.equals(SimNavigation.DELETE)) {
                panel.handleCancelOrder();
            } else if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrint();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void handleCreateOrder(NavigationEvent event) {
        if (panel.handleCreateOrder()) {
            return;
        }
        event.consume();
    }
}
