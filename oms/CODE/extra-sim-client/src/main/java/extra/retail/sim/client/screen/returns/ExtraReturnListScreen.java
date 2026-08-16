package extra.retail.sim.client.screen.returns;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Return List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraReturnListScreen extends SimScreen {
    private static final long serialVersionUID = 3029107439863605895L;

    private ExtraReturnListPanel panel = new ExtraReturnListPanel();

    public ExtraReturnListScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Return List";
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
        if (panel.isCancelReturnNotAvailable()) {
            removeNavButton(SimNavigation.DELETE);
        }
        if (panel.isDispatchNotAvailable()) {
            removeNavButton(SimNavigation.DISPATCH);
        }
    }

    public void resume() throws Exception {
        displayMenu();
        if (RepositoryManager.getStateObject(SimClientStateKey.RETURN_DETAIL_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.RETURN_DETAIL_MODIFIED);
            panel.start();
        }
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.RETURN_FILTER);
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.DISPATCH)) {
                panel.handleDispatch();
            } else if (command.equals(SimNavigation.DELETE)) {
                panel.handleCancelReturn();
            } else if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrint();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
