package oracle.retail.sim.client.screen.translation;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Translation Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TranslationDetailScreen extends SimScreen {
    private static final long serialVersionUID = -4140395909063312886L;

    private TranslationDetailPanel panel = new TranslationDetailPanel();

    public TranslationDetailScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Translation Details";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
    }

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.CREATE)) {
                panel.createTranslation();
            } else if (command.equals(SimNavigation.SEARCH)) {
                panel.handleSearch();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }
}
