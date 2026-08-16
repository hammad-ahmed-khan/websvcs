package oracle.retail.sim.client.screen.uin;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * UIN Detail Display Screen. This screen displays all the UINs for a particular item.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UINDetailScreen extends SimScreen {
    private static final long serialVersionUID = 4367068784219107620L;

    private UINDetailPanel panel = new UINDetailPanel();

    public UINDetailScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "UIN Detail";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
        validateButtons();
    }

    public void resume() throws Exception {
        RepositoryManager.removeStateObject(SimClientStateKey.ITEM_UIN_STATUS_MODIFIED);
        showMenu();
        panel.resume();
        validateButtons();
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.ITEM_UIN_FILTER);
    }

    private void validateButtons() {
        if (panel.isSerialNumberType()) {
            removeNavButton(SimNavigation.UIN_PRINT_TICKET);
        }
    }

    /****************************************************************************************************
     * Handle Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.VIEW_HISTORY)) {
                panel.handleViewHistory();
            }
            if (command.equals(SimNavigation.UIN_PRINT_TICKET)) {
                panel.handlePrintTicket();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
