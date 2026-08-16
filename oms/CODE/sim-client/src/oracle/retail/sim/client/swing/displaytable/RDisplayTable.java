package oracle.retail.sim.client.swing.displaytable;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.RDisplayTableTransferHandler;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.event.RTableCellEvent;
import oracle.retail.sim.client.swing.event.RTableCellListener;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.tableconfig.RTableConfigConstants;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.client.swing.util.UIProblem;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.format.Mask;
import oracle.retail.sim.common.logging.LogService;

/********************************************************************************************************
 * This class subclasses the standard JTable class in the Swing package to provide custom functionality
 * for the RCOM client application. This functionality includes allowing the tab key to exit a table
 * instead of going between cells. Each cell of a RDisplayTable must contain a RDisplayTableCell. This
 * table is only capable of displaying and the cells can never be editable.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RDisplayTable extends JTable implements KeyListener, MouseListener, MouseMotionListener, PropertyChangeListener, RTableHeaderInterface, RTableColumnInterface {
    private static final long serialVersionUID = 3511451521533366309L;

    public static final String TABLE_SORTING = "Sorting table...";
    public static final String TABLE_SORTED = "Table is sorted.";

    public static final int NO_ROWS = 0;
    public static final int SINGLE_ROW = 1;
    public static final int MULTIPLE_ROWS = 2;

    private static final boolean ASCENDING = true;

    private RDisplayTablePopupMenu popupMenu;
    private RDisplayTableConfigDialog configDialog;
    private String identifier = StringConstants.EMPTY;

    private TableRowSorter tableSortTool = new TableRowSorter();
    private TableRowDisplayer tableRowDisplayer;
    private String displayerKeyColumn;

    private String[] columnHeaderArray = new String[0];

    private int[] lockedSortColumnArray = new int[0];
    private boolean[] lockedSortOrderArray = new boolean[0];

    private int[] userSortColumnArray = new int[0];
    private boolean[] userSortOrderArray = new boolean[0];

    private int[] defaultSortColumnArray = new int[0];
    private boolean[] defaultSortOrderArray = new boolean[0];

    private int[] columnTypeArray = new int[0];
    private Color[] columnBackgroundArray = new Color[0];
    private Color[] columnForegroundArray = new Color[0];

    private REventListener singleActionListener;
    private REventListener doubleActionListener;
    private RTableCellListener tableCellActionListener;

    private String singleActionCommand;
    private String doubleActionCommand;
    private String tableCellActionCommand;

    private MouseEvent storeMouseEvent;

    private Map problemMap = new HashMap();

    private Color tableHeaderBackground = Color.WHITE;
    private Color tableHeaderForeground = Color.BLACK;
    private Color defaultRowForeground = Color.BLACK;
    private Color defaultRowBackground = Color.WHITE;
    private Color alternateRowBackground = Color.BLACK;
    private Color errorStateBackground = Color.RED;
    private Color errorStateForeground = Color.WHITE;

    private Font tableHeaderFont = new Font("Verdana", Font.BOLD, 10);

    // private Map columnIconMap = new HashMap();

    private int totalAvailableWidth;
    private int totalHeaderWidth;
    private int tableSortLimit = 1000;

    private boolean columnSortingEnabled = true;
    private boolean configurationEnabled;
    private boolean isHyperlinkCursor;
    private boolean resettingTable;

    /****************************************************************************************************
     * Returns new RDisplayTable object.
     ***************************************************************************************************/
    public RDisplayTable(String identifier) {
        setModel(new RDisplayTableModel(new String[0], 0));
        setIdentifier(identifier);
        getTableHeader().addMouseListener(this);
        setColumnSelectionAllowed(false);
        setRowSelectionMode(SINGLE_ROW);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setTransferHandler(new RDisplayTableTransferHandler());
        validateGridLines(RTableConfigConstants.COL_GRIDLINES);
        initializeUIDefaults();
    }

    /****************************************************************************************************
     * Initialize UI defaults.
     ***************************************************************************************************/
    private void initializeUIDefaults() {
        try {
            tableSortLimit = Integer.parseInt(UIManager.getString(UIThemeName.RDISPLAYTABLE_SORT_LIMIT));
        } catch (Throwable exception) {
        }
        setTableHeaderBackground(UIManager.getColor(UIThemeName.RDISPLAYTABLE_HEADER_BACKGROUND));
        setTableHeaderForeground(UIManager.getColor(UIThemeName.RDISPLAYTABLE_HEADER_FOREGROUND));
        setTableHeaderFont(UIManager.getFont(UIThemeName.RDISPLAYTABLE_HEADER_FONT));
        setRowBackground(UIManager.getColor(UIThemeName.RDISPLAYTABLE_DEFAULT_BACKGROUND));
        setRowForeground(UIManager.getColor(UIThemeName.RDISPLAYTABLE_DEFAULT_FOREGROUND));
        setAlternateRowBackground(UIManager.getColor(UIThemeName.RDISPLAYTABLE_ALTERNATE_BACKGROUND));
        setErrorBackground(UIManager.getColor(UIThemeName.RDISPLAYTABLE_ERROR_BACKGROUND));
        setErrorForeground(UIManager.getColor(UIThemeName.RDISPLAYTABLE_ERROR_FOREGROUND));
    }

    /****************************************************************************************************
     * Retrieves the RDisplayTableModel from the table.
     * <p>
     * @return The RDisplayTableModel assigned to the table.
     ***************************************************************************************************/
    public RDisplayTableModel getTableModel() {
        return (RDisplayTableModel) getModel();
    }

    /****************************************************************************************************
     * Assigns an identifier to the display table. This identifier is used to find table configuration
     * information. It cannot be null or empty and must be set before the table configuration will work.
     * <p>
     * @param identifier The identifier to assign to the table.
     ***************************************************************************************************/
    public void setIdentifier(String identifier) {
        if (StringUtility.isNullOrEmpty(identifier)) {
            throw new IllegalArgumentException("RDisplayTable identifier cannot be null or empty!");
        }
        this.identifier = identifier;
    }

    /****************************************************************************************************
     * Retrieves the identifier of the display table.
     * <p>
     * @return The identifier.
     ***************************************************************************************************/
    public String getIdentifier() {
        return identifier;
    }

    /****************************************************************************************************
     * Assigns the column headers to the table. This method is required if the table is going to be
     * useful.
     ***************************************************************************************************/
    public void setColumnHeaders(String[] columnHeaders) {
        if (columnHeaders == null || columnHeaders.length == 0) {
            throw new IllegalArgumentException("Cannot set column headers to null or empty.");
        }
        setModel(new RDisplayTableModel(columnHeaders, 0));
        buildColumns(columnHeaders);
        columnHeaderArray = columnHeaders;
    }

    /****************************************************************************************************
     * Sets the table row selection mode, which determines the number of rows that can be selected. Valid
     * selection modes are: NO_ROWS, SINGLE_ROW, and MULTI_ROWS. The default mode is SINGLE_ROW.
     * <p>
     * @param mode The selection mode to assign to the table.
     ***************************************************************************************************/
    public void setRowSelectionMode(int mode) {
        if (mode == NO_ROWS) {
            setRowSelectionAllowed(false);
        } else {
            setRowSelectionAllowed(true);
        }
        if (mode == SINGLE_ROW) {
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        } else if (mode == MULTIPLE_ROWS) {
            setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        }
    }

    /****************************************************************************************************
     * Sets whether or not column re-ordering is allowed. This determines whether or not the user can
     * drag the columns around the table, thus placing them in a different order.
     * <p>
     * @param allowed True if columns should allow re-ordering, false if they should not.
     ***************************************************************************************************/
    public void setColumnReorderingAllowed(boolean allowed) {
        getTableHeader().setReorderingAllowed(allowed);
    }

    /****************************************************************************************************
     * Sets the available width of the table. If this value is greater than zero, then the table will
     * calculate the size of the header and place a scrollbar on the table if it exceeds the available
     * space.
     * <p>
     * @param width The available width of the table in pixels.
     ***************************************************************************************************/
    public void setAvailableWidth(int width) {
        if (width > 0) {
            totalAvailableWidth = width - UIManager.getInt(UIThemeName.SCROLLBAR_WIDTH);
        }
    }

    /****************************************************************************************************
     * Set whether or not column sorting is active. Column sorting is enabled by default.
     * <p>
     * @param enable True if sorting should be enabled, false otherwise.
     ***************************************************************************************************/
    public void setColumnSortingEnabled(boolean enabled) {
        columnSortingEnabled = enabled;
    }

    /****************************************************************************************************
     * Retrieves whether or not column sorting is active.
     ***************************************************************************************************/
    public boolean isColumnSortingEnabled() {
        return columnSortingEnabled;
    }

    /****************************************************************************************************
     * Assigns whether or not the configuration dialog/menu on the table should be active.
     * <p>
     * @param enable True if the configuration dialog should be enabled, false otherwise.
     ***************************************************************************************************/
    public void setConfigurationEnabled(boolean enabled) {
        configurationEnabled = enabled;
    }

    /****************************************************************************************************
     * Retrieves whether or not the configuration dialog/menu on the table should be active.
     ***************************************************************************************************/
    public boolean isConfigurationEnabled() {
        return configurationEnabled;
    }

    /****************************************************************************************************
     * Assigns a table row displayer to this table. This method will override the table model and column
     * definitions, replacing their values with those found inside the displayer. Note: Currently, if
     * values are already assigned to the table, this will not refresh. Functionality should be added in
     * the future.
     * <p>
     * @return The TableRowDisplayer assigned to the table.
     ***************************************************************************************************/
    public void setTableRowDisplayer(TableRowDisplayer displayer) {
        tableRowDisplayer = displayer;

        if (tableRowDisplayer != null) {
            String[] headers = tableRowDisplayer.getHeaders();
            int[] types = new int[headers.length];
            for (int i = 0; i < types.length; i++) {
                types[i] = DataTypeConstants.TEXT;
            }
            try {
                int[] columnTypes = tableRowDisplayer.getColumnTypes();
                if (columnTypes != null) {
                    types = columnTypes;
                }
            } catch (Throwable exception) {
                displayInnerException(exception);
            }
            int[] sizes = tableRowDisplayer.getColumnSizes();

            if (headers.length == 0) {
                throw new IllegalArgumentException("SelectableTableRowDisplayer must contain headers!");
            }
            displayerKeyColumn = headers[0];

            setColumnHeaders(headers);

            for (int i = 0; i < headers.length; i++) {
                buildColumn(headers[i], types[i], sizes[i], sizes[i], sizes[i]);
            }
        }
    }

    /****************************************************************************************************
     * Sets the order of columns sorted within the table.
     * <p>
     * @param columnHeaders An array of column headers to assign as the default sort order.
     ***************************************************************************************************/
    public void setColumnSortOrder(String[] columnHeaders) {
        defaultSortColumnArray = new int[0];
        defaultSortOrderArray = new boolean[0];
        try {
            for (String columnHeader : columnHeaders) {
                int column = getColumnModelIndex(columnHeader);
                defaultSortColumnArray = addToIntArray(defaultSortColumnArray, column);
                defaultSortOrderArray = addToBooleanArray(defaultSortOrderArray, ASCENDING);
            }
        } catch (Throwable t) {
            throw new IllegalArgumentException("Invalid column name in sort order!");
        }
        refreshTableHeader();
    }

    /****************************************************************************************************
     * Sets the sort order of a specific column in the table.
     * <p>
     * @param columnName The column name.
     * @param isAscending True if ascending is desired, false if not.
     ***************************************************************************************************/
    public void setColumnSortDirection(String columnName, boolean isAscending) {
        setColumnSortDirectionByIndex(getColumnModelIndex(columnName), isAscending);
    }

    /****************************************************************************************************
     * Sets the sort order of a specific column in the table.
     * <p>
     * @param column The column index.
     * @param isAscending True if ascending is desired, false if not.
     ***************************************************************************************************/
    private void setColumnSortDirectionByIndex(int column, boolean isAscending) {
        for (int i = 0; i < defaultSortColumnArray.length; i++) {
            if (defaultSortColumnArray[i] == column) {
                defaultSortOrderArray[i] = isAscending;
            }
        }
        refreshTableHeader();
    }

    /****************************************************************************************************
     * Assigns a table cell action listener to the table. If this is assigned and a cell is clicked that
     * produces an action (such as a hyperlink), an action is sent to the table cell listener.
     * <p>
     * @param listener The listener to assign to the table to receive table cell actions.
     * @param command The command to send in the action event.
     ***************************************************************************************************/
    public void registerTableCellAction(RTableCellListener listener, String command) {
        tableCellActionListener = listener;
        tableCellActionCommand = command;
    }

    /****************************************************************************************************
     * Removes the table cell action listener from the table.
     ***************************************************************************************************/
    public void removeTableCellAction() {
        tableCellActionListener = null;
        tableCellActionCommand = null;
    }

    /****************************************************************************************************
     * Assigns an action to be sent when a row is selected through key navigation or a single click. The
     * command will be sent to the listener.
     * <p>
     * @param listener The listener to assign to the table to receive single click actions.
     * @param command The command to send in the action event.
     ***************************************************************************************************/
    public void registerSingleClickAction(REventListener listener, String command) {
        singleActionListener = listener;
        singleActionCommand = command;
    }

    /****************************************************************************************************
     * Removes the single click action event from the table. No action will be sent for row selections.
     ***************************************************************************************************/
    public void removeSingleClickAction() {
        singleActionListener = null;
        singleActionCommand = null;
    }

    /****************************************************************************************************
     * Assigns an action to be sent when a row is double-clicked or a row is selected and the enter key
     * is pressed. The command will be sent to the listener inside an RActionEvent.
     * <p>
     * @param listener The listener to assign to the table to receive single click actions.
     * @param command The command to send in the action event.
     ***************************************************************************************************/
    public void registerDoubleClickAction(REventListener listener, String command) {
        doubleActionListener = listener;
        doubleActionCommand = command;
    }

    /****************************************************************************************************
     * Removes the double click action event from the table. No action will be sent when double-clicking
     * on a row.
     ***************************************************************************************************/
    public void removeDoubleClickAction() {
        doubleActionListener = null;
        doubleActionCommand = null;
    }

    /****************************************************************************************************
     * Builds all the columns indicated by the column headers as text cells that should expand to consume
     * available space.
     * <p>
     * @param headers A string array of column header text.
     ***************************************************************************************************/
    private void buildColumns(String[] columnArray) {
        columnTypeArray = new int[columnArray.length];
        columnBackgroundArray = new Color[columnArray.length];
        columnForegroundArray = new Color[columnArray.length];

        for (String column : columnArray) {
            buildColumn(column, DataTypeConstants.TEXT, -1, -1);
        }
    }

    /****************************************************************************************************
     * Builds a table column.
     * <p>
     * @param columnName The column header name.
     * @param columnType The column type to assign to cells in the column.
     * @param minSize Minimum width in pixels the column should allow.
     * @param maxSize Maximum width in pixels the column should allow.
     ***************************************************************************************************/
    public void buildColumn(String name, int type, int minSize, int maxSize) {
        buildColumn(name, type, minSize, -1, maxSize);
    }

    /****************************************************************************************************
     * Builds a table column. In the case of a column without a header title to display, the column is
     * assigned its index internally for reference. No header title will be displayed.
     * <p>
     * For sizes: -1 means do not set this value, 0 means set to the size of the column headers, and a
     * positive number means set to that value.
     * <p>
     * @param columnName The name of the column.
     * @param columnType The column type to assign to cells in the column.
     * @param minSize Minimum width in pixels the column should allow.
     * @param prefSize Preferred width in pixels the column should have upon creation.
     * @param maxSize Maximum width in pixels the column should allow.
     ***************************************************************************************************/
    public void buildColumn(String name, int type, int minSize, int prefSize, int maxSize) {
        RTableHeader headerRenderer = new RTableHeader();
        DefaultTableCellRenderer cellRenderer = new RTableCellRenderer();

        try {
            TableColumn column = getColumn(name);

            if (minSize == 0) {
                FontMetrics metrics = headerRenderer.getFontMetrics(headerRenderer.getFont());
                minSize = StringUtility.longestSize(metrics, name, "\\|") + 25;
            }
            if (minSize != -1) {
                column.setMinWidth(minSize);
                validateHeaderWidth(minSize);
            }

            if (maxSize == 0 && minSize > -1) {
                column.setMaxWidth(minSize);
            } else if (maxSize > 0) {
                column.setMaxWidth(maxSize);
            }

            if (prefSize > 0) {
                column.setPreferredWidth(prefSize);
            }

            columnTypeArray[column.getModelIndex()] = type;

            column.setHeaderRenderer(headerRenderer);
            column.setCellRenderer(cellRenderer);
        } catch (Exception exception) {
            throw new IllegalArgumentException("An invalid column name was used.");
        }
    }

    /****************************************************************************************************
     * Validates the width of the header versus the available width and assigns a scrollbar to the table
     * if necessary.
     * <p>
     * @param width Column width to add to the total header size.
     ***************************************************************************************************/
    private void validateHeaderWidth(int columnWidth) {
        totalHeaderWidth += columnWidth;

        if (totalAvailableWidth > 0 && totalHeaderWidth > totalAvailableWidth) {
            setAutoResizeMode(AUTO_RESIZE_OFF);
        }
    }

    /****************************************************************************************************
     * Assigns a formatted mask to all table cells within a column.
     * <p>
     * @param columnName The name of the column to assign the mask to.
     * @param formatMask The format mask to assign to the column.
     ***************************************************************************************************/
    public void setMask(String columnName, Mask formatMask) {
        try {
            TableColumn tableColumn = getColumn(columnName);
            Object renderer = tableColumn.getCellRenderer();
            if (renderer instanceof RTableCellRenderer) {
                ((RTableCellRenderer) renderer).setMask(formatMask);
            }
        } catch (Exception exception) {
            throw new IllegalArgumentException("An invalid column name was used.");
        }
    }

    /****************************************************************************************************
     * Assigns an icon to display in a particular cell if the cell value is true within that column
     * <p>
     * @param columnName The name of the column.
     * @param icon The icon to assign to the column.
     ***************************************************************************************************/
    public void setColumnIcon(String columnName, Icon icon) {
        try {
            TableColumn tableColumn = getColumn(columnName);
            Object renderer = tableColumn.getCellRenderer();
            if (renderer instanceof RTableCellRenderer) {
                ((RTableCellRenderer) renderer).setColumnIcon(icon);
            }
        } catch (Exception exception) {
            throw new IllegalArgumentException("An invalid column name was used.");
        }
    }

    /****************************************************************************************************
     * Assigns the header area background color.
     * <p>
     * @param color The color to assign.
     ***************************************************************************************************/
    public void setTableHeaderBackground(Color color) {
        if (color != null) {
            tableHeaderBackground = color;
        }
    }

    /****************************************************************************************************
     * Retrieves the header area background color.
     * <p>
     * @return The header area background color.
     ***************************************************************************************************/
    public Color getTableHeaderBackground() {
        return tableHeaderBackground;
    }

    /****************************************************************************************************
     * Assigns the header area foreground color.
     * <p>
     * @param color The color to assign.
     ***************************************************************************************************/
    public void setTableHeaderForeground(Color color) {
        if (color != null) {
            tableHeaderForeground = color;
        }
    }

    /****************************************************************************************************
     * Retrieves the header area foreground color.
     * <p>
     * @return The header area foreground color.
     ***************************************************************************************************/
    public Color getTableHeaderForeground() {
        return tableHeaderForeground;
    }

    /****************************************************************************************************
     * Assigns the header area font.
     * <p>
     * @param font The font to assign
     ***************************************************************************************************/
    public void setTableHeaderFont(Font font) {
        if (font != null) {
            tableHeaderFont = font;
        }
    }

    /****************************************************************************************************
     * Retrieves the header area font.
     * <p>
     * @return The header area font.
     ***************************************************************************************************/
    public Font getTableHeaderFont() {
        return tableHeaderFont;
    }

    /****************************************************************************************************
     * Assigns the row background color.
     * <p>
     * @param color The row background color.
     ***************************************************************************************************/
    public void setRowBackground(Color color) {
        if (color != null) {
            defaultRowBackground = color;
        }
    }

    /****************************************************************************************************
     * Retrieves the row background color.
     * <p>
     * @return The row background color.
     ***************************************************************************************************/
    public Color getRowBackground() {
        return defaultRowBackground;
    }

    /****************************************************************************************************
     * Assigns the row foreground color.
     * <p>
     * @param color The row foreground color.
     ***************************************************************************************************/
    public void setRowForeground(Color color) {
        if (color != null) {
            defaultRowForeground = color;
        }
    }

    /****************************************************************************************************
     * Retrieves the row foreground color.
     * <p>
     * @return The row foreground color.
     ***************************************************************************************************/
    public Color getRowForeground() {
        return defaultRowForeground;
    }

    /****************************************************************************************************
     * Assigns the row alternate background color.
     * <p>
     * @param color The row alternate background color.
     ***************************************************************************************************/
    public void setAlternateRowBackground(Color color) {
        if (color != null) {
            alternateRowBackground = color;
        }
    }

    /****************************************************************************************************
     * Retrieves the row alternate background color.
     * <p>
     * @return The row alternate background color.
     ***************************************************************************************************/
    public Color getAlternateRowBackground() {
        return alternateRowBackground;
    }

    /****************************************************************************************************
     * Assigns the error state background color. This will not change the color of rows already in error.
     * <p>
     * @param color The error state background color.
     ***************************************************************************************************/
    public void setErrorBackground(Color color) {
        if (color != null) {
            errorStateBackground = color;
        }
    }

    /****************************************************************************************************
     * Retrieves the error state background color.
     * <p>
     * @return The error state background color.
     ***************************************************************************************************/
    public Color getErrorStateBackground() {
        return errorStateBackground;
    }

    /****************************************************************************************************
     * Assigns the error state foreground color. This will not change the color of rows already in error.
     * <p>
     * @param color The error state foreground color.
     ***************************************************************************************************/
    public void setErrorForeground(Color color) {
        if (color != null) {
            errorStateForeground = color;
        }
    }

    /****************************************************************************************************
     * Retrieves the error state foreground color.
     * <p>
     * @return The error state foreground color.
     ***************************************************************************************************/
    public Color getErrorStateForeground() {
        return errorStateForeground;
    }

    // /******************************************************************************************
    // * Retrieves the specific column default background color.
    // * <p>
    // * @param columnName The name of the color to retrieve the background color for.
    // * @return The specific column default background color.
    // *****************************************************************************************/
    // private Color getColumnBackground(String columnName) {
    // int column = getColumnModelIndex(columnName);
    // if (columnBackgroundArray[column] != null) {
    // return columnBackgroundArray[column];
    // }
    // return null;
    // }
    //
    // /******************************************************************************************
    // * Retrieves the specific column default foreground color.
    // * <p>
    // * @param columnName The name of the color to retrieve the foreground color for.
    // * @return The specific column default foreground color.
    // *****************************************************************************************/
    // private Color getColumnForeground(String columnName) {
    // int column = getColumnModelIndex(columnName);
    // if (columnForegroundArray[column] != null) {
    // return columnForegroundArray[column];
    // }
    // return null;
    // }

    /****************************************************************************************************
     * Retrieves the specific column default background color.
     * <p>
     * @param column The column index to retrieve the color at.
     * @return The specific column default background color.
     ***************************************************************************************************/
    public Color getColumnBackground(int row, int column) {
        RDisplayTableCell tableCell = (RDisplayTableCell) getValueAt(row, column);
        if (tableCell != null && tableCell.isErrorState()) {
            return errorStateBackground;
        }
        if (columnBackgroundArray[column] != null) {
            return columnBackgroundArray[column];
        }
        if (Math.IEEEremainder(row, 2d) == 0d) {
            return defaultRowBackground;
        }
        return alternateRowBackground;
    }

    /****************************************************************************************************
     * Retrieves the specific column default foreground color.
     * <p>
     * @param column The model column index to retrieve the color at.
     * @return The specific column default foreground color.
     ***************************************************************************************************/
    public Color getColumnForeground(int row, int column) {
        RDisplayTableCell tableCell = (RDisplayTableCell) getValueAt(row, column);
        if (tableCell != null && tableCell.isErrorState()) {
            return errorStateForeground;
        }
        if (columnForegroundArray[column] != null) {
            return columnForegroundArray[column];
        }
        return defaultRowForeground;
    }

    /****************************************************************************************************
     * Assigns a background color to a particular column.
     * <p>
     * @param columnName The name of the column.
     * @param color The color to assign to the column.
     ***************************************************************************************************/
    public void setColumnBackgroundColor(String columnName, Color color) {
        try {
            int index = getColumnModelIndex(columnName);
            columnBackgroundArray[index] = color;
            invalidate();
            validate();
            repaint();
        } catch (Exception exception) {
            throw new IllegalArgumentException("An invalid column name was used.");
        }
    }

    /****************************************************************************************************
     * Assigns a foreground color to a particular column.
     * <p>
     * @param columnName The name of the column.
     * @param color The color to assign to the column.
     ***************************************************************************************************/
    public void setColumnForegroundColor(String columnName, Color color) {
        try {
            int index = getColumnModelIndex(columnName);
            columnForegroundArray[index] = color;
            invalidate();
            validate();
            repaint();
        } catch (Exception exception) {
            throw new IllegalArgumentException("An invalid column name was used.");
        }
    }

    /****************************************************************************************************
     * Retrieves the value within a cell as a string.
     * <p>
     * @param row The row index of the cell.
     * @param column The column model index of the cell.
     * @return The value contained within the cell at the given row and column.
     * @exception IllegalStateException Thrown if the cell does not exists or does not contain an
     *                RDisplayTableCell object.
     ***************************************************************************************************/
    private String getCellValueAt(int row, int column) {
        Object object = getValueAt(row, column);
        try {
            return ((RDisplayTableCell) object).getValue();
        } catch (Exception exception) {
            throw new IllegalStateException("Row " + row + " or Column " + column + " do not exist");
        }
    }

    /****************************************************************************************************
     * Retrieves the value within a cell as a string.
     * <p>
     * @param row The row index of the cell.
     * @param columnName The column name of the cell.
     * @return The value contained within the cell at the given row and column.
     * @exception IllegalStateException Thrown if the cell does not exists or does not contain an
     *                RDisplayTableCell object.
     ***************************************************************************************************/
    public String getCellValueAt(int row, String columnName) {
        Object object = getValueAt(row, getColumnViewIndex(columnName));
        try {
            return ((RDisplayTableCell) object).getValue();
        } catch (Exception exception) {
            throw new IllegalStateException("Row " + row + " or Column " + columnName + " do not exist");
        }
    }

    /****************************************************************************************************
     * Retrieves the row number for a value searching the specified column.
     * <p>
     * @param columnName The column name to retrieve selections from.
     * @param value The value to match in the column.
     * @return The row number corresponding to the column and value.
     ***************************************************************************************************/
    public int getRowNumber(String columnName, String value) {
        int rows = getRowCount();
        int index = getColumnViewIndex(columnName);
        String testValue;

        for (int i = 0; i < rows; i++) {
            testValue = getCellValueAt(i, index);
            if (testValue.equals(value)) {
                return i;
            }
        }
        return -1;
    }

    /****************************************************************************************************
     * Retrieves the row number for a value searching the specified column.
     * <p>
     * @param columnName The column name to retrieve selections from.
     * @param object An object to find within the column.
     * @return The row number corresponding to the column and value.
     ***************************************************************************************************/
    public int getRowNumber(String columnName, Object object) {
        return getRowNumber(getColumnViewIndex(columnName), object);
    }

    /****************************************************************************************************
     * Retrieves the row number for a value searching the specified column.
     * <p>
     * @param index The column index to retrieve selections from.
     * @param object An object to find within the column.
     * @return The row number corresponding to the column and value.
     ***************************************************************************************************/
    private int getRowNumber(int index, Object object) {
        if (object == null) {
            return -1;
        }
        int rows = getRowCount();
        for (int i = 0; i < rows; i++) {
            Object testObject = getData(i, index);
            if (testObject != null && testObject.equals(object)) {
                return i;
            }
        }
        return -1;
    }

    /****************************************************************************************************
     * Retrieves selected column data for a column name. This retrieves all the values in a a column
     * identified by columnName that have their rows selected.
     * <p>
     * @param columnName The column name to retrieve selections from.
     * @return A string array containing the selected column data.
     ***************************************************************************************************/
    public String[] getSelectedColumnData(String columnName) {
        String[] columnData = new String[getSelectedRowCount()];
        try {
            int[] rows = getSelectedRows();
            int index = getColumnViewIndex(columnName);

            for (int i = 0; i < rows.length; i++) {
                columnData[i] = getCellValueAt(rows[i], index);
            }
        } catch (Exception exception) {
            throw new IllegalArgumentException("Column name " + columnName + " does not exist!");
        }
        return columnData;
    }

    /****************************************************************************************************
     * Retrieves all the data objects matching the value against the filter column.
     * <p>
     * @param filterColumn The name of the column to filter on.
     * @param value The value to filter on.
     ***************************************************************************************************/
    public List getData(String filterColumn, String value) {
        List list = new ArrayList();
        int count = getRowCount();
        int column = convertColumnIndexToView(0);
        int filter = getColumnViewIndex(filterColumn);
        String[] row;
        for (int i = 0; i < count; i++) {
            row = getRowDisplayData(i);
            if (row[filter].equals(value)) {
                list.add(getData(i, column));
            }
        }
        return list;
    }

    /****************************************************************************************************
     * Retrieves all the objects stored within the key column matching the value against the filter
     * column.
     * <p>
     * @param keyColumn The name of the column to retrieve the data from.
     * @param filterColumn The name of the column to filter on.
     * @param value The value to filter on.
     ***************************************************************************************************/
    public List getData(String keyColumn, String filterColumn, String value) {
        List list = new ArrayList();
        int count = getRowCount();
        int column = getColumnViewIndex(keyColumn);
        int filter = getColumnViewIndex(filterColumn);
        String[] row;
        for (int i = 0; i < count; i++) {
            row = getRowDisplayData(i);
            if (row[filter].equals(value)) {
                list.add(getData(i, column));
            }
        }
        return list;
    }

    /****************************************************************************************************
     * Retrieves all the objects stored within the given column for the entire table. This returns an
     * empty list if the table is empty.
     * <p>
     * @return A list of data objects stored within the table.
     ***************************************************************************************************/
    public List getAllData() {
        return getAllData(convertColumnIndexToView(0));
    }

    /****************************************************************************************************
     * Retrieves all the objects stored within the given column for the entire table. This returns an
     * empty list if the table is empty.
     * <p>
     * @param columnName The column name to retrieve the data object from.
     * @return A list of data objects stored within the table.
     ***************************************************************************************************/
    public List getAllData(String columnName) {
        return getAllData(getColumnViewIndex(columnName));
    }

    /****************************************************************************************************
     * Retrieves all the objects stored within the given column for the entire table. This returns an
     * empty list if the table is empty.
     * <p>
     * @param column The column index to retrieve the data object from.
     * @return A list of data objects stored within the table.
     ***************************************************************************************************/
    private List getAllData(int column) {
        List list = new ArrayList();
        int count = getRowCount();
        for (int i = 0; i < count; i++) {
            list.add(getData(i, column));
        }
        return list;
    }

    /****************************************************************************************************
     * Retrieves all the selected objects stored within the given column. This returns an empty list if
     * the table is empty.
     * <p>
     * @return A list of selected objects stored within the table.
     ***************************************************************************************************/
    public List getAllSelectedData() {
        return getAllSelectedData(convertColumnIndexToView(0));
    }

    /****************************************************************************************************
     * Retrieves all the selected objects stored within the given column. This returns an empty list if
     * the table is empty.
     * <p>
     * @param columnName The column name to retrieve the data object from.
     * @return A list of selected objects stored within the table.
     ***************************************************************************************************/
    public List getAllSelectedData(String columnName) {
        return getAllSelectedData(getColumnViewIndex(columnName));
    }

    /****************************************************************************************************
     * Retrieves all the selected objects stored within the given column. This returns an empty list if
     * the table is empty.
     * <p>
     * @param columnName The column name to retrieve the data object from.
     * @return A list of selected objects stored within the table.
     ***************************************************************************************************/
    private List getAllSelectedData(int column) {
        List list = new ArrayList();
        int[] rows = getSelectedRows();
        for (int row : rows) {
            list.add(getData(row, column));
        }
        return list;
    }

    /****************************************************************************************************
     * Retrieves the data object stored in a cell for the selected row and the given column name. This
     * returns null if no row is selected. It returns the first row selected data if more than one row is
     * selected.
     * <p>
     * @return An object stored within the table cell.
     ***************************************************************************************************/
    public Object getSelectedData() {
        int row = getSelectedRow();
        if (row > -1) {
            return getData(row, convertColumnIndexToView(0));
        }
        return null;
    }

    /****************************************************************************************************
     * Retrieves the data object stored in a cell for the selected row and the given column name. This
     * returns null if no row is selected. It returns the first row selected data if more than one row is
     * selected.
     * <p>
     * @param columnName The column name to retrieve the data object from.
     * @return An object stored within the table cell.
     ***************************************************************************************************/
    public Object getSelectedData(String columnName) {
        int row = getSelectedRow();
        if (row > -1) {
            return getData(row, getColumnViewIndex(columnName));
        }
        return null;
    }

    /****************************************************************************************************
     * Retrieves the data object stored in a cell for the given row number. This assumes that the data is
     * stored in the first column (the default column).
     * <p>
     * @param row The row number of the cell to retrieve the data object from.
     * @param columnName The column name to retrieve the data object from.
     * @return An object stored within the table cell.
     ***************************************************************************************************/
    public Object getData(int row) {
        return getData(row, convertColumnIndexToView(0));
    }

    /****************************************************************************************************
     * Retrieves the data object stored in a cell for the given row number and column name.
     * <p>
     * @param row The row number of the cell to retrieve the data object from.
     * @param columnName The column name to retrieve the data object from.
     * @return An object stored within the table cell.
     ***************************************************************************************************/
    public Object getData(int row, String columnName) {
        try {
            return getData(row, getColumnViewIndex(columnName));
        } catch (Exception exception) {
            throw new IllegalArgumentException("Column name " + columnName + " does not exist!");
        }
    }

    /****************************************************************************************************
     * Retrieves the data object stored in a cell for the selected row and the given column index.
     * <p>
     * @param row The row number of the cell to retrieve the data object from.
     * @param column The column view index of the cell to retrieve the data object from.
     * @return A data object stored within the table cell.
     ***************************************************************************************************/
    private Object getData(int row, int column) {
        RDisplayTableCell tableCell = (RDisplayTableCell) getValueAt(row, column);
        return tableCell.getData();
    }

    /****************************************************************************************************
     * Retrieves the selected row data. This only works when the table is in single selection mode. If in
     * multiple selection mode, this will return the data of the first selected row, but none of the
     * additional selected rows. This method returns an empty array if no row is selected.
     * <p>
     * @return An array containing the string representations of the row data.
     ***************************************************************************************************/
    public String[] getSelectedRowDisplayData() {
        int row = getSelectedRow();
        if (row > -1) {
            return getRowDisplayData(row);
        }
        return new String[0];
    }

    /****************************************************************************************************
     * Retrieves all the row data as a string array given a row number.
     * <p>
     * @param rowNumber The row number of the row to retrieve.
     * @return An string array containing the string representation of each cell in the row.
     ***************************************************************************************************/
    public String[] getRowDisplayData(int rowNumber) {
        int columnCount = getColumnCount();
        String[] rowData = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            rowData[i] = getCellValueAt(rowNumber, i);
        }
        return rowData;
    }

    /****************************************************************************************************
     * Retrieves the entire row data for a given row number. This means retrieving the RDisplayTableCell
     * object for each cell within the row. If an exception occurs, such as NullPointer or ClassCast,
     * throw an IllegalStateException to indicate that the table has been used improperly.
     * <p>
     * @param row The row index to retrieve data for.
     * @return A RDisplayTableCell array containing the data of each cell in the row.
     ***************************************************************************************************/
    public RDisplayTableCell[] getFullRowDisplayData(int row) {
        try {
            Vector dataVector = getTableModel().getDataVector();
            Vector rowVector = (Vector) dataVector.elementAt(row);
            RDisplayTableCell[] rowData = new RDisplayTableCell[rowVector.size()];

            for (int i = 0; i < rowVector.size(); i++) {
                rowData[i] = (RDisplayTableCell) rowVector.elementAt(i);
            }
            return rowData;
        } catch (Exception exception) {
            throw new IllegalArgumentException("Row index " + row + " was out of range.");
        }
    }

    /****************************************************************************************************
     * Gathers all the data inside the table (without turning it into a string array). This will return a
     * giant list of arrays of RDisplayTableCell objects. Each array represents a row and each
     * RDisplayTableCell represents a single cell.
     * <p>
     * @return An list containing rows of data represented by RDisplayTableCell arrays.
     ***************************************************************************************************/
    public List getFullTableData() {
        List dataList = new ArrayList();
        try {
            Vector tableVector = getTableModel().getDataVector();

            for (int i = 0; i < tableVector.size(); i++) {
                Vector rowVector = (Vector) tableVector.elementAt(i);
                RDisplayTableCell[] rowArray = new RDisplayTableCell[rowVector.size()];

                for (int index = 0; index < rowVector.size(); index++) {
                    rowArray[index] = (RDisplayTableCell) rowVector.elementAt(index);
                }
                dataList.add(rowArray);
            }
        } catch (ClassCastException castException) {
            throw new IllegalStateException("Unable to retrieve full data.");
        }

        return dataList;
    }

    /****************************************************************************************************
     * Assigns a cell type for an entire column. This updates the cell information for each cell within
     * the column (and any future cells in the column).
     * <p>
     * @param columnName The name of the column to update.
     * @param columnType Type the column should be: STRING, DATE or NUMBER.
     ***************************************************************************************************/
    public void setColumnType(String columnName, int columnType) {
        int rowCount = getRowCount();
        int modelColumn = getColumnModelIndex(columnName);
        int viewColumn = getColumnViewIndex(columnName);
        for (int row = 0; row < rowCount; row++) {
            RDisplayTableCell tableCell = (RDisplayTableCell) getValueAt(row, viewColumn);
            tableCell.setType(columnType);
            setValueAt(tableCell, row, viewColumn);
        }
        columnTypeArray[modelColumn] = columnType;
    }

    /****************************************************************************************************
     * Sets the value information for a single cell in the table.
     * <p>
     * @param row The row index to update.
     * @param columnName The name of the column to update.
     * @param value The new value to put in the cell.
     ***************************************************************************************************/
    public void setCellValue(int row, String columnName, String value) {
        try {
            int column = getColumnViewIndex(columnName);
            setCellValue(row, column, value);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Column name " + columnName + " does not exist.");
        }
    }

    /****************************************************************************************************
     * Sets the value information for a single cell in the table.
     * <p>
     * @param row The row index to update.
     * @param column The column view index to update.
     * @param value The new value to put in the cell.
     ***************************************************************************************************/
    private void setCellValue(int row, int column, String value) {
        RDisplayTableCell tableCell = (RDisplayTableCell) getValueAt(row, column);
        tableCell.setValue(value);
        setValueAt(tableCell, row, column);
    }

    /****************************************************************************************************
     * Sets the data to store in the cell. If the column matches the primary row column as defined by the
     * table row displayer, NOTHING will be done by this method. The developer should use updateRow()
     * instead.
     * <p>
     * @param row The row index to store the data in.
     * @param columnName The name of the column to store the data in.
     * @param object The data object to store in the cell.
     ***************************************************************************************************/
    public void setData(int row, String columnName, Object object) {
        try {
            int column = getColumnViewIndex(columnName);
            setData(row, column, object);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Column name " + columnName + " does not exist.");
        }
    }

    /****************************************************************************************************
     * Sets the data to store in the cell.
     * <p>
     * @param row The row index to store the data in.
     * @param column The column view index to store the data in.
     * @param object The data object to store in the cell.
     ***************************************************************************************************/
    private void setData(int row, int column, Object object) {
        RDisplayTableCell tableCell = (RDisplayTableCell) getValueAt(row, column);
        tableCell.setData(object);
        setValueAt(tableCell, row, column);
    }

    /****************************************************************************************************
     * Clears all values in the column. In the case of text, it becomes empty. For numbers, it becomes 0,
     * and for boolean all values are set to not selected.
     * <p>
     * @param columnName The name of the column to update.
     ***************************************************************************************************/
    public void clearColumn(String columnName) {
        int column = getColumnViewIndex(columnName);
        int rowCount = getRowCount();
        RDisplayTableCell tableCell;

        for (int i = 0; i < rowCount; i++) {
            tableCell = (RDisplayTableCell) getValueAt(i, column);

            switch (tableCell.getType()) {
                case DataTypeConstants.BOOLEAN:
                case DataTypeConstants.ICON:
                    tableCell.setValue(Boolean.FALSE.toString());
                    break;
                case DataTypeConstants.CURRENCY:
                case DataTypeConstants.CURRENCY_LEFT:
                case DataTypeConstants.CURRENCY_RIGHT:
                case DataTypeConstants.DECIMAL:
                case DataTypeConstants.DECIMAL_LEFT:
                case DataTypeConstants.DECIMAL_RIGHT:
                case DataTypeConstants.INTEGER:
                case DataTypeConstants.INTEGER_LEFT:
                case DataTypeConstants.INTEGER_RIGHT:
                    tableCell.setValue("0");
                    break;
                default:
                    tableCell.setValue(StringConstants.EMPTY);
                    break;
            }
        }
        repaint();
    }

    /****************************************************************************************************
     * Sets a single row as the row selection by row number (which begins at zero).
     * <p>
     * @param row The row number to select.
     ***************************************************************************************************/
    public void setRowSelection(int row) {
        if (row > -1 && row < getRowCount()) {
            setRowSelectionInterval(row, row);
        }
    }

    /****************************************************************************************************
     * Selects a row based on an object. It searches each row and checks the parameter dataObject against
     * the data stored in the row. If a match is found, the row is selected and the method stops. A default
     * column name is supplied by the table.
     * <p>
     * @param dataObject The object to seek in data.
     ***************************************************************************************************/
    public void setRowSelection(Object dataObject) {
        setRowSelection(convertColumnIndexToView(0), dataObject);
    }

    /****************************************************************************************************
     * Selects a row based on column name and an object. It searches each row and checks the parameter
     * object against the data stored in the row. If a match is found, the row is selected and the method
     * stops.
     * <p>
     * @param columnName The name of the column to search.
     * @param dataObject The object to seek in data within the column.
     ***************************************************************************************************/
    public void setRowSelection(String columnName, Object dataObject) {
        setRowSelection(getColumnViewIndex(columnName), dataObject);
    }

    /****************************************************************************************************
     * Selects a row based on column name and an object. It searches each row and checks the parameter
     * object against the data stored in the row. If a match is found, the row is selected and the method
     * stops.
     * <p>
     * @param column The column index to search.
     * @param dataObject The object to seek in data within the column.
     ***************************************************************************************************/
    private void setRowSelection(int column, Object dataObject) {
        if (dataObject == null) {
            return;
        }
        int rows = getRowCount();
        for (int i = 0; i < rows; i++) {
            Object testObject = getData(i, column);
            if (testObject != null && testObject.equals(dataObject)) {
                setRowSelection(i);
                return;
            }
        }
    }

    /****************************************************************************************************
     * Sets a collection of data objects on the table. This method will only function if a
     * SelectableTableRowDisplayer has been assign to this table. It will remove all data before adding
     * the rows.
     * <p>
     * @param collection A collection of data objects.
     ***************************************************************************************************/
    public void setRows(Collection collection) {
        clearTable();
        addRows(collection);
    }

    /****************************************************************************************************
     * Adds a collection of data objects to the table. This method will only function if a
     * SelectableTableRowDisplayer has been assign to this table.
     * <p>
     * @param collection A collection of data objects.
     ***************************************************************************************************/
    public void addRows(Collection collection) {
        if (tableRowDisplayer == null) {
            throw new UnsupportedOperationException("No TableRowDisplayer has been assigned!");
        }
        if (collection.isEmpty()) {
            return;
        }
        List objectList = new ArrayList();
        List rowList = new ArrayList();

        for (Object object : collection) {
            try {
                objectList.add(object);
                rowList.add(tableRowDisplayer.buildRow(object));
            } catch (Throwable exception) {
                displayInnerException(exception);
                return;
            }
        }

        int size = objectList.size();
        int column = getColumnViewIndex(displayerKeyColumn);
        try {
            for (int i = 0; i < size; i++) {
                addRow((String[]) rowList.get(i));
                setData(getLastRowNumber(), column, objectList.get(i));
            }
        } catch (Throwable exception) {
            throw new IllegalStateException("Unable to add row!");
        }
    }

    /****************************************************************************************************
     * Adds a single data object to the table. This method will only function if a
     * SelectableTableRowDisplayer has been assign to this table.
     * <p>
     * @param collection A collection of data objects.
     ***************************************************************************************************/
    public void addRow(Object value) {
        if (value != null) {
            List rowList = new ArrayList();
            rowList.add(value);
            addRows(rowList);
        }
    }

    /****************************************************************************************************
     * Adds a row of data to the table. This method accepts a string array that represents a single row.
     * It is converted into an array of RDisplayTableCell objects in which each RDisplayTable Cell
     * represents a single cell in the table. This method causes the table to repaint. If many rows are
     * going to be added consecutively, all the string arrays should be built prior to adding any to the
     * table for optimized performance.
     * <p>
     * @param rowArray An array of strings representing a single row.
     ***************************************************************************************************/
    public void addRow(String[] rowArray) {
        addRow(buildRow(rowArray));
    }

    /****************************************************************************************************
     * Adds a row of data to the table. This method accepts an array of RDisplayTableCell that represents
     * a single row. Each RDisplayTableCell within the array represents a single cell. This method causes
     * the table to repaint. If many rows are going to be added consecutively, all the string arrays
     * should be built prior to adding any to the table for optimized performance.
     * <p>
     * @param rowArray An array of RDisplayTableCell objects representing a single table row.
     ***************************************************************************************************/
    public void addRow(RDisplayTableCell[] rowArray) {
        getTableModel().addRow(rowArray);
    }

    /****************************************************************************************************
     * Adds a row of data to the table. This method accepts an array of RDisplayTableCell that represents
     * a single row. Each RDisplayTableCell within the array represents a single cell. This method causes
     * the table to repaint. If many rows are going to be added consecutively, all the string arrays
     * should be built prior to adding any to the table for optimized performance.
     * <p>
     * @param rowArray An array of RDisplayTableCell objects representing a single table row.
     * @param dataObject A data object to place in the row at a given column.
     ***************************************************************************************************/
    public void addRow(String[] rowArray, Object dataObject) {
        try {
            addRow(rowArray);
            setData(getLastRowNumber(), convertColumnIndexToView(0), dataObject);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to add row!");
        }
    }

    /****************************************************************************************************
     * Adds a row of data to the table. This method accepts an array of RDisplayTabelCell that represents
     * a single row. Each RDisplayTabelCell within the array represents a single cell. This method causes
     * the table to repaint. If many rows are going to be added consecutively, all the string arrays
     * should be built prior to adding any to the table for optimized performance.
     * <p>
     * @param rowArray An array of RDisplayTabelCell objects representing a single table row.
     * @param columnName The name of the column to place the data in.
     * @param dataObject A data object to place in the row at a given column.
     ***************************************************************************************************/
    public void addRow(String[] rowArray, String columnName, Object dataObject) {
        try {
            addRow(rowArray);
            int column = getColumnViewIndex(columnName);
            setData(getLastRowNumber(), column, dataObject);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to add row!");
        }
    }

    /****************************************************************************************************
     * Inserts a row of data into the table. This method requires that a SelectableTableRowDisplayer has
     * been assigned to the table.
     * <p>
     * @param data The object to insert.
     * @param rowNumber The table location to insert the row at.
     ***************************************************************************************************/
    public void insertRow(Object data, int rowNumber) {
        if (tableRowDisplayer == null) {
            throw new UnsupportedOperationException("No SelectableTableRowDisplayer has been assigned!");
        }
        try {
            insertRow(tableRowDisplayer.buildRow(data), displayerKeyColumn, data, rowNumber);
        } catch (Throwable exception) {
            displayInnerException(exception);
        }
    }

    /****************************************************************************************************
     * Inserts a row of data into the table. This method accepts an array of RDisplayTableCell objects
     * that represents a single row. Each RDisplayTableCell within the array represents a single cell.
     * <p>
     * @param rowArray An array of RDisplayTableCell objects representing a single table row.
     * @param rowNumber The table location to insert the row at.
     ***************************************************************************************************/
    public void insertRow(RDisplayTableCell[] rowArray, int rowNumber) {
        try {
            RDisplayTableModel tableModel = (RDisplayTableModel) getModel();
            tableModel.insertRow(rowNumber, rowArray);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to insert row!");
        }
    }

    // TEST THIS!!!

    /****************************************************************************************************
     * Inserts a row of data into the table.
     * <p>
     * @param rowArray An array of strings to display in the row.
     * @param columnName Name of the column to place the data object in.
     * @param data An object to store in the row at the column indicated by column name.
     * @param rowNumber The table location to insert the row at.
     ***************************************************************************************************/
    public void insertRow(String[] rowArray, String columnName, Object data, int rowNumber) {
        try {
            RDisplayTableModel tableModel = (RDisplayTableModel) getModel();
            tableModel.insertRow(rowNumber, buildRow(rowArray));
            int column = getColumnViewIndex(columnName);
            setData(rowNumber, column, data);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to insert row!");
        }
    }

    /****************************************************************************************************
     * Updates a row of data into the table. This method requires that a SelectableTableRowDisplayer has
     * been assigned to the table.
     * <p>
     * @param data The object to update.
     ***************************************************************************************************/
    public void updateRow(Object data) {
        if (tableRowDisplayer == null) {
            throw new UnsupportedOperationException("No SelectableTableRowDisplayer has been assigned!");
        }
        try {
            updateRow(tableRowDisplayer.buildRow(data), displayerKeyColumn, data);
        } catch (UIException exception) {
            displayInnerException(exception);
        }
    }

    // TEST THIS!!!

    /****************************************************************************************************
     * Updates a row of data in the table using a String array and a data object in the table.
     * <p>
     * @param rowArray An array of Strings representing a single table row.
     * @param rowNumber The table row to update.
     ***************************************************************************************************/
    public void updateRow(String[] rowArray, String columnName, Object object) {
        updateRow(rowArray, getRowNumber(columnName, object));
    }

    // TEST THIS!!!

    /****************************************************************************************************
     * Updates a row of data in the table. This method accepts an array of RDisplayTableCells that
     * represents a single row. Each RDisplayTableCell within the array represents a single cell.
     * <p>
     * @param rowArray An array of RDisplayTableCell objects representing a single table row.
     * @param rowNumber The table row to update.
     ***************************************************************************************************/
    public void updateRow(RDisplayTableCell[] rowArray, int rowNumber) {
        try {
            for (int i = 0; i < rowArray.length; i++) {
                setValueAt(rowArray[i], rowNumber, i);
            }
            clearProblem(rowNumber);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to update row!");
        }
    }

    // TEST THIS!!!

    /****************************************************************************************************
     * Updates a row of data in the table using a String array and preserving the other values assigned
     * to the RDisplayTableCell.
     * <p>
     * @param rowArray An array of Strings representing a single table row.
     * @param rowNumber The table row to update.
     ***************************************************************************************************/
    public void updateRow(String[] rowArray, int rowNumber) {
        try {
            int columnCount = getColumnCount();
            for (int i = 0; i < rowArray.length; i++) {
                if (i < columnCount) {
                    setCellValue(rowNumber, convertColumnIndexToModel(i), rowArray[i]);
                }
            }
            clearProblem(rowNumber);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to update row!");
        }
    }

    // TEST THIS!!!

    /****************************************************************************************************
     * Removes each row from the table based on values found within a column. This removes rows based on
     * whether or not the column value is found in the identified column. This method must search the
     * table backwards as removing a row shrinks the total count.
     * <p>
     * @param columnName The column to search for matching value.
     * @param columnValue The value to match with the column.
     ***************************************************************************************************/
    public void removeRow(String columnName, String columnValue) {
        try {
            int rsize = getTableModel().getRowCount();
            int index = getColumnViewIndex(columnName);

            for (int i = rsize - 1; i > -1; i--) {
                String[] row = getRowDisplayData(i);

                if (row[index].equals(columnValue)) {
                    removeRow(i);
                }
            }
        } catch (Exception except) {
            throw new IllegalStateException("Unable to remove row!");
        }
    }

    /****************************************************************************************************
     * Removes each row from the table based on the value within the column. This removes rows based on
     * whether or not the column value is found in the identified column. This method must search the
     * table backwards as removing a row shrinks the total count.
     * <p>
     * @param columnValue The value to match with the column.
     ***************************************************************************************************/
    public void removeRowByData(Object data) {
        removeRowByData(displayerKeyColumn, data);
    }

    // TEST THIS!!!

    /****************************************************************************************************
     * Removes each row from the table based on the value within the column. This removes rows based on
     * whether or not the column value is found in the identified column. This method must search the
     * table backwards as removing a row shrinks the total count.
     * <p>
     * @param columnName The column to search for matching value.
     * @param columnValue The value to match with the column.
     ***************************************************************************************************/
    public void removeRowByData(String columnName, Object data) {
        try {
            int rsize = getTableModel().getRowCount();
            int index = getColumnViewIndex(columnName);

            for (int i = rsize - 1; i > -1; i--) {
                Object object = getData(i, index);

                if (object != null && object == data) {
                    removeRow(i);
                }
            }
        } catch (Exception except) {
            throw new IllegalStateException("Unable to remove row!");
        }
    }

    // TEST THIS!!!

    /****************************************************************************************************
     * Removes each row from the table based on the value within the column. This removes rows based on
     * whether or not the column value is found in the identified column. This method must search the
     * table backwards as removing a row shrinks the total count.
     * <p>
     * @param columnName The column to search for matching value.
     * @param data The values to match with the column.
     ***************************************************************************************************/
    public void removeRowsByData(String columnName, Collection data) {
        try {
            int rsize = getTableModel().getRowCount();
            int index = getColumnViewIndex(columnName);

            for (int i = rsize - 1; i > -1; i--) {
                Object object = getData(i, index);

                if (object != null && data.contains(object)) {
                    removeRow(i);
                }
            }
        } catch (Exception except) {
            throw new IllegalStateException("Unable to remove row!");
        }
    }

    /****************************************************************************************************
     * Removes a row from the table.
     * <p>
     * @param rowNumber The row number of the table row to remove.
     ***************************************************************************************************/
    public void removeRow(int rowNumber) {
        if (rowNumber > -1) {
            removeRowSelectionInterval(rowNumber, rowNumber);
            getTableModel().removeRow(rowNumber);
            clearProblem(rowNumber);
        }
    }

    /****************************************************************************************************
     * Removes the currently selected row from the table. This should probably only be used in single
     * selection mode as in multi-selection tables, it removes only the first selected row and ignores
     * each additional row.
     ***************************************************************************************************/
    public void removeSelectedRow() {
        removeRow(getSelectedRow());
    }

    /****************************************************************************************************
     * Removes all currently selected rows from the table. This remove occurs from the bottom of the
     * table backwards as everything is reorganized when a row is removed.
     ***************************************************************************************************/
    public void removeSelectedRows() {
        int[] selectedRows = getSelectedRows();

        for (int i = selectedRows.length - 1; i >= 0; i--) {
            removeRow(selectedRows[i]);
        }
    }

    /****************************************************************************************************
     * Retrieves true if the table has any selected rows, false otherwise.
     * <p>
     * @return True if the table has selected rows, false otherwise.
     ***************************************************************************************************/
    public boolean hasSelectedRows() {
        return getSelectedRowCount() > 0;
    }

    /****************************************************************************************************
     * Retrieves the last row number of the table.
     * <p>
     * @return The last row number of the table.
     ***************************************************************************************************/
    public int getLastRowNumber() {
        return getRowCount() - 1;
    }

    /****************************************************************************************************
     * Sets the entire table data from a single list. The list must contain string arrays (or the
     * equivalent list). Each string array represents an entire row of cell values in the correct order.
     * This method will automatically clear the table of all previous information.
     * <p>
     * @param list A list of string arrays (or equivalent list) representing rows.
     ***************************************************************************************************/
    public void fillTable(List list) {
        try {
            resetTable();

            for (Object object : list) {
                String[] nextRow;
                if (object instanceof List) {
                    List objectList = (List) object;
                    nextRow = (String[]) objectList.toArray(new String[objectList.size()]);
                } else {
                    nextRow = (String[]) object;
                }
                if (nextRow != null) {
                    addRow(buildRow(nextRow));
                }
            }
        } catch (ClassCastException e) {
            throw new IllegalStateException("Unable to fill table!");
        }
    }

    /****************************************************************************************************
     * Builds a default formatted table row from an array of cell values.
     * <p>
     * @param dataArray A string array representing a row of values to be properly formatted.
     * @param color The color to set the cell display to for the entire row.
     * @return An array containing a row with a single RDisplayTableCell for each cell.
     ***************************************************************************************************/
    private RDisplayTableCell[] buildRow(String[] dataArray) {
        RDisplayTableCell[] row = new RDisplayTableCell[dataArray.length];

        try {
            for (int i = 0; i < dataArray.length; i++) {
                RDisplayTableCell tableCell = new RDisplayTableCell();
                tableCell.setValue(dataArray[i]);
                tableCell.setType(columnTypeArray[i]);
                row[i] = tableCell;
            }
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to build row!");
        }
        return row;
    }

    /****************************************************************************************************
     * Retrieves column view index for column name.
     * <p>
     * @param columnName The column name.
     * @return The view index of the column.
     ***************************************************************************************************/
    private int getColumnViewIndex(String columnName) {
        return convertColumnIndexToView(getColumn(columnName).getModelIndex());
    }

    /****************************************************************************************************
     * Retrieves column model index for column name.
     * <p>
     * @param columnName The column name.
     * @return The model index of the column.
     ***************************************************************************************************/
    private int getColumnModelIndex(String columnName) {
        return getColumn(columnName).getModelIndex();
    }

    /****************************************************************************************************
     * Clears the entire table of rows. It calls repaint because removing rows does not automatically
     * trigger a repaint, but we want it to anyway. Additionally, it will send an event for every row
     * that is deselected during the clear process.
     ***************************************************************************************************/
    public void clearTable() {
        clearSelection();
        clearProblems();
        getTableModel().clear();
        refreshTableHeader();
        repaint();
    }

    /****************************************************************************************************
     * Clears the entire table of rows. It calls repaint because removing rows does not automatically
     * trigger a repaint, but we want it to anyway. This method does not trigger any events if rows were
     * programmatically de-selected. This method should only be used in the process of refreshing a
     * table.
     ***************************************************************************************************/
    public void resetTable() {
        resettingTable = true;
        clearSelection();
        clearProblems();
        getTableModel().clear();
        refreshTableHeader();
        resettingTable = false;
        repaint();
    }

    /****************************************************************************************************
     * Returns true if the table is empty.
     ***************************************************************************************************/
    public boolean isEmpty() {
        return getRowCount() == 0;
    }

    /****************************************************************************************************
     * Empty implementation of the mouse listener interface method.
     ***************************************************************************************************/
    public void keyTyped(KeyEvent keyEvent) {
    }

    /****************************************************************************************************
     * Empty implementation of the key listener interface method.
     ***************************************************************************************************/
    public void keyReleased(KeyEvent keyEvent) {
    }

    /****************************************************************************************************
     * Implements the key listener interface 'key pressed' method. This method checks to see if the tab
     * key was pressed, and if so, it transfers the focus to the next focusable component. If ALT-HotKey
     * is pressed, the appropriate column is sorted. If ENTER is pressed, then the table row selected
     * action is triggered.
     * <p>
     * @param event Details about the key event that occurred.
     ***************************************************************************************************/
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.isAltDown()) {
            handleSortAction(String.valueOf(keyEvent.getKeyChar()));
            return;
        }
        if (isFocusOwner()) {
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_TAB:
                    keyEvent.consume();
                    if (keyEvent.isShiftDown()) {
                        doShiftTabPressed();
                    } else {
                        doTabPressed();
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    keyEvent.consume();
                    doDoubleClickAction();
                    break;
                default:
                    break;
            }
        }
    }

    /****************************************************************************************************
     * Method is executed when the shift-tab key is pressed within a RDisplayTable. It transfers the
     * focus to the previous component (if one is assigned)..
     ***************************************************************************************************/
    private void doShiftTabPressed() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent(this);
    }

    /****************************************************************************************************
     * Method is executed when the tab key is pressed within a RDisplayTable. It transfers the focus to
     * the next component.
     ***************************************************************************************************/
    private void doTabPressed() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(this);
    }

    /****************************************************************************************************
     * Overrides the JTable list selection event and notifies single action listener of the event. This
     * method strips out adjusting values.
     ***************************************************************************************************/
    public void valueChanged(ListSelectionEvent event) {
        super.valueChanged(event);

        if (!event.getValueIsAdjusting()) {
            displayProblemsForSelectedRows();
            doSingleClickAction();
        }
    }

    /****************************************************************************************************
     * Empty implementation of the mouse motion listener interface method.
     ***************************************************************************************************/
    public void mouseDragged(MouseEvent event) {
    }

    /****************************************************************************************************
     * Implement the mouse motion listener method to alter cursor if hovering over a hyperlink.
     ***************************************************************************************************/
    public void mouseMoved(MouseEvent event) {
        if (isHyperlink(event.getPoint())) {
            if (!isHyperlinkCursor) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                isHyperlinkCursor = true;
            }
        } else if (isHyperlinkCursor) {
            setCursor(Cursor.getDefaultCursor());
            isHyperlinkCursor = false;
        }
    }

    // TEST THIS!!!

    /****************************************************************************************************
     * Retrieves whether or not the point is a hyperlink or not.
     ***************************************************************************************************/
    private boolean isHyperlink(Point point) {
        return columnTypeArray[columnAtPoint(point)] == DataTypeConstants.HYPERLINK;
    }

    /****************************************************************************************************
     * Empty implementation of the mouse listener interface method.
     ***************************************************************************************************/
    public void mousePressed(MouseEvent event) {
    }

    /****************************************************************************************************
     * Empty implementation of the mouse listener interface method.
     ***************************************************************************************************/
    public void mouseReleased(MouseEvent event) {
    }

    /****************************************************************************************************
     * Empty implementation of the mouse listener interface method.
     ***************************************************************************************************/
    public void mouseEntered(MouseEvent event) {
    }

    /****************************************************************************************************
     * Empty implementation of the mouse listener interface method.
     ***************************************************************************************************/
    public void mouseExited(MouseEvent event) {
    }

    /****************************************************************************************************
     * Implement the mouse listener interface 'mouse clicked' method. If the time event is identical to
     * the last event, then do nothing. If a double click occurs within a row, the table listener is
     * informed. If a single click occurs within a column header, the table is sorted on that column. If
     * the same column is clicked again, the sort order is reversed.
     * <p>
     * @param mouseEvent Details about the mouse event that occurred.
     ***************************************************************************************************/
    public void mouseClicked(MouseEvent mouseEvent) {
        if (storeMouseEvent != null) {
            long oldEvent = storeMouseEvent.getWhen();
            long newEvent = mouseEvent.getWhen();
            if (oldEvent == newEvent) {
                return;
            }
        }
        Object object = mouseEvent.getComponent();

        if (SwingUtilities.isRightMouseButton(mouseEvent) && configurationEnabled) {
            displayPopupMenu(object, mouseEvent.getPoint());
        } else if (object instanceof JTableHeader && columnSortingEnabled) {
            sortLeftClick((JTableHeader) object, mouseEvent.getPoint());
        } else if (object instanceof JTable) {
            if (isHyperlink(mouseEvent.getPoint())) {
                doHyperlinkClickAction(mouseEvent.getPoint());
            } else if (mouseEvent.getClickCount() == 2) {
                doDoubleClickAction();
            }
        }
        storeMouseEvent = mouseEvent;
    }

    // TEST THIS!!!!

    /****************************************************************************************************
     * Pops up the display table menu item and the indicated location.
     ***************************************************************************************************/
    protected void displayPopupMenu(Object object, Point point) {
        if (popupMenu == null) {
            popupMenu = new RDisplayTablePopupMenu(this);
        }
        int column = -1;
        if (object instanceof JTableHeader) {
            column = ((JTableHeader) object).columnAtPoint(point);
        }
        popupMenu.initialize(column);
        popupMenu.show(this, point.x, point.y);
    }

    /****************************************************************************************************
     * Pops up the table configuration dialog.
     ***************************************************************************************************/
    protected void displayConfigurationDialog() {
        if (configDialog == null) {
            Container container = getTopLevelAncestor();

            if (container instanceof JFrame) {
                configDialog = new RDisplayTableConfigDialog((JFrame) container);
            } else if (container instanceof JDialog) {
                configDialog = new RDisplayTableConfigDialog((JDialog) container);
            } else {
                return;
            }
            configDialog.addPropertyChangeListener(this);
        }
        configDialog.initialize(identifier, columnHeaderArray);
        configDialog.setVisible(true);
    }

    /****************************************************************************************************
     * Implements the property change listener to update the table on property changes.
     ***************************************************************************************************/
    public void propertyChange(PropertyChangeEvent event) {
        String propertyName = event.getPropertyName();
        if (propertyName.equals(UIPropertyName.TABLE_CONFIGURATION_ALTERED)) {
            doTableConfigurationAltered((TableConfigurationData) event.getNewValue());
        }
    }

    /****************************************************************************************************
     * Executed when the table configuration data is altered.
     ***************************************************************************************************/
    private void doTableConfigurationAltered(TableConfigurationData data) {
        validateGridLines(data.getGridlinesSetting());
        validateFontSize(data.getSizeContent());
        // validateHiddenColumns(data.getHiddenColumns());
        // validateColumnDisplayOrder(data.getColumnDisplayOrder());
    }

    /****************************************************************************************************
     * Updates the gridline settings.
     ***************************************************************************************************/
    protected void validateGridLines(int setting) {
        switch (setting) {
            case RTableConfigConstants.NO_GRIDLINES:
                setShowVerticalLines(false);
                setShowHorizontalLines(false);
                setIntercellSpacing(new Dimension(0, 0));
                break;
            case RTableConfigConstants.COL_GRIDLINES:
                setShowVerticalLines(true);
                setShowHorizontalLines(false);
                setIntercellSpacing(new Dimension(2, 0));
                break;
            case RTableConfigConstants.ROW_GRIDLINES:
                setShowVerticalLines(false);
                setShowHorizontalLines(true);
                setIntercellSpacing(new Dimension(0, 2));
                break;
            default:
                setShowVerticalLines(true);
                setShowHorizontalLines(true);
                setIntercellSpacing(new Dimension(2, 2));
                break;
        }
    }

    /****************************************************************************************************
     * Initializes the size content information in the tab.
     ***************************************************************************************************/
    protected void validateFontSize(int sizeContent) {
        Font currentFont = getFont();
        switch (sizeContent) {
            case RTableConfigConstants.SMALLEST:
                setFont(currentFont.deriveFont(currentFont.getStyle(), 8f));
                break;
            case RTableConfigConstants.SMALLER:
                setFont(currentFont.deriveFont(currentFont.getStyle(), 9f));
                break;
            case RTableConfigConstants.LARGE:
                setFont(currentFont.deriveFont(currentFont.getStyle(), 11f));
                break;
            case RTableConfigConstants.LARGER:
                setFont(currentFont.deriveFont(currentFont.getStyle(), 12f));
                break;
            case RTableConfigConstants.LARGEST:
                setFont(currentFont.deriveFont(currentFont.getStyle(), 14f));
                break;
            default:
                setFont(currentFont.deriveFont(currentFont.getStyle(), 10f));
                break;
        }
    }

    /****************************************************************************************************
     * Performs the right mouse click action, locking or releasing a particular column sort.
     ***************************************************************************************************/
    // private void sortRightClick(JTableHeader header, Point point) {
    // This functionality is undefined or changed in Project202
    // int column = header.columnAtPoint(point);
    // int size = lockedSortColumnArray.length;
    //
    // if (isLocked(column)) {
    // int[] carray = new int[size - 1];
    // boolean[] oarray = new boolean[size - 1];
    // int index = 0;
    //
    // for (int i = 0; i < size; i++) {
    // if (lockedSortColumnArray[i] != column) {
    // carray[index] = lockedSortColumnArray[i];
    // oarray[index] = lockedSortOrderArray[i];
    // index++;
    // }
    // }
    //
    // lockedSortColumnArray = carray;
    // lockedSortOrderArray = oarray;
    //
    // resetSortColumnArrows();
    // } else if (column == lastSortedColumn) {
    // int[] carray = new int[size + 1];
    // boolean[] oarray = new boolean[size + 1];
    //
    // System.arraycopy(lockedSortColumnArray, 0, carray, 0, size);
    // System.arraycopy(lockedSortOrderArray, 0, oarray, 0, size);
    //
    // carray[carray.length - 1] = lastSortedColumn;
    // oarray[oarray.length - 1] = lastSortedOrder;
    //
    // lockedSortColumnArray = carray;
    // lockedSortOrderArray = oarray;
    // }
    // }
    // TEST THIS!!!

    /****************************************************************************************************
     * Performs the left mouse click action, sorting a column if it is not locked.
     * <p>
     * @param header The table header where a mouse click occurred.
     * @param point A point registered from a mouse event (located within the header).
     ***************************************************************************************************/
    private void sortLeftClick(JTableHeader header, Point point) {
        int column = header.columnAtPoint(point);
        if (isLocked(column)) {
            return;
        }
        handleSortAction(column);
    }

    // TEST THIS!!!

    /****************************************************************************************************
     * Retrieves whether not a column is currently within the locked list.
     * <p>
     * @param column The column to check if locked.
     ***************************************************************************************************/
    private boolean isLocked(int column) {
        for (int lockedSortColumn : lockedSortColumnArray) {
            if (lockedSortColumn == column) {
                return true;
            }
        }
        return false;
    }

    /****************************************************************************************************
     * Resorts the table.
     ***************************************************************************************************/
    public void resort() {
        handleSortAction();
    }

    // TEST THIS!!!

    /****************************************************************************************************
     * Retrieves the sort number for a given column.
     * <p>
     * @param column The column number
     *            <p>
     * @return The numerical sequence number of the sorted column (-1 if column not sorted).
     ***************************************************************************************************/
    public int getSortNumber(int column) {
        int sortNumber = 1;
        for (int lockedSortColumn : lockedSortColumnArray) {
            if (lockedSortColumn == column) {
                return sortNumber;
            }
            sortNumber++;
        }
        for (int userSortColumn : userSortColumnArray) {
            if (userSortColumn == column) {
                return sortNumber;
            }
            sortNumber++;
        }
        for (int defaultSortColumn : defaultSortColumnArray) {
            if (defaultSortColumn == column) {
                return sortNumber;
            }
            sortNumber++;
        }
        return -1;
    }

    // TEST THIS!!!

    /****************************************************************************************************
     * Retrieves the sort ascending flag for a given column.
     * <p>
     * @param column The column number.
     *            <p>
     * @return True if the column is sorted ascending, false if descending.
     ***************************************************************************************************/
    public boolean getSortAscending(int column) {
        for (int i = 0; i < lockedSortColumnArray.length; i++) {
            if (lockedSortColumnArray[i] == column) {
                return lockedSortOrderArray[i];
            }
        }
        for (int i = 0; i < userSortColumnArray.length; i++) {
            if (userSortColumnArray[i] == column) {
                return userSortOrderArray[i];
            }
        }
        for (int i = 0; i < defaultSortColumnArray.length; i++) {
            if (defaultSortColumnArray[i] == column) {
                return defaultSortOrderArray[i];
            }
        }
        return ASCENDING;
    }

    // TEST THIS!!!

    /****************************************************************************************************
     * Handles sorting the data inside a table on the columns selected. A selected column is triggered by
     * single-clicking on the column header (or by pressing the hot key). This checks if there are more
     * than sort limit records, which takes too long to sort.
     * <p>
     * @param hotkey The ALT-hotkey that was pressed to trigger this action.
     ***************************************************************************************************/
    private void handleSortAction(String hotkey) {
        try {
            int column = Integer.parseInt(hotkey);
            if (column <= getColumnCount()) {
                handleSortAction(column - 1);
            }
        } catch (Exception exception) {
            // Do Nothing If An Exception Occurs
            LogService.debug(this, "ignoring Excepton");
        }
    }

    // TEST THIS!!!

    /****************************************************************************************************
     * Handles sorting the data inside a table on the columns selected. A selected column is triggered by
     * single-clicking on the column header (or by pressing the hot key). This checks if there are more
     * than sort limit records, which takes too long to sort. This method also tracks any row that was
     * selected and resets the selection after cleaning the table and redisplaying the sorted
     * information.
     * <p>
     * @param selectedColumn The column number of the selected column.
     ***************************************************************************************************/
    private void handleSortAction(int column) {
        if (isLocked(column)) {
            return;
        }
        if (userSortColumnArray.length > 0 && userSortColumnArray[0] == column) {
            userSortOrderArray[0] = !userSortOrderArray[0];
            handleSortAction();
            return;
        }
        userSortColumnArray = new int[0];
        userSortOrderArray = new boolean[0];

        for (int i = 0; i < defaultSortColumnArray.length; i++) {
            if (defaultSortColumnArray[i] == column) {
                defaultSortOrderArray[i] = !defaultSortOrderArray[i];
                handleSortAction();
                return;
            }
        }
        userSortColumnArray = addToIntArray(userSortColumnArray, column);
        userSortOrderArray = addToBooleanArray(userSortOrderArray, ASCENDING);
        handleSortAction();
    }

    // TEST THIS!!!

    /****************************************************************************************************
     * Handles sorting the data inside a table on the columns selected. A selected column is triggered by
     * single-clicking on the column header (or by pressing the hot key). This checks if there are more
     * than sort limit records, which takes too long to sort. This method also tracks any row that was
     * selected and resets the selection after cleaning the table and redisplaying the sorted
     * information.
     * <p>
     * @param selectedColumn The column number of the selected column.
     ***************************************************************************************************/
    private void handleSortAction() {
        try {
            List tableList = getFullTableData();
            int selectedRow = getSelectedRow();
            Object selected = null;

            if (selectedRow != -1) {
                selected = tableList.get(selectedRow);
            }

            if (tableList.size() > tableSortLimit) {
                displayInnerMessage(UIMessageText.TABLE_LIMIT_ERROR);
                return;
            }

            int lockedSize = lockedSortColumnArray.length;
            int userSize = userSortColumnArray.length;
            int defaultSize = defaultSortColumnArray.length;
            int index = 0;

            int[] fullSortColumns = new int[lockedSize + userSize + defaultSize];
            boolean[] fullSortOrders = new boolean[lockedSize + userSize + defaultSize];

            for (int i = 0; i < lockedSortColumnArray.length; i++) {
                fullSortColumns[index] = lockedSortColumnArray[i];
                fullSortOrders[index] = lockedSortOrderArray[i];
                index++;
            }
            for (int i = 0; i < userSortColumnArray.length; i++) {
                fullSortColumns[index] = userSortColumnArray[i];
                fullSortOrders[index] = userSortOrderArray[i];
                index++;
            }
            for (int i = 0; i < defaultSortColumnArray.length; i++) {
                fullSortColumns[index] = defaultSortColumnArray[i];
                fullSortOrders[index] = defaultSortOrderArray[i];
                index++;
            }

            tableSortTool.sortTable(tableList, fullSortColumns, fullSortOrders);

            resettingTable = true;
            getTableModel().clear();
            resettingTable = false;

            for (Object row : tableList) {
                addRow((RDisplayTableCell[]) row);
            }
            setRowSelection(tableList.indexOf(selected));
            refreshTableHeader();
        } catch (Throwable exception) {
            throw new IllegalStateException("Table failed to sort!", exception);
        }
    }

    /****************************************************************************************************
     * Redraws the header of the table. This is often called when the sort icons have been changed.
     ***************************************************************************************************/
    protected void refreshTableHeader() {
        getTableHeader().invalidate();
        getTableHeader().repaint();
    }

    // TEST THIS!!!

    /****************************************************************************************************
     * Notifies the single-click action listener if a hyperlink has been selected.
     ***************************************************************************************************/
    private void doHyperlinkClickAction(Point point) {
        if (tableCellActionListener != null && tableCellActionCommand != null) {
            String command = tableCellActionCommand;
            int index = columnAtPoint(point);
            String column = (String) getColumnModel().getColumn(index).getHeaderValue();
            int row = getSelectionModel().getLeadSelectionIndex();
            Object data = null;
            if (row > -1) {
                data = getCellValueAt(row, index);
            }
            tableCellActionListener.processTableCellEvent(new RTableCellEvent(this, command, column, row, data));
        }
    }

    /****************************************************************************************************
     * Notifies the single-click action listener if a row has been selected.
     ***************************************************************************************************/
    private void doSingleClickAction() {
        if (singleActionListener != null && singleActionCommand != null && !resettingTable) {
            singleActionListener.performActionEvent(new RActionEvent(this, singleActionCommand));
        }
    }

    /****************************************************************************************************
     * Notifies a double-click action listener if a row has been double-clicked.
     ***************************************************************************************************/
    private void doDoubleClickAction() {
        if (doubleActionListener == null || doubleActionCommand == null || resettingTable) {
            return;
        }
        if (getSelectedRowCount() > 0) {
            doubleActionListener.performActionEvent(new RActionEvent(this, doubleActionCommand));
        }
    }

    /****************************************************************************************************
     * Retrieves all the problems still being display in the table.
     * <p>
     * @return A collection of UIProblems to displayed within the table.
     ***************************************************************************************************/
    public List<UIProblem> getProblems() {
        List<UIProblem> problems = new ArrayList<>();
        for (Object problem : problemMap.values()) {
            problems.add(((RDisplayTableProblem) problem).getProblem());
        }
        return problems;
    }

    /****************************************************************************************************
     * Displays a UIException in the table. It will find the object within each problem of the exception
     * and highlight the row matching that object. When that row is selected, the error from that row
     * will display.
     * <p>
     * @param problems A collection of UIProblems to display within the table.
     ***************************************************************************************************/
    public void displayProblems(List<UIProblem> problems) {
        displayProblems(problems, 0);
    }

    /****************************************************************************************************
     * Displays UIProblems in the table. It will find the object within each problem of the exception and
     * highlight the row matching that object. When that row is selected, the error from that row will
     * display.
     * <p>
     * @param problems A collection of UIProblems to display within the table.
     * @param columnName The column to find the matching object in.
     ***************************************************************************************************/
    public void displayProblems(List<UIProblem> problems, String columnName) {
        displayProblems(problems, getColumnViewIndex(columnName));
    }

    /****************************************************************************************************
     * Displays UIProblems in the table. It will find the object within each problem of the exception and
     * highlight the row matching that object. When that row is selected, the error from that row will
     * display.
     * <p>
     * @param problems A collection of UIProblems to display within the table.
     * @param columnName The column to find the matching object in.
     ***************************************************************************************************/
    private void displayProblems(List<UIProblem> problems, int column) {
        RDisplayTableProblem tableProblem;
        Object testObject;

        problemMap = new HashMap<>();

        int rowCount = getRowCount();
        for (int row = 0; row < rowCount; row++) {
            testObject = getData(row, column);
            if (testObject != null) {
                for (UIProblem problem : problems) {
                    if (problem.getData() == testObject) {
                        tableProblem = new RDisplayTableProblem(problem, row, column);
                        problemMap.put(problem.getData(), tableProblem);
                        setErrorState(row, true);
                    }
                }
            }
        }
    }

    /****************************************************************************************************
     * Displays the problem message for selected rows.
     ***************************************************************************************************/
    private void displayProblemsForSelectedRows() {
        int selectedRow = getSelectedRow();
        if (selectedRow != -1) {
            UIProblem problem = getProblem(selectedRow);
            if (problem != null) {
                displayInnerProblem(problem);
            }
        }
    }

    /****************************************************************************************************
     * Clears all problems from the display table.
     ***************************************************************************************************/
    public void clearProblems() {
        RDisplayTableProblem problem;
        for (Object problemKey : problemMap.keySet()) {
            problem = (RDisplayTableProblem) problemMap.get(problemKey);
            if (problem != null) {
                setErrorState(problem.getRow(), false);
            }
        }
        problemMap = new HashMap<>();
    }

    /****************************************************************************************************
     * Clears the problem associated with the value object from the display table.
     * <p>
     * @param value The object to clear.
     ***************************************************************************************************/
    public void clearProblem(Object value) {
        RDisplayTableProblem problem = (RDisplayTableProblem) problemMap.get(value);
        if (problem != null) {
            setErrorState(problem.getRow(), false);
            problemMap.remove(value);
        }
    }

    /****************************************************************************************************
     * Clears the problem associated with the specific row in the table.
     * <p>
     * @param row The row to clear the problem from.
     ***************************************************************************************************/
    public void clearProblem(int row) {
        UIProblem problem = getProblem(row);
        if (problem != null) {
            clearProblem(problem);
        }
    }

    /****************************************************************************************************
     * Retrieves the UIProblem for a specific row number.
     ***************************************************************************************************/
    private UIProblem getProblem(int row) {
        RDisplayTableProblem problem;
        for (Object problemKey : problemMap.keySet()) {
            problem = (RDisplayTableProblem) problemMap.get(problemKey);
            if (problem.getRow() == row) {
                return problem.getProblem();
            }
        }
        return null;
    }

    /****************************************************************************************************
     * Retrieves whether or not the row matching the object data is considered "in error".
     * <p>
     * @param data The data object of the row.
     *            <p>
     * @return True if the row is in error, false otherwise.
     ***************************************************************************************************/
    public boolean isProblemRow(Object data) {
        return isProblemRow(getRowNumber(0, data));
    }

    /****************************************************************************************************
     * Retrieves whether or not the row is considered "in error".
     * <p>
     * @param data The data object of the row.
     * @param columnName The name of the column in which the data is stored.
     *            <p>
     * @return True if the row is in error, false otherwise.
     ***************************************************************************************************/
    public boolean isProblemRow(Object data, String columnName) {
        return isProblemRow(getRowNumber(columnName, data));
    }

    /****************************************************************************************************
     * Retrieves whether or not the row is considered "in error".
     * <p>
     * @param rowNumber The row number of the row to check.
     *            <p>
     * @return True if the row is in error, false otherwise.
     ***************************************************************************************************/
    public boolean isProblemRow(int rowNumber) {
        RDisplayTableCell tableCell = (RDisplayTableCell) getValueAt(rowNumber, 0);
        return tableCell.isErrorState();
    }

    /****************************************************************************************************
     * Assigns whether or not the row is considered "in error". If true, the row will display itself with
     * with error state colors. If false, it will return to normal colors.
     * <p>
     * @param rowNumber The row number to place in error.
     * @param isError True if the row should be in error, false otherwise.
     ***************************************************************************************************/
    private void setErrorState(int rowNumber, boolean isError) {
        int columnCount = getColumnCount();

        RDisplayTableCell tableCell;
        for (int i = 0; i < columnCount; i++) {
            int index = convertColumnIndexToModel(i);
            tableCell = (RDisplayTableCell) getValueAt(rowNumber, index);
            tableCell.setErrorState(isError);
        }
        repaint();
    }

    /****************************************************************************************************
     * Displays an internal exception, which is different from an external exception. This usually comes
     * from a selectableDisplayer or some such thing.
     ***************************************************************************************************/
    private void displayInnerException(Throwable exception) {
        UIStatusUtility.displayException(this, exception);
    }

    /****************************************************************************************************
     * Displays an internal message.
     ***************************************************************************************************/
    private void displayInnerMessage(MessageText message) {
        UIStatusUtility.displayMessage(this, message);
    }

    /****************************************************************************************************
     * Displays an inner problem by placing its message on the status bar.
     ***************************************************************************************************/
    private void displayInnerProblem(UIProblem problem) {
        Object[] values = problem.getMessageValues();
        if (values != null && values.length > 0) {
            UIStatusUtility.displayMessage(this, problem.getMessageText(), values[0].toString());
        } else {
            UIStatusUtility.displayMessage(this, problem.getMessageText());
        }
    }

    /****************************************************************************************************
     * Adds value to the end of a integer area.
     ***************************************************************************************************/
    private int[] addToIntArray(int[] originalArray, int value) {
        int[] tempArray = new int[originalArray.length + 1];
        System.arraycopy(originalArray, 0, tempArray, 0, originalArray.length);
        tempArray[tempArray.length - 1] = value;
        return tempArray;
    }

    /****************************************************************************************************
     * Adds value to the end of a boolean area.
     ***************************************************************************************************/
    private boolean[] addToBooleanArray(boolean[] originalArray, boolean value) {
        boolean[] tempArray = new boolean[originalArray.length + 1];
        System.arraycopy(originalArray, 0, tempArray, 0, originalArray.length);
        tempArray[tempArray.length - 1] = value;
        return tempArray;
    }
}
