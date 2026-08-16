package oracle.retail.sim.client.screen.item;

import java.math.BigDecimal;
import java.util.Map;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.item.Item;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.StockItem;

/********************************************************************************************************
 * Item Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemDetailTabModel extends SimScreenModel {

    private ItemDetailVO detailVO;

    public boolean setItemDetail(ItemDetailVO itemVO) throws Exception {
        if (detailVO != itemVO) {
            detailVO = itemVO;
            return true;
        }
        return false;
    }

    public ItemDetailVO getDetailVO() {
        return detailVO;
    }

    public boolean isDisplaySequenceActive() {
        return getStoreBoolean(StoreConfigKeys.DISPLAY_SEQUENCE_FIELDS);
    }

    public boolean isDisplayDeliveryBayActive() {
        return getStoreBoolean(StoreConfigKeys.REPLENISHMENT_DELIVERY_BAY_INVENTORY);
    }

    public boolean isMultipleDeliveryAllowed() {
        if (detailVO != null) {
            if (detailVO.isStoreOrderReplenishmentType() && detailVO.isMultipleDeliveryAllowed()) {
                return getStoreBoolean(StoreConfigKeys.DISPLAY_ITEM_REQUEST_DELIVERY_TIMESLOT);
            }
        }
        return false;
    }

    public Quantity calculateBreakableQuantity(StockItem packItem, StockItem packComponent, Quantity quantity) {
        if (!packItem.isPack()) {
            return quantity;
        }
        if (packComponent != null && quantity != null) {

            Map<Item, BigDecimal> packComponents = packItem.getPackComponents();
            BigDecimal componentItemQty = packComponents.get(packComponent.getItem());
            return quantity.divide(new Quantity(componentItemQty));
        }
        return Quantity.ZERO;
    }
}
