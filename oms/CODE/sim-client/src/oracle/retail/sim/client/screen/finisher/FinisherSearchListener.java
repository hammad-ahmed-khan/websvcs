package oracle.retail.sim.client.screen.finisher;

import oracle.retail.sim.client.swing.editor.SearchListener;
import oracle.retail.sim.common.source.Finisher;

/********************************************************************************************************
 * Generic Search Listener For Finisher Search Field Editor
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public abstract class FinisherSearchListener implements SearchListener {

    public void search() {
        FinisherLookupDialog dialog = new FinisherLookupDialog();
        dialog.setSearchListener(this);
        dialog.setVisible(true);
    }

    public void assign(Object value) {
        assignFinisher((Finisher) value);
    }

    public abstract void assignFinisher(Finisher finisher);
}
