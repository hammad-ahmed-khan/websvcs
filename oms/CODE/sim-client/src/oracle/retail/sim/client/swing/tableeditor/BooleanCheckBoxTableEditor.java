package oracle.retail.sim.client.swing.tableeditor;

import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.widget.RCheckBox;

/********************************************************************************************************
 * Table editor implemented to handle Boolean value with a Check Box appearance.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class BooleanCheckBoxTableEditor extends RCheckBox implements SimTableEditor, ChangeListener {
    private static final long serialVersionUID = -5107823538322201052L;

    private SimTableEditorEventAdaptor eventAdaptor;

    /****************************************************************************************************
     * Constructor.
     ***************************************************************************************************/

    public BooleanCheckBoxTableEditor() {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        initEditor();
    }

    public BooleanCheckBoxTableEditor(boolean isYesNoEditor) {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        initEditor();
    }

    private void initEditor() {
        setHorizontalAlignment(CENTER);
        setOpaque(true);
        addChangeListener(this);
    }

    /****************************************************************************************************
     * Basic Property Methods of a Table Editor
     ***************************************************************************************************/

    public Class getValueClass() {
        return Boolean.class;
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

    /****************************************************************************************************
     * Assign coordinates to the editor.
     ***************************************************************************************************/
    public void setCoordinates(int row, int column) {
        // Ignore
    }

    /****************************************************************************************************
     * Get, Set and Check the data value of the editor
     ***************************************************************************************************/

    public Object getValue() {
        return getBoolean();
    }

    public Boolean getBoolean() {
        return isSelected();
    }

    public void setValue(Object value) {
        if (value instanceof Boolean) {
            setSelected((Boolean) value);
            return;
        }
        setSelected(false);
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
     * Implement change listener to fire editor event when data is changed in table editor.
     ***************************************************************************************************/

    public void stateChanged(ChangeEvent event) {
        eventAdaptor.fireTypeEditorEvent();
    }

    /****************************************************************************************************
     * Determines if keystroke is invalid for field. Always false for a boolean editor.
     ***************************************************************************************************/

    public boolean isInvalidKeystroke(KeyEvent event) {
        return false;
    }
}
