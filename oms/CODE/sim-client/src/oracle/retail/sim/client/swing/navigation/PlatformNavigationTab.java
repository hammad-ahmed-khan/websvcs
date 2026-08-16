package oracle.retail.sim.client.swing.navigation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.UIManager;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.panel.RSplitPane;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.common.config.NavigationTabData;
import oracle.retail.sim.common.config.NavigationTaskData;
import oracle.retail.sim.common.config.NavigationTaskItemData;
import oracle.retail.sim.common.core.locale.StringConstants;

/*********************************************************************************************
 * This class is a tab placed in the navigation tabbed pane for the poor usability standars
 * of the old platform application frame. It controls displaying the list of tasks and list
 * of task items.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *********************************************************************************************/

public class PlatformNavigationTab extends RPanel implements PropertyChangeListener {
    private static final long serialVersionUID = -6979790456738829710L;

    private PlatformNavigationMenuPanel menuPanel = new PlatformNavigationMenuPanel();
    private PlatformNavigationMenuPanel itemPanel = new PlatformNavigationMenuPanel();

    private RSplitPane menuSplitPane = new RSplitPane(RSplitPane.VERTICAL_SPLIT);
    private RSplitPane itemSplitPane = new RSplitPane(RSplitPane.VERTICAL_SPLIT);

    private RPanel taskItemPanel = new RPanel();
    private JLabel taskItemLabel = new JLabel(StringConstants.SPACE);
    private JLabel fillerLabel = new RLabel();

    //These should not be hard-coded, but they represent a value that is difficult to figure out.
    private static final int TASK_OFFSET = 16;
    private static final int TASK_ITEM_OFFSET = 20;
    private static final int DIVIDER_OFFSET = 140;

    /*********************************************************************************************
     * Creates a new PlatformNavigationTab populated with menus constructed from the navigation
     * tab data.
     * <p>
     * @param navigationTabData The navigation tab data.
     *********************************************************************************************/
    public PlatformNavigationTab(NavigationTabData navigationTabData) {
        setBackground(UIManager.getColor(UIThemeName.NAVIGATION_MENU_BACKGROUND));
        setMinimumSize(new Dimension(0, 0));
        setEmptyBorder(5);
        initializeComponents(navigationTabData);
        layoutNavigationTab();
    }

    /**********************************************************************************************
     * Initializes the components of the navigation tab.
     **********************************************************************************************/
    private void initializeComponents(NavigationTabData navigationTabData) {
        menuPanel.displayTasks(navigationTabData.getTasks());
        menuPanel.setTitle(navigationTabData.getDisplayName());
        menuPanel.addPropertyChangeListener(this);
        itemPanel.addPropertyChangeListener(this);

        taskItemPanel.setOpaque(false);
        taskItemLabel.setOpaque(false);
        fillerLabel.setOpaque(false);
    }

    /**********************************************************************************************
     * Lays out components in the split panes and navigation tab.
     **********************************************************************************************/
    private void layoutNavigationTab() {
        itemSplitPane.setBorder(null);
        itemSplitPane.setTopComponent(itemPanel);
        itemSplitPane.setBottomComponent(fillerLabel);
        itemSplitPane.setContinuousLayout(true);
        itemSplitPane.setVisible(false);

        taskItemPanel.setLayout(new BorderLayout());
        taskItemPanel.add(taskItemLabel, BorderLayout.NORTH);
        taskItemPanel.add(itemSplitPane, BorderLayout.CENTER);

        menuSplitPane.setBorder(null);
        menuSplitPane.setTopComponent(menuPanel);
        menuSplitPane.setBottomComponent(taskItemPanel);
        menuSplitPane.setContinuousLayout(true);

        setLayout(new GridBagLayout());
        add(menuSplitPane, GridTool.constraints(0, 0, 1, 1, 1, 1, 3, 3, 0, 0, 0, 0));
    }

    /*********************************************************************************************
     * Implements the property change listener method to perform the correct method when a property
     * changes on a menu panel.
     *********************************************************************************************/
    public void propertyChange(PropertyChangeEvent event) {
        String propertyName = event.getPropertyName();
        if (propertyName.equals(UIPropertyName.MENU_PRESSED)) {
            doMenuPressed((NavigationTaskData) event.getNewValue());
        } else if (propertyName.equals(UIPropertyName.MENU_ITEM_PRESSED)) {
            doMenuItemPressed((NavigationTaskItemData) event.getNewValue());
        }
    }

    /*********************************************************************************************
     * Display the appropriate item panel for the menu.
     *********************************************************************************************/
    private void doMenuPressed(NavigationTaskData taskData) {
        itemPanel.setTitle(taskData.getDisplayName());
        itemPanel.displayTaskItems(taskData.getTaskItems());
        itemSplitPane.setVisible(true);
        resetSplitPaneHeights();
    }

    /*********************************************************************************************
     * Resets the default split pane data whenever the application frame is resized
     *********************************************************************************************/
    protected void resetSplitPaneHeights() {
        int tabHeight = getSize().height;
        int taskHeight = menuPanel.getPreferredSize().height;
        taskItemPanel.setMinimumSize(new Dimension(0, tabHeight - taskHeight - TASK_OFFSET));

        int taskItemHeight = taskItemPanel.getMinimumSize().height;
        int taskItemPanelHeight = itemPanel.getPreferredSize().height;
        fillerLabel.setMinimumSize(new Dimension(0, taskItemHeight - taskItemPanelHeight - TASK_ITEM_OFFSET));

        int baseValue = ApplicationInternal.getFrame().getHeight() - menuSplitPane.getDividerLocation() - DIVIDER_OFFSET;
        if (baseValue - itemSplitPane.getDividerLocation() < 0) {
            itemSplitPane.setDividerLocation(baseValue);
        }
        menuSplitPane.revalidate();
        menuSplitPane.repaint();
    }

    /*********************************************************************************************
     * Informs the application frame that it should attempt to navigation to the selected task item.
     *********************************************************************************************/
    private void doMenuItemPressed(NavigationTaskItemData data) {
        ApplicationInternal.navigate(data);
    }
}
