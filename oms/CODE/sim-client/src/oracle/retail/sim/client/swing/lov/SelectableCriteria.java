package oracle.retail.sim.client.swing.lov;

import java.io.Serializable;
import oracle.retail.sim.client.swing.filter.TableFilterElement;
import oracle.retail.sim.client.swing.filter.TableSortElement;

/******************************************************************************************
 * This class represents the criteria necessary to inform a list model to find
 * selectableValues().
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class SelectableCriteria implements Serializable {
    private static final long serialVersionUID = -6172804865623806245L;

    private int page = 1;
    private int pageSize = -1;
    private int pageThreshold = Integer.MAX_VALUE;

    private TableSortElement tableSortElement;
    private TableFilterElement tableFilterElement;

    /******************************************************************************************
     * Constructs new empty selectable criteria.
     ******************************************************************************************/
    public SelectableCriteria() {
    }

    /******************************************************************************************
     * Constructs new empty selectable criteria.
     * <p>
     * @param page The page.
     * @param pageSize The number of values per page.
     * @param threshold The threshold at which paging begins.
     ******************************************************************************************/
    public SelectableCriteria(int page, int pageSize, int threshold) {
        setPage(page);
        setPageSize(pageSize);
        setPageThreshold(threshold);
    }

    /******************************************************************************************
     * Assigns the page.
     * <p>
     * @param page The page.
     ******************************************************************************************/
    public void setPage(int page) {
        this.page = page;
    }

    /******************************************************************************************
     * Retrieves the page.
     * <p>
     * @return The page.
     ******************************************************************************************/
    public int getPage() {
        return page;
    }

    /******************************************************************************************
     * Assigns the number of values allowed per page.
     * <p>
     * @param pageSize The number of values allowed per page.
     ******************************************************************************************/
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /******************************************************************************************
     * Retrieves the page size.
     * <p>
     * @return The page size.
     ******************************************************************************************/
    public int getPageSize() {
        return pageSize;
    }

    /******************************************************************************************
     * Assigns the number of values at which paging begins.
     * <p>
     * @param threshold The number of values at which paging begins.
     ******************************************************************************************/
    public void setPageThreshold(int threshold) {
        pageThreshold = threshold;
    }

    /******************************************************************************************
     * Retrieves the number of values at which paging begins.
     * <p>
     * @return The number of values at which paging begins.
     ******************************************************************************************/
    public int getPageThreshold() {
        return pageThreshold;
    }

    /******************************************************************************************
     * Assigns a TableSortElement to the SelectableCriteria.
     * <p>
     * @param element A TableSortElement.
     ******************************************************************************************/
    public void setTableSortElement(TableSortElement element) {
        tableSortElement = element;
    }

    /******************************************************************************************
     * Retrieves the table sort element of the SelectableCriteria.
     * <p>
     * @return The TableSortElement.
     ******************************************************************************************/
    public TableSortElement getTableSortElement() {
        return tableSortElement;
    }

    /******************************************************************************************
     * Assigns a TableFilterElement to the SelectableCriteria.
     * <p>
     * @param element A TableFilterElement.
     ******************************************************************************************/
    public void setTableFilterElement(TableFilterElement element) {
        tableFilterElement = element;
    }

    /******************************************************************************************
     * Retrieves the table filter element of the SelectableCriteria.
     * <p>
     * @return The TableFilterElement.
     ******************************************************************************************/
    public TableFilterElement getTableFilterElement() {
        return tableFilterElement;
    }
}
