package oracle.retail.sim.client.swing.entrytable.editor;

import java.awt.Font;
import java.util.Date;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.entrytable.REntryTableEditor;
import oracle.retail.sim.client.swing.util.UIException;

/********************************************************************************************************
 * DefaultEntryTableDateEditor
 * <p>
 * This class wraps an RDateFieldEditor and provides a generic interface to the entry table by implementing REntryTableEditor.
 * <p>
 * @see REntryTableEditor
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DefaultEntryTableDateEditor extends REntryTableEditor {

    private RDateFieldEditor editor = new RDateFieldEditor();

    public DefaultEntryTableDateEditor() {
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
        if (value instanceof Date) {
            editor.setDate((Date) value);
            return;
        }
        try {
            editor.setText(value.toString());
        } catch (UIException e) {
            editor.clear();
        }
    }

    public Object getData() throws UIException {
        return editor.getDate();
    }
}
