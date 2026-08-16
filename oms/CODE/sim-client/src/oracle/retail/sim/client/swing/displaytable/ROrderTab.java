package oracle.retail.sim.client.swing.displaytable;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.UIManager;
import oracle.retail.sim.client.swing.panel.RListTransferPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RTab;

/*************************************************************************************************
 * This class is the column disply and ordering portion of the table configuration.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *************************************************************************************************/

class ROrderTab extends RTab implements PropertyChangeListener {
    private static final long serialVersionUID = -8493438929139421002L;

    private RListTransferPanel transferPanel = new RListTransferPanel();
    private String[] translatedHeaders = new String[0];
    private boolean isDataModified;

    /*************************************************************************************************
     * Constructs a new filter tab.
     * <p>
     * @param title The title to assign to the tab.
     *************************************************************************************************/
    public ROrderTab(String title) {
        setTitle(title);
        initializeTab();
        layoutTab();
    }

    /*************************************************************************************************
     * Initializes the components of the tab.
     *************************************************************************************************/
    private void initializeTab() {
        transferPanel.setTitle("Show These Columns:", "Hide These Columns:");
        transferPanel.setTitleFont(UIManager.getFont(UIThemeName.THEME_BOLD_FONT));
        transferPanel.setSelectableAutoSort(false);
        transferPanel.setSelectableAllowsSwap(true);
        transferPanel.setIncludeAllOptions(false);
        transferPanel.addPropertyChangeListener(this);
        transferPanel.addSelectableChangeListener(this);
    }

    /*************************************************************************************************
     * Lays out the components of the tab.
     *************************************************************************************************/
    private void layoutTab() {
        setLayout(new BorderLayout());
        add(transferPanel, BorderLayout.CENTER);
    }

    /*************************************************************************************************
     * Assigns the headers to the filter tab.
     * <p>
     * @param tHeaders Language translated headers.
     *************************************************************************************************/
    protected void setHeaders(String[] headers) {
        translatedHeaders = headers;
    }

    /*************************************************************************************************
     * Initializes the tab to the configuration data.
     *************************************************************************************************/
    protected void initialize(TableConfigurationData configurationData) {
        String[] headers = buildHeaders(configurationData.getColumnDisplayOrder());
        if (headers.length == 0) {
            transferPanel.setSelectableItems(translatedHeaders);
            isDataModified = true;
        } else {
            transferPanel.setSelectableItems(buildHeaders(configurationData.getColumnDisplayOrder()));
            transferPanel.setSelectedItems(buildHeaders(configurationData.getHiddenColumns()));
            isDataModified = false;
        }
    }

    /*************************************************************************************************
     * Implements property change listnener to listen to changes on the transfer panel.
     *************************************************************************************************/
    public void propertyChange(PropertyChangeEvent event) {
        String name = event.getPropertyName();

        if (name.equals(UIPropertyName.LIST_TRANSFER_OCCURRED)) {
            isDataModified = true;
        } else if (name.equals(UIPropertyName.LIST_VALUE_POSITION)) {
            isDataModified = true;
        }
    }

    /*************************************************************************************************
     * Saves the information to the configuration data.
     *************************************************************************************************/
    protected void save(TableConfigurationData configurationData) {
        if (isDataModified) {
            configurationData.setColumnDisplayOrder(convertHeaders(transferPanel.getRemainingSelectableItems()));
            configurationData.setHiddenColumns(convertHeaders(transferPanel.getSelectedItems()));
        }
    }

    /*************************************************************************************************
     * Converts a list of headers into an array of indexes.
     *************************************************************************************************/
    private int[] convertHeaders(List tempHeaders) {
        return convertHeaders(tempHeaders.toArray());
    }

    /*************************************************************************************************
     * Converts an object array of headers into an array of indexes.
     *************************************************************************************************/
    private int[] convertHeaders(Object[] headers) {
        int[] indexArray = new int[headers.length];
        int index = 0;
        for (Object header : headers) {
            for (int innerIndex = 0; innerIndex < translatedHeaders.length; innerIndex++) {
                if (header.equals(translatedHeaders[innerIndex])) {
                    indexArray[index] = innerIndex;
                    index++;
                }
            }
        }
        return indexArray;
    }

    /*************************************************************************************************
     * Converts an array of indexes into an array of headers.
     *************************************************************************************************/
    private String[] buildHeaders(int[] headerIndexArray) {
        String[] headers = new String[headerIndexArray.length];
        for (int i = 0; i < headerIndexArray.length; i++) {
            headers[i] = translatedHeaders[headerIndexArray[i]];
        }
        return headers;
    }
}
