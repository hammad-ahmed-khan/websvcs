package oracle.retail.sim.client.screen.translation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.translation.TranslationServices;

/********************************************************************************************************
 * Translation Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TranslationDetailModel extends SimScreenModel {
    private Map<String, String> translationKeyMap;
    private Map<String, String> englishTranslationMap;
    private Map<String, String> languageTranslationMap;
    private Map<String, String> localeTranslationMap;
    private List<Locale> localeList;
    private Locale currentLocale;

    public void refreshLocales() throws Exception {
        localeList = ClientServiceFactory.getTranslationServices().findLocales();
    }

    public List<Locale> findLanguages() throws Exception {
        return localeList;
    }

    public boolean addTranslationKey(TranslationDetailWrapper wrapper) {
        if (translationKeyMap != null) {
            translationKeyMap.put(wrapper.getKey(), wrapper.getComment());
            englishTranslationMap.put(wrapper.getKey(), wrapper.getEnglish());
            localeTranslationMap.put(wrapper.getKey(), wrapper.getValue());
            return true;
        }
        return false;
    }

    public boolean updateTranslationKey(TranslationDetailWrapper wrapper) {
        if (translationKeyMap != null) {
            translationKeyMap.put(wrapper.getKey(), wrapper.getComment());
            localeTranslationMap.put(wrapper.getKey(), wrapper.getValue());
            return true;
        }
        return false;
    }

    public List<TranslationDetailWrapper> findLanguageDetails(Locale locale, String filterText) throws Exception {
        List<TranslationDetailWrapper> wrappers = new ArrayList<>();
        if (locale == null) {
            return wrappers;
        }
        TranslationServices translationServices = ClientServiceFactory.getTranslationServices();
        if (translationKeyMap == null) {
            translationKeyMap = translationServices.findTranslationKeys();
            englishTranslationMap = translationServices.findTranslations(Locale.ENGLISH).getTranslations();
        }
        if (!locale.equals(currentLocale)) {
            localeTranslationMap = translationServices.findAllTranslations(locale).getTranslations();

            if (locale.getCountry() == null) {
                languageTranslationMap = new HashMap<>();
            } else {
                Locale languageLocale = new Locale(locale.getLanguage());
                languageTranslationMap = translationServices.findAllTranslations(languageLocale).getTranslations();
            }
            currentLocale = locale;
        }

        if (StringUtility.isNullOrEmpty(filterText)) {
            filterText = null;
        } else {
            filterText = StringUtility.toUpperCase(filterText);
        }
        for (Map.Entry<String, String> entry : translationKeyMap.entrySet()) {
            String key = entry.getKey();
            String comment = entry.getValue();
            String english = englishTranslationMap.get(key);
            String value = localeTranslationMap.get(key);
            if (StringUtility.isNullOrEmpty(value)) {
                value = languageTranslationMap.get(key);
            }
            if (filterText != null) {
                if (StringUtility.indexOf(StringUtility.toUpperCase(value), filterText) < 0) {
                    continue;
                }
            }
            wrappers.add(ClientWrapperFactory.createTranslationDetailWrapper(key, english, value, comment));
        }
        return wrappers;
    }
}
