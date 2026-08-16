package oracle.retail.sim.client.screen.security;

import oracle.retail.sim.common.core.SimEnum;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public enum UserRoleAssignmentAction implements SimEnum<Integer> {
    ADD(0, "Add Role"),
    DELETE(1, "Delete Role");

    private final int code;
    private final String description;

    UserRoleAssignmentAction(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String toString() {
        return description;
    }

    public static UserRoleAssignmentAction toValue(Integer code) {
        if (code != null) {
            for (UserRoleAssignmentAction value : values()) {
                if (value.code == code) {
                    return value;
                }
            }
        }
        return null;
    }
}
