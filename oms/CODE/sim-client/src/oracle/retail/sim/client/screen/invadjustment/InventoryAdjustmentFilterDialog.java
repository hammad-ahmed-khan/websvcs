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
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RNumericIdEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentQueryFilter;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReasonComparator;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentStatus;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateVO;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.NonSellableQtyType;

/********************************************************************************************************
 * This dialog handles entering the filter information for inventory adjustments.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryAdjustmentFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -1531541908109400068L;

    private InventoryAdjustmentFilterDialogModel model = new InventoryAdjustmentFilterDialogModel();

    private RDateFieldEditor fromDateEditor = new RDateFieldEditor("From Date");
    private RDateFieldEditor toDateEditor = new RDateFieldEditor("To Date");

    private RSearchFieldEditor itemEditor = SimEditorFactory.createItemVOSearchFieldEditor(false);
    private RComboBoxEditor nonSellableTypeEditor = new RComboBoxEditor("Sub-bucket");
    private RComboBoxEditor reasonEditor = new RComboBoxEditor("Reason");
    private RComboBoxEditor userEditor = new RComboBoxEditor("User");
    private RNumericIdEditor adjustmentEditor = new RNumericIdEditor("Adjustment ID", "Adjustment");
    private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");
    private RComboBoxEditor templateEditor = new RComboBoxEditor("Template");
    private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public InventoryAdjustmentFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Inventory Adjustment Filter");
        initContent();
        layoutContent();
        validateNonSellableTypes();
        centerOnOwner();
    }

    private void initContent() {
        adjustmentEditor.setIdentifier(SimName.INVENTORY_ADJUSTMENT_NUMBER);
        adjustmentEditor.setEntryAlignmentLeft();

        reasonEditor.setDisplayer(new TranslatedObjectDisplayer());
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());

        searchLimitEditor.setIdentifier(SimName.INVENTORY_ADJUSTMENT_SEARCH_LIMIT);
        searchLimitEditor.setMinimumValue(1);
        searchLimitEditor.setMaximumValue(999);

        fromDateEditor.setSizeType(EditorConstants.LARGE);
        toDateEditor.setSizeType(EditorConstants.LARGE);
        nonSellableTypeEditor.setSizeType(EditorConstants.LARGE);
        reasonEditor.setSizeType(EditorConstants.LARGE);
        userEditor.setSizeType(EditorConstants.LARGE);
        adjustmentEditor.setSizeType(EditorConstants.MEDIUM);
        statusEditor.setSizeType(EditorConstants.LARGE);
        templateEditor.setSizeType(EditorConstants.LARGE);
        searchLimitEditor.setSizeType(EditorConstants.SMALL);

        nonSellableTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        reasonEditor.setEmptyType(RComboBoxEmptyType.ALL);
        userEditor.setEmptyType(RComboBoxEmptyType.ALL);
        statusEditor.setEmptyType(RComboBoxEmptyType.ALL);
        templateEditor.setEmptyType(RComboBoxEmptyType.ALL);

        reasonEditor.setComparator(new InventoryAdjustmentReasonComparator());

        itemEditor.setSearchListener(buildItemSearchListener());

        RDateFieldRangeUtility.setDateRangeEditors(fromDateEditor, toDateEditor);

        applyButton.addMouseFocusGrabber();

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

        REditorPanel miscFilterPanel = new REditorPanel(8);
        miscFilterPanel.setTitleBorder("Additional Filters");
        miscFilterPanel.add(itemEditor);
        miscFilterPanel.add(nonSellableTypeEditor);
        miscFilterPanel.add(reasonEditor);
        miscFilterPanel.add(userEditor);
        miscFilterPanel.add(adjustmentEditor);
        miscFilterPanel.add(statusEditor);
        miscFilterPanel.add(templateEditor);
        miscFilterPanel.add(searchLimitEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(dateFilterPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(miscFilterPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(new JLabel(), GridTool.constraints(0, 2, 2, 1, 0, 1, 0, 3, 0, 0, 0, 0));

        LayoutUtility.alignPanels(dateFilterPanel, miscFilterPanel);

        setContentPane(mainPanel);
    }

    private void validateNonSellableTypes() {
        if (model.isNonSellableTypesActive()) {
            nonSellableTypeEditor.setVisible(true);
            setSize(450, 385);
        } else {
            nonSellableTypeEditor.setVisible(false);
            setSize(450, 360);
        }
    }

    /****************************************************************************************************
     * Assign Filter To Dialog
     ***************************************************************************************************/

    public void setFilter(InventoryAdjustmentQueryFilter filter) throws Exception {
        model.setFilter(filter);

        userEditor.setItems(model.getUsernameList());
        statusEditor.setItems(model.getStatusList());
        reasonEditor.setItems(model.getReasonList());
        templateEditor.setItems(model.getTemplateList());

        fromDateEditor.setDate(filter.getFromDate());
        toDateEditor.setDate(filter.getToDate());
        if (filter.getItemId() != null) {
            itemEditor.setData(model.loadItem());
        } else {
            itemEditor.clear();
        }

        userEditor.setSelectedItem(filter.getUsername());
        reasonEditor.setSelectedItem(model.getInventoryAdjustmentReason(filter.getReasonId()));
        adjustmentEditor.setLong(filter.getInventoryAdjustmentId());
        statusEditor.setSelectedItem(filter.getStatus());
        templateEditor.setSelectedItem(model.getTemplateVO(filter.getTemplateId()));
        searchLimitEditor.setInteger(filter.getSearchLimit());

        if (model.isNonSellableTypesActive()) {
            nonSellableTypeEditor.setItems(model.getNonSellableTypeList());
            nonSellableTypeEditor.setSelectedItem(model.getNonSellableQtyType(filter.getNonSellableTypeId()));
        }

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
     * Apply Action
     ***************************************************************************************************/
    private void doApply() throws Exception {
        InventoryAdjustmentQueryFilter filter = model.getFilter();

        filter.setDateRange(fromDateEditor.getDateAtStartOfDay(), toDateEditor.getDateAtEndOfDay());
        filter.setUsername((String) userEditor.getSelectedItem());
        filter.setInventoryAdjustmentId(adjustmentEditor.getLongOrNull());
        filter.setStatus((InventoryAdjustmentStatus) statusEditor.getSelectedItem());
        filter.setSearchLimit(searchLimitEditor.getIntegerValue());

        ItemVO item = (ItemVO) itemEditor.getData();
        if (item != null) {
            filter.setItemId(item.getId());
        } else {
            filter.setItemId(null);
        }

        if (model.isNonSellableTypesActive()) {
            NonSellableQtyType nonSellableType = (NonSellableQtyType) nonSellableTypeEditor.getSelectedItem();
            if (nonSellableType != null) {
                filter.setNonSellableTypeId(nonSellableType.getId());
                filter.doSetNonSellableTypeDescription(nonSellableType.getDescription());
            } else {
                filter.setNonSellableTypeId(null);
                filter.doSetNonSellableTypeDescription(null);
            }
        }

        InventoryAdjustmentReason reason = (InventoryAdjustmentReason) reasonEditor.getSelectedItem();
        if (reason != null) {
            filter.setReasonId(reason.getId());
            filter.doSetReasonDescription(reason.getDescription());
        } else {
            filter.setReasonId(null);
            filter.doSetReasonDescription(null);
        }

        InventoryAdjustmentTemplateVO templateVO = (InventoryAdjustmentTemplateVO) templateEditor.getSelectedItem();
        if (templateVO != null) {
            filter.setTemplateId(templateVO.getId());
            filter.doSetTemplateDescription(templateVO.getDescription());
        } else {
            filter.setTemplateId(null);
            filter.doSetTemplateDescription(null);
        }

        if (item != null) {
            RepositoryManager.addStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_FILTER_ITEM_VO, item);
        } else {
            RepositoryManager.removeStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_FILTER_ITEM_VO);
        }

        RepositoryManager.addStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.INVENTORY_ADJUSTMENT_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
