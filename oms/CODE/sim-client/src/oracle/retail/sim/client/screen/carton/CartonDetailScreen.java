package oracle.retail.sim.client.screen.carton;

import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Carton Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class CartonDetailScreen extends SimScreen {
    private static final long serialVersionUID = -2290084641485595498L;

    private CartonDetailPanel panel = new CartonDetailPanel();

    public CartonDetailScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Container Detail";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() {
        showMenu();
        panel.start();
    }

    public void stop() {
        panel.stop();
    }
}
