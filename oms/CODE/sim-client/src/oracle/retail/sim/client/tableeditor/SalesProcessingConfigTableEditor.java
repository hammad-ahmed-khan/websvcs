package oracle.retail.sim.client.tableeditor;

import oracle.retail.sim.client.displayer.SimDisplayerFactory;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.stockcount.StockCountSalesProcess;

/********************************************************************************************************
 * A table editor that allows a combo style selection of the stock count sales processing value (either
 * Timestamp or Daily Sales)
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SalesProcessingConfigTableEditor extends RComboBoxTableEditor {
    private static final long serialVersionUID = 5629868664350183651L;

    public SalesProcessingConfigTableEditor() {
        setDisplayer(SimDisplayerFactory.createSimEnumDisplayer(StockCountSalesProcess.class));
        addItem(StockCountSalesProcess.TIMESTAMP.getCode());
        addItem(StockCountSalesProcess.DAILY.getCode());
        removeEmptySelection();
        setValueClass(String.class);
    }
}
