package oracle.retail.sim.client.swing.plaf.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import oracle.retail.sim.client.application.RPropertyBundle;
import oracle.retail.sim.client.application.ThemeUtility;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.theme.CustomTheme;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * This class retrieves translations for a given locale. It will cache translations on the client machine
 * and will fetch updated translations from the server.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CustomThemeManager {
    public static final String PACKAGE = CustomThemeManager.class.getPackage().getName() + ".";
    public static final String DEFAULT_THEME = "Oracle Swan";

    private static List<CustomTheme> activeThemes = new ArrayList<>();
    private static RPropertyBundle TEXT_BUNDLE;

    private CustomThemeManager() {
    }

    /****************************************************************************************************
     * Returns the default text resource bundle.
     ***************************************************************************************************/
    public static String getDefaultText(String key) {
        if (TEXT_BUNDLE == null) {
            try {
                TEXT_BUNDLE = new RPropertyBundle();
                TEXT_BUNDLE.loadPropertyBundle("DefaultText");
            } catch (Throwable exception) {
                UILog.error(CustomThemeManager.class, UIMessageText.UNABLE_TO_LOAD_LABELS, exception);
                TEXT_BUNDLE = null;
                return key;
            }
        }
        return TEXT_BUNDLE.getString(key);
    }

    /****************************************************************************************************
     * Converts a map into an object array where array[i] = key and array[i + 1] = value.
     * <p>
     * @param map The map to convert.
     * @return The array of objects in the form (key, value, key, value, key, etc...).
     ***************************************************************************************************/
    public static Object[] convertToArray(Map map) {
        Object[] defaults = new Object[0];
        if (map != null) {
            defaults = new Object[map.size() * 2];
            int i = 0;
            for (Object key : map.keySet()) {
                defaults[i++] = key;
                defaults[i++] = map.get(key);
            }
        }
        return defaults;
    }

    /****************************************************************************************************
     * Apply Theme Based On A Theme Name
     ***************************************************************************************************/
    public static void applyTheme(String themeName, Locale locale) {
        if (applyTheme(themeName)) {
            return;
        }
        if (locale == null) {
            applyTheme(DEFAULT_THEME);
        } else if (locale.getLanguage().equals(Locale.JAPANESE.getLanguage())) {
            applyTheme("Oracle Japanese");
        } else if (locale.getLanguage().equals(Locale.KOREAN.getLanguage())) {
            applyTheme("Oracle Korean");
        } else if (locale.getLanguage().equals(Locale.CHINESE.getLanguage())) {
            if (locale.getCountry().equals(Locale.TRADITIONAL_CHINESE.getCountry())) {
                applyTheme("Oracle Chinese Traditional");
            } else {
                applyTheme("Oracle Chinese Simplified");
            }
        } else {
            applyTheme(DEFAULT_THEME);
        }
    }

    /****************************************************************************************************
     * Apply theme.
     * @param activeThemes List of themes that can be used.
     * @param themeName The name of the theme desired.
     * @return True if the theme was applied, false otherwise.
     ***************************************************************************************************/
    private static boolean applyTheme(String themeName) {
        if (StringUtility.isNullOrEmpty(themeName)) {
            return false;
        }
        try {
            if (activeThemes.isEmpty()) {
                activeThemes = ClientServiceFactory.getCustomThemeServices().findActiveCustomThemes();
            }
            for (CustomTheme theme : activeThemes) {
                if (theme.getName().equals(themeName)) {
                    ThemeUtility.applyCustomTheme(theme);
                    return true;
                }
            }
        } catch (Throwable exception) {
            UILog.debug(CustomThemeManager.class, exception);
        }
        return false;
    }
}
