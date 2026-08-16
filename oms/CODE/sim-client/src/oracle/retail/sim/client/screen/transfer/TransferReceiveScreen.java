package oracle.retail.sim.client.screen.transfer;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Transfer Receive Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferReceiveScreen extends TransferDetailScreen {
    private static final long serialVersionUID = 5858056652246355168L;

    private TransferReceivePanel panel = new TransferReceivePanel();

    public TransferReceiveScreen() {
        add(panel);
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public void start() throws Exception {
        showMenu();
        panel.start();
        validateButtons();
    }

    public void resume() {
        showMenu();
        panel.resume();
        validateButtons();
    }

    private void validateButtons() {
        if (!panel.isAddItemAvailable()) {
            removeNavButton(SimNavigation.ADD_ITEM);
        }
        if (panel.isReceiptAdjustmentMode()) {
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.RECEIVE_ALL);
        }
        if (panel.isReceiveEntireTransferOnly()) {
            removeNavButton(SimNavigation.ADD_ITEM);
            removeNavButton(SimNavigation.REMOVE_ITEM);
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.RECEIVE_ALL);
            removeNavButton(SimNavigation.SCANNER);
        }
        // Only available on transfer view screen
        removeNavButton(SimNavigation.BACK);
    }

    public void pause() {
        panel.pause();
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
            } else if (command.equals(SimNavigation.RECEIVE_ALL)) {
                panel.handleReceiveAll();
            } else if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            } else if (command.equals(SimNavigation.CONFIRM)) {
                handleReceive(event);
            } else if (command.equals(SimNavigation.BILL_OF_LADING)) {
                panel.storeTransferForBillOfLading();
            } else if (command.equals(SimNavigation.SCANNER)) {
                panel.handleScanner();
            } else if (command.equals(SimNavigation.TRANSFER_INFO)) {
                panel.handleTransferInfo();
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
     * Receive Transfer
     ***************************************************************************************************/

    private void handleReceive(NavigationEvent event) throws Exception {
        if (panel.invalidTransferLock()) {
            processLockError(event);
            return;
        }
        if (panel.handleReceive()) {
            return;
        }
        event.consume();
    }
}
