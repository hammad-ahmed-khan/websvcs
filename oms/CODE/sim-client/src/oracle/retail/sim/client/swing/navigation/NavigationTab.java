package oracle.retail.sim.client.swing.navigation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RScrollPane;
import oracle.retail.sim.common.config.NavigationTabData;
import oracle.retail.sim.common.config.NavigationTaskData;
import oracle.retail.sim.common.config.NavigationTaskItemData;

/*********************************************************************************************
 * This class is a tab placed in the navigation tabbed pane. It contains a list of menus
 * available for selection and opens like a tree structure where multiple menus can be
 * open at the same time.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *********************************************************************************************/

public class NavigationTab extends JPanel {
    private static final long serialVersionUID = 1409980020336363585L;

    private RPanel navigationPanel = new RPanel();
    private RScrollPane navigationPane = new RScrollPane(navigationPanel);
    private JLabel fillerLabel = new RLabel();

    private ImageIcon menuOpenIcon;
    private ImageIcon menuClosedIcon;
    private ImageIcon menuItemIcon;

    private Font menuFont;
    private Color menuNormalColor;
    private Color menuFocusColor;

    private Font menuItemFont;
    private Color menuItemBackgroundColor;
    private Color menuItemForegroundColor;
    private Color menuItemRolloverBackColor;
    private Color menuItemRolloverForeColor;
    private Color menuItemActiveBackColor;
    private Color menuItemActiveForeColor;

    private Map menuDataMap = new HashMap<>();
    private Map menuItemDataMap = new HashMap<>();
    private Map menuItemPanelMap = new HashMap<>();

    private JLabel selectedLabel;
    private JLabel firstMenuLabel;
    //	private Border menuItemBorder = null;

    private int baseWidthPad = 22;
    private int rowCounter;

    /*********************************************************************************************
     * Creates a new DefaultNavigationTab populated with the data in the menu array.
     * <p>
     * @param navigationTabData The navigation tab data that defines the tab.
     *********************************************************************************************/
    public NavigationTab(NavigationTabData navigationTabData) {
        initializeLookAndFeelDefaults();
        initializeNavigationComponents();

        NavigationTaskData[] menuArray = navigationTabData.getTasks();
        for (NavigationTaskData menu : menuArray) {
            addMenu(menu);
        }

        addFocusListener(createPanelFocusListener());

        setLayout(new BorderLayout());
        add(navigationPane, BorderLayout.CENTER);
    }

    /*********************************************************************************************
     * Initializes the look and feel fonts, colors and icons of the menus and menu items.
     *********************************************************************************************/
    private void initializeLookAndFeelDefaults() {
        menuFont = UIManager.getFont(UIThemeName.NAVIGATION_MENU_FONT);
        menuItemFont = UIManager.getFont(UIThemeName.NAVIGATION_MENU_ITEM_FONT);

        menuItemIcon = (ImageIcon) UIManager.getIcon(UIThemeName.NAVIGATION_MENU_ITEM_ICON);
        menuOpenIcon = (ImageIcon) UIManager.getIcon(UIThemeName.NAVIGATION_MENU_OPEN_ICON);
        menuClosedIcon = (ImageIcon) UIManager.getIcon(UIThemeName.NAVIGATION_MENU_CLOSED_ICON);

        menuNormalColor = UIManager.getColor(UIThemeName.NAVIGATION_MENU_FOREGROUND);
        menuFocusColor = UIManager.getColor(UIThemeName.NAVIGATION_MENU_FOCUS_FOREGROUND);

        menuItemBackgroundColor = UIManager.getColor(UIThemeName.NAVIGATION_MENU_ITEM_BACKGROUND);
        menuItemForegroundColor = UIManager.getColor(UIThemeName.NAVIGATION_MENU_ITEM_FOREGROUND);
        menuItemRolloverBackColor = UIManager.getColor(UIThemeName.NAVIGATION_MENU_ITEM_ROLLOVER_BACKGROUND);
        menuItemRolloverForeColor = UIManager.getColor(UIThemeName.NAVIGATION_MENU_ITEM_ROLLOVER_FOREGROUND);
        menuItemActiveBackColor = UIManager.getColor(UIThemeName.NAVIGATION_MENU_ITEM_ACTIVE_BACKGROUND);
        menuItemActiveForeColor = UIManager.getColor(UIThemeName.NAVIGATION_MENU_ITEM_ACTIVE_FOREGROUND);
    }

