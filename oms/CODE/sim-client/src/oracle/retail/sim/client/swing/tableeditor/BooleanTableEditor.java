package oracle.retail.sim.client.swing.tableeditor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.widget.RComboBox;

/********************************************************************************************************
 * Table editor implemented to handle Boolean value in a Combo Box like appearance. This editor can be
 * set to True/False or to Yes/No as display text.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class BooleanTableEditor extends RComboBox implements SimTableEditor, ItemListener {
    private static final long serialVersionUID = -5107823538322201052L;

    private SimTableEditorEventAdaptor eventAdaptor;
    private BooleanDisplayer displayer = new BooleanDisplayer();

    /****************************************************************************************************
     * Constructors
     ***************************************************************************************************/

    public BooleanTableEditor() {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        initEditor();
    }

    private void initEditor() {
        setDisplayer(displayer);
        setSelectionRequired(true);
        addItem(Boolean.TRUE);
        addItem(Boolean.FALSE);
        setSelectedItem(Boolean.FALSE);
        addItemListener(this);
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

    public void setCoordinates(int row, int column) {
        // Ignore
    }

    /****************************************************************************************************
     * Get, Set and Check the data value of the editor
     ***************************************************************************************************/

    public Object getValue() {
        return getSelectedItem();
    }

    public Boolean getBoolean() {
        return (Boolean) getSelectedItem();
    }

    public void setValue(Object value) {
        if (value instanceof Boolean) {
            setSelectedItem(value);
            return;
        }
        setSelectedItem(Boolean.FALSE);
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
     * Implement item change listener to fire editor event when data is changed in table editor.
     ***************************************************************************************************/
    public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            eventAdaptor.fireTypeEditorEvent();
        }
    }

    /****************************************************************************************************
     * Determines if keystroke is invalid for field. Always false for a boolean editor.
     ***************************************************************************************************/
    public boolean isInvalidKeystroke(KeyEvent event) {
        return false;
    }
}
