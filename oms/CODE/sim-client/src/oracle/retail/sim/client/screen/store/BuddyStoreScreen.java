package oracle.retail.sim.client.screen.store;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Buddy Store Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class BuddyStoreScreen extends SimScreen {
    private static final long serialVersionUID = 5805898917096684599L;

    private BuddyStorePanel panel = new BuddyStorePanel();

    public BuddyStoreScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Buddy Stores";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() {
        showMenu();
        panel.start();
    }

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                panel.handleSave();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
