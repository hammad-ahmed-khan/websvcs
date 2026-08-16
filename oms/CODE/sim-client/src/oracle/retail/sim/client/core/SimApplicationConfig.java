package oracle.retail.sim.client.core;

import java.util.Properties;
import oracle.retail.sim.client.configutil.ClientCacheManager;
import oracle.retail.sim.client.swing.tableconfig.RTableConfigRepository;

/********************************************************************************************************
 * Wraps access to SIM application configuration information that is stored in sim.ini and
 * tableconfig.ini.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class SimApplicationConfig {

    private static final String TABLE_CONFIG = "table_config";
    private static final String WINDOW_PROPERTIES = "window_properties";
    private static final String LAST_USER = "last_user";
    private static final String LAST_THEME = "last_theme";

    /****************************************************************************************************
     * Private constructor for static class.
     ***************************************************************************************************/
    private SimApplicationConfig() {
    }

    /****************************************************************************************************
     * Sets the last user ID to access the system in the configuration information.
     ***************************************************************************************************/
    public static void cacheLastUserName(String userName) {
        ClientCacheManager.writeToClientCache(LAST_USER, userName, false);
    }

    /****************************************************************************************************
     * Retrieves the last user ID to access the system on the current machine.
     ***************************************************************************************************/
    public static String getLastUserNameFromCache() {
        return (String) ClientCacheManager.readFromClientCache(LAST_USER, false);
    }

    /****************************************************************************************************
     * Sets the last theme name used for the particular user.
     ***************************************************************************************************/
    public static void cacheLastThemeName(String themeName) {
        ClientCacheManager.writeToClientCache(LAST_THEME, themeName, true);
    }

    /****************************************************************************************************
     * Retrieves the last theme name for the particular user.
     ***************************************************************************************************/
    public static String getLastThemeNameFromCache() {
        return (String) ClientCacheManager.readFromClientCache(LAST_THEME, true);
    }

    /****************************************************************************************************
     * Loads the table configuration information.
     ***************************************************************************************************/
    public static void loadTableConfigurationFromCache() {
        RTableConfigRepository.loadRepositoryFromCache(TABLE_CONFIG);
    }

    /****************************************************************************************************
     * Saves the table configuration.
     ***************************************************************************************************/
    public static void cacheTableConfiguration() {
        RTableConfigRepository.cacheRepository(TABLE_CONFIG);
    }

    /****************************************************************************************************
     * Saves the window properties.
     ***************************************************************************************************/
    public static void cacheWindowProperties(Properties windowProperties) {
        ClientCacheManager.writeToClientCache(WINDOW_PROPERTIES, windowProperties, true);
    }

    public static Properties getWindowPropertiesFromCache() {
        return (Properties) ClientCacheManager.readFromClientCache(WINDOW_PROPERTIES, true);
    }
}
