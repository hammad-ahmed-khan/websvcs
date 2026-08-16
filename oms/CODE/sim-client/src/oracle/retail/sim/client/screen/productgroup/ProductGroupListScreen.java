package oracle.retail.sim.client.screen.productgroup;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Product Group List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupListScreen extends SimScreen {
    private static final long serialVersionUID = -4291398947369276696L;

    private ProductGroupListPanel panel = new ProductGroupListPanel();

    public ProductGroupListScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Product Group List";
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
        if (RepositoryManager.getStateObject(SimClientStateKey.PRODUCT_GROUP_DETAIL_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.PRODUCT_GROUP_DETAIL_MODIFIED);
            panel.start();
        }
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.PRODUCT_GROUP_FILTER);
    }

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        if (command.equals(SimNavigation.DELETE)) {
            panel.handleCancelGroup();
        }
    }
}
