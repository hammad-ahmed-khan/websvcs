package oracle.retail.sim.client.swing.displaytable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.widget.RIconButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RScrollPane;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * This class is a title pane that contains a display table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RDisplayTablePane extends RPanel implements ActionListener, MouseListener {
    private static final long serialVersionUID = 5852178643215546463L;

    private RPanel titlePanel = new RPanel();
    private RLabel titleLabel = new RLabel();
    private RScrollPane scrollPane = new RScrollPane();
    private RDisplayTable displayTable;
    private RIconButton configButton = new RIconButton();
    private boolean titleActive;

    /****************************************************************************************************
     * Creates a new display table pane.
     ***************************************************************************************************/
    public RDisplayTablePane() {
        initialize();
    }

    /****************************************************************************************************
     * Creates a new display table pane around a display table.
     * <p>
     * @param table The table to assign to the pane.
     ***************************************************************************************************/
    public RDisplayTablePane(RDisplayTable table) {
        initialize();
        setDisplayTable(table);
    }

    /****************************************************************************************************
     * Creates a new display table pane around a display table.
     * <p>
     * @param table The table to assign to the pane.
     * @param title The title to assign to the pane.
     ***************************************************************************************************/
    public RDisplayTablePane(RDisplayTable table, String title) {
        initialize();
        setDisplayTable(table);
        setTitle(title);
    }

    /****************************************************************************************************
     * Initializes the display table pane.
     ***************************************************************************************************/
    private void initialize() {
        titleActive = StringUtility.booleanValue(UIManager.getString(UIThemeName.RDISPLAYTABLE_TITLE_ACTIVE));

        setTitleBackground(UIManager.getColor(UIThemeName.RDISPLAYTABLEPANE_BACKGROUND));

        scrollPane.setLineBorder();
        scrollPane.addMouseListener(this);

        titleLabel.setFont(UIManager.getFont(UIThemeName.RDISPLAYTABLEPANE_FONT));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.addMouseListener(this);

        configButton.setIcon(UIManager.getIcon(UIThemeName.RDISPLAYTABLE_CONFIG_ICON));
        configButton.addActionListener(this);

        titlePanel.setBorder(new MatteBorder(1, 1, 0, 1, scrollPane.getLineColor()));
        titlePanel.setOpaque(true);
        titlePanel.setVisible(false);

        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(configButton, BorderLayout.EAST);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    /****************************************************************************************************
     * Assign the display table to the display table pane.
     * <p>
     * @param table The RDisplayTable to assign to the pane.
     ***************************************************************************************************/
    public void setDisplayTable(RDisplayTable table) {
        scrollPane.setViewportView(table);
        displayTable = table;
    }

    /****************************************************************************************************
     * Retrieves the display table assigned to the pane.
     * <p>
     * @return The RDisplayTable assigned to the pane, or null if none has been assigned.
     ***************************************************************************************************/
    public RDisplayTable getDisplayTable() {
        return displayTable;
    }

    /****************************************************************************************************
     * Assigns a title to the display table pane.
     * <p>
     * @param title The title to assign.
     ***************************************************************************************************/
    public void setTitle(String title) {
        if (StringUtility.isNullOrEmpty(title) || !titleActive) {
            titleLabel.setText(StringConstants.EMPTY);
            titlePanel.setVisible(false);
        } else {
            titleLabel.setText(title);
            titlePanel.setVisible(true);
        }
    }

    /****************************************************************************************************
     * Assigns whether or not the configuration icon is enabled on the display table pane. Note: Really,
     * this makes it invisible.
     * <p>
     * @param enabled True if the configuration box should be enabled, false otherwise.
     ***************************************************************************************************/
    public void setConfigurationEnabled(boolean enabled) {
        configButton.setVisible(enabled);
    }

    /****************************************************************************************************
     * Assigns a title background color.
     * <p>
     * @param color The title background color.
     ***************************************************************************************************/
    public void setTitleBackground(Color color) {
        titlePanel.setBackground(color);
        titleLabel.setBackground(color);
        configButton.setBackground(color);
    }

    /****************************************************************************************************
     * Assigns a title foreground color.
     * <p>
     * @param color The title foreground color.
     ***************************************************************************************************/
    public void setTitleForeground(Color color) {
        titleLabel.setForeground(color);
    }

    /****************************************************************************************************
     * Assigns the background color of the pane. This color is propagated through the scrollpane.
     * <p>
     * @param color The background color.
     ***************************************************************************************************/
    public void setBackground(Color color) {
        if (color != null) {
            super.setBackground(color);
            if (scrollPane != null) {
                scrollPane.setExtendedBackground(color);
            }
        }
    }

    /****************************************************************************************************
     * Implements the action listener method to display the configuration dialog.
     ***************************************************************************************************/
    public void actionPerformed(ActionEvent event) {
        if (titleActive && displayTable != null) {
            displayTable.displayConfigurationDialog();
        }
    }

    /****************************************************************************************************
     * Implements the mouse listener methods to display the table popup on a right mouse click.
     ***************************************************************************************************/
    public void mousePressed(MouseEvent event) {
    }

    public void mouseReleased(MouseEvent event) {
    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }

    public void mouseClicked(MouseEvent event) {
        if (titleActive && displayTable != null && SwingUtilities.isRightMouseButton(event)) {
            Point point = SwingUtilities.convertPoint(event.getComponent(), event.getX(), event.getY(), displayTable);
            displayTable.displayPopupMenu(titleLabel, point);
        }
    }
}
