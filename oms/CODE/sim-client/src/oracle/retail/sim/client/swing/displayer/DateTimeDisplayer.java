package oracle.retail.sim.client.swing.displayer;

import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Displays formatted text for a date object using the current locale's assigned short date and time
 * format.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DateTimeDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object object) {
        if (object == null) {
            return StringConstants.EMPTY;
        }
        return LocaleManager.getShortDateTimeFormatter().format(object);
    }
}
