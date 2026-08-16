package oracle.retail.sim.client.screen.fulfillmentorder;

import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Customer Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class CustomerDetailScreen extends SimScreen {
    private static final long serialVersionUID = 1606526248198381761L;

    private CustomerDetailPanel panel = new CustomerDetailPanel();

    public CustomerDetailScreen() {
        add(panel);
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public String getScreenName() {
        return "Customer Detail";
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
    }
}
