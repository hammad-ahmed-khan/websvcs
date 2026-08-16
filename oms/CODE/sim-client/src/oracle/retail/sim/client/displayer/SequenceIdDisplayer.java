package oracle.retail.sim.client.displayer;

import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Format the stock count line item sequence.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SequenceIdDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object object) {
        if (object instanceof Long) {
            return String.valueOf(object);
        }
        return StringConstants.EMPTY;
    }
}
