package oracle.retail.sim.client.screen.store;

import oracle.retail.sim.client.core.SimBackgroundPanel;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Store Admin Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class StoreAdminScreen extends SimScreen {
    private static final long serialVersionUID = -8724336247175191254L;

    public StoreAdminScreen() {
        add(new SimBackgroundPanel());
    }

    public String getScreenName() {
        return "SIM Stores";
    }

    public ScreenPanel getScreenPanel() {
        return null;
    }

    public void start() {
        showMenu();
    }
}
