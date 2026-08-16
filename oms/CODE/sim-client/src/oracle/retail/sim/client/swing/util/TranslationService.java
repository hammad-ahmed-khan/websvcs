package oracle.retail.sim.client.swing.util;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * This is a placeholder interface for the service that will be used to fetch translation maps from the
 * server.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public interface TranslationService {
    /**
     * Return a map of translations for the given locale.
     */
    Map getTranslationMap(Locale locale) throws Exception;

    /**
     * Return a map of translations for the given locale, but only if changes have been made since the
     * given timestamp.
     */
    Map getTranslationMap(Locale locale, Date timestamp) throws Exception;
}
