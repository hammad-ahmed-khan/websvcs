package oracle.retail.sim.client.screen.finisher;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Finisher Lookup Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FinisherLookupScreen extends SimScreen {
    private static final long serialVersionUID = -6603326409338670929L;

    private FinisherLookupPanel panel = new FinisherLookupPanel();

    public FinisherLookupScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Finisher Lookup";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() {
        showMenu(SimNavigation.SEARCH);
        panel.start();
    }

    public void resume() {
        showMenu(SimNavigation.SEARCH);
    }

    /****************************************************************************************************
     * Handle Navigation Eventss
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SEARCH)) {
                panel.handleSearch();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }
}
