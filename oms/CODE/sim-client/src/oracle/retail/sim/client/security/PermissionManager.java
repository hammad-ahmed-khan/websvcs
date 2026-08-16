package oracle.retail.sim.client.security;

import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.security.PermissionSet;

/**
 * Permission Manager
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class PermissionManager {
    private PermissionManager() {
    }

    public static PermissionSet getPermissions() {
        return SimRepository.getPermissions();
    }

    public static void setPermissions(PermissionSet permissions) {
        SimRepository.setPermissions(permissions);
    }

    /**
     * Validates if the current user session is authorized to access the specified permission.
     * <p>
     * @param permissionName The name of the permission to validate.
     * @return true if the access is allowed, false otherwise.
     */
    public static boolean hasPermission(String permissionName) {
        return getPermissions().containsPermission(permissionName);
    }

    /**
     * Validates if the current user session is authorized to access the specified data permission and parameter.
     * <p>
     * @param permissionName The name of the data permission to validate.
     * @param value The parameter to validate for the data permission.
     * @return true if the access is allowed, false otherwise.
     */
    public static boolean hasDataPermission(String permissionName, String value) {
        return getPermissions().containsPermission(permissionName, PermissionKey.DATA_VALUE_KEY, value);
    }

    /**
     * Validates if the current user session is authorized to access the specified data permission and parameter.
     * <p>
     * @param permissionName The name of the data permission to validate.
     * @param value The parameter to validate for the data permission.
     * @return true if the access is allowed, false otherwise.
     */
    public static boolean hasDataPermission(String permissionName, Object value) {
        return hasDataPermission(permissionName, String.valueOf(value));
    }

    /**
     * Validates if the current user session is authorized to access the specified data permission and parameter.
     * <p>
     * @param permissionName The name of the data permission to validate.
     * @param value The parameter to validate for the data permission.
     * @return true if the access is allowed, false otherwise.
     */
    public static boolean hasDataPermission(String permissionName, int value) {
        return hasDataPermission(permissionName, String.valueOf(value));
    }
}