    /*********************************************************************************************
     * Initializes the navigation pane, turning the horizontal scrollbar off and changing the
     * background color of the navigation scrollpane.
     *********************************************************************************************/
    private void initializeNavigationComponents() {
        navigationPane.setBorder(null);
        navigationPane.turnHorizontalScrollBarOff();
        navigationPane.setExtendedBackground(UIManager.getColor(UIThemeName.NAVIGATION_MENU_BACKGROUND));

        navigationPanel.setLayout(new GridBagLayout());
        navigationPanel.setEmptyBorder(5);
        navigationPanel.setBackground(UIManager.getColor(UIThemeName.NAVIGATION_MENU_BACKGROUND));

        //		Border emptyBorder = new EmptyBorder(2, 4, 2, 4);
        //		Border matteBorder = new MatteBorder(0, 0, 1, 0, menuItemForegroundColor);

        //		menuItemBorder = new CompoundBorder(matteBorder, emptyBorder);
    }

    /*********************************************************************************************
     * Retrieves the preferred width of the navigation tab. This calculates the minimum width that
     * the tab needs to be to display all visible menus and menu items within the tab.
     * <p>
     * @return The width in pixels the tab needs to be to display all its information.
     *********************************************************************************************/
    public int getPreferredWidth() {
        List visibleMenuItemList = new ArrayList<>();

        int baseWidth = 0;
        int testWidth = 0;
        for (Iterator iterator = menuDataMap.keySet().iterator(); iterator.hasNext();) {
            testWidth = ((JLabel) iterator.next()).getPreferredSize().width;

            if (testWidth > baseWidth) {
                baseWidth = testWidth;
            }
        }

        for (Iterator iterator = menuItemPanelMap.values().iterator(); iterator.hasNext();) {
            MenuItemPanel menuItemPanel = (MenuItemPanel) iterator.next();

            if (menuItemPanel.isVisible()) {
                visibleMenuItemList.addAll(menuItemPanel.getMenuItemList());
            }
        }

        for (Iterator iterator = visibleMenuItemList.iterator(); iterator.hasNext();) {
            testWidth = ((JLabel) iterator.next()).getPreferredSize().width;

            if (testWidth > baseWidth) {
                baseWidth = testWidth;
            }
        }

        return baseWidth + baseWidthPad;
    }

