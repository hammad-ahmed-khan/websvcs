package oracle.retail.sim.client.tableeditor;

import oracle.retail.sim.client.displayer.BooleanAvailableDisplayer;
import oracle.retail.sim.client.swing.tableeditor.BooleanTableEditor;

/********************************************************************************************************
 * A table editor that allows a combo box style selection of available/unavailable options.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class BooleanAvailableTableEditor extends BooleanTableEditor {
    private static final long serialVersionUID = 1941912759676149077L;

    public BooleanAvailableTableEditor() {
        setDisplayer(new BooleanAvailableDisplayer());
    }
}
