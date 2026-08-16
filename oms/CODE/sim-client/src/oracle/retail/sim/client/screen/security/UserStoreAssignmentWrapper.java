package oracle.retail.sim.client.screen.security;

import oracle.retail.sim.common.business.Wrapper;
import oracle.retail.sim.common.store.Store;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/*
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class UserStoreAssignmentWrapper extends Wrapper {
    private UserStoreAssignmentAction action;
    private Store store;

    public UserStoreAssignmentWrapper(UserStoreAssignmentAction action, Store store) {
        this.action = action;
        this.store = store;
    }

    public UserStoreAssignmentAction getAction() {
        return action;
    }

    public Store getStore() {
        return store;
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
        UserStoreAssignmentWrapper that = (UserStoreAssignmentWrapper) object;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(store, that.store);
        return builder.isEquals();
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(store);
        return builder.toHashCode();
    }
}
