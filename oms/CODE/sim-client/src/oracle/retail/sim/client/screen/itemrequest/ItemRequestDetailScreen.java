package oracle.retail.sim.client.screen.itemrequest;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.CommonMessageText;

/*******************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
 * Item Request Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/

public class ItemRequestDetailScreen extends SimScreen {
    private static final long serialVersionUID = -5445661725984877885L;

    private ItemRequestDetailPanel panel = new ItemRequestDetailPanel();

    public ItemRequestDetailScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Item Request Detail";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * State Management
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/

    public boolean isStartable() {
        try {
            panel.loadItemRequest();
        } catch (Throwable exception) {
            displayException(exception);
            return false;
        }
        return true;
    }

    public void start() throws Exception {
        panel.start();
        displayMenu();
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_ITEM_REQUEST);
    }

    private void displayMenu() throws Exception {
        showMenu();

        if (panel.isItemRequestUnmodifiable()) {
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.CANCEL);
            removeNavButton(SimNavigation.ADD_ITEM);
            removeNavButton(SimNavigation.REMOVE_ITEM);
            removeNavButton(SimNavigation.REQUEST);
            removeNavButton(SimNavigation.PRINT);
            removeNavButton(SimNavigation.SCANNER);
            return;
        }
        if (panel.validateIsNotPending()) {
            removeNavButton(SimNavigation.ADD_ITEM);
            removeNavButton(SimNavigation.REMOVE_ITEM);
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.CANCEL);
            removeNavButton(SimNavigation.REQUEST);
        } else {
            removeNavButton(SimNavigation.BACK);
        }
        if (!panel.isAddItemAvailable()) {
            removeNavButton(SimNavigation.ADD_ITEM);
        }
        if (!panel.isScannerAvailable()) {
            removeNavButton(SimNavigation.SCANNER);
        }
    }

    public void stop() {
        panel.stop();
    }

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * Handle Navigation Events
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.BACK)) {
                panel.clearScreen();
            } else if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            } else if (command.equals(SimNavigation.ADD_ITEM)) {
                panel.handleAddItem();
            } else if (command.equals(SimNavigation.REMOVE_ITEM)) {
                handleRemoveItem();
            } else if (command.equals(SimNavigation.REQUEST)) {
                handleRequest(event);
            } else if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrint();
            } else if (command.equals(SimNavigation.SCANNER)) {
                panel.handleScanner();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    public void handleSave(NavigationEvent event) throws Exception {
        if (validItemRequest() && panel.handleSave()) {
            return;
        }
        event.consume();
    }

    public void handleRequest(NavigationEvent event) throws Exception {
        if (validItemRequest() && panel.handleRequest()) {
            return;
        }
        event.consume();
    }

    public void handleRemoveItem() throws Exception {
        panel.handleRemoveItem();
        validItemRequest();
    }

    private boolean validItemRequest() throws Exception {
        if (panel.isRequestEmpty()) {
            if (RConfirmUtility.confirm("Item Delete Confirmation", CommonMessageText.NO_ROWS_REMAINING)) {
                panel.cancelEmptyRequest();
                navigate(SimNavigation.PREVIOUS_SCREEN);
                return false;
            }
        }
        return true;
    }
}
