package oracle.retail.sim.client.swing.frame;

import oracle.retail.sim.common.core.SimEnum;

/**
 * This class encapsulates all the mode that the application frame can use to exit.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public enum ApplicationExitMode implements SimEnum<Integer> {
    NORMAL(0, "NORMAL"),
    DISSOLVE(1, "DISSOLVE"),
    SHRINK(2, "SHRINK"),
    SPIN(3, "SPIN");

    private final int code;
    private final String description;

    ApplicationExitMode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String toString() {
        return description;
    }

    public static ApplicationExitMode toValue(Integer code) {
        if (code != null) {
            for (ApplicationExitMode value : values()) {
                if (value.code == code) {
                    return value;
                }
            }
        }
        return NORMAL;
    }

    public static ApplicationExitMode toValue(String description) {
        if (description != null) {
            for (ApplicationExitMode value : values()) {
                if (value.description.equalsIgnoreCase(description)) {
                    return value;
                }
            }
        }
        return NORMAL;
    }
}
