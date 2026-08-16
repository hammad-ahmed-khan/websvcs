package oracle.retail.sim.client.screen.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.security.Permission;
import oracle.retail.sim.common.security.PermissionGroup;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.security.RoleQueryFilter;
import oracle.retail.sim.common.security.RoleType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Role Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RoleFilterDialogModel extends SimScreenModel {
    private List<RoleType> availableRoleTypes;
    private boolean restrictedRoleTypes;
    private List<PermissionGroup> permissionGroups;
    private Map<String, List<Permission>> permissionMap;

    public RoleQueryFilter resetFilter() {
        return BOFactory.createRoleQueryFilter();
    }

    public void setAvailableRoleTypes(List<RoleType> availableRoleTypes, boolean restrictedRoleTypes) {
        this.availableRoleTypes = availableRoleTypes;
        this.restrictedRoleTypes = restrictedRoleTypes;
    }

    public List<RoleType> findAvailableRoleTypes() throws Exception {
        if (availableRoleTypes == null) {
            List<RoleType> roleTypes = ClientServiceFactory.getSecurityServices().findRoleTypes();
            availableRoleTypes = new ArrayList<RoleType>();
            for (RoleType roleType : roleTypes) {
                if (hasDataPermission(PermissionKey.DATA_ROLE_TYPE, Long.toString(roleType.getId()))) {
                    availableRoleTypes.add(roleType);
                }
            }
            restrictedRoleTypes = availableRoleTypes.size() != roleTypes.size();
        }
        return availableRoleTypes;
    }

    public void setFilterRoleTypeNames(RoleQueryFilter filter, RoleType selectedType) throws BusinessException {
        if (selectedType != null) {
            filter.setTypeName(selectedType.getName());
        } else if (restrictedRoleTypes) {
            List<String> availableRoleTypeNames = new ArrayList<String>(availableRoleTypes.size());
            for (RoleType roleType : availableRoleTypes) {
                availableRoleTypeNames.add(roleType.getName());
            }
            filter.doSetTypeNames(availableRoleTypeNames);
        }
    }

    public List<PermissionGroup> findPermissionGroups() throws Exception {
        if (permissionGroups == null) {
            permissionGroups = ClientServiceFactory.getSecurityServices().findPermissionGroups(BOFactory.createPermissionGroupQueryFilter());
        }
        return permissionGroups;
    }

    public List<Permission> findPermissions(PermissionGroup permissionGroup) throws Exception {
        if (permissionMap == null) {
            permissionMap = new HashMap<String, List<Permission>>();
            List<Permission> allPermissions = ClientServiceFactory.getSecurityServices().findPermissions(BOFactory.createPermissionQueryFilter());
            for (Permission permission : allPermissions) {
                PermissionGroup group = permission.getGroup();
                if (group == null) {
                    continue;
                }
                List<Permission> permissions = permissionMap.get(group.getName());
                if (permissions == null) {
                    permissions = new ArrayList<Permission>();
                    permissionMap.put(group.getName(), permissions);
                }
                permissions.add(permission);
            }
        }

        return permissionMap.get(permissionGroup.getName());
    }

    public RoleType getRoleTypeFromList(List<RoleType> roleTypes, String roleTypeName) {
        for (RoleType roleType : roleTypes) {
            if (roleTypeName.equals(roleType.getName())) {
                return roleType;
            }
        }
        return null;
    }

    public PermissionGroup getPermissionGroupFromList(List<PermissionGroup> permissionGroups, String permissionGroupName) {
        for (PermissionGroup permissionGroup : permissionGroups) {
            if (permissionGroupName.equals(permissionGroup.getName())) {
                return permissionGroup;
            }
        }
        return null;
    }

    public Permission getPermissionFromList(List<Permission> permissions, String permissionName) {
        for (Permission permission : permissions) {
            if (permissionName.equals(permission.getName())) {
                return permission;
            }
        }
        return null;
    }
}
