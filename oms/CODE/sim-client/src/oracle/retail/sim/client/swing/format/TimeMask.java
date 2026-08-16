package oracle.retail.sim.client.swing.format;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.format.MaskAdaptor;

/******************************************************************************************
 * This classes handles formatting and unformatting of time values.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class TimeMask extends MaskAdaptor {

    /******************************************************************************************
     * Constructor
     *****************************************************************************************/
    public TimeMask() {
    }

    /******************************************************************************************
     * Formats an object into a time string. If the object is not a Date object, this will
     * return an empty string. It will return the time in the short time format if a Date object
     * is passed in.
     *****************************************************************************************/
    public String formatData(Object object) {
        if (object == null) {
            return StringConstants.EMPTY;
        }
        if (object instanceof Date) {
            return LocaleManager.getShortTimeFormatter().format((Date) object);
        }
        return StringConstants.EMPTY;
    }

    /******************************************************************************************
     * Formats a string into the proper time string. This will use TimeMaskUtility to attempt
     * to parse the text string for the particular locale, otherwise it will use the JAVA
     * default parser if not installed parser is found.
     *****************************************************************************************/
    public String format(String text) {
        if (StringUtility.isNullOrEmpty(text)) {
            return StringConstants.EMPTY;
        }

        text = TimeMaskUtility.parseText(LocaleManager.getLanguageLocale(), text);

        try {
            String dateText = LocaleManager.getShortDateFormatter().format(SimDateUtil.getCurrentDate());
            Date date = LocaleManager.getShortDateTimeFormatter().parse(dateText + " " + text);
            return LocaleManager.getShortTimeFormatter().format(date);
        } catch (Throwable exception) {
            return text;
        }
    }

    /******************************************************************************************
     * Unformats a text string.
     *****************************************************************************************/
    public String unformat(String text) {
        if (StringUtility.isNullOrEmpty(text)) {
            return StringConstants.EMPTY;
        }
        return text.trim();
    }

    /******************************************************************************************
     * Validate the text. This will use TimeMaskUtility to attempt to parse the text string
     * for the particular locale, otherwise it will use the JAVA default parser if not
     * installed parser is found.
     *****************************************************************************************/
    public BusinessException validate(String text) {
        if (text == null || text.length() == 0) {
            return null;
        }

        text = TimeMaskUtility.parseText(LocaleManager.getLanguageLocale(), text);

        try {
            String dateText = LocaleManager.getShortDateFormatter().format(SimDateUtil.getCurrentDate());
            Date date = LocaleManager.getShortDateTimeFormatter().parse(dateText + " " + text);
            LocaleManager.getShortTimeFormatter().format(date);
        } catch (Throwable exception) {
            return new BusinessException(UIMessageText.TIME_FORMAT_INVALID);
        }
        return null;
    }

    /******************************************************************************************
     * Validates the character entered into the field.
     *****************************************************************************************/
    public boolean validCharacter(char character) {
        if (Character.isDigit(character)) {
            return true;
        }
        if (Character.isISOControl(character)) {
            return true;
        }
        DateFormatSymbols symbols = LocaleManager.getDateFormatSymbols();
        String[] ampmArray = symbols.getAmPmStrings();
        for (String element : ampmArray) {
            char[] charArray = element.toCharArray();
            for (char element2 : charArray) {
                if (Character.toLowerCase(element2) == Character.toLowerCase(character)) {
                    return true;
                }
            }
        }
        SimpleDateFormat formatter = (SimpleDateFormat) LocaleManager.getShortTimeFormatter();
        char[] timeArray = formatter.toPattern().toCharArray();
        for (int i = 0; i < timeArray.length; i++) {
            if (!Character.isLetter(timeArray[i])) {
                if (timeArray[i] == character) {
                    return true;
                }
            }
        }
        return false;
    }
}
