package oracle.retail.sim.client.screen.directdelivery;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Direct Delivery ASN List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class DirectDeliveryAsnListScreen extends SimScreen {
    private static final long serialVersionUID = -4728749509556648236L;

    private DirectDeliveryAsnListPanel panel = new DirectDeliveryAsnListPanel();

    public DirectDeliveryAsnListScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "ASN List";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() {
        showMenu();
        panel.start();
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
            if (command.equals(SimNavigation.CANCEL)) {
                panel.handleCancel();
            } else if (command.equals(SimNavigation.USE_ASN)) {
                handleUseAsn(event);
            }
        } catch (Throwable t) {
            displayException(panel, event, t);
        }
    }

    private void handleUseAsn(NavigationEvent event) throws Exception {
        if (!panel.handleUseAsn()) {
            event.consume();
            return;
        }
        removeFromScreenHistory();
        stop();
    }
}
