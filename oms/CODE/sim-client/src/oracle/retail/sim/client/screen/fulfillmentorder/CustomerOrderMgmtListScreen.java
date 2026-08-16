package oracle.retail.sim.client.screen.fulfillmentorder;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Fulfillment Order Management List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class CustomerOrderMgmtListScreen extends SimScreen {

    private static final long serialVersionUID = -3949291361988007896L;

    private FulfillmentOrderMgmtListPanel panel = new FulfillmentOrderMgmtListPanel();

    public CustomerOrderMgmtListScreen() {
        add(panel);
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Customer Order Management List";
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
    }

    public void resume() throws Exception {
        showMenu();
        panel.start();
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.FULFILLMENT_ORDER_MANAGEMENT_FILTER);
        panel.stop();
    }

}