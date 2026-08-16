package oracle.retail.sim.client.screen.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.security.Role;
import oracle.retail.sim.common.security.SecurityUtility;
import oracle.retail.sim.common.security.UserRole;
import oracle.retail.sim.common.security.UserSecurityMode;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * User Roles Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UserRolesModel extends SimScreenModel {
    private UserDetailWrapper wrapper;

    public UserDetailWrapper getUserDetailWrapper() {
        if (wrapper == null) {
            wrapper = (UserDetailWrapper) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_USER);
        }
        return wrapper;
    }

    public List<UserRoleWrapper> getUserRoleWrappers() {
        Map<Long, Store> availableStores = wrapper.getAvailableStores();
        Map<String, Role> availableRoles = wrapper.getAvailableRoles();
        Set<UserRole> userRoles = wrapper.getAssignedUserRoles();
        List<UserRoleWrapper> userRoleWrappers = new ArrayList<>(userRoles.size());
        for (UserRole userRole : userRoles) {
            userRoleWrappers.add(ClientWrapperFactory.createUserRoleWrapper(userRole, availableRoles.get(userRole.getRoleName()), availableStores.get(userRole.getStoreId())));
        }
        return userRoleWrappers;
    }

    public Store getStore(Long storeId) {
        return wrapper.getAvailableStores().get(storeId);
    }

    public Role getRole(String roleName) {
        return wrapper.getAvailableRoles().get(roleName);
    }

    public boolean isSecurityModeInternal() {
        UserSecurityMode securityMode = SecurityUtility.getUserSecurityMode();
        return securityMode == UserSecurityMode.INTERNAL || securityMode == UserSecurityMode.HYBRID_AUTHN_INTERNAL_AUTHZ;
    }
}
