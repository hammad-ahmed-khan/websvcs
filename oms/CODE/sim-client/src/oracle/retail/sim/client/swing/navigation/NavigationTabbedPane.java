package oracle.retail.sim.client.swing.navigation;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RTabbedPane;
import oracle.retail.sim.common.config.NavigationData;
import oracle.retail.sim.common.config.NavigationTabData;
import oracle.retail.sim.common.config.NavigationTaskItemData;

/******************************************************************************************
 * The navigation panel is a panel for navigation through the application. It loads an
 * XML file that defines the navigation structure. If no navigation exists or an error
 * occurs during startup, the panel does not display itself.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class NavigationTabbedPane extends RTabbedPane implements PropertyChangeListener, ChangeListener {
    private static final long serialVersionUID = -382628360655343570L;

    private int originalWidth;

    /******************************************************************************************
     * Returns new NavigationPanel object.
     *****************************************************************************************/
    public NavigationTabbedPane() {
        setBackground(UIManager.getColor(UIThemeName.NAVIGATION_TAB_PANE_BACKGROUND));
        setSelectedTabBackground(UIManager.getColor(UIThemeName.NAVIGATION_TAB_PANE_BACKGROUND));
        setSelectedTabForeground(UIManager.getColor(UIThemeName.NAVIGATION_TAB_PANE_FOREGROUND));
        setTabAreaBackground(UIManager.getColor(UIThemeName.NAVIGATION_TAB_AREA_BACKGROUND));
        addChangeListener(this);
    }

    /******************************************************************************************
     * Initializes the navigation panel by creating a series of navigation tabs based on the
     * navigation data. This will refresh the navigation panel if it is already constructed.
     * <p>
     * @param navigationData The NavigationData object containing all the information.
     *****************************************************************************************/
    public void initializeNavigation(NavigationData navigationData) {
        removeAll();

        try {
            NavigationTabData[] tabArray = navigationData.getTabs();

            NavigationTab navigationTab;
            for (NavigationTabData tabData : tabArray) {
                navigationTab = new NavigationTab(tabData);
                navigationTab.setName(tabData.getDisplayName());
                navigationTab.addPropertyChangeListener(this);

                add(tabData.getDisplayName(), navigationTab);
            }
            originalWidth = getPreferredSize().width;
            setVisible(true);
        } catch (Throwable throwable) {
            UILog.fatal(getClass(), throwable);
            setVisible(false);
        }
    }

    /******************************************************************************************
     * Resize the tab pane whenever a new tab is chosen.
     *****************************************************************************************/
    public void stateChanged(ChangeEvent event) {
        resizeTabPane();
    }

    /******************************************************************************************
     * Property Change Listeners takkes appropriate action when property changes within the
     * navigation tab.
     *****************************************************************************************/
    public void propertyChange(PropertyChangeEvent event) {
        String propertyName = event.getPropertyName();

        if (propertyName.equals(UIPropertyName.MENU_PRESSED)) {
            resizeTabPane();
        } else if (propertyName.equals(UIPropertyName.MENU_ITEM_PRESSED)) {
            navigate((NavigationTaskItemData) event.getNewValue());
        }
    }

    /******************************************************************************************
     * Resizes the tabbed pane to the width of all the tabs, or to the currently selected component.
     *****************************************************************************************/
    private void resizeTabPane() {
        NavigationTab selectedTab = (NavigationTab) getSelectedComponent();

        int preferredWidth = selectedTab.getPreferredWidth();

        if (originalWidth > preferredWidth) {
            preferredWidth = originalWidth;
        }
        Dimension dimension = new Dimension(preferredWidth, getPreferredSize().height);

        setVisible(false);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setVisible(true);

        requestFocusInWindow();
    }

    /******************************************************************************************
     * Called when a new menu item is selected, it delegates the navigation to the
     * ApplicationFrame class.
     * <p>
     * @param data The NavigationMenuItemData of the menu item that was selected.
     *****************************************************************************************/
    protected void navigate(NavigationTaskItemData data) {
        ApplicationInternal.navigate(data);
    }
}
