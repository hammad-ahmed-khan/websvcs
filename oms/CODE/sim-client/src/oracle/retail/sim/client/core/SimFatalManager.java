package oracle.retail.sim.client.core;

import java.util.Date;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.dialog.RSystemErrorDialog;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.util.WindowPlacer;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.SimServerException;

/**
 * Class handles fatal exceptions within SIM.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class SimFatalManager {

    /**
     * Logs the exception, displays the error window and then resets the client application.
     * @param classType The class that failed.
     * @param exception The exception;
     */
    public static void resetApplication(Class classType, Throwable exception) {
        UILog.fatal(classType, exception);
        resetApplication(exception);
    }

    /**
     * Displays the error window and then resets the client application.
     * @param exception The exception;
     */
    public static void resetApplication(Throwable exception) {
        StringBuilder errorMessage = new StringBuilder();
        if (exception instanceof SimServerException) {
            SimServerException sse = (SimServerException) exception;
            Object[] values = new String[2];
            values[0] = sse.getId();
            values[1] = LocaleManager.getShortDateTimeFormatter().format(new Date(sse.getTimestamp()));
            errorMessage.append(Translator.getMessage(CommonMessageText.ERROR_FATAL_DETAIL_MESSAGE.getText(), values));
        } else {
            errorMessage.append(Translator.getMessage(CommonMessageText.ERROR_FATAL_DEFAULT_MESSAGE.getText()));
        }
        errorMessage.append(" ");
        errorMessage.append(Translator.getMessage(CommonMessageText.ERROR_FATAL_CONTACT_MESSAGE.getText()));

        RSystemErrorDialog dialog = new RSystemErrorDialog(Application.getFrame(), exception, errorMessage.toString());
        WindowPlacer.centerOnWindow(Application.getFrame(), dialog);
        dialog.setVisible(true);
        Application.getNavigationManager().goHome();
    }
}
