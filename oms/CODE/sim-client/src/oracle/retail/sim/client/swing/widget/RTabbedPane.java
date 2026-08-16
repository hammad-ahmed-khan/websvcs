package oracle.retail.sim.client.swing.widget;

import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;

/******************************************************************************************
 * This class subclasses JTabbedPane to supply additional features, including allowing the
 * user to change some of the color scheme.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RTabbedPane extends JTabbedPane {
    private static final long serialVersionUID = -6408914316745627349L;

    private Color tabAreaBackground;
    private Color selectedTabBackground;
    private Color selectedTabForeground;

    /******************************************************************************************
     * Creates an RTabbedPane object.
     *****************************************************************************************/
    public RTabbedPane() {
        initializeColors();
        setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
        setOpaque(false);
    }

    /******************************************************************************************
     * Initializes the colors to the default colors in the UIManager for a tabbed pane.
     *****************************************************************************************/
    private void initializeColors() {
        selectedTabBackground = UIManager.getColor(UIThemeName.TABBEDPANE_BACKGROUND);
        selectedTabForeground = UIManager.getColor(UIThemeName.TABBEDPANE_FOREGROUND);
        tabAreaBackground = UIManager.getColor(UIThemeName.TABBEDPANE_TAB_AREA_BACKGROUND);
    }

    /******************************************************************************************
     * Assigns the color of the area behind and between the tabs. This method automatically
     * updates the background color of the non-selected tabs.
     * <p>
     * @param color The color to assign to the tab area background.
     *****************************************************************************************/
    public void setTabAreaBackground(Color color) {
        setTabAreaBackground(color, true);
    }

    /******************************************************************************************
     * Assigns the color of the area behind and between the tabs. This method automatically
     * updates the background color of the non-selected tabs based on the paramater.
     * <p>
     * @param color The color to assign to the tab area background.
     * @param updateTabs True if the tab backgrounds should be updated, false if not.
     *****************************************************************************************/
    public void setTabAreaBackground(Color color, boolean updateTabs) {
        if (color != null) {
            tabAreaBackground = color;

            if (updateTabs) {
                for (int i = 0; i < getTabCount(); i++) {
                    setBackgroundAt(i, tabAreaBackground);
                }
            }
        }
    }

    /******************************************************************************************
     * Retrieves the color of the area behind and between the tabs.
     * <p>
     * @return The color of the area behind and between the tabs.
     *****************************************************************************************/
    public Color getTabAreaBackground() {
        if (isOpaque()) {
            return tabAreaBackground;
        }
        return getRootPane().getBackground();
    }

    /******************************************************************************************
     * Assigns the selected tab foreground color.
     * <p>
     * @param color The color to assign to the tab's text when the tab is selected.
     ******************************************************************************************/
    public void setSelectedTabForeground(Color color) {
        if (color != null) {
            selectedTabForeground = color;
        }
    }

    /******************************************************************************************
     * Retrieves the selected tab foreground color.
     * <p>
     * @return The color of the tab's text when the tab is selected.
     ******************************************************************************************/
    public Color getSelectedTabForeground() {
        return selectedTabForeground;
    }

    /******************************************************************************************
     * Assigns the selected tab background color.
     * <p>
     * @param color The color to assign to the tab's background when the tab is selected.
     *****************************************************************************************/
    public void setSelectedTabBackground(Color color) {
        if (color != null) {
            selectedTabBackground = color;
        }
    }

    /******************************************************************************************
     * Retrieves the selected tab background color.
     * <p>
     * @return The color of the tab's background when the tab is selected.
     ******************************************************************************************/
    public Color getSelectedTabBackground() {
        return selectedTabBackground;
    }

    /******************************************************************************************
     * Overrides the insertTab() method in the superclass to assign the default background
     * color of the tab being inserted.
     * <p>
     * @param title the title to be displayed in this tab
     * @param icon the icon to be displayed in this tab
     * @param component The component to be displayed when this tab is clicked.
     * @param tip the tooltip to be displayed for this tab
     * @param index the position to insert this new tab
     ******************************************************************************************/
    public void insertTab(String title, Icon icon, Component component, String tip, int index) {
        super.insertTab(Translator.getText(title), icon, component, tip, index);
        if (index < getTabCount()) {
            setBackgroundAt(index, tabAreaBackground);
        }
    }

    /******************************************************************************************
     * Adds an RTab to the tabbed pane.
     * <p>
     * @param tab The RTab to be added.
     *****************************************************************************************/
    public void addTab(RTab tab) {
        if (StringUtility.isNullOrEmpty(tab.getTitle())) {
            throw new IllegalArgumentException("Cannot add an RTab without a title to the tabbed pane!");
        }
        addTab(tab.getTitle(), tab);
    }

    /******************************************************************************************
     * Adds a <code>component</code> represented by a <code>title</code> and no icon.
     * <p>
     * @param title the title to be displayed in this tab
     * @param component the component to be displayed when this tab is clicked
     *****************************************************************************************/
    public void addTab(String title, Component component) {
        super.addTab(Translator.getText(title), component);
    }

    /******************************************************************************************
     * Adds a <code>component</code> represented by a <code>title</code> and/or <code>icon</code>,
     * either of which can be <code>null</code>. If <code>icon</code> is non-<code>null</code>
     * and it implements <code>ImageIcon</code> a corresponding disabled icon will automatically
     * be created and set on the tabbedpane.
     * <p>
     * @param title the title to be displayed in this tab
     * @param icon the icon to be displayed in this tab
     * @param component the component to be displayed when this tab is clicked
     *****************************************************************************************/
    public void addTab(String title, Icon icon, Component component) {
        super.addTab(Translator.getText(title), icon, component);
    }

    /******************************************************************************************
     * Adds a <code>component</code> and <code>tip</code> represented by a <code>title</code>
     * and/or <code>icon</code>, either of which can be <code>null</code>.  If <code>icon</code>
     * is non-<code>null</code> and it implements <code>ImageIcon</code> a corresponding
     * disabled icon will automatically be created and set on the tabbedpane.
     * <p>
     * @param title the title to be displayed in this tab
     * @param icon the icon to be displayed in this tab
     * @param component the component to be displayed when this tab is clicked
     * @param tip the tooltip to be displayed for this tab
     *****************************************************************************************/
    public void addTab(String title, Icon icon, Component component, String tip) {
        super.addTab(Translator.getText(title), icon, component, tip);
    }

    /******************************************************************************************
     * Assigns the selected tab for the tabbed pane. This will trigger the state changed action.
     * <p>
     * @param tab The tab to select.
     *****************************************************************************************/
    public void setSelectedTab(RTab tab) {
        setSelectedComponent(tab);
    }

    /******************************************************************************************
     * Retrieves the selected tab for the tabbed pane.
     * <p>
     * @return The selected tab for the tab pane.
     *****************************************************************************************/
    public RTab getSelectedTab() {
        return (RTab) getSelectedComponent();
    }

    /******************************************************************************************
     * Assigns the tab enabled or disabled state.
     * <p>
     * @param title The title of the tab.
     * @param enabled True if the tab should be enabled, false otherwise.
     *****************************************************************************************/
    public void setEnabledAt(String title, boolean enabled) {
        int index = indexOfTab(Translator.getText(title));
        if (index > -1) {
            setEnabledAt(index, enabled);
        }
    }

    /******************************************************************************************
     * Retrieves the tab enabled or disabled state.
     * <p>
     * @param title The title of the tab.
     *****************************************************************************************/
    public boolean isEnabledAt(String title) {
        int index = indexOfTab(Translator.getText(title));
        if (index < 0) {
            return false;
        }
        return isEnabledAt(index);
    }
}
