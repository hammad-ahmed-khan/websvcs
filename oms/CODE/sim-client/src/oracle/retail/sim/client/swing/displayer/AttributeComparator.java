package oracle.retail.sim.client.swing.displayer;

import java.util.Comparator;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.DisplayerUtility;
import oracle.retail.sim.client.swing.util.UIMessageText;

/********************************************************************************************************
 * This comparators sorts data by using the attribute assigned and reflection to get a display value from
 * the objects. It then sorts by the display value. Note that this comparator is not case sensitive.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class AttributeComparator implements Comparator {

    private String attribute;

    public AttributeComparator(String attribute) {
        this.attribute = attribute;
    }

    public int compare(Object o1, Object o2) {
        try {
            Object value1 = DisplayerUtility.getValue(o1, attribute);
            Object value2 = DisplayerUtility.getValue(o2, attribute);

            if (value1 == null && value2 == null) {
                return 0;
            }
            if (value1 == null) {
                return -1;
            }
            if (value2 == null) {
                return 1;
            }

            if (!(value1 instanceof String) && value1 instanceof Comparable) {
                return ((Comparable) value1).compareTo(value2);
            }

            String text1 = DisplayerUtility.getDisplayValue(o1, attribute, DataTypeConstants.TEXT);
            String text2 = DisplayerUtility.getDisplayValue(o2, attribute, DataTypeConstants.TEXT);

            return StringUtility.compareToIgnoreCase(text1, text2);
        } catch (Throwable exception) {
            UILog.debug(getClass(), UIMessageText.COMPARATOR_FAILURE, exception);
            return 0;
        }
    }
}
