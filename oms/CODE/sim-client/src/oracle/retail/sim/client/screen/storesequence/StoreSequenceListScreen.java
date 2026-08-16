package oracle.retail.sim.client.screen.storesequence;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Store Sequence List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreSequenceListScreen extends SimScreen {
    private static final long serialVersionUID = -5564756264237739884L;

    private final String LIST_SCREEN_NAME = "Macro Sequence List";
    private final String EDIT_SCREEN_NAME = "Macro Sequence Edit";

    private StoreSequenceListPanel panel = new StoreSequenceListPanel();
    private String screenName = LIST_SCREEN_NAME;

    public StoreSequenceListScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return screenName;
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() {
        panel.start();
        validateButtons();
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.STORE_SEQUENCE_EDIT_MODE);
        RepositoryManager.removeStateObject(SimClientStateKey.ALL_ITEM_STOCK_COUNT_ACTIVE);
    }

    public void resume() {
        if (RepositoryManager.getStateObject(SimClientStateKey.STORE_SEQUENCE_MODIFIED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.STORE_SEQUENCE_MODIFIED);
            panel.resume();
        }
        validateButtons();
    }

    private void validateButtons() {
        showMenu(SimNavigation.BACK);
        if (panel.isEditMode()) {
            removeNavButton(SimNavigation.BACK);
            removeNavButton(SimNavigation.EDIT_SEQUENCES);
            removeNavButton(SimNavigation.SEARCH);
            screenName = EDIT_SCREEN_NAME;
        } else {
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.APPLY_CLASS_LIST);
            removeNavButton(SimNavigation.ADD_SEQUENCE);
            removeNavButton(SimNavigation.REMOVE_SEQUENCE);
            removeNavButton(SimNavigation.MOVE_DOWN);
            removeNavButton(SimNavigation.MOVE_UP);
            removeNavButton(SimNavigation.CANCEL);
            screenName = LIST_SCREEN_NAME;
        }
        updateScreenName(screenName);
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.CANCEL)) {
                handleCancel(event);
            } else if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            } else if (command.equals(SimNavigation.EDIT_SEQUENCES)) {
                handleEditSequences();
            } else if (command.equals(SimNavigation.ADD_SEQUENCE)) {
                panel.handleAddSequence();
            } else if (command.equals(SimNavigation.REMOVE_SEQUENCE)) {
                panel.handleRemoveSequence();
            } else if (command.equals(SimNavigation.MOVE_UP)) {
                panel.handleMoveUp();
            } else if (command.equals(SimNavigation.MOVE_DOWN)) {
                panel.handleMoveDown();
            } else if (command.equals(SimNavigation.APPLY_CLASS_LIST)) {
                panel.handleApplyClassList();
            } else if (command.equals(SimNavigation.SEARCH)) {
                handleItemLookup(event);
            } else if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrint();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void handleEditSequences() throws Exception {
        panel.handleEditSequences();
        start();
        assignFocusInScreen();
    }

    private void handleItemLookup(NavigationEvent event) {
        if (panel.storeItemForLookup()) {
            RepositoryManager.addStateObject(SimClientStateKey.STORE_SEQUENCE_ITEM_ORIGIN, SimClientStateKey.STORE_SEQUENCE_LIST);
            return;
        }
        event.consume();
    }

    private void handleSave(NavigationEvent event) throws Exception {
        if (panel.handleSave()) {
            event.consume();
            start();
            assignFocusInScreen();
        }
    }

    private void handleCancel(NavigationEvent event) throws Exception {
        if (panel.handleCancel()) {
            event.consume();
            start();
            assignFocusInScreen();
        }
    }
}
