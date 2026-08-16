package oracle.retail.sim.client.screen.login;

import oracle.retail.sim.client.core.SimBackgroundPanel;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Printer Setup Menu Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class PrinterSetupMenuScreen extends SimScreen {
    private static final long serialVersionUID = -8526303835698401829L;

    public PrinterSetupMenuScreen() {
        add(new SimBackgroundPanel());
    }

    public String getScreenName() {
        return "Printer Setup";
    }

    public ScreenPanel getScreenPanel() {
        return null;
    }
}
