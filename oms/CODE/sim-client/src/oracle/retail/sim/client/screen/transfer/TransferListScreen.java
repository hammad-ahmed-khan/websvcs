package oracle.retail.sim.client.screen.transfer;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Transfer List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferListScreen extends SimScreen {
    private static final long serialVersionUID = 6223997925352878110L;

    private TransferListPanel panel = new TransferListPanel();

    public TransferListScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Transfer List";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu(SimNavigation.BACK);
        panel.start();
    }

    public void resume() throws Exception {
        showMenu(SimNavigation.BACK);
        if (RepositoryManager.getStateObject(SimClientStateKey.TRANSFER_DETAIL_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.TRANSFER_DETAIL_MODIFIED);
            panel.start();
        }
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.TRANSFER_FILTER);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_TRANSFER);
    }

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.DISPATCH)) {
                panel.handleDispatch();
            } else if (command.equals(SimNavigation.DELETE)) {
                panel.handleCancelTransfer();
            } else if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrint();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
