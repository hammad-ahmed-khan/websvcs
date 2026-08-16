package oracle.retail.sim.client.swing.entrytable.editor;

import java.awt.Font;
import javax.swing.JComponent;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.entrytable.REntryTableEditor;

/********************************************************************************************************
 * DefaultEntryTableBooleanEditor
 * <p>
 * This class wraps an RCheckBoxEditor and provides a generic interface to the entry table by
 * implementing REntryTableEditor.
 * <p>
 * @see REntryTableEditor
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DefaultEntryTableBooleanEditor extends REntryTableEditor {

    private RCheckBoxEditor editor = new RCheckBoxEditor();

    public DefaultEntryTableBooleanEditor() {
        editor.getCheckBox().setHorizontalAlignment(EditorConstants.CENTER);
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
            editor.setSelected(false);
        } else if (value instanceof Boolean) {
            editor.setSelected((Boolean) value);
        } else {
            editor.setSelected(StringUtility.isEqual("TRUE", StringUtility.toUpperCase(value.toString())));
        }
    }

    public Object getData() {
        return editor.isSelected();
    }
}
