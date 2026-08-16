package oracle.retail.sim.client.application;

import java.util.Hashtable;

/**
 * Default implementation of a repository. It separates repository information into permanent global
 * objects and temporary state objects.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class DefaultRepository {
    private final Hashtable<String, Object> stateTable = new Hashtable<>();

    /**
     * Return an array of all the keys that are currently being used by the state repository. Each key
     * will be map to a non-<null>object.
     * @return All the state repository object keys.
     */
    public String[] getStateKeys() {
        return stateTable.keySet().toArray(new String[stateTable.size()]);
    }

    /**
     * Add an object to the State Repository.
     * @param key The specified key that maps the stored object.
     * @param object The object to be stored.
     */
    public void addStateObject(String key, Object object) {
        stateTable.put(key, object);
    }

    /**
     * Retrieve an object from the State Repository.
     * @param key The key that an object is mapped to.
     * @return The Object stored in repository mapped to specified key. May be <null>if key does not
     *         exist.
     */
    public Object getStateObject(String key) {
        return stateTable.get(key);
    }

    /**
     * Permanently remove the object that is mapped to the specified key from the State Repository. The
     * object removed will be garbage collected when the next gc routine runs in which the object has no
     * more external references.
     * <p>
     * @param key The key that maps to the target object. If <code>aKey</code> does not exist in the
     *            mapping, nothing will occur.
     */
    public void removeStateObject(String key) {
        stateTable.remove(key);
    }

    /**
     * Remove all mappings from the State Repository. This typically should be done once all information
     * within is no longer pertinent.
     */
    public void clearStateObjects() {
        stateTable.clear();
    }
}
