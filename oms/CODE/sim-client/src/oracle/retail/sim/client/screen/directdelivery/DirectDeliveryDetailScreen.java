package oracle.retail.sim.client.screen.directdelivery;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Direct Delivery Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class DirectDeliveryDetailScreen extends SimScreen {
    private static final long serialVersionUID = -8899799090956785971L;

    private DirectDeliveryDetailPanel panel = new DirectDeliveryDetailPanel();

    public DirectDeliveryDetailScreen() {
        add(panel);
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Direct Delivery Detail";
    }

    public void start() {
        panel.start();
        displayMenu();
    }

    public void stop() {
        panel.stop();
    }
    
    public void resume() throws Throwable {
    	displayMenu();
    } 

    private void displayMenu() {
        showMenu();

        if (panel.isViewOnlyMode()) {
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.CONFIRM);
            removeNavButton(SimNavigation.RECEIVE_ALL);
            removeNavButton(SimNavigation.CANCEL);
            removeNavButton(SimNavigation.ADD_ITEM);
            removeNavButton(SimNavigation.REMOVE_ITEM);
            removeNavButton(SimNavigation.ADJUST);
            removeNavButton(SimNavigation.REJECT);
            removeNavButton(SimNavigation.SCANNER);
            return;
        }
        if (panel.isDeliveryClosed()) {
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.CONFIRM);
            removeNavButton(SimNavigation.RECEIVE_ALL);
            removeNavButton(SimNavigation.CANCEL);
        }
        if (!panel.isViewOnlyMode() && !panel.isDeliveryClosed()) {
            removeNavButton(SimNavigation.BACK);
        }
        if (!panel.isAddItemAllowed()) {
            removeNavButton(SimNavigation.ADD_ITEM);
        }
        if (!panel.isRemoveItemAllowed()) {
            removeNavButton(SimNavigation.REMOVE_ITEM);
        }
        if (!panel.hasPurchaseOrder()) {
            removeNavButton(SimNavigation.RECEIVE_ALL);
        }
        if (!panel.isDeliveryAdjustable()) {
            removeNavButton(SimNavigation.ADJUST);
        }
        if (panel.isAllowAdjustment()) {
            removeNavButton(SimNavigation.SAVE);
        }
        if (!panel.isRejectDeliveryAllowed()) {
            removeNavButton(SimNavigation.REJECT);
        }
        if (!panel.isScannerAvailable()) {
            removeNavButton(SimNavigation.SCANNER);
        }
    }

    /****************************************************************************************************
     * Handle Navigation Actions
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.CANCEL)) {
                handleCancel();
            } else if (command.equals(SimNavigation.ADD_ITEM)) {
                handleAddItem();
            } else if (command.equals(SimNavigation.REMOVE_ITEM)) {
                handleRemoveItem();
            } else if (command.equals(SimNavigation.RECEIVE_ALL)) {
                handleReceiveAll();
            } else if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            } else if (command.equals(SimNavigation.CONFIRM)) {
                handleConfirm(event);
            } else if (command.equals(SimNavigation.PRINT)) {
                handlePrint();
            } else if (command.equals(SimNavigation.ADJUST)) {
                handleAdjustDelivery();
            } else if (command.equals(SimNavigation.REJECT)) {
                handleRejectDelivery(event);
            } else if (command.equals(SimNavigation.SCANNER)) {
                handleScanner();
            }
        } catch (Throwable t) {
            displayException(panel, event, t);
        }
    }

    private void handleCancel() {
        panel.handleCancel();
    }

    private void handleAddItem() throws Exception {
        panel.handleAddItem();
    }

    private void handleRemoveItem() throws Exception {
        panel.handleRemoveItem();
    }

    private void handleReceiveAll() throws Exception {
        panel.handleReceiveAll();
    }

    private void handleSave(NavigationEvent event) {
        if (!panel.handleSave()) {
            event.consume();
        }
    }

    private void handleConfirm(NavigationEvent event) throws Exception {
        if (!panel.handleConfirm()) {
            event.consume();
        }
    }

    private void handlePrint() throws Exception {
        panel.handlePrint();
    }

    private void handleAdjustDelivery() throws Exception {
        if (panel.handleAdjustDelivery()) {
            displayMenu();
        }
    }

    private void handleRejectDelivery(NavigationEvent event) throws Exception {
        if (!panel.handleRejectDelivery()) {
            event.consume();
        }
    }

    private void handleScanner() {
        panel.handleScanner();
    }
}
