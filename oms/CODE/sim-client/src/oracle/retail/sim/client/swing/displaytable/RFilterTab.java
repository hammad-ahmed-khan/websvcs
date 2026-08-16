package oracle.retail.sim.client.swing.displaytable;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.filter.ColumnFilterElement;
import oracle.retail.sim.client.swing.filter.FilterElement;
import oracle.retail.sim.client.swing.filter.FilterGroup;
import oracle.retail.sim.client.swing.filter.FilterRepository;
import oracle.retail.sim.client.swing.filter.FilterType;
import oracle.retail.sim.client.swing.filter.RFilterChooserDialog;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RIconButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RTab;

/*************************************************************************************************
 * This class is the filter tab portion of table configuration.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *************************************************************************************************/

public class RFilterTab extends RTab implements REventListener {
    private static final long serialVersionUID = 8879340586945209478L;

    private static final String COLUMN = "Column";
    private static final String FILTER_TYPE = "Filter Type";
    private static final String FILTER_ELEM = "Filter Element";
    private static final String FILTER_VALUE = "Filter Value";
    private static final String REMOVE = "Remove Filter";
    private static final String REMOVE_ALL = "Remove All";
    private static final String APPLY = "Apply Filter";
    private static final String APPLY_SELECTED = "Apply Selected Filter";
    private static final String APPLY_CREATED = "Apply Created Filter";

    private RDisplayTable filterTable = new RDisplayTable("ConfigurationDialog.filterTab");
    private RDisplayTablePane filterPane = new RDisplayTablePane(filterTable);

    private String[] headers = { COLUMN, FILTER_TYPE, FILTER_VALUE };

    private RPanel removePanel = new RPanel();
    private RPanel detailPanel = new RPanel();

    private RLabel columnLabel1 = new RLabel(COLUMN);
    private RLabel columnLabel2 = new RLabel(COLUMN);
    private RLabel filterElementLabel = new RLabel(FILTER_ELEM);
    private RLabel filterTypeLabel = new RLabel(FILTER_TYPE);
    private RLabel filterValueLabel = new RLabel(FILTER_VALUE);

    private RComboBoxEditor columnEditor1 = new RComboBoxEditor();
    private RComboBoxEditor columnEditor2 = new RComboBoxEditor();
    private RComboBoxEditor filterElementEditor = new RComboBoxEditor();
    private RComboBoxEditor filterTypeEditor = new RComboBoxEditor();
    private RTextFieldEditor filterValueEditor = new RTextFieldEditor();

    private RButton removeButton = new RButton(REMOVE);
    private RButton removeAllButton = new RButton(REMOVE_ALL);

    private RButton selectApplyButton = new RButton(APPLY);
    private RButton createApplyButton = new RButton(APPLY);

    private static final String CREATE_FONTS = "CreateFontCommand";
    private RIconButton fontButton = new RIconButton();

    private String[] columnHeaders = new String[0];
    private String[] translatedHeaders = new String[0];

    private boolean isDataModified;

    /*************************************************************************************************
     * Constructs a new filter tab.
     * <p>
     * @param title The title to assign to the tab.
     *************************************************************************************************/
    public RFilterTab(String title) {
        setTitle(title);
        initializeTab();
        layoutTab();
    }

    /*************************************************************************************************
     * Initializes the components of the tab.
     *************************************************************************************************/
    private void initializeTab() {
        detailPanel.setTitleBorder("Create Filters");

        filterValueEditor.setLength(FilterElement.VALUE_LENGTH);

        fontButton.setIcon(UIManager.getIcon(UIThemeName.RDISPLAYTABLE_CONFIG_ICON));
        fontButton.registerAction(this, CREATE_FONTS);
        removeButton.registerAction(this, REMOVE);
        removeAllButton.registerAction(this, REMOVE_ALL);
        selectApplyButton.registerAction(this, APPLY_SELECTED);
        createApplyButton.registerAction(this, APPLY_CREATED);

        filterTable.setColumnHeaders(headers);
    }

