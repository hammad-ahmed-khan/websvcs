package oracle.retail.sim.client.screen.returns;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.finisher.FinisherSearchListener;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.screen.itemprice.PromotionSearchListener;
import oracle.retail.sim.client.screen.supplier.SupplierSearchListener;
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
import oracle.retail.sim.client.swing.panel.RMatrixPanel;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.itemprice.PromotionVO;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.source.ContextTypeComparator;
import oracle.retail.sim.common.source.Finisher;
import oracle.retail.sim.common.source.Source;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.source.WarehouseComparator;
import oracle.retail.sim.common.stockreturn.ReturnQueryFilter;
import oracle.retail.sim.common.stockreturn.ReturnQueryStatus;
import oracle.retail.sim.common.stockreturn.ReturnReason;

/********************************************************************************************************
 * This dialog handles entering the filter information for returns.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ReturnFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 3622239405679722977L;

    private ReturnFilterDialogModel model = new ReturnFilterDialogModel();

    private RDateFieldEditor startDateEditor = new RDateFieldEditor("Return From Date");
    private RDateFieldEditor finalDateEditor = new RDateFieldEditor("Return To Date");
    private RNumericIdEditor returnEditor = new RNumericIdEditor("Return Number", "Return");
    private RSearchFieldEditor supplierEditor = SimEditorFactory.createSupplierSearchFieldEditor();
    private RSearchFieldEditor finisherEditor = SimEditorFactory.createFinisherSearchFieldEditor();
    private RComboBoxEditor warehouseEditor = new RComboBoxEditor("Warehouse");
    private RComboBoxEditor contextTypeEditor = new RComboBoxEditor("Context Type");
    private RSearchFieldEditor contextValueEditor = SimEditorFactory.createPromotionSearchFieldEditor();
    private RSearchFieldEditor itemEditor = SimEditorFactory.createItemVOSearchFieldEditor(false);
    private RTextFieldEditor authorizeEditor = new RTextFieldEditor("Authorization Number");
    private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");
    private RComboBoxEditor reasonEditor = new RComboBoxEditor("Reason");
    private RComboBoxEditor employeeEditor = new RComboBoxEditor("User");
    private RTextFieldEditor externalIdEditor = new RTextFieldEditor("External Id");

    private static final String WAREHOUSE_SELECTED = "Warehouse.selected";

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public ReturnFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Return List Filter");
        setSize(450, 500);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        returnEditor.setIdentifier(SimName.TRANSFER_ID);
        returnEditor.setEntryAlignmentLeft();
        authorizeEditor.setIdentifier(SimName.TRANSFER_AUTH);
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        statusEditor.setEmptyType(RComboBoxEmptyType.ALL);
        reasonEditor.setDisplayer(new TranslatedObjectDisplayer());
        warehouseEditor.setDisplayer(new IdNameDisplayer());
        warehouseEditor.setComparator(WarehouseComparator.getInstance());
        itemEditor.setSearchListener(buildItemSearchListener());
        supplierEditor.setSearchListener(buildSupplierSearchListener());
        finisherEditor.setSearchListener(buildFinisherSearchListener());
        contextTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        contextTypeEditor.setComparator(ContextTypeComparator.getInstance());
        contextValueEditor.setSearchListener(buildPromotionSearchListener());
        reasonEditor.setEmptyType(RComboBoxEmptyType.ALL);
        warehouseEditor.setEmptyType(RComboBoxEmptyType.ALL);
        contextTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        employeeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        startDateEditor.setSizeType(EditorConstants.MEDIUM);
        finalDateEditor.setSizeType(EditorConstants.MEDIUM);
        externalIdEditor.setIdentifier(SimName.TRANSFER_EXTERNAL_ID);

        RDateFieldRangeUtility.setDateRangeEditors(startDateEditor, finalDateEditor);

        warehouseEditor.registerAction(this, WAREHOUSE_SELECTED);

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

        REditorPanel miscFilterPanel = new REditorPanel(12);
        miscFilterPanel.setTitleBorder("Additional Filters");
        miscFilterPanel.add(returnEditor);
        miscFilterPanel.add(supplierEditor);
        if (model.isFinishersEnabled()) {
            miscFilterPanel.add(finisherEditor);
        }
        miscFilterPanel.add(warehouseEditor);
        miscFilterPanel.add(contextTypeEditor);
        miscFilterPanel.add(contextValueEditor);
        miscFilterPanel.add(itemEditor);
        miscFilterPanel.add(authorizeEditor);
        miscFilterPanel.add(statusEditor);
        miscFilterPanel.add(reasonEditor);
        miscFilterPanel.add(employeeEditor);
        miscFilterPanel.add(externalIdEditor);

        RMatrixPanel mainPanel = new RMatrixPanel(2, 1);
        mainPanel.add(dateFilterPanel);
        mainPanel.add(miscFilterPanel);

        LayoutUtility.alignPanels(dateFilterPanel, miscFilterPanel);

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Assign Filter To Dialog
     ***************************************************************************************************/

    public void setFilter(ReturnQueryFilter filter) throws Exception {
        model.setFilter(filter);

        employeeEditor.setActionsEnabled(false);
        employeeEditor.setItems(model.findEmployeeIds());
        employeeEditor.setActionsEnabled(true);

        warehouseEditor.setActionsEnabled(false);
        warehouseEditor.setItems(model.findAllWarehouses());
        warehouseEditor.setActionsEnabled(true);

        contextTypeEditor.setActionsEnabled(false);
        contextTypeEditor.setItems(model.findAllContextTypes());
        contextTypeEditor.setActionsEnabled(false);

        statusEditor.setActionsEnabled(false);
        statusEditor.setItems(model.findReturnQueryStatus());
        statusEditor.setActionsEnabled(true);

        reasonEditor.setActionsEnabled(false);
        reasonEditor.setItems(model.findReturnReasons());
        reasonEditor.setActionsEnabled(true);

        if (filter.getReturnId() != null) {
            returnEditor.setLong(filter.getReturnId());
        } else {
            returnEditor.clear();
        }
        externalIdEditor.setText(filter.getExternalId());
        authorizeEditor.setText(filter.getAuthCode());
        startDateEditor.setDate(filter.getFromDate());
        finalDateEditor.setDate(filter.getToDate());
        supplierEditor.setText(filter.getSupplierId());
        finisherEditor.setText(filter.getFinisherId());
        contextValueEditor.setText(filter.getContextValue());
        statusEditor.setSelectedItem(filter.getStatus());
        reasonEditor.setSelectedItem(filter.getReason());
        warehouseEditor.setSelectedItem(model.getWarehouse(filter.getWarehouseId()));
        contextTypeEditor.setSelectedItem(filter.getContextType());
        itemEditor.setText(filter.getItemId());
        employeeEditor.setSelectedItem(filter.getUserId());

        doResetSourceEditors();

        setDefaultButton(applyButton);
    }

    private void doResetSourceEditors() {
        if (supplierEditor.getData() != null) {
            finisherEditor.clear();
            finisherEditor.setEnabled(false);
            warehouseEditor.setEmptySelection();
            warehouseEditor.setEnabled(false);
        } else if (finisherEditor.getData() != null) {
            supplierEditor.clear();
            supplierEditor.setEnabled(false);
            warehouseEditor.setEmptySelection();
            warehouseEditor.setEnabled(false);
        } else if (warehouseEditor.getSelectedItem() != null) {
            supplierEditor.clear();
            supplierEditor.setEnabled(false);
            finisherEditor.clear();
            finisherEditor.setEnabled(false);
        } else {
            supplierEditor.setEnabled(true);
            finisherEditor.setEnabled(true);
            warehouseEditor.setEnabled(true);
        }
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

    private SupplierSearchListener buildSupplierSearchListener() {
        return new SupplierSearchListener() {
            public void assignSupplier(Supplier supplier) {
                if (supplier != null) {
                    supplierEditor.setData(supplier);
                    finisherEditor.clear();
                    finisherEditor.setEnabled(false);
                    warehouseEditor.setEmptySelection();
                    warehouseEditor.setEnabled(false);
                }
            }
        };
    }

    private FinisherSearchListener buildFinisherSearchListener() {
        return new FinisherSearchListener() {
            public void assignFinisher(Finisher finisher) {
                if (finisher != null) {
                    finisherEditor.setData(finisher);
                    supplierEditor.clear();
                    supplierEditor.setEnabled(false);
                    warehouseEditor.setEmptySelection();
                    warehouseEditor.setEnabled(false);
                }
            }
        };
    }

    private PromotionSearchListener buildPromotionSearchListener() {
        return new PromotionSearchListener() {
            public void assignPromotion(PromotionVO promotion) {
                if (promotion != null) {
                    contextValueEditor.setData(promotion);
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
            } else if (command.equals(WAREHOUSE_SELECTED)) {
                doWarehouseSelected();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doWarehouseSelected() {
        if (warehouseEditor.getSelectedItem() != null) {
            supplierEditor.clear();
            supplierEditor.setEnabled(false);
            finisherEditor.clear();
            finisherEditor.setEnabled(false);
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
        ReturnQueryFilter filter = model.getFilter();

        filter.setDateRange(startDateEditor.getDateAtStartOfDay(), finalDateEditor.getDateAtEndOfDay());
        filter.setReturnId(returnEditor.getLongOrNull());
        filter.setAuthCode(authorizeEditor.getTextOrNull());
        filter.setStatus((ReturnQueryStatus) statusEditor.getSelectedItem());
        filter.setUserId((String) employeeEditor.getSelectedItem());
        filter.setReason((ReturnReason) reasonEditor.getSelectedItem());

        Source source = (Source) warehouseEditor.getSelectedItem();
        if (source != null) {
            filter.setWarehouseId(source.getId());
        } else {
            filter.setWarehouseId(null);
        }

        Supplier supplier = (Supplier) supplierEditor.getData();
        if (supplier != null) {
            filter.setSupplierId(supplier.getId());
        } else {
            filter.setSupplierId(null);
        }

        Finisher finisher = (Finisher) finisherEditor.getData();
        if (finisher != null) {
            filter.setFinisherId(finisher.getId());
        } else {
            filter.setFinisherId(null);
        }

        ItemVO item = (ItemVO) itemEditor.getData();
        if (item != null) {
            filter.setItemId(item.getId());
        } else {
            filter.setItemId(null);
        }

        ContextType contextType = (ContextType) contextTypeEditor.getSelectedItem();
        if (contextType != null) {
            filter.setContextType(contextType);
        } else {
            filter.setContextType(null);
        }

        PromotionVO promotionVO = (PromotionVO) contextValueEditor.getData();
        if (promotionVO != null) {
            filter.setContextValue(promotionVO.getId());
        } else {
            filter.setContextValue(null);
        }

        filter.setExternalId(externalIdEditor.getTextOrNull());
        RepositoryManager.addStateObject(SimClientStateKey.RETURN_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.RETURN_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
