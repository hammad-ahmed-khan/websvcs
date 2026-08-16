package oracle.retail.sim.client.swing.tableeditor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/******************************************************************************************
 * Key Value Pair. A wrapper for <code>Map.Entry</code> objects.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class KeyValuePair {

    private Map.Entry entry;

    /******************************************************************************************
     * Constructs a KeyValuePair from a given map entry.
     * <p>
     * @param entry a <code>Map.Entry</code> object
     ******************************************************************************************/
    public KeyValuePair(Map.Entry entry) {
        this.entry = entry;
    }

    /******************************************************************************************
     * Returns the key corresponding to this KeyValuePair.
     ******************************************************************************************/
    public Object getKey() {
        return entry.getKey();
    }

    /******************************************************************************************
     * Replaces the value corresponding to this KeyValuePair with the specified value.
     ******************************************************************************************/
    public void setValue(Object value) {
        entry.setValue(value);
    }

    /******************************************************************************************
     * Returns the value corresponding to this KeyValuePair.
     ******************************************************************************************/
    public Object getContainedValue() {
        return entry.getValue();
    }

    /******************************************************************************************
     * Returns a reference to this object.
     ******************************************************************************************/
    public KeyValuePair getValue() {
        return this;
    }

    /******************************************************************************************
     * Creates a List of KeyValuePair objects for the contents of a given Map.
     * <p>
     * @param map A map
     * @return A <code>List</code> containing KeyValuePair objects.
     ******************************************************************************************/
    public static List<KeyValuePair> createKeyValuePairs(Map map) {
        List<KeyValuePair> keyValuePairList = new ArrayList<>(map.size());
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
            keyValuePairList.add(new KeyValuePair((Map.Entry) iterator.next()));
        }
        return keyValuePairList;
    }
}
