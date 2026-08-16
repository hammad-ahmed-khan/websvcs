package oracle.retail.sim.client.swing.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.client.swing.widget.RArrowButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************
 * This represents a panel with a title area (with a separator line) and a content area
 * that can be made to expand or collapse by clicking on the title area.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RExpandablePanel extends RPanel {
    private static final long serialVersionUID = -2836735972024331403L;

    private RExpandableTitlePanel titlePanel = new RExpandableTitlePanel();
    private RPanel contentPane = new RPanel();
    private boolean isExpanded = true;

    /******************************************************************************************
     * Constructs new RExpandablePanel.
     *****************************************************************************************/
    public RExpandablePanel() {
        super(new GridBagLayout());
        initializePanel();
    }

    /******************************************************************************************
     * Constructs new RExpandablePanel with a title.
     * <p>
     * @param title The title of the panel.
     *****************************************************************************************/
    public RExpandablePanel(String title) {
        super(new BorderLayout(0, 4));
        titlePanel.setTitle(title);
        initializePanel();
    }

    /******************************************************************************************
     * Initializes the panel.
     *****************************************************************************************/
    private void initializePanel() {
        super.add(titlePanel, BorderLayout.NORTH);
        super.add(contentPane, BorderLayout.CENTER);
    }

    /******************************************************************************************
     * Assigns layout manager to the panel. This method is overridden to only accepts GridBagLayout.
     * <p>
     * @param manager A GridBagLayout object.
     *****************************************************************************************/
    public void setLayout(LayoutManager manager) {
        if (!(manager instanceof BorderLayout)) {
            throw new IllegalArgumentException("RExpandablePanel can only accept GridBagLayout as a layout.");
        }
        super.setLayout(manager);
    }

    /******************************************************************************************
     * Adds the specified component to the end of this container. The component is added to the
     * panel at the next available row (within the current column).
     * <p>
     * @param component The editor to add to the panel.
     *****************************************************************************************/
    public Component add(Component component) {
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
    }

    /************************************************************************************
     * Adds the specified component to this container. It is strongly advised to use the
     * 1.1 method, add(Component, Object), in place of this method. This method has been
     * overridden and made unavailable.
     * <p>
     * @param name A string name for the component.
     * @param component The component to be added.
     * @return The component argument.
     ************************************************************************************/
    public Component add(String name, Component component) {
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
    }

    /************************************************************************************
     * Adds the specified component to this container at the given index. This method has
     * been overridden and made unavailable.
     * <p>
     * @param component The component to be added
     * @param index Position to add the component
     * @return The component argument.
     ************************************************************************************/
    public Component add(Component component, int index) {
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
    }

    /************************************************************************************
     * Adds the specified component to the end of this container. This method has been
     * overridden and made unavailable.
     * <p>
     * @param component The component to be added
     * @param constraints An object expressing layout contraints for this
     ************************************************************************************/
    public void add(Component component, Object constraints) {
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
    }

    /************************************************************************************
     * Adds the specified component to this container with the specified constraints at
     * the specified index.  Also notifies the layout manager to add the component to the
     * this container's layout using the specified constraints object.
     * <p>
     * @param component The component to be added
     * @param constraints An object expressing layout contraints for this
     * @param index The position in the container's list at which to insert the component.
     * 		-1 means insert at the end.
     ************************************************************************************/
    public void add(Component component, Object constraints, int index) {
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
    }

    /************************************************************************************
     * Assigns the title to the expandable panel.
     * <p>
     * @param title The title to assign.
     ************************************************************************************/
    public void setTitle(String title) {
        titlePanel.setTitle(title);
    }

    /************************************************************************************
     * Retrieves the content pane of the expandable panel.
     * <p>
     * @return The current content pane.
     ************************************************************************************/
    public RPanel getContentPane() {
        return contentPane;
    }

    /************************************************************************************
     * Assigns the content pane for the expandable panel.
     * <p>
     * @return The current content pane.
     ************************************************************************************/
    public void setContentPane(RPanel panel) {
        if (panel != null) {
            remove(contentPane);
            contentPane = panel;
            super.add(contentPane, BorderLayout.CENTER);
            if (isExpanded) {
                revalidate();
                repaint();
            }
        }
    }

    /************************************************************************************
     * Retrieves whether or not the expandable panel is currently expanded.
     * <p>
     * @return True if the panel is expanded, false if not.
     ************************************************************************************/
    public boolean isExpanded() {
        return isExpanded;
    }

    /************************************************************************************
     * Assigns whether or not the panel is expanded.
     * <p>
     * @param expanded True if the panel should be expanded, false if not.
     ************************************************************************************/
    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
        contentPane.setVisible(expanded);
    }

    /************************************************************************************
     * Assigns whether or not the expandable panel is enabled.
     * <p>
     * @param enabled True if the panel should be enabled, false otherwise.
     ************************************************************************************/
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        titlePanel.setEnabled(enabled);
    }

    /************************************************************************************
     * Sets the foreground color of the panel (basically the title area and separator line).
     * <p>
     * @param color The color to assign.
     ************************************************************************************/
    public void setForeground(Color color) {
        if (color != null) {
            super.setForeground(color);
            if (titlePanel != null) {
                titlePanel.setForeground(color);
            }
        }
    }

    /************************************************************************************
     *
     * INNER CLASS TITLE AREA
     *
     ************************************************************************************/
    private class RExpandableTitlePanel extends RPanel {
        private static final long serialVersionUID = 5635853470190842443L;

        private RExpandButton arrowButton = new RExpandButton(RExpandButton.SOUTH);
        private RLabel titleLabel = new RLabel();
        private MouseListener storedMouseListener;

        public RExpandableTitlePanel() {
            buildTitlePanel();
            layoutTitlePanel();
        }

        private void buildTitlePanel() {
            arrowButton.setBorder(null);
            arrowButton.setOpaque(false);
            arrowButton.addActionListener(createSwapActionListener());

            storedMouseListener = createSwapMouseListener();

            titleLabel.addMouseListener(storedMouseListener);

            setForeground(UIManager.getColor(UIThemeName.REXPANDABLEPANEL_FOREGROUND));
        }

        private void layoutTitlePanel() {
            setLayout(new GridBagLayout());
            add(arrowButton, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
            add(titleLabel, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        }

        private ActionListener createSwapActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    setExpanded(!isExpanded);
                    validateArrowButton();
                }
            };
        }

        private MouseListener createSwapMouseListener() {
            return new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    setExpanded(!isExpanded);
                    validateArrowButton();
                }
            };
        }

        public void setTitle(String title) {
            if (title == null) {
                title = StringConstants.EMPTY;
            }
            titleLabel.setText(title);
        }

        public void setForeground(Color color) {
            if (color != null) {
                super.setForeground(color);
                setBorder(new MatteBorder(0, 0, 1, 0, color));
                if (titleLabel != null) {
                    titleLabel.setForeground(color);
                }
            }
        }

        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            arrowButton.setEnabled(enabled);
            titleLabel.removeMouseListener(storedMouseListener);
            if (enabled) {
                titleLabel.addMouseListener(storedMouseListener);
            }
        }

        private void validateArrowButton() {
            if (isExpanded) {
                arrowButton.setDirection(RArrowButton.SOUTH);
            } else {
                arrowButton.setDirection(RArrowButton.EAST);
            }
        }
    }

    /************************************************************************************
     *
     * INNER CLASS BUTTON
     *
     ************************************************************************************/
    private class RExpandButton extends RArrowButton {
        private static final long serialVersionUID = 3794075441306966673L;

        public RExpandButton(int direction) {
            super(direction);
        }

        public String getUIClassID() {
            return "ExpandButtonUI";
        }
    }
}
