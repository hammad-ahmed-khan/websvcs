package oracle.retail.sim.client.screen.invadjustment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReasonProperty;
import oracle.retail.sim.common.item.InventoryDisposition;
import oracle.retail.sim.common.item.NonSellableQtyType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Inventory Adjustment Reason Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryAdjustmentReasonModel extends SimScreenModel {

    private List<Long> deletedReasonIds = new ArrayList<>();

    private Set<Integer> activelyUsedReasonCodes;

    public List<InventoryAdjustmentReasonWrapper> getInventoryAdjustmentReasonWrappers() throws Exception {
        List<InventoryAdjustmentReason> reasons = ClientServiceFactory.getInventoryAdjustmentServices().findAllInventoryAdjustmentReasons();
        List<InventoryAdjustmentReasonWrapper> wrappers = new ArrayList<>(reasons.size());
        for (InventoryAdjustmentReason reason : reasons) {
            wrappers.add(new InventoryAdjustmentReasonWrapper(reason));
        }
        return wrappers;
    }

    public Set<Integer> loadActivelyUsedReasonCodes() throws Exception {
        if (activelyUsedReasonCodes == null) {
            activelyUsedReasonCodes = ClientServiceFactory.getInventoryAdjustmentServices().findInUseInventoryAdjustmentReasonCodes();
        }
        return activelyUsedReasonCodes;
    }

    public InventoryAdjustmentReasonWrapper createInventoryAdjustmentReasonWrapper() throws Exception {
        return new InventoryAdjustmentReasonWrapper(BOFactory.createInventoryAdjustmentReason());
    }

    public void deleteReason(InventoryAdjustmentReasonWrapper wrapper) {
        if (wrapper.getId() != null) {
            deletedReasonIds.add(wrapper.getId());
        }
    }

    public boolean obtainReasonAdminLock() throws Exception {
        return obtainLock(ActivityLockType.INVENTORY_ADJUSTMENT_REASON, InventoryAdjustmentReasonProperty.LOCK_ID);
    }

    public boolean confirmReasonAdminLock() throws Exception {
        return confirmLock(ActivityLockType.INVENTORY_ADJUSTMENT_REASON, InventoryAdjustmentReasonProperty.LOCK_ID);
    }

    public void releaseReasonAdminLock() throws Exception {
        releaseLock(ActivityLockType.INVENTORY_ADJUSTMENT_REASON, InventoryAdjustmentReasonProperty.LOCK_ID);
    }

    public void updateInventoryAdjustmentReasons(List<InventoryAdjustmentReasonWrapper> wrappers) throws Exception {
        List<InventoryAdjustmentReason> modifiedReasons = new ArrayList<>();
        for (InventoryAdjustmentReasonWrapper wrapper : wrappers) {
            InventoryAdjustmentReason reason = wrapper.getReason();
            if (reason.isDirty() && reason.isCoherent()) {
                modifiedReasons.add(reason);
            }
        }
        ClientServiceFactory.getInventoryAdjustmentServices().updateInventoryAdjustmentReasons(modifiedReasons, deletedReasonIds);
    }

    public List<NonSellableQtyType> getNonSellableQtyTypes() throws Exception {
        if (isNonSellableTypesActive()) {
            return ClientDataCacheUtility.getNonSellableQtyTypes();
        }
        return Collections.emptyList();
    }

    public List<InventoryDisposition> getInventoryDispositions() {
        List<InventoryDisposition> dispositions = new ArrayList<>();
        dispositions.add(InventoryDisposition.AVAILABLE_SOH_TO_UNAVAILABLE_SOH);
        dispositions.add(InventoryDisposition.AVAILABLE_TO_CUSTOMER_RESERVED);
        dispositions.add(InventoryDisposition.AVAILABLE_TO_OUT);
        dispositions.add(InventoryDisposition.CUSTOMER_RESERVED_TO_AVAILABLE);
        dispositions.add(InventoryDisposition.OUT_TO_AVAILABLE);
        dispositions.add(InventoryDisposition.UNAVAILABLE_SOH_TO_AVAILABLE_SOH);
        dispositions.add(InventoryDisposition.UNAVAILABLE_TO_OUT);
        if (isNonSellableTypesActive()) {
            dispositions.add(InventoryDisposition.UNAVAILABLE_TO_UNAVAILABLE);
        }
        return dispositions;
    }
}
