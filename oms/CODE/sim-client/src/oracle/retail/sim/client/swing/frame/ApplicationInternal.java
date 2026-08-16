package oracle.retail.sim.client.swing.frame;

import javax.swing.JFrame;
import oracle.retail.sim.client.swing.navigation.SecurityInterface;
import oracle.retail.sim.common.config.NavigationTaskItemData;

/******************************************************************************************
 * This static class needs to hold references to all globally accessible values by the
 * framework such as the background application frame, the security manager, and the error
 * manager.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class ApplicationInternal {

    private static ApplicationFrame applicationFrame;
    private static JFrame backgroundFrame;

    /******************************************************************************************
     * Constructor
     ******************************************************************************************/
    private ApplicationInternal() {
    }

    /******************************************************************************************
     * Assigns the application frame for the application.
     * <p>
     * @param frame The application frame.
     * <p>
     * @throws IllegalArgumentException If the application frame is not a JFrame.
     ******************************************************************************************/
    public static void setApplicationFrame(ApplicationFrame frame) {
        if (!(frame instanceof JFrame)) {
            throw new IllegalArgumentException("The application background frame must subclass JFrame.");
        }
        applicationFrame = frame;
    }

    /******************************************************************************************
     * Retrieves the background frame for the application.
     * <p>
     * @return The background frame for the application.
     ******************************************************************************************/
    public static ApplicationFrame getApplicationFrame() {
        return applicationFrame;
    }

    /******************************************************************************************
     * Retrieves the security manager for the application.
     * <p>
     * @return The security manager for the application.
     ******************************************************************************************/
    public static SecurityInterface getSecurityManager() {
        return applicationFrame.getSecurityManager();
    }

    /******************************************************************************************
     * Assigns the background frame for the application.
     * <p>
     * @return The background frame for the application.
     *****************************************************************************************/
    public static void setFrame(JFrame frame) {
        backgroundFrame = frame;
    }

    /******************************************************************************************
     * Retrieves the background frame for the application as a JFrame.
     * <p>
     * @return The background frame for the application.
     ******************************************************************************************/
    public static JFrame getFrame() {
        return backgroundFrame;
    }

    /******************************************************************************************
     * Handles a navigate command to move from one location to another.
     * <p>
     * @return The security manager for the application.
     ******************************************************************************************/
    public static void navigate(NavigationTaskItemData data) {
        applicationFrame.navigate(data);
    }

    /******************************************************************************************
     * Handles a navigate command to move from one location to another.
     * <p>
     * @return The security manager for the application.
     ******************************************************************************************/
    public static void navigate(String className) {
        applicationFrame.navigate(className);
    }
}
