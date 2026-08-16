package oracle.retail.sim.client.swing.displayer;

import java.util.Locale;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Displays the language of the locale using the current user's locale.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class LocaleLanguageDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object object) {
        if (object instanceof Locale) {
            Locale locale = (Locale) object;
            String language = locale.getDisplayLanguage(LocaleManager.getLanguageLocale());
            String country = locale.getDisplayCountry(LocaleManager.getLanguageLocale());
            String variant = locale.getVariant();
            StringBuilder displayText = new StringBuilder(language);
            if (!StringUtility.isNullOrEmpty(country)) {
                displayText.append(" - ");
                displayText.append(country);
            }
            if (!StringUtility.isNullOrEmpty(variant)) {
                displayText.append(" - ");
                displayText.append(variant);
            }
            return displayText.toString();
        }
        return StringConstants.EMPTY;
    }
}
