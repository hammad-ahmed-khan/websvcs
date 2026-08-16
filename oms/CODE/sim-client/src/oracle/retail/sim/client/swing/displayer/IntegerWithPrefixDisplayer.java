package oracle.retail.sim.client.swing.displayer;

import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Displays an integer in the format of the current locale.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class IntegerWithPrefixDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object object) {
        if (object instanceof Quantity) {
            Quantity quantity = (Quantity) object;
            return formatInteger(quantity.intValue());
        }
        if (object instanceof Integer) {
            return formatInteger((Integer) object);
        }
        return StringConstants.EMPTY;
    }

    private String formatInteger(Integer value) {
        if (value < 0) {
            return LocaleManager.getIntegerFormatter().format(value);
        }
        return "+" + LocaleManager.getIntegerFormatter().format(value);
    }
}
