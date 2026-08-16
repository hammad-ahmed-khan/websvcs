package oracle.retail.sim.client.screen.returns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.item.NonSellableQtyType;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.stockreturn.ReturnReason;
import oracle.retail.sim.common.stockreturn.ReturnReasonProperty;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Return Reason Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ReturnReasonModel extends SimScreenModel {

    private List<ReturnReason> returnReasons = null;
    private Set<ReturnReason> activeReasons = null;
    private List<Long> deletedReasonIds = new ArrayList<>();

    public void loadReturnReasons() throws Exception {
        returnReasons = ClientServiceFactory.getReturnServices().findAllReturnReasons();
    }

    public List<ReturnReasonWrapper> getFilteredReasons(SourceType sourceType) {
        if (sourceType == null) {
            return getWrappedReasons(returnReasons);
        }
        List<ReturnReason> filteredReasons = new ArrayList<>();
        for (ReturnReason reason : returnReasons) {
            if (reason.getType() == sourceType) {
                filteredReasons.add(reason);
            }
        }
        return getWrappedReasons(filteredReasons);
    }

    private List<ReturnReasonWrapper> getWrappedReasons(List<ReturnReason> reasons) {
        List<ReturnReasonWrapper> wrappers = new ArrayList<>(reasons.size());
        for (ReturnReason reason : reasons) {
            wrappers.add(ClientWrapperFactory.createReturnReasonWrapper(reason));
        }
        return wrappers;
    }

    public Set<ReturnReason> loadActivelyUsedReasons() throws Exception {
        if (activeReasons == null) {
            activeReasons = ClientServiceFactory.getReturnServices().findInUseReturnReasons();
        }
        return activeReasons;
    }

    public ReturnReasonWrapper buildReturnReasonWrapper() throws Exception {
        ReturnReason returnReason = BOFactory.createReturnReason();
        returnReasons.add(returnReason);
        return ClientWrapperFactory.createReturnReasonWrapper(returnReason);
    }

    public void deleteReason(ReturnReason reason) {
        if (reason.getId() != null) {
            deletedReasonIds.add(reason.getId());
        }
        returnReasons.remove(reason);
    }

    public boolean obtainReasonAdminLock() throws Exception {
        return obtainLock(ActivityLockType.RETURN_REASON, ReturnReasonProperty.LOCK_ID);
    }

    public boolean confirmReasonAdminLock() throws Exception {
        return confirmLock(ActivityLockType.RETURN_REASON, ReturnReasonProperty.LOCK_ID);
    }

    public void releaseReasonAdminLock() throws Exception {
        releaseLock(ActivityLockType.RETURN_REASON, ReturnReasonProperty.LOCK_ID);
    }

    public void updateReturnReasons(List<ReturnReason> modifiedReasons) throws Exception {
        ClientServiceFactory.getReturnServices().updateReturnReasons(modifiedReasons, deletedReasonIds);
    }

    public List<NonSellableQtyType> getNonSellableQtyTypes() throws Exception {
        if (isNonSellableTypesActive()) {
            return ClientDataCacheUtility.getNonSellableQtyTypes();
        }
        return Collections.emptyList();
    }
}
