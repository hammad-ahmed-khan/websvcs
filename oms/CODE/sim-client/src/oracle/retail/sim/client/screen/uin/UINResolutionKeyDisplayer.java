package oracle.retail.sim.client.screen.uin;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.BasicDisplayer;
import oracle.retail.sim.common.uin.UINKey;

/********************************************************************************************************
 * Displays the number of UINs found on its model. The model instanceof if logic should be in the order
 * of the most frequently encountered.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UINResolutionKeyDisplayer implements BasicDisplayer {

    public String getDisplayText(Object value, Object model) {
        if (value != null) {
            String text = value.toString();
            if (UINKey.NOT_APPLICABLE.equals(text)) {
                return Translator.getText(text);
            }
            return text;
        }
        return StringConstants.EMPTY;
    }

    public String getDisplayText(Object value) {
        return getDisplayText(value, null);
    }
}
