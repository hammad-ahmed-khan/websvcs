package oracle.retail.sim.client.screen.login;

import oracle.retail.sim.client.core.SimBackgroundPanel;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Technical Maintenance Menu Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TechMenuScreen extends SimScreen {
    private static final long serialVersionUID = 9194248608602541040L;

    public TechMenuScreen() {
        add(new SimBackgroundPanel());
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Technical Maintenance";
    }

    public ScreenPanel getScreenPanel() {
        return null;
    }

    public void start() {
        showMenu();
    }

    public void resume() {
        showMenu();
    }
}
