package oracle.retail.sim.client.swing.displaytable;

import oracle.retail.sim.client.swing.util.DisplayerUtility;
import oracle.retail.sim.client.swing.util.UIException;

/******************************************************************************************
 * This class represents a default selectable row displayer that uses reflection to display
 * a table row.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class DefaultTableRowDisplayer implements TableRowDisplayer {

    private Class dataType;
    private String[] headers;
    private String[] attributes;
    private int[] columnTypes;
    private int[] columnSizes;

    /******************************************************************************************
     * Constructs a new default selectable row displayer.
     * <p>
     * @param dataType The class object that will be placed in a row for display.
     * @param headers An array of column headers that also represent attributes on the data type.
     ******************************************************************************************/
    public DefaultTableRowDisplayer(Class dataType, String[] headers) {
        this(dataType, headers, headers, null);
    }

    /******************************************************************************************
     * Constructs a new default selectable row displayer.
     * <p>
     * @param dataType The class object that will be placed in a row for display.
     * @param headers An array of column headers that also represent attributes on the data type.
     ******************************************************************************************/
    public DefaultTableRowDisplayer(RTableDefinition definition) {
        this(definition.getTypeClass(), definition.getHeaders(), definition.getAttributes(), definition.getSizes());
    }

    /******************************************************************************************
     * Constructs a new default selectable row displayer.
     * <p>
     * @param dataType The class object that will be placed in a row for display.
     * @param headers An array of column headers.
     * @param attributes An array of attributes on the data type.
     *******************************************************************************************/
    public DefaultTableRowDisplayer(Class dataType, String[] headers, String[] attributes) {
        this(dataType, headers, attributes, null);
    }

    /******************************************************************************************
     * Constructs a new default selectable row displayer.
     * <p>
     * @param dataType The class object that will be placed in a row for display.
     * @param headers An array of column headers.
     * @param attributes An array of attributes on the data type.
     * @param size An array of column sizes to size the columns to.
     ******************************************************************************************/
    public DefaultTableRowDisplayer(Class dataType, String[] headers, String[] attributes, int[] sizes) {
        if (dataType == null) {
            throw new IllegalArgumentException("Data type cannot be null!");
        }
        if (headers == null) {
            throw new IllegalArgumentException("Table Headers cannot be null!");
        }
        if (headers.length == 0) {
            throw new IllegalArgumentException("Table Headers  cannot be empty!");
        }
        if (attributes == null) {
            throw new IllegalArgumentException("Attributes cannot be null!");
        }
        if (attributes.length == 0) {
            throw new IllegalArgumentException("Attributes cannot be empty!");
        }
        if (sizes == null) {
            sizes = new int[headers.length];
            for (int i = 0; i < sizes.length; i++) {
                sizes[i] = -1;
            }
        } else if (sizes.length != headers.length) {
            throw new IllegalArgumentException("Sizes parameter length does not equals headers parameter length!");
        }
        this.dataType = dataType;
        this.headers = headers;
        this.attributes = attributes;
        columnSizes = sizes;
    }

    /******************************************************************************************
     * Retrieves the column headers for the table.
     * <p>
     * @return An array of column headers.
     ******************************************************************************************/
    public String[] getHeaders() {
        return headers;
    }

    /******************************************************************************************
     * Retrieves the column types for the table.
     * <p>
     * @return An array of column types.
     * <p>
     * @throws OldUIException Thrown if an error occurs trying to determine column types.
     ******************************************************************************************/
    public int[] getColumnTypes() throws UIException {
        if (columnTypes == null) {
            columnTypes = new int[headers.length];
            for (int i = 0; i < columnTypes.length; i++) {
                columnTypes[i] = DisplayerUtility.getDisplayTableCellType(dataType, attributes[i]);
            }
        }
        return columnTypes;
    }

    /******************************************************************************************
     * Retrieves the column sizes for the table.
     * <p>
     * @return An array of column sizes.
     ******************************************************************************************/
    public int[] getColumnSizes() {
        return columnSizes;
    }

    /******************************************************************************************
     * Retrieves a display row for the table. This method uses the attribute name for the
     * column to retrieve the data from the assigned attribute class.
     * <p>
     * @return An array of display text.
     * <p>
     * @throws OldUIException Thrown if an error occurs trying to build the row.
     ******************************************************************************************/
    public String[] buildRow(Object object) throws UIException {
        String[] row = new String[headers.length];
        for (int i = 0; i < row.length; i++) {
            row[i] = DisplayerUtility.getDisplayValue(object, attributes[i], columnTypes[i]);
        }
        return row;
    }
}
