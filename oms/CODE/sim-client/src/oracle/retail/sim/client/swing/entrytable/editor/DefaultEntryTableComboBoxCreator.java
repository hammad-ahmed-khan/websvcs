package oracle.retail.sim.client.swing.entrytable.editor;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.swing.entrytable.REntryTableEditor;
import oracle.retail.sim.client.swing.entrytable.REntryTableEditorCreator;

/********************************************************************************************************
 * DefaultEntryTableComboBoxCreator
 * <p>
 * This class implements the REntryTableEditorCreator interface and creates a combo box editor for the
 * entry table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DefaultEntryTableComboBoxCreator implements REntryTableEditorCreator {

    private List items = new ArrayList<>();

    public DefaultEntryTableComboBoxCreator() {
    }

    public DefaultEntryTableComboBoxCreator(List items) {
        setItems(items);
    }

    public void setItems(List items) {
        this.items = items;
    }

    public REntryTableEditor createEditor() {
        DefaultEntryTableComboBoxEditor editor = new DefaultEntryTableComboBoxEditor();
        editor.setItems(items);
        return editor;
    }
}
