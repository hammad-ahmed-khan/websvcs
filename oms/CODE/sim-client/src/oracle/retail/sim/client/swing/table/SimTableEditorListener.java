package oracle.retail.sim.client.swing.table;

import java.util.EventListener;

/***********************************************************************************************************
 * Implementors of this interface may get called when things happen on a SimTableEditor.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ***********************************************************************************************************/

public interface SimTableEditorListener extends EventListener {

    /********************************************************************************************************
    * This method gets called whenever something appropriate changes on a table editor.
    *********************************************************************************************************/
    void performTableEditorEvent(SimTableEditorEvent event);
}
