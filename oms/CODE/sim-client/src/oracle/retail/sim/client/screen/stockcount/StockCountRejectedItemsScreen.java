package oracle.retail.sim.client.screen.stockcount;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Stock Count Rejected Items Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountRejectedItemsScreen extends SimScreen {
    private static final long serialVersionUID = -4930443173030112177L;

    private StockCountRejectedItemsPanel panel = new StockCountRejectedItemsPanel();

    public StockCountRejectedItemsScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Rejected Items";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() {
        showMenu();
        panel.start();
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.STOCK_COUNT_REJECTED_ITEMS);
        RepositoryManager.removeStateObject(SimClientStateKey.STOCK_COUNT_REJECTED_ITEMS_EDITABLE);
    }

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            } else if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrintAll();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void handleSave(NavigationEvent event) throws Exception {
        if (panel.handleSave()) {
            return;
        }
        event.consume();
    }
}
