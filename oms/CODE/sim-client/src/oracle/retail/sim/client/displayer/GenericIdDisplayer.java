package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Formats the stock event id of the object or return the translation of the word "New" if necessary.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class GenericIdDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object value) {
        if (value == null) {
            return Translator.getText("New");
        }
        return value.toString();
    }
}
