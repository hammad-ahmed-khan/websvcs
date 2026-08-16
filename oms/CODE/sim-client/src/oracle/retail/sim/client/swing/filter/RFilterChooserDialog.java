package oracle.retail.sim.client.swing.filter;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RListEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RCardPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.util.WindowPlacer;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBox;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RRadioButton;
import oracle.retail.sim.client.swing.widget.RTextField;

/******************************************************************************************
 * This filter chooses dialog contains a means to setup and modify filter elements as well
 * as groups of filter elements. This window is then used to select or apply a chosen
 * filter element or filter group to whomever is interested.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class RFilterChooserDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 6802329032415707110L;

    private static final String EDIT_FILTER = "Add/Edit Filter";
    private static final String EDIT_GROUP = "Add/Edit Filter Group";
    private static final String REMOVE = "Remove";
    private static final String SAVE_FILTER = "Save Filter";
    private static final String SAVE_GROUP = "Save Filter Group";
    private static final String APPLY = "Apply";
    private static final String CLOSE = "Close";
    private static final String FILTERS = "Available Filters";

    private static final String EMPTY_CARD = "Empty Card";
    private static final String FILTER_CARD = "Filter Card";
    private static final String GROUP_CARD = "Group Card";

    private RListEditor filterListEditor = new RListEditor(FILTERS);
    private RListEditor groupListEditor = new RListEditor();

    private RPanel radioPanel = new RPanel();
    private RPanel filterPanel = new RPanel();
    private RPanel groupPanel = new RPanel();
    private RPanel emptyPanel = new RPanel();

    private RCardPanel cardPanel = new RCardPanel();

    private RLabel filterNameLabel = new RLabel("Identifier");
    private RLabel filterGroupLabel = new RLabel("Identifier");
    private RCheckBoxEditor availableEditor = new RCheckBoxEditor("Display As Available");
    private RTextField filterNameField = new RTextField();
    private RTextField filterGroupField = new RTextField();
    private RComboBox filterTypeCombo = new RComboBox();
    private RTextField filterValueField = new RTextField();

    private RRadioButton andRadioButton = new RRadioButton("And");
    private RRadioButton orRadioButton = new RRadioButton("Or");
    private ButtonGroup buttonGroup = new ButtonGroup();

    private RButton filterButton = new RButton(EDIT_FILTER);
    private RButton groupButton = new RButton(EDIT_GROUP);
    private RButton removeButton = new RButton(REMOVE);
    private RButton saveFilterButton = new RButton(SAVE_FILTER);
    private RButton saveGroupButton = new RButton(SAVE_GROUP);
    private RButton applyButton = new RButton(APPLY);
    private RButton closeButton = new RButton(CLOSE);

    /******************************************************************************************
     * Creates new filter chooser dialog around a parent dialog.
     * <p>
     * @param dialog The parent dialog.
     ******************************************************************************************/
    public RFilterChooserDialog(JDialog dialog) {
        super(dialog);
        initialize();
    }

    /******************************************************************************************
     * Initializes the basic properties of the filter chooser.
     ******************************************************************************************/
    private void initialize() {
        setTitle("Select Filter");
        setSize(600, 450);
        setStatusBarVisible(false);
        initializeComponents();
        layoutDialog();
        WindowPlacer.centerWindow(this);
    }

    /******************************************************************************************
     * Initializes the basic properties of all the components on the filter chooser.
     ******************************************************************************************/
    private void initializeComponents() {
        filterNameField.setLength(FilterElement.IDENTIFIER_LENGTH);
        filterGroupField.setLength(FilterElement.IDENTIFIER_LENGTH);
        filterValueField.setLength(FilterElement.VALUE_LENGTH);

        availableEditor.setTitleAlignment(EditorConstants.RIGHT);

        filterTypeCombo.setItems(FilterType.getAllFilterTypes());

        filterListEditor.setMinimumWidth(225);
        filterListEditor.setSingleSelectionMode();
        filterListEditor.registerAction(this, FILTERS);

        buttonGroup.add(andRadioButton);
        buttonGroup.add(orRadioButton);

        filterButton.registerAction(this, EDIT_FILTER);
        groupButton.registerAction(this, EDIT_GROUP);
        removeButton.registerAction(this, REMOVE);
        saveFilterButton.registerAction(this, SAVE_FILTER);
        saveGroupButton.registerAction(this, SAVE_GROUP);
        applyButton.registerAction(this, APPLY);
        closeButton.registerAction(this, CLOSE);

        removeButton.setEnabled(false);
        applyButton.setEnabled(false);

        refreshFilterList();
    }

    /******************************************************************************************
     * Handles the layout of the filter chooser dialog.
     ******************************************************************************************/
    private void layoutDialog() {
        addButton(filterButton);
        addButton(groupButton);
        addButton(removeButton);
        addButton(applyButton);
        addButton(closeButton);

        cardPanel.addCard(EMPTY_CARD, emptyPanel);
        cardPanel.addCard(FILTER_CARD, filterPanel);
        cardPanel.addCard(GROUP_CARD, groupPanel);

        filterPanel.setLayout(new GridBagLayout());
        filterPanel.add(filterNameLabel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 2, 0));
        filterPanel.add(filterNameField, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 5, 0));
        filterPanel.add(availableEditor, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 1, 0, 0, 5, 0));
        filterPanel.add(filterTypeCombo, GridTool.constraints(0, 3, 1, 1, 1, 0, 0, 1, 0, 0, 5, 0));
        filterPanel.add(filterValueField, GridTool.constraints(0, 4, 1, 1, 1, 0, 0, 1, 0, 0, 5, 0));
        filterPanel.add(saveFilterButton, GridTool.constraints(0, 5, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));

        radioPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        radioPanel.add(andRadioButton);
        radioPanel.add(orRadioButton);

        groupPanel.setLayout(new GridBagLayout());
        groupPanel.add(filterGroupLabel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 2, 0));
        groupPanel.add(filterGroupField, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 2, 15));
        groupPanel.add(radioPanel, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 1, 0, 0, 2, 0));
        groupPanel.add(groupListEditor, GridTool.constraints(0, 3, 1, 1, 1, 1, 0, 3, 0, 0, 2, 0));
        groupPanel.add(saveGroupButton, GridTool.constraints(0, 4, 1, 1, 0, 0, 0, 0, 0, 0, 2, 0));

        RPanel panel = (RPanel) getContentPane();

        panel.setLayout(new GridBagLayout());
        panel.add(filterListEditor, GridTool.constraints(0, 0, 1, 2, 0, 1, 0, 2, 0, 0, 0, 0));
        panel.add(cardPanel, GridTool.constraints(1, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
    }

    /******************************************************************************************
     * Implements the RActionEventListener method to delegate to the correct method.
     ******************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        switch (event.getEventCommand()) {
            case CLOSE:
                closeWindow();
                break;
            case SAVE_FILTER:
                saveFilterElement();
                break;
            case SAVE_GROUP:
                saveFilterGroup();
                break;
            case EDIT_FILTER:
                editFilterElement();
                break;
            case EDIT_GROUP:
                editFilterGroup();
                break;
            case REMOVE:
                removeFilterOrGroup();
                break;
            case APPLY:
                applyFilter();
                break;
            case FILTERS:
                doFilterSelected();
                break;
        }
    }

    /******************************************************************************************
     * Controls the state of the apply/remove buttons when a filter is selected.
     ******************************************************************************************/
    private void doFilterSelected() {
        if (filterListEditor.getSelectedValue() != null) {
            removeButton.setEnabled(true);
            applyButton.setEnabled(true);
        } else {
            cardPanel.showCard(EMPTY_CARD);
            removeButton.setEnabled(false);
            applyButton.setEnabled(false);
        }
    }

    /******************************************************************************************
     * Allows for the editing or adding of a filter element. It switches the card panel to
     * filter element mode and displays the filter data if any exists.
     ******************************************************************************************/
    private void editFilterElement() {
        cardPanel.showCard(FILTER_CARD);

        Object[] values = filterListEditor.getSelectedValues();
        if (values.length == 1 && values[0] instanceof FilterElement) {
            FilterElement element = (FilterElement) values[0];

            filterNameField.setText(element.getIdentifier());
            filterNameField.setEnabled(false);
            availableEditor.setSelected(element.isAvailable());
            filterTypeCombo.setSelectedItem(element.getType());
            filterValueField.setText(element.getValue());
            filterTypeCombo.requestFocusInWindow();
        } else {
            filterNameField.setEnabled(true);
            filterNameField.requestFocusInWindow();
        }
    }

    /******************************************************************************************
     * Allows for the editing or adding of a filter groups. It switches the card panel to
     * filter group mode and displays the filter group data if any exists.
     ******************************************************************************************/
    private void editFilterGroup() {
        cardPanel.showCard(GROUP_CARD);

        List elementList = new ArrayList();
        for (Object object : FilterRepository.getAllFilters()) {
            if (object instanceof FilterElement) {
                elementList.add(object);
            }
        }
        groupListEditor.setItems(elementList);

        Object[] values = filterListEditor.getSelectedValues();

        if (values.length == 1 && values[0] instanceof FilterGroup) {
            displayFilterGroup((FilterGroup) values[0]);
        } else {
            andRadioButton.setSelected(true);
            filterGroupField.setEnabled(true);
            filterGroupField.requestFocusInWindow();
        }
    }

    /******************************************************************************************
     * Helper method to display a filter group in its edit panel.
     ******************************************************************************************/
    private void displayFilterGroup(FilterGroup group) {
        filterGroupField.setText(group.getIdentifier());
        filterGroupField.setEnabled(false);

        if (group.isAndGroup()) {
            andRadioButton.setSelected(true);
        } else {
            orRadioButton.setSelected(false);
        }
        groupListEditor.setSelectedValues(group.getFilterElements());

        andRadioButton.requestFocusInWindow();
    }

    /******************************************************************************************
     * Removes a filter from the list of available filters.
     ******************************************************************************************/
    private void removeFilterOrGroup() {
        Object[] values = filterListEditor.getSelectedValues();
        for (Object value : values) {
            if (value instanceof FilterElement) {
                FilterRepository.removeFilter((FilterElement) value);
            } else {
                FilterRepository.removeFilterGroup((FilterGroup) value);
            }
        }
        refreshFilterList();
    }

    /******************************************************************************************
     * Saves a filter element. This will actually create a new filter element every time. In
     * the case of an edited filter, the name cannot be altered to the new filter element will
     * replace its counterpart in the filter repository.
     ******************************************************************************************/
    private void saveFilterElement() {
        FilterType filterType = (FilterType) filterTypeCombo.getSelectedItem();
        String filterValue = filterValueField.getText();
        String identifier = filterNameField.getText();
        boolean availableFilter = availableEditor.isSelected();

        if (filterType == null) {
            return;
        }
        if (StringUtility.isNullOrEmpty(filterValue)) {
            return;
        }
        if (StringUtility.isNullOrEmpty(identifier)) {
            return;
        }

        FilterRepository.addFilter(new FilterElement(filterType, filterValue, identifier, availableFilter));

        refreshFilterList();

        filterTypeCombo.setEmptySelection();
        filterValueField.clear();
        filterNameField.clear();

        cardPanel.showCard(EMPTY_CARD);
    }

    /******************************************************************************************
     * Saves a filter group. This will actually create a new filter group every time. In
     * the case of an edited filter group, the name cannot be altered to the new filter group
     * will replace its counterpart in the filter repository.
     ******************************************************************************************/
    private void saveFilterGroup() {
        Object[] filterArray = groupListEditor.getSelectedValues();
        String identifier = filterGroupField.getText();

        if (StringUtility.isNullOrEmpty(identifier)) {
            return;
        }
        if (filterArray.length == 0) {
            return;
        }

        FilterGroup filterGroup = new FilterGroup(identifier, Arrays.asList(filterArray), andRadioButton.isSelected());
        FilterRepository.addFilterGroup(filterGroup);

        refreshFilterList();

        filterGroupField.clear();
        groupListEditor.removeItems();

        cardPanel.showCard(EMPTY_CARD);
    }

    /******************************************************************************************
     * Refreshes the filter list.
     ******************************************************************************************/
    private void refreshFilterList() {
        filterListEditor.setItems(FilterRepository.getAvailableFilters());
    }

    /******************************************************************************************
     * This method will attempt to apply a filter to a parent window by sending a filter
     * applied notification.
     ******************************************************************************************/
    private void applyFilter() {
        Object selectedFilter = filterListEditor.getSelectedValue();
        notifyREventListeners(new RActionEvent(this, UIPropertyName.FILTER_SELECTED, selectedFilter));
        closeWindow();
    }
}
