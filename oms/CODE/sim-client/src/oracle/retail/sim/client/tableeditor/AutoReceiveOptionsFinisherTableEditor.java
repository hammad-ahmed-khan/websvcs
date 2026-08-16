package oracle.retail.sim.client.tableeditor;

import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.warehousedelivery.AutoReceiveOptionsFinisher;

/********************************************************************************************************
 * A table editor that allows a combo box style selection of auto receiving options.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class AutoReceiveOptionsFinisherTableEditor extends RComboBoxTableEditor {
    private static final long serialVersionUID = -4488965733229267703L;

    public AutoReceiveOptionsFinisherTableEditor() {
        setDisplayer(new TranslatedObjectDisplayer());
        addItem(AutoReceiveOptionsFinisher.NOT_ALLOWED.toString());
        addItem(AutoReceiveOptionsFinisher.EXTERNAL_MESSAGE.toString());
        addItem(AutoReceiveOptionsFinisher.DATE_DRIVEN.toString());
        removeEmptySelection();
        setValueClass(String.class);
    }
}
