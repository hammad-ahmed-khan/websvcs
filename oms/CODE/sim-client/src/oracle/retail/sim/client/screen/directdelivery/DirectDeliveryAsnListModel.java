package oracle.retail.sim.client.screen.directdelivery;

import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.directdelivery.DirectDelivery;
import oracle.retail.sim.common.directdelivery.DirectDeliveryVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Direct Delivery ASN List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class DirectDeliveryAsnListModel extends SimScreenModel {
    public List<DirectDeliveryVO> getOpenAsnDeliveryVOs() {
        return (List<DirectDeliveryVO>) RepositoryManager.getStateObject(SimClientStateKey.ASNS_FOR_SELECTION);
    }

    public boolean selectAsnDelivery(Long deliveryId) throws Exception {
        if (!obtainLock(ActivityLockType.DIRECT_DELIVERY, deliveryId.toString())) {
            return false;
        }
        DirectDelivery delivery = ClientServiceFactory.getDirectDeliveryServices().prepareDirectDeliveryAsnForPurchaseOrder(deliveryId);
        delivery.markInProgress();
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_DIRECT_DELIVERY, delivery);
        return true;
    }

    public void storeCanceled() {
        RepositoryManager.addStateObject(SimClientStateKey.DIRECT_DELIVERY_CANCELED, true);
    }

    public void clearState() {
        RepositoryManager.removeStateObject(SimClientStateKey.ASNS_FOR_SELECTION);
    }
}
