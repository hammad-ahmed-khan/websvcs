package extra.retail.sim.client.screen.returns;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.stockreturn.ReturnMessageText;

/********************************************************************************************************
 * Return Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraReturnDetailScreen extends SimScreen {
    private static final long serialVersionUID = 5490661554257991646L;

    private ExtraReturnDetailPanel panel = new ExtraReturnDetailPanel();

    public ExtraReturnDetailScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Return Detail";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public boolean isStartable() {
        return panel.isStartable();
    }

    /**
     * No return means we are creating a new one, we will internally create a new warehouse return, but
     * will not show the user this. This is a little strange, but we do it so that some of the values
     * will be filled in (date, user, etc.,) and so we don't need to do a null check. When the user
     * selects a warehouse or supplier return, then we will replace this value with the new one and
     * disable to radio button group.
     */
    public void start() throws Exception {
        panel.start();
        displayMenu();
    }

    public void resume() throws Exception {
        panel.resume();
        displayMenu();
    }

    private void displayMenu() throws Exception {
        showMenu();
        if (panel.isReturnRequestUnmodifiable()) {
            panel.displayError(ReturnMessageText.BEYOND_NOT_AFTER_DATE);
            panel.setDetailScreenViewOnly();
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.CANCEL);
            removeNavButton(SimNavigation.ADD_ITEM);
            removeNavButton(SimNavigation.REMOVE_ITEM);
            removeNavButton(SimNavigation.DISPATCH);
            removeNavButton(SimNavigation.SCANNER);
            removeNavButton(SimNavigation.SUBMIT);
            removeNavButton(SimNavigation.CANCEL_SUBMIT);
            return;
        }
        if (panel.isReturnUnmodifiable()) {
            panel.setDetailScreenViewOnly();
            if (panel.isReturnSubmitted()) {
                removeNavButton(SimNavigation.SAVE);
                removeNavButton(SimNavigation.CANCEL);
                removeNavButton(SimNavigation.ADD_ITEM);
                removeNavButton(SimNavigation.REMOVE_ITEM);
                removeNavButton(SimNavigation.SCANNER);
                removeNavButton(SimNavigation.SUBMIT);
            } else {
                removeNavButton(SimNavigation.SAVE);
                removeNavButton(SimNavigation.CANCEL);
                removeNavButton(SimNavigation.ADD_ITEM);
                removeNavButton(SimNavigation.REMOVE_ITEM);
                removeNavButton(SimNavigation.DISPATCH);
                removeNavButton(SimNavigation.SCANNER);
                removeNavButton(SimNavigation.SUBMIT);
                removeNavButton(SimNavigation.CANCEL_SUBMIT);
                return;
            }
        }

        if (!panel.isObtainReturnLock()) {
            panel.setDetailScreenViewOnly();
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.CANCEL);
            removeNavButton(SimNavigation.ADD_ITEM);
            removeNavButton(SimNavigation.REMOVE_ITEM);
            removeNavButton(SimNavigation.DISPATCH);
            removeNavButton(SimNavigation.SCANNER);
            removeNavButton(SimNavigation.SUBMIT);
            removeNavButton(SimNavigation.CANCEL_SUBMIT);
            return;
        }

        if (!panel.isReturnRequestUnmodifiable() && !panel.isReturnUnmodifiable()) {
            removeNavButton(SimNavigation.BACK);
        }
        if (panel.isSubmitNotAvailable()) {
            removeNavButton(SimNavigation.SUBMIT);
        }
        if (panel.isCancelSubmitNotAvailable()) {
            removeNavButton(SimNavigation.CANCEL_SUBMIT);
        }
        if (panel.isDispatchNotAvailable()) {
            removeNavButton(SimNavigation.DISPATCH);
        }
        if (panel.isAddItemNotAvailable()) {
            removeNavButton(SimNavigation.ADD_ITEM);
        }
        if (panel.isRemoveItemNotAvailable()) {
            removeNavButton(SimNavigation.REMOVE_ITEM);
        }
        if (!panel.isScannerAvailable()) {
            removeNavButton(SimNavigation.SCANNER);
        }
        if (!panel.isShipTrailerEnabled()) {
        	removeNavButton(SimNavigation.SHIP_TRAILER);
        }
    }

    public void pause() {
        panel.pauseScreen();
    }

    public void stop() {
        panel.stopScreen();
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.ADD_ITEM)) {
                handleAddItem();
            } else if (command.equals(SimNavigation.REMOVE_ITEM)) {
                handleRemoveItem();
            } else if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            } else if (command.equals(SimNavigation.DISPATCH)) {
                handleDispatch(event);
            } else if (command.equals(SimNavigation.SUBMIT)) {
                handleSubmit(event);
            } else if (command.equals(SimNavigation.BILL_OF_LADING)) {
                handleBillOfLading(event);
            } else if (command.equals(SimNavigation.SCANNER)) {
                handleScanner();
            } else if (command.equals(SimNavigation.CANCEL_SUBMIT)) {
                handleCancelSubmission(event);
            } else if (command.equals(SimNavigation.SHIP_TRAILER)) {
            	handleShipTrailer(event);
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void handleShipTrailer(NavigationEvent event) throws Exception {
    	if (panel.isReturnTypeWH()) {    		
    		panel.handleShipTrailer();
    		return;
    	}
    	event.consume();
	}

	private void handleAddItem() throws Exception {
        if (confirmActivityLock()) {
            panel.handleAddItem();
        }
    }

    private void handleRemoveItem() throws Exception {
        if (confirmActivityLock()) {
            panel.handleRemoveItem();
            validateEmptyReturn();
        }
    }

    private void handleDispatch(NavigationEvent event) throws Exception {
        if (panel.hasNoQuantity()) {
            if (!panel.cancelReturn()) {
                event.consume();
                return;
            }
            panel.stopScreen();
            return;
        }
        if (panel.validateShipTrailer() && confirmActivityLock() && validateEmptyReturn()) {
            if (panel.handleDispatch()) {
                panel.stopScreen();
                return;
            }
        }
        event.consume();
    }

    private void handleSubmit(NavigationEvent event) throws Exception {
        if (panel.handleSubmit()) {
            panel.stopScreen();
            return;
        }
        event.consume();
    }

    private void handleCancelSubmission(NavigationEvent event) throws Exception {
        if (panel.handleCancelSubmission()) {
            panel.stopScreen();
            return;
        }
        event.consume();
    }

    private void handleSave(NavigationEvent event) throws Exception {
        if (panel.hasNoQuantity()) {
            if (panel.cancelReturn()) {
                panel.stopScreen();
            } else {
                event.consume();
            }
            return;
        }
        if (!confirmActivityLock()) {
            return;
        }
        if (validateEmptyReturn() && panel.isValidForDone()) {
            panel.updateStockReturn();
            panel.stopScreen();
            return;
        }
        event.consume();
    }

	private void handleBillOfLading(NavigationEvent event) throws Exception {
        if (panel.isReturnUnmodifiable() || panel.isReturnRequestUnmodifiable()) {
            panel.storeReturnForBillOfLading();
            return;
        }
        if (panel.isValidForBillOfLading() && confirmActivityLock()) {
            panel.storeReturnForBillOfLading();
            return;
        }
        event.consume();
    }

    private void handleScanner() {
        panel.handleScanner();
    }

    private boolean validateEmptyReturn() throws Exception {
        if (panel.isReturnEmpty()) {
            if (RConfirmUtility.confirm("Item Delete Confirmation", CommonMessageText.NO_ROWS_REMAINING)) {
                panel.cancelEmptyReturn();
                navigate(SimNavigation.PREVIOUS_SCREEN);
                return false;
            }
        }
        return true;
    }

    /****************************************************************************************************
     * Helper Methods
     ***************************************************************************************************/

    private boolean confirmActivityLock() throws Exception {
        if (panel.confirmReturnLock()) {
            return true;
        }
        displayException(CommonMessageText.LOCK_TAKEN_OVER);
        panel.stopScreen();
        navigate(SimNavigation.PREVIOUS_SCREEN);
        return false;
    }
}
