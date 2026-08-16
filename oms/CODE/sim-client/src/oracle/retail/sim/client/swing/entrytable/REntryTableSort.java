package oracle.retail.sim.client.swing.entrytable;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import oracle.retail.sim.client.locale.StringUtility;

/********************************************************************************************************
 * This class sorts REntryTable based information by a specific column. This class is exclusive to the
 * REntryTable and will only work on data formatted for a REntryTable.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class REntryTableSort {

    private REntryTableComparator comparator = new REntryTableComparator();
    private GregorianCalendar calendarOne = new GregorianCalendar();
    private GregorianCalendar calendarTwo = new GregorianCalendar();

    /****************************************************************************************************
     * Returns new REntryTableSort object.
     ***************************************************************************************************/
    public REntryTableSort() {
    }

    /****************************************************************************************************
     * Sorts the input ArrayList based on the column number. Each element of the ArrayList is a single
     * table row (in other words, an REntryRow object);
     * <p>
     * @param wrapper The wrapper to extra data fromt the data list
     * @param list The list of table rows to sort.
     * @param attribute The data attribute to sort on.
     * @param isAscending If true, the sort is ascending, if false, descending.
     * @throws ClassCastException Thrown if the data in a table element is not valid.
     ***************************************************************************************************/
    protected void sort(ReflectionWrapper wrapper, List data, String attribute, boolean isAscending) {
        comparator.initialize(wrapper, attribute, isAscending);
        Collections.sort(data, comparator);
    }

    /****************************************************************************************************
     * This private inner class is a comparator that handles the sort functionality.
     ***************************************************************************************************/
    private class REntryTableComparator implements Comparator {

        private ReflectionWrapper wrapper;
        private String attribute;
        private boolean sortAscending = true;

        /************************************************************************************************
         * Initializes the comparator
         * <p>
         * @param sortWrapper The wrapper to extra data fromt the data list
         * @param attribute The data attribute to sort on.
         * @param isAscending If true, the sort is ascending, if false, descending.
         ***********************************************************************************************/
        public void initialize(ReflectionWrapper sortWrapper, String sortAttribute, boolean isAscending) {
            wrapper = sortWrapper;
            attribute = sortAttribute;
            sortAscending = isAscending;
        }

        /************************************************************************************************
         * Implements the compare method to compare two objects. It takes two REntryRow objects and then
         * extracts the data at the column. It makes a comparison and swaps if necessary.
         * <p>
         * @param objectOne The first object.
         * @param objectTwo The second object.
         * @return Return the comparator value of the comparison (-1 is less than, 0 is equal, or 1 is
         *         greater).
         ***********************************************************************************************/
        public int compare(Object object1, Object object2) {
            Object data1 = null;
            Object data2 = null;

            if (sortAscending) {
                data1 = wrapper.getValue(((REntryRow) object1).getRowData(), attribute);
                data2 = wrapper.getValue(((REntryRow) object2).getRowData(), attribute);
            } else {
                data1 = wrapper.getValue(((REntryRow) object2).getRowData(), attribute);
                data2 = wrapper.getValue(((REntryRow) object1).getRowData(), attribute);
            }

            if (data1 == null) {
                return 0;
            }
            if (data2 == null) {
                return 1;
            }
            if (data1 instanceof String && data2 instanceof String) {
                return StringUtility.compareTo(data1.toString(), data2.toString());
            }
            if (data1 instanceof Boolean && data2 instanceof Boolean) {
                return StringUtility.compareTo(data1.toString(), data2.toString());
            }
            if (data1 instanceof Date && data2 instanceof Date) {
                calendarOne.setTime((Date) data1);
                calendarTwo.setTime((Date) data2);

                if (calendarOne.after(calendarTwo)) {
                    return 1;
                } else if (calendarOne.equals(calendarTwo)) {
                    return 0;
                }
                return -1;
            }
            if (data1 instanceof Number && data2 instanceof Number) {
                Double value1 = ((Number) data1).doubleValue();
                Double value2 = ((Number) data2).doubleValue();
                return value1.compareTo(value2);
            }
            if (data1 instanceof Comparable && data2 instanceof Comparable) {
                Comparable value1 = (Comparable) data1;
                Comparable value2 = (Comparable) data2;
                return value1.compareTo(value2);
            }
            return StringUtility.compareTo(data1.toString(), data2.toString());
        }
    }
}
