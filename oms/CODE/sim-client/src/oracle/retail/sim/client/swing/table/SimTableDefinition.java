package oracle.retail.sim.client.swing.table;

import java.util.Collections;
import java.util.List;

/********************************************************************************************************
 * This Class defines the various aspects of a table. Only the identifier method is implemented with
 * default functionality.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class SimTableDefinition {

    /****************************************************************************************************
     * The unique identifier of the table. This will be used to track configuration information. The
     * default identifier is declaringClassName.className
     ***************************************************************************************************/
    public String getIdentifier() {
        StringBuilder identifier = new StringBuilder();
        if (getClass().getDeclaringClass() != null) {
            identifier.append(getClass().getDeclaringClass().getSimpleName());
        }
        identifier.append(".");
        identifier.append(getClass().getSimpleName());
        return identifier.toString();
    }

    /****************************************************************************************************
     * Retrieves the list of default sort attributes. If the table configuration does not have any sort
     * configuration settings. These sort attributes will be used to generate the sort configuration.
     * @return A List of attributes in the order they should be sorted.
     ***************************************************************************************************/
    public List<SimTableSortAttribute> getSortAttributes() {
        return Collections.emptyList();
    }

    /****************************************************************************************************
     * Retrieves a list of attributes that do not require a setter() method on the model in order to
     * be considered "editable" by the table.
     ***************************************************************************************************/
    public List<String> getOverrideEditableAttributes() {
        return Collections.emptyList();
    }

    /****************************************************************************************************
     * The Class of the data that each row in the table represents. This will be passed into the table
     * model class.
     ***************************************************************************************************/
    public abstract Class getDataClass();

    /****************************************************************************************************
     * A list of SIM table attributes that represent the attribute of each column.
     ***************************************************************************************************/
    public abstract List<SimTableAttribute> getAttributes();
}
