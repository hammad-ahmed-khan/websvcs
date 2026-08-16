package oracle.retail.sim.client.screen.productgroup;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Product Group Schedule List Screen - Lists all product group schedules the system knows about.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupScheduleListScreen extends SimScreen {
    private static final long serialVersionUID = 8125776461165478729L;

    private ProductGroupScheduleListPanel panel = new ProductGroupScheduleListPanel();

    public ProductGroupScheduleListScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Product Group Schedule List";
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
        if (RepositoryManager.getStateObject(SimClientStateKey.PRODUCT_GROUP_SCHEDULE_DETAIL_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.PRODUCT_GROUP_SCHEDULE_DETAIL_MODIFIED);
            panel.start();
        }
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.PRODUCT_GROUP_SCHEDULE_FILTER);
    }

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.DELETE)) {
                panel.handleDelete();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
