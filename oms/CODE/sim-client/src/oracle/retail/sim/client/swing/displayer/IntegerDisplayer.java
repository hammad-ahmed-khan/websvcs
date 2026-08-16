package oracle.retail.sim.client.swing.displayer;

import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Displays an integer in the format of the current locale.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class IntegerDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object object) {
        if (object instanceof Integer) {
            return LocaleManager.getIntegerFormatter().format(object);
        }
        return StringConstants.EMPTY;
    }
}
