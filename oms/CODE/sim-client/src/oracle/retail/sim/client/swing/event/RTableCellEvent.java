package oracle.retail.sim.client.swing.event;

import java.util.EventObject;
import oracle.retail.sim.common.core.locale.StringConstants;

/******************************************************************************************
 * This class represents a table cell event that took place. It contains a command, a
 * row, a column name, and the data object in the cell (or row if cell is null).
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class RTableCellEvent extends EventObject {
    private static final long serialVersionUID = -5223293117333476259L;

    private String eventCommand = StringConstants.EMPTY;
    private String eventColumnName = StringConstants.EMPTY;
    private int eventRowNumber = -1;
    private Object eventData;

    /******************************************************************************************
     * Returns new RTableCellEvent object.
     * <p>
     *@param source The object that generated the event.
     *@param command An event command.
     ******************************************************************************************/
    public RTableCellEvent(Object source, String command, String column, int row, Object data) {
        super(source);
        setEventCommand(command);
        setEventColumn(column);
        setEventRow(row);
        setEventData(data);
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
     * Sets the event column.
     * <p>
     *@param columnName The column header of the cell event.
     ******************************************************************************************/
    public void setEventColumn(String columnName) {
        eventColumnName = columnName;
    }

    /******************************************************************************************
     * Retrieves the event column header.
     * <p>
     *@return The event column header.
     ******************************************************************************************/
    public String getEventColumn() {
        return eventColumnName;
    }

    /******************************************************************************************
     * Sets the event row.
     * <p>
     *@param row The event row.
     ******************************************************************************************/
    public void setEventRow(int row) {
        eventRowNumber = row;
    }

    /******************************************************************************************
     * Retrieves the event row.
     * <p>
     *@return The event row.
     ******************************************************************************************/
    public int getEventRow() {
        return eventRowNumber;
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
     * A description of the object.
     ******************************************************************************************/
    public String toString() {
        StringBuilder buffer = new StringBuilder("RTableCellEvent [");
        buffer.append("Command = ").append(getEventCommand());
        buffer.append("; Column = ").append(getEventColumn());
        buffer.append("; Row = ").append(getEventRow());
        buffer.append("; Data = ").append(getEventData());
        buffer.append("; Source = ").append(getSource());
        buffer.append("]");
        return buffer.toString();
    }
}
