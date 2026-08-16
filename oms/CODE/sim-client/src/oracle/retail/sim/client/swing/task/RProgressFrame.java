package oracle.retail.sim.client.swing.task;

import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.frame.RProgressIndicator;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.WindowPlacer;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RTextArea;

/********************************************************************************************************
 * This frame contains a progress indicator and executes a progress task asynchronously from the rest of
 * the application.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RProgressFrame extends JFrame implements WindowListener {
    private static final long serialVersionUID = -7066219750182049637L;

    private static final String PROCESSING = "Processing";

    private RTextArea textArea = new RTextArea();
    private RLabel textLabel = new RLabel();
    private RProgressIndicator progressBar = new RProgressIndicator();

    private UIProgressTask task;

    /****************************************************************************************************
     * Constructor.
     ***************************************************************************************************/
    public RProgressFrame() {
        setTitle(Translator.getText("Progress"));
        setSize(400, 160);
        buildWidgets();
        buildLayout();
        WindowPlacer.centerWindow(this);
    }

    /****************************************************************************************************
     * Build the component settings within the frame.
     ***************************************************************************************************/
    private void buildWidgets() {
        textArea.setInactiveForeground(UIManager.getColor(UIThemeName.TEXTAREA_FOREGROUND));
        textArea.setFont(UIManager.getFont(UIThemeName.RERRORDIALOG_FONT));
        textLabel.setFont(UIManager.getFont(UIThemeName.RERRORDIALOG_FONT));
    }

    /****************************************************************************************************
     * Layout the components within the frame.
     ***************************************************************************************************/
    private void buildLayout() {
        RPanel panel = new RPanel(new GridBagLayout());
        panel.setEmptyBorder(5);
        panel.add(textArea, GridTool.constraints(0, 0, 2, 1, 1, 1, 0, 3, 0, 0, 10, 0));
        panel.add(progressBar, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 10, 0));
        panel.add(textLabel, GridTool.constraints(1, 1, 1, 1, 1, 0, 0, 1, 0, 5, 10, 0));

        getContentPane().add(panel);
    }

    /****************************************************************************************************
     * Starts the progress task. This will update the windwo title, display the permanent message,
     * display the current progress message, activate the progress indicator and execute the task before
     * displaying the frame on the monitor.
     ***************************************************************************************************/
    public void start(UIProgressTask task) {
        this.task = task;
        updateTitle();
        updatePermanentMessage();
        updateProgressMessage();
        activateProgressBar();
        executeTask();
        setVisible(true);
    }

    /****************************************************************************************************
     * Empty implementation for the event listener.
     ***************************************************************************************************/
    public void windowOpened(WindowEvent event) {
    }

    public void windowClosed(WindowEvent event) {
    }

    public void windowIconified(WindowEvent event) {
    }

    public void windowDeiconified(WindowEvent event) {
    }

    public void windowActivated(WindowEvent event) {
    }

    public void windowDeactivated(WindowEvent event) {
    }

    public void windowClosing(WindowEvent event) {
        task.deactivate();
        progressBar.deactivate();
    }

    /****************************************************************************************************
     * Updates the frame title.
     ***************************************************************************************************/
    private void updateTitle() {
        setTitle(Translator.getText(task.getTitle()));
    }

    /****************************************************************************************************
     * Update the permanent message area.
     ***************************************************************************************************/
    private void updatePermanentMessage() {
        textArea.setText(Translator.getMessage(task.getPermanentMessage().getText()));
    }

    /****************************************************************************************************
     * Update the progress message. "Processing...." by default.
     ***************************************************************************************************/
    private void updateProgressMessage() {
        if (task.getProgressMessage() != null) {
            textLabel.setText(Translator.getMessage(task.getProgressMessage()));
        } else {
            textLabel.setText(Translator.getText(PROCESSING) + "...");
        }
    }

    /****************************************************************************************************
     * Activates the progress bar.
     ***************************************************************************************************/
    private void activateProgressBar() {
        progressBar.activate();
    }

    /****************************************************************************************************
     * Executes the task assigned to the frame on a separator thread.
     ***************************************************************************************************/
    private void executeTask() {
        new TaskExecutor().start();
    }

    /****************************************************************************************************
     * Inner class is a thread that attempts to execute the task request. If successful, it then executes
     * a task response. Finally, it updages the progress message, deactivates the progress bar and pops
     * the window to the front of the application.
     ***************************************************************************************************/
    private class TaskExecutor extends Thread {

        public void run() {
            if (task.executeRequest()) {
                task.executeResponse();
            }
            updateProgressMessage();
            progressBar.deactivate();
            task.deactivate();
            setVisible(false);
            dispose();
        }
    }
}
