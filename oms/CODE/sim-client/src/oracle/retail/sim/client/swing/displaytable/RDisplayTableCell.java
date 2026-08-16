package oracle.retail.sim.client.swing.displaytable;

import java.awt.Color;
import oracle.retail.sim.client.swing.util.DataTypeConstants;

/******************************************************************************************
 * This class represents the data that can be stored in a table cell.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RDisplayTableCell {

    private Color foregroundColor = Color.BLACK;
    private Color backgroundColor = Color.WHITE;

    private Object data;
    private String value = "";
    private int type = DataTypeConstants.TEXT;
    private boolean isErrorState;

    /******************************************************************************************
     * Returns new RDisplayTableCell object.
     *****************************************************************************************/
    public RDisplayTableCell() {
    }

    /******************************************************************************************
     * Assigns a cell type to the cell.
     * <p>
     * @param type The cell type to assign to the cell.
     *****************************************************************************************/
    public void setType(int type) {
        if (type >= DataTypeConstants.TEXT && type <= DataTypeConstants.ICON) {
            this.type = type;
        }
    }

    /******************************************************************************************
     * Retrieves the cell type from the cell.
     * <p>
     * @return The cell type of the cell.
     *****************************************************************************************/
    public int getType() {
        return type;
    }

    /******************************************************************************************
     * Assigns a cell value to the cell.
     * <p>
     * @param value The cell value to assign to the cell.
     *****************************************************************************************/
    public void setValue(String value) {
        if (value != null) {
            this.value = value;
        }
    }

    /******************************************************************************************
     * Retrieves the cell value from the cell.
     * <p>
     * @return The cell value of the cell.
     *****************************************************************************************/
    public String getValue() {
        return value;
    }

    /******************************************************************************************
     * Assigns an object within the cell as stored (non-displayed data).
     * <p>
     * @param object The object to store.
     *****************************************************************************************/
    public void setData(Object object) {
        data = object;
    }

    /******************************************************************************************
     * Retrieves the secondary (non-displayed) data within the cell.
     * <p>
     * @return A data object or null if none has been assigned.
     *****************************************************************************************/
    public Object getData() {
        return data;
    }

    /******************************************************************************************
     * Assigns a foreground cell color to the cell.
     * <p>
     * @param color The color to assign to the foreground of the cell.
     *****************************************************************************************/
    public void setForeground(Color color) {
        if (color != null) {
            foregroundColor = color;
        }
    }

    /******************************************************************************************
     * Retrieves the foreground cell color from the cell.
     * <p>
     * @return The foreground color of the cell.
     *****************************************************************************************/
    public Color getForeground() {
        return foregroundColor;
    }

    /******************************************************************************************
     * Assigns a background cell color to the cell.
     * <p>
     * @param color The color to assign to the background of the cell.
     *****************************************************************************************/
    public void setBackground(Color color) {
        if (color != null) {
            backgroundColor = color;
        }
    }

    /******************************************************************************************
     * Retrieves the background cell color from the cell.
     * <p>
     * @return The background color of the cell.
     *****************************************************************************************/
    public Color getBackground() {
        return backgroundColor;
    }

    /******************************************************************************************
     * Sets the error state of this particular cell in the table.
     * <p>
     * @param isError True if the table cell is in error, false otherwise.
     *****************************************************************************************/
    public void setErrorState(boolean isError) {
        isErrorState = isError;
    }

    /******************************************************************************************
     * Retrieves whether or not this table cell is in error.
     * <p>
     * @return True if the table cell is in error, otherwise false.
     *****************************************************************************************/
    public boolean isErrorState() {
        return isErrorState;
    }

    /******************************************************************************************
     * Returns the value of this cell.
     * <p>
     * @return The value of the cell.
     *****************************************************************************************/
    public String toString() {
        return getValue();
    }
}
