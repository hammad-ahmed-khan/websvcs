package oracle.retail.sim.client.swing.entrytable.editor;

import java.awt.Font;
import java.math.BigDecimal;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.editor.RDecimalFieldEditor;
import oracle.retail.sim.client.swing.entrytable.REntryTableEditor;
import oracle.retail.sim.client.swing.util.UIException;

/********************************************************************************************************
 * DefaultEntryTableDecimalEditor
 * <p>
 * This class wraps an RDecimalFieldEditor and provides a generic interface to the entry table by
 * implementing REntryTableEditor.
 * <p>
 * @see REntryTableEditor
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DefaultEntryTableDecimalEditor extends REntryTableEditor {

    private RDecimalFieldEditor editor = new RDecimalFieldEditor();

    public DefaultEntryTableDecimalEditor() {
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
        } else if (value instanceof BigDecimal) {
            editor.setDecimal((BigDecimal) value);
        } else if (value instanceof Double) {
            editor.setDecimal((Double) value);
        } else {
            editor.setText(value.toString());
        }
    }

    public Object getData() throws UIException {
        return editor.getBigDecimal();
    }
}
