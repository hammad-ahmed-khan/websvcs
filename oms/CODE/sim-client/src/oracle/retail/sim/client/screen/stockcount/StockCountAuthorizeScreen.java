package oracle.retail.sim.client.screen.stockcount;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Stock Authorization Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountAuthorizeScreen extends SimScreen {
    private static final long serialVersionUID = 6339608776527771767L;

    private StockCountAuthorizePanel panel = new StockCountAuthorizePanel();

    public StockCountAuthorizeScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Stock Count Authorization";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        panel.start();
        displayMenu();
    }

    private void displayMenu() {
        showMenu();
        if (!panel.isStockCountScreenEditable()) {
            removeNavButton(SimNavigation.UPDATE_SNAPSHOT);
            removeNavButton(SimNavigation.CONFIRM_CHILD);
            removeNavButton(SimNavigation.SAVE_CHILD);
            removeNavButton(SimNavigation.CANCEL);
            removeNavButton(SimNavigation.UPDATE_AUTH_QTY);
        }
    }

    public void resume() throws Exception {
        displayMenu();
    }

    public void stop() {
        panel.stop();
    }

    /****************************************************************************************************
     * Handle Navigation Methods
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.BACK)) {
                handleBack(event);
            } else if (command.equals(SimNavigation.UPDATE_SNAPSHOT)) {
                panel.handleApplyLateSales();
            } else if (command.equals(SimNavigation.COUNT_DETAIL)) {
                panel.handleCountDetail();
            } else if (command.equals(SimNavigation.UPDATE_AUTH_QTY)) {
                panel.handleUpdateAuthQty();
            } else if (command.equals(SimNavigation.SAVE_CHILD)) {
                panel.handleSaveChild();
            } else if (command.equals(SimNavigation.CONFIRM_CHILD)) {
                panel.handleConfirm();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void handleBack(NavigationEvent event) throws Exception {
        if (panel.handleBack()) {
            return;
        }
        event.consume();
    }
}