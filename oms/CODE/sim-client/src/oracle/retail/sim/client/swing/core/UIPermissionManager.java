package oracle.retail.sim.client.swing.core;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.util.PermissionTranslator;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.config.NavigationPermission;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * Utility to find the permission for a particular identifier and owner.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UIPermissionManager {

    /****************************************************************************************************
     * Retrieves the permission for a particular identifier and owner.
     * <p>
     * @param identifier The identifier of the component or editor to retrieve a permission for.
     * @param ownerPrefix The owner class name to attach to the identifier to find permission.
     ***************************************************************************************************/
    public NavigationPermission getComponentPermission(String identifier, String ownerPrefix) throws UIException {
        if (StringUtility.isNullOrEmpty(identifier)) {
            return NavigationPermission.FULL;
        }
        String permissionId = PermissionTranslator.getPermission(ownerPrefix + StringConstants.DOT + identifier);
        if (StringUtility.isNullOrEmpty(permissionId)) {
            return NavigationPermission.FULL;
        }
        return Application.getSecurityManager().getComponentPermission(permissionId);
    }

    /****************************************************************************************************
     * Retrieves the permission for a particular identifier and owner.
     * <p>
     * @param identifier The identifier of the component or editor to retrieve a permission for.
     * @param ownerPrefix The owner class name to attach to the identifier to find permission.
     ***************************************************************************************************/
    public NavigationPermission getComponentPermission(String permissionName) throws UIException {
        if (StringUtility.isNullOrEmpty(permissionName)) {
            return NavigationPermission.FULL;
        }
        return Application.getSecurityManager().getComponentPermission(permissionName);
    }

    /****************************************************************************************************
     * Retrieves the permission for a particular identifier and owner.
     * <p>
     * @param identifier The identifier of the component or editor to retrieve a permission for.
     * @param ownerPrefix The owner class name to attach to the identifier to find permission. /
     ***************************************************************************************************/
    public static NavigationPermission getNavigationPermission(String permissionName) throws UIException {
        if (StringUtility.isNullOrEmpty(permissionName)) {
            return NavigationPermission.FULL;
        }
        return Application.getSecurityManager().getNavigationPermission(permissionName);
    }
}
