package oracle.retail.sim.client.swing.entrytable.editor;

import java.awt.Font;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.entrytable.REntryTableEditor;

/********************************************************************************************************
 * DefaultEntryTableTextEditor
 * <p>
 * This class wraps an RTextFieldEditor and provides a generic interface to the entry table by
 * implementing REntryTableEditor.
 * <p>
 * @see REntryTableEditor
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DefaultEntryTableTextEditor extends REntryTableEditor {

    private RTextFieldEditor editor = new RTextFieldEditor();

    public DefaultEntryTableTextEditor() {
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
        if (value == null) {
            editor.clear();
            return;
        }
        editor.setText(value.toString());
    }

    public Object getData() {
        return editor.getText();
    }
}
