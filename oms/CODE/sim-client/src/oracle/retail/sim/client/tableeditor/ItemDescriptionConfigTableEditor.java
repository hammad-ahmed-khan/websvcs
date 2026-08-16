package oracle.retail.sim.client.tableeditor;

import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.item.ItemDescriptionType;

/********************************************************************************************************
 * A table editor that allows a combo style selection of the item description type (either short or
 * long).
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemDescriptionConfigTableEditor extends RComboBoxTableEditor {
    private static final long serialVersionUID = 5629868664350183651L;

    public ItemDescriptionConfigTableEditor() {
        setDisplayer(new TranslatedObjectDisplayer());
        addItem(ItemDescriptionType.SHORT.toString());
        addItem(ItemDescriptionType.LONG.toString());
        removeEmptySelection();
        setValueClass(String.class);
    }
}
