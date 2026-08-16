package oracle.retail.sim.client.screen.fulfillmentorder;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Fulfillment Order Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FulfillmentOrderDetailScreen extends SimScreen {
    private static final long serialVersionUID = 1870693928386335887L;

    private FulfillmentOrderDetailPanel panel = new FulfillmentOrderDetailPanel();

    public FulfillmentOrderDetailScreen() {
        add(panel);
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Customer Order Detail";
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
        validateButtons();
    }

    public void resume() throws Exception {
        showMenu();
        panel.start();
        validateButtons();
    }

    private void validateButtons() {
        if (panel.isItemDetailOrigin()) {
            removeNavButton(SimNavigation.CUSTOMER);
            removeNavButton(SimNavigation.DELIVERY);
            removeNavButton(SimNavigation.REVERSE_PICK);
        }
        if (!panel.isWebOrder()) {
            removeNavButton(SimNavigation.CUSTOMER);
            removeNavButton(SimNavigation.REVERSE_PICK);
            removeNavButton(SimNavigation.REJECT);
        }
    }

    /****************************************************************************************************
     * Navigation Methods
     ***************************************************************************************************/
    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                panel.handleSave();
            } else if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrint();
            } else if (command.equals(SimNavigation.NOTES)) {
                panel.handleNotes();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
