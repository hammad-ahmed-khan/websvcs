package oracle.retail.sim.client.screen.login;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimBackgroundPanel;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.common.configutil.SimConfigManager;

/********************************************************************************************************
 * Inventory Menu Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryMenuScreen extends SimScreen {
    private static final long serialVersionUID = 6918388369220508674L;

    public InventoryMenuScreen() {
        add(new SimBackgroundPanel());
    }

    public String getScreenName() {
        return "Inventory Management";
    }

    public ScreenPanel getScreenPanel() {
        return null;
    }

    public void start() {
        showMenu();
        if (!SimConfigManager.getBoolean(SimConfigManager.ENABLE_RSL_CALL)) {
            removeNavButton(SimNavigation.PRICE_CHANGE);
        }
    }

    public void resume() {
        start();
    }

    /****************************************************************************************************
     * Handle Navigation Event
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        if (command.equals(SimNavigation.STORE_ORDERS)) {
            doStoreOrder(event);
        }
    }

    /****************************************************************************************************
     * STORE PURCHASE ORDER - Checks to see if the RSL connection is broken
     ***************************************************************************************************/
    private void doStoreOrder(NavigationEvent event) {
        //        try {
        //            ClientServiceFactory.getStoreOrderServices().pingExternalService("test");
        //        } catch (Throwable exception) {
        //            displayException(StoreOrderMessageText.BAD_CONNECTION);
        //            RepositoryManagerX.removeStateObject(SimClientStateKey.STORE_ORDER_FILTER);
        //            event.consume();
        //        }
    }
}
