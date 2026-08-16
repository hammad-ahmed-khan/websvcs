package oracle.retail.sim.client.swing.entrytable.editor;

import java.awt.Font;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.entrytable.REntryTableEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.widget.RHyperlink;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * DefaultEntryTableHyperlinkEditor
 * <p>
 * This class wraps an RHyperlink and provides a generic interface to the entry table by implementing
 * REntryTableEditor.
 * <p>
 * @see REntryTableEditor
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DefaultEntryTableHyperlinkEditor extends REntryTableEditor {

    private RHyperlink editor = new RHyperlink();

    public DefaultEntryTableHyperlinkEditor() {
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
            editor.setText(StringConstants.EMPTY);
        } else {
            editor.setText(value.toString());
        }
    }

    public Object getData() {
        return editor.isSelected();
    }

    public void performActionEvent(RActionEvent event) {
    }
}
