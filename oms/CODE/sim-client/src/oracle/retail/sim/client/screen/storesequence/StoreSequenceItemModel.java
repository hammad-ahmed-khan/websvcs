package oracle.retail.sim.client.screen.storesequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.activitylock.ActivityLockUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;
import oracle.retail.sim.common.itemticket.TicketTypeId;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.storesequence.StoreSequenceArea;
import oracle.retail.sim.common.storesequence.StoreSequenceItem;
import oracle.retail.sim.common.storesequence.StoreSequenceItemComparator;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Store Sequence Item Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreSequenceItemModel extends SimScreenModel {

    private StoreSequenceArea sequenceArea;
    private List<StoreSequenceItem> sequenceItems = new ArrayList<>();
    private List<StoreSequenceItem> deletedSequenceItems = new ArrayList<>();
    private TicketTypeFormat defaultTicketTypeFormat;
    private Integer defaultTicketQuantity;
    private Boolean assignShelfLabels;

    public void loadStoreSequence() throws Exception {
        sequenceArea = (StoreSequenceArea) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_STORE_SEQUENCE);
        sequenceItems = ClientServiceFactory.getStoreSequenceServices().findStoreSequenceItems(sequenceArea.getId(), getStoreId());
        deletedSequenceItems = new ArrayList<>();
    }

    public StoreSequenceArea getSequenceArea() {
        return sequenceArea;
    }

    public boolean isSequencingUnmodifiable() {
        return !hasPermission(PermissionKey.PC_EDIT_ITEM_SEQUENCING_LOCATION);
    }

    public boolean isScannerAvailable() {
        return isEditMode() && !isSequencingUnmodifiable();
    }

    public boolean isEditMode() {
        if (isAllItemStockCountActive()) {
            return false;
        }
        return RepositoryManager.getStateObject(SimClientStateKey.STORE_SEQUENCE_ITEM_EDIT_MODE) != null;
    }

    public boolean isViewMode() {
        return !isEditMode();
    }

    public boolean isAllItemStockCountActive() {
        return RepositoryManager.getStateObject(SimClientStateKey.ALL_ITEM_STOCK_COUNT_ACTIVE) != null;
    }

    public boolean isNotSequenced() {
        return sequenceArea.isNotSequenced();
    }

    public List<StoreSequenceItemWrapper> getSequenceItems() throws Exception {
        Collections.sort(sequenceItems, new StoreSequenceItemComparator());
        List<StoreSequenceItemWrapper> wrappers = new ArrayList<>();
        Map<String, TicketTypeFormat> shelfLabelMap = getShelfLabelFormatMap();
        StoreSequenceItemWrapper wrapper = null;
        for (StoreSequenceItem sequenceItem : sequenceItems) {
            wrapper = ClientWrapperFactory.createStoreSequenceItemWrapper(sequenceItem);
            wrapper.setShelfLabelMap(shelfLabelMap);
            wrapper.doSetSequenceArea(sequenceArea);

            wrappers.add(wrapper);
        }
        return wrappers;
    }

    public List<TicketTypeFormat> getShelfLabelFormats() throws Exception {
        return ClientDataCacheUtility.getShelfLabelFormats();
    }

    private Map<String, TicketTypeFormat> getShelfLabelFormatMap() throws Exception {
        List<TicketTypeFormat> formats = getShelfLabelFormats();
        Map<String, TicketTypeFormat> formatMap = new HashMap<>(formats.size());
        for (TicketTypeFormat format : formats) {
            formatMap.put(format.getId(), format);
        }
        return formatMap;
    }

    public boolean storeItemAndObtainAllSequenceLock(StockItem stockItem) throws Exception {
        if (obtainLock(ActivityLockType.STORE_SEQUENCE_ALL, String.valueOf(getStoreId()))) {
            RepositoryManager.addStateObject(SimClientStateKey.SELECTED_ITEM, stockItem);
            return true;
        }
        return false;
    }

    public void releaseSequenceAreaLock() throws Exception {
        releaseLock(ActivityLockType.STORE_SEQUENCE, sequenceArea.getIdAsString());
    }

    public boolean obtainSequenceAreaLock() throws Exception {
        String storeId = String.valueOf(getStoreId());
        Set<String> lockOwners = ActivityLockUtility.findActivityLockOwners(ActivityLockType.STORE_SEQUENCE_ALL, storeId);
        for (String owner : lockOwners) {
            if (!owner.equals(getUserName())) {
                return false;
            }
        }
        return obtainLock(ActivityLockType.STORE_SEQUENCE, sequenceArea.getIdAsString());
    }

    public boolean checkSequenceAreaLock() throws Exception {
        return confirmLock(ActivityLockType.STORE_SEQUENCE, sequenceArea.getIdAsString());
    }

    public void updateExistingLineItem(StoreSequenceItemWrapper wrapper) throws BusinessException {
        wrapper.setCapacity(wrapper.getCapacity().add(Quantity.ONE));
        wrapper.setWidth(wrapper.getWidth().add(Quantity.ONE));
    }

    public void updateSequenceOrder(List<StoreSequenceItemWrapper> wrappers) throws BusinessException {
        int order = 1;
        for (StoreSequenceItemWrapper wrapper : wrappers) {
            wrapper.setSequenceOrder(order++);
        }
    }

    public void updateStoreSequenceItems(List<StoreSequenceItemWrapper> wrappers) throws Exception {
        List<StoreSequenceItem> updatedSequenceItems = new ArrayList<>();
        for (StoreSequenceItemWrapper wrapper : wrappers) {
            StoreSequenceItem tempItem = wrapper.getStoreSequenceItem();
            if (tempItem.isCoherent()) {
                if (tempItem.getId() == null || tempItem.isDirty()) {
                    updatedSequenceItems.add(tempItem);
                }
            }
        }
        ClientServiceFactory.getStoreSequenceServices().updateStoreSequenceItems(updatedSequenceItems, deletedSequenceItems, getStoreId());
        RepositoryManager.addStateObject(SimClientStateKey.STORE_SEQUENCE_MODIFIED, Boolean.TRUE);
    }

    public void applyItemList() throws Exception {
        Long storeId = getStoreId();
        Long departmentId = sequenceArea.getDepartmentId();
        Long classId = sequenceArea.getClassId();
        List<StockItem> stockItems = ClientServiceFactory.getItemServices().findStockItems(storeId, departmentId, classId);
        if (stockItems.isEmpty()) {
            return;
        }

        int sequenceOrder = -1;
        Set<String> existingItemIds = new HashSet<>();
        for (StoreSequenceItem sequenceItem : sequenceItems) {
            existingItemIds.add(sequenceItem.getItemId());

            if (sequenceItem.getOrder() > sequenceOrder) {
                sequenceOrder = sequenceItem.getOrder();
            }
        }
        sequenceOrder = sequenceOrder + 1;

        Collections.sort(stockItems, new StockItemIdComparator());

        StoreSequenceItem sequenceItem = null;

        for (StockItem stockItem : stockItems) {
            if (sequenceArea.isShopFloorType() && !stockItem.isSellable()) {
                continue;
            }
            if (existingItemIds.contains(stockItem.getId())) {
                continue;
            }
            sequenceItem = BOFactory.createStoreSequenceItem();
            sequenceItem.doSetStoreId(storeId);
            sequenceItem.doSetSequenceAreaId(sequenceArea.getId());
            sequenceItem.doSetItemId(stockItem.getId());
            sequenceItem.doSetShortDescription(stockItem.getShortDescription());
            sequenceItem.doSetLongDescription(stockItem.getLongDescription());
            sequenceItem.doSetOrder(sequenceOrder++);
            sequenceItem.doSetCapacity(Quantity.ZERO);
            sequenceItem.doSetDirty(true);

            sequenceItems.add(sequenceItem);
        }
    }

    public boolean containsBlankSequenceItem(List<StoreSequenceItemWrapper> wrappers) {
        StockItem stockItem = null;
        for (StoreSequenceItemWrapper wrapper : wrappers) {
            stockItem = wrapper.getStockItem();

            if (stockItem == null || StringUtility.isNullOrEmpty(stockItem.getId())) {
                return true;
            }
        }
        return false;
    }

    public StoreSequenceItemWrapper createNewSequenceItem(List<StoreSequenceItemWrapper> wrappers) throws Exception {
        validateStoreConfigurations();

        StoreSequenceItem sequenceItem = BOFactory.createStoreSequenceItem();
        sequenceItem.doSetSequenceAreaId(sequenceArea.getId());
        sequenceItem.doSetStoreId(sequenceArea.getStoreId());
        sequenceItem.doSetOrder(getNextSequenceOrder(wrappers));

        if (defaultTicketTypeFormat != null) {
            sequenceItem.doSetTicketTypeFormatId(defaultTicketTypeFormat.getId());
            sequenceItem.doSetTicketQuantity(defaultTicketQuantity);
        }

        StoreSequenceItemWrapper wrapper = null;

        if (sequenceItems.add(sequenceItem)) {
            wrapper = ClientWrapperFactory.createStoreSequenceItemWrapper(sequenceItem);
            wrapper.setShelfLabelMap(getShelfLabelFormatMap());
            wrapper.doSetSequenceArea(sequenceArea);
            return wrapper;
        }
        return wrapper;
    }

    private int getNextSequenceOrder(List<StoreSequenceItemWrapper> wrappers) throws Exception {
        int order = -1;
        for (StoreSequenceItemWrapper wrapper : wrappers) {
            if (wrapper.getStoreSequenceItem().getOrder() > -1) {
                order = wrapper.getStoreSequenceItem().getOrder();
            }
        }
        return order + 1;
    }

    private void validateStoreConfigurations() throws Exception {
        if (assignShelfLabels == null) {
            assignShelfLabels = SimConfigManager.getStoreBoolean(StoreConfigKeys.AUTO_DEFAULT_LABEL, getStoreId());
        }
        if (assignShelfLabels) {
            defaultTicketTypeFormat = ClientServiceFactory.getItemTicketServices().findDefaultTicketTypeFormat(getStoreId(), TicketTypeId.SHELF_LABEL_ID);
            defaultTicketQuantity = 1;
        } else {
            defaultTicketTypeFormat = null;
            defaultTicketQuantity = null;
        }
    }

    public void removeItem(StoreSequenceItemWrapper wrapper) {
        StoreSequenceItem storeSequenceItem = wrapper.getStoreSequenceItem();
        Collections.sort(sequenceItems, new StoreSequenceItemComparator());
        if(sequenceItems.size()==1) {
            sequenceItems.remove(0);
        } else {
            sequenceItems.remove(storeSequenceItem.getOrder());
        }

        if (storeSequenceItem.getId() != null) {
            deletedSequenceItems.add(storeSequenceItem);
        }
    }

    private class StockItemIdComparator implements Comparator {

        public int compare(Object object1, Object object2) {
            String id1 = ((StockItem) object1).getId();
            String id2 = ((StockItem) object2).getId();
            return id1.compareTo(id2);
        }
    }
}
