package oracle.retail.sim.client.swing.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import oracle.retail.sim.client.swing.util.Repository;

/******************************************************************************************
 * In memory location to store all filters. New code needs to be added in the future to
 * persist the FilterRepository and retrieve it from memory.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class FilterRepository extends Repository implements Serializable {
    private static final long serialVersionUID = 3228686835571159358L;

    private static Repository repository = new Repository();

    /******************************************************************************************
     * Adds a new filter to the repository.
     * <p>
     * @param element The filter to add.
     ******************************************************************************************/
    public static void addFilter(FilterElement element) {
        repository.put(element.getIdentifier(), element);
    }

    /******************************************************************************************
     * Retrieves a filter from the repository.
     * <p>
     * @param identifier The filter identifier.
     * <p>
     * @return The filter element.
     ******************************************************************************************/
    public static FilterElement getFilter(String identifier) {
        return (FilterElement) repository.get(identifier);
    }

    /******************************************************************************************
     * Removes a filter from the repository.
     * <p>
     * @param identifier The filter identifier.
     ******************************************************************************************/
    public static void removeFilter(String identifier) {
        repository.remove(identifier);
    }

    /******************************************************************************************
     * Removes a filter from the repository.
     * <p>
     * @param element The filter to remove.
     ******************************************************************************************/
    public static void removeFilter(FilterElement element) {
        repository.remove(element.getIdentifier());
    }

    /******************************************************************************************
     * Adds a new filter group to the repository.
     * <p>
     * @param element The filter group to add.
     ******************************************************************************************/
    public static void addFilterGroup(FilterGroup group) {
        repository.put(group.getIdentifier(), group);
    }

    /******************************************************************************************
     * Retrieves a filter group from the repository.
     * <p>
     * @param identifier The filter identifier.
     * <p>
     * @return The filter group.
     ******************************************************************************************/
    public static FilterGroup getFilterGroup(String identifier) {
        return (FilterGroup) repository.get(identifier);
    }

    /******************************************************************************************
     * Removes a filter group from the repository.
     * <p>
     * @param identifier The filter group identifier.
     ******************************************************************************************/
    public static void removeFilterGroup(String identifier) {
        repository.remove(identifier);
    }

    /******************************************************************************************
     * Removes a filter group from the repository.
     * <p>
     * @param group The filter group to remove.
     ******************************************************************************************/
    public static void removeFilterGroup(FilterGroup group) {
        repository.remove(group.getIdentifier());
    }

    /******************************************************************************************
     * Retrieves all filters from the repository.
     * <p>
     * @return All filters in the repository.
     ******************************************************************************************/
    public static Collection getAllFilters() {
        return repository.getAllValues();
    }

    /******************************************************************************************
     * Retrieves all available (ie. selectable) filters from the repository.
     * <p>
     * @return All available filters.
     ******************************************************************************************/
    public static List getAvailableFilters() {
        List filterList = new ArrayList();
        for (Object object : repository.getAllValues()) {
            if (object instanceof FilterGroup || object instanceof FilterElement && ((FilterElement) object).isAvailable()) {
                filterList.add(object);
            }
        }
        return filterList;
    }
}
