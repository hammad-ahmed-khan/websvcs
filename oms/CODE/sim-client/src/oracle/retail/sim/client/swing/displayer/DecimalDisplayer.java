package oracle.retail.sim.client.swing.displayer;

import java.text.NumberFormat;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Displays an object as a formatted decimal number of the current locale.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DecimalDisplayer extends AbstractDisplayer {

    private int fraction;

    public DecimalDisplayer(int fraction) {
        this.fraction = fraction;
    }

    public String getDisplayText(Object object) {
        if (object instanceof Number) {
            NumberFormat formatter = LocaleManager.getNumberFormatter();
            formatter.setMaximumFractionDigits(fraction);
            formatter.setMinimumFractionDigits(fraction);
            return formatter.format(object);
        }
        return StringConstants.EMPTY;
    }
}
