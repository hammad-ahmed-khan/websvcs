package oracle.retail.sim.client.screen.fulfillmentorderreversepick;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.common.business.CommonMessageText;

/********************************************************************************************************
 * Fulfillment Order Reverse Pick Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderReversePickDetailScreen extends SimScreen {
    private static final long serialVersionUID = -3509583262320607906L;
    
    private FulfillmentOrderReversePickDetailPanel panel = new FulfillmentOrderReversePickDetailPanel();

    public FulfillmentOrderReversePickDetailScreen() {
        add(panel);
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public String getScreenName() {
        return "Customer Order Reverse Pick Detail";
    }

    public void start() throws Exception {
        panel.start();
        displayMenu();
    }

    public void resume() throws Exception {
        panel.start();
        displayMenu();
    }

    private void displayMenu() throws Exception {
        showMenu();
        if (panel.isReversePickClosed() || !panel.isReversePickEditAllowed()) {
            removeNavButton(SimNavigation.DEFAULT_QUANTITIES);
            removeNavButton(SimNavigation.CONFIRM);
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.SCANNER);
            removeNavButton(SimNavigation.CANCEL);
        } else {
            removeNavButton(SimNavigation.BACK);
        }
        if (!panel.isConfirmFunctionAvailable()) {
            removeNavButton(SimNavigation.CONFIRM);
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
            if (command.equals(SimNavigation.DEFAULT_QUANTITIES)) {
                handleDefaultQuantities();
            } else if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            } else if (command.equals(SimNavigation.CONFIRM)) {
                handleConfirm(event);
            } else if (command.equals(SimNavigation.NOTES)) {
                handleNotes();
            } else if (command.equals(SimNavigation.SCANNER)) {
                handleScanner();
            } else if (command.equals(SimNavigation.CANCEL)) {
                handleCancel();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void handleDefaultQuantities() throws Exception {
        panel.handleDefaultQuantities();
    }

    private void handleSave(NavigationEvent event) throws Exception {
        if (confirmActivityLock()) {
            if (panel.handleDone()) {
                return;
            }
        }
        event.consume();
    }

    private void handleConfirm(NavigationEvent event) throws Exception {
        if (confirmActivityLock()) {
            if (panel.handleConfirm()) {
                return;
            }
        }
        event.consume();
    }

    private void handleNotes() throws Exception {
        panel.handleNotes();
    }
    
    private void handleScanner() throws Exception {
        panel.handleScanner();
    }

    private void handleCancel() throws Exception {
        panel.handleCancel();
    }

    /****************************************************************************************************
     * Helper Methods
     ***************************************************************************************************/

    private boolean confirmActivityLock() throws Exception {
        if (panel.confirmActivityLock()) {
            return true;
        }
        displayException(CommonMessageText.LOCK_TAKEN_OVER);
        navigate(SimNavigation.PREVIOUS_SCREEN);
        return false;
    }
}
