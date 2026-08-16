package oracle.retail.sim.client.swing.filter;

import java.io.Serializable;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.common.core.type.Displayable;

/******************************************************************************************
 * This class represents a single data filter. It contains the filter type and the text to
 * be filtered on.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class FilterElement implements Displayable, Serializable {
    private static final long serialVersionUID = 1123104516096058877L;

    public static final int IDENTIFIER_LENGTH = 100;
    public static final int VALUE_LENGTH = 100;

    private FilterType filterType = FilterType.CONTAINS;
    private String value;
    private String identifier;
    private boolean available = true;

    /******************************************************************************************
     * Constructs a new empty FilterElement.
     ******************************************************************************************/
    public FilterElement() {
    }

    /******************************************************************************************
     * Constructs a new FilterElement with an assigned type.
     * <p>
     * @param filterType The type to assign to the filter.
     ******************************************************************************************/
    public FilterElement(FilterType filterType) {
        setType(filterType);
    }

    /******************************************************************************************
     * Constructs a new FilterElement with an assigned value. The default filter type is
     * FilterType.CONTAINS.
     * <p>
     * @param value The text to filter on.
     ******************************************************************************************/
    public FilterElement(String value) {
        setValue(value);
    }

    /******************************************************************************************
     * Constructs a new FilterElement with an assigned type and value.
     * <p>
     * @param filterType The type to assign to the filter.
     * @param value The text to filter on.
     ******************************************************************************************/
    public FilterElement(FilterType filterType, String value) {
        setType(filterType);
        setValue(value);
    }

    /******************************************************************************************
     * Constructs a new FilterElement with an assigned type and value.
     * <p>
     * @param filterType The type to assign to the filter.
     * @param value The text to filter on.
     * @param identifier An identifier to assign to the filter.
     ******************************************************************************************/
    public FilterElement(FilterType filtertype, String value, String identifier) {
        setType(filterType);
        setValue(value);
        setIdentifier(identifier);
    }

    /******************************************************************************************
     * Assigns an identifier to the filter.
     * <p>
     * @param The identifier to assign (cannot be null or empty).
     ******************************************************************************************/
    public void setIdentifier(String identifier) {
        if (StringUtility.isNullOrEmpty(identifier)) {
            throw new IllegalArgumentException("Filter identifier cannot be empty or null!");
        }
        this.identifier = identifier;
    }

    /******************************************************************************************
     * Retrieves the identifier.
     * <p>
     * @return The identifier.
     ******************************************************************************************/
    public String getIdentifier() {
        if (identifier == null) {
            return filterType.getIdentifier() + value;
        }
        return identifier;
    }

    /******************************************************************************************
     * Constructs a new FilterElement with an assigned type and value.
     * <p>
     * @param filterType The type to assign to the filter.
     * @param value The text to filter on.
     * @param identifier An identifier to assign to the filter.
     * @param available True if element should be available as a single editor.
     ******************************************************************************************/
    public FilterElement(FilterType filtertype, String value, String identifier, boolean available) {
        setType(filterType);
        setValue(value);
        setIdentifier(identifier);
        setAvailable(available);
    }

    /******************************************************************************************
     * Assigns a filter type.
     * <p>
     * @param A filter type to assign (cannot be null).
     ******************************************************************************************/
    public void setType(FilterType filterType) {
        if (filterType == null) {
            throw new IllegalArgumentException("Filter type cannot be null!");
        }
        this.filterType = filterType;
    }

    /******************************************************************************************
     * Retrieves the filter type.
     * <p>
     * @return The filter type.
     ******************************************************************************************/
    public FilterType getType() {
        return filterType;
    }

    /******************************************************************************************
     * Assigns the text to filter on.
     * <p>
     * @param value The text to filter on (cannot be null or empty).
     ******************************************************************************************/
    public void setValue(String value) {
        if (StringUtility.isNullOrEmpty(value)) {
            throw new IllegalArgumentException("Filter value cannot be empty or null!");
        }
        this.value = value;
    }

    /******************************************************************************************
     * Retrieves the text to filter on.
     * <p>
     * @return The text to filter on.
     ******************************************************************************************/
    public String getValue() {
        return value;
    }

    /******************************************************************************************
     * Assigns whether or not the filter element should be available as a filter selection. A
     * filter not available for selection is still available to become part of a filter group.
     * <p>
     * @param available True if the filter should be available as a selection, false otherwise.
     ******************************************************************************************/
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /******************************************************************************************
     * Retrieves whether or not the filter element should be available as a filter selection.
     * <p>
     * @return True if the filter should be available as a selection, false otherwise.
     ******************************************************************************************/
    public boolean isAvailable() {
        return available;
    }

    /******************************************************************************************
     * Retrieves the display string for the object.
     ******************************************************************************************/
    public String toDisplayString() {
        return getIdentifier();
    }
}
