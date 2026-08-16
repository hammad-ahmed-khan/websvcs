package oracle.retail.sim.client.application;

import java.awt.Window;
import java.util.LinkedList;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.task.UITaskManager;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.configutil.ConfigManager;

/********************************************************************************************************
 * NAVIGATION MANAGER
 * <p>
 * This class is responsible for handling all navigation functionality.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class NavigationManager {
    public static final String DEFAULT_SCREEN = "GUI.DEFAULT_SCREEN";
    public static final String LOGOUT_SCREEN = "GUI.LOGOUT_SCREEN";

    private LinkedList<Screen> navigationStack = new LinkedList<>();
    private String defaultScreenName;
    private String logoutScreenName;
    private boolean isGoingHomeActivated;

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    public NavigationManager() {
        ConfigManager configManager = Application.getConfigManager();
        defaultScreenName = configManager.getString(DEFAULT_SCREEN);
        logoutScreenName = configManager.getString(LOGOUT_SCREEN);
    }

    /****************************************************************************************************
     * Retrieves the current screen from the navigation stack. If the stack is empty, it returns the
     * MainScreen of the application.
     * <p>
     * @return The current screen or null if no screen is being displayed.
     ***************************************************************************************************/
    public Screen getCurrentScreen() {
        if (navigationStack.isEmpty()) {
            return null;
        }
        return navigationStack.getLast();
    }

    /****************************************************************************************************
     * Shows the default screen.
     ***************************************************************************************************/
    public void showDefaultScreen() {
        clearNavigationStack();
        showScreen(defaultScreenName);
    }

    /****************************************************************************************************
     * Shows the logout screen.
     ***************************************************************************************************/
    public void showLogoutScreen() {
        clearNavigationStack();
        showScreen(logoutScreenName);
    }

    /****************************************************************************************************
     * Removes last screen from stack.
     ***************************************************************************************************/
    public void removeScreenFromStack(Screen screen) {
        navigationStack.remove(screen);
    }

    /****************************************************************************************************
     * Adds a screen to the stack without navigating to it.
     ***************************************************************************************************/
    public void addScreenToStack(String screenName) {
        Screen targetScreen = createScreen(screenName);
        if (targetScreen != null) {
            if (targetScreen.isStartable()) {
                try {
                    targetScreen.start();
                    navigationStack.add(targetScreen);
                } catch (Throwable exception) {
                    UIStatusUtility.displayException(this, exception);
                }
            }
        }
    }

    /****************************************************************************************************
     * Attempts to navigation to the desired screen. If NONE is passed in, no navigation takes place. If
     * BACK is passed in, the application returns to the previous screen.
     * <p>
     * @param screenName Fully qualified class name to the desired screen, or NONE or BACK.
     ***************************************************************************************************/
    public Screen navigate(String screenName) {
        UITaskManager.showBusy(true);
        try {
            if (StringUtility.isNullOrEmpty(screenName)) {
                return null;
            }
            if ("NONE".equalsIgnoreCase(screenName)) {
                return null;
            }
            if ("BACK".equalsIgnoreCase(screenName)) {
                return startPreviousScreen();
            }
            return startScreen(screenName);
        } catch (Throwable exception) {
            UILog.error(getClass(), exception);
            return null;
        } finally {
            UITaskManager.showBusy(false);
        }
    }

    /****************************************************************************************************
     * Returns the application to the home screen. If an attempt to go home occurs when there are NO
     * screens on the screen stack. Then we haven't even made it to the main background screen and so we
     * exit the program.
     * <p>
     * It validates that returning to home is allowed, then closes all open windows, then stops the
     * current screen, clears the entire navigation stack and shows the default screen for the
     * application.
     ***************************************************************************************************/
    public void goHome() {
        Screen currentScreen = getCurrentScreen();
        if (currentScreen == null) {
            System.exit(1);
        }
        if (currentScreen.isHomeAllowed() && !isGoingHomeActivated) {
            isGoingHomeActivated = true;
            currentScreen.stop();
            closeAllWindows();
            clearNavigationStack();
            isGoingHomeActivated = false;
            showScreen(defaultScreenName);
        }
    }

    /****************************************************************************************************
     * Helper method to close all open windows.
     ***************************************************************************************************/
    private void closeAllWindows() {
        Window[] openWindows = Application.getFrame().getOwnedWindows();
        for (Window openWindow : openWindows) {
            openWindow.dispose();
        }
    }

    /****************************************************************************************************
     * Helper method that clears all screens on the navigation stack.
     ***************************************************************************************************/
    private void clearNavigationStack() {
        for (Screen screen : navigationStack) {
            if (screen.isPaused()) {
                screen.stop();
                screen.setPaused(false);
            }
        }
        navigationStack.clear();
    }

    /****************************************************************************************************
     * Helper method that clears all screens except the default screen on the navigation stack.
     ***************************************************************************************************/
    public void clearNavigationStackToHome() {
        Screen defaultScreen = navigationStack.getFirst();
        for (Screen screen : navigationStack) {
            if (screen.isPaused()) {
                screen.stop();
                screen.setPaused(false);
            }
        }
        navigationStack.clear();
        navigationStack.add(defaultScreen);
    }

    /****************************************************************************************************
     * Repaints the current screen
     ***************************************************************************************************/
    public void repaintCurrentScreen() {
        Screen screen = getCurrentScreen();
        if (screen != null) {
            screen.repaint();
        }
    }

    /****************************************************************************************************
     * Helper method that starts and displays the screen (identified by screen name) on the frame.
     ***************************************************************************************************/
    private void showScreen(String screenName) {
        Application.getApplicationFrame().showScreen(startScreen(screenName));
    }

    /****************************************************************************************************
     * Helper method that retrieves a name. It uses the passed in screen name (fully qualified class) and
     * reflection to create a new instance of the screen.
     ***************************************************************************************************/
    private Screen createScreen(String screenName) {
        try {
            return (Screen) Class.forName(screenName).newInstance();
        } catch (Throwable exception) {
            UILog.error(getClass(), UIMessageText.UNABLE_TO_FIND_SCREEN, screenName, exception);
        }
        return null;
    }

    /****************************************************************************************************
     * Starts the next screen for use within the application. This will get the current screen and target
     * screen, attempt to stop the current screen, attempt to start the next screen, add it to the
     * navigation stack and return the new screen.
     ***************************************************************************************************/
    private Screen startScreen(String screenName) {
        if (UILog.isDebugEnabled(getClass())) {
            UILog.debug(getClass(), UIMessageText.STARTING_SCREEN, screenName);
        }
        Screen currentScreen = getCurrentScreen();
        Screen targetScreen = createScreen(screenName);
        if (currentScreen != null) {
            currentScreen.pause();
            currentScreen.setPaused(true);
        }
        if (targetScreen != null) {
            if (targetScreen.isStartable()) {
                if (startScreen(targetScreen)) {
                    navigationStack.add(targetScreen);
                    return targetScreen;
                }
            }
        }
        try {
            currentScreen.resume();
        } catch (Throwable exception) {
            UILog.error(getClass(), UIMessageText.RESUME_SCREEN_FAILURE, exception);
        }
        return currentScreen;
    }

    /****************************************************************************************************
     * Helper method that navigates back to the previous screen
     ***************************************************************************************************/
    private Screen startPreviousScreen() {
        if (navigationStack.size() < 2) {
            return null;
        }
        int lastIndex = navigationStack.size() - 1;
        Screen currentScreen = navigationStack.get(lastIndex);
        Screen previousScreen = navigationStack.get(lastIndex - 1);

        navigationStack.removeLast();
        if (currentScreen.isStoppable()) {
            currentScreen.stop();
            currentScreen.setPaused(false);

            if (startScreen(previousScreen)) {
                return previousScreen;
            }
            try {
                if (UILog.isDebugEnabled(getClass())) {
                    UILog.debug(getClass(), UIMessageText.RESUMING_SCREEN, currentScreen.getScreenName());
                }
                currentScreen.resume();
            } catch (Throwable exception) {
                UILog.error(getClass(), UIMessageText.RESUME_SCREEN_FAILURE, exception);
            }
        }
        return currentScreen;
    }

    /****************************************************************************************************
     * Start the specified screen. If the screen being started is currently paused, perform the
     * lighter-weight resume() operation, instead of normal start() behavior.
     * <p>
     * @param screen The screen to start.
     ***************************************************************************************************/
    private boolean startScreen(Screen screen) {
        if (screen.isPaused()) {
            try {
                screen.resume();
            } catch (Throwable exception) {
                UIStatusUtility.displayException(this, exception);
                return false;
            }
            screen.setPaused(false);
            screen.assignFocusInScreen();
            return true;
        }
        try {
            screen.start();
            screen.assignFocusInScreen();
        } catch (Throwable exception) {
            UIStatusUtility.displayException(this, exception);
            return false;
        }
        return true;
    }
}
