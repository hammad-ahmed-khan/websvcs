package oracle.retail.sim.client.screen.invadjustment;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateQueryFilter;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateStatus;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Inventory Template Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryTemplateFilterDialogModel extends SimScreenModel {

    private InventoryAdjustmentTemplateQueryFilter filter;
    List<InventoryAdjustmentReason> availableReasons = new ArrayList<>();
    private List<String> templateUsernames = new ArrayList<>();

    public void setFilter(InventoryAdjustmentTemplateQueryFilter filter) {
        this.filter = filter;
    }

    public InventoryAdjustmentTemplateQueryFilter resetFilter() throws BusinessException {
        filter = BOFactory.createInventoryAdjustmentTemplateQueryFilter();
        filter.setStoreId(getStoreId());
        filter.setStatus(InventoryAdjustmentTemplateStatus.IN_PROGRESS);
        return filter;
    }

    public InventoryAdjustmentTemplateQueryFilter getFilter() {
        return filter;
    }

    public List<InventoryAdjustmentReason> getInventoryAdjustmentReasons() throws Exception {
        if (availableReasons.isEmpty()) {
            for (InventoryAdjustmentReason reason : ClientDataCacheUtility.getDisplayableInventoryAdjustmentReasons()) {
                if (hasDataPermission(PermissionKey.DATA_INV_ADJUSTMENT_REASON, reason.getId())) {
                    if (reason.isValidTemplateReason()) {
                        availableReasons.add(reason);
                    }
                }
            }
        }
        return availableReasons;
    }

    public InventoryAdjustmentReason getInventoryAdjustmentReason(Long reasonId) throws Exception {
        for (InventoryAdjustmentReason reason : getInventoryAdjustmentReasons()) {
            if (reason.getId().equals(reasonId)) {
                return reason;
            }
        }
        return null;
    }

    public List<String> findTemplateUsernames() throws Exception {
        if (templateUsernames.isEmpty()) {
            templateUsernames = ClientServiceFactory.getInventoryAdjustmentServices().findTemplateUsernames(getStoreId());
        }
        return templateUsernames;
    }
}