    /*********************************************************************************************
     * Adds a new menu to the navigation tab. This menu will be placed at the bottom of all
     * previous menus.
     *********************************************************************************************/
    private void addMenu(NavigationTaskData menuData) {
        String menuTitle = menuData.getDisplayName();

        NavigationLabel menuLabel = buildMenu(menuTitle);
        MenuItemPanel menuItemPanel = new MenuItemPanel(menuTitle);

        NavigationTaskItemData[] itemDataArray = menuData.getTaskItems();

        for (NavigationTaskItemData menuItem : itemDataArray) {
            addMenuItem(menuItemPanel, menuItem);
        }

        if (firstMenuLabel == null) {
            firstMenuLabel = menuLabel;
        }

        menuDataMap.put(menuLabel, menuData);
        menuItemPanelMap.put(menuLabel, menuItemPanel);

        navigationPanel.add(menuLabel, GridTool.constraints(0, rowCounter, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        rowCounter++;
        navigationPanel.add(menuItemPanel, GridTool.constraints(0, rowCounter, 1, 1, 1, 0, 0, 1, 0, 0, 5, 0));
        rowCounter++;
        navigationPanel.add(fillerLabel, GridTool.constraints(0, rowCounter, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
    }

    /*********************************************************************************************
     * Adds a menu item to the navigation tab.
     *********************************************************************************************/
    private void addMenuItem(MenuItemPanel menuItemPanel, NavigationTaskItemData menuItemData) {
        JLabel menuItemLabel = buildMenuItem(menuItemData.getDisplayName());

        menuItemDataMap.put(menuItemLabel, menuItemData);

        menuItemPanel.addMenuItem(menuItemLabel);
    }

    /*********************************************************************************************
     * Builds a NavigationLabel that represents a menu.
     *********************************************************************************************/
    private NavigationLabel buildMenu(String title) {
        NavigationLabel label = new NavigationLabel(title);

        label.setOpaque(false);
        label.setBorder(null);
        label.setIcon(menuClosedIcon);
        label.setFont(menuFont);
        label.setForeground(menuNormalColor);

        label.addMouseListener(createMenuMouseListener());
        label.addFocusListener(createMenuFocusListener());
        label.addKeyListener(createMenuKeyListener());

        return label;
    }

    /*********************************************************************************************
     * Builds a NavigationLabel that represents a menu item.
     *********************************************************************************************/
    private NavigationLabel buildMenuItem(String title) {
        NavigationLabel label = new NavigationLabel(title);

        label.setOpaque(true);
        label.setBorder(new EmptyBorder(4, 20, 4, 4));
        label.setFont(menuItemFont);
        label.setIcon(menuItemIcon);
        label.setBackground(menuItemBackgroundColor);
        label.setForeground(menuItemForegroundColor);

        label.addMouseListener(getMenuItemMouseListener());
        label.addFocusListener(createMenuItemFocusListener());
        label.addKeyListener(createMenuItemKeyListener());

        return label;
    }

    /*********************************************************************************************
     * Creates a panel focus listener that moves the focus to the first when the panel gets focus.
     *********************************************************************************************/
    private FocusListener createPanelFocusListener() {
        return new FocusListener() {
            public void focusGained(FocusEvent event) {
                if (firstMenuLabel != null) {
                    firstMenuLabel.requestFocusInWindow();
                }
            }

            public void focusLost(FocusEvent event) {
            }
        };
    }

    /*********************************************************************************************
     * Creates focus listener for a menu that calls the appropriate methods when focus is altered.
     *********************************************************************************************/
    private FocusListener createMenuFocusListener() {
        return new FocusListener() {
            public void focusGained(FocusEvent event) {
                doMenuEntered((JLabel) event.getSource());
            }

            public void focusLost(FocusEvent event) {
                doMenuExited((JLabel) event.getSource());
            }
        };
    }

    /*********************************************************************************************
     * Creates key listener for a menu that calls the appropriate methods when keys are struck.
     *********************************************************************************************/
    private KeyListener createMenuKeyListener() {
        return new KeyListener() {
            public void keyReleased(KeyEvent event) {
            }

            public void keyTyped(KeyEvent event) {
            }

            public void keyPressed(KeyEvent event) {
                switch (event.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                    case KeyEvent.VK_SPACE:
                        doMenuPressed((JLabel) event.getSource());
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /*********************************************************************************************
     * Creates mouse listener for a menu that calls the appropriate methods when mouse events are
     * received.
     *********************************************************************************************/
    private MouseListener createMenuMouseListener() {
        return new MouseListener() {
            public void mouseEntered(MouseEvent event) {
                doMenuEntered((JLabel) event.getSource());
            }

            public void mouseExited(MouseEvent event) {
                doMenuExited((JLabel) event.getSource());
            }

            public void mouseClicked(MouseEvent event) {
            }

            public void mousePressed(MouseEvent event) {
                doMenuPressed((JLabel) event.getSource());
            }

            public void mouseReleased(MouseEvent event) {
            }
        };
    }

    /*********************************************************************************************
     * Creates focus listener for a menu item that calls the appropriate methods when focus is altered.
     *********************************************************************************************/
    private FocusListener createMenuItemFocusListener() {
        return new FocusListener() {
            public void focusGained(FocusEvent event) {
                doMenuItemEntered((JLabel) event.getSource());
            }

            public void focusLost(FocusEvent event) {
                doMenuItemExited((JLabel) event.getSource());
            }
        };
    }

    /*********************************************************************************************
     * Creates key listener for a menu  item that calls the appropriate methods when keys are struck.
     *********************************************************************************************/
    private KeyListener createMenuItemKeyListener() {
        return new KeyListener() {
            public void keyReleased(KeyEvent event) {
            }

            public void keyTyped(KeyEvent event) {
            }

            public void keyPressed(KeyEvent event) {
                switch (event.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                    case KeyEvent.VK_SPACE:
                        doMenuItemPressed((JLabel) event.getSource());
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /*********************************************************************************************
     * Creates mouse listener for a menu item that calls the appropriate methods when mouse events
     * are received.
     *********************************************************************************************/
    private MouseListener getMenuItemMouseListener() {
        return new MouseListener() {
            public void mouseEntered(MouseEvent event) {
                doMenuItemEntered((JLabel) event.getSource());
            }

            public void mouseExited(MouseEvent event) {
                doMenuItemExited((JLabel) event.getSource());
            }

            public void mouseClicked(MouseEvent event) {
            }

            public void mousePressed(MouseEvent event) {
                doMenuItemPressed((JLabel) event.getSource());
            }

            public void mouseReleased(MouseEvent event) {
            }
        };
    }

    /*********************************************************************************************
     * When menu gains focus, the menu text is altered to the focus color.
     *********************************************************************************************/
    private void doMenuEntered(JLabel label) {
        label.setForeground(menuFocusColor);
    }

    /*********************************************************************************************
     * When menu looses focus, the menu text reverts to default menu color.
     *********************************************************************************************/
    private void doMenuExited(JLabel label) {
        if (label.isFocusOwner()) {
            return;
        }
        label.setForeground(menuNormalColor);
    }

    /*********************************************************************************************
     * When clicked, the menu either displays or removes its menu item panel based on state.
     * It also triggers the tab size property change event and requests the focus.
     *********************************************************************************************/
    private void doMenuPressed(JLabel label) {
        MenuItemPanel menuItemPanel = (MenuItemPanel) menuItemPanelMap.get(label);

        if (label.getIcon() == menuClosedIcon) {
            label.setIcon(menuOpenIcon);

            menuItemPanel.setVisible(true);
        } else {
            label.setIcon(menuClosedIcon);

            menuItemPanel.setVisible(false);
        }
        firePropertyChange(UIPropertyName.MENU_PRESSED, true, false);

        label.requestFocusInWindow();
    }

    /*********************************************************************************************
     * When menu item gains focus, its colors are altered.
     *********************************************************************************************/
    private void doMenuItemEntered(JLabel label) {
        label.setBackground(menuItemRolloverBackColor);
        label.setForeground(menuItemRolloverForeColor);
    }

    /*********************************************************************************************
     * When menu item looses focus, its colors revert to either the selected or normal colors.
     *********************************************************************************************/
    private void doMenuItemExited(JLabel label) {
        if (label == selectedLabel) {
            label.setBackground(menuItemActiveBackColor);
            label.setForeground(menuItemActiveForeColor);
        } else {
            label.setBackground(menuItemBackgroundColor);
            label.setForeground(menuItemForegroundColor);
        }
    }

    /*********************************************************************************************
     * When clicked, the label changes it color to match the new state and requests the focus.
     *********************************************************************************************/
    private void doMenuItemPressed(JLabel label) {
        if (selectedLabel != null) {
            selectedLabel.setBackground(menuItemBackgroundColor);
            selectedLabel.setForeground(menuItemForegroundColor);
        }
        selectedLabel = label;
        selectedLabel.setBackground(menuItemActiveBackColor);
        selectedLabel.setForeground(menuItemActiveForeColor);

        firePropertyChange(UIPropertyName.MENU_ITEM_PRESSED, null, menuItemDataMap.get(label));

        selectedLabel.requestFocusInWindow();
    }

    /*********************************************************************************************
     * INNER CLASS - This class is a panel that contains a series of menu item labels.
     *********************************************************************************************/
    private class MenuItemPanel extends RPanel {
        private static final long serialVersionUID = 34771874262501476L;

        private List menuItemList = new ArrayList<>();

        public MenuItemPanel(String name) {
            setLoweredBevelBorder();
            setName(name);
            setVisible(false);
            setLayout(new GridBagLayout());
        }

        public void addMenuItem(JLabel label) {
            menuItemList.add(label);

            add(label, GridTool.constraints(0, rowCounter++, 1, 1, 1, 0, 0, 1, 0, 0, 1, 0));
        }

        public List getMenuItemList() {
            return menuItemList;
        }
    }

    /*********************************************************************************************
     * INNER CLASS - This class is a label that is focus traversable.
     *********************************************************************************************/

    private class NavigationLabel extends JLabel {
        private static final long serialVersionUID = 4176041086999228938L;

        public NavigationLabel(String title) {
            setHorizontalAlignment(LEFT);
            setText(Translator.getText(title));
            setName(title);
            setFocusable(true);
        }
    }
}
