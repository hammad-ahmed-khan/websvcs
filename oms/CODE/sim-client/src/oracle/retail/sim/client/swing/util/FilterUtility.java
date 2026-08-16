package oracle.retail.sim.client.swing.util;

import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.filter.FilterElement;
import oracle.retail.sim.client.swing.filter.FilterType;

/********************************************************************************************************
 * Utility class to assist with client-side filtering.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FilterUtility {

    /****************************************************************************************************
     * Returns a filter object array based on passed in parameters.
     * <p>
     * @param objectArray The array of objects to filter.
     * @param valueArray Array of strings to filter by (matches the object array).
     * @param filter The filter element to filter with.
     *            <p>
     * @return The filtered object array.
     ***************************************************************************************************/
    public static Object[] filter(Object[] objectArray, String[] valueArray, FilterElement filter) {
        FilterType type = filter.getType();
        if (FilterType.CONTAINS == type) {
            return filterContains(objectArray, valueArray, filter.getValue());
            // } else if (FilterType.BEGIN == type) {
            // } else if (FilterType.END == type) {
            // } else if (FilterType.EQUAL == type) {
            // } else if (FilterType.GREATER == type) {
            // } else if (FilterType.GREATER_EQUAL == type) {
            // } else if (FilterType.LESS == type) {
            // } else if (FilterType.LESS_EQUAL == type) {
            // } else if (FilterType.NOT_BEGIN == type) {
        } else if (FilterType.NOT_CONTAINS == type) {
            return filterNotContains(objectArray, valueArray, filter.getValue());
            // } else if (FilterType.NOT_END == type) {
            // } else if (FilterType.NOT_EQUAL == type) {
        }
        throw new IllegalArgumentException("Invalid filter type!");
    }

    /****************************************************************************************************
     * Returns a filter object array filtered by "contains" filter type.
     * <p>
     * @param objectArray The array of objects to filter.
     * @param valueArray Array of strings to filter by (matches the object array).
     * @param filter The value the string must contain.
     *            <p>
     * @return The filtered object array.
     ***************************************************************************************************/
    public static Object[] filterContains(Object[] objectArray, String[] valueArray, String filter) {
        Object[] filterArray = new Object[0];
        Object[] tempArray;
        for (int i = 0; i < objectArray.length; i++) {
            if (StringUtility.indexOf(StringUtility.toLowerCase(valueArray[i]), StringUtility.toLowerCase(filter)) > -1) {
                tempArray = new Object[filterArray.length + 1];
                System.arraycopy(filterArray, 0, tempArray, 0, filterArray.length);
                filterArray = tempArray;
                filterArray[filterArray.length - 1] = objectArray[i];
            }
        }
        return filterArray;
    }

    /****************************************************************************************************
     * Returns a filter object array filtered by "not contains" filter type.
     * <p>
     * @param objectArray The array of objects to filter.
     * @param valueArray Array of strings to filter by (matches the object array).
     * @param filter The value the string must not contain.
     *            <p>
     * @return The filtered object array.
     ***************************************************************************************************/
    public static Object[] filterNotContains(Object[] objectArray, String[] valueArray, String filter) {
        Object[] filterArray = new Object[0];
        Object[] tempArray;
        for (int i = 0; i < objectArray.length; i++) {
            if (StringUtility.indexOf(StringUtility.toLowerCase(valueArray[i]), StringUtility.toLowerCase(filter)) == -1) {
                tempArray = new Object[filterArray.length + 1];
                System.arraycopy(filterArray, 0, tempArray, 0, filterArray.length);
                filterArray = tempArray;
                filterArray[filterArray.length - 1] = objectArray[i];
            }
        }
        return filterArray;
    }
}
