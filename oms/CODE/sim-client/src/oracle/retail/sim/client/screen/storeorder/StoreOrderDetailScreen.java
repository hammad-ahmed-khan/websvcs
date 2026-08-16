package oracle.retail.sim.client.screen.storeorder;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Store Order Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreOrderDetailScreen extends SimScreen implements REventListener {
    private static final long serialVersionUID = 4800896660684757674L;

    private StoreOrderDetailPanel panel = new StoreOrderDetailPanel();

    public StoreOrderDetailScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Store Order Detail";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        panel.start();
        panel.addREventListener(this);
        displayMenu();
    }

    public void resume() throws Exception {
        displayMenu();
    }

    public void stop() {
        panel.stop();
    }

    private void displayMenu() {
        showMenu();
        if (panel.isStoreOrderUnmodifiable()) {
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.CANCEL);
            removeNavButton(SimNavigation.ADD_ITEM);
            removeNavButton(SimNavigation.REMOVE_ITEM);
            removeNavButton(SimNavigation.DEALS_QUERY);
            removeNavButton(SimNavigation.ITEM_ORDERS);
            removeNavButton(SimNavigation.ITEM_SALES);
            removeNavButton(SimNavigation.PRINT_TICKETS);
        }
        if (panel.isApproveUnavailable()) {
            removeNavButton(SimNavigation.APPROVE);
        }
        if (panel.hasStoreOrder()) {
            panel.setNavigationState(true);

            if (panel.isNewStoreOrder()) {
                removeNavButton(SimNavigation.PRINT);
            }
            if (!panel.isSupplierOrder() || panel.isTSFStoreOrder()) {
                removeNavButton(SimNavigation.DEALS_QUERY);
            }
            if (panel.isAddItemUnavailable()) {
                removeNavButton(SimNavigation.ADD_ITEM);
            }
            if (panel.isStoreOrderClosed()) {
                panel.setNavigationState(false);
                removeNavButton(SimNavigation.ADD_ITEM);
                removeNavButton(SimNavigation.REMOVE_ITEM);
                removeNavButton(SimNavigation.APPROVE);
                removeNavButton(SimNavigation.ITEM_ORDERS);
                removeNavButton(SimNavigation.ITEM_SALES);
                removeNavButton(SimNavigation.DEALS_QUERY);
                removeNavButton(SimNavigation.CANCEL);
                removeNavButton(SimNavigation.SAVE);
                removeNavButton(SimNavigation.SCANNER);
            }
            if (panel.isStoreOrderApproved()) {
                panel.setNavigationState(false);
                removeNavButton(SimNavigation.ADD_ITEM);
                removeNavButton(SimNavigation.REMOVE_ITEM);
                removeNavButton(SimNavigation.APPROVE);
                removeNavButton(SimNavigation.SAVE);
                removeNavButton(SimNavigation.CANCEL);
                removeNavButton(SimNavigation.SCANNER);
            }
            return;
        }
        removeNavButton(SimNavigation.APPROVE);

        if (!panel.isSupplierOrder()) {
            removeNavButton(SimNavigation.DEALS_QUERY);
        }
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                panel.handleSave();
            } else if (command.equals(SimNavigation.CANCEL)) {
                panel.handleCancel();
            } else if (command.equals(SimNavigation.ADD_ITEM)) {
                panel.handleAddItem();
            } else if (command.equals(SimNavigation.REMOVE_ITEM)) {
                handleCancelItem();
            } else if (command.equals(SimNavigation.APPROVE)) {
                panel.handleApprove();
            } else if (command.equals(SimNavigation.DEALS_QUERY)) {
                handleDealsQuery(event);
            } else if (command.equals(SimNavigation.ITEM_ORDERS)) {
                handleItemOrders(event);
            } else if (command.equals(SimNavigation.ITEM_SALES)) {
                handleItemSales(event);
            } else if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrint();
            } else if (command.equals(SimNavigation.SCANNER)) {
                panel.handleScanner();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void handleItemOrders(NavigationEvent event) {
        if (panel.handleItemOrders()) {
            return;
        }
        event.consume();
    }

    private void handleItemSales(NavigationEvent event) {
        if (panel.handleItemSales()) {
            return;
        }
        event.consume();
    }

    private void handleDealsQuery(NavigationEvent event) throws Exception {
        if (panel.handleDealsQuery()) {
            return;
        }
        event.consume();
    }

    public void handleCancelItem() throws Exception {
        if (panel.handleCancelItem()) {
            navigate(SimNavigation.PREVIOUS_SCREEN);
        }
    }

    /****************************************************************************************************
     * Handle Action Sent From Panel
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(SimClientStateKey.STORE_ORDER_TYPE_MODIFIED)) {
            displayMenu();
        }
    }
}
