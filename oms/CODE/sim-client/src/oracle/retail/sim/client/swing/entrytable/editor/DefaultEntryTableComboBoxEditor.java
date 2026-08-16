package oracle.retail.sim.client.swing.entrytable.editor;

import java.awt.Font;
import java.util.List;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.entrytable.REntryTableEditor;

/********************************************************************************************************
 * DefaultEntryTableComboBoxEditor
 * <p>
 * This class wraps an RComboBoxEditor and provides a generic interface to the entry table by
 * implementing REntryTableEditor.
 * <p>
 * @see REntryTableEditor
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DefaultEntryTableComboBoxEditor extends REntryTableEditor {

    private RComboBoxEditor editor = new RComboBoxEditor();

    public DefaultEntryTableComboBoxEditor() {
        editor.registerAction(this, "data.modified");
    }

    public JComponent getComponent() {
        return editor;
    }

    public void setIdentifier(String identifier) {
        editor.setIdentifier(identifier);
    }

    public void setFont(Font font) {
        editor.setFont(font);
    }

    public void setData(Object value) {
        editor.setSelectedItem(value);
    }

    public Object getData() {
        return editor.getSelectedItem();
    }

    public void setItems(List items) {
        editor.setActionsEnabled(false);
        editor.setItems(items);
        editor.setActionsEnabled(true);
    }
}
