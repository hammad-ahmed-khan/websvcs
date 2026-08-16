package oracle.retail.sim.client.swing.test;

import oracle.retail.sim.client.swing.navigation.SecurityInterface;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.config.NavigationPermission;

/********************************************************************************************************
 * This class handles checking a single permission id and returning the appropriate class permission.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestSecurityManager implements SecurityInterface {

    /****************************************************************************************************
     * Constructs new DefaultNavigationSecurity.
     ***************************************************************************************************/
    public TestSecurityManager() {
    }

    /****************************************************************************************************
     * Retrieves the appropriate permission for the permission id. The default implementation always
     * returns FULL permissions. The implementing client should implement install a new
     * NavigationSecurity object through their application frame.
     * <p>
     * @param permissionId A permission id from the workflow.
     *            <p>
     * @return A UIPermission object representing the appropriate permission for the id.
     ***************************************************************************************************/
    public NavigationPermission getNavigationPermission(String permissionId) throws UIException {
        return NavigationPermission.FULL;
    }

    /****************************************************************************************************
     * Retrieves the appropriate permission for the permission id. The default implementation always
     * returns FULL permissions. The implementing client should implement install a new
     * NavigationSecurity object through their application frame.
     * <p>
     * @param permissionId A permissionId id.
     *            <p>
     * @return A UIPermission object representing the appropriate permission for the id.
     ***************************************************************************************************/
    public NavigationPermission getComponentPermission(String permissionId) throws UIException {
        if (permissionId.equalsIgnoreCase(NavigationPermission.NONE.toString())) {
            return NavigationPermission.NONE;
        } else if (permissionId.equalsIgnoreCase(NavigationPermission.VIEW.toString())) {
            return NavigationPermission.VIEW;
        }
        return NavigationPermission.FULL;
    }
}
