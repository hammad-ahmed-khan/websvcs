package oracle.retail.sim.client.screen.tolerances;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.tolerance.ToleranceAdmin;
import oracle.retail.sim.common.tolerance.ToleranceTopic;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Tolerances Admin Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TolerancesAdminModel extends SimScreenModel {
    private List<ToleranceAdmin> adhocAdminData = new ArrayList<>();
    private List<ToleranceAdmin> pickAdminData = new ArrayList<>();

    public void loadData() throws Exception {
        List<ToleranceAdmin> admins = ClientServiceFactory.getToleranceAdminServices().findAllToleranceAdmins(getStoreId());
        for (ToleranceAdmin admin : admins) {
            if (admin.getTopic() == ToleranceTopic.ADHOC_STOCK_COUNT) {
                adhocAdminData.add(admin);
            } else if (admin.getTopic() == ToleranceTopic.FULFILLMENT_ORDER_PICKING) {
                pickAdminData.add(admin);
            }
        }
    }

    public List<ToleranceAdmin> getAdhocAdminData() throws Exception {
        return adhocAdminData;
    }

    public List<ToleranceAdmin> getPickAdminData() throws Exception {
        return pickAdminData;
    }

    public void updateAdhocAdminData() throws Exception {
        List<ToleranceAdmin> updatedDataRecords = new ArrayList<>();
        for (ToleranceAdmin adhocCountAdmin : adhocAdminData) {
            if (adhocCountAdmin.isDirty()) {
                if (!adhocCountAdmin.isCoherent()) {
                    throw new BusinessException(CommonMessageText.MISSING_VARIANCE_VALUE);
                }
                updatedDataRecords.add(adhocCountAdmin);
            }
        }
        for (ToleranceAdmin adhocCountAdmin : pickAdminData) {
            if (adhocCountAdmin.isDirty()) {
                if (!adhocCountAdmin.isCoherent()) {
                    throw new BusinessException(CommonMessageText.MISSING_VARIANCE_VALUE);
                }
                updatedDataRecords.add(adhocCountAdmin);
            }
        }
        if (!updatedDataRecords.isEmpty()) {
            ClientServiceFactory.getToleranceAdminServices().update(updatedDataRecords);
        }
    }
}
