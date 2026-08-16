package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;

/********************************************************************************************************
 * Displays Available/Unavailable for boolean values.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class BooleanDirectShipValidateDisplayer extends BooleanDisplayer {

    public BooleanDirectShipValidateDisplayer() {
        setTrueText("Ship Submit");
        setFalseText("Ship Direct");
    }
}
