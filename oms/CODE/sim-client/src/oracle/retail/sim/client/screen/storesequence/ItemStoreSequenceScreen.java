package oracle.retail.sim.client.screen.storesequence;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Item Store Sequence Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemStoreSequenceScreen extends SimScreen {
    private static final long serialVersionUID = 8378187673930148107L;

    private ItemStoreSequencePanel panel = new ItemStoreSequencePanel();

    public ItemStoreSequenceScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Item Locations List";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        if (panel.isItemSequenceUnmodifiable()) {
            removeNavButton(SimNavigation.ADD_SEQUENCE);
            removeNavButton(SimNavigation.REMOVE_SEQUENCE);
            removeNavButton(SimNavigation.CANCEL);
            removeNavButton(SimNavigation.SAVE);
        } else {
            removeNavButton(SimNavigation.BACK);
        }
        if (panel.isActiveAllItemCount()) {
            removeNavButton(SimNavigation.ADD_SEQUENCE);
            removeNavButton(SimNavigation.REMOVE_SEQUENCE);
        }
        panel.start();
    }

    public void stop() {
        panel.stop();
        RepositoryManager.removeStateObject(SimClientStateKey.STORE_SEQUENCE_ITEM_ORIGIN);
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            } else if (command.equals(SimNavigation.ADD_SEQUENCE)) {
                panel.handleAddSequence();
            } else if (command.equals(SimNavigation.REMOVE_SEQUENCE)) {
                panel.handleRemoveSequence();
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
