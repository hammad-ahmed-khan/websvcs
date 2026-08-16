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
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.business.CommonMessageText;

/********************************************************************************************************
 * A table editor that edits Big Decimal values. Minimum and maximum decimal places may be assigned for
 * formatting. Please note that table editor must be declared individually for each attribute on the
 * table. Note that by default the minimum allowed fraction digits is zero (meaning whole numbers are
 * also valid).
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class BigDecimalTableEditor extends JTextField implements SimTableEditor {
    private static final long serialVersionUID = 130049090206699928L;

    private SimTableEditorEventAdaptor eventAdaptor;
    private BigDecimal editorValue;
    private int minimumFractionDigits;
    private int maximumFractionDigits = 3;
    private BigDecimal minValue = BigDecimal.ZERO;
    private BigDecimal maxValue = BigDecimal.valueOf(99999999.9999);
    private boolean nullable = true;
    private int row = -1;
    private int column = -1;

    /****************************************************************************************************
     * Constructors
     ***************************************************************************************************/

    public BigDecimalTableEditor() {
        this(0);
    }

    public BigDecimalTableEditor(int minimumFractionDigits) {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        addFocusListener(createFocusListener());
        setHorizontalAlignment(RIGHT);
        setMinimumFractionDigits(minimumFractionDigits);
    }

    public BigDecimalTableEditor(int minimumFractionDigits, int maximumFractionDigits) {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        addFocusListener(createFocusListener());
        setHorizontalAlignment(RIGHT);
        setMinimumFractionDigits(minimumFractionDigits);
        setMaximumFractionDigits(maximumFractionDigits);
    }

    /****************************************************************************************************
     * Basic Property Methods of a Table Editor
     ***************************************************************************************************/

    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    public void setMaximumFractionDigits(int digits) {
        maximumFractionDigits = digits;
    }

    public void setMinimumFractionDigits(int digits) {
        minimumFractionDigits = digits;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isNullable() {
        return nullable;
    }

    public Class getValueClass() {
        return BigDecimal.class;
    }

    public void setValueClass(Class classType) {
        // Ignore
    }

    public void setModel(Object model) {
        // Ignore
    }

    public JComponent getComponent() {
        return this;
    }

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
            } catch (Throwable exception) {
                UILog.debug(getClass(), exception);
            }
        }
    }

    /****************************************************************************************************
     * Get, Set data value of the editor.
     ***************************************************************************************************/

    public void setValue(Object value) {
        if (value instanceof Number) {
            editorValue = BigDecimal.valueOf(((Number) value).doubleValue());
        } else {
            editorValue = null;
        }
        setTextValue();
        eventAdaptor.fireTypeEditorEvent();
    }

    public Object getValue() {
        return editorValue;
    }

    private void setTextValue() {
        if (editorValue != null) {
            NumberFormat formatter = LocaleManager.getNumberFormatter();
            formatter.setMinimumFractionDigits(minimumFractionDigits);
            formatter.setMaximumFractionDigits(maximumFractionDigits);
            setText(formatter.format(editorValue));
        } else {
            setText(null);
        }
    }

    /****************************************************************************************************
     * Validates the information. If the value is not a BigDecimal or outside of the assigned range, then
     * an exception is displayed and focus is returned to the editor.
     ***************************************************************************************************/
    public boolean checkValue() {
        String text = getText();

        if (StringUtility.isNullOrEmpty(text)) {
            if (nullable) {
                editorValue = null;
                return true;
            }
            setTextValue();
            return false;
        }

        NumberFormat formatter = LocaleManager.getNumberFormatter();
        formatter.setMinimumFractionDigits(minimumFractionDigits);
        formatter.setMaximumFractionDigits(maximumFractionDigits);
        try {
            double value = formatter.parse(text).doubleValue();
            if (value < minValue.doubleValue() || value > maxValue.doubleValue()) {
                displayInvalidRangeException(value);
                return false;
            }
            editorValue = BigDecimal.valueOf(value);
            return true;
        } catch (Throwable exception) {
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
                if (checkValue()) {
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
     * Produces the invalid range exception for the amount.
     ***************************************************************************************************/
    private void displayInvalidRangeException(double amount) {
        Object[] values = new Object[3];
        values[0] = LocaleManager.getNumberFormatter().format(amount);
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
}
