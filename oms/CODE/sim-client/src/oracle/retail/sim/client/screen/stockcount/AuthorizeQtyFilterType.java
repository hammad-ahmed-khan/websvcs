package oracle.retail.sim.client.screen.stockcount;

import oracle.retail.sim.common.core.SimEnum;

/*
* Copyright 2004, 2013, Oracle. All rights reserved.
*/
public enum AuthorizeQtyFilterType implements SimEnum<Integer> {
    ALL(1, "All Items"),
    AUTHORIZED(2, "Authorized"),
    UNAUTHORIZED(3, "Unauthorized");

    private final int code;
    private final String description;

    AuthorizeQtyFilterType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String toString() {
        return description;
    }

    public static AuthorizeQtyFilterType toValue(Integer code) {
        if (code != null) {
            for (AuthorizeQtyFilterType value : values()) {
                if (value.code == code) {
                    return value;
                }
            }
        }
        return null;
    }
}
