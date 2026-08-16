package oracle.retail.sim.client.application;

import javax.swing.JFrame;

/**
 * Interface defining globar bar functionality.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public interface StatusBarInterface {

    /**
     * Notifies this object that the parent frame is in existance.
     */
    void setParentFrame(JFrame frame);

    /**
     * Sets the screen name
     */
    void setScreenName(String screenName);

    /**
     * Sets the store number
     */
    void setStoreInfo(String storeText);

    /**
     * Displays the current user information
     */
    void setUser(String userId);

    /**
     * Process a hot key that has been pressed
     */
    void doHotKeyPressed(int keycode);
}
