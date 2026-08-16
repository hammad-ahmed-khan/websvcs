package oracle.retail.sim.client.screen.uin;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * UIN Update Status Screen. This screen allows the user to view the history information for a UIN and
 * update the status of the UIN manually.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UINHistoryScreen extends SimScreen {
    private static final long serialVersionUID = -478330955274635628L;

    private UINHistoryPanel panel = new UINHistoryPanel();

    public UINHistoryScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "UIN History";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
        validateButtons();
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.ITEM_UIN_PROBLEM);
    }

    private void validateButtons() {
        if (panel.isSerialNumberEditable()) {
            removeNavButton(SimNavigation.BACK);
        } else {
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.CANCEL);
        }
    }

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void handleSave(NavigationEvent event) throws Exception {
        if (panel.handleSave()) {
            return;
        }
        event.consume();
    }
}
