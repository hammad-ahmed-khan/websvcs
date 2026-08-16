package oracle.retail.sim.client.application;

/**
 * This class defines an object that maintains three (2) mappings than can be accessed via instance-level
 * methods. The three mappings that are managed here are known as the "Global Repository" and "State
 * Repository".
 * <p>
 * The purpose of the Global Repository is to maintain objects for the duration of the owning
 * application. Typically RepositoryManager is only owned and manipulated by one application during its
 * existance. Any objects placed into the Global Repository should be guaranteed to be there for
 * retrieval afterwards and in practice, do not change in value or at least in nature. Values are usually
 * loaded immediately during initialization of the application.
 * <p>
 * Examples are objects representing the user location (Store) or ClientServices e.g.
 * EmployeeClientServices, or system settings.
 * <p>
 * The purpose of the State Repository is to maintain objects that are created and pertain to the "state"
 * of the application. These objects are generally considered transitive to the session of the
 * application. There is no guarantee that any requested object in this repository will exist when
 * requested.
 * <p>
 * @note All the mappings available in this class are implemented by java.util.Hashtable to provide
 *       synchronization, therefore <null> can not be passed as the <code>key</code> to map objects by
 *       or as the <code>object</code> object to store without causing a NullPointerException.
 * @note Any object previously mapped to <code>key</code> will be replaced when the same key is used
 *       again for a different object.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public final class RepositoryManager {
    private static DefaultRepository instance;

    private RepositoryManager() {
    }

    public static void setInstance(DefaultRepository newInstance) {
        instance = newInstance;
    }

    /**
     * Lazy-loads the DefaultRepository implementation.
     */
    private static synchronized DefaultRepository getInstance() {
        if (instance == null) {
            instance = new DefaultRepository();
        }
        return instance;
    }

    /**
     * Add an object to the State Repository.
     * <p>
     * @param key The specified key that maps the stored object.
     * @param obj The object to be stored.
     */
    public static void addStateObject(String key, Object obj) {
        getInstance().addStateObject(key, obj);
    }

    /**
     * Retrieve an object from the State Repository.
     * <p>
     * @param key The key that requested object is mapped to.
     * @return The Object stored in repository mapped to specified key. May be <null> if key does not
     *         exist.
     */
    public static Object getStateObject(String key) {
        return getInstance().getStateObject(key);
    }

    /**
     * Return an array of all the keys that are currently being used by the State Repository.
     * <p>
     * @return All the keys that this mapping has placed within it.
     */
    public static String[] getStateKeys() {
        return getInstance().getStateKeys();
    }

    /**
     * Permanantly remove the object that is mapped to the specified key from the State Repository. The
     * object removed will be garbage collected when the next gc routine runs in which the object has no
     * more external references.
     * <p>
     * @param key The key that maps to the target object. If <code>key</code> does not exist in the
     *            mapping, nothing will occur.
     */
    public static void removeStateObject(String key) {
        getInstance().removeStateObject(key);
    }

    /**
     * Remove all mappings from the State Repository. This typically should be done once all information
     * within is no longer pertinent.
     */
    public static void clearStateObjects() {
        getInstance().clearStateObjects();
    }
}
