package oracle.retail.sim.client.swing.filter;

import java.io.Serializable;
import oracle.retail.sim.client.swing.util.DataTypeConstants;

/******************************************************************************************
 * A TableSortElement consists of a column header to sort on and ascending/descending flag.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class TableSortElement implements Serializable {
    private static final long serialVersionUID = 1307470207853392275L;

    private String columnHeader;
    private int dataType = DataTypeConstants.TEXT;
    private boolean isAscending = true;

    /******************************************************************************************
     * Constructs new TableSortElement.
     * <p>
     * @param columnHeader The column header.
     * @param dataType The data type represents by the column (a DataTypeConstants).
     * @param isAscending True if the data should be sorted ascending, false otherwise.
     *****************************************************************************************/
    public TableSortElement(String columnHeader, int dataType, boolean isAscending) {
        this.columnHeader = columnHeader;
        this.isAscending = isAscending;
    }

    /******************************************************************************************
     * Retrieves the column header.
     * <p>
     * @return The column header.
     *****************************************************************************************/
    public String getColumnHeader() {
        return columnHeader;
    }

    /******************************************************************************************
     * Retrieves the data type.
     * <p>
     * @return The data type.
     *****************************************************************************************/
    public int getDataType() {
        return dataType;
    }

    /******************************************************************************************
     * Retrieves whether or not the table sort is ascending.
     * <p>
     * @return True if the table sort is ascending, false otherwise.
     *****************************************************************************************/
    public boolean isAscending() {
        return isAscending;
    }
}
