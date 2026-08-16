package oracle.retail.sim.client.swing.util;

import java.text.NumberFormat;
import oracle.retail.sim.client.application.RPropertyBundle;
import oracle.retail.sim.client.locale.LocaleManager;

/******************************************************************************************
 * This class provides translation for text lengths for text fields and text areas within
 * the application.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class TextLengthTranslator {

    private static RPropertyBundle propertyBundle = new RPropertyBundle();

    /******************************************************************************************
     * Returns new TextLengthTranslator object.
     ******************************************************************************************/
    private TextLengthTranslator() {
    }

    /******************************************************************************************
     * Assign the property bundle for the text length translator to retrieve values from.
     * <p>
     * @param filename The base file name of the property file.
     * @throws Exception Thrown if an error occurs loading the property file.
     ******************************************************************************************/
    public static void setBundle(String filename) throws Exception {
        setBundle(new RPropertyBundle(filename));
    }

    /******************************************************************************************
     * Assign the property bundle for the text length translator to retrieve values from.
     * <p>
     *@param bundle The property bundle to assign to the translator.
     ******************************************************************************************/
    public static void setBundle(RPropertyBundle bundle) {
        if (bundle != null) {
            propertyBundle = bundle;
        }
    }

    /******************************************************************************************
     * Retrieves the maximum length of the text field based on a key. If no value or a negative
     * value is found, zero is returned.
     * <p>
     *@param key The key to return the text for,
     *@return The text length assign to the key.
     ******************************************************************************************/
    public static int getLength(String key) {
        Integer length = propertyBundle.getInteger(key);
        if (length == null || length < 0) {
            return 0;
        }
        return length;
    }

    /******************************************************************************************
     * Retrieves the an array of integers from the properties file based on key.
     * <p>
     *@param key The key to return the text for.
     *@return An array of integers representing two or more lengths.
     ******************************************************************************************/
    public static int[] getLengths(String key) {
        String[] textValues = propertyBundle.getStringArray(key);
        if (textValues == null) {
            return null;
        }

        NumberFormat formatter = LocaleManager.getIntegerFormatter();
        int[] values = new int[textValues.length];
        for (int i = 0; i < textValues.length; i++) {
            try {
                values[i] = (Integer) formatter.parse(textValues[i]);
            } catch (Throwable exception) {
                values[i] = -1;
            }
        }
        return values;
    }
}
