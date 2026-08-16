package extra.retail.sim.client.screen.item;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Item Lookup Screen
 * <p>
 * The item lookup screen can have four different purposes determined by the LOOKUP TYPE in the state
 * repository. If null, that means this screen came from the main menu. If STOCK_ITEM_LOOKUP, then a
 * StockItem should be assigned as the selected item. If VALUE_ITEM_LOOKUP, then an ItemVO should be
 * assigned as the selected item.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraItemLookupScreen extends SimScreen {
    private static final long serialVersionUID = 4578922934781018233L;

    private ExtraItemLookupPanel panel = new ExtraItemLookupPanel();

    public ExtraItemLookupScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Item Lookup";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() {
        showMenu(SimNavigation.SEARCH);
        panel.start();
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.QUICK_JUMP_ITEM);
    }

    public void resume() {
        showMenu(SimNavigation.SEARCH);
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SEARCH)) {
                panel.handleSearch();
            } else if (command.equals(SimNavigation.RESET)) {
                panel.handleReset();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
