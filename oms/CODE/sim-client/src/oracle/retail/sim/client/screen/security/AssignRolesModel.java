package oracle.retail.sim.client.screen.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.security.PermissionManager;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.security.Role;
import oracle.retail.sim.common.security.RoleQueryFilter;
import oracle.retail.sim.common.security.RoleType;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.security.UserRole;
import oracle.retail.sim.common.security.UserRolesSaveVO;
import oracle.retail.sim.common.security.UserType;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Assign Roles Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class AssignRolesModel extends SimScreenModel {
    private List<UserType> availableUserTypes;
    private Map<Long, Store> availableStores;
    private Map<String, Role> availableRoles;

    public List<UserType> findAvailableUserTypes() {
        if (availableUserTypes == null) {
            availableUserTypes = SimEnumUtility.findAvailableUserTypes();
        }
        return availableUserTypes;
    }

    public Map<Long, Store> findAvailableStores() throws Exception {
        if (availableStores == null) {
            List<Store> stores = SimRepository.getAllowedStores();
            availableStores = new HashMap<Long, Store>(stores.size());
            for (Store store : stores) {
                availableStores.put(store.getId(), store);
            }
        }
        return availableStores;
    }

    public Map<String, Role> findAvailableRoles() throws Exception {
        if (availableRoles == null) {
            List<RoleType> roleTypes = ClientServiceFactory.getSecurityServices().findRoleTypes();
            List<String> availableRoleTypeNames = new ArrayList<String>();
            for (RoleType roleType : roleTypes) {
                if (PermissionManager.hasDataPermission(PermissionKey.DATA_ROLE_TYPE, Long.toString(roleType.getId()))) {
                    availableRoleTypeNames.add(roleType.getName());
                }
            }
            RoleQueryFilter filter = BOFactory.createRoleQueryFilter();
            if (availableRoleTypeNames.size() != roleTypes.size()) {
                filter.doSetTypeNames(availableRoleTypeNames);
            }
            List<Role> roles = ClientServiceFactory.getSecurityServices().findRoles(filter);
            availableRoles = new HashMap<String, Role>(roles.size());
            for (Role role : roles) {
                availableRoles.put(role.getName(), role);
            }
        }
        return availableRoles;
    }

    public Store getStore(Long storeId) {
        return availableStores.get(storeId);
    }

    public Role getRole(String roleName) {
        return availableRoles.get(roleName);
    }

    public void saveAssignments(List<UserWrapper> userWrappers, List<UserRoleAssignmentWrapper> assignmentWrappers) throws Exception {
        UserRolesSaveVO userRolesSaveVO = BOFactory.createUserRolesSaveVO();
        List<User> users = new ArrayList<User>(userWrappers.size());
        for (UserWrapper userWrapper : userWrappers) {
            users.add(userWrapper.getUser());
        }
        userRolesSaveVO.doSetUsers(users);
        List<UserRole> addedRoleAssignments = new ArrayList<UserRole>();
        List<UserRole> removedRoleAssignments = new ArrayList<UserRole>();
        for (UserRoleAssignmentWrapper assignmentWrapper : assignmentWrappers) {
            Long storeId = assignmentWrapper.getStoreId();
            String roleName = assignmentWrapper.getRoleName();
            if (storeId == null || StringHelper.isNullOrEmpty(roleName)) {
                continue;
            }
            UserRole userRole = BOFactory.createUserRole();
            userRole.doSetRoleName(roleName);
            userRole.doSetStoreId(storeId);
            userRole.doSetEndDate(assignmentWrapper.getEndDate());
            switch (assignmentWrapper.getAction()) {
                case ADD:
                    addedRoleAssignments.add(userRole);
                    break;
                case DELETE:
                    removedRoleAssignments.add(userRole);
                    break;
            }
        }
        userRolesSaveVO.doSetAddedRoleAssignments(addedRoleAssignments);
        userRolesSaveVO.doSetRemovedRoleAssignments(removedRoleAssignments);
        if (!userRolesSaveVO.isEmpty()) {
            ClientServiceFactory.getSecurityServices().saveUserRoles(userRolesSaveVO, getStoreId());
        }
    }
}
