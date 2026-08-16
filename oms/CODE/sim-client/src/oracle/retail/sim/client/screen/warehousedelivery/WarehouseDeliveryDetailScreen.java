package oracle.retail.sim.client.screen.warehousedelivery;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Warehouse Delivery Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class WarehouseDeliveryDetailScreen extends SimScreen {
    private static final long serialVersionUID = 4719786348850106469L;

    private WarehouseDeliveryDetailPanel panel = new WarehouseDeliveryDetailPanel();

    public WarehouseDeliveryDetailScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Receive Container";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() {
        try {
            panel.start();
            displayMenu();
        } catch (Throwable t) {
            displayException(t);
        }
    }

    public void resume() {
        try {
            if (panel.resume()) {
                displayMenu();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    public void stop() {
        panel.stop();
    }

    private void displayMenu() {
        showMenu();

        if (!panel.isFulfillmentOrderRelated()) {
            removeNavButton(SimNavigation.CUSTOMER_ORDERS);
        }
        if (panel.isViewOnlyMode()) {
            removeNavButton(SimNavigation.CONFIRM);
            removeNavButton(SimNavigation.RECEIVE);
            removeNavButton(SimNavigation.UNRECEIVE);
            removeNavButton(SimNavigation.ADJUST);
            removeNavButton(SimNavigation.CANCEL);
            return;
        }
        if (panel.isDeliveryClosed()) {
            removeNavButton(SimNavigation.CONFIRM);
            removeNavButton(SimNavigation.RECEIVE);
            removeNavButton(SimNavigation.UNRECEIVE);
            removeNavButton(SimNavigation.CANCEL);
            removeNavButton(SimNavigation.SAVE);
        }
        if (!panel.isViewOnlyMode() && !panel.isDeliveryClosed()) {
            removeNavButton(SimNavigation.BACK);
        }
        if (!panel.isDeliveryAdjustable()) {
            removeNavButton(SimNavigation.ADJUST);
        }
        if (panel.isAllowAdjustment()) {
            removeNavButton(SimNavigation.BACK);
            removeNavButton(SimNavigation.SAVE);
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
            } else if (command.equals(SimNavigation.RECEIVE)) {
                handleReceive();
            } else if (command.equals(SimNavigation.CONFIRM)) {
                handleConfirm(event);
            } else if (command.equals(SimNavigation.CANCEL)) {
                handleCancel();
            } else if (command.equals(SimNavigation.UNRECEIVE)) {
                handleUnreceive();
            } else if (command.equals(SimNavigation.ADJUST)) {
                handleAdjustDelivery();
            } else if (command.equals(SimNavigation.CUSTOMER_ORDERS)) {
                handleFulfillmentOrders(event);
            }
        } catch (Throwable t) {
            displayException(panel, event, t);
        }
    }

    private void handleAdjustDelivery() throws Exception {
        if (panel.handleAdjustDelivery()) {
            displayMenu();
        }
    }

    private void handleCancel() {
        panel.handleCancel();
    }

    private void handleReceive() throws Exception {
        panel.handleReceive();
    }

    public void handleUnreceive() throws Exception {
        panel.handleUnreceive();
    }

    public void handleSave(NavigationEvent event) {
        if (!panel.handleSave()) {
            event.consume();
        }
    }

    private void handleConfirm(NavigationEvent event) {
        if (!panel.handleConfirm()) {
            event.consume();
        }
    }

    private void handleFulfillmentOrders(NavigationEvent event) throws Exception {
        if (!panel.handleFulfillmentOrders()) {
            event.consume();
        }
    }
}
