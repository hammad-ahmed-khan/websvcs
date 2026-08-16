package oracle.retail.sim.client.screen.productgroup;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.productgroup.ProductGroup;
import oracle.retail.sim.common.productgroup.ProductGroupQueryFilter;
import oracle.retail.sim.common.productgroup.ProductGroupStatus;
import oracle.retail.sim.common.productgroup.ProductGroupVO;
import oracle.retail.sim.common.schedule.ProductGroupScheduleQueryFilter;
import oracle.retail.sim.common.schedule.ProductGroupScheduleVO;
import oracle.retail.sim.common.schedule.ScheduleStatus;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.productgroup.ProductGroupServices;

/********************************************************************************************************
 * Product Group List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupListModel extends SimScreenModel {
    /****************************************************************************************************
     * If filter is null, creates a new one and adds to state repository. If super user, see "all" groups
     * in the list as well as store, else only show store.
     ***************************************************************************************************/
    public List<ProductGroupVO> findProductGroups() throws Exception {
        return ClientServiceFactory.getProductGroupServices().findProductGroupVOs(getFilter());
    }

    /****************************************************************************************************
     * Retrieve the filter. Create if necessary.
     ***************************************************************************************************/
    public ProductGroupQueryFilter getFilter() {
        ProductGroupQueryFilter filter = (ProductGroupQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.PRODUCT_GROUP_FILTER);
        if (filter == null) {
            filter = BOFactory.createProductGroupQueryFilter();
            filter.doSetStoreId(getStoreId());
            RepositoryManager.addStateObject(SimClientStateKey.PRODUCT_GROUP_FILTER, filter);
        }
        return filter;
    }

    /****************************************************************************************************
     * If no activity lock, do not save. Product groups are canceled and then purged at a later date.
     ***************************************************************************************************/
    public void cancelProductGroups(List<ProductGroupVO> productGroups) throws Exception {
        ProductGroupServices productGroupServices = ClientServiceFactory.getProductGroupServices();
        for (ProductGroupVO groupVO : productGroups) {
            if (obtainProductGroupLock(groupVO.getId())) {
                ProductGroup productGroup = productGroupServices.readProductGroup(groupVO.getId());
                productGroup.doSetStatus(ProductGroupStatus.CANCELED);
                productGroupServices.update(productGroup);
                releaseProductGroupLock(groupVO.getId());
            }
        }
    }

    /****************************************************************************************************
     * If no activity lock, do not save. Product groups are canceled and then purged at a later date.
     ***************************************************************************************************/
    public boolean isAttachedToSchedule(Long groupId) throws Exception {
        ProductGroupScheduleQueryFilter filter = BOFactory.createProductGroupScheduleQueryFilter();
        filter.doSetStatus(ScheduleStatus.OPEN);
        filter.doSetGroupId(groupId);

        List<ProductGroupScheduleVO> attachedCounts = ClientServiceFactory.getProductGroupScheduleServices().findProductGroupScheduleVOs(filter);
        if (attachedCounts.isEmpty()) {
            return false;
        }
        return true;
    }

    public void storeProductGroup(ProductGroupVO groupVO) throws Exception {
        ProductGroup productGroup = ClientServiceFactory.getProductGroupServices().readProductGroup(groupVO.getId());
        RepositoryManager.addStateObject(SimClientStateKey.PRODUCT_GROUP_DETAIL, productGroup);
    }

    public Map<String, String> getDescriptionMap() {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        ProductGroupQueryFilter queryFilter = getFilter();
        if (queryFilter.getDepartmentId() != null) {
            descriptionMap.put("Dept", String.valueOf(queryFilter.getDepartmentId()));
        }
        if (queryFilter.getClassId() != null) {
            descriptionMap.put("Class", String.valueOf(queryFilter.getClassId()));
        }
        if (queryFilter.getSubclassId() != null) {
            descriptionMap.put("Sub-Class", String.valueOf(queryFilter.getSubclassId()));
        }
        if (queryFilter.getProductGroupType() != null) {
            descriptionMap.put("Type", Translator.getText(queryFilter.getProductGroupType().toString()));
        }
        if (queryFilter.getDescription() != null) {
            descriptionMap.put("Description", queryFilter.getDescription());
        }
        if (queryFilter.getStoreId() != null) {
            descriptionMap.put("Store", queryFilter.getStoreId().toString());
        }
        if (queryFilter.getItemId() != null) {
            descriptionMap.put("Item", queryFilter.getItemId());
        }
        return descriptionMap;
    }

    public boolean obtainProductGroupLock(Long productGroupId) throws Exception {
        return obtainLock(ActivityLockType.PRODUCT_GROUP, String.valueOf(productGroupId));
    }

    public void releaseProductGroupLock(Long productGroupId) throws Exception {
        releaseLock(ActivityLockType.PRODUCT_GROUP, String.valueOf(productGroupId));
    }
}
