package oracle.retail.sim.client.application;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class KeyValueData {

    private String key;
    private Object value;

    public KeyValueData(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        if (value == null) {
            return "";
        }
        return value.toString();
    }
}
