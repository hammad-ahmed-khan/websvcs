package oracle.retail.sim.client.screen.login;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimBackgroundPanel;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.configutil.SimConfigManager;

/********************************************************************************************************
 * Lookup Menu Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class LookupMenuScreen extends SimScreen {
    private static final long serialVersionUID = -4574215843047535850L;

    public LookupMenuScreen() {
        add(new SimBackgroundPanel());
    }

    public String getScreenName() {
        return "Lookups";
    }

    public ScreenPanel getScreenPanel() {
        return null;
    }

    public void start() {
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_ITEM);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_SUPPLIER);
        displayMenu();
    }

    private void displayMenu() {
        showMenu();
        if (!SimConfigManager.getBoolean(SimConfigManager.EXTERNAL_FINISHER_ENABLED)) {
            removeNavButton(SimNavigation.FINISHER_LOOKUP);
        }
    }

    public void resume() throws Exception {
        displayMenu();
    }
}
