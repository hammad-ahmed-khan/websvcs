package oracle.retail.sim.client.screen.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.security.UserQueryFilter;
import oracle.retail.sim.common.security.UserStatus;
import oracle.retail.sim.common.security.UserType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * User List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UserListModel extends SimScreenModel {
    private List<UserType> availableUserTypes;
    private boolean restrictedUserTypes;

    public UserQueryFilter getFilter() {
        UserQueryFilter filter = (UserQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.USER_FILTER);
        if (filter == null) {
            filter = BOFactory.createUserQueryFilter();
            filter.doSetStoreId(getStoreId());
            filter.doSetDefaultStore(true);
            filter.doSetStatus(UserStatus.ACTIVE);
            filter.doSetSearchLimit(1000);
            if (restrictedUserTypes) {
                filter.doSetTypes(availableUserTypes);
            }
            RepositoryManager.addStateObject(SimClientStateKey.USER_FILTER, filter);
        }
        return filter;
    }

    public void loadAvailableUserTypes() {
        availableUserTypes = SimEnumUtility.findAvailableUserTypes();
        restrictedUserTypes = availableUserTypes.size() != SimEnumUtility.findUserTypes().size();
    }

    public List<UserType> getAvailableUserTypes() {
        return availableUserTypes;
    }

    public boolean isRestrictedUserTypes() {
        return restrictedUserTypes;
    }

    public List<UserWrapper> findUsers() throws Exception {
        if (restrictedUserTypes && availableUserTypes.isEmpty()) {
            return Collections.emptyList();
        }
        List<User> users = ClientServiceFactory.getSecurityServices().findUsers(getFilter());
        List<UserWrapper> wrappers = new ArrayList<UserWrapper>(users.size());
        for (User user : users) {
            wrappers.add(ClientWrapperFactory.createUserWrapper(user));
        }
        return wrappers;
    }

    public void deleteUsers(List<String> userNames) throws Exception {
        if (userNames.size() == 1) {
            ClientServiceFactory.getSecurityServices().deleteUser(userNames.get(0), getStoreId());
            return;
        }
        ClientServiceFactory.getSecurityServices().deleteUsers(userNames, getStoreId());
    }

    public void storeUser(UserWrapper wrapper) {
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_USER, ClientWrapperFactory.createUserDetailWrapper(wrapper.getUser()));
    }

    public Map<String, String> getFilterDescriptionMap() {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        UserQueryFilter filter = getFilter();
        if (filter.getUserName() != null) {
            descriptionMap.put("Username", filter.getUserName());
        }
        if (filter.getFirstName() != null) {
            descriptionMap.put("First Name", filter.getFirstName());
        }
        if (filter.getLastName() != null) {
            descriptionMap.put("Last Name", filter.getLastName());
        }
        if (filter.getRoleName() != null) {
            descriptionMap.put("Role", filter.getRoleName());
        }
        if (filter.getTypes().size() == 1) {
            descriptionMap.put("Type", filter.getTypes().get(0).toString());
        }
        if (filter.getStatus() != null) {
            descriptionMap.put("Status", filter.getStatus().toString());
        }
        if (filter.getCreateDateMin() != null) {
            descriptionMap.put("Create Date", LocaleManager.getShortDateFormatter().format(filter.getCreateDateMin()));
        }
        if (filter.getStartDateMin() != null) {
            descriptionMap.put("Start Date", LocaleManager.getShortDateFormatter().format(filter.getStartDateMin()));
        }
        if (filter.getStoreId() != null) {
            descriptionMap.put("Store", filter.getStoreId().toString());
        }
        if (filter.isDefaultStore() != null) {
            descriptionMap.put("Default Store", filter.isDefaultStore().toString());
        }
        if (filter.getComments() != null) {
            descriptionMap.put("Comments", filter.getComments());
        }
        return descriptionMap;
    }
}
