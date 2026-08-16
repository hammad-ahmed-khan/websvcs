package oracle.retail.sim.client.screen.warehousedelivery;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Warehouse Delivery Carton Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class WarehouseDeliveryCartonDetailScreen extends SimScreen {
    private static final long serialVersionUID = 7673903170992479591L;

    private WarehouseDeliveryCartonDetailPanel panel = new WarehouseDeliveryCartonDetailPanel();

    public WarehouseDeliveryCartonDetailScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Receive Case";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() {
        try {
            panel.start();
            displayMenu();
        } catch (Throwable e) {
            displayException(e);
        }
    }

    public void stop() {
        panel.stop();
    }

    private void displayMenu() {
        showMenu();

        if (panel.isViewOnlyMode()) {
            removeNavButton(SimNavigation.ADD_ITEM);
            removeNavButton(SimNavigation.REMOVE_ITEM);
            removeNavButton(SimNavigation.CANCEL);
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.SCANNER);
            return;
        }
        if (panel.isDeliveryClosed()) {
            removeNavButton(SimNavigation.CANCEL);
            removeNavButton(SimNavigation.SAVE);
        } else {
            removeNavButton(SimNavigation.BACK);
        }
        if (!panel.isAddItemAllowed()) {
            removeNavButton(SimNavigation.ADD_ITEM);
        }
        if (!panel.isDeleteItemAllowed()) {
            removeNavButton(SimNavigation.REMOVE_ITEM);
        }
        if (!panel.isScannerAvailable()) {
            removeNavButton(SimNavigation.SCANNER);
        }
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            } else if (command.equals(SimNavigation.ADD_ITEM)) {
                handleAddItem();
            } else if (command.equals(SimNavigation.REMOVE_ITEM)) {
                handleRemoveItem();
            } else if (command.equals(SimNavigation.CANCEL)) {
                handleCancel();
            } else if (command.equals(SimNavigation.SCANNER)) {
                handleScanner();
            }
        } catch (Throwable e) {
            displayException(panel, event, e);
        }
    }

    private void handleSave(NavigationEvent event) {
        if (!panel.handleSave()) {
            event.consume();
        }
    }

    private void handleAddItem() throws Exception {
        panel.handleAddItem();
    }

    private void handleRemoveItem() throws Exception {
        panel.handleRemoveItem();
    }

    private void handleCancel() {
        panel.handleCancel();
    }

    private void handleScanner() {
        panel.handleScanner();
    }
}
