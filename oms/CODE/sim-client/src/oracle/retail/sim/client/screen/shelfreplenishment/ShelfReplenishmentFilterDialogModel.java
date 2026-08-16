package oracle.retail.sim.client.screen.shelfreplenishment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.productgroup.ProductGroupQueryFilter;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.productgroup.ProductGroupVO;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentQueryFilter;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentStatus;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Shelf Replenishment List Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ShelfReplenishmentFilterDialogModel extends SimScreenModel {
    private ShelfReplenishmentQueryFilter filter;
    private List<String> employeeIds;
    private List<ProductGroupVO> productGroups;

    public void setFilter(ShelfReplenishmentQueryFilter filter) {
        this.filter = filter;
    }

    public ShelfReplenishmentQueryFilter getFilter() {
        return filter;
    }

    public ShelfReplenishmentQueryFilter resetFilter() throws BusinessException {
        Date today = SimDateUtil.getCurrentDate();
        Date startDate = SimDateUtil.getDateAtStartOfDay(getTimeZone(), today);
        Date endDate = SimDateUtil.getDateAtEndOfDay(getTimeZone(), today);
        filter = BOFactory.createShelfReplenishmentQueryFilter();
        filter.doSetStatus(ShelfReplenishmentStatus.NEW);
        filter.doSetStoreId(getStoreId());
        filter.doSetDateRange(startDate, endDate);
        RepositoryManager.addStateObject(SimClientStateKey.SHELF_REPLENISHMENT_FILTER, filter);
        return filter;
    }

    public List<String> findEmployeesIds() throws Exception {
        if (employeeIds == null) {
            employeeIds = ClientServiceFactory.getShelfReplenishmentServices().findEmployeesIds(getStoreId());
        }
        return employeeIds;
    }

    public List<ProductGroupVO> findProductGroups() throws Exception {
        if (productGroups == null) {
            ProductGroupQueryFilter groupFilter = BOFactory.createProductGroupQueryFilter();
            groupFilter.doSetProductGroupType(ProductGroupType.SHELF_REPLENISHMENT);
            groupFilter.doSetStoreId(getStoreId());

            productGroups = ClientServiceFactory.getProductGroupServices().findProductGroupVOs(groupFilter);
        }
        return productGroups;
    }

    public ProductGroupVO readProductGroupVO(Long productGroupId) throws Exception {
        if (productGroupId == null) {
            return null;
        }
        return ClientServiceFactory.getProductGroupServices().readProductGroupVO(productGroupId);
    }

    public List<ShelfReplenishmentStatus> findShelfReplenishmentStatus() {
        List<ShelfReplenishmentStatus> statusList = new ArrayList<>(4);
        statusList.add(ShelfReplenishmentStatus.NEW);
        statusList.add(ShelfReplenishmentStatus.COMPLETE);
        statusList.add(ShelfReplenishmentStatus.CANCELED);
        statusList.add(ShelfReplenishmentStatus.IN_PROGRESS);
        return statusList;
    }

    public List<ShelfReplenishmentType> findShelfReplenishmentTypes() {
        List<ShelfReplenishmentType> typeList = new ArrayList<>(2);
        typeList.add(ShelfReplenishmentType.WITHIN_DAY);
        typeList.add(ShelfReplenishmentType.END_OF_DAY);
        return typeList;
    }
}
