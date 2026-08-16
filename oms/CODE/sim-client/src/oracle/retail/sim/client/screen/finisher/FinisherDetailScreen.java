package oracle.retail.sim.client.screen.finisher;

import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Finisher Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FinisherDetailScreen extends SimScreen {
    private static final long serialVersionUID = 5431414646606033654L;

    private FinisherDetailPanel panel = new FinisherDetailPanel();

    public FinisherDetailScreen() {
        add(panel);
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    /****************************************************************************************************
     * Screen methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Finisher Detail";
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
    }
}
