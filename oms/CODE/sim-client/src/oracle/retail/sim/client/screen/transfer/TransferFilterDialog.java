package oracle.retail.sim.client.screen.transfer;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.screen.itemprice.PromotionSearchListener;
import oracle.retail.sim.client.screen.store.StoreSearchListener;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldRangeUtility;
import oracle.retail.sim.client.swing.editor.RNumericIdEditor;
import oracle.retail.sim.client.swing.editor.RSearchComboEditor;
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
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.itemprice.PromotionVO;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.source.ContextTypeComparator;
import oracle.retail.sim.common.store.BuddyStore;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.common.transfer.TransferPhase;
import oracle.retail.sim.common.transfer.TransferQueryFilter;
import oracle.retail.sim.common.transfer.TransferQueryStatus;

/********************************************************************************************************
 * This dialog handles entering the filter information for transfers and is attached to the transfer
 * list screen.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -2651519771204569162L;

    private TransferFilterDialogModel model = new TransferFilterDialogModel();

    private RDateFieldEditor startDateEditor = new RDateFieldEditor("From Date");
    private RDateFieldEditor endDateEditor = new RDateFieldEditor("To Date");
    private RSearchComboEditor storeEditor = SimEditorFactory.createStoreSearchComboEditor("Transfer Location");
    private RSearchFieldEditor itemEditor = SimEditorFactory.createItemVOSearchFieldEditor(false);
    private RNumericIdEditor transferIdEditor = new RNumericIdEditor("Transfer Id", "Transfer");
    private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");
    private RComboBoxEditor employeeEditor = new RComboBoxEditor("User");
    private RComboBoxEditor phaseEditor = new RComboBoxEditor("Type");
    private RTextFieldEditor externalIdEditor = new RTextFieldEditor("External Id");
    private RTextFieldEditor customerOrderIdEditor = new RTextFieldEditor("Customer Order ID");
    private RTextFieldEditor fulfillmentOrderExternalIdEditor = new RTextFieldEditor("Fulfillment Order ID");
    private RCheckBoxEditor anyCustomerOrderEditor = new RCheckBoxEditor("Customer Orders");
    private RComboBoxEditor contextTypeEditor = new RComboBoxEditor("Context Type");
    private RSearchFieldEditor contextValueEditor = SimEditorFactory.createPromotionSearchFieldEditor();
    private static final String CONTEXT_TYPE_MODIFIED = "ContextType.modified";

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public TransferFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Transfer List Filter");
        setSize(450, 500);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        startDateEditor.setSizeType(EditorConstants.LARGE);
        endDateEditor.setSizeType(EditorConstants.LARGE);

        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        statusEditor.setEmptyType(RComboBoxEmptyType.ALL);

        contextTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        contextTypeEditor.setComparator(ContextTypeComparator.getInstance());
        contextTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        contextTypeEditor.registerAction(this, CONTEXT_TYPE_MODIFIED);

        //contextValueEditor.setEnabled(false);
        contextValueEditor.setSearchListener(buildPromotionSearchListener());

        itemEditor.setSearchListener(buildItemSearchListener());
        employeeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        transferIdEditor.setIdentifier(SimName.TRANSFER_ID);

        phaseEditor.setDisplayer(new TranslatedObjectDisplayer());
        phaseEditor.setEmptyType(RComboBoxEmptyType.ALL);

        storeEditor.getComboBox().setEmptyType(RComboBoxEmptyType.ALL);
        storeEditor.setSearchListener(buildStoreSearchListener());
        externalIdEditor.setIdentifier(SimName.TRANSFER_EXTERNAL_ID);
        RDateFieldRangeUtility.setDateRangeEditors(startDateEditor, endDateEditor);

        customerOrderIdEditor.setIdentifier(SimName.CUSTOMER_ORDER_ID);
        fulfillmentOrderExternalIdEditor.setIdentifier(SimName.CUSTOMER_ORDER_FULFILLMENT_ID);
        anyCustomerOrderEditor.setSizeType(EditorConstants.SMALL);

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);

        validateEditorState();
    }

    private void validateEditorState() {
        setContextValueEditorState();
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        REditorPanel dateFilterPanel = new REditorPanel(2);
        dateFilterPanel.setTitleBorder("Date Filters");
        dateFilterPanel.add(startDateEditor);
        dateFilterPanel.add(endDateEditor);

        REditorPanel miscFilterPanel = new REditorPanel(13);
        miscFilterPanel.setTitleBorder("Additional Filters");
        miscFilterPanel.add(storeEditor);
        miscFilterPanel.add(statusEditor);
        miscFilterPanel.add(contextTypeEditor);
        miscFilterPanel.add(contextValueEditor);
        miscFilterPanel.add(itemEditor);
        miscFilterPanel.add(employeeEditor);
        miscFilterPanel.add(transferIdEditor);
        miscFilterPanel.add(externalIdEditor);
        miscFilterPanel.add(phaseEditor);
        miscFilterPanel.add(customerOrderIdEditor);
        miscFilterPanel.add(fulfillmentOrderExternalIdEditor);
        miscFilterPanel.add(anyCustomerOrderEditor);

        RMatrixPanel mainPanel = new RMatrixPanel(2, 1);
        mainPanel.add(dateFilterPanel);
        mainPanel.add(miscFilterPanel);

        LayoutUtility.alignPanels(dateFilterPanel, miscFilterPanel);

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Assign Filter To Dialog
     ***************************************************************************************************/

    public void setFilter(TransferQueryFilter filter) throws Exception {
        model.setFilter(filter);

        BuddyStore buddyStore = filter.getBuddyStore();

        storeEditor.setActionsEnabled(false);
        storeEditor.setItems(model.findBuddyStores());
        if (buddyStore != null) {
            storeEditor.addItem(buddyStore);
            storeEditor.setSelectedItem(buddyStore);
        }
        storeEditor.setActionsEnabled(true);
        statusEditor.setItems(model.findAllTransferQueryStatus());

        contextTypeEditor.setActionsEnabled(false);
        contextTypeEditor.setItems(model.findContextTypes());
        contextTypeEditor.setActionsEnabled(true);

        contextValueEditor.setEnabled(false);
        contextValueEditor.setText(filter.getContextValue());
        phaseEditor.setItems(model.findAllTransferPhase());
        startDateEditor.setDate(filter.getFromDate());
        endDateEditor.setDate(filter.getToDate());
        itemEditor.setText(filter.getItemId());
        transferIdEditor.setLong(filter.getTransferId());
        statusEditor.setSelectedItem(filter.getStatus());
        phaseEditor.setSelectedItem(filter.getTransferPhase());
        employeeEditor.setActionsEnabled(false);
        employeeEditor.setItems(model.findEmployeeIds());
        employeeEditor.setActionsEnabled(true);
        employeeEditor.setSelectedItem(filter.getUserId());
        externalIdEditor.setText(filter.getExternalId());

        customerOrderIdEditor.setText(filter.getCustomerOrderId());
        fulfillmentOrderExternalIdEditor.setText(filter.getFulfillmentOrderExternalId());
        anyCustomerOrderEditor.setSelected(filter.isAnyFulfillmentOrder());

        setDefaultButton(applyButton);
    }

    /****************************************************************************************************
     * Search Listeners - Pops up either the item or store search dialog.
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

    private StoreSearchListener buildStoreSearchListener() {
        return new StoreSearchListener() {
            public void assignStore(Store store) {
                if (store != null) {
                    storeEditor.setData(BOFactory.createBuddyStore(store));
                }
            }
        };
    }

    private PromotionSearchListener buildPromotionSearchListener() {
        return new PromotionSearchListener() {
            public void assignPromotion(PromotionVO promotionVO) {
                if (promotionVO != null) {
                    contextValueEditor.setData(promotionVO);
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
            } else if (command.equals(CONTEXT_TYPE_MODIFIED)) {
                doContextTypeChanged();
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
        TransferQueryFilter filter = model.getFilter();

        filter.setDateRange(startDateEditor.getDateAtStartOfDay(), endDateEditor.getDateAtEndOfDay());
        filter.setTransferId(transferIdEditor.getLongOrNull());
        filter.setStatus((TransferQueryStatus) statusEditor.getSelectedItem());
        filter.setTransferPhase((TransferPhase) phaseEditor.getSelectedItem());
        filter.setUserId((String) employeeEditor.getSelectedItem());
        filter.setBuddyStore((BuddyStore) storeEditor.getSelectedItem());
        filter.setExternalId(externalIdEditor.getTextOrNull());
        filter.setCustomerOrderId(customerOrderIdEditor.getTextOrNull());
        filter.setFulfillmentOrderExternalId(fulfillmentOrderExternalIdEditor.getTextOrNull());
        filter.setAnyFulfillmentOrder(anyCustomerOrderEditor.isSelected());

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

        RepositoryManager.addStateObject(SimClientStateKey.TRANSFER_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.TRANSFER_FILTER_MODIFIED, filter));
        closeWindow();
    }

    private void doContextTypeChanged() {
        setContextValueEditorState();
    }

    private void setContextValueEditorState() {
        Object obj = contextTypeEditor.getSelectedItem();
        if (obj instanceof ContextType) {
            ContextType contextType = (ContextType) obj;
            if (contextType.isPromotion()) {
                contextValueEditor.setEnabled(true);
            } else {
                contextValueEditor.setText(null);
                contextValueEditor.setEnabled(false);
            }
        }
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
