package oracle.retail.sim.client.swing.frame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import oracle.retail.sim.client.swing.panel.RButtonPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RLabel;

/******************************************************************************************
 * The class contains all the task buttons of the task panel.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class RTaskButtonPanel extends RPanel {
    private static final long serialVersionUID = -2432771387992881231L;

    private RLabel fillerLabel = new RLabel();
    private RLabel brandLabel = new RLabel();
    private RButtonPanel buttonPanel = new RButtonPanel();

    /******************************************************************************************
     * Returns new RButtonPanel object.
     *****************************************************************************************/
    public RTaskButtonPanel() {
        setDoubleBuffered(true);
        setLineBorder(2);
        setRightLayout();
        initializeIcon();
        initializePanel();
        layoutPanel();
    }

    /******************************************************************************************
     * Initializes the oracle logo that goes to the left of the buttons.
     *****************************************************************************************/
    private void initializeIcon() {
        Icon icon = UIManager.getIcon(UIThemeName.TASKPANEL_RETAIL_ICON);
        brandLabel.setIcon(icon);
        brandLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
    }

    /******************************************************************************************
     * Initializes the panel.
     *****************************************************************************************/
    private void initializePanel() {
        buttonPanel.setBorder(null);
    }

    /******************************************************************************************
     * Lays out the components in the panel.
     *****************************************************************************************/
    private void layoutPanel() {
        setLayout(new GridBagLayout());
        add(fillerLabel, GridTool.constraints(0, 0, 1, 1, 0, 1, 1, 2, 0, 0, 0, 0));
        add(brandLabel, GridTool.constraints(0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0));
        add(buttonPanel, GridTool.constraints(1, 0, 1, 2, 1, 0, 1, 1, 0, 0, 0, 0));
    }

    /******************************************************************************************
     * Sets the layout of buttons to centered.
     *****************************************************************************************/
    public void setCenterLayout() {
        buttonPanel.setCenterLayout();
    }

    /******************************************************************************************
     * Sets the layout of buttons to right justified.
     *****************************************************************************************/
    public void setRightLayout() {
        buttonPanel.setRightLayout();
    }

    /******************************************************************************************
     * Sets the layout of buttons to left justified.
     *****************************************************************************************/
    public void setLeftLayout() {
        buttonPanel.setLeftLayout();
    }

    /******************************************************************************************
     * Assigns the background color of the task panel button area.
     * <p>
     * @param color The color to assign.
     *****************************************************************************************/
    public void setBackground(Color color) {
        super.setBackground(color);

        if (buttonPanel != null) {
            buttonPanel.setBackground(color);
        }
    }

    /******************************************************************************************
     * Adds a new component to the task button area. This component should always be a button.
     *****************************************************************************************/
    public Component add(Component component) {
        return buttonPanel.add(component);
    }

    /******************************************************************************************
     * Adds a new button to the task button area.
     * <p>
     * @param button The button to add.
     *****************************************************************************************/
    public void addButton(JButton button) {
        buttonPanel.addButton(button);
    }

    /******************************************************************************************
     * Returns all the buttons within the panel.
     * <p>
     * @return All the buttons within the panel.
     *****************************************************************************************/
    public JButton[] getButtons() {
        List buttonList = new ArrayList<>();
        Component[] components = getComponents();
        for (Component component : components) {
            findButtons(buttonList, component);
        }
        if (buttonList.isEmpty()) {
            return new JButton[0];
        }
        JButton[] buttonArray = new JButton[buttonList.size()];
        for (int i = 0; i < buttonArray.length; i++) {
            buttonArray[i] = (JButton) buttonList.get(i);
        }
        return buttonArray;
    }

    /******************************************************************************************
     * Keep finding buttons
     *****************************************************************************************/
    private void findButtons(List buttonList, Component component) {
        if (component instanceof JButton) {
            buttonList.add(component);
        } else if (component instanceof Container) {
            Component[] components = ((Container) component).getComponents();
            for (Component componentx : components) {
                findButtons(buttonList, componentx);
            }
        }
    }
}
