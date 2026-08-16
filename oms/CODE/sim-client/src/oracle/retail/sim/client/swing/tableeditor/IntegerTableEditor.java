package oracle.retail.sim.client.swing.tableeditor;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
 * A table editor that edits Integer values. Minimum and maximum allowed values may be assigned. Please
 * note that table editor must be declared individually for each attribute on the table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class IntegerTableEditor extends JTextField implements SimTableEditor {
    private static final long serialVersionUID = 130049090206699928L;

    private SimTableEditorEventAdaptor eventAdaptor;
    private Integer editorValue;
    private int minValue;
    private int maxValue = 99999999;
    private boolean nullable = true;
    private int row = -1;
    private int column = -1;

    /****************************************************************************************************
     * Constructors
     ***************************************************************************************************/

    public IntegerTableEditor() {
        initializeEditor();
    }

    public IntegerTableEditor(int minValue, int maxValue) {
        initializeEditor();
        setMinValue(minValue);
        setMaxValue(maxValue);
    }

    private void initializeEditor() {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        addFocusListener(createFocusListener());
        setHorizontalAlignment(RIGHT);
    }

    /****************************************************************************************************
     * Basic Property Methods of a Table Editor
     ***************************************************************************************************/

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isNullable() {
        return nullable;
    }

    public Class getValueClass() {
        return Integer.class;
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
     * Get, Set the data value of the editor
     ***************************************************************************************************/

    public Object getValue() {
        return editorValue;
    }

    public void setValue(Object value) {
        if (value instanceof Number) {
            editorValue = ((Number) value).intValue();
        } else {
            editorValue = null;
        }
        setTextValue();
        eventAdaptor.fireTypeEditorEvent();
    }

    private void setTextValue() {
        if (editorValue != null) {
            setText(LocaleManager.getIntegerFormatter().format(editorValue));
        } else {
            setText(null);
        }
    }

    /****************************************************************************************************
     * Validates the information. If the value is not an integer or outside of the assigned range, then
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
        if (!StringUtility.isValidIntegerInput(text)) {
            displayInvalidNumberException();
            return false;
        }
        try {
            Number number = LocaleManager.getNumberFormatter().parse(text);
            int value = number.intValue();
            if (value < minValue || value > maxValue) {
                if (number.doubleValue() <= 99999999999999d) { // Format value if less than parse max
                    text = LocaleManager.getNumberFormatter().format(number);
                }
                displayNotInRangeError(text);
                return false;
            }
            if (number.doubleValue() != number.intValue()) {
                displayInvalidIntegerException();
                return false;
            }
            editorValue = value;
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
     * Helper method to display errors
     ***************************************************************************************************/
    private void displayNotInRangeError(String text) {
        NumberFormat formatter = LocaleManager.getNumberFormatter();

        Object[] values = new Object[3];
        values[0] = text;
        values[1] = formatter.format(minValue);
        values[2] = formatter.format(maxValue);

        UIStatusUtility.displayException(this, new UIException(CommonMessageText.VALUE_NOT_IN_RANGE, values));
        setTextValue();
    }

    /****************************************************************************************************
     * Displays The Invalid Integer Exception
     ***************************************************************************************************/
    private void displayInvalidIntegerException() {
        UIStatusUtility.displayException(this, new UIException(CommonMessageText.VALUE_NOT_WHOLE, getText()));
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
