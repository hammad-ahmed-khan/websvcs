package oracle.retail.sim.client.tableeditor;

import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.warehousedelivery.AutoReceiveOptionsWarehouse;

/********************************************************************************************************
 * A table editor that allows a combo box style selection of auto receiving options.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class AutoReceiveOptionsWarehouseTableEditor extends RComboBoxTableEditor {
    private static final long serialVersionUID = -4488965733229267703L;

    public AutoReceiveOptionsWarehouseTableEditor() {
        setDisplayer(new TranslatedObjectDisplayer());
        addItem(AutoReceiveOptionsWarehouse.NOT_ALLOWED.toString());
        addItem(AutoReceiveOptionsWarehouse.EXTERNAL_MESSAGE.toString());
        addItem(AutoReceiveOptionsWarehouse.DATE_DRIVEN.toString());
        removeEmptySelection();
        setValueClass(String.class);
    }
}
