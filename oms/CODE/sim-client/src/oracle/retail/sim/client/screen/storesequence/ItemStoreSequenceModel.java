package oracle.retail.sim.client.screen.storesequence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.activitylock.ActivityLockUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;
import oracle.retail.sim.common.itemticket.TicketTypeId;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.storesequence.StoreSequenceArea;
import oracle.retail.sim.common.storesequence.StoreSequenceItem;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Item Store Sequence Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemStoreSequenceModel extends SimScreenModel {

    private StockItem stockItem;
    private Map<Long, StoreSequenceArea> sequenceAreaMap = new HashMap<>();
    private Set<StoreSequenceItem> deletedSequenceItems = new HashSet<>();
    private TicketTypeFormat defaultShelfLabelFormat;

    /****************************************************************************************************
     * State Validation
     ***************************************************************************************************/

    public boolean isActiveAllLocationCount() {
        return RepositoryManager.getStateObject(SimClientStateKey.ALL_ITEM_STOCK_COUNT_ACTIVE) != null;
    }

    public boolean isSequencingUnmodifiable() {
        return !hasPermission(PermissionKey.PC_EDIT_ITEM_SEQUENCING_LOCATION);
    }

    public StockItem loadStockItem() throws Exception {
        Object object = RepositoryManager.getStateObject(SimClientStateKey.SELECTED_ITEM);
        if (object instanceof ItemVO) {
            stockItem = ClientServiceFactory.getItemServices().readStockItem(((ItemVO) object).getId(), getStoreId());
        } else {
            stockItem = (StockItem) object;
        }
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_ITEM);
        return stockItem;
    }

    public List<StoreSequenceArea> getStoreSequences() throws Exception {
        if (sequenceAreaMap.isEmpty()) {
            List<StoreSequenceArea> sequenceAreas = ClientServiceFactory.getStoreSequenceServices().findStoreSequenceAreas(getStoreId());
            for (StoreSequenceArea sequenceArea : sequenceAreas) {
                sequenceAreaMap.put(sequenceArea.getId(), sequenceArea);
            }
        }
        return new ArrayList<>(sequenceAreaMap.values());
    }

    public List<StoreSequenceItemWrapper> findStoreSequenceItems() throws Exception {
        deletedSequenceItems = new HashSet<>();

        if (sequenceAreaMap.isEmpty()) {
            List<StoreSequenceArea> sequenceAreas = ClientServiceFactory.getStoreSequenceServices().findStoreSequenceAreas(getStoreId());
            for (StoreSequenceArea sequenceArea : sequenceAreas) {
                sequenceAreaMap.put(sequenceArea.getId(), sequenceArea);
            }
        }
        List<StoreSequenceItem> sequenceItems = ClientServiceFactory.getStoreSequenceServices().findStoreSequenceItems(stockItem.getId(), getStoreId());

        Map<String, TicketTypeFormat> shelfLabelMap = getShelfLabelFormatMap();
        List<StoreSequenceItemWrapper> wrappers = new ArrayList<>();
        for (StoreSequenceItem sequenceItem : sequenceItems) {
            StoreSequenceItemWrapper wrapper = ClientWrapperFactory.createStoreSequenceItemWrapper(sequenceItem);
            wrapper.doSetSequenceArea(sequenceAreaMap.get(sequenceItem.getSequenceAreaId()));
            wrapper.setShelfLabelMap(shelfLabelMap);
            wrappers.add(wrapper);
        }
        return wrappers;
    }

    private Map<String, TicketTypeFormat> getShelfLabelFormatMap() throws Exception {
        List<TicketTypeFormat> formats = getShelfLabelFormats();
        Map<String, TicketTypeFormat> formatMap = new HashMap<>(formats.size());
        for (TicketTypeFormat format : formats) {
            formatMap.put(format.getId(), format);
        }
        return formatMap;
    }

    public List<TicketTypeFormat> getShelfLabelFormats() throws Exception {
        return ClientDataCacheUtility.getShelfLabelFormats();
    }

    public StoreSequenceItemWrapper createNewStoreSequenceItem() throws Exception {
        StoreSequenceItem sequenceItem = BOFactory.createStoreSequenceItem();
        sequenceItem.doSetItemId(stockItem.getId());
        sequenceItem.doSetStoreId(getStoreId());
        sequenceItem.doSetShortDescription(stockItem.getShortDescription());
        sequenceItem.doSetLongDescription(stockItem.getLongDescription());
        sequenceItem.doSetCapacity(Quantity.ZERO);
        sequenceItem.doSetPrimary(false);
        StoreSequenceItemWrapper wrapper = ClientWrapperFactory.createStoreSequenceItemWrapper(sequenceItem);
        Map<String, TicketTypeFormat> shelfLabelMap = getShelfLabelFormatMap();
        wrapper.setShelfLabelFormat(getDefaultShelfLabelFormat());
        wrapper.setShelfLabelMap(shelfLabelMap);
        return wrapper;
    }

    public void removeSequenceItem(StoreSequenceItemWrapper wrapper) {
        StoreSequenceItem sequenceItem = wrapper.getStoreSequenceItem();
        if (sequenceItem.getId() != null) {
            deletedSequenceItems.add(sequenceItem);
        }
    }

    public boolean isOnlyInNoSequencedArea(List<StoreSequenceItemWrapper> wrappers) {
        if (wrappers.size() != 1) {
            return false;
        }
        if (wrappers.iterator().next().getSequenceArea().isNotSequenced()) {
            return true;
        }
        return false;
    }

    private boolean obtainSequenceLocks(List<String> storeSequenceIds) throws Exception {
        String storeId = String.valueOf(getStoreId());
        Set<String> lockOwners = ActivityLockUtility.findActivityLockOwners(ActivityLockType.STORE_SEQUENCE_ALL, storeId);
        for (String owner : lockOwners) {
            if (!owner.equals(getUserName())) {
                return false;
            }
        }
        for (String storeSequenceId : storeSequenceIds) {
            if (!obtainLock(ActivityLockType.STORE_SEQUENCE, storeSequenceId)) {
                return false;
            }
        }
        return true;
    }

    public void updateStoreSequenceItems(List<StoreSequenceItemWrapper> wrappers) throws Exception {
        List<StoreSequenceItem> updatedRecords = new ArrayList<>();
        List<String> storeSequenceIds = new ArrayList<>();
        for (StoreSequenceItemWrapper wrapper : wrappers) {
            StoreSequenceItem storeSequenceItem = wrapper.getStoreSequenceItem();
            if (storeSequenceItem.getId() == null || storeSequenceItem.isDirty()) {
                updatedRecords.add(storeSequenceItem);
                storeSequenceIds.add(storeSequenceItem.getSequenceAreaId().toString());
            }
        }
        if (obtainSequenceLocks(storeSequenceIds)) {
            List<StoreSequenceItem> deletedRecords = new ArrayList<>(deletedSequenceItems);
            ClientServiceFactory.getStoreSequenceServices().updateStoreSequenceItems(updatedRecords, deletedRecords, getStoreId());
            RepositoryManager.addStateObject(SimClientStateKey.STORE_SEQUENCE_MODIFIED, Boolean.TRUE);
        }
    }

    private TicketTypeFormat getDefaultShelfLabelFormat() throws Exception {
        if (defaultShelfLabelFormat == null) {
            if (getStoreBoolean(StoreConfigKeys.AUTO_DEFAULT_LABEL)) {
                defaultShelfLabelFormat = ClientServiceFactory.getItemTicketServices().findDefaultTicketTypeFormat(getStoreId(), TicketTypeId.SHELF_LABEL_ID);
            }
        }
        return defaultShelfLabelFormat;
    }

    public void releaseAllSequenceLocks() {
        try {
            ActivityLockUtility.releaseAllSessionActivityLocks();
        } catch (Exception e) {
            UILog.debug(getClass(), e);
        }
    }
}
