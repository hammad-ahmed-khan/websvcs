package oracle.retail.sim.client.swing.displayer;

import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.core.type.Displayable;

/********************************************************************************************************
 * This class represents a default displayer that uses the Displayable interface or toString() to get
 * the value from an object.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DefaultDisplayer extends AbstractDisplayer {
    public String getDisplayText(Object object) {
        if (object instanceof Displayable) {
            return ((Displayable) object).toDisplayString();
        } else if (object != null) {
            return object.toString();
        }
        return StringConstants.EMPTY;
    }
}
