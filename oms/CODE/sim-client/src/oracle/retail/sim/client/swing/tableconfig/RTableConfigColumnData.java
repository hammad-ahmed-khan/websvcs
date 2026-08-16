package oracle.retail.sim.client.swing.tableconfig;

import java.io.Serializable;

/********************************************************************************************************
 * This configuration information for a single column of a table. This includes the column title, whether
 * or not it is visible, whether or not it is the sorted column and whether the sorting is ascending or
 * descending.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class RTableConfigColumnData implements Serializable {
    private static final long serialVersionUID = -1918900162825159011L;

    private String title;
    private boolean visible = true;
    private boolean sort;
    private boolean ascending = true;
    private int sortOrder = -1;

    public RTableConfigColumnData(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public boolean isSort() {
        return sort;
    }

    public boolean isAscending() {
        return ascending;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("RTableConfigColumnTab[");
        buffer.append(" Title=").append(title);
        buffer.append(" Visible=").append(visible);
        buffer.append(" Sort Order=").append(sortOrder);
        buffer.append(" ]");
        return buffer.toString();
    }
}
