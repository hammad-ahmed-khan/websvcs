package oracle.retail.sim.client.screen.stockcount;

import oracle.retail.sim.common.core.SimEnum;

/*
* Copyright 2004, 2013, Oracle. All rights reserved.
*/
public enum DiscrepantFilterType implements SimEnum<Integer> {
    ALL(1, "All Items"),
    DISCREPANT(2, "Discrepant Items");

    private final int code;
    private final String description;

    DiscrepantFilterType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String toString() {
        return description;
    }

    public static DiscrepantFilterType toValue(Integer code) {
        if (code != null) {
            for (DiscrepantFilterType value : values()) {
                if (value.code == code) {
                    return value;
                }
            }
        }
        return null;
    }
}
