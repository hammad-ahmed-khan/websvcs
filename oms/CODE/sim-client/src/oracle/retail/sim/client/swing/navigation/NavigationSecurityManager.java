package oracle.retail.sim.client.swing.navigation;

import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.config.NavigationData;
import oracle.retail.sim.common.config.NavigationPermission;
import oracle.retail.sim.common.config.NavigationTabData;
import oracle.retail.sim.common.config.NavigationTaskData;
import oracle.retail.sim.common.config.NavigationTaskItemData;

/******************************************************************************************
 * This class handles the algorithm for including or excluding tabs, tasks and task items.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class NavigationSecurityManager {

    private static SecurityInterface navigationSecurity = new DefaultSecurityManager();

    /******************************************************************************************
     * Private constructor for static class.
     ******************************************************************************************/
    private NavigationSecurityManager() {
    }

    /******************************************************************************************
     * Installs a new NavigationSecurity protocol.
     * <p>
     * @param security The NavigationSecurity protocol to install.
     ******************************************************************************************/
    public static void installNavigationSecurity(SecurityInterface security) {
        if (security != null) {
            navigationSecurity = security;
        }
    }

    /******************************************************************************************
     * Retrieves the secure NavigationData object for the input NavigationData object. It should
     * validate each permission through the NavigationSecurity object and include or exclude
     * the data. Each tab should be checked first, then each task, then each task item.
     * <p>
     * @param security The NavigationSecurity protocol to install.
     ******************************************************************************************/
    public static NavigationData getSecureNavigationData(NavigationData navigationData) throws UIException {
        NavigationTabData[] tabArray = navigationData.getTabs();
        for (NavigationTabData tabData : tabArray) {
            tabData.setNavigationPermission(navigationSecurity.getNavigationPermission(tabData.getPermissionName()));

            if (tabData.getNavigationPermission() == NavigationPermission.NONE) {
                navigationData.removeTab(tabData);
            } else {
                validateTaskPermissions(tabData);
            }
        }
        return navigationData;
    }

    /******************************************************************************************
     * Validates all the task permissions for a tab. It should validate each permission through
     * the NavigationSecurity object and include or exclude the data.
     * <p>
     * @param data The NavigationTabData to verify tasks for.
     ******************************************************************************************/
    private static void validateTaskPermissions(NavigationTabData tabData) throws UIException {
        NavigationTaskData[] taskArray = tabData.getTasks();
        for (NavigationTaskData taskData : taskArray) {
            taskData.setNavigationPermission(navigationSecurity.getNavigationPermission(taskData.getPermissionName()));

            if (taskData.getNavigationPermission() == NavigationPermission.NONE) {
                tabData.removeTask(taskData);
            } else {
                validateTaskItemPermissions(taskData);
            }
        }
    }

    /******************************************************************************************
     * Validates all the task items permissions for a tab. It should validate each permission through
     * the NavigationSecurity object and include or exclude the data.
     * <p>
     * @param data The NavigationTaskData to verify task items for.
     ******************************************************************************************/
    private static void validateTaskItemPermissions(NavigationTaskData taskData) throws UIException {
        NavigationTaskItemData[] taskItemArray = taskData.getTaskItems();
        NavigationPermission permission;
        for (NavigationTaskItemData taskItemData : taskItemArray) {
            permission = navigationSecurity.getNavigationPermission(taskItemData.getPermissionName());
            taskItemData.setNavigationPermission(permission);

            if (taskItemData.getNavigationPermission() == NavigationPermission.NONE) {
                taskData.removeTaskItem(taskItemData);
            }
        }
    }
}
