package oracle.retail.sim.client.swing.navigation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RIconButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RScrollPane;
import oracle.retail.sim.common.config.NavigationTaskData;
import oracle.retail.sim.common.config.NavigationTaskItemData;

/*********************************************************************************************
 * This class represents a single menu or menu item displayer panel. It contains the title
 * with collapsible icon as well as the list of items contained within a scroll pane contained
 * within a split pane.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *********************************************************************************************/

public class PlatformNavigationMenuPanel extends RPanel {
    private static final long serialVersionUID = 6610432358686092239L;

    private RPanel labelPanel = new RPanel();
    private RScrollPane scrollPane = new RScrollPane(labelPanel);

    private RIconButton expandButton = new RIconButton();
    private RLabel titleLabel = new RLabel();

    private ImageIcon menuOpenIcon;
    private ImageIcon menuClosedIcon;
    private ImageIcon menuItemIcon;

    private Font menuFont;
    private Font labelFont;
    private Border labelBorder;

    private Color labelBackgroundColor;
    private Color labelForegroundColor;
    private Color labelRolloverBackColor;
    private Color labelRolloverForeColor;
    private Color labelActiveBackColor;
    private Color labelActiveForeColor;

    private JLabel selectedLabel;
    private Map labelDataMap = new HashMap<>();
    private boolean isTaskItem;

    /*********************************************************************************************
     * Constructs new Platform Menu Panel.
     *********************************************************************************************/
    public PlatformNavigationMenuPanel() {
        initializeLookAndFeelDefaults();
        initializeMenuPanelComponents();
        layoutMenuPanel();
    }

    /*********************************************************************************************
     * Initializes the look and feel fonts, colors and icons of the labels.
     *********************************************************************************************/
    private void initializeLookAndFeelDefaults() {
        menuFont = UIManager.getFont(UIThemeName.NAVIGATION_MENU_FONT);
        labelFont = UIManager.getFont(UIThemeName.NAVIGATION_MENU_ITEM_FONT);

        menuItemIcon = (ImageIcon) UIManager.getIcon(UIThemeName.NAVIGATION_MENU_ITEM_ICON);
        menuOpenIcon = (ImageIcon) UIManager.getIcon(UIThemeName.NAVIGATION_MENU_OPEN_ICON);
        menuClosedIcon = (ImageIcon) UIManager.getIcon(UIThemeName.NAVIGATION_MENU_CLOSED_ICON);

        labelBackgroundColor = UIManager.getColor(UIThemeName.NAVIGATION_MENU_ITEM_BACKGROUND);
        labelForegroundColor = UIManager.getColor(UIThemeName.NAVIGATION_MENU_ITEM_FOREGROUND);
        labelRolloverBackColor = UIManager.getColor(UIThemeName.NAVIGATION_MENU_ITEM_ROLLOVER_BACKGROUND);
        labelRolloverForeColor = UIManager.getColor(UIThemeName.NAVIGATION_MENU_ITEM_ROLLOVER_FOREGROUND);
        labelActiveBackColor = UIManager.getColor(UIThemeName.NAVIGATION_MENU_ITEM_ACTIVE_BACKGROUND);
        labelActiveForeColor = UIManager.getColor(UIThemeName.NAVIGATION_MENU_ITEM_ACTIVE_FOREGROUND);
    }

    /*********************************************************************************************
     * Initializes the menu panel components.
     *********************************************************************************************/
    private void initializeMenuPanelComponents() {
        titleLabel.setFont(menuFont);

        scrollPane.turnHorizontalScrollBarOff();
        scrollPane.setExtendedBackground(UIManager.getColor(UIThemeName.NAVIGATION_MENU_BACKGROUND));

        expandButton.setIcon(menuOpenIcon);
        expandButton.addActionListener(createIconActionListener());

        Border emptyBorder = new EmptyBorder(2, 4, 2, 4);
        Border matteBorder = new MatteBorder(0, 0, 1, 0, labelForegroundColor);

        labelBorder = new CompoundBorder(matteBorder, emptyBorder);
    }

