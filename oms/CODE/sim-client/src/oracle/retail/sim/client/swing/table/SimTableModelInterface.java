package oracle.retail.sim.client.swing.table;

import javax.swing.table.TableModel;

/********************************************************************************************************
 * A sortable interface to be placed on top of a table model. This allows some connections to take place
 * between the model and the table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public interface SimTableModelInterface extends TableModel {

    /****************************************************************************************************
     * Determines if the given column should be sortable. If this returns false, then the user will be
     * unable to sort on the given column.
     ***************************************************************************************************/
    boolean isSortable(int columnIndex);

    /****************************************************************************************************
     * Sorts on the given column. This is a convenience method that should probably delegate the call to
     * the subsequent sort method. It will likely be simpler for a "simple" sort (involving a single
     * column) to be invoked through this method through the UI.
     ***************************************************************************************************/
    void sort(int columnIndex);

    /****************************************************************************************************
     * Sorts the table model on the given criteria.
     * <p>
     * @param criteria an array of SortCriteria object. Each element in the the array specifies a given
     *            column (by index,) and whether the column should be sorted ascending or descending.
     *            Using this collection, it is possible for the table to be sorted on multiple criteria.
     ***************************************************************************************************/
    void sort(SimTableSortCriteria[] criteria);

    /****************************************************************************************************
     * Gets the current sort criteria. This is how the table is currently sorted. This method may return
     * null if the table is not currently sorted in any way.
     ***************************************************************************************************/
    SimTableSortCriteria[] getSortCriteria();
}
