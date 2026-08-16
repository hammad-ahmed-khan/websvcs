package oracle.retail.sim.client.screen.invadjustment;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
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
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldRangeUtility;
import oracle.retail.sim.client.swing.editor.RNumericIdEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateQueryFilter;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateStatus;
import oracle.retail.sim.common.item.ItemVO;

/********************************************************************************************************
 * This dialog handles entering the filter information for inventory templates.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryTemplateFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -1838016496550579556L;

    private InventoryTemplateFilterDialogModel model = new InventoryTemplateFilterDialogModel();

    private RDateFieldEditor fromDateEditor = new RDateFieldEditor("From Date");
    private RDateFieldEditor toDateEditor = new RDateFieldEditor("To Date");

    private RSearchFieldEditor itemEditor = SimEditorFactory.createItemVOSearchFieldEditor(false);
    private RNumericIdEditor templateEditor = new RNumericIdEditor("Template ID", "Template");
    private RTextFieldEditor descriptionEditor = new RTextFieldEditor("Description");
    private RComboBoxEditor reasonEditor = new RComboBoxEditor("Reason");
    private RComboBoxEditor userEditor = new RComboBoxEditor("User");
    private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public InventoryTemplateFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Template Filter");
        setSize(450, 330);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        templateEditor.setIdentifier(SimName.INVENTORY_ADJUSTMENT_TEMPLATE_ID);
        templateEditor.setEntryAlignmentLeft();

        descriptionEditor.setIdentifier(SimName.INVENTORY_ADJUSTMENT_TEMPLATE_DESCRIPTION);

        reasonEditor.setDisplayer(new TranslatedObjectDisplayer());
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());

        fromDateEditor.setSizeType(EditorConstants.LARGE);
        toDateEditor.setSizeType(EditorConstants.LARGE);
        reasonEditor.setSizeType(EditorConstants.LARGE);
        userEditor.setSizeType(EditorConstants.LARGE);
        statusEditor.setSizeType(EditorConstants.LARGE);

        reasonEditor.setEmptyType(RComboBoxEmptyType.ALL);
        userEditor.setEmptyType(RComboBoxEmptyType.ALL);
        statusEditor.setEmptyType(RComboBoxEmptyType.ALL);

        itemEditor.setSearchListener(buildItemSearchListener());

        RDateFieldRangeUtility.setDateRangeEditors(fromDateEditor, toDateEditor);

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        REditorPanel dateFilterPanel = new REditorPanel(2);
        dateFilterPanel.setTitleBorder("Date Filters");
        dateFilterPanel.add(fromDateEditor);
        dateFilterPanel.add(toDateEditor);

        REditorPanel miscFilterPanel = new REditorPanel(6);
        miscFilterPanel.setTitleBorder("Additional Filters");
        miscFilterPanel.add(itemEditor);
        miscFilterPanel.add(templateEditor);
        miscFilterPanel.add(descriptionEditor);
        miscFilterPanel.add(reasonEditor);
        miscFilterPanel.add(userEditor);
        miscFilterPanel.add(statusEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(dateFilterPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(miscFilterPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(new JLabel(), GridTool.constraints(0, 2, 2, 1, 0, 1, 0, 3, 0, 0, 0, 0));

        LayoutUtility.alignPanels(dateFilterPanel, miscFilterPanel);

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Assign Filter To Dialog
     ***************************************************************************************************/

    public void setFilter(InventoryAdjustmentTemplateQueryFilter filter) throws Exception {
        model.setFilter(filter);

        reasonEditor.setItems(model.getInventoryAdjustmentReasons());
        userEditor.setItems(model.findTemplateUsernames());
        statusEditor.setItems(InventoryAdjustmentTemplateStatus.values());

        fromDateEditor.setDate(filter.getFromDate());
        toDateEditor.setDate(filter.getToDate());
        if (filter.getItemId() != null) {
            itemEditor.setText(filter.getItemId());
        } else {
            itemEditor.clear();
        }
        templateEditor.setLong(filter.getTemplateId());
        descriptionEditor.setText(filter.getDescription());
        userEditor.setSelectedItem(filter.getUsername());
        statusEditor.setSelectedItem(filter.getStatus());
        reasonEditor.setSelectedItem(model.getInventoryAdjustmentReason(filter.getReasonId()));

        setDefaultButton(applyButton);
    }

    /****************************************************************************************************
     * Item Search Listener - pops open the item lookup dialog
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
     * Use Action
     ***************************************************************************************************/
    private void doApply() throws Exception {
        InventoryAdjustmentTemplateQueryFilter filter = model.getFilter();

        filter.setDateRange(fromDateEditor.getDateAtStartOfDay(), toDateEditor.getDateAtEndOfDay());
        filter.setTemplateId(templateEditor.getLongOrNull());
        filter.setDescription(descriptionEditor.getTextOrNull());
        filter.setStatus((InventoryAdjustmentTemplateStatus) statusEditor.getSelectedItem());
        filter.setUsername((String) userEditor.getSelectedItem());

        InventoryAdjustmentReason reason = (InventoryAdjustmentReason) reasonEditor.getSelectedItem();
        if (reason != null) {
            filter.setReasonId(reason.getId());
        } else {
            filter.setReasonId(null);
        }

        ItemVO item = (ItemVO) itemEditor.getData();
        if (item != null) {
            filter.setItemId(item.getId());
        } else {
            filter.setItemId(null);
        }

        RepositoryManager.addStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_TEMPLATE_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.INVENTORY_ADJUSTMENT_TEMPLATE_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
