package oracle.retail.sim.client.screen.shelfreplenishment;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Shelf Replenishment List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ShelfReplenishmentListScreen extends SimScreen {
    private static final long serialVersionUID = -4921384531683708647L;

    private ShelfReplenishmentListPanel panel = new ShelfReplenishmentListPanel();

    public ShelfReplenishmentListScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Shelf Replenishment List";
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
        if (RepositoryManager.getStateObject(SimClientStateKey.SHELF_REPLENISHMENT_DETAIL_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.SHELF_REPLENISHMENT_DETAIL_MODIFIED);
            panel.start();
        }
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.SHELF_REPLENISHMENT_FILTER);
    }

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.DELETE)) {
                panel.handleCancelReplenishment();
            } else if (command.equals(SimNavigation.REFRESH)) {
                panel.handleRefresh();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
