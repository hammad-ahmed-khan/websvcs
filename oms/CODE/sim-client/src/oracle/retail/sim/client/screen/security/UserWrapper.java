package oracle.retail.sim.client.screen.security;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.security.PermissionManager;
import oracle.retail.sim.common.business.Wrapper;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.security.UserStatus;
import oracle.retail.sim.common.security.UserType;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.service.core.ClientServiceFactory;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/********************************************************************************************************
 * User Wrapper - Wraps the user object for UI use. It includes a password that is newly assigned or
 * modified, the original stores the user had and the updated stores from the UI.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UserWrapper extends Wrapper {
    private User user;
    private Boolean userReadOnly;
    private Store defaultStore;

    private Map<Long, Store> availableStores;

    public UserWrapper(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public boolean isNew() {
        return user.getId() == null && !user.isCached();
    }

    public boolean isCached() {
        return user.isCached();
    }

    public String getUserName() {
        return user.getUserName();
    }

    public String getName() {
        StringBuilder sb = new StringBuilder();
        String firstName = user.getFirstName();
        if (!StringHelper.isNullOrEmpty(firstName)) {
            sb.append(firstName);
        }
        String lastName = user.getLastName();
        if (!StringHelper.isNullOrEmpty(lastName)) {
            if (sb.length() > 0) {
                sb.append(StringConstants.SPACE);
            }
            sb.append(lastName);
        }
        return sb.toString();
    }

    public UserStatus getStatus() {
        return user.getStatus();
    }

    public UserType getType() {
        return user.getType();
    }

    public Date getCreateDate() {
        return user.getCreateDate();
    }

    public Date getStartDate() {
        return user.getStartDate();
    }

    public Date getEndDate() {
        return user.getEndDate();
    }

    public boolean isUserReadOnly() {
        if (userReadOnly == null) {
            userReadOnly = initializeUserReadOnly();
        }
        return userReadOnly;
    }

    private boolean initializeUserReadOnly() {
        if (user.isCached()) {
            return true;
        }
        if (user.getId() == null) {
            return false;
        }
        if (!PermissionManager.hasPermission(PermissionKey.PC_EDIT_USER)) {
            return true;
        }
        if (!PermissionManager.hasDataPermission(PermissionKey.DATA_USER_TYPE, user.getType().getCode())) {
            return true;
        }
        if (SimRepository.getUser().isSuperUser()) {
            return false;
        }
        Long defaultStoreId = user.getDefaultStoreId();
        if (defaultStoreId == null || getAvailableStores().containsKey(defaultStoreId)) {
            return false;
        }
        return true;
    }

    public Store getDefaultStore() {
        return defaultStore;
    }

    public Map<Long, Store> getAvailableStores() {
        if (availableStores == null) {
            List<Store> stores = SimRepository.getAllowedStores();
            availableStores = new HashMap<Long, Store>(stores.size());
            for (Store store : stores) {
                availableStores.put(store.getId(), store);
            }
        }
        return availableStores;
    }

    public void setAvailableStores(Map<Long, Store> availableStores) {
        this.availableStores = availableStores;
    }

    public void loadDefaultStore() throws Exception {
        Long defaultStoreId = user.getDefaultStoreId();
        if (defaultStoreId == null) {
            defaultStore = null;
            return;
        }
        if (defaultStore != null && defaultStoreId.equals(defaultStore.getId())) {
            return;
        }
        defaultStore = getAvailableStores().get(defaultStoreId);
        if (defaultStore == null) {
            defaultStore = ClientServiceFactory.getStoreServices().readStore(defaultStoreId);
        }
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        UserWrapper that = (UserWrapper) object;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(user, that.user);
        return builder.isEquals();
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(user);
        return builder.toHashCode();
    }
}
