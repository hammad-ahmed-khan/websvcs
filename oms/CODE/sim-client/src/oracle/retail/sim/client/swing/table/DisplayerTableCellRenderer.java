package oracle.retail.sim.client.swing.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.common.core.type.Displayer;
import oracle.retail.sim.common.core.type.HelpDisplayer;

/********************************************************************************************************
 * This class allows table cell renderer definitions based on a TypeRenderer. Doing things this way
 * allows the String conversion process to happen separately from the table cell renderer - so that these
 * String conversions can be done outside the context of a table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DisplayerTableCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 3133069632441420291L;

    private static Border LAST_LINE_BORDER = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY);

    private Displayer displayer;
    private int txtAlignment = LEFT;

    /****************************************************************************************************
     * Create a new table cell renderer with the given Displayer.
     ***************************************************************************************************/
    public DisplayerTableCellRenderer(Displayer displayer) {
        this.displayer = displayer;
    }

    /****************************************************************************************************
     * Create a new table cell renderer with the given Displayer.
     ***************************************************************************************************/
    public DisplayerTableCellRenderer(Displayer displayer, int txtAlignment) {
        this.displayer = displayer;
        this.txtAlignment = txtAlignment;
    }

    /****************************************************************************************************
     * Retrieves the displayer
     ***************************************************************************************************/
    public Displayer getDisplayer() {
        return displayer;
    }

    /****************************************************************************************************
     * Creates the correct table cell renderer using the displayer to translate to text.
     ***************************************************************************************************/
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (table instanceof SimTable) {
            return getSimTableCellRendererComponent((SimTable) table, value, isSelected, hasFocus, row, column);
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }

    /****************************************************************************************************
     * Creates specific renderer for SIM table. It uses the displayer to set the value and uses the
     * appropriate assigned them values to build font and colors.
     ***************************************************************************************************/
    private Component getSimTableCellRendererComponent(SimTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Object modelValue = table.getRowData(row);

        setHorizontalAlignment(txtAlignment);
        setValue(displayer.getDisplayText(value, modelValue));
        setFont(table.getFont());

        if (modelValue instanceof HelpDisplayer) {
            setToolTipText(((HelpDisplayer) modelValue).toHelpString());
        }

        if (isSelected) {
            setForeground(UIManager.getColor(UIThemeName.TABLE_SELECTION_FOREGROUND));
            setBackground(UIManager.getColor(UIThemeName.TABLE_SELECTION_BACKGROUND));
        } else if (row % 2 == 0) {
            setBackground(table.getRowBackground());
            setForeground(table.getRowForeground());
        } else {
            setBackground(table.getAlternateRowBackground());
            setForeground(table.getRowForeground());
        }

        if (hasFocus) {
            setBorder(UIManager.getBorder(UIThemeName.TABLE_FOCUS_CELL_BORDER));

            if (table.isCellEditable(row, column)) {
                setForeground(UIManager.getColor(UIThemeName.TABLE_FOCUS_CELL_FOREGROUND));
                setBackground(UIManager.getColor(UIThemeName.TABLE_FOCUS_CELL_BACKGROUND));
            }
        } else if (table.getRowCount() - 1 == row) {
            setBorder(LAST_LINE_BORDER);
        } else {
            setBorder(noFocusBorder);
        }
        return this;
    }
}
