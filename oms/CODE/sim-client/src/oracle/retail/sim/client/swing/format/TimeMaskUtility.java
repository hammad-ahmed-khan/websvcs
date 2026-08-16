package oracle.retail.sim.client.swing.format;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/******************************************************************************************
 * This class handles parsing a time string for a particular locale and creating the
 * locale-standing time string for the locale.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class TimeMaskUtility {

    private static Map timeParserMap = new HashMap<>();

    /******************************************************************************************
     * Constructor
     *****************************************************************************************/
    private TimeMaskUtility() {
    }

    /******************************************************************************************
     * Installs a time parser for the particular locale.
     * <p>
     * @param locale The locale
     * @param parser The time parser
     *****************************************************************************************/
    public static void installTimeParser(Locale locale, TimeParser parser) {
        timeParserMap.put(locale, parser);
    }

    /******************************************************************************************
     * Parses the time string from the particular locale.
     * <p>
     * @param locale The locale
     * @param text The time string
     *****************************************************************************************/
    public static String parseText(Locale locale, String text) {
        TimeParser parser = (TimeParser) timeParserMap.get(locale);
        if (parser != null) {
            return parser.parseText(text);
        }
        return text;
    }
}
