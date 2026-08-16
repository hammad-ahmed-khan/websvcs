package oracle.retail.sim.client.screen.login;

import oracle.retail.sim.client.core.SimBackgroundPanel;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.common.security.SecurityUtility;
import oracle.retail.sim.common.security.UserSecurityMode;

/********************************************************************************************************
 * Security Menu Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SecurityMenuScreen extends SimScreen {
    private static final long serialVersionUID = 1535213307646824041L;

    public SecurityMenuScreen() {
        add(new SimBackgroundPanel());
    }

    public String getScreenName() {
        return "Security";
    }

    public ScreenPanel getScreenPanel() {
        return null;
    }

    public void start() throws Exception {
        displayMenu();
    }

    public void resume() throws Exception {
        displayMenu();
    }

    private void displayMenu() {
        showMenu();

        if (SecurityUtility.getUserSecurityMode() == UserSecurityMode.EXTERNAL) {
            removeNavButton(SimNavigation.PASSWORD_CONFIGURATION);
            removeNavButton(SimNavigation.USER_MAINTENANCE);
            removeNavButton(SimNavigation.MASS_ASSIGN_ROLES);
            removeNavButton(SimNavigation.MASS_ASSIGN_STORES);
        }
    }
}
