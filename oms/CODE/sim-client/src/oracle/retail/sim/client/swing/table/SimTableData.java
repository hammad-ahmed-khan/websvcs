package oracle.retail.sim.client.swing.table;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.util.UIMessageText;

/********************************************************************************************************
 * The BeanWrapper class may be used to determine the bean-like methods which a specific class exposes.
 * These methods are cached in the constructor call, and may be retrieved through the getReadMethod(),
 * and getWriteMethod() methods.
 * <p>
 * All method information will be cached after the constructor call returns.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimTableData {

    private Map readMethods = new HashMap<>(); // Caches the read methods
    private Map writeMethods = new HashMap<>(); // Caches the write methods
    private Map containedWrappers = new HashMap<>(); // Caches additional wrappers for nested a lookups

    private Class dataClass; // The data class object that this "wraps"
    private Method checkModifiableMethod;
    private static final String CHECK_MODIFIABLE_METHOD_NAME = "isPropertyModifiable";

    private static final String GET = "get";
    private static final String IS = "is";
    private static final String SET = "set";

    private static final String ATTRIBUTE_SEPARATOR = ".";
    private static final String INDEXED_BEGIN = "[";

    /****************************************************************************************************
     * Creates a new data wrapper for the given class. All method information will be cached after the
     * constructor call returns.
     ***************************************************************************************************/
    public SimTableData(Class dataClass) {
        this.dataClass = dataClass;
        initMethodMaps();
    }

    /****************************************************************************************************
     * Initializes the internal method maps by finding all the get(), is() and set() methods in the data
     * class.
     ***************************************************************************************************/
    private void initMethodMaps() {
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

            if (methodName.startsWith(IS)) {
                if (methodName.equals(CHECK_MODIFIABLE_METHOD_NAME) && method.getParameterTypes().length == 1) {
                    checkModifiableMethod = method;
                } else if (method.getParameterTypes().length == 0) {
                    readMethods.put(methodName.substring(2, 3).toLowerCase() + methodName.substring(3), method);
                }
            }

            if (methodName.startsWith(SET) && method.getParameterTypes().length == 1) {
                writeMethods.put(methodName.substring(3, 4).toLowerCase() + methodName.substring(4), method);
            }
        }
    }

    /****************************************************************************************************
     * Gets the wrapper for this class. The wrapper may be this, or it may be another wrapper (if talking
     * about contained properties.) Currently, if the class classType is assignable from the main class
     * (assigned in the constructor,) then this wrapper will be returned - this may not be the desired
     * result (i.e. a bean has a method that returns a subclass of the bean... this method will return
     * the this wrapper - therefore all of the method calls will act as though they were called on the
     * superclass... this should be rare.) Watch for this situation.
     ***************************************************************************************************/
    private SimTableData getTableData(Class classType) {
        if (dataClass.isAssignableFrom(classType)) {
            return this;
        }
        SimTableData tmpTableData = (SimTableData) containedWrappers.get(classType);
        if (tmpTableData == null) {
            tmpTableData = new SimTableData(classType);
            containedWrappers.put(classType, tmpTableData);
        }
        return tmpTableData;
    }

    /****************************************************************************************************
     * Gets the internal data for the named attribute. This method will take care of indexed property
     * calls. It is also assumed that any nested properties have been filtered out previous to this
     * method call (i.e. this method should never be called with a attribute of 'creationDate.minutes.'
     ***************************************************************************************************/
    private Object getDataInternal(Object actor, String attribute) {
        SimTableData tableData = getTableData(actor.getClass());
        try {
            return tableData.getReadMethod(attribute).invoke(actor, (Object[]) null);
        } catch (Throwable exception) {
            String value = actor.getClass().getName() + "." + attribute;
            UILog.error(getClass(), UIMessageText.GET_DATA_INTERNAL_ERROR, value, exception);
        }
        return null;
    }

    /****************************************************************************************************
     * Creates a tokenizer for this attribute.
     ***************************************************************************************************/
    private StringTokenizer getTokenizer(String attribute) {
        return new StringTokenizer(attribute, ATTRIBUTE_SEPARATOR);
    }

    /****************************************************************************************************
     * Gets the data type associated with this wrapper.
     ***************************************************************************************************/
    public Class getDataType() {
        return dataClass;
    }

    /****************************************************************************************************
     * Gets the data type for the given attribute.
     ***************************************************************************************************/
    public Class getDataType(String attribute) {
        try {
            StringTokenizer tokenizer = getTokenizer(attribute);
            Class returnValue = dataClass;
            while (tokenizer.hasMoreTokens()) {
                returnValue = getTableData(returnValue).getReadMethod(tokenizer.nextToken()).getReturnType();
            }
            return returnValue;
        } catch (Exception exception) {
            UILog.error(getClass(), UIMessageText.GET_DATA_TYPE_ERROR, attribute);
            return Object.class;
        }
    }

    /****************************************************************************************************
     * Gets the object data for the given object (assumed to be of the type set up in this constructor.)
     * This method will handle nested and indexed properties.
     ***************************************************************************************************/
    public Object getData(Object actor, String attribute) throws IllegalAccessException, InvocationTargetException {
        StringTokenizer tokenizer = getTokenizer(attribute);
        Object returnValue = actor;
        while (tokenizer.hasMoreTokens()) {
            returnValue = getDataInternal(returnValue, tokenizer.nextToken());

            if (returnValue == null) {
                break;
            }
        }
        return returnValue;
    }

    /****************************************************************************************************
     * Sets the object data for the given object (assumed to be of the type set up in this constructor.)
     * This method will handle nested and indexed properties.
     ***************************************************************************************************/
    public void setData(Object actor, String attribute, Object value) throws IllegalAccessException, InvocationTargetException {
        Object finalActor = actor;
        String currentAttribute = null;
        StringTokenizer tokenizer = getTokenizer(attribute);

        while (tokenizer.hasMoreTokens()) {
            currentAttribute = tokenizer.nextToken();

            if (tokenizer.hasMoreTokens()) {
                finalActor = getDataInternal(finalActor, currentAttribute);
            }
            if (finalActor == null) {
                break;
            }
        }
        if (finalActor != null) {
            SimTableData tableData = getTableData(finalActor.getClass());
            tableData.getWriteMethod(currentAttribute).invoke(finalActor, value);
        }
    }

    /****************************************************************************************************
     * Retrieves the attribute name
     ***************************************************************************************************/
    private String getAttributeName(String attribute) {
        if (attribute.indexOf(INDEXED_BEGIN) > 0) {
            return attribute.substring(0, attribute.indexOf(INDEXED_BEGIN));
        }
        return attribute;
    }

    /****************************************************************************************************
     * Gets a read method for the given property name.
     ***************************************************************************************************/
    public Method getReadMethod(String attribute) {
        return (Method) readMethods.get(getAttributeName(attribute));
    }

    /****************************************************************************************************
     * Gets a write method for the given property name.
     ***************************************************************************************************/
    public Method getWriteMethod(String attribute) {
        return (Method) writeMethods.get(getAttributeName(attribute));
    }

    /****************************************************************************************************
     * Checks if this helper knows about a read method with the given property name.
     ***************************************************************************************************/
    public boolean containsReadMethod(String attribute) {
        return readMethods.containsKey(getAttributeName(attribute));
    }

    /****************************************************************************************************
     * Checks if this helper knows about a write method with the given property name.
     ***************************************************************************************************/
    public boolean containsWriteMethod(String attribute) {
        return writeMethods.containsKey(getAttributeName(attribute));
    }

    /****************************************************************************************************
     * Determines if the given property is modifiable. This will check the special checkMofifiableMethod
     * method if it exists to see if the object allows this property to be edited.
     ***************************************************************************************************/
    public boolean isModifiable(Object actor, String attribute) {
        try {
            if (attribute.indexOf(ATTRIBUTE_SEPARATOR) > 0) {
                int index = attribute.indexOf(ATTRIBUTE_SEPARATOR);
                String nextAttribute = attribute.substring(0, index);
                String remainingAttribute = attribute.substring(index + ATTRIBUTE_SEPARATOR.length());
                Object newActor = getData(actor, nextAttribute);
                if (newActor == null) {
                    return false;
                }
                return getTableData(newActor.getClass()).isModifiable(newActor, remainingAttribute);
            }
            if (containsWriteMethod(attribute)) {
                if (checkModifiableMethod != null) {
                    Object[] params = new Object[] { attribute };
                    return (Boolean) checkModifiableMethod.invoke(actor, params);
                }
                return true;
            } else {
                UILog.debug(getClass(), UIMessageText.UNABLE_TO_FIND_METHOD, attribute);
            }
        } catch (Exception exception) {
            UILog.error(getClass(), UIMessageText.REFLECTION_EXCEPTION, actor + "." + attribute, exception);
        }
        return false;
    }

    /****************************************************************************************************
     * Determines if the given property is modifiable. This will check the special checkMofifiableMethod
     * method if it exists to see if the object allows this property to be edited.
     ***************************************************************************************************/
    public boolean isPropertyModifiable(Object actor, String attribute) {
        try {
            if (attribute.indexOf(ATTRIBUTE_SEPARATOR) > 0) {
                int index = attribute.indexOf(ATTRIBUTE_SEPARATOR);
                String nextAttribute = attribute.substring(0, index);
                String remainingAttribute = attribute.substring(index + ATTRIBUTE_SEPARATOR.length());
                Object newActor = getData(actor, nextAttribute);
                if (newActor == null) {
                    return false;
                }
                return getTableData(newActor.getClass()).isPropertyModifiable(newActor, remainingAttribute);
            }
            if (checkModifiableMethod != null) {
                Object[] params = new Object[] { attribute };
                return (Boolean) checkModifiableMethod.invoke(actor, params);
            }
            return false;
        } catch (Exception exception) {
            UILog.error(getClass(), UIMessageText.REFLECTION_EXCEPTION, actor + "." + attribute, exception);
        }
        return false;
    }

    /**
     * Implementation of ToString() only displays data class of this object.
     */
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("SimTableData [");
        buffer.append(" Data Class=").append(dataClass);
        buffer.append("]");
        return buffer.toString();
    }
}
