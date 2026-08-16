package oracle.retail.sim.client.screen.security;

import java.util.Date;
import oracle.retail.sim.common.business.Wrapper;
import oracle.retail.sim.common.security.Role;
import oracle.retail.sim.common.security.UserRole;
import oracle.retail.sim.common.store.Store;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/*
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class UserRoleWrapper extends Wrapper {
    private UserRole userRole;
    private Role role;
    private Store store;

    public UserRoleWrapper(UserRole userRole, Role role, Store store) {
        this.userRole = userRole;
        this.role = role;
        this.store = store;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public Role getRole() {
        return role;
    }

    public Store getStore() {
        return store;
    }

    public String getRoleDescription() {
        return role.getDescription();
    }

    public Date getEndDate() {
        return userRole.getEndDate();
    }

    public boolean isNew() {
        return userRole.getId() == null;
    }

    public boolean isCached() {
        return userRole.isCached();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        UserRoleWrapper that = (UserRoleWrapper) object;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(userRole, that.userRole);
        return builder.isEquals();
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(userRole);
        return builder.toHashCode();
    }
}
