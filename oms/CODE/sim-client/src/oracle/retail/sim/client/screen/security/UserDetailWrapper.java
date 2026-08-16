package oracle.retail.sim.client.screen.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.security.PermissionManager;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Wrapper;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.security.Role;
import oracle.retail.sim.common.security.RoleQueryFilter;
import oracle.retail.sim.common.security.RoleType;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.security.UserRole;
import oracle.retail.sim.common.security.UserStatus;
import oracle.retail.sim.common.security.UserStore;
import oracle.retail.sim.common.security.UserType;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.service.core.ClientServiceFactory;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/********************************************************************************************************
 * User Detail Wrapper - Wraps the user object for UI use. It includes a password that is newly assigned or
 * modified, the original stores the user had and the updated stores from the UI.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UserDetailWrapper extends Wrapper {
    private final User user;
    private char[] password;
    private Boolean userReadOnly;
    private Store defaultStore;

    private Map<Long, Store> availableStores;
    private Map<String, Role> availableRoles;

    private Map<Long, UserStore> userStores = new HashMap<>();
    private List<UserStore> updatedUserStores = new ArrayList<>();
    private List<UserStore> removedUserStores = new ArrayList<>();
    private Set<Long> assignedStoreIds = new HashSet<>();

    private Map<UserRole, UserRole> userRoles = new HashMap<>();
    private List<UserRole> updatedUserRoles = new ArrayList<>();
    private List<UserRole> removedUserRoles = new ArrayList<>();
    private Set<UserRole> assignedUserRoles = new HashSet<>();

    public UserDetailWrapper(User user) {
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

    public String getUserName() {
        return user.getUserName();
    }

    public void setUserName(String userName) throws BusinessException {
        if (!StringHelper.isNullOrEmpty(userName) && userName.equals(user.getUserName())) {
            return;
        }
        user.setUserName(userName);
        //Update username for store and role assignments
        for (UserStore userStore : updatedUserStores) {
            userStore.setUserName(userName);
        }
        for (UserRole userRole : updatedUserRoles) {
            userRole.setUserName(userName);
        }
    }

    public boolean isDeleted() {
        return user.getStatus() == UserStatus.DELETE;
    }

    public UserStatus getStatus() {
        return user.getStatus();
    }

    public Date getStartDate() {
        return user.getStartDate();
    }

    public Date getEndDate() {
        return user.getEndDate();
    }

    public char[] getPassword() {
        return password;
    }

    public void doSetPassword(char[] password) {
        this.password = password;
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

    public boolean isSuperUser() {
        return user.isSuperUser();
    }

    public boolean isTemporaryUser() {
        return user.getType() == UserType.TEMPORARY_USER;
    }

    public UserType getType() {
        return user.getType();
    }

    public void setType(UserType type) throws BusinessException {
        if (user.getType() == type) {
            return;
        }
        user.setType(type);
        if (user.isSuperUser()) {
            assignedStoreIds = new HashSet<>();
            updatedUserStores = new ArrayList<>();
            removedUserStores = new ArrayList<>();
            return;
        }
        //Retain store assignments for default store, role assignments
        Set<Long> storeIds = new HashSet<>();
        if (user.getDefaultStoreId() != null) {
            storeIds.add(user.getDefaultStoreId());
        }
        for (UserRole userRole : assignedUserRoles) {
            if (userRole.getStoreId() != null) {
                storeIds.add(userRole.getStoreId());
            }
        }
        assignStores(storeIds);
    }

    public boolean isDefaultStoreRequired() {
        return user.getDefaultStoreId() == null && !isUserReadOnly();
    }

    public Store getDefaultStore() {
        return defaultStore;
    }

    public void setDefaultStore(Store store) throws BusinessException {
        if (store == null || store.getId().equals(user.getDefaultStoreId())) {
            return;
        }
        user.setDefaultStoreId(store.getId());
        defaultStore = store;
    }

    public Map<Long, Store> getAvailableStores() {
        if (availableStores == null) {
            List<Store> stores = SimRepository.getAllowedStores();
            availableStores = new HashMap<>(stores.size());
            for (Store store : stores) {
                availableStores.put(store.getId(), store);
            }
        }
        return availableStores;
    }

    public Map<Long, UserStore> getUserStores() {
        return userStores;
    }

    public List<UserStore> getUpdatedUserStores() {
        return updatedUserStores;
    }

    public List<UserStore> getRemovedUserStores() {
        return removedUserStores;
    }

    public Set<Long> getAssignedStoreIds() {
        return assignedStoreIds;
    }

    public Map<String, Role> getAvailableRoles() {
        return availableRoles;
    }

    public Map<UserRole, UserRole> getUserRoles() {
        return userRoles;
    }

    public List<UserRole> getUpdatedUserRoles() {
        return updatedUserRoles;
    }

    public List<UserRole> getRemovedUserRoles() {
        return removedUserRoles;
    }

    public Set<UserRole> getAssignedUserRoles() {
        return assignedUserRoles;
    }

    public void assignStores(Collection<Long> selectedStoreIds) throws BusinessException {
        assignedStoreIds = new HashSet<>(selectedStoreIds);
        updatedUserStores = new ArrayList<>();
        removedUserStores = new ArrayList<>();
        String userName = user.getUserName();
        Long defaultStoreId = user.getDefaultStoreId();
        for (Long storeId : getAvailableStores().keySet()) {
            UserStore userStore = userStores.get(storeId);
            if (userStore == null) {
                if (assignedStoreIds.contains(storeId)) {
                    userStore = BOFactory.createUserStore();
                    userStore.setStoreId(storeId);
                    userStore.setUserName(userName);
                    updatedUserStores.add(userStore);
                }
            } else if (!userStore.isCached() && !storeId.equals(defaultStoreId) && !assignedStoreIds.contains(storeId)) {
                removedUserStores.add(userStore);
            }
        }
    }

    public void assignRoles(Collection<UserRole> selectedUserRoles) {
        assignedUserRoles = new HashSet<>(selectedUserRoles.size());
        updatedUserRoles = new ArrayList<>();
        removedUserRoles = new ArrayList<>();
        for (UserRole selectedUserRole : selectedUserRoles) {
            UserRole userRole = userRoles.get(selectedUserRole);
            if (userRole == null) {
                updatedUserRoles.add(selectedUserRole);
            } else if (!userRole.isCached()) {
                selectedUserRole.doSetId(userRole.getId());
                updatedUserRoles.add(selectedUserRole);
            }
            assignedUserRoles.add(selectedUserRole);
        }
        for (UserRole userRole : userRoles.keySet()) {
            if (!userRole.isCached() && !assignedUserRoles.contains(userRole)) {
                removedUserRoles.add(userRole);
            }
        }
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

    public void loadUserStores() throws Exception {
        userStores = new HashMap<>();
        updatedUserStores = new ArrayList<>();
        removedUserStores = new ArrayList<>();
        assignedStoreIds = new HashSet<>();
        if (user.isSuperUser()) {
            return;
        }
        Map<Long, Store> availableStores = getAvailableStores();
        boolean currentSuperUser = SimRepository.getUser().isSuperUser();
        List<UserStore> allUserStores = ClientServiceFactory.getSecurityServices().readUserStores(user.getUserName());
        for (UserStore userStore : allUserStores) {
            if (currentSuperUser || availableStores.containsKey(userStore.getStoreId())) {
                userStores.put(userStore.getStoreId(), userStore);
            }
        }
        assignedStoreIds = new HashSet<>(userStores.keySet());
    }

    public void loadAvailableRoles() throws Exception {
        List<RoleType> roleTypes = ClientServiceFactory.getSecurityServices().findRoleTypes();
        List<String> availableRoleTypeNames = new ArrayList<>();
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
        availableRoles = new HashMap<>(roles.size());
        for (Role role : roles) {
            availableRoles.put(role.getName(), role);
        }
    }

    public void loadUserRoles() throws Exception {
        userRoles = new HashMap<>();
        updatedUserRoles = new ArrayList<>();
        removedUserRoles = new ArrayList<>();
        assignedUserRoles = new HashSet<>();
        boolean currentSuperUser = SimRepository.getUser().isSuperUser();
        List<UserRole> allUserRoles = ClientServiceFactory.getSecurityServices().readUserRoles(user.getUserName());
        for (UserRole userRole : allUserRoles) {
            if (availableRoles.containsKey(userRole.getRoleName()) && (currentSuperUser || userRole.getStoreId() == null || availableStores.containsKey(userRole.getStoreId()))) {
                userRoles.put(userRole, userRole);
            }
        }
        boolean isSuperUser = user.isSuperUser();
        for (UserRole userRole : userRoles.keySet()) {
            if (isSuperUser || userRole.getStoreId() == null || assignedStoreIds.contains(userRole.getStoreId())) {
                assignedUserRoles.add(userRole);
            }
        }
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        UserDetailWrapper that = (UserDetailWrapper) object;
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
