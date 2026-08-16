package oracle.retail.sim.client.swing.table;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.logging.LogService;

/********************************************************************************************************
 * This is a table model which is able to use reflection on objects inside it and know what to display.
 * For this reason, this table model needs to know about the class which it will hold - this is the type
 * of object for each row in the table, and needs to be supplied in the constructor.
 *
 * The columns are known about by creating instances of the SimTableColumn class and adding them to this
 * model. Without these, the model will not know which columns should be presented to the table object.
 *
 * Arrays are not supported in the table to the class type may not be an array. (i.e. competitorStore[1],
 * competitorStore[2]).
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimTableModel extends AbstractTableModel implements TableModel {
    private static final long serialVersionUID = 2543327080606344082L;

    private boolean isEditable = true;
    private List<Object> tableRows = new ArrayList<>();
    private List<SimTableColumn> tableColumns = new ArrayList<>();
    private List<String> overrideEditableAttributes = new ArrayList<>();
    private SimTableData tableData;
    private SimTableRowComparator sorter;
    private SimTableSortCriteria[] defaultSortCriteria;
    private SimTableSortCriteria[] configSortCriteria;

    /****************************************************************************************************
     * Creates a new table model for the given model object type.
     * @param clazz The Class of the object that will be used to represent row values in the table.
     ***************************************************************************************************/
    public SimTableModel(Class clazz) {
        tableData = new SimTableData(clazz);
        sorter = new SimTableRowComparator(this);
    }

    /****************************************************************************************************
     * Assigns editable override attributes that do not require a set() method to be activated.
     ***************************************************************************************************/
    public void setOverrideEditableAttributes(List<String> attributes) {
        if (attributes != null) {
            overrideEditableAttributes = attributes;
        }
    }

    /****************************************************************************************************
     * Adds a row to the table model.
     * @param value The object to add.
     ***************************************************************************************************/
    public void addRow(Object value) {
        tableRows.add(value);
        fireTableRowsInserted(tableRows.size(), tableRows.size());
    }

    /****************************************************************************************************
     * Adds a collection of rows to the table model.
     ***************************************************************************************************/
    public void addRows(Collection values) {
        tableRows.addAll(values);
        fireTableRowsInserted(tableRows.size() - values.size(), tableRows.size());
    }

    /****************************************************************************************************
     * Replaces the rows in the table model. Any old rows will be removed as a result of this call. The
     * table is re-sorted when this call is finished.
     ***************************************************************************************************/
    public void setRows(Collection values) {
        doClearRows();
        for (Object value : values) {
            if (value != null) {
                tableRows.add(value);
            }
        }
        sort();
        fireTableDataChanged();
    }

    /****************************************************************************************************
     * Removes the specified row from the model. This will fire the appropriate events so the associated
     * table will update. It is extremely important that the object provide an appropriate equals()
     * method on it.
     ***************************************************************************************************/
    public boolean removeRow(Object value) {
        int index = tableRows.indexOf(value);
        if (index >= 0) {
            tableRows.remove(index);
            fireTableRowsDeleted(index, index);
            return true;
        }
        return false;
    }

    /****************************************************************************************************
     * Removes the specified row from the model. This will fire the appropriate events so the associated
     * table will update. It is extremely important that the object provide an appropriate equals()
     * method on it.
     ***************************************************************************************************/
    public boolean updateRow(Object value) {
        int index = tableRows.indexOf(value);
        if (index >= 0) {
            tableRows.remove(index);
            tableRows.add(index, value);
            fireTableRowsUpdated(index, index);
            return true;
        }
        return false;
    }

    /****************************************************************************************************
     * Inserts a specified value into the model. It will be placed at the row number indicated by index
     * parameter. This method will not re-sort the table after the information is inserted.
     ***************************************************************************************************/
    public boolean insertRow(int index, Object value) {
        if (index < 0) {
            return false;
        }
        if (index >= tableRows.size()) {
            addRow(value);
            return true;
        }
        tableRows.add(index, value);
        fireTableDataChanged();
        return true;
    }

    /****************************************************************************************************
     * Clears all the rows in the table.
     ***************************************************************************************************/
    public void clearRows() {
        doClearRows();
        fireTableDataChanged();
    }

    /****************************************************************************************************
     * Clears the column definitions in the table.
     ***************************************************************************************************/
    public void clearColumns() {
        doClearColumns();
        fireTableStructureChanged();
    }

    /****************************************************************************************************
     * Adds the column if it does not already exist.
     ***************************************************************************************************/
    public void addColumn(SimTableColumn column) {
        if (!tableColumns.contains(column)) {
            tableColumns.add(column);
            fireTableStructureChanged();
        }
    }

    /****************************************************************************************************
     * Adds the column to the specified index if it does not already exist.
     ***************************************************************************************************/
    public void addColumn(int index, SimTableColumn column) {
        if (!tableColumns.contains(column)) {
            tableColumns.add(index, column);
            fireTableStructureChanged();
        }
    }

    /****************************************************************************************************
     * Removes the column if it exists.
     * <p>
     * @return Whether the column was removed from the column list or not. False usually means that the
     *         column did not exist in the model in the first place.
     ***************************************************************************************************/
    public boolean removeColumn(SimTableColumn column) {
        if (tableColumns.remove(column)) {
            fireTableStructureChanged();
            return true;
        }
        return false;
    }

    /****************************************************************************************************
     * Adds a collection of columns to the table struction.
     ***************************************************************************************************/
    public void addColumns(Collection columns) {
        tableColumns.addAll(columns);
        fireTableStructureChanged();
    }

    /****************************************************************************************************
     * Resets the column collection to columns.
     ***************************************************************************************************/
    public void setColumns(Collection columns) {
        doClearColumns();
        tableColumns.addAll(columns);
        fireTableStructureChanged();
    }

    /****************************************************************************************************
     * Gets the column object for the given index.
     ***************************************************************************************************/
    public SimTableColumn getColumn(int index) {
        return tableColumns.get(index);
    }

    /****************************************************************************************************
     * Gets the index of the column with the given name. The passed in name is the attribute of the
     * column, and not the value that is actually displayed.
     ***************************************************************************************************/
    public int findColumn(String attribute) {
        int counter = 0;
        for (SimTableColumn tableColumn : tableColumns) {
            if (tableColumn.getAttribute().equals(attribute)) {
                return counter;
            }
            counter++;
        }
        return -1;
    }

    /****************************************************************************************************
     * Gets the index of the column with the given title. The passed in value is the title of the column.
     * Reasonably, columns should not have two identical columns.
     ***************************************************************************************************/
    public int findColumnByTitle(String title) {
        int counter = 0;
        for (SimTableColumn tableColumn : tableColumns) {
            if (tableColumn.getTitle().equals(title)) {
                return counter;
            }
            counter++;
        }
        return -1;
    }

    /****************************************************************************************************
     * Determines if the entire table is editable. If this returns false, then no cells in the table
     * should be editable. If this returns true, then the framework should also check an individual
     * column to see if it is editable, as well as the model object type to look for any "write" methods
     * it knows about. In short, if this returns true, that does not mean the entire table is actually
     * editable.
     ***************************************************************************************************/
    public boolean isTableEditable() {
        return isEditable;
    }

    /****************************************************************************************************
     * Sets the table editable attribute.
     ***************************************************************************************************/
    public void setTableEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    /****************************************************************************************************
     * Determines if the given cell is editable. This will check the following properties in this order:
     * the isTableEditable() return value, if false, then this method returns false, otherwise, if the
     * underlying SimTableColumn object is editable. If this returns false, then this method returns
     * false, otherwise check the tableData bean wrapper to see if the cell is editable.
     * <p>
     * @param rowIndex The row being queried
     * @param columnIndex The column being queried
     * @return Whether this cell is editable or not.
     ***************************************************************************************************/
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (isEditable) {
            SimTableColumn column = getColumn(columnIndex);
            if (column.isEditable()) {
                if (overrideEditableAttributes.contains(column.getAttribute())) {
                    return tableData.isPropertyModifiable(getRow(rowIndex), column.getAttribute());
                }
                return tableData.isModifiable(getRow(rowIndex), column.getAttribute());
            }
        }
        return false;
    }

    /****************************************************************************************************
     * Returns the column name for this column. This value will be translated through the translation
     * services.
     * <p>
     * @param column The column.
     *            <p>
     * @return The translated column name.
     ***************************************************************************************************/
    public String getColumnName(int column) {
        return getColumn(column).getTitle();
    }

    /****************************************************************************************************
     * Returns the class object for the type of object held in this column.
     * <p>
     * @param columnIndex The column being queried
     * @return The correct class for this column.
     ***************************************************************************************************/
    public Class getColumnClass(int columnIndex) {
        SimTableColumn column = getColumn(columnIndex);
        return tableData.getDataType(column.getAttribute());
    }

    /****************************************************************************************************
     * Sets the value on the underlying model object.
     * <p>
     * @param value Value to assign to cell
     * @param rowIndex Row of cell
     * @param columnIndex Column of cell
     ***************************************************************************************************/
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        try {
            tableData.setData(tableRows.get(rowIndex), getColumn(columnIndex).getAttribute(), value);

            fireTableRowsUpdated(rowIndex, rowIndex);
        } catch (InvocationTargetException invocationException) {
            throw new SimTableException(invocationException.getTargetException(), value);
        } catch (IndexOutOfBoundsException iob) {
            // Do Nothing
            LogService.debug(this, "ignoring Excepton");
        } catch (Throwable exception) {
            throw new SimTableException(exception, value);
        }
    }

    /****************************************************************************************************
     * Returns the value for the cell at columnIndex and rowIndex.
     * <p>
     * @param rowIndex The row.
     * @param columnIndex The column whose value is to be queried.
     * @return The value in the specified cell /
     ***************************************************************************************************/
    public Object getValueAt(int rowIndex, int columnIndex) {
        SimTableColumn column = getColumn(columnIndex);
        String attribute = column.getAttribute();
        try {
            return tableData.getData(tableRows.get(rowIndex), attribute);
        } catch (InvocationTargetException invocationException) {
            log(invocationException.getTargetException(), attribute);
        } catch (Throwable throwable) {
            log(throwable, attribute);
        }
        return null;
    }

    /****************************************************************************************************
     * Returns the number of columns in the model. JTable uses this method to determine how many columns
     * it should create and display by default.
     * <p>
     * @return The number of columns in the model.
     ***************************************************************************************************/
    public int getColumnCount() {
        return tableColumns.size();
    }

    /****************************************************************************************************
     * Returns the column titles of the table.
     * <p>
     * @return A List containing the column titles in the table.
     ***************************************************************************************************/
    public List<String> getColumnTitles() {
        int columnCount = getColumnCount();
        List<String> titles = new ArrayList<>(columnCount);
        for (int i = 0; i < columnCount; i++) {
            titles.add(getColumnName(i));
        }
        return titles;
    }

    /****************************************************************************************************
     * Returns the number of rows in the model. A JTable uses this method to determine how many rows it
     * should display. This method should be quick, as it is called frequently during rendering.
     * <p>
     * @return The number of rows in the model
     ***************************************************************************************************/
    public int getRowCount() {
        return tableRows.size();
    }

    /****************************************************************************************************
     * Returns the actual row list, but is unmodifiable.
     ***************************************************************************************************/
    public List getAllRows() {
        return Collections.unmodifiableList(new ArrayList<>(tableRows));
    }

    /****************************************************************************************************
     * Returns the row data object for the given index.
     ***************************************************************************************************/
    public Object getRow(int rowIndex) {
        if (rowIndex < 0) {
            return null;
        }
        if (rowIndex >= tableRows.size()) {
            return null;
        }
        return tableRows.get(rowIndex);
    }

    /****************************************************************************************************
     * Gets the collection of row data objects defined by the passed in indices.
     ***************************************************************************************************/
    public List getRows(int[] indices) {
        if (indices == null) {
            return Collections.emptyList();
        }
        List objectList = new ArrayList<>(indices.length);
        for (int index : indices) {
            objectList.add(getRow(index));
        }
        return objectList;
    }

    /****************************************************************************************************
     * Determines if the given column should be sortable. If this returns false, then the user will be
     * unable to sort on the given column.
     ***************************************************************************************************/
    public boolean isSortable(int columnIndex) {
        return getColumn(columnIndex).isSortable();
    }

    /****************************************************************************************************
     * Assigns the default sort criteria used if no other sort criteria can be determined.
     ***************************************************************************************************/
    public void setDefaultSortCriteria(SimTableSortCriteria[] criteria) {
        defaultSortCriteria = criteria;
    }

    /****************************************************************************************************
     * Assigns the configuration sort criteria for the table. This is the primary sorting criteria.
     ***************************************************************************************************/
    public void setConfigSortCriteria(SimTableSortCriteria[] criteria) {
        configSortCriteria = criteria;
    }

    /****************************************************************************************************
     * Resorts the table model with the same sort criteria as the previous sort. If no previous sort
     * exists, it will use the configuration sort criteria. If this does not exist, it will use the
     * default sort criteria.
     ***************************************************************************************************/
    public void sort() {
        SimTableSortCriteria[] criteria = sorter.getSortCriteria();
        if (criteria == null) {
            criteria = configSortCriteria;
        }
        if (criteria == null) {
            criteria = defaultSortCriteria;
        }
        if (criteria == null) {
            return;
        }
        sort(criteria);
    }

    /****************************************************************************************************
     * Sorts on the index passed into the table. It clears all other sort information.
     ***************************************************************************************************/
    protected void sort(int index) {
        if (index < 0) {
            return;
        }
        SimTableSortCriteria[] criteria = new SimTableSortCriteria[1];
        criteria[0] = new SimTableSortCriteria(index, true);

        SimTableSortCriteria[] oldCriteria = sorter.getSortCriteria();
        if (oldCriteria != null) {
            for (SimTableSortCriteria oldCriterion : oldCriteria) {
                if (oldCriterion.getColumnIndex() == index) {
                    criteria[0].setAscending(!oldCriterion.isAscending());
                }
            }
        }
        sort(criteria);
    }

    /****************************************************************************************************
     * Sorts the table model using the given criteria.
     * <p>
     * @param criteria An array of SortCriteria object. Each element in the array specifies a given
     *            column (by index) and whether the column should be sorted ascending or descending.
     *            Using this collection, it is possible for the table to be sorted on multiple criteria.
     ***************************************************************************************************/
    public void sort(SimTableSortCriteria[] criteria) {
        sorter.setSortCriteria(criteria);
        Collections.sort(tableRows, sorter);
        fireTableDataChanged();
    }

    /****************************************************************************************************
     * Gets the current sort criteria. This is how the table is currently sorted. This method may return
     * null if the table is not currently sorted in any way.
     ***************************************************************************************************/
    public SimTableSortCriteria[] getSortCriteria() {
        return sorter.getSortCriteria();
    }

    /****************************************************************************************************
     * Clears out all the sort criteria. The next sort will attempt to use configuration and then default
     * values.
     ***************************************************************************************************/
    public void clearSortCriteria() {
        sorter.setSortCriteria(null);
    }

    /****************************************************************************************************
     * Checks if this model contains the given data object in its model.
     ***************************************************************************************************/
    public boolean containsRow(Object row) {
        for (Object tableRow : tableRows) {
            if (row == null) {
                if (tableRow == null) {
                    return true;
                }
            } else if (row.equals(tableRow)) {
                return true;
            }
        }
        return false;
    }

    /****************************************************************************************************
     * Returns the index of the row containing the data object if it exists in the table, -1 otherwise.
     * @param value The data object.
     * @return The row index if the data exists in the table, -1 otherwise.
     ***************************************************************************************************/
    public int getRowIndex(Object value) {
        if (value != null) {
            return tableRows.indexOf(value);
        }
        return -1;
    }

    /****************************************************************************************************
     * Gets the table data definition object used by this table model.
     ***************************************************************************************************/
    public SimTableData getTableData() {
        return tableData;
    }

    /****************************************************************************************************
     * Clears the columns without firing an event. Use this internally if an event will be fired later.
     ***************************************************************************************************/
    private void doClearColumns() {
        tableColumns.clear();
    }

    /****************************************************************************************************
     * Clears all the rows in the table - does not fire any events. Use this internally if an event will
     * be fired later (avoids firing multiple events which could result in multiple table repaints.
     ***************************************************************************************************/
    private void doClearRows() {
        tableRows.clear();
    }

    /****************************************************************************************************
     * Helper method to log an exception.
     ***************************************************************************************************/
    private void log(Throwable exception, String attribute) {
        UILog.error(getClass(), UIMessageText.REFLECTION_EXCEPTION, tableData.getDataType().getName() + "." + attribute, exception);
    }
}
