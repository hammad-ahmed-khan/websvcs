package oracle.retail.sim.client.screen.printer;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Label, Ticket and Report Format Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RetailStorePrinterScreen extends SimScreen {
    private static final long serialVersionUID = -7616536278775126051L;
    private RetailStorePrinterPanel panel = new RetailStorePrinterPanel();

    public RetailStorePrinterScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Printers";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu(SimNavigation.SAVE);
        panel.start();
    }

    public void resume() {
        showMenu(SimNavigation.SAVE);
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.RETAIL_STORE_PRINTER);
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                panel.handleSave();
            } else if (command.equals(SimNavigation.DELETE)) {
                panel.handleDelete();
            } else if (command.equals(SimNavigation.ADD)) {
                panel.handleCreate();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
