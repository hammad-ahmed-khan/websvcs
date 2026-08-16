package oracle.retail.sim.client.screen.security;

import java.util.Date;
import oracle.retail.sim.common.security.Role;
import oracle.retail.sim.common.store.Store;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/*
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class UserRoleAssignmentWrapper {
    private final UserRoleAssignmentAction action;
    private final Store store;
    private final Role role;
    private final Date endDate;

    public UserRoleAssignmentWrapper(UserRoleAssignmentAction action, Role role, Store store, Date endDate) {
        this.action = action;
        this.store = store;
        this.role = role;
        this.endDate = endDate;
    }

    public UserRoleAssignmentAction getAction() {
        return action;
    }

    public Role getRole() {
        return role;
    }

    public Store getStore() {
        return store;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getRoleName() {
        return role != null ? role.getName() : null;
    }

    public String getRoleDescription() {
        return role != null ? role.getDescription() : null;
    }

    public Long getStoreId() {
        return store != null ? store.getId() : null;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        UserRoleAssignmentWrapper that = (UserRoleAssignmentWrapper) object;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(role, that.role);
        builder.append(store, that.store);
        return builder.isEquals();
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(role);
        builder.append(store);
        return builder.toHashCode();
    }
}
