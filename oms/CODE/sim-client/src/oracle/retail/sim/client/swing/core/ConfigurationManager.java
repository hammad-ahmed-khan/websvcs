package oracle.retail.sim.client.swing.core;

import java.awt.FocusTraversalPolicy;
import java.awt.KeyboardFocusManager;
import java.util.Locale;
import oracle.retail.sim.client.application.RPropertyBundle;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.swing.format.TimeMaskUtility;
import oracle.retail.sim.client.swing.format.USTimeParser;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.plaf.custom.CustomSwanLookAndFeel;
import oracle.retail.sim.client.swing.util.PermissionTranslator;
import oracle.retail.sim.client.swing.util.TextLengthTranslator;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * This class handles configuration of Oracle Swing GUI application...
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ConfigurationManager {
    private static final String CONFIG_TEXT_LENGTH_FILENAME = "TextLength Filename";
    private static final String CONFIG_PERMISSION_FILENAME = "Permission Filename";
    private static final String CONFIG_REQUIRED_FILENAME = "Required Filename";
    private static final String CONFIG_MARGIN_THEME_FILENAME = "MarginTheme Filename";
    private static final String CONFIG_ICON_THEME_FILENAME = "IconTheme Filename";
    private static final String CONFIG_WIDGET_THEME_FILENAME = "WidgetTheme Filename";
    private static final String CONFIG_FOCUS_POLICY = "FocusPolicy";

    private static final String DEFAULT_TEXT_LENGTH_FILENAME = "TextLength";
    private static final String DEFAULT_MARGIN_THEME_FILENAME = "MarginTheme";
    private static final String DEFAULT_ICON_THEME_FILENAME = "IconTheme";
    private static final String DEFAULT_WIDGET_THEME_FILENAME = "WidgetTheme";
    private static final String DEFAULT_PERMISSION_FILENAME = "Permission";
    private static final String DEFAULT_REQUIRED_FILENAME = "Required";

    private static RPropertyBundle configuration = new RPropertyBundle();
    private final ThemeManager themeManager = new ThemeManager();

    /****************************************************************************************************
     * Returns new ConfigurationManager object.
     ***************************************************************************************************/
    public ConfigurationManager() {
    }

    public static RPropertyBundle getConfiguration() {
        return configuration;
    }

    /****************************************************************************************************
     * Configures the application. Since configuration must succeed for the application to run, fatal
     * errors will display to System.err and terminate the client startup process.
     * <p>
     * @param filename The filename of the system configuration file.
     ***************************************************************************************************/
    public void configure(String filename) {
        configure(filename, true);
    }

    /****************************************************************************************************
     * Configures the application. Since configuration must succeed for the application to run, fatal
     * errors will display to System.err and terminate the client startup process.
     * <p>
     * @param filename The filename of the system configuration file.
     ***************************************************************************************************/
    public void configure(String filename, boolean initLookAndFeel) {
        UILog.info(getClass(), UIMessageText.CONFIG_STARTING);
        try {
            loadConfigurationFile(filename);

            configureLocale();
            configureTextFieldLength();
            configurePermissions();
            configureRequired();
            configureBasicThemeSettings();
            if (initLookAndFeel) {
                configureLookAndFeelStyle();
            }
        } catch (Throwable exception) {
            UILog.error(getClass(), UIMessageText.CONFIG_ERROR, exception);
            System.exit(1);
        }
    }

    /****************************************************************************************************
     * Loads the configuration file into memory. If this fails, the program is exited with an error
     * message displayed to the console.
     * <p>
     * @param filename The filename of the system configuration file.
     ***************************************************************************************************/
    private void loadConfigurationFile(String filename) throws Exception {
        UILog.info(getClass(), UIMessageText.CONFIG_FILE_LOADING, filename);
        configuration = new RPropertyBundle(filename);
        UILog.info(getClass(), UIMessageText.CONFIG_FILE_LOADED);
    }

    /****************************************************************************************************
     * Configures the locale of the Swing application.
     ***************************************************************************************************/
    private void configureLocale() {
        TimeMaskUtility.installTimeParser(Locale.US, new USTimeParser());
    }

    /****************************************************************************************************
     * Configure field lengths. These values are used to determine the enterable text length of text
     * fields and text areas.
     ***************************************************************************************************/
    private void configureTextFieldLength() throws Exception {
        String filename = configuration.getString(CONFIG_TEXT_LENGTH_FILENAME);

        if (filename.equals(CONFIG_TEXT_LENGTH_FILENAME)) {
            filename = DEFAULT_TEXT_LENGTH_FILENAME;
        }
        TextLengthTranslator.setBundle(new RPropertyBundle(filename));
    }

    /****************************************************************************************************
     * Configure permissions. These values are used to determine the permissions on specific components
     * within the application.
     ***************************************************************************************************/
    private void configurePermissions() throws Exception {
        String filename = configuration.getString(CONFIG_PERMISSION_FILENAME);

        if (filename.equals(CONFIG_PERMISSION_FILENAME)) {
            filename = DEFAULT_PERMISSION_FILENAME;
        }
        PermissionTranslator.setBundle(new RPropertyBundle(filename));
    }

    /****************************************************************************************************
     * Configure required settings. These values are used to determine which editors are flagged as
     * required and which are not.
     ***************************************************************************************************/
    private void configureRequired() throws Exception {
        String filename = configuration.getString(CONFIG_REQUIRED_FILENAME);

        if (filename.equals(CONFIG_REQUIRED_FILENAME)) {
            filename = DEFAULT_REQUIRED_FILENAME;
        }
        PermissionTranslator.setBundle(new RPropertyBundle(filename));
    }

    /****************************************************************************************************
     * Configures the look and feel of the application along with the font and color scheme.
     ***************************************************************************************************/
    private void configureBasicThemeSettings() throws Exception {
        configureFocusPolicy();
        configureMarginTheme();
        configureIconTheme();
        configureWidgetTheme();

        UILog.info(getClass(), UIMessageText.MESSAGE_LOOK_AND_FEEL_COMPLETE);
    }

    /****************************************************************************************************
     * Configures the look and feel style of the application.
     ***************************************************************************************************/
    private void configureLookAndFeelStyle() throws Exception {
        CustomSwanLookAndFeel theme = new CustomSwanLookAndFeel();
        UILog.info(getClass(), UIMessageText.MESSAGE_LOOK_AND_FEEL_INIT, theme.getName());
        themeManager.setLookAndFeel(theme);
    }

    /****************************************************************************************************
     * Configures the look and feel plaf of the application.
     ***************************************************************************************************/
    private void configureFocusPolicy() {
        FocusTraversalPolicy focusPolicy = new OracleFocusPolicy();

        String filename = configuration.getString(CONFIG_FOCUS_POLICY);
        if (filename != null && !filename.equals(CONFIG_FOCUS_POLICY)) {
            try {
                UILog.info(getClass(), UIMessageText.INITIALIZING, filename);
                focusPolicy = (FocusTraversalPolicy) Class.forName(filename).newInstance();
            } catch (Throwable t) {
                UILog.error(getClass(), UIMessageText.FOCUS_POLICY_ERROR, t);
            }
        }
        KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalPolicy(focusPolicy);
    }

    /****************************************************************************************************
     * Configure the margin theme of the application.
     ***************************************************************************************************/
    private void configureMarginTheme() throws Exception {
        String filename = configuration.getString(CONFIG_MARGIN_THEME_FILENAME);
        if (CONFIG_MARGIN_THEME_FILENAME.equals(filename)) {
            filename = DEFAULT_MARGIN_THEME_FILENAME;
        }
        themeManager.setMarginTheme(filename);
    }

    /****************************************************************************************************
     * Configure the icon theme of the application.
     ***************************************************************************************************/
    private void configureIconTheme() throws Exception {
        String filename = configuration.getString(CONFIG_ICON_THEME_FILENAME);
        if (CONFIG_ICON_THEME_FILENAME.equals(filename)) {
            filename = DEFAULT_ICON_THEME_FILENAME;
        }
        themeManager.setIconTheme(filename);
    }

    /****************************************************************************************************
     * Configure the widget theme of the application.
     ***************************************************************************************************/
    private void configureWidgetTheme() throws Exception {
        String filename = configuration.getString(CONFIG_WIDGET_THEME_FILENAME);
        if (CONFIG_WIDGET_THEME_FILENAME.equals(filename)) {
            filename = DEFAULT_WIDGET_THEME_FILENAME;
        }
        themeManager.setWidgetTheme(filename);
    }

    /****************************************************************************************************
     * Uses the filename to create a property bundle to retrieve translations from. This method will
     * attempt to load the filename for a particular Locale in a sequence from most generic to most
     * specific. It loads the language, then the country file, then the variant file.
     * <p>
     * @param filename The filename of a properties file.
     * @param encoding The file encoding to use when reading the properties file.
     * @return The property bundle
     * @throws Exception Thrown if a file error occurs in the underlying bundle.
     ***************************************************************************************************/
    private RPropertyBundle retrieveLanguageBundle(String filename, String encoding) throws Exception {
        StringBuilder fileBuffer = new StringBuilder(filename);
        Locale locale = LocaleManager.getLanguageLocale();
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String variant = locale.getVariant();
        if (language.length() < 1) {
            return null;
        }
        fileBuffer.append(StringConstants.UNDERLINE);
        fileBuffer.append(language);
        RPropertyBundle bundle = new RPropertyBundle();
        bundle.loadPropertyBundle(fileBuffer.toString(), encoding);
        if (country.length() > 0) {
            fileBuffer.append(StringConstants.UNDERLINE);
            fileBuffer.append(country);
            try {
                bundle.loadPropertyBundle(fileBuffer.toString(), encoding);
            } catch (Exception exception) {
                UILog.error(getClass(), UIMessageText.LANGUAGE_NOT_LOADED, fileBuffer.toString(), exception);
            }
        }
        if (variant.length() > 0) {
            fileBuffer.append(StringConstants.UNDERLINE);
            fileBuffer.append(variant);
            try {
                bundle.loadPropertyBundle(fileBuffer.toString(), encoding);
            } catch (Exception exception) {
                UILog.error(getClass(), UIMessageText.VARIANT_NOT_LOADED, fileBuffer.toString(), exception);
            }
        }
        return bundle;
    }
}
