package oracle.retail.sim.client.screen.stockcount;

import oracle.retail.sim.common.core.SimEnum;

/**
 * This class encapsulates all the types for the stock count detail item count filter.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public enum ItemCountType implements SimEnum<Integer> {
    ALL(1, "All"),
    COUNTED(2, "Counted"),
    UNCOUNTED(3, "Uncounted");

    private final int code;
    private final String description;

    ItemCountType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String toString() {
        return description;
    }

    public static ItemCountType toValue(Integer code) {
        if (code != null) {
            for (ItemCountType value : values()) {
                if (value.code == code) {
                    return value;
                }
            }
        }
        return null;
    }
}
