package oracle.retail.sim.client.swing.displayer;

import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Displays the object by calling toString() on the object. See DefaultDisplayer for an expanded version
 * of this displayer.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ObjectDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object object) {
        if (object == null) {
            return StringConstants.EMPTY;
        }
        return object.toString();
    }
}
