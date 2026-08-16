package oracle.retail.sim.client.screen.fulfillmentorderpick;

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
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickQueryFilter;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickStatus;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickType;
import oracle.retail.sim.common.item.ItemVO;

/********************************************************************************************************
 * Fulfillment Order Pick Filter Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderPickFilterDialog extends RDialog implements REventListener {

    private static final long serialVersionUID = 8948011767797853653L;

    private FulfillmentOrderPickFilterDialogModel model = new FulfillmentOrderPickFilterDialogModel();

    private RDateFieldEditor fromDateEditor = new RDateFieldEditor("From Date");
    private RDateFieldEditor toDateEditor = new RDateFieldEditor("To Date");

    private RNumericIdEditor pickEditor = new RNumericIdEditor("Pick ID", "Pick ID");
    private RNumericIdEditor simCustomerOrderEditor = new RNumericIdEditor("SIM Customer Order ID", "SIM Customer Order ID");
    private RTextFieldEditor fulfillmentOrderEditor = new RTextFieldEditor("Fulfillment Order ID");
    private RTextFieldEditor customerOrderEditor = new RTextFieldEditor("Customer Order ID");
    private RTextFieldEditor binEditor = new RTextFieldEditor("Bin ID");

    private RSearchFieldEditor itemEditor = SimEditorFactory.createItemVOSearchFieldEditor(true);
    private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");
    private RComboBoxEditor typeEditor = new RComboBoxEditor("Type");
    private RComboBoxEditor userEditor = new RComboBoxEditor("User");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    public FulfillmentOrderPickFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Customer Order Pick Filter");
        setSize(450, 400);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        pickEditor.setIdentifier(SimName.CUSTOMER_ORDER_PICK_ID);
        simCustomerOrderEditor.setIdentifier(SimName.CUSTOMER_ORDER_ID);
        customerOrderEditor.setIdentifier(SimName.CUSTOMER_ORDER_NUMBER);
        fulfillmentOrderEditor.setIdentifier(SimName.CUSTOMER_ORDER_FULFILLMENT_ID);
        binEditor.setIdentifier(SimName.CUSTOMER_ORDER_BIN_ID);

        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        typeEditor.setDisplayer(new TranslatedObjectDisplayer());
        userEditor.setDisplayer(new TranslatedObjectDisplayer());

        fromDateEditor.setSizeType(EditorConstants.LARGE);
        toDateEditor.setSizeType(EditorConstants.LARGE);

        pickEditor.setSizeType(EditorConstants.LARGE);
        simCustomerOrderEditor.setSizeType(EditorConstants.LARGE);
        fulfillmentOrderEditor.setSizeType(EditorConstants.MEDIUM);
        customerOrderEditor.setSizeType(EditorConstants.LARGE);
        customerOrderEditor.setLength(128);
        binEditor.setSizeType(EditorConstants.MEDIUM);
        statusEditor.setSizeType(EditorConstants.LARGE);
        typeEditor.setSizeType(EditorConstants.LARGE);
        userEditor.setSizeType(EditorConstants.LARGE);

        itemEditor.setSearchListener(buildItemSearchListener());

        statusEditor.setEmptyType(RComboBoxEmptyType.ALL);
        typeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        userEditor.setEmptyType(RComboBoxEmptyType.ALL);

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        REditorPanel dateFilterPanel = new REditorPanel(3);
        dateFilterPanel.setTitleBorder("Date Filters");
        dateFilterPanel.add(fromDateEditor);
        dateFilterPanel.add(toDateEditor);

        REditorPanel miscFilterPanel = new REditorPanel(10);
        miscFilterPanel.setTitleBorder("Additional Filters");
        miscFilterPanel.add(pickEditor);
        miscFilterPanel.add(simCustomerOrderEditor);
        miscFilterPanel.add(customerOrderEditor);
        miscFilterPanel.add(fulfillmentOrderEditor);
        miscFilterPanel.add(binEditor);
        miscFilterPanel.add(itemEditor);
        miscFilterPanel.add(statusEditor);
        miscFilterPanel.add(typeEditor);
        miscFilterPanel.add(userEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(dateFilterPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(miscFilterPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(new JLabel(), GridTool.constraints(0, 2, 2, 1, 0, 1, 0, 3, 0, 0, 0, 0));

        LayoutUtility.alignPanels(dateFilterPanel, miscFilterPanel);

        setContentPane(mainPanel);
    }

    public void setFilter(FulfillmentOrderPickQueryFilter filter) throws Exception {
        model.setFilter(filter);

        fromDateEditor.setDate(filter.getFromDate());
        toDateEditor.setDate(filter.getToDate());
        statusEditor.setItems(model.findFulfillmentOrderPickStatus());
        typeEditor.setItems(model.findFulfillmentOrderPickTypes());
        userEditor.setItems(model.findUsernames());

        if (filter.getSimCustomerOrderId() != null) {
            simCustomerOrderEditor.setLong(filter.getSimCustomerOrderId());
        } else {
            simCustomerOrderEditor.clear();
        }
        if (filter.getPickId() != null) {
            pickEditor.setLong(filter.getPickId());
        } else {
            pickEditor.clear();
        }
        customerOrderEditor.setText(filter.getCustomerOrderId());
        fulfillmentOrderEditor.setText(filter.getFulfillmentOrderId());
        binEditor.setText(filter.getBinId());

        if (filter.getItemId() != null) {
            itemEditor.setData(model.loadItem());
        }
        statusEditor.setSelectedItem(filter.getStatus());
        typeEditor.setSelectedItem(filter.getType());
        userEditor.setSelectedItem(filter.getUserId());

        setDefaultButton(applyButton);
    }

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
        itemEditor.clear();
        setFilter(model.resetFilter());
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }

    /****************************************************************************************************
     * Use Action
     ***************************************************************************************************/
    private void doApply() throws Exception {
        FulfillmentOrderPickQueryFilter filter = model.getFilter();

        filter.setBinId(binEditor.getTextOrNull());
        filter.setCustomerOrderId(customerOrderEditor.getTextOrNull());
        filter.setStatus((FulfillmentOrderPickStatus) statusEditor.getSelectedItem());
        filter.setDateRange(fromDateEditor.getDateAtStartOfDay(), toDateEditor.getDateAtEndOfDay());
        filter.setFulfillmentOrderId(fulfillmentOrderEditor.getTextOrNull());
        filter.setSimCustomerOrderId(simCustomerOrderEditor.getLongOrNull());
        filter.setPickId(pickEditor.getLongOrNull());
        filter.setType((FulfillmentOrderPickType) typeEditor.getSelectedItem());
        filter.setUserId((String) userEditor.getSelectedItem());

        ItemVO item = (ItemVO) itemEditor.getData();
        if (item != null) {
            filter.setItemId(item.getId());
        } else {
            filter.setItemId(null);
        }

        if (item != null) {
            RepositoryManager.addStateObject(SimClientStateKey.FULFILLMENT_ORDER_PICK_FILTER_ITEM_VO, item);
        } else {
            RepositoryManager.removeStateObject(SimClientStateKey.FULFILLMENT_ORDER_PICK_FILTER_ITEM_VO);
        }
        RepositoryManager.addStateObject(SimClientStateKey.FULFILLMENT_ORDER_PICK_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.FULFILLMENT_ORDER_PICK_FILTER_MODIFIED, filter));
        closeWindow();
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
}
