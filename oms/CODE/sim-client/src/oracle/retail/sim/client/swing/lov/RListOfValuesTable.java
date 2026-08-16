package oracle.retail.sim.client.swing.lov;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
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
import oracle.retail.sim.client.swing.displaytable.RDisplayTableCell;
import oracle.retail.sim.client.swing.displaytable.RDisplayTableModel;
import oracle.retail.sim.client.swing.displaytable.RTableCellRenderer;
import oracle.retail.sim.client.swing.displaytable.RTableColumnInterface;
import oracle.retail.sim.client.swing.displaytable.RTableHeader;
import oracle.retail.sim.client.swing.displaytable.RTableHeaderInterface;
import oracle.retail.sim.client.swing.displaytable.TableRowDisplayer;
import oracle.retail.sim.client.swing.displaytable.TableRowSorter;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.event.RListOfValuesTableTransferHandler;
import oracle.retail.sim.client.swing.filter.TableSortElement;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.WindowPlacer;
import oracle.retail.sim.common.logging.LogService;

/******************************************************************************************
 * This class subclasses the standard JTable class in the Swing package to provide custom
 * functionality for the list of values editor. This functionality includes allowing the
 * tab key to exit a table instead of going between cells. Each cell of a RDisplayTable must
 * contain a RDisplayTableCell. This table is only capable of displaying and the cells can
 * never be editable.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RListOfValuesTable extends JTable implements KeyListener, MouseListener, RTableHeaderInterface, RTableColumnInterface {
    private static final long serialVersionUID = 8629381917985973809L;

    private static final boolean ASCENDING = true;

    private RListOfValuesFilterDialog filterDialog;
    private final TableRowSorter tableRowSorter = new TableRowSorter();
    private TableRowDisplayer tableRowDisplayer;

    private String[] columnHeaderArray = new String[0];
    private int[] columnTypeArray = new int[0];
    private Color[] columnBackgroundArray = new Color[0];
    private Color[] columnForegroundArray = new Color[0];

    private final int[] sortColumnArray = new int[1];
    private final boolean[] sortOrderArray = new boolean[1];

    private REventListener singleActionListener;
    private String singleActionCommand;

    private REventListener doubleActionListener;
    private String doubleActionCommand;

    private PropertyChangeListener filterChangeListener;

    private MouseEvent storeMouseEvent;

    private Color tableHeaderBackground = Color.WHITE;
    private Color tableHeaderForeground = Color.BLACK;
    private Color defaultRowForeground = Color.BLACK;
    private Color defaultRowBackground = Color.WHITE;
    private Color alternateRowBackground = Color.BLACK;

    private Font tableHeaderFont = new Font("Verdana", Font.BOLD, 10);

    private int totalAvailableWidth;
    private int totalHeaderWidth;

    private boolean resettingTable;

    /******************************************************************************************
     * Returns new RListOfValuesTable object.
     *****************************************************************************************/
    public RListOfValuesTable() {
        setModel(new RDisplayTableModel(new String[0], 0));
        getTableHeader().addMouseListener(this);
        initRowSelectionMode();
        addMouseListener(this);
        addKeyListener(this);
        setTransferHandler(new RListOfValuesTableTransferHandler());
        initializeUIDefaults();
    }

    /******************************************************************************************
     * Initialize UI defaults.
     *****************************************************************************************/
    private void initializeUIDefaults() {
        tableHeaderFont = UIManager.getFont(UIThemeName.RDISPLAYTABLE_HEADER_FONT);
        tableHeaderBackground = UIManager.getColor(UIThemeName.RDISPLAYTABLE_HEADER_BACKGROUND);
        tableHeaderForeground = UIManager.getColor(UIThemeName.RDISPLAYTABLE_HEADER_FOREGROUND);
        defaultRowForeground = UIManager.getColor(UIThemeName.RDISPLAYTABLE_DEFAULT_FOREGROUND);
        defaultRowBackground = UIManager.getColor(UIThemeName.RDISPLAYTABLE_DEFAULT_BACKGROUND);
        alternateRowBackground = UIManager.getColor(UIThemeName.RDISPLAYTABLE_ALTERNATE_BACKGROUND);

        if (tableHeaderFont == null) {
            tableHeaderFont = new Font("Verdana", Font.BOLD, 10);
        }
        if (tableHeaderBackground == null) {
            tableHeaderBackground = Color.WHITE;
        }
        if (tableHeaderForeground == null) {
            tableHeaderForeground = Color.BLACK;
        }
        if (defaultRowForeground == null) {
            defaultRowForeground = Color.BLACK;
        }
        if (defaultRowBackground == null) {
            defaultRowBackground = Color.WHITE;
        }
        if (alternateRowBackground == null) {
            alternateRowBackground = Color.GRAY;
        }
    }

    /******************************************************************************************
     * Retrieves the RDisplayTableModel from the table.
     * <p>
     *@return The RDisplayTableModel assigned to the table.
     *****************************************************************************************/
    private RDisplayTableModel getTableModel() {
        return (RDisplayTableModel) getModel();
    }

    /******************************************************************************************
     * Initializes the row selection mode.
     *****************************************************************************************/
    private void initRowSelectionMode() {
        setColumnSelectionAllowed(false);
        setRowSelectionAllowed(true);
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    /******************************************************************************************
     * Assigns a table row displayer to this table.
     * <p>
     *@return The TableRowDisplayer assigned to the table.
     *****************************************************************************************/
    protected void setTableRowDisplayer(TableRowDisplayer displayer) {
        if (displayer == null) {
            throw new IllegalArgumentException("TableRowDisplayer cannot be null!");
        }
        tableRowDisplayer = displayer;
        columnHeaderArray = tableRowDisplayer.getHeaders();

        if (columnHeaderArray.length == 0) {
            throw new IllegalArgumentException("TableRowDisplayer must contain headers!");
        }

        try {
            columnTypeArray = tableRowDisplayer.getColumnTypes();
        } catch (UIException exception) {
            displayInnerException(exception);
            columnTypeArray = new int[columnHeaderArray.length];
            for (int i = 0; i < columnTypeArray.length; i++) {
                columnTypeArray[i] = DataTypeConstants.TEXT;
            }
        }
        int[] sizes = tableRowDisplayer.getColumnSizes();

        setModel(new RDisplayTableModel(columnHeaderArray, 0));

        for (int i = 0; i < columnHeaderArray.length; i++) {
            buildColumn(columnHeaderArray[i], columnTypeArray[i], sizes[i], sizes[i], sizes[i]);
        }
        columnBackgroundArray = new Color[columnHeaderArray.length];
        columnForegroundArray = new Color[columnHeaderArray.length];
    }

    /******************************************************************************************
     * Sets a filter change listener on the table. If a new filter is selected, this listener
     * is notified.
     * <p>
     * @param listener The listener to assign.
     *****************************************************************************************/
    public void setFilterChangeListener(PropertyChangeListener listener) {
        filterChangeListener = listener;
    }

    /******************************************************************************************
     * Assigns an action to be sent when a row is selected through key navigation or a single
     * click. The command will be sent to the listener.
     * <p>
     * @param listener The listener to assign to the table to receive single click actions.
     * @param command The command to send in the action event.
     *****************************************************************************************/
    protected void registerSingleClickAction(REventListener listener, String command) {
        singleActionListener = listener;
        singleActionCommand = command;
    }

    /******************************************************************************************
     * Assigns an action to be sent when a row is double-clicked or a row is selected and the
     * enter key is pressed. The command will be sent to the listener inside an RActionEvent.
     * <p>
     * @param listener The listener to assign to the table to receive single click actions.
     * @param command The command to send in the action event.
     *****************************************************************************************/
    protected void registerDoubleClickAction(REventListener listener, String command) {
        doubleActionListener = listener;
        doubleActionCommand = command;
    }

    /******************************************************************************************
     * Builds a table column. In the case of a column without a header title to display, the
     * column is assigned its index internally for reference. No header title will be displayed.
     * <p>
     * For sizes: -1 means do not set this value, 0 means set to the size of the column headers,
     * and a positive number means set to that value.
     * <p>
     *@param columnName The name of the column.
     *@param columnType The column type to assign to cells in the column.
     *@param minSize Minimum width in pixels the column should allow.
     *@param prefSize Preferred width in pixels the column should have upon creation.
     *@param maxSize Maximum width in pixels the column should allow.
     *****************************************************************************************/
    private void buildColumn(String name, int type, int minSize, int prefSize, int maxSize) {
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

    /******************************************************************************************
     * Validates the width of the header versus the available width and assigns a scrollbar
     * to the table if necessary.
     * <p>
     * @param width Column width to add to the total header size.
     *****************************************************************************************/
    private void validateHeaderWidth(int columnWidth) {
        totalHeaderWidth += columnWidth;

        if (totalAvailableWidth > 0 && totalHeaderWidth > totalAvailableWidth) {
            setAutoResizeMode(AUTO_RESIZE_OFF);
        }
    }

    //    /******************************************************************************************
    //     * Assigns a formatted mask to all table cells within a column.
    //     * <p>
    //     * @param columnName The name of the column to assign the mask to.
    //     * @param formatMask The format mask to assign to the column.
    //     *****************************************************************************************/
    //    public void setMask(String columnName, Mask formatMask) {
    //       try {
    //            TableColumn tableColumn = getColumn(columnName);
    //            Object renderer = tableColumn.getCellRenderer();
    //            if (renderer instanceof RTableCellRenderer) {
    //            	((RTableCellRenderer) renderer).setMask(formatMask);
    //            }
    //        } catch (Exception exception) {
    //            throw new IllegalArgumentException("An invalid column name was used.");
    //        }
    //    }

    /******************************************************************************************
     * Implements the RTableHeaderInterface by returning all the colors.
     *****************************************************************************************/
    public Color getTableHeaderBackground() {
        return tableHeaderBackground;
    }

    public Color getTableHeaderForeground() {
        return tableHeaderForeground;
    }

    public Font getTableHeaderFont() {
        return tableHeaderFont;
    }

    public Color getRowBackground() {
        return defaultRowBackground;
    }

    public Color getRowForeground() {
        return defaultRowForeground;
    }

    public Color getAlternateRowBackground() {
        return alternateRowBackground;
    }

    /******************************************************************************************
     * Implements the RTableColumnInterface by returning the column background for a cell.
     *****************************************************************************************/
    public Color getColumnBackground(int row, int column) {
        if (columnBackgroundArray[column] != null) {
            return columnBackgroundArray[column];
        }
        if (Math.IEEEremainder(row, 2d) == 0d) {
            return defaultRowBackground;
        }
        return alternateRowBackground;
    }

    /******************************************************************************************
     * Implements the RTableColumnInterface by returning the column foreground for a cell.
     *****************************************************************************************/
    public Color getColumnForeground(int row, int column) {
        if (columnForegroundArray[column] != null) {
            return columnForegroundArray[column];
        }
        return defaultRowForeground;
    }

    /*****************************************************************************************
     * Retrieves all the objects stored within the entire table. This returns an empty list
     * if the table is empty.
     * <p>
     *@return A list of data objects stored within the table.
     *****************************************************************************************/
    protected List getAllData() {
        return getAllData(convertColumnIndexToModel(0));
    }

    /*****************************************************************************************
     * Retrieves all the objects stored within the given column for the entire table. This
     * returns an empty list if the table is empty.
     * <p>
     *@param column The column index to retrieve the data object from.
     *@return A list of data objects stored within the table.
     *****************************************************************************************/
    private List getAllData(int column) {
        List list = new ArrayList<>();
        int count = getRowCount();
        for (int i = 0; i < count; i++) {
            list.add(getData(i, column));
        }
        return list;
    }

    /*****************************************************************************************
     * Retrieves all the selected objects stored within the given column. This returns an empty
     * list if the table is empty.
     * <p>
     *@param columnName The column name to retrieve the data object from.
     *@return A list of selected objects stored within the table.
     *****************************************************************************************/
    protected List getAllSelectedData() {
        return getAllSelectedData(convertColumnIndexToModel(0));
    }

    /*****************************************************************************************
     * Retrieves all the selected objects stored within the given column. This returns an empty
     * list if the table is empty.
     * <p>
     *@param columnName The column name to retrieve the data object from.
     *@return A list of selected objects stored within the table.
     *****************************************************************************************/
    private List getAllSelectedData(int column) {
        List list = new ArrayList<>();
        int[] rows = getSelectedRows();
        for (int row : rows) {
            list.add(getData(row, column));
        }
        return list;
    }

    /*****************************************************************************************
     * Retrieves the data object stored in a cell for the selected row and the given column index.
     * <p>
     *@param row The row number of the cell to retrieve the data object from.
     *@param column The column index of the cell to retrieve the data object from.
     *@return A data object stored within the table cell.
     *****************************************************************************************/
    protected Object getData(int row, int column) {
        return ((RDisplayTableCell) getValueAt(row, column)).getData();
    }

    /*****************************************************************************************
     * Gathers all the data inside the table (without turning it into a string array). This
     * will return a giant list of arrays of RDisplayTableCell objects. Each array represents
     * a row and each RDisplayTableCell represents a single cell.
     * <p>
     *@return An list containing rows of data represented by RDisplayTableCell arrays.
     *****************************************************************************************/
    protected List getFullTableData() {
        List dataList = new ArrayList<>();
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

    /*****************************************************************************************
     * Sets the data to store in the cell.
     * <p>
     *@param row The row index to store the data in.
     *@param column The column index to store the data in.
     *@param object The data object to store in the cell.
     *****************************************************************************************/
    protected void setData(int row, int column, Object object) {
        RDisplayTableCell tableCell = (RDisplayTableCell) getValueAt(row, column);
        tableCell.setData(object);
        setValueAt(tableCell, row, column);
    }

    /*****************************************************************************************
     * Sets a single row as the row selection by row number (which begins at zero).
     * <p>
     *@param row The row number to select.
     *****************************************************************************************/
    protected void setRowSelection(int row) {
        if (row > -1 && row < getRowCount()) {
            setRowSelectionInterval(row, row);
        }
    }

    /*****************************************************************************************
     * Adds a row of data to the table. This method accepts an array of RDisplayTableCell that
     * represents a single row. Each RDisplayTableCell within the array represents a single cell.
     * This method causes the table to repaint. If many rows are going to be added consecutively,
     * all the string arrays should be built prior to adding any to the table for optimized
     * performance.
     * <p>
     *@param rowArray An array of RDisplayTabelCell objects representing a single table row.
     *****************************************************************************************/
    protected void addRow(RDisplayTableCell[] rowArray) {
        getTableModel().addRow(rowArray);
    }

    /*****************************************************************************************
     * Adds a collection of data objects to the table. This method will only function if a
     * SelectableTableRowDisplayer has been assign to this table.
     * <p>
     *@param collection A collection of data objects.
     *****************************************************************************************/
    protected void addRows(Collection collection) {
        if (tableRowDisplayer == null) {
            throw new UnsupportedOperationException("No TableRowDisplayer has been assigned!");
        }
        List objectList = new ArrayList<>();
        List rowList = new ArrayList<>();

        for (Object object : collection) {
            try {
                objectList.add(object);
                rowList.add(buildRow(tableRowDisplayer.buildRow(object)));
            } catch (Throwable exception) {
                displayInnerException(exception);
                return;
            }
        }

        int size = objectList.size();
        int column = 0;
        try {
            for (int i = 0; i < size; i++) {
                addRow((RDisplayTableCell[]) rowList.get(i));
                setData(getLastRowNumber(), column, objectList.get(i));
            }
        } catch (Throwable exception) {
            throw new IllegalStateException("Unable to add row!");
        }
    }

    /******************************************************************************************
     * Removes a row from the table.
     * <p>
     *@param rowNumber The row number of the table row to remove.
     *****************************************************************************************/
    protected void removeRow(int rowNumber) {
        if (rowNumber > -1) {
            removeRowSelectionInterval(rowNumber, rowNumber);
            getTableModel().removeRow(rowNumber);
        }
    }

    /*****************************************************************************************
     * Removes the currently selected row from the table. This should probably only be used
     * in single selection mode as in multi-selection tables, it removes only the first
     * selected row and ignores each additional row.
     *****************************************************************************************/
    protected void removeSelectedRow() {
        removeRow(getSelectedRow());
    }

    /******************************************************************************************
     * Removes all currently selected rows from the table. This remove occurs from the bottom
     * of the table backwards as everything is reorganized when a row is removed.
     *****************************************************************************************/
    protected void removeSelectedRows() {
        int[] selectedRows = getSelectedRows();
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            removeRow(selectedRows[i]);
        }
    }

    /******************************************************************************************
     * Retrieves true if the table has any selected rows, false otherwise.
     * <p>
     * @return True if the table has selected rows, false otherwise.
     *****************************************************************************************/
    protected boolean hasSelectedRows() {
        return getSelectedRowCount() > 0;
    }

    /******************************************************************************************
     * Retrieves the last row number of the table.
     * <p>
     * @return The last row number of the table.
     *****************************************************************************************/
    protected int getLastRowNumber() {
        return getRowCount() - 1;
    }

    /*****************************************************************************************
     * Builds a default formatted table row from an array of cell values.
     * <p>
     *@param dataArray A string array representing a row of values to be properly formatted.
     *@param color The color to set the cell display to for the entire row.
     *@return An array containing a row with a single RDisplayTableCell for each cell.
     *****************************************************************************************/
    private RDisplayTableCell[] buildRow(String[] dataArray) {
        RDisplayTableCell[] row = new RDisplayTableCell[dataArray.length];
        try {
            RDisplayTableCell tableCell;
            for (int i = 0; i < dataArray.length; i++) {
                tableCell = new RDisplayTableCell();
                tableCell.setValue(dataArray[i]);
                tableCell.setType(columnTypeArray[i]);
                row[i] = tableCell;
            }
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to build row!");
        }
        return row;
    }

    /*****************************************************************************************
     * Clears the entire table of rows. It calls repaint because removing rows does not
     * automatically trigger a repaint, but we want it to anyway. This method does not trigger
     * any events if rows were programmatically de-selected. This method should only be used
     * in the process of refreshing a table.
     *****************************************************************************************/
    protected void resetTable() {
        resettingTable = true;
        clearSelection();
        getTableModel().clear();
        refreshTableHeader();
        resettingTable = false;
        repaint();
    }

    /*****************************************************************************************
     * Returns true if the table is empty.
     *****************************************************************************************/
    protected boolean isEmpty() {
        return getRowCount() == 0;
    }

    /*****************************************************************************************
     * Empty implementation of the key listener interface method.
     *****************************************************************************************/
    public void keyTyped(KeyEvent keyEvent) {
    }

    /*****************************************************************************************
     * Empty implementation of the key listener interface method.
     *****************************************************************************************/
    public void keyReleased(KeyEvent keyEvent) {
    }

    /*****************************************************************************************
     * Implements the key listener interface 'key pressed' method. This method checks to see
     * if the tab key was pressed, and if so, it transfers the focus to the next focusable
     * component. If ALT-HotKey is pressed, the appropriate column is sorted. If ENTER is
     * pressed, then the table row selected action is triggered.
     * <p>
     *@param event Details about the key event that occurred.
     *****************************************************************************************/
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

    /******************************************************************************************
     * Method is executed when the shift-tab key is pressed within a RDisplayTable. It
     * transfers the focus to the previous component (if one is assigned)..
     *****************************************************************************************/
    private void doShiftTabPressed() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent(this);
    }

    /******************************************************************************************
     * Method is executed when the tab key is pressed within a RDisplayTable. It transfers the
     * focus to the next component.
     *****************************************************************************************/
    private void doTabPressed() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(this);
    }

    /******************************************************************************************
     * Overrides the JTable list selection event and notifies single action listener of the
     * event. This method strips out adjusting values.
     *****************************************************************************************/
    public void valueChanged(ListSelectionEvent event) {
        super.valueChanged(event);
        if (!event.getValueIsAdjusting()) {
            doSingleClickAction();
        }
    }

    /*****************************************************************************************
     * Empty implementation of the mouse listener interface methods.
     *****************************************************************************************/
    public void mousePressed(MouseEvent event) {
    }

    public void mouseReleased(MouseEvent event) {
    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }

    /*****************************************************************************************
     * Implement the mouse listener interface 'mouse clicked' method. If the time event is
     * identical to the last event, then do nothing. If a double click occurs within a row,
     * the table listener is informed. If a single click occurs within a column header, the
     * table is sorted on that column.  If the same column is clicked again, the sort order
     * is reversed.
     * <p>
     *@param mouseEvent Details about the mouse event that occurred.
     *****************************************************************************************/
    public void mouseClicked(MouseEvent mouseEvent) {
        if (storeMouseEvent != null) {
            long oldEvent = storeMouseEvent.getWhen();
            long newEvent = mouseEvent.getWhen();
            if (oldEvent == newEvent) {
                return;
            }
        }
        Object object = mouseEvent.getComponent();

        if (SwingUtilities.isRightMouseButton(mouseEvent) && filterChangeListener != null) {
            displayFilterDialog(object, mouseEvent.getPoint());
        } else if (object instanceof JTableHeader) {
            sortLeftClick((JTableHeader) object, mouseEvent.getPoint());
        } else if (object instanceof JTable && mouseEvent.getClickCount() == 2) {
            doDoubleClickAction();
        }
        storeMouseEvent = mouseEvent;
    }

    /*****************************************************************************************
     * Pops up the display table menu item and the indicated location.
     *****************************************************************************************/
    protected void displayFilterDialog(Object object, Point point) {
        if (filterDialog == null) {
            Container container = getTopLevelAncestor();

            if (container instanceof JFrame) {
                filterDialog = new RListOfValuesFilterDialog((JFrame) container);
            } else if (container instanceof JDialog) {
                filterDialog = new RListOfValuesFilterDialog((JDialog) container);
            }
            filterDialog.addPropertyChangeListener(filterChangeListener);
        }
        int column;
        if (object instanceof JTableHeader) {
            column = ((JTableHeader) object).columnAtPoint(point);
        } else {
            column = ((JTable) object).columnAtPoint(point);
        }
        column = convertColumnIndexToModel(column);

        WindowPlacer.alignLocation(this, filterDialog, point);

        filterDialog.setColumn(getColumnName(column), columnHeaderArray);
        filterDialog.setVisible(true);
    }

    /*****************************************************************************************
     * Displays an internal exception, which is different from an external exception. This
     * usually comes from a selectabledisplayer or some such thing.
     ****************************************************************************************/
    private void displayInnerException(Throwable exception) {
        UIStatusUtility.displayException(this, exception);
    }

    /*****************************************************************************************
     * Resorts the table.
     *****************************************************************************************/
    protected void resort() {
        handleSortAction();
    }

    /*****************************************************************************************
     * Performs the left mouse click action, sorting a column if it is not locked.
     * <p>
     * @param header The table header where a mouse click occurred.
     * @param point A point registered from a mouse event (located within the header).
     *****************************************************************************************/
    private void sortLeftClick(JTableHeader header, Point point) {
        handleSortAction(header.columnAtPoint(point));
    }

    /*****************************************************************************************
     * Retrieves the TableSortElement currently associated with the table.
     * <p>
     * @return The TableSortElement.
     *****************************************************************************************/
    public TableSortElement getTableSortElement() {
        int column = sortColumnArray[0];
        return new TableSortElement(columnHeaderArray[column], columnTypeArray[column], sortOrderArray[0]);
    }

    /*****************************************************************************************
     * Retrieves the sort number for a given column.
     * <p>
     * @param column The column number
     * <p>
     * @return The numerical sequence number of the sorted column (-1 if column not sorted).
     *****************************************************************************************/
    public int getSortNumber(int column) {
        if (column == sortColumnArray[0]) {
            return 1;
        }
        return -1;
    }

    /*****************************************************************************************
     * Retrieves the sort ascending flag for a given column.
     * <p>
     * @param column The column number.
     * <p>
     * @return True if the column is sorted ascending, false if descending.
     *****************************************************************************************/
    public boolean getSortAscending(int column) {
        if (column == sortColumnArray[0]) {
            return sortOrderArray[0];
        }
        return ASCENDING;
    }

    /*****************************************************************************************
     * Handles sorting the data inside a table on the columns selected. A selected column
     * is triggered by single-clicking on the column header (or by pressing the hot key).
     * <p>
     *@param hotkey The ALT-hotkey that was pressed to trigger this action.
     *****************************************************************************************/
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

    /*****************************************************************************************
     * Handles sorting the data inside a table on the columns selected. A selected column
     * is triggered by single-clicking on the column header (or by pressing the hot key).
     * <p>
     *@param column The column number of the selected column.
     *****************************************************************************************/
    private void handleSortAction(int column) {
        if (column == sortColumnArray[0]) {
            sortOrderArray[0] = !sortOrderArray[0];
        }
        sortColumnArray[0] = column;
        handleSortAction();
    }

    /*****************************************************************************************
     * Handles sorting the data inside a table on the columns selected. A selected column
     * is triggered by single-clicking on the column header (or by pressing the hot key).
     * This method also tracks any row that was selected and resets the selection after
     * cleaning the table and redisplaying the sorted information.
     * <p>
     *@param selectedColumn The column number of the selected column.
     *****************************************************************************************/
    private void handleSortAction() {
        try {
            List tableList = getFullTableData();
            int selectedRow = getSelectedRow();
            Object selected = null;

            if (selectedRow != -1) {
                selected = tableList.get(selectedRow);
            }
            tableRowSorter.sortTable(tableList, sortColumnArray, sortOrderArray);

            resettingTable = true;
            getTableModel().clear();
            resettingTable = false;

            for (Object row : tableList) {
                addRow((RDisplayTableCell[]) row);
            }
            setRowSelection(tableList.indexOf(selected));
            refreshTableHeader();
        } catch (Throwable exception) {
            throw new IllegalStateException("Table failed to sort!");
        }
    }

    /******************************************************************************************
     * Redraws the header of the table. This is often called when the sort icons have been
     * changed.
     *****************************************************************************************/
    protected void refreshTableHeader() {
        getTableHeader().invalidate();
        getTableHeader().repaint();
    }

    /******************************************************************************************
     * Notifies the single-click action listener if a row has been selected.
     *****************************************************************************************/
    private void doSingleClickAction() {
        if (singleActionListener != null && singleActionCommand != null && !resettingTable) {
            singleActionListener.performActionEvent(new RActionEvent(this, singleActionCommand));
        }
    }

    /******************************************************************************************
     * Notifies a double-click action listener if a row has been double-clicked.
     *****************************************************************************************/
    private void doDoubleClickAction() {
        if (doubleActionListener == null || doubleActionCommand == null || resettingTable) {
            return;
        }
        if (getSelectedRowCount() > 0) {
            doubleActionListener.performActionEvent(new RActionEvent(this, doubleActionCommand));
        }
    }
}
