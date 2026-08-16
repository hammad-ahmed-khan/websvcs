package oracle.retail.sim.client.screen.item;

import java.util.Date;
import java.util.TimeZone;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.StoreItemStockVO;

/********************************************************************************************************
 * Stock Locator Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockLocatorWrapper {

    private StoreItemStockVO stockVO;
    private TimeZone timeZone;
    private Date today;

    public StockLocatorWrapper(StoreItemStockVO stockVO, TimeZone timeZone, Date today) {
        this.stockVO = stockVO;
        this.timeZone = timeZone;
        this.today = today;
    }

    public String getStoreDescription() {
        return stockVO.getStoreDescription();
    }

    public Quantity getAvailableStockOnHand() {
        return stockVO.getAvailableStockOnHand();
    }

    public Quantity getReceivedToday() {
        if (SimDateUtil.isSameDay(timeZone, stockVO.getLastReceivedDay(), today)) {
            return stockVO.getReceivedToday();
        }
        return Quantity.ZERO;
    }

    public boolean isBuddyStore() {
        return stockVO.isBuddyStore();
    }
}
