package oracle.retail.sim.client.swing.table;

/********************************************************************************************************
 * This class is a representation of a sort request. It is simply a data holder to hold on to a column
 * identifier (index) and whether the sort is to be ascending or descending.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimTableSortCriteria {

    private int columnIndex;
    private boolean ascending = true;

    /****************************************************************************************************
     * Create a new SortCriteria object.
     * <p>
     * @param columnIndex The column to be sorted on.
     * @param ascending Whether this criteria is for an ascending or descending sort.
     ***************************************************************************************************/
    public SimTableSortCriteria(int columnIndex, boolean ascending) {
        this.columnIndex = columnIndex;
        this.ascending = ascending;
    }

    /****************************************************************************************************
     * Gets the column index for this criteria.
     ***************************************************************************************************/
    public int getColumnIndex() {
        return columnIndex;
    }

    /****************************************************************************************************
     * Determines if this criteria is specifying an ascending or descending sort.
     ***************************************************************************************************/
    public boolean isAscending() {
        return ascending;
    }

    /****************************************************************************************************
     * Sets whether this criteria specifies an ascending or a descending sort.
     ***************************************************************************************************/
    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    /****************************************************************************************************
     * Display string for the column sort criteria
     ***************************************************************************************************/
    public String toString() {
        return "[ " + columnIndex + " " + ascending + " ]";
    }
}
