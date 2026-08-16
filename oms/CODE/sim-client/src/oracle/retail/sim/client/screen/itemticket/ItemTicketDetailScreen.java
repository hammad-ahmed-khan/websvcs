package oracle.retail.sim.client.screen.itemticket;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Item Ticket Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemTicketDetailScreen extends SimScreen {
    private static final long serialVersionUID = -5808128423056805498L;

    private ItemTicketDetailPanel panel = new ItemTicketDetailPanel();

    public ItemTicketDetailScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Item Tickets Detail";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public void start() throws Exception {
        panel.start();
        displayMenu();
    }

    public void resume() {
        displayMenu();
        panel.resume();
    }

    private void displayMenu() {
        showMenu();

        if (panel.isItemTicketUnmodifiable()) {
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.CANCEL);
            removeNavButton(SimNavigation.PRINT_TICKETS);
        } else {
            removeNavButton(SimNavigation.BACK);
        }
        if (panel.isUINProcessDisabled()) {
            removeNavButton(SimNavigation.UIN_SELECT);
            removeNavButton(SimNavigation.UIN_REMOVE);
        }
        if (panel.isTicketCancelled()) {
            removeNavButton(SimNavigation.UIN_SELECT);
            removeNavButton(SimNavigation.UIN_REMOVE);
            removeNavButton(SimNavigation.PRINT_TICKETS);
        }
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
            if (command.equals(SimNavigation.SAVE)) {
                panel.handleSave();
            } else if (command.equals(SimNavigation.PRINT_TICKETS)) {
                panel.handlePrintTickets();
            } else if (command.equals(SimNavigation.UIN_SELECT)) {
                panel.handleUinLookup();
            } else if (command.equals(SimNavigation.UIN_REMOVE)) {
                panel.handleUinRemove();
            } else if (command.equals(SimNavigation.SCANNER)) {
                panel.handleScanner();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
