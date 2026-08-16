package oracle.retail.sim.client.swing.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.UIManager;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.NavigationManager;
import oracle.retail.sim.client.application.Screen;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.event.MouseSimTableDeactivateAdapter;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;

/******************************************************************************************
 * This class is the standard button panel.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RButtonPanel extends RPanel {
    private static final long serialVersionUID = 2183211709386253706L;

    private ComponentListener componentListener;

    /******************************************************************************************
     * Returns new RButtonPanel object.
     *****************************************************************************************/
    public RButtonPanel() {
        setDecorated(false);
    }

    /******************************************************************************************
     * Returns new RButtonPanel object.
     * <p>
     * @param decorated True if it should be decorated (with border and colors), false otherwise.
     *****************************************************************************************/
    public RButtonPanel(boolean decorated) {
        setDecorated(decorated);
    }

    /******************************************************************************************
     * Sets whether or not the button panel is decorated (special background and border).
     * <p>
     * @param decorated True if it should be decorated (with border and colors), false otherwise.
     *****************************************************************************************/
    public void setDecorated(boolean decorated) {
        if (decorated) {
            setDoubleBuffered(true);
            setBackground(UIManager.getColor(UIThemeName.BUTTON_PANEL_BACKGROUND));
            setForeground(UIManager.getColor(UIThemeName.BUTTON_PANEL_FOREGROUND));
            setLineBorder(2);
            setRightLayout();
            setOpaque(true);
        } else {
            setDoubleBuffered(true);
            setBackground(UIManager.getColor(UIThemeName.PANEL_BACKGROUND));
            setForeground(UIManager.getColor(UIThemeName.PANEL_FOREGROUND));
            setBorder(null);
            setRightLayout();
            setOpaque(false);
        }
        setWrapButtons(true);
    }

    /******************************************************************************************
     * Sets whether the layout will wrap buttons when horizontal space is limited.
     *****************************************************************************************/
    public void setWrapButtons(boolean wrap) {
        if (wrap) {
            if (componentListener == null) {
                componentListener = buildComponentListener();
                addComponentListener(componentListener);
            }
        } else if (componentListener != null) {
            removeComponentListener(componentListener);
            componentListener = null;
        }
    }

    /******************************************************************************************
     * Sets the layout of buttons to centered.
     *****************************************************************************************/
    public void setCenterLayout() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    /******************************************************************************************
     * Sets the layout of buttons to right justified.
     *****************************************************************************************/
    public void setRightLayout() {
        setLayout(new FlowLayout(FlowLayout.RIGHT));
    }

    /******************************************************************************************
     * Sets the layout of buttons to left justified.
     *****************************************************************************************/
    public void setLeftLayout() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    /******************************************************************************************
     * Retrieves the line color used by the panel to paint line borders and titles.
     * <p>
     * @return The line color.
     *****************************************************************************************/
    public Color getLineColor() {
        if (lineColor == null) {
            lineColor = UIManager.getColor(UIThemeName.BUTTON_PANEL_LINE_COLOR);
        }
        return lineColor;
    }

    /******************************************************************************************
     * Overrides the superclass method in order to gaurantee that the added component is an
     * instance of a button.
     * <p>
     * @param component A button to add to the panel.
     *****************************************************************************************/
    public Component add(Component component) {
        if (!(component instanceof JButton)) {
            throw new IllegalArgumentException("Cannot add: " + component.getClass() + " to a button panel!");
        }
        return addButton((JButton) component);
    }

    /******************************************************************************************
     * Adds a button to the panel.
     * <p>
     * @param component A button to add to the panel.
     ******************************************************************************************/
    public Component addButton(JButton button) {
        NavigationManager manager = Application.getNavigationManager();
        if (manager != null) {
            Screen screen = manager.getCurrentScreen();
            if (screen != null) {
                button.addMouseListener(new MouseSimTableDeactivateAdapter(screen));
            }
        }
        return super.add(button);
    }

    /******************************************************************************************
     * Adds a button to the panel.
     * <p>
     * @param component A button to add to the panel.
     ******************************************************************************************/
    public Component addButton(JButton button, RDialog dialog) {
        if (dialog != null) {
            button.addMouseListener(new MouseSimTableDeactivateAdapter(dialog));
        }
        return super.add(button);
    }

    /******************************************************************************************
     * Returns all the buttons within the panel.
     * <p>
     * @return All the buttons within the panel.
     *****************************************************************************************/
    public JButton[] getButtons() {
        List<JButton> buttonList = new ArrayList<>();
        Component[] components = getComponents();
        for (Component component : components) {
            findButtons(buttonList, component);
        }
        if (buttonList.isEmpty()) {
            return new JButton[0];
        }
        return buttonList.toArray(new JButton[buttonList.size()]);
    }

    /******************************************************************************************
     * Returns true if mouse is over button.
     *****************************************************************************************/
    public boolean isMouseOverButton() {
        JButton[] buttons = getButtons();
        for (JButton button : buttons) {
            if (button.getMousePosition(false) != null) {
                return true;
            }
        }
        return false;
    }

    /******************************************************************************************
     * Keep finding buttons
     *****************************************************************************************/
    private void findButtons(List<JButton> buttonList, Component component) {
        if (component instanceof JButton) {
            buttonList.add((JButton) component);
        } else if (component instanceof Container) {
            Component[] components = ((Container) component).getComponents();
            for (Component componentx : components) {
                findButtons(buttonList, componentx);
            }
        }
    }

    /******************************************************************************************
     * Retrieves the component listener used for wrapping buttons during resizing.
     *****************************************************************************************/
    private ComponentListener buildComponentListener() {
        return new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int maxWidth = 0;
                int maxY = 0;
                Component[] components = getComponents();
                for (Component component : components) {
                    if (!component.isVisible()) {
                        continue;
                    }
                    if (component.getWidth() > maxWidth) {
                        maxWidth = component.getWidth();
                    }
                    int y = component.getY() + component.getHeight();
                    if (y > maxY) {
                        maxY = y;
                    }
                }
                Insets insets = getInsets();
                Dimension margin = new Dimension(0, 0);
                LayoutManager layoutManager = getLayout();
                if (layoutManager instanceof FlowLayout) {
                    FlowLayout flowLayout = (FlowLayout) layoutManager;
                    margin = new Dimension(flowLayout.getHgap(), flowLayout.getVgap());
                }
                int minWidth = maxWidth + insets.left + insets.right + margin.width * 2;
                int minHeight = maxY + insets.top + insets.bottom + margin.height;
                Dimension minimumSize = new Dimension(minWidth, minHeight);
                if (!minimumSize.equals(getMinimumSize())) {
                    setMinimumSize(minimumSize);
                    revalidate();
                }
            }
        };
    }
}
