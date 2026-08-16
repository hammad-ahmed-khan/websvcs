package oracle.retail.sim.client.application;

import oracle.retail.sim.client.swing.navigation.SecurityInterface;

/********************************************************************************************************
 * Simplified Application Interface
 * <p>
 * An interface to a GUI which the client application will require an instance that implements it. The
 * client framework will interact with the GUI presentation that implements this interface through these
 * defined methods.
 * <p>
 * The framework will look an implementing class with the key GUI.MAINFRAME.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public interface SimplifiedApplication {

    /**
     * Return the status bar owned by this application frame
     *
     * @return The status bar owned by this application frame
     */
    StatusBarInterface getStatusBar();

    /**
     * Return the navigation tool bar owned by this application frame
     *
     * @return The navigation tool bar owned by this application frame
     */
    NavigationToolbar getNavigationToolbar();

    /**
     * Subclasses should implement to return the security manager for the application.
     */
    SecurityInterface getSecurityManager();

    /**
     * Initializes the focus within the application frame
     */
    void initializeFocus();

    /**
     * Show the specified screen
     *
     * @param screen The screen.
     */
    void showScreen(Screen screen);

    /**
     * Fires a navigation event based on a command.
     *
     * @param command The command to fire a navigation event for
     */
    void navigate(String command);

    /**
     * Restarts the application
     */
    void restartApplication();

    /**
     * Resets the window properties.
     */
    void resetWindow();

    /**
     * Clears the application session information. This method cannot be allowed to fail, so simply log any exceptions.
     */
    void clearSession();
}
