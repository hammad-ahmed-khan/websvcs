package oracle.retail.sim.client.screen.invadjustment;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.common.business.CommonMessageText;

/********************************************************************************************************
 * Inventory Adjustment Reason Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryAdjustmentReasonScreen extends SimScreen {
    private static final long serialVersionUID = 8115289757449131556L;

    private InventoryAdjustmentReasonPanel panel = new InventoryAdjustmentReasonPanel();

    public InventoryAdjustmentReasonScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/
    public String getScreenName() {
        return "Inventory Adjustment Reason Maintenance";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
    }

    /****************************************************************************************************
     * Navigation Methods
     ***************************************************************************************************/
    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            } else if (command.equals(SimNavigation.ADD)) {
                handleAdd(event);
            } else if (command.equals(SimNavigation.DELETE)) {
                handleDelete(event);
            } else if (command.equals(SimNavigation.CANCEL)) {
                handleCancel();
            }
        } catch (Throwable e) {
            displayException(panel, event, e);
        }
    }

    private void handleAdd(NavigationEvent event) throws Exception {
        if (confirmActivityLock(true)) {
            panel.handleAdd();
            return;
        }
        event.consume();
    }

    private void handleDelete(NavigationEvent event) throws Exception {
        if (confirmActivityLock(true)) {
            panel.handleDelete();
            return;
        }
        event.consume();
    }

    public void handleSave(NavigationEvent event) throws Exception {
        if (confirmActivityLock(true)) {
            if (panel.handleSave()) {
                return;
            }
        }
        event.consume();
    }

    private void handleCancel() {
        panel.handleCancel();
    }

    private boolean confirmActivityLock(boolean exitScreen) throws Exception {
        if (panel.confirmReasonAdminLock()) {
            return true;
        }
        displayException(CommonMessageText.LOCK_TAKEN_OVER);
        if (exitScreen) {
            navigate(SimNavigation.PREVIOUS_SCREEN);
        }
        return false;
    }
}
