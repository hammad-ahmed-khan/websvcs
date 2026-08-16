package oracle.retail.sim.client.screen.transfer;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Transfer Approve Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferApproveScreen extends TransferDetailScreen {
    private static final long serialVersionUID = -481019552998109071L;

    private TransferApprovePanel panel = new TransferApprovePanel();

    public TransferApproveScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public void start() throws Exception {
        showMenu();
        panel.start();
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void resume() {
        showMenu();
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
            if (command.equals(SimNavigation.ACCEPT)) {
                handleAccept(event);
            } else if (command.equals(SimNavigation.REJECT)) {
                handleReject(event);
            } else if (command.equals(SimNavigation.DEFAULT_QUANTITIES)) {
                panel.handleDefaultQuantities();
            } else if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            } else if (command.equals(SimNavigation.BILL_OF_LADING)) {
                handleBillOfLading(event);
            } else if (command.equals(SimNavigation.TRANSFER_INFO)) {
                handleTransferInfo(event);
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void handleAccept(NavigationEvent event) throws Exception {
        if (panel.invalidTransferLock()) {
            processLockError(event);
            return;
        }
        if (panel.handleAccept()) {
            return;
        }
        event.consume();
    }

    private void handleReject(NavigationEvent event) throws Exception {
        if (panel.invalidTransferLock()) {
            processLockError(event);
            return;
        }
        if (panel.handleReject()) {
            return;
        }
        event.consume();
    }

    private void handleSave(NavigationEvent event) throws Exception {
        if (panel.invalidTransferLock()) {
            processLockError(event);
            return;
        }
        panel.handleSave();
    }

    private void handleBillOfLading(NavigationEvent event) {
        panel.storeTransferForBillOfLading();
    }

    private void handleTransferInfo(NavigationEvent event) {
        panel.handleTransferInfo();
    }
}
