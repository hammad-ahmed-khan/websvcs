package oracle.retail.sim.client.screen.supplier;

import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Additial Suppliers Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class AdditionalSupplierScreen extends SimScreen {
    private static final long serialVersionUID = -4675530832733059267L;

    private AdditionalSupplierPanel panel = new AdditionalSupplierPanel();

    public AdditionalSupplierScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Additional Suppliers";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
    }
}
