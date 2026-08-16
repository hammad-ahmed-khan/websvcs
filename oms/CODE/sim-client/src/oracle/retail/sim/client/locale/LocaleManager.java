package oracle.retail.sim.client.locale;

import java.lang.reflect.Method;
import java.text.Collator;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.swing.UIManager;
import oracle.retail.sim.common.core.locale.DateFormatHelper;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.logging.LogService;

/********************************************************************************************************
 * This class handles assigning a single JVM locale instance to all of the various utilities.
 * <p>
 * Numeric locale handles all numbers, currencies and dates. Language locale handles all labels and
 * messages. These two values will often be the same, but may sometimes be different.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class LocaleManager {

    private static Locale numericLocale = Locale.getDefault();
    private static Locale languageLocale = Locale.getDefault();
    private static TimeZone applicationTimeZone;
    private static TranslatorManager translatorManager = new TranslatorManager();

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    private LocaleManager() {
    }

    /****************************************************************************************************
     * Sets the language locale of the application.
     * <p>
     * @param locale The locale object to assign to the application.
     ***************************************************************************************************/
    public static void setLanguageLocale(Locale locale) {
        languageLocale = locale;
        assignStringLocale();
        assignTranslatorLocale();
    }

    /****************************************************************************************************
     * Sets the numeric locale of the application. This will format numbers and dates.
     * <p>
     * @param locale The locale object to assign to the application.
     ***************************************************************************************************/
    public static void setNumericLocale(Locale locale) {
        numericLocale = locale;
        assignNumberLocale();
    }

    /****************************************************************************************************
     * Sets the timezone of the application.
     * <p>
     * @param timeZone The TimeZone to assign to the application.
     ***************************************************************************************************/
    public static void setTimeZone(TimeZone timeZone) {
        applicationTimeZone = timeZone;
    }

    /****************************************************************************************************
     * Retrieves the application locale.
     ***************************************************************************************************/
    public static Locale getNumericLocale() {
        return numericLocale;
    }

    /****************************************************************************************************
     * Retrieves the application locale.
     ***************************************************************************************************/
    public static Locale getLanguageLocale() {
        return languageLocale;
    }

    /****************************************************************************************************
     * Returns a country name for a user's locale given a country code (2- or 3- letter like US, FR, DE).
     ***************************************************************************************************/
    public static String getDisplayCountry(String countryCode, boolean useStoreLocale) {
        if (useStoreLocale) {
            return new Locale(StringConstants.EMPTY, countryCode).getDisplayCountry(numericLocale);
        } else {
            return new Locale(StringConstants.EMPTY, countryCode).getDisplayCountry(languageLocale);
        }
    }

    /****************************************************************************************************
     * Retrieves the application time zone.
     ***************************************************************************************************/
    public static TimeZone getTimeZone() {
        return applicationTimeZone;
    }

    /****************************************************************************************************
     * Retrieves the application collator. This has a default strength of Collator.TERTIARY.
     ***************************************************************************************************/
    public static Collator getCollator() {
        return Collator.getInstance(languageLocale);
    }

    /****************************************************************************************************
     * Retrieves the integer formatter for the locale.
     ***************************************************************************************************/
    public static NumberFormat getIntegerFormatter() {
        NumberFormat formatter = NumberFormat.getIntegerInstance(numericLocale);
        formatter.setGroupingUsed(false);
        return formatter;
    }

    /****************************************************************************************************
     * Retrieves the integer formatter for the locale.
     * <p>
     * @param isGroupingUsed True if grouping should be used in formatting, false otherwise.
     ***************************************************************************************************/
    public static NumberFormat getIntegerFormatter(boolean isGroupingUsed) {
        NumberFormat formatter = NumberFormat.getIntegerInstance(numericLocale);
        formatter.setGroupingUsed(isGroupingUsed);
        return formatter;
    }

    /****************************************************************************************************
     * Retrieves the general number formatter for the locale.
     ***************************************************************************************************/
    public static NumberFormat getNumberFormatter() {
        NumberFormat formatter = NumberFormat.getNumberInstance(numericLocale);
        formatter.setGroupingUsed(false);
        return formatter;
    }

    /****************************************************************************************************
     * Retrieves the general percent formatter for the locale.
     ***************************************************************************************************/
    public static NumberFormat getPercentFormatter() {
        NumberFormat percentFormat = NumberFormat.getPercentInstance(numericLocale);
        percentFormat.setMaximumFractionDigits(2);
        percentFormat.setGroupingUsed(false);
        return percentFormat;
    }

    /****************************************************************************************************
     * Retrieves the general currency formatter for the locale.
     ***************************************************************************************************/
    public static DecimalFormat getCurrencyFormatter() {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(numericLocale);
        decimalFormat.setGroupingUsed(false);
        return decimalFormat;
    }

    /****************************************************************************************************
     * Retrieves the general currency formatter for the specified currency.
     ***************************************************************************************************/
    public static DecimalFormat getCurrencyFormatter(Currency currency) {
        DecimalFormat decimalFormat = getCurrencyFormatter();
        decimalFormat.setGroupingUsed(false);
        decimalFormat.setCurrency(currency);
        return decimalFormat;
    }

    /****************************************************************************************************
     * Retrieves the general number decimal symbols for the locale.
     ***************************************************************************************************/
    public static DecimalFormatSymbols getNumberDecimalSymbols() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(numericLocale);
        if (numberFormat instanceof DecimalFormat) {
            return ((DecimalFormat) numberFormat).getDecimalFormatSymbols();
        }
        return null;
    }

    /****************************************************************************************************
     * Retrieves the general currency decimal symbols for the locale.
     ***************************************************************************************************/
    public static DecimalFormatSymbols getCurrencyDecimalSymbols() {
        return getCurrencyFormatter().getDecimalFormatSymbols();
    }

    /****************************************************************************************************
     * Retrieves the general currency decimal symbols for the currency.
     ***************************************************************************************************/
    public static DecimalFormatSymbols getCurrencyDecimalSymbols(Currency currency) {
        return getCurrencyFormatter(currency).getDecimalFormatSymbols();
    }

    /****************************************************************************************************
     * Retrieves the general currency decimal symbols for the currency.
     ***************************************************************************************************/
    public static DateFormatSymbols getDateFormatSymbols() {
        // Attempt JDK 6 version of getting date format symbols
        try {
            Method instanceMethod = DateFormatSymbols.class.getMethod("getInstance", new Class[] { Locale.class });
            Object objectValue = instanceMethod.invoke(null, numericLocale);
            if (objectValue instanceof DateFormatSymbols) {
                return (DateFormatSymbols) objectValue;
            }
        } catch (Throwable exception) {
            return new DateFormatSymbols(numericLocale);
        }
        return new DateFormatSymbols(numericLocale);
    }

    /****************************************************************************************************
     * Retrieves the message formatter for the locale with an empty pattern.
     ***************************************************************************************************/
    public static MessageFormat getMessageFormatter() {
        return new MessageFormat("", languageLocale);
    }

    /****************************************************************************************************
     * Retrieves the message formatter for the locale with a specified pattern.
     ***************************************************************************************************/
    public static MessageFormat getMessageFormatter(String pattern) {
        return new MessageFormat(pattern, languageLocale);
    }

    /****************************************************************************************************
     * Retrieves a short form date parser for the locale.
     ***************************************************************************************************/
    public static DateFormat getDateParser() {
        return createDateParser(numericLocale, applicationTimeZone, false);
    }

    /****************************************************************************************************
     * Retrieves a short form date parser for the locale.
     * @param timeZone Override current store timezone.
     ***************************************************************************************************/
    public static DateFormat getDateParser(TimeZone timeZone) {
        return createDateParser(numericLocale, timeZone, false);
    }

    /****************************************************************************************************
     * Retrieves a short form date time parser for the locale.
     ***************************************************************************************************/
    public static DateFormat getDateTimeParser(int dateType) {
        return createDateTimeParser(dateType, numericLocale, applicationTimeZone, false);
    }

    /****************************************************************************************************
     * Retrieves a short form date time parser for the locale.
     *  @param timeZone Override current store timezone.
     ***************************************************************************************************/
    public static DateFormat getDateTimeParser(int dateType, TimeZone timeZone) {
        return createDateTimeParser(dateType, numericLocale, timeZone, false);
    }

    /****************************************************************************************************
     * Retrieves a short form date formatter for the locale.
     ***************************************************************************************************/
    public static DateFormat getShortDateFormatter() {
        return createDateFormatter(DateFormat.SHORT, numericLocale, applicationTimeZone, false);
    }

    /****************************************************************************************************
     * Retrieves a short form time formatter for the locale.
     ***************************************************************************************************/
    public static DateFormat getShortTimeFormatter() {
        return createTimeFormatter(DateFormat.SHORT, numericLocale, applicationTimeZone, true);
    }

    /****************************************************************************************************
     * Retrieves a short form date time formatter for the locale.
     ***************************************************************************************************/
    public static DateFormat getShortDateTimeFormatter() {
        return createDateTimeFormatter(DateFormat.SHORT, numericLocale, applicationTimeZone, true);
    }

    /****************************************************************************************************
     * Retrieves a medium form date formatter for the locale.
     ***************************************************************************************************/
    public static DateFormat getMediumDateFormatter() {
        return createDateFormatter(DateFormat.MEDIUM, numericLocale, applicationTimeZone, true);
    }

    /****************************************************************************************************
     * Retrieves a medium form time formatter for the locale.
     ***************************************************************************************************/
    public static DateFormat getMediumTimeFormatter() {
        return createTimeFormatter(DateFormat.MEDIUM, numericLocale, applicationTimeZone, true);
    }

    /****************************************************************************************************
     * Retrieves a medium form date time formatter for the locale.
     ***************************************************************************************************/
    public static DateFormat getMediumDateTimeFormatter() {
        return createDateTimeFormatter(DateFormat.MEDIUM, numericLocale, applicationTimeZone, true);
    }

    /****************************************************************************************************
     * Retrieves a long form date formatter for the locale.
     ***************************************************************************************************/
    public static DateFormat getLongDateFormatter() {
        return createTimeFormatter(DateFormat.LONG, numericLocale, applicationTimeZone, true);
    }

    /****************************************************************************************************
     * Retrieves a long form time formatter for the locale.
     ***************************************************************************************************/
    public static DateFormat getLongTimeFormatter() {
        return createTimeFormatter(DateFormat.LONG, numericLocale, applicationTimeZone, true);
    }

    /****************************************************************************************************
     * Retrieves a long form date time formatter for the locale.
     ***************************************************************************************************/
    public static DateFormat getLongDateTimeFormatter() {
        return createDateTimeFormatter(DateFormat.LONG, numericLocale, applicationTimeZone, true);
    }

    /****************************************************************************************************
     * Retrieves a full form date formatter for the locale.
     ***************************************************************************************************/
    public static DateFormat getFullDateFormatter() {
        return createDateFormatter(DateFormat.FULL, numericLocale, null, true);
    }

    /****************************************************************************************************
     * Retrieves a full form time formatter for the locale.
     ***************************************************************************************************/
    public static DateFormat getFullTimeFormatter() {
        return createTimeFormatter(DateFormat.FULL, numericLocale, applicationTimeZone, true);
    }

    /****************************************************************************************************
     * Retrieves a full form date time formatter for the locale.
     ***************************************************************************************************/
    public static DateFormat getFullDateTimeFormatter() {
        return createDateTimeFormatter(DateFormat.FULL, numericLocale, applicationTimeZone, true);
    }

    /****************************************************************************************************
     * Retrieves the first day of the week.
     ***************************************************************************************************/
    public static int getFirstDayOfWeek() {
        StringBuilder key = new StringBuilder();
        key.append(numericLocale.getLanguage());
        key.append(numericLocale.getCountry());
        key.append(".");
        key.append("firstDayOfWeek");

        String number = UIManager.getString(key.toString());
        if (number != null) {
            try {
                int value = Integer.parseInt(number);
                if (value > 0 && value < 8) {
                    return value;
                }
            } catch (Throwable exception) {
                // Allow method to complete
                // Ignore these...
                LogService.debug(LocaleManager.class, "LocaleManager ignoring Excepton string to number " + key.toString());
            }
        }
        return GregorianCalendar.getInstance(numericLocale).getFirstDayOfWeek();
    }

    /****************************************************************************************************
     * Return true if short date text is invalid for a SHORT format, false if it is valid.
     ***************************************************************************************************/
    public static boolean isInvalidShortDate(String dateText) {
        return !DateFormatHelper.isValidShortDate(dateText, LocaleManager.getDateParser());
    }

    /****************************************************************************************************
     * Helper method to assign the locale to the String Utility class.
     ***************************************************************************************************/
    private static void assignStringLocale() {
        StringUtility.setTextLocale(languageLocale);
    }

    /****************************************************************************************************
     * Helper method to assign the locale to the String Utility class.
     ***************************************************************************************************/
    private static void assignNumberLocale() {
        StringUtility.setNumericLocale(numericLocale);
    }

    /****************************************************************************************************
     * Helper method to assign the locale to the translator class.
     ***************************************************************************************************/
    private static void assignTranslatorLocale() {
        Translator.setTranslationMap(translatorManager.readTranslationMap(languageLocale));
    }

    /****************************************************************************************************
     * Helper method to create a data formatter. This will attempt to find a configurable overriding
     * pattern for the date type and locale and assign it.
     ***************************************************************************************************/
    private static SimpleDateFormat createTimeFormatter(int dateType, Locale locale, TimeZone timeZone, boolean lenient) {
        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getTimeInstance(dateType, locale);
        if (timeZone != null) {
            formatter.setTimeZone(timeZone);
        }
        formatter.setLenient(lenient);
        return formatter;
    }

    /****************************************************************************************************
     * Helper method to create a data formatter. This will attempt to find a configurable overriding
     * pattern for the date type and locale and assign it.
     ***************************************************************************************************/
    private static SimpleDateFormat createDateTimeFormatter(int dateType, Locale locale, TimeZone timeZone, boolean lenient) {
        StringBuilder key = new StringBuilder();
        key.append(locale.getLanguage());
        key.append(locale.getCountry());
        key.append(".");

        if (dateType == DateFormat.SHORT) {
            key.append("shortDate");
        } else if (dateType == DateFormat.MEDIUM) {
            key.append("mediumDate");
        } else if (dateType == DateFormat.LONG) {
            key.append("longDate");
        } else {
            key.append("fullDate");
        }

        String datePattern = UIManager.getString(key.toString());
        String timePattern = createTimeFormatter(dateType, locale, timeZone, lenient).toPattern();

        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateTimeInstance(dateType, dateType, locale);
        if (datePattern != null) {
            formatter.applyPattern(datePattern + " " + timePattern);
        }
        if (timeZone != null) {
            formatter.setTimeZone(timeZone);
        }
        formatter.setLenient(lenient);
        return formatter;
    }

    /****************************************************************************************************
     * Helper method to create a data parser. This will attempt to find a configurable overriding pattern
     * for the date type and locale and assign it.
     ***************************************************************************************************/
    private static SimpleDateFormat createDateParser(Locale locale, TimeZone timeZone, boolean lenient) {
        StringBuilder key = new StringBuilder();
        key.append(locale.getLanguage());
        key.append(locale.getCountry());
        key.append(".");
        key.append("entryDate");

        String pattern = UIManager.getString(key.toString());
        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, locale);
        if (pattern != null) {
            formatter.applyPattern(pattern);
        }
        if (timeZone != null) {
            formatter.setTimeZone(timeZone);
        }
        formatter.setLenient(lenient);
        return formatter;
    }

    /****************************************************************************************************
     * Helper method to create a data parser. This will attempt to find a configurable overriding pattern
     * for the date type and locale and assign it.
     ***************************************************************************************************/
    private static SimpleDateFormat createDateTimeParser(int dateType, Locale locale, TimeZone timeZone, boolean lenient) {
        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateInstance(dateType, locale);
        if (timeZone != null) {
            formatter.setTimeZone(timeZone);
        }
        formatter.setLenient(lenient);
        return formatter;
    }

    /****************************************************************************************************
     * Helper method to create a data formatter. This will attempt to find a configurable overriding
     * pattern for the date type and locale and assign it.
     ***************************************************************************************************/
    private static SimpleDateFormat createDateFormatter(int dateType, Locale locale, TimeZone timeZone, boolean lenient) {
        StringBuilder key = new StringBuilder();
        key.append(locale.getLanguage());
        key.append(locale.getCountry());
        key.append(".");

        if (dateType == DateFormat.SHORT) {
            key.append("shortDate");
        } else if (dateType == DateFormat.MEDIUM) {
            key.append("mediumDate");
        } else if (dateType == DateFormat.LONG) {
            key.append("longDate");
        } else {
            key.append("fullDate");
        }

        String pattern = UIManager.getString(key.toString());
        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateInstance(dateType, locale);
        if (pattern != null) {
            formatter.applyPattern(pattern);
        }
        if (timeZone != null) {
            formatter.setTimeZone(timeZone);
        }
        formatter.setLenient(lenient);
        return formatter;
    }
}
