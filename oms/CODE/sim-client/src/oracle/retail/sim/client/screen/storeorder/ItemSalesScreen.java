package oracle.retail.sim.client.screen.storeorder;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Item Sales Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemSalesScreen extends SimScreen {
    private static final long serialVersionUID = -4942018007525002147L;

    private ItemSalesPanel panel = new ItemSalesPanel();

    public ItemSalesScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Item Sales";
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
