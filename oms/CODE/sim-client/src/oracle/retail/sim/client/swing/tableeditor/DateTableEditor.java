package oracle.retail.sim.client.swing.tableeditor;

import java.awt.event.KeyEvent;
import java.util.Date;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.widget.RDateField;
import oracle.retail.sim.common.date.SimDateUtil;

/********************************************************************************************************
 * Table editor designed to edit a date value using the RDateField functionality. If dealing with an
 * invalid date value whether as a passed in object or entered within the field, the invalid information
 * is converted to a NULL date.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DateTableEditor extends RDateField implements SimTableEditor {
    private static final long serialVersionUID = 8279608250215538040L;

    private SimTableEditorEventAdaptor eventAdaptor;
    private boolean selectionRequired = true;

    /****************************************************************************************************
     * Constructors
     ***************************************************************************************************/

    public DateTableEditor() {
        this(true);
    }

    public DateTableEditor(boolean selectionRequired) {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        this.selectionRequired = selectionRequired;
    }

    /****************************************************************************************************
     * Basic Property Methods of a Table Editor
     ***************************************************************************************************/

    public Class getValueClass() {
        return Date.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignore
    }

    public void setModel(Object model) {
        // Ignore
    }

    public JComponent getComponent() {
        return this;
    }

    public void setCoordinates(int row, int column) {
        // Ignore
    }

    /****************************************************************************************************
     * Get, Set and Check the data value of the editor
     ***************************************************************************************************/

    public Object getValue() {
        try {
            return getDate();
        } catch (UIException e) {
            return null;
        }
    }

    public void setValue(Object value) {
        if (value instanceof Date) {
            setDate((Date) value);
        } else if (selectionRequired) {
            setDate(SimDateUtil.getCurrentDate());
        } else {
            setDate(null);
        }
    }

    public boolean checkValue() {
        return true;
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
     * Determines if keystroke is INVALID for field. Always false for a date editor.
     ***************************************************************************************************/

    public boolean isInvalidKeystroke(KeyEvent event) {
        return false;
    }
}
