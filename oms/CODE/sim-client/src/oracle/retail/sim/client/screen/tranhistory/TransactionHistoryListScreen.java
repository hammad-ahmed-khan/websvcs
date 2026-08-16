package oracle.retail.sim.client.screen.tranhistory;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Transaction History List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransactionHistoryListScreen extends SimScreen {
    private static final long serialVersionUID = 3800120879384941874L;

    private TransactionHistoryListPanel panel = new TransactionHistoryListPanel();

    public TransactionHistoryListScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Transaction History List";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.TRANSACTION_HISTORY_FILTER);
    }
}
