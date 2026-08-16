package oracle.retail.sim.client.screen.security;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.security.SecurityUtility;
import oracle.retail.sim.common.security.UserSecurityMode;
import oracle.retail.sim.common.security.UserStore;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * User Stores Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UserStoresModel extends SimScreenModel {
    private UserDetailWrapper wrapper;
    private Store defaultStore;

    public UserDetailWrapper getUserDetailWrapper() {
        if (wrapper == null) {
            wrapper = (UserDetailWrapper) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_USER);
            defaultStore = wrapper.getDefaultStore();
        }
        return wrapper;
    }

    public boolean isCachedAssignment(Long storeId) {
        if (storeId == null) {
            return false;
        }
        UserStore userStore = wrapper.getUserStores().get(storeId);
        return userStore != null && userStore.isCached();
    }

    public boolean isDefaultStore(Long storeId) {
        return defaultStore != null && defaultStore.getId().equals(storeId);
    }

    public Store getDefaultStore() {
        return defaultStore;
    }

    public void setDefaultStore(Long storeId) {
        defaultStore = wrapper.getAvailableStores().get(storeId);
    }

    public Store getStore(Long storeId) {
        return wrapper.getAvailableStores().get(storeId);
    }

    public boolean isSecurityModeInternal() {
        UserSecurityMode securityMode = SecurityUtility.getUserSecurityMode();
        return securityMode == UserSecurityMode.INTERNAL || securityMode == UserSecurityMode.HYBRID_AUTHN_INTERNAL_AUTHZ;
    }
}
