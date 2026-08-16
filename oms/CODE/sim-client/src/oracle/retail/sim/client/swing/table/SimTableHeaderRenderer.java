package oracle.retail.sim.client.swing.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RLabel;

/********************************************************************************************************
 * Table Headerer Renderer for the SIM Table. This renderer allows multiple line headers. The separator
 * that indicates a label should be split is a "|" symbol. Not optimal, but will work for now.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimTableHeaderRenderer extends JPanel implements TableCellRenderer {
    private static final long serialVersionUID = 7148475786550521838L;

    private static Border emptyBorder = new EmptyBorder(0, 2, 0, 2);
    private static Border raisedBorder = BorderFactory.createRaisedBevelBorder();
    private static String LEFT = "left";
    private static String RIGHT = "right";

    private RLabel headerSortLabel = new RLabel();
    private RLabel headerLabelOne = new RLabel();
    private RLabel headerLabelTwo = new RLabel();

    /****************************************************************************************************
     * Returns new SimTableHeaderRenderer object.
     ***************************************************************************************************/
    public SimTableHeaderRenderer() {
        initializeLabels();

        setLayout(new GridBagLayout());
        add(headerLabelOne, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        add(headerLabelTwo, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        add(headerSortLabel, GridTool.constraints(1, 0, 1, 2, 0, 0, 0, 2, 0, 0, 0, 0));
    }

    /****************************************************************************************************
     * Inititalizes the label components.
     ***************************************************************************************************/
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

    /****************************************************************************************************
     * Overrides the superclass method to create our own customer renderer for the table header. It
     * provides automatic language translation for any header titles.
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
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        SimTableHeaderInterface tableHeader = (SimTableHeaderInterface) table;

        Color background = tableHeader.getTableHeaderBackground();
        Color foreground = tableHeader.getTableHeaderForeground();
        Font font = tableHeader.getTableHeaderFont();

        setBorder(new CompoundBorder(raisedBorder, emptyBorder));
        setBackground(background);

        headerSortLabel.setIcon(null);

        SimTableSortCriteria[] criteria = ((SimTableModel) table.getModel()).getSortCriteria();
        if (criteria != null) {
            for (int i = 0; i < criteria.length; i++) {
                if (criteria[i].getColumnIndex() == table.convertColumnIndexToModel(column)) {
                    if (criteria[i].isAscending()) {
                        headerSortLabel.setIcon(findAscendingIcon(i + 1));
                    } else {
                        headerSortLabel.setIcon(findDescendingIcon(i + 1));
                    }
                    break;
                }
            }
        }

        String text = value.toString();
        int index = StringUtility.indexOf(text, "|");
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

    /****************************************************************************************************
     * Retrieves the appropriate ascending Icon.
     ***************************************************************************************************/
    private Icon findAscendingIcon(int sequence) {
        switch (sequence) {
            case 1:
                return UIManager.getIcon(UIThemeName.SORT_ASC_1);
            case 2:
                return UIManager.getIcon(UIThemeName.SORT_ASC_2);
            case 3:
                return UIManager.getIcon(UIThemeName.SORT_ASC_3);
            case 4:
                return UIManager.getIcon(UIThemeName.SORT_ASC_4);
            case 5:
                return UIManager.getIcon(UIThemeName.SORT_ASC_5);
            case 6:
                return UIManager.getIcon(UIThemeName.SORT_ASC_6);
            case 7:
                return UIManager.getIcon(UIThemeName.SORT_ASC_7);
            case 8:
                return UIManager.getIcon(UIThemeName.SORT_ASC_8);
            case 9:
                return UIManager.getIcon(UIThemeName.SORT_ASC_9);
            default:
                return UIManager.getIcon(UIThemeName.SORT_ASC_X);
        }
    }

    /****************************************************************************************************
     * Retrieves the appropriate descending Icon.
     ***************************************************************************************************/
    private Icon findDescendingIcon(int sequence) {
        switch (sequence) {
            case 1:
                return UIManager.getIcon(UIThemeName.SORT_DES_1);
            case 2:
                return UIManager.getIcon(UIThemeName.SORT_DES_2);
            case 3:
                return UIManager.getIcon(UIThemeName.SORT_DES_3);
            case 4:
                return UIManager.getIcon(UIThemeName.SORT_DES_4);
            case 5:
                return UIManager.getIcon(UIThemeName.SORT_DES_5);
            case 6:
                return UIManager.getIcon(UIThemeName.SORT_DES_6);
            case 7:
                return UIManager.getIcon(UIThemeName.SORT_DES_7);
            case 8:
                return UIManager.getIcon(UIThemeName.SORT_DES_8);
            case 9:
                return UIManager.getIcon(UIThemeName.SORT_DES_9);
            default:
                return UIManager.getIcon(UIThemeName.SORT_DES_X);
        }
    }
}
