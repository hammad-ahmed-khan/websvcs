package oracle.retail.sim.client.screen.storesequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.screen.itemticket.ItemTicketPrintUtility;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.itemticket.ItemTicket;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyCache;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;
import oracle.retail.sim.common.report.ReportMessageText;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.StorePrinter;
import oracle.retail.sim.common.storesequence.StoreSequenceArea;
import oracle.retail.sim.common.storesequence.StoreSequenceAreaType;
import oracle.retail.sim.common.storesequence.StoreSequenceMessageText;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Store Sequence List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreSequenceListModel extends SimScreenModel {

    private StoreSequenceWrapper storeSequence = ClientWrapperFactory.createStoreSequenceWrapper(getStoreId());

    public boolean isEditMode() {
        return RepositoryManager.getStateObject(SimClientStateKey.STORE_SEQUENCE_EDIT_MODE) != null;
    }

    public void loadSequenceAreas() throws Exception {
        storeSequence.setSequenceAreas(ClientServiceFactory.getStoreSequenceServices().findStoreSequenceAreas(getStoreId()));
    }

    public String getSelectedItemId() {
        Object object = RepositoryManager.getStateObject(SimClientStateKey.SELECTED_ITEM);
        if (object != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_ITEM);
        }
        if (object instanceof ItemVO) {
            return ((ItemVO) object).getId();
        }
        if (object instanceof StockItem) {
            return ((StockItem) object).getId();
        }
        return null;
    }

    // Start method by making sure re-sort data to incorporate any sequence changes
    public List<StoreSequenceAreaWrapper> getSequenceAreaWrappers() {
        List<StoreSequenceAreaWrapper> sequenceAreaWrappers = new ArrayList<>();
        boolean isEditMode = isEditMode();
        for (StoreSequenceArea tempSequenceArea : storeSequence.getSortedSequenceAreas()) {
            if (isEditMode && tempSequenceArea.isNoLocationType()) {
                continue;
            }
            sequenceAreaWrappers.add(ClientWrapperFactory.createStoreSequenceAreaWrapper(tempSequenceArea, storeSequence));
        }
        return sequenceAreaWrappers;
    }

    public void storeSelectedSequenceArea(StoreSequenceAreaWrapper wrapper) {
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_STORE_SEQUENCE, wrapper.getSequenceArea());
    }

    public void storeItemForLookup(String itemId) throws Exception {
        if (StringUtility.isNullOrEmpty(itemId)) {
            throw new BusinessException(ItemMessageText.ITEM_NOT_FOUND);
        }
        StockItem stockItem = ClientServiceFactory.getItemServices().readStockItem(itemId, getStoreId());
        if (stockItem == null) {
            throw new BusinessException(ItemMessageText.ITEM_NOT_FOUND);
        }
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_ITEM, stockItem);
    }

    public StoreSequenceAreaWrapper createSequenceAreaWrapper() {
        return ClientWrapperFactory.createStoreSequenceAreaWrapper(storeSequence.createSequenceArea(), storeSequence);
    }

    public void applyClassList(StoreSequenceAreaType areaType) throws Exception {
        List<MdseHierarchyNode> hierarchyClassList = new ArrayList<>();
        for (MdseHierarchyNode departmentNode : MdseHierarchyCache.getAllDepartments()) {
            hierarchyClassList.addAll(MdseHierarchyCache.getClasses(departmentNode));
        }
        Collections.sort(hierarchyClassList, new HiearchyNodeComparator());
        storeSequence.applyClassList(areaType, hierarchyClassList);
    }

    public void removeSequenceArea(StoreSequenceAreaWrapper wrapper) throws BusinessException {
        storeSequence.removeSequenceArea(wrapper.getSequenceArea());
    }

    public boolean obtainSequenceLock() throws Exception {
        return obtainLock(ActivityLockType.STORE_SEQUENCE_ALL, storeSequence.getLockId());
    }

    public void releaseSequenceLock() throws Exception {
        releaseLock(ActivityLockType.STORE_SEQUENCE_ALL, storeSequence.getLockId());
    }

    public boolean isSequenceAreasCoherent(List<StoreSequenceAreaWrapper> wrappers) throws BusinessException {
        Map<String, StoreSequenceAreaType> descriptionAreaMap = new HashMap<>();
        for (StoreSequenceAreaWrapper wrapper : wrappers) {
            StoreSequenceArea sequenceArea = wrapper.getSequenceArea();
            if (sequenceArea.isCoherent()) {
                StoreSequenceAreaType areaType = descriptionAreaMap.get(sequenceArea.getDescription());
                if (areaType == sequenceArea.getAreaType()) {
                    throw new BusinessException(StoreSequenceMessageText.DUPLICATE_ENTRY);
                }
                descriptionAreaMap.put(sequenceArea.getDescription(), sequenceArea.getAreaType());
            }
        }
        return true;
    }

    public void updateStoreSequences() throws Exception {
        ClientServiceFactory.getStoreSequenceServices().updateStoreSequenceAreas(storeSequence.getUpdatedAreas(), storeSequence.getDeletedSequenceAreas(), getStoreId());
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_STORE_SEQUENCE);
        RepositoryManager.removeStateObject(SimClientStateKey.STORE_SEQUENCE_ITEM_FILTER);
        releaseSequenceLock();
    }

    public List<ItemTicket> buildTickets(List<StoreSequenceAreaWrapper> wrappers) throws Exception {
        List<Long> sequenceAreaIds = new ArrayList<>();
        for (StoreSequenceAreaWrapper wrapper : wrappers) {
            sequenceAreaIds.add(wrapper.getId());
        }
        return ClientServiceFactory.getItemTicketServices().createItemTickets(getStoreId(), sequenceAreaIds);
    }

    public int printTickets(List<ItemTicket> itemTickets) throws Exception {
        int failed = 0;
        if(itemTickets == null) {
            return 0;
        }
        for (ItemTicket itemTicket : itemTickets) {
            StorePrinter printer = SimClientPrintUtility.selectItemTicketPrinter(itemTicket, getStore());
            if (printer == null) {
                return 0;
            }
            ReportResponse response = ItemTicketPrintUtility.printTicket(itemTicket, printer);
            if (response != null && response.getMessage() == ReportMessageText.BIP_AUTHENTICATION_FAILURE_MESSAGE) {
                failed = -1; // Changed to -1 in case of Authentication Failure
                break;
            }
            if (response != null) {
                failed++;
            }
        }
        return failed;
    }

    /** HIERARCHY CLASS COMPARATOR * */
    private class HiearchyNodeComparator implements Comparator<MdseHierarchyNode> {
        public int compare(MdseHierarchyNode node1, MdseHierarchyNode node2) {
            int value = StringUtility.compareTo(node1.getDepartmentName(), node2.getDepartmentName());
            if (value == 0) {
                value = StringUtility.compareTo(node1.getClassName(), node2.getClassName());
            }
            return value;
        }
    }
}
