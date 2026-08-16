package oracle.retail.sim.client.swing.tableeditor;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.locale.StringHelper;

/********************************************************************************************************
 * A table editor that edits Quantity values. Minimum and maximum decimal places may be assigned for
 * formatting. Please note that table editor must be declared individually for each attribute on the
 * table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class QuantityTableEditor extends JTextField implements SimTableEditor {
    private static final long serialVersionUID = 130049090206699928L;

    private Class classType;
    private SimTableEditorEventAdaptor eventAdaptor;
    private Quantity value;
    private Object model;
    private int minimumFractionDigits;
    private int maximumFractionDigits = ValidKeystrokeUtility.DECIM_PLACE_SIZE;
    private boolean nullable;
    private BigDecimal maxValue = BigDecimal.valueOf(9999999d);
    private int row = -1;
    private int column = -1;
    private boolean isValueChanged;

    private static final Class[] CLASS_TYPES = new Class[] { String.class };

    /****************************************************************************************************
     * Constructors
     ***************************************************************************************************/

    public QuantityTableEditor() {
        this(false);
    }

    public QuantityTableEditor(boolean isNullAllowed) {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        setHorizontalAlignment(RIGHT);
        setNullable(isNullAllowed);
        addFocusListener(createFocusListener());
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

    protected boolean isDecimalAllowed() {
        return true;
    }

    public JComponent getComponent() {
        return this;
    }

    /****************************************************************************************************
     * Assign coordinates to the editor.
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

    // If decimal are not allowed and a fractional value exists, then it must be rounded up to '1'.

    private void setTextValue() {
        if (value == null) {
            setText(null);
            return;
        }
        if (isDecimalAllowed()) {
            NumberFormat formatter = LocaleManager.getNumberFormatter();
            formatter.setMinimumFractionDigits(minimumFractionDigits);
            formatter.setMaximumFractionDigits(maximumFractionDigits);
            setText(formatter.format(value.getBigDecimal()));
            return;
        }
        setText(LocaleManager.getNumberFormatter().format(value.getBigDecimal()));
    }

    public void setValue(Object object) {
        value = (Quantity) object;
        setTextValue();
        eventAdaptor.fireTypeEditorEvent();
    }

    /****************************************************************************************************
     * Validates the information in the editor is a Quantity. Only returns true (ie. value changed) if
     * the new number is different than the old value.
     ***************************************************************************************************/
    public boolean checkValue() {
        String text = StringHelper.trimToNull(getText());

        // Check For Null
        if (StringUtility.isNullOrEmpty(text)) {
            if (nullable) {
                isValueChanged = value != null;
                value = null;
            } else {
                setTextValue();
                isValueChanged = false;
            }
            return true;
        }

        // Check For Invalid Text
        if (!StringUtility.isValidDecimalInput(text)) {
            displayInvalidNumberException();
            return false;
        }

        try {
            // Check For Range
            Number number = LocaleManager.getNumberFormatter().parse(text);
            Number fullNumber = getFullQuantity(number);
            if (fullNumber.doubleValue() > maxValue.doubleValue()) {
                if (fullNumber.doubleValue() <= 99999999999999d) { // Format value if less than parse max
                    text = LocaleManager.getNumberFormatter().format(fullNumber);
                }
                displayNotInRangeError(text);
                return false;
            }

            // Check For Negative
            Quantity testNumber = (Quantity) classType.getConstructor(CLASS_TYPES).newInstance(number.toString());
            if (testNumber.doubleValue() < 0) {
                displayInvalidNegativeException();
                return false;
            }

            // Check If Changed
            isValueChanged = !isValueEqual(testNumber);

            // Validate If Changed
            if (isValueChanged) {
                if (isDecimalAllowed()) {
                    value = new Quantity(BigDecimal.valueOf(testNumber.doubleValue()));
                } else if (testNumber.doubleValue() == testNumber.longValue()) {
                    value = new Quantity(BigDecimal.valueOf(testNumber.longValue()));
                } else {
                    displayInvalidNumberException();
                    return false;
                }
            }
            return true;
        } catch (Throwable exception) {
            displayInvalidNumberException();
        }
        return false;
    }

    /****************************************************************************************************
     * Returns the full quantity entered. This is abstracted as a method to allow for subclasses to
     * alter logic.
     ***************************************************************************************************/
    protected Number getFullQuantity(Number enteredQty) {
        return enteredQty;
    }

    /****************************************************************************************************
     * Displays The Invalid Negative Exception
     ***************************************************************************************************/
    private void displayInvalidNegativeException() {
        UIStatusUtility.displayException(this, new UIException(CommonMessageText.QUANTITY_NOT_POSITIVE));
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
     * Helper method to display errors
     ***************************************************************************************************/
    private void displayNotInRangeError(String text) {
        NumberFormat formatter = LocaleManager.getNumberFormatter();

        Object[] values = new Object[3];
        values[0] = text;
        values[1] = formatter.format(BigDecimal.ZERO);
        values[2] = formatter.format(maxValue);

        UIStatusUtility.displayException(this, new UIException(CommonMessageText.VALUE_NOT_IN_RANGE, values));
        setTextValue();
    }

    /****************************************************************************************************
     * Compares whether or not the object is equal to the current value within the editor.
     ***************************************************************************************************/
    private boolean isValueEqual(Quantity testQuantity) {
        if (value == null && testQuantity == null) {
            return true;
        }
        if (value == null || testQuantity == null) {
            return false;
        }
        return value.equals(testQuantity);
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
                if (checkValue()) {
                    if (isValueChanged) {
                        setTextValue();
                        if (eventAdaptor != null) {
                            eventAdaptor.fireTypeEditorEvent();
                        }
                    }
                    return;
                }
                reactivateEditing(event.getOppositeComponent());
            }
        };
    }
}
