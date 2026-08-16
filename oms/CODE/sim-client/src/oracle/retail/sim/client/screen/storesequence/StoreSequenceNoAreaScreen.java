package oracle.retail.sim.client.screen.storesequence;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Store Sequence No Area Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreSequenceNoAreaScreen extends SimScreen {
    private static final long serialVersionUID = -8190373655077730756L;

    private StoreSequenceNoAreaPanel panel = new StoreSequenceNoAreaPanel();

    public StoreSequenceNoAreaScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "No Location List";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu(SimNavigation.SAVE);
        panel.start();
    }

    public void resume() throws Exception {
        showMenu(SimNavigation.SAVE);
        panel.resume();
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.STORE_SEQUENCE_ITEM_FILTER);
        panel.stop();
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