    /*********************************************************************************************
     * Creates an action listener for the menu icon button to hide and display the menu panel.
     *********************************************************************************************/
    private ActionListener createIconActionListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (expandButton.getIcon() == menuOpenIcon) {
                    expandButton.setIcon(menuClosedIcon);
                    scrollPane.setVisible(false);
                } else {
                    expandButton.setIcon(menuOpenIcon);
                    scrollPane.setVisible(true);
                }
            }
        };
    }

    /*********************************************************************************************
     * Lays out the menu panel.
     *********************************************************************************************/
    private void layoutMenuPanel() {
        setOpaque(false);
        setLayout(new GridBagLayout());
        add(expandButton, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 5, 0));
        add(titleLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 1, 0, 5, 5, 0));
        add(scrollPane, GridTool.constraints(0, 1, 2, 1, 1, 1, 0, 3, 0, 0, 0, 0));
    }

    /*********************************************************************************************
     * Retrieves the minimum size of the menu panel.
     *********************************************************************************************/
    public Dimension getMinimumSize() {
        return new Dimension(0, 0);
    }

    /*********************************************************************************************
     * Sets the title of the menu panel.
     * <p>
     * @param title The title to assign.
     *********************************************************************************************/
    protected void setTitle(String title) {
        titleLabel.setText(title);
    }

    /*********************************************************************************************
     * Displays all the menus within the menu panel. This loops through the menu data, creates
     * a new label for each menu, puts the associated menu data into a hash map for the label and
     * then adds the label to the list of created labels. It then builds the panel when it is
     * finished (performance) and notes that menus are NOT task items.
     *********************************************************************************************/
    protected void displayTasks(NavigationTaskData[] taskDataArray) {
        List menuList = new ArrayList<>();
        JLabel menuLabel;
        for (NavigationTaskData element : taskDataArray) {
            menuLabel = createLabel(element.getDisplayName());
            labelDataMap.put(menuLabel, element);
            menuList.add(menuLabel);
        }
        populatePanel(menuList);
        isTaskItem = false;
    }

    /*********************************************************************************************
     * Performs the exact same functionailty as displayTasks() except that it sets a special
     * task item icon on the label. It sets the state of this panel to represent task items.
     *********************************************************************************************/
    protected void displayTaskItems(NavigationTaskItemData[] taskItemArray) {
        labelDataMap.clear();
        List menuList = new ArrayList<>();
        JLabel menuLabel;
        for (NavigationTaskItemData element : taskItemArray) {
            menuLabel = createLabel(element.getDisplayName());
            menuLabel.setIcon(menuItemIcon);
            labelDataMap.put(menuLabel, element);
            menuList.add(menuLabel);
        }
        populatePanel(menuList);
        isTaskItem = true;
    }

    /*********************************************************************************************
     * Creates new platform task or task item label. It sets all the appropriate label properties
     * and listeners on the label.
     *********************************************************************************************/
    private JLabel createLabel(String title) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setText(Translator.getText(title));
        label.setName(title);
        label.setFocusable(true);
        label.setOpaque(true);
        label.setFont(labelFont);
        label.setBackground(labelBackgroundColor);
        label.setForeground(labelForegroundColor);
        label.setBorder(labelBorder);
        label.addMouseListener(createMenuMouseListener());
        label.addFocusListener(createMenuFocusListener());
        label.addKeyListener(createMenuKeyListener());
        label.setMinimumSize(new Dimension(0, 0));
        return label;
    }

    /*********************************************************************************************
     * Creates new empty label
     *********************************************************************************************/
    private JLabel createEmptyLabel() {
        JLabel label = new JLabel();
        label.setFocusable(false);
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setForeground(Color.WHITE);
        label.setBorder(null);
        return label;
    }

    /*********************************************************************************************
     * Populates a panel with all the labels in the label list. This removes all previous labels
     * and then iterates through the list passed in. It adds all the labels to the label panel
     * using a counter for layout purposes. It then adds one last empty label with fill capacity
     * in order to keep the labels at the top of their panel.
     *********************************************************************************************/
    private void populatePanel(List labelList) {
        labelPanel.removeAll();
        labelPanel.setLayout(new GridBagLayout());

        JLabel label;
        int counter = 0;
        for (Iterator iterator = labelList.iterator(); iterator.hasNext();) {
            label = (JLabel) iterator.next();
            labelPanel.add(label, GridTool.constraints(0, counter++, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        }
        labelPanel.add(createEmptyLabel(), GridTool.constraints(0, counter++, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        labelPanel.revalidate();
        labelPanel.repaint();
    }

    /*********************************************************************************************
     * Creates focus listener for a menu that calls the appropriate methods when focus is altered.
     *********************************************************************************************/
    private FocusListener createMenuFocusListener() {
        return new FocusListener() {
            public void focusGained(FocusEvent event) {
                doLabelEntered((JLabel) event.getSource(), selectedLabel);
            }

            public void focusLost(FocusEvent event) {
                doLabelExited((JLabel) event.getSource(), selectedLabel);
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
     * triggered.
     *********************************************************************************************/
    private MouseListener createMenuMouseListener() {
        return new MouseListener() {
            public void mouseEntered(MouseEvent event) {
                doLabelEntered((JLabel) event.getSource(), selectedLabel);
            }

            public void mouseExited(MouseEvent event) {
                doLabelExited((JLabel) event.getSource(), selectedLabel);
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
     * When a navigation label gains focus, the color is altered.
     *********************************************************************************************/
    private void doLabelEntered(JLabel label, JLabel localSelectedLabel) {
        if (label != localSelectedLabel) {
            label.setBackground(labelRolloverBackColor);
            label.setForeground(labelRolloverForeColor);
        }
    }

    /*********************************************************************************************
     * When a navigation label looses focus, its colors revert to either the selected or normal colors.
     *********************************************************************************************/
    private void doLabelExited(JLabel label, JLabel localSelectedLabel) {
        if (label == localSelectedLabel) {
            label.setBackground(labelActiveBackColor);
            label.setForeground(labelActiveForeColor);
        } else {
            label.setBackground(labelBackgroundColor);
            label.setForeground(labelForegroundColor);
        }
    }

    /*********************************************************************************************
     * Called when a menu or menu item is pressed. After setting the correct color, it fires the
     * appropriate event including the data associated with the selected label.
     *********************************************************************************************/
    private void doMenuPressed(JLabel label) {
        if (selectedLabel != null) {
            selectedLabel.setBackground(labelBackgroundColor);
            selectedLabel.setForeground(labelForegroundColor);
        }
        selectedLabel = label;
        selectedLabel.setBackground(labelActiveBackColor);
        selectedLabel.setForeground(labelActiveForeColor);

        if (isTaskItem) {
            firePropertyChange(UIPropertyName.MENU_ITEM_PRESSED, null, labelDataMap.get(selectedLabel));
        } else {
            firePropertyChange(UIPropertyName.MENU_PRESSED, null, labelDataMap.get(selectedLabel));
        }
    }
}
