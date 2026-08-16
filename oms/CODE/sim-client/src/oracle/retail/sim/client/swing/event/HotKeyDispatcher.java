package oracle.retail.sim.client.swing.event;

/******************************************************************************************
 * This interface must be implemented by objects interested in receiving FUNCTION KEY
 * events from the HotKeyDispatcher.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public interface HotKeyDispatcher {
    boolean hotKeyPressed(int keyCode);
}
