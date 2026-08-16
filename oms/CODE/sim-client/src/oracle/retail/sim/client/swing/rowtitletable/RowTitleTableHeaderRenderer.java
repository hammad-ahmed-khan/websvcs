package oracle.retail.sim.client.swing.rowtitletable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.CustomSwanLookAndFeel;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * This is the column header renderer for the row title table.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RowTitleTableHeaderRenderer extends RPanel implements TableCellRenderer {
    private static final long serialVersionUID = 4041569500327401164L;

    private RLabel headerLabel = new RLabel(StringConstants.EMPTY);
    private Color background = Color.WHITE;
    private Color foreground = Color.BLACK;
    private Border border = BorderFactory.createBevelBorder(BevelBorder.RAISED);
    private Font font = CustomSwanLookAndFeel.getControlTextFont();

    /****************************************************************************************************
     * Constructs a new RowTitleTableHeaderRenderer.
     ***************************************************************************************************/
    public RowTitleTableHeaderRenderer(RowTitleTable parent) {
        background = parent.getTableHeaderBackground();
        foreground = parent.getTableHeaderForeground();
        font = parent.getTableHeaderFont();
        border = parent.getTableHeaderBorder();
        setLayout(new BorderLayout());
        add(headerLabel, BorderLayout.CENTER);
        setOpaque(true);
    }

    /****************************************************************************************************
     * Retrieves the component that should render the row title table column header.
     ***************************************************************************************************/
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setBorder(border);
        setBackground(background);
        setForeground(foreground);
        headerLabel.setFont(font);
        headerLabel.setText(Translator.getText(value.toString()));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return this;
    }
}
