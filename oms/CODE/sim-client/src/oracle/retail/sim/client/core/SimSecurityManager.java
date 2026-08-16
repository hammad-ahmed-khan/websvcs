package oracle.retail.sim.client.core;

import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.security.PermissionManager;
import oracle.retail.sim.client.swing.navigation.SecurityInterface;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.config.NavigationPermission;

/********************************************************************************************************
 * This class handles checking a single permission id and returning the appropriate class permission.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimSecurityManager implements SecurityInterface {
    /****************************************************************************************************
     * Constructs new SimSecurityManager.
     ***************************************************************************************************/
    public SimSecurityManager() {
    }

    /****************************************************************************************************
     * Retrieves the appropriate permission for the permission id. The default implementation always
     * returns FULL permissions. The implementing client should implement install a new
     * NavigationSecurity object through their application frame.
     * <p>
     * @param permissionId A permission id from the workflow.
     * @return A UIPermission object representing the appropriate permission for the id.
     ***************************************************************************************************/
    public NavigationPermission getNavigationPermission(String permissionId) throws UIException {
        if (StringUtility.isNullOrEmpty(permissionId)) {
            return NavigationPermission.FULL;
        }
        return PermissionManager.hasPermission(permissionId) ? NavigationPermission.FULL : NavigationPermission.NONE;
    }

    /****************************************************************************************************
     * Retrieves the appropriate permission for the permission id. The default implementation always
     * returns FULL permissions. The implementing client should implement install a new
     * NavigationSecurity object through their application frame.
     * <p>
     * @param permissionId A permission id.
     * @return A UIPermission object representing the appropriate permission for the id.
     ***************************************************************************************************/
    public NavigationPermission getComponentPermission(String permissionId) throws UIException {
        return NavigationPermission.FULL;
    }
}
