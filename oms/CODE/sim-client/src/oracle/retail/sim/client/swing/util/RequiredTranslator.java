package oracle.retail.sim.client.swing.util;

import oracle.retail.sim.client.application.RPropertyBundle;
import oracle.retail.sim.client.locale.StringUtility;

/******************************************************************************************
 * This class provides translation between component identifiers and the required indicators
 * they are associated to.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class RequiredTranslator {

    private static RPropertyBundle propertyBundle = new RPropertyBundle();

    /******************************************************************************************
     * Returns new RequiredTranslator object.
     ******************************************************************************************/
    private RequiredTranslator() {
    }

    /******************************************************************************************
     * Assign the property bundle for the required translator to retrieve values from.
     * <p>
     * @param filename The base file name of the property file.
     * @throws Exception Thrown if an error occurs loading the property file.
     ******************************************************************************************/
    public static void setBundle(String filename) throws Exception {
        setBundle(new RPropertyBundle(filename));
    }

    /******************************************************************************************
     * Assign the property bundle for the required translator to retrieve values from.
     * <p>
     *@param bundle The property bundle to assign to the translator.
     ******************************************************************************************/
    public static void setBundle(RPropertyBundle bundle) {
        if (bundle != null) {
            propertyBundle = bundle;
        }
    }

    /******************************************************************************************
     * Retrieves the required indicator for the given key.
     * <p>
     *@param key The key to return the permission for.
     *@return The required indicator that matches the key.
     ******************************************************************************************/
    public static boolean isRequired(String key) {
        return StringUtility.booleanValue(propertyBundle.getString(key));
    }
}
