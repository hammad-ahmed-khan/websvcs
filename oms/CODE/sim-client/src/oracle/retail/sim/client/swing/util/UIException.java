package oracle.retail.sim.client.swing.util;

import java.util.Collections;
import java.util.List;
import oracle.retail.sim.common.business.MessageText;

/********************************************************************************************************
 * This class is a GUI representation of a Exception. It contains a complete list of problems within the
 * exception along with an identifier associated to each problem. The default error severity of a
 * UIException is ERROR.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UIException extends Exception {
    private static final long serialVersionUID = 6531624784945476974L;

    private static final String MESSAGE_NULL = "Message cannot be null!";

    private final RErrorSeverity errorSeverity;
    private final List<UIProblem> problems;

    /****************************************************************************************************
     * Creates a new UIException.
     * @param message A message text enum
     ***************************************************************************************************/
    public UIException(MessageText message) {
        if (message == null) {
            throw new IllegalArgumentException(MESSAGE_NULL);
        }
        problems = Collections.singletonList(new UIProblem(message));
        errorSeverity = RErrorSeverity.ERROR;
    }

    /****************************************************************************************************
     * Creates a new UIException.
     * @param message A message text enum
     * @param messageValue A string to substitute within the message
     ***************************************************************************************************/
    public UIException(MessageText message, String messageValue) {
        if (message == null) {
            throw new IllegalArgumentException(MESSAGE_NULL);
        }
        problems = Collections.singletonList(new UIProblem(message, messageValue));
        errorSeverity = RErrorSeverity.ERROR;
    }

    /****************************************************************************************************
     * Creates a new BusinessException.
     * @param message An message text enum
     * @param messageValues Values to substitute in the message
     ***************************************************************************************************/
    public UIException(MessageText message, Object[] messageValues) {
        if (message == null) {
            throw new IllegalArgumentException(MESSAGE_NULL);
        }
        problems = Collections.singletonList(new UIProblem(message, messageValues));
        errorSeverity = RErrorSeverity.ERROR;
    }

    /****************************************************************************************************
     * Creates a new UIException.
     * @param message A message text enum
     ***************************************************************************************************/
    public UIException(MessageText message, RErrorSeverity severity) {
        if (message == null) {
            throw new IllegalArgumentException(MESSAGE_NULL);
        }
        problems = Collections.singletonList(new UIProblem(message));
        errorSeverity = severity;
    }
    
    /****************************************************************************************************
     * Creates a new BusinessException.
     * @param message An message text enum
     * @param messageValue Value to substitute in the message
     ***************************************************************************************************/
    public UIException(MessageText message, String messageValue, RErrorSeverity severity) {
        if (message == null) {
            throw new IllegalArgumentException(MESSAGE_NULL);
        }
        problems = Collections.singletonList(new UIProblem(message, messageValue));
        errorSeverity = severity;
    }

    /****************************************************************************************************
     * Creates a new BusinessException.
     * @param message An message text enum
     * @param messageValues Values to substitute in the message
     ***************************************************************************************************/
    public UIException(MessageText message, Object[] messageValues, RErrorSeverity severity) {
        if (message == null) {
            throw new IllegalArgumentException(MESSAGE_NULL);
        }
        problems = Collections.singletonList(new UIProblem(message, messageValues));
        errorSeverity = severity;
    }

    /****************************************************************************************************
     * Creates a new BusinessException.
     * @param message An message text enum
     * @param messageValues Values to substitute in the message
     ***************************************************************************************************/
    public UIException(List<UIProblem> problems) {
        this.problems = problems;
        errorSeverity = RErrorSeverity.ERROR;
    }

    /****************************************************************************************************
     * Returns true if the exception is fatal, false if not.
     ***************************************************************************************************/
    public boolean isFatal() {
        return RErrorSeverity.FATAL == errorSeverity;
    }

    /****************************************************************************************************
     * Returns true if the exception is an error, false if not.
     ***************************************************************************************************/
    public boolean isError() {
        return RErrorSeverity.ERROR == errorSeverity;
    }

    /****************************************************************************************************
     * Returns true if the exception is a warning, false if not.
     ***************************************************************************************************/
    public boolean isWarning() {
        return RErrorSeverity.WARNING == errorSeverity;
    }

    /****************************************************************************************************
     * Returns true if the exception is informational, false if not.
     ***************************************************************************************************/
    public boolean isInformation() {
        return RErrorSeverity.INFO == errorSeverity;
    }

    /****************************************************************************************************
     * Retrieves the error severity of the exception (INFO, WARNING, ERROR, FATAL).
     * <p>
     * @return The error severity.
     ***************************************************************************************************/
    public RErrorSeverity getSeverity() {
        return errorSeverity;
    }

    /****************************************************************************************************
     * Overrides Throwable to return the text of the primary message. This method returns the base string
     * only without any translation or formatting.
     ***************************************************************************************************/
    public String getMessage() {
        return getPrimaryMessageText().getText();
    }

    /****************************************************************************************************
     * Overrides Throwable to return the text of the primary message. This method returns the base string
     * only without any translation or formatting.
     ***************************************************************************************************/
    public String getLocalizedMessage() {
        return getPrimaryMessageText().getText();
    }

    /****************************************************************************************************
     * Retrieves all the errors within the exception.
     * @return A List of BusinessError objects.
     ***************************************************************************************************/
    public List<UIProblem> getProblems() {
        return problems;
    }

    /****************************************************************************************************
     * Retrieves the primary (first in the list) error code.
     ***************************************************************************************************/
    public MessageText getPrimaryMessageText() {
        return problems.get(0).getMessageText();
    }

    /****************************************************************************************************
     * Retrieves the primary (first in the list) error values.
     ***************************************************************************************************/
    public Object[] getPrimaryMessageValues() {
        return problems.get(0).getMessageValues();
    }
}
