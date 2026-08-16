package oracle.retail.sim.client.screen.item;

import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Item Customer Order Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemCustomerOrderScreen extends SimScreen {
    private static final long serialVersionUID = -1521242442482341886L;

    private ItemCustomerOrderPanel panel = new ItemCustomerOrderPanel();

    public ItemCustomerOrderScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Item Customer Order";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() {
        showMenu();
        panel.start();
    }

    public void resume() {
        showMenu();
        panel.resume();
    }
}
