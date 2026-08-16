package oracle.retail.sim.client.tableeditor;

import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.transfer.StoreAutoReceiveOptions;

/********************************************************************************************************
 * A table editor that allows a combo box style selection of auto receiving options.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class StoreAutoReceiveOptionsTableEditor extends RComboBoxTableEditor {

	private static final long serialVersionUID = 155226454558394350L;

	public StoreAutoReceiveOptionsTableEditor() {
        setDisplayer(new TranslatedObjectDisplayer());
        addItem(StoreAutoReceiveOptions.NOT_ALLOWED.toString());
        addItem(StoreAutoReceiveOptions.EXTERNAL_MESSAGE.toString());
        addItem(StoreAutoReceiveOptions.DATE_DRIVEN.toString());
        removeEmptySelection();
        setValueClass(String.class);
    }
}
