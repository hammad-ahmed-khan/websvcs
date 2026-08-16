package oracle.retail.sim.client.swing.lov;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIMessageText;

/***************************************************************************************************
 * This is used to select values from a full list of data to select based on an attribute in the data.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ***************************************************************************************************/

public class ListOfValuesSelectorTool {

    private static final UIMessageText ERROR_CODE = UIMessageText.DATA_TYPE_ATTRIBUTE_MISMATCH;
    private static final String GET = "get";

    private Class classType;
    private String attribute;
    private Collection fullData = new ArrayList<>();

    /***************************************************************************************************
     * Constructs a new selector tool around the defined class.
     * <p>
     * @param classType The Class that will be processed by this selector.
     * @param attribute The data attribute to compare when selecting (must be a STRING at the moment).
     ***************************************************************************************************/
    public ListOfValuesSelectorTool(Class classType, String attribute) {
        this.classType = classType;
        this.attribute = attribute;
    }

    /***************************************************************************************************
     * Assigns the all the selectable data to scan through when finding selected values.
     * <p>
     * @param data All the selectable data.
     ***************************************************************************************************/
    public void setSelectableData(Collection data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        fullData = data;
    }

    /**************************************************************************************************
     * This utility will scan through the collection of data and seek matching objects based on the
     * values passed in (and the attribute set at the class level).
     * <p>
     * @param data A Collection of data objects to search through for matching values on the attribute.
     * @param values A String array of values to match against.
     * <p>
     * @return A Collection of data objects where the attribute matches something in values.
     **************************************************************************************************/
    public Collection getSelectedValues(Collection data, String[] values) throws UIException {
        setSelectableData(data);
        return getSelectedValues(values);
    }

    /**************************************************************************************************
     * This utility will scan through the collection of data and seek matching objects based ont he
     * values passed in (and the attribute set at the class level). Do not forget to assign the
     * selectable data before calling this method.
     * <p>
     * @param values A String array of values to match against.
     * <p>
     * @return A Collection of data objects where the attribute matches something in values.
     **************************************************************************************************/
    public Collection getSelectedValues(String[] values) throws UIException {
        if (classType == null || StringUtility.isNullOrEmpty(attribute)) {
            return new ArrayList<>();
        }
        if (fullData.isEmpty()) {
            return new ArrayList<>();
        }
        if (values == null || values.length == 0) {
            return new ArrayList<>();
        }
        List selectedList = new ArrayList<>();
        String methodName = StringUtility.getMethodName(GET, attribute);
        try {
            Method method = classType.getDeclaredMethod(methodName, (Class[]) null);
            Object object;
            String value;
            for (Iterator iterator = fullData.iterator(); iterator.hasNext();) {
                object = iterator.next();
                value = (String) method.invoke(object, (Object[]) null);
                for (String value2 : values) {
                    if (value != null && value.equalsIgnoreCase(value2)) {
                        selectedList.add(object);
                    }
                }
            }
        } catch (Exception exception) {
            throw buildException(classType, attribute, exception.getClass().getName());
        }
        return selectedList;
    }

    /***************************************************************************************************
     * Builds a fatal exception.
     * <p>
     * @param object The object class name that caused the fatal exception.
     * @param attribute The attribute name on the object that caused the fatal exception.
     * @param exception The inner exception that occurred.
     * <p>
     * @return A UIException containing the fatal exception information.
     ***************************************************************************************************/
    private static UIException buildException(Object object, String attribute, String exception) {
        Object[] values = new Object[] { object.getClass().getName(), attribute, exception };
        return new UIException(ERROR_CODE, values, RErrorSeverity.FATAL);
    }
}
