package oracle.retail.sim.client.screen.mps;

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
import oracle.retail.sim.client.swing.editor.RNumericIdEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.integration.SimMessageDirection;
import oracle.retail.sim.common.integration.SimMessageFamily;
import oracle.retail.sim.common.mps.MpsStagedMessageQueryFilter;

/********************************************************************************************************
 * This dialog handles entering the filter information for staged messages.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class MpsStagedMessageFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 6614954530317633306L;

    private MpsStagedMessageFilterDialogModel model = new MpsStagedMessageFilterDialogModel();

    private RNumericIdEditor messageIdEditor = new RNumericIdEditor("Record Id", "Message");
    private RComboBoxEditor messageFamilyEditor = new RComboBoxEditor("Family");
    private RComboBoxEditor messageDirectionEditor = new RComboBoxEditor("In/Out");
    private RCheckBoxEditor messageShowPendingEditor = new RCheckBoxEditor("Show Pending");
    private RCheckBoxEditor messageShowRetryEditor = new RCheckBoxEditor("Show Retry");
    private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public MpsStagedMessageFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("MPS Staged Message Filter");
        setSize(425, 235);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        messageIdEditor.setIdentifier(SimName.STAGER_MESSAGE_ID);

        messageFamilyEditor.setEmptyType(RComboBoxEmptyType.ALL);
        try {
            messageFamilyEditor.setItems(model.getSimMessageFamilyValues());
        } catch (Exception e) {
            messageFamilyEditor.clear();
            displayException(e);
        }

        messageDirectionEditor.setDisplayer(new TranslatedObjectDisplayer());
        messageDirectionEditor.setItems(SimMessageDirection.values());
        messageDirectionEditor.setEmptyType(RComboBoxEmptyType.ALL);

        messageShowPendingEditor.setEnabled(true, true);
        messageShowRetryEditor.setEnabled(true, true);

        searchLimitEditor.setIdentifier(SimName.STAGER_SEARCH_LIMIT);
        searchLimitEditor.setMinimumValue(1);
        searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
        searchLimitEditor.setInteger(model.getDefaultSearchLimit());
        searchLimitEditor.setSizeType(EditorConstants.SMALL);

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        REditorPanel mainPanel = new REditorPanel(6, 1);
        mainPanel.add(messageIdEditor);
        mainPanel.add(messageFamilyEditor);
        mainPanel.add(messageDirectionEditor);
        mainPanel.add(messageShowPendingEditor);
        mainPanel.add(messageShowRetryEditor);
        mainPanel.add(searchLimitEditor);

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Assign Filter To Dialog
     ***************************************************************************************************/

    public void setFilter(MpsStagedMessageQueryFilter filter) {
        model.setFilter(filter);
        setActionsEnabled(false);
        messageIdEditor.setLong(filter.getMessageId());
        messageFamilyEditor.setSelectedItem(filter.getMessageFamily());
        messageDirectionEditor.setSelectedItem(filter.isInbound() != null ? SimMessageDirection.toValue(filter.isInbound()) : null);
        messageShowPendingEditor.setSelected(filter.isShowPending());
        messageShowRetryEditor.setSelected(filter.isShowRetry());
        searchLimitEditor.setInteger(filter.getSearchLimit());
        setActionsEnabled(true);
        setDefaultButton(applyButton);
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        try {
            switch (event.getEventCommand()) {
                case SimNavigation.DIALOG_RESET:
                    doReset();
                    break;
                case SimNavigation.DIALOG_APPLY:
                    doApply();
                    break;
                case SimNavigation.DIALOG_CANCEL:
                    doCancel();
                    break;
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    /****************************************************************************************************
     * Reset Action
     ***************************************************************************************************/
    private void doReset() {
        setFilter(model.resetFilter());
    }

    /****************************************************************************************************
     * Apply Action
     ***************************************************************************************************/
    private void doApply() throws Exception {
        MpsStagedMessageQueryFilter filter = model.getFilter();
        filter.setMessageId(messageIdEditor.getLongOrNull());
        filter.setMessageFamily((SimMessageFamily) messageFamilyEditor.getSelectedItem());
        SimMessageDirection messageDirection = (SimMessageDirection) messageDirectionEditor.getSelectedItem();
        filter.setInbound(messageDirection != null ? messageDirection.isInbound() : null);
        filter.setShowPending(messageShowPendingEditor.isSelected());
        filter.setShowRetry(messageShowRetryEditor.isSelected());
        filter.setSearchLimit(searchLimitEditor.getIntegerValue());

        RepositoryManager.addStateObject(SimClientStateKey.STAGED_MESSAGE_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.STAGED_MESSAGE_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
