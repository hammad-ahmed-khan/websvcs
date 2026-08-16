package oracle.retail.sim.client.locale;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import oracle.retail.sim.client.configutil.ClientCacheManager;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.translation.TranslationMap;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * This class retrieves translations for a given locale. It will cache translations on the client machine
 * and will fetch updated translations from the server.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TranslatorManager {
    private static final String TRANSLATIONS = "translations";
    private static final String LAST_LOCALE = "last_locale";

    /****************************************************************************************************
     * Reads the translations for the given locale. If the server call fails, the failure is logged and
     * an empty map is returned.
     * <p>
     * @param locale The locale object to fetch translations for
     ***************************************************************************************************/
    public Map<String, String> readTranslationMap(Locale locale) {
        try {
            TranslationMap tm = loadTranslationMapFromCache(locale);
            if (tm == null || tm.getTranslations().isEmpty()) {
                tm = ClientServiceFactory.getTranslationServices().findTranslations(locale);
                cacheLocale(locale);
                cacheTranslationMap(tm, locale);
            } else {
                TranslationMap updates = ClientServiceFactory.getTranslationServices().findUpdatedTranslations(locale, tm.getTimestamp());
                tm.addAll(updates);
                cacheLocale(locale);
                cacheTranslationMap(tm, locale);
            }
            return tm.getTranslations();
        } catch (Exception e) {
            LogService.error(TranslatorManager.class, "Couldn't get translations from the server", e);
        }
        return new HashMap<>();
    }

    /****************************************************************************************************
     * Constructs the correct object filename for a particular local.
     ***************************************************************************************************/
    private static String buildFilename(Locale locale) {
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String variant = locale.getVariant();
        StringBuilder filename = new StringBuilder(TRANSLATIONS);
        filename.append("_").append("_").append(language);
        if (!StringHelper.isNullOrEmpty(country)) {
            filename.append("_").append(country);
        }
        if (!StringHelper.isNullOrEmpty(variant)) {
            filename.append("_").append(variant);
        }
        return filename.toString();
    }

    /****************************************************************************************************
     * Helper method to write translations to a local cache (by locale).
     ***************************************************************************************************/
    private void cacheTranslationMap(TranslationMap translationMap, Locale locale) {
        ClientCacheManager.writeToClientCache(buildFilename(locale), translationMap, false);
    }

    /****************************************************************************************************
     * Retrieves a translation map from the local cache based on the locale passed in.
     * <p>
     * @param locale The locale object to retrieve language translations for.
     ***************************************************************************************************/
    public static TranslationMap loadTranslationMapFromCache(Locale locale) {
        return (TranslationMap) ClientCacheManager.readFromClientCache(buildFilename(locale), false);
    }

    /****************************************************************************************************
     * Writes a locale object to client cache.
     * <p>
     * @param locale The locale object to store on the locale system.
     ***************************************************************************************************/
    public static void cacheLocale(Locale locale) {
        ClientCacheManager.writeToClientCache(LAST_LOCALE, locale, false);
    }

    /****************************************************************************************************
     * Reads the last locale stored in client cache.
     * <p>
     * @return locale The locale object last stored on the local system.
     ***************************************************************************************************/
    public static Locale getLocaleFromCache() {
        return (Locale) ClientCacheManager.readFromClientCache(LAST_LOCALE, false);
    }
}
