package oracle.retail.sim.client.screen.security;

import java.util.List;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Wrapper;
import oracle.retail.sim.common.security.Permission;
import oracle.retail.sim.common.security.PermissionSet;
import oracle.retail.sim.common.security.Role;
import oracle.retail.sim.common.security.RoleType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/********************************************************************************************************
 * Role Wrapper - Wraps the role object for UI use. This includes the permissionSet currently associated
 * with the role. It also includes inventory adjustment reasons and role types because internal methods
 * need to translate back and forth between those business objects and their permission values.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RoleWrapper extends Wrapper {
    private Role role;
    private PermissionSet permissionSet;

    public RoleWrapper(Role role) {
        this.role = role;
        permissionSet = BOFactory.createPermissionSet();
    }

    public Role getRole() {
        return role;
    }

    public boolean isNew() {
        return role == null || role.getId() == null;
    }

    public String getName() {
        return role.getName();
    }

    public void setName(String name) throws BusinessException {
        role.setName(name);
    }

    public String getDescription() {
        return role.getDescription();
    }

    public void setDescription(String description) throws BusinessException {
        role.setDescription(description);
    }

    public RoleType getType() {
        return role.getType();
    }

    public void setType(RoleType type) throws BusinessException {
        role.setType(type);
    }

    public boolean isDateRequired() {
        return role.isDateRequired();
    }

    public void setDateRequired(boolean dateRequired) throws BusinessException {
        role.setDateRequired(dateRequired);
    }

    public PermissionSet getPermissionSet() throws Exception {
        return permissionSet;
    }

    public void setPermissionSet(PermissionSet permissionSet) throws Exception {
        this.permissionSet = permissionSet;
    }

    public void removePermissions(List<Permission> permissions) throws BusinessException {
        for (Permission permission : permissions) {
            permissionSet.removePermission(permission.getName());
        }
    }

    public void storePermissions(List<Permission> permissions) throws BusinessException {
        for (Permission permission : permissions) {
            permissionSet.addPermission(permission.getName());
        }
    }

    public void removeDataPermissions(List<DataPermissionWrapper> permissions) throws BusinessException {
        for (DataPermissionWrapper permission : permissions) {
            permissionSet.removePermission(permission.getName(), permission.getKey(), permission.getValue());
        }
    }

    public void storeDataPermissions(List<DataPermissionWrapper> permissions) throws BusinessException {
        for (DataPermissionWrapper permission : permissions) {
            permissionSet.addPermission(permission.getName(), permission.getKey(), permission.getValue());
        }
    }

    /**
     * Returns true if the role has data permissions assigned to it.
     */
    public boolean hasDataPermissions() {
        for (String name : SimEnumUtility.findDataPermissionNames()) {
            if (permissionSet.containsPermission(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        RoleWrapper that = (RoleWrapper) object;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(role, that.role);
        return builder.isEquals();
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(role);
        return builder.toHashCode();
    }
}
