package oracle.retail.sim.client.screen.invadjustment;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.common.business.CommonMessageText;

/********************************************************************************************************
 * Inventory Adjustment Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryAdjustmentDetailScreen extends SimScreen {
    private static final long serialVersionUID = 8174510207626951426L;

    private InventoryAdjustmentDetailPanel panel = new InventoryAdjustmentDetailPanel();

    public InventoryAdjustmentDetailScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/
    public String getScreenName() {
        return "Inventory Adjustment Detail";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
        validateButtons();
    }

    private void validateButtons() {
        if (!panel.isAdjustmentEditable()) {
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.CANCEL);
            removeNavButton(SimNavigation.CONFIRM);
            removeNavButton(SimNavigation.ADD_ITEM);
            removeNavButton(SimNavigation.REMOVE_ITEM);
        } else {
            removeNavButton(SimNavigation.BACK);
        }
        if (!panel.isConfirmFunctionAvailable()) {
            removeNavButton(SimNavigation.CONFIRM);
        }
        if (!panel.isCopyFunctionAvailable()) {
            removeNavButton(SimNavigation.COPY);
        }
        if (!panel.isPrintFunctionAvailable()) {
            removeNavButton(SimNavigation.PRINT);
        }
        if (!panel.isScannerAvailable()) {
            removeNavButton(SimNavigation.SCANNER);
        }
    }

    public void stop() {
        panel.stop();
    }

    /****************************************************************************************************
     * Navigation Methods
     ***************************************************************************************************/
    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            } else if (command.equals(SimNavigation.CONFIRM)) {
                handleConfirm(event);
            } else if (command.equals(SimNavigation.ADD_ITEM)) {
                handleAddItem();
            } else if (command.equals(SimNavigation.REMOVE_ITEM)) {
                handleDeleteItem();
            } else if (command.equals(SimNavigation.CANCEL)) {
                handleCancel();
            } else if (command.equals(SimNavigation.COPY)) {
                handleCopy();
            } else if (command.equals(SimNavigation.PRINT)) {
                handlePrint();
            } else if (command.equals(SimNavigation.SCANNER)) {
                handleScanner();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void handleConfirm(NavigationEvent event) throws Exception {
        if (!panel.isAdjustmentEditable()) {
            return;
        }
        if (!confirmActivityLock()) {
            event.consume();
            return;
        }
        if (panel.handleConfirm()) {
            return;
        }
        event.consume();
    }

    private void handleSave(NavigationEvent event) throws Exception {
        if (!confirmActivityLock()) {
            event.consume();
            return;
        }
        if (panel.handleSave()) {
            return;
        }
        event.consume();
    }

    private void handleAddItem() throws Exception {
        panel.handleAddItem();
    }

    private void handleDeleteItem() {
        panel.handleDeleteItem();
    }

    private void handleCancel() {
        panel.handleCancel();
    }

    private void handleCopy() throws Exception {
        if (panel.handleCopy()) {
            start();
        }
    }

    private void handlePrint() throws Exception {
        panel.handlePrint();
    }

    private void handleScanner() {
        panel.handleScanner();
    }

    /****************************************************************************************************
     * Helper Methods
     ***************************************************************************************************/

    private boolean confirmActivityLock() throws Exception {
        if (panel.confirmInventoryAdjustmentLock()) {
            return true;
        }
        displayException(CommonMessageText.LOCK_TAKEN_OVER);
        navigate(SimNavigation.PREVIOUS_SCREEN);
        return false;
    }

    public void resume() throws Exception {
        showMenu();
        validateButtons();
    }
}