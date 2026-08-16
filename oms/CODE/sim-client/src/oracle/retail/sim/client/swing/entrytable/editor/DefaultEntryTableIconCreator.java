package oracle.retail.sim.client.swing.entrytable.editor;

import oracle.retail.sim.client.swing.entrytable.REntryTableEditor;
import oracle.retail.sim.client.swing.entrytable.REntryTableEditorCreator;

/********************************************************************************************************
 * DefaultEntryTableIconCreator
 * <p>
 * This class implements the REntryTableEditorCreator interface and creates a icon editor for the entry
 * table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DefaultEntryTableIconCreator implements REntryTableEditorCreator {

    public REntryTableEditor createEditor() {
        return new DefaultEntryTableIconEditor();
    }
}
