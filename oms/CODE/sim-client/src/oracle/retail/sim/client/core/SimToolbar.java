package oracle.retail.sim.client.core;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.UIManager;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.NavigationToolbar;
import oracle.retail.sim.client.swing.event.MouseSimTableDeactivateAdapter;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;

/********************************************************************************************************
 * SIM Main Menu
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimToolbar extends NavigationToolbar {
    private static final long serialVersionUID = -7859393161861594149L;

    private MouseListener tableDeactivateListener = null;
    private JButton[] buttons;

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    public SimToolbar() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setFloatable(false);
        setDoubleBuffered(true);
        setBackground(UIManager.getColor(UIThemeName.PANEL_BACKGROUND));
        setForeground(UIManager.getColor(UIThemeName.PANEL_FOREGROUND));
        setOpaque(true);
        addComponentListener(buildComponentListener());
    }

    /****************************************************************************************************
     * Retrieves the UIClassID.
     * <p>
     * @return The UI class ID.
     ***************************************************************************************************/
    public String getUIClassID() {
        return "SimToolBarUI";
    }

    /****************************************************************************************************
     * Retrieve all the buttons.
     ***************************************************************************************************/
    public JButton[] getButtons() {
        return buttons;
    }

    /****************************************************************************************************
     * Request focus to the first button
     ***************************************************************************************************/
    public boolean requestFocusInWindow() {
        List<JButton> visibleButtons = getVisibleButtons();
        if (visibleButtons.size() > 0) {
            for (JButton button : visibleButtons) {
                if (button.isDefaultButton()) {
                    return button.requestFocusInWindow();
                }
            }
            return visibleButtons.get(0).requestFocusInWindow();
        }
        return super.requestFocusInWindow();
    }

    /****************************************************************************************************
     * Display this defined set of buttons.
     ***************************************************************************************************/
    public void setButtons(JButton[] buttonArray) {
        ActionListener listener = (ActionListener) Application.getApplicationFrame();

        tableDeactivateListener = new MouseSimTableDeactivateAdapter();

        if (buttons != null) {
            for (JButton button : buttons) {
                button.removeActionListener(listener);
                button.removeMouseListener(tableDeactivateListener);
            }
        }

        removeAll();

        buttons = buttonArray;

        if (buttons == null) {
            return;
        }

        for (JButton element : buttonArray) {
            element.addActionListener(listener);
            element.addMouseListener(tableDeactivateListener);
            add(element);
        }

        validate();

        int buttonY = 0;
        int buttonHeight = 0;
        int length = buttonArray.length;
        if (length > 0) {
            buttonY = buttonArray[length - 1].getY();
            buttonHeight = buttonArray[length - 1].getHeight();
        }
        setPreferredSize(new Dimension(5, buttonY + buttonHeight + 5));
        revalidate();
        repaint();
    }

    /****************************************************************************************************
     * Gets the armed button, if any, and performs a doClick() on it. This method is commonly used when a
     * confirm (yes/no) dialog is presented to the user, and the user selects an option which should NOT
     * cancel any button actions.
     *
     * For instance: the user enters an item quantity greater than stock on hand for that item and
     * presses the 'Done' button, so the system prompts the user: 'Quantity entered is greater than Stock
     * On Hand for that item, do you wish to use this quantity?' If the user presses 'Yes,' then we want
     * the 'Done' button action to continue (cancelling the button action is the default, so nothing has
     * to be done if we want to cancel the 'Done' action.) For the 'Done' button press to actually
     * happen, call this method.
     ***************************************************************************************************/
    public void clickArmedButton() {
        for (JButton button : buttons) {
            if (button.getModel().isArmed()) {
                button.doClick();
                return;
            }
        }
    }

    /****************************************************************************************************
     * A function key has been pressed
     ***************************************************************************************************/
    public void doHotKeyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_F1:
                processFunctionKey(1);
                break;
            case KeyEvent.VK_F2:
                processFunctionKey(2);
                break;
            case KeyEvent.VK_F3:
                processFunctionKey(3);
                break;
            case KeyEvent.VK_F4:
                processFunctionKey(4);
                break;
            case KeyEvent.VK_F5:
                processFunctionKey(5);
                break;
            case KeyEvent.VK_F6:
                processFunctionKey(6);
                break;
            case KeyEvent.VK_F7:
                processFunctionKey(7);
                break;
            case KeyEvent.VK_F8:
                processFunctionKey(8);
                break;
            case KeyEvent.VK_F9:
                processFunctionKey(9);
                break;
            case KeyEvent.VK_F10:
                processFunctionKey(10);
                break;
            default:
                break;
        }
    }

    /****************************************************************************************************
     * Pressed the correct button for function key
     ***************************************************************************************************/
    private void processFunctionKey(int key) {
        List<JButton> visibleButtons = getVisibleButtons();
        if (key <= visibleButtons.size()) {
            visibleButtons.get(key - 1).doClick();
        }
    }

    /****************************************************************************************************
     * Component Listener
     ***************************************************************************************************/
    private ComponentListener buildComponentListener() {
        return new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setButtons(buttons);
            }
        };
    }

    /****************************************************************************************************
     * Component Listener
     ***************************************************************************************************/
    private List<JButton> getVisibleButtons() {
        List<JButton> visibleButtons = new ArrayList<>();
        if (buttons != null) {
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i].isVisible()) {
                    visibleButtons.add(buttons[i]);
                }
            }
        }
        return visibleButtons;
    }
}
