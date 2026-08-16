package oracle.retail.sim.client.swing.table;

import java.util.Objects;

/********************************************************************************************************
 * This class is a representation of a sort request. It is simply a data holder to hold on to a column
 * identifier and whether the sort is to be ascending or descending. This is converted to sort criteria
 * within the table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimTableSortAttribute {

    private String attribute;
    private boolean ascending = true;

    /****************************************************************************************************
     * Create a new ascending SortCriteria object.
     * <p>
     * @param attribute The attribute of the column to be sorted on.
     ***************************************************************************************************/
    public SimTableSortAttribute(String attribute) {
        this(attribute, true);
    }

    /****************************************************************************************************
     * Create a new SortCriteria object.
     * <p>
     * @param attribute The attribute of the column to be sorted on.
     * @param ascending Whether this is an ascending or descending sort.
     ***************************************************************************************************/
    public SimTableSortAttribute(String attribute, boolean ascending) {
        this.attribute = attribute;
        this.ascending = ascending;
    }

    /****************************************************************************************************
     * Gets the column attribute for this criteria.
     ***************************************************************************************************/
    public String getAttribute() {
        return attribute;
    }

    /****************************************************************************************************
     * Determines if this criteria is specifying an ascending or descending sort.
     ***************************************************************************************************/
    public boolean isAscending() {
        return ascending;
    }
    
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        SimTableSortAttribute that = (SimTableSortAttribute) object;
        return Objects.equals(attribute, that.attribute);
    }

    public int hashCode() {
        return Objects.hash(attribute);
    }
}
