package oracle.retail.sim.client.swing.displaytable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.swing.tableconfig.RTableConfigConstants;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * This class contains all the data of a table configuration. It is serializable for simple saving and
 * data transfer reasons.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TableConfigurationData implements Serializable {
    private static final long serialVersionUID = -784114732144180211L;

    private String identifier = StringConstants.EMPTY;
    private int sizeContent = RTableConfigConstants.STANDARD;
    private int gridlineSetting = RTableConfigConstants.ALL_GRIDLINES;
    private List filterList = new ArrayList();
    private int[] columnDisplayOrderArray = new int[0];
    private int[] hiddenColumnArray = new int[0];

    private boolean generalDataModified;
    private boolean filterDataModified;
    private boolean displayDataModified;
    private boolean sortingDataModified;

    /****************************************************************************************************
     * Constructs new TableConfigurationData object.
     ***************************************************************************************************/
    public TableConfigurationData() {
    }

    /****************************************************************************************************
     * Constructs new TableConfigurationData object with an identifier.
     * <p>
     * @param identifier The identifier.
     ***************************************************************************************************/
    public TableConfigurationData(String identifier) {
        setIdentifier(identifier);
    }

    /****************************************************************************************************
     * Assigns an identifier to the table configuration data. This identifier should be unique. Two
     * tables with the same identifier will use the same configuration data.
     * <p>
     * @param identifier The identifier.
     ***************************************************************************************************/
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /****************************************************************************************************
     * Retrieves the identifier assigned to this table configuration.
     * <p>
     * @return The table configuration data.
     ***************************************************************************************************/
    public String getIdentifier() {
        return identifier;
    }

    /****************************************************************************************************
     * Retrieves whether or not the general data portion of the table configuration is modified.
     * <p>
     * @return True if the data has been altered, false if not.
     ***************************************************************************************************/
    public boolean isGeneralDataModified() {
        return generalDataModified;
    }

    /****************************************************************************************************
     * Retrieves whether or not the filter portion of the table configuration is modified.
     * <p>
     * @return True if the data has been altered, false if not.
     ***************************************************************************************************/
    public boolean isFilterDataModified() {
        return filterDataModified;
    }

    /****************************************************************************************************
     * Retrieves whether or not the display data portion of the table configuration is modified.
     * <p>
     * @return True if the data has been altered, false if not.
     ***************************************************************************************************/
    public boolean isDisplayDataModified() {
        return displayDataModified;
    }

    /****************************************************************************************************
     * Retrieves whether or not the sorting data portion of the table configuration is modified.
     * <p>
     * @return True if the data has been altered, false if not.
     ***************************************************************************************************/
    public boolean isSortingDataModified() {
        return sortingDataModified;
    }

    /****************************************************************************************************
     * Resets the table configuration data to an unmodified state. This is usually called after the table
     * is done updating itself to the configuration information.
     ***************************************************************************************************/
    public void resetModifiedDataState() {
        generalDataModified = false;
        filterDataModified = false;
        displayDataModified = false;
        sortingDataModified = false;
    }

    /****************************************************************************************************
     * Assigns the size content to the table configuration data.
     * <p>
     * @param sizeContent The size content.
     ***************************************************************************************************/
    public void setSizeContent(int sizeContent) {
        if (sizeContent < RTableConfigConstants.SMALLEST || sizeContent > RTableConfigConstants.LARGEST) {
            throw new IllegalArgumentException("Invalid size content for table configuration.");
        }
        this.sizeContent = sizeContent;
        generalDataModified = true;
    }

    /****************************************************************************************************
     * Retrieves the size content of the table configuration data.
     * <p>
     * @return The size content.
     ***************************************************************************************************/
    public int getSizeContent() {
        return sizeContent;
    }

    /****************************************************************************************************
     * Assigns the gridline setting to the table configuration data. Valid options include NO_GRIDLINES,
     * COL_GRIDLINES, ROW_GRIDLINES, ALL_GRIDLINES.
     * <p>
     * @param setting The setting.
     ***************************************************************************************************/
    public void setGridlineSetting(int setting) {
        if (setting < RTableConfigConstants.NO_GRIDLINES || setting > RTableConfigConstants.ALL_GRIDLINES) {
            throw new IllegalArgumentException("Invalid gridline setting for table configuration.");
        }
        gridlineSetting = setting;
        generalDataModified = true;
    }

    /****************************************************************************************************
     * Retrieves the size content of the table configuration data.
     * <p>
     * @return The size content.
     ***************************************************************************************************/
    public int getGridlinesSetting() {
        return gridlineSetting;
    }

    /****************************************************************************************************
     * Sets all the column filters associated with the table.
     * <p>
     * @param filterList The column filters.
     ***************************************************************************************************/
    public void setColumnFilters(List filterList) {
        this.filterList = filterList;
        filterDataModified = true;
    }

    /****************************************************************************************************
     * Sets all the column filters associated with the table.
     * <p>
     * @return The column filters.
     ***************************************************************************************************/
    public List getColumnFilters() {
        return filterList;
    }

    /****************************************************************************************************
     * Assigns the column display order to the table.
     * <p>
     * @param array The column display order (in indexes).
     ***************************************************************************************************/
    public void setColumnDisplayOrder(int[] array) {
        columnDisplayOrderArray = array;
        displayDataModified = true;
    }

    /****************************************************************************************************
     * Retrieves the column display order of the table.
     * <p>
     * @return An array of column indexes in the desired order.
     ***************************************************************************************************/
    public int[] getColumnDisplayOrder() {
        return columnDisplayOrderArray;
    }

    /****************************************************************************************************
     * Assigns the hidden columns to the table.
     * <p>
     * @param array An array of hidden column indexes.
     ***************************************************************************************************/
    public void setHiddenColumns(int[] array) {
        hiddenColumnArray = array;
        displayDataModified = true;
    }

    /****************************************************************************************************
     * Retrieves the hidden columns of the table.
     * <p>
     * @return An array of hidden column indexes.
     ***************************************************************************************************/
    public int[] getHiddenColumns() {
        return hiddenColumnArray;
    }
}
