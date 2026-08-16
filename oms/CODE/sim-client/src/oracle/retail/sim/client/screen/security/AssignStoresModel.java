package oracle.retail.sim.client.screen.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.security.UserStoresSaveVO;
import oracle.retail.sim.common.security.UserType;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Assign Stores Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class AssignStoresModel extends SimScreenModel {
    private List<UserType> availableUserTypes;
    private Map<Long, Store> availableStores;

    public List<UserType> findAvailableUserTypes() {
        if (availableUserTypes == null) {
            availableUserTypes = SimEnumUtility.findAvailableUserTypes();
        }
        return availableUserTypes;
    }

    public Map<Long, Store> findAvailableStores() {
        if (availableStores == null) {
            List<Store> stores = SimRepository.getAllowedStores();
            availableStores = new HashMap<Long, Store>(stores.size());
            for (Store store : stores) {
                availableStores.put(store.getId(), store);
            }
        }
        return availableStores;
    }

    public Store getStore(Long storeId) {
        return availableStores.get(storeId);
    }

    public void saveAssignments(List<UserWrapper> userWrappers, List<UserStoreAssignmentWrapper> assignmentWrappers) throws Exception {
        UserStoresSaveVO userStoresSaveVO = BOFactory.createUserStoresSaveVO();
        userStoresSaveVO.doSetSecurityUserName(getUserName());
        List<User> users = new ArrayList<User>(userWrappers.size());
        for (UserWrapper userWrapper : userWrappers) {
            users.add(userWrapper.getUser());
        }
        userStoresSaveVO.doSetUsers(users);
        List<Long> addedStoreAssignments = new ArrayList<Long>();
        List<Long> removedStoreAssignments = new ArrayList<Long>();
        for (UserStoreAssignmentWrapper assignmentWrapper : assignmentWrappers) {
            Long storeId = assignmentWrapper.getStoreId();
            if (storeId == null) {
                continue;
            }
            switch (assignmentWrapper.getAction()) {
                case ADD:
                    addedStoreAssignments.add(storeId);
                    break;
                case DELETE:
                    removedStoreAssignments.add(storeId);
                    break;
                case DEFAULT:
                    userStoresSaveVO.doSetDefaultStoreId(storeId);
                    break;
            }
        }
        userStoresSaveVO.doSetAddedStoreAssignments(addedStoreAssignments);
        userStoresSaveVO.doSetRemovedStoreAssignments(removedStoreAssignments);
        if (!userStoresSaveVO.isEmpty()) {
            ClientServiceFactory.getSecurityServices().saveUserStores(userStoresSaveVO, getStoreId());
        }
    }
}
