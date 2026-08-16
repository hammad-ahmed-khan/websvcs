package oracle.retail.sim.client.screen.invadjustment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentQueryFilter;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentStatus;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateQueryFilter;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateVO;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.NonSellableQtyType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Inventory Adjustment Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryAdjustmentFilterDialogModel extends SimScreenModel {

    private InventoryAdjustmentQueryFilter filter;
    private List<String> usernames;
    private List<InventoryAdjustmentTemplateVO> templateVOs;

    public void setFilter(InventoryAdjustmentQueryFilter filter) {
        this.filter = filter;
    }

    public InventoryAdjustmentQueryFilter getFilter() {
        return filter;
    }

    public InventoryAdjustmentQueryFilter resetFilter() throws BusinessException {
        filter = BOFactory.createInventoryAdjustmentQueryFilter();
        filter.setStoreId(getStoreId());
        filter.setStatus(InventoryAdjustmentStatus.IN_PROGRESS);
        filter.setSearchLimit(getDefaultSearchLimit());
        return filter;
    }

    private Integer getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_INV_ADJUSTMENT);
    }

    public ItemVO loadItem() {
        return (ItemVO) RepositoryManager.getStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_FILTER_ITEM_VO);
    }

    public List<InventoryAdjustmentReason> getReasonList() throws Exception {
        List<InventoryAdjustmentReason> filteredReasons = new ArrayList<>();
        for (InventoryAdjustmentReason reason : ClientDataCacheUtility.getInventoryAdjustmentReasons()) {
            if (reason.isDisplayable()) {
                filteredReasons.add(reason);
            }
        }
        return filteredReasons;
    }

    public NonSellableQtyType getNonSellableQtyType(Long nonSellableTypeId) throws Exception {
        for (NonSellableQtyType type : ClientDataCacheUtility.getNonSellableQtyTypes()) {
            if (type.getId().equals(nonSellableTypeId)) {
                return type;
            }
        }
        return null;
    }

    public InventoryAdjustmentReason getInventoryAdjustmentReason(Long reasonId) throws Exception {
        for (InventoryAdjustmentReason reason : getReasonList()) {
            if (reason.getId().equals(reasonId)) {
                return reason;
            }
        }
        return null;
    }

    public List<String> getUsernameList() throws Exception {
        if (usernames == null) {
            usernames = ClientServiceFactory.getInventoryAdjustmentServices().findInventoryAdjustmentUsernames(getStoreId());
        }
        return usernames;
    }

    public List<InventoryAdjustmentStatus> getStatusList() {
        return Arrays.asList(InventoryAdjustmentStatus.values());
    }

    public List<NonSellableQtyType> getNonSellableTypeList() throws Exception {
        return ClientDataCacheUtility.getNonSellableQtyTypes();
    }

    public List<InventoryAdjustmentTemplateVO> getTemplateList() throws Exception {
        if (templateVOs == null) {
            InventoryAdjustmentTemplateQueryFilter filter = BOFactory.createInventoryAdjustmentTemplateQueryFilter();
            filter.doSetStoreId(getStoreId());
            templateVOs = ClientServiceFactory.getInventoryAdjustmentServices().findTemplateVOs(filter);
        }
        return templateVOs;
    }

    public InventoryAdjustmentTemplateVO getTemplateVO(Long templateId) throws Exception {
        for (InventoryAdjustmentTemplateVO templateVO : getTemplateList()) {
            if (templateVO.getId().equals(templateId)) {
                return templateVO;
            }
        }
        return null;
    }
}
