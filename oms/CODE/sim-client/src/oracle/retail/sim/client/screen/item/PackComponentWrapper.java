package oracle.retail.sim.client.screen.item;

import java.io.Serializable;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.StockItem;

/********************************************************************************************************
 * Pack Component Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class PackComponentWrapper implements Serializable {
    private static final long serialVersionUID = 5259697477798637996L;

    private StockItem stockItem;
    private Quantity componentQty;
    private String upc;

    public PackComponentWrapper(StockItem stockItem) {
        this.stockItem = stockItem;
    }

    public String getId() {
        return stockItem.getId();
    }

    public String getDescription() {
        if (SimConfigManager.isItemShortDescription()) {
            return stockItem.getShortDescription();
        }
        return stockItem.getLongDescription();
    }

    public Quantity getAvailableStockOnHand() {
        return stockItem.getAvailableStockOnHand();
    }

    public Quantity getComponentQuantity() {
        return componentQty;
    }

    public void doSetComponentQuantity(Quantity componentQty) {
        this.componentQty = componentQty;
    }

    public String getUPC() {
        return upc;
    }

    public void doSetUPC(String upc) {
        this.upc = upc;
    }
}
