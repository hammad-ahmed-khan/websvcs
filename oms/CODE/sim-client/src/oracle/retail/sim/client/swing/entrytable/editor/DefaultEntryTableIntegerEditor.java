package oracle.retail.sim.client.swing.entrytable.editor;

import java.awt.Font;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.entrytable.REntryTableEditor;
import oracle.retail.sim.client.swing.util.UIException;

/********************************************************************************************************
 * DefaultEntryTableIntegerEditor
 * <p>
 * This class wraps an RIntegerFieldEditor and provides a generic interface to the entry table by
 * implementing REntryTableEditor.
 * <p>
 * @see REntryTableEditor
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DefaultEntryTableIntegerEditor extends REntryTableEditor {

    private RIntegerFieldEditor editor = new RIntegerFieldEditor();

    public DefaultEntryTableIntegerEditor() {
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
        } else if (value instanceof Integer) {
            editor.setInteger((Integer) value);
        } else {
            editor.setText(value.toString());
        }
    }

    public Object getData() throws UIException {
        return editor.getInteger();
    }
}
