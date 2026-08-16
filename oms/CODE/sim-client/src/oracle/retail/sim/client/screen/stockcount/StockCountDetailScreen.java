package oracle.retail.sim.client.screen.stockcount;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Stock Count Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountDetailScreen extends SimScreen {
    private static final long serialVersionUID = 1730021254302025383L;

    private StockCountDetailPanel panel = new StockCountDetailPanel();

    public StockCountDetailScreen() {
        add(panel);
    }

    public String getScreenName() {
        if (panel.isRecountMode()) {
            return "Stock Re-Count Detail";
        }
        return "Stock Count Detail";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        panel.start();
        displayMenu();
    }

    private void displayMenu() throws Exception {
        showMenu();

        if (panel.isFutureStockCount()) {
            removeNavButton(SimNavigation.PRINT);
            removeNavButton(SimNavigation.TAKE_SNAPSHOT);
            removeNavButton(SimNavigation.COMPLETE_COUNT);
            removeNavButton(SimNavigation.CANCEL);
            removeNavButton(SimNavigation.SCANNER);
        }
        if (panel.isStockCountChildEditable()) {
            removeNavButton(SimNavigation.BACK);
        } else {
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.CANCEL);
        }
        if (!panel.isTakeSnapshotAvailable()) {
            removeNavButton(SimNavigation.TAKE_SNAPSHOT);
        }
        if (!panel.isCompleteAvailable()) {
            removeNavButton(SimNavigation.COMPLETE_COUNT);
        }
        if (!panel.hasStockCountChildPermissions()) {
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.PRINT);
            removeNavButton(SimNavigation.TAKE_SNAPSHOT);
            removeNavButton(SimNavigation.COMPLETE_COUNT);
            removeNavButton(SimNavigation.CANCEL);
            removeNavButton(SimNavigation.SCANNER);
        }
        if (!panel.isScannerAvailable()) {
            removeNavButton(SimNavigation.SCANNER);
        }
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
            if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrint();
            } else if (command.equals(SimNavigation.SAVE)) {
                panel.handleSave();
            } else if (command.equals(SimNavigation.TAKE_SNAPSHOT)) {
                handleTakeSnapshot();
            } else if (command.equals(SimNavigation.COMPLETE_COUNT)) {
                handleComplete(event);
            } else if (command.equals(SimNavigation.SCANNER)) {
                panel.handleScanner();
            } else if (command.equals(SimNavigation.CANCEL)) {
                handleCancel(event);
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    public void handleTakeSnapshot() throws Exception {
        panel.handleTakeSnapshot();
        displayMenu();
    }

    private void handleCancel(NavigationEvent event) throws Exception {
        if (panel.handleCancel()) {
            return;
        }
        event.consume();
    }

    private void handleComplete(NavigationEvent event) throws Exception {
        if (panel.handleComplete()) {
            return;
        }
        event.consume();
    }
}
