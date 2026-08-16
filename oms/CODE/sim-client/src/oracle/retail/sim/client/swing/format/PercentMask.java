package oracle.retail.sim.client.swing.format;

import java.text.ParseException;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.format.MaskAdaptor;

/********************************************************************************************************
 * This classes handles formatting and validating of percent text.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class PercentMask extends MaskAdaptor {

    private boolean limitToHundred;

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    public PercentMask() {
    }

    /****************************************************************************************************
     * Constructor
     * <p>
     * @param limitToHundred True if the percent range should be limited to maximum 100%, false if
     *            allowed to be greater than 100.
     ***************************************************************************************************/
    public PercentMask(boolean limitToHundred) {
        this.limitToHundred = limitToHundred;
    }

    /****************************************************************************************************
     * Formats data to a standard percent format built using the current locale. If the date is not a
     * valid percent, the original text is returned.
     * <p>
     * @param text The input text.
     * @return The formatted text.
     ***************************************************************************************************/
    public String formatData(Object data) {
        if (data == null) {
            return StringConstants.EMPTY;
        }
        try {
            return LocaleManager.getPercentFormatter().format(data);
        } catch (Exception exception) {
            return data.toString();
        }
    }

    /****************************************************************************************************
     * Formats a text string to be in the percent format.
     ***************************************************************************************************/
    public String format(String text) {
        if (StringUtility.isNullOrEmpty(text)) {
            return StringConstants.EMPTY;
        }
        if (!StringUtility.isValidPercentInput(text)) {
            return text;
        }
        try {
            return LocaleManager.getPercentFormatter().format(parsePercent(text));
        } catch (Throwable exception) {
            return text;
        }
    }

    /****************************************************************************************************
     * Unformats a percent text string to just return the number.
     ***************************************************************************************************/
    public String unformat(String text) {
        if (text == null) {
            return StringConstants.EMPTY;
        }
        char percent = LocaleManager.getNumberDecimalSymbols().getPercent();
        char separator = LocaleManager.getNumberDecimalSymbols().getGroupingSeparator();
        int percentSignIndex = StringUtility.indexOf(text, percent);
        if (percentSignIndex == 0) {
            text = StringConstants.EMPTY;
        } else if (percentSignIndex > 0) {
            text = StringUtility.substring(text, 0, percentSignIndex);
        }
        return StringUtility.replace(text, String.valueOf(separator), StringConstants.EMPTY);
    }

    /****************************************************************************************************
     * Validates percent text to verify it is a valid percent number.
     ***************************************************************************************************/
    public BusinessException validate(String text) {
        if (text.length() > 0) {
            if (!StringUtility.isValidPercentInput(text)) {
                return buildInvalidNumberException(text);
            }
            text = StringUtility.collapseToDecimal(text);
            try {
                double value = parsePercent(text);
                if (value < 0) {
                    return new BusinessException(CommonMessageText.VALUE_INVALID_NEGATIVE);
                }
                if (limitToHundred && value > 1) {
                    Object[] params = { text, "0", "1" };
                    return new BusinessException(CommonMessageText.VALUE_NOT_IN_RANGE, params);
                }
            } catch (ParseException e) {
                return buildInvalidNumberException(text);
            }
        }
        return null;
    }

    /****************************************************************************************************
     * Helper method to build the invalid number exception.
     ***************************************************************************************************/
    private BusinessException buildInvalidNumberException(String text) {
        return new BusinessException(CommonMessageText.VALUE_NOT_VALID, text);
    }

    /****************************************************************************************************
     * This method attempts to validate that the character is allowed for this particular mask. Text
     * fields that have a mask will not allow entry that is invalid.
     * <p>
     * @param character The character to validate.
     * @return True if the character is valid, false otherwise.
     ***************************************************************************************************/
    public boolean validCharacter(char character) {
        return true;
    }

    /****************************************************************************************************
     * Parse text number down to a percent that can be formatted later.
     ***************************************************************************************************/
    private double parsePercent(String text) throws ParseException {
        return LocaleManager.getNumberFormatter().parse(text).doubleValue() / 100;
    }
}
