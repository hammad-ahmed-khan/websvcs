package oracle.retail.sim.client.swing.displayer;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * This displayer takes a Long object and displays the hours and minutes found within the object in a
 * translated and localized format.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DurationDisplayer extends AbstractDisplayer {

    public static final long ONE_SECOND = 1000;
    public static final long SECONDS = 60;
    public static final long ONE_MINUTE = ONE_SECOND * 60;
    public static final long MINUTES = 60;
    public static final long ONE_HOUR = ONE_MINUTE * 60;
    public static final long HOURS = 24;
    public static final long ONE_DAY = ONE_HOUR * 24;

    public String getDisplayText(Object object) {
        if (object instanceof Long) {
            long duration = (Long) object;
            if (duration > 0L) {
                return formatDuration(duration);
            } else if (duration == -1L) {
                return Translator.getMessage("Never");
            } else {
                // This happens when server/client clock is a bit off (can be slightly negative)
                return formatDuration(0L);
            }
        }
        return Translator.getMessage("Unknown");
    }

    /*
     * Converts time (in milliseconds) to human-readable format
     * <w> days, <x> hours, <y> minutes and (z) seconds
    */

    private String formatDuration(long duration) {

        StringBuilder res = new StringBuilder();
        long temp = 0;

        if (duration < 0L) {
            return "Warning: Future Date Found!";
        }

        if (duration >= ONE_SECOND) {

            temp = duration / ONE_DAY;
            if (temp > 0) {
                String dayStr = temp > 1 ? Translator.getMessage("days") : Translator.getMessage("day");
                duration -= temp * ONE_DAY;
                res.append(temp).append(" ").append(dayStr).append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_HOUR;
            if (temp > 0) {
                duration -= temp * ONE_HOUR;
                String hourStr = temp > 1 ? Translator.getMessage("hours") : Translator.getMessage("hour");
                res.append(temp).append(" ").append(hourStr).append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_MINUTE;
            if (temp > 0) {
                duration -= temp * ONE_MINUTE;
                String minuteStr = temp > 1 ? Translator.getMessage("minutes") : Translator.getMessage("minute");
                res.append(temp).append(" ").append(minuteStr);
            }

            if (res.length() > 0 && duration >= ONE_SECOND) {
                res.append(" ").append(Translator.getMessage("and")).append(" ");
            }

            temp = duration / ONE_SECOND;
            if (temp > 0) {
                String secondStr = temp > 1 ? Translator.getMessage("seconds") : Translator.getMessage("second");
                res.append(temp).append(" ").append(secondStr);
            }

            return res.toString();
        } else {
            return "0 " + Translator.getMessage("second");
        }
    }
}
