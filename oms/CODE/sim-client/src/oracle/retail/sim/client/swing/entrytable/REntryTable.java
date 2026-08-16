package oracle.retail.sim.client.swing.entrytable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.RErrorEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RScrollPane;

/********************************************************************************************************
 * REntryTable
 * <p>
 * Entry Table is a table-like component containing a group of rows and columns defined by properties
 * assigned to the table. Each cell contains an editor component.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

/****************************************************************************************************
 * 1) Volume Testing
 * 2) Error Handling
 * 3) Send Action Notices Out Of The Entire Table
 ***************************************************************************************************/

public class REntryTable extends RPanel implements PropertyChangeListener {
    private static final long serialVersionUID = -2703140581832378620L;

    private REntryTableTraits tableTraits = new REntryTableTraits();
    private REntryTableSort tableSorter = new REntryTableSort();

    private JPanel mainPanel = new JPanel();
    private JPanel fillerRow = new JPanel();
    private RScrollPane scrollPane = new RScrollPane(mainPanel);

    private REntryHeaderRow headerRow = new REntryHeaderRow(tableTraits);
    private REntryRow[] rowArray = new REntryRow[10];
    private int rowExpansion = 10;
    private int rowCount;
    private int yCoord = 1;

    private boolean isSortable = true;

    /****************************************************************************************************
     * Creates a new REntryTable.
     ***************************************************************************************************/
    public REntryTable() {
        initializeColors();
        initializeFonts();
        scrollPane.setBorder(null);
        scrollPane.turnHorizontalScrollBarOff();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        initializeLayout();
    }

    /****************************************************************************************************
     * Initializes the colors
     ***************************************************************************************************/
    private void initializeColors() {
        if (tableTraits.getForegroundColor() == null) {
            tableTraits.setForegroundColor(getForeground());
        }
        if (tableTraits.getBackgroundColor() == null) {
            tableTraits.setBackgroundColor(getBackground());
        }
        if (tableTraits.getAlternateBackgroundColor() == null) {
            tableTraits.setAlternateBackgroundColor(Color.WHITE);
        }
        if (tableTraits.getBorderColor() == null) {
            tableTraits.setBorderColor(Color.BLACK);
        }
    }

    /****************************************************************************************************
     * Initializes the fonts
     ***************************************************************************************************/
    private void initializeFonts() {
        setFont(tableTraits.getFont());
    }

