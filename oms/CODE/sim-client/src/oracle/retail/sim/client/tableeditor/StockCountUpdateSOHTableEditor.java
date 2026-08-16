package oracle.retail.sim.client.tableeditor;

import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;

/********************************************************************************************************
 * A table editor that allows a combo style selection of a boolean for the update stock on hand options.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountUpdateSOHTableEditor extends RComboBoxTableEditor {
    private static final long serialVersionUID = -1460513442201228829L;

    public StockCountUpdateSOHTableEditor() {
        setDisplayer(new StockCountUpdateSOHTableDisplayer());
        addItem(Boolean.TRUE);
        addItem(Boolean.FALSE);
        removeEmptySelection();
        setValueClass(Boolean.class);
    }
}
