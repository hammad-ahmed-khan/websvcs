package oracle.retail.sim.client.screen.itemprice;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Price Change List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemPriceListScreen extends SimScreen {
    private static final long serialVersionUID = 1005368108357164611L;

    private ItemPriceListPanel panel = new ItemPriceListPanel();

    public ItemPriceListScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Price Change List";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
    }

    public void resume() throws Exception {
        showMenu();
        if (RepositoryManager.getStateObject(SimClientStateKey.PRICE_CHANGE_DETAIL_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.PRICE_CHANGE_DETAIL_MODIFIED);
            panel.refreshScreen();
        }
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.PRICE_CHANGE_FILTER);
        RepositoryManager.addStateObject(SimClientStateKey.PRICE_CHANGE_DONE, Boolean.TRUE);
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.CREATE)) {
                panel.handleCreate();
            } else if (command.equals(SimNavigation.ITEM_TICKET)) {
                panel.handleItemTickets();
            } else if (command.equals(SimNavigation.SHELF_LABELS)) {
                panel.handleShelfLabels();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
