package oracle.retail.sim.client.swing.tableeditor;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.widget.RTextField;

/********************************************************************************************************
 * A simple table editor for editing text within a text field style widget within a table cell. If the
 * constructor without a name is used, the table cell will not be editable.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StringTableEditor extends RTextField implements SimTableEditor {
    private static final long serialVersionUID = 118070803127606570L;

    private SimTableEditorEventAdaptor eventAdaptor;

    /****************************************************************************************************
     * Constructor.
     ***************************************************************************************************/

    public StringTableEditor() {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        addFocusListener(createFocusListener());
        setBorder(null);
        setMargin(null);
    }

    public StringTableEditor(String name) {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        addFocusListener(createFocusListener());
        setIdentifier(name);
        setBorder(null);
        setMargin(null);
    }

    /****************************************************************************************************
     * Basic Property Methods of a Table Editor
     ***************************************************************************************************/

    public Class getValueClass() {
        return String.class;
    }

    public void setValueClass(Class editorClass) {
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

    public void setValue(Object value) {
        if (value == null) {
            clear();
            return;
        }
        setText(value.toString());
        eventAdaptor.fireTypeEditorEvent();
    }

    public Object getValue() {
        return getText();
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
     * Triggers events when focus is lost in the field.
     ***************************************************************************************************/
    private FocusListener createFocusListener() {
        return new FocusAdapter() {
            public void focusLost(FocusEvent event) {
                if (!event.isTemporary() && checkValue()) {
                    if (eventAdaptor != null) {
                        eventAdaptor.fireTypeEditorEvent();
                    }
                }
            }
        };
    }

    /****************************************************************************************************
     * Determines if keystroke is invalid for field. Always false for a string editor.
     ***************************************************************************************************/
    public boolean isInvalidKeystroke(KeyEvent event) {
        return false;
    }
}
