package oracle.retail.sim.client.screen.item;

import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.ItemVOQueryFilter;
import oracle.retail.sim.common.item.OrderItem;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.source.Finisher;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.source.Warehouse;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Item Lookup Tab Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemLookupTabModel extends SimScreenModel {

    private ItemDetailVO detailVO;
    private ItemLookupType lookupType = ItemLookupType.ITEM;

    public ItemDetailVO getDetailItem(ItemVO itemVO) throws Exception {
        if (detailVO == null || !detailVO.getId().equals(itemVO.getId())) {
            detailVO = ClientServiceFactory.getItemServices().readItemDetailVO(itemVO.getId(), getStoreId());
        }
        return detailVO;
    }

    public void setItemLookupType(ItemLookupType lookupType) {
        this.lookupType = lookupType;
    }

    public List<ItemVO> findItemVOs(ItemVOQueryFilter filter) throws Exception {
        return ClientServiceFactory.getItemServices().findItemVOs(filter, getStoreId());
    }

    public Object processItemVO(ItemVO itemVO) throws BusinessException {
        Object selectedItem = RepositoryManager.getStateObject(SimClientStateKey.SELECTED_ITEM);
        if (lookupType == ItemLookupType.STOCK_ITEM) {
            return readStockItem(itemVO.getId(), selectedItem);
        }
        if (lookupType == ItemLookupType.ORDER_ITEM) {
            return readOrderItem(itemVO.getId(), selectedItem);
        }
        return itemVO;
    }

    // Checking for size one is valid because we already found ranged or non-ranged VOs, meaning no barcode
    private StockItem readStockItem(String itemId, Object selectedItem) throws BusinessException {
        if (selectedItem instanceof StockItem) {
            StockItem stockItem = (StockItem) selectedItem;
            if (itemId.equals(stockItem.getId())) {
                return stockItem;
            }
        }
        try {
            List<StockItem> stockItems = ClientServiceFactory.getItemServices().findStockItems(itemId, getStoreId());
            if (stockItems.size() == 1) {
                return stockItems.get(0);
            }
            throw new BusinessException(ItemMessageText.ITEM_READ_FAILED);
        } catch (Throwable exception) {
            throw new BusinessException(ItemMessageText.ITEM_READ_FAILED, exception);
        }
    }

    private OrderItem readOrderItem(String itemId, Object selectedItem) throws BusinessException {
        try {
            return ClientServiceFactory.getItemServices().readOrderItem(itemId, getStoreId(), true);
        } catch (Throwable exception) {
            throw new BusinessException(ItemMessageText.ITEM_READ_FAILED, exception);
        }
    }

    public Integer getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_ITEM_LOOKUP);
    }

    public boolean isFinishersEnabled() {
        return SimConfigManager.getBoolean(SimConfigManager.EXTERNAL_FINISHER_ENABLED);
    }

    public Supplier getSupplier() {
        return (Supplier) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_SUPPLIER);
    }

    public Warehouse getWarehouse() {
        return (Warehouse) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_WAREHOUSE);
    }

    public Finisher getFinisher() {
        return (Finisher) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_FINISHER);
    }
}
