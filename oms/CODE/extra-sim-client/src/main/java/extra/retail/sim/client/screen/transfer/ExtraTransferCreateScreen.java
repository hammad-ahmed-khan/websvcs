package extra.retail.sim.client.screen.transfer;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Transfer Create Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraTransferCreateScreen extends ExtraTransferDetailScreen {
    private static final long serialVersionUID = 1315629364557437749L;

    private ExtraTransferCreatePanel panel = new ExtraTransferCreatePanel();

    public ExtraTransferCreateScreen() {
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

    private void validateButtons() {
        if (!panel.isSubmitAvailable()) {
            removeNavButton(SimNavigation.SUBMIT);
            removeNavButton(SimNavigation.CANCEL_SUBMIT);
        }
        if (!panel.isDispatchAvailable()) {
            removeNavButton(SimNavigation.DISPATCH);
        }
        if (!panel.isShipTrailerEnabled()) {
        	removeNavButton(SimNavigation.SHIP_TRAILER);
        }
        removeNavButton(SimNavigation.BACK);
    }

    public void resume() {
        showMenu();
        panel.resume();
        validateButtons();
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
            } else if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            } else if (command.equals(SimNavigation.SUBMIT)) {
                handleSubmit(event);
            } else if (command.equals(SimNavigation.DISPATCH)) {
                handleDispatch(event);
            } else if (command.equals(SimNavigation.BILL_OF_LADING)) {
                handleBillOfLading(event);
            } else if (command.equals(SimNavigation.SCANNER)) {
                panel.handleScanner();
            } else if (command.equals(SimNavigation.SHIP_TRAILER)) {
            	handleShipTrailer();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void handleShipTrailer() throws Exception {
    	panel.handleShipTrailer();
	}

    /****************************************************************************************************
     * Save Transfer
     ***************************************************************************************************/

    private void handleSave(NavigationEvent event) throws Exception {
        if (panel.invalidTransferLock()) {
            processLockError(event);
            return;
        }
        if (panel.handleSave()) {
            return;
        }
        event.consume();
    }

    /****************************************************************************************************
     * Submit Transfer
     ***************************************************************************************************/

    private void handleSubmit(NavigationEvent event) throws Exception {
        if (panel.invalidTransferLock()) {
            processLockError(event);
            return;
        }
        if (panel.handleSubmit()) {
            return;
        }
        event.consume();
    }

    /****************************************************************************************************
     * Dispatch Transfer
     ***************************************************************************************************/

    private void handleDispatch(NavigationEvent event) throws Exception {
        if (panel.invalidTransferLock()) {
            processLockError(event);
            return;
        }
        if (panel.handleDispatch()) {
            return;
        }
        event.consume();
    }

    /****************************************************************************************************
     * Handle Bill Of Lading
     ***************************************************************************************************/

    private void handleBillOfLading(NavigationEvent event) throws Exception {
        if (panel.invalidTransferLock()) {
            processLockError(event);
            return;
        }
        if (panel.handleBillOfLading()) {
            return;
        }
        event.consume();
    }
}
