package oracle.retail.sim.client.screen.productgroup;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Product Group Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupDetailScreen extends SimScreen {
    private static final long serialVersionUID = -6121729172886754309L;

    private ProductGroupDetailPanel panel = new ProductGroupDetailPanel();

    public ProductGroupDetailScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Product Group Detail";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        panel.start();
        displayMenu();
    }

    private void displayMenu() throws Exception {
        showMenu();
        if (panel.isProductGroupEditable()) {
            removeNavButton(SimNavigation.BACK);
        } else {
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.CANCEL);
        }
    }

    public void stop() {
        panel.stop();
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            } else if (command.equals(SimNavigation.CANCEL)) {
                handleCancel(event);
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    /****************************************************************************************************
     * Handle Cancel
     ***************************************************************************************************/

    private void handleCancel(NavigationEvent event) throws Exception {
        if (panel.handleCancel()) {
            return;
        }
        event.consume();
    }

    /****************************************************************************************************
     * Handle Done
     ***************************************************************************************************/

    private void handleSave(NavigationEvent event) throws Exception {
        if (panel.handleSave()) {
            return;
        }
        event.consume();
    }

}
