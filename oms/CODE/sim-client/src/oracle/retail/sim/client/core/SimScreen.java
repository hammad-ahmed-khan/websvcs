package oracle.retail.sim.client.core;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.Screen;
import oracle.retail.sim.client.application.SimplifiedApplicationFrame;
import oracle.retail.sim.client.swing.event.RErrorEvent;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.rules.RuleEngine;

/********************************************************************************************************
 * The superclass of all SIM screens. Contains useful functionality for the screen subclasses to use.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class SimScreen extends Screen {
    private boolean isNavigationValid = true;
    private boolean initialized;
    private boolean paused;
    private JButton[] buttons = new JButton[0];

    /****************************************************************************************************
     * Called when home navigation is desired. By default, this method return true. If overridden by a
     * subclass to return false, this will keep a navigation to home from taking place.
     * <p>
     * @return True if the screen is allowed to return home, false otherwise.
     ***************************************************************************************************/
    public boolean isHomeAllowed() {
        return true;
    }

    /****************************************************************************************************
     * Called each time the screen is shown on the screen (with the exception of pause/resume).
     ***************************************************************************************************/
    public void start() throws Throwable {
        showMenu();
    }

    /****************************************************************************************************
     * Called each time the screen is removed from the screen (with the exception of pause/resume).
     ***************************************************************************************************/
    public void stop() {
    }

    /****************************************************************************************************
     * Returns whether or not the screen is startable. If the screen is not startable, then attempts to
     * navigation to it will be blocked.
     ***************************************************************************************************/
    public boolean isStartable() {
        return true;
    }

    /****************************************************************************************************
     * Returns whether or not the screen is stoppable. If the screen is not stoppable, then attempts to
     * navigation away from it will be blocked.
     ***************************************************************************************************/
    public boolean isStoppable() {
        return true;
    }

    /****************************************************************************************************
     * Called when a screen is being paused by the screen manager. A different screen is being shown, but
     * this current screen is being paused in its current state. Subclasses can override this behavior if
     * anything special is required for pause/resume cleanup. Default implementation does nothing.
     ***************************************************************************************************/
    public void pause() {
    }

    /****************************************************************************************************
     * Called when an screen is resumed. This is called when the screen is now being shown again after
     * being paused. Subclasses can override this behavior if anything special is required for
     * pause/resume cleanup. Default implementation shows the menu (which was altered when the screen was
     * exited.
     ***************************************************************************************************/
    public void resume() throws Throwable {
        showMenu();
    }

    /****************************************************************************************************
     * This method will locate the screen panel assign to the screen and call assign focus in screen on
     * that panel.
     ***************************************************************************************************/
    public void assignFocusInScreen() {
        Component[] components = getComponents();
        if (components != null) {
            for (Component component : components) {
                if (component instanceof ScreenPanel) {
                    ((ScreenPanel) component).assignFocusInScreen();
                    return;
                }
            }
        }
        buttons[0].requestFocusInWindow();
    }

    /****************************************************************************************************
     * Assigns whether or not the screen has been initialized.
     * <p>
     * @param initialized True if the screen is initialized, otherwise false.
     ***************************************************************************************************/
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    /****************************************************************************************************
     * Retrieves whether or not the screen has been initialized.
     * <p>
     * @return True if the screen is initialized, otherwise false.
     ***************************************************************************************************/
    public boolean isInitialized() {
        return initialized;
    }

    /****************************************************************************************************
     * Assigns whether or not the screen has been paused.
     * <p>
     * @param paused True if the screen is paused, otherwise false.
     ***************************************************************************************************/
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /****************************************************************************************************
     * Retrieves whether or not the screen has been paused.
     * <p>
     * @return True if the screen is paused, otherwise false.
     ***************************************************************************************************/
    public boolean isPaused() {
        return paused;
    }

    /****************************************************************************************************
     * Retrieves whether or not navigation is valid for the screen. An invalid navigation means that
     * something has taken place where the screen should not be displayed while in the process of
     * navigation.
     * <p>
     * @return True if should skip in display, otherwise false.
     ***************************************************************************************************/
    public boolean isNavigationValid() {
        return isNavigationValid;
    }

    /****************************************************************************************************
     * Returns the ScreenPanel that is associated with the panel.
     ***************************************************************************************************/
    public abstract ScreenPanel getScreenPanel();

    /****************************************************************************************************
     * Empty implementation of the navigation listener.
     * <p>
     * @param event The NavigationEvent.
     ***************************************************************************************************/
    public void navigationEvent(NavigationEvent event) {
        SimScreenNavigationListener listener = SimScreenNavigationListenerFactory.getNavigationListener(this);
        if (listener != null) {
            listener.processEvent(event);
        }
        if (!event.isConsumed()) {
            performNavigationEvent(event);
        }
    }

    /****************************************************************************************************
     * Empty method that must be overridden by all sub-classes to handle the navigation events that
     * come from the button menu.
     * @param event The NavigationEvent.
     ***************************************************************************************************/
    public void performNavigationEvent(NavigationEvent event) {
    }

    /****************************************************************************************************
     * Fires a navigation event through the application frame.
     * @param command The location to navigation to or the key values "BACK" or "HOME".
     ***************************************************************************************************/
    public void navigate(String command) {
        getApplicationFrame().navigate(command);
    }

    /****************************************************************************************************
     * Fires a navigation event through the application frame later after the event queue is clear of
     * events.
     * @param command The location to navigation to or the key values "BACK" or "HOME".
     ***************************************************************************************************/
    public void navigateLater(final String command) {
        isNavigationValid = false;

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getApplicationFrame().navigate(command);
            }
        });
    }

    /****************************************************************************************************
     * Removes this screen from the record screen history. Therefore, BACK will not return to this screen.
     ***************************************************************************************************/
    public void removeFromScreenHistory() {
        Application.getNavigationManager().removeScreenFromStack(this);
    }

    /****************************************************************************************************
     * Overrides the add() of the parent class to remove all other components before adding. SimScreen
     * may only have one component at a time.
     ***************************************************************************************************/
    public Component add(Component comp) {
        removeAll();
        addImpl(comp, null, -1);
        return comp;
    }

    /****************************************************************************************************
     * Shows the screen menu with no default button.
     ***************************************************************************************************/
    protected JButton[] showMenu() {
        return showMenu(null);
    }

    /****************************************************************************************************
     * Shows the menu with a default button name.
     * <p>
     * @param buttonName The name of the default button.
     ***************************************************************************************************/
    protected JButton[] showMenu(String buttonName) {
        JRootPane rootPane = Application.getFrame().getRootPane();
        rootPane.setDefaultButton(null);

        buttons = getApplicationFrame().showMenu(getClass(), SimRepository.getUser());

        if (buttons != null) {
            for (JButton button : buttons) {
                if (button.getName().equals(buttonName)) {
                    rootPane.setDefaultButton(button);
                    break;
                }
            }
        }
        return buttons;
    }

    /****************************************************************************************************
     * Remove navigation button based on button name.
     ***************************************************************************************************/
    protected void removeNavButton(String buttonName) {
        for (JButton button : buttons) {
            if (button.getName().equals(buttonName)) {
                button.setVisible(false);
                return;
            }
        }
    }

    /****************************************************************************************************
     * Triggers a click event for the navigation button based on the button name.
     ***************************************************************************************************/
    protected void fireNavButton(String buttonName) {
        for (JButton button : buttons) {
            if (button.getName().equals(buttonName)) {
                button.doClick();
                return;
            }
        }
    }

    /****************************************************************************************************
     * Execute the RuleEngine's execute property method using this class name and this object.
     *
     * @param methodName The name of the method to run.
     * @param valueArray The array of parameters for the method.
     * @throws BusinessException If a rule fails.
     * @see RuleEngine#execute(String, String, Object, Object[])
     ***************************************************************************************************/
    public void executeRule(String methodName, Object[] valueArray) throws BusinessException {
        RuleEngine.getInstance().executePropertyRule(methodName, this, valueArray);
    }

    /****************************************************************************************************
     * Execute the RuleEngine's execute method using this class name and this object.
     *
     * @param methodName The name of the method to run.
     * @param valueThe parameter for the method.
     * @throws BusinessException If a rule fails.
     * @see RuleEngine#execute(String, String, Object, Object[])
     ***************************************************************************************************/
    public void executeRule(String methodName, Object value) throws BusinessException {
        executeRule(methodName, new Object[] { value });
    }

    /****************************************************************************************************
     * Execute the RuleEngine's execute method using this class name and this object.
     *
     * @param methodName The name of the method to run.
     * @throws BusinessException If a rule fails.
     * @see RuleEngine#execute(String, String, Object, Object[])
     ***************************************************************************************************/
    public void executeRule(String methodName) throws BusinessException {
        executeRule(methodName, new Object[] {});
    }

    /****************************************************************************************************
     * Handles displaying a message through the exception handling system.
     * <p>
     * @param message The message enum to display.
     ***************************************************************************************************/
    protected void displayException(MessageText message) {
        displayException(new BusinessException(message));
    }

    /****************************************************************************************************
     * Handles displaying an exception.
     * <p>
     * @param exception The exception to display.
     ***************************************************************************************************/
    protected void displayException(Throwable exception) {
        UIStatusUtility.displayException(this, exception);
    }

    /****************************************************************************************************
     * Handles displaying an exception. In this version of exception display, the following occurs: if
     * the panel is busy, it is made not busy; the event is consumed so that execution will not continue
     * after the exception is display; if the exception is fatal the application is reset.
     * <p>
     * @param panel The screen panel.
     * @param event The Navigation Event.
     * @param exception The exception to display.
     ***************************************************************************************************/
    protected void displayException(ScreenPanel panel, NavigationEvent event, Throwable exception) {
        if (panel != null) {
            panel.showScreenBusy(false);
        }
        if (event != null) {
            event.consume();
        }
        UIStatusUtility.displayException(this, exception);
    }

    /****************************************************************************************************
     * Empty implementation of a painful method that is required for REventListener but nearly never
     * needed.
     ***************************************************************************************************/
    public void performErrorEvent(RErrorEvent event) {
    }

    /****************************************************************************************************
     * Retrieves the application frame.
     ***************************************************************************************************/
    protected SimplifiedApplicationFrame getApplicationFrame() {
        return (SimplifiedApplicationFrame) Application.getApplicationFrame();
    }

    /****************************************************************************************************
     * Updates the screen name in the area at the bottom of the application.
     ***************************************************************************************************/
    protected void updateScreenName(String screenName) {
        Application.getApplicationFrame().getStatusBar().setScreenName(screenName);
    }
}
