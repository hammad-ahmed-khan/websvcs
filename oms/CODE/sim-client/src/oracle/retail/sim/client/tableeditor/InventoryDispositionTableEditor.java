package oracle.retail.sim.client.tableeditor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JComponent;
import oracle.retail.sim.client.displayer.InventoryDispositionDisplayer;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.widget.RComboBox;
import oracle.retail.sim.common.item.InventoryDisposition;

/*******************************************************************************
 * A table editor for Inventory Disposition objects that displays a combo box.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************/
public class InventoryDispositionTableEditor extends RComboBox implements SimTableEditor, ItemListener {
    private static final long serialVersionUID = -2178745443339619214L;

    private SimTableEditorEventAdaptor eventAdaptor;

    public InventoryDispositionTableEditor() {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        setDisplayer(new InventoryDispositionDisplayer());
        setSelectionRequired(true);
        removeEmptySelection();
        addItemListener(this);
    }

    public void setDispositions(List<InventoryDisposition> dispositions) {
        removeItemListener(this);
        setItems(dispositions);
        removeEmptySelection();
        addItemListener(this);
    }

    public Class getValueClass() {
        return InventoryDisposition.class;
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

    public InventoryDisposition getDisposition() {
        return (InventoryDisposition) getValue();
    }

    public Object getValue() {
        if (getSelectedIndex() > -1) {
            return getSelectedItem();
        }
        return null;
    }

    public void setValue(Object value) {
        if (getSelectedIndex() > -1) {
            setSelectedItem(value);
            eventAdaptor.fireTypeEditorEvent();
        }
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
