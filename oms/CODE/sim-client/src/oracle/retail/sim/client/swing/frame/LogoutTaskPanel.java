package oracle.retail.sim.client.swing.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * The class is a deafault task panel that displays when no other task is selected.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class LogoutTaskPanel extends RBaseTaskPanel {
    private static final long serialVersionUID = -8799358436437721521L;

    private RButton logoutButton = new RButton("Logout");

    /****************************************************************************************************
     * Returns a new DefaultTaskPanel.
     ***************************************************************************************************/
    public LogoutTaskPanel() {
        logoutButton.setBackground(UIManager.getColor(UIThemeName.TASKPANEL_BORDER_BACKGROUND));
        logoutButton.setForeground(UIManager.getColor(UIThemeName.TASKPANEL_FOREGROUND));
        logoutButton.addActionListener(getExitActionListener());

        addButton(logoutButton);
    }

    /****************************************************************************************************
     * Retrieves exit action listener.
     ***************************************************************************************************/
    public ActionListener getExitActionListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (ApplicationInternal.getApplicationFrame().exitApplication()) {
                    ApplicationExit.exit(ApplicationInternal.getFrame());
                }
            }
        };
    }

    /****************************************************************************************************
     * Returns the task title
     ***************************************************************************************************/
    public String getTaskTitle() {
        return Translator.getText("No Task Selected");
    }

    /****************************************************************************************************
     * Returns an empty task description.
     ***************************************************************************************************/
    public String getTaskDescription() {
        return StringConstants.EMPTY;
    }

    /****************************************************************************************************
     * Resets focus to the logout button.
     ***************************************************************************************************/
    public boolean requestFocusInWindow() {
        return super.requestFocusInWindow() && logoutButton.requestFocusInWindow();
    }

    /****************************************************************************************************
     * Called when intialized.
     ***************************************************************************************************/
    public void init() {
    }

    /****************************************************************************************************
     * Does Nothing
     ***************************************************************************************************/
    public void start() {
    }

    /****************************************************************************************************
     * Does Nothing
     ***************************************************************************************************/
    public void stop() {
    }

    /****************************************************************************************************
     * Does Nothing
     ***************************************************************************************************/
    public boolean isStartable() {
        return true;
    }

    /****************************************************************************************************
     * Does Nothing
     ***************************************************************************************************/
    public boolean isStoppable() {
        return true;
    }
}
