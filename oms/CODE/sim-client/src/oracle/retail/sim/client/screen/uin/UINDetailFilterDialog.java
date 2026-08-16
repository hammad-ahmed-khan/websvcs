package oracle.retail.sim.client.screen.uin;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.uin.UINAvailability;
import oracle.retail.sim.common.uin.UINDetailLookupQueryFilter;
import oracle.retail.sim.common.uin.UINStatus;

/********************************************************************************************************
 * This dialog handles entering the filter information for returns.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UINDetailFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -234712440362551461L;

    private UINDetailFilterDialogModel model = new UINDetailFilterDialogModel();

    private RTextFieldEditor serialNumberEditor = new RTextFieldEditor("UIN");
    private RComboBoxEditor availabilityEditor = new RComboBoxEditor("Availability");
    private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");
    private RComboBoxEditor functionalAreaEditor = new RComboBoxEditor("Functional Area");
    private RTextFieldEditor functionalIdEditor = new RTextFieldEditor("Functional Area Id");
    private RCheckBoxEditor damagedOnlyEditor = new RCheckBoxEditor("Damaged Only");
    private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Display Limit");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public UINDetailFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("UIN Detail Filter");
        setSize(450, 300);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        serialNumberEditor.setIdentifier(SimName.SERIAL_NUMBER);

        availabilityEditor.setActionsEnabled(false);
        availabilityEditor.setDisplayer(new TranslatedObjectDisplayer());
        availabilityEditor.setItems(UINAvailability.getList());
        availabilityEditor.setEmptyType(RComboBoxEmptyType.ALL);
        availabilityEditor.setActionsEnabled(true);

        statusEditor.setActionsEnabled(false);
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        statusEditor.setEmptyType(RComboBoxEmptyType.ALL);
        statusEditor.setItems(UINStatus.values());
        statusEditor.removeItem(UINStatus.UNCONFIRMED);
        statusEditor.setActionsEnabled(true);

        functionalAreaEditor.setActionsEnabled(false);
        functionalAreaEditor.setDisplayer(new TranslatedObjectDisplayer());
        functionalAreaEditor.setEmptyType(RComboBoxEmptyType.ALL);
        functionalAreaEditor.setItems(FunctionalArea.values());
        functionalAreaEditor.setActionsEnabled(true);

        functionalIdEditor.setIdentifier(SimName.FUNCTIONAL_AREA_ID);

        searchLimitEditor.setLength(3);
        searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
        searchLimitEditor.setMinimumValue(1);
        searchLimitEditor.setSizeType(EditorConstants.SMALL);

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        REditorPanel editorPanel = new REditorPanel(7);
        editorPanel.setLineBorder(1);
        editorPanel.add(serialNumberEditor);
        editorPanel.add(availabilityEditor);
        editorPanel.add(statusEditor);
        editorPanel.add(functionalAreaEditor);
        editorPanel.add(functionalIdEditor);
        editorPanel.add(damagedOnlyEditor);
        editorPanel.add(searchLimitEditor);

        setContentPane(editorPanel);
    }

    /****************************************************************************************************
     * Assign Filter To Dialog
     ***************************************************************************************************/

    public void setFilter(UINDetailLookupQueryFilter filter) throws Exception {
        model.setFilter(filter);

        serialNumberEditor.setText(filter.getSerialNumber());
        statusEditor.setSelectedItem(filter.getStatus());
        availabilityEditor.setSelectedItem(filter.getAvailibility());
        functionalAreaEditor.setSelectedItem(filter.getFunctionalArea());
        functionalIdEditor.setText(filter.getFunctionalAreaId());
        damagedOnlyEditor.setSelected(filter.isDamagedOnly());
        searchLimitEditor.setInteger(filter.getSearchLimit());

        setDefaultButton(applyButton);
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_RESET)) {
                doReset();
            } else if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Reset Action
     ***************************************************************************************************/
    private void doReset() throws Exception {
        setFilter(model.resetFilter());
    }

    /****************************************************************************************************
     * Apply Action
     ***************************************************************************************************/
    private void doApply() throws Exception {
        UINDetailLookupQueryFilter filter = model.getFilter();

        filter.setSerialNumber(serialNumberEditor.getTextOrNull());
        filter.setAvailability((UINAvailability) availabilityEditor.getSelectedItem());
        filter.setStatus((UINStatus) statusEditor.getSelectedItem());
        filter.setFunctionalArea((FunctionalArea) functionalAreaEditor.getSelectedItem());
        filter.setFunctionalAreaId(functionalIdEditor.getTextOrNull());
        filter.setDamagedOnly(damagedOnlyEditor.isSelected());
        filter.setSearchLimit(searchLimitEditor.getIntegerValue());

        RepositoryManager.addStateObject(SimClientStateKey.ITEM_UIN_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.ITEM_UIN_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
