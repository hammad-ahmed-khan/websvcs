package oracle.retail.sim.client.screen.tolerances;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Tolerances Admin Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TolerancesAdminScreen extends SimScreen {
    private static final long serialVersionUID = 8339544959709341096L;

    private TolerancesAdminPanel panel = new TolerancesAdminPanel();

    public TolerancesAdminScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Tolerances";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu(SimNavigation.SAVE);
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
