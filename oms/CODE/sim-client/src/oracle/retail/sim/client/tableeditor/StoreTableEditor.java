package oracle.retail.sim.client.tableeditor;

import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * A table editor for choosing a store in the system.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreTableEditor extends SimSearchTableEditor implements SimTableEditor {
    private static final long serialVersionUID = -963115747398292876L;

    private final SimTableEditorEventAdaptor eventAdaptor;

    public StoreTableEditor() {
        this(true);
    }

    public StoreTableEditor(boolean nullable) {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        setDisplayer(new AttributeDisplayer("id"));
        setIdentifier(SimName.STORE_ID);
    }

    public Class getValueClass() {
        return Store.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignore
    }

    public void setModel(Object model) {
        // Ignore
    }

    public Object getValue() {
        if (isErrorState()) {
            return null;
        }
        return getSearchData();
    }

    public void setData(Object value) {
        if (value instanceof Store) {
            setSearchData(value);
            checkValue();
            eventAdaptor.fireTypeEditorEvent();
            return;
        }
        if (value == null) {
            setSearchData(null);
            return;
        }
        throw new IllegalArgumentException("UserTableEditor only edits Employees!");
    }

    public void setValue(Object value) {
        if (value instanceof String) {
            getTextField().setText((String) value);
            checkValue();
        } else {
            setSearchData(value);
        }
        eventAdaptor.fireTypeEditorEvent();
    }

    public JComponent getComponent() {
        return this;
    }

    /****************************************************************************************************
     * Validates the stockable information in the table editor. It will find the stock item and verify
     * its isRanged value. If non-ranged are allowed, then a window will ask the user if they would like
     * to range the item.
     ***************************************************************************************************/
    public boolean checkValue() {
        return true;
    }

    public void addTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.addTableEditorListener(listener);
    }

    public void removeTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.removeTableEditorListener(listener);
    }

    /****************************************************************************************************
     * Id Value IS Modified
     ***************************************************************************************************/
    protected boolean doValueModified() {
        if (checkValue()) {
            eventAdaptor.fireTypeEditorEvent();
            return true;
        }
        return false;
    }

    /****************************************************************************************************
     * Determines if keystroke is invalid for input into the field.
     ***************************************************************************************************/
    public boolean isInvalidKeystroke(KeyEvent event) {
        return false;
    }
}
