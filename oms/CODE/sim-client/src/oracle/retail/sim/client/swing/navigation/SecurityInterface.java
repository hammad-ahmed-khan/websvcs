package oracle.retail.sim.client.swing.navigation;

import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.config.NavigationPermission;

/******************************************************************************************
 * This interface must be implemented by any object that chooses to handle security for
 * the client application.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public interface SecurityInterface {

    /******************************************************************************************
     * This method is called to determine the security on a navigation task or task item.
     ******************************************************************************************/
    NavigationPermission getNavigationPermission(String permissionId) throws UIException;

    /******************************************************************************************
     * This method is called to determine the security on a registered component or editor.
     ******************************************************************************************/
    NavigationPermission getComponentPermission(String permissionId) throws UIException;
}
