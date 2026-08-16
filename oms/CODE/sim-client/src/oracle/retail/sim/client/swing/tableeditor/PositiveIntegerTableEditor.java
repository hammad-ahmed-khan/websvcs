package oracle.retail.sim.client.swing.tableeditor;

/********************************************************************************************************
 * A table editor that edits only positive Integer values. By setting the minimum value of an integer
 * table editor to 0, it will only allow positive number.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class PositiveIntegerTableEditor extends IntegerTableEditor {
    private static final long serialVersionUID = 3380899479548987528L;

    public PositiveIntegerTableEditor() {
        setMinValue(0);
    }

    public PositiveIntegerTableEditor(int maxValue) {
        super(0, maxValue);
    }

    public PositiveIntegerTableEditor(int minValue, int maxValue) {
        super(minValue, maxValue);
        if (minValue < 0) {
            setMinValue(0);
        }
    }
}