    /****************************************************************************************************
     * Initializes the default layout
     ***************************************************************************************************/
    private void initializeLayout() {
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(fillerRow, GridTool.constraints(0, yCoord, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setLineBorder(tableTraits.getBorderColor(), 1, 0);
        setLayout(new BorderLayout(0, 3));
        add(headerRow, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    //	/****************************************************************************************************
    //	 * Retrieves the scroll pane used by the entry table.
    //	 * <p>
    //	 * @return The scroll pane used by the entry table.
    //	 ***************************************************************************************************/
    //	public RScrollPane getScrollPane() {
    //		return scrollPane;
    //	}

    /****************************************************************************************************
     * Assigns a table definition object to the entry table. This is the only way to activate the table.
     * The definition contains the data type and column definitions.
     * <p>
     * @param definition An REntryTableDefinition containing all the information to initialize the table.
     ***************************************************************************************************/
    public void setTableDefinition(REntryTableDefinition definition) {
        tableTraits.setDataClass(definition.getDataClass());
        tableTraits.setColumns(definition.getAttributes());
        redrawTable();
    }

    /****************************************************************************************************
     * Assigns the row height of the table.
     * <p>
     * @param height The row height of the table (in pixels).
     ***************************************************************************************************/
    public void setRowHeight(int height) {
        tableTraits.setRowHeight(height);
    }

    /****************************************************************************************************
     * Assigns a column gap to place between the columns.
     * <p>
     * @param gap The gap (in pixels) to place between the columns.
     ***************************************************************************************************/
    public void setColumnGap(int gap) {
        tableTraits.setColumnGap(gap);
    }

    /****************************************************************************************************
     * Assigns a column width to a specific column.
     * <p>
     * @param title The column title.
     * @param width The width of the column (in pixels).
     ***************************************************************************************************/
    public void setColumnWidth(String title, int width) {
        tableTraits.setColumnWidth(findColumnIndex(title), width);
        redrawTable();
    }

    /****************************************************************************************************
     * Assigns a mnemonic to a column in the header row.
     * <p>
     * @param title The column title.
     * @param mnemonic The mnemonic to assign to the column.
     ***************************************************************************************************/
    public void setMnemonic(String title, char mnemonic) {
        headerRow.setMnemonic(title, mnemonic);
    }

    /****************************************************************************************************
     * Assigns a font to the table. This font is assigned as the default font of all rows and editors
     * within the table.
     * <p>
     * @param font The font.
     ***************************************************************************************************/
    public void setFont(Font font) {
        if (font != null) {
            super.setFont(font);

            if (tableTraits != null) {
                tableTraits.setFont(font);
            }
        }
    }

    /****************************************************************************************************
     * Assigns the foreground color to the table.
     * <p>
     * @param foreground The color.
     ***************************************************************************************************/
    public void setForeground(Color foreground) {
        if (foreground != null) {
            super.setForeground(foreground);

            if (tableTraits != null) {
                tableTraits.setForegroundColor(foreground);
            }
        }
    }

    /****************************************************************************************************
     * Assigns the background color to the table.
     * <p>
     * @param background The color.
     ***************************************************************************************************/
    public void setBackground(Color background) {
        if (background != null) {
            super.setBackground(background);

            if (tableTraits != null) {
                tableTraits.setBackgroundColor(background);
            }
        }
    }

    /****************************************************************************************************
     * Assigns the alternate background color to the table.
     * <p>
     * @param color The color.
     ***************************************************************************************************/
    public void setAlternateBackground(Color color) {
        if (color != null) {
            tableTraits.setAlternateBackgroundColor(color);
        }
    }

    /****************************************************************************************************
     * Assigns the foreground color to a specific column within the table.
     * <p>
     * @param title The column title.
     * @param foreground The foreground color.
     ***************************************************************************************************/
    public void setColumnForeground(String title, Color foreground) {
        if (foreground != null) {
            tableTraits.setColumnForeground(findColumnIndex(title), foreground);
        }
    }

    /****************************************************************************************************
     * Assigns the background color to a specific column within the table.
     * <p>
     * @param title The column title.
     * @param background The background color.
     ***************************************************************************************************/
    public void setColumnBackground(String title, Color background) {
        if (background != null) {
            tableTraits.setColumnBackground(findColumnIndex(title), background);
        }
    }

    /****************************************************************************************************
     * Assigns a font to a specific column of the table.
     * <p>
     * @param title The column title.
     * @param font The font to assign.
     ***************************************************************************************************/
    public void setColumnFont(String title, Font font) {
        if (font != null) {
            tableTraits.setColumnFont(findColumnIndex(title), font);
        }
    }

    /****************************************************************************************************
     * Sets all the rows on the table. This method duplicates all the logic within addRow(), but does so
     * in an efficient manner to produce a faster display of all the rows. This will remove ALL the rows
     * in the table before assigning the new rows.
     * <p>
     * @param rows A list of data objects to display within the table.
     ***************************************************************************************************/
    public void setRows(List rows) {
        int size = rows.size();

        rowArray = new REntryRow[size];
        for (int i = 0; i < size; i++) {
            Object object = rows.get(i);
            rowArray[i] = new REntryRow(tableTraits);
            rowArray[i].addPropertyChangeListener(this);
            rowArray[i].setRowData(object);
            rowArray[i].setRowNumber(rowCount);
        }

        createMainPanel();

        for (REntryRow element : rowArray) {
            mainPanel.add(element, GridTool.constraints(0, yCoord, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
            yCoord++;
            mainPanel.add(fillerRow, GridTool.constraints(0, yCoord, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
            rowCount++;
        }

        mainPanel.revalidate();
    }

    /****************************************************************************************************
     * Adds a list of rows to the table. This loops through the data objects and adds a row for each one.
     * This method is not efficient.
     * <p>
     * @param rows A list of data objects to display within the table.
     ***************************************************************************************************/
    public void addRows(List rows) {
        for (Iterator iterator = rows.iterator(); iterator.hasNext(); ) {
            addRow(iterator.next());
        }
    }

    /****************************************************************************************************
     * Adds a row to the table.
     * <p>
     * @param object The data object that is represented by the row.
     ***************************************************************************************************/
    public void addRow(Object object) {
        validateRowArray();

        rowArray[rowCount] = new REntryRow(tableTraits);
        rowArray[rowCount].addPropertyChangeListener(this);
        rowArray[rowCount].setRowData(object);
        rowArray[rowCount].setRowNumber(rowCount);

        mainPanel.add(rowArray[rowCount], GridTool.constraints(0, yCoord, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        yCoord++;
        mainPanel.add(fillerRow, GridTool.constraints(0, yCoord, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        mainPanel.revalidate();
        rowCount++;
    }

    /****************************************************************************************************
     * Assigns a data object to a particular row.
     * <p>
     * @param row The row number.
     * @param The data object.
     ***************************************************************************************************/
    public void updateRow(int row, Object object) {
        validateRow(row);
        rowArray[row].updateRowData(object);
    }

    /****************************************************************************************************
     * Remove the row containing the given data object.
     * <p>
     * @param object The data object
     ***************************************************************************************************/
    public void removeRow(Object object) {
        if (object != null) {
            removeRow(findRow(object));
        }
    }

    /****************************************************************************************************
     * Remove the row identified by the given row number.
     * <p>
     * @param row The row to remove.
     ***************************************************************************************************/
    public void removeRow(int row) {
        validateRow(row);

        int[] location = rowArray[row].getFocusedLocation();
        for (int i = row; i < rowCount; i++) {
            if (i + 1 == rowCount) {
                rowArray[i] = null;
                rowCount--;
            } else {
                rowArray[i] = rowArray[i + 1];
                rowArray[i].setRowNumber(i);
            }
        }
        redrawTable();
        assignFocus(location);
    }

    /****************************************************************************************************
     * Retrieves the number of rows being displayed by the table.
     * <p>
     * @return The number of rows being displayed by the table.
     ***************************************************************************************************/
    public int getRowCount() {
        return rowCount;
    }

    /****************************************************************************************************
     * Retrieves the last row index of the table.
     * <p>
     * @return The last row index of the table.
     ***************************************************************************************************/
    public int getLastRow() {
        return rowCount - 1;
    }

    /****************************************************************************************************
     * Retrieves whether or not the table is empty.
     * <p>
     * @return True if the table is empty, false if not.
     ***************************************************************************************************/
    public boolean isEmpty() {
        return rowCount == 0;
    }

    /****************************************************************************************************
     * Finds a row index for given object stored within a row.
     * <p>
     * @param object The data object that generated the row.
     * @return The index of the row for the object or false if no row is found.
     ***************************************************************************************************/
    public int findRow(Object object) {
        if (object != null) {
            for (int i = 0; i < rowCount; i++) {
                if (object.equals(rowArray[i].getRowData())) {
                    return i;
                }
            }
        }
        return -1;
    }

    /****************************************************************************************************
     * Retrieves all the stored object within the rows in sequential order.
     * <p>
     * @return A list of all the stored data objects.
     ***************************************************************************************************/
    public List getAllRowData() {
        List dataList = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            if (rowArray[i].getRowData() != null) {
                dataList.add(rowArray[i].getRowData());
            }
        }
        return dataList;
    }

    /****************************************************************************************************
     * Retrieves the stored object within the row. Returns null if row or object does not exist.
     * <p>
     * @param row The row number.
     * @return The object stored within the row.
     ***************************************************************************************************/
    public Object getRow(int row) {
        validateRow(row);
        return rowArray[row].getRowData();
    }

    /****************************************************************************************************
     * Clears the table of all rows. In order for the scrollbar to reset itself, this requires deleting
     * the panel and scrollpane, rebuilding a new reference to a scrollpane and adding it back to the
     * table. The setVisible() method is the only means of getting BorderLayout to accurately redraw
     * itself.
     ***************************************************************************************************/
    public void clear() {
        setVisible(false);
        createMainPanel();
        headerRow.setSortedHeader(-1, true); // Clears All Arrows
        rowArray = new REntryRow[10];
        yCoord = 1;
        rowCount = 0;
        setVisible(true);
    }

    /****************************************************************************************************
     * Retrieves the column index for a particular header.
     * <p>
     * @param header The header title.
     * @return The column index.
     ***************************************************************************************************/
    public int findColumnIndex(String header) {
        return headerRow.getColumnIndex(header);
    }

    /****************************************************************************************************
     * Property change listener definition. Listens for certain property names and executes appropriate
     * method.
     ***************************************************************************************************/
    public void propertyChange(PropertyChangeEvent event) {
        String name = event.getPropertyName();

        if (name.equals(UIPropertyName.ENTRY_TABLE_CONFIGURATION)) {
            doConfiguration();
        } else if (name.equals(UIPropertyName.ENTRY_TABLE_UP_PRESSED)) {
            doUpPressed();
        } else if (name.equals(UIPropertyName.ENTRY_TABLE_DOWN_PRESSED)) {
            doDownPressed();
        } else if (name.equals(UIPropertyName.ENTRY_TABLE_TAB_PRESSED)) {
            doTabPressed((Boolean) event.getNewValue());
        } else if (name.equals(UIPropertyName.ENTRY_TABLE_MNEMONIC_SORT)) {
            doMnemonicSort((String) event.getNewValue());
        } else if (name.equals(UIPropertyName.ENTRY_TABLE_SORT_TRIGGERED)) {
            doIndexSort((Integer) event.getNewValue());
        } else {
            validate();
            repaint();
        }
    }

    /****************************************************************************************************
     * Moves the focus a single row closer to the top of the table. When it reaches the first row of the
     * table, it cycles around to the bottom.
     ***************************************************************************************************/
    private void doUpPressed() {
        int[] array = getFocusedLocation();
        if (array != null) {
            int prevrow = array[0];
            do {
                prevrow--;
                if (prevrow == -1) {
                    prevrow = rowCount - 1;
                }
            } while (!rowArray[prevrow].isColumnTraversable(array[1]));

            rowArray[prevrow].requestColumnFocus(array[1]);
        }
    }

    /****************************************************************************************************
     * Moves the focus a single row closer to the bottom of the table. When it reaches the last row of
     * the table, it cycles around to the top.
     ***************************************************************************************************/
    private void doDownPressed() {
        int[] array = getFocusedLocation();
        if (array != null) {
            int nextrow = array[0];
            do {
                nextrow++;
                if (nextrow == rowCount) {
                    nextrow = 0;
                }
            } while (!rowArray[nextrow].isColumnTraversable(array[1]));

            rowArray[nextrow].requestColumnFocus(array[1]);
        }
    }

    /****************************************************************************************************
     * Retrieves the location of the focus within the table as a row and column.
     * <p>
     * @return The location of the focus within the table as a row and column.
     ***************************************************************************************************/
    private int[] getFocusedLocation() {
        for (int i = 0; i < rowCount; i++) {
            int[] location = rowArray[i].getFocusedLocation();

            if (location != null) {
                return location;
            }
        }
        return null;
    }

    /****************************************************************************************************
     * Try to assign focus to the next available component going forward through the table and then
     * wrapping around.
     * <p>
     * @param object The object to identify the row with.
     ***************************************************************************************************/
    private void assignFocus(int[] location) {
        if (location != null) {
            int row = location[0];
            int column = location[1];
            if (row < rowCount) {
                if (rowArray[row].isColumnTraversable(column)) {
                    rowArray[row].requestColumnFocus(column);
                    return;
                }
            }
            transferFocus();
        }
    }

    /****************************************************************************************************
     * Moves the focus to the requested row and column.
     * <p>
     * @param row The row number.
     * @param title The title of the column.
     ***************************************************************************************************/
    public void setFocusAt(int row, String title) {
        setFocusAt(row, findColumnIndex(title));
    }

    /****************************************************************************************************
     * Moves the focus to the requested row and column.
     * <p>
     * @param row The row number.
     * @param column A column index.
     ***************************************************************************************************/
    public void setFocusAt(int row, int column) {
        validateRow(row);
        rowArray[row].requestColumnFocus(column);
    }

    /****************************************************************************************************
     * Method is executed when the tab key is pressed. It transfers the focus to the correct component.
     ***************************************************************************************************/
    private void doTabPressed(Boolean isShiftTab) {
        if (isShiftTab) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent(this);
        } else {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(this);
        }
    }

    /****************************************************************************************************
     * Sets the table to enabled or disabled state.
     * <p>
     * @param enabled True if the component should be enabled, false if not.
     ***************************************************************************************************/
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        for (int row = 0; row < rowCount; row++) {
            rowArray[row].setEnabled(enabled);
        }
    }

    /****************************************************************************************************
     * Sets the component at the row and column to enabled or disabled state.
     * <p>
     * @param The row number.
     * @param A column header.
     * @param enabled True if the component should be enabled, false if not.
     ***************************************************************************************************/
    public void setEnabledAt(int row, String header, boolean enabled) {
        setEnabledAt(row, findColumnIndex(header), enabled);
    }

    /****************************************************************************************************
     * Sets the compnoent at the row and column to enabled or disabled state.
     * <p>
     * @param row The row number.
     * @param column A column index.
     * @param enabled True if the component should be enabled, false if not.
     ***************************************************************************************************/
    public void setEnabledAt(int row, int column, boolean enabled) {
        validateRow(row);
        rowArray[row].setEnabledAt(column, enabled);
    }

    /****************************************************************************************************
     * Retrieves the enabled state of the component at the specified row and column.
     * <p>
     * @param row The row number.
     * @param header A column header.
     * @param return True if the component at the location is enabled, false otherwise.
     ***************************************************************************************************/
    public boolean isEnabledAt(int row, String header) {
        return isEnabledAt(row, findColumnIndex(header));
    }

    /****************************************************************************************************
     * Retrieves the enabled state of the component at the specified row and column.
     * <p>
     * @param row The row number.
     * @param column A column number.
     * @param return True if the component at the location is enabled, false otherwise.
     ***************************************************************************************************/
    private boolean isEnabledAt(int row, int column) {
        validateRow(row);
        return rowArray[row].isEnabledAt(column);
    }

    /****************************************************************************************************
     * Enables or disables sorting on the table.
     * <p>
     * @param enabled True if the table should allow sorting, false if not
     ***************************************************************************************************/
    public void setSortingEnabled(boolean enabled) {
        isSortable = enabled;
    }

    /****************************************************************************************************
     * Retrieves whether or not the table is sortable.
     * <p>
     * @return True if the table is sortable, false if not.
     ***************************************************************************************************/
    public boolean isSortable() {
        return isSortable && rowCount > 0;
    }

    /****************************************************************************************************
     * Sorts the information in the table based on the column that triggered the sort. It finds which
     * column index to use based on the event, gathers all the primary rows into a list, sorts the
     * information, and redraws the table.
     ***************************************************************************************************/
    private void doMnemonicSort(String mnemonic) {
        if (isSortable()) {
            sort(headerRow.getColumnIndexByMnemonic(mnemonic));
        }
    }

    /****************************************************************************************************
     * Sorts the information in the table based on the column index that triggered the sort. It gathers
     * all the primary rows into a list, and sorts the information using REntryTableSort tool and then
     * rebuilds the row array in sorted order.
     ***************************************************************************************************/
    private void doIndexSort(Integer index) {
        if (isSortable()) {
            sort(index);
        }
    }

    /****************************************************************************************************
     * Sorts the information in the table based on a column index. It gathers all the primary rows into a
     * list, sorts the information using the column index and REntryTableSort tool and rebuilds the row
     * array in sorted order.
     * <p>
     * @param column The column index.
     ***************************************************************************************************/
    public void sort(int column) {
        List dataList = new ArrayList<>();
        dataList.addAll(Arrays.asList(rowArray).subList(0, rowCount));

        boolean sortAscending = headerRow.shouldSortAscending(column);
        String[] attributes = tableTraits.getVisibleAttributes();
        ReflectionWrapper wrapper = tableTraits.getDataWrapper();

        tableSorter.sort(wrapper, dataList, attributes[column], sortAscending);

        rowArray = buildSortedArray(dataList);

        for (int i = 0; i < rowCount; i++) {
            rowArray[i].setRowNumber(i);
        }
        headerRow.setSortedHeader(column, sortAscending);
        redrawTableForSort();
    }

    /****************************************************************************************************
     * Builds a sorted array with children from the sorted list of primary rows.
     * <p>
     * @param dataList A list of sorted primary rows.
     * @return A REntryRow array of all sorted rows including the children rows in the proper order.
     ***************************************************************************************************/
    private REntryRow[] buildSortedArray(List dataList) {
        REntryRow[] sortedArray = new REntryRow[rowArray.length];
        int oldIndex = 0;
        int newIndex = 0;
        for (Iterator iterator = dataList.iterator(); iterator.hasNext(); ) {
            oldIndex = ((REntryRow) iterator.next()).getRowNumber();
            sortedArray[newIndex++] = rowArray[oldIndex];
        }
        return sortedArray;
    }

    /****************************************************************************************************
     * Redraws the table. This rebuilds the entire table from scratch. Very slow.
     ***************************************************************************************************/
    private void redrawTableForSort() {
        Dimension size = getPreferredSize();
        Point point = getLocation();
        setVisible(false);
        displayRows(size, point);
    }

    /****************************************************************************************************
     * Redraws the table. This rebuilds the entire table from scratch. Very slow.
     ***************************************************************************************************/
    private void redrawTable() {
        Dimension size = getPreferredSize();
        Point point = getLocation();
        setVisible(false);
        resetHeader();
        resetTraits();
        displayRows(size, point);
    }

    /****************************************************************************************************
     * Recreates the main panel and scrollpane.
     ***************************************************************************************************/
    private void createMainPanel() {
        remove(scrollPane);

        mainPanel = new JPanel();
        scrollPane = new RScrollPane(mainPanel);
        scrollPane.turnHorizontalScrollBarOff();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(null);

        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(fillerRow, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        add(scrollPane, BorderLayout.CENTER);

        rowCount = 0;
        yCoord = 1;
    }

    /****************************************************************************************************
     * Resets the header row
     ***************************************************************************************************/
    private void resetHeader() {
        remove(headerRow);
        headerRow = new REntryHeaderRow(tableTraits);
        headerRow.addPropertyChangeListener(this);
        add(headerRow, BorderLayout.NORTH);
    }

    /****************************************************************************************************
     * Resets the table traits
     ***************************************************************************************************/
    private void resetTraits() {
        tableTraits.setHeaderColumnWidths(headerRow.getColumnWidths());
    }

    /****************************************************************************************************
     * Displays the rows
     ***************************************************************************************************/
    private void displayRows(Dimension size, Point point) {
        List data = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            data.add(rowArray[i].getRowData());
            rowArray[i] = null;
        }
        createMainPanel();
        for (Iterator iterator = data.iterator(); iterator.hasNext(); ) {
            addRow(iterator.next());
        }
        setPreferredSize(size);
        setLocation(point);
        setVisible(true);
        validate();
        repaint();
    }

    /****************************************************************************************************
     * Validates a row index actually exists in the table.
     * <p>
     * @param row The row index to validate.
     ***************************************************************************************************/
    private void validateRow(int row) {
        if (row < 0 || row >= rowCount) {
            throw new IllegalArgumentException("Invalid Row Index");
        }
    }

    /****************************************************************************************************
     * Validates the row array is large enough to add another object.
     ***************************************************************************************************/
    private void validateRowArray() {
        int oldLength = rowArray.length;
        if (oldLength == rowCount) {
            REntryRow[] newArray = new REntryRow[oldLength + rowExpansion];
            System.arraycopy(rowArray, 0, newArray, 0, oldLength);
            rowArray = newArray;
        }
    }

    /****************************************************************************************************
     * Handles displaying the configuratin dialog.
     ***************************************************************************************************/
    private void doConfiguration() {
        REntryTableDialog dialog = new REntryTableDialog(ApplicationInternal.getFrame());
        Container container = getTopLevelAncestor();
        if (container instanceof JDialog) {
            dialog = new REntryTableDialog((JDialog) container);
        }
        dialog.setColumns(tableTraits.getColumns());
        dialog.addREventListener(buildConfigurationEventListener());
        dialog.setVisible(true);
    }

    /****************************************************************************************************
     * Implements a listener for the configuration window that updates the table traits and redraws the
     * table when the configuration dialog sends a notification.
     ***************************************************************************************************/
    private REventListener buildConfigurationEventListener() {
        return new REventListener() {
            public void performErrorEvent(RErrorEvent event) {
            }

            public void performActionEvent(final RActionEvent event) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        REntryColumn[] tmpColumns = (REntryColumn[]) event.getEventData();
                        tableTraits.setColumns(tmpColumns);
                        int sortColumn = -1;
                        for (int i = 0; i < tmpColumns.length; i++) {
                            if (tmpColumns[i].isPrimarySort() && tmpColumns[i].isVisible()) {
                                sortColumn = i;
                                break;
                            }
                        }
                        if (sortColumn == -1) {
                            resetHeader();
                            redrawTable();
                        } else {
                            resetHeader();
                            sort(sortColumn);
                        }
                    }
                });
            }
        };
    }

    /****************************************************************************************************
     * OLD CODE THAT NEEDS TO BE PORTED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     ***************************************************************************************************/

    // /***************************************************************************************************************
    // * Assigns an action command to a column within the primary row.
    // * <p>
    // * @param label The header label of the column to assign the validator.
    // * @param command The command to assign to the column.
    // **************************************************************************************************************/
    // public void setColumnAction(String label, String command) {
    // setColumnAction(PRIMARY_ROW, findColumnIndex(label), command);
    // }
    //
    // /*******************************************************************************************************************
    // * Assigns an action command to a column within a row level.
    // * <p>
    // * @param level The row level.
    // * @param column The column index.
    // * @param command The command to assign to the column.
    // ******************************************************************************************************************/
    // public void setColumnAction(int level, int column, String command) {
    // validateLevel(level);
    // validateColumn(level, column);
    // rowPropertyArray[level].getColumnProperties(column).setColumnCommand(command);
    // }
    // /******************************************************************************************
    // * Enables or disables the action triggers on all the components within the table.
    // * <p>
    // * @param enabled True if the actions should be enabled, false if they should be ignored.
    // ******************************************************************************************/
    // public void setActionsEnabled(boolean enabled) {
    // actionsEnabled = enabled;
    // }
    //
    // /******************************************************************************************
    // * Retrieves whether or not the
    // * <p>
    // * @param enabled True if the actions should be enabled, false if they should be ignored.
    // ******************************************************************************************/
    // public boolean isActionsEnabled() {
    // return actionsEnabled;
    // }
    //
    // /******************************************************************************************
    // * Passes along the action events from specific rows.
    // ******************************************************************************************/
    // public void performActionEvent(RActionEvent event) {
    // if (actionsEnabled) {
    // notifyREventListeners(event);
    // }
    // }
}
