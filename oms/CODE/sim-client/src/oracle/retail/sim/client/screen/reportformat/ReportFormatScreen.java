package oracle.retail.sim.client.screen.reportformat;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Label, Ticket and Report Format Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ReportFormatScreen extends SimScreen {
    private static final long serialVersionUID = -2679343759999316691L;

    private ReportFormatPanel panel = new ReportFormatPanel();

    public ReportFormatScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Formats";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu(SimNavigation.SAVE);
        panel.start();
    }

    public void resume() {
        showMenu(SimNavigation.SAVE);
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.LABEL_AND_TICKET_FORMAT);
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                panel.handleSave();
            } else if (command.equals(SimNavigation.DELETE)) {
                panel.handleDelete();
            } else if (command.equals(SimNavigation.ADD)) {
                panel.handleCreate();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }
}
