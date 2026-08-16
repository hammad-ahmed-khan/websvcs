package oracle.retail.sim.client.swing.filter;

import java.io.Serializable;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * This class represents a filter group or filter element associated with a column.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ColumnFilterElement implements Serializable {
    private static final long serialVersionUID = -3146971266361370796L;

    private String columnName = StringConstants.EMPTY;
    private int columnIndex = -1;
    private FilterGroup filterGroup;

    /****************************************************************************************************
     * Constructs a new ColumnFilterElement.
     * <p>
     * @param name The column name.
     * @param index The column index.
     * @param group The filter group.
     ***************************************************************************************************/
    public ColumnFilterElement(String name, int index, FilterGroup group) {
        columnName = name;
        columnIndex = index;
        filterGroup = group;
    }

    /****************************************************************************************************
     * Retrieves the original untranslated column name associated with the column filter element.
     ***************************************************************************************************/
    public String getColumnName() {
        return columnName;
    }

    /****************************************************************************************************
     * Retrieves the column index associated with the column filter element.
     ***************************************************************************************************/
    public int getColumnIndex() {
        return columnIndex;
    }

    /****************************************************************************************************
     * Retrieves the filter group assigned to the column.
     ***************************************************************************************************/
    public FilterGroup getFilterGroup() {
        return filterGroup;
    }

    /****************************************************************************************************
     * Overrides the toString() for better display.
     ***************************************************************************************************/
    public String toString() {
        StringBuilder buffer = new StringBuilder("ColumnFilterElement [");
        buffer.append("Column = ").append(columnName);
        buffer.append("; Index = ").append(columnIndex);
        buffer.append("; Filter Group = ").append(filterGroup);
        return buffer.toString();
    }
}
