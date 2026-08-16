package oracle.retail.sim.client.screen.uin;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Displayer for UIN Resolution combo box.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UINResolutionBooleanDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object object) {
        if (object == null) {
            return StringConstants.EMPTY;
        }
        if ((Boolean) object) {
            return Translator.getText("Resolved");
        }
        return Translator.getText("Not Resolved");
    }

    public String getDisplayText(boolean value) {
        if (value) {
            return Translator.getText("Resolved");
        }
        return Translator.getText("Not Resolved");
    }
}
