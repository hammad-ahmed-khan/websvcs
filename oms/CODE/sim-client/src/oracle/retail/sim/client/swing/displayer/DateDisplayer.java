package oracle.retail.sim.client.swing.displayer;

import java.text.DateFormat;
import java.util.TimeZone;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Displays formatted text for a date object using the current locale's assigned short date format.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DateDisplayer extends AbstractDisplayer {

    private TimeZone timezone;

    /**
     * Default constructor will use the timezone of the store currently logged into.
     */
    public DateDisplayer() {
    }

    /**
     * Overrides the default timezone to use when formatting the data.
     */
    public DateDisplayer(TimeZone timezone) {
        this.timezone = timezone;
    }

    public String getDisplayText(Object object) {
        if (object == null) {
            return StringConstants.EMPTY;
        }
        DateFormat formatter = LocaleManager.getShortDateFormatter();
        if (timezone != null) {
            formatter.setTimeZone(timezone);
        }
        return formatter.format(object);
    }
}
