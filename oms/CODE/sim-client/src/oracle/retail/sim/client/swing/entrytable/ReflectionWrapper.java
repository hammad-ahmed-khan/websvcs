package oracle.retail.sim.client.swing.entrytable;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIMessageText;

/********************************************************************************************************
 * ReflectionWrapper
 * <p>
 * This class wraps an object and uses reflection to help get and set values on the data object. This is
 * primarily used in tables.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ReflectionWrapper {

    private static final String GET = "get";
    private static final String IS = "is";
    private static final String SET = "set";
    private static final String INDEXED_BEGIN = "[";

    private Class dataClass;
    private Map readMethods = new HashMap<>();
    private Map writeMethods = new HashMap<>();

    /****************************************************************************************************
     * Constructor - Assumes default data type of Object.
     ***************************************************************************************************/
    public ReflectionWrapper() {
        this(Object.class);
    }

    /****************************************************************************************************
     * Constructor
     * <p>
     * @param The Class of object that this wrapper will process.
     ***************************************************************************************************/
    public ReflectionWrapper(Class dataClass) {
        this.dataClass = dataClass;
        inititalizeMethods();
    }

    /****************************************************************************************************
     * This method caches all the read and write method (get, set, is) on the data type assigned to the
     * wrapper.
     ***************************************************************************************************/
    private void inititalizeMethods() {
        readMethods.clear();
        writeMethods.clear();

        Method method = null;
        String methodName = null;

        Method[] allMethods = dataClass.getMethods();

        for (Method allMethod : allMethods) {
            method = allMethod;
            methodName = method.getName();

            if (methodName.startsWith(GET) && method.getParameterTypes().length == 0) {
                readMethods.put(methodName.substring(3, 4).toLowerCase() + methodName.substring(4), method);
            }
            if (methodName.startsWith(IS) && method.getParameterTypes().length == 0) {
                readMethods.put(methodName.substring(2, 3).toLowerCase() + methodName.substring(3), method);
            }
            if (methodName.startsWith(SET) && method.getParameterTypes().length == 1) {
                writeMethods.put(methodName.substring(3, 4).toLowerCase() + methodName.substring(4), method);
            }
        }
    }

    /****************************************************************************************************
     * Retrieves the data class this wrapper processes.
     * <p>
     * @return A Class object.
     ***************************************************************************************************/
    protected Class getDataClass() {
        return dataClass;
    }

    /****************************************************************************************************
     * Retrieves an array of data objects for the given parent object and an array of attributes. Each
     * attribute in the array is "read" from the parent object (ex: object = a, attribute = b, then
     * a.getB()).
     * <p>
     * @param object The object to access the attributes from.
     * @param attributes An array of attribute name to retrieve from the object.
     * @return An array of objects representing the attributes on the data object.
     ***************************************************************************************************/
    public Object[] getRowValues(Object object, String[] attributes) {
        Object[] values = new Object[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            values[i] = getValue(object, attributes[i]);
        }
        return values;
    }

    /****************************************************************************************************
     * Retrieves a value from the data object passed in based on the attribute (ex: object = a, attribute =
     * b, then a.getB()).
     * <p>
     * @param object The data object.
     * @param attribute The attribute to read from the data object.
     * @return The value of the attribute on the data object.
     ***************************************************************************************************/
    public Object getValue(Object object, String attribute) {
        try {
            return getReadMethod(attribute).invoke(object, (Object[]) null);
        } catch (Throwable exception) {
            String inputValue = dataClass.toString() + "." + attribute;
            UILog.error(getClass(), UIMessageText.REFLECTION_EXCEPTION, inputValue, exception);
        }
        return null;
    }

    /****************************************************************************************************
     * Assigns a value to the object based on the attribute (ex. if object = a, attribute = b, and value =
     * e, then a.setB(c).
     * <p>
     * @param object The data object.
     * @param attribute The attribute to assign to the data object.
     * @param value The value of the assigned attribute.
     ***************************************************************************************************/
    public void setValue(Object object, String attribute, Object value) throws UIException {
        Object[] parameters = new Object[1];
        parameters[0] = value;

        Method method = getWriteMethod(attribute);
        try {
            method.invoke(object, parameters);
        } catch (Throwable exception) {
            if (exception instanceof UIException) {
                throw (UIException) exception;
            }
            String inputValue = dataClass.toString() + "." + attribute;
            UILog.error(getClass(), UIMessageText.REFLECTION_EXCEPTION, inputValue, exception);
        }
    }

    /****************************************************************************************************
     * Gets a read method for the given attribute (if attribute = a, then either isA() or getA()
     * <p>
     * @param attribute The attribute to find a read method for.
     * @param The Method object found on the parent class.
     ***************************************************************************************************/
    public Method getReadMethod(String attribute) {
        return (Method) readMethods.get(getAttributeName(attribute));
    }

    /****************************************************************************************************
     * Gets a write method for the given attribute (if attribute = a, then setA()
     * <p>
     * @param attribute The attribute to find a write method for.
     * @param The Method object found on the parent class.
     ***************************************************************************************************/
    public Method getWriteMethod(String attribute) {
        return (Method) writeMethods.get(getAttributeName(attribute));
    }

    /****************************************************************************************************
     * Process the attribute name to ensure it is correctly formatted. (????)
     ***************************************************************************************************/
    private String getAttributeName(String attribute) {
        if (attribute.indexOf(INDEXED_BEGIN) > 0) {
            return attribute.substring(0, attribute.indexOf(INDEXED_BEGIN));
        }
        return attribute;
    }
}
