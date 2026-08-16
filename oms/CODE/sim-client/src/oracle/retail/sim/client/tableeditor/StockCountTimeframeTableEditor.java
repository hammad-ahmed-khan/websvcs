package oracle.retail.sim.client.tableeditor;

import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.stockcount.StockCountMessageText;

/********************************************************************************************************
 * A table editor that allows a combo style selection of the stock count timeframe value (either After or
 * Before Store Horus).
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountTimeframeTableEditor extends RComboBoxTableEditor {
    private static final long serialVersionUID = -4366644587019012760L;

    public StockCountTimeframeTableEditor() {
        setDisplayer(new TranslatedObjectDisplayer());
        addItem(StockCountMessageText.AFTER_STORE_CLOSE.getText());
        addItem(StockCountMessageText.BEFORE_STORE_OPEN.getText());
        removeEmptySelection();
        setValueClass(String.class);
    }
}
