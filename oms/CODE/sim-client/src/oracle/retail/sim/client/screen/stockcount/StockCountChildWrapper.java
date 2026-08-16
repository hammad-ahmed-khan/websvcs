package oracle.retail.sim.client.screen.stockcount;

import oracle.retail.sim.common.stockcount.StockCountChild;
import oracle.retail.sim.common.stockcount.StockCountPhase;
import oracle.retail.sim.common.stockcount.StockCountStatus;
import oracle.retail.sim.common.storesequence.StoreSequenceAreaType;

/********************************************************************************************************
 * Stock Count Location Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountChildWrapper {

    private StockCountChild stockCountChild;

    public StockCountChildWrapper(StockCountChild stockCountChild) {
        this.stockCountChild = stockCountChild;
    }

    public StockCountChild getStockCountChild() {
        return stockCountChild;
    }

    public Long getId() {
        return stockCountChild.getId();
    }

    public String getDescription() {
        StringBuilder buffer = new StringBuilder();
        if (stockCountChild.getDescription() != null) {
            buffer.append(stockCountChild.getDescription());
        }
        if (stockCountChild.getBreakdownDescription() != null) {
            if (stockCountChild.getDescription() != null) {
                buffer.append(" ");
            }
            buffer.append("(").append(stockCountChild.getBreakdownDescription()).append(")");
        }
        return buffer.toString();
    }

    public StoreSequenceAreaType getArea() {
        return stockCountChild.getArea();
    }

    public StockCountPhase getPhase() {
        return stockCountChild.getPhase();
    }

    public StockCountStatus getStatus() {
        return stockCountChild.getStatus();
    }

    public String getUser() {
        if (stockCountChild.getAuthorizeUser() != null) {
            return stockCountChild.getAuthorizeUser();
        }
        if (stockCountChild.getRecountUser() != null) {
            return stockCountChild.getRecountUser();
        }
        return stockCountChild.getCountUser();
    }

    public Integer getItemsLeftToCount() {
        return stockCountChild.getItemsLeftToCount();
    }
}
