package oracle.retail.sim.client.swing.displaytable;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.widget.RCheckBox;
import oracle.retail.sim.client.swing.widget.RHyperlink;
import oracle.retail.sim.common.format.Mask;

/******************************************************************************************
 * The display table cell renderer for non-editable table cells, using the
 * DefaultTableCellRenderer for basic GUI appearance. Each cell of a RDisplayTable must
 * contain a RDisplayTableCell object for the cell to be displayed with this render.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RTableCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = -5599534657801830008L;

    private Mask formatMask;
    private Icon columnIcon;

    /******************************************************************************************
     * Returns new RDisplayTableCellRenderer object.
     *****************************************************************************************/
    public RTableCellRenderer() {
    }

    /******************************************************************************************
     * Assigns a format mask to the table cell renderer to format the text displayed in the
     * table cell.
     * <p>
     * @param formatMask The format mask to assign to the cell renderer.
     *****************************************************************************************/
    public void setMask(Mask formatMask) {
        this.formatMask = formatMask;
    }

    /******************************************************************************************
     * Assigns the icon to display in this renderer if its column's data type is icon and
     * it is set to true.
     * <p>
     * @param formatMask The format mask to assign to the cell renderer.
     *****************************************************************************************/
    public void setColumnIcon(Icon columnIcon) {
        this.columnIcon = columnIcon;
    }

    /******************************************************************************************
     * Retrieves the component used to paint the value of the cell. When the RDisplayTable uses
     * this renderer, the object stored within must be a RDisplayTableCell or the cell will
     * be left empty.
     * <p>
     *@param table The table to be painted.
     *@param value The value to be painted.
     *@param isSelected True if the specified value is selected.
     *@param hasFocus True if the specified value has the focus.
     *@param row The row to be painted.
     *@param column The column to be painted.
     *
     *@return A JLabel with the appropriate background and foreground color and font.
     *****************************************************************************************/
    public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(table, object, isSelected, hasFocus, row, column);

        RTableColumnInterface tableColumn = (RTableColumnInterface) table;

        int colorColumn = table.convertColumnIndexToModel(column);

        Color background = tableColumn.getColumnBackground(row, colorColumn);
        Color foreground = tableColumn.getColumnForeground(row, colorColumn);

        if (isSelected) {
            background = UIManager.getColor(UIThemeName.TABLE_SELECTION_BACKGROUND);
            foreground = UIManager.getColor(UIThemeName.TABLE_SELECTION_FOREGROUND);
        }

        try {
            RDisplayTableCell tableCell = (RDisplayTableCell) object;

            switch (tableCell.getType()) {
                case DataTypeConstants.BOOLEAN:
                    return getCheckBox(tableCell.getValue(), background);
                case DataTypeConstants.ICON:
                    return getIconLabel(label, tableCell.getValue(), background, foreground);
                case DataTypeConstants.HYPERLINK:
                    return getHyperlink(table, row, column, tableCell.getValue(), background, foreground);
                case DataTypeConstants.TEXT_FULL:
                    return getTextArea(table, row, column, tableCell.getValue(), background, foreground);
                case DataTypeConstants.CURRENCY:
                case DataTypeConstants.CURRENCY_LEFT:
                case DataTypeConstants.CURRENCY_RIGHT:
                case DataTypeConstants.DECIMAL:
                case DataTypeConstants.DECIMAL_LEFT:
                case DataTypeConstants.DECIMAL_RIGHT:
                case DataTypeConstants.INTEGER:
                case DataTypeConstants.INTEGER_LEFT:
                case DataTypeConstants.INTEGER_RIGHT:
                    label.setBackground(background);
                    label.setForeground(foreground);
                    label.setHorizontalAlignment(RIGHT);
                    break;
                default:
                    label.setBackground(background);
                    label.setForeground(foreground);
                    label.setHorizontalAlignment(LEFT);
            }

            if (formatMask != null) {
                label.setText(formatMask.format(tableCell.getValue()));
            } else {
                label.setText(tableCell.getValue());
            }
        } catch (Exception exception) {
            label.setForeground(background);
            label.setBackground(foreground);
            label.setText("");
        }
        return label;
    }

    /******************************************************************************************
     * Retrieves a check box for internal display in the table cell.
     * <p>
     * @param value The value to display in the check box ("true" or "false")
     * @param isSelected True if the table row is selected, false if not.
     *****************************************************************************************/
    private JLabel getIconLabel(JLabel label, String value, Color background, Color foreground) {
        label.setBackground(background);
        label.setForeground(foreground);
        label.setHorizontalAlignment(CENTER);
        label.setText("");

        if (value.equalsIgnoreCase(Boolean.TRUE.toString())) {
            label.setIcon(columnIcon);
        }
        return label;
    }

    /******************************************************************************************
     * Retrieves a check box for internal display in the table cell.
     * <p>
     * @param value The value to display in the check box ("true" or "false")
     * @param isSelected True if the table row is selected, false if not.
     *****************************************************************************************/
    private RCheckBox getCheckBox(String value, Color background) {
        RCheckBox checkBox = new RCheckBox();

        checkBox.setHorizontalAlignment(CENTER);
        checkBox.setBackground(background);
        checkBox.setBackgroundPaintActivated(false);
        checkBox.setOpaque(true);

        if (value.equalsIgnoreCase(Boolean.TRUE.toString())) {
            checkBox.setSelected(true);
        }
        return checkBox;
    }

    /******************************************************************************************
     * Retrieves a hyperlink for internal display in the table cell.
     * <p>
     * @param value The value to display in the text area.
     * @param isSelected True if the table row is selected, false if not.
     *****************************************************************************************/
    private RHyperlink getHyperlink(JTable table, int row, int column, String value, Color bColor, Color fColor) {
        RHyperlink hyperlink = new RHyperlink(value);

        hyperlink.setHorizontalAlignment(LEFT);
        hyperlink.setBackground(bColor);
        hyperlink.setForeground(fColor);
        hyperlink.setDisabledForeground(fColor);
        hyperlink.setSelectedForeground(fColor);
        hyperlink.setBorder(null);
        hyperlink.setOpaque(true);

        return hyperlink;
    }

    /******************************************************************************************
     * Retrieves a text area for internal display in the table cell.
     * <p>
     * @param value The value to display in the text area.
     * @param isSelected True if the table row is selected, false if not.
     *****************************************************************************************/
    private JTextArea getTextArea(JTable table, int row, int column, String value, Color bColor, Color fColor) {
        JTextArea textArea = new JTextArea();

        FontMetrics metrics = textArea.getFontMetrics(textArea.getFont());
        String[] tokenArray = StringUtility.getStringArray(value, " ");
        StringBuilder buffer = new StringBuilder();
        String token = null;

        int lineLength = 0;
        int tokenLength = 0;
        int spaceLength = metrics.stringWidth(" ");
        int columnWidth = table.getColumn(table.getColumnName(column)).getWidth() - 10;

        for (String element : tokenArray) {
            token = element;
            tokenLength = metrics.stringWidth(token);

            if (lineLength + tokenLength > columnWidth) {
                if (lineLength > 0) {
                    buffer.append("\n");
                    lineLength = 0;
                }
            }
            buffer.append(token);
            buffer.append(" ");

            int newLineIndex = token.lastIndexOf('\n');
            if (newLineIndex > -1) {
                lineLength = 0;
                tokenLength = token.substring(newLineIndex).length();
            }
            lineLength = lineLength + tokenLength + spaceLength;
        }

        textArea.setText(buffer.toString());
        textArea.setBackground(bColor);
        textArea.setForeground(fColor);
        textArea.setBorder(null);

        int preferredHeight = textArea.getPreferredSize().height;
        int actualHeight = table.getRowHeight(row);

        if (preferredHeight > actualHeight) {
            table.setRowHeight(row, preferredHeight);
        }

        return textArea;
    }
}
