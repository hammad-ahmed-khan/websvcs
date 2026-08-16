package oracle.retail.sim.client.swing.entrytable;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.SwingPropertyChangeSupport;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.UIPropertyName;

/********************************************************************************************************
 * REntryTableTraits
 * <p>
 * This class contains the basic settings for an REntryTable.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

class REntryTableTraits {

    private Color borderColor = Color.BLACK;
    private Color defaultBackgroundColor;
    private Color defaultForegroundColor;
    private Color alternateBackgroundColor;
    private Color headerBackgroundColor;
    private Color headerForegroundColor;

    private Font tableFont;
    private Font headerFont;

    private Border headerBorder = BorderFactory.createRaisedBevelBorder();
    private REntryColumn[] columns = new REntryColumn[0];
    private int[] columnWidths;

    private ReflectionWrapper dataWrapper = new ReflectionWrapper();
    private SwingPropertyChangeSupport propertyNotifier;

    private int rowHeight = 20;
    private int columnGap = 2;

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    public REntryTableTraits() {
        defaultBackgroundColor = UIManager.getColor(UIThemeName.RENTRYTABLE_BACKGROUND);
        defaultForegroundColor = UIManager.getColor(UIThemeName.RENTRYTABLE_FOREGROUND);
        alternateBackgroundColor = UIManager.getColor(UIThemeName.RENTRYTABLE_ALTERNATE_BACKGROUND);
        borderColor = UIManager.getColor(UIThemeName.RENTRYTABLE_BORDER_COLOR);
        tableFont = UIManager.getFont(UIThemeName.RENTRYTABLE_FONT);
        headerBackgroundColor = UIManager.getColor(UIThemeName.RENTRYHEADER_BACKGROUND);
        headerForegroundColor = UIManager.getColor(UIThemeName.RENTRYHEADER_FOREGROUND);
        headerFont = UIManager.getFont(UIThemeName.RENTRYHEADER_FONT);
        headerBorder = BorderFactory.createRaisedBevelBorder();
        propertyNotifier = new SwingPropertyChangeSupport(this);
    }

    /****************************************************************************************************
     * Retrieves the foreground color for the table rows.
     ***************************************************************************************************/
    protected Color getForegroundColor() {
        return defaultForegroundColor;
    }

    /****************************************************************************************************
     * Assigns the foreground color of the table rows.
     * <p>
     * @param color The color.
     ***************************************************************************************************/
    protected void setForegroundColor(Color color) {
        if (defaultForegroundColor != color) {
            defaultForegroundColor = color;
            firePropertyChange(UIPropertyName.ENTRY_TABLE_REPAINT, null, defaultForegroundColor);
        }
    }

    /****************************************************************************************************
     * Retrieves the background color of the table rows (displayed on odd rows only).
     ***************************************************************************************************/
    protected Color getBackgroundColor() {
        return defaultBackgroundColor;
    }

    /****************************************************************************************************
     * Assigns the background color of the table rows (displayed on odd rows only).
     * <p>
     * @parma color The color.
     ***************************************************************************************************/
    protected void setBackgroundColor(Color color) {
        if (defaultBackgroundColor != color) {
            defaultBackgroundColor = color;
            firePropertyChange(UIPropertyName.ENTRY_TABLE_REPAINT, null, defaultBackgroundColor);
        }
    }

    /****************************************************************************************************
     * Retrieves the alternate background color of the table rows (displayed on even rows only).
     ***************************************************************************************************/
    protected Color getAlternateBackgroundColor() {
        return alternateBackgroundColor;
    }

    /****************************************************************************************************
     * Assigns the alternate background color of the tabe rows (displayed on even rows only).
     * <p>
     * @param color The color.
     ***************************************************************************************************/
    protected void setAlternateBackgroundColor(Color color) {
        if (alternateBackgroundColor != color) {
            alternateBackgroundColor = color;
            firePropertyChange(UIPropertyName.ENTRY_TABLE_REPAINT, null, alternateBackgroundColor);
        }
    }

    /****************************************************************************************************
     * Retrieves the correct background color for the row number.
     * <p>
     * @param row The row number.
     * @return The background color for the row.
     ***************************************************************************************************/
    protected Color getRowBackground(int row) {
        if (Math.IEEEremainder(row, 2) == 0) {
            return defaultBackgroundColor;
        }
        return alternateBackgroundColor;
    }

    /****************************************************************************************************
     * Retrieves the border color for the row.
     ***************************************************************************************************/
    protected Color getBorderColor() {
        return borderColor;
    }

    /****************************************************************************************************
     * Assigns the border color for the row.
     * <p>
     * @param color The color.
     ***************************************************************************************************/
    protected void setBorderColor(Color color) {
        borderColor = color;
    }

    /****************************************************************************************************
     * Retrieves the table font.
     ***************************************************************************************************/
    protected Font getFont() {
        return tableFont;
    }

    /****************************************************************************************************
     * Assigns the table font. This will be assigned to all editors within the table.
     * <p>
     * @param font The font.
     ***************************************************************************************************/
    protected void setFont(Font font) {
        tableFont = font;
    }

    /****************************************************************************************************
     * Retrieves the background color of the headers.
     ***************************************************************************************************/
    protected Color getHeaderBackgroundColor() {
        return headerBackgroundColor;
    }

    /****************************************************************************************************
     * Assigns the background color of the headers.
     * <p>
     * @param color The color.
     ***************************************************************************************************/
    protected void setHeaderBackgroundColor(Color color) {
        headerBackgroundColor = color;
    }

    /****************************************************************************************************
     * Retrieves the foreground color of the headers.
     ***************************************************************************************************/
    protected Color getHeaderForegroundColor() {
        return headerForegroundColor;
    }

    /****************************************************************************************************
     * Assigns the foreground color of the headers.
     * <p>
     * @param color The color.
     ***************************************************************************************************/
    protected void setHeaderForegroundColor(Color color) {
        headerForegroundColor = color;
    }

    /****************************************************************************************************
     * Retrieves the font of the headers.
     ***************************************************************************************************/
    protected Font getHeaderFont() {
        return headerFont;
    }

    /****************************************************************************************************
     * Assigns the font of the headers.
     * <p>
     * @param font The font.
     ***************************************************************************************************/
    protected void setHeaderFont(Font font) {
        headerFont = font;
    }

    /****************************************************************************************************
     * Retrieves the border of the headers.
     ***************************************************************************************************/
    protected Border getHeaderBorder() {
        return headerBorder;
    }

    /****************************************************************************************************
     * Assigns the border of the headers.
     * <p>
     * @param border The border.
     ***************************************************************************************************/
    protected void setHeaderBorder(Border border) {
        headerBorder = border;
    }

    /****************************************************************************************************
     * Retrieves the configuration column (which is always displayed as the last column in the table).
     ***************************************************************************************************/
    protected REntryColumn getConfigurationColumn() {
        REntryColumn column = new REntryColumn();
        column.setTitle("");
        column.setAttribute("");
        column.setRequired(true);
        return column;
    }

    /****************************************************************************************************
     * Assigns the row height of each row in the table.
     * <p>
     * @param height The height in pixels.
     ***************************************************************************************************/
    protected void setRowHeight(int height) {
        rowHeight = height;
    }

    /****************************************************************************************************
     * Retrieves the row height assigned to the table.
     ***************************************************************************************************/
    protected int getRowHeight() {
        return rowHeight;
    }

    /****************************************************************************************************
     * Assigns the gap between columns in the table.
     * <p>
     * @param gap The gap (in pixels) to place between each column.
     ***************************************************************************************************/
    protected void setColumnGap(int gap) {
        if (gap > -1) {
            columnGap = gap;
        } else {
            columnGap = 2;
        }
    }

    /****************************************************************************************************
     * Retrieves the gap between columns in the table.
     ***************************************************************************************************/
    protected int getColumnGap() {
        return columnGap;
    }

    /****************************************************************************************************
     * Assigns the data class that each row of the table contains.
     ***************************************************************************************************/
    public void setDataClass(Class dataClass) {
        dataWrapper = new ReflectionWrapper(dataClass);
    }

    /****************************************************************************************************
     * Retrieves the data class that each row of the table contains.
     ***************************************************************************************************/
    public Class getDataClass() {
        return dataWrapper.getDataClass();
    }

    /****************************************************************************************************
     * Retrieves the ReflectionWrapper class responsible for getting and setting attributes on the data
     * object.
     ***************************************************************************************************/
    public ReflectionWrapper getDataWrapper() {
        return dataWrapper;
    }

    /****************************************************************************************************
     * Builds all the columns of the table. The input attributes are taken and converted into columns
     * which are only accessible by the table.
     * <p>
     * @param attributes The attributes defined for the table.
     ***************************************************************************************************/
    protected void setColumns(REntryAttribute[] attributes) {
        columns = new REntryColumn[attributes.length];

        for (int i = 0; i < attributes.length; i++) {
            columns[i] = new REntryColumn();
            columns[i].setTitle(attributes[i].getTitle());
            columns[i].setAttribute(attributes[i].getAttribute());
            columns[i].setIdentifier(attributes[i].getIdentifier());
            columns[i].setEditorCreator(attributes[i].getEditorCreator());
            columns[i].setRequired(attributes[i].isRequired());
        }
    }

    /****************************************************************************************************
     * Assigns the columns to the table settings.
     * <p>
     * @param columns The columns.
     ***************************************************************************************************/
    protected void setColumns(REntryColumn[] columns) {
        this.columns = columns;
    }

    /****************************************************************************************************
     * Retrieves the columns.
     ***************************************************************************************************/
    protected REntryColumn[] getColumns() {
        return columns;
    }

    /****************************************************************************************************
     * Retrieve the column for the specified index.
     * <p>
     * @param index The column index.
     * @return The column at that index.
     ***************************************************************************************************/
    protected REntryColumn getColumn(int index) {
        return columns[index];
    }

    /****************************************************************************************************
     * Returns the number of columns in the table.
     ***************************************************************************************************/
    protected int getColumnCount() {
        return columns.length;
    }

    /****************************************************************************************************
     * Retrieves all the visible attributes of the table.
     * <p>
     * @return An array of attributes.
     ***************************************************************************************************/
    protected String[] getVisibleAttributes() {
        List attributes = new ArrayList<>();
        for (REntryColumn column : columns) {
            if (column.isVisible()) {
                attributes.add(column.getAttribute());
            }
        }
        return (String[]) attributes.toArray(new String[attributes.size()]);
    }

    /****************************************************************************************************
     * Assigns the widths of all the columns in the table.
     * <p>
     * @param widths An array of integers in the correct sequence (containing the widths in pixels).
     ***************************************************************************************************/
    protected void setColumnWidths(int[] widths) {
        for (int i = 0; i < columns.length; i++) {
            setColumnWidth(i, widths[i]);
        }
    }

    /****************************************************************************************************
     * Assigns the width of a specific column within the table.
     * <p>
     * @param colum The column index.
     * @param width The width (in pixels).
     ***************************************************************************************************/
    protected void setColumnWidth(int column, int width) {
        validateColumn(column);
        columns[column].setWidth(width);
    }

    /****************************************************************************************************
     * Assigns the foreground color of a specific column within the table.
     * <p>
     * @param column The column index.
     * @param foreground The color.
     ***************************************************************************************************/
    protected void setColumnForeground(int column, Color foreground) {
        validateColumn(column);
        Color oldColor = columns[column].getForeground();
        if (!oldColor.equals(foreground)) {
            columns[column].setForeground(foreground);
            firePropertyChange(UIPropertyName.ENTRY_TABLE_REPAINT, oldColor, foreground);
        }
    }

    /****************************************************************************************************
     * Assigns the background color of a specific column within the table.
     * <p>
     *
     * @param column The column index.
     * @param foreground The color.
     ***************************************************************************************************/
    protected void setColumnBackground(int column, Color background) {
        validateColumn(column);
        Color oldColor = columns[column].getBackground();
        if (!oldColor.equals(background)) {
            columns[column].setBackground(background);
            firePropertyChange(UIPropertyName.ENTRY_TABLE_REPAINT, oldColor, background);
        }
    }

    /****************************************************************************************************
     * Assigns the font of a specific column within the table.
     * <p>
     * @param column The column index.
     * @param font The font.
     ***************************************************************************************************/
    protected void setColumnFont(int column, Font font) {
        validateColumn(column);
        Font oldFont = columns[column].getFont();
        if (!oldFont.equals(font)) {
            columns[column].setFont(font);
            firePropertyChange(UIPropertyName.ENTRY_TABLE_REPAINT, oldFont, font);
        }
    }

    protected void setHeaderColumnWidths(int[] columnWidths) {
        this.columnWidths = columnWidths;
    }

    public int getHeaderColumnWidth(int i) {
        if (columnWidths == null) {
            return -1;
        }
        validateColumn(i);
        return columnWidths[i];
    }

    /****************************************************************************************************
     * Validates that the column index actually exists within the columns
     * <p>
     * @param column The column index.
     ***************************************************************************************************/
    private void validateColumn(int column) {
        int count = columns.length;
        if (column < 0 || column >= count) {
            throw new IllegalArgumentException("Invalid Column Index or Header");
        }
    }

    /****************************************************************************************************
     * This fires a property change notice for the table traits.
     * <p>
     * @param propertyName The property name.
     ***************************************************************************************************/
    private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyNotifier.firePropertyChange(propertyName, oldValue, newValue);
    }
}
