package oracle.retail.sim.client.core;

import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.security.PermissionManager;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.activitylock.ActivityLockUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.store.SimStore;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * SimScreenModel is the superclass of ALL SIM screen or panel model. This should be subclassed and all
 * SIM business logic or logic that accesses services should be placed here.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class SimScreenModel {
    /****************************************************************************************************
     * Retrieves the application user.
     * <p>
     * @return The logged in User.
     ***************************************************************************************************/
    public User getUser() {
        return SimRepository.getUser();
    }

    /****************************************************************************************************
     * Retrieves the application User Name.
     * <p>
     * @return The application user name.
     ***************************************************************************************************/
    public String getUserName() {
        return SimRepository.getUserName();
    }

    /****************************************************************************************************
     * Return true if the user is a super user, otherwise false.
     ***************************************************************************************************/
    public boolean isSuperUser() {
        return SimRepository.getUser().isSuperUser();
    }

    /****************************************************************************************************
     * Retrieves the application store.
     * <p>
     * @return The application store.
     ***************************************************************************************************/
    public Store getStore() {
        return SimRepository.getStore();
    }

    /****************************************************************************************************
     * Retrieves the application store Id
     * <p>
     * @return The application store id
     ***************************************************************************************************/
    public Long getStoreId() {
        return SimRepository.getStoreId();
    }

    /****************************************************************************************************
     * Retrieves the sim store (allows buddy stores)
     * <p>
     * @return The sim store.
     ***************************************************************************************************/
    public static SimStore getSimStore() {
        return SimRepository.getSimStore();
    }

    /****************************************************************************************************
     * Retrieves the employee's allowed stores.
     * <p>
     * @return The allowed stores.
     ***************************************************************************************************/
    public List<Store> getAllowedStores() {
        return SimRepository.getAllowedStores();
    }

    /****************************************************************************************************
     * Retrieves the application time zone.
     * <p>
     * @return The application time zone.
     ***************************************************************************************************/
    public TimeZone getTimeZone() {
        return SimRepository.getStore().getTimeZone();
    }

    /****************************************************************************************************
     * Retrieves the state object for the key.
     * <p>
     * @return The state object for the key.
     ***************************************************************************************************/
    public Object getStateObject(String key) {
        return RepositoryManager.getStateObject(key);
    }

    /****************************************************************************************************
     * Validates if the current user session is authorized to access the specified permission.
     * <p>
     * @param permissionName The name of the permission to validate.
     * @return true if the access is allowed, false otherwise.
     ***************************************************************************************************/
    public boolean hasPermission(String permissionName) {
        return PermissionManager.hasPermission(permissionName);
    }

    /****************************************************************************************************
     * Validates if the current user session is authorized to access the specified data permission and parameter.
     * <p>
     * @param permissionName The name of the data permission to validate.
     * @param value The parameter to validate for the data permission.
     * @return true if the access is allowed, false otherwise.
     ***************************************************************************************************/
    public boolean hasDataPermission(String permissionName, String value) {
        return PermissionManager.hasDataPermission(permissionName, value);
    }

    /****************************************************************************************************
     * Validates if the current user session is authorized to access the specified data permission and parameter.
     * <p>
     * @param permissionName The name of the data permission to validate.
     * @param value The parameter to validate for the data permission.
     * @return true if the access is allowed, false otherwise.
     ***************************************************************************************************/
    public boolean hasDataPermission(String permissionName, Object value) {
        return PermissionManager.hasDataPermission(permissionName, value);
    }

    /****************************************************************************************************
     * Validates if the current user session is authorized to access the specified data permission and parameter.
     * <p>
     * @param permissionName The name of the data permission to validate.
     * @param value The parameter to validate for the data permission.
     * @return true if the access is allowed, false otherwise.
     ***************************************************************************************************/
    public boolean hasDataPermission(String permissionName, int value) {
        return PermissionManager.hasDataPermission(permissionName, value);
    }

    /****************************************************************************************************
     * Returns true if the store configuration value is set.
     ***************************************************************************************************/
    public boolean getStoreBoolean(String storeConfigKey) {
        return SimConfigManager.getStoreBoolean(storeConfigKey, getStoreId());
    }

    /****************************************************************************************************
     * Returns Integer of the store configuration value
     ***************************************************************************************************/
    public Integer getStoreInteger(String storeConfigKey) {
        return SimConfigManager.getStoreInteger(storeConfigKey, getStoreId());
    }

    /****************************************************************************************************
     * Returns String of the store configuration value
     ***************************************************************************************************/
    public String getStoreString(String storeConfigKey) {
        return SimConfigManager.getStoreString(storeConfigKey, getStoreId());
    }

    /****************************************************************************************************
     * Returns true if serial number processing is enabled for the store.
     ***************************************************************************************************/
    public boolean isSerialNumberProcessingEnabled() {
        return SimConfigManager.getStoreBoolean(StoreConfigKeys.UIN_PROCESSING_ENABLED, getStoreId());
    }

    /****************************************************************************************************
     * Returns true if non sellable types are active in the system
     ***************************************************************************************************/
    public boolean isNonSellableTypesActive() {
        return SimConfigManager.getBoolean(SimConfigManager.ENABLE_SUB_BUCKETS);
    }

    /****************************************************************************************************
     * Returns true if advanced item entry screen is enabled for the system.
     ***************************************************************************************************/
    public boolean isScannerAutoDisplay() {
        return SimConfigManager.getStoreBoolean(StoreConfigKeys.USE_ADVANCED_ITEM_ENTRY, getStoreId());
    }
    
    /****************************************************************************************************
     * Returns true if AllowUnexpectedUINs is enabled for the system.
     ***************************************************************************************************/
    public boolean isAllowUnexpectedUINs() {
        return SimConfigManager.getBoolean(SimConfigManager.ALLOW_UNEXPECTED_UINS);
    }

    /****************************************************************************************************
     * Release the lock held by the caller. Displays a message dialog if the lock could not be released.
     ***************************************************************************************************/
    protected void releaseLock(ActivityLockType activityType, Long activityId) throws Exception {
        ActivityLockUtility.releaseSessionActivityLock(activityType, activityId);
    }

    /****************************************************************************************************
     * Release the lock held by the caller. Displays a message dialog if the lock could not be released.
     ***************************************************************************************************/
    protected void releaseLock(ActivityLockType activityType, String activityId) throws Exception {
        ActivityLockUtility.releaseSessionActivityLock(activityType, activityId);
    }

    /****************************************************************************************************
     * Check to make sure user STILL has the lock. Needed because it may happen that user-A had lock then
     * user-B came along, broke the lock and changed things and saved (i.e. lock was released). Now when
     * user-A goes tries to finish her work, she may overwrite user-B's changes.
     * <p>
     * @param activityId The id to check
     * @return True if the lock is still obtained.
     ***************************************************************************************************/
    protected boolean confirmLock(ActivityLockType activityType, Long activityId) throws Exception {
        return ActivityLockUtility.confirmActivityLock(activityType, activityId);
    }

    /****************************************************************************************************
     * Check to make sure user STILL has the lock. Needed because it may happen that user-A had lock then
     * user-B came along, broke the lock and changed things and saved (i.e. lock was released). Now when
     * user-A goes tries to finish her work, she may overwrite user-B's changes.
     * <p>
     * @param activityId The id to check
     * @return True if the lock is still obtained.
     ***************************************************************************************************/
    protected boolean confirmLock(ActivityLockType activityType, String activityId) throws Exception {
        return ActivityLockUtility.confirmActivityLock(activityType, activityId);
    }

    /****************************************************************************************************
     * Lock the provided id of the given type. If a lock is already held the user is given the option of
     * breaking the existing lock.
     * <p>
     * @param activityType The type of activity to lock.
     * @param activityId The ID of the business object to lock.
     * @return True if the lock was obtained successfully; false otherwise.
     ***************************************************************************************************/
    protected boolean obtainLock(ActivityLockType activityType, Long activityId) throws Exception {
        return obtainLock(activityType, activityId, CommonMessageText.LOCK_HELD_CONFIRM);
    }

    /****************************************************************************************************
     * Lock the provided id of the given type. If a lock is already held the user is given the option of
     * breaking the existing lock.
     * <p>
     * @param activityType The type of activity to lock.
     * @param activityId The ID of the business object to lock.
     * @return True if the lock was obtained successfully; false otherwise.
     ***************************************************************************************************/
    protected boolean obtainLock(ActivityLockType activityType, String activityId) throws Exception {
        return obtainLock(activityType, activityId, CommonMessageText.LOCK_HELD_CONFIRM);
    }

    /****************************************************************************************************
     * Lock the provided id of the given type. If a lock is already held the user is given the option of
     * breaking the existing lock.
     * <p>
     * @param activityId The ID of the business object to lock.
     * @param message The message to display if an ActivityLocationException occurs.
     * @return True if the lock was obtained successfully; false otherwise.
     ***************************************************************************************************/
    protected boolean obtainLock(ActivityLockType activityType, Long activityId, MessageText message) throws BusinessException {
        if (activityId == null) {
            LogService.warn(this, "Invalid ActivityId");
            return false;
        }
        return obtainLock(activityType, activityId.toString(), message);
    }

    /****************************************************************************************************
     * Lock the provided id of the given type. If a lock is already held the user is given the option of
     * breaking the existing lock.
     * <p>
     * @param activityId The ID of the business object to lock.
     * @param message The message to display if an ActivityLocationException occurs.
     * @return True if the lock was obtained successfully; false otherwise.
     ***************************************************************************************************/
    protected boolean obtainLock(ActivityLockType activityType, String activityId, MessageText message) throws BusinessException {
        String lockOwner = StringConstants.EMPTY;
        try {
            if (ActivityLockUtility.createActivityLock(activityType, activityId)) {
                return true;
            }
            Set<String> lockOwners = ActivityLockUtility.findActivityLockOwners(activityType, activityId);
            if (!lockOwners.isEmpty()) {
                lockOwner = lockOwners.iterator().next();
            }
            if (!RConfirmUtility.confirm("Break Lock Confirmation", message, lockOwner)) {
                return false;
            }
            if (ActivityLockUtility.overrideActivityLock(activityType, activityId)) {
                return true;
            }
        } catch (Throwable t) {
            throw new BusinessException(CommonMessageText.LOCK_NOT_GRANTED, t);
        }
        throw new BusinessException(CommonMessageText.LOCK_HELD_ERROR, lockOwner);
    }
}
