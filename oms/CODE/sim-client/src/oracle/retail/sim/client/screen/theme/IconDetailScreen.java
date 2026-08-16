package oracle.retail.sim.client.screen.theme;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Icon Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class IconDetailScreen extends SimScreen {
    private static final long serialVersionUID = -4140395909063312886L;

    private IconDetailPanel panel = new IconDetailPanel();

    /****************************************************************************************************
     * Constructors
     ***************************************************************************************************/

    public IconDetailScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Icon Detail";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
    }

    /****************************************************************************************************
     * Handle Navigation Event
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.RESET)) {
                panel.doResetIcon();
            } else if (command.equals(SimNavigation.EDIT)) {
                panel.doEditIcon();
            } else if (command.equals(SimNavigation.SAVE)) {
                panel.doSaveIcons();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }
}
