package oracle.retail.sim.client.swing.displayer;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.DisplayerUtility;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * This displays a single attribute of an object using reflection.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class AttributeDisplayer extends AbstractDisplayer {

    private String attribute;
    private String emptyReturnText = StringConstants.EMPTY;

    /****************************************************************************************************
     * Constructs a new AttributeDisplayer.
     * @param attribute The attribute to use when creating a formatted string.
     ***************************************************************************************************/
    public AttributeDisplayer(String attribute) {
        this.attribute = attribute;
    }

    /****************************************************************************************************
     * Constructs a new AttributeDisplayer.
     * @param attribute The attribute to use when creating a formatted string.
     ***************************************************************************************************/
    public AttributeDisplayer(String attribute, String emptyReturnText) {
        this.attribute = attribute;
        this.emptyReturnText = emptyReturnText;
    }

    /****************************************************************************************************
     * Formats the object into a string by calling get with the attribute. For example, if the attribute
     * is hello, then it calls getHello() on the passed in parameter.
     * <p>
     * @param object The object to format.
     * @return The formatted string representing the attribute on the object.
     ***************************************************************************************************/
    public String getDisplayText(Object object) {
        if (object != null && attribute != null) {
            try {
                return DisplayerUtility.getDisplayValue(object, attribute, DataTypeConstants.TEXT);
            } catch (Throwable exception) {
                UILog.error(getClass(), UIMessageText.FAILED_ATTRIBUTE_REFLECTION, exception);
            }
        }
        return Translator.getText(emptyReturnText);
    }
}
