package oracle.retail.sim.client.screen.theme;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Font Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FontDetailScreen extends SimScreen {
    private static final long serialVersionUID = -4140395909063312886L;

    private FontDetailPanel panel = new FontDetailPanel();

    /****************************************************************************************************
     * Constructors
     ***************************************************************************************************/

    public FontDetailScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Font Detail";
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
                panel.doResetFont();
            } else if (command.equals(SimNavigation.EDIT)) {
                panel.doEditFont();
            } else if (command.equals(SimNavigation.APPLY_TO_ALL)) {
                panel.doApplyToAll();
            } else if (command.equals(SimNavigation.SAVE)) {
                panel.doSaveFonts();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }
}
