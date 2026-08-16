package oracle.retail.sim.client.swing.event;

import java.awt.Component;
import java.util.EventObject;

/******************************************************************************************
 * This class represents a RActionEvent. It defines a means of knowing the event source,
 * an event number, an event command and associated event data.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class RActionEvent extends EventObject {
    private static final long serialVersionUID = 5258601881537805430L;

    private String eventCommand = "";
    private String eventText = "";
    private Object eventData = new Object();
    private Component focusComponent;
    private int eventNumber;

    /******************************************************************************************
     * Returns new RActionEvent object.
     * <p>
     *@param source The object that generated the event.
     *@param command An event command.
     ******************************************************************************************/
    public RActionEvent(Object source, String command) {
        super(source);
        setEventCommand(command);
    }

    /******************************************************************************************
     * Returns new RActionEvent object.
     * <p>
     *@param source The object that generated the event.
     *@param number An event identifier.
     ******************************************************************************************/
    public RActionEvent(Object source, int number) {
        super(source);
        setEventNumber(number);
    }

    /******************************************************************************************
     * Returns new RActionEvent object.
     * <p>
     *@param source The object that generated the event.
     *@param command An event command.
     *@param text A text string to associate with the event.
     ******************************************************************************************/
    public RActionEvent(Object source, String command, String text) {
        super(source);
        setEventCommand(command);
        setEventText(text);
    }

    /******************************************************************************************
     * Returns new RActionEvent object.
     * <p>
     *@param source The object that generated the event.
     *@param number An event number.
     *@param text A text string to associate with the event.
     ******************************************************************************************/
    public RActionEvent(Object source, int number, String text) {
        super(source);
        setEventNumber(number);
        setEventText(text);
    }

    /******************************************************************************************
     * Returns new RActionEvent object.
     * <p>
     *@param source The object that generated the event.
     *@param command An event command.
     *@param eventData An object containing data to be transmitted with the event.
     ******************************************************************************************/
    public RActionEvent(Object source, String command, Object eventData) {
        super(source);
        setEventCommand(command);
        setEventData(eventData);
    }

    /******************************************************************************************
     * Returns new RActionEvent object.
     * <p>
     *@param source The object that generated the event.
     *@param number An event number.
     *@param eventData An object containing data to be transmitted with the event.
     ******************************************************************************************/
    public RActionEvent(Object source, int number, Object eventData) {
        super(source);
        setEventNumber(number);
        setEventData(eventData);
    }

    /******************************************************************************************
     * Sets the event number.
     * <p>
     *@param number The event number.
     ******************************************************************************************/
    public void setEventNumber(int number) {
        eventNumber = number;
    }

    /******************************************************************************************
     * Retrieves the event number.
     * <p>
     *@return The event number.
     ******************************************************************************************/
    public int getEventNumber() {
        return eventNumber;
    }

    /******************************************************************************************
     * Sets the event command.
     * <p>
     *@param eventCommand The event command.
     ******************************************************************************************/
    public void setEventCommand(String command) {
        eventCommand = command;
    }

    /******************************************************************************************
     * Retrieves the event command.
     * <p>
     *@return The event command.
     ******************************************************************************************/
    public String getEventCommand() {
        return eventCommand;
    }

    /******************************************************************************************
     * Returns whether or not this event matches an event number.
     * <p>
     *@param number An event number.
     *@return True if the event matches the event number, false if it does not.
     ******************************************************************************************/
    public boolean isEvent(int number) {
        return eventNumber == number;
    }

    /******************************************************************************************
     * Returns whether or not this event matches an event command.
     * <p>
     *@param command An event command.
     *@return True if the event matches the event command, false if it does not.
     ******************************************************************************************/
    public boolean isEvent(String command) {
        if (command != null) {
            return eventCommand.equals(command);
        }
        return false;
    }

    /******************************************************************************************
     * Sets the event text. This is used to store a text string associated to the event.
     * <p>
     *@param text A text string to assign to the event.
     ******************************************************************************************/
    public void setEventText(String text) {
        eventText = text;
    }

    /******************************************************************************************
     * Retrieves the event text.
     * <p>
     *@return The event text.
     ******************************************************************************************/
    public String getEventText() {
        return eventText;
    }

    /******************************************************************************************
     * Sets the event data object.
     * <p>
     *@param data The event data object.
     ******************************************************************************************/
    public void setEventData(Object data) {
        eventData = data;
    }

    /******************************************************************************************
     * Retrieves the event data object.
     * <p>
     *@return The event data object.
     ******************************************************************************************/
    public Object getEventData() {
        return eventData;
    }

    /******************************************************************************************
     * Sets the component to receive focus next. This will only be set by the framework when
     * an RActionEvent is trigged by loosing focus. It will set this value the opposite-focus
     * component from the focus event.
     * <p>
     *@param component A component.
     ******************************************************************************************/
    public void setNextFocusComponent(Component component) {
        focusComponent = component;
    }

    /******************************************************************************************
     * Retrieves the component that will receive focus next. If the event was trigged by a
     * focus lost event, this will return the component that will be gaining focus next.
     * <p>
     *@return A component
     ******************************************************************************************/
    public Component getNextFocusComponent() {
        return focusComponent;
    }

    /******************************************************************************************
     * A description of the object.
     ******************************************************************************************/
    public String toString() {
        StringBuilder buffer = new StringBuilder("RActionEvent [");
        buffer.append("Command = ").append(getEventCommand());
        buffer.append("; Number = ").append(getEventNumber());
        buffer.append("; Text = ").append(getEventText());
        buffer.append("; Data = ").append(getEventData());
        buffer.append("; Source = ").append(getSource());
        buffer.append("]");
        return buffer.toString();
    }
}
