package oracle.retail.sim.client.swing.displayer;

import java.util.Date;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Displays formatted text for a date object using the current locale's assigned full date format.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class MediumDateTimeDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object object) {
        if (object instanceof Date) {
            return LocaleManager.getMediumDateTimeFormatter().format(object);
        }
        return StringConstants.EMPTY;
    }
}
