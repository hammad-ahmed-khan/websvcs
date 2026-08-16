package oracle.retail.sim.client.screen.storeorder;

import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Deals Query Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DealsQueryScreen extends SimScreen {
    private static final long serialVersionUID = 1522256997252495041L;

    private DealsQueryPanel panel = new DealsQueryPanel();

    public DealsQueryScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Deals Query";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() {
        showMenu();
        panel.start();
    }
}
