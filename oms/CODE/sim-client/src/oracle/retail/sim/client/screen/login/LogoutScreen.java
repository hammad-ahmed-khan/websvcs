package oracle.retail.sim.client.screen.login;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimBackgroundPanel;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.security.SimLoginManager;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Logout Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class LogoutScreen extends LoginScreen {
    private static final long serialVersionUID = 3739821322712195701L;

    public LogoutScreen() {
        add(new SimBackgroundPanel());
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Logout";
    }

    public ScreenPanel getScreenPanel() {
        return null;
    }

    public void start() {
        showMenu();
        updateStatusBar();
    }

    /****************************************************************************************************
     * Handle Navigation Event
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        if (command.equals(SimNavigation.EXIT)) {
            doExit();
        } else if (command.equals(SimNavigation.LOGIN)) {
            doLogin(event);
        }
    }

    /****************************************************************************************************
     * Login
     ***************************************************************************************************/

    protected void doLogin(NavigationEvent event) {
        event.consume();
        if (SimLoginManager.login()) {
            updateBackgroundFrame();
            Application.getNavigationManager().showDefaultScreen();
        }
    }

    /****************************************************************************************************
     * Exit
     ***************************************************************************************************/

    private void doExit() {
        Application.getFrame().closeWindow();
    }
}
