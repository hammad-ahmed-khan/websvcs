package oracle.retail.sim.client.swing.logging;

import javax.swing.JDialog;
import javax.swing.JFrame;
import oracle.retail.sim.client.swing.dialog.RErrorDialog;
import oracle.retail.sim.client.swing.dialog.RInfoDialog;
import oracle.retail.sim.client.swing.editor.RetailEditor;
import oracle.retail.sim.client.swing.event.RErrorEvent;
import oracle.retail.sim.common.business.MessageText;

/********************************************************************************************************
 * This is the static exception displayer utility that handles displaying all exceptions, errors and
 * messages in the framework. It uses a base status displayer by default, but allows the installation of
 * another displayer.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UIStatusUtility {

    private static UIStatusDisplayer displayer = new UIPlatformStatusDisplayer();

    /****************************************************************************************************
     * Private constructor.
     ***************************************************************************************************/
    private UIStatusUtility() {
    }

    /****************************************************************************************************
     * Install the status displayer.
     * <p>
     * @param statusDisplayer The status displayer to install.
     ***************************************************************************************************/
    public static void installDisplayer(UIStatusDisplayer statusDisplayer) {
        if (statusDisplayer != null) {
            displayer = statusDisplayer;
        }
    }

    /****************************************************************************************************
     * Displays an exception from the particular object within the application framework.
     * <p>
     * @param object The object associated with the source of the exception.
     * @param exception The exception.
     ***************************************************************************************************/
    public static void displayException(Object object, Throwable exception) {
        displayer.displayException(object, exception);
    }

    /****************************************************************************************************
     * Displays an exception from the particular object within the application framework.
     * <p>
     * @param object The object associated with the source of the exception.
     * @param message An additional message to place before the exception
     * @param exception The exception.
     ***************************************************************************************************/
    public static void displayException(Object object, MessageText message, Throwable exception) {
        displayer.displayException(object, exception, message);
    }

    /****************************************************************************************************
     * Displays a message from the particular object within the application framework.
     * <p>
     * @param object The object associated with the source of the exception.
     * @param message The message to display.
     ***************************************************************************************************/
    public static void displayException(Object object, MessageText message) {
        displayer.displayException(object, null, message);
    }

    /****************************************************************************************************
     * Displays an error event within the application framework.
     * <p>
     * @param object The object associated with the source of the exception.
     * @param event The error event.
     ***************************************************************************************************/
    public static void displayException(Object object, RErrorEvent event) {
        displayer.displayException(object, event);
    }

    /****************************************************************************************************
     * Displays an exception found within an editor.
     * <p>
     * @param editor The editor that is in error state.
     ***************************************************************************************************/
    public static void displayException(RetailEditor editor) {
        displayer.displayException(editor);
    }

    /****************************************************************************************************
     * Clears an exception because the editor has exited an error state.
     * <p>
     * @param editor The editor that is no longer in error state.
     ***************************************************************************************************/
    public static void clearException(RetailEditor editor) {
        displayer.clearException(editor);
    }

    /****************************************************************************************************
     * Clears an exception because the editor has exited an error state.
     * <p>
     * @param editor The editor that is no longer in error state.
     ***************************************************************************************************/
    public static void clearStatus() {
        displayer.clear();
    }

    /****************************************************************************************************
     * Displays a warning within the application framework.
     * <p>
     * @param object The object associated with the source of the exception.
     * @param message The message.
     ***************************************************************************************************/
    public static void displayWarning(Object object, MessageText message) {
        displayer.displayWarning(object, message);
    }
    
    /****************************************************************************************************
     * Displays a message from the particular object within the application framework.
     * <p>
     * @param object The object associated with the source of the message.
     * @param message The message.
     ***************************************************************************************************/
    public static void displayMessage(Object object, MessageText message) {
        displayer.displayMessage(object, message);
    }

    /****************************************************************************************************
     * Displays a message from the particular object within the application framework.
     * <p>
     * @param object The object associated with the source of the message.
     * @param message The message.
     * @param messageValue A value to substitute in the message
     ***************************************************************************************************/
    public static void displayMessage(Object object, MessageText message, String messageValue) {
        displayer.displayMessage(object, message, messageValue);
    }

    /****************************************************************************************************
     * Displays a search based message in the framework.
     * <p>
     * @param object The object associated with the source of the message.
     * @param message The message.
     ***************************************************************************************************/
    public static void displaySearchMessage(Object object, MessageText message) {
        displayer.displaySearchMessage(object, message);
    }

    /****************************************************************************************************
     * Displays an error dialog on the parent frame.
     * <p>
     * @param parentFrame The object that will own the error dialog.
     * @param title The title of the dialog.
     * @param message The error message.
     ***************************************************************************************************/
    public static void showErrorDialog(JFrame parentFrame, String title, MessageText message) {
        RErrorDialog dialog = new RErrorDialog(parentFrame);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.activate();
    }

    /****************************************************************************************************
     * Displays an error dialog on the parent frame.
     * <p>
     * @param parentFrame The object that will own the error dialog.
     * @param title The title of the dialog.
     * @param exception The exception to display.
     ***************************************************************************************************/
    public static void showErrorDialog(JFrame parentFrame, String title, Throwable exception) {
        RErrorDialog dialog = new RErrorDialog(parentFrame);
        dialog.setTitle(title);
        dialog.setMessage(exception);
        dialog.activate();
    }

    /****************************************************************************************************
     * Displays a error dialog on the parent frame.
     * <p>
     * @param parentDialog The object that will own the error dialog.
     * @param title The title of the dialog.
     * @param message The error message.
     ***************************************************************************************************/
    public static void showErrorDialog(JDialog parentDialog, String title, MessageText message) {
        RErrorDialog dialog = new RErrorDialog(parentDialog);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.activate();
    }

    /****************************************************************************************************
     * Displays an info dialog on the parent frame.
     * <p>
     * @param parentFrame The object that will own the information dialog.
     * @param title The title of the dialog.
     * @param message The informational message.
     ***************************************************************************************************/
    public static void showInfoDialog(JFrame parentFrame, String title, MessageText message) {
        RInfoDialog dialog = new RInfoDialog(parentFrame, title);
        dialog.displayMessage(message);
    }

    /****************************************************************************************************
     * Displays an info dialog on the parent frame.
     * <p>
     * @param parentDialog The object that will own the information dialog.
     * @param title The title of the dialog.
     * @param message The informational message.
     ***************************************************************************************************/
    public static void showInfoDialog(JDialog parentDialog, String title, MessageText message) {
        RInfoDialog dialog = new RInfoDialog(parentDialog, title);
        dialog.displayMessage(message);
    }

    /****************************************************************************************************
     * Displays a warning dialog on the parent frame.
     * <p>
     * @param parentFrame The object that will own the warning dialog.
     * @param title The title of the dialog.
     * @param message The warning message.
     ***************************************************************************************************/
    public static void showWarningDialog(JFrame parentFrame, String title, MessageText message) {
        RInfoDialog dialog = new RInfoDialog(parentFrame, title);
        dialog.displayWarning(message);
    }

    /****************************************************************************************************
     * Displays a warning dialog on the parent frame.
     * <p>
     * @param parentDialog The object that will own the warning dialog.
     * @param title The title of the dialog.
     * @param message The warning message.
     ***************************************************************************************************/
    public static void showWarningDialog(JDialog parentDialog, String title, MessageText message) {
        RInfoDialog dialog = new RInfoDialog(parentDialog, title);
        dialog.displayWarning(message);
    }
}
