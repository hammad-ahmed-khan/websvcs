package oracle.retail.sim.client.screen.security;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.core.DeviceType;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.security.Permission;
import oracle.retail.sim.common.security.PermissionGroup;
import oracle.retail.sim.common.security.PermissionGroupKey;
import oracle.retail.sim.common.security.PermissionSet;
import oracle.retail.sim.common.security.Role;
import oracle.retail.sim.common.security.RoleQueryFilter;
import oracle.retail.sim.common.security.RoleType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Role Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RoleDetailModel extends SimScreenModel {
    private RoleWrapper roleWrapper;
    private List<RoleType> roleTypes;
    private List<PermissionGroup> permissionGroups;
    private List<Permission> permissions;

    public RoleWrapper getRoleWrapper() {
        if (roleWrapper == null) {
            roleWrapper = (RoleWrapper) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_ROLE);
            if (roleWrapper == null) {
                roleWrapper = ClientWrapperFactory.createRoleWrapper(BOFactory.createRole());
                RepositoryManager.addStateObject(SimClientStateKey.SELECTED_ROLE, roleWrapper);
            }
        }
        return roleWrapper;
    }

    public void loadRoleWrapperPermissionSet() throws Exception {
        if (roleWrapper == null || roleWrapper.isNew()) {
            return;
        }
        PermissionSet permissionSet = ClientServiceFactory.getSecurityServices().readRolePermissions(roleWrapper.getName());
        if (permissionSet != null) {
            roleWrapper.setPermissionSet(permissionSet);
        }
    }

    public List<RoleType> findRoleTypes() throws Exception {
        if (roleTypes == null) {
            roleTypes = ClientServiceFactory.getSecurityServices().findRoleTypes();
        }
        return roleTypes;
    }

    public List<DeviceType> findDeviceTypes() {
        return SimEnumUtility.findDeviceTypes();
    }

    public List<PermissionGroup> findPermissionGroups() throws Exception {
        if (permissionGroups == null) {
            permissionGroups = ClientServiceFactory.getSecurityServices().findPermissionGroups(BOFactory.createPermissionGroupQueryFilter());
            for (PermissionGroup permissionGroup : permissionGroups) {
                if (PermissionGroupKey.DATA.equals(permissionGroup.getName())) {
                    permissionGroups.remove(permissionGroup);
                    break;
                }
            }
        }
        return permissionGroups;
    }

    public List<Permission> findPermissions() throws Exception {
        if (permissions == null) {
            permissions = new ArrayList<>();
            List<Permission> allPermissions = ClientServiceFactory.getSecurityServices().findPermissions(BOFactory.createPermissionQueryFilter());
            for (Permission permission : allPermissions) {
                if (permission.getGroup() != null && PermissionGroupKey.DATA.equals(permission.getGroup().getName())) {
                    continue;
                }
                permissions.add(permission);
            }
        }
        return permissions;
    }

    /*
     * Filter all permissions to include permissions matching the filter parameters
     */

    public List<Permission> findAvailablePermissions(PermissionGroup permissionGroup, DeviceType deviceType) throws Exception {
        List<Permission> allPermissions = findPermissions();
        if (permissionGroup == null && deviceType == null) {
            return allPermissions;
        }
        List<Permission> availablePermissions = new ArrayList<>();
        for (Permission permission : allPermissions) {
            if (permissionGroup != null && !permissionGroup.equals(permission.getGroup())) {
                continue;
            }
            if (deviceType != null && deviceType != permission.getDeviceType()) {
                continue;
            }
            availablePermissions.add(permission);
        }
        return availablePermissions;
    }

    /*
     * Filter available permissions to include permissions matching the filter parameters and exist in the role permission set
     */

    public List<Permission> findSelectedPermissions(List<Permission> availablePermissions) throws Exception {
        List<Permission> selectedPermissions = new ArrayList<>();
        PermissionSet permissionSet = roleWrapper.getPermissionSet();
        for (Permission permission : availablePermissions) {
            if (permissionSet.containsPermission(permission.getName())) {
                selectedPermissions.add(permission);
            }
        }
        return selectedPermissions;
    }

    public boolean roleNameExists() throws Exception {
        String name = roleWrapper.getName();
        if (StringHelper.isNullOrEmpty(name)) {
            return true;
        }
        RoleQueryFilter filter = BOFactory.createRoleQueryFilter();
        filter.doSetName(name);
        List<Role> roles = ClientServiceFactory.getSecurityServices().findRoles(filter);
        for (Role role : roles) {
            if (name.equalsIgnoreCase(role.getName())) {
                return true;
            }
        }
        return false;
    }

    public void saveRole() throws Exception {
        ClientServiceFactory.getSecurityServices().saveRole(roleWrapper.getRole(), roleWrapper.getPermissionSet(), getStoreId());
        RepositoryManager.addStateObject(SimClientStateKey.ROLE_DETAIL_MODIFIED, Boolean.TRUE);
    }
}
