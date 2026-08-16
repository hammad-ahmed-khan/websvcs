package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Gets the display value given a country code appropriate for the user's locale.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CountryDisplayer extends AbstractDisplayer {

    /*
     * @see oracle.retail.sim.common.core.type.AbstractDisplayer#getDisplayText(java.lang.Object)
     * value here is expected to be a 2- or 3-letter country code, like US, FR, DE
     */
    public String getDisplayText(Object value) {
        if (value instanceof String) {
            return LocaleManager.getDisplayCountry((String) value, false);
        }
        return StringConstants.EMPTY;
    }
}
