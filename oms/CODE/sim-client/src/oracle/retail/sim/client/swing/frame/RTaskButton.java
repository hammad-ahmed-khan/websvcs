package oracle.retail.sim.client.swing.frame;

import javax.swing.UIManager;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.widget.RButton;

/******************************************************************************************
 * This class is a used as a suspended task button on the status bar.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

class RTaskButton extends RButton {
    private static final long serialVersionUID = 2727133662483099721L;

    private RTaskPanel taskPanel;

    /******************************************************************************************
     * Creates a new RTaskButton with a specific title.
     * <p>
     * @param title The title to create the button with.
     *****************************************************************************************/
    public RTaskButton(String title) {
        setBackground(UIManager.getColor(UIThemeName.CHROME_PANEL_BACKGROUND));
        setText(title);
    }

    /******************************************************************************************
     * Creates a new RTaskButton around a RTaskPanel.
     * <p>
     * @param panel The panel to create the RTaskButton with.
     *****************************************************************************************/
    public RTaskButton(RTaskPanel panel) {
        setBackground(UIManager.getColor(UIThemeName.CHROME_PANEL_BACKGROUND));
        setText(panel.toDisplayString());
        taskPanel = panel;
    }

    /******************************************************************************************
     * Retrieves the RTaskPanel associated with this button.
     * <p>
     * @return The RTaskPanel.
     *****************************************************************************************/
    public RTaskPanel getTask() {
        return taskPanel;
    }
}
