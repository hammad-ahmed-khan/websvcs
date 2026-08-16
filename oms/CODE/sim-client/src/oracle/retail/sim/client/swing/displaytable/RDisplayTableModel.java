package oracle.retail.sim.client.swing.displaytable;

import javax.swing.table.DefaultTableModel;

/******************************************************************************************
 * This class defines a table model that has table cells which cannot be edited.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RDisplayTableModel extends DefaultTableModel {
    private static final long serialVersionUID = -4805116015483867125L;

    /******************************************************************************************
     * Returns new RDisplayTableModel object.
     * <p>
     *@param headers An object array of column headers.
     *@param rowCount The number of rows to initialize the table with.
     *****************************************************************************************/
    public RDisplayTableModel(Object[] headers, int rowCount) {
        super(headers, rowCount);
    }

    /******************************************************************************************
     * Retrieves whether or not the table cell is editable at a certain cell location. This
     * method always returns false to make the table cells non-editable (thus the reason it
     * is called display table).
     * <p>
     *@param row The cell row.
     *@param column The cell column.
     *<p>
     *@return false
     *****************************************************************************************/
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /******************************************************************************************
     * Clears all rows from the model.
     *****************************************************************************************/
    public void clear() {
        int rowCount = getRowCount();
        for (int i = 1; i <= rowCount; i++) {
            removeRow(rowCount - i);
        }
    }
}
