package oracle.retail.sim.client.screen.storesequence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;
import oracle.retail.sim.common.itemticket.TicketTypeId;
import oracle.retail.sim.common.storesequence.StoreSequenceArea;
import oracle.retail.sim.common.storesequence.StoreSequenceItem;
import oracle.retail.sim.common.storesequence.StoreSequenceItemQueryFilter;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Store Sequence No Area Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreSequenceNoAreaModel extends SimScreenModel {

    private List<StoreSequenceArea> sequenceAreas = null;
    private boolean isViewOnly = false;

    public void loadSequenceAreas() throws Exception {
        sequenceAreas = ClientServiceFactory.getStoreSequenceServices().findStoreSequenceAreas(getStoreId());
    }

    public List<StoreSequenceArea> getSequenceAreas() {
        return sequenceAreas;
    }

    public StoreSequenceArea getNoAreaStoreSequence() {
        for (StoreSequenceArea sequenceArea : sequenceAreas) {
            if (sequenceArea.isNotSequenced()) {
                return sequenceArea;
            }
        }
        return null;
    }

    public boolean isActiveAllLocationCount() {
        return RepositoryManager.getStateObject(SimClientStateKey.ALL_ITEM_STOCK_COUNT_ACTIVE) != null;
    }

    public List<StoreSequenceItemWrapper> findSequenceItems() throws Exception {
        StoreSequenceArea sequenceArea = getNoAreaStoreSequence();
        Map<String, TicketTypeFormat> shelfLabelMap = getShelfLabelMap();
        StoreSequenceItemQueryFilter filter = getFilter();

        List<StoreSequenceItem> sequenceItems = ClientServiceFactory.getStoreSequenceServices().findNoAreaStoreSequenceItems(filter, getStoreId());

        // Get Default Shelf Label
        String shelfLabelFormatId = null;
        if (SimConfigManager.getStoreBoolean(StoreConfigKeys.AUTO_DEFAULT_LABEL, getStoreId())) {
            TicketTypeFormat defaultFormat = ClientServiceFactory.getItemTicketServices().findDefaultTicketTypeFormat(getStoreId(), TicketTypeId.SHELF_LABEL_ID);
            if (defaultFormat != null) {
                shelfLabelFormatId = defaultFormat.getId();
            }
        }

        // Build Wrappers
        List<StoreSequenceItemWrapper> wrappers = new ArrayList<>();
        StoreSequenceItemWrapper wrapper = null;
        for (StoreSequenceItem sequenceItem : sequenceItems) {
            if (sequenceItem.getTicketTypeFormatId() == null) {
                sequenceItem.doSetTicketTypeFormatId(shelfLabelFormatId);
            }
            wrapper = ClientWrapperFactory.createStoreSequenceItemWrapper(sequenceItem);
            wrapper.doSetSequenceArea(sequenceArea);
            wrapper.setShelfLabelMap(shelfLabelMap);

            wrappers.add(wrapper);
        }
        return wrappers;
    }

    private Map<String, TicketTypeFormat> getShelfLabelMap() throws Exception {
        List<TicketTypeFormat> formats = getShelfLabelFormats();
        Map<String, TicketTypeFormat> formatMap = new HashMap<>(formats.size());
        for (TicketTypeFormat format : formats) {
            formatMap.put(format.getId(), format);
        }
        return formatMap;
    }

    public StoreSequenceItemQueryFilter getFilter() {
        StoreSequenceItemQueryFilter filter = (StoreSequenceItemQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.STORE_SEQUENCE_ITEM_FILTER);
        if (filter == null) {
            filter = BOFactory.createStoreSequenceItemQueryFilter();
            filter.doSetSearchLimit(getDefaultSearchLimit());
            RepositoryManager.addStateObject(SimClientStateKey.STORE_SEQUENCE_ITEM_FILTER, filter);
        }
        return filter;
    }

    private int getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_STORE_SEQUENCE);
    }

    public List<TicketTypeFormat> getShelfLabelFormats() throws Exception {
        return ClientDataCacheUtility.getShelfLabelFormats();
    }

    public Map<String, String> getDescriptionMap() throws BusinessException {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        StoreSequenceItemQueryFilter filter = getFilter();
        if (filter.getItemId() != null) {
            descriptionMap.put("Item", filter.getItemId());
        }
        if (filter.getItemDescription() != null) {
            descriptionMap.put("Item Description", filter.getItemDescription());
        }
        descriptionMap.put("Search Limit", String.valueOf(filter.getSearchLimit()));
        return descriptionMap;
    }

    public void updateSequenceItems(List<StoreSequenceItemWrapper> wrappers) throws Exception {
        List<StoreSequenceItem> updatedSequencedItems = new ArrayList<>();
        List<StoreSequenceItem> deletedSequencedItems = new ArrayList<>();
        for (StoreSequenceItemWrapper wrapper : wrappers) {
            if (isUpdateRequired(wrapper)) {
                updatedSequencedItems.add(wrapper.getStoreSequenceItem());
            }
        }
        if (updatedSequencedItems.isEmpty()) {
            return;
        }
        ClientServiceFactory.getStoreSequenceServices().updateStoreSequenceItems(updatedSequencedItems, deletedSequencedItems, getStoreId());
        RepositoryManager.addStateObject(SimClientStateKey.STORE_SEQUENCE_MODIFIED, Boolean.TRUE);
    }

    private boolean isUpdateRequired(StoreSequenceItemWrapper wrapper) {
        if (wrapper.getStoreSequenceItem().isDirty()) {
            if (wrapper.getSequenceArea() == null) {
                return false;
            }
            if (wrapper.getSequenceArea().isNotSequenced()) {
                return false;
            }
            if (wrapper.getTicketQuantity() == null) {
                return false;
            }
            if (wrapper.getTicketQuantity().intValue() < 1) {
                return false;
            }
            return true;
        }
        return false;
    }

    public void releaseAllSequenceLock() {
        try {
            releaseLock(ActivityLockType.STORE_SEQUENCE_ALL, String.valueOf(getStoreId()));
        } catch (Exception e) {
            UILog.debug(getClass(), e);
        }
    }
}
