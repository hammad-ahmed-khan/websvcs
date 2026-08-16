package oracle.retail.sim.client.screen.invadjustment;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Inventory Adjustment List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryAdjustmentListScreen extends SimScreen {
    private static final long serialVersionUID = -2392331892000077804L;

    private InventoryAdjustmentListPanel panel = new InventoryAdjustmentListPanel();

    public InventoryAdjustmentListScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/
    public String getScreenName() {
        return "Inventory Adjustment List";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
        validateButtons();
    }

    public void resume() throws Exception {
        showMenu();
        if (RepositoryManager.getStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_MODIFIED);
            panel.start();
            validateButtons();
        }
    }

    private void validateButtons() {
        if (!panel.isDeleteFunctionAvailable()) {
            removeNavButton(SimNavigation.DELETE);
        }
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_FILTER);
    }

    /****************************************************************************************************
     * Navigation Methods
     ***************************************************************************************************/
    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrint();
            } else if (command.equals(SimNavigation.DELETE)) {
                panel.handleDelete();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}