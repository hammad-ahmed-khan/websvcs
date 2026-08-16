package oracle.retail.sim.client.screen.theme;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Theme List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ThemeListScreen extends SimScreen {
    private static final long serialVersionUID = -4140395909063312886L;

    private ThemeListPanel panel = new ThemeListPanel();

    /****************************************************************************************************
     * Constructors
     ***************************************************************************************************/

    public ThemeListScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Available Themes";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_THEME);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_THEME_DETAIL);
    }

    /****************************************************************************************************
     * Handle Navigation Event
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.CREATE)) {
                panel.doCreateTheme();
            } else if (command.equals(SimNavigation.FONTS)) {
                panel.doStoreTheme();
            } else if (command.equals(SimNavigation.COLORS)) {
                panel.doStoreTheme();
            } else if (command.equals(SimNavigation.ICONS)) {
                panel.doStoreTheme();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

}
