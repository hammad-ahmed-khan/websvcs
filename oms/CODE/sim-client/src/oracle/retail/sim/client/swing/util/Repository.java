package oracle.retail.sim.client.swing.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/********************************************************************************************************
 * This class is basically just an HashMap wrapper for storing objects. It is generally used for state
 * management. It is a static class that cannot be constructed. All the methods are static for
 * convenience.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class Repository implements Serializable {
    private static final long serialVersionUID = 8936400915664737443L;

    private final Map<String, Object> objectMap = new HashMap<>();

    /****************************************************************************************************
     * Adds an object to the repository to make it available for global access. Only one object may
     * exists per name. New objects added with the same name replace the old objects.
     * <p>
     * @param name The name to assign to the object in the repository.
     * @param object The object to place in the repository.
     ***************************************************************************************************/
    public void put(String name, Object object) {
        objectMap.put(name, object);
    }

    /****************************************************************************************************
     * Returns an object in the repository for a given name.
     * <p>
     * @param name The name of the object to retrieve from the repository.
     * @return The object in the repository for the given name, or null if none exists.
     ***************************************************************************************************/
    public Object get(String name) {
        return objectMap.get(name);
    }

    /****************************************************************************************************
     * Returns a list from the repository for a given name.
     * <p>
     * @param name The name of the list to retrieve from the repository.
     * @return The list or an empty list if none is found.
     ***************************************************************************************************/
    public List getList(String name) {
        List list = (List) objectMap.get(name);
        if (list != null) {
            return list;
        }
        return new ArrayList();
    }

    /****************************************************************************************************
     * Returns a list from the repository for a given name.
     * <p>
     * @param name The name of the list to retrieve from the repository.
     * @return The list or an empty list if none is found.
     ***************************************************************************************************/
    public Map getMap(String name) {
        Map map = (Map) objectMap.get(name);
        if (map != null) {
            return map;
        }
        return new HashMap();
    }

    /****************************************************************************************************
     * Removes an object from the Repository for a given name.
     * <p>
     * @param name The name of the object to remove from the repository.
     ***************************************************************************************************/
    public void remove(String name) {
        objectMap.remove(name);
    }

    /****************************************************************************************************
     * Retrieves all keys from the repository.
     * <p>
     * @return All keys in the repository.
     ***************************************************************************************************/
    public Set<String> getAllKeys() {
        return objectMap.keySet();
    }

    /****************************************************************************************************
     * Retrieves all values in the repository.
     * <p>
     * @return All values in the repository.
     ***************************************************************************************************/
    public Collection<Object> getAllValues() {
        return objectMap.values();
    }

    /****************************************************************************************************
     * Clear The Repository
     ***************************************************************************************************/
    public void clearRepository() {
        objectMap.clear();
    }

    /****************************************************************************************************
     * Compares an object with its key name to what is currently in the repository and returns whether or
     * not it is equal to the one already there.
     * <p>
     * @param object The object to compare.
     * @param name The name of the object in the Repository.
     ***************************************************************************************************/
    public boolean isEquals(Object object, String name) {
        Object currentObject = get(name);
        if (currentObject == null && object == null) {
            return true;
        }
        return object != null && object.equals(currentObject);
    }
}
