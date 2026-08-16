package oracle.retail.sim.client.screen.stockcount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.stockcount.StockCount;
import oracle.retail.sim.common.stockcount.StockCountChild;
import oracle.retail.sim.common.stockcount.StockCountDisplayStatus;
import oracle.retail.sim.common.stockcount.StockCountLineItem;
import oracle.retail.sim.common.stockcount.StockCountMessageText;
import oracle.retail.sim.common.stockcount.StockCountPhase;
import oracle.retail.sim.common.stockcount.StockCountStatus;
import oracle.retail.sim.common.stockcount.StockCountTimeframe;
import oracle.retail.sim.common.stockcount.StockCountType;
import oracle.retail.sim.common.stockcount.StockCountingMethod;
import oracle.retail.sim.common.storesequence.StoreSequenceAreaType;
import oracle.retail.sim.common.storesequence.StoreSequenceConstants;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Stock Count Wrapper - Wraps a StockCount object to handle some common GUI client side issues.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountWrapper {
    private StockCount stockCount;
    private StockCountChild stockCountChild;
    protected List<StockCountLineItem> lineItems;

    public StockCountWrapper(StockCount stockCount) {
        this.stockCount = stockCount;
    }

    /****************************************************************************************************
     * Basic Getters
     ***************************************************************************************************/

    public StockCount getStockCount() {
        return stockCount;
    }

    public StockCountChild getStockCountChild() {
        return stockCountChild;
    }

    public Long getId() {
        return stockCount.getId();
    }

    public Long getStoreId() {
        return stockCount.getStoreId();
    }

    public String getCountDescription() {
        return stockCount.getCountDescription();
    }

    public String getGroupDescription() {
        return stockCount.getGroupDescription();
    }

    public String getLocationDescription() {
        String description = stockCountChild.getDescription();
        if (StoreSequenceConstants.NO_SEQUENCE_DESCRIPTION.equals(description)) {
            return Translator.getText(description);
        }
        StringHelper helper = StringHelper.getInstance(LocaleManager.getLanguageLocale());
        StringBuilder buffer = new StringBuilder(helper.replace(description, ":::", " - "));
        StoreSequenceAreaType area = stockCountChild.getArea();
        if (area != null) {
            buffer.append(" - ");
            buffer.append(Translator.getText(area.toString()));
        }
        return buffer.toString();
    }

    public StockCountType getType() {
        return stockCount.getType();
    }

    public StockCountPhase getPhase() {
        return stockCount.getPhase();
    }

    public boolean isCountPhase() {
        return getPhase() == StockCountPhase.COUNT;
    }

    public boolean isRecountPhase() {
        return getPhase() == StockCountPhase.RECOUNT;
    }

    public boolean isAuthorizePhase() {
        return getPhase() == StockCountPhase.AUTHORIZE;
    }

    public StockCountingMethod getCountingMethod() {
        return stockCount.getCountingMethod();
    }

    public Date getScheduledDate() {
        return stockCount.getScheduledDate();
    }

    public StockCountDisplayStatus getDisplayStatus() {
        return stockCount.getStatus().getDisplayStatus();
    }

    public StockCountDisplayStatus getLocationDisplayStatus() {
        return stockCountChild.getStatus().getDisplayStatus();
    }

    public StockCountPhase getLocationPhase() {
        return stockCountChild.getPhase();
    }

    public MessageText getBeforeStoreOpen() {
        if (stockCount.getTimeframe() == StockCountTimeframe.NONE) {
            return null;
        }
        if (stockCount.getTimeframe() == StockCountTimeframe.BEFORE_STORE_OPEN) {
            return StockCountMessageText.BEFORE_STORE_OPEN;
        }
        return StockCountMessageText.AFTER_STORE_CLOSE;
    }

    // If count or new, get total number of line items, otherwise get discrepant item count.

    public Integer getItemsLeftToCount() {
        return stockCount.getItemsLeftToCount();
    }

    public boolean isRecountRequired() {
        return stockCount.isRecountRequired();
    }

    public boolean isUnitAndAmount() {
        return stockCount.getType() == StockCountType.UNIT_AMOUNT;
    }

    public boolean isRMSSync() {
        return stockCount.getType() == StockCountType.RMS_SYNC;
    }

    public boolean isStockCountComplete() {
        return stockCount.getStatus() == StockCountStatus.APPROVAL_COMPLETE;
    }

    public boolean isBreakdownSequenced() {
        return stockCount.getBreakdownType().isSequenced();
    }

    public boolean isLocationAlreadySnapshot() {
        if (stockCount.isUnitAndAmount()) {
            return stockCountChild.getCountSnapshotTime() != null;
        }
        if (stockCountChild.getPhase() == StockCountPhase.RECOUNT) {
            return stockCountChild.getRecountSnapshotTime() != null;
        }
        return stockCountChild.getCountSnapshotTime() != null;
    }

    /****************************************************************************************************
     * Deleteable Methods
     ***************************************************************************************************/

    public boolean isStockCountDeletable() throws Exception {
        if (stockCount.getStatus().isDeletable()) {
            if (stockCount.getStatus() == StockCountStatus.APPROVAL_IN_PROGRESS) {
                return isStockCountChildsDeletable();
            }
            return true;
        }
        return false;
    }

    /** Note this will be slow, but only called if header is authorize-in-progress status. */
    private boolean isStockCountChildsDeletable() throws Exception {
        List<StockCountChild> locations = ClientServiceFactory.getStockCountChildServices().findStockCountChilds(stockCount.getId());
        for (StockCountChild tempLocation : locations) {
            if (!tempLocation.getStatus().isDeletable()) {
                return false;
            }
        }
        return true;
    }

    /****************************************************************************************************
     * Basic Setters
     ***************************************************************************************************/

    public void setActiveStockCountChild(StockCountChild stockCountChild) {
        this.stockCountChild = stockCountChild;
        clearLineItems();
    }

    public void setTimeFrame(String beforeStoreOpen) throws BusinessException {
        if (!StringHelper.isNullOrEmpty(beforeStoreOpen)) {
            if (StockCountMessageText.BEFORE_STORE_OPEN.getText().equals(beforeStoreOpen)) {
                stockCount.setTimeframe(StockCountTimeframe.BEFORE_STORE_OPEN);
            } else {
                stockCount.setTimeframe(StockCountTimeframe.AFTER_STORE_CLOSE);
            }
        }
    }

    /****************************************************************************************************
     * Reset Methods
     ***************************************************************************************************/

    public void refreshStockCount() throws Exception {
        if (stockCount != null) {
            stockCount = ClientServiceFactory.getStockCountServices().readStockCount(stockCount.getId());
        }
    }

    private void refreshStockCountChild() throws Exception {
        if (stockCountChild != null) {
            stockCountChild = ClientServiceFactory.getStockCountChildServices().readStockCountChild(stockCountChild.getId());
        }
    }

    /****************************************************************************************************
     * Mark Stock Count or Stock Location Location As Started (Snapshot)
     ***************************************************************************************************/
    public void markStockCountAsStarted() throws Exception {
        ClientServiceFactory.getStockCountChildServices().markStockCountChildsStarted(stockCount.getId());
        refreshStockCount();
        refreshStockCountChild();
        clearLineItems();
    }

    public void markStockCountChildAsStarted() throws Exception {
        List<Long> stockCountChildIds = Collections.singletonList(stockCountChild.getId());
        ClientServiceFactory.getStockCountChildServices().markStockCountChildsStarted(stockCount.getId(), stockCountChildIds);
        refreshStockCount();
        refreshStockCountChild();
        clearLineItems();
    }

    public void markStockCountChildAsStarted(List<Long> stockCountChildIds) throws Exception {
        ClientServiceFactory.getStockCountChildServices().markStockCountChildsStarted(stockCount.getId(), stockCountChildIds);
        refreshStockCount();
        refreshStockCountChild();
        clearLineItems();
    }

    /****************************************************************************************************
     * Mark Stock Count Location As Count Completed
     ***************************************************************************************************/

    public void markStockCountChildAsCompleted() throws Exception {
        if (stockCountChild.getPhase() == StockCountPhase.COUNT) {
            ClientServiceFactory.getStockCountChildServices().markStockCountChildCounted(stockCount.getId(), stockCountChild.getId());
        } else {
            ClientServiceFactory.getStockCountChildServices().markStockCountChildRecounted(stockCount.getId(), stockCountChild.getId());
        }
        refreshStockCount();
        refreshStockCountChild();
        clearLineItems();
    }

    /****************************************************************************************************
     * Mark Unit And Amount Stock Count Location As Confirmed
     ***************************************************************************************************/

    public void markStockCountChildConfirmed() throws Exception {
        ClientServiceFactory.getStockCountChildServices().markStockCountChildApproved(stockCount.getId(), stockCountChild.getId());
        refreshStockCount();
        refreshStockCountChild();
        clearLineItems();
    }

    /****************************************************************************************************
     * Mark Stock Count Location As Ready To Approve
     ***************************************************************************************************/

    public void markStockCountChildReadyToApprove() throws Exception {
        List<Long> readyLocationIds = Collections.singletonList(stockCountChild.getId());
        ClientServiceFactory.getStockCountChildServices().markStockCountChildsReadyToApprove(stockCount.getId(), readyLocationIds);
        refreshStockCount();
        refreshStockCountChild();
        clearLineItems();
    }

    /****************************************************************************************************
     * Mark Stock Count Location As Approved
     ***************************************************************************************************/

    public void markStockCountChildApproved() throws Exception {
        ClientServiceFactory.getStockCountChildServices().markStockCountChildApproved(stockCount.getId(), stockCountChild.getId());
        refreshStockCount();
        refreshStockCountChild();
        clearLineItems();
    }

    /****************************************************************************************************
     * Line Item Wrapper Methods
     ***************************************************************************************************/

    public List<StockCountLineItemWrapper> getLineItemWrappers() throws Exception {
        List<StockCountLineItemWrapper> wrappers = new ArrayList<>();
        for (StockCountLineItem lineItem : getModifiableLineItems()) {
            wrappers.add(ClientWrapperFactory.createStockCountLineItemWrapper(lineItem, isAuthorizePhase()));
        }
        return wrappers;
    }
    
    public List<StockCountLineItemWrapper> getAllLineItemWrappers() throws Exception {
        List<StockCountLineItemWrapper> wrappers = new ArrayList<>();
        for (StockCountLineItem lineItem : getAllLineItems()) {
            wrappers.add(ClientWrapperFactory.createStockCountLineItemWrapper(lineItem, isAuthorizePhase()));
        }
        return wrappers;
    }

    public List<StockCountLineItem> getDirtyLineItems() throws Exception {
        List<StockCountLineItem> dirtyLineItems = new ArrayList<>();
        for (StockCountLineItem lineItem : getModifiableLineItems()) {
            if (lineItem.isDirty()) {
                dirtyLineItems.add(lineItem);
            }
        }
        return Collections.unmodifiableList(dirtyLineItems);
    }

    public void saveDirtyLineItems() throws Exception {
        List<StockCountLineItem> lineItems = getDirtyLineItems();
        ClientServiceFactory.getStockCountLineItemServices().updateLineItems(stockCount.getId(), stockCountChild.getId(), lineItems);
        refreshStockCount();
        refreshStockCountChild();
        clearLineItems();
    }
    
    public Boolean isLineItemsSaved() throws Exception {
        return getDirtyLineItems().isEmpty();
    }

    /****************************************************************************************************
     * Line Item Methods
     ***************************************************************************************************/

    private List<StockCountLineItem> getModifiableLineItems() throws Exception {
        if (lineItems == null && stockCountChild != null) {
            if (stockCountChild.getStatus().getPhase() == StockCountPhase.AUTHORIZE) {
                lineItems = ClientServiceFactory.getStockCountLineItemServices().readPrimaryStockCountLineItems(stockCountChild.getId());
            } else {
                lineItems = ClientServiceFactory.getStockCountLineItemServices().readStockCountLineItems(stockCountChild.getId());
            }
        }
        if (lineItems != null) {
            return lineItems;
        }
        return Collections.emptyList();
    }
    
    private List<StockCountLineItem> getAllLineItems() throws Exception {
        if (lineItems == null && stockCountChild != null) {
        	lineItems = ClientServiceFactory.getStockCountLineItemServices().readStockCountLineItems(stockCountChild.getId());
        }
        if (lineItems != null) {
            return lineItems;
        }
        return Collections.emptyList();
    }

    public void clearLineItems() {
        if (lineItems != null) {
            lineItems.clear();
            lineItems = null;
        }
    }
    
    /****************************************************************************************************
     * Apply Late Sales
     ***************************************************************************************************/
    
    public void applyLateSales() throws Exception {
        List<StockCountLineItem> lineItems = getDirtyLineItems();
        ClientServiceFactory.getStockCountLineItemServices().updateLineItems(stockCount.getId(), stockCountChild.getId(), lineItems);
        ClientServiceFactory.getStockCountChildServices().applySales(stockCount.getId(), stockCountChild.getId());
        refreshStockCount();
        refreshStockCountChild();
        clearLineItems();
    }

    /****************************************************************************************************
     * Update Authorization Quantities - For each line item, complete with the last counted value in the
     * following priority: authorizedValue, recountedValue, countedValue
     ***************************************************************************************************/

    public void updateAuthorizationQuantities() throws BusinessException {
        if (lineItems != null) {
            boolean isUnitAndAmount = stockCount.isUnitAndAmount();
            boolean isAllSOHCount = SimConfigManager.getBoolean(SimConfigManager.STOCK_COUNT_UPDATE_ALL_SOH);
            for (StockCountLineItem lineItem : lineItems) {
                if (lineItem.getStockApproved() == null) {
                    if (isUnitAndAmount || lineItem.isDiscrepant() || isAllSOHCount) {
                        if (lineItem.isDependentTotal()) {
                            if (lineItem.getStockRecountedTotal() != null) {
                                lineItem.setStockApproved(lineItem.getStockRecountedTotal());
                            } else {
                                lineItem.setStockApproved(lineItem.getStockCountedTotal());
                            }
                        } else {
                            if (lineItem.getStockRecounted() != null) {
                                lineItem.setStockApproved(lineItem.getStockRecounted());
                            } else {
                                lineItem.setStockApproved(lineItem.getStockCounted());
                            }
                        }
                    }
                }
            }
        }
    }
}
