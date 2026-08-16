package oracle.retail.sim.client.swing.rowtitletable;

import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/********************************************************************************************************
 * This class is the table cell renderer for a cell within the row title table.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RowTitleTableCellRenderer extends JPanel implements TableCellRenderer {
    private static final long serialVersionUID = -5118388404636677147L;

    private RowTitleTable parent;

    /****************************************************************************************************
     * Creates a new RowTitleTableCellRenderer for a particular table.
     * <p>
     * @param table The table to create the cell renderer for.
     ***************************************************************************************************/
    public RowTitleTableCellRenderer(RowTitleTable table) {
        parent = table;
        setOpaque(true);
    }

    /****************************************************************************************************
     * Retrieves the table cell renderer component for a particular value at a particular cell.
     ***************************************************************************************************/
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        RowTitleTableCell cellData = (RowTitleTableCell) value;

        removeAll();
        setLayout(new GridLayout(cellData.getSize(), 1));

        if (hasFocus) {
            setBorder(parent.getCellFocusBorder());
            setBackground(parent.getCellFocusBackground());
            parent.sendCellSelectionNotification();
        } else {
            setBorder(null);
            setBackground(parent.getCellDefaultBackground());
        }

        JLabel label;
        for (int i = 0; i < cellData.getSize(); i++) {
            label = new JLabel();
            label.setText(cellData.getDisplayValue(i));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            if (hasFocus) {
                label.setForeground(parent.getCellFocusForeground());
            } else {
                label.setForeground(parent.getCellDefaultForeground());
            }
            add(label);
        }
        return this;
    }
}
