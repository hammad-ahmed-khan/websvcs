package oracle.retail.sim.client.swing.entrytable.editor;

import oracle.retail.sim.client.swing.entrytable.REntryTableEditor;
import oracle.retail.sim.client.swing.entrytable.REntryTableEditorCreator;

/********************************************************************************************************
 * DefaultEntryTablePercentCreator
 * <p>
 * This class implements the REntryTableEditorCreator interface and creates a percent field editor for
 * the entry table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DefaultEntryTablePercentCreator implements REntryTableEditorCreator {

    public REntryTableEditor createEditor() {
        return new DefaultEntryTablePercentEditor();
    }
}
