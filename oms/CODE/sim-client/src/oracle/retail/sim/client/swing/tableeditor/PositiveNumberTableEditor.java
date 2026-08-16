package oracle.retail.sim.client.swing.tableeditor;

/********************************************************************************************************
 * A table editor that edits only positive Number values. By setting the minimum value of an integer
 * table editor to 0, it will only allow positive number.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class PositiveNumberTableEditor extends NumberTableEditor {
    private static final long serialVersionUID = 3380899479548987528L;

    public PositiveNumberTableEditor() {
        setMinValue(0);
    }

    public PositiveNumberTableEditor(boolean isNullable) {
        super(isNullable);
        setMinValue(0);
    }

    public PositiveNumberTableEditor(boolean isNullable, double maxValue) {
        super(isNullable);
        setMinValue(0);
        setMaxValue(maxValue);
    }

    public PositiveNumberTableEditor(boolean isNullable, boolean isWholeNumber, double maxValue) {
        super(isNullable);
        setMinValue(0);
        setMaxValue(maxValue);
        if (isWholeNumber) {
            setMinimumFractionDigits(0);
            setMaximumFractionDigits(0);
        }
    }

    public PositiveNumberTableEditor(boolean isNullable, double minValue, double maxValue) {
        super(isNullable);
        setMinValue(minValue);
        setMaxValue(maxValue);
    }
}
