package oracle.retail.sim.client.screen.store;

import oracle.retail.sim.client.swing.editor.SearchListener;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * Generic Store Listener For Item Search Field Editor
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public abstract class StoreSearchListener implements SearchListener {

    public void search() {
        StoreLookupDialog dialog = new StoreLookupDialog();
        dialog.setSearchListener(this);
        dialog.setVisible(true);
    }

    public void assign(Object value) {
        assignStore((Store) value);
    }

    public abstract void assignStore(Store store);
}
