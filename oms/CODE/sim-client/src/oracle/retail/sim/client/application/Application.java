package oracle.retail.sim.client.application;

import java.util.Date;
import oracle.retail.sim.client.swing.frame.RFrame;
import oracle.retail.sim.client.swing.navigation.SecurityInterface;
import oracle.retail.sim.client.swing.util.Repository;
import oracle.retail.sim.common.config.NavigationData;
import oracle.retail.sim.common.configutil.ConfigManager;

/********************************************************************************************************
 * APPLICATION REPOSITORY
 * <p>
 * Stores global repository information
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class Application {

    private static Repository repository = new Repository();

    private static final String APPLICATION_MANAGER = "ApplicationManager";
    private static final String BACKGROUND_FRAME = "BackgroundFrame";
    private static final String CONFIG_MANAGER = "ConfigurationManager";
    private static final String MENU_MANAGER = "MenuManager";
    private static final String NAVIGATION_DATA = "NavigationData";
    private static final String NAVIGATION_MANAGER = "NavigationManager";
    private static final String STARTUP_DISPLAYER = "StartupDisplayer";
    private static final String TIME_STARTED = "TimeStarted";

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/

    private Application() {
    }

    /****************************************************************************************************
     * Application Frame
     ***************************************************************************************************/

    public static void setApplicationFrame(SimplifiedApplication frame) {
        repository.put(APPLICATION_MANAGER, frame);
    }

    public static SimplifiedApplication getApplicationFrame() {
        return (SimplifiedApplication) repository.get(APPLICATION_MANAGER);
    }

    public static SecurityInterface getSecurityManager() {
        return getApplicationFrame().getSecurityManager();
    }

    /****************************************************************************************************
     * Background Frame
     ***************************************************************************************************/

    public static void setFrame(RFrame frame) {
        repository.put(BACKGROUND_FRAME, frame);
    }

    public static RFrame getFrame() {
        return (RFrame) repository.get(BACKGROUND_FRAME);
    }

    /****************************************************************************************************
     * Configuration Manager
     ***************************************************************************************************/

    public static void setConfigManager(ConfigManager manager) {
        repository.put(CONFIG_MANAGER, manager);
    }

    public static ConfigManager getConfigManager() {
        return (ConfigManager) repository.get(CONFIG_MANAGER);
    }

    /****************************************************************************************************
     * Navigation Manager
     ***************************************************************************************************/

    public static void setNavigationManager(NavigationManager manager) {
        repository.put(NAVIGATION_MANAGER, manager);
    }

    public static NavigationManager getNavigationManager() {
        return (NavigationManager) repository.get(NAVIGATION_MANAGER);
    }

    /****************************************************************************************************
     * Menu Manager
     ***************************************************************************************************/

    public static void setNavigationData(NavigationData data) {
        repository.put(NAVIGATION_DATA, data);
    }

    public static NavigationData getNavigationData() {
        return (NavigationData) repository.get(NAVIGATION_DATA);
    }

    /****************************************************************************************************
     * Menu Manager
     ***************************************************************************************************/

    public static void setMenuManager(MenuManager manager) {
        repository.put(MENU_MANAGER, manager);
    }

    public static MenuManager getMenuManager() {
        return (MenuManager) repository.get(MENU_MANAGER);
    }

    /****************************************************************************************************
     * Startup Displayer
     ***************************************************************************************************/

    public static void setStartupDisplayer(StartupDisplayer displayer) {
        repository.put(STARTUP_DISPLAYER, displayer);
    }

    public static StartupDisplayer getStartupDisplayer() {
        return (StartupDisplayer) repository.get(STARTUP_DISPLAYER);
    }

    /****************************************************************************************************
     * Time Started
     ***************************************************************************************************/

    public static void setTimeStarted(Date date) {
        repository.put(TIME_STARTED, date);
    }

    public static Date getTimeStarted() {
        return (Date) repository.get(TIME_STARTED);
    }

    /****************************************************************************************************
     * Adds an object to the repository to make it available for global access. Only one object may
     * exists per name. New objects added with the same name replace the old objects.
     * <p>
     * @param name The name to assign to the object in the repository.
     * @param object The object to place in the repository.
     ***************************************************************************************************/
    public static void put(String name, Object object) {
        repository.put(name, object);
    }

    /****************************************************************************************************
     * Returns an object in the repository for a given name.
     * <p>
     * @param name The name of the object to retrieve from the repository.
     * @return The object in the repository for the given name, or null if none exists.
     ***************************************************************************************************/
    public static Object get(String name) {
        return repository.get(name);
    }
}
