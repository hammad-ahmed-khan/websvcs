package oracle.retail.sim.client.swing.util;

import oracle.retail.sim.common.business.MessageText;

/******************************************************************************************
 * This class represents a single problem stored within the UIException. UIProblems are
 * associated with identifiers. These identifiers may also used as the identity for a particular
 * editor. When the editor gains focus, the problem is can then be automatically displayed.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class UIProblem {

    private final MessageText messageText;
    private final Object[] messageValues;
    private final String identifier;
    private final Object data;

    /******************************************************************************************
     * Constructors
     ******************************************************************************************/
    public UIProblem(MessageText messageText) {
        this.messageText = messageText;
        this.messageValues = null;
        this.identifier = null;
        this.data = null;
    }

    public UIProblem(MessageText messageText, String messageValue) {
        this.messageText = messageText;
        this.messageValues = new Object[] { messageValue };
        this.identifier = null;
        this.data = null;
    }

    public UIProblem(MessageText messageText, Object[] messageValues) {
        this.messageText = messageText;
        this.messageValues = messageValues;
        this.identifier = null;
        this.data = null;
    }

    public UIProblem(String identifier, MessageText message) {
        this.messageText = message;
        this.messageValues = null;
        this.identifier = null;
        this.data = null;
    }

    public UIProblem(MessageText message, Object data) {
        this.messageText = message;
        this.messageValues = null;
        this.identifier = null;
        this.data = data;
    }

    /******************************************************************************************
     * Retrieves the identifier of the reason.
     * <p>
     * @return The source.
     ******************************************************************************************/
    public String getIdentifier() {
        return identifier;
    }

    /******************************************************************************************
     * Retrieves a data object associated with the problem.
     * <p>
     * @return The data object.
     ******************************************************************************************/
    public Object getData() {
        return data;
    }

    /******************************************************************************************
     * Retrieves the untranslated message and severity for this problem.
     ******************************************************************************************/
    public String toString() {
        if (messageText == null) {
            return "UIProblem: " + identifier;
        }
        return "UIProblem: " + identifier + " " + messageText.getText();
    }

    /******************************************************************************************
     * Retrieves the message text assigned to the error
     ******************************************************************************************/
    public MessageText getMessageText() {
        return messageText;
    }

    /******************************************************************************************
     * Retrieves the values that will be substituted in the message
     ******************************************************************************************/
    public Object[] getMessageValues() {
        return messageValues;
    }
}
