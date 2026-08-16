package extra.retail.sim.client.screen.transfer;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Transfer Dispatch Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraTransferDispatchScreen extends ExtraTransferDetailScreen {
    private static final long serialVersionUID = -7302068171094246448L;

    private ExtraTransferDispatchPanel panel = new ExtraTransferDispatchPanel();

    public ExtraTransferDispatchScreen() {
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
        if (!panel.isSubmitAllowed()) {
            removeNavButton(SimNavigation.SUBMIT);
        }
        if (!panel.isDispatchAllowed()) {
            removeNavButton(SimNavigation.DISPATCH);
        }
        if (!panel.isShipTrailerEnabled()) {
        	removeNavButton(SimNavigation.SHIP_TRAILER);
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
                panel.handleDeleteItem();
            } else if (command.equals(SimNavigation.DEFAULT_QUANTITIES)) {
                panel.handleDefaultQuantities();
            } else if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            } else if (command.equals(SimNavigation.SUBMIT)) {
                panel.handleSubmit();
            } else if (command.equals(SimNavigation.DISPATCH)) {
                handleDispatch(event);
            } else if (command.equals(SimNavigation.BILL_OF_LADING)) {
                panel.storeTransferForBillOfLading();
            } else if (command.equals(SimNavigation.SCANNER)) {
                panel.handleScanner();
            } else if (command.equals(SimNavigation.TRANSFER_INFO)) {
                handleTransferInfo(event);
            } else if (command.equals(SimNavigation.SHIP_TRAILER)) {
            	handleShipTrailer();
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
        if (panel.handleSave()) {
            return;
        }
        event.consume();
    }

    /****************************************************************************************************
     * Request Transfer
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
     * Handle 
     ***************************************************************************************************/

    public void handleTransferInfo(NavigationEvent event) throws Exception {
        if (panel.handleTransferInfo()) {
            return;
        }
        event.consume();
    }
   
    private void handleShipTrailer() throws Exception {
    	panel.handleShipTrailer();
	}
}
