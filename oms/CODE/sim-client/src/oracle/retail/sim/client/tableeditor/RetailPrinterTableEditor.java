package oracle.retail.sim.client.tableeditor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.widget.RComboBox;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.common.report.RetailStorePrinterComparator;
import oracle.retail.sim.common.report.StorePrinter;

/********************************************************************************************************
 * A table editor that displays available printers in a combo box style editor.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RetailPrinterTableEditor extends RComboBox implements SimTableEditor, ItemListener {
    private static final long serialVersionUID = 1315788613326657918L;

    private SimTableEditorEventAdaptor eventAdaptor;

    public RetailPrinterTableEditor(boolean isBlank) {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        setDisplayer(new AttributeDisplayer("description"));
        setComparator(new RetailStorePrinterComparator());
        setSortEnabled(true);
        addItemListener(this);
        if (isBlank) {
            setEmptyType(RComboBoxEmptyType.BLANK);
        }
    }

    public Class getValueClass() {
        return StorePrinter.class;
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

    public StorePrinter getRetailPrinter() {
        return (StorePrinter) getValue();
    }

    public Object getValue() {
        Object object = getSelectedItem();
        if (object instanceof StorePrinter) {
            return object;
        }
        return null;
    }

    public void setValue(Object value) {
        if (value != null) {
            setSelectedItem(value);
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
