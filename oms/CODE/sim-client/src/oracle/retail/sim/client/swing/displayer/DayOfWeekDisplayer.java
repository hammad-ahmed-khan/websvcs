package oracle.retail.sim.client.swing.displayer;

import java.util.Calendar;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * For an Integer value matching day of week within the Calendar class, this displayer will display the
 * full name of the day of the week. This will be translated to the current locale.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DayOfWeekDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object value) {
        if (value instanceof Integer) {
            Integer day = (Integer) value;
            switch (day) {
                case Calendar.SUNDAY:
                    return Translator.getText("Sunday");
                case Calendar.MONDAY:
                    return Translator.getText("Monday");
                case Calendar.TUESDAY:
                    return Translator.getText("Tuesday");
                case Calendar.WEDNESDAY:
                    return Translator.getText("Wednesday");
                case Calendar.THURSDAY:
                    return Translator.getText("Thursday");
                case Calendar.FRIDAY:
                    return Translator.getText("Friday");
                case Calendar.SATURDAY:
                    return Translator.getText("Saturday");
                default:
                    return StringConstants.EMPTY;
            }
        }
        return StringConstants.EMPTY;
    }
}
