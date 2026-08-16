package oracle.retail.sim.client.displayer;

import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.store.BuddyStore;
import oracle.retail.sim.common.store.SimStore;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * This displayer formats to "id - description" for a variety of objects, otherwise it return toString().
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreDisplayer extends AbstractDisplayer {

    private final String separator = " - ";

    public String getDisplayText(Object object) {
        if (object == null) {
            return StringConstants.EMPTY;
        }
        if (object instanceof BuddyStore) {
            BuddyStore store = (BuddyStore) object;
            return store.getId() + separator + store.getName();
        }
        if (object instanceof Store) {
            Store store = (Store) object;
            return store.getId() + separator + store.getName();
        }
        if (object instanceof SimStore) {
            SimStore store = (SimStore) object;
            return store.getId() + separator + store.getName();
        }
        return object.toString();
    }
}
