package oracle.retail.sim.client.tableeditor;

import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.fulfillmentorderpick.HandheldPickingModeOptions;

/********************************************************************************************************
 * A table editor that allows a combo box style selection of handheld picking mode options.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class HandheldPickingModeOptionsTableEditor extends RComboBoxTableEditor {

    private static final long serialVersionUID = 2231515645271979253L;

    public HandheldPickingModeOptionsTableEditor() {
        setDisplayer(new TranslatedObjectDisplayer());
        addItem(HandheldPickingModeOptions.ENTER_PICK_QUANTITY.toString());
        addItem(HandheldPickingModeOptions.SCAN_EVERY_ITEM.toString());
        removeEmptySelection();
        setValueClass(String.class);
    }
}
