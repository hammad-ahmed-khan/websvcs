package oracle.retail.sim.client.screen.itemrequest;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Item Request List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemRequestListScreen extends SimScreen {
    private static final long serialVersionUID = 7960446264560818878L;

    private ItemRequestListPanel panel = new ItemRequestListPanel();

    public ItemRequestListScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Item Request List";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu(SimNavigation.BACK);
        panel.start();
    }

    public void resume() throws Exception {
        showMenu(SimNavigation.BACK);
        if (RepositoryManager.getStateObject(SimClientStateKey.ITEM_REQUEST_DETAIL_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.ITEM_REQUEST_DETAIL_MODIFIED);
            panel.start();
        }
    }

    public void stop() {
        panel.stop();
    }

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.DELETE)) {
                panel.handleCancelRequest();
            } else if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrint();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }
}
