package oracle.retail.sim.client.screen.login;

import oracle.retail.sim.client.core.SimBackgroundPanel;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Shipping Menu Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ShippingMenuScreen extends SimScreen {
    private static final long serialVersionUID = 5385358634319586473L;

    public ShippingMenuScreen() {
        add(new SimBackgroundPanel());
    }

    public String getScreenName() {
        return "Shipping/Receiving";
    }

    public ScreenPanel getScreenPanel() {
        return null;
    }

    public void start() {
        showMenu();
    }
}
