package oracle.retail.sim.client.screen.supplier;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Supplier Lookup Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SupplierLookupScreen extends SimScreen {
    private static final long serialVersionUID = -7238305725993481285L;

    private SupplierLookupPanel panel = new SupplierLookupPanel();

    public SupplierLookupScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Supplier Lookup";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() {
        showMenu(SimNavigation.SEARCH);
        panel.start();
    }

    public void resume() {
        showMenu(SimNavigation.SEARCH);
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
            if (command.equals(SimNavigation.SEARCH)) {
                panel.handleSearch();
            } else if (command.equals(SimNavigation.SUPPLIER_DETAIL)) {
                panel.handleSupplierDetail();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }
}
