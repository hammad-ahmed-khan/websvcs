package oracle.retail.sim.client.swing.util;

import oracle.retail.sim.client.application.RPropertyBundle;
import oracle.retail.sim.common.core.locale.StringConstants;

/******************************************************************************************
 * This class provides translation between component identifiers and the permission ids
 * they are associated to.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class PermissionTranslator {

    private static RPropertyBundle propertyBundle = new RPropertyBundle();

    /******************************************************************************************
     * Returns new PermissionTranslator object.
     ******************************************************************************************/
    private PermissionTranslator() {
    }

    /******************************************************************************************
     * Assign the property bundle for the permission translator to retrieve values from.
     * <p>
     * @param filename The base file name of the property file.
     * @throws Exception Thrown if an error occurs loading the property file.
     ******************************************************************************************/
    public static void setBundle(String filename) throws Exception {
        setBundle(new RPropertyBundle(filename));
    }

    /******************************************************************************************
     * Assign the property bundle for the permission translator to retrieve values from.
     * <p>
     *@param bundle The property bundle to assign to the translator.
     ******************************************************************************************/
    public static void setBundle(RPropertyBundle bundle) {
        if (bundle != null) {
            propertyBundle = bundle;
        }
    }

    /******************************************************************************************
     * Retrieves the permission code for the given key.
     * <p>
     *@param key The key to return the permission for.
     *@return The permission code that matches the key.
     ******************************************************************************************/
    public static String getPermission(String key) {
        String permission = propertyBundle.getString(key);
        if (permission == null) {
            permission = StringConstants.EMPTY;
        } else if (permission.equals(key)) {
            permission = StringConstants.EMPTY;
        }
        return permission;
    }
}
