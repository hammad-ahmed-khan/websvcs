package oracle.retail.sim.client.screen.security;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.deliverytimeslot.DeliveryTimeSlot;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.security.PermissionSet;
import oracle.retail.sim.common.security.RoleType;
import oracle.retail.sim.common.security.UserType;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.stockcount.StockCountingMethod;
import oracle.retail.sim.common.stockreturn.ReturnReason;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Data Permissions Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DataPermissionDetailModel extends SimScreenModel {
    private RoleWrapper roleWrapper;
    private List<DataPermissionWrapper> dataPermissions;
    private List<InventoryAdjustmentReason> inventoryAdjustmentReasons;
    private List<ReturnReason> returnReasons;
    private List<DeliveryTimeSlot> deliveryTimeSlots;
    private List<RoleType> roleTypes;

    public RoleWrapper getRoleWrapper() {
        if (roleWrapper == null) {
            roleWrapper = (RoleWrapper) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_ROLE);
        }
        return roleWrapper;
    }

    public List<String> findDataPermissionNames() {
        return SimEnumUtility.findDataPermissionNames();
    }

    private List<InventoryAdjustmentReason> findInventoryAdjustmentReasons() throws Exception {
        if (inventoryAdjustmentReasons == null) {
            inventoryAdjustmentReasons = ClientServiceFactory.getInventoryAdjustmentServices().findAllInventoryAdjustmentReasons();
        }
        return inventoryAdjustmentReasons;
    }

    private List<DeliveryTimeSlot> findItemRequestDeliveryTimeSlots() throws Exception {
        if (deliveryTimeSlots == null) {
            deliveryTimeSlots = ClientServiceFactory.getItemRequestServices().findDeliveryTimeSlots();
        }
        return deliveryTimeSlots;
    }

    private List<RoleType> findRoleTypes() throws Exception {
        if (roleTypes == null) {
            roleTypes = ClientServiceFactory.getSecurityServices().findRoleTypes();
        }
        return roleTypes;
    }

    /*
     * Retrieve all four different types of data as actual objects/enums and then build wrappers around
     * them so that they can all be treated identically.
     */
    private List<DataPermissionWrapper> findDataPermissions() throws Exception {
        if (dataPermissions == null) {
            dataPermissions = new ArrayList<DataPermissionWrapper>();
            for (InventoryAdjustmentReason inventoryAdjustmentReason : findInventoryAdjustmentReasons()) {
                dataPermissions.add(ClientWrapperFactory.createDataPermissionWrapper(PermissionKey.DATA_INV_ADJUSTMENT_REASON, inventoryAdjustmentReason));
            }
            for (DeliveryTimeSlot itemRequestDeliveryTimeSlot : findItemRequestDeliveryTimeSlots()) {
                dataPermissions.add(ClientWrapperFactory.createDataPermissionWrapper(PermissionKey.DATA_ITEM_REQUEST_DELIVERY_TIMESLOT, itemRequestDeliveryTimeSlot));
            }
            for (ProductGroupType productGroupType : SimEnumUtility.findProductGroupTypes()) {
                dataPermissions.add(ClientWrapperFactory.createDataPermissionWrapper(PermissionKey.DATA_PRODUCT_GROUP_TYPE, productGroupType));
            }
            for (ReturnReason returnReason : findReturnReasons()) {
                dataPermissions.add(ClientWrapperFactory.createDataPermissionWrapper(PermissionKey.DATA_RETURN_REASON_CODE, returnReason));
            }

            for (SourceType sourceType : SimEnumUtility.findReturnSourceTypes()) {
                dataPermissions.add(ClientWrapperFactory.createDataPermissionWrapper(PermissionKey.DATA_RETURN_SOURCE, sourceType));
            }
            for (StockCountingMethod stockCountingMethod : SimEnumUtility.findStockCountingMethodsForPermissions()) {
                dataPermissions.add(ClientWrapperFactory.createDataPermissionWrapper(PermissionKey.DATA_COUNTING_METHOD, stockCountingMethod));
            }
            for (RoleType roleType : findRoleTypes()) {
                dataPermissions.add(ClientWrapperFactory.createDataPermissionWrapper(PermissionKey.DATA_ROLE_TYPE, roleType));
            }
            for (UserType userType : SimEnumUtility.findUserTypes()) {
                dataPermissions.add(ClientWrapperFactory.createDataPermissionWrapper(PermissionKey.DATA_USER_TYPE, userType));
            }
        }
        return dataPermissions;
    }

    private List<ReturnReason> findReturnReasons() throws Exception {
        if (returnReasons == null) {
            returnReasons = ClientServiceFactory.getReturnServices().findAllReturnReasons();
        }
        return returnReasons;
    }

    public List<DataPermissionWrapper> findAvailableDataPermissions(String dataPermissionName) throws Exception {
        findDataPermissions();

        if (StringHelper.isNullOrEmpty(dataPermissionName)) {
            return dataPermissions;
        }

        List<DataPermissionWrapper> availableDataPermissions = new ArrayList<DataPermissionWrapper>();
        for (DataPermissionWrapper dataPermission : dataPermissions) {
            if (dataPermissionName.equals(dataPermission.getName())) {
                availableDataPermissions.add(dataPermission);
            }
        }
        return availableDataPermissions;
    }

    public List<DataPermissionWrapper> findSelectedDataPermissions(List<DataPermissionWrapper> availableDataPermissions) throws Exception {
        List<DataPermissionWrapper> selectedDataPermissions = new ArrayList<DataPermissionWrapper>();
        PermissionSet permissionSet = roleWrapper.getPermissionSet();
        for (DataPermissionWrapper dataPermission : availableDataPermissions) {
            if (permissionSet.containsPermission(dataPermission.getName(), dataPermission.getKey(), dataPermission.getValue())) {
                selectedDataPermissions.add(dataPermission);
            }
        }
        return selectedDataPermissions;
    }
}
