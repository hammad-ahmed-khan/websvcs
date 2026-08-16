package oracle.retail.sim.client.swing.lov;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RScrollPane;
import oracle.retail.sim.common.core.locale.StringConstants;

/******************************************************************************************
 * This class is a title pane that contains a lov display table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class RListOfValuesTablePane extends RPanel {
    private static final long serialVersionUID = 7154951780966986267L;

    private RPanel titlePanel = new RPanel();
    private RLabel titleLabel = new RLabel();
    private RScrollPane scrollPane = new RScrollPane();

    /******************************************************************************************
     * Creates a new LOV display table pane around a display table.
     * <p>
     * @param table The table to assign to the pane.
     ******************************************************************************************/
    public RListOfValuesTablePane(RListOfValuesTable table) {
        initialize();
        scrollPane.setViewportView(table);
    }

    /******************************************************************************************
     * Initializes the display table pane.
     ******************************************************************************************/
    private void initialize() {
        Color background = UIManager.getColor(UIThemeName.RDISPLAYTABLEPANE_BACKGROUND);

        scrollPane.setLineBorder();

        titleLabel.setBackground(background);
        titleLabel.setFont(UIManager.getFont(UIThemeName.RDISPLAYTABLEPANE_FONT));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titlePanel.setBackground(background);
        titlePanel.setBorder(new MatteBorder(1, 1, 0, 1, scrollPane.getLineColor()));
        titlePanel.setOpaque(true);
        titlePanel.setVisible(false);
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    /******************************************************************************************
     * Assigns a title to the display table pane.
     * <p>
     * @param title The title to assign.
     ******************************************************************************************/
    protected void setTitle(String title) {
        if (StringUtility.isNullOrEmpty(title)) {
            titleLabel.setText(StringConstants.EMPTY);
            titlePanel.setVisible(false);
        } else {
            titleLabel.setText(title);
            titlePanel.setVisible(true);
        }
    }

    /******************************************************************************************
     * Assigns the background color of the pane. This color is propagated through the scrollpane.
     * <p>
     * @param color The background color.
     ******************************************************************************************/
    public void setBackground(Color color) {
        if (color != null) {
            super.setBackground(color);
            if (scrollPane != null) {
                scrollPane.setExtendedBackground(color);
            }
        }
    }
}
