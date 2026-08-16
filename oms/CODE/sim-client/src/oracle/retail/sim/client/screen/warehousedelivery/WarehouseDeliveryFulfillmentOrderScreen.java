package oracle.retail.sim.client.screen.warehousedelivery;

import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Warehouse Delivery Fulfillment Order Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class WarehouseDeliveryFulfillmentOrderScreen extends SimScreen {
    private static final long serialVersionUID = 6385658470048384846L;

    private WarehouseDeliveryFulfillmentOrderPanel panel = new WarehouseDeliveryFulfillmentOrderPanel();

    public WarehouseDeliveryFulfillmentOrderScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Warehouse Delivery Customer Orders";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    /****************************************************************************************************
     * State Management
     ***************************************************************************************************/

    public void start() throws Exception {
        showMenu();
        panel.start();
    }
}
