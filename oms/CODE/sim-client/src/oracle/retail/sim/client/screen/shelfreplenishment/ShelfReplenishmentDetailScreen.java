package oracle.retail.sim.client.screen.shelfreplenishment;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Shelf Replenishment List Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ShelfReplenishmentDetailScreen extends SimScreen {
    private static final long serialVersionUID = 611758923053786001L;

    private ShelfReplenishmentDetailPanel panel = new ShelfReplenishmentDetailPanel();

    public ShelfReplenishmentDetailScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Shelf Replenishment List Detail";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        panel.start();
        displayMenu();
    }

    private void displayMenu() {
        showMenu();
        if (!panel.isShelfReplenishmentAdjustable()) {
            removeNavButton(SimNavigation.ADJUST);
        }
        if(panel.isAllowAdjustment()) {
        	removeNavButton(SimNavigation.SAVE);
        }
        if (!panel.isScannerAvailable()) {
            removeNavButton(SimNavigation.SCANNER);
        }
    }

    public void stop() {
        panel.stop();
    }

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                panel.handleSave();
            } else if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrint();
            	displayMenu();
            } else if (command.equals(SimNavigation.ADJUST)) {
            	handleAdjustShelfReplenishment();
            } else if (command.equals(SimNavigation.SCANNER)) {
                panel.handleScanner();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
    
    private void handleAdjustShelfReplenishment() throws Exception {
        if (panel.handleAdjustShelfReplenishment()) {
        	displayMenu();
        }
    }
}
