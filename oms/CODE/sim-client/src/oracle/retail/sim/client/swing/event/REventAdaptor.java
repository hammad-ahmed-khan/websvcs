package oracle.retail.sim.client.swing.event;

import java.util.ArrayList;
import java.util.List;

/******************************************************************************************
 * This class is an Event adaptor to simplify the usage of REventListeners and
 * REvents. Other classes should declare and use the adaptor in order to send events
 * (it is simply easier folks ;-)). This class is not thread-safe. If used across
 * multiple threads, first obtain a lock on the object holding the adaptor before using.
 * <p>
 * Currently, an ArrayList is used to store the REventListeners. This is because only
 * a handful of listeners are usually assigned to any one thing. In the future, if many
 * REventListeners are being assigned to the same component, then the ArrayList should
 * be converted to a plain array for performance reasons.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class REventAdaptor {
    private List listeners = new ArrayList(3);

    /******************************************************************************************
     * Returns new REventAdaptor object.
     ******************************************************************************************/
    public REventAdaptor() {
    }

    /******************************************************************************************
     * Adds a REventListener to the REventListener list. This method does not allow duplicates.
     * <p>
     *@param listener The REventListener to add.
     ******************************************************************************************/
    public void addREventListener(REventListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    /******************************************************************************************
     * Removes a REventListener from the REventListener list.
     * <p>
     *@param listener The REventListener to add.
     ******************************************************************************************/
    public void removeREventListener(REventListener listener) {
        listeners.remove(listener);
    }

    /******************************************************************************************
     * Removes all REventListeners from the REventListener list.
     ******************************************************************************************/
    public void removeAllREventListeners() {
        listeners.clear();
    }

    /******************************************************************************************
     * Notifies all registered REventListeners that a RActionEvent has occured.
     * <p>
     *@param event The RActionEvent to notify Rcom Event Listeners with.
     ******************************************************************************************/
    public void notifyREventListeners(RActionEvent event) {
        for (Object listener : listeners) {
            ((REventListener) listener).performActionEvent(event);
        }
    }

    /******************************************************************************************
     * Notifies all registered REventListeners that a RErrorEvent has occured.
     * <p>
     *@param event The RErrorEvent to notify REventListeners with.
     ******************************************************************************************/
    public void notifyREventListeners(RErrorEvent event) {
        for (Object listener : listeners) {
            ((REventListener) listener).performErrorEvent(event);
        }
    }
}
