package oracle.retail.sim.client.swing.lov;

import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.Displayable;

/******************************************************************************************
 * Simple implementation of the ListOfValuesDisplayer inteface that calls toDisplayString()
 * or toString() for both the entry name formatting and the description formatting.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class SimpleListOfValuesDisplayer implements ListOfValuesDisplayer {

    /******************************************************************************************
     * Creates new SimpleListOfValuesDisplayer.
     *****************************************************************************************/
    public SimpleListOfValuesDisplayer() {
    }

    /******************************************************************************************
     * Retrieves the entry display text for a given object.
     * <p>
     * @param value The object to retrieve the display text from.
     * <p>
     * @return The display text.
     *****************************************************************************************/
    public String getEntryText(Object value) {
        if (value == null) {
            return StringConstants.EMPTY;
        } else if (value instanceof Displayable) {
            return ((Displayable) value).toDisplayString();
        }
        return value.toString();
    }

    /******************************************************************************************
     * Retrieves the description display text for a given object.
     * <p>
     * @param value The object to retrieve the display text from.
     * <p>
     * @return The display text.
     *****************************************************************************************/
    public String getDescriptionText(Object value) {
        if (value == null) {
            return StringConstants.EMPTY;
        } else if (value instanceof Displayable) {
            return ((Displayable) value).toDisplayString();
        }
        return value.toString();
    }
}
