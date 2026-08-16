package extra.retail.sim.client.screen.imeifulfillmentorderdelivery;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
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
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryStatus;
import oracle.retail.sim.common.report.ReportMessageText;

public class IMEIFulfillmentOrderDeliveryListPanel extends ScreenPanel implements REventListener {

    private static final long serialVersionUID = -1565132665004289310L;

    private static final String FULFILL_ORDER_DELIVERY_SELECTED = "FulfillmentOrderDelivery.selected";

    private IMEIFulfillmentOrderDeliveryListModel model = new IMEIFulfillmentOrderDeliveryListModel();

    private RDisplayLabelEditor customerOrderIdEditor = new RDisplayLabelEditor("Customer Order ID");
    private RDisplayLabelEditor fulfillmentOrderIdEditor = new RDisplayLabelEditor("Fulfillment Order ID");
    private RDisplayLabelEditor simFulfilOrderIdEditor = new RDisplayLabelEditor("SIM Customer Order ID");
    private RDisplayLabelEditor resvTypeEditor = new RDisplayLabelEditor("Reservation Type");
    private RDisplayLabelEditor orderStatusEditor = new RDisplayLabelEditor("Order Status");
    private RDisplayLabelEditor createDateEditor = new RDisplayLabelEditor("Order Create Date");
    private RDisplayLabelEditor releaseDateEditor = new RDisplayLabelEditor("Order Release Date");
    private RDisplayLabelEditor deliveryDateEditor = new RDisplayLabelEditor("Order Delivery Date");
    private RDisplayLabelEditor deliveryTypeEditor = new RDisplayLabelEditor("Delivery Type");
    private RDisplayLabelEditor carrierEditor = new RDisplayLabelEditor("Carrier");
    private RDisplayLabelEditor serviceEditor = new RDisplayLabelEditor("Service");
    private RDisplayLabelEditor partialDeliveryEditor = new RDisplayLabelEditor("Allow Partial Delivery");
    private RLongTextFieldEditor commentsEditor = new RLongTextFieldEditor("Comments");

    private SimTable deliveryTable = new SimTable(new FulfillmentOrderDeliveryListDefinition());
    private SimTablePane deliveryPane = new SimTablePane(deliveryTable);

    public IMEIFulfillmentOrderDeliveryListPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        simFulfilOrderIdEditor.setDisplayer(new IdNameDisplayer());
        customerOrderIdEditor.setDisplayer(new IdNameDisplayer());
        fulfillmentOrderIdEditor.setDisplayer(new IdNameDisplayer());
        resvTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        orderStatusEditor.setDisplayer(new TranslatedObjectDisplayer());
        deliveryTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        carrierEditor.setDisplayer(new TranslatedObjectDisplayer());
        serviceEditor.setDisplayer(new TranslatedObjectDisplayer());
        createDateEditor.setDisplayer(new DateTimeDisplayer());
        deliveryDateEditor.setDisplayer(new DateTimeDisplayer());
        releaseDateEditor.setDisplayer(new DateTimeDisplayer());
        partialDeliveryEditor.setDisplayer(new BooleanDisplayer());

        commentsEditor.setEnabled(true, false);

