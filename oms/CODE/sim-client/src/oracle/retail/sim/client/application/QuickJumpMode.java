package oracle.retail.sim.client.application;

import oracle.retail.sim.common.core.SimEnum;

/**
 * This class encapsulates all the quick jump mode.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public enum QuickJumpMode implements SimEnum<String> {
    DIRECT_DELIVERY("Direct Delivery"),
    FULFILLMENT_ORDER("Customer Order"),
    FULFILLMENT_ORDER_PICK("Customer Order Pick"),
    INVENTORY_ADJUSTMENT("Inventory Adjustment"),
    ITEM_LOOKUP("Item Lookup"),
    PRODUCT_GROUP("Product Group"),
    RETURN("Return"),
    STOCK_COUNT("Stock Count"),
    TRANSFER("Transfer"),
    TRANSACTION_HISTORY("Transaction History"),
    WAREHOUSE_DELIVERY("Warehouse Delivery");

    private String description;

    QuickJumpMode(String description) {
        this.description = description;
    }

    public String getCode() {
        return name();
    }

    public String toString() {
        return description;
    }
}
