package oracle.retail.sim.client.displayer;

import java.util.Comparator;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.common.store.BuddyStore;
import oracle.retail.sim.common.store.SimStore;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * Compares store names instead of toString();
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreNameComparator implements Comparator<Object> {

    public int compare(Object object1, Object object2) {
        if (object1 == object2) {
            return 0;
        }
        if (object1 == null) {
            return -1;
        }
        if (object2 == null) {
            return 1;
        }
        if (object1 instanceof Store && object2 instanceof Store) {
            return StringUtility.compareTo(((Store) object1).getName(), ((Store) object2).getName());
        }
        if (object1 instanceof BuddyStore && object2 instanceof BuddyStore) {
            return StringUtility.compareTo(((BuddyStore) object1).getName(), ((BuddyStore) object2).getName());
        }
        if (object1 instanceof SimStore && object2 instanceof SimStore) {
            return StringUtility.compareTo(((SimStore) object1).getName(), ((SimStore) object2).getName());
        }
        return 0;
    }
}
