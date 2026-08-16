package oracle.retail.sim.client.screen.customuin;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * UIN Create Screen - This screens provides the ability to create and insert new UINs into SIM without
 * validation or generating matching transactions. This should be used exclusively to dataseed UINs
 * into the application.
 * <p>
 * This is future work-in-progress code for future SIM 14.0 or later release.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CustomUINCreateScreen extends SimScreen {
    private static final long serialVersionUID = 487712289865375658L;

    private CustomUINCreatePanel panel = new CustomUINCreatePanel();

    public CustomUINCreateScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "UIN Create";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public boolean isStartable() {
        return panel.isStartable();
    }

    public void start() throws Throwable {
        showMenu();
        panel.start();
    }

    public void stop() {
        panel.stop();
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        try {
            switch (event.getCommand()) {
                case SimNavigation.ADD_ITEM:
                    panel.doAddItem();
                    break;
                case SimNavigation.REMOVE_ITEM:
                    panel.doRemoveItem();
                    break;
                case SimNavigation.SAVE:
                    panel.doHandleDone();
                    break;
            }
        } catch (Throwable t) {
            displayException(panel, event, t);
        }
    }
}
