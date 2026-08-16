package oracle.retail.sim.client.tableeditor;

import java.awt.event.ItemEvent;
import java.util.List;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.stockreturn.ReturnReason;

/********************************************************************************************************
 * A table editor that allows for selection of return reasons within a combo box display.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ReturnReasonTableEditor extends RComboBoxTableEditor {
    private static final long serialVersionUID = 2284790267012564333L;

    private TranslatedObjectDisplayer displayer = new TranslatedObjectDisplayer();

    public ReturnReasonTableEditor() {
        setDisplayer(displayer);
    }

    public void setReturnReasons(List<ReturnReason> reasons) {
        removeItemListener(this);
        setItems(reasons);
        addItemListener(this);
    }

    public ReturnReason getReturnReason() {
        return (ReturnReason) getSelectedItem();
    }

    public Class getValueClass() {
        return ReturnReason.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignore
    }

    public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            removeEmptySelection();
            super.itemStateChanged(event);
        }
    }
}
