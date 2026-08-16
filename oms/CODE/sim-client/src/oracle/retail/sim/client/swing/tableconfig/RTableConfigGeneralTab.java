package oracle.retail.sim.client.swing.tableconfig;

import java.awt.GridLayout;
import javax.swing.ButtonGroup;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.widget.RTab;

/********************************************************************************************************
 * This class is the general tab portion of table configuration. It contains widgets that control the
 * font size and grid lines of the table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

class RTableConfigGeneralTab extends RTab {
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

    private REditorPanel sizeContentPanel = new REditorPanel(6);
    private REditorPanel gridLinesPanel = new REditorPanel(4);

    private ButtonGroup sizeContentGroup = new ButtonGroup();
    private ButtonGroup gridLinesGroup = new ButtonGroup();

    private String identifier;

    /****************************************************************************************************
     * Constructs a new general tab.
     * <p>
     * @param title The title to assign to the tab.
     ***************************************************************************************************/
    public RTableConfigGeneralTab(String title) {
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

        gridLinesGroup.add(allLinesEditor.getCheckBox());
        gridLinesGroup.add(noLinesEditor.getCheckBox());
        gridLinesGroup.add(columnLinesEditor.getCheckBox());
        gridLinesGroup.add(rowLinesEditor.getCheckBox());
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
     * Initializes the tab to the configuration data. It loads the table configuration data from the
     * repository and sets the value of the editors.
     * @param identifer The table identifier.
     ***************************************************************************************************/
    protected void loadConfigSettings(String identifier) {
        RTableConfigData data = RTableConfigRepository.getTableConfigurationData(identifier);
        setSizeContentData(data.getFontSizeSetting());
        setGridlineData(data.getGridlinesSetting());
        this.identifier = identifier;
    }

    /****************************************************************************************************
     * Assigns the size content information to the editors.
     * @param sizeContent The size content setting.
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
     * Assigns the gridline information to the editors.
     * @param setting The gridline setting.
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
     * Saves the information back to the configuration data and stores it in the repository.
     ***************************************************************************************************/
    protected void saveConfigSettings() {
        RTableConfigData data = RTableConfigRepository.getTableConfigurationData(identifier);

        if (smallestSizeEditor.isSelected()) {
            data.setFontSizeSetting(RTableConfigConstants.SMALLEST);
        } else if (smallerSizeEditor.isSelected()) {
            data.setFontSizeSetting(RTableConfigConstants.SMALLER);
        } else if (largeSizeEditor.isSelected()) {
            data.setFontSizeSetting(RTableConfigConstants.LARGE);
        } else if (largerSizeEditor.isSelected()) {
            data.setFontSizeSetting(RTableConfigConstants.LARGER);
        } else if (largestSizeEditor.isSelected()) {
            data.setFontSizeSetting(RTableConfigConstants.LARGEST);
        } else {
            data.setFontSizeSetting(RTableConfigConstants.STANDARD);
        }

        if (noLinesEditor.isSelected()) {
            data.setGridlineSetting(RTableConfigConstants.NO_GRIDLINES);
        } else if (columnLinesEditor.isSelected()) {
            data.setGridlineSetting(RTableConfigConstants.COL_GRIDLINES);
        } else if (rowLinesEditor.isSelected()) {
            data.setGridlineSetting(RTableConfigConstants.ROW_GRIDLINES);
        } else {
            data.setGridlineSetting(RTableConfigConstants.ALL_GRIDLINES);
        }
    }
}
