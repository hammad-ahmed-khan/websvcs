package oracle.retail.sim.client.screen.uin;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * UIN Attribute Screen.
 * <p>
 * This allows a user to setup the UIN attributes at the class level within the merchandise hierarchy.
 * The process will take the attributes and propagate them down to each item/location for all items
 * within the class. This screen should not be used if the RMS environment is present.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class UINAttributeScreen extends SimScreen {
    private static final long serialVersionUID = 2042409678032942937L;

    private UINAttributePanel panel = new UINAttributePanel();

    public UINAttributeScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "UIN Attributes";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                doSave(event);
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void doSave(NavigationEvent event) throws Exception {
        if (panel.handleSave()) {
            return;
        }
        event.consume();
    }
}
