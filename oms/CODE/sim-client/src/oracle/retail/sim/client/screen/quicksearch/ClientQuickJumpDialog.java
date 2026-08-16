package oracle.retail.sim.client.screen.quicksearch;

import javax.swing.JFrame;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.QuickJumpMode;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.core.locale.StringHelper;

/********************************************************************************************************
 * CLIENT QUICK JUMP DIALOG
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ClientQuickJumpDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -9164604242188208747L;

    private ClientQuickJumpDialogModel model = new ClientQuickJumpDialogModel();

    private RComboBoxEditor modeEditor = new RComboBoxEditor("Task");
    private RTextFieldEditor valueEditor = new RTextFieldEditor("ID");

    private RButton searchButton = new RButton(SimNavigation.DIALOG_JUMP);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    private static final String MODE_MODIFIED = "Mode.modified";

    /********************************************************************************************************
     * Build Dialog
     *******************************************************************************************************/

    public ClientQuickJumpDialog(JFrame frame) {
        super(frame, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Quick Jump");
        setStatusBarVisible(false);
        setDefaultButton(searchButton);
        setSize(350, 150);
        initInfoDialog();
        layoutInfoDialog();
        centerOnOwner();
    }

    private void initInfoDialog() {
        modeEditor.setDisplayer(new TranslatedObjectDisplayer());
        modeEditor.registerAction(this, MODE_MODIFIED);
        valueEditor.setLength(200);
        searchButton.registerAction(this, SimNavigation.DIALOG_JUMP);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutInfoDialog() {
        addButton(searchButton);
        addButton(cancelButton);

        REditorPanel mainPanel = new REditorPanel(2);
        mainPanel.add(modeEditor);
        mainPanel.add(valueEditor);

        setContentPane(mainPanel);
    }

    public void loadAvailableSearches() {
        modeEditor.setItems(model.getAvailableSearches());
        modeEditor.removeEmptySelection();
    }

    /********************************************************************************************************
     *  Handle Actions
     *******************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(MODE_MODIFIED)) {
                doModeModified();
            } else if (command.equals(SimNavigation.DIALOG_JUMP)) {
                doSearch();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doDone();
            }
        } catch (Exception e) {
            displayException(e);
        }
    }

    /********************************************************************************************************
     * Mode Modified
     *******************************************************************************************************/

    public void doModeModified() {
        QuickJumpMode mode = (QuickJumpMode) modeEditor.getSelectedItem();
        valueEditor.setEnabled(mode != QuickJumpMode.TRANSACTION_HISTORY);
    }

    /********************************************************************************************************
     *  Perform Done Action
     *******************************************************************************************************/

    public void doDone() {
        closeWindow();
    }

    /********************************************************************************************************
     *  Perform Search Action
     *******************************************************************************************************/

    private void doSearch() throws Exception {
        QuickJumpMode mode = (QuickJumpMode) modeEditor.getSelectedItem();
        String identifier = valueEditor.getTextOrNull();
        try {
            switch (mode) {
                case DIRECT_DELIVERY:
                    jumpToDirectDelivery(identifier);
                    break;
                case FULFILLMENT_ORDER:
                    jumpToFulfillmentOrder(identifier);
                    break;
                case FULFILLMENT_ORDER_PICK:
                    jumpToFulfillmentOrderPick(identifier);
                    break;
                case INVENTORY_ADJUSTMENT:
                    jumpToInventoryAdjustment(identifier);
                    break;
                case ITEM_LOOKUP:
                    jumpToItemLookup(identifier);
                    break;
                case PRODUCT_GROUP:
                    jumpToProductGroup(identifier);
                    break;
                case RETURN:
                    jumpToReturn(identifier);
                    break;
                case STOCK_COUNT:
                    jumpToStockCount(identifier);
                    break;
                case TRANSACTION_HISTORY:
                    jumpToTransactionHistory();
                    break;
                case TRANSFER:
                    jumpToTransfer(identifier);
                    break;
                case WAREHOUSE_DELIVERY:
                    jumpToWarehouseDelivery(identifier);
                    break;
                default:
                    break;
            }
            closeWindow();
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /** DIRECT DELIVERY */
    private void jumpToDirectDelivery(String identifier) throws Exception {
        if (StringHelper.isNullOrEmpty(identifier)) {
            jumpToScreen(SimScreenName.DIRECT_DELIVERY_LIST_SCREEN);
            return;
        }
        TransactionLoaderUtility.loadDirectDelivery(identifier);
        jumpToScreen(SimScreenName.DIRECT_DELIVERY_DETAIL_SCREEN);
    }

    /** FULFILLMENT ORDER */
    private void jumpToFulfillmentOrder(String identifier) throws Exception {
        if (StringHelper.isNullOrEmpty(identifier)) {
            jumpToScreen(SimScreenName.FULFILLMENT_ORDER_LIST_SCREEN);
            return;
        }
        TransactionLoaderUtility.loadCustomerOrder(identifier);
        jumpToScreen(SimScreenName.FULFILLMENT_ORDER_DETAIL_SCREEN);
    }

    /** FULFILLMENT ORDER PICK */
    private void jumpToFulfillmentOrderPick(String identifier) throws Exception {
        if (StringHelper.isNullOrEmpty(identifier)) {
            jumpToScreen(SimScreenName.FULFILLMENT_ORDER_PICK_LIST_SCREEN);
            return;
        }
        TransactionLoaderUtility.loadCustomerOrderPick(identifier);
        jumpToScreen(SimScreenName.FULFILLMENT_ORDER_PICK_DETAIL_SCREEN);
    }

    /** INVENTORY ADJUSTMENT */
    private void jumpToInventoryAdjustment(String identifier) throws Exception {
        if (StringHelper.isNullOrEmpty(identifier)) {
            jumpToScreen(SimScreenName.INVENTORY_ADJUSTMENT_LIST_SCREEN);
            return;
        }
        TransactionLoaderUtility.loadInventoryAdjustment(identifier);
        jumpToScreen(SimScreenName.INVENTORY_ADJUSTMENT_DETAIL_SCREEN);
    }

    /** ITEM LOOKUP */
    private void jumpToItemLookup(String identifier) throws Exception {
        if (StringHelper.isNullOrEmpty(identifier)) {
            Application.getApplicationFrame().navigate(SimScreenName.ITEM_LOOKUP_SCREEN);
            return;
        }
        TransactionLoaderUtility.loadItem(identifier);
        Application.getApplicationFrame().navigate(SimScreenName.ITEM_DETAIL_SCREEN);
    }

    /** PRODUCT GROUP */
    private void jumpToProductGroup(String identifier) throws Exception {
        if (StringHelper.isNullOrEmpty(identifier)) {
            jumpToScreen(SimScreenName.PRODUCT_GROUP_LIST_SCREEN);
            return;
        }
        TransactionLoaderUtility.loadProductGroup(identifier);
        jumpToScreen(SimScreenName.PRODUCT_GROUP_DETAIL_SCREEN);
    }

    /** RETURN */
    private void jumpToReturn(String identifier) throws Exception {
        if (StringHelper.isNullOrEmpty(identifier)) {
            jumpToScreen(SimScreenName.RETURN_LIST_SCREEN);
            return;
        }
        TransactionLoaderUtility.loadReturn(identifier);
        jumpToScreen(SimScreenName.RETURN_DETAIL_SCREEN);
    }

    /** STOCK COUNT */
    private void jumpToStockCount(String identifier) throws Exception {
        if (identifier == null) {
            jumpToScreen(SimScreenName.STOCK_COUNT_LIST_SCREEN);
            return;
        }
        TransactionLoaderUtility.loadStockCount(identifier);
        jumpToScreen(SimScreenName.STOCK_COUNT_LOCATION_SCREEN);
    }

    /** TRANSACTION HISTORY */
    private void jumpToTransactionHistory() {
        jumpToScreen(SimScreenName.TRANSACTION_HISTORY_SCREEN);
    }

    /** TRANSFER */
    private void jumpToTransfer(String identifier) throws Exception {
        if (StringUtility.isNullOrEmpty(identifier)) {
            jumpToScreen(SimScreenName.TRANSFER_LIST_SCREEN);
            return;
        }
        jumpToScreen(TransactionLoaderUtility.loadTransfer(identifier));
    }

    /** WAREHOUSE DELIVERY */
    private void jumpToWarehouseDelivery(String identifier) throws Exception {
        if (StringHelper.isNullOrEmpty(identifier)) {
            jumpToScreen(SimScreenName.WAREHOUSE_DELIVERY_LIST_SCREEN);
            return;
        }
        TransactionLoaderUtility.loadWarehouseDelivery(identifier);
        jumpToScreen(SimScreenName.WAREHOUSE_DELIVERY_DETAIL_SCREEN);
    }

    private void jumpToScreen(String screenName) {
        Application.getNavigationManager().clearNavigationStackToHome();
        Application.getApplicationFrame().navigate(screenName);
    }
}
