package oracle.retail.sim.client.core;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.swing.util.Repository;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.report.SessionPrinter;
import oracle.retail.sim.common.security.PermissionSet;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.store.SimStore;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * SIM REPOSITORY
 * <p>
 * Client side repository for storing global information.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimRepository {
    private static Repository repository = new Repository();

    private static final String USER = "User";
    private static final String STORE = "Store";
    private static final String SIMSTORE = "SimStore";
    private static final String ALLOWEDSTORES = "AllowedStores";
    private static final String PERMISSIONS = "Permissions";
    private static final String SESSION_PRINTERS = "sessionPrinters";

    /****************************************************************************************************
     * User
     ***************************************************************************************************/
    public static void setUser(User user) {
        repository.put(USER, user);
    }

    public static User getUser() {
        return (User) repository.get(USER);
    }

    public static String getUserName() {
        User user = getUser();
        if (user != null) {
            return user.getUserName();
        }
        return StringConstants.EMPTY;
    }

    /****************************************************************************************************
     * Store
     ***************************************************************************************************/
    public static void setStore(Store store) {
        repository.put(STORE, store);
        repository.put(SIMSTORE, new SimStore(store));
    }

    public static Store getStore() {
        return (Store) repository.get(STORE);
    }

    public static Long getStoreId() {
        Store store = getStore();
        if (store != null) {
            return store.getId();
        }
        return null;
    }

    /****************************************************************************************************
     * Sim Store
     ***************************************************************************************************/
    public static SimStore getSimStore() {
        return (SimStore) repository.get(SIMSTORE);
    }

    /****************************************************************************************************
     * Allowed Stores
     ***************************************************************************************************/
    public static List<Store> getAllowedStores() {
        List<Store> allowedStores = (List<Store>) repository.get(ALLOWEDSTORES);
        if (allowedStores == null) {
            return Collections.emptyList();
        }
        return allowedStores;
    }

    public static void setAllowedStores(List<Store> stores) {
        repository.put(ALLOWEDSTORES, stores);
    }

    /****************************************************************************************************
     * Allowed Permissions
     ***************************************************************************************************/
    public static PermissionSet getPermissions() {
        PermissionSet permissions = (PermissionSet) repository.get(PERMISSIONS);
        if (permissions == null) {
            permissions = BOFactory.createPermissionSet();
        }
        return permissions;
    }

    public static void setPermissions(PermissionSet permissions) {
        repository.put(PERMISSIONS, permissions);
    }

    /************************************************************************
     * Session Printers
     ************************************************************************/
    public static List<SessionPrinter> getSessionPrinters() {
       return (List<SessionPrinter>) repository.get(SESSION_PRINTERS);
    }

    public static void setSessionPrinters(List<SessionPrinter> sessionPrinters) {
        repository.put(SESSION_PRINTERS, sessionPrinters);
    }

    /****************************************************************************************************
     * Generate Keys and Values
     ***************************************************************************************************/
    public static Set<String> getStateKeys() {
        return repository.getAllKeys();
    }

    public static Object getStateObject(String key) {
        return repository.get(key);
    }

    /****************************************************************************************************
     * Cleanup
     ***************************************************************************************************/
    public static void clearRepository() {
        repository.clearRepository();
    }
}
