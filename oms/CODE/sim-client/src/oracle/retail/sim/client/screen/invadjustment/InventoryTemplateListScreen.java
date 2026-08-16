package oracle.retail.sim.client.screen.invadjustment;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Inventory Template List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryTemplateListScreen extends SimScreen {
    private static final long serialVersionUID = -3546787145373965855L;

    private InventoryTemplateListPanel panel = new InventoryTemplateListPanel();

    public InventoryTemplateListScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/
    public String getScreenName() {
        return "Template List";
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
        if (RepositoryManager.getStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_TEMPLATE_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_TEMPLATE_MODIFIED);
            panel.start();
        }
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_TEMPLATE_FILTER);
        RepositoryManager.removeStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_TEMPLATE_FILTER_MODIFIED);
        RepositoryManager.removeStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_TEMPLATE_MODIFIED);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_INVENTORY_ADJUSTMENT_TEMPLATE);
    }

    /****************************************************************************************************
     * Navigation Methods
     ***************************************************************************************************/
    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.DELETE)) {
                panel.handleCancelTemplate();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}