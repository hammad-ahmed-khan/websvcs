package oracle.retail.sim.client.screen.printer;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Session Printer Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SessionPrinterScreen extends SimScreen {
    private static final long serialVersionUID = -7616536278775126051L;

    private SessionPrinterPanel panel = new SessionPrinterPanel();

    public SessionPrinterScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Session Printer";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu(SimNavigation.SAVE);
        panel.start();
    }

    public void resume() {
        showMenu(SimNavigation.SAVE);
    }

    public void stop() {
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

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
