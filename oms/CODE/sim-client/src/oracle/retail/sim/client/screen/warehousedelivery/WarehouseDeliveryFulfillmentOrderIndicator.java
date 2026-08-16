package oracle.retail.sim.client.screen.warehousedelivery;

import oracle.retail.sim.common.core.SimEnum;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public enum WarehouseDeliveryFulfillmentOrderIndicator implements SimEnum<Integer> {
    NO(0, "No"),
    YES(1, "Yes"),
    MIXED(2, "Mixed");

    private final Integer code;
    private final String description;

    WarehouseDeliveryFulfillmentOrderIndicator(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String toString() {
        return description;
    }

    public static WarehouseDeliveryFulfillmentOrderIndicator toValue(Integer code) {
        if (code != null) {
            for (WarehouseDeliveryFulfillmentOrderIndicator value : values()) {
                if (value.code == code) {
                    return value;
                }
            }
        }
        return null;
    }
}
