package oracle.retail.sim.client.screen.storeorder;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Item Orders Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemOrdersScreen extends SimScreen {
    private static final long serialVersionUID = 1377419605469627175L;

    private ItemOrdersPanel panel = new ItemOrdersPanel();

    public ItemOrdersScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Item Orders";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() {
        showMenu(SimNavigation.BACK);
        panel.start();
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_ITEM);
    }
}
