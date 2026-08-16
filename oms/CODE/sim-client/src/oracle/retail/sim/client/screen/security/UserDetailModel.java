package oracle.retail.sim.client.screen.security;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.security.Role;
import oracle.retail.sim.common.security.SecurityMessageText;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.security.UserRole;
import oracle.retail.sim.common.security.UserSaveVO;
import oracle.retail.sim.common.security.UserStatus;
import oracle.retail.sim.common.security.UserStore;
import oracle.retail.sim.common.security.UserType;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.common.util.ArrayUtility;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * User Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UserDetailModel extends SimScreenModel {
    private UserDetailWrapper wrapper;
    private List<Locale> availableLocales;
    private List<UserType> availableUserTypes;

    public UserDetailWrapper getUserDetailWrapper() throws Exception {
        if (wrapper == null) {
            wrapper = (UserDetailWrapper) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_USER);
            if (wrapper == null) {
                wrapper = createNewUserWrapper();
                wrapper.loadAvailableRoles();
                RepositoryManager.addStateObject(SimClientStateKey.SELECTED_USER, wrapper);
            } else {
                wrapper.loadAvailableRoles();
                wrapper.loadDefaultStore();
                wrapper.loadUserStores();
                wrapper.loadUserRoles();
            }
        }
        return wrapper;
    }

    private UserDetailWrapper createNewUserWrapper() throws BusinessException {
        User user = BOFactory.createUser();
        user.setLocale(LocaleManager.getLanguageLocale());
        user.setStatus(UserStatus.ACTIVE);
        //Default user type when only 1 available
        List<UserType> userTypes = findAvailableUserTypes();
        if (userTypes.size() == 1) {
            user.setType(userTypes.get(0));
        }
        //Create store assignment and set as default for current store
        Store store = getStore();
        UserDetailWrapper wrapper = ClientWrapperFactory.createUserDetailWrapper(user);
        wrapper.setDefaultStore(store);
        if (!user.isSuperUser()) {
            UserStore userStore = BOFactory.createUserStore();
            userStore.setStoreId(store.getId());
            wrapper.getUpdatedUserStores().add(userStore);
            wrapper.getAssignedStoreIds().add(store.getId());
        }
        return wrapper;
    }

    public List<Locale> findAvailableLocales() throws Exception {
        if (availableLocales == null) {
            availableLocales = ClientServiceFactory.getTranslationServices().findLocales();
            Locale locale = wrapper.getUser().getLocale();
            if (!availableLocales.contains(locale)) {
                availableLocales.add(locale);
            }
            Locale currentLocale = LocaleManager.getLanguageLocale();
            if (!currentLocale.equals(locale) && !availableLocales.contains(currentLocale)) {
                availableLocales.add(currentLocale);
            }
        }
        return availableLocales;
    }

    public List<UserType> findAvailableUserTypes() {
        if (availableUserTypes == null) {
            availableUserTypes = SimEnumUtility.findAvailableUserTypes();
        }
        return availableUserTypes;
    }

    public List<UserStatus> findAvailableUserStatuses(UserStatus currentStatus) {
        List<UserStatus> userStatuses = SimEnumUtility.findUserStatuses();
        if (currentStatus != UserStatus.DELETE) {
            userStatuses.remove(UserStatus.DELETE);
        }
        if (currentStatus != UserStatus.LOCKED) {
            userStatuses.remove(UserStatus.LOCKED);
        }
        return userStatuses;
    }

    public String buildDefaultUsername(String firstName, String lastName) {
        return BOFactory.createUsernameGenerator().generateUsername(firstName, lastName);
    }

    public boolean validateUserName() {
        String userName = wrapper.getUserName();
        if (StringHelper.isNullOrEmpty(userName)) {
            return false;
        }
        String illegalCharacterSet = "&><\\\\/\"';:$*?+^%}{)(\\]\\[ \t\n\r";
        if (StringHelper.getInstance().containsCharacterFromSet(userName, illegalCharacterSet)) {
            return false;
        }
        return true;
    }

    public boolean userNameExists() throws Exception {
        return ClientServiceFactory.getSecurityServices().readUser(wrapper.getUserName()) != null;
    }

    public boolean copyAssignments(User user) throws Exception {
        if (!wrapper.isSuperUser() && user.isSuperUser()) {
            throw new BusinessException(SecurityMessageText.INVALID_USER_SELECTED);
        }
        if (!availableUserTypes.contains(user.getType())) {
            throw new BusinessException(SecurityMessageText.INVALID_USER_SELECTED);
        }

        boolean completeCopy = true;
        boolean currentSuperUser = isSuperUser();
        Map<Long, Store> availableStores = wrapper.getAvailableStores();

        //Set default store if selected user's default store is available, otherwise set to current store
        Long defaultStoreId = user.getDefaultStoreId();
        if (defaultStoreId == null || !currentSuperUser && !availableStores.containsKey(defaultStoreId)) {
            defaultStoreId = getStoreId();
            completeCopy = false;
        }
        wrapper.setDefaultStore(availableStores.get(defaultStoreId));

        //Set store assignments if not superuser
        Set<Long> selectedStoreIds = new HashSet<>();
        if (!wrapper.isSuperUser()) {
            if (!user.isSuperUser()) {
                List<UserStore> userStores = ClientServiceFactory.getSecurityServices().readUserStores(user.getUserName());
                for (UserStore userStore : userStores) {
                    if (currentSuperUser || availableStores.containsKey(userStore.getStoreId())) {
                        selectedStoreIds.add(userStore.getStoreId());
                    } else if (completeCopy) {
                        completeCopy = false;
                    }
                }
            } else if (completeCopy) {
                completeCopy = false;
            }
            selectedStoreIds.add(defaultStoreId);
            wrapper.assignStores(selectedStoreIds);
        }

        //Set role assignments
        String userName = wrapper.getUserName();
        Date currentDate = SimDateUtil.getCurrentDate();
        Map<String, Role> availableRoles = wrapper.getAvailableRoles();
        Set<UserRole> selectedUserRoles = new HashSet<>();
        List<UserRole> userRoles = ClientServiceFactory.getSecurityServices().readUserRoles(user.getUserName());
        for (UserRole userRole : userRoles) {
            Role role = availableRoles.get(userRole.getRoleName());
            if (role == null) {
                completeCopy = false;
                continue;
            }
            Date endDate = userRole.getEndDate();
            if (endDate == null) {
                if (role.isDateRequired()) {
                    completeCopy = false;
                    continue;
                }
            } else if (currentDate.compareTo(endDate) >= 0) {
                completeCopy = false;
                continue;
            }
            Long storeId = userRole.getStoreId();
            if (storeId == null) {
                completeCopy = false;
                continue;
            }
            if (!wrapper.isSuperUser() && !selectedStoreIds.contains(storeId)) {
                completeCopy = false;
                continue;
            }
            if (!currentSuperUser && !availableStores.containsKey(storeId)) {
                completeCopy = false;
                continue;
            }
            UserRole newUserRole = BOFactory.createUserRole();
            newUserRole.setUserName(userName);
            newUserRole.setRoleName(userRole.getRoleName());
            newUserRole.setStoreId(storeId);
            if (endDate != null) {
                newUserRole.setEndDate(endDate);
            }
            selectedUserRoles.add(newUserRole);
        }
        wrapper.assignRoles(selectedUserRoles);
        return completeCopy;
    }

    public void saveUser() throws Exception {
        UserSaveVO userSaveVO = BOFactory.createUserSaveVO();
        String userName = wrapper.getUserName();
        userSaveVO.doSetUserName(userName);
        userSaveVO.doSetExternalUser(wrapper.isCached());
        if (!wrapper.isUserReadOnly()) {
            userSaveVO.doSetUser(wrapper.getUser());
            char[] password = wrapper.getPassword();
            if (!ArrayUtility.isNullOrEmpty(password)) {
                userSaveVO.doSetPassword(password);
            }
        }
        //Don't save store assignments for super users
        if (!wrapper.isSuperUser()) {
            userSaveVO.doSetUpdatedUserStores(wrapper.getUpdatedUserStores());
            userSaveVO.doSetRemovedUserStores(wrapper.getRemovedUserStores());
        }
        userSaveVO.doSetUpdatedUserRoles(wrapper.getUpdatedUserRoles());
        userSaveVO.doSetRemovedUserRoles(wrapper.getRemovedUserRoles());
        if (!userSaveVO.isEmpty()) {
            ClientServiceFactory.getSecurityServices().saveUser(userSaveVO, getStoreId());
            RepositoryManager.addStateObject(SimClientStateKey.USER_DETAIL_MODIFIED, Boolean.TRUE);
        }
    }

    public void clearState() {
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_USER);
    }
}
