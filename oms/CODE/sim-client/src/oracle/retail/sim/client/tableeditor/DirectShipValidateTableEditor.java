package oracle.retail.sim.client.tableeditor;

import oracle.retail.sim.client.displayer.BooleanDirectShipValidateDisplayer;
import oracle.retail.sim.client.swing.tableeditor.BooleanTableEditor;

/********************************************************************************************************
 * A table editor that allows a combo box style selection of available/unavailable options.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class DirectShipValidateTableEditor extends BooleanTableEditor {
    private static final long serialVersionUID = -2515828380965641387L;

    public DirectShipValidateTableEditor() {
        setDisplayer(new BooleanDirectShipValidateDisplayer());
    }
}
