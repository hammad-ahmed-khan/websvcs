package oracle.retail.sim.client.swing.displayer;

import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Formats an object that can be a string or a number into a formatted text ID of the object.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class NumericIdDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object object) {
        if (object == null) {
            return StringConstants.EMPTY;
        }
        if (object instanceof String) {
            return (String) object;
        }
        if (object instanceof Number) {
            return LocaleManager.getIntegerFormatter().format(object);
        }
        return StringConstants.EMPTY;
    }
}
