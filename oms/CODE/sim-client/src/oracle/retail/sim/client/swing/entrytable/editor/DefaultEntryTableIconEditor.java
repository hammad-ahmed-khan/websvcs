package oracle.retail.sim.client.swing.entrytable.editor;

import java.awt.Font;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.entrytable.REntryTableEditor;
import oracle.retail.sim.client.swing.widget.RIconButton;

/********************************************************************************************************
 * DefaultEntryTableIconEditor
 * <p>
 * This class wraps an RIconButton and provides a generic interface to the entry table by implementing
 * REntryTableEditor.
 * <p>
 * @see REntryTableEditor
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DefaultEntryTableIconEditor extends REntryTableEditor {

    private RIconButton editor = new RIconButton();

    public DefaultEntryTableIconEditor() {
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
            editor.setIcon(null);
        } else {
            editor.setIcon(value.toString(), "Error");
        }
    }

    public Object getData() {
        return editor.getIcon();
    }
}
