package oracle.retail.sim.client.swing.displaytable;

import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.util.DataTypeConstants;

/******************************************************************************************
 * Partial implementation of a table row displayer for easy subclassing at the local
 * implementation level.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public abstract class SimpleTableRowDisplayer implements TableRowDisplayer {

    private String[] headers = new String[0];
    private int[] types = new int[0];
    private int[] sizes = new int[0];

    /******************************************************************************************
     * Constructs a new SimpleTableRowDisplayer.
     * <p>
     * @param headers The column headers to assign to the table.
     ******************************************************************************************/
    protected SimpleTableRowDisplayer(String[] headers) {
        if (headers == null || headers.length == 0) {
            throw new IllegalArgumentException("Headers cannot be null in TableRowDisplayer!");
        }
        this.headers = headers;
        initializeTypes();
        initializeSizes();
    }

    /******************************************************************************************
     * Initializes all the types to DataTypeConstants.TEXT.
     ******************************************************************************************/
    private void initializeTypes() {
        types = new int[headers.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = DataTypeConstants.TEXT;
        }
    }

    /******************************************************************************************
     * Initializes all the sizes to EditorConstants.COLUMN_STRETCHABLE.
     ******************************************************************************************/
    private void initializeSizes() {
        sizes = new int[headers.length];
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = EditorConstants.COLUMN_STRETCHABLE;
        }
    }

    /******************************************************************************************
     * Assigns a data type to a particular column.
     * <p>
     * @param column The column header to assign the size to.
     * @param type A DataTypeConstant that defines the data type.
     ******************************************************************************************/
    public void setDataType(String column, int type) {
        if (type < DataTypeConstants.TEXT || type > DataTypeConstants.ICON) {
            throw new IllegalArgumentException("Data type must be a DataTypeConstant!");
        }
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equals(column)) {
                types[i] = type;
                break;
            }
        }
    }

    /******************************************************************************************
     * Assigns a column size to the given column.
     * <p>
     * @param column The column header to assign the size to.
     * @param size The width of the column in pixels or EditorConstants.COLUMN_STRETCHABLE or
     * EditorConstants.COLUMN_LABEL_WIDTH.
     ******************************************************************************************/
    public void setColumnSize(String column, int size) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equals(column)) {
                sizes[i] = size;
                break;
            }
        }
    }

    /******************************************************************************************
     * Retrieves the array of the headers to display. These should be the untranslated table
     * column identifiers.
     ******************************************************************************************/
    public String[] getHeaders() {
        return headers;
    }

    /******************************************************************************************
     * Returns an array of the column types to display.
     ******************************************************************************************/
    public int[] getColumnTypes() {
        return types;
    }

    /******************************************************************************************
     * Returns an array of the column sizes to assigns to the column.
     ******************************************************************************************/
    public int[] getColumnSizes() {
        return sizes;
    }
}
