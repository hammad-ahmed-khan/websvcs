package oracle.retail.sim.client.screen.invadjustment;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.common.business.CommonMessageText;

/********************************************************************************************************
 * Inventory Template Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryTemplateDetailScreen extends SimScreen {
    private static final long serialVersionUID = 1626846855825065766L;

    private InventoryTemplateDetailPanel panel = new InventoryTemplateDetailPanel();

    public InventoryTemplateDetailScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/
    public String getScreenName() {
        return "Template Detail";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
        validateButtons();
    }

    private void validateButtons() {
        if (panel.isTemplateEditable()) {
            removeNavButton(SimNavigation.BACK);
        } else {
            removeNavButton(SimNavigation.ADD_ITEM);
            removeNavButton(SimNavigation.REMOVE_ITEM);
            removeNavButton(SimNavigation.CONFIRM);
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.CANCEL);
        }
    }

    /****************************************************************************************************
     * Navigation Methods
     ***************************************************************************************************/
    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            } else if (command.equals(SimNavigation.ADD_ITEM)) {
                handleAddItem();
            } else if (command.equals(SimNavigation.REMOVE_ITEM)) {
                handleRemoveItem();
            } else if (command.equals(SimNavigation.CONFIRM)) {
                handleConfirm(event);
            } else if (command.equals(SimNavigation.CANCEL)) {
                handleCancel();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void handleAddItem() throws Exception {
        if (confirmActivityLock()) {
            panel.handleAddItem();
        }
    }

    private void handleRemoveItem() throws Exception {
        if (confirmActivityLock()) {
            panel.handleRemoveItem();
        }
    }

    private void handleConfirm(NavigationEvent event) throws Exception {
        if (!panel.isTemplateEditable()) {
            return;
        }
        if (!confirmActivityLock()) {
            event.consume();
            return;
        }
        if (panel.handleConfirm()) {
            return;
        }
        event.consume();
    }

    private void handleSave(NavigationEvent event) throws Exception {
        if (!confirmActivityLock()) {
            event.consume();
            return;
        }
        if (panel.handleSave()) {
            return;
        }
        event.consume();
    }

    public void handleCancel() throws Exception {
        panel.handleCancel();
    }

    /****************************************************************************************************
     * Helper Methods
     ***************************************************************************************************/

    private boolean confirmActivityLock() throws Exception {
        if (panel.confirmTemplateLock()) {
            return true;
        }
        displayException(CommonMessageText.LOCK_TAKEN_OVER);
        navigate(SimNavigation.PREVIOUS_SCREEN);
        return false;
    }
}