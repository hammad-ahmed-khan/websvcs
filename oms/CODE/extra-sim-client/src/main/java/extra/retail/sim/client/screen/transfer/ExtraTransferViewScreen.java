package extra.retail.sim.client.screen.transfer;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Transfer View Only Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraTransferViewScreen extends ExtraTransferDetailScreen {
    private static final long serialVersionUID = 615327504100732925L;

    private ExtraTransferViewPanel panel = new ExtraTransferViewPanel();

    public ExtraTransferViewScreen() {
        add(panel);
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu(SimNavigation.BACK);
        panel.start();
        validateNavigationButtons();
    }

    private void validateNavigationButtons() {
        if (!panel.isAdjustmentAllowed()) {
            removeNavButton(SimNavigation.ADJUST);
        }
        if (!panel.isDispatchAllowed()) {
            removeNavButton(SimNavigation.DISPATCH);
        }
        if (!panel.isSubmitAllowed()) {
            removeNavButton(SimNavigation.CANCEL_SUBMIT);
        }
    }

    public void resume() {
        showMenu(SimNavigation.BACK);
        validateNavigationButtons();
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
            if (command.equals(SimNavigation.ADJUST)) {
                handleAdjustDelivery(event);
            } else if (command.equals(SimNavigation.BILL_OF_LADING)) {
                handleBillOfLading(event);
            } else if (command.equals(SimNavigation.TRANSFER_INFO)) {
                panel.handleTransferInfo();
            } else if (command.equals(SimNavigation.DISPATCH)) {
                panel.handleDispatch();
            } else if (command.equals(SimNavigation.CANCEL_SUBMIT)) {
                panel.handleCancelSubmit();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void handleAdjustDelivery(NavigationEvent event) throws Exception {
        if (panel.handleAdjustDelivery()) {
            removeFromScreenHistory();
            navigate(SimScreenName.TRANSFER_RECEIVE_SCREEN);
            return;
        }
        event.consume();
    }

    private void handleBillOfLading(NavigationEvent event) {
        panel.storeTransferForBillOfLading();
    }
}
