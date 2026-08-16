package oracle.retail.sim.client.swing.filter;

import java.io.Serializable;

/******************************************************************************************
 * A TableFilterElement consists of a FilterElement combined with a column header.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class TableFilterElement implements Serializable {
    private static final long serialVersionUID = -809348731057659190L;

    private String columnHeader;
    private FilterElement filterElement;

    /******************************************************************************************
     * Constructs new TableFilterElement.
     * <p>
     * @param columnHeader The column header.
     * @param filterElement The filter element.
     *****************************************************************************************/
    public TableFilterElement(String columnHeader, FilterElement filterElement) {
        this.columnHeader = columnHeader;
        this.filterElement = filterElement;
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
     * Retrieves the filter element.
     * <p>
     * @return The filter element.
     *****************************************************************************************/
    public FilterElement getFilterElement() {
        return filterElement;
    }

    /******************************************************************************************
     * Retrieves the filter type within the filter element.
     * <p>
     * @return A FilterType object.
     *****************************************************************************************/
    public FilterType getFilterType() {
        return filterElement.getType();
    }

    /******************************************************************************************
     * Retrieves the filter value within the filter element.
     * <p>
     * @return The filter value.
     *****************************************************************************************/
    public String getFilterValue() {
        return filterElement.getValue();
    }
}
