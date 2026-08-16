package oracle.retail.sim.client.swing.displaytable;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.common.core.locale.NumberParser;

/********************************************************************************************************
 * This class sorts RDisplayTable based information by a specific column. This class is exclusive to the
 * RDisplayTable and will only work on data formated for a RDisplayTable.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TableRowSorter {

    private GregorianCalendar calendarOne = new GregorianCalendar();
    private GregorianCalendar calendarTwo = new GregorianCalendar();
    private Double d1;
    private Double d2;

    private RDisplayTableCellComparator comparator = new RDisplayTableCellComparator();

    /****************************************************************************************************
     * Returns new TableRowSorter object.
     ***************************************************************************************************/
    public TableRowSorter() {
    }

    /****************************************************************************************************
     * Sorts the input ArrayList based on the column number. Each element of the ArrayList is a single
     * table row (in other words, a RDisplayTableCell array);
     * <p>
     * @param list The list of table rows to sort.
     * @param column The table column to sort the list on.
     * @param sortAscending If true, the sort is ascending, if false, descending.
     *            <p>
     * @throws ClassCastException Thrown if the data in a table element is not valid.
     ***************************************************************************************************/
    public void sortTable(List list, int[] columnArray, boolean[] orderArray) {
        comparator.setSortParameters(columnArray, orderArray);

        Collections.sort(list, comparator);
    }

    /****************************************************************************************************
     *
     * INNER CLASS - Comparator that handles the sort functionality.
     *
     ***************************************************************************************************/
    private class RDisplayTableCellComparator implements Comparator {

        private boolean[] sortOrderArray = new boolean[0];
        private int[] sortColumnArray = new int[0];

        /************************************************************************************************
         * Assigns the sort parameters for the comparator.
         * <p>
         * @param columnArray An array of columns to sort.
         * @param orderArray An array of ascending flags (true = ascending, false = descending).
         ***********************************************************************************************/
        public void setSortParameters(int[] columnArray, boolean[] orderArray) {
            if (columnArray == null || orderArray == null) {
                return;
            }
            if (columnArray.length != orderArray.length) {
                return;
            }
            sortColumnArray = columnArray;
            sortOrderArray = orderArray;
        }

        /************************************************************************************************
         * Implements the compare method to compare two objects. It takes two row objects (array of
         * RDisplayTableCell objects) and then extracts the RDisplayTableCell at the appropriate column.
         * From these specific cells, it retrieves the value and type and makes a comparison.
         * <p>
         * @param o1 The first object.
         * @param 02 The second object.
         * @return Return the comparator value of the comparison (-1 is less than, 0 is equal, or 1 is
         *         greater).
         ***********************************************************************************************/
        public int compare(Object object1, Object object2) {
            RDisplayTableCell[] row1 = (RDisplayTableCell[]) object1;
            RDisplayTableCell[] row2 = (RDisplayTableCell[]) object2;

            String cellValue1 = null;
            String cellValue2 = null;
            int columnType = 0;
            int returnValue = 0;

            for (int i = 0; i < sortColumnArray.length; i++) {
                cellValue1 = row1[sortColumnArray[i]].getValue();
                cellValue2 = row2[sortColumnArray[i]].getValue();
                columnType = row1[sortColumnArray[i]].getType();

                if (sortOrderArray[i]) {
                    returnValue = compareByType(cellValue1, cellValue2, columnType);
                } else {
                    returnValue = compareByType(cellValue2, cellValue1, columnType);
                }

                if (returnValue != 0) {
                    return returnValue;
                }
            }
            return returnValue;
        }

        /************************************************************************************************
         * Implements the compare method to compare two objects. It takes two row objects (array of
         * RDisplayTableCell objects) and then extracts the RDisplayTableCell at the appropriate column.
         * From these specific cells, it retrieves the value and type and makes a comparison.
         * <p>
         * @param s1 The first string.
         * @param s2 The second string.
         * @param type The type that the string represents.
         ***********************************************************************************************/
        private int compareByType(String s1, String s2, int type) {
            try {
                switch (type) {
                    case DataTypeConstants.TEXT:
                    case DataTypeConstants.TEXT_FULL:
                    case DataTypeConstants.BOOLEAN:
                    case DataTypeConstants.HYPERLINK:
                        return StringUtility.compareTo(s1, s2);
                    case DataTypeConstants.CURRENCY:
                    case DataTypeConstants.CURRENCY_LEFT:
                    case DataTypeConstants.CURRENCY_RIGHT:
                    case DataTypeConstants.DECIMAL:
                    case DataTypeConstants.DECIMAL_LEFT:
                    case DataTypeConstants.DECIMAL_RIGHT:
                    case DataTypeConstants.INTEGER:
                    case DataTypeConstants.INTEGER_LEFT:
                    case DataTypeConstants.INTEGER_RIGHT:
                        NumberParser parser = NumberParser.getInstance(LocaleManager.getNumericLocale());
                        d1 = parser.getDouble(s1);
                        d2 = parser.getDouble(s2);
                        return d1.compareTo(d2);
                    case DataTypeConstants.DATE:
                    case DataTypeConstants.DATE_SHORT:
                        return compareDates(s1, s2, LocaleManager.getDateParser());
                    case DataTypeConstants.DATE_MEDIUM:
                        return compareDates(s1, s2, LocaleManager.getDateTimeParser(DateFormat.MEDIUM));
                    case DataTypeConstants.DATE_LONG:
                        return compareDates(s1, s2, LocaleManager.getDateTimeParser(DateFormat.LONG));
                    case DataTypeConstants.DATE_FULL:
                        return compareDates(s1, s2, LocaleManager.getDateTimeParser(DateFormat.MEDIUM));
                    case DataTypeConstants.ICON:
                        return 0;
                    default:
                        return StringUtility.compareTo(s1, s2);
                }
            } catch (Throwable exception) {
                return 0;
            }
        }

        /************************************************************************************************
         * Implements the compare method to compare two dates. It takes two strings and a formatter and
         * compares the dates.
         * <p>
         * @param s1 The first string.
         * @param s2 The second string.
         * @param formatter The date formatter to parse the strings.
         ***********************************************************************************************/
        private int compareDates(String s1, String s2, DateFormat formatter) throws ParseException {
            if (s1.length() == 0) {
                return -1;
            }
            if (s2.length() == 0) {
                return 1;
            }
            calendarOne.setTime(formatter.parse(s1));
            calendarTwo.setTime(formatter.parse(s2));
            if (calendarOne.before(calendarTwo)) {
                return -1;
            }
            if (calendarOne.after(calendarTwo)) {
                return 1;
            }
            return 0;
        }
    }
}
