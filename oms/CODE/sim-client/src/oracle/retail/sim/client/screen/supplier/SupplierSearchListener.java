package oracle.retail.sim.client.screen.supplier;

import oracle.retail.sim.client.swing.editor.SearchListener;
import oracle.retail.sim.common.source.Supplier;

/********************************************************************************************************
 * Generic Search Listener For Supplier Search Field Editor
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public abstract class SupplierSearchListener implements SearchListener {

    public void search() {
        SupplierLookupDialog dialog = new SupplierLookupDialog();
        dialog.setSearchListener(this);
        dialog.setVisible(true);
    }

    public void assign(Object value) {
        assignSupplier((Supplier) value);
    }

    public abstract void assignSupplier(Supplier supplier);
}
