package oracle.retail.sim.client.screen.stockcount;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Stock Count List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountListScreen extends SimScreen {
    private static final long serialVersionUID = -5820607441871584596L;

    private StockCountListPanel panel = new StockCountListPanel();

    public StockCountListScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Stock Count List";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        panel.start();
        displayMenu();
    }

    public void resume() throws Exception {
        if (RepositoryManager.getStateObject(SimClientStateKey.STOCK_COUNT_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.STOCK_COUNT_MODIFIED);
            panel.start();
        }
        displayMenu();
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.STOCK_COUNT_FILTER);
    }

    private void displayMenu() {
        showMenu();
    }

    /****************************************************************************************************
     * Button Actions
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrint();
            } else if (command.equals(SimNavigation.REFRESH)) {
                panel.handleRefresh();
            } else if (command.equals(SimNavigation.DELETE)) {
                panel.handleDelete();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
