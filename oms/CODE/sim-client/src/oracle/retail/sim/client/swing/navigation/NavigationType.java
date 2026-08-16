package oracle.retail.sim.client.swing.navigation;

/******************************************************************************************
 * Enumeration of navigation types. The default type is a tree structure where the silly
 * type is a one-layer task/task item breakdown.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class NavigationType {
    public static final NavigationType TREE = new NavigationType("T", "Tree");
    public static final NavigationType LIST = new NavigationType("L", "List");

    private String id;
    private String description;

    /******************************************************************************************
     * Private constructor makes this an enum.
     ******************************************************************************************/
    private NavigationType(String id, String description) {
        this.id = id;
        this.description = description;
    }

    /******************************************************************************************
     * Returns the unique identifier of the Navigation Type
     ******************************************************************************************/
    public String getId() {
        return id;
    }

    /******************************************************************************************
     * Returns the description of the Navigation Type
     ******************************************************************************************/
    public String getDescription() {
        return description;
    }
}
