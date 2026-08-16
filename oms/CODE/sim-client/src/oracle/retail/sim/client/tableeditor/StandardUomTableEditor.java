package oracle.retail.sim.client.tableeditor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import oracle.retail.sim.client.displayer.StandardUomDisplayer;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.widget.RComboBox;
import oracle.retail.sim.common.lineitem.UOMMode;

/********************************************************************************************************
 * A table editor that allows for selection of units of measure from a combo box display.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StandardUomTableEditor extends RComboBox implements SimTableEditor, ItemListener {
    private static final long serialVersionUID = 5295468547302255651L;

    private SimTableEditorEventAdaptor eventAdaptor;
    private StandardUomDisplayer displayer = new StandardUomDisplayer();

    /****************************************************************************************************
     * Create a new editor for UOM's. Clients should be sure to call the setModel() method with a
     * LineItem if they wish to have a specific standard UOM show up in this editor. If this is not
     * called, or it is called with a null value, then a string similar to 'Standard UOM' will show up
     * (depending on locale of course.)
     ***************************************************************************************************/
    public StandardUomTableEditor() {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        setDisplayer(displayer);
        setSelectionRequired(true);
        addItem(UOMMode.STANDARD);
        addItem(UOMMode.CASES);
        addItemListener(this);
    }

    public Class getValueClass() {
        return Integer.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignore
    }

    public void setModel(Object model) {
        // Ignore
    }

    public void setCoordinates(int row, int column) {
        // Ignore
    }

    public void setValue(Object value) {
        if (value instanceof Integer) {
            Integer uomType = (Integer) value;
            if (UOMMode.STANDARD.getCode().equals(uomType)) {
                value = UOMMode.STANDARD;
            } else {
                value = UOMMode.CASES;
            }
        }
        if (value instanceof UOMMode) {
            setSelectedItem(value);
        } else {
            setEmptySelection();
        }
        eventAdaptor.fireTypeEditorEvent();
    }

    public Object getValue() {
        if (getSelectedIndex() >= 0) {
            UOMMode uomType = (UOMMode) getSelectedItem();
            return uomType.getCode();
        }
        return null;
    }

    public UOMMode getUnitOfMeasureMode() {
        return (UOMMode) getValue();
    }

    public JComponent getComponent() {
        return this;
    }

    public boolean checkValue() {
        return true;
    }

    public void addTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.addTableEditorListener(listener);
    }

    public void removeTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.removeTableEditorListener(listener);
    }

    public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            eventAdaptor.fireTypeEditorEvent();
        }
    }

    public boolean isInvalidKeystroke(KeyEvent event) {
        return false;
    }
}
