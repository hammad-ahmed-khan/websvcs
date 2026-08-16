package oracle.retail.sim.client.screen.item;

import oracle.retail.sim.common.core.SimEnum;

/**
 * This class encapsulates all the types for the item lookup dialog.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public enum ItemLookupType implements SimEnum<String> {
    ITEM,
    STOCK_ITEM,
    ORDER_ITEM;

    public String getCode() {
        return name();
    }
}
