package oracle.retail.sim.client.swing.lov;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.filter.FilterElement;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIMessageText;

/********************************************************************************************************
 * This tool can be used to filter data objects from a collection of data objects. At the moment, this
 * ONLY filter attributes that are of a (String) data type.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ListOfValuesFilterTool {

    private static final String GET = "get";

    private Class classType;
    private String attribute;
    private FilterElement filterElement;

    /****************************************************************************************************
     * Constructs a new ListOfValuesFilterTool for a particular class type of data object.
     * <p>
     * @param classType The Class of the data object involved.
     ***************************************************************************************************/
    public ListOfValuesFilterTool(Class classType) {
        this.classType = classType;
    }

    /****************************************************************************************************
     * Assigns the attribute of the class type being filtered on. For example, if the classType is A and
     * attributes is X, then A.getX() is called to test for filtering.
     * <p>
     * @param attribute The attribute to filter on.
     ***************************************************************************************************/
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /****************************************************************************************************
     * Assigns the filter element that is being filtered with.
     * <p>
     * @param element The Filter Element.
     ***************************************************************************************************/
    public void setFilterElement(FilterElement element) {
        filterElement = element;
    }

    /****************************************************************************************************
     * This filteres the input data using the class type, attribute and element to determine matches.
     * <p>
     * @param data The input data to filter through.
     * @param attribute The attribute of the class type being filtered on.
     * @param element The filter element that is being filtered with.
     * @return The data that matched the filter.
     * @throws OldUIException Thrown if an error occurs while filtering the data.
     ***************************************************************************************************/
    public List getFilteredValues(List data, String attribute, FilterElement element) throws UIException {
        setAttribute(attribute);
        setFilterElement(element);
        return getFilteredValues(data);
    }

    /****************************************************************************************************
     * This filteres the input data using the previously assigned class type, attribute and filter
     * element.
     * <p>
     * @param data The input data to filter through.
     * @return The data that matched the filter.
     * @throws OldUIException Thrown if an error occurs while filtering the data.
     ***************************************************************************************************/
    public List getFilteredValues(List data) throws UIException {
        if (classType == null || StringUtility.isNullOrEmpty(attribute)) {
            return data;
        }
        if (data == null || data.isEmpty()) {
            return data;
        }
        if (filterElement == null) {
            return data;
        }

        // LISTOFVALUES - Not Done - Before filtering, find the attribute data type and use it to filter.

        List filteredList = new ArrayList<>();
        String methodName = StringUtility.getMethodName(GET, attribute);
        try {
            Method method = classType.getDeclaredMethod(methodName, (Class[]) null);
            String filterValue = filterElement.getValue();
            Object object;
            String value;
            for (Iterator iterator = data.iterator(); iterator.hasNext();) {
                object = iterator.next();
                value = (String) method.invoke(object, (Object[]) null);
                if (value != null && StringUtility.indexOf(value, filterValue) != -1) {
                    filteredList.add(object);
                }
            }
        } catch (Exception exception) {
            throw buildException(classType, attribute, exception.getClass().getName());
        }
        return filteredList;
    }

    /****************************************************************************************************
     * Builds a fatal exception.
     * <p>
     * @param object The object class name that caused the fatal exception.
     * @param attribute The attribute name on the object that caused the fatal exception.
     * @param exception The inner exception that occurred.
     *            <p>
     * @return A UIException containing the fatal exception information.
     ***************************************************************************************************/
    private static UIException buildException(Object object, String attribute, String exception) {
        Object[] values = new Object[] { object.getClass().getName(), attribute, exception };
        return new UIException(UIMessageText.DATA_TYPE_ATTRIBUTE_MISMATCH, values, RErrorSeverity.FATAL);
    }
}
