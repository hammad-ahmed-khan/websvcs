package oracle.retail.sim.client.tableeditor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.widget.RComboBox;
import oracle.retail.sim.common.storesequence.StoreSequenceAreaType;

/********************************************************************************************************
 * Table editor that chooses a Store Sequence Area based on a combo box display.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SequenceAreaTypeTableEditor extends RComboBox implements SimTableEditor, ItemListener {
    private static final long serialVersionUID = 593962081918060770L;

    private SimTableEditorEventAdaptor eventAdaptor;

    public SequenceAreaTypeTableEditor() {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        setDisplayer(new TranslatedObjectDisplayer());
        removeItemListener(this);
        addItem(StoreSequenceAreaType.BACKROOM);
        addItem(StoreSequenceAreaType.SHOPFLOOR);
        addItemListener(this);
    }

    public StoreSequenceAreaType getSequenceAreaType() {
        if (isEmptySelection()) {
            return null;
        }
        return (StoreSequenceAreaType) getSelectedItem();
    }

    public Class getValueClass() {
        return StoreSequenceAreaType.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignored
    }

    public void setModel(Object model) {
        // Ignored
    }

    public void setCoordinates(int row, int column) {
        // Ignored
    }

    public Object getValue() {
        return getSequenceAreaType();
    }

    public void setValue(Object value) {
        StoreSequenceAreaType areaType = null;
        if (value instanceof StoreSequenceAreaType) {
            areaType = (StoreSequenceAreaType) value;
            if (StoreSequenceAreaType.NO_LOCATION == areaType) {
                areaType = null;
            }
        }
        if (areaType != null) {
            setSelectedItem(areaType);
        } else {
            setEmptySelection();
        }
        eventAdaptor.fireTypeEditorEvent();
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
