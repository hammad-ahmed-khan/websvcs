package oracle.retail.sim.client.swing.lov;

import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.DisplayerUtility;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.core.locale.StringConstants;

/******************************************************************************************
 * Default implementation of the ListOfValuesDisplayer interface that takes an entry field
 * attribute name and description field attribute name and uses reflection to display the
 * values.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class DefaultListOfValuesDisplayer implements ListOfValuesDisplayer {

    private String entryName;
    private String descriptionName;

    /******************************************************************************************
     * Creates new DefaultListOfValuesDisplayer object.
     * <p>
     * @param entryAttributeName The attribute name of the value to display in the entry field.
     * @param descriptionAttributeName The attribute name of the value to display in the
     * description field.
     *****************************************************************************************/
    public DefaultListOfValuesDisplayer(String entryAttributeName, String descriptionAttributeName) {
        entryName = entryAttributeName;
        descriptionName = descriptionAttributeName;
    }

    /******************************************************************************************
     * Retrieves the entry display text for a given object (using the entry attribute name).
     * This method is not allowed to trigger an exception, so if invalid input occurs an
     * empty string is returned. Custom implementations of ListOfValuesDisplayer should handle
     * any exceptions.
     * <p>
     * @param value The object to retrieve the entry text from.
     * <p>
     * @return The entry text.
     *****************************************************************************************/
    public String getEntryText(Object value) {
        try {
            return DisplayerUtility.getDisplayValue(value, entryName, DataTypeConstants.TEXT);
        } catch (UIException exception) {
            return StringConstants.EMPTY;
        }
    }

    /******************************************************************************************
     * Retrieves the description display text for a given object (using the description
     * attribute name). This method is not allowed to trigger an exception, so if invalid
     * input occurs an empty string is returned. Custom implementations of ListOfValuesDisplayer
     * should handle any exceptions.
     * <p>
     * @param value The object to retrieve the display text from.
     * <p>
     * @return The display text.
     *****************************************************************************************/
    public String getDescriptionText(Object value) {
        try {
            return DisplayerUtility.getDisplayValue(value, descriptionName, DataTypeConstants.TEXT);
        } catch (UIException exception) {
            return StringConstants.EMPTY;
        }
    }
}
