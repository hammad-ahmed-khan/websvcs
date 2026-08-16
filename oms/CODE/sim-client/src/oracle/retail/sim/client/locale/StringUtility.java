package oracle.retail.sim.client.locale;

import java.awt.FontMetrics;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import oracle.retail.sim.common.core.locale.NumberHelper;
import oracle.retail.sim.common.core.locale.StringHelper;

/********************************************************************************************************
 * Static class that assists in the management and manipulation of strings specifically for the client.
 * This should not be used by classes that might execute on the server such as rules, beans and business
 * objects.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StringUtility {
    private static StringHelper stringHelper = new StringHelper(Locale.US);
    private static NumberHelper numberHelper = new NumberHelper(Locale.US);

    /****************************************************************************************************
     * Static constructor.
     ***************************************************************************************************/
    private StringUtility() {
    }

    /****************************************************************************************************
     * Assigns the locale of the string helper. Locale.US is the default.
     * <p>
     * @param locale The locale to assign.
     ***************************************************************************************************/
    public static void setTextLocale(Locale locale) {
        stringHelper = new StringHelper(locale);
    }

    /****************************************************************************************************
     * Assigns the locale of the number helper. Locale.US is the default.
     * <p>
     * @param locale The numeric locale to assign.
     ***************************************************************************************************/
    public static void setNumericLocale(Locale locale) {
        numberHelper = new NumberHelper(locale);
    }

    /****************************************************************************************************
     * Returns true if the text string is null or empty, false if it contains something.
     * <p>
     * @param text The text.
     * @return True if the text was null or empty, false otherwise.
     ***************************************************************************************************/
    public static boolean isNullOrEmpty(String text) {
        return StringHelper.isNullOrEmpty(text);
    }

    /****************************************************************************************************
     * Trims a string, returning null if the input string is already null and an empty string if the
     * string is empty.
     * <p>
     * @param text The text.
     * @return The trimmed text.
     ***************************************************************************************************/
    public static String trim(String text) {
        return StringHelper.trim(text);
    }

    /****************************************************************************************************
     * Trims a string, returning null if the input string is null or empty.
     * <p>
     * @param text The text.
     * @param The trimmed text or null if the text is empty.
     ***************************************************************************************************/
    public static String trimToNull(String text) {
        return StringHelper.trimToNull(text);
    }

    /****************************************************************************************************
     * Return true if the text represents a positive whole number (one such that it would be valid
     * as the unique identifier of a business object).
     * <p>
     * @param text The string to validate.
     * @return True if the string is a positive whole number, false otherwise.
     ***************************************************************************************************/
    public static boolean isIdentifierNumeric(String text) {
        return NumberHelper.isIdentifierNumeric(text);
    }

    /****************************************************************************************************
     * Returns true if the string is an integer or a decimal, false if not a number
     * <p>
     * @param text The string to validate.
     * @return True if the string is numeric, false otherwise.
     ***************************************************************************************************/
    public static boolean isNumericOrDecimal(String text) {
        return numberHelper.isNumericOrDecimal(text);
    }

    /****************************************************************************************************
     * Return true if the text represents a valid integer. In SIM, only a minus sign is allowed.
     * <p>
     * @param text The string to validate.
     * @return True if the string is numeric, false otherwise.
     ***************************************************************************************************/
    public static boolean hasDecimalSeparator(String text) {
        return numberHelper.hasDecimalSeparator(text);
    }

    /****************************************************************************************************
     * Return true if the text represents a valid integer. In SIM, only a minus sign is allowed.
     * <p>
     * @param text The string to validate.
     * @return True if the string is numeric, false otherwise.
     ***************************************************************************************************/
    public static boolean isValidIntegerInput(String text) {
        return numberHelper.isValidIntegerInput(text);
    }

    /****************************************************************************************************
     * Return true if the text represents a valid decimal input. In SIM, numeric grouping separators are
     * not allowed.
     * <p>
     * @param text The string to validate.
     * @return True if the string is valid decimal input, false otherwise.
     ***************************************************************************************************/
    public static boolean isValidDecimalInput(String text) {
        return numberHelper.isValidDecimalInput(text);
    }

    /****************************************************************************************************
     * Return true if the text represents a valid percent input. In SIM, numeric grouping separators are
     * not allowed.
     * <p>
     * @param text The string to validate.
     * @return True if the string is valid decimal input, false otherwise.
     ***************************************************************************************************/
    public static boolean isValidPercentInput(String text) {
        return numberHelper.isValidPercentInput(text);
    }

    /****************************************************************************************************
     * Returns true if two strings are equal by locale collator standards, false if not.
     * Two null strings will be considered equal.
     * <p>
     * @param stringA The first string.
     * @param stringB The second string.
     * @return True if the two strings are equal.
     ***************************************************************************************************/
    public static boolean isEqual(String stringA, String stringB) {
        return stringHelper.isEqual(stringA, stringB);
    }

    /****************************************************************************************************
     * Returns true if two strings are equal by locale collator standards after trimming trailing whitespace.
     * Two null strings will be considered equal.
     * <p>
     * @param stringA The first string.
     * @param stringB The second string.
     * @return True if the two strings are equal.
     ***************************************************************************************************/
    public static boolean isEqualTrim(String stringA, String stringB) {
        return stringHelper.isEqualTrim(stringA, stringB);
    }

    /****************************************************************************************************
     * Returns true if the string represents true [ "1", "yes", "true" ]. This method is not yet
     * internationalized.
     * <p>
     * @param text The string.
     * @return The boolean value that the string represents.
     ***************************************************************************************************/
    public static boolean booleanValue(String value) {
        return stringHelper.booleanValue(value);
    }

    /****************************************************************************************************
     * Returns the index within this string of the first occurrence of the specified character.
     * <p>
     * @param text The string to search.
     * @param pattern The pattern to find within the text.
     * @return The index (or -1 if the text is null or the pattern is not found)
     ***************************************************************************************************/
    public static int indexOf(String text, char pattern) {
        return stringHelper.indexOf(text, pattern);
    }

    /****************************************************************************************************
     * Returns the index within this string of the last occurrence of the specified character.
     * <p>
     * @param text The string to search.
     * @param pattern The pattern to find within the text.
     * @return The index (or -1 if the text is null or the pattern is not found)
     ***************************************************************************************************/
    public static int lastIndexOf(String text, char pattern) {
        return stringHelper.lastIndexOf(text, pattern);
    }

    /****************************************************************************************************
     * Returns the index within this string of the first occurrence of the specified substring.
     * <p>
     * @param text The string to search.
     * @param pattern The pattern to find within the text.
     * @return The index (or -1 if the text is null or the pattern is not found)
     ***************************************************************************************************/
    public static int indexOf(String text, String pattern) {
        return stringHelper.indexOf(text, pattern);
    }

    /****************************************************************************************************
     * Returns the index within this string of the last occurrence of the specified substring.
     * <p>
     * @param text The string to search.
     * @param pattern The pattern to find within the text.
     * @return The index (or -1 if the text is null or the pattern is not found)
     ***************************************************************************************************/
    public static int lastIndexOf(String text, String pattern) {
        return stringHelper.lastIndexOf(text, pattern);
    }

    /****************************************************************************************************
     * Returns true if the text starts with the specified substring.
     * <p>
     * <p>
     * @param text The string to search.
     * @param pattern The pattern the string must begin with.
     * @return True if the text begins with the specified substring.
     ***************************************************************************************************/
    public static boolean startsWith(String text, String pattern) {
        return stringHelper.startsWith(text, pattern);
    }

    /****************************************************************************************************
     * Returns the comparison value of two strings: a negative number if less than, zero if equal two, or
     * a positive number if greater than.
     * <p>
     * @param textOne The first string.
     * @param textTwo The second string.
     * @return A negative integer, zero, or a positive integer if the first argument is less than, equal
     *         to, or greater than the second.
     ***************************************************************************************************/
    public static int compareTo(String textOne, String textTwo) {
        return stringHelper.compareTo(textOne, textTwo);
    }

    /****************************************************************************************************
     * Returns the comparison value of two strings: a negative number if less than, zero if equal two, or
     * a positive number if greater than. This comparator will ignore capitalization (ie. case).
     * <p>
     * @param textOne The first string.
     * @param textTwo The second string.
     * @return A negative integer, zero, or a positive integer if the first argument is less than, equal
     *         to, or greater than the second.
     ***************************************************************************************************/
    public static int compareToIgnoreCase(String textOne, String textTwo) {
        return stringHelper.compareToIgnoreCase(textOne, textTwo);
    }

    /****************************************************************************************************
     * Sorts the text array using the rules of the current locale.
     * <p>
     * @param textArray An array of strings.
     * @return The sorted array of strings.
     ***************************************************************************************************/
    public static String[] sort(String[] textArray) {
        return stringHelper.sort(textArray);
    }

    /****************************************************************************************************
     * Sorts the text array using the rules of the current locale.
     * <p>
     * @param textList A list containing only strings.
     * @return The sorted list of strings.
     ***************************************************************************************************/
    public static List<String> sort(List<String> textList) {
        return stringHelper.sort(textList);
    }

    /****************************************************************************************************
     * Returns true if the string contains two or more characters.
     * <p>
     * @param text The string to search for multiple characters.
     * @param character The character to check for multiple occurrences.
     * @return True if multiple instances of the character is found.
     ***************************************************************************************************/
    public static boolean hasMultiplesOfChar(String text, char character) {
        return stringHelper.hasMultiplesOfChar(text, character);
    }

    /****************************************************************************************************
     * Convert the text string to upper case using the rules of the locale. Returns empty strings for
     * null text values as default.
     * <p>
     * @param text The string to convert.
     * @return The converted string or null.
     ***************************************************************************************************/
    public static String toUpperCase(String text) {
        return stringHelper.toUpperCase(text, false);
    }

    /****************************************************************************************************
     * Convert the text string to upper case using the rules of the locale.
     * <p>
     * @param text The string to convert.
     * @param returnNull True if a null string should return null, false returns empty string.
     * @return The converted string, null, or empty string depending on the parameters.
     ***************************************************************************************************/
    public static String toUpperCase(String text, boolean returnNull) {
        return stringHelper.toUpperCase(text, returnNull);
    }

    /****************************************************************************************************
     * Covert the text string to lower case using the rules of the locale. Returns empty strings for null
     * text values as default.
     * <p>
     * @param text The string to convert.
     * @return The converted string or null.
     ***************************************************************************************************/
    public static String toLowerCase(String text) {
        return stringHelper.toLowerCase(text, false);
    }

    /****************************************************************************************************
     * Convert the text string to lower case using the rules of the locale.
     * <p>
     * @param text The string to convert.
     * @param returnNull True if a null string should return null, false returns empty string.
     * @return The converted string, null, or empty string depending on the parameters.
     ***************************************************************************************************/
    public static String toLowerCase(String text, boolean returnNull) {
        return stringHelper.toLowerCase(text, returnNull);
    }

    /****************************************************************************************************
     * Returns a new string that is a substring of this string. The substring begins with the character
     * at the specified index and extends to the end of this string.
     * <p>
     * @param text The string to create the substring from.
     * @param beginIndex The beginning index, inclusive.
     * @return The new substring.
     ***************************************************************************************************/
    public static String substring(String text, int beginIndex) {
        return stringHelper.substring(text, beginIndex);
    }

    /****************************************************************************************************
     * Returns a new string that is a substring of this string. The substring begins at the specified
     * beginIndex and extends to the character at index endIndex - 1. Thus the length of the substring is
     * endIndex-beginIndex.
     * <p>
     * @param text The string to create the substring from.
     * @param beginIndex The beginning index, inclusive.
     * @param endIndex The ending index, inclusive.
     * @return The new substring.
     ***************************************************************************************************/
    public static String substring(String text, int beginIndex, int endIndex) {
        return stringHelper.substring(text, beginIndex, endIndex);
    }

    /****************************************************************************************************
     * Truncates the string to the length of bytes or less depending on character boundaries.
     * <p>
     * @param text The string to truncate.
     * @param maxBytes Maximum number of bytes.
     * @return The truncated string.
     * @throws UnsupportedEncodingException
     ***************************************************************************************************/
    public static String truncate(String text, int maxBytes) throws UnsupportedEncodingException {
        return StringHelper.truncate(text, maxBytes);
    }

    /****************************************************************************************************
     * Replaces one substring within text with a replacement string.
     * <p>
     * @param text The string to scan and replace.
     * @param substring The substring to convert.
     * @param replacement The replacement string
     * @return The new string with the replaced text.
     ***************************************************************************************************/
    public static String replace(String text, String substring, String replacement) {
        return stringHelper.replace(text, substring, replacement);
    }

    /****************************************************************************************************
     * Retrieves remaining string after the last instance of the separator string.
     * <p>
     * @param text The source text.
     * @param separator The separator pattern to find with the text.
     * @return A new string containing all text after last occurance of the separator.
     ***************************************************************************************************/
    public static String getRemainingText(String text, String separator) {
        return stringHelper.getRemainingText(text, separator);
    }

    /****************************************************************************************************
     * Returns an array of string, each string shorter than the lineLenth indicated in the parameter.
     * This method will split the text on a SPACE character.
     * <p>
     * @param text The text to break into an array based on length.
     * @param lineLength The maximum length to allow for a single string.
     * @return An array of strings.
     ***************************************************************************************************/
    public static String[] splitStringByLength(String text, int lineLength) {
        return stringHelper.splitStringByLength(text, lineLength);
    }

    /****************************************************************************************************
     * Remove all whitespace from within a string.
     * <p>
     * @param text The string.
     * @return A new string containing no whitespace.
     ***************************************************************************************************/
    public static String removeAllWhitespace(String text) {
        return StringHelper.removeAllWhitespace(text);
    }

    /****************************************************************************************************
     * Collapses a text string to a valid integer. It removes all non-digit text except for the minus
     * sign of the locale. Zero is returned if no valid numeric text is found.
     * <p>
     * @param text The string.
     * @return An integer string.
     ***************************************************************************************************/
    public static String collapseToInteger(String text) {
        return numberHelper.collapseToInteger(text);
    }

    /****************************************************************************************************
     * Collapses a text string to a valid decimal. It removes all non-digit text except for the minus
     * sign and decimal separator of the locale. Zero is returned if no valid numeric text is found.
     * <p>
     * @param text The string.
     * @return An decimal string.
     ***************************************************************************************************/
    public static String collapseToDecimal(String text) {
        return numberHelper.collapseToDecimal(text);
    }

    /****************************************************************************************************
     * Parses the string into a string array based on the token based in.
     * <p>
     * @param text The original string.
     * @param regex The regular expression to break up the string on.
     * @return An array of strings split by the regular expression delimeter.
     ***************************************************************************************************/
    public static String[] getStringArray(String text, String regex) {
        return StringHelper.getStringArray(text, regex);
    }

    /****************************************************************************************************
     * Parses the text into an array and then measures each member of the array, returning the longest
     * size. This method is not truly intended for external consumption, but to process framework
     * information only. It has not been internationalized.
     * <p>
     * @param metrics The font metrics to measures the strings with.
     * @param text The original text.
     * @param delimeter The text delimeter.
     * @return The longest parsed text string in number of pixels.
     ***************************************************************************************************/
    public static int longestSize(FontMetrics metrics, String text, String delimeter) {
        return stringHelper.longestSize(metrics, text, delimeter);
    }

    /****************************************************************************************************
     * Retrieves a method name. It capitalizes the first letter and leaves the remainder unchanged. For
     * example: promotion becomes Promotion and promotionPrice becomes PromotionPrice. This is used to
     * scrub method names before calling getX() or getY(). This is not internationalized, but is used
     * strictly for internal framework functionality..
     * <p>
     * @param prefix The prefix to apply (usually "get" or "set")
     * @param attribute The attribute to apply to the method name.
     ***************************************************************************************************/
    public static String getMethodName(String prefix, String attribute) {
        return StringHelper.getMethodName(prefix, attribute);
    }

    /****************************************************************************************************
     * The input text is scanned for occurrences of "/" or "\" within a string and these characters are
     * replaced with the system file separator. This method is used for internal framework functionality
     * only and is not internationalized.
     * <p>
     * @param text The original text.
     * @return The converted text.
     ***************************************************************************************************/
    public static String convertToFilename(String text) {
        return StringHelper.convertToFilename(text);
    }

    /****************************************************************************************************
     * Removes all duplicate strings from the array.
     * <p>
     * @param array An array of strings.
     * @return An array of unique strings.
     ***************************************************************************************************/
    public static String[] removeDuplicateStrings(String[] array) {
        return stringHelper.removeDuplicateStrings(array);
    }

    /****************************************************************************************************
     * Returns the start and end indexes of the first instance of 'pattern' found in 'text'. The value of
     * return[0] = start index of pattern, or -1 if pattern not found. The value of return[1] = end index
     * of pattern, or -1 if pattern not found.
     * <p>
     * @param text The text to search within.
     * @param pattern The text to search for.
     * @return An int array.
     ***************************************************************************************************/
    public int[] indexRangeOf(String text, String pattern) {
        return stringHelper.indexRangeOf(text, pattern);
    }
}
