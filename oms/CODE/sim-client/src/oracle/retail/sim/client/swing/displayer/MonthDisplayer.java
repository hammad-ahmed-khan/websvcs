package oracle.retail.sim.client.swing.displayer;

import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * For a given integer value matching the MONTH value of the Calendar class, this will format into a
 * month name text string.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class MonthDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object value) {
        if (value instanceof Integer) {
            Integer day = (Integer) value;
            String[] months = LocaleManager.getDateFormatSymbols().getMonths();
            return months[day];
        }
        return StringConstants.EMPTY;
    }
}
