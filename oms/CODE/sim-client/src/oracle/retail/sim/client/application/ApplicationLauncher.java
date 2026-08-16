package oracle.retail.sim.client.application;

import java.util.Arrays;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.locale.TranslatorManager;
import oracle.retail.sim.client.security.SimLoginManager;
import oracle.retail.sim.client.swing.core.ConfigurationManager;
import oracle.retail.sim.client.swing.dialog.RErrorDialog;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.frame.RFrame;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.logging.UILoggerFactory;
import oracle.retail.sim.client.swing.logging.UIStatusDisplayer;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.plaf.custom.CustomSwanLookAndFeel;
import oracle.retail.sim.client.swing.plaf.custom.CustomThemeManager;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.configutil.ConfigManager;
import oracle.retail.sim.common.configutil.SimConfigFiles;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.DeviceType;
import oracle.retail.sim.common.core.JvmLocation;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.translation.TranslationMap;
import oracle.retail.sim.common.util.InitializerUtility;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * APPLICATION LAUNCHER
 * <p>
 * This class launches an application.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ApplicationLauncher {
    public static final String STARTUP_DISPLAY = "STARTUP_DISPLAY";
    public static final String GUI_MAINFRAME = "GUI.MAIN_FRAME";
    public static final String GUI_LOGGERFACTORY = "GUI.LOGGER_FACTORY";
    public static final String GUI_STATUSDISPLAYER = "GUI.STATUS_DISPLAYER";
    public static final String INITIALIZERS = "INITIALIZERS";
    public static final String RESOURCE_FILE = "Application";
    public static final String DEFAULT_THEME = CustomSwanLookAndFeel.class.getName();

    /****************************************************************************************************
     * Main method that starts the launching of the application
     ***************************************************************************************************/
    public static void main(String[] args) {
        LogService.debug(ApplicationLauncher.class, "main args: " + Arrays.asList(args));
        try {
			//Enable WebLogic JSSE SSL
			System.setProperty("weblogic.security.SSL.enableJSSE", "true");
            JvmLocation.setClient();
            UIManager.setLookAndFeel(DEFAULT_THEME);
            ApplicationArguments.setApplicationArgs(args);
            new ApplicationLauncher().launch();
        } catch (Throwable t) {
            LogService.error(ApplicationLauncher.class, "problem launching app", t);
            if (Application.getStartupDisplayer() != null) {
                Application.getStartupDisplayer().toBack();
            }
            UILog.error(ApplicationLauncher.class, UIMessageText.FAILED_TO_START_APP, t);
            RErrorDialog dialog = new RErrorDialog(new JFrame());
            dialog.setMessage(t);
            dialog.activate();
            System.exit(1);
        }
    }

    /****************************************************************************************************
     * Primary method executed from main. It loads the configuration manager and background frame and
     * then delegates to all the different startup phases. The following methods must be executed in
     * exactly this order for everything to work!
     ***************************************************************************************************/
    private void launch() throws Exception {
        UniversalContext.setSingleSession(true);
        UniversalContext.startSession(SimLoginManager.DEFAULT_PC_USER_ID, DeviceType.PC);

        Application.setTimeStarted(SimDateUtil.getCurrentDate());
        Application.setConfigManager(new ConfigManager(SimConfigFiles.CLIENT_CONFIG));
        UILog.installFactory(Application.getConfigManager().getObject(GUI_LOGGERFACTORY, UILoggerFactory.class));

        loadTranslationFromCache();
        launchStartupDisplayer();
        logVersionInformation();
        launchInitializers();
        installApplicationSettings();

        clientLogin();

        try {
            SimConfigManager.reloadConfigMap();
        } catch (Throwable t) {
            throw new Exception("Could not connect to server!");
        }

        loadCustomTheme();
        launchNavigation();

        RFrame mainFrame = Application.getConfigManager().getObject(GUI_MAINFRAME, RFrame.class);
        ApplicationInternal.setFrame(mainFrame);
        Application.setFrame(mainFrame);
        Application.setApplicationFrame((SimplifiedApplication) mainFrame);

        launchApplicationFrame();
    }

    /****************************************************************************************************
     * Attempts to load translation from cache only
     ***************************************************************************************************/
    private void loadTranslationFromCache() {
        try {
            Locale locale = TranslatorManager.getLocaleFromCache();
            if (locale != null) {
                TranslationMap translationMap = TranslatorManager.loadTranslationMapFromCache(locale);
                Translator.setTranslationMap(translationMap.getTranslations());
            }
        } catch (Throwable e) {
            UILog.error(getClass(), UIMessageText.FAILED_TO_LOAD_LANGUAGE, e);
        }
    }

    /****************************************************************************************************
     * Launches startup displayer
     ***************************************************************************************************/
    private void launchStartupDisplayer() {
        try {
            StartupDisplayer startupDisplayer = Application.getConfigManager().getObject(STARTUP_DISPLAY, StartupDisplayer.class);
            Application.setStartupDisplayer(startupDisplayer);
            startupDisplayer.setProgressTarget(100);
            startupDisplayer.setStatus("Starting client...");
            startupDisplayer.show();
            startupDisplayer.setProgress(10);
        } catch (Throwable t) {
            UILog.error(getClass(), UIMessageText.FAILED_TO_START_APP, t);
        }
    }

    /****************************************************************************************************
     * Log version information
     ***************************************************************************************************/
    private void logVersionInformation() {
        for (Object objectKey : System.getProperties().keySet()) {
            String key = (String) objectKey;
            if (key.endsWith(".version")) {
                UILog.info(getClass(), UIMessageText.SYSTEM_VERSION, key + " = " + System.getProperty(key));
            }
        }
        Application.getStartupDisplayer().setProgress(20);
    }

    /****************************************************************************************************
     * Launches the loading of all initializers.
     ***************************************************************************************************/
    private void launchInitializers() {
        try {
            String initializers = Application.getConfigManager().getString(INITIALIZERS);
            InitializerUtility.executeInitializers(initializers);
            Application.getStartupDisplayer().setProgress(30);
        } catch (Throwable t) {
            if (Application.getStartupDisplayer() != null) {
                Application.getStartupDisplayer().toBack();
            }
            UIStatusUtility.showErrorDialog(Application.getFrame(), "Shutdown", t);
            System.exit(1);
        }
    }

    /****************************************************************************************************
     * Install application settings
     ***************************************************************************************************/
    private void installApplicationSettings() {
        Application.getStartupDisplayer().setProgress(40);
        UIStatusUtility.installDisplayer(Application.getConfigManager().getObject(GUI_STATUSDISPLAYER, UIStatusDisplayer.class));
        new ConfigurationManager().configure(RESOURCE_FILE, false);
        ToolTipManager.sharedInstance().setDismissDelay(10000);
        Application.getStartupDisplayer().setProgress(50);
    }

    /****************************************************************************************************
     * Client login
     ***************************************************************************************************/
    private void clientLogin() throws Exception {
        Application.getStartupDisplayer().setStatus("Connecting to server...");
        Application.getStartupDisplayer().setProgress(60);
        if (!SimLoginManager.login()) {
            System.exit(0);
        }
        Application.getStartupDisplayer().setProgress(70);
    }

    /****************************************************************************************************
     * Attempts to load custom theme
     ***************************************************************************************************/
    private void loadCustomTheme() {
        try {
            CustomThemeManager.applyTheme(null, TranslatorManager.getLocaleFromCache());
        } catch (Throwable e) {
            UILog.error(getClass(), UIMessageText.FAILED_TO_LOAD_THEME, e);
        }
    }

    /****************************************************************************************************
     * Launches the navigation manager
     ***************************************************************************************************/
    private void launchNavigation() throws Exception {
        Application.getStartupDisplayer().setStatus("Loading...");
        Application.setNavigationData(ClientServiceFactory.getConfigServices().getClientNavigationData());
        Application.setNavigationManager(new NavigationManager());
        Application.getStartupDisplayer().setProgress(80);
        Application.setMenuManager(new MenuManager());
        Application.getStartupDisplayer().setProgress(90);
        Application.getMenuManager().loadMenuManager();
        Application.getStartupDisplayer().setProgress(100);
    }

    /****************************************************************************************************
     * Launches the application frame.
     ***************************************************************************************************/
    private void launchApplicationFrame() {
        Application.getStartupDisplayer().hide();
        Application.setStartupDisplayer(null);
        Application.getNavigationManager().showDefaultScreen();
        Application.getApplicationFrame().initializeFocus();
    }
}
