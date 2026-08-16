package oracle.retail.sim.client.screen.carton;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Carton Lookup Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CartonLookupScreen extends SimScreen {
    private static final long serialVersionUID = 3844965466683011131L;

    private CartonLookupPanel panel = new CartonLookupPanel();

    public CartonLookupScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Container Lookup";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() {
        showMenu(SimNavigation.SEARCH);
        panel.start();
    }

    public void resume() {
        start();
    }

    public void stop() {
        panel.stop();
    }

    /****************************************************************************************************
     * Handle navigation events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SEARCH)) {
                panel.handleSearch();
            }
        } catch (Throwable e) {
            displayException(e);
        }
    }
}