        deliveryTable.setTableEditable(false);
        deliveryTable.setMultipleRowSelectionMode();
        deliveryTable.registerDoubleClickAction(this, FULFILL_ORDER_DELIVERY_SELECTED);
    }

    private void layoutScreen() {
        REditorPanel detailPanel = new REditorPanel(5, 3);
        detailPanel.add(customerOrderIdEditor);
        detailPanel.add(fulfillmentOrderIdEditor);
        detailPanel.add(simFulfilOrderIdEditor);
        detailPanel.add(orderStatusEditor);
        detailPanel.add(resvTypeEditor);
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
        mainPanel.add(deliveryPane, GridTool.constraints(0, 3, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        LayoutUtility.alignPanels(detailPanel, commentPanel);

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return deliveryTable;
    }

    public void start() {
        try {
            populateScreen();
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void populateScreen() throws Exception {
        FulfillmentOrder order = model.getFulfillmentOrder();
        simFulfilOrderIdEditor.setData(order.getId());
        customerOrderIdEditor.setData(order.getCustomerOrderId());
        fulfillmentOrderIdEditor.setData(order.getExternalId());
        commentsEditor.setText(order.getComments());
        createDateEditor.setData(order.getCreateDate());
        releaseDateEditor.setData(order.getReleaseDate());
        deliveryDateEditor.setData(order.getDeliveryDate());
        orderStatusEditor.setData(order.getStatus());
        resvTypeEditor.setData(order.getOrderType());
        deliveryTypeEditor.setData(order.getDeliveryType());
        carrierEditor.setData(order.getDeliveryCarrier());
        serviceEditor.setData(order.getDeliveryService());
        partialDeliveryEditor.setData(order.isAllowPartialDelivery());

        deliveryTable.setRows(model.findFulfillmentOrderDeliveryVOs());
    }

    /****************************************************************************************************
     * Handle Create
     ***************************************************************************************************/
    public void handleCreateDelivery() throws Exception {
        if (!model.isCustomerOrderActive()) {
            displayWarning(FulfillmentOrderMessageText.INVALID_CUSTOMER_ORDER_STATUS_DELIVERY_CREATE);
            return;
        }
        if (!model.validateForCreate()) {
            return;
        }
        model.createDelivery();
        model.markFulfillmentOrderAsInProgress();
        navigate(SimScreenName.FULFILLMENT_ORDER_DELIVERY_DETAIL_SCREEN);
    }

    public boolean isCreateFunctionAvailable() {
        return model.isCreateFunctionAvailable();
    }

    /****************************************************************************************************
     * Handle Print
     ***************************************************************************************************/
    public void handlePrint() throws Exception {
        List<IMEIFulfillmentOrderDeliveryWrapper> wrappers = deliveryTable.getAllSelectedRowData();
        if (wrappers.isEmpty()) {
            throw new BusinessException(ReportMessageText.NO_ROWS_SELECTED_PRINT);
        }
        model.printDeliveries(wrappers);
    }

    /****************************************************************************************************
     * Handle Delete
     ***************************************************************************************************/
    public void handleCancelDelivery() throws Exception {
        deliveryTable.stopEditing();

        if (deliveryTable.getSelectedRowCount() < 1) {
            displayMessage(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }

        if (RConfirmUtility.confirm("Delivery Delete Confirmation", FulfillmentOrderMessageText.DELIVERY_DELETE_CONFIRM)) {
            try {
                List<IMEIFulfillmentOrderDeliveryWrapper> wrappers = deliveryTable.getAllSelectedRowData();
                for (IMEIFulfillmentOrderDeliveryWrapper wrapper : wrappers) {
                    if (wrapper.getStatus() != FulfillmentOrderDeliveryStatus.IN_PROGRESS) {
                        displayWarning(FulfillmentOrderMessageText.INVALID_DELIVERY_STATUS_TO_DELETE);
                        return;
                    }
                }
                model.cancelDeliveries(wrappers);
                populateScreen();
            } catch (Throwable exception) {
                displayException(exception);
            }
        }
    }

    /****************************************************************************************************
     * Handle Notes
     ***************************************************************************************************/

    public void handleNotes() {
        try {
            NotesDialog dialog = new NotesDialog();
            dialog.setTitle("Customer Order Notes");
            dialog.loadNotes(FunctionalArea.CUSTOMER_ORDER, model.getFulfillmentOrderId(), model.isNotesEditable());
            dialog.setVisible(true);
        } catch (Exception exception) {
            displayException(exception);
        }
    }

    public boolean isCancelFunctionAvailable() {
        return model.isDeleteFunctionAvailable();
    }

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(FULFILL_ORDER_DELIVERY_SELECTED)) {
                doDeliverySelected();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doDeliverySelected() throws Exception {
        IMEIFulfillmentOrderDeliveryWrapper wrapper = (IMEIFulfillmentOrderDeliveryWrapper) deliveryTable.getSelectedRowData();
        if (wrapper != null) {
            model.storeDelivery(wrapper);
            navigate(SimScreenName.FULFILLMENT_ORDER_DELIVERY_DETAIL_SCREEN);
        }
    }

    /****************************************************************************************************
     * Customer Order Delivery List Table Definition
     ***************************************************************************************************/

    private class FulfillmentOrderDeliveryListDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return IMEIFulfillmentOrderDeliveryWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("createDate", false));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(4);
            attributes.add(new SimTableAttribute("Delivery ID", "deliveryId", new GenericIdDisplayer()));
            attributes.add(new SimTableAttribute("Status", "status", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("User", "createUser"));
            attributes.add(new SimTableAttribute("Create Date", "createDate", new MediumDateTimeDisplayer()));
            return attributes;
        }
    }

}
