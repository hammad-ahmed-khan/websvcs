package oracle.retail.sim.client.screen.itemticket;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Item Ticket List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemTicketListScreen extends SimScreen {
    private static final long serialVersionUID = 5796857166670559059L;

    private ItemTicketListPanel panel = new ItemTicketListPanel();

    public ItemTicketListScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Item Tickets List";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    /****************************************************************************************************
     * State Management
     ***************************************************************************************************/

    public void start() throws Exception {
        showMenu();
        panel.start();
    }

    public void resume() throws Exception {
        showMenu();
        if (RepositoryManager.getStateObject(SimClientStateKey.ITEM_TICKET_DETAIL_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.ITEM_TICKET_DETAIL_MODIFIED);
        }
        panel.resume();
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.ITEM_TICKET_FILTER);
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.PRINT_TICKETS)) {
                panel.handlePrintTickets();
            } else if (command.equals(SimNavigation.CREATE)) {
                panel.handleCreate();
            } else if (command.equals(SimNavigation.UPDATE_SOH)) {
                panel.handleUpdateStockOnHand();
            } else if (command.equals(SimNavigation.DELETE)) {
                panel.handleDelete();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
