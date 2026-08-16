package oracle.retail.sim.client.swing.logging;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.business.MessageText;

/*****************************************************************************************
 * This is the log object used to log all information in the client.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class UILog {

    private static Map loggerMap = new HashMap<>();
    private static UILoggerFactory loggerFactory;

    /*****************************************************************************************
     * Private constructor.
     *****************************************************************************************/
    private UILog() {
    }

    /*****************************************************************************************
     * Installs a UILoggerFactory that can create a new UILogger for the application.
     *****************************************************************************************/
    public static void installFactory(UILoggerFactory factory) {
        loggerFactory = factory;
    }

    /*****************************************************************************************
     * This logs a UIException object correctly depending on its severity.
     * <p>
     * @param source The class of the source object.
     * @param exception The UIException object to log.
     *****************************************************************************************/
    public static void log(Class source, UIException exception) {
        if (exception.isFatal()) {
            fatal(source, exception.getPrimaryMessageText(), exception);
        } else if (exception.isError()) {
            error(source, exception.getPrimaryMessageText(), exception);
        } else {
            info(source, exception.getPrimaryMessageText());
        }
    }

    /*****************************************************************************************
     * Returns true if debug is enabled for source.
     *****************************************************************************************/
    public static boolean isDebugEnabled(Class source) {
        UILogger logger = getLogger(source);
        if (logger == null) {
            return false;
        }
        return logger.isDebugEnabled();
    }

    /*****************************************************************************************
     * This logs a debug message for the object. The message will not be translated.
     * <p>
     * @param source The class of the source object.
     * @param message The message to log.
     *****************************************************************************************/
    public static void debug(Class source, MessageText message) {
        debug(source, message, null, null);
    }

    /*****************************************************************************************
     * This logs a debug message for the object. The message will not be translated.
     * <p>
     * @param source The class of the source object.
     * @param exception The exception that caused the problem.
     *****************************************************************************************/
    public static void debug(Class source, Throwable exception) {
        debug(source, null, null, exception);
    }

    /*****************************************************************************************
     * This logs a debug message for the object. The message will not be translated.
     * <p>
     * @param source The class of the source object.
     * @param message The message to log.
     * @param value A substitute value for the message
     * @param exception The exception that caused the problem.
     *****************************************************************************************/
    public static void debug(Class source, MessageText message, String value) {
        debug(source, message, value, null);
    }

    /*****************************************************************************************
     * This logs a debug message for the object. The message will not be translated.
     * <p>
     * @param source The class of the source object.
     * @param message The message to log.
     * @param exception The exception that caused the problem.
     *****************************************************************************************/
    public static void debug(Class source, MessageText message, Throwable exception) {
        debug(source, message, null, exception);
    }

    /*****************************************************************************************
     * This logs a debug message for the object. The message will not be translated.
     * <p>
     * @param source The class of the source object.
     * @param message The message to log.
     * @param value A substitute value for the message
     * @param exception The exception that caused the problem.
     *****************************************************************************************/
    public static void debug(Class source, MessageText message, String value, Throwable exception) {
        UILogger logger = getLogger(source);
        if (logger == null) {
            printToSystemOut(message, value, exception);
            return;
        }
        if (message != null) {
            if (value != null) {
                logger.debug(Translator.getMessage(message.getText(), value));
            } else {
                logger.debug(Translator.getMessage(message.getText()));
            }
        }
        if (exception != null) {
            logger.debug(getExceptionStackTrace(exception));
        }
    }

    /*****************************************************************************************
     * This logs an informational message for the object. The message will be translated.
     * <p>
     * @param source The class of the source object.
     * @param message The message to log.
     *****************************************************************************************/
    public static void info(Class source, MessageText message) {
        UILogger logger = getLogger(source);
        if (logger != null) {
            logger.info(Translator.getMessage(message.getText()));
            return;
        }
        System.out.println(Translator.getMessage(message.getText()));
    }

    /*****************************************************************************************
     * This logs an informational message for the object. The message will be translated.
     * <p>
     * @param source The class of the source object.
     * @param message The message to log.
     * @param value Value to substitute into the message
     *****************************************************************************************/
    public static void info(Class source, MessageText message, String value) {
        UILogger logger = getLogger(source);
        if (logger != null) {
            logger.info(Translator.getMessage(message.getText(), value));
            return;
        }
        System.out.println(Translator.getMessage(message.getText(), value));
    }

    /*****************************************************************************************
     * This logs an error message for the object. The message will be translated.
     * <p>
     * @param source The class of the source object.
     * @param message The message to log.
     *****************************************************************************************/
    public static void error(Class source, MessageText message) {
        error(source, message, null, null);
    }

    /*****************************************************************************************
     * This logs an error message for the object. The message will be translated.
     * <p>
     * @param source The class of the source object.
     * @param message The message to log.
     *****************************************************************************************/
    public static void error(Class source, Throwable exception) {
        error(source, null, null, exception);
    }

    /*****************************************************************************************
     * This logs an error message for the object. The message will be translated.
     * <p>
     * @param source The class of the source object.
     * @param message The message to log.
     * @param value A message substitute value.
     * @param exception The exception to log.
     *****************************************************************************************/
    public static void error(Class source, MessageText message, Throwable exception) {
        error(source, message, null, exception);
    }

    /*****************************************************************************************
     * This logs an error message for the object. The message will be translated.
     * <p>
     * @param source The class of the source object.
     * @param message The message to log.
     * @param value A message substitute value.
     *****************************************************************************************/
    public static void error(Class source, MessageText message, String value) {
        error(source, message, value, null);
    }

    /*****************************************************************************************
     * This logs an error message for the object. The message will be translated.
     * <p>
     * @param source The class of the source object.
     * @param message The message to log.
     * @param messageValue A message substitute value.
     * @param exception The exception to log.
     *****************************************************************************************/
    public static void error(Class source, MessageText message, String messageValue, Throwable exception) {
        UILogger logger = getLogger(source);
        if (logger == null) {
            printToSystemOut(message, messageValue, exception);
            return;
        }
        if (message != null) {
            if (messageValue != null) {
                logger.error(Translator.getMessage(message.getText(), messageValue));
            } else {
                logger.error(Translator.getMessage(message.getText()));
            }
        }
        if (exception != null) {
            logger.error(getExceptionStackTrace(exception));
        }
    }

    /*****************************************************************************************
     * This logs an fatal message for the object. The message will be translated.
     * <p>
     * @param source The class of the source object.
     * @param message The message to log.
     *****************************************************************************************/
    public static void fatal(Class source, MessageText message) {
        fatal(source, message, null);
    }

    /*****************************************************************************************
     * This logs an fatal message for the object. The message will be translated.
     * <p>
     * @param source The class of the source object.
     * @param exception The exception to log.
     *****************************************************************************************/
    public static void fatal(Class source, Throwable exception) {
        fatal(source, null, exception);
    }

    /*****************************************************************************************
     * This logs an fatal message for the object. The message will be translated.
     * <p>
     * @param source The class of the source object.
     * @param message The message to log.
     * @param exception The exception to log.
     *****************************************************************************************/
    public static void fatal(Class source, MessageText message, Throwable exception) {
        UILogger logger = getLogger(source);
        if (logger == null) {
            printToSystemOut(message, null, exception);
            return;
        }
        if (message != null) {
            logger.fatal(Translator.getMessage(message.getText()));
        }
        if (exception != null) {
            logger.fatal(getExceptionStackTrace(exception));
        }
    }

    /*****************************************************************************************
     * Helper method to print information to system out.
     *****************************************************************************************/
    private static void printToSystemOut(MessageText message, String value, Throwable exception) {
        if (message != null) {
            if (value != null) {
                System.out.println(Translator.getMessage(message.getText(), value));
            } else {
                System.out.println(Translator.getMessage(message.getText()));
            }
        }
        if (exception != null) {
            System.out.println(getExceptionStackTrace(exception));
        }
    }

    /*****************************************************************************************
     * Converts an exception stack trace into a displayable string.
     * <p>
     * @param exception The exception.
     * @return The displayable stack trace.
     *****************************************************************************************/
    private static String getExceptionStackTrace(Throwable exception) {
        StringBuilder displayText = new StringBuilder();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(stream);
        exception.printStackTrace(writer);
        writer.flush();
        displayText.append(stream.toString());
        writer.close();

        return displayText.toString();
    }

    /*****************************************************************************************
     * This helper message finds the appropriate logger for the source. If one does not exist,
     * it attempts to create one. Failing that, it returns null as a value.
     * <p>
     * @param source The class of the source object.
     * @return A UILogger for the source.
     *****************************************************************************************/
    private static UILogger getLogger(Class source) {
        UILogger logger = (UILogger) loggerMap.get(source);
        if (logger == null && loggerFactory != null) {
            logger = loggerFactory.buildLogger(source);
            loggerMap.put(source, logger);
        }
        return logger;
    }
}
