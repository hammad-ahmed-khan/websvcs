package oracle.retail.sim.client.swing.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.IllegalComponentStateException;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.core.OracleFocusPolicy;
import oracle.retail.sim.client.swing.core.RetailEditorManager;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.RErrorEvent;
import oracle.retail.sim.client.swing.event.REventAdaptor;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.RStatusBar;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.panel.RButtonPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.task.UITask;
import oracle.retail.sim.client.swing.task.UITaskExecutor;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.WindowPlacer;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * This class sublcasses the standard JDialog class in the Swing package to provide custom functionality
 * for Oracle Retail. Dialog windows are modal windows that "lock" the system until exited.
 * WindowListener is fully defined so that subclasses only have to implement methods that require
 * functionality.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RDialog extends JDialog implements WindowListener {
    private static final long serialVersionUID = 3496214809476325152L;

    private RetailEditorManager editorManager = new RetailEditorManager();
    private REventAdaptor eventAdaptor = new REventAdaptor();
    private RPanel contentPanel = new RPanel();
    private RButtonPanel buttonPanel = new RButtonPanel();
    private RStatusBar statusBar = new RStatusBar();

    /****************************************************************************************************
     * Returns new RDialog associated with a parent JFrame.
     * <p>
     * @param frame The owner to associate this dialog box with.
     ***************************************************************************************************/
    public RDialog(JFrame frame) {
        this(frame, true);
    }

    /****************************************************************************************************
     * Returns new RDialog associated with a parent JDialog.
     * <p>
     * @param dialog The owner to associated this dialog box with.
     ***************************************************************************************************/
    public RDialog(JDialog dialog) {
        this(dialog, true);
    }

    /****************************************************************************************************
     * Returns new RDialog associated with a parent JFrame.
     * <p>
     * @param frame The owner to associate this dialog box with.
     * @param model True if the window should be modal, false if not.
     ***************************************************************************************************/
    public RDialog(JFrame frame, boolean modal) {
        super(frame, StringConstants.EMPTY, modal);
        initializeDialog();
    }

    /****************************************************************************************************
     * Returns new RDialog associated with a parent JDialog.
     * <p>
     * @param dialog The owner to associated this dialog box with.
     * @param model True if the window should be modal, false if not.
     ***************************************************************************************************/
    public RDialog(JDialog dialog, boolean modal) {
        super(dialog, StringConstants.EMPTY, modal);
        initializeDialog();
    }

    /****************************************************************************************************
     * Initializes the default setting of the dialog.
     ***************************************************************************************************/
    protected void initializeDialog() {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setFocusTraversalPolicy(new OracleFocusPolicy());
        addWindowListener(this);
        editorManager.setOwner(this);
        contentPanel.setEmptyBorder(5);
        layoutContents();
    }

    /****************************************************************************************************
     * Layout components on the dialog.
     ***************************************************************************************************/
    protected void layoutContents() {
        Container container = super.getContentPane();

        container.setLayout(new GridBagLayout());
        container.add(contentPanel, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));
        container.add(buttonPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        container.add(statusBar, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
    }

    /****************************************************************************************************
     * Return the internal content panel.
     * <p>
     * @param The internal content panel.
     ***************************************************************************************************/
    public Container getContentPane() {
        return contentPanel;
    }

    /****************************************************************************************************
     * Sets the <code>contentPane</code> property. This method is overriden strictly to provide better
     * documentation. The RDialog has been defined to have a button panel and a status bar. The usage of
     * setContentPane(container) should be strictly avoided unless defining an entirely new type of
     * framework dialog. The method setContentPane(RPanel) should be used instead!
     * <p>
     * @param contentPane the <code>contentPane</code> object for this dialog.
     *            <p>
     * @exception IllegalComponentStateException (a runtime exception) if the content pane
     *                parameter is <code>null</code>.
     ***************************************************************************************************/
    public void setContentPane(Container container) {
        super.setContentPane(container);
    }

    /****************************************************************************************************
     * Assigns the entire content panel of the dialog. This is the preferred setContentPane().
     * <p>
     * @param panel The panel to assign as the content pane.
     ***************************************************************************************************/
    public void setContentPane(RPanel panel) {
        if (panel != null) {
            super.getContentPane().removeAll();
            contentPanel = panel;
            layoutContents();
            repaint();
        }
    }

    /****************************************************************************************************
     * Sets the title of the window. This method provides automatic language translation.
     * <p>
     * @param title The title to set in the window.
     ***************************************************************************************************/
    public void setTitle(String title) {
        super.setTitle(Translator.getText(title));
    }

    /****************************************************************************************************
     * Adds a button to the button panel.
     * <p>
     * @param button The button to add.
     ***************************************************************************************************/
    public void addButton(RButton button) {
        buttonPanel.addButton(button, this);
    }

    /****************************************************************************************************
     * Assigns the default button to the root pane. This will cause the button to be pressed when the
     * ENTER key is pressed.
     * <p>
     * @param button The butotn to assign as the default button of the dialog.
     ***************************************************************************************************/
    public void setDefaultButton(RButton button) {
        getRootPane().setDefaultButton(button);
    }

    /****************************************************************************************************
     * Sets the button panel visible state in the dialog.
     * <p>
     * @param visible True if the button panel should be visible, false if not.
     ***************************************************************************************************/
    public void setButtonPanelVisible(boolean visible) {
        buttonPanel.setVisible(visible);
    }

    /****************************************************************************************************
     * Returns true if button panel current has mouse.
     ***************************************************************************************************/
    public boolean isMouseOverButton() {
        return buttonPanel.isMouseOverButton();
    }

    /****************************************************************************************************
     * Sets the status bar visible state in the dialog.
     * <p>
     * @param visible True if the status bar should be visible, false if not.
     ***************************************************************************************************/
    public void setStatusBarVisible(boolean visible) {
        statusBar.setVisible(visible);
    }

    /****************************************************************************************************
     * Retrieves the status bar associated with the dialog.
     * <p>
     * @return The status bar.
     ***************************************************************************************************/
    public RStatusBar getStatusBar() {
        return statusBar;
    }

    /****************************************************************************************************
     * Centers the dialog window on the desktop.
     ***************************************************************************************************/
    public void centerWindow() {
        WindowPlacer.centerWindow(this);
    }

    /****************************************************************************************************
     * Centers the dialog window within its owner (or parent) window.
     ***************************************************************************************************/
    public void centerOnOwner() {
        WindowPlacer.centerOnOwner(this);
    }

    /****************************************************************************************************
     * Centers the dialog window within its owner (or parent) window otherwise on desktop.
     ***************************************************************************************************/
    public void centerOnOwnerOrWindow() {
        WindowPlacer.centerOnOwnerOrWindow(this);
    }

    /****************************************************************************************************
     * Indents this window within another window from both the top and left side of the parent window.
     * <p>
     * @param parent The window to calculate the indentation from.
     * @param indent The number of pixels to indent.
     ***************************************************************************************************/
    public void indentWindow(Window parent, int indent) {
        WindowPlacer.indentWindow(parent, this, indent, indent);
    }

    /****************************************************************************************************
     * Adds a REventListener to the REventListener list.
     * <p>
     * @param listener The REventListener to add.
     ***************************************************************************************************/
    public void addREventListener(REventListener listener) {
        eventAdaptor.addREventListener(listener);
    }

    /****************************************************************************************************
     * Removes a REventListener from the REventListener list.
     * <p>
     * @param listener The REventListener to remove.
     ***************************************************************************************************/
    public void removeREventListener(REventListener listener) {
        eventAdaptor.removeREventListener(listener);
    }

    /****************************************************************************************************
     * Removes all REventListeners from the REventListener list.
     ***************************************************************************************************/
    public void removeAllREventListeners() {
        eventAdaptor.removeAllREventListeners();
    }

    /****************************************************************************************************
     * Notifies all listeners of an action event.
     * <p>
     * @param event An RActionEvent.
     ***************************************************************************************************/
    public void notifyREventListeners(RActionEvent event) {
        eventAdaptor.notifyREventListeners(event);
    }

    /****************************************************************************************************
     * Notifies all listeners of an error event.
     * <p>
     * @param event An RErrorEvent.
     ***************************************************************************************************/
    public void notifyREventListeners(RErrorEvent event) {
        eventAdaptor.notifyREventListeners(event);
    }

    /****************************************************************************************************
     * Implements the required REventListener method. It sends the event to all current REventListeners
     * of the panel.
     * <p>
     * @param event The RErrorEvent that triggered this listener method.
     ***************************************************************************************************/
    public void performErrorEvent(RErrorEvent event) {
        eventAdaptor.notifyREventListeners(event);
    }

    /****************************************************************************************************
     * Implements default window listener interface 'window iconified' method.
     * <p>
     * @param event Details about the window event that occurred.
     ***************************************************************************************************/
    public void windowIconified(WindowEvent event) {
    }

    /****************************************************************************************************
     * Implements default window listener interface 'window deiconified' method.
     * <p>
     * @param event Details about the window event that occurred.
     ***************************************************************************************************/
    public void windowDeiconified(WindowEvent event) {
    }

    /****************************************************************************************************
     * Implements default window listener interface 'window activated' method.
     * <p>
     * @param event Details about the window event that occurred.
     ***************************************************************************************************/
    public void windowActivated(WindowEvent event) {
    }

    /****************************************************************************************************
     * Implements default window listener interface 'window deactivated' method.
     * <p>
     * @param event Details about the window event that occurred.
     ***************************************************************************************************/
    public void windowDeactivated(WindowEvent event) {
    }

    /****************************************************************************************************
     * Implements default window listener interface 'window closed' method.
     * <p>
     * @param event Details about the window event that occurred.
     ***************************************************************************************************/
    public void windowClosed(WindowEvent event) {
    }

    /****************************************************************************************************
     * Implements default window listener interface 'window opened' method.
     * <p>
     * @param event Details about the window event that occurred.
     ***************************************************************************************************/
    public void windowOpened(WindowEvent event) {
    }

    /****************************************************************************************************
     * Implements default window listener interface 'window closing' method.
     * <p>
     * @param event Details about the window event that occurred.
     ***************************************************************************************************/
    public void windowClosing(WindowEvent event) {
        closeWindow();
    }

    /****************************************************************************************************
     * Closes the window.
     ***************************************************************************************************/
    public void closeWindow() {
        dispose();
    }

    /****************************************************************************************************
     * Stops editing in the dialog.
     ***************************************************************************************************/
    public void stopEditing() {
    }

    /****************************************************************************************************
     * Returns the current width of the screen minus 80 pixels. 
     * <p>
     * @return The current width of the screen minus 80 pixels.
     ***************************************************************************************************/
    public int getDefaultWidth() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize().width - 80;
    }

    /****************************************************************************************************
     * Retrieves a component contained within the dialog by its identifier. This will search through all
     * editors and widgets of the dialog and return the first component with a matching identifier.
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
     * Sets whether or not the editors and widgets should send actions.
     * <p>
     * @return True if editors and widgets should send actions, false if not.
     ***************************************************************************************************/
    public void setActionsEnabled(boolean enabled) {
        editorManager.setActionsEnabled(enabled);
    }

    /****************************************************************************************************
     * Retrieves whether or not actions are enabled for editors and wdigets.
     * <p>
     * @return True if actions are enabled, false otherwise.
     ***************************************************************************************************/
    public void isActionsEnabled() {
        editorManager.isActionsEnabled();
    }

    /****************************************************************************************************
     * Sets the state of the dialog to modified (meaning editor and widget content is considered
     * altered).
     * <p>
     * @param modified True if the dialog content is considered modified, false if not.
     ***************************************************************************************************/
    public void setContentModified(boolean modified) {
        editorManager.setContentModified(modified);
    }

    /****************************************************************************************************
     * Retrieves whether or not the dialog contents have been modified.
     * <p>
     * @return True if the dialog contents have been modified, false otherwise.
     ***************************************************************************************************/
    public boolean isContentModified() {
        return editorManager.isContentModified();
    }

    /****************************************************************************************************
     * Returns true if all the editors on the dialog are empty of content.
     * <p>
     * @return True if the dialog contents are empty of content, false otherwise.
     ***************************************************************************************************/
    public boolean isAllContentEmpty() {
        return editorManager.isAllContentEmpty();
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
     * Clears all the exceptions from the dialog.
     ***************************************************************************************************/
    public void clearExceptions() {
        editorManager.clearExceptions();
    }

    /****************************************************************************************************
     * Retrieves whether or not the dialog currently contains any editors that are in an error state.
     * <p>
     * @return True if their is at least one editor in error state, otherwise false.
     ***************************************************************************************************/
    public boolean hasExceptions() {
        return editorManager.hasExceptions();
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
     * Displays a message in the status bar (defaults to RErrorSeverity.INFO) and sends the status bar
     * into searching mode.
     * <p>
     * @param message A message code to translate and display.
     ***************************************************************************************************/
    public void displaySearchMessage(MessageText message) {
        UIStatusUtility.displaySearchMessage(this, message);
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
     * Displays a RErrorEvent exception on the status bar.
     * <p>
     * @param exception A RErrorEvent (or sub-class thereof).
     ***************************************************************************************************/
    public void displayException(RErrorEvent event) {
        UIStatusUtility.displayException(this, event);
    }

    /****************************************************************************************************
     * Displays a RErrorEvent exception on the status bar.
     * <p>
     * @param message The message to display.
     ***************************************************************************************************/
    public void displayException(MessageText message) {
        UIStatusUtility.displayException(this, message);
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
     * @param component The source of the exception.
     * @param throwable A throwable exception.
     ***************************************************************************************************/
    public void displayException(Component component, MessageText message, Throwable exception) {
        UIStatusUtility.displayException(component, message, exception);
    }

    /****************************************************************************************************
     * Displays an exception in the appropriate manner. It determines if the exception is a UIException,
     * RuntimeException or other and calls the appropriate method.
     * <p>
     * @param component The source of the exception.
     * @param throwable A throwable exception.
     ***************************************************************************************************/
    public void displayException(MessageText message, String param) {
        UIStatusUtility.displayException(this, new BusinessException(message, param));
    }

    /****************************************************************************************************
     * Displays an exception in the appropriate manner. It determines if the exception is a UIException,
     * RuntimeException or other and calls the appropriate method.
     * <p>
     * @param component The source of the exception.
     * @param throwable A throwable exception.
     ***************************************************************************************************/
    public void displayException(MessageText message, String[] params) {
        UIStatusUtility.displayException(this, new BusinessException(message, params));
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
     * Overrides the show option to validate both the required editors and widget permissions.
     ***************************************************************************************************/
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        try {
            editorManager.validateRequiredEditors();
            editorManager.validatePermissions();
        } catch (Throwable exception) {
            UILog.error(getClass(), exception);
        }
    }

    /****************************************************************************************************
     * Called after a system error to recover the state of the application. By default the dialog will
     * kill itself. This method should be overriden if subclasses need specific error recovery.
     ***************************************************************************************************/
    public void recover() {
        dispose();
    }
}
