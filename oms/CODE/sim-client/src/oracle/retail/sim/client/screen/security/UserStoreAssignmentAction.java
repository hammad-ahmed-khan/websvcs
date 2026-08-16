package oracle.retail.sim.client.screen.security;

import oracle.retail.sim.common.core.SimEnum;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public enum UserStoreAssignmentAction implements SimEnum<Integer> {
    DEFAULT(0, "Assign Default Store"),
    ADD(1, "Add Store"),
    DELETE(2, "Delete Store");

    private final int code;
    private final String description;

    UserStoreAssignmentAction(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String toString() {
        return description;
    }

    public static UserStoreAssignmentAction toValue(Integer code) {
        if (code != null) {
            for (UserStoreAssignmentAction value : values()) {
                if (value.code == code) {
                    return value;
                }
            }
        }
        return null;
    }
}
