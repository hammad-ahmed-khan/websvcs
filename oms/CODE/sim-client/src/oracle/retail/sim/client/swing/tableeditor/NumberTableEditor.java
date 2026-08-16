package oracle.retail.sim.client.swing.tableeditor;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.NumberFormat;
import javax.swing.JComponent;
import javax.swing.JTextField;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.editor.ValidKeystrokeUtility;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.logging.LogService;

/********************************************************************************************************
 * A table editor that edits all style of numbers. Minimum and maximum decimal places may be assigned for
 * formatting. Please note that table editor must be declared individually for each attribute on the
 * table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class NumberTableEditor extends JTextField implements SimTableEditor {
    private static final long serialVersionUID = 130049090206699928L;

    private Class classType;
    private SimTableEditorEventAdaptor eventAdaptor;
    private Number value;
    private Object model;
    private int minimumFractionDigits;
    private int maximumFractionDigits = ValidKeystrokeUtility.DECIM_PLACE_SIZE;
    private BigDecimal minValue = BigDecimal.valueOf(Double.MIN_VALUE);
    private BigDecimal maxValue = BigDecimal.valueOf(Double.MAX_VALUE);
    private boolean nullable;

    private int row = -1;
    private int column = -1;

    /****************************************************************************************************
     * Constructors
     ***************************************************************************************************/

    public NumberTableEditor() {
        this(0);
    }

    public NumberTableEditor(boolean isNullable) {
        this(0);
        setNullable(isNullable);
    }

    public NumberTableEditor(int minimumFractionDigits, int maximumFractionDigits) {
        this(minimumFractionDigits);
        setMaximumFractionDigits(maximumFractionDigits);
    }

    public NumberTableEditor(int minimumFractionDigits) {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        addFocusListener(createFocusListener());
        setHorizontalAlignment(RIGHT);
        setMinimumFractionDigits(minimumFractionDigits);
    }

    /****************************************************************************************************
     * Basic Property Methods of a Table Editor
     ***************************************************************************************************/

    public Class getValueClass() {
        return classType;
    }

    public void setValueClass(Class valueClass) {
        classType = valueClass;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    protected Object getModel() {
        return model;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public void setMaximumFractionDigits(int digits) {
        maximumFractionDigits = digits;
    }

    public void setMinimumFractionDigits(int digits) {
        minimumFractionDigits = digits;
    }

    public void setMinValue(double minValue) {
        this.minValue = BigDecimal.valueOf(minValue);
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = BigDecimal.valueOf(maxValue);
    }

    public JComponent getComponent() {
        return this;
    }

    /****************************************************************************************************
     * Assign the table coordinates.
     ***************************************************************************************************/
    public void setCoordinates(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /****************************************************************************************************
     * Reactivate editing within the table cell.
     ***************************************************************************************************/
    private void reactivateEditing(Object object) {
        if (object instanceof SimTable) {
            SimTable table = (SimTable) object;
            try {
                if (table.getSelectedRow() != row) {
                    table.setRowSelectionInterval(row, row);
                }
                table.editCellAt(row, column);
            } catch (Throwable ex) {
                UILog.debug(getClass(), ex);
            }
        }
    }

    /****************************************************************************************************
     * Get and Set data value of the editor.
     ***************************************************************************************************/

    public Object getValue() {
        return value;
    }

    public void setValue(Object object) {
        if (object instanceof Number) {
            value = (Number) object;
        } else {
            value = null;
        }
        setTextValue();
        eventAdaptor.fireTypeEditorEvent();
    }

    private void setTextValue() {
        if (value != null) {
            NumberFormat formatter = LocaleManager.getNumberFormatter();
            formatter.setMinimumFractionDigits(minimumFractionDigits);
            formatter.setMaximumFractionDigits(maximumFractionDigits);
            setText(formatter.format(value));
        } else {
            setText(null);
        }
    }

    /****************************************************************************************************
     * Validates the information in the editor is a number. Only returns true (ie. value changed) if the
     * new number is different than the old value.
     ***************************************************************************************************/
    public boolean checkValue() {
        String text = getText();

        if (StringUtility.isNullOrEmpty(text)) {
            if (nullable) {
                value = null;
                return true;
            }
            setTextValue();
            return false;
        }
        if (!StringUtility.isValidDecimalInput(text)) {
            displayInvalidNumberException();
            return false;
        }
        try {
            String testText = LocaleManager.getNumberFormatter().parse(text).toString();
            Number testNumber = (Number) classType.getConstructor(String.class).newInstance(testText);

            if (testNumber.doubleValue() < minValue.doubleValue()) {
                displayInvalidRangeException(LocaleManager.getNumberFormatter().format(testNumber));
                return false;
            }
            if (testNumber.doubleValue() > maxValue.doubleValue()) {
                if (testNumber.doubleValue() <= 99999999999999d) { // Format value if less than true parse maximum.
                    text = LocaleManager.getNumberFormatter().format(testNumber);
                }
                displayInvalidRangeException(text);
                return false;
            }
            if (maximumFractionDigits < 1) {
                if (testNumber.doubleValue() != testNumber.intValue()) {
                    displayInvalidIntegerException();
                    return false;
                }
            }
            value = testNumber;
            return true;
        } catch (Throwable t) {
            displayInvalidNumberException();
        }
        return false;
    }

    /****************************************************************************************************
     * Auto select the text when focus is gained.
     ***************************************************************************************************/

    public boolean requestFocusInWindow() {
        boolean returnValue = super.requestFocusInWindow();
        selectAll();
        return returnValue;
    }

    /****************************************************************************************************
     * Table Editor Listener
     ***************************************************************************************************/

    public void addTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.addTableEditorListener(listener);
    }

    public void removeTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.removeTableEditorListener(listener);
    }

    /****************************************************************************************************
     * Listens to focus on the field and fires a table editor event when focus is lost
     ***************************************************************************************************/
    private FocusListener createFocusListener() {
        return new FocusAdapter() {
            public void focusLost(FocusEvent event) {
                if (event.isTemporary()) {
                    return;
                }
                Number originalValue = value;
                if (checkValue()) {
                    if (isValueEqual(originalValue)) {
                        return;
                    }
                    setTextValue();
                    if (eventAdaptor != null) {
                        eventAdaptor.fireTypeEditorEvent();
                    }
                    return;
                }
                reactivateEditing(event.getOppositeComponent());
            }
        };
    }

    /****************************************************************************************************
     * Compares whether or not the object is equal to the current value within the editor.
     ***************************************************************************************************/
    private boolean isValueEqual(Object object) {
        if (value == null && object == null) {
            return true;
        }
        if (value == null || object == null) {
            return false;
        }

        Boolean isEqual = null;
        Method method = null;
        try {
            method = classType.getMethod("compareTo", Object.class);
        } catch (NoSuchMethodException nsme) {
            LogService.debug(this, "ignoring Excepton");
        }
        if (method != null) {
            try {
                Integer checkValue = (Integer) method.invoke(value, object);
                isEqual = checkValue == 0;
            } catch (Exception ex) {
                LogService.debug(this, "ignoring Excepton");
            }
        }
        if (isEqual == null) {
            isEqual = value.equals(object);
        }
        return isEqual;
    }

    /****************************************************************************************************
     * Produces the invalid range exception for the amount.
     ***************************************************************************************************/
    private void displayInvalidRangeException(String text) {
        Object[] values = new Object[3];
        values[0] = text;
        values[1] = LocaleManager.getNumberFormatter().format(minValue);
        values[2] = LocaleManager.getNumberFormatter().format(maxValue);
        UIStatusUtility.displayException(this, new UIException(CommonMessageText.VALUE_NOT_IN_RANGE, values));
        setTextValue();
    }

    /****************************************************************************************************
     * Displays The Invalid Number Exception
     ***************************************************************************************************/
    private void displayInvalidNumberException() {
        UIStatusUtility.displayException(this, new UIException(CommonMessageText.VALUE_NOT_VALID, getText()));
        setTextValue();
    }

    /****************************************************************************************************
     * Displays The Invalid Integer Exception
     ***************************************************************************************************/
    private void displayInvalidIntegerException() {
        UIStatusUtility.displayException(this, new UIException(CommonMessageText.VALUE_NOT_WHOLE, getText()));
        setTextValue();
    }
}
