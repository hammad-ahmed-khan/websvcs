package oracle.retail.sim.client.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import oracle.retail.sim.client.swing.frame.RFrame;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * The SimplifiedApplicationFrame class is meant to contain a bunch of generic code to produce an general
 * application frame format (navigation on the top, work space centered, and status bar on the bottom.
 * This class is abstract and must be subclassed to produce the local application frame of a project.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class SimplifiedApplicationFrame extends RFrame implements SimplifiedApplication, ActionListener {
    public static final String STATUSBAR = "GUI.GLOBALBAR";
    public static final String NAVIGATEBAR = "GUI.APPTOOLBAR";
    public static final String SHUTDOWN = "SHUTDOWN";
    public static final String RESTART = "RESTART_CLIENT";
    public static final String CLIENT_EXEC = "GUI.CLIENT_EXEC";

    protected NavigationToolbar navigationBar;
    protected RPanel contentContainer = new RPanel();
    protected StatusBarInterface statusBar;
    protected Screen currentScreen;
    protected String lastMenu;
    protected Class<?> lastScreen;

    private boolean processingNavigation;

    /****************************************************************************************************
     * Constructor - This is called by the framework using reflection. See the SimConfigFiles.CLIENT_CONFIG file to
     * see where this is specified.
     ***************************************************************************************************/
    protected SimplifiedApplicationFrame() {
        setTitleIcon((ImageIcon) UIManager.getIcon(UIThemeName.TITLEBAR_MINI_ICON));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(true);
        initComponents();
        layoutComponents();
    }

    /****************************************************************************************************
     * Assigns the title icon to the title bar and the minimize frame.
     ***************************************************************************************************/
    protected void setTitleIcon(ImageIcon imageIcon) {
        if (imageIcon != null) {
            setIconImage(imageIcon.getImage());
        }
    }

    /****************************************************************************************************
     * Initializes the components
     ***************************************************************************************************/
    protected void initComponents() {
        navigationBar = Application.getConfigManager().getObject(NAVIGATEBAR, NavigationToolbar.class);
        statusBar = Application.getConfigManager().getObject(STATUSBAR, StatusBarInterface.class);
    }

    /****************************************************************************************************
     * Initialize the layout
     ***************************************************************************************************/
    protected void layoutComponents() {
        statusBar.setParentFrame(this);

        Color color = UIManager.getColor("RContentPanel.borderColor");

        contentContainer.setLayout(new BorderLayout());
        contentContainer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, color));

        getContentPane().add(navigationBar, BorderLayout.NORTH);
        getContentPane().add(contentContainer, BorderLayout.CENTER);
        getContentPane().add((Component) statusBar, BorderLayout.SOUTH);

        pack();
    }

    /****************************************************************************************************
     * Assigns the screen name to the status bar.
     ***************************************************************************************************/
    private void setScreenName(String name) {
        statusBar.setScreenName(name);
    }

    /****************************************************************************************************
     * Shows a screen within the frame.
     ***************************************************************************************************/
    public void showScreen(Screen screen) {
        if (currentScreen != null) {
            contentContainer.remove(currentScreen);
        }
        currentScreen = screen;
        contentContainer.add(currentScreen, BorderLayout.CENTER);
        currentScreen.revalidate();
        currentScreen.repaint();
        setScreenName(screen.getScreenName());
    }

    /****************************************************************************************************
     * Initialize the focus
     ***************************************************************************************************/
    public void initializeFocus() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                navigationBar.requestFocusInWindow();
            }
        });
    }

    /****************************************************************************************************
     * Handle a hot key by passing it on to the navigation bar.
     ***************************************************************************************************/
    protected boolean doHotKeyPressed(int keyCode) {
        if (!isActive()) {
            return false;
        }
        switch (keyCode) {
            case KeyEvent.VK_F12:
                statusBar.doHotKeyPressed(keyCode);
                return true;
            default:
                navigationBar.doHotKeyPressed(keyCode);
                return true;
        }
    }

    /****************************************************************************************************
     * Returns the current status bar.
     ***************************************************************************************************/
    public StatusBarInterface getStatusBar() {
        return statusBar;
    }

    /****************************************************************************************************
     * Return the current navigation toolbar
     ***************************************************************************************************/
    public NavigationToolbar getNavigationToolbar() {
        return navigationBar;
    }

    /****************************************************************************************************
     * Shows a menu based on the screen an the user
     ***************************************************************************************************/
    public JButton[] showMenu(Class<?> classType, User user) {
        return showMenu(classType, user, null);
    }

    /****************************************************************************************************
     * Shows a menu based on the menu name, user and store.
     ***************************************************************************************************/
    public JButton[] showMenu(Class<?> classType, User user, Store store) {
        lastScreen = classType;
        JButton[] buttons = Application.getMenuManager().getMenu(classType, user, store);
        if (buttons != null) {
            navigationBar.setButtons(buttons);
        }
        return buttons;
    }

    /****************************************************************************************************
     * Clears the menu panel.
     ***************************************************************************************************/
    public void clearMenu() {
        lastMenu = null;
        navigationBar.setButtons(new JButton[0]);
    }

    /****************************************************************************************************
     * Receives callbacks from the application's main frame when an application toolbar button is
     * pressed. If the user pressed the help menu option, the application help is simply displayed. If
     * the user selected some other menu option checks are performed to ensure that the user has the
     * appropriate security role and that the action is allowed. Processing is then passed on to the the
     * current screen.
     ***************************************************************************************************/
    public void actionPerformed(ActionEvent event) {
        if (processingNavigation) {
            return;
        }
        processingNavigation = true;

        if (event.getSource() instanceof JButton) {
            JButton button = (JButton) event.getSource();

            NavigationEvent navigationEvent = new NavigationEvent();
            navigationEvent.setCommand(button.getName());
            navigationEvent.setScreen(button.getActionCommand());

            Application.getNavigationManager().getCurrentScreen().navigationEvent(navigationEvent);

            if (!navigationEvent.isConsumed()) {
                navigate(navigationEvent.getScreen());
            }
        }
        processingNavigation = false;
    }

    /****************************************************************************************************
     * Fires a navigation event. It uses the command to attempt to navigate to a new state.
     ***************************************************************************************************/
    public void navigate(String screenName) {
        Screen nextScreen = Application.getNavigationManager().navigate(screenName);
        if (nextScreen != null && nextScreen.isNavigationValid()) {
            showScreen(nextScreen);
        }
    }

    /****************************************************************************************************
     * Restarts the application based on configuration settings.
     ***************************************************************************************************/
    public void restartApplication() {
        String command = Application.getConfigManager().getString(SHUTDOWN);
        try {
            if (RESTART.equals(command)) {
                String restartCommand = System.getProperty(CLIENT_EXEC);
                if (restartCommand != null) {
                    UILog.info(getClass(), UIMessageText.LAUNCHING_COMMAND, restartCommand);
                    Runtime.getRuntime().exec(restartCommand);
                }
            } else {
                UILog.info(getClass(), UIMessageText.CONFIG_SHUTDOWN_LAUNCHING, command);
                Runtime.getRuntime().exec(command);
            }
        } catch (Throwable exception) {
            UILog.error(getClass(), UIMessageText.CONFIG_SHUTDOWN_LAUNCHING, command);
        }
        System.exit(0);
    }

    /****************************************************************************************************
     * Returns the application to the "home" screen.
     ***************************************************************************************************/
    public void goHome() {
        Application.getNavigationManager().goHome();
    }
}
