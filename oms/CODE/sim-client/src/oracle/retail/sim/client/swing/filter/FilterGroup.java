package oracle.retail.sim.client.swing.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.Displayable;

/******************************************************************************************
 * Filter Group represents a collection (ordered list) of filter elements with an identifier
 * and an AND/OR property.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class FilterGroup implements Displayable, Serializable {
    private static final long serialVersionUID = 4840813409010151192L;

    public static final boolean AND = true;
    public static final boolean OR = false;

    private String identifier = StringConstants.EMPTY;
    private List filterElementList = new ArrayList();
    private boolean groupType = AND;

    /******************************************************************************************
     * Constructs a new filter group.
     * <p>
     * @param identifier An identifier to assign to the group.
     ******************************************************************************************/
    public FilterGroup(String identifier) {
        setIdentifier(identifier);
    }

    /******************************************************************************************
     * Constructs a new filter group.
     * <p>
     * @param identifier An identifier to assign to the group.
     * @param elementList A list of filter elements to assign to the group.
     ******************************************************************************************/
    public FilterGroup(String identifier, List elementList) {
        setIdentifier(identifier);
        setFilterElements(elementList);
    }

    /******************************************************************************************
     * Constructs a new filter group.
     * <p>
     * @param identifier An identifier to assign to the group.
     * @param elementList A list of filter elements to assign to the group.
     * @param groupType Either the AND value or the OR value.
     ******************************************************************************************/
    public FilterGroup(String identifier, List elementList, boolean groupType) {
        setIdentifier(identifier);
        setFilterElements(elementList);
    }

    /******************************************************************************************
     * Assigns an identifier to the filter group.
     * <p>
     * @param identifier The identifier to assign.
     ******************************************************************************************/
    public void setIdentifier(String identifier) {
        if (StringUtility.isNullOrEmpty(identifier)) {
            throw new IllegalArgumentException("Filter Group identifier cannot be null or empty!");
        }
        this.identifier = identifier;
    }

    /******************************************************************************************
     * Retrieves the identifier for the filter group.
     * <p>
     * @return The identifier.
     ******************************************************************************************/
    public String getIdentifier() {
        return identifier;
    }

    /******************************************************************************************
     * Sets the entire list of filter elements in the filter group. These elements are ordered.
     * <p>
     * @param elementList The element list to assign.
     ******************************************************************************************/
    public void setFilterElements(List elementList) {
        if (elementList == null) {
            elementList = new ArrayList();
        }
        for (Object element : elementList) {
            if (!(element instanceof FilterElement)) {
                throw new IllegalArgumentException("Filter Group element list must contain FilterElement objects!");
            }
        }
        filterElementList = elementList;
    }

    /******************************************************************************************
     * Retrieves the entire list of filter elements in the filter group.
     * <p>
     * @return The filter elements.
     ******************************************************************************************/
    public List getFilterElements() {
        return filterElementList;
    }

    /******************************************************************************************
     * Adds a filter element to the end of the filter element list.
     * <p>
     * @param element The element to add.
     ******************************************************************************************/
    public void addFilterElement(FilterElement element) {
        filterElementList.add(element);
    }

    /******************************************************************************************
     * Removes a filter element from the filter element list.
     * <p>
     * @param element The element to remove.
     ******************************************************************************************/
    public void removeFilterElement(FilterElement element) {
        filterElementList.remove(element);
    }

    /******************************************************************************************
     * Assigns a group type to the filter group. True is equivalent to FilterGroup.AND, false
     * is equivalent to FilterGroup.OR.
     * <p>
     * @param groupType Either FilterGroup.AND or FilterGroup.OR
     ******************************************************************************************/
    public void setGroupType(boolean groupType) {
        this.groupType = groupType;
    }

    /******************************************************************************************
     * Returns true if the filter group has the AND modifier, false if it has the OR modifier.
     * <p>
     * @return True if is an AND filter group, false otherwise.
     ******************************************************************************************/
    public boolean isAndGroup() {
        return groupType;
    }

    /******************************************************************************************
     * Returns true if the filter group has the OR modifier, false if it has the AND modifier.
     * <p>
     * @return True if is an OR filter group, false otherwise.
     ******************************************************************************************/
    public boolean isOrGroup() {
        return !groupType;
    }

    /******************************************************************************************
     * Retrieves the number of filter elements within the group.
     * <p>
     * @return The number of filter elements.
     ******************************************************************************************/
    public int size() {
        return filterElementList.size();
    }

    /******************************************************************************************
     * Retrieves the display string for the object.
     ******************************************************************************************/
    public String toDisplayString() {
        return getIdentifier();
    }

    /******************************************************************************************
     * Overrides the toString() for better display.
     ******************************************************************************************/
    public String toString() {
        StringBuilder buffer = new StringBuilder("FilterGroup [");
        buffer.append("Identifier = ").append(identifier);
        buffer.append("; AND (Group) = ").append(groupType);
        buffer.append("; Number of Elements = ").append(size());
        return buffer.toString();
    }
}
