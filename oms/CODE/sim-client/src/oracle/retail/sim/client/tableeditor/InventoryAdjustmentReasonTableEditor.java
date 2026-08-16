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
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;

/********************************************************************************************************
 * A table editor for inventory adjustment return reasons that displays a combo box.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class InventoryAdjustmentReasonTableEditor extends RComboBoxTableEditor implements SimTableEditor, ItemListener, PopupMenuListener {
    private static final long serialVersionUID = 6727855264543252835L;

    private SimTableEditorEventAdaptor eventAdaptor;

    public InventoryAdjustmentReasonTableEditor() {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        setDisplayer(new TranslatedObjectDisplayer());
        setSelectionRequired(true);
        addPopupMenuListener(this);
        removeEmptySelection();
    }

    public void setAdjustmentReasons(List<InventoryAdjustmentReason> reasons) {
        removeItemListener(this);
        setItems(reasons);
        removeEmptySelection();
        addItemListener(this);
    }

    public Class getValueClass() {
        return InventoryAdjustmentReason.class;
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

    public InventoryAdjustmentReason getAdjustmentReason() {
        return (InventoryAdjustmentReason) getValue();
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
