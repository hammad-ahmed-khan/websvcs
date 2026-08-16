package oracle.retail.sim.client.swing.lov;

import java.util.Collection;
import oracle.retail.sim.client.swing.util.UIException;

/******************************************************************************************
 * This interface must be implemented by any class that wishes to be assigned to a
 * list-of-values editor. The implementation of this may make server calls, read files
 * or any number of possibilities. However, this method may NOT throw errors and SHOULD NOT
 * return a null array, but rather an empty one. Errors must be dealt with in the local
 * implementation of this interface. This model uses all the locally implemented client
 * functionality.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public interface ListOfValuesModel {

    /******************************************************************************************
     * Retrieves a collection of all available selectable values.
     ******************************************************************************************/
    Collection getSelectableValues() throws UIException;

    /******************************************************************************************
     * Retrieves a collection of values for the passed in string identifiers.
     ******************************************************************************************/
    Collection getSelectedValues(String[] values) throws UIException;

    /******************************************************************************************
     * Assigns a collection of selected values from the list of values editor
     ******************************************************************************************/
    void setSelectedValues(Collection collection) throws UIException;
}
