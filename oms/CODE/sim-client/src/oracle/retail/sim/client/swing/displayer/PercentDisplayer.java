package oracle.retail.sim.client.swing.displayer;

import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Displays a number in the format of the current locale.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class PercentDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object object) {
        if (object instanceof Number) {
            return LocaleManager.getPercentFormatter().format(object);
        }
        if (object instanceof Quantity) {
            Quantity quantity = (Quantity) object;
            return LocaleManager.getPercentFormatter().format(quantity.doubleValue());
        }
        return StringConstants.EMPTY;
    }
}
