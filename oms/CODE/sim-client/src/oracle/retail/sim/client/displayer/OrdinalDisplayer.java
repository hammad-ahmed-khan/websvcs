package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Displays a list of ordinals.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class OrdinalDisplayer extends AbstractDisplayer {

    private String prefix = "ORDINAL_";

    public OrdinalDisplayer() {
    }

    public String getDisplayText(Object value) {
        if (value == null) {
            return StringConstants.EMPTY;
        }
        if (value instanceof Character) {
            Character character = (Character) value;

            if (Character.isWhitespace(character.charValue())) {
                return StringConstants.EMPTY;
            }
        }
        return Translator.getText(prefix + value.toString());
    }
}
