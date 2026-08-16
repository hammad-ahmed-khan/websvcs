package oracle.retail.sim.client.swing.displayer;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.core.type.Displayable;

/********************************************************************************************************
 * Attempts to get the translated version of the toString() of the object it displayer.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TranslatedObjectDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object value) {
        if (value == null) {
            return StringConstants.EMPTY;
        }
        if (value instanceof Displayable) {
            return Translator.getText(((Displayable) value).toDisplayString());
        }
        return Translator.getText(value.toString());
    }
}
