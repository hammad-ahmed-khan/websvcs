package oracle.retail.sim.client.screen.supplier;

import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Supplier Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SupplierDetailScreen extends SimScreen {
    private static final long serialVersionUID = 9198294289387818010L;

    private SupplierDetailPanel panel = new SupplierDetailPanel();

    public SupplierDetailScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Supplier Detail";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
    }
}
