package oracle.retail.sim.client.swing.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.Displayable;
import oracle.retail.sim.common.core.type.Displayer;

/********************************************************************************************************
 * A utility class for using reflection to perform certain access to objects.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DisplayerUtility {

    private static Map displayerMap = new HashMap<>(7);

    private static final String GET = "get";

    /****************************************************************************************************
     * Static class constructor.
     ***************************************************************************************************/
    private DisplayerUtility() {
    }

    /****************************************************************************************************
     * Assigns a displayer to use when formatting a particular type of data.
     * <p>
     * @param dataType The data type to use the displayer for.
     * @param displayer The displayer
     ***************************************************************************************************/
    public static void installDisplayer(int dataType, Displayer displayer) {
        displayerMap.put(dataType, displayer);
    }

    /****************************************************************************************************
     * Assigns a displayer to use when formatting a particular type of data.
     * <p>
     * @param classType The class type to use the displayer for.
     * @param displayer The displayer
     ***************************************************************************************************/
    public static void installDisplayer(Class classType, Displayer displayer) {
        displayerMap.put(classType, displayer);
    }

    /****************************************************************************************************
     * Retrieves the actual object value based on the attribute name.
     * <p>
     * @param object The object.
     * @param attributeName The attribute name.
     *            <p>
     * @return The display value of the attribute name within the object.
     ***************************************************************************************************/
    public static Object getValue(Object object, String attribute) throws UIException {
        if (object == null) {
            return null;
        }
        if (StringUtility.isNullOrEmpty(attribute)) {
            return null;
        }
        String methodName = StringUtility.getMethodName(GET, attribute);
        try {
            Method method = object.getClass().getMethod(methodName, (Class[]) null);
            return method.invoke(object, (Object[]) null);
        } catch (InvocationTargetException exception) {
            throw buildException(exception, methodName);
        } catch (Throwable exception) {
            throw buildException(object, attribute, exception.getClass().getName());
        }
    }

    /****************************************************************************************************
     * Retrieves the string display value from an object based on an attribute name. In other words, it
     * calls getAttributeName() on the object.
     * <p>
     * @param object The object.
     * @param attributeName The attribute name.
     * @param dataType The type of data to display.
     *            <p>
     * @return The display value of the attribute name within the object.
     ***************************************************************************************************/
    public static String getDisplayValue(Object object, String attributeName, int dataType) throws UIException {
        if (object == null) {
            return StringConstants.EMPTY;
        }
        if (StringUtility.isNullOrEmpty(attributeName)) {
            if (object instanceof Displayable) {
                return ((Displayable) object).toDisplayString();
            }
            return object.toString();
        }
        String methodName = StringUtility.getMethodName(GET, attributeName);
        try {
            Method method = object.getClass().getMethod(methodName, (Class[]) null);
            Object value = method.invoke(object, (Object[]) null);

            if (value == null) {
                return StringConstants.EMPTY;
            }
            if (value instanceof String) {
                return value.toString();
            }

            Displayer classTypeDisplayer = (Displayer) displayerMap.get(object.getClass());
            if (classTypeDisplayer != null) {
                return classTypeDisplayer.getDisplayText(value, null);
            }

            Displayer dataTypeDisplayer = (Displayer) displayerMap.get(dataType);
            if (dataTypeDisplayer != null) {
                return dataTypeDisplayer.getDisplayText(value, null);
            }

            switch (dataType) {
                case DataTypeConstants.TEXT:
                case DataTypeConstants.TEXT_FULL:
                    return value.toString();
                case DataTypeConstants.INTEGER:
                case DataTypeConstants.INTEGER_LEFT:
                case DataTypeConstants.INTEGER_RIGHT:
                    return LocaleManager.getIntegerFormatter().format(value);
                case DataTypeConstants.DECIMAL:
                case DataTypeConstants.DECIMAL_LEFT:
                case DataTypeConstants.DECIMAL_RIGHT:
                    return LocaleManager.getNumberFormatter().format(value);
                case DataTypeConstants.CURRENCY:
                case DataTypeConstants.CURRENCY_LEFT:
                case DataTypeConstants.CURRENCY_RIGHT:
                    throw new IllegalArgumentException();
                case DataTypeConstants.DATE:
                case DataTypeConstants.DATE_SHORT:
                    return LocaleManager.getShortDateFormatter().format(value);
                case DataTypeConstants.DATE_MEDIUM:
                    return LocaleManager.getMediumDateFormatter().format(value);
                case DataTypeConstants.DATE_LONG:
                    return LocaleManager.getLongDateFormatter().format(value);
                case DataTypeConstants.DATE_FULL:
                    return LocaleManager.getFullDateFormatter().format(value);
                default:
                    return value.toString();
            }
        } catch (InvocationTargetException exception) {
            throw buildException(exception, methodName);
        } catch (Throwable exception) {
            throw buildException(object, attributeName, exception.getClass().getName());
        }
    }

    /****************************************************************************************************
     * Retrieves the RDisplayTableCell type value for a specific attributeName within a class.
     * <p>
     * @param attributeclass The class containing the attribute name.
     * @param attributeName The attribute name.
     *            <p>
     * @return The RDisplayTableCell type for the specific attribute. TEXT by default.
     ***************************************************************************************************/
    public static int getDisplayTableCellType(Class attributeClass, String attributeName) throws UIException {
        if (attributeClass == null) {
            return DataTypeConstants.TEXT;
        }
        if (StringUtility.isNullOrEmpty(attributeName)) {
            return DataTypeConstants.TEXT;
        }
        try {
            String methodName = StringUtility.getMethodName(GET, attributeName);
            Method method = attributeClass.getDeclaredMethod(methodName, (Class[]) null);
            Class returnClass = method.getReturnType();

            if (returnClass.isPrimitive()) {
                if (returnClass == Boolean.TYPE) {
                    return DataTypeConstants.BOOLEAN;
                } else if (returnClass == Integer.TYPE) {
                    return DataTypeConstants.INTEGER;
                } else if (returnClass == Double.TYPE) {
                    return DataTypeConstants.DECIMAL;
                } else if (returnClass == Float.TYPE) {
                    return DataTypeConstants.DECIMAL;
                } else if (returnClass == Short.TYPE) {
                    return DataTypeConstants.INTEGER;
                } else if (returnClass == Long.TYPE) {
                    return DataTypeConstants.INTEGER;
                }
            }

            if (returnClass == Date.class) {
                return DataTypeConstants.DATE;
            }
        } catch (Exception exception) {
            throw buildException(attributeClass, attributeName, exception.getClass().getName());
        }
        return DataTypeConstants.TEXT;
    }

    /****************************************************************************************************
     * Builds a fatal exception.
     * <p>
     * @param exception The exception that occurred.
     * <p>
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
        return new UIException(UIMessageText.DATA_TYPE_ATTRIBUTE_MISMATCH, values, RErrorSeverity.FATAL);
    }
}
