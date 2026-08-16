package oracle.retail.sim.client.swing.displaytable;

import oracle.retail.sim.client.swing.util.UIException;

/******************************************************************************************
 * Interface must be implemented by any class that wishes to be assigned to a table-of-values
 * editor or RDisplayTable. It controls the display of the information inside the table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public interface TableRowDisplayer {

    /******************************************************************************************
     * An array of the headers to display. The strings in this array are automatically
     * internationalized through Translator.LABEL.
     ******************************************************************************************/
    String[] getHeaders();

    /******************************************************************************************
     * An array of the column types to display. Valid column types are DataTypeConstants. This
     * type array must be the same length as the headers or it will be truncated or expanded
     * to match. TEXT will be substituted for all expanded columns. Returning null is acceptable.
     ******************************************************************************************/
    int[] getColumnTypes() throws UIException;

    /******************************************************************************************
     * An array of the column sizes to assigns to the column. This type array must be the same
     * length as the headers or it will be truncated or expanded to match. Any empty values will
     * be assigned a -1. The int value represents the pixels of width of the particular column
     * where -1 means the column receives an equal share of available space. 0 means that the
     * column will lock itself to the size of its header.
     ******************************************************************************************/
    int[] getColumnSizes();

    /******************************************************************************************
     * Builds a row of string information for a specified object. This string array must be
     * the same length as the headers array where each index is the display information for the
     * appropriate column. If the row is not the exact same length as the headers, it will be
     * truncated, or toString() will be substituted for each additional column needed.
     ******************************************************************************************/
    String[] buildRow(Object object) throws UIException;
}
