package oracle.retail.sim.client.screen.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.security.Role;
import oracle.retail.sim.common.security.RoleQueryFilter;
import oracle.retail.sim.common.security.RoleType;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.security.UserQueryFilter;
import oracle.retail.sim.common.security.UserStatus;
import oracle.retail.sim.common.security.UserType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * User Lookup Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UserLookupDialogModel extends SimScreenModel {
    private List<Role> availableRoles;
    private List<UserType> availableUserTypes;
    private boolean restrictedUserTypes;
    private UserStatus excludeUserStatus;

    public void setAvailableRoles(List<Role> availableRoles) {
        this.availableRoles = availableRoles;
    }

    public void setAvailableUserTypes(List<UserType> availableUserTypes, boolean restrictedUserTypes) {
        this.availableUserTypes = availableUserTypes;
        this.restrictedUserTypes = restrictedUserTypes;
    }

    public void setExcludeUserStatus(UserStatus excludeUserStatus) {
        this.excludeUserStatus = excludeUserStatus;
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
        List<UserStatus> userStatuses = SimEnumUtility.findUserStatuses();
        if (excludeUserStatus != null) {
            userStatuses.remove(excludeUserStatus);
        }
        return userStatuses;
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

    public List<UserWrapper> findUsers(UserQueryFilter filter) throws Exception {
        if (restrictedUserTypes && availableUserTypes.isEmpty()) {
            return Collections.emptyList();
        }
        if (excludeUserStatus != null && filter.getStatus() == null) {
            filter.doSetStatus(excludeUserStatus);
            filter.doSetExcludeStatus(true);
        }
        List<User> users = ClientServiceFactory.getSecurityServices().findUsers(filter);
        List<UserWrapper> userWrappers = new ArrayList<UserWrapper>(users.size());
        for (User user : users) {
            userWrappers.add(ClientWrapperFactory.createUserWrapper(user));
        }
        return userWrappers;
    }

    public User getSingleUser(List<UserWrapper> wrappers) {
        return !wrappers.isEmpty() ? wrappers.get(0).getUser() : null;
    }

    public List<User> getUsers(List<UserWrapper> wrappers) {
        List<User> users = new ArrayList<User>(wrappers.size());
        for (UserWrapper wrapper : wrappers) {
            users.add(wrapper.getUser());
        }
        return users;
    }
}
