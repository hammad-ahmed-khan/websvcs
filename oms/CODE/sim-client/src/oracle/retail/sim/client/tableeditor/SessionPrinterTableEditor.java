package oracle.retail.sim.client.tableeditor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.report.StorePrinter;

/********************************************************************************************************
 * A table editor for Session printers that displays a combo box.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class SessionPrinterTableEditor extends RComboBoxTableEditor implements SimTableEditor, ItemListener, PopupMenuListener {
    private static final long serialVersionUID = -2924881413193879753L;
    private SimTableEditorEventAdaptor eventAdaptor;

    public SessionPrinterTableEditor() {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        setDisplayer(new TranslatedObjectDisplayer());
        //setSelectionRequired(true);
        addPopupMenuListener(this);
        //removeEmptySelection();
    }

    public void setPrinters(List<StorePrinter> printers) {
        removeItemListener(this);
        setItems(printers);
        //removeEmptySelection();
        addItemListener(this);
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

    public StorePrinter getPrinterDesc() {
        return (StorePrinter) getValue();
    }

    public JComponent getComponent() {
        return this;
    }

    public void addTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.addTableEditorListener(listener);
    }

    public void removeTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.removeTableEditorListener(listener);
    }

    public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED && !isPopupVisible()) {
            eventAdaptor.fireTypeEditorEvent();
        }
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent event) {
    }

    public void popupMenuCanceled(PopupMenuEvent event) {
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
        eventAdaptor.fireTypeEditorEvent();
    }

    public boolean isInvalidKeystroke(KeyEvent event) {
        return false;
    }
}
