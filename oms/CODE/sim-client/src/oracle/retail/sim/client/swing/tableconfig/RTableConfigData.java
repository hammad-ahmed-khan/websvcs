package oracle.retail.sim.client.swing.tableconfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * This class contains all the data of a table configuration. It is serializable for simple saving and
 * data transfer reasons.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RTableConfigData implements Serializable {
    private static final long serialVersionUID = 8495350391471285900L;

    private String identifier = StringConstants.EMPTY;
    private int fontSizeSetting = RTableConfigConstants.STANDARD;
    private int gridlineSetting = RTableConfigConstants.COL_GRIDLINES;

    private List<RTableConfigColumnData> columnDataList = new ArrayList<>();

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
     * Assigns the size content to the table configuration data.
     * <p>
     * @param setting The size content.
     ***************************************************************************************************/
    public void setFontSizeSetting(int setting) {
        if (setting < RTableConfigConstants.SMALLEST || setting > RTableConfigConstants.LARGEST) {
            throw new IllegalArgumentException("Invalid size content for table configuration.");
        }
        fontSizeSetting = setting;
    }

    /****************************************************************************************************
     * Retrieves the size content of the table configuration data.
     * <p>
     * @return The size content.
     ***************************************************************************************************/
    public int getFontSizeSetting() {
        return fontSizeSetting;
    }

    /****************************************************************************************************
     * Assigns the gridline setting to the table configuration data. Valid options (found in
     * RtableSimConfigFiles) include NO_GRIDLINES, COL_GRIDLINES, ROW_GRIDLINES, ALL_GRIDLINES.
     * <p>
     * @param setting The setting.
     ***************************************************************************************************/
    public void setGridlineSetting(int setting) {
        if (setting < RTableConfigConstants.NO_GRIDLINES || setting > RTableConfigConstants.ALL_GRIDLINES) {
            throw new IllegalArgumentException("Invalid gridline setting for table configuration.");
        }
        gridlineSetting = setting;
    }

    /****************************************************************************************************
     * Retrieves the gridline setting of the table configuration data.
     * <p>
     * @return The gridline setting.
     ***************************************************************************************************/
    public int getGridlinesSetting() {
        return gridlineSetting;
    }

    /****************************************************************************************************
     * Retrieves the column configuration information for the table.
     * <p>
     * @return A List of RTableConfigColumnData objects.
     ***************************************************************************************************/
    public List<RTableConfigColumnData> getColumnConfigData() {
        return columnDataList;
    }

    /****************************************************************************************************
     * Assigns the column configuration information for the table.
     * <p>
     * @param list A List of RTableConfigColumnData objects.
     ***************************************************************************************************/
    public void setColumnConfigData(List<RTableConfigColumnData> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        columnDataList = list;
    }

    /****************************************************************************************************
     * Retrieves the column configuration information for only those columns that are morked for sorting.
     * It returns these columns in the sort order sequence.
     * <p>
     * @return A List of RTableConfigColumnData objects.
     ***************************************************************************************************/
    public List<RTableConfigColumnData> getSortColumnList() {
        List<RTableConfigColumnData> sortColumnList = new ArrayList<>();
        for (RTableConfigColumnData data : columnDataList) {
            if (data.isSort()) {
                sortColumnList.add(data);
            }
        }
        Collections.sort(sortColumnList, new RTableConfigSortSeqComparator());
        return sortColumnList;
    }

    /****************************************************************************************************
     * This method takes the sort list in sequence and resets the sort order number starting at 1.
     ***************************************************************************************************/
    public void resetSortOrder() {
        int index = 1;
        for (RTableConfigColumnData data : getSortColumnList()) {
            data.setSortOrder(index++);
        }
    }

    /****************************************************************************************************
     * Retrieves the next sort order for the sort sequence.
     ***************************************************************************************************/
    public int getNextSortOrder() {
        int sortOrder = 1;
        for (RTableConfigColumnData data : columnDataList) {
            if (data.isSort()) {
                sortOrder++;
            }
        }
        return sortOrder;
    }
}
