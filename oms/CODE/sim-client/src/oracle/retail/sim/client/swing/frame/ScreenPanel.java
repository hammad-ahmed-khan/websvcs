package oracle.retail.sim.client.swing.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.core.RetailEditorManager;
import oracle.retail.sim.client.swing.event.RErrorEvent;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableDef;
import oracle.retail.sim.client.swing.task.UITask;
import oracle.retail.sim.client.swing.task.UITaskExecutor;
import oracle.retail.sim.client.swing.task.UITaskManager;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.MessageText;

/********************************************************************************************************
 * ScreenPanel is the superclass of ALL SIM screen panels.
 * <p>
 * setContentPane() should be used to assign an RPanel that contains the actual screen content.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class ScreenPanel extends RPanel {

    private RetailEditorManager editorManager = new RetailEditorManager();

    private static final String METHOD_UNAVAILABLE = "Method is unavailable. Please use setContentPane().";

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    protected ScreenPanel() {
        super(new BorderLayout());
        setEmptyBorder(3);
        editorManager.setOwner(this);
    }

    /****************************************************************************************************
     * Assigns a content panel to the screen.
     * <p>
     * @param panel The panel to assign.
     ***************************************************************************************************/
    public void setContentPane(RPanel panel) {
        addImpl(panel, BorderLayout.CENTER, -1);
        validate();
    }

    /****************************************************************************************************
     * Assigns layout manager to the panel. This method is overridden to only accept BorderLayout.
     * <p>
     * @param manager A BorderLayout object.
     ***************************************************************************************************/
    public void setLayout(LayoutManager manager) {
        if (!(manager instanceof BorderLayout)) {
            throw new IllegalArgumentException("SimScreen can only accept BorderLayout as a layout.");
        }
        super.setLayout(manager);
    }

    /****************************************************************************************************
     * All panels must implement this method. It is often called by the screen that uses the panel to
     * initialize the state of the panel.
     ***************************************************************************************************/
    public abstract void start() throws Throwable;

    /****************************************************************************************************
     * Adds the specified component to this container. It is strongly advised to use the 1.1 method,
     * add(Component, Object), in place of this method. This method has been overridden and made
     * unavailable.
     *
     * @param name A string name for the component.
     * @param component The component to be added.
     * @return The component argument.
     ***************************************************************************************************/
    public Component add(String name, Component component) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Adds the specified component to this container at the given index. This method has been overridden
     * and made unavailable.
     * <p>
     * @param component The component to be added
     * @param index Position to add the component
     * @return The component argument.
     ***************************************************************************************************/
    public Component add(Component component, int index) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Adds the specified component to the end of this container. This method has been overridden and
     * made unavailable.
     * <p>
     * @param component The component to be added
     * @param constraints An object expressing layout contraints for this
     ***************************************************************************************************/
    public void add(Component component, Object constraints) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Adds the specified component to this container with the specified constraints at the specified
     * index. Also notifies the layout manager to add the component to the this container's layout using
     * the specified constraints object. This method has been overridden and made unavailable
     * <p>
     * @param component The component to be added
     * @param constraints An object expressing layout contraints for this
     * @param index The position in the container's list at which to insert the component. -1 means
     *            insert at the end.
     ***************************************************************************************************/
    public void add(Component component, Object constraints, int index) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Returns the screen model that is associated with the panel.
     ***************************************************************************************************/
    public abstract SimScreenModel getScreenModel();

    /****************************************************************************************************
     * Returns the primary SIM table associated with the panel or null if none exists.
     ***************************************************************************************************/
    public abstract SimTable getScreenTable();

    /****************************************************************************************************
     * Return the table identifier for the data class on this panel.
     ***************************************************************************************************/
    public SimTableDef createTableDef(Class dataClass) {
        StringBuilder identifier = new StringBuilder();
        identifier.append(getClass().getSimpleName());
        identifier.append(".");
        identifier.append(dataClass.getSimpleName());
        return new SimTableDef(identifier.toString(), dataClass);
    }

    /****************************************************************************************************
     * Retrieves a component contained within the panel by its identifier. This will search through all
     * editors and widgets of the panel and return the first component with a matching identifier.
     * <p>
     * @param identifier The identifier of the editor or widget.
     * @return The editor or widget.
     ***************************************************************************************************/
    public Component findComponent(String identifier) {
        return editorManager.findComponent(identifier);
    }

    /****************************************************************************************************
     * Retrieves all components contained within the dialog by the identifier. This will search through
     * all editors and widgets of the dialog and return an array of all the components with a matching
     * identifier.
     * <p>
     * @param identifier The identifier of the editors or widgets.
     * @return An array of editors or widgets with the specified identifier.
     ***************************************************************************************************/
    public Component[] findComponents(String identifier) {
        return editorManager.findComponents(identifier);
    }

    /****************************************************************************************************
     * Sets whether or not the editors and widgets within the panel should send actions.
     * <p>
     * @return True if editors and widgets within the panel should send actions, false if not.
     ***************************************************************************************************/
    public void setActionsEnabled(boolean enabled) {
        editorManager.setActionsEnabled(enabled);
    }

    /****************************************************************************************************
     * Retrieves whether or not actions are enabled for editors and wdigets.
     * <p>
     * @return True if actions are enabled, false otherwise.
     ***************************************************************************************************/
    public boolean isActionsEnabled() {
        return editorManager.isActionsEnabled();
    }

    /****************************************************************************************************
     * Sets the state of the panel to modified (meaning editor and widget content is considered altered).
     * <p>
     * @param modified True if the panel content is considered modified, false if not.
     ***************************************************************************************************/
    public void setContentModified(boolean modified) {
        editorManager.setContentModified(modified);
    }

    /****************************************************************************************************
     * Retrieves whether or not the panel contents have been modified.
     * <p>
     * @return True if the panel contents have been modified, false otherwise.
     ***************************************************************************************************/
    public boolean isContentModified() {
        return editorManager.isContentModified();
    }

    /****************************************************************************************************
     * Validates that all editors marked as required contain either selection or input data.
     * <p>
     * @throws OldUIException Thrown if an editor marked as required does not contain data.
     ***************************************************************************************************/
    public void validateRequiredContent() throws UIException {
        editorManager.validateRequiredContent();
    }

    /****************************************************************************************************
     * Executes a task asynchronously.
     * <p>
     * @param task The task to execute.
     ***************************************************************************************************/
    public void execute(UITask task) {
        UITaskExecutor.execute(this, task);
    }

    /****************************************************************************************************
     * Executes a task asynchronously.
     * <p>
     * @param task The task to execute.
     * @param message Message to display while executing.
     ***************************************************************************************************/
    public void execute(UITask task, MessageText message) {
        UITaskExecutor.execute(this, task, message);
    }

    /****************************************************************************************************
     * Fires a navigation command through the application frame.
     * <p>
     * @param command The location to navigate to or the two available keys "BACK" and "HOME".
     ***************************************************************************************************/
    public void navigate(String command) {
        Application.getApplicationFrame().navigate(command);
    }

    /****************************************************************************************************
     * Fires a navigation command through the application frame later.
     * <p>
     * @param command The location to navigate to or the two available keys "BACK" and "HOME".
     ***************************************************************************************************/
    public void navigateLater(final String command) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Application.getApplicationFrame().navigate(command);
            }
        });
    }

    /****************************************************************************************************
     * Clears all the exceptions from the panel.
     ***************************************************************************************************/
    public void clearExceptions() {
        editorManager.clearExceptions();
    }

    /****************************************************************************************************
     * Retrieves whether or not the panel currently contains any editors that are in an error state.
     * <p>
     * @return True if their is at least one editor in error state, otherwise false.
     ***************************************************************************************************/
    public boolean hasExceptions() {
        return editorManager.hasExceptions();
    }

    /****************************************************************************************************
     * Validates all the required editors and highlights those that do not contain data.
     ***************************************************************************************************/
    public void validateRequiredEditors() {
        editorManager.validateRequiredEditors();
    }

    /****************************************************************************************************
     * Validates permissions for all the editors within the manager.
     ***************************************************************************************************/
    public void validatePermissions() throws UIException {
        editorManager.validatePermissions();
    }

    /****************************************************************************************************
     * Will attempt to request focus on the navigation toolbar and thus the default button for the
     * screen. Executed in a SwingUtilities.invokeLater(), thereby not being executed until anything else
     * in the event queue is finished.
     ***************************************************************************************************/
    public void assignFocusInScreen() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Application.getApplicationFrame().getNavigationToolbar().requestFocusInWindow();
            }
        });
    }

    /****************************************************************************************************
     * Will attempt to request focus on the parameter component, but in a SwingUtilities.invokeLater(),
     * thereby not being executed until anything else in the event queue is finished.
     * <p>
     * @param component The component to receive focus.
     ***************************************************************************************************/
    protected void assignFocusInScreen(final JComponent component) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }

    /****************************************************************************************************
     * Displays a message in the status bar (defaults to RErrorSeverity.INFO);
     * <p>
     * @param message A message code to translate and display.
     ***************************************************************************************************/
    public void displayMessage(MessageText message) {
        UIStatusUtility.displayMessage(this, message);
    }

    /****************************************************************************************************
     * Displays a message in the status bar (defaults to RErrorSeverity.INFO);
     * <p>
     * @param message A message code to translate and display.
     * @param value A value to substiatue in the message.
     ***************************************************************************************************/
    public void displayMessage(MessageText message, String value) {
        UIStatusUtility.displayMessage(this, message, value);
    }

    /****************************************************************************************************
     * Displays a message in the status bar (defaults to RErrorSeverity.INFO);
     * <p>
     * @param component The component that should own the message.
     * @param message A message code to translate and display.
     ***************************************************************************************************/
    public void displayMessage(Component component, MessageText message) {
        UIStatusUtility.displayMessage(component, message);
    }

    /****************************************************************************************************
     * Logs a debug message.
     * <p>
     * @param message The message for the logger to handle.
     ***************************************************************************************************/
    protected void displayDebugMessage(MessageText message) {
        UILog.debug(getClass(), message);
    }

    /****************************************************************************************************
     * Displays a warning message in the status bar.
     * <p>
     * @param warning A warning message to translate and display.
     ***************************************************************************************************/
    public void displayWarning(MessageText message) {
        UIStatusUtility.displayWarning(this, message);
    }

    /****************************************************************************************************
     * Displays an error message in the status bar.
     * <p>
     * @param error An error message to translate and display.
     ***************************************************************************************************/
    public void displayError(MessageText message) {
        UIStatusUtility.displayException(this, message);
    }

    /****************************************************************************************************
     * Displays an error message in the status bar.
     * <p>
     * @param message An error message to translate and display.
     ***************************************************************************************************/
    public void displayError(MessageText message, String value) {
        UIStatusUtility.displayException(this, new BusinessException(message, value));
    }

    /****************************************************************************************************
     * Displays an error message in the status bar.
     * <p>
     * @param message A message text enum.
     * @param values Values to substitute in the message.
     ***************************************************************************************************/
    public void displayError(MessageText message, Object[] values) {
        UIStatusUtility.displayException(this, new BusinessException(message, values));
    }

    /****************************************************************************************************
     * Displays a RErrorEvent exception on the status bar.
     * <p>
     * @param exception A RErrorEvent (or sub-class thereof).
     ***************************************************************************************************/
    public void displayException(RErrorEvent event) {
        UIStatusUtility.displayException(this, event);
    }

    /****************************************************************************************************
     * Displays an exception in the appropriate manner. It determines if the exception is a UIException,
     * RuntimeException or other and calls the appropriate method.
     * <p>
     * @param component The source of the exception.
     * @param throwable A throwable exception.
     ***************************************************************************************************/
    public void displayException(Component component, Throwable exception) {
        UIStatusUtility.displayException(component, exception);
    }

    /****************************************************************************************************
     * Displays an exception in the appropriate manner. It determines if the exception is a UIException,
     * RuntimeException or other and calls the appropriate method.
     * <p>
     * @param throwable A throwable exception.
     ***************************************************************************************************/
    public void displayException(Throwable exception) {
        UIStatusUtility.displayException(this, exception);
    }

    /****************************************************************************************************
     * Puts a glass pane above the application while its working on something.
     * <p>
     * @param busy True if the application should show itself busy, false if not.
     ***************************************************************************************************/
    public void showScreenBusy(boolean busy) {
        UITaskManager.showBusy(busy);
    }
}
