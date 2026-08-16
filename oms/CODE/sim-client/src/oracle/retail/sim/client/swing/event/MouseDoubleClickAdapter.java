package oracle.retail.sim.client.swing.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/******************************************************************************************
 * This subclass of the mouse adapter watches for mouse double-click events and calls
 * the appropriate abstract method.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public abstract class MouseDoubleClickAdapter extends MouseAdapter {

    private MouseEvent storeMouseEvent;

    /******************************************************************************************
     * Implements the mouse clicked method to call mouseDoubleClick when an event occurs.
     ******************************************************************************************/
    public void mouseClicked(MouseEvent mouseEvent) {
        if (storeMouseEvent != null) {
            long oldEvent = storeMouseEvent.getWhen();
            long newEvent = mouseEvent.getWhen();
            if (oldEvent == newEvent) {
                return;
            }
        }
        storeMouseEvent = mouseEvent;

        if (mouseEvent.getClickCount() == 2) {
            mouseDoubleClicked(mouseEvent);
        }
    }

    /******************************************************************************************
     * Abstract method that must be defined by all subclasses.
     ******************************************************************************************/
    public abstract void mouseDoubleClicked(MouseEvent event);
}
