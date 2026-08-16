package oracle.retail.sim.client.swing.widget;

import java.text.DecimalFormatSymbols;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.util.UIPropertyName;

/********************************************************************************************************
 * This class sub-classes RTextField class in our custom package to provide custom functionality for the
 * application.
 * <p>
 * This widgets has the ability to limit the number of character allowed in the field. RNumberFields will
 * remain disabled until and entry length is specified. It only counts numeric characters when
 * calculating the types characters.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RNumberField extends RTextField {
    private static final long serialVersionUID = -8568831670122027220L;

    /****************************************************************************************************
     * Returns a new RNumberField object.
     ***************************************************************************************************/
    public RNumberField() {
    }

    /****************************************************************************************************
     * Helper method to validate the allowed length in the field. If the text currently being viewed is
     * greater than the maximum allowed (usually via paste or complex formatting), then clear the field
     * This should be overriden by subclasses that would use a different method to calculated valid
     * characters to count.
     ***************************************************************************************************/
    public String validateAllowedLength(String text) {
        if (allowedLength > 0) {
            int count = countNumericCharacters(text);
            if (count > allowedLength) {
                firePropertyChange(UIPropertyName.TEXT_COMPONENT_TEXT_ERROR, Boolean.TRUE, Boolean.FALSE);
                return StringUtility.substring(text, 0, allowedLength);
            }
        }
        return text;
    }

    /****************************************************************************************************
     * Calculates the "length" of numeric characters in the text.
     ***************************************************************************************************/
    protected int calculateLength() {
        return countNumericCharacters(getUnformattedText());
    }

    /****************************************************************************************************
     * Helper method that counts the numeric characters.
     ***************************************************************************************************/
    private int countNumericCharacters(String text) {
        DecimalFormatSymbols symbols = LocaleManager.getNumberDecimalSymbols();
        char decimal = symbols.getDecimalSeparator();
        char minus = symbols.getMinusSign();
        char mdecimal = symbols.getMonetaryDecimalSeparator();
        char percent = symbols.getPercent();
        boolean hasDecimal = false;
        boolean hasMinus = false;
        boolean hasMdecimal = false;
        boolean hasPercent = false;
        int count = 0;
        char[] charArray = text.toCharArray();
        for (char element : charArray) {
            if (Character.isDigit(element)) {
                count++;
                continue;
            }
            if (element == decimal && !hasDecimal) {
                hasDecimal = true;
                continue;
            }
            if (element == minus && !hasMinus) {
                hasMinus = true;
                continue;
            }
            if (element == mdecimal && !hasMdecimal) {
                hasMdecimal = true;
                continue;
            }
            if (element == percent && !hasPercent) {
                hasPercent = true;
                continue;
            }
            count++;
        }
        return count;
    }
}
