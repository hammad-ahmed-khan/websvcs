package oracle.retail.sim.client.screen.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.security.Role;
import oracle.retail.sim.common.security.RoleQueryFilter;
import oracle.retail.sim.common.security.RoleType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Role List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RoleListModel extends SimScreenModel {
    private List<RoleType> availableRoleTypes;
    private boolean restrictedRoleTypes;

    public RoleQueryFilter getFilter() {
        RoleQueryFilter filter = (RoleQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.ROLE_FILTER);
        if (filter == null) {
            filter = BOFactory.createRoleQueryFilter();
            if (restrictedRoleTypes) {
                List<String> availableRoleTypeNames = new ArrayList<String>(availableRoleTypes.size());
                for (RoleType roleType : availableRoleTypes) {
                    availableRoleTypeNames.add(roleType.getName());
                }
                filter.doSetTypeNames(availableRoleTypeNames);
            }
            RepositoryManager.addStateObject(SimClientStateKey.ROLE_FILTER, filter);
        }
        return filter;
    }

    public void loadAvailableRoleTypes() throws Exception {
        List<RoleType> roleTypes = ClientServiceFactory.getSecurityServices().findRoleTypes();
        availableRoleTypes = new ArrayList<RoleType>();
        for (RoleType roleType : roleTypes) {
            if (hasDataPermission(PermissionKey.DATA_ROLE_TYPE, Long.toString(roleType.getId()))) {
                availableRoleTypes.add(roleType);
            }
        }
        restrictedRoleTypes = availableRoleTypes.size() != roleTypes.size();
    }

    public List<RoleType> getAvailableRoleTypes() {
        return availableRoleTypes;
    }

    public boolean isRestrictedRoleTypes() {
        return restrictedRoleTypes;
    }

    public List<RoleWrapper> findRoles() throws Exception {
        if (restrictedRoleTypes && availableRoleTypes.isEmpty()) {
            return Collections.emptyList();
        }
        List<Role> roles = ClientServiceFactory.getSecurityServices().findRoles(getFilter());
        List<RoleWrapper> wrappers = new ArrayList<RoleWrapper>(roles.size());
        for (Role role : roles) {
            wrappers.add(ClientWrapperFactory.createRoleWrapper(role));
        }
        return wrappers;
    }

    public boolean isAttachedToUser(RoleWrapper wrapper) throws Exception {
        return ClientServiceFactory.getSecurityServices().isRoleAssigned(wrapper.getName());
    }

    public void deleteRoles(List<String> roleNames) throws Exception {
        if (roleNames.size() == 1) {
            ClientServiceFactory.getSecurityServices().deleteRole(roleNames.get(0), getStoreId());
            return;
        }
        ClientServiceFactory.getSecurityServices().deleteRoles(roleNames, getStoreId());
    }

    public void storeRole(RoleWrapper wrapper) {
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_ROLE, wrapper);
    }

    public RoleType getRoleTypeFromList(List<RoleType> roleTypes, String roleTypeName) {
        for (RoleType roleType : roleTypes) {
            if (roleTypeName.equals(roleType.getName())) {
                return roleType;
            }
        }
        return null;
    }

    public Map<String, String> getFilterDescriptionMap() {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        RoleQueryFilter filter = getFilter();
        if (filter.getName() != null) {
            descriptionMap.put("Role Name", filter.getName());
        }
        if (filter.getDescription() != null) {
            descriptionMap.put("Description", filter.getDescription());
        }
        if (filter.getTypeNames().size() == 1) {
            RoleType roleType = getRoleTypeFromList(availableRoleTypes, filter.getTypeNames().get(0));
            if (roleType != null) {
                descriptionMap.put("Role Type", roleType.getDescription());
            }
        }
        if (filter.getPermissionGroupName() != null) {
            descriptionMap.put("Topic", filter.getPermissionGroupName());
        }
        if (filter.getPermissionName() != null) {
            descriptionMap.put("Permission", filter.getPermissionName());
        }
        return descriptionMap;
    }
}
