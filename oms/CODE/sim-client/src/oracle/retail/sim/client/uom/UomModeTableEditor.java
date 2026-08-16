package oracle.retail.sim.client.uom;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.widget.RComboBox;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.lineitem.UnitOfMeasureWrapper;

/********************************************************************************************************
 * A table editor that allows for selection of units of measure from a combo box display.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UomModeTableEditor extends RComboBox implements SimTableEditor, ItemListener {
    private static final long serialVersionUID = 5295468547302255651L;

    private SimTableEditorEventAdaptor eventAdaptor;
    private UomModeDisplayer displayer = new UomModeDisplayer();

    /****************************************************************************************************
     * Create a new editor for UOMs. Clients should be sure to call the setModel() method with a
     * LineItem if they wish to have a specific standard UOM show up in this editor. If this is not
     * called, or it is called with a null value, then a string similar to 'Standard UOM' will show up
     * (depending on locale of course.)
     ***************************************************************************************************/
    public UomModeTableEditor() {
        this(false);
    }

    public UomModeTableEditor(boolean includePreferredUom) {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        setDisplayer(displayer);
        setSelectionRequired(true);
        addItem(UOMMode.STANDARD);
        addItem(UOMMode.CASES);
        if (includePreferredUom) {
            addItem(UOMMode.PREFERRED);
        }
        addItemListener(this);
    }

    public Class getValueClass() {
        return UOMMode.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignore
    }

    public void setModel(Object model) {
        displayer.setModel(model);
        if (model instanceof UnitOfMeasureWrapper) {
            UnitOfMeasureWrapper wrapper = (UnitOfMeasureWrapper) model;
            if (wrapper.isPreferredUomConversionAvailable()) {
                addPreferredMode();
            } else {
                removePreferredMode();
            }
        }
    }

    private void addPreferredMode() {
        for (Object object : getItems()) {
            if (object == UOMMode.PREFERRED) {
                return;
            }
        }
        addItem(UOMMode.PREFERRED);
    }

    private void removePreferredMode() {
        removeItem(UOMMode.PREFERRED);
    }

    public void setCoordinates(int row, int column) {
        // Ignore
    }

    public void setValue(Object value) {
        if (value instanceof UOMMode) {
            setSelectedItem(value);
        } else {
            setEmptySelection();
        }
        eventAdaptor.fireTypeEditorEvent();
    }

    public Object getValue() {
        if (getSelectedIndex() >= 0) {
            return getSelectedItem();
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
