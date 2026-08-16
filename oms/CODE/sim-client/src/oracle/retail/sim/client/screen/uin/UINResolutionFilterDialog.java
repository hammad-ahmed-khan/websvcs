package oracle.retail.sim.client.screen.uin;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateRangeEditor;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.uin.UINProblemDetailQueryFilter;
import oracle.retail.sim.common.uin.UINStatus;

/********************************************************************************************************
 * This dialog handles entering the filter information for UIN Resolution List Screen. This filter is
 * used to find UIN Problem Detail objects.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UINResolutionFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -2163414942375044542L;

    private UINResolutionFilterDialogModel model = new UINResolutionFilterDialogModel();

    private RSearchFieldEditor itemEditor = SimEditorFactory.createItemVOSearchFieldEditor(false);
    private RTextFieldEditor uinEditor = new RTextFieldEditor("UIN");
    private RComboBoxEditor statusEditor = new RComboBoxEditor("Existing Status");
    private RDateRangeEditor dateRangeEditor = new RDateRangeEditor("Create Date");
    private RComboBoxEditor resolvedEditor = new RComboBoxEditor("Resolved");
    private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public UINResolutionFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("UIN Resolution Filter");
        setSize(450, 270);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        statusEditor.setItems(UINStatus.values());
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());

        itemEditor.setSearchListener(buildItemSearchListener());
        uinEditor.setIdentifier(SimName.SERIAL_NUMBER);

        searchLimitEditor.setIdentifier(SimName.UIN_RESOLUTION_SEARCH_LIMIT);
        searchLimitEditor.setSizeType(EditorConstants.SMALL);
        searchLimitEditor.setMinimumValue(1);
        searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);

        resolvedEditor.setDisplayer(new UINResolutionBooleanDisplayer());
        resolvedEditor.setEmptyType(RComboBoxEmptyType.ALL);
        resolvedEditor.addItem(Boolean.TRUE);
        resolvedEditor.addItem(Boolean.FALSE);

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        REditorPanel editorPanel = new REditorPanel(7);
        editorPanel.add(itemEditor);
        editorPanel.add(uinEditor);
        editorPanel.add(statusEditor);
        editorPanel.add(dateRangeEditor);
        editorPanel.add(resolvedEditor);
        editorPanel.add(searchLimitEditor);

        setContentPane(editorPanel);
    }

    /****************************************************************************************************
     * Assign Filter To Dialog
     ***************************************************************************************************/

    public void setFilter(UINProblemDetailQueryFilter filter) throws Exception {
        model.setFilter(filter);
        itemEditor.setText(filter.getItemId());
        uinEditor.setText(filter.getUinValue());
        statusEditor.setSelectedItem(filter.getStatus());
        dateRangeEditor.setStartDate(filter.getFromDate());
        dateRangeEditor.setEndDate(filter.getToDate());
        resolvedEditor.setSelectedItem(filter.getResolved());
        searchLimitEditor.setInteger(filter.getSearchLimit());

        setDefaultButton(applyButton);
    }

    public UINProblemDetailQueryFilter getFilter() throws BusinessException {
        return model.getFilter();
    }

    /****************************************************************************************************
     * Item Search Listener - Pops open the item lookup dialog
     ***************************************************************************************************/

    private ItemSearchListener buildItemSearchListener() {
        return new ItemSearchListener() {
            public void assignItem(ItemVO itemVO) {
                if (itemVO != null) {
                    itemEditor.setData(itemVO);
                }
            }
        };
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
        UINProblemDetailQueryFilter filter = model.getFilter();
        filter.setItemId(itemEditor.getText());
        filter.setUinValue(uinEditor.getText());
        UINStatus status = (UINStatus) statusEditor.getSelectedItem();
        if (status != null) {
            filter.setStatus(status);
        }
        filter.setDateRange(dateRangeEditor.getStartDate(), dateRangeEditor.getEndDate());
        filter.setResolved((Boolean) resolvedEditor.getSelectedItem());
        filter.setSearchLimit(searchLimitEditor.getIntegerValue());
        RepositoryManager.addStateObject(SimClientStateKey.UIN_ATTRIBUTE_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.UIN_ATTRIBUTE_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
