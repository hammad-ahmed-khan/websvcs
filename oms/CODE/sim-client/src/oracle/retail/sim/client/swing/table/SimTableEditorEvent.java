package oracle.retail.sim.client.swing.table;

import java.util.EventObject;

/********************************************************************************************************
 * An event that has occurred in a table editor.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimTableEditorEvent extends EventObject {
    private static final long serialVersionUID = -7672004389231904956L;

    private boolean isFocusLostEvent;

    /****************************************************************************************************
     * Constructor
     * <p>
     * @param editor The table editor that was the source of the event.
     ***************************************************************************************************/
    public SimTableEditorEvent(SimTableEditor editor) {
        super(editor);
    }

    /****************************************************************************************************
     * Constructor
     * <p>
     * @param editor The table editor that was the source of the event.
     * @param isFocusLostEvent True if the editor event was trigged because the focus was lost.
     ***************************************************************************************************/
    public SimTableEditorEvent(SimTableEditor editor, boolean isFocusLostEvent) {
        super(editor);
        this.isFocusLostEvent = isFocusLostEvent;
    }

    /****************************************************************************************************
     * Retrieves the table editor that was the source of the event.
     ***************************************************************************************************/
    public SimTableEditor getTableEditor() {
        return (SimTableEditor) source;
    }

    /****************************************************************************************************
     * Returns trrue if the editor event was trigged because the focus was lost.
     ***************************************************************************************************/
    public boolean isFocusLostEvent() {
        return isFocusLostEvent;
    }
}
