package oracle.retail.sim.client.tableeditor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import oracle.retail.sim.client.displayer.RetailStorePrinterTypeDisplayer;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.widget.RComboBox;
import oracle.retail.sim.common.report.StorePrinter;

/********************************************************************************************************
 * A table editor that allows for selection of printer types from a combo box display.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RetailStorePrinterTypeTableEditor extends RComboBox implements SimTableEditor, ItemListener {

	private static final long serialVersionUID = -3926088442048064739L;
	private SimTableEditorEventAdaptor eventAdaptor;
    private RetailStorePrinterTypeDisplayer displayer = new RetailStorePrinterTypeDisplayer();

    public RetailStorePrinterTypeTableEditor() {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        setDisplayer(displayer);
        setSelectionRequired(true);
        addItem(StorePrinter.TYPE_POSTSCRIPT);
        addItem(StorePrinter.TYPE_TICKET);
        addItemListener(this);
    }

    public Class getValueClass() {
        return Long.class;
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
        if (value != null && value instanceof Long && ((Long)value).longValue() > 0) {
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
