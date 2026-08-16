package oracle.retail.sim.client.swing.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import oracle.retail.sim.client.swing.navigation.PlatformNavigationTabbedPane;
import oracle.retail.sim.client.swing.panel.RSplitPane;
import oracle.retail.sim.common.config.NavigationData;

/******************************************************************************************
 * This class subclasses the application frame to supply the poor usability standards of
 * platform.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public abstract class RSliderApplicationFrame extends RPlatformApplicationFrame {

    private RSplitPane applicationSplitPane = new RSplitPane(RSplitPane.HORIZONTAL_SPLIT);
    private PlatformNavigationTabbedPane navigationTabbedPane = new PlatformNavigationTabbedPane();
    private int lastDividerLocation;

    /******************************************************************************************
     * Creates a new PlatformApplicationFrame.
     *****************************************************************************************/
    protected RSliderApplicationFrame() {
        navigationPanel.setOpaque(false);
        navigationPanel.setEmptyBorder(2, 0, 0, 0);
        navigationPanel.setLayout(new BorderLayout());
        navigationPanel.add(navigationTabbedPane, BorderLayout.CENTER);
        navigationPanel.setMinimumSize(new Dimension(0, 0));

        applicationSplitPane.setLeftComponent(navigationPanel);
        applicationSplitPane.setRightComponent(defaultTaskPanel);
        applicationSplitPane.setContinuousLayout(true);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(applicationSplitPane, BorderLayout.CENTER);
    }

    /******************************************************************************************
     * Initializes the navigation tabbed pane with navigation data. Security should have
     * already been applied to the navigation data prior to making it to this location.
     * <p>
     * @param secureNavigationData The navigation data.
     *****************************************************************************************/
    protected void initializeNavigationTabbedPane(NavigationData secureNavigationData) {
        navigationTabbedPane.initializeNavigation(secureNavigationData);
    }

    /******************************************************************************************
     * Overrides the superclass method to swaps the task panel maximize state. Additional
     * functionality includes tracking the divider location.
     *****************************************************************************************/
    public void doSwapTaskResizeState() {
        isTaskMaximized = !isTaskMaximized;

        currentPanel.setTaskResizeState(isTaskMaximized);

        if (isTaskMaximized && isNavigationVisible) {
            lastDividerLocation = applicationSplitPane.getDividerLocation();
            navigationPanel.setVisible(false);
        } else if (isNavigationVisible) {
            applicationSplitPane.setDividerLocation(lastDividerLocation);
            navigationPanel.setVisible(true);
        }
        invalidate();
        validate();
    }

    /******************************************************************************************
     * Displays the appropriate task panel within the workspace. Overrides the superclass
     * method to assign the new task panel as the right/bottom component of the split pane.
     *****************************************************************************************/
    protected void displayTaskPanel(RTaskPanel panel) {
        currentPanel = panel;

        int location = applicationSplitPane.getDividerLocation();
        applicationSplitPane.setBottomComponent(currentPanel);
        applicationSplitPane.setDividerLocation(location);

        if (panel == defaultTaskPanel) {
            defaultTaskPanel.requestFocusInWindow();
        }
    }

    /******************************************************************************************
     * Retrieves first navigation component for focus purposes
     *****************************************************************************************/
    protected Component getFirstNavigationComponent() {
        return navigationTabbedPane;
    }
}
