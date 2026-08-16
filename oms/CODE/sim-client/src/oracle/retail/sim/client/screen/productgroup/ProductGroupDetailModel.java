package oracle.retail.sim.client.screen.productgroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.ProductGroupItem;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;
import oracle.retail.sim.common.productgroup.ProductGroup;
import oracle.retail.sim.common.productgroup.ProductGroupHierarchy;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.schedule.ProductGroupScheduleQueryFilter;
import oracle.retail.sim.common.schedule.ProductGroupScheduleVO;
import oracle.retail.sim.common.schedule.ScheduleStatus;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.stockcount.StockCountBreakdownType;
import oracle.retail.sim.common.stockcount.StockCountingMethod;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.common.storesequence.StoreSequenceArea;
import oracle.retail.sim.service.core.ClientServiceFactory;

/*******************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
 * Product Group Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/

public class ProductGroupDetailModel extends SimScreenModel {
    private IdNameDisplayer storeDisplayer = new IdNameDisplayer();

    private ProductGroupWrapper productGroupWrapper;
    private Boolean isStoreSequenced;
    private Boolean isEditModeAllowed;

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * Load and Get Product Group Wrapper and product group
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
    public ProductGroupWrapper getProductGroupWrapper() {
        return productGroupWrapper;
    }

    public ProductGroup getProductGroup() {
        return productGroupWrapper.getProductGroup();
    }

    public boolean loadProductGroup() throws Exception {
        ProductGroup productGroup = (ProductGroup) RepositoryManager.getStateObject(SimClientStateKey.PRODUCT_GROUP_DETAIL);
        if (productGroup != null) {
            productGroupWrapper = new ProductGroupWrapper(productGroup, isSuperUser());
            RepositoryManager.removeStateObject(SimClientStateKey.PRODUCT_GROUP_DETAIL);
            return true;
        }
        return false;
    }

    public void createProductGroup(ProductGroupType productGroupType) throws Exception {
        ProductGroup productGroup = BOFactory.createProductGroup(productGroupType);
        productGroup.setStore(getStore());
        if (productGroupWrapper != null) {
            productGroup.doSetDescription(productGroupWrapper.getDescription());
        }
        if (productGroupType.isStockCountUnitAmount() && hasThirdPartyPermission()) {
            productGroup.doSetCountingMethod(StockCountingMethod.THIRD_PARTY);
        } else {
            productGroup.doSetCountingMethod(StockCountingMethod.UNGUIDED);
        }
        productGroupWrapper = new ProductGroupWrapper(productGroup, isSuperUser());
        isEditModeAllowed = null;
    }

    public boolean isProductGroupEditable() throws Exception {
        return isNewProductGroup() || isDataEntryAllowed();
    }

    public boolean isNewProductGroup() {
        return productGroupWrapper == null || StringUtility.isNullOrEmpty(productGroupWrapper.getId());
    }

    public boolean isDataEntryAllowed() throws Exception {
        if (productGroupWrapper == null) {
            return false;
        }
        if (isNewProductGroup()) {
            return true;
        }
        if (isEditModeAllowed != null) {
            return isEditModeAllowed;
        }
        if (!hasPermission(PermissionKey.PC_EDIT_PRODUCT_GROUP)) {
            isEditModeAllowed = Boolean.FALSE;
            return isEditModeAllowed;
        }
        if (!hasDataPermission(PermissionKey.DATA_PRODUCT_GROUP_TYPE, productGroupWrapper.getGroupType().getCode())) {
            isEditModeAllowed = Boolean.FALSE;
            return isEditModeAllowed;
        }
        if (productGroupWrapper.getCountingMethod() == StockCountingMethod.THIRD_PARTY) {
            if (!hasThirdPartyPermission()) {
                isEditModeAllowed = Boolean.FALSE;
                return isEditModeAllowed;
            }
        }
        if (!StringUtility.isNullOrEmpty(productGroupWrapper.getId())) {
            if (!obtainLock(ActivityLockType.PRODUCT_GROUP, productGroupWrapper.getId())) {
                isEditModeAllowed = Boolean.FALSE;
                return isEditModeAllowed;
            }
        }
        if (productGroupWrapper.getGroupType().isStockCountUnitAmount()) {
            List<ProductGroupScheduleVO> openSchedules = getOpenSchedules();
            if (openSchedules.isEmpty()) {
                isEditModeAllowed = Boolean.TRUE;
                return isEditModeAllowed;
            }
            for (ProductGroupScheduleVO scheduleVO : openSchedules) {
                Date startDate = SimDateUtil.convertDateFromUTC(getTimeZone(), scheduleVO.getSchedule().getStartDate());
                Integer lockoutDays = SimConfigManager.getInteger(SimConfigManager.STOCK_COUNT_LOCKOUT_DAYS);
                Date compareDate = SimDateUtil.addDays(getTimeZone(), SimDateUtil.getCurrentDate(), lockoutDays);
                if (SimDateUtil.isSameDay(getTimeZone(), compareDate, startDate)) {
                    isEditModeAllowed = Boolean.FALSE;
                    return isEditModeAllowed;
                }
                if (compareDate.compareTo(startDate) >= 0) {
                    isEditModeAllowed = Boolean.FALSE;
                    return isEditModeAllowed;
                }
            }
        }
        isEditModeAllowed = Boolean.TRUE;
        return isEditModeAllowed;
    }

    public boolean isCountMethodDisplayable() {
        ProductGroupType type = productGroupWrapper.getGroupType();
        return type.isStockCountUnit() || type.isStockCountUnitAmount() || type.isStockCountProblemLine();
    }

    private List<ProductGroupScheduleVO> getOpenSchedules() throws Exception {
        Long productGroupId = productGroupWrapper.getProductGroup().getId();
        if (productGroupId != null) {
            ProductGroupScheduleQueryFilter filter = BOFactory.createProductGroupScheduleQueryFilter();
            filter.doSetStatus(ScheduleStatus.OPEN);
            filter.doSetGroupId(productGroupId);

            return ClientServiceFactory.getProductGroupScheduleServices().findProductGroupScheduleVOs(filter);
        }
        return Collections.emptyList();
    }

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * Retrieve basic types
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/

    public List<ProductGroupType> getProductGroupTypes() {
        return SimEnumUtility.findProductGroupTypes();
    }

    public List<ProductGroupType> getFilteredProductGroupTypes() {
        List<ProductGroupType> filteredTypeList = new ArrayList<>();
        for (ProductGroupType productGroupType : SimEnumUtility.findProductGroupTypes()) {
            if (hasDataPermission(PermissionKey.DATA_PRODUCT_GROUP_TYPE, productGroupType.getCode())) {
                filteredTypeList.add(productGroupType);
            }
        }
        return filteredTypeList;
    }

    public List<UOMMode> getUnitOfMeasureModes() {
        List<UOMMode> uomList = new ArrayList<>(2);
        uomList.add(UOMMode.STANDARD);
        uomList.add(UOMMode.CASES);
        return uomList;
    }

    public List<StockCountingMethod> getCountingMethods() {
        List<StockCountingMethod> methodList = new ArrayList<>(3);
        methodList.add(StockCountingMethod.UNGUIDED);
        if (isStoreSequenced()) {
            methodList.add(StockCountingMethod.GUIDED);
        }
        methodList.add(StockCountingMethod.THIRD_PARTY);
        if (isNewProductGroup() && !hasThirdPartyPermission()) {
            methodList.remove(StockCountingMethod.THIRD_PARTY);
        }
        return methodList;
    }

    public List<StockCountBreakdownType> getBreakdownTypes() {
        List<StockCountBreakdownType> typeList = new ArrayList<>(5);
        typeList.add(StockCountBreakdownType.NONE);
        typeList.add(StockCountBreakdownType.LOCATION);
        typeList.add(StockCountBreakdownType.DEPARTMENT);
        typeList.add(StockCountBreakdownType.CLASS);
        typeList.add(StockCountBreakdownType.SUBCLASS);
        return typeList;
    }

    public String getStoreDescription(Store store) {
        return storeDisplayer.getDisplayText(store);
    }

    public boolean hasThirdPartyPermission() {
        return hasDataPermission(PermissionKey.DATA_COUNTING_METHOD, StockCountingMethod.THIRD_PARTY.getCode());
    }

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * Logic
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/

    public boolean isTypeSelected() {
        return productGroupWrapper.getProductGroup().getType() != null;
    }

    public boolean isProductGroupModified() {
        if (productGroupWrapper != null) {
            ProductGroup productGroup = productGroupWrapper.getProductGroup();
            if (productGroup.getId() == null) {
                return isTypeSelected();
            }
            return productGroup.isDirty();
        }
        return false;
    }

    public ProductGroupHierarchy buildProductGroupHierarchy(MdseHierarchyNode node, Integer itemCount) throws Exception {
        ProductGroupHierarchy hierarchy = BOFactory.createProductGroupHierarchy(node.getDepartmentId(), node.getClassId(), node.getSubclassId());
        hierarchy.setNumberOfItems(itemCount);
        return hierarchy;
    }

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * Line Item Methods
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/

    public List<ProductGroupDetailLineItemWrapper> getDetailLineItems() throws Exception {
        ProductGroup productGroup = productGroupWrapper.getProductGroup();
        List<ProductGroupDetailLineItemWrapper> wrappers = new ArrayList<>();
        if (productGroup == null) {
            return wrappers;
        }
        List<ProductGroupHierarchy> hierarchies = productGroup.getHierarchies();
        if (hierarchies != null) {
            for (ProductGroupHierarchy hierarchy : hierarchies) {
                wrappers.add(ClientWrapperFactory.createProductGroupDetailLineItemWrapper(hierarchy));
            }
        }
        List<String> itemIds = productGroup.getSingleItemIds();
        if (itemIds != null) {
            List<ProductGroupItem> singleItems = ClientServiceFactory.getItemServices().readProductGroupItems(itemIds, getStoreId());
            for (ProductGroupItem item : singleItems) {
                wrappers.add(ClientWrapperFactory.createProductGroupDetailLineItemWrapper(item));
            }
        }
        return wrappers;
    }

    public void removeLineItem(ProductGroupDetailLineItemWrapper wrapper) throws BusinessException {
        ProductGroup productGroup = productGroupWrapper.getProductGroup();
        if (wrapper.isItemType()) {
            productGroup.removeSingleItem(wrapper.getItem());
        } else {
            productGroup.removeHierarchy(wrapper.getHierarchy());
        }
    }

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * Find items for detail information
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
    public List<ProductGroupItem> findPromotionItems(Long promotionId) throws Exception {
        return ClientServiceFactory.getItemServices().findProductGroupItemsByPromotion(promotionId, getStoreId());
    }

    public List<ProductGroupItem> findSupplierItems(Supplier supplier) throws Exception {
        return ClientServiceFactory.getItemServices().findProductGroupItemsBySupplier(supplier.getId(), getStoreId());
    }

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * Item Calculation Methods
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/

    public Integer getRecommendedItemCount() {
        Integer recommendedCount = null;
        if (productGroupWrapper != null) {
            ProductGroupType type = productGroupWrapper.getProductGroup().getType();
            if (type == ProductGroupType.ITEM_REQUEST) {
                recommendedCount = SimConfigManager.getInteger(SimConfigManager.PRODUCT_GROUP_ITEM_REQUEST_LIMIT);
            } else if (type == ProductGroupType.SHELF_REPLENISHMENT) {
                recommendedCount = SimConfigManager.getInteger(SimConfigManager.PRODUCT_GROUP_SHELF_REPLENISHMENT_LIMIT);
            } else if (type == ProductGroupType.STOCK_COUNT_PROBLEM_LINE) {
                recommendedCount = SimConfigManager.getInteger(SimConfigManager.PRODUCT_GROUP_PROBLEM_LINE_LIMIT);
            } else if (type == ProductGroupType.STOCK_COUNT_UNIT) {
                recommendedCount = SimConfigManager.getInteger(SimConfigManager.PRODUCT_GROUP_UNIT_LIMIT);
            } else if (type == ProductGroupType.STOCK_COUNT_UNIT_AMOUNT) {
                recommendedCount = SimConfigManager.getInteger(SimConfigManager.PRODUCT_GROUP_UNIT_AMOUNT_LIMIT);
            }
        }
        if (recommendedCount == null) {
            recommendedCount = SimConfigManager.MAX_PRODUCT_GROUP_ITEMS;
        }
        return recommendedCount;
    }

    public Integer calculateHierarchyItemCount(MdseHierarchyNode node) throws Exception {
        ProductGroup productGroup = productGroupWrapper.getProductGroup();
        ProductGroupType type = productGroup.getType();
        Store store = productGroup.getStore();
        Long deptId = node.getDepartmentId();
        Long classId = node.getClassId();
        Long subclassId = node.getSubclassId();
        if (store != null) {
            return ClientServiceFactory.getProductGroupServices().calculateNumberOfItems(type, deptId, classId, subclassId, store.getId());
        }
        return ClientServiceFactory.getProductGroupServices().calculateNumberOfItems(type, deptId, classId, subclassId, null);
    }

    public Integer calculateItemCount(List<ProductGroupDetailLineItemWrapper> wrappers) throws Exception {
        ProductGroup productGroup = productGroupWrapper.getProductGroup();
        if (productGroup.isAllItems()) {
            Store store = productGroup.getStore();
            if (store != null) {
                return ClientServiceFactory.getProductGroupServices().calculateNumberOfItems(store.getId());
            }
            return ClientServiceFactory.getProductGroupServices().calculateNumberOfItems(getStoreId());
        }
        int itemCount = 0;
        for (ProductGroupDetailLineItemWrapper wrapper : wrappers) {
            itemCount = itemCount + wrapper.getItemCount();
        }
        return itemCount;
    }

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * IS UNIT AMOUNT GROUP ALL LOCATIONS BUTTONS ENABLED HELPER
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/

    private boolean isStoreSequenced() {
        if (isStoreSequenced == null) {
            try {
                List<StoreSequenceArea> sequenceAreas = ClientServiceFactory.getStoreSequenceServices().findStoreSequenceAreas(getStoreId());
                isStoreSequenced = (sequenceAreas.size() > 1);
            } catch (Exception e) {
                isStoreSequenced = Boolean.FALSE;
            }
        }
        return isStoreSequenced;
    }

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * LOCK METHODS
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/

    public boolean checkProductGroupLock() throws Exception {
        Long productGroupId = getProductGroup().getId();
        return productGroupId == null || confirmLock(ActivityLockType.PRODUCT_GROUP, productGroupId.toString());
    }

    public void releaseProductGroupLock() throws Exception {
        if (productGroupWrapper == null) {
            return;
        }
        ProductGroup productGroup = productGroupWrapper.getProductGroup();
        if (productGroup == null) {
            return;
        }
        Long productGroupId = productGroup.getId();
        if (productGroupId == null) {
            return;
        }
        releaseLock(ActivityLockType.PRODUCT_GROUP, productGroup.getId().toString());
    }

    /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * SAVE PRODUCT GROUP
     **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/

    public void saveProductGroup() throws Exception {
        ProductGroup productGroup = getProductGroup();
        if (productGroup.getId() == null) {
            ClientServiceFactory.getProductGroupServices().create(productGroup);
        } else {
            ClientServiceFactory.getProductGroupServices().update(productGroup);
        }
        RepositoryManager.addStateObject(SimClientStateKey.PRODUCT_GROUP_DETAIL_MODIFIED, Boolean.TRUE);
    }
}
