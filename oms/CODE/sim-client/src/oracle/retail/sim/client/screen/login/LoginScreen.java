package oracle.retail.sim.client.screen.login;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.StatusBarInterface;
import oracle.retail.sim.client.core.SimApplicationConfig;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.displayer.StoreDisplayer;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.common.security.SecurityUtility;
import oracle.retail.sim.common.security.User;

/********************************************************************************************************
 * Login Screen
 * <p>
 * This is the superclass of logout and main screen, both which allow the login process.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class LoginScreen extends SimScreen {
    private static final long serialVersionUID = -3368881666150206625L;

    private StoreDisplayer storeDisplayer = new StoreDisplayer();

    /****************************************************************************************************
     * LOGIN METHODS
     ***************************************************************************************************/

    protected void updateBackgroundFrame() {
        try {
            loadUserSettings();
            updateStatusBar();
            displayTitle();
            displayMenu();
            Application.getFrame().setSize(800, 600);
            getApplicationFrame().resetWindow();
            Application.getFrame().setVisible(true);
        } catch (Exception e) {
            displayException(e);
            System.exit(1);
        }
    }

    protected void displayMenu() {
        showMenu();

        switch (SecurityUtility.getUserSecurityMode()) {
            case EXTERNAL:
                removeNavButton(SimNavigation.CHANGE_PASSWORD);
                break;
            case HYBRID:
                User user = SimRepository.getUser();
                if (user == null || user.isCached()) {
                    removeNavButton(SimNavigation.CHANGE_PASSWORD);
                }
                break;
        }
    }

    private void loadUserSettings() {
        SimApplicationConfig.loadTableConfigurationFromCache();
    }

    protected void updateStatusBar() {
        StatusBarInterface statusBar = getApplicationFrame().getStatusBar();
        User user = SimRepository.getUser();
        if (user != null) {
            statusBar.setUser(user.getFirstName() + " " + user.getLastName());
        } else {
            statusBar.setUser(Translator.getText("Login Required"));
        }
        statusBar.setScreenName(getScreenName());
        statusBar.setStoreInfo(storeDisplayer.getDisplayText(SimRepository.getStore()));
    }

    protected void displayTitle() {
        Application.getFrame().setTitle(Translator.getText("Store Inventory Management"));
    }

    public ScreenPanel getScreenPanel() {
        return null;
    }
}
