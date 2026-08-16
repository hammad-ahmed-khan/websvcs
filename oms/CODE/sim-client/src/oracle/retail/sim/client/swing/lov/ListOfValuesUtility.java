package oracle.retail.sim.client.swing.lov;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIMessageText;

/********************************************************************************************************
 * A utility class for using reflection to perform certain standard list of values functionality.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ListOfValuesUtility {

    private static final UIMessageText ERROR_CODE = UIMessageText.DATA_TYPE_ATTRIBUTE_MISMATCH;
    private static final String GET_SELECTABLE = "getSelectable";
    private static final String GET_SELECTED = "getSelected";
    private static final String SET_SELECTED = "setSelected";

    /****************************************************************************************************
     * Static class constructor.
     ***************************************************************************************************/
    private ListOfValuesUtility() {
    }

    /****************************************************************************************************
     * Retrieves the selectable values for the object and an attribute name.
     * <p>
     * @param screenModel The object that has the retrieval method on it (often a screen model).
     * @param attributeName The attribute name.
     * @param criteria The SelectableCriteria that is a a parameter to the method.
     * @return The SelectableResults object that represents the outcome of the search.
     ***************************************************************************************************/
    public static SelectableResults getSelectableValues(Object screenModel, String attributeName, SelectableCriteria criteria) throws UIException {
        if (screenModel == null) {
            return new SelectableResults();
        }
        if (StringUtility.isNullOrEmpty(attributeName)) {
            return new SelectableResults();
        }
        String methodName = StringUtility.getMethodName(GET_SELECTABLE, attributeName);
        try {
            Class[] parameterTypeArray = new Class[1];
            parameterTypeArray[0] = SelectableCriteria.class;

            Object[] parameterArray = new Object[1];
            parameterArray[0] = criteria;

            Method method = screenModel.getClass().getDeclaredMethod(methodName, parameterTypeArray);
            return (SelectableResults) method.invoke(screenModel, parameterArray);
        } catch (InvocationTargetException exception) {
            throw buildException(exception, methodName);
        } catch (Exception exception) {
            throw buildException(screenModel, attributeName, exception.getClass().getName());
        }
    }

    /****************************************************************************************************
     * Retrieves all the selectable values by calling a method on the object for the attribute name.
     * <p>
     * @param screenModel The object that has the retrieval method on it (often a screen model).
     * @param attributeName The attribute name.
     * @return All the selectable data objects.
     ***************************************************************************************************/
    public static Collection getSelectableValues(Object screenModel, String attributeName) throws UIException {
        if (screenModel == null) {
            return new ArrayList<>();
        }
        if (StringUtility.isNullOrEmpty(attributeName)) {
            return new ArrayList<>();
        }
        String methodName = StringUtility.getMethodName(GET_SELECTABLE, attributeName);
        try {
            Method method = screenModel.getClass().getDeclaredMethod(methodName, (Class[]) null);
            return (Collection) method.invoke(screenModel, (Object[]) null);
        } catch (InvocationTargetException exception) {
            throw buildException(exception, methodName);
        } catch (Exception exception) {
            throw buildException(screenModel, attributeName, exception.getClass().getName());
        }
    }

    /****************************************************************************************************
     * Retrieves all the selected values for the array of parameters using the attribute name for
     * comparison.
     * <p>
     * @param screenModel The object that has the retrieval method on it (often a screen model).
     * @param attributeName The attribute name (ie. screenModel.getSelectedAttributename()).
     * @param values An array of matchable strings.
     ***************************************************************************************************/
    public static Collection getSelectedValues(Object screenModel, String attributeName, String[] values) throws UIException {
        if (screenModel == null || StringUtility.isNullOrEmpty(attributeName)) {
            return new ArrayList<>();
        }
        String methodName = StringUtility.getMethodName(GET_SELECTED, attributeName);
        try {
            Class[] parameterTypeArray = new Class[1];
            parameterTypeArray[0] = String[].class;

            Object[] parameterArray = new Object[1];
            parameterArray[0] = values;

            Method method = screenModel.getClass().getDeclaredMethod(methodName, parameterTypeArray);
            return (Collection) method.invoke(screenModel, parameterArray);
        } catch (InvocationTargetException exception) {
            throw buildException(exception, methodName);
        } catch (Exception exception) {
            throw buildException(screenModel, attributeName, exception.getClass().getName());
        }
    }

    /****************************************************************************************************
     * Assigns the selected values for a particular object and attribute name.
     * <p>
     * @param object The object that has the retrieval method on it (often a screen model).
     * @param attributeName The attribute name.
     *            <p>
     * @return A collection of the selectable values.
     ***************************************************************************************************/
    public static void setSelectedValues(Object object, String attributeName, Collection collection) throws UIException {
        if (object == null || StringUtility.isNullOrEmpty(attributeName)) {
            return;
        }
        String methodName = StringUtility.getMethodName(SET_SELECTED, attributeName);
        try {
            Class[] parameterTypeArray = new Class[1];
            parameterTypeArray[0] = Collection.class;

            Object[] parameterArray = new Object[1];
            parameterArray[0] = collection;

            Method method = object.getClass().getDeclaredMethod(methodName, parameterTypeArray);
            method.invoke(object, parameterArray);
        } catch (InvocationTargetException exception) {
            throw buildException(exception, methodName);
        } catch (Exception exception) {
            throw buildException(object, attributeName, exception.getClass().getName());
        }
    }

    /****************************************************************************************************
     * Builds a fatal exception.
     * <p>
     * @param exception The exception that occurred.
     *            <p>
     * @return A UIException containing the exception information.
     ***************************************************************************************************/
    private static UIException buildException(InvocationTargetException exception, String methodName) {
        Throwable causeException = exception.getCause();
        if (causeException instanceof UIException) {
            return (UIException) causeException;
        }
        return new UIException(UIMessageText.REFLECTION_EXCEPTION, methodName);
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
        return new UIException(ERROR_CODE, values, RErrorSeverity.FATAL);
    }
}
