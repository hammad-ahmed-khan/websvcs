package oracle.retail.sim.client.tableeditor;

import oracle.retail.sim.client.displayer.BooleanEnabledDisplayer;
import oracle.retail.sim.client.swing.tableeditor.BooleanTableEditor;

/********************************************************************************************************
 * A table editor that allows a combo box style selection of auto receiving options.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class BooleanEnabledTableEditor extends BooleanTableEditor {
    private static final long serialVersionUID = -6739730935400359561L;

    public BooleanEnabledTableEditor() {
        setDisplayer(new BooleanEnabledDisplayer());
    }
}
