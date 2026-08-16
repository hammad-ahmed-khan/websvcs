package oracle.retail.sim.client.screen.storesequence;

import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.storesequence.StoreSequenceArea;
import oracle.retail.sim.common.storesequence.StoreSequenceAreaType;
import oracle.retail.sim.common.storesequence.StoreSequenceMessageText;
import oracle.retail.sim.common.storesequence.StoreSequenceProperty;

/********************************************************************************************************
 * Store Sequence Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreSequenceAreaWrapper {

    private StoreSequenceWrapper storeSequence;
    private StoreSequenceArea sequenceArea;

    public StoreSequenceAreaWrapper(StoreSequenceArea sequenceArea, StoreSequenceWrapper storeSequence) {
        this.sequenceArea = sequenceArea;
        this.storeSequence = storeSequence;
    }

    public StoreSequenceArea getSequenceArea() {
        return sequenceArea;
    }

    public Long getId() {
        return sequenceArea.getId();
    }

    public StoreSequenceAreaType getAreaType() {
        return sequenceArea.getAreaType();
    }

    public void setAreaType(StoreSequenceAreaType areaType) throws BusinessException {
        // Validate Not A Duplicate
        for (StoreSequenceArea tempSequenceArea : storeSequence.getSequenceAreas()) {
            if (tempSequenceArea != sequenceArea) {
                if (sequenceArea.getDescription() != null) {
                    if (sequenceArea.getDescription().equals(tempSequenceArea.getDescription())) {
                        if (tempSequenceArea.getAreaType() == areaType) {
                            throw new BusinessException(StoreSequenceMessageText.DUPLICATE_ENTRY_ERROR);
                        }
                    }
                }
            }
        }
        sequenceArea.setAreaType(areaType);
    }

    public String getDescription() {
        return sequenceArea.getDescription();
    }

    public void setDescription(String description) throws BusinessException {
        if (StringUtility.isNullOrEmpty(description)) {
            return;
        }
        description = description.trim();

        // Validate Not A Duplicate
        for (StoreSequenceArea tempSequenceArea : storeSequence.getSequenceAreas()) {
            if (tempSequenceArea != sequenceArea) {
                if (description.equals(tempSequenceArea.getDescription())) {
                    if ((tempSequenceArea.getAreaType() != null) && (sequenceArea.getAreaType() != null)) {
                        if (tempSequenceArea.getAreaType() == sequenceArea.getAreaType()) {
                            throw new BusinessException(StoreSequenceMessageText.DUPLICATE_ENTRY_ERROR);
                        }
                    }
                }
            }
        }
        sequenceArea.setDescription(description);
    }

    public void swapSequenceOrder(StoreSequenceAreaWrapper otherWrapper) {
        StoreSequenceArea otherSequenceArea = otherWrapper.getSequenceArea();
        int myOrder = sequenceArea.getOrder();
        int otherOrder = otherSequenceArea.getOrder();
        sequenceArea.setOrder(otherOrder);
        otherSequenceArea.setOrder(myOrder);
    }

    public boolean isNotSequenced() {
        return sequenceArea.isNotSequenced();
    }

    public Integer getNumberOfItems() {
        return sequenceArea.getNumberOfItems();
    }

    public boolean isPropertyModifiable(String property) {
        if (property.equals(StoreSequenceProperty.AREA_DESCRIPTION)) {
            return !sequenceArea.isCreatedFromHierarchy();
        }
        if (property.equals(StoreSequenceProperty.AREA_TYPE)) {
            return true;
        }
        return false;
    }
}