package oracle.retail.sim.client.util;

import java.net.URL;
import java.text.MessageFormat;
import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;

/********************************************************************************************************
 * Help Launcher
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class HelpLauncher {
    public static final String HELP_EXECUTABLE = "HELP_EXECUTABLE";

    private HelpLauncher() {
    }

    /****************************************************************************************************
     * Launch the help browser.
     ***************************************************************************************************/
    public static void showHelp() {
        String helpUrlString = null;
        URL url;
        try {
            helpUrlString = SimConfigManager.getString(SimConfigManager.ONLINE_HELP_URL);
            url = new URL(helpUrlString);
        } catch (Throwable exception) {
            UILog.error(HelpLauncher.class, UIMessageText.COULD_NOT_CREATE_HELP_URL, helpUrlString, exception);
            UIStatusUtility.displayException(HelpLauncher.class, new BusinessException(CommonMessageText.HELP_URL_ERROR));
            return;
        }

        try {
            // Use the javax.jnlp.BasicService object (works only if client started via Web Start)
            BasicService service = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
            service.showDocument(url);
        } catch (UnavailableServiceException jex) {
            // Must not have started with Web Start...try the command line
            tryThroughCommandLine(helpUrlString);
        } catch (Throwable exception) {
            UIStatusUtility.displayException(HelpLauncher.class, exception);
        }
    }

    private static void tryThroughCommandLine(String helpURL) {
        String helpExecutable = Application.getConfigManager().getString(HELP_EXECUTABLE);
        try {
            Runtime.getRuntime().exec(MessageFormat.format(helpExecutable, helpURL));
        } catch (Throwable exception) {
            UIStatusUtility.displayException(HelpLauncher.class, exception);
        }
    }
}
