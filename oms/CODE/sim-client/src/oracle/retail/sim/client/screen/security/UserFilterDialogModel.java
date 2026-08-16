package oracle.retail.sim.client.screen.security;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.security.Role;
import oracle.retail.sim.common.security.RoleQueryFilter;
import oracle.retail.sim.common.security.RoleType;
import oracle.retail.sim.common.security.UserQueryFilter;
import oracle.retail.sim.common.security.UserStatus;
import oracle.retail.sim.common.security.UserType;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * User Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UserFilterDialogModel extends SimScreenModel {
    private List<Role> availableRoles;
    private List<UserType> availableUserTypes;
    private boolean restrictedUserTypes;

    public UserQueryFilter resetFilter() throws BusinessException {
        UserQueryFilter filter = BOFactory.createUserQueryFilter();
        filter.doSetStoreId(getStoreId());
        filter.doSetDefaultStore(true);
        filter.doSetStatus(UserStatus.ACTIVE);
        if (restrictedUserTypes) {
            filter.doSetTypes(availableUserTypes);
        }
        return filter;
    }

    public void setAvailableUserTypes(List<UserType> availableUserTypes, boolean restrictedUserTypes) {
        this.availableUserTypes = availableUserTypes;
        this.restrictedUserTypes = restrictedUserTypes;
    }

    public List<UserType> findAvailableUserTypes() {
        if (availableUserTypes == null) {
            availableUserTypes = SimEnumUtility.findAvailableUserTypes();
            restrictedUserTypes = availableUserTypes.size() != SimEnumUtility.findUserTypes().size();
        }
        return availableUserTypes;
    }

    public void setFilterUserTypes(UserQueryFilter filter, UserType selectedType) throws BusinessException {
        if (selectedType != null) {
            filter.setType(selectedType);
        } else if (restrictedUserTypes) {
            filter.doSetTypes(availableUserTypes);
        }
    }

    public List<UserStatus> findUserStatuses() {
        return SimEnumUtility.findUserStatuses();
    }

    public List<Boolean> findDefaultStoreValues() {
        List<Boolean> values = new ArrayList<Boolean>(2);
        values.add(Boolean.TRUE);
        values.add(Boolean.FALSE);
        return values;
    }

    public List<Role> findAvailableRoles() throws Exception {
        if (availableRoles == null) {
            List<RoleType> roleTypes = ClientServiceFactory.getSecurityServices().findRoleTypes();
            List<String> availableRoleTypeNames = new ArrayList<String>();
            for (RoleType roleType : roleTypes) {
                if (hasDataPermission(PermissionKey.DATA_ROLE_TYPE, Long.toString(roleType.getId()))) {
                    availableRoleTypeNames.add(roleType.getName());
                }
            }
            RoleQueryFilter filter = BOFactory.createRoleQueryFilter();
            if (availableRoleTypeNames.size() != roleTypes.size()) {
                filter.doSetTypeNames(availableRoleTypeNames);
            }
            availableRoles = ClientServiceFactory.getSecurityServices().findRoles(filter);
        }
        return availableRoles;
    }

    public Role getRoleFromList(List<Role> roles, String roleName) throws Exception {
        for (Role role : roles) {
            if (role.getName().equals(roleName)) {
                return role;
            }
        }
        return null;
    }

    public Store getStoreFromList(List<Store> stores, Long storeId) throws Exception {
        for (Store store : stores) {
            if (store.getId().equals(storeId)) {
                return store;
            }
        }
        return null;
    }
}
