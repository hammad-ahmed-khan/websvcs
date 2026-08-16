package oracle.retail.sim.client.swing.util;

import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.common.config.NavigationPermission;
import oracle.retail.sim.common.core.locale.StringConstants;

/******************************************************************************************
 * Utility to find the permission for a particular identifier and owner.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class PermissionUtility {

    /******************************************************************************************
     * Retrieves the permission for a particular identifier and owner.
     * <p>
     * @param identifier The identifier of the component or editor to retrieve a permission for.
     * @param ownerPrefix The owner class name to attach to the identifier to find permission.
    /******************************************************************************************/
    public NavigationPermission getPermission(String identifier, String ownerPrefix) throws UIException {
        if (StringUtility.isNullOrEmpty(identifier)) {
            return NavigationPermission.FULL;
        }
        String permissionId = PermissionTranslator.getPermission(ownerPrefix + StringConstants.DOT + identifier);
        if (StringUtility.isNullOrEmpty(permissionId)) {
            return NavigationPermission.FULL;
        }
        return ApplicationInternal.getApplicationFrame().getSecurityManager().getComponentPermission(permissionId);
    }
}
