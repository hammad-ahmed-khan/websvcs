package oracle.retail.sim.client.swing.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.UIManager;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;

/******************************************************************************************
 * This class is the title area of the task panel.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class RTaskTitlePanel extends RPanel {
    private static final long serialVersionUID = 7340785139132327460L;

    protected static final String MAX_MIN_COMMAND = "Maximize-Minimize";

    private RButton iconButton = new RButton();
    private RLabel titleLabel = new RLabel();
    private RLabel descriptionLabel = new RLabel();

    private Icon maxIcon;
    private Icon minIcon;

    /******************************************************************************************
     * Creatres new TaskTitlePanel();
     *****************************************************************************************/
    public RTaskTitlePanel() {
        initializeFontsAndColors();
        initializeIcon();
        layoutPanel();
    }

    /******************************************************************************************
     * Initializes the fonts and colors.
     *****************************************************************************************/
    private void initializeFontsAndColors() {
        setFont(UIManager.getFont(UIThemeName.TASKPANEL_TITLE_FONT));
        setTaskTitleFont(UIManager.getFont(UIThemeName.TASKPANEL_TITLE_FONT));
        setTaskTitleColor(UIManager.getColor(UIThemeName.TASKPANEL_FOREGROUND));
        setTaskDescriptionColor(UIManager.getColor(UIThemeName.TASKPANEL_FOREGROUND));
    }

    /******************************************************************************************
     * Initializes the icon.
     *****************************************************************************************/
    private void initializeIcon() {
        maxIcon = UIManager.getIcon(UIThemeName.TASKPANEL_TITLE_MAX_ICON);
        minIcon = UIManager.getIcon(UIThemeName.TASKPANEL_TITLE_MIN_ICON);

        iconButton.setIcon(maxIcon);
        iconButton.setBorder(null);
        iconButton.setActionCommand(MAX_MIN_COMMAND);
        iconButton.addActionListener(createMaximizeListener());
    }

    /*********************************************************************************************
     * Creates a maximize-minimize listener for the task panel.
     *********************************************************************************************/
    private ActionListener createMaximizeListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                ApplicationInternal.getApplicationFrame().doSwapTaskResizeState();
            }
        };
    }

    /******************************************************************************************
     * Lays out the components in the panel.
     *****************************************************************************************/
    private void layoutPanel() {
        setLayout(new GridBagLayout());
        add(iconButton, GridTool.constraints(0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0));
        add(titleLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0));
        add(descriptionLabel, GridTool.constraints(2, 0, 1, 1, 1, 0, 1, 1, 0, 2, 0, 0));
    }

    /******************************************************************************************
     * Adds an action listener to the title panel (namely to the maximize, minimize).
     *****************************************************************************************/
    public void addActionListener(ActionListener listener) {
        iconButton.addActionListener(listener);
    }

    /******************************************************************************************
     * Assigns the font to the task area and task description.
     * <p>
     * @param font The font to assign.
     *****************************************************************************************/
    public void setFont(Font font) {
        if (font != null) {
            super.setFont(font);
            if (descriptionLabel != null) {
                setTaskDescriptionFont(font);
            }
        }
    }

    /******************************************************************************************
     * Assigns the title to the task title area.
     * <p>
     * @param title The title to assign.
     *****************************************************************************************/
    public void setTaskTitle(String title) {
        titleLabel.setText(title);
    }

    /******************************************************************************************
     * Retrieves the task title.
     * <p>
     * @return The task title of the title panel.
     *****************************************************************************************/
    public String getTaskTitle() {
        return titleLabel.getText();
    }

    /******************************************************************************************
     * Assigns the font to the task title.
     * <p>
     * @param font The font to assign.
     *****************************************************************************************/
    public void setTaskTitleFont(Font font) {
        if (font != null) {
            titleLabel.setFont(font);
        }
    }

    /******************************************************************************************
     * Assigns the color of the title.
     * <p>
     * @param color The Color to assign.
     *****************************************************************************************/
    public void setTaskTitleColor(Color color) {
        if (color != null) {
            titleLabel.setForeground(color);
        }
    }

    /******************************************************************************************
     * Assigns a description to the title area (right of the title).
     * <p>
     * @param description The description to assign.
     *****************************************************************************************/
    public void setTaskDescription(String description) {
        descriptionLabel.setText(description);
    }

    /******************************************************************************************
     * Assigns the font of the task description.
     * <p>
     * @param font The font to assign.
     *****************************************************************************************/
    public void setTaskDescriptionFont(Font font) {
        if (font != null) {
            descriptionLabel.setFont(font);
        }
    }

    /******************************************************************************************
     * Assigns the color of the task description.
     * <p>
     * @param color The color to assign.
     *****************************************************************************************/
    public void setTaskDescriptionColor(Color color) {
        if (color != null) {
            descriptionLabel.setForeground(color);
        }
    }

    /******************************************************************************************
     * Sets the task resize state. It changes which icon is displayed.
     * <p>
     * @param isMaximized True if the task panel is maximized, false if it is not.
     *****************************************************************************************/
    public void setTaskResizeState(boolean isMaximized) {
        if (isMaximized) {
            iconButton.setIcon(minIcon);
        } else {
            iconButton.setIcon(maxIcon);
        }
    }
}
