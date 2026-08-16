package oracle.retail.sim.client.swing.displayer;

import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Displays the time in the current locale's long-form time format.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TimeLongDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object object) {
        if (object == null) {
            return StringConstants.EMPTY;
        }
        return LocaleManager.getLongTimeFormatter().format(object);
    }
}
