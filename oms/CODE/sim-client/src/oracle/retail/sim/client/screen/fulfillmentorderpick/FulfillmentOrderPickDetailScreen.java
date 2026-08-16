package oracle.retail.sim.client.screen.fulfillmentorderpick;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.common.business.CommonMessageText;

/********************************************************************************************************
 * Fulfillment Order Pick Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderPickDetailScreen extends SimScreen {

    private static final long serialVersionUID = -5376081491268915885L;

    private FulfillmentOrderPickDetailPanel panel = new FulfillmentOrderPickDetailPanel();

    public FulfillmentOrderPickDetailScreen() {
        add(panel);
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public String getScreenName() {
        return "Customer Order Pick Detail";
    }

    public void start() throws Exception {
        panel.start();
        displayMenu();
    }

    public void resume() throws Exception {
        panel.start();
        displayMenu();
    }

    private void displayMenu() {
        showMenu();
        if (!panel.isPickBinType()) {
            removeNavButton(SimNavigation.BINS);
        }
        if (panel.isViewOnlyMode() || panel.isPickClosed()) {
            removeNavButton(SimNavigation.CONFIRM);
            removeNavButton(SimNavigation.CANCEL);
            removeNavButton(SimNavigation.SCANNER);
            removeNavButton(SimNavigation.SAVE);
        } else {
            removeNavButton(SimNavigation.BACK);
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
            } else if (command.equals(SimNavigation.BINS)) {
                handleBins();
            } else if (command.equals(SimNavigation.ITEM_SUBSTITUTION)) {
                handleSubstitution();
            } else if (command.equals(SimNavigation.PRINT)) {
                handlePrint();
            } else if (command.equals(SimNavigation.SCANNER)) {
                handleScanner();
            } else if (command.equals(SimNavigation.CANCEL)) {
                handleCancel();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void handleSave(NavigationEvent event) throws Exception {
        if (confirmActivityLock()) {
            if (panel.handleSave()) {
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

    private void handleBins() throws Exception {
        panel.handleBins();
    }

    private void handleSubstitution() throws Exception {
        panel.handleSubstitution();
    }

    private void handlePrint() throws Exception {
        panel.handlePrint();
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
