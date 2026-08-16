package oracle.retail.sim.client.swing.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.Screen;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.table.SimTable;

/******************************************************************************************
 * This subclass of the mouse adapter handles deactivating the first encountered table
 * within the root pane when a JButton is entered by a mouse. If the default constructor
 * is used, find the current screen, otherwise use the screen or dialog assigned in order
 * to determine what componentns to deactivate.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class MouseSimTableDeactivateAdapter extends MouseAdapter {

    private RDialog dialog = null;
    private Screen screen = null;

    public MouseSimTableDeactivateAdapter() {
        dialog = null;
        screen = null;
    }

    public MouseSimTableDeactivateAdapter(RDialog dialog) {
        this.dialog = dialog;
    }

    public MouseSimTableDeactivateAdapter(Screen screen) {
        this.screen = screen;
    }

    public void mouseEntered(MouseEvent event) {
        if (dialog != null) {
            dialog.stopEditing();
            return;
        }
        if (screen == null) {
            screen = Application.getNavigationManager().getCurrentScreen();
        }
        if (screen instanceof SimScreen) {
            SimScreen simScreen = (SimScreen) screen;
            if (simScreen != null) {
                ScreenPanel panel = simScreen.getScreenPanel();
                if (panel != null) {
                    SimTable table = panel.getScreenTable();
                    if (table != null) {
                        table.stopEditing();
                    }
                }
            }
        }
    }
}
