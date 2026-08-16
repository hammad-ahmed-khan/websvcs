package oracle.retail.sim.client.tableeditor;

import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.fulfillmentorderpick.DefaultCustomerOrderPickingMethodOptions;

/********************************************************************************************************
 * A table editor that allows a combo box style selection of default customer order picking options.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class DefaultCustomerOrderPickingOptionsTableEditor extends RComboBoxTableEditor {

    private static final long serialVersionUID = 7248044452115728517L;

    public DefaultCustomerOrderPickingOptionsTableEditor() {
        setDisplayer(new TranslatedObjectDisplayer());
        addItem(DefaultCustomerOrderPickingMethodOptions.SIM_CUSTOMER_ORDER.toString());
        addItem(DefaultCustomerOrderPickingMethodOptions.BIN.toString());
        removeEmptySelection();
        setValueClass(String.class);
    }
}
