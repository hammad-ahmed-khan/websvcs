package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.DisplayerUtility;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * This displayer will use reflection to format a data object into a string equivalent to "attribute 1 -
 * attribute 2".
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TranslatedDualAttributeDisplayer extends AbstractDisplayer {
    private String attributeOne;
    private String attributeTwo;
    private String separator;

    /****************************************************************************************************
     * Constructs a Dual Attribute Displayer
     * <p>
     * @param attributeOne This first of the two attributes to display.
     * @param attributeTwo The second of the two attributes to displayer.
     ***************************************************************************************************/
    public TranslatedDualAttributeDisplayer(String attributeOne, String attributeTwo) {
        this(attributeOne, attributeTwo, " - ");
    }

    public TranslatedDualAttributeDisplayer(String attributeOne, String attributeTwo, String separator) {
        this.attributeOne = attributeOne;
        this.attributeTwo = attributeTwo;
        this.separator = separator != null ? separator : StringConstants.SPACE;
    }

    /****************************************************************************************************
     * Formats the data object by calling get on attribute one and attribute two. For example, if the id
     * attribute is "myid" and the description attribute is "mydescription", then this mask will format
     * by attempting to call getMyid() and getMydescription() on the object.
     ***************************************************************************************************/
    public String getDisplayText(Object object) {
        StringBuilder buffer = new StringBuilder();
        try {
            buffer.append(DisplayerUtility.getDisplayValue(object, attributeOne, DataTypeConstants.TEXT));
            if (buffer.length() > 0) {
                buffer.append(separator);
            }
            buffer.append(DisplayerUtility.getDisplayValue(object, attributeTwo, DataTypeConstants.TEXT));
        } catch (UIException e) {
            buffer = new StringBuilder();
            if (object != null) {
                buffer.append(object);
            }
        }
        return Translator.getText(buffer.toString());
    }
}
