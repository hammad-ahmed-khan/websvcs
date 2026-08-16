package oracle.retail.sim.client.swing.table;

import java.util.ArrayList;
import java.util.List;

/********************************************************************************************************
 * This Class defines the various aspects of a table. 
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimTableDef {

    private Class dataClass;
    private String identifier;
    private List<SimTableAttribute> tableAttributes = new ArrayList<>();
    private List<SimTableSortAttribute> sortAttributes = new ArrayList<>();
    private List<String> overrideAttributes = new ArrayList<>();

    /****************************************************************************************************
     * Constructors a new table definition.
     * @param ownerClass The class object of the owning class.
     * @param dataClass The class object of a single row of the table.
     ***************************************************************************************************/
    public SimTableDef(String identifier, Class dataClass) {
        if (identifier == null) {
            throw new IllegalArgumentException("Identifier cannot be null!");
        }
        if (dataClass == null) {
            throw new IllegalArgumentException("Data class cannot be null!");
        }
        this.identifier = identifier;
        this.dataClass = dataClass;
    }

    /****************************************************************************************************
     * Returns the data class of a single row of the table.
     ***************************************************************************************************/
    public Class getDataClass() {
        return dataClass;
    }

    /****************************************************************************************************
     * The unique identifier of the table. This will be used to track configuration information. The
     * identifier of the table identifier is declaringClassName.className
     ***************************************************************************************************/
    public String getIdentifier() {
        return identifier;
    }

    public List<SimTableAttribute> getTableAttributes() {
        return tableAttributes;
    }

    /****************************************************************************************************
     * Retrieves the list of default sort attributes. If the table configuration does not have any sort
     * configuration settings. These sort attributes will be used to generate the sort configuration.
     * @return A List of attributes in the order they should be sorted.
     ***************************************************************************************************/
    public List<SimTableSortAttribute> getSortAttributes() {
        return sortAttributes;
    }

    public List<String> getNotEditableAttributes() {
        return overrideAttributes;
    }

    public void addTableAttribute(SimTableAttribute attribute) {
        if (attribute != null && !tableAttributes.contains(attribute)) {
            tableAttributes.add(attribute);
        }
    }
    
    public void removeTableAttribute(SimTableAttribute attribute) {
        if (attribute != null && tableAttributes.contains(attribute)) {
            tableAttributes.remove(attribute);
        }
    }

    public void addSortAttribute(SimTableSortAttribute attribute) {
        if (attribute != null && !sortAttributes.contains(attribute)) {
            sortAttributes.add(attribute);
        }
    }

    public void removeSortAttribute(SimTableSortAttribute attribute) {
        if (attribute != null && sortAttributes.contains(attribute)) {
            sortAttributes.remove(attribute);
        }
    }

    public void addNotEditableAttribute(String attribute) {
        if (attribute != null && !overrideAttributes.contains(attribute)) {
            overrideAttributes.add(attribute);
        }
    }

    public void removeNotEditableAttribute(String attribute) {
        if (attribute != null && overrideAttributes.contains(attribute)) {
            overrideAttributes.remove(attribute);
        }
    }
}
