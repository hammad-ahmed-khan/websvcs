package oracle.retail.sim.client.swing.util;

import oracle.retail.sim.client.locale.Translator;

/******************************************************************************************
 * This class represents the available error severity levels for RErrorEvents and UIExceptions.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class RErrorSeverity {
    public static final RErrorSeverity INFO = new RErrorSeverity("Informational");
    public static final RErrorSeverity WARNING = new RErrorSeverity("Warning");
    public static final RErrorSeverity ERROR = new RErrorSeverity("Error");
    public static final RErrorSeverity FATAL = new RErrorSeverity("Fatal");

    private String description;

    /******************************************************************************************
     * Private constructor makes it so that only this class can define error severity levels.
     * <p>
     * @param name The name of the error severity.
     ******************************************************************************************/
    private RErrorSeverity(String name) {
        description = name;
    }

    /******************************************************************************************
     * Retrieves the description of the error severity.
     ******************************************************************************************/
    public String getDescription() {
        return Translator.getText(description);
    }

    /******************************************************************************************
     * Retrieves the description of the error severity.
     ******************************************************************************************/
    public String toString() {
        return Translator.getText(description);
    }
}
