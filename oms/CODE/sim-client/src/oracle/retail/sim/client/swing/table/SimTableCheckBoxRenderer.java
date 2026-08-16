package oracle.retail.sim.client.swing.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.widget.RCheckBox;

/********************************************************************************************************
 * This is a check box renderer for boolean values that can be used in the SIM table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimTableCheckBoxRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = -3508054134203999427L;

    private static Border LAST_LINE_BORDER = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY);
    private boolean paintBox;

    /****************************************************************************************************
     * Creates new SimTableCheckBoxRenderer.
     ***************************************************************************************************/
    public SimTableCheckBoxRenderer() {
    }

    /****************************************************************************************************
     * Creates new SimTableCheckBoxRenderer.
     ***************************************************************************************************/
    public SimTableCheckBoxRenderer(boolean paintBox) {
        this.paintBox = paintBox;
    }

    /****************************************************************************************************
     * Retrieves the component used to paint the value of the cell. When the RDisplayTable uses this
     * renderer, the object stored within must be a RDisplayTableCell or the cell will be left empty.
     * <p>
     * @param table The table to be painted.
     * @param value The value to be painted.
     * @param isSelected True if the specified value is selected.
     * @param hasFocus True if the specified value has the focus.
     * @param row The row to be painted.
     * @param column The column to be painted.
     *
     * @return A JLabel with the appropriate background and foreground color and font.
     ***************************************************************************************************/
    public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column) {
        RCheckBox checkBox = new RCheckBox();
        checkBox.setHorizontalAlignment(CENTER);
        checkBox.setBackgroundPaintActivated(paintBox);
        checkBox.setOpaque(true);

        if (table instanceof SimTable) {
            SimTable simTable = (SimTable) table;
            if (isSelected) {
                checkBox.setForeground(UIManager.getColor(UIThemeName.TABLE_SELECTION_FOREGROUND));
                checkBox.setBackground(UIManager.getColor(UIThemeName.TABLE_SELECTION_BACKGROUND));
            } else if (row % 2 == 0) {
                checkBox.setBackground(simTable.getRowBackground());
                checkBox.setForeground(simTable.getRowForeground());
            } else {
                checkBox.setBackground(simTable.getAlternateRowBackground());
                checkBox.setForeground(simTable.getRowForeground());
            }
        }
        if (object instanceof Boolean) {
            checkBox.setSelected((Boolean) object);
        } else if (object != null) {
            checkBox.setSelected(Boolean.TRUE.toString().equalsIgnoreCase(object.toString()));
        } else {
            checkBox.setSelected(false);
        }
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(checkBox, BorderLayout.CENTER);
        if (table.getRowCount() - 1 == row) {
            panel.setBorder(LAST_LINE_BORDER);
        }
        return panel;
    }
}
