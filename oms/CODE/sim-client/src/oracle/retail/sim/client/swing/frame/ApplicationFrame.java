package oracle.retail.sim.client.swing.frame;

import oracle.retail.sim.client.swing.navigation.SecurityInterface;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.config.NavigationTaskItemData;

/******************************************************************************************
 * This is an interface that all future application containers must extend in order to
 * to work with the rest of the swing framework for GUI building. This allows several
 * different types of application containers to control the same basic screens.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public interface ApplicationFrame {

    /******************************************************************************************
     * Subclasses should implement this method to perform all their initalization.
     *****************************************************************************************/
    void initializeApplication() throws UIException;

    /******************************************************************************************
     * Subclasses should implement to clean up the application cleanly. If this method returns
     * false, the application should not be allowed to close.
     *****************************************************************************************/
    boolean exitApplication();

    /******************************************************************************************
     * Subclasses should implement to return the security manager for the application.
     *****************************************************************************************/
    SecurityInterface getSecurityManager();

    /******************************************************************************************
     * Navigates to the new task indicated by the selected menu item. This is the default
     * implementation of navigate(). Override this method to supply custom navigation logic.
     * The default implements requires that the menu item action command in the .xml file
     * contain the full pathname to a class that subclasses RTaskPanel.
     * <p>
     * @param data The NavigationMenuItemData that contains the navigational information.
     *****************************************************************************************/
    void navigate(NavigationTaskItemData data);

    /******************************************************************************************
     * Navigates to the new task indicated by the selected menu item. This is the default
     * implementation of navigate(). Override this method to supply custom navigation logic.
     * The default implements requires that the menu item action command in the .xml file
     * contain the full pathname to a class that subclasses RTaskPanel.
     * <p>
     * @param taskItemClassPath The full class path of the task item to navigate to. This should
     * match the class path in the .xml file.
     *****************************************************************************************/
    void navigate(String taskItemClassPath);

    /******************************************************************************************
     * Swaps the task panel maximize state.
     *****************************************************************************************/
    void doSwapTaskResizeState();

    /******************************************************************************************
     * Clears the current task panel from the work space.
     *****************************************************************************************/
    void clearTaskPanel();

    /******************************************************************************************
     * Clears the current task panel from the work space.
     *****************************************************************************************/
    void killTaskPanel();

    /******************************************************************************************
     * Clears the current task panel from the work space.
     *****************************************************************************************/
    void recover();
}
