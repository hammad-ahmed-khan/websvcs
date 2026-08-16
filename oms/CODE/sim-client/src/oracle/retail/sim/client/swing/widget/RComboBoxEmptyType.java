package oracle.retail.sim.client.swing.widget;

import oracle.retail.sim.common.core.SimEnum;

/**
 * This class encapsulates all the empty display values allows for a combo box.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public enum RComboBoxEmptyType implements SimEnum<Integer> {
    BLANK(1, " "),
    SELECT(2, "-Select-"),
    ALL(3, "-All-"),
    ALL_STORES(4, "All Stores");

    private final int code;
    private final String description;

    RComboBoxEmptyType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String toString() {
        return description;
    }

    public static RComboBoxEmptyType toValue(Integer code) {
        if (code != null) {
            for (RComboBoxEmptyType value : values()) {
                if (value.code == code) {
                    return value;
                }
            }
        }
        return null;
    }
}
