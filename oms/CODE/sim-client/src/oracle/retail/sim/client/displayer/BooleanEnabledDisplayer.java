package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;

/********************************************************************************************************
 * Displays Enabled/Disables for boolean values.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class BooleanEnabledDisplayer extends BooleanDisplayer {

    public String getDisplayText(Object object) {
        Boolean value = Boolean.FALSE;
        if (object instanceof Boolean) {
            value = (Boolean) object;
        } else if (object instanceof String) {
            value = Boolean.valueOf((String) object);
        }
        if (value) {
            return Translator.getText("Enabled");
        }
        return Translator.getText("Disabled");
    }
}
