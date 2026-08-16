package oracle.retail.sim.client.screen.productgroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.dialog.RInfoDialog;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.productgroup.ProductGroup;
import oracle.retail.sim.common.productgroup.ProductGroupMessageText;
import oracle.retail.sim.common.productgroup.ProductGroupQueryFilter;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.productgroup.ProductGroupVO;
import oracle.retail.sim.common.rules.schedule.ProductGroupScheduleValidationRule;
import oracle.retail.sim.common.schedule.ProductGroupSchedule;
import oracle.retail.sim.common.schedule.Schedule;
import oracle.retail.sim.common.schedule.ScheduleStatus;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Product Group Schedule Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupScheduleDetailModel extends SimScreenModel {

    private ProductGroupSchedule productGroupSchedule;
    private List<Store> availableStores;

    /****************************************************************************************************
     * Basic Product Group Schedule State Methods
     ***************************************************************************************************/
    public void loadProductGroupSchedule() {
        productGroupSchedule = (ProductGroupSchedule) RepositoryManager.getStateObject(SimClientStateKey.PRODUCT_GROUP_SCHEDULE_DETAIL);
        if (productGroupSchedule == null) {
            productGroupSchedule = BOFactory.createProductGroupSchedule();
        }
        RepositoryManager.removeStateObject(SimClientStateKey.PRODUCT_GROUP_SCHEDULE_DETAIL);
    }

    public ProductGroupSchedule getProductGroupSchedule() {
        return productGroupSchedule;
    }

    public boolean isNewProductGroupSchedule() {
        return StringUtility.isNullOrEmpty(productGroupSchedule.getIdAsString());
    }

    /****************************************************************************************************
     * Check to see if product group schedule beginning/end date are editable. These dates are editable
     * unless the schedule product group is of type UnitAndAmount and today's date is less than (the
     * beginning date - lockoutDays) or if the unit and amount schedule is closed.
     ***************************************************************************************************/
    public boolean isProductGroupScheduleEditable() {
        ProductGroup productGroup = productGroupSchedule.getProductGroup();
        if (StringUtility.isNullOrEmpty(productGroupSchedule.getIdAsString())) {
            return true;
        }
        if (productGroupSchedule.getStatus().equals(ScheduleStatus.CLOSED)) {
            return false;
        }
        if (!hasPermission(PermissionKey.PC_EDIT_PRODUCT_GROUP_SCHEDULE)) {
            return false;
        }
        if (productGroup != null) {
            if (!hasDataPermission(PermissionKey.DATA_PRODUCT_GROUP_TYPE, productGroup.getType().getCode())) {
                return false;
            }
        }
        if (isUnitAndAmountSchedule()) {
            try {
                productGroupSchedule.isEditable(getLockoutDays());
            } catch (Throwable exception) {
                return false;
            }
        }
        return true;
    }

    public boolean isUnitAndAmountSchedule() {
        ProductGroup productGroup = productGroupSchedule.getProductGroup();
        if (productGroup == null) {
            return false;
        }
        return productGroup.getType().isStockCountUnitAmount();
    }

    public boolean useStartDateAsEndDate() {
        ProductGroup productGroup = productGroupSchedule.getProductGroup();
        if (productGroup != null) {
            return productGroup.getType().isStockCountUnitAmount() || productGroup.getType().isStockCountRmsSync();
        }
        return false;
    }

    public boolean isScheduleDetailModifiable(ProductGroupType groupType, Date startDate, Date endDate) {
        if (!isProductGroupScheduleEditable()) {
            return false;
        }
        if (groupType == ProductGroupType.STOCK_COUNT_UNIT_AMOUNT) {
            return false;
        }
        if (groupType == ProductGroupType.STOCK_COUNT_PROBLEM_LINE) {
            return false;
        }
        return !SimDateUtil.isSameDay(getTimeZone(), startDate, endDate);
    }

    public void validateSchedule(Schedule schedule) throws BusinessException {
        ProductGroupScheduleValidationRule.execute(productGroupSchedule, schedule, getLockoutDays(), getTimeZone());
    }

    /****************************************************************************************************
     * Retrieve system property lockout days
     ***************************************************************************************************/
    public Integer getLockoutDays() {
        Integer lockoutDays = SimConfigManager.getInteger(SimConfigManager.STOCK_COUNT_LOCKOUT_DAYS);
        if (lockoutDays != null) {
            return lockoutDays;
        }
        return SimConfigManager.STOCK_COUNT_LOCKOUT_DAYS_DEFAULT;
    }

    /****************************************************************************************************
     * Activity Locking Methods
     ***************************************************************************************************/
    public boolean obtainProductGroupScheduleLock() throws Exception {
        if (isProductGroupSchedulePersisted() && isProductGroupScheduleEditable()) {
            return obtainLock(ActivityLockType.PRODUCT_GROUP_SCHEDULE, productGroupSchedule.getIdAsString());
        }
        return true;
    }

    public void releaseProductGroupScheduleLock() throws Exception {
        if (isProductGroupSchedulePersisted()) {
            releaseLock(ActivityLockType.PRODUCT_GROUP_SCHEDULE, productGroupSchedule.getIdAsString());
        }
    }

    public boolean checkProductGroupScheduleLock() throws Exception {
        if (isProductGroupSchedulePersisted()) {
            return confirmLock(ActivityLockType.PRODUCT_GROUP_SCHEDULE, productGroupSchedule.getIdAsString());
        }
        return true;
    }

    /****************************************************************************************************
     * Is Product Group Schedule Persisted
     ***************************************************************************************************/
    public boolean isProductGroupSchedulePersisted() {
        if (productGroupSchedule != null) {
            if (productGroupSchedule.getId() != null) {
                return !ProductGroupSchedule.NO_ID.equals(productGroupSchedule.getId());
            }
        }
        return false;
    }

    /****************************************************************************************************
     * Retrieve available stores
     ***************************************************************************************************/
    public List<Store> findAvailableStores(ProductGroupVO group) throws Exception {
        Store store = group.getStore();
        if (store != null) {
            availableStores = Collections.singletonList(store);
        }
        if (availableStores == null) {
            availableStores = SimRepository.getAllowedStores();
        }
        return availableStores;
    }

    /****************************************************************************************************
     * Find product group types
     ***************************************************************************************************/
    public List<ProductGroupType> getProductGroupTypes() {
        return SimEnumUtility.findProductGroupScheduleTypes();
    }

    public List<ProductGroupType> getFilteredProductGroupTypes() {
        List<ProductGroupType> filteredTypeList = new ArrayList<>();
        for (ProductGroupType productGroupType : SimEnumUtility.findProductGroupScheduleTypes()) {
            if (hasDataPermission(PermissionKey.DATA_PRODUCT_GROUP_TYPE, productGroupType.getCode())) {
                filteredTypeList.add(productGroupType);
            }
        }
        return filteredTypeList;
    }

    /****************************************************************************************************
     * Find product groups
     ***************************************************************************************************/
    public List<ProductGroupVO> findProductGroups(ProductGroupType groupType) throws Exception {
        ProductGroupQueryFilter filter = BOFactory.createProductGroupQueryFilter();
        filter.doSetProductGroupType(groupType);
        filter.doSetStoreId(getStoreId());

        return ClientServiceFactory.getProductGroupServices().findProductGroupVOs(filter);
    }

    public ProductGroup readProductGroup(ProductGroupVO productGroupVO) throws Exception {
        return ClientServiceFactory.getProductGroupServices().readProductGroup(productGroupVO.getId());
    }

    public ProductGroupVO convertToVO(ProductGroup productGroup) {
        ProductGroupVO groupVO = null;
        if (productGroup != null) {
            groupVO = BOFactory.createProductGroupVO();
            groupVO.doSetDescription(productGroup.getDescription());
            groupVO.doSetId(productGroup.getId());
            groupVO.doSetStore(productGroup.getStore());
            groupVO.doSetType(productGroup.getType());
        }
        return groupVO;
    }

    /****************************************************************************************************
     * Save Schedule
     ***************************************************************************************************/
    public void persistProductGroupSchedule() throws Exception {
        if (isProductGroupSchedulePersisted()) {
            ClientServiceFactory.getProductGroupScheduleServices().update(productGroupSchedule);
        } else {
            Long scheduleId = ClientServiceFactory.getProductGroupScheduleServices().create(productGroupSchedule);
            productGroupSchedule.doSetId(scheduleId);
        }
        RepositoryManager.addStateObject(SimClientStateKey.PRODUCT_GROUP_SCHEDULE_DETAIL_MODIFIED, Boolean.TRUE);
    }

    /****************************************************************************************************
     * Generate various types of transaction depending on the schedule
     ***************************************************************************************************/

    public boolean isGenerateTransactionNeeded() {
        Date today = SimDateUtil.convertDateToNoonUTC(getTimeZone(), SimDateUtil.getCurrentDate());
        ProductGroupType type = productGroupSchedule.getProductGroup().getType();
        if (type == ProductGroupType.STOCK_COUNT_WASTAGE && productGroupSchedule.getSchedule().getStartDate().equals(today)) {
            RInfoDialog dialog = new RInfoDialog(Application.getFrame(), StringConstants.EMPTY);
            dialog.displayMessage(ProductGroupMessageText.WASTAGE_NOTICE);
            return false;
        }
        if (type == ProductGroupType.STOCK_COUNT_WASTAGE || type == ProductGroupType.SHELF_REPLENISHMENT || type == ProductGroupType.STOCK_COUNT_UNIT_AMOUNT) {
            return false;
        }
        return SimDateUtil.isSameDay(getTimeZone(), productGroupSchedule.getSchedule().getFirstDate(), today);
    }

    public void generateTransactionsFromSchedule() throws Exception {
        ProductGroupType type = productGroupSchedule.getProductGroup().getType();
        Date nextScheduleDate = productGroupSchedule.getSchedule().getNextDate(getTimeZone(), SimDateUtil.getCurrentDate());
        Long scheduleId = productGroupSchedule.getId();
        for (Long storeId : productGroupSchedule.getStoreIds()) {
            if (type == ProductGroupType.STOCK_COUNT_UNIT) {
                ClientServiceFactory.getStockCountServices().generateStockCounts(storeId, scheduleId, nextScheduleDate, type);
            } else if (type == ProductGroupType.STOCK_COUNT_PROBLEM_LINE) {
                ClientServiceFactory.getStockCountServices().generateStockCounts(storeId, scheduleId, nextScheduleDate, type);
            } else if (type == ProductGroupType.ITEM_REQUEST) {
                ClientServiceFactory.getItemRequestServices().generateItemRequests(storeId, scheduleId, nextScheduleDate);
            }
        }
    }

    /****************************************************************************************************
     * Methods to convert Stores to store Ids and visa versa.
     ***************************************************************************************************/
    public List<Long> convertToStoreIds(List<Store> stores) {
        List<Long> storeIdList = new ArrayList<>();
        for (Store store : stores) {
            storeIdList.add(store.getId());
        }
        return storeIdList;
    }

    public List<Store> convertToStores(List<Long> storeIds) {
        List<Store> stores = new ArrayList<>();
        for (Long storeId : storeIds) {
            for (Store store : SimRepository.getAllowedStores()) {
                if (storeId.equals(store.getId())) {
                    stores.add(store);
                }
            }
        }
        return stores;
    }
}
