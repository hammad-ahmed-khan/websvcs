package oracle.retail.sim.client.swing.rowtitletable;

import oracle.retail.sim.common.core.locale.StringConstants;

/****************************************************************************************************
 * Class represents a single cell of the RowTitleTable.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ****************************************************************************************************/

public class RowTitleTableCell {

    private Object data;
    private String[] array;

    /****************************************************************************************************
     * Constructor
     * <p>
     * @param displayArray An array of strings to display in the cell.
     * @param dataObject A data object represented by the cell.
     ****************************************************************************************************/
    public RowTitleTableCell(String[] displayArray, Object dataObject) {
        array = displayArray;
        data = dataObject;
    }

    /****************************************************************************************************
     * Retrieves the data stored within the cell.
     * <p>
     * @return The data stored within the cell.
     ****************************************************************************************************/
    public Object getData() {
        return data;
    }

    /****************************************************************************************************
     * Retrieves the size of the array of strings to display in the cell.
     * <p>
     * @return The size of the array of strings to display in the cell.
     ****************************************************************************************************/
    public int getSize() {
        return array.length;
    }

    /****************************************************************************************************
     * Retrieves the display value at the indicated index.
     * <p>
     * @param index The index into the display array assigned in the constructor.
     ****************************************************************************************************/
    public String getDisplayValue(int index) {
        if (index < 0 || index >= array.length) {
            return StringConstants.EMPTY;
        }
        return array[index];
    }
}
