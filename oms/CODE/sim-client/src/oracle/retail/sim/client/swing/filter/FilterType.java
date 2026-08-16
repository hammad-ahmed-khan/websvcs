package oracle.retail.sim.client.swing.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.locale.Translator;

/******************************************************************************************
 * Filter Type Enum holds all the available types of filters.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

//TODO: replace with enum!
public class FilterType implements Serializable {
    private static final long serialVersionUID = 8821779095860262281L;

    public static final FilterType EQUAL = new FilterType("is equal to");
    public static final FilterType NOT_EQUAL = new FilterType("is not equal to");
    public static final FilterType GREATER = new FilterType("is greater than");
    public static final FilterType GREATER_EQUAL = new FilterType("is greater than/equal to");
    public static final FilterType LESS = new FilterType("is less than");
    public static final FilterType LESS_EQUAL = new FilterType("is less than/equal to");
    public static final FilterType BEGIN = new FilterType("begins with");
    public static final FilterType NOT_BEGIN = new FilterType("does not begin with");
    public static final FilterType END = new FilterType("ends with");
    public static final FilterType NOT_END = new FilterType("does not end with");
    public static final FilterType CONTAINS = new FilterType("contains");
    public static final FilterType NOT_CONTAINS = new FilterType("does not contain");

    private final String identifier;

    /******************************************************************************************
     * Private constructor makes it so that only this class can define filter types.
     * <p>
     * @param identifier The identifier of the filter type.
     ******************************************************************************************/
    private FilterType(String identifier) {
        this.identifier = identifier;
    }

    /******************************************************************************************
     * Retrieves the identifier of the filter type.
     ******************************************************************************************/
    public String getIdentifier() {
        return identifier;
    }

    /******************************************************************************************
     * Defines the toString() to be the translated label.
     ******************************************************************************************/
    public String toString() {
        return Translator.getText(identifier);
    }

    /******************************************************************************************
     * Retrieves all filter types in the order they should be displayed.
     ******************************************************************************************/
    public static List<FilterType> getAllFilterTypes() {
        List<FilterType> filterTypeList = new ArrayList<>();
        filterTypeList.add(EQUAL);
        filterTypeList.add(NOT_EQUAL);
        filterTypeList.add(GREATER);
        filterTypeList.add(GREATER_EQUAL);
        filterTypeList.add(LESS);
        filterTypeList.add(LESS_EQUAL);
        filterTypeList.add(BEGIN);
        filterTypeList.add(NOT_BEGIN);
        filterTypeList.add(END);
        filterTypeList.add(NOT_END);
        filterTypeList.add(CONTAINS);
        filterTypeList.add(NOT_CONTAINS);
        return filterTypeList;
    }
}
