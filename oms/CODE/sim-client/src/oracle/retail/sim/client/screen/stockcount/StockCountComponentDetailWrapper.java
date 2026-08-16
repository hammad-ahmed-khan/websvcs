package oracle.retail.sim.client.screen.stockcount;

import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.business.Wrapper;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemType;
import oracle.retail.sim.common.stockcount.StockCountLineItemCompBreakdownVO;

/********************************************************************************************************
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class StockCountComponentDetailWrapper extends Wrapper {

    private StockCountLineItemCompBreakdownVO breakdownVO;

    public StockCountComponentDetailWrapper(StockCountLineItemCompBreakdownVO breakdownVO) {
        this.breakdownVO = breakdownVO;
    }

    public String getItemId() {
        return breakdownVO.getItemId();
    }

    public String getItemDescription() {
        if (SimConfigManager.isItemShortDescription()) {
            return breakdownVO.getShortDescription();
        }
        return breakdownVO.getLongDescription();
    }

    public ItemType getItemType() {
        return breakdownVO.getItemType();
    }

    public Integer getComponentCount() {
        return breakdownVO.getComponentCount();
    }

    public String getUnitOfMeasure() {
        return breakdownVO.getUnitOfMeasure();
    }

    public Quantity getStockCounted() {
        return breakdownVO.getStockCounted();
    }

    public Quantity getStockRecounted() {
        return breakdownVO.getStockRecounted();
    }
}