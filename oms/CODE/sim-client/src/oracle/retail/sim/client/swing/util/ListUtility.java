package oracle.retail.sim.client.swing.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/******************************************************************************************
 * Helps convert arrays into list.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class ListUtility {

    /******************************************************************************************
     * Converts an array of objects into a list. If array is not null, uses Arrays.asList();
     * <p>
     * @param array Array of objects.
     * @return A list of the objects, or an empty list if the array is null.
     *****************************************************************************************/
    public static List<Object> asList(Object[] array) {
        if (array == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(array));
    }
}
