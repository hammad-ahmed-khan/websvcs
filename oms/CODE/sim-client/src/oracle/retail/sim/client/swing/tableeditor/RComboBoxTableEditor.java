package oracle.retail.sim.client.swing.tableeditor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.widget.RComboBox;
import oracle.retail.sim.common.core.type.BasicDisplayer;

/********************************************************************************************************
 * Genertic table editor for editing values in a combo box style. A customized display can be passed into
 * the constructor to control the display of the object information within the combo box.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RComboBoxTableEditor extends RComboBox implements SimTableEditor, ItemListener {
    private static final long serialVersionUID = 4026098469833340581L;

    private SimTableEditorEventAdaptor eventAdaptor;
    private Class valueClass;

    private int row = -1;
    private int column = -1;

    /****************************************************************************************************
     * Constructors
     ***************************************************************************************************/

    public RComboBoxTableEditor() {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        addItemListener(this);
    }

    public RComboBoxTableEditor(BasicDisplayer displayer) {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        setDisplayer(displayer);
        addItemListener(this);
    }

    /****************************************************************************************************
     * Basic Property Methods of a Table Editor
     ***************************************************************************************************/

    public Class getValueClass() {
        return valueClass;
    }

    public void setValueClass(Class valueClass) {
        this.valueClass = valueClass;
    }

    public void setModel(Object model) {
        // Ignore
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
    protected void reactivateEditing(Object object) {
        if (object instanceof SimTable) {
            try {
                ((SimTable) object).editCellAt(row, column);
            } catch (Throwable ex) {
                UILog.debug(getClass(), ex);
            }
        }
    }

    /****************************************************************************************************
     * Get, Set and Check the data value of the editor
     ***************************************************************************************************/

    public Object getValue() {
        if (isEmptySelection()) {
            return null;
        }
        return getSelectedItem();
    }

    public void setValue(Object value) {
        setSelectedItem(value);
        eventAdaptor.fireTypeEditorEvent();
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
     * Determines if keystroke is invalid for field. Always false for a combo box editor.
     ***************************************************************************************************/
    public boolean isInvalidKeystroke(KeyEvent event) {
        return false;
    }
}