    /*************************************************************************************************
     * Lays out the components of the tab.
     *************************************************************************************************/
    private void layoutTab() {
        detailPanel.setLayout(new GridBagLayout());
        detailPanel.add(columnLabel1, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
        detailPanel.add(columnEditor1, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        detailPanel.add(columnLabel2, GridTool.constraints(0, 2, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
        detailPanel.add(columnEditor2, GridTool.constraints(0, 3, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        detailPanel.add(filterElementLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
        detailPanel.add(filterElementEditor, GridTool.constraints(1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        detailPanel.add(filterTypeLabel, GridTool.constraints(1, 2, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
        detailPanel.add(filterTypeEditor, GridTool.constraints(1, 3, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        detailPanel.add(filterValueLabel, GridTool.constraints(2, 2, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
        detailPanel.add(filterValueEditor, GridTool.constraints(2, 3, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        detailPanel.add(selectApplyButton, GridTool.constraints(3, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
        detailPanel.add(createApplyButton, GridTool.constraints(3, 3, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
        detailPanel.add(fontButton, GridTool.constraints(4, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));

        removePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        removePanel.add(removeButton);
        removePanel.add(removeAllButton);

        setLayout(new GridBagLayout());
        add(filterPane, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        add(removePanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 5, 0));
        add(detailPanel, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 1, 5, 0, 0, 0));
    }

    /*************************************************************************************************
     * Initializes the tab to the configuration data.
     *************************************************************************************************/
    protected void initialize(TableConfigurationData configurationData) {
        filterElementEditor.setItems(FilterRepository.getAvailableFilters());
        filterTypeEditor.setItems(FilterType.getAllFilterTypes());
        loadFilterTable(configurationData.getColumnFilters());
        isDataModified = false;
    }

    /*************************************************************************************************
     * Loads the filter table full of ColumnFilterElements. Does it oddly for optimization.
     *************************************************************************************************/
    private void loadFilterTable(List filterList) {
        List rowList = new ArrayList();
        for (Object filterElement : filterList) {
            rowList.add(buildFilterRow((ColumnFilterElement) filterElement));
        }
        filterTable.clearTable();
        for (int i = 0; i < rowList.size(); i++) {
            filterTable.addRow((String[]) rowList.get(i), filterList.get(i));
        }
        filterTable.resort();
    }

    /*************************************************************************************************
     * Assigns the headers to the filter tab.
     * <p>
     * @param cHeaders Original column headers.
     * @param tHeaders Language translated headers.
     *************************************************************************************************/
    public void setHeaders(String[] cHeaders, String[] tHeaders) {
        columnHeaders = cHeaders;
        translatedHeaders = tHeaders;
        columnEditor1.setItems(translatedHeaders);
        columnEditor2.setItems(translatedHeaders);
    }

    /*************************************************************************************************
     * Implements the action listener method to handle the buttons that are clicked on the filter tab.
     *************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        switch (event.getEventCommand()) {
            case CREATE_FONTS:
                displayFilterDialog();
                refreshFilterElements();
                break;
            case REMOVE:
                removeFilter();
                break;
            case REMOVE_ALL:
                removeAllFilters();
                break;
            case APPLY_SELECTED:
                applySelectedFilter();
                break;
            case APPLY_CREATED:
                applyCreatedFilter();
                break;
        }
    }

    /*************************************************************************************************
     * Displays the filter dialog and allows for the creation of more filters.
     *************************************************************************************************/
    private void displayFilterDialog() {
        RFilterChooserDialog dialog = new RFilterChooserDialog(RDisplayTableConfigDialog.getDialog());
        dialog.addREventListener(this);
        dialog.setVisible(true);
    }

    /*************************************************************************************************
     * Refreshes the filter elements.
     *************************************************************************************************/
    private void refreshFilterElements() {
        filterElementEditor.setItems(FilterRepository.getAvailableFilters());
    }

    /*************************************************************************************************
     * Removes a single selected filter.
     *************************************************************************************************/
    private void removeFilter() {
        Object data = filterTable.getSelectedData();
        if (data != null) {
            ColumnFilterElement element = (ColumnFilterElement) data;
            addColumnName(translatedHeaders[element.getColumnIndex()]);
            filterTable.removeSelectedRow();
            isDataModified = true;
        }
    }

    /*************************************************************************************************
     * Removes all filters from the table configuration.
     *************************************************************************************************/
    private void removeAllFilters() {
        int count = filterTable.getRowCount();
        if (count > 0) {
            filterTable.clearTable();
            resetColumnNameEditors();
            isDataModified = true;
        }
    }

    /*************************************************************************************************
     * Applies a selected filter element to the table configuration.
     *************************************************************************************************/
    private void applySelectedFilter() {
        String columnName = (String) columnEditor1.getSelectedItem();
        Object filter = filterElementEditor.getSelectedItem();

        if (columnName == null || filter == null) {
            return;
        }

        FilterGroup filterGroup;
        if (filter instanceof FilterGroup) {
            filterGroup = (FilterGroup) filter;
        } else {
            FilterElement filterElement = (FilterElement) filter;
            filterGroup = createFilterGroup(filterElement.getIdentifier() + "Group", filterElement);
        }

        int index = getIndex(columnName);

        ColumnFilterElement columnFilter = new ColumnFilterElement(columnHeaders[index], index, filterGroup);

        filterTable.addRow(buildFilterRow(columnFilter), columnFilter);
        filterTable.resort();

        removeColumnName(columnName);

        columnEditor1.setEmptySelection();
        filterElementEditor.setEmptySelection();

        isDataModified = true;
    }

    /*************************************************************************************************
     * Applies a newly created filter element to the table configuration.
     *************************************************************************************************/
    private void applyCreatedFilter() {
        String columnName = (String) columnEditor2.getSelectedItem();
        FilterType filterType = (FilterType) filterTypeEditor.getSelectedItem();
        String value = filterValueEditor.getText();

        if (columnName == null || filterType == null || StringUtility.isNullOrEmpty(value)) {
            return;
        }

        String identifier = columnName + "-" + value;
        FilterGroup group = createFilterGroup(identifier, new FilterElement(filterType, value, identifier, false));
        int index = getIndex(columnName);

        ColumnFilterElement columnFilter = new ColumnFilterElement(columnHeaders[index], index, group);

        filterTable.addRow(buildFilterRow(columnFilter), columnFilter);
        filterTable.resort();

        removeColumnName(columnName);

        columnEditor2.setEmptySelection();
        filterTypeEditor.setEmptySelection();
        filterValueEditor.clear();

        isDataModified = true;
    }

    /*************************************************************************************************
     * Builds the display data for a single row in the table.
     *************************************************************************************************/
    private String[] buildFilterRow(ColumnFilterElement columnFilter) {
        FilterGroup filterGroup = columnFilter.getFilterGroup();

        String[] row = new String[3];
        row[0] = translatedHeaders[columnFilter.getColumnIndex()];

        if (filterGroup.size() == 1) {
            FilterElement filterElement = (FilterElement) filterGroup.getFilterElements().iterator().next();
            row[1] = filterElement.getType().getIdentifier();
            row[2] = filterElement.getValue();
        } else {
            row[1] = Translator.getText("Filter Group");
            row[2] = filterGroup.toDisplayString();
        }

        return row;
    }

    /*************************************************************************************************
     * Retrieves the index for the selected column name.
     * <p>
     * @param columnName The column name (translated) selected from the combo box.
     * @return The index of the column name in the stored array.
     *************************************************************************************************/
    private int getIndex(String columnName) {
        for (int i = 0; i < translatedHeaders.length; i++) {
            if (translatedHeaders[i].equals(columnName)) {
                return i;
            }
        }
        throw new IllegalStateException("Filter Configuration: Unable to find table headers for selected value.");
    }

    /*************************************************************************************************
     * Creates a filter group around the singular filter element.
     *************************************************************************************************/
    private FilterGroup createFilterGroup(String identifier, FilterElement element) {
        List filterList = new ArrayList<>();
        filterList.add(element);
        return new FilterGroup(identifier, filterList);
    }

    /*************************************************************************************************
     * Resets the column name editors to have all column names.
     *************************************************************************************************/
    private void resetColumnNameEditors() {
        columnEditor1.setItems(translatedHeaders);
        columnEditor2.setItems(translatedHeaders);
    }

    /*************************************************************************************************
     * Adds a column name back to the available choices.
     *************************************************************************************************/
    private void addColumnName(String columnName) {
        columnEditor1.addItem(columnName);
        columnEditor2.addItem(columnName);
        columnEditor1.sort();
        columnEditor2.sort();
    }

    /*************************************************************************************************
     * Removes a column from the available choices.
     *************************************************************************************************/
    private void removeColumnName(String columnName) {
        columnEditor1.removeItem(columnName);
        columnEditor2.removeItem(columnName);
    }

    /*************************************************************************************************
     * Saves the information to the configuration data.
     *************************************************************************************************/
    protected void save(TableConfigurationData configurationData) {
        if (isDataModified) {
            configurationData.setColumnFilters(filterTable.getAllData());
        }
    }
}
