package oracle.retail.sim.client.screen.stockcount;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Stock Count Location Screen - This displays all the location counts of an all location stock count.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountChildScreen extends SimScreen {
    private static final long serialVersionUID = 7411063026838133589L;

    private StockCountChildPanel panel = new StockCountChildPanel();

    public StockCountChildScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Child Stock Count List";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        panel.start();
        displayMenu();
    }

    public void resume() throws Exception {
        if (RepositoryManager.getStateObject(SimClientStateKey.STOCK_COUNT_LOCATION_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.STOCK_COUNT_LOCATION_MODIFIED);
            panel.start();
        }
        displayMenu();
    }

    public void displayMenu() {
        showMenu();

        if (panel.isFutureStockCount()) {
            removeNavButton(SimNavigation.PRINT);
            removeNavButton(SimNavigation.TAKE_SNAPSHOT);
            removeNavButton(SimNavigation.COMPLETE_COUNT);
            removeNavButton(SimNavigation.AUTHORIZE);
            removeNavButton(SimNavigation.UPDATE_AUTH_QTY);
            removeNavButton(SimNavigation.CONFIRM_AUTHORIZATION);
            removeNavButton(SimNavigation.REJECTED_ITEMS);
            removeNavButton(SimNavigation.REFRESH);
        }
        if (panel.isRMSSyncStockCount()) {
            removeNavButton(SimNavigation.COMPLETE_COUNT);
            removeNavButton(SimNavigation.AUTHORIZE);
            removeNavButton(SimNavigation.UPDATE_AUTH_QTY);
            removeNavButton(SimNavigation.CONFIRM_AUTHORIZATION);
            removeNavButton(SimNavigation.REJECTED_ITEMS);
            removeNavButton(SimNavigation.REFRESH);
        }
        if (panel.isEditPermissionDenied()) {
            removeNavButton(SimNavigation.PRINT);
            removeNavButton(SimNavigation.TAKE_SNAPSHOT);
            removeNavButton(SimNavigation.COMPLETE_COUNT);
            removeNavButton(SimNavigation.AUTHORIZE);
            removeNavButton(SimNavigation.UPDATE_AUTH_QTY);
            removeNavButton(SimNavigation.CONFIRM_AUTHORIZATION);
            removeNavButton(SimNavigation.REJECTED_ITEMS);
        }
        if (!panel.isPrintOptionAvailable()) {
            removeNavButton(SimNavigation.PRINT);
        }
        if (!panel.isCompleteOptionAvailable()) {
            removeNavButton(SimNavigation.COMPLETE_COUNT);
        }
        if (!panel.isRejectItemsOptionAvailable()) {
            removeNavButton(SimNavigation.REJECTED_ITEMS);
        }
        if (!panel.isAuthorizeOptionAvailable()) {
            removeNavButton(SimNavigation.AUTHORIZE);
        }
        if (!panel.isUpdateAuthQtyOptionAvailable()) {
            removeNavButton(SimNavigation.UPDATE_AUTH_QTY);
        }
        if (!panel.isConfirmAuthorizationOptionAvailable()) {
            removeNavButton(SimNavigation.CONFIRM_AUTHORIZATION);
        }
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrint();
            } else if (command.equals(SimNavigation.TAKE_SNAPSHOT)) {
                panel.handleTakeSnapshot();
            } else if (command.equals(SimNavigation.COMPLETE_COUNT)) {
                panel.handleComplete();
            } else if (command.equals(SimNavigation.REFRESH)) {
                panel.handleRefresh();
            } else if (command.equals(SimNavigation.REJECTED_ITEMS)) {
                panel.handleRejectedItems();
            } else if (command.equals(SimNavigation.AUTHORIZE)) {
                panel.handleAuthorize();
            } else if (command.equals(SimNavigation.UPDATE_AUTH_QTY)) {
                panel.handleUpdateAuthQty();
            } else if (command.equals(SimNavigation.CONFIRM_AUTHORIZATION)) {
                panel.handleConfirmAuthorization();
            }
            displayMenu();
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
