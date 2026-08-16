package oracle.retail.sim.client.swing.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.panel.RContentPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RScrollPane;
import oracle.retail.sim.common.config.NavigationTaskItemData;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * RBaseTaskPanel is a subclass of the task panel with implementation for the base bath usage. Tasks are
 * controlled by the application frame and contain a set of content panels.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class RBaseTaskPanel extends RTaskPanel implements PropertyChangeListener {

    private RTaskTitlePanel titlePanel = new RTaskTitlePanel();
    private RPanel borderPanel = new RPanel();
    private RPanel centerPanel = new RPanel();
    private RLabel fillerLabel = new RLabel();
    private RTaskButtonPanel buttonPanel = new RTaskButtonPanel();
    private RScrollPane centerPane = new RScrollPane(centerPanel);

    private NavigationTaskItemData taskItemData;

    private RContentPanel[] panelArray = new RContentPanel[5];
    private int panelIndex;

    /****************************************************************************************************
     * Creates a new RBaseTaskPanel object.
     ***************************************************************************************************/
    protected RBaseTaskPanel() {
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

        Border outerBorder = BorderFactory.createLoweredBevelBorder();
        Border innerBorder = new EmptyBorder(0, 5, 5, 5);

        centerPane.setBorder(null);
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBorder(new CompoundBorder(outerBorder, innerBorder));

        fillerLabel.setBackground(getBackground());
    }

    /****************************************************************************************************
     * Lays out the task panel.
     ***************************************************************************************************/
    private void layoutPanel() {
        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(borderPanel, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.SOUTH);
        add(centerPane, BorderLayout.CENTER);
    }

    /****************************************************************************************************
     * Assigns the navigation task item data to the task panel.
     * <p>
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

        if (centerPane != null) {
            centerPane.setExtendedBackground(color, false);
        }
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
     * Adds a content panel to the content area.
     * <p>
     * @param panel The RContentPanel to add.
     ***************************************************************************************************/
    public void addContentPanel(RContentPanel panel) {
        panel.addPropertyChangeListener(this);
        panelArray[panelIndex++] = panel;
        validatePanelIndex();
        layoutContentPanels();
    }

    /****************************************************************************************************
     * Removes a content panel from the content area.
     * <p>
     * @param panel The RContentPanel to remove.
     ***************************************************************************************************/
    public void removeContentPanel(RContentPanel panel) {
        centerPanel.setVisible(false);
        centerPanel.remove(panel);
        centerPanel.setVisible(true);

        panel.removePropertyChangeListener(this);

        for (int i = 0; i < panelArray.length; i++) {
            if (panelArray[i] == panel) {
                panelArray[i] = null;
            }
        }
    }

    /****************************************************************************************************
     * Clears all content panels from the content area of the task.
     ***************************************************************************************************/
    public void clearContentPanels() {
        centerPanel.removeAll();
        for (RContentPanel element : panelArray) {
            if (element != null) {
                element.removePropertyChangeListener(this);
            }
        }
        panelArray = new RContentPanel[2];
        panelIndex = 0;
    }

    /****************************************************************************************************
     * Validates that the number of panels have not exceeded the space in the array. It increases the
     * array size if necessary and collapses the array leaving no null values.
     ***************************************************************************************************/
    private void validatePanelIndex() {
        if (panelIndex == panelArray.length) {
            RContentPanel[] tempArray = new RContentPanel[panelIndex + 10];
            System.arraycopy(panelArray, 0, tempArray, 0, panelArray.length);
            panelArray = tempArray;
        }
        int index = 0;
        RContentPanel[] smallArray = new RContentPanel[panelArray.length];
        for (RContentPanel element : panelArray) {
            if (element != null) {
                smallArray[index++] = element;
            }
        }
        panelArray = smallArray;
    }

    /****************************************************************************************************
     * Implements the property change listener method. If a content panel has its size or expandibility
     * modified, the task panel should re-layout its content panels.
     ***************************************************************************************************/
    public void propertyChange(PropertyChangeEvent event) {
        String propertyName = event.getPropertyName();

        if (propertyName.equals(UIPropertyName.CONTENTPANEL_SIZE_CHANGED)) {
            layoutContentPanels();
        } else if (propertyName.equals(UIPropertyName.CONTENTPANEL_EXPANDABLE)) {
            layoutContentPanels();
        }
    }

    /****************************************************************************************************
     * Lays out all of the content panels according to their state.
     ***************************************************************************************************/
    private void layoutContentPanels() {
        centerPanel.removeAll();

        removeAssignedSizes();

        boolean needsFiller = true;
        for (int i = 0; i < panelArray.length; i++) {
            if (panelArray[i] != null) {
                if (panelArray[i].isStretchable() && panelArray[i].isExpanded()) {
                    centerPanel.add(panelArray[i], GridTool.constraints(0, i, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));
                    needsFiller = false;
                } else {
                    centerPanel.add(panelArray[i], GridTool.constraints(0, i, 1, 1, 1, 0, 0, 3, 5, 0, 0, 0));
                }
            }
        }

        calculateSizes(centerPanel);

        if (needsFiller) {
            centerPanel.add(fillerLabel, GridTool.constraints(0, panelArray.length, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));
        }

        centerPanel.revalidate();
        centerPanel.repaint();
    }

    /****************************************************************************************************
     * Removes all the minimum, maximum, and preferred sizes from content panels. That task panel
     * controls the sizing of content panels. Note that if the end user set the content size property on
     * the panel, this will act much like preferred size.
     ***************************************************************************************************/
    private void removeAssignedSizes() {
        for (RContentPanel element : panelArray) {
            if (element != null) {
                element.setMinimumSize(null);
                element.setPreferredSize(null);
                element.setMaximumSize(null);
            }
        }
    }

    /****************************************************************************************************
     * Assign preferred sizes to the components inside the center panel of the task.
     ***************************************************************************************************/
    private void calculateSizes(RPanel centerPanel) {
        int fullheight = ApplicationInternal.getFrame().getSize().height - 134; // 134 Is Size of
        // Extras...
        int fixedheight = 0;

        Component[] array = centerPanel.getComponents();
        List stretchableList = new ArrayList<>();
        RContentPanel contentPanel;
        Dimension contentDimension;

        for (Component element : array) {
            if (element instanceof RContentPanel) {
                contentPanel = (RContentPanel) element;
                if (contentPanel.isStretchable()) {
                    stretchableList.add(contentPanel);
                    continue;
                }
                contentDimension = contentPanel.getPreferredPanelSize();
                if (contentDimension == null) {
                    fixedheight = fixedheight + contentPanel.getPreferredSize().height;
                    continue;
                }
                contentPanel.setMinimumSize(contentDimension);
                contentPanel.setPreferredSize(contentDimension);
                fixedheight = contentDimension.height;
            }
        }
        if (fixedheight > fullheight || stretchableList.isEmpty()) {
            return;
        }
        int tempheight = (fullheight - fixedheight) / stretchableList.size();
        for (Iterator iterator = stretchableList.iterator(); iterator.hasNext();) {
            contentPanel = (RContentPanel) iterator.next();
            if (contentPanel.isExpanded()) {
                contentPanel.setMinimumSize(new Dimension(0, tempheight));
                contentPanel.setPreferredSize(new Dimension(0, tempheight));
            }
        }
    }

    /****************************************************************************************************
     * If the task is stoppable, this removes the task from the application frame.
     ***************************************************************************************************/
    public void closeTask() {
        ApplicationInternal.getApplicationFrame().clearTaskPanel();
    }

    /****************************************************************************************************
     * This removes the task from the application frame without any validation.
     ***************************************************************************************************/
    public void killTask() {
        ApplicationInternal.getApplicationFrame().killTaskPanel();
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
     * Protected method called by the application frame to determine if a task is startable or not. It
     * calls isStartable() on the subclass when it is done doing its own validation of permissions.
     * <p>
     * @return True if the task is startable, false otherwise.
     ***************************************************************************************************/
    protected boolean validateTask() throws UIException {
        for (RContentPanel element : panelArray) {
            if (element != null) {
                element.validateRequiredEditors();
                element.validatePermissions();
            }
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
