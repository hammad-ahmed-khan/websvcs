package oracle.retail.sim.client.core;

import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.swing.dialog.RErrorDialog;
import oracle.retail.sim.client.swing.dialog.RInfoDialog;
import oracle.retail.sim.client.swing.editor.RetailEditor;
import oracle.retail.sim.client.swing.event.RErrorEvent;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.logging.UIStatusDisplayer;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.core.DowntimeException;

/********************************************************************************************************
 * Handles displaying exceptions, errors and messages for the SIM application.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimStatusDisplayer implements UIStatusDisplayer {

    private static final String WARNING = "Warning";
    private static final String INFO = "Informational";

    private RErrorDialog errorDialog;

    /****************************************************************************************************
     * Displays an exception. Pops up an error dialog with the message.
     * <p>
     * Note: This makes no effort to do anything "special" or fancy with BusinessException. Once the
     * application is actually working, have Functional define this with GUI standards and then
     * implement.
     * <p>
     *
     * @param object The object that is associated with the error.
     * @param exception The exception to display.
     ***************************************************************************************************/
    public void displayException(Object object, Throwable exception) {
        displayException(object, exception, null);
    }

    /****************************************************************************************************
     * Displays an exception. Pops up an error dialog with the message.
     * <p>
     * Note: This makes no effort to do anything "special" or fancy with BusinessException. Once the
     * application is actually working, have Functional define this with GUI standards and then
     * implement.
     * <p>
     * @param object The object that is associated with the error.
     * @param exception The exception to display.
     * @param message Only used to log additional information in the UILog
     ***************************************************************************************************/
    public void displayException(Object object, Throwable exception, MessageText message) {
        if (errorDialog == null) {
            if (message == null) {
                UILog.error(object.getClass(), exception);
            } else {
                UILog.error(object.getClass(), message, exception);
            }
            if (exception == null) {
                errorDialog = buildErrorDialog(object);
                errorDialog.setMessage(message);
                errorDialog.activate();
                errorDialog = null;
                return;
            }
            if (exception instanceof BusinessException) {
                BusinessException bException = (BusinessException) exception;
                errorDialog = buildErrorDialog(object);
                errorDialog.setMessage(bException);
                errorDialog.activate();
                errorDialog = null;
                return;
            }
            if (exception instanceof UIException) {
                UIException uException = (UIException) exception;
                if (!uException.isFatal()) {
                    errorDialog = buildErrorDialog(object);
                    errorDialog.setMessage(uException);
                    errorDialog.activate();
                    errorDialog = null;
                    return;
                }
            }
            if (exception instanceof DowntimeException) {
                DowntimeException downtimeException = (DowntimeException) exception;
                SimFatalManager.resetApplication(downtimeException.getCause());
                return;
            }
            SimFatalManager.resetApplication(getClass(), exception);
        }
    }

    /****************************************************************************************************
     * Displays an exception. Pops up an error dialog with the message.
     * <p>
     * @param object The object that is associated with the error.
     * @param event The error event to display.
     ***************************************************************************************************/
    public void displayException(Object object, RErrorEvent event) {
        UILog.error(object.getClass(), event.getMessage());
        if (errorDialog == null) {
            errorDialog = buildErrorDialog(object);
            errorDialog.setMessage(event.getMessage());
            errorDialog.activate();
            errorDialog = null;
        }
    }

    /****************************************************************************************************
     * Displays an editor exception. Not implemented for SIM.
     ***************************************************************************************************/
    public void displayException(RetailEditor editor) {
    }

    /****************************************************************************************************
     * Clears an editor exception. Not implemented for SIM.
     ***************************************************************************************************/
    public void clearException(RetailEditor editor) {
    }

    /****************************************************************************************************
     * Clears an editor exception. Not implemented for SIM.
     ***************************************************************************************************/
    public void clear() {
    }

    /****************************************************************************************************
     * Displays a warning. Pops up an warning dialog with the message.
     * <p>
     *
     * @param object The object that is associated with the warning.
     * @param message The message to display.
     ***************************************************************************************************/
    public void displayWarning(Object object, MessageText message) {
        UILog.info(object.getClass(), message);

        RInfoDialog dialog = buildInfoDialog(object, WARNING);
        dialog.displayWarning(message);
    }
    
    /****************************************************************************************************
     * Displays a message. Pops up an informational dialog with the message.
     * <p>
     * @param object The object that is associated with the message.
     * @param message The message to display.
     ***************************************************************************************************/
    public void displayMessage(Object object, MessageText message) {
        UILog.info(object.getClass(), message);

        RInfoDialog dialog = buildInfoDialog(object, INFO);
        dialog.displayMessage(message);
    }

    /****************************************************************************************************
     * Displays a message. Pops up an informational dialog with the message.
     * <p>
     * @param object The object that is associated with the message.
     * @param message The message to display.
     ***************************************************************************************************/
    public void displayMessage(Object object, MessageText message, String messageValue) {
        UILog.info(object.getClass(), message);

        RInfoDialog dialog = buildInfoDialog(object, INFO);
        dialog.displayMessage(message, messageValue);
    }

    /****************************************************************************************************
     * Displays a search message. Empty implementation for SIM.
     ***************************************************************************************************/
    public void displaySearchMessage(Object object, MessageText message) {
    }

    /****************************************************************************************************
     * Helper method to build an error dialog.
     ***************************************************************************************************/
    private RErrorDialog buildErrorDialog(Object object) {
        if (object instanceof JFrame) {
            return new RErrorDialog((JFrame) object);
        } else if (object instanceof JDialog) {
            return new RErrorDialog((JDialog) object);
        }
        if (object instanceof JComponent) {
            Container container = ((JComponent) object).getTopLevelAncestor();
            if (container instanceof JFrame) {
                return new RErrorDialog((JFrame) container);
            } else if (container instanceof JDialog) {
                return new RErrorDialog((JDialog) container);
            }
        }
        return new RErrorDialog(Application.getFrame());
    }

    /****************************************************************************************************
     * Helper method to build an informational dialog.
     ***************************************************************************************************/
    private RInfoDialog buildInfoDialog(Object object, String title) {
        if (object instanceof JFrame) {
            return new RInfoDialog((JFrame) object, title);
        } else if (object instanceof JDialog) {
            return new RInfoDialog((JDialog) object, title);
        }

        if (object instanceof JComponent) {
            Container container = ((JComponent) object).getTopLevelAncestor();

            if (container instanceof JFrame) {
                return new RInfoDialog((JFrame) container, title);
            } else if (container instanceof JDialog) {
                return new RInfoDialog((JDialog) container, title);
            }
        }
        return new RInfoDialog(Application.getFrame(), title);
    }
}
