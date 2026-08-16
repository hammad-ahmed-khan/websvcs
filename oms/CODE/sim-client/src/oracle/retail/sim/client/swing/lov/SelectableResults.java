package oracle.retail.sim.client.swing.lov;

import java.util.ArrayList;
import java.util.Collection;

/******************************************************************************************
 * This class represents the results of getSelectableValues() search.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class SelectableResults {

    private int totalPages = 1;
    private int totalValues;
    private Collection dataCollection = new ArrayList<>();

    /******************************************************************************************
     * Creates empty SelectableResults object.
     ******************************************************************************************/
    public SelectableResults() {
    }

    /******************************************************************************************
     * Creates single page SelectableResults.
     ******************************************************************************************/
    public SelectableResults(Collection data) {
        setData(data);
        totalValues = data.size();
    }

    /******************************************************************************************
     * Creates new SelectableResults object.
     * <p>
     * @param data A collection of data objects.
     * @param total The total number of data objects available.
     * @param size The number of data objects allowed on a page.
     ******************************************************************************************/
    public SelectableResults(Collection data, int total, int size) {
        setData(data);
        setTotalValues(total);
        calculateTotalPages(size);
    }

    /******************************************************************************************
     * Retrieves the total number of data pages available.
     * <p>
     * @return the total number of data pages available.
     ******************************************************************************************/
    public int getTotalPages() {
        return totalPages;
    }

    /******************************************************************************************
     * Assigns the total number of pages of data that are available.
     * <p>
     * @param pages The total number of pages available.
     ******************************************************************************************/
    private void calculateTotalPages(int size) {
        if (totalValues < 1) {
            totalPages = 1;
            return;
        }
        if (size < 1) {
            size = 1;
        }
        totalPages = totalValues / size;
        if (Math.IEEEremainder(totalValues, size) != 0) {
            totalPages++;
        }
    }

    /******************************************************************************************
     * Retrieves the total number of values available.
     * <p>
     * @return The total number of values available.
     ******************************************************************************************/
    public int getTotalValues() {
        if (totalPages == 1) {
            return getDataSize();
        }
        return totalValues;
    }

    /******************************************************************************************
     * Assigns the total number of data values that are available.
     * <p>
     * @param values The total number of values available.
     ******************************************************************************************/
    private void setTotalValues(int values) {
        if (values < 0) {
            values = 0;
        }
        totalValues = values;
    }

    /******************************************************************************************
     * Retrieves the data results.
     * <p>
     * @return The data.
     ******************************************************************************************/
    public Collection getData() {
        return dataCollection;
    }

    /******************************************************************************************
     * Assigns the result data.
     * <p>
     * @param data A collection of data objects.
     ******************************************************************************************/
    private void setData(Collection data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        dataCollection = data;
    }

    /******************************************************************************************
     * Retrieves the current number of data values.
     * <p>
     * @return The current number of data values.
     ******************************************************************************************/
    public int getDataSize() {
        return dataCollection.size();
    }
}
