package oracle.retail.sim.client.screen.shelfreplenishment;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldRangeUtility;
import oracle.retail.sim.client.swing.editor.RNumericIdEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RMatrixPanel;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.productgroup.ProductGroupVO;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentQueryFilter;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentStatus;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentType;

/********************************************************************************************************
 * This dialog handles entering the filter information for Shelf Replenishment lists.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ShelfReplenishmentFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -2163414942375044542L;

    private ShelfReplenishmentFilterDialogModel model = new ShelfReplenishmentFilterDialogModel();

    private RDateFieldEditor startDateEditor = new RDateFieldEditor("From Date");
    private RDateFieldEditor finalDateEditor = new RDateFieldEditor("To Date");
    private RNumericIdEditor shelfReplenishmentEditor = new RNumericIdEditor("Shelf Replenishment ID", "Shelf Replenishment");
    private RComboBoxEditor productGroupEditor = new RComboBoxEditor("Product Group");
    private RSearchFieldEditor itemEditor = SimEditorFactory.createItemVOSearchFieldEditor(false);
    private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");
    private RComboBoxEditor typeEditor = new RComboBoxEditor("Type");
    private RComboBoxEditor userEditor = new RComboBoxEditor("User");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public ShelfReplenishmentFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Shelf Replenishment List Filter");
        setSize(450, 350);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        startDateEditor.setSizeType(EditorConstants.LARGE);
        finalDateEditor.setSizeType(EditorConstants.LARGE);
        shelfReplenishmentEditor.setIdentifier(SimName.SHELF_REPLENISHMENT_ID);
        productGroupEditor.setDisplayer(new AttributeDisplayer("description"));
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        typeEditor.setDisplayer(new TranslatedObjectDisplayer());
        productGroupEditor.setEmptyType(RComboBoxEmptyType.ALL);
        typeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        statusEditor.setEmptyType(RComboBoxEmptyType.ALL);
        userEditor.setEmptyType(RComboBoxEmptyType.ALL);
        itemEditor.setSearchListener(buildItemSearchListener());

        RDateFieldRangeUtility.setDateRangeEditors(startDateEditor, finalDateEditor);

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
        dateFilterPanel.add(startDateEditor);
        dateFilterPanel.add(finalDateEditor);

        REditorPanel miscFilterPanel = new REditorPanel(6);
        miscFilterPanel.setTitleBorder("Additional Filters");
        miscFilterPanel.add(shelfReplenishmentEditor);
        miscFilterPanel.add(productGroupEditor);
        miscFilterPanel.add(itemEditor);
        miscFilterPanel.add(statusEditor);
        miscFilterPanel.add(typeEditor);
        miscFilterPanel.add(userEditor);

        RMatrixPanel mainPanel = new RMatrixPanel(2, 1);
        mainPanel.add(dateFilterPanel);
        mainPanel.add(miscFilterPanel);

        LayoutUtility.alignPanels(dateFilterPanel, miscFilterPanel);

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Assign Filter To Dialog
     ***************************************************************************************************/

    public void setFilter(ShelfReplenishmentQueryFilter filter) throws Exception {
        model.setFilter(filter);

        statusEditor.setItems(model.findShelfReplenishmentStatus());
        typeEditor.setItems(model.findShelfReplenishmentTypes());
        userEditor.setItems(model.findEmployeesIds());
        productGroupEditor.setItems(model.findProductGroups());

        startDateEditor.setDate(filter.getFromDate());
        finalDateEditor.setDate(filter.getToDate());
        if (filter.getShelfReplenishmentId() != null) {
            shelfReplenishmentEditor.setText(filter.getShelfReplenishmentId().toString());
        }
        statusEditor.setSelectedItem(filter.getStatus());
        typeEditor.setSelectedItem(filter.getType());
        itemEditor.setText(filter.getItemId());
        userEditor.setSelectedItem(filter.getUserId());

        productGroupEditor.setSelectedItem(model.readProductGroupVO(filter.getProductGroupID()));

        setDefaultButton(applyButton);
    }

    /****************************************************************************************************
     * Build Item Search Listener
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
        ShelfReplenishmentQueryFilter filter = model.getFilter();

        filter.setDateRange(startDateEditor.getDateAtStartOfDay(), finalDateEditor.getDateAtEndOfDay());
        filter.setShelfReplenishmentId(shelfReplenishmentEditor.getLongOrNull());
        filter.setStatus((ShelfReplenishmentStatus) statusEditor.getSelectedItem());
        filter.setType((ShelfReplenishmentType) typeEditor.getSelectedItem());
        filter.setUserId((String) userEditor.getSelectedItem());
        filter.setItemId(null);
        filter.setProductGroupId(null);

        ItemVO item = (ItemVO) itemEditor.getData();
        if (item != null) {
            filter.setItemId(item.getId());
        }

        ProductGroupVO productGroupVO = (ProductGroupVO) productGroupEditor.getSelectedItem();
        if (productGroupVO != null) {
            filter.setProductGroupId(productGroupVO.getId());
        }

        RepositoryManager.addStateObject(SimClientStateKey.SHELF_REPLENISHMENT_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.SHELF_REPLENISHMENT_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
