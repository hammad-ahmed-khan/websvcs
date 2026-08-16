package oracle.retail.sim.client.swing.navigation;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.UIManager;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.widget.RScrollPane;
import oracle.retail.sim.client.swing.widget.RTabbedPane;
import oracle.retail.sim.common.config.NavigationData;
import oracle.retail.sim.common.config.NavigationTabData;

/******************************************************************************************
 * The navigation tabbed pane contains all the tabs in the navigation area of the
 * application. This version is for the old poor-usability platform method of doing things.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class PlatformNavigationTabbedPane extends RTabbedPane {
    private static final long serialVersionUID = -148351586674611613L;

    /******************************************************************************************
     * Returns new PlatformNavigationTabbedPane object.
     *****************************************************************************************/
    public PlatformNavigationTabbedPane() {
        setBackground(UIManager.getColor(UIThemeName.NAVIGATION_TAB_PANE_BACKGROUND));
        setSelectedTabBackground(UIManager.getColor(UIThemeName.NAVIGATION_TAB_PANE_BACKGROUND));
        setSelectedTabForeground(UIManager.getColor(UIThemeName.NAVIGATION_TAB_PANE_FOREGROUND));
        setTabAreaBackground(UIManager.getColor(UIThemeName.NAVIGATION_TAB_AREA_BACKGROUND));
        setMinimumSize(new Dimension(0, 0));
        addComponentListener(getTabSizeListener());
    }

    /*********************************************************************************************
     * Creates a component resized listener for this object. When the navigation tab is resized,
     * it will attempt to reset the split pane default heights so that the slider bars work
     * smoothly.
     *********************************************************************************************/
    private ComponentListener getTabSizeListener() {
        return new ComponentAdapter() {
            public void componentResized(ComponentEvent event) {
                RScrollPane scrollPane = (RScrollPane) getSelectedComponent();
                PlatformNavigationTab tab = (PlatformNavigationTab) scrollPane.getViewport().getView();
                tab.resetSplitPaneHeights();
            }
        };
    }

    /******************************************************************************************
     * Initializes the navigation panel by creating a series of navigation tabs based on the
     * navigation data. Each navigation tab is placed inside a scrollpane (with no scrolling
     * available) for splitpane layout reasons.
     * <p>
     * @param navigationData The NavigationData object containing all the information.
     *****************************************************************************************/
    public void initializeNavigation(NavigationData navigationData) {
        removeAll();
        try {
            NavigationTabData[] tabArray = navigationData.getTabs();
            for (NavigationTabData element : tabArray) {
                PlatformNavigationTab navigationTab = new PlatformNavigationTab(element);
                navigationTab.setName(element.getDisplayName());

                RScrollPane scrollPane = new RScrollPane(navigationTab);
                scrollPane.turnHorizontalScrollBarOff();
                scrollPane.turnVerticalScrollBarOff();
                scrollPane.setBorder(null);
                scrollPane.setOpaque(false);
                scrollPane.setExtendedBackground(getBackground(), true);

                add(element.getDisplayName(), scrollPane);
            }
            setVisible(true);
        } catch (Throwable throwable) {
            UILog.fatal(getClass(), throwable);
            setVisible(false);
        }
    }
}
