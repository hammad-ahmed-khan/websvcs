package oracle.retail.sim.client.swing.displaytable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RLabel;

/******************************************************************************************
 * Custom table column header that performs language translation on the label.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RTableHeader extends JPanel implements TableCellRenderer {
    private static final long serialVersionUID = 2949938293633260083L;

    private static Border emptyBorder = new EmptyBorder(0, 2, 0, 2);
    private static Border raisedBorder = BorderFactory.createRaisedBevelBorder();
    private static String LEFT = "left";
    private static String RIGHT = "right";

    private RLabel headerSortLabel = new RLabel();
    private RLabel headerLabelOne = new RLabel();
    private RLabel headerLabelTwo = new RLabel();

    /******************************************************************************************
     * Returns new RDisplayTableHeader object.
     *****************************************************************************************/
    public RTableHeader() {
        initializeLabels();

        setLayout(new GridBagLayout());
        add(headerLabelOne, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        add(headerLabelTwo, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        add(headerSortLabel, GridTool.constraints(1, 0, 1, 2, 0, 0, 0, 2, 0, 0, 0, 0));
    }

    private void initializeLabels() {
        String value = UIManager.getString(UIThemeName.RDISPLAYTABLE_HEADER_ALIGNMENT);

        if (LEFT.equalsIgnoreCase(value)) {
            headerLabelOne.setHorizontalAlignment(SwingConstants.LEFT);
            headerLabelTwo.setHorizontalAlignment(SwingConstants.LEFT);
        } else if (RIGHT.equalsIgnoreCase(value)) {
            headerLabelOne.setHorizontalAlignment(SwingConstants.RIGHT);
            headerLabelTwo.setHorizontalAlignment(SwingConstants.RIGHT);
        } else {
            headerLabelOne.setHorizontalAlignment(SwingConstants.CENTER);
            headerLabelTwo.setHorizontalAlignment(SwingConstants.CENTER);
        }

        headerLabelOne.setOpaque(true);
        headerLabelTwo.setOpaque(true);

        headerLabelTwo.setVisible(false);
    }

    /******************************************************************************************
     * Overrides the superclass method to create our own customer renderer for the table header.
     * It provides automatic language translation for any header titles.
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
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        RTableHeaderInterface tableHeader = (RTableHeaderInterface) table;

        Color background = tableHeader.getTableHeaderBackground();
        Color foreground = tableHeader.getTableHeaderForeground();
        Font font = tableHeader.getTableHeaderFont();

        setBorder(new CompoundBorder(raisedBorder, emptyBorder));
        setBackground(background);

        int sortColumn = tableHeader.getSortNumber(column);
        boolean ascending = tableHeader.getSortAscending(column);

        if (sortColumn < 0) {
            headerSortLabel.setIcon(null);
        } else {
            headerSortLabel.setIcon(RDisplayTableIcon.getIcon(sortColumn, ascending));
        }

        String text = value.toString();
        int index = text.indexOf("|");
        if (index > -1) {
            headerLabelOne.setText(Translator.getText(text.substring(0, index)));
            headerLabelOne.setBackground(background);
            headerLabelOne.setForeground(foreground);
            headerLabelOne.setFont(font);

            headerLabelTwo.setText(Translator.getText(text.substring(index + 1)));
            headerLabelTwo.setBackground(background);
            headerLabelTwo.setForeground(foreground);
            headerLabelTwo.setFont(font);
            headerLabelTwo.setVisible(true);
        } else {
            headerLabelOne.setText(Translator.getText(text));
            headerLabelOne.setBackground(background);
            headerLabelOne.setForeground(foreground);
            headerLabelOne.setFont(font);

            headerLabelTwo.setVisible(false);
        }

        return this;
    }
}
