package oracle.retail.sim.client.screen.storesequence;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Store Sequence Item List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreSequenceItemScreen extends SimScreen {
    private static final long serialVersionUID = -5918165326462330447L;

    private final String LIST_SCREEN_NAME = "Micro Sequence List";
    private final String EDIT_SCREEN_NAME = "Micro Sequence Edit";

    private StoreSequenceItemPanel panel = new StoreSequenceItemPanel();
    private String screenName = LIST_SCREEN_NAME;

    public StoreSequenceItemScreen() {
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

    public void start() throws Exception {
        showMenu();
        validateButtons();
        panel.start();
        validateSequencingButtons();
    }

    // We do not remove the state object on resume, because the store sequence screen still needs to see
    // the flag. This is not optimal for performance but is required functionality at the moment.
    public void resume() throws Exception {
        showMenu();
        validateButtons();
        if (RepositoryManager.getStateObject(SimClientStateKey.STORE_SEQUENCE_MODIFIED) != null) {
            panel.start();
        } else {
            panel.resume();
        }
        validateSequencingButtons();
    }

    private void validateButtons() {
        if (panel.isSequencingUnmodifiable()) {
            removeNavButton(SimNavigation.EDIT_ITEMS);
            removeNavButton(SimNavigation.APPLY_ITEM_LIST);
            removeNavButton(SimNavigation.ADD_ITEM);
            removeNavButton(SimNavigation.REMOVE_ITEM);
            removeNavButton(SimNavigation.MOVE_UP);
            removeNavButton(SimNavigation.MOVE_DOWN);
            removeNavButton(SimNavigation.CANCEL);
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.SCANNER);
            return;
        }
        if (panel.isAllItemStockCountActive()) {
            removeNavButton(SimNavigation.EDIT_ITEMS);
        }
        if (!panel.isScannerAvailable()) {
            removeNavButton(SimNavigation.SCANNER);
        }
        if (panel.isEditMode()) {
            removeNavButton(SimNavigation.BACK);
            removeNavButton(SimNavigation.EDIT_ITEMS);
            screenName = EDIT_SCREEN_NAME;
        } else {
            removeNavButton(SimNavigation.APPLY_ITEM_LIST);
            removeNavButton(SimNavigation.ADD_ITEM);
            removeNavButton(SimNavigation.REMOVE_ITEM);
            removeNavButton(SimNavigation.MOVE_UP);
            removeNavButton(SimNavigation.MOVE_DOWN);
            removeNavButton(SimNavigation.CANCEL);
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.SCANNER);
            screenName = LIST_SCREEN_NAME;
        }
        updateScreenName(screenName);
    }

    private void validateSequencingButtons() {
        if (panel.isEditMode() && panel.isNotSequenced()) {
            removeNavButton(SimNavigation.MOVE_DOWN);
            removeNavButton(SimNavigation.MOVE_UP);
        }
    }

    public void stop() {
        panel.stop();
        RepositoryManager.removeStateObject(SimClientStateKey.STORE_SEQUENCE_ITEM_EDIT_MODE);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_STORE_SEQUENCE);
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                handleDone(event);
            } else if (command.equals(SimNavigation.APPLY_ITEM_LIST)) {
                panel.handleApplyItemList();
            } else if (command.equals(SimNavigation.ADD_ITEM)) {
                panel.handleAdd();
            } else if (command.equals(SimNavigation.REMOVE_ITEM)) {
                panel.handleRemoveItem();
            } else if (command.equals(SimNavigation.CANCEL)) {
                handleCancel(event);
            } else if (command.equals(SimNavigation.MOVE_UP)) {
                panel.handleMoveUp();
            } else if (command.equals(SimNavigation.MOVE_DOWN)) {
                panel.handleMoveDown();
            } else if (command.equals(SimNavigation.SCANNER)) {
                panel.handleScanner();
            } else if (command.equals(SimNavigation.EDIT_ITEMS)) {
                handleEditItemLocations();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void handleCancel(NavigationEvent event) throws Exception {
        panel.clearScreen();
        panel.releaseActivityLock();
        if (panel.isEditMode()) {
            RepositoryManager.removeStateObject(SimClientStateKey.STORE_SEQUENCE_ITEM_EDIT_MODE);
            event.consume();
            start();
            return;
        }
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_STORE_SEQUENCE);
    }

    private void handleDone(NavigationEvent event) throws Exception {
        if (panel.handleSave()) {
            handleCancel(event);
            return;
        }
        event.consume();
    }

    private void handleEditItemLocations() throws Exception {
        panel.handleEditItemLocations();
        start();
    }
}
