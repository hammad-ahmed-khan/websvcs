package oracle.retail.sim.client.core;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.event.RErrorEvent;
import oracle.retail.sim.client.swing.frame.RTaskButtonPanel;
import oracle.retail.sim.client.swing.frame.RTaskPanel;
import oracle.retail.sim.client.swing.frame.RTaskTitlePanel;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.config.NavigationTaskItemData;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * SimTaskPanel is a subclass of the task panel for implementing tasks in the simplied application
 * framework.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class SimTaskPanel extends RTaskPanel {

    private RTaskTitlePanel titlePanel = new RTaskTitlePanel();
    private RPanel borderPanel = new RPanel();
    private RPanel centerPanel = new RPanel();
    private RLabel fillerLabel = new RLabel();
    private RTaskButtonPanel buttonPanel = new RTaskButtonPanel();
    private ScreenPanel currentScreen;

    private NavigationTaskItemData taskItemData;

    /****************************************************************************************************
     * Creates a new SimTaskPanel object.
     ***************************************************************************************************/
    protected SimTaskPanel() {
        initializeColors();
        initializePanels();
        layoutPanel();
    }

    /****************************************************************************************************
     * Creates a new SimTaskPanel object.
     ***************************************************************************************************/
    protected SimTaskPanel(String title) {
        setTaskTitle(title);
        initializeColors();
        initializePanels();
        layoutPanel();
    }

    /****************************************************************************************************
     * Initializes the colors.
     ***************************************************************************************************/
    private void initializeColors() {
        setBackground(UIManager.getColor(UIThemeName.TASKPANEL_BACKGROUND));
        setBorderBackground(UIManager.getColor(UIThemeName.TASKPANEL_BORDER_BACKGROUND));
    }

    /****************************************************************************************************
     * Initializes the panels.
     ***************************************************************************************************/
    private void initializePanels() {
        borderPanel.setBorder(null);
        buttonPanel.setBorder(null);

        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        fillerLabel.setBackground(getBackground());
    }

    /****************************************************************************************************
     * Lays out the task panel.
     ***************************************************************************************************/
    private void layoutPanel() {
        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(borderPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /****************************************************************************************************
     * Assigns the navigation task item data to the task panel.
     * <p>
     *
     * @param data The navigation task item data to assign.
     ***************************************************************************************************/
    protected void setNavigationTaskItemData(NavigationTaskItemData data) {
        taskItemData = data;
    }

    /****************************************************************************************************
     * Retrieves the navigation task item data of the task panel.
     * <p>
     * @return The navigation task item data.
     ***************************************************************************************************/
    protected NavigationTaskItemData getNavigationTaskItemData() {
        return taskItemData;
    }

    /****************************************************************************************************
     * Sets the background color of the panel and the work space area.
     * <p>
     * @param color The color to assign to the work space area.
     ***************************************************************************************************/
    public void setBackground(Color color) {
        super.setBackground(color);

        if (centerPanel != null) {
            centerPanel.setBackground(color);
        }
    }

    /****************************************************************************************************
     * Sets the background color of the border area of the task panel.
     * <p>
     * @param color The color to assign to the border area.
     ***************************************************************************************************/
    public void setBorderBackground(Color color) {
        titlePanel.setBackground(color);
        borderPanel.setBackground(color);
        buttonPanel.setBackground(color);
    }

    /****************************************************************************************************
     * Retrieves the background color of the border area of the task panel.
     * <p>
     * @return The background color of the border area of the task panel.
     ***************************************************************************************************/
    public Color getBorderBackground() {
        return borderPanel.getBackground();
    }

    /****************************************************************************************************
     * Sets the task title of the task panel.
     * <p>
     * @param title The title to assign to the task panel.
     ***************************************************************************************************/
    public void setTaskTitle(String title) {
        titlePanel.setTaskTitle(title);
    }

    /****************************************************************************************************
     * Sets the task description of the task panel. This text is displayed to the right of the title, but
     * does not need to be a description.
     * <p>
     * @param description A text string.
     ***************************************************************************************************/
    public void setTaskDescription(String description) {
        titlePanel.setTaskDescription(description);
    }

    /****************************************************************************************************
     * Sets the task resize state.
     * <p>
     * @param isMaximized True if the task panel is maximized, false if it is not.
     ***************************************************************************************************/
    protected void setTaskResizeState(boolean isMaximized) {
        titlePanel.setTaskResizeState(isMaximized);
    }

    /****************************************************************************************************
     * Adds a new button to the task button area. All buttons added to this panel are converted to chrome
     * colored.
     * <p>
     * @param button The button to add.
     ***************************************************************************************************/
    public void addButton(JButton button) {
        button.setBackground(getBorderBackground());
        button.setForeground(getForeground());

        buttonPanel.addButton(button);
    }

    /****************************************************************************************************
     * Assigns the screen to display in the content area.
     * <p>
     * @param screen The SimScreen to display.
     ***************************************************************************************************/
    public void setScreen(ScreenPanel screen) {
        currentScreen = screen;
        centerPanel.add(currentScreen, BorderLayout.CENTER);
        validate();
    }

    /****************************************************************************************************
     * This method is called when the application attempts to recover from a fatal error. The default
     * implementation kills the curren task with no cleanup. Override this method to provided custom
     * recovery for specific tasks.
     ***************************************************************************************************/
    public void recover() {
        killTask();
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
     * @param message A message code to translate and display.
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
     * Displays an exception in the appropriate manner. This first passes along the exception to each
     * content panel to display problems and then passes the exception to the application frame to be
     * dealt with.
     * <p>
     * @param throwable A throwable exception.
     ***************************************************************************************************/
    public void displayException(Throwable throwable) {
        if (currentScreen != null) {
            currentScreen.displayException(throwable);
        }
        UIStatusUtility.displayException(this, throwable);
    }

    /****************************************************************************************************
     * Protected method called by the application frame to determine if a task is startable or not. It
     * calls isStartable() on the subclass when it is done doing its own validation of permissions.
     * <p>
     * @return True if the task is startable, false otherwise.
     ***************************************************************************************************/
    protected boolean validateTask() throws UIException {
        if (currentScreen != null) {
            currentScreen.validateRequiredEditors();
            currentScreen.validatePermissions();
        }
        String name = StringUtility.getRemainingText(getClass().getName(), StringConstants.DOT);
        JButton[] buttonArray = buttonPanel.getButtons();
        for (JButton element : buttonArray) {
            if (element instanceof RButton) {
                ((RButton) element).validatePermission(name);
            }
        }
        return isStartable();
    }

    /****************************************************************************************************
     * toDisplayString() returns the title of the task.
     ***************************************************************************************************/
    public String toDisplayString() {
        String title = titlePanel.getTaskTitle();
        if (StringUtility.isNullOrEmpty(title)) {
            return toString();
        }
        return title;
    }
}
