package oracle.retail.sim.client.swing.table;

import javax.swing.JComponent;

/********************************************************************************************************
 * A table editor defines a widget that may be used to edit a data object within the cell of a table. The
 * class type of objects passed into this editor should be of the same class type as the getEditorClass()
 * method. Please note that table editor must be declared individually for each attribute on the table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public interface SimTableEditor {

    /****************************************************************************************************
     * Gets the class that this table editor knows how to handle. Currently, it is assumed that a table
     * editor can only handle one Class.
     ***************************************************************************************************/
    Class getValueClass();

    /****************************************************************************************************
     * Assigns the class that is expected back to the client of this table editor. Most table editors can
     * ignore this, since they will be typed statically at development time. This method is provided so
     * that the clients can let the table editor know what type of object they expect.
     ***************************************************************************************************/
    void setValueClass(Class valueClass);

    /****************************************************************************************************
     * Sets the model object which this table editor may use. Most editors can ignore this - it should
     * only be used when the editor needs to reference the model for whatever reason. In this case, the
     * model passed in is equivalent to the "object" represented by the row in the table.
     ***************************************************************************************************/
    void setModel(Object model);

    /****************************************************************************************************
     * Assigns the coordinates in the table. These are reset every single the time the cell is activated.
     * They can be used to reassign editing focus into the table.
     ***************************************************************************************************/
    void setCoordinates(int row, int column);

    /****************************************************************************************************
     * Gets the value held in this table editor. The actual object type should be the same as the return
     * value of getEditorClass().
     ***************************************************************************************************/
    Object getValue();

    /****************************************************************************************************
     * Sets the value of this table editor. The type of this should argument should be the same as the
     * return value from getValueClass().
     ***************************************************************************************************/
    void setValue(Object value);

    /****************************************************************************************************
     * Gets the main component in this table editor. This table editor should be used for things like
     * enabling/disabling, and adding listeners. Indeed, this method should almost NEVER be used within
     * regular development code.
     ***************************************************************************************************/
    JComponent getComponent();

    /****************************************************************************************************
     * Checks the value on the table editor. If the value has changed, this method should return true. If
     * nothing has changed, or the value is invalid, it should return false.
     ***************************************************************************************************/
    boolean checkValue();

    /****************************************************************************************************
     * Adds a TableEditorListener to the listener collection. This listener will get notified of
     * appropriate events on the Editor.
     ***************************************************************************************************/
    void addTableEditorListener(SimTableEditorListener listener);

    /****************************************************************************************************
     * Removes the specified listener from the listener list.
     ***************************************************************************************************/
    void removeTableEditorListener(SimTableEditorListener listener);
}
