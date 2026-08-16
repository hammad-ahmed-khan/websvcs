package oracle.retail.sim.client.screen.stockcount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.item.StockCountItem;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.stockcount.StockCount;
import oracle.retail.sim.common.stockcount.StockCountChild;
import oracle.retail.sim.common.stockcount.StockCountDisplayStatus;
import oracle.retail.sim.common.stockcount.StockCountPhase;
import oracle.retail.sim.common.stockcount.StockCountStatus;
import oracle.retail.sim.common.stockcount.StockCountType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Stock Count Authorization Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountAuthorizeModel extends SimScreenModel {
    private StockCountWrapper stockCountWrapper;
    private List<StockCountChild> stockCountChilds;
    private AuthorizeQueryFilter authorizeQueryFilter = new AuthorizeQueryFilter();

    public void loadStockCount() throws Exception {
        stockCountWrapper = (StockCountWrapper) getStateObject(SimClientStateKey.SELECTED_STOCK_COUNT);
        stockCountWrapper.refreshStockCount();
    }

    public StockCountWrapper getStockCountWrapper() {
        return stockCountWrapper;
    }

    public boolean isRecountRequired() {
        if (stockCountWrapper != null) {
            return stockCountWrapper.isRecountRequired();
        }
        return false;
    }

    public boolean isUnitAndAmount() {
        if (stockCountWrapper != null) {
            return stockCountWrapper.isUnitAndAmount();
        }
        return false;
    }

    public boolean isStockCountChildFinished() {
        return stockCountWrapper.getDisplayStatus() == StockCountDisplayStatus.COMPLETED || stockCountWrapper.getDisplayStatus() == StockCountDisplayStatus.AUTHORIZED;
    }

    public boolean isStockCountScreenEditable() {
        StockCount stockCount = stockCountWrapper.getStockCount();
        if (stockCount.getStatus().getCode() > StockCountStatus.APPROVAL_IN_PROGRESS.getCode()) {
            return false;
        }
        StockCountType type = stockCountWrapper.getStockCount().getType();
        if (type == StockCountType.ADHOC) {
            return hasPermission(PermissionKey.PC_EDIT_ADHOC_STOCK_COUNT);
        } else if (type == StockCountType.UNIT) {
            return hasPermission(PermissionKey.PC_EDIT_UNIT_STOCK_COUNT);
        } else if (type == StockCountType.PROBLEM_LINE) {
            return hasPermission(PermissionKey.PC_EDIT_UNIT_STOCK_COUNT);
        } else if (type == StockCountType.UNIT_AMOUNT) {
            return hasPermission(PermissionKey.PC_EDIT_UNIT_AMOUNT_STOCK_COUNT);
        }
        return false;
    }

    public boolean isStockCountTableEditable() {
        StockCountChild childCount = stockCountWrapper.getStockCountChild();
        if (childCount != null) {
            if (childCount.getStatus().getCode() > StockCountStatus.APPROVAL_IN_PROGRESS.getCode()) {
                return false;
            }
        }
        return isStockCountScreenEditable();
    }

    public boolean isStockCountChildSelected() {
        return stockCountWrapper.getStockCountChild() != null;
    }

    public boolean isBreakdownSequenced() {
        if (stockCountWrapper != null) {
            return stockCountWrapper.isBreakdownSequenced();
        }
        return false;
    }

    public boolean isInventoryAdjustmentValidationNeeded() throws Exception {
        if (getStoreBoolean(StoreConfigKeys.DISPLAY_LATE_ADJUSTMENT_MESSAGE)) {
            Long stockCountChildId = stockCountWrapper.getStockCountChild().getId();
            return ClientServiceFactory.getStockCountChildServices().hasOpenInventoryAdjustmentItems(stockCountWrapper.getStoreId(), stockCountChildId);
        }
        return false;
    }
    
    public List<StockCountDisplayStatus> getAllFilterStatus() {
        return SimEnumUtility.findAllAuthorizeStatus();
    }

    public void setAuthorizeQueryFilter(AuthorizeQueryFilter filter) {
        authorizeQueryFilter = filter;
    }

    public AuthorizeQueryFilter getAuthorizeQueryFilter() {
        return authorizeQueryFilter;
    }

    public void setActiveLocation(StockCountChild stockCountChild) throws Exception {
        if (stockCountChild != null) {
            stockCountWrapper.setActiveStockCountChild(stockCountChild);
            obtainLock(ActivityLockType.STOCK_COUNT_CHILD, stockCountChild.getId());
        }
    }

    public List<StockCountChild> findStockCountChilds(StockCountDisplayStatus displayStatus) throws Exception {
        List<StockCountChild> filteredLocations = new ArrayList<>();
        if (stockCountChilds == null) {
            stockCountChilds = ClientServiceFactory.getStockCountChildServices().findStockCountChilds(stockCountWrapper.getId());
        }
        for (StockCountChild location : stockCountChilds) {
            StockCountStatus status = location.getStatus();
            if (status.getPhase() == StockCountPhase.AUTHORIZE) {
                if (status.getDisplayStatus() == displayStatus) {
                    filteredLocations.add(location);
                }
            }
        }
        return filteredLocations;
    }

    public List<Long> getAvailableDepartments() throws Exception {
        Set<Long> nodeSet = new HashSet<>();
        List<StockCountLineItemWrapper> wrappers = stockCountWrapper.getLineItemWrappers();
        for (StockCountLineItemWrapper wrapper : wrappers) {
            Long departmentId = wrapper.getLineItem().getStockCountItem().getDepartmentId();
            if (departmentId != null) {
                nodeSet.add(departmentId);
            }
        }
        return new ArrayList<>(nodeSet);
    }

    public List<StockCountLineItemWrapper> getFilteredLineItems(DiscrepantFilterType discrepantType, AuthorizeQtyFilterType authorizeType) throws Exception {
        List<StockCountLineItemWrapper> wrappers = stockCountWrapper.getLineItemWrappers();

        MdseHierarchyNode hierarchyNode = authorizeQueryFilter.getHierarchyNode();
        Integer varianceUom = authorizeQueryFilter.getVarianceUom();
        BigDecimal variancePercent = authorizeQueryFilter.getVariancePercent();
        boolean isRecountRequired = isRecountRequired();

        List<StockCountLineItemWrapper> filteredWrappers = new ArrayList<>();
        for (StockCountLineItemWrapper wrapper : wrappers) {
            StockCountItem stockCountItem = wrapper.getLineItem().getStockCountItem();
            if (stockCountItem.isInventoryAtComponentLevel()) {
                continue;
            }
            if (discrepantType == DiscrepantFilterType.DISCREPANT) {
                if (!wrapper.getLineItem().isDiscrepant()) {
                    continue;
                }
            }
            if (authorizeType == AuthorizeQtyFilterType.AUTHORIZED) {
                if (wrapper.getStockApproved() == null) {
                    continue;
                }
            } else if (authorizeType == AuthorizeQtyFilterType.UNAUTHORIZED) {
                if (wrapper.getStockApproved() != null) {
                    continue;
                }
            }
            if (hierarchyNode != null) {
                if (!hierarchyNode.getDepartmentId().equals(stockCountItem.getDepartmentId())) {
                    continue;
                }
                if (hierarchyNode.getClassId() != null) {
                    if (!hierarchyNode.getClassId().equals(stockCountItem.getClassId())) {
                        continue;
                    }
                }
                if (hierarchyNode.getSubclassId() != null) {
                    if (!hierarchyNode.getSubclassId().equals(stockCountItem.getSubclassId())) {
                        continue;
                    }
                }
            }
            if (varianceUom != null) {
                Quantity variance = wrapper.getStockCountedVariance();
                if (isRecountRequired) {
                    variance = wrapper.getStockRecountedVariance();
                }
                if (variance != null && varianceUom >= variance.abs().intValue()) {
                    continue;
                }
            }
            if (variancePercent != null) {
                Quantity percent = wrapper.getStockCountedPercent();
                if (isRecountRequired) {
                    percent = wrapper.getStockRecountedPercent();
                }
                if (percent != null && variancePercent.doubleValue() >= percent.doubleValue() * 100d) {
                    continue;
                }
            }
            filteredWrappers.add(wrapper);
        }
        return filteredWrappers;
    }

    public void updateAuthorizationQuantities() throws BusinessException {
        stockCountWrapper.updateAuthorizationQuantities();
    }

    public boolean checkStockCountChildLock() throws Exception {
        return confirmLock(ActivityLockType.STOCK_COUNT_CHILD, stockCountWrapper.getStockCountChild().getId());
    }

    public void saveStockCountChild() throws Exception {
        stockCountWrapper.saveDirtyLineItems();
        RepositoryManager.addStateObject(SimClientStateKey.STOCK_COUNT_LOCATION_MODIFIED, Boolean.TRUE);
        stockCountChilds = null;
    }

    public void applyLateSales() throws Exception {
        stockCountWrapper.applyLateSales();
        RepositoryManager.addStateObject(SimClientStateKey.STOCK_COUNT_LOCATION_MODIFIED, Boolean.TRUE);
        stockCountChilds = null;
    }

    public void releaseStockCountChild() {
        StockCountChild location = stockCountWrapper.getStockCountChild();
        if (location != null) {
            try {
                releaseLock(ActivityLockType.STOCK_COUNT_CHILD, location.getId());
            } catch (Exception exception) {
                UILog.error(getClass(), exception);
            }
            stockCountWrapper.setActiveStockCountChild(null);
        }
    }

    public void markStockCountChildReadyToApprove() throws Exception {
        stockCountWrapper.markStockCountChildReadyToApprove();
        stockCountChilds = null;
    }

	public boolean isLineItemsSaved() throws Exception {
		return stockCountWrapper.isLineItemsSaved();
	}
}
