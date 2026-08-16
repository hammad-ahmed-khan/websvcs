package oracle.retail.sim.client.swing.entrytable.editor;

import oracle.retail.sim.client.swing.entrytable.REntryTableEditor;
import oracle.retail.sim.client.swing.entrytable.REntryTableEditorCreator;

/********************************************************************************************************
 * DefaultEntryTableIntegerCreator
 * <p>
 * This class implements the REntryTableEditorCreator interface and creates a integer editor for the
 * entry table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DefaultEntryTableIntegerCreator implements REntryTableEditorCreator {

    public REntryTableEditor createEditor() {
        return new DefaultEntryTableIntegerEditor();
    }
}
