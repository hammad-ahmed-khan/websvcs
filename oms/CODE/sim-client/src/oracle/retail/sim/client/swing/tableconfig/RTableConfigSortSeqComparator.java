package oracle.retail.sim.client.swing.tableconfig;

import java.util.Comparator;

/********************************************************************************************************
 * This class sorts RTableColumnConfigData by sort order sequence.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class RTableConfigSortSeqComparator implements Comparator {

    public int compare(Object object1, Object object2) {
        if (object1 == null) {
            return -1;
        }
        if (object2 == null) {
            return 1;
        }
        int order1 = ((RTableConfigColumnData) object1).getSortOrder();
        int order2 = ((RTableConfigColumnData) object2).getSortOrder();
        if (order1 < order2) {
            return -1;
        }
        if (order1 > order2) {
            return 1;
        }
        return 0;
    }
}
