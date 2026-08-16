package oracle.retail.sim.client.screen.storesequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;
import oracle.retail.sim.common.storesequence.StoreSequenceArea;
import oracle.retail.sim.common.storesequence.StoreSequenceAreaComparator;
import oracle.retail.sim.common.storesequence.StoreSequenceAreaType;
import oracle.retail.sim.common.storesequence.StoreSequenceMessageText;

/**
 * Represents a store that can be sequenced. It contains all the sequenced areas. This is not a business object passed between the client
 * and server but rather an object to "warp" the sequence areas being worked on for convenience.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class StoreSequenceWrapper {

    private Long storeId;
    private List<StoreSequenceArea> sequenceAreas = new ArrayList<>();
    private List<StoreSequenceArea> deletedSequenceAreas = new ArrayList<>();

    public StoreSequenceWrapper(Long storeId) {
        this.storeId = storeId;
    }

    public String getLockId() {
        return storeId.toString();
    }

    public void setSequenceAreas(List<StoreSequenceArea> seqAreas) {
        if (seqAreas == null) {
            seqAreas = new ArrayList<>();
        }
        sequenceAreas = seqAreas;
    }

    public List<StoreSequenceArea> getSequenceAreas() {
        return sequenceAreas;
    }

    public List<StoreSequenceArea> getSortedSequenceAreas() {
        Collections.sort(sequenceAreas, new StoreSequenceAreaComparator());
        return sequenceAreas;
    }

    public List<StoreSequenceArea> getUpdatedAreas() {
        List<StoreSequenceArea> updatedSequenceAreas = new ArrayList<>();
        for (StoreSequenceArea tempSequence : sequenceAreas) {
            if (tempSequence.getId() == null || tempSequence.isDirty()) {
                updatedSequenceAreas.add(tempSequence);
            }
        }
        return updatedSequenceAreas;
    }

    public List<StoreSequenceArea> getDeletedSequenceAreas() {
        return deletedSequenceAreas;
    }

    public void addSequenceArea(StoreSequenceArea storeSequenceArea) {
        sequenceAreas.add(storeSequenceArea);
    }

    public StoreSequenceArea createSequenceArea() {
        StoreSequenceArea sequenceArea = BOFactory.createStoreSequenceArea();
        sequenceArea.doSetStoreId(storeId);
        sequenceArea.setOrder(getNextSequenceOrder());

        sequenceAreas.add(sequenceArea);

        return sequenceArea;
    }

    public void applyClassList(StoreSequenceAreaType areaType, List<MdseHierarchyNode> hierarchyClassList) throws Exception {
        int sequenceOrder = getNextSequenceOrder();

        Set<String> areaNames = new HashSet<>();

        StoreSequenceArea sequenceArea = null;
        String compositeName = null;
        for (MdseHierarchyNode classNode : hierarchyClassList) {
            compositeName = classNode.getCompositeName();

            sequenceArea = BOFactory.createStoreSequenceArea();
            sequenceArea.doSetStoreId(storeId);
            sequenceArea.doSetDescription(compositeName);
            sequenceArea.doSetAreaType(areaType);
            sequenceArea.doSetDepartmentId(classNode.getDepartmentId());
            sequenceArea.doSetClassId(classNode.getClassId());
            sequenceArea.doSetOrder(sequenceOrder++);

            if (!areaNames.contains(compositeName)) {
                sequenceAreas.add(sequenceArea);
            }
            areaNames.add(compositeName);
        }
    }

    public int getNextSequenceOrder() {
        int maxSequence = 0;
        for (StoreSequenceArea sequenceArea : sequenceAreas) {
            if (sequenceArea.getOrder() > maxSequence) {
                maxSequence = sequenceArea.getOrder();
            }
        }
        return maxSequence + 1;
    }

    public void removeSequenceArea(StoreSequenceArea sequenceArea) throws BusinessException {
        if (sequenceArea.getNumberOfItems() > 0) {
            throw new BusinessException(StoreSequenceMessageText.DELETE_LOCATION_ERROR);
        }
        if (sequenceArea.getId() != null) {
            sequenceAreas.remove(sequenceArea);
            deletedSequenceAreas.add(sequenceArea);
            return;
        }
        for (int i = 0; i < sequenceAreas.size(); i++) {
            StoreSequenceArea tmpArea = sequenceAreas.get(i);
            if (tmpArea == sequenceArea) {
                sequenceAreas.remove(i);
                return;
            }
        }
    }
}
