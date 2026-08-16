package oracle.retail.sim.client.tableeditor;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.displayer.StoreSequenceAreaDisplayer;
import oracle.retail.sim.client.swing.table.SimTableEditorEvent;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.storesequence.StoreSequenceArea;

/********************************************************************************************************
 * A table editor which allows selections of a store sequence within a combo box display.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreSequenceAreaTableEditor extends RComboBoxTableEditor implements SimTableEditorListener {
    private static final long serialVersionUID = -5389791466531889821L;

    private StoreSequenceAreaDisplayer displayer = new StoreSequenceAreaDisplayer();
    private List<StoreSequenceArea> sequenceAreas = new ArrayList<>();
    private boolean includeEmptyOption = false;

    public StoreSequenceAreaTableEditor() {
        this(false);
    }

    public StoreSequenceAreaTableEditor(boolean includeEmptySelection) {
        setDisplayer(displayer);
        addTableEditorListener(this);
        includeEmptyOption = includeEmptySelection;
        if (!includeEmptyOption) {
            removeEmptySelection();
        }
    }

    public void setSequenceAreas(List<StoreSequenceArea> sequenceAreas) {
        if (sequenceAreas == null) {
            sequenceAreas = new ArrayList<>();
        }
        this.sequenceAreas = sequenceAreas;
    }

    public Class getValueClass() {
        return StoreSequenceArea.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignored
    }

    public boolean requestFocusInWindow() {
        boolean returnValue = super.requestFocusInWindow();
        removeItemListener(this);
        setItems(sequenceAreas);
        addItemListener(this);
        setValue(getSelectedItem());
        if (!includeEmptyOption) {
            removeEmptySelection();
        }
        return returnValue;
    }

    public void performTableEditorEvent(SimTableEditorEvent event) {
        if (getSelectedItem() instanceof StoreSequenceArea) {
            removeEmptySelection();
        }
    }
}
