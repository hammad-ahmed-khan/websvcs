package oracle.retail.sim.client.screen.uin;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * UIN Resolution List Screen. Screen that lists all the current UIN problem details and allows the user to perform work on them
 * (update the status, or resolve them).
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UINResolutionListScreen extends SimScreen {
    private static final long serialVersionUID = -1433794880507158602L;

    private UINResolutionListPanel panel = new UINResolutionListPanel();

    public UINResolutionListScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "UIN Resolution List";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
    }

    public void resume() throws Exception {
        showMenu();
        if (RepositoryManager.getStateObject(SimClientStateKey.ITEM_UIN_STATUS_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.ITEM_UIN_STATUS_MODIFIED);
            panel.resolve();
        }
        panel.start();
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_ITEM);
        RepositoryManager.removeStateObject(SimClientStateKey.UIN_ATTRIBUTE_FILTER);
    }

    public void performNavigationEvent(NavigationEvent event) {
        try {
            String command = event.getCommand();
            if (command.equals(SimNavigation.VIEW_HISTORY)) {
                handleViewHistory(event);
            } else if (command.equals(SimNavigation.RESOLVE)) {
                handleResolve(event);
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void handleViewHistory(NavigationEvent event) throws Exception {
        if (panel.handleViewHistory()) {
            return;
        }
        event.consume();
    }

    private void handleResolve(NavigationEvent event) throws Exception {
        if (panel.handleResolve()) {
            return;
        }
        event.consume();
    }
}
