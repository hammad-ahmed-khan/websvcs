package oracle.retail.sim.client.application;

import javax.swing.JFrame;

/**
 * Interface defining globar bar functionality.
 */
public interface IStatusBar {

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
    void setStoreNum(String storeNumber);

    /**
     * Displays the current user information
     */
    void setUser(String userId);
}
