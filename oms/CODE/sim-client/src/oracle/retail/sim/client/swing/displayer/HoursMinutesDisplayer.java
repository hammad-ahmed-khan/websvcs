package oracle.retail.sim.client.swing.displayer;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * This displayer takes a Long object and displays the hours and minutes found within the object in a
 * translated and localized format.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class HoursMinutesDisplayer extends AbstractDisplayer {

    private static final UIMessageText DISPLAY_TEXT = UIMessageText.MESSAGE_HOURS_MINUTES;

    public String getDisplayText(Object object) {
        if (object instanceof Long) {
            long milliseconds = (Long) object;
            long hours = milliseconds / 3600000;
            long minutes = milliseconds % 3600000 / 60000;
            long seconds = milliseconds / 1000 - 3600 * hours - 60 * minutes;
            if (seconds > 30) {
                minutes++;
            }
            return Translator.getMessage(DISPLAY_TEXT.getText(), String.valueOf(hours), String.valueOf(minutes));
        }
        return StringConstants.EMPTY;
    }
}
