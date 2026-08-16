package oracle.retail.sim.client.swing.entrytable.editor;

import java.awt.Font;
import java.math.BigDecimal;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.editor.RMoneyFieldEditor;
import oracle.retail.sim.client.swing.entrytable.REntryTableEditor;
import oracle.retail.sim.client.swing.util.UIException;

/***********************************************************************************************************************
 * DefaultEntryTableMoneyEditor
 * <p>
 * This class wraps an RMoneyFieldEditor and provides a generic interface to the entry table by implementing
 * REntryTableEditor.
 * <p>
 * @see REntryTableEditor
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 **********************************************************************************************************************/

public class DefaultEntryTableMoneyEditor extends REntryTableEditor {

    private RMoneyFieldEditor editor = new RMoneyFieldEditor();

    public DefaultEntryTableMoneyEditor() {
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
        if (value instanceof BigDecimal) {
            editor.setAmount((BigDecimal) value);
        } else {
            editor.clear();
        }
    }

    public Object getData() throws UIException {
        return editor.getAmount();
    }
}
