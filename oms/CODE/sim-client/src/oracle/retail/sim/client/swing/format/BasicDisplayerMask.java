package oracle.retail.sim.client.swing.format;

import oracle.retail.sim.common.core.type.BasicDisplayer;
import oracle.retail.sim.common.format.MaskAdaptor;

/******************************************************************************************
 * This mask uses a basic displayer to display the data passed into the formatData() or
 * format() method.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class BasicDisplayerMask extends MaskAdaptor {

    private BasicDisplayer displayer;

    public BasicDisplayerMask(BasicDisplayer displayer) {
        this.displayer = displayer;
    }

    public String formatData(Object object) {
        if (displayer != null) {
            return displayer.getDisplayText(object);
        }
        return super.formatData(object);
    }
}
