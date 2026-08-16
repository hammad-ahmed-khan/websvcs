package oracle.retail.sim.client.swing.logging;

import java.awt.Container;
import java.awt.Window;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.dialog.RSystemErrorDialog;
import oracle.retail.sim.client.swing.editor.RetailEditor;
import oracle.retail.sim.client.swing.event.RErrorEvent;
import oracle.retail.sim.client.swing.frame.RPlatformApplicationFrame;
import oracle.retail.sim.client.swing.frame.RStatusBar;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.business.MessageText;

/********************************************************************************************************
 * Handles displaying exceptions, errors and messages for the Application.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UIPlatformStatusDisplayer implements UIStatusDisplayer {

    /****************************************************************************************************
     * Display an exception in the application.
     ***************************************************************************************************/
    public void displayException(Object object, Throwable exception) {
        displayException(object, exception, null);
    }

    /****************************************************************************************************
     * Display an exception in the application.
     ***************************************************************************************************/
    public void displayException(Object object, Throwable exception, MessageText message) {
        Window window = findWindow(object);
        RStatusBar statusBar = null;

        if (window instanceof RPlatformApplicationFrame) {
            statusBar = ((RPlatformApplicationFrame) window).getStatusBar();
        } else if (window instanceof RDialog) {
            statusBar = ((RDialog) window).getStatusBar();
        }

        if (statusBar == null) {
            return;
        }

        if (exception instanceof UIException) {
            UIException uiex = (UIException) exception;

            if (!uiex.isFatal()) {
                statusBar.displayException(uiex);
                UILog.error(object.getClass(), uiex);
                return;
            }
        }
        SwingUtilities.invokeLater(new FatalWindowCommand(window, object.getClass(), exception, message));
    }

    /****************************************************************************************************
     * Display an error event in the application.
     ***************************************************************************************************/
    public void displayException(Object object, RErrorEvent event) {
        displayException(object, new UIException(event.getMessage(), event.getErrorValues(), event.getSeverity()));
    }

    /****************************************************************************************************
     * Displays an editor's error message on the status bar.
     ***************************************************************************************************/
    public void displayException(RetailEditor editor) {
        RStatusBar statusBar = findStatusBar(editor);
        if (statusBar != null) {
            String[] messageValues = new String[] { editor.getErrorMessage() };
            statusBar.displayStatus(UIMessageText.EDITOR_ERROR, messageValues, RErrorSeverity.ERROR);
        }
    }

    /****************************************************************************************************
     * Displays a warning in the application.
     ***************************************************************************************************/
    public void displayWarning(Object object, MessageText message) {
        RStatusBar statusBar = findStatusBar(object);
        if (statusBar != null) {
            statusBar.displayStatus(message, RErrorSeverity.WARNING);
        }
        UILog.info(object.getClass(), message);
    }

    /****************************************************************************************************
     * Displays a message in the application.
     ***************************************************************************************************/
    public void displayMessage(Object object, MessageText message) {
        RStatusBar statusBar = findStatusBar(object);
        if (statusBar != null) {
            statusBar.displayStatus(message, RErrorSeverity.INFO);
        }
        UILog.info(object.getClass(), message);
    }

    /****************************************************************************************************
     * Displays a message in the application.
     ***************************************************************************************************/
    public void displayMessage(Object object, MessageText message, String messageValue) {
        RStatusBar statusBar = findStatusBar(object);
        if (statusBar != null) {
            String[] messageValues = new String[] { messageValue };
            statusBar.displayStatus(message, messageValues, RErrorSeverity.INFO);
        }
        UILog.info(object.getClass(), message);
    }

    /****************************************************************************************************
     * Displays a search message in the application.
     ***************************************************************************************************/
    public void displaySearchMessage(Object object, MessageText message) {
        RStatusBar statusBar = findStatusBar(object);
        if (statusBar != null) {
            statusBar.displaySearchStatus(message, RErrorSeverity.INFO);
        }
    }

    /****************************************************************************************************
     * Displays an empty status message to clear the retail editor problem display.
     ***************************************************************************************************/
    public void clearException(RetailEditor editor) {
        RStatusBar statusBar = findStatusBar(editor);
        if (statusBar != null) {
            statusBar.displayStatus(null, RErrorSeverity.WARNING);
        }
    }

    /****************************************************************************************************
     * Displays an empty status message to clear the retail editor problem display.
     ***************************************************************************************************/
    public void clear() {
        RStatusBar statusBar = findStatusBar(null);
        if (statusBar != null) {
            statusBar.clear();
        }
    }

    /****************************************************************************************************
     * Helper method to find the status bar.
     ***************************************************************************************************/
    private RStatusBar findStatusBar(Object object) {
        Window window = findWindow(object);

        if (window instanceof RPlatformApplicationFrame) {
            return ((RPlatformApplicationFrame) window).getStatusBar();
        } else if (window instanceof RDialog) {
            return ((RDialog) window).getStatusBar();
        }
        return null;
    }

    /****************************************************************************************************
     * Helper method to find the status bar.
     ***************************************************************************************************/
    private RStatusBar findStatusBar(RetailEditor editor) {
        if (editor != null) {
            Window window = findWindow(editor);

            if (window instanceof RPlatformApplicationFrame) {
                return ((RPlatformApplicationFrame) window).getStatusBar();
            } else if (window instanceof RDialog) {
                return ((RDialog) window).getStatusBar();
            }
        }
        if (Application.getFrame() instanceof RPlatformApplicationFrame) {
            return ((RPlatformApplicationFrame) Application.getFrame()).getStatusBar();
        }
        return null;
    }

    /****************************************************************************************************
     * Helper method to determine what window belongs to a component.
     ***************************************************************************************************/
    private Window findWindow(Object object) {
        if (object instanceof RPlatformApplicationFrame) {
            return (Window) object;
        } else if (object instanceof RDialog) {
            return (Window) object;
        }
        if (object instanceof JComponent) {
            Container container = ((JComponent) object).getTopLevelAncestor();

            if (container instanceof RPlatformApplicationFrame) {
                return (Window) container;
            } else if (container instanceof RDialog) {
                return (Window) container;
            }
        }
        return new JFrame();
    }

    /****************************************************************************************************
     * Helper method to determine what window belongs to an editor.
     ***************************************************************************************************/
    private Window findWindow(RetailEditor editor) {
        if (editor != null) {
            Container container = editor.getLabel().getTopLevelAncestor();

            if (container instanceof RPlatformApplicationFrame) {
                return (Window) container;
            } else if (container instanceof RDialog) {
                return (Window) container;
            }
        }
        return Application.getFrame();
    }

    /****************************************************************************************************
     *
     * INNER CLASS - FATAL WINDOW LAUNCH
     *
     ***************************************************************************************************/
    private class FatalWindowCommand implements Runnable {

        private Window fatalWindowParent;
        private Throwable fatalException;
        private Class fatalSource;
        private MessageText fatalMessage;

        public FatalWindowCommand(Window window, Class source, Throwable exception) {
            fatalWindowParent = window;
            fatalSource = source;
            fatalException = exception;
            fatalMessage = null;
        }

        public FatalWindowCommand(Window window, Class source, Throwable exception, MessageText message) {
            fatalWindowParent = window;
            fatalSource = source;
            fatalException = exception;
            fatalMessage = message;
        }

        public void run() {
            UILog.fatal(fatalSource, fatalMessage, fatalException);
            if (fatalWindowParent instanceof JDialog) {
                RSystemErrorDialog dialog = new RSystemErrorDialog((JDialog) fatalWindowParent, fatalException, fatalMessage);
                dialog.setVisible(true);
            } else if (fatalWindowParent instanceof JFrame) {
                RSystemErrorDialog dialog = new RSystemErrorDialog((JFrame) fatalWindowParent, fatalException, fatalMessage);
                dialog.setVisible(true);
            }
            SwingUtilities.invokeLater(new RecoverCommand(fatalWindowParent));
        }
    }

    /****************************************************************************************************
     *
     * INNER CLASS - RECOVER COMMAND
     *
     ***************************************************************************************************/
    private class RecoverCommand implements Runnable {

        private Window recoverWindow;

        public RecoverCommand(Window window) {
            recoverWindow = window;
        }

        public void run() {
            if (recoverWindow instanceof RPlatformApplicationFrame) {
                ((RPlatformApplicationFrame) recoverWindow).recover();
            } else if (recoverWindow instanceof RDialog) {
                ((RDialog) recoverWindow).recover();
            }
        }
    }
}
