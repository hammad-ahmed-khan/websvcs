package oracle.retail.sim.client.swing.event;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/******************************************************************************************
 * An enter key adapter that listens for the enter key and calls a method.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public abstract class EnterKeyAdapter extends KeyAdapter {

    /******************************************************************************************
     * Implements the key listener interface "key pressed" method. The method tracks whether
     * or not the key pressed was the ENTER key and triggers the enterKeyPressed() method if
     * it was.
     * <p>
     *@param event Details about the key event that occurred.
     *****************************************************************************************/
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            event.consume();
            enterKeyPressed(event);
        }
    }

    /******************************************************************************************
     * Method that is called when the enter key is pressed. Must be implemented by sub-classes.
     *****************************************************************************************/
    public abstract void enterKeyPressed(KeyEvent event);
}
