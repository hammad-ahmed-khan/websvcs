package oracle.retail.sim.client.screen.login;

import oracle.retail.sim.client.core.SimBackgroundPanel;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;

/********************************************************************************************************
 * Admin Menu Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class AdminMenuScreen extends SimScreen {
    private static final long serialVersionUID = 3347729955423385352L;

    public AdminMenuScreen() {
        add(new SimBackgroundPanel());
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Admin";
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

    public void displayMenu() {
        showMenu();

        if (!SimConfigManager.getStoreBoolean(StoreConfigKeys.UIN_PROCESSING_ENABLED, SimRepository.getStoreId())) {
            removeNavButton(SimNavigation.UIN_RESOLUTION);
        }
    }
}
