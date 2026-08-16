package oracle.retail.sim.client.tableeditor;

import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.fulfillmentorderpick.GenerateBinsOptions;

/********************************************************************************************************
 * A table editor that allows a combo box style selection of Bin generation options.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class GenerateBinsOptionsTableEditor extends RComboBoxTableEditor {

    private static final long serialVersionUID = -350220782719813876L;

    public GenerateBinsOptionsTableEditor() {
        setDisplayer(new TranslatedObjectDisplayer());
        addItem(GenerateBinsOptions.MANUAL.toString());
        addItem(GenerateBinsOptions.SYSTEM.toString());
        removeEmptySelection();
        setValueClass(String.class);
    }
}
