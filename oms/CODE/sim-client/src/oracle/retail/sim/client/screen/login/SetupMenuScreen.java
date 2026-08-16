package oracle.retail.sim.client.screen.login;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimBackgroundPanel;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;

/********************************************************************************************************
 * Setup Menu Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SetupMenuScreen extends SimScreen {
    private static final long serialVersionUID = 9194248608602541040L;

    public SetupMenuScreen() {
        add(new SimBackgroundPanel());
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Setup";
    }

    public ScreenPanel getScreenPanel() {
        return null;
    }

    public void start() {
        displayMenu();
    }

    public void resume() {
        displayMenu();
    }

    private void displayMenu() {
        showMenu();
        if (!SimConfigManager.getBoolean(SimConfigManager.AUTO_DEFAULT_UIN_ATTRIBUTES)) {
            removeNavButton(SimNavigation.UIN_ATTRIBUTES);
        }
        if (!SimConfigManager.getStoreBoolean(StoreConfigKeys.UIN_PROCESSING_ENABLED, SimRepository.getStoreId())) {
            removeNavButton(SimNavigation.UIN_ATTRIBUTES);
        }
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
    }

}
