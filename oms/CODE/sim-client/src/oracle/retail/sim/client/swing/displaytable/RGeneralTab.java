package oracle.retail.sim.client.swing.displaytable;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonGroup;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.tableconfig.RTableConfigConstants;
import oracle.retail.sim.client.swing.widget.RTab;

/********************************************************************************************************
 * This class is the filter tab portion of table configuration.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

class RGeneralTab extends RTab implements ItemListener {
    private static final long serialVersionUID = -3137455785340006060L;

    private RCheckBoxEditor smallestSizeEditor = new RCheckBoxEditor("Smallest (8pt)");
    private RCheckBoxEditor smallerSizeEditor = new RCheckBoxEditor("Smaller (9pt)");
    private RCheckBoxEditor standardSizeEditor = new RCheckBoxEditor("Standard (10pt)");
    private RCheckBoxEditor largeSizeEditor = new RCheckBoxEditor("Large (11pt)");
    private RCheckBoxEditor largerSizeEditor = new RCheckBoxEditor("Larger (12pt)");
    private RCheckBoxEditor largestSizeEditor = new RCheckBoxEditor("Largest (14pt)");

    private RCheckBoxEditor allLinesEditor = new RCheckBoxEditor("Row & Column Lines");
    private RCheckBoxEditor noLinesEditor = new RCheckBoxEditor("No Lines");
    private RCheckBoxEditor columnLinesEditor = new RCheckBoxEditor("Column Lines Only");
    private RCheckBoxEditor rowLinesEditor = new RCheckBoxEditor("Row Lines Only");

    private REditorPanel sizeContentPanel = new REditorPanel(5);
    private REditorPanel gridLinesPanel = new REditorPanel(4);

    private ButtonGroup sizeContentGroup = new ButtonGroup();
    private ButtonGroup gridLinesGroup = new ButtonGroup();

    private boolean isDataModified;

    /****************************************************************************************************
     * Constructs a new filter tab.
     * <p>
     * @param title The title to assign to the tab.
     ***************************************************************************************************/
    public RGeneralTab(String title) {
        setTitle(title);
        initializeTab();
        layoutTab();
    }

    /****************************************************************************************************
     * Initializes the components of the tab.
     ***************************************************************************************************/
    private void initializeTab() {
        sizeContentPanel.setTitleBorder("Content Font Size");
        gridLinesPanel.setTitleBorder("Gridlines Setting");

        sizeContentGroup.add(smallestSizeEditor.getCheckBox());
        sizeContentGroup.add(smallerSizeEditor.getCheckBox());
        sizeContentGroup.add(standardSizeEditor.getCheckBox());
        sizeContentGroup.add(largeSizeEditor.getCheckBox());
        sizeContentGroup.add(largerSizeEditor.getCheckBox());
        sizeContentGroup.add(largestSizeEditor.getCheckBox());

        smallestSizeEditor.getCheckBox().addItemListener(this);
        smallerSizeEditor.getCheckBox().addItemListener(this);
        standardSizeEditor.getCheckBox().addItemListener(this);
        largeSizeEditor.getCheckBox().addItemListener(this);
        largerSizeEditor.getCheckBox().addItemListener(this);
        largestSizeEditor.getCheckBox().addItemListener(this);

        gridLinesGroup.add(allLinesEditor.getCheckBox());
        gridLinesGroup.add(noLinesEditor.getCheckBox());
        gridLinesGroup.add(columnLinesEditor.getCheckBox());
        gridLinesGroup.add(rowLinesEditor.getCheckBox());

        allLinesEditor.getCheckBox().addItemListener(this);
        noLinesEditor.getCheckBox().addItemListener(this);
        columnLinesEditor.getCheckBox().addItemListener(this);
        rowLinesEditor.getCheckBox().addItemListener(this);
    }

    /****************************************************************************************************
     * Lays out the components of the tab.
     ***************************************************************************************************/
    private void layoutTab() {
        sizeContentPanel.add(smallestSizeEditor);
        sizeContentPanel.add(smallerSizeEditor);
        sizeContentPanel.add(standardSizeEditor);
        sizeContentPanel.add(largeSizeEditor);
        sizeContentPanel.add(largerSizeEditor);
        sizeContentPanel.add(largestSizeEditor);

        gridLinesPanel.add(allLinesEditor);
        gridLinesPanel.add(noLinesEditor);
        gridLinesPanel.add(columnLinesEditor);
        gridLinesPanel.add(rowLinesEditor);

        setLayout(new GridLayout(1, 2));
        add(sizeContentPanel);
        add(gridLinesPanel);
    }

    /****************************************************************************************************
     * Implements the item listener to set the tab modified state to true.
     ***************************************************************************************************/
    public void itemStateChanged(ItemEvent event) {
        isDataModified = true;
    }

    /****************************************************************************************************
     * Initializes the tab to the configuration data.
     ***************************************************************************************************/
    protected void initialize(TableConfigurationData configurationData) {
        setSizeContentData(configurationData.getSizeContent());
        setGridlineData(configurationData.getGridlinesSetting());
        isDataModified = false;
    }

    /****************************************************************************************************
     * Initializes the size content information in the tab.
     ***************************************************************************************************/
    private void setSizeContentData(int sizeContent) {
        switch (sizeContent) {
            case RTableConfigConstants.SMALLEST:
                smallestSizeEditor.setSelected(true);
                break;
            case RTableConfigConstants.SMALLER:
                smallerSizeEditor.setSelected(true);
                break;
            case RTableConfigConstants.LARGE:
                largeSizeEditor.setSelected(true);
                break;
            case RTableConfigConstants.LARGER:
                largerSizeEditor.setSelected(true);
                break;
            case RTableConfigConstants.LARGEST:
                largestSizeEditor.setSelected(true);
                break;
            default:
                standardSizeEditor.setSelected(true);
                break;
        }
    }

    /****************************************************************************************************
     * Initializes the gridline information in the tab.
     ***************************************************************************************************/
    private void setGridlineData(int setting) {
        switch (setting) {
            case RTableConfigConstants.NO_GRIDLINES:
                noLinesEditor.setSelected(true);
                break;
            case RTableConfigConstants.COL_GRIDLINES:
                columnLinesEditor.setSelected(true);
                break;
            case RTableConfigConstants.ROW_GRIDLINES:
                rowLinesEditor.setSelected(true);
                break;
            default:
                allLinesEditor.setSelected(true);
                break;
        }
    }

    /****************************************************************************************************
     * Saves the information to the configuration data.
     ***************************************************************************************************/
    protected void save(TableConfigurationData configurationData) {
        if (isDataModified) {
            if (smallestSizeEditor.isSelected()) {
                configurationData.setSizeContent(RTableConfigConstants.SMALLEST);
            } else if (smallerSizeEditor.isSelected()) {
                configurationData.setSizeContent(RTableConfigConstants.SMALLER);
            } else if (largeSizeEditor.isSelected()) {
                configurationData.setSizeContent(RTableConfigConstants.LARGE);
            } else if (largerSizeEditor.isSelected()) {
                configurationData.setSizeContent(RTableConfigConstants.LARGER);
            } else if (largestSizeEditor.isSelected()) {
                configurationData.setSizeContent(RTableConfigConstants.LARGEST);
            } else {
                configurationData.setSizeContent(RTableConfigConstants.STANDARD);
            }

            if (noLinesEditor.isSelected()) {
                configurationData.setGridlineSetting(RTableConfigConstants.NO_GRIDLINES);
            } else if (columnLinesEditor.isSelected()) {
                configurationData.setGridlineSetting(RTableConfigConstants.COL_GRIDLINES);
            } else if (rowLinesEditor.isSelected()) {
                configurationData.setGridlineSetting(RTableConfigConstants.ROW_GRIDLINES);
            } else {
                configurationData.setGridlineSetting(RTableConfigConstants.ALL_GRIDLINES);
            }
        }
    }
}
