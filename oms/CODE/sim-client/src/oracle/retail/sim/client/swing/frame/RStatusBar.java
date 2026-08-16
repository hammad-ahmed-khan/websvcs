package oracle.retail.sim.client.swing.frame;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIExceptionDisplayer;
import oracle.retail.sim.client.swing.util.WindowPlacer;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.logging.LogService;

/******************************************************************************************
 * This subclasses RPanel and provides a status bar that displays status messages, error
 * messages, search indicator and activity indicates.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RStatusBar extends RPanel implements ActionListener {
    private static final long serialVersionUID = -207914620957864092L;

    private RMessageLabel messageLabel = new RMessageLabel();
    private RButton messageButton = new RButton("Details");
    private RProgressIndicator progressIndicator = new RProgressIndicator();
    private RPanel indicatorPanel = new RPanel();
    private RPanel suspendedPanel = new RPanel();
    private RStatusWindow statusWindow;
    private RStatusTimer messageTimer;

    private Map<String, RStatusIndicator> indicatorMap = new HashMap<>(5);
    private UIException exception;

    /******************************************************************************************
     * Returns new RStatusBar object.
     *****************************************************************************************/
    public RStatusBar() {
        initializeStatusBar();
        initializeMessageTimer();
        layoutStatusBar();
    }

    /******************************************************************************************
     * Initialize all the colors and widgets.
     *****************************************************************************************/
    private void initializeStatusBar() {
        setBackground(UIManager.getColor(UIThemeName.STATUSBAR_BACKGROUND));
        setForeground(UIManager.getColor(UIThemeName.STATUSBAR_FOREGROUND));
        setOpaque(false);

        messageButton.setBackground(UIManager.getColor(UIThemeName.MESSAGE_BUTTON_BACKGROUND));
        messageButton.setForeground(UIManager.getColor(UIThemeName.MESSAGE_BUTTON_FOREGROUND));
        messageButton.setVisible(false);
        messageButton.addActionListener(this);

        indicatorPanel.setOpaque(false);
        indicatorPanel.setVisible(false);

        suspendedPanel.setOpaque(false);
        suspendedPanel.setVisible(false);
        suspendedPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    }

    /******************************************************************************************
     * Initializes the message timer.
     *****************************************************************************************/
    private void initializeMessageTimer() {
        messageTimer = new RStatusTimer(this);
    }

    /******************************************************************************************
     * Layout the internal widgets on the status bar.
     *****************************************************************************************/
    private void layoutStatusBar() {
        indicatorPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 0));

        setBorder(BorderFactory.createEmptyBorder(5, 5, 3, 5));

        setLayout(new GridBagLayout());
        add(suspendedPanel, GridTool.constraints(0, 0, 1, 1, 0, 0, 1, 3, 0, 0, 0, 2));
        add(messageLabel, GridTool.constraints(1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 5));
        add(messageButton, GridTool.constraints(2, 0, 1, 1, 0, 0, 1, 3, 0, 0, 0, 5));
        add(progressIndicator, GridTool.constraints(3, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 5));
        add(indicatorPanel, GridTool.constraints(4, 0, 1, 1, 0, 0, 1, 3, 0, 0, 0, 0));
    }

    /*****************************************************************************************
     * Implements the action listener method for when the details button is pressed. This
     * displays a popup window attached to the top level window of the status bar.
     *****************************************************************************************/
    public void actionPerformed(ActionEvent event) {
        showStatusWindow();
    }

    /*****************************************************************************************
     * Adds a status indicator to the indicator panel. If the same name is used twice,
     * the indicator will replace the previous indicator using the same name.
     * <p>
     *@param name The name of the status indicator - this is required.
     *@param indicator The RStatusIndicator object to display in the indicator panel.
     *****************************************************************************************/
    public void addIndicator(String name, RStatusIndicator indicator) {
        if (indicator == null) {
            throw new IllegalArgumentException("Indicator cannot be null.");
        }
        if (name == null || name.trim().length() < 1) {
            throw new IllegalArgumentException("A name is required for the indicator.");
        }
        indicator.setName(name);

        addIndicator(indicator);
    }

    /*****************************************************************************************
     * Adds a status indicator to the indicator panel. The indicator is required to have a name.
     * <p>
     *@param indicator The RStatusIndicator object to display in the indicator panel.
     *****************************************************************************************/
    public void addIndicator(RStatusIndicator indicator) {
        if (indicator == null) {
            throw new IllegalArgumentException("Indicator cannot be null.");
        }

        String name = indicator.getName();

        if (name == null || name.trim().length() < 1) {
            throw new IllegalArgumentException("A name is required for the indicator.");
        }

        removeIndicator(name);

        indicator.setName(name);
        indicator.setMinimumHeight(messageLabel.getPreferredSize().height);

        indicatorMap.put(name, indicator);

        indicatorPanel.add(indicator);
        indicatorPanel.setVisible(true);

        repaintStatusBar();
    }

    /*****************************************************************************************
     * Remove an indicator from the status bar.
     * <p>
     * @param name The name of the indicator to be removed from the status bar.
     *****************************************************************************************/
    public void removeIndicator(String name) {
        RStatusIndicator indicator = indicatorMap.get(name);

        if (indicator != null) {
            indicatorPanel.remove(indicator);
        }

        indicatorMap.remove(name);

        if (indicatorMap.isEmpty()) {
            indicatorPanel.setVisible(false);
        }
        repaintStatusBar();
    }

    /*****************************************************************************************
     * Assigns indicator active state.
     * <p>
     * @param name The name of the indicator.
     * @param isActive True if the indicator should be active, false if not.
     *****************************************************************************************/
    public void setIndicatorActive(String name, boolean isActive) {
        RStatusIndicator indicator = indicatorMap.get(name);
        if (indicator != null) {
            indicator.setActive(isActive);
        }
    }

    /*****************************************************************************************
     * Displays the list of task buttons as the suspended tasks in the status bar.
     * <p>
     * @param buttons The list of suspended task buttons.
     *****************************************************************************************/
    protected void displaySuspendedTasks(List<RTaskButton> buttons) {
        suspendedPanel.removeAll();
        for (RTaskButton taskButton : buttons) {
            suspendedPanel.add(taskButton);
        }
        suspendedPanel.setVisible(true);
        repaintStatusBar();
    }

    /*****************************************************************************************
     * Clears all suspended tasks button from the status bar.
     *****************************************************************************************/
    protected void clearSuspendedTasks() {
        suspendedPanel.removeAll();
        suspendedPanel.setVisible(false);
        repaintStatusBar();
    }

    /*****************************************************************************************
     * Attempts to repaint the status bar.
     *****************************************************************************************/
    private void repaintStatusBar() {
        Container container = getTopLevelAncestor();
        if (container != null) {
            container.validate();
        }
    }

    /*****************************************************************************************
     * Displays the status window if the status bar currently has an exception.
     *****************************************************************************************/
    public void showStatusWindow() {
        if (exception != null) {
            SwingUtilities.invokeLater(new StatusWindowDisplayer(exception));
        }
    }

    /*****************************************************************************************
     * Activate the progress indicator.
     *****************************************************************************************/
    public void activateProgressIndicator() {
        progressIndicator.activate();
    }

    /*****************************************************************************************
     * Deactive the progress indicator.
     *****************************************************************************************/
    public void deactivateProgressIndicator() {
        progressIndicator.deactivate();
    }

    /*****************************************************************************************
     * Clears the status bar of all text and stops the progress indicator if it is running.
     *****************************************************************************************/
    public void clear() {
        exception = null;
        messageLabel.clear();
        messageButton.setVisible(false);
        deactivateProgressIndicator();
    }

    /*****************************************************************************************
     * Clears the status bar when the timer is up for a warning/info message.
     *****************************************************************************************/
    protected synchronized void clearFromTimer() {
        if (messageLabel.isErrorDisplayed()) {
            return;
        }
        clear();
    }

    /*****************************************************************************************
     * Retrieves whether or not an warning message is currently being displayed.
     * <p>
     * @return True if an warning message is currently being displayed, false if not.
     *****************************************************************************************/
    public boolean isWarningDisplayed() {
        return messageLabel.isWarningDisplayed();
    }

    /*****************************************************************************************
     * Retrieves whether or not an error message is currently being displayed.
     * <p>
     * @return True if an error message is currently being displayed, false if not.
     *****************************************************************************************/
    public boolean isErrorDisplayed() {
        return messageLabel.isErrorDisplayed();
    }

    /*****************************************************************************************
     * Displays an exception. This will activate the details button if the exception has
     * multiple messages.
     * <p>
     * @param exception The exception to display in the details window.
     *****************************************************************************************/
    public void displayException(UIException exception) {
        displayStatus(null, null, exception.getSeverity(), exception);
    }

    /*****************************************************************************************
     * Displays text in the status bar and starts the progress indicator.
     * <p>
     *@param message The status text to display in the window.
     *@param severity The severity level (INFO, WARNING, FATAL, etc).
     *****************************************************************************************/
    public void displaySearchStatus(MessageText message, RErrorSeverity severity) {
        displayStatus(message, null, severity, null);
        activateProgressIndicator();
    }

    /*****************************************************************************************
     * Displays text in the status bar changing the background and foreground color of the
     * status bar as appropriate.
     * <p>
     *@param message The status text to display in the window.
     *@param severity The severity level (INFO, WARNING, FATAL, etc).
     *****************************************************************************************/
    public void displayStatus(MessageText message, RErrorSeverity severity) {
        displayStatus(message, null, severity, null);
    }

    /*****************************************************************************************
     * Displays text in the status bar changing the background and foreground color of the
     * status bar as appropriate.
     * <p>
     *@param message The status text to display in the window.
     *@param severity The severity level (INFO, WARNING, FATAL, etc).
     *****************************************************************************************/
    public void displayStatus(MessageText message, String[] messageValues, RErrorSeverity severity) {
        displayStatus(message, messageValues, severity, null);
    }

    /*****************************************************************************************
     * Displays text in the status bar changing the background and foreground color of the
     * status bar as appropriate. This method automatically de-activates the progress indicator
     * so that the presence of a new status always stops it.
     * <p>
     *@param message The status text to display in the window.
     *@param severity The severity level (INFO, WARNING, FATAL, etc).
     *****************************************************************************************/
    private void displayStatus(MessageText message, String[] messageValues, RErrorSeverity severity, UIException exception) {
        this.exception = exception;

        deactivateProgressIndicator();

        if (severity == RErrorSeverity.WARNING) {
            messageLabel.setWarningFormat();
            messageTimer.deactivate();
        } else if (severity == RErrorSeverity.ERROR) {
            messageLabel.setErrorFormat();
            messageTimer.deactivate();
        } else if (severity == RErrorSeverity.FATAL) {
            messageLabel.setErrorFormat();
            messageTimer.deactivate();
        } else {
            messageLabel.setMessageFormat();
            messageTimer.activate();
        }

        if (exception != null) {
            messageButton.setVisible(severity != RErrorSeverity.INFO);
        } else {
            messageButton.setVisible(false);
        }

        String translatedMessage = null;
        if (exception != null) {
            translatedMessage = new UIExceptionDisplayer().getDisplayText(exception);
        } else if ((message != null) && (messageValues != null)) {
            translatedMessage = Translator.getMessage(message.getText(), messageValues);
        } else if (message != null) {
            translatedMessage = Translator.getMessage(message.getText());
        } else {
            translatedMessage = RMessageLabel.EMPTY_LABEL;
        }
        messageLabel.setText(translatedMessage);
        messageLabel.setToolTipText(translatedMessage);

        validate();

        try {
            RepaintManager.currentManager(this).paintDirtyRegions();
        } catch (Throwable throwable) {
            // If this fails, do not repaint and do not crash system.
            LogService.debug(this, "ignoring Excepton");
        }
    }

    /*****************************************************************************************
     * Private inner class that displays the status window in a runnable object that can be
     * invoked by SwingUtilities.
     *****************************************************************************************/

    private class StatusWindowDisplayer implements Runnable {

        private UIException localException;

        public StatusWindowDisplayer(UIException exception) {
            localException = exception;
        }

        public void run() {
            Container container = getTopLevelAncestor();

            if (statusWindow == null) {
                if (container instanceof Frame) {
                    statusWindow = new RStatusWindow((Frame) container);
                } else if (container instanceof Dialog) {
                    statusWindow = new RStatusWindow((Dialog) container);
                } else {
                    statusWindow = new RStatusWindow();
                }
            }

            WindowPlacer.centerWindow(statusWindow);

            statusWindow.displayException(localException);
            statusWindow.setVisible(true);
        }
    }
}
