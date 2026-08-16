package oracle.retail.sim.client.screen.billoflading;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Bill Of Lading Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class BillOfLadingDetailScreen extends SimScreen {
    private static final long serialVersionUID = 6135592157771665683L;

    private BillOfLadingDetailPanel panel = new BillOfLadingDetailPanel();

    public BillOfLadingDetailScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "BOL Detail";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        panel.start();
        displayMenu();
    }

    private void displayMenu() {
        showMenu();

        if (!panel.isEditable()) {
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.CANCEL);
        } else {
            removeNavButton(SimNavigation.BACK);
        }
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.BACK)) {
                handleCancel();
            } else if (command.equals(SimNavigation.CANCEL)) {
                handleCancel();
            } else if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void handleCancel() throws Exception {
        panel.handleCancel();
    }

    private void handleSave(NavigationEvent event) throws Exception {
        if (!panel.handleSave()) {
            event.consume();
        }
        panel.handleCancel();
    }
}
