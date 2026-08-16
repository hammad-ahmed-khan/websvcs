package oracle.retail.sim.client.swing.format;

import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.common.core.locale.StringConstants;

/***********************************************************************************
 * Parse a text string representing a time and makes some intelligent decisions
 * about the string, attempting to format it into a system-standard for the United
 * States.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ************************************************************************************/

public class USTimeParser implements TimeParser {

    /******************************************************************************************
     * Format a wide variety of input text to return to time in HH:MM PM format.
     *****************************************************************************************/
    public String parseText(String text) {
        text = trim(text);

        if (text.length() == 0) {
            return StringConstants.EMPTY;
        }

        int index = StringUtility.indexOf(text, ":");
        if (index < 0) {
            return formatNoColon(text);
        }
        return formatColon(text, index);
    }

    /******************************************************************************************
     * Private inner method to format text that has a colon in it.
     *****************************************************************************************/
    private String formatColon(String text, int index) {
        StringBuilder formatBuffer = new StringBuilder();

        switch (index) {
            case 0:
                formatBuffer.append('0');
                formatBuffer.append('0');
                break;
            case 1:
                formatBuffer.append('0');
                formatBuffer.append(text.charAt(0));
                break;
            default:
                formatBuffer.append(text.charAt(0));
                formatBuffer.append(text.charAt(1));
                break;
        }

        formatBuffer.append(":");

        switch (text.length() - index - 1) {
            case 0:
                formatBuffer.append("00 PM");
                break;
            case 1:
                formatBuffer.append(text.charAt(index + 1));
                formatBuffer.append("0 PM");
                break;
            case 2:
                formatBuffer.append(text.charAt(index + 1));
                formatBuffer.append(text.charAt(index + 2));
                formatBuffer.append(" PM");
                break;
            default:
                formatBuffer.append(text.charAt(index + 1));
                formatBuffer.append(text.charAt(index + 2));
                switch (text.charAt(index + 3)) {
                    case 'A':
                    case 'a':
                        formatBuffer.append(" AM");
                        break;
                    default:
                        formatBuffer.append(" PM");
                        break;
                }
                break;
        }
        return formatBuffer.toString();
    }

    /******************************************************************************************
     * Private inner method to format text that has no colon in it.
     *****************************************************************************************/
    private String formatNoColon(String text) {
        StringBuilder formatBuffer = new StringBuilder();

        int amIndex = text.indexOf('A');
        if (amIndex < 0) {
            amIndex = text.indexOf('a');
        }
        int pmIndex = text.indexOf('P');
        if (pmIndex < 0) {
            pmIndex = text.indexOf('p');
        }
        int index = amIndex;
        if (index < 0) {
            index = pmIndex;
        }
        if (index > -1) {
            text = text.substring(0, index);
        }

        switch (text.length()) {
            case 0:
                return "00:00 AM";
            case 1:
                text = "0" + text + "00";
                break;
            case 2:
                text = text + "00";
                break;
            case 3:
                text = "0" + text;
                break;
            default:
        }

        formatBuffer.append(text.charAt(0));
        formatBuffer.append(text.charAt(1));
        formatBuffer.append(":");
        formatBuffer.append(text.charAt(2));
        formatBuffer.append(text.charAt(3));

        if (amIndex > -1) {
            formatBuffer.append(" AM");
        } else {
            formatBuffer.append(" PM");
        }
        return formatBuffer.toString();
    }

    /******************************************************************************************
     * Trims whitespace and extra colons from the text
     *****************************************************************************************/
    private String trim(String text) {
        StringBuilder buffer = new StringBuilder();
        boolean colonNotFound = true;
        for (int i = 0; i < text.length(); i++) {
            char testChar = text.charAt(i);
            switch (testChar) {
                case ' ':
                    break;
                case ':':
                    if (colonNotFound) {
                        buffer.append(testChar);
                        colonNotFound = false;
                    }
                    break;
                default:
                    buffer.append(testChar);
                    break;
            }
        }
        return buffer.toString();
    }
}
