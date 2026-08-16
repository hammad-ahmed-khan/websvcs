package oracle.retail.sim.client.screen.item;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.StoreItemStockVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Stock Locator Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockLocatorModel extends SimScreenModel {

    public ItemDetailVO loadItem() {
        return (ItemDetailVO) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_ITEM);
    }

    public List<StockLocatorWrapper> findAvailableStock(ItemDetailVO itemDetailVO) throws Exception {
        List<StockLocatorWrapper> wrappers = new ArrayList<>();
        Date today = SimDateUtil.getCurrentDate();
        TimeZone timeZone = getTimeZone();
        List<StoreItemStockVO> stockVOs = ClientServiceFactory.getItemServices().findStoreItemStockVOs(itemDetailVO.getId(), getStoreId());
        for (StoreItemStockVO stockVO : stockVOs) {
            wrappers.add(new StockLocatorWrapper(stockVO, timeZone, today));
        }
        return wrappers;
    }
}
