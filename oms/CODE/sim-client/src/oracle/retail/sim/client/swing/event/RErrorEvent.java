package oracle.retail.sim.client.swing.event;

import java.util.EventObject;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.common.business.MessageText;

/******************************************************************************************
 * This class represents a generic RErrorEvent that has occurred in the client. It
 * contains an error code that represents the key into the messages properties file. Each
 * value assigned to the RErrorEvent represents a replacement value when using the
 * MessageFormat class. RErrorEvent defaults to a "warning" severity level upon creation.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class RErrorEvent extends EventObject {
    private static final long serialVersionUID = 8442976958956564461L;

    private RErrorSeverity severity = RErrorSeverity.WARNING;
    private MessageText message = null;

    private String[] valueArray = new String[3];
    private int valueIndex;
    private int resizeValue = 3;

    /******************************************************************************************
     * Returns new RErrorEvent object.
     * <p>
     *@param source The object that generated the event.
     *@param errorCode The error code to translate into an error message.
     ******************************************************************************************/
    public RErrorEvent(Object source, MessageText message) {
        super(source);
        setMessage(message);
    }

    /******************************************************************************************
     * Returns new RErrorEvent object. All error values should already be formatted to the
     * correct locale before being assigned to the error event.
     * <p>
     *@param source The object that generated the event.
     *@param errorCode The message to translate into an error message.
     *@param valueOne The first value to substitute in the error message.
     ******************************************************************************************/
    public RErrorEvent(Object source, MessageText message, String valueOne) {
        super(source);
        setMessage(message);
        addErrorValue(valueOne);
    }

    /******************************************************************************************
     * Creates new RErrorEvent object. All error values should already be formatted to the
     * correct locale before being assigned to the error event.
     * <p>
     *@param source The object that generated the event.
     *@param message The message to translate into an error message.
     *@param valueOne The first value to substitute in the error message.
     *@param valueTwo The second value to substitute in the error message.
     ******************************************************************************************/
    public RErrorEvent(Object source, MessageText message, String valueOne, String valueTwo) {
        super(source);
        setMessage(message);
        addErrorValue(valueOne);
        addErrorValue(valueTwo);
    }

    /******************************************************************************************
     * Sets the error code for the error event. This code is used for translation into a
     * localized message.
     * <p>
     *@param message The error code to set in the event.
     ******************************************************************************************/
    public void setMessage(MessageText message) {
        this.message = message;
    }

    /******************************************************************************************
     * Retrieves the untranslated error code for the error event.
     * <p>
     * @return The message of this event.
     ******************************************************************************************/
    public MessageText getMessage() {
        return message;
    }

    /******************************************************************************************
     * Adds an error value to the stored error values. These values are used to fill
     * placeholders when MessageFormat is used on the message.
     * <p>
     * @param value A single error value.
     ******************************************************************************************/
    public void addErrorValue(String value) {
        if (value != null) {
            if (valueIndex == valueArray.length) {
                String[] tempArray = new String[valueArray.length + resizeValue];

                System.arraycopy(valueArray, 0, tempArray, 0, valueArray.length);

                valueArray = tempArray;
            }
            valueArray[valueIndex] = value;
            valueIndex++;
        }
    }

    /******************************************************************************************
     * Retrieves all the error values for the event.
     * <p>
     * @param The array of all error values.
     ******************************************************************************************/
    public String[] getErrorValues() {
        return valueArray;
    }

    /******************************************************************************************
     * Retrieves a single error value stored in the RErrorEvent by index. This index begins
     * with one (1) and goes up to the number of values assigned to the error. If the index
     * is out of range, an empty string is returned.
     * <p>
     * @param The index of the error value assigned to the error.
     ******************************************************************************************/
    public String getErrorValue(int index) {
        if (index < 1 || index > valueArray.length) {
            return "";
        }
        return valueArray[index - 1];
    }

    /******************************************************************************************
     * Sets the severity level of the exception. The default value is ErrorSeverity.WARNING.
     * <p>
     * @param severity The severity level .
     ******************************************************************************************/
    public void setSeverity(RErrorSeverity severity) {
        this.severity = severity;
    }

    /******************************************************************************************
     * Retrieves the severity level of the exception.
     * <p>
     * @return The severity level.
     ******************************************************************************************/
    public RErrorSeverity getSeverity() {
        return severity;
    }

    /******************************************************************************************
     * A description of the object.
     ******************************************************************************************/
    public String toString() {
        StringBuilder buffer = new StringBuilder("RErrorEvent [");
        buffer.append("Message = ").append(getMessage());
        buffer.append("; Source = ").append(getSource());
        buffer.append("]");
        return buffer.toString();
    }
}
