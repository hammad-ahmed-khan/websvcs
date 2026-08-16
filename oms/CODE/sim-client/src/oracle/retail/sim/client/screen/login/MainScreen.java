package oracle.retail.sim.client.screen.login;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.security.SimLoginManager;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.HelpLauncher;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;

/********************************************************************************************************
 * SIM Main Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class MainScreen extends LoginScreen implements REventListener {
    private static final long serialVersionUID = 8056008097197178747L;

    private MainScreenPanel mainPanel = new MainScreenPanel();

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public MainScreen() {
        add(mainPanel);
        mainPanel.addREventListener(this);
    }

    public String getScreenName() {
        return "SIM Login";
    }

    public ScreenPanel getScreenPanel() {
        return mainPanel;
    }

    public void start() throws Exception {
        try {
            //Validate active login session
            if (!SimLoginManager.isSessionActive()) {
                throw new BusinessException(CommonMessageText.ERROR_FATAL_DEFAULT_MESSAGE);
            }
            if (!Application.getFrame().isVisible()) {
                updateBackgroundFrame();
            } else {
                displayMenu();
                displayTitle();
                updateStatusBar();
            }
            mainPanel.start();
        } catch (Throwable t) {
            displayException(t);
            System.exit(1);
        }
    }

    public void resume() throws Exception {
        displayMenu();
    }

    /****************************************************************************************************
     * Handle Navigation Event
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        if (command.equals(SimNavigation.CHANGE_PASSWORD)) {
            doChangePassword();
        } else if (command.equals(SimNavigation.HELP)) {
            doHelp();
        } else if (command.equals(SimNavigation.REPORTS)) {
            doReports();
        } else if (command.equals(SimNavigation.LOGOUT)) {
            doLogout(event);
        }
    }

    /****************************************************************************************************
     * Handle Action Sent From Panel
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(SimClientStateKey.LOGIN_STORE)) {
            displayMenu();
            displayTitle();
            updateStatusBar();
        }
    }

    /****************************************************************************************************
     * Change Password
     ***************************************************************************************************/
    private void doChangePassword() {
        ChangePasswordDialog dialog = new ChangePasswordDialog();
        dialog.setVisible(true);
    }

    /****************************************************************************************************
     * Help
     ***************************************************************************************************/
    private void doHelp() {
        HelpLauncher.showHelp();
    }

    /****************************************************************************************************
     * Reports
     ***************************************************************************************************/
    private void doReports() {
        SimClientPrintUtility.launchBrowserShowReports(SimRepository.getStore());
    }

    /****************************************************************************************************
     * Logout
     ***************************************************************************************************/
    private void doLogout(NavigationEvent event) {
        event.consume();
        mainPanel.handleLogout();
    }

}
