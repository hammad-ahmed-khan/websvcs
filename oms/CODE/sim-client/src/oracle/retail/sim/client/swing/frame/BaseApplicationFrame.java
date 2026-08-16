package oracle.retail.sim.client.swing.frame;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.RErrorEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.navigation.DefaultNavigationDataBuilder;
import oracle.retail.sim.client.swing.navigation.DefaultSecurityManager;
import oracle.retail.sim.client.swing.navigation.NavigationDataBuilder;
import oracle.retail.sim.client.swing.navigation.NavigationSecurityManager;
import oracle.retail.sim.client.swing.navigation.NavigationTabbedPane;
import oracle.retail.sim.client.swing.navigation.SecurityInterface;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.client.swing.util.WindowPlacer;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.config.NavigationData;
import oracle.retail.sim.common.config.NavigationTaskItemData;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * The BaseApplicationFrame class is meant to contain a bunch of generic code to produce an general
 * application frame format (navigation on the left, work space on the right, and status bar on the
 * bottom. This class is abstract and must be subclassed to produce the local application frame of a
 * project. This application frame uses the OS level window decorations.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class BaseApplicationFrame extends RFrame implements ApplicationFrame {

    private static SecurityInterface securityManager = new DefaultSecurityManager();

    private static final String SUSPENDED_TASKS = "Suspended Tasks";
    private static final int SUSPENDED_MAX = 2;

    protected NavigationDataBuilder navigationBuilder = new DefaultNavigationDataBuilder();
    protected NavigationData navigationData;
    protected NavigationTabbedPane navigationTabbedPane = new NavigationTabbedPane();

    protected RStatusBar statusBar = new RStatusBar();
    protected RPanel navigationPanel = new RPanel();
    protected RTaskPanel defaultTaskPanel = new LogoutTaskPanel();
    protected RTaskPanel currentPanel = defaultTaskPanel;

    protected boolean duplicateTask;
    protected boolean isTaskMaximized;
    protected boolean isNavigationVisible = true;
    protected boolean isNavigationInitialized;

    private List suspendedTaskList = new ArrayList<>();
    private List suspendedTaskButtonList = new ArrayList<>();
    private List genericTaskButtonList = new ArrayList<>();
    private Map suspendedTaskMap = new HashMap<>();

    /****************************************************************************************************
     * Creates a new BaseApplicationFrame.
     ***************************************************************************************************/
    protected BaseApplicationFrame() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(true);
        layoutFrame();
    }

    /****************************************************************************************************
     * Initializes and lays out the components in the frame.
     ***************************************************************************************************/
    protected void layoutFrame() {
        navigationPanel.setOpaque(false);
        navigationPanel.setEmptyBorder(2, 0, 0, 0);
        navigationPanel.setLayout(new BorderLayout());
        navigationPanel.add(navigationTabbedPane, BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(navigationPanel, BorderLayout.WEST);
        getContentPane().add(defaultTaskPanel, BorderLayout.CENTER);
        getContentPane().add(statusBar, BorderLayout.SOUTH);
    }

    /****************************************************************************************************
     * Retrieves the security manager used for the application.
     ***************************************************************************************************/
    public SecurityInterface getSecurityManager() {
        return securityManager;
    }

    /****************************************************************************************************
     * Centers the window on the desktop.
     ***************************************************************************************************/
    public void centerWindow() {
        WindowPlacer.centerWindow(this);
    }

    /****************************************************************************************************
     * Sets the visible state of the navigation area of the application.
     * <p>
     * @param visible True if the navigation area should be visible, false if not.
     ***************************************************************************************************/
    public void setNavigationVisible(boolean visible) {
        isNavigationVisible = visible;
        if (isTaskMaximized) {
            navigationPanel.setVisible(isNavigationVisible);
        }
    }

    /****************************************************************************************************
     * Initializes the application frame. This will make a call to the abstract method doInitialize().
     ***************************************************************************************************/
    public void initialize() throws UIException {
        statusBar.activateProgressIndicator();
        initializeComponents();
        intitializeApplicationFrame();
        initializeApplication();
        initializeNavigation();
        statusBar.deactivateProgressIndicator();
    }

    /****************************************************************************************************
     * Initializes inner componenets.
     ***************************************************************************************************/
    private void initializeComponents() {
        RTaskButton taskButton = new RTaskButton(SUSPENDED_TASKS);
        taskButton.addActionListener(createAllTasksListeners());
        genericTaskButtonList.add(taskButton);
    }

    /****************************************************************************************************
     * Initializes application frame
     ***************************************************************************************************/
    private void intitializeApplicationFrame() {
        ApplicationInternal.setApplicationFrame(this);
    }

    /****************************************************************************************************
     * Initializes the navigation by ciphering the data through security and then displaying it in the
     * navigation panel.
     ***************************************************************************************************/
    private void initializeNavigation() throws UIException {
        navigationData = navigationBuilder.getNavigationData();
        navigationData = NavigationSecurityManager.getSecureNavigationData(navigationData);
        navigationTabbedPane.initializeNavigation(navigationData);
        isNavigationInitialized = true;
    }

    /****************************************************************************************************
     * Installs new navigation data builder. This will only work up until the point that the navigation
     * data has been initialized.
     * <p>
     * @param builder The builder responsible for creating the navigation data.
     ***************************************************************************************************/
    public void installNavigationBuilder(NavigationDataBuilder builder) throws UIException {
        if (builder != null && !isNavigationInitialized) {
            navigationBuilder = builder;
        }
    }

    /****************************************************************************************************
     * Installs new navigation security object.
     * <p>
     * @param manager The class responsible for handling navigation security.
     ***************************************************************************************************/
    public void installNavigationSecurity(SecurityInterface manager) {
        if (!isNavigationInitialized) {
            NavigationSecurityManager.installNavigationSecurity(manager);
            securityManager = manager;
        }
    }

    /****************************************************************************************************
     * Assigns the default task panel to be displayed when no other tasks are active. If the parameter is
     * null, it is reset to the system default task panel.
     * <p>
     * @param taskPanel A task panel to assign as the default task panel. t*************
     ***************************************************************************************************/
    public void setDefaultTaskPanel(RTaskPanel taskPanel) {
        if (taskPanel == null) {
            taskPanel = new LogoutTaskPanel();
        }
        boolean displayPanel = currentPanel == defaultTaskPanel;

        defaultTaskPanel = taskPanel;
        defaultTaskPanel.init();
        defaultTaskPanel.start();

        if (displayPanel) {
            resetDefaultTaskPanel();
        }
    }

    /****************************************************************************************************
     * Navigates to the new task indicated by the selected menu item. This is the default implementation
     * of navigate(). Override this method to supply custom navigation logic. The default implements
     * requires that the menu item action command in the .xml file contain the full pathname to a class
     * that subclasses RTaskPanel.
     * <p>
     * @param taskItemClassPath The full class path of the task item to navigate to. This should match
     *            the class path in the .xml file.
     ***************************************************************************************************/
    public void navigate(String taskItemClassPath) {
        navigate(navigationData.findTaskItem(taskItemClassPath));
    }

    /****************************************************************************************************
     * Navigates to the new task indicated by the selected menu item. This is the default implementation
     * of navigate(). Override this method to supply custom navigation logic. The default implements
     * requires that the menu item action command in the .xml file contain the full pathname to a class
     * that subclasses RTaskPanel.
     * <p>
     * @param data The NavigationMenuItemData that contains the navigational information.
     ***************************************************************************************************/
    public void navigate(NavigationTaskItemData data) {
        NavigationTaskItemData currentData = currentPanel.getNavigationTaskItemData();

        if (currentData == data && currentData != null && !currentData.allowsMultiple()) {
            return;
        }
        if (!stopCurrentTask()) {
            return;
        }
        if (data == null) {
            clearTaskPanel();
            return;
        }
        displaySearchMessage(UIMessageText.MESSAGE_LOADING_TASK);

        String taskClass = data.getActionCommand();
        String taskName = StringUtility.getRemainingText(taskClass, StringConstants.DOT);

        RTaskPanel taskPanel;
        try {
            taskPanel = (RTaskPanel) Class.forName(taskClass).newInstance();
            taskPanel.setNavigationTaskItemData(data);
        } catch (Throwable exception) {
            UILog.error(getClass(), UIMessageText.UNABLE_TO_START_TASK, taskName, exception);
            displayException(new UIException(UIMessageText.TASK_NOT_CREATED, taskName));
            displayTaskPanel(defaultTaskPanel);
            return;
        }
        SwingUtilities.invokeLater(new NavigationThread(taskPanel, data.allowsMultiple(), taskName));
    }

    /****************************************************************************************************
     * Second half of the navigation is seperated out into its own method so that it can be run in a
     * local private thread.
     * <p>
     * @param taskPanel The task panel to display.
     * @param duplicate True if the task can be duplicated.
     * @param task The task name to display.
     ***************************************************************************************************/
    private void navigate(RTaskPanel taskPanel, boolean duplicate, String task) {
        taskPanel.init();

        try {
            if (!taskPanel.validateTask()) {
                resetDefaultTaskPanel();
                return;
            }
        } catch (UIException exception) {
            resetDefaultTaskPanel();
            displayException(exception);
            return;
        } catch (Throwable throwable) {
            resetDefaultTaskPanel();
            displayNavigationException(throwable);
            return;
        }
        taskPanel.start();
        displayTaskPanel(taskPanel);
        duplicateTask = duplicate;
        clearStatusBar();
    }

    /****************************************************************************************************
     * Resets to the default task panel.
     ***************************************************************************************************/
    private void resetDefaultTaskPanel() {
        displayTaskPanel(defaultTaskPanel);
        duplicateTask = false;
    }

    /****************************************************************************************************
     * Displays the appropriate task panel within the workspace.
     ***************************************************************************************************/
    protected void displayTaskPanel(RTaskPanel panel) {
        Container container = getContentPane();
        container.setVisible(false);

        if (currentPanel != null) {
            container.remove(currentPanel);
        }
        currentPanel = panel;

        container.add(currentPanel, BorderLayout.CENTER);
        container.setVisible(true);

        currentPanel.requestFocusInWindow();
    }

    /****************************************************************************************************
     * Stores a task in the suspended task list.
     ***************************************************************************************************/
    protected void storeTask(RTaskPanel panel) {
        if (suspendedTaskList.contains(panel)) {
            return;
        }
        RTaskButton taskButton = new RTaskButton(panel);
        taskButton.addActionListener(createSingleTaskListener());
        suspendedTaskButtonList.add(taskButton);

        suspendedTaskList.add(panel);
        suspendedTaskMap.put(panel, taskButton);

        displayTaskButtons();
    }

    /****************************************************************************************************
     * Removes a task from the suspended task list.
     ***************************************************************************************************/
    protected void removeTask(RTaskPanel panel) {
        suspendedTaskList.remove(panel);

        if (suspendedTaskList.isEmpty()) {
            suspendedTaskButtonList.clear();
            suspendedTaskMap.clear();
            statusBar.clearSuspendedTasks();
            return;
        }

        RTaskButton taskButton = (RTaskButton) suspendedTaskMap.remove(panel);
        if (taskButton != null) {
            suspendedTaskButtonList.remove(taskButton);
        }
        displayTaskButtons();
    }

    /****************************************************************************************************
     * Displays the task buttons.
     ***************************************************************************************************/
    private void displayTaskButtons() {
        if (suspendedTaskList.size() > SUSPENDED_MAX) {
            statusBar.displaySuspendedTasks(genericTaskButtonList);
        } else {
            statusBar.displaySuspendedTasks(suspendedTaskButtonList);
        }
    }

    /****************************************************************************************************
     * Creates a single suspended task listener. It displays the suspended task when clicked.
     ***************************************************************************************************/
    private ActionListener createSingleTaskListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                RTaskButton button = (RTaskButton) event.getSource();
                displaySuspendedTask(button.getTask());
            }
        };
    }

    /****************************************************************************************************
     * Creates the all suspended tasks button listener. It displays the popup window when clicked.
     ***************************************************************************************************/
    private ActionListener createAllTasksListeners() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                popupSuspendedTaskList((RTaskButton) event.getSource());
            }
        };
    }

    /****************************************************************************************************
     * Clears the current task panel from the work space.
     ***************************************************************************************************/
    public void clearTaskPanel() {
        try {
            if (currentPanel.isStoppable()) {
                currentPanel.stop();
                removeTask(currentPanel);
                duplicateTask = false;
                displayTaskPanel(defaultTaskPanel);
                clearStatusBar();
            }
        } catch (UIException exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Clears the current task panel from the work space.
     ***************************************************************************************************/
    public void killTaskPanel() {
        removeTask(currentPanel);
        currentPanel = null;
        duplicateTask = false;
        displayTaskPanel(defaultTaskPanel);
        clearStatusBar();
    }

    /****************************************************************************************************
     * Displays the popup suspended task list aligning to the button passed in.
     ***************************************************************************************************/
    private void popupSuspendedTaskList(RTaskButton button) {
        ApplicationTaskDialog dialog = new ApplicationTaskDialog(this, suspendedTaskList);
        dialog.registerAction(createTaskListener(), SUSPENDED_TASKS);
        WindowPlacer.alignToComponent(this, button, dialog, false, true);
        dialog.setVisible(true);
    }

    /****************************************************************************************************
     * Creates a task listener for the suspended task dialog.
     ***************************************************************************************************/
    private REventListener createTaskListener() {
        return new REventListener() {
            public void performErrorEvent(RErrorEvent event) {
            }

            public void performActionEvent(RActionEvent event) {
                displaySuspendedTask((RTaskPanel) event.getEventData());
            }
        };
    }

    /****************************************************************************************************
     * Attempt to redisplay a suspended task in the workspace.
     ***************************************************************************************************/
    private void displaySuspendedTask(RTaskPanel panel) {
        if (stopCurrentTask()) {
            displayTaskPanel(panel);
            duplicateTask = true;
        }
    }

    /****************************************************************************************************
     * Attempts to cleanly stop the current task.
     * <p>
     * @return True if the current task was stopped, false otherwise;
     ***************************************************************************************************/
    private boolean stopCurrentTask() {
        try {
            if (duplicateTask) {
                storeTask(currentPanel);
            }
            if (currentPanel.isStoppable()) {
                currentPanel.stop();
                clearStatusBar();
                return true;
            }
        } catch (UIException exception) {
            displayException(exception);
        }
        return false;
    }

    /****************************************************************************************************
     * Implements the hot key listener to pop the Navigator Window when the hot key is pressed.
     ***************************************************************************************************/
    public boolean hotKeyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_F2:
                doSwapTaskResizeState();
                return false;
            case KeyEvent.VK_F3:
                navigationTabbedPane.requestFocusInWindow();
                return false;
            case KeyEvent.VK_F4:
                currentPanel.requestFocusInWindow();
                return false;
        }
        return doHotKeyPressed(keyCode);
    }

    /****************************************************************************************************
     * Swaps the task panel maximize state.
     ***************************************************************************************************/
    public void doSwapTaskResizeState() {
        isTaskMaximized = !isTaskMaximized;

        currentPanel.setTaskResizeState(isTaskMaximized);

        if (isTaskMaximized && isNavigationVisible) {
            navigationPanel.setVisible(false);
        } else if (isNavigationVisible) {
            navigationPanel.setVisible(true);
        }
    }

    /****************************************************************************************************
     * Returns whether or not the ApplicationFrame is closeable. This delegates to exitApplication().
     * <p>
     * @return True if the ApplicationFrame is closeable, false otherwise.
     ***************************************************************************************************/
    public boolean isCloseable() {
        return exitApplication();
    }

    /****************************************************************************************************
     * Retrieves the status bar associated with the application.
     * <p>
     * @return The status bar.
     ***************************************************************************************************/
    public RStatusBar getStatusBar() {
        return statusBar;
    }

    /****************************************************************************************************
     * Helper method to clear the status bar (for readability)
     ***************************************************************************************************/
    private void clearStatusBar() {
        statusBar.clear();
    }

    /****************************************************************************************************
     * Helper method that displays a search message in the application.
     ***************************************************************************************************/
    private void displaySearchMessage(MessageText message) {
        UIStatusUtility.displaySearchMessage(this, message);
    }

    /****************************************************************************************************
     * Helper method that displays an exception in the application.
     ***************************************************************************************************/
    private void displayException(UIException exception) {
        UIStatusUtility.displayException(this, exception);
    }

    /****************************************************************************************************
     * Helper method that displays a fatal navigation exception in the application.
     ***************************************************************************************************/
    private void displayNavigationException(Throwable throwable) {
        UIStatusUtility.displayException(this, UIMessageText.TASK_NOT_CREATED_SEVERE, throwable);
    }

    /****************************************************************************************************
     *
     * INNER CLASS - NAVIGATION THREAD - LOADS A TASK PANEL ASYNCHRONOUSLY
     *
     ***************************************************************************************************/
    private class NavigationThread extends Thread {

        private RTaskPanel taskPanel;
        private boolean taskDuplicate;
        private String taskName;

        private NavigationThread(RTaskPanel panel, boolean duplicate, String name) {
            taskPanel = panel;
            taskDuplicate = duplicate;
            taskName = name;
        }

        public void run() {
            navigate(taskPanel, taskDuplicate, taskName);
        }
    }

    /****************************************************************************************************
     * Called by the fatal dialog window after the fatal error is displayed. The default version of this
     * method call recover on the current task panel to allow it to clean itself up. This task panel
     * should not through exceptions, but kill itself if an exception occurs during cleanup processing.
     * This method should be overridden by all subclasses that wish to supply custom severe error
     * recovery.
     ***************************************************************************************************/
    public void recover() {
        if (currentPanel != null) {
            currentPanel.recover();
        }
    }
}
