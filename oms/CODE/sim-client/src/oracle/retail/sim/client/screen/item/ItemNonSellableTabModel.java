package oracle.retail.sim.client.screen.item;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.NonSellableQtyType;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.lineitem.UOMMode;

/********************************************************************************************************
 * Item Non-Sellable Quantity Information Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemNonSellableTabModel extends SimScreenModel {
    private ItemDetailVO itemDetailVO;

    public boolean setItemDetail(ItemDetailVO itemVO) throws BusinessException {
        if (itemDetailVO == null || !itemDetailVO.equals(itemVO)) {
            itemDetailVO = itemVO;
            return true;
        }
        return false;
    }

    public String getItemId() {
        return itemDetailVO.getId();
    }

    public String getItemDescription() {
        if (SimConfigManager.isItemShortDescription()) {
            return itemDetailVO.getShortDescription();
        }
        return itemDetailVO.getLongDescription();
    }

    public String getUnitOfMeasure() {
        return new UomModeDisplayer().getDisplayText(UOMMode.STANDARD, itemDetailVO);
    }

    public Quantity getNonSellableQuantity() {
        StockItem stockItem = itemDetailVO.getStockItem();
        return stockItem.isInventoryDisplayable() ? stockItem.getNonSellableQty() : null;
    }

    public List<NonSellableQuantityWrapper> getQuantityWrappers() throws Exception {
        List<NonSellableQuantityWrapper> wrappers = new ArrayList<>();
        StockItem stockItem = itemDetailVO.getStockItem();
        for (NonSellableQtyType quantityType : ClientDataCacheUtility.getNonSellableQtyTypes()) {
            if (stockItem.isInventoryDisplayable()) {
                Quantity quantity = stockItem.getNonSellableTypeQty(quantityType.getId());
                if (quantity != null && quantity.isPositive()) {
                    wrappers.add(ClientWrapperFactory.createNonSellableQuantityWrapper(quantityType, quantity));
                }
            }
        }
        return wrappers;
    }
}
