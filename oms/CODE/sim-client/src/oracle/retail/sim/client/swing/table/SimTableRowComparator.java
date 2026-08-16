package oracle.retail.sim.client.swing.table;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.core.type.Displayer;

/********************************************************************************************************
 * A comparator that knows how to compare objects held in an introspection-based table model. It is
 * intended that any introspection-based table model could hold on to one of these comparator objects,
 * and then change the sort criteria - so that multiple of these objects would not need to be created in
 * the table models.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimTableRowComparator implements Comparator {

    private SimTableModel model;
    private SimTableSortCriteria[] sortCriteria;

    /****************************************************************************************************
     * Create a new comparator. Once this comparator is created, it can be reused by setting different
     * sort criteria on it. In this way, an introspection-based table model can have one reference to one
     * comparator and reuse it... since it will know how to deal with the given model object type.
     * <p>
     * @param model The table model.
     ***************************************************************************************************/
    public SimTableRowComparator(SimTableModel model) {
        this.model = model;
    }

    /****************************************************************************************************
     * Sets the sort criteria for the comparator to use when sorting table data.
     ***************************************************************************************************/
    public void setSortCriteria(SimTableSortCriteria[] criteria) {
        sortCriteria = criteria;
    }

    /****************************************************************************************************
     * Gets the current sort criteria.
     ***************************************************************************************************/
    public SimTableSortCriteria[] getSortCriteria() {
        return sortCriteria;
    }

    /****************************************************************************************************
     * Compares two objects. It will attempt to use the objects passed in and the matching attributes on
     * the sort criteria and use reflection to get the actual value to compare with. If the value back
     * implements Comparable, it is used, otherwise the toString() value of the object is used.
     ***************************************************************************************************/
    public int compare(Object object1, Object object2) {
        if (sortCriteria == null) {
            return 0;
        }

        SimTableColumn column = null;
        Displayer displayer = null;
        Object real1 = null;
        Object real2 = null;
        boolean isAscending = false;
        int returnValue = 0;

        try {
            for (SimTableSortCriteria element : sortCriteria) {
                column = model.getColumn(element.getColumnIndex());
                displayer = column.getDisplayer();
                isAscending = element.isAscending();
                real1 = model.getTableData().getData(object1, column.getAttribute());
                real2 = model.getTableData().getData(object2, column.getAttribute());

                if (real1 == null && real2 == null) {
                    return 0;
                }
                if (real1 == null) {
                    return isAscending ? -1 : 1;
                }
                if (real2 == null) {
                    return isAscending ? 1 : -1;
                }

                if (real1 instanceof Comparable && !(real1 instanceof String) && !(real1 instanceof Enum)) {
                    returnValue = ((Comparable) real1).compareTo(real2);
                } else if (displayer != null) {
                    real1 = displayer.getDisplayText(real1, object1);
                    real2 = displayer.getDisplayText(real2, object2);
                    returnValue = StringUtility.compareToIgnoreCase(real1.toString(), real2.toString());
                } else {
                    returnValue = StringUtility.compareToIgnoreCase(real1.toString(), real2.toString());
                }

                if (returnValue == 0) {
                    continue;
                }
                if (!isAscending) {
                    returnValue = returnValue * -1;
                }
                break;
            }
        } catch (IllegalAccessException iaException) {
            UILog.error(getClass(), UIMessageText.COMPARE_FAILURE, iaException);
        } catch (InvocationTargetException itException) {
            Exception exception = itException;
            if (itException.getTargetException() instanceof Exception) {
                exception = (Exception) itException.getTargetException();
            }
            UILog.error(getClass(), UIMessageText.COMPARE_FAILURE, exception);
        }
        return returnValue;
    }

    /****************************************************************************************************
     * Tests if this comparator is equal to another.
     ***************************************************************************************************/
    public boolean equals(Object object) {
        return object == this;
    }

    /****************************************************************************************************
     * Returns the hashcode
     ***************************************************************************************************/
    public int hashCode() {
        return 3048756;
    }
}
