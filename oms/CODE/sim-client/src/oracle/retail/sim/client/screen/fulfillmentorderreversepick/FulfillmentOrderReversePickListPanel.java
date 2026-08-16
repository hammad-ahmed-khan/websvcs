package oracle.retail.sim.client.screen.fulfillmentorderreversepick;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.displayer.GenericIdDisplayer;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.screen.notes.NotesDialog;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.DateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.MediumDateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickProperty;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickStatus;
import oracle.retail.sim.common.report.ReportMessageText;

/********************************************************************************************************
 * Fulfillment Order Reverse Pick List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderReversePickListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -8294549523048834439L;

    private FulfillmentOrderReversePickListModel model = new FulfillmentOrderReversePickListModel();

    private RDisplayLabelEditor simFulfilOrderIdEditor = new RDisplayLabelEditor("SIM Customer Order ID");
    private RDisplayLabelEditor customerOrderIdEditor = new RDisplayLabelEditor("Customer Order ID");
    private RDisplayLabelEditor fulfillmentOrderIdEditor = new RDisplayLabelEditor("Fulfillment Order ID");
    private RDisplayLabelEditor orderStatusEditor = new RDisplayLabelEditor("Order Status");
    private RDisplayLabelEditor reservationTypeEditor = new RDisplayLabelEditor("Reservation Type");
    private RLongTextFieldEditor commentsEditor = new RLongTextFieldEditor("Comments");
    private RDisplayLabelEditor createDateEditor = new RDisplayLabelEditor("Order Create Date");
    private RDisplayLabelEditor releaseDateEditor = new RDisplayLabelEditor("Order Release Date");
    private RDisplayLabelEditor deliveryDateEditor = new RDisplayLabelEditor("Order Delivery Date");
    private RDisplayLabelEditor deliveryTypeEditor = new RDisplayLabelEditor("Delivery Type");
    private RDisplayLabelEditor carrierEditor = new RDisplayLabelEditor("Carrier");
    private RDisplayLabelEditor serviceEditor = new RDisplayLabelEditor("Service");
    private RDisplayLabelEditor partialDeliveryEditor = new RDisplayLabelEditor("Allow Partial Delivery");

    private SimTable reversePickTable = new SimTable(new CustomerOrderReversePickListDefinition());
    private SimTablePane reversePickPane = new SimTablePane(reversePickTable);

    private static final String REVERSE_PICK_SELECTED = "OrderReversePick.selected";

    public FulfillmentOrderReversePickListPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        simFulfilOrderIdEditor.setDisplayer(new IdNameDisplayer());
        customerOrderIdEditor.setDisplayer(new IdNameDisplayer());
        fulfillmentOrderIdEditor.setDisplayer(new IdNameDisplayer());
        orderStatusEditor.setDisplayer(new TranslatedObjectDisplayer());
        reservationTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        commentsEditor.setEnabled(true, false);
        createDateEditor.setDisplayer(new DateTimeDisplayer());
        releaseDateEditor.setDisplayer(new DateTimeDisplayer());
        deliveryDateEditor.setDisplayer(new DateTimeDisplayer());
        deliveryTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        carrierEditor.setDisplayer(new TranslatedObjectDisplayer());
        serviceEditor.setDisplayer(new TranslatedObjectDisplayer());
        partialDeliveryEditor.setDisplayer(new BooleanDisplayer());
        reversePickTable.setTableEditable(false);
        reversePickTable.setMultipleRowSelectionMode();
        reversePickTable.registerDoubleClickAction(this, REVERSE_PICK_SELECTED);
    }

    private void layoutScreen() {
        REditorPanel detailPanel = new REditorPanel(5, 3);
        detailPanel.add(customerOrderIdEditor);
        detailPanel.add(fulfillmentOrderIdEditor);
        detailPanel.add(simFulfilOrderIdEditor);
        detailPanel.add(orderStatusEditor);
        detailPanel.add(reservationTypeEditor);
        detailPanel.add(createDateEditor);
        detailPanel.add(releaseDateEditor);
        detailPanel.add(deliveryDateEditor);
        detailPanel.skip();
        detailPanel.skip();
        detailPanel.add(deliveryTypeEditor);
        detailPanel.add(carrierEditor);
        detailPanel.add(serviceEditor);
        detailPanel.add(partialDeliveryEditor);

        REditorPanel commentPanel = new REditorPanel(1, 1);
        commentPanel.add(commentsEditor);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(detailPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(commentPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(divider, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(reversePickPane, GridTool.constraints(0, 3, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

        LayoutUtility.alignPanels(detailPanel, commentPanel);

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Start Screen
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadFulfillmentOrder();
        populateScreen();
    }

    private void populateScreen() throws Exception {
        FulfillmentOrder fulfillmentOrder = model.getFulfillmentOrder();
        simFulfilOrderIdEditor.setData(fulfillmentOrder.getId());
        customerOrderIdEditor.setData(fulfillmentOrder.getCustomerOrderId());
        fulfillmentOrderIdEditor.setData(fulfillmentOrder.getExternalId());
        orderStatusEditor.setData(fulfillmentOrder.getStatus());
        reservationTypeEditor.setData(fulfillmentOrder.getOrderType());
        commentsEditor.setText(fulfillmentOrder.getComments());
        createDateEditor.setData(fulfillmentOrder.getCreateDate());
        releaseDateEditor.setData(fulfillmentOrder.getReleaseDate());
        deliveryDateEditor.setData(fulfillmentOrder.getDeliveryDate());
        deliveryTypeEditor.setData(fulfillmentOrder.getDeliveryType());
        carrierEditor.setData(fulfillmentOrder.getDeliveryCarrier());
        serviceEditor.setData(fulfillmentOrder.getDeliveryService());
        partialDeliveryEditor.setData(fulfillmentOrder.isAllowPartialDelivery());
        reversePickTable.setRows(model.findFulfillmentOrderReversePickVOs());
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return reversePickTable;
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(REVERSE_PICK_SELECTED)) {
                doReversePickSelected();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doReversePickSelected() throws Exception {
        FulfillmentOrderReversePickWrapper wrapper = (FulfillmentOrderReversePickWrapper) reversePickTable.getSelectedRowData();
        if (wrapper != null) {
            model.storeReversePick(wrapper);
            navigate(SimScreenName.FULFILLMENT__ORDER_REVERSE_PICK_DETAIL_SCREEN);
        }
    }

    public boolean isCreateFunctionAvailable() {
        return model.isCreateFunctionAvailable();
    }

    public boolean isCancelFunctionAvailable() {
        return model.isDeleteFunctionAvailable();
    }

    /****************************************************************************************************
     * Handle Create
     ***************************************************************************************************/
    public void handleCreatePick() throws Exception {
        model.createReversePick();
    }

    /****************************************************************************************************
     * Handle Delete
     * @throws Exception
     ***************************************************************************************************/
    public void handleCancelPick() throws Exception {
        reversePickTable.stopEditing();

        if (reversePickTable.getSelectedRowCount() < 1) {
            displayMessage(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }
        if (RConfirmUtility.confirm("Reverse Pick Delete Confirmation", FulfillmentOrderMessageText.REVERSE_PICK_DELETE_CONFIRM)) {
            List<FulfillmentOrderReversePickWrapper> wrappers = reversePickTable.getAllSelectedRowData();
            for (FulfillmentOrderReversePickWrapper wrapper : wrappers) {
                try {
                    if (wrapper.getStatus() != FulfillmentOrderReversePickStatus.NEW && wrapper.getStatus() != FulfillmentOrderReversePickStatus.IN_PROGRESS) {
                        displayWarning(FulfillmentOrderMessageText.INVALID_REVERSE_PICK_STATUS_TO_DELETE);
                        return;
                    }
                    model.cancelReversePick(wrapper);
                } catch (Exception e) {
                    displayException(e);
                } finally {

                    model.releaseLock(wrapper.getIdAsString());
                }
            }
            populateScreen();

        }
    }

    /****************************************************************************************************
     * Handle Print
     ***************************************************************************************************/
    public void handlePrint() throws Exception {
        //TODO: neetusin add details.
        List<FulfillmentOrderReversePickWrapper> wrappers = reversePickTable.getAllSelectedRowData();
        if (wrappers.isEmpty()) {
            throw new BusinessException(ReportMessageText.NO_ROWS_SELECTED_PRINT);
        }
        model.printReversePicks(wrappers);

    }

    /****************************************************************************************************
     * Handle Notes
     ***************************************************************************************************/

    public void handleNotes() {
        try {
            NotesDialog dialog = new NotesDialog();
            dialog.setTitle("Customer Order Notes");
            dialog.loadNotes(FunctionalArea.CUSTOMER_ORDER, model.getFulfillmentOrder().getId(), model.isNotesEditable());
            dialog.setVisible(true);
        } catch (Exception exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Customer Order Reverse Pick List Table Definition
     ***************************************************************************************************/

    private class CustomerOrderReversePickListDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return FulfillmentOrderReversePickWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> sortAttributes = new ArrayList<>();
            sortAttributes.add(new SimTableSortAttribute(FulfillmentOrderReversePickProperty.CREATE_DATE, false));
            return sortAttributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(4);
            attributes.add(new SimTableAttribute("ID", FulfillmentOrderReversePickProperty.REVERSE_PICK_ID, new GenericIdDisplayer()));
            attributes.add(new SimTableAttribute("Status", FulfillmentOrderReversePickProperty.STATUS, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("User", FulfillmentOrderReversePickProperty.CREATE_USER));
            attributes.add(new SimTableAttribute("Create Date", FulfillmentOrderReversePickProperty.CREATE_DATE, new MediumDateTimeDisplayer()));
            return attributes;
        }
    }
}
