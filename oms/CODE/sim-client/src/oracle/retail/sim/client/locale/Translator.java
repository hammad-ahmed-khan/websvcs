package oracle.retail.sim.client.locale;

import java.util.HashMap;
import java.util.Map;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.logging.LogService;

/********************************************************************************************************
 * This class handles translation for the application on the client side.
 * <p>
 * As of the current design, string values are the only "replacement" values allowed by this utility. All
 * formatting of the "replacement" data should be handled prior to building the message. For example, if
 * the replacement value in a message is a timestamp, the date object should be formatted into a
 * timestamp string by another utility before using this class. Although MessageFormat (upon which this
 * class is based) allows for "replacement" values to have a format assigned to them (ie, date, number),
 * they use system default formatters to accomplish the translation. It is simpler and more consistant to
 * pass in already formatted data.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class Translator {
    private static Map<String, String> translationMap = new HashMap<String, String>();

    /****************************************************************************************************
     * Private constructor for static class.
     ***************************************************************************************************/
    private Translator() {
    }

    /****************************************************************************************************
     * Assigns the translation map (of key=value pairs) to the translator.
     * <p>
     * @param map The map of key-value pairs to use for translation.
     ***************************************************************************************************/
    public static void setTranslationMap(Map<String, String> map) {
        if (map != null) {
            translationMap = map;
        }
    }

    /****************************************************************************************************
     * Retrieves the translation map (of key=value pairs).
     * <p>
     * @param map The map of key-value pairs used for translation.
     ***************************************************************************************************/
    public static Map<String, String> getTranslationMap() {
        return translationMap;
    }

    /****************************************************************************************************
     * Retrieves the translation for a specific text key. If a value is not found, or the value is not a
     * String object, the key is returned.
     * <p>
     * @param text The text to translate.
     * @return The translated text.
     ***************************************************************************************************/
    public static String getText(String key) {
        String translation = translationMap.get(key);
        if (translation == null) {
            return processUntranslatedKey(key);
        }
        return translation;
    }

    /****************************************************************************************************
     * Retrieves the translation for a specific text key. If a value is not found, or the value is not a
     * String object, the key is returned.
     * <p>
     * @param text The text to translate.
     * @return The translated text.
     ***************************************************************************************************/
    public static char getMnemonic(String key) {
        try {
            return translationMap.get(key).charAt(0);
        } catch (Exception exception) {
            return '\0';
        }
    }

    /****************************************************************************************************
     * Retrieves the translation for a specific text key. If a value is not found, or the value is not a
     * String object, the key is returned. This method is identical to getText().
     * <p>
     * @param text The text to translate.
     * @return The translated text.
     ***************************************************************************************************/
    public static String getMessage(String key) {
        String translation = translationMap.get(key);
        if (translation == null) {
            return processUntranslatedKey(key);
        }
        return translation;
    }

    /****************************************************************************************************
     * Retrieves the message translation for a specific message. If no translation exists, the the
     * original message is returned. This method allows a single string parameter to replace the first
     * placeholder in the message text. The value should be properly formatted prior to calling this
     * method.
     * <p>
     * @param message The message to retrieve a translation for.
     * @param valueOne A string value to replace a placeholder in the message text.
     * @return The translated and expanded message.
     ***************************************************************************************************/
    public static String getMessage(String message, String value) {
        String[] array = new String[1];
        array[0] = value;
        return getMessage(message, array);
    }

    /****************************************************************************************************
     * Retrieves the message translation for a specific message. If no translation exists, the the
     * original message is returned. This method allows a pair of string parameters to replace the first
     * few placeholders in the message text. The values should be properly formatted prior to calling
     * this method.
     * <p>
     * @param message The message to retrieve a translation for.
     * @param valueOne A string value to replace a placeholder in the message text.
     * @param valueTwo A string value to replace a placeholder in the message text.
     * @return The translated and expanded message.
     ***************************************************************************************************/
    public static String getMessage(String message, String valueOne, String valueTwo) {
        String[] array = new String[2];
        array[0] = valueOne;
        array[1] = valueTwo;
        return getMessage(message, array);
    }

    /****************************************************************************************************
     * Retrieves the message translation for a specific message. If no translation exists, the the
     * original message is returned. This method allows a pair of string parameters to replace the first
     * few placeholders in the message text. The values should be properly formatted prior to calling
     * this method.
     * <p>
     * @param message The message to retrieve a translation for.
     * @param valueOne A string value to replace a placeholder in the message text.
     * @param valueTwo A string value to replace a placeholder in the message text.
     * @param valueThree A string value to replace a placeholder in the message text.
     * @return The translated and expanded message.
     ***************************************************************************************************/
    public static String getMessage(String message, String valueOne, String valueTwo, String valueThree) {
        String[] array = new String[3];
        array[0] = valueOne;
        array[1] = valueTwo;
        array[2] = valueThree;
        return getMessage(message, array);
    }

    /****************************************************************************************************
     * Retrieves the message translation for a specific message. If no translation exists, the original
     * message is returned. This method uses the string array to replace any placeholders found in the
     * message text in sequence. The values should be properly formatted prior to calling this method.
     * <p>
     * @param message The message to retrieve a translation for.
     * @param valueArray An array of strings to replace placeholders in the message text.
     * @return The translated and expanded message.
     ***************************************************************************************************/
    public static String getMessage(String message, Object[] valueArray) {
        String messageText = getMessage(message);
        if (valueArray == null || valueArray.length <= 0) {
            return messageText;
        }
        for (int i = 0; i < valueArray.length; i++) {
            if (valueArray[i] instanceof String) {
                String translation = translationMap.get(valueArray[i]);
                if (translation != null) {
                    valueArray[i] = translation;
                }
            } else if (valueArray[i] == null) {
                valueArray[i] = StringConstants.EMPTY;
            } else {
                valueArray[i] = String.valueOf(valueArray[i]);
            }
        }
        String messagePattern = StringUtility.replace(messageText, "'", "''");
        return LocaleManager.getMessageFormatter(messagePattern).format(valueArray);
    }

    /****************************************************************************************************
     * Processes an untranslated key to flag it in the system visually.
     ***************************************************************************************************/
    private static String processUntranslatedKey(String key) {
        if (LogService.isDebugEnabled(Translator.class)) {
            LogService.debug(Translator.class, "The following key has no translation: [" + key + "]");
        }
        return key;
    }
}
