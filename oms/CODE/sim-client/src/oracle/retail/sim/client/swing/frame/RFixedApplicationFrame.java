package oracle.retail.sim.client.swing.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import oracle.retail.sim.client.swing.navigation.NavigationTabbedPane;
import oracle.retail.sim.common.config.NavigationData;

/******************************************************************************************
 * This class subclasses the application frame to supply high usability standards at the
 * application level.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public abstract class RFixedApplicationFrame extends RPlatformApplicationFrame {

    private NavigationTabbedPane navigationTabbedPane = new NavigationTabbedPane();

    /******************************************************************************************
     * Creates a new DefaultApplicationFrame.
     *****************************************************************************************/
    protected RFixedApplicationFrame() {
        layoutFrame();
    }

    /******************************************************************************************
     * Initializes and lays out the components in the frame.
     *****************************************************************************************/
    protected void layoutFrame() {
        navigationPanel.setOpaque(false);
        navigationPanel.setEmptyBorder(2, 0, 0, 0);
        navigationPanel.setLayout(new BorderLayout());
        navigationPanel.add(navigationTabbedPane, BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(navigationPanel, BorderLayout.WEST);
        getContentPane().add(defaultTaskPanel, BorderLayout.CENTER);
    }

    /******************************************************************************************
     * Initializes the navigation tabbed pane with navigation data.
     *****************************************************************************************/
    protected void initializeNavigationTabbedPane(NavigationData secureNavigationData) {
        navigationTabbedPane.initializeNavigation(secureNavigationData);
    }

    /******************************************************************************************
     * Retrieves first navigation component for focus purposes
     *****************************************************************************************/
    protected Component getFirstNavigationComponent() {
        return navigationTabbedPane;
    }
}
