package oracle.retail.sim.client.screen.login;

import oracle.retail.sim.client.core.SimBackgroundPanel;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * UI Config Menu Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UIConfigMenuScreen extends SimScreen {
    private static final long serialVersionUID = -4574215843047535850L;

    public UIConfigMenuScreen() {
        add(new SimBackgroundPanel());
    }

    public String getScreenName() {
        return "UI Config";
    }

    public ScreenPanel getScreenPanel() {
        return null;
    }

    public void start() {
        showMenu();
    }
}
