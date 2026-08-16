package oracle.retail.sim.client.swing.event;

/******************************************************************************************
 * This interface must be implemented by objects that wish to receive generic swing
 * framework event actions. RFrame, RDialog, RScreen and RTab contain empty implementations
 * of these listener methods for convenience.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public interface REventListener {
    void performActionEvent(RActionEvent event);

    void performErrorEvent(RErrorEvent event);
}
