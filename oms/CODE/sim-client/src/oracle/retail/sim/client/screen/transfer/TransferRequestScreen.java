package oracle.retail.sim.client.screen.transfer;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Transfer Request Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferRequestScreen extends TransferDetailScreen {
    private static final long serialVersionUID = 1784613514461675353L;

    private TransferRequestPanel panel = new TransferRequestPanel();

    public TransferRequestScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
        validateButtons();
    }

    public void resume() {
        showMenu();
        validateButtons();
    }

    private void validateButtons() {
        // Only available on transfer view screen
        removeNavButton(SimNavigation.BACK);
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
            if (command.equals(SimNavigation.ADD_ITEM)) {
                panel.handleAddItem();
            } else if (command.equals(SimNavigation.REMOVE_ITEM)) {
                panel.handleRemoveItem();
            } else if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            } else if (command.equals(SimNavigation.REQUEST)) {
                handleRequest(event);
            } else if (command.equals(SimNavigation.BILL_OF_LADING)) {
                handleBillOfLading(event);
            } else if (command.equals(SimNavigation.SCANNER)) {
                panel.handleScanner();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    /****************************************************************************************************
     * Done/Save Transfer
     ***************************************************************************************************/

    private void handleSave(NavigationEvent event) throws Exception {
        if (panel.invalidTransferLock()) {
            processLockError(event);
            return;
        }
        panel.handleSave();
    }

    /****************************************************************************************************
     * Request Transfer
     ***************************************************************************************************/

    private void handleRequest(NavigationEvent event) throws Exception {
        if (panel.invalidTransferLock()) {
            processLockError(event);
            return;
        }
        if (panel.handleRequest()) {
            return;
        }
        event.consume();
    }

    private void handleBillOfLading(NavigationEvent event) {
        panel.storeTransferForBillOfLading();
    }
}
