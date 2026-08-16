package oracle.retail.sim.client.screen.storesequence;

import java.util.HashMap;
import java.util.Map;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.activitylock.ActivityLockUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.item.Item;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.rules.storesequence.StoreSequenceItemShopFloorRule;
import oracle.retail.sim.common.storesequence.StoreSequenceArea;
import oracle.retail.sim.common.storesequence.StoreSequenceItem;
import oracle.retail.sim.common.storesequence.StoreSequenceMessageText;
import oracle.retail.sim.common.storesequence.StoreSequenceProperty;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * A Wrapper For Store Sequence Items in order for them to interface with tables
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreSequenceItemWrapper {

    private StoreSequenceArea sequenceArea;
    private StoreSequenceItem sequenceItem;
    private Map<String, TicketTypeFormat> shelfLabelMap = new HashMap<>();

    public StoreSequenceItemWrapper(StoreSequenceItem sequenceItem) {
        this.sequenceItem = sequenceItem;
    }

    public StoreSequenceItem getStoreSequenceItem() {
        return sequenceItem;
    }

    public StoreSequenceArea getSequenceArea() {
        return sequenceArea;
    }

    public void setSequenceArea(StoreSequenceArea sequenceArea) throws BusinessException {
        if (sequenceArea != null) {
            if (isAreaLocked(sequenceArea.getIdAsString())) {
                throw new BusinessException(StoreSequenceMessageText.SEQUENCE_AREA_LOCKED);
            }
            if (sequenceItem != null) {
                sequenceItem.setSequenceAreaId(sequenceArea.getId());
            }
            doSetSequenceArea(sequenceArea);
        }
    }

    public void doSetSequenceArea(StoreSequenceArea sequenceArea) {
        this.sequenceArea = sequenceArea;
    }

    public void setShelfLabelMap(Map<String, TicketTypeFormat> shelfLabelMap) {
        this.shelfLabelMap = shelfLabelMap;
    }

    public void setStockItem(StockItem stockItem) throws Exception {
        if (stockItem == null) {
            return;
        }
        StoreSequenceItemShopFloorRule.execute(sequenceArea, stockItem);
        sequenceItem.doSetItemId(stockItem.getId());
        sequenceItem.doSetShortDescription(stockItem.getShortDescription());
        sequenceItem.doSetLongDescription(stockItem.getLongDescription());
        sequenceItem.doSetMultipleAreaSequenced(isItemAlreadySequenced(stockItem.getId(), stockItem.getStoreId()));
        sequenceItem.doSetCapacity(Quantity.ZERO);
        sequenceItem.doSetWidth(Quantity.ZERO);
    }

    /**
     * Return a false item associated with this sequence item. NOTE: The returned StockItem is
     * incoherent. In only contains the item identifier and descriptions of the item as sequences
     * information needs none of the other values.
     */
    public StockItem getStockItem() {
        Item item = BOFactory.createItem(sequenceItem.getItemId());
        item.doSetShortDescription(sequenceItem.getShortDescription());
        item.doSetLongDescription(sequenceItem.getLongDescription());

        return BOFactory.createStockItem(item, sequenceItem.getStoreId());
    }

    public String getItemDescription() {
        return sequenceItem != null ? getStockItem().getItemDescription() : null;
    }

    public Quantity getCapacity() {
        return sequenceItem != null ? sequenceItem.getCapacity() : null;
    }

    public void setCapacity(Quantity capacity) throws BusinessException {
        sequenceItem.setCapacity(capacity);
    }

    public Quantity getWidth() {
        return sequenceItem != null ? sequenceItem.getWidth() : null;
    }

    public void setWidth(Quantity width) throws BusinessException {
        sequenceItem.setWidth(width);
    }

    public UOMMode getUnitOfMeasureMode() {
        return sequenceItem != null ? sequenceItem.getUnitOfMeasureMode() : UOMMode.STANDARD;
    }

    public void setUnitOfMeasureMode(UOMMode uomMode) throws BusinessException {
        sequenceItem.setUnitOfMeasureMode(uomMode);
    }

    public TicketTypeFormat getShelfLabelFormat() {
        return sequenceItem != null ? shelfLabelMap.get(sequenceItem.getTicketTypeFormatId()) : null;
    }

    public void setShelfLabelFormat(TicketTypeFormat ticketTypeFormat) throws BusinessException {
        if (ticketTypeFormat != null) {
            sequenceItem.setTicketTypeFormatId(ticketTypeFormat.getId());
            return;
        }
        if (!SimConfigManager.getBoolean(StoreConfigKeys.AUTO_DEFAULT_LABEL)) {
            sequenceItem.setTicketTypeFormatId(null);
        }
    }

    public Integer getTicketQuantity() {
        return sequenceItem != null ? sequenceItem.getTicketQuantity() : null;
    }

    public void setTicketQuantity(Integer ticketQuantity) throws BusinessException {
        sequenceItem.setTicketQuantity(ticketQuantity);
    }

    public Boolean isMultipleAreaSequenced() {
        return sequenceItem != null ? sequenceItem.isMultipleAreaSequenced() : Boolean.FALSE;
    }

    public Boolean isPrimary() {
        return sequenceItem.isPrimary();
    }

    public void setPrimary(Boolean isPrimary) throws BusinessException {
        sequenceItem.setPrimary(isPrimary);
    }

    public void setSequenceOrder(int order) throws BusinessException {
        sequenceItem.setOrder(order);
    }

    public void swapSequenceOrder(StoreSequenceItemWrapper otherWrapper) throws BusinessException {
        StoreSequenceItem otherItem = otherWrapper.getStoreSequenceItem();
        int myOrder = sequenceItem.getOrder();
        int otherOrder = otherItem.getOrder();
        sequenceItem.setOrder(otherOrder);
        otherItem.setOrder(myOrder);
    }

    public boolean isPropertyModifiable(String property) {
        if (StoreSequenceProperty.SEQUENCE_AREA.equals(property)) {
            if (sequenceArea != null) {
                return sequenceArea.isNotSequenced();
            }
            return isSequenceItemModifiable(property);
        }
        if (StoreSequenceProperty.STOCK_ITEM.equals(property)) {
            if (sequenceItem.getItemId() != null) {
                return false;
            }
            return isSequenceItemModifiable(property);
        }

        if (sequenceArea == null || sequenceItem == null || sequenceItem.getItemId() == null) {
            return false;
        }
        return isSequenceItemModifiable(property);
    }

    private boolean isItemAlreadySequenced(String itemId, Long storeId) throws Exception {
        return ClientServiceFactory.getStoreSequenceServices().findStoreSequenceItems(itemId, storeId).size() > 0;
    }

    private boolean isSequenceItemModifiable(String property) {
        if (sequenceItem == null) {
            return true;
        }
        return sequenceItem.isPropertyModifiable(property);
    }

    private boolean isAreaLocked(String lockId) {
        try {
            return !ActivityLockUtility.findActivityLockOwners(ActivityLockType.STORE_SEQUENCE, lockId).isEmpty();
        } catch (Exception ex) {
            return true;
        }
    }
}
