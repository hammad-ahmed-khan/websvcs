package oracle.retail.sim.client.screen.directdelivery;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.displayer.SimMoneyDisplayer;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.tableeditor.SimMoneyTableEditor;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.directdelivery.DirectDelivery;
import oracle.retail.sim.common.directdelivery.DirectDeliveryMessageText;
import oracle.retail.sim.common.directdelivery.DirectDeliveryVO;
import oracle.retail.sim.common.directdelivery.PurchaseOrder;
import oracle.retail.sim.common.directdelivery.PurchaseOrderProperty;

/********************************************************************************************************
 * Purchase Order Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class PurchaseOrderDetailPanel extends ScreenPanel {
    private static final long serialVersionUID = -7715020932528904694L;

    private PurchaseOrderDetailModel model = new PurchaseOrderDetailModel();

    private RDisplayLabelEditor supplierEditor = new RDisplayLabelEditor("Supplier");
    private RDisplayLabelEditor purchaseOrderEditor = new RDisplayLabelEditor("Purchase Order");
    private RDisplayLabelEditor statusEditor = new RDisplayLabelEditor("Status");
    private RDisplayLabelEditor createDateEditor = new RDisplayLabelEditor("Create Date");
    private RDisplayLabelEditor notBeforeDateEditor = new RDisplayLabelEditor("Not Before Date");
    private RDisplayLabelEditor notAfterDateEditor = new RDisplayLabelEditor("Not After Date");
    private RDisplayLabelEditor customerOrderEditor = new RDisplayLabelEditor("Customer Order");
    private RDisplayLabelEditor fulfillmentOrderEditor = new RDisplayLabelEditor("Fulfillment Order");
    private RDisplayLabelEditor expectedCasesEditor = new RDisplayLabelEditor("Expected Cases");
    private RDisplayLabelEditor receivedCasesEditor = new RDisplayLabelEditor("Received Cases");
    private RDisplayLabelEditor orderedCasesEditor = new RDisplayLabelEditor("Ordered Cases");
    private RLongTextFieldEditor commentsEditor = new RLongTextFieldEditor("Comments");

    private SimTable lineItemTable = new SimTable(new PurchaseOrderItemDefinition());

    public PurchaseOrderDetailPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        supplierEditor.setDisplayer(new IdNameDisplayer());
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        createDateEditor.setDataType(DataTypeConstants.DATE_SHORT);
        notBeforeDateEditor.setDataType(DataTypeConstants.DATE_SHORT);
        notAfterDateEditor.setDataType(DataTypeConstants.DATE_SHORT);
        expectedCasesEditor.setDataType(DataTypeConstants.INTEGER);
        receivedCasesEditor.setDataType(DataTypeConstants.INTEGER);
        orderedCasesEditor.setDataType(DataTypeConstants.INTEGER);
        commentsEditor.setIdentifier(SimName.PURCHASE_ORDER_COMMENT);
        commentsEditor.setEnabled(true, false);
    }

    private void layoutPanel() {
        REditorPanel headerPanel = new REditorPanel(4, 3);
        headerPanel.add(supplierEditor);
        headerPanel.add(purchaseOrderEditor);
        headerPanel.add(customerOrderEditor);
        headerPanel.add(fulfillmentOrderEditor);
        headerPanel.add(createDateEditor);
        headerPanel.add(notBeforeDateEditor);
        headerPanel.add(notAfterDateEditor);
        headerPanel.add(new JLabel());
        headerPanel.add(statusEditor);
        headerPanel.add(expectedCasesEditor);
        headerPanel.add(receivedCasesEditor);
        headerPanel.add(orderedCasesEditor);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);
        SimTablePane lineItemPane = new SimTablePane(lineItemTable);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(commentsEditor, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 2, 5, 0));
        mainPanel.add(divider, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(lineItemPane, GridTool.constraints(0, 3, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return lineItemTable;
    }

    /****************************************************************************************************
     * State Methods for Screen
     ***************************************************************************************************/

    public boolean isCreateDeliveryAllowed() {
        return model.isCreateDeliveryAllowed();
    }

    /****************************************************************************************************
     * Populate Panel
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadState();
        populateScreen();
    }

    public void stop() {
        model.clearState();
    }

    /****************************************************************************************************
     * Create Delivery
     ***************************************************************************************************/
    public boolean handleCreateDelivery() throws Exception {
        if (!model.obtainLock(model.getPurchaseOrder())) {
            return false;
        }

        //Prompt user for confirmation to receive against inactive suppliers
        if (model.getPurchaseOrder().getSupplier().isInactive()) {
            if (!RConfirmUtility.confirm("Inactive Supplier", CommonMessageText.SUPPLIER_INACTIVE_CONFIRM)) {
                return false;
            }
        }
        DirectDelivery delivery = null;
        if (model.isCreateDeliveryWithAsnAllowed()) {
            List<DirectDeliveryVO> deliveryVOs = model.findOpenAsnDirectDeliveryVOs();
            if (deliveryVOs.size() > 1 && RConfirmUtility.confirm("Apply Open ASN?", DirectDeliveryMessageText.ASN_MULTIPLE_OPEN_QUESTION)) {
                //Multiple open ASN deliveries exist for purchase order
                model.storeOpenAsnsDeliveryVOs(deliveryVOs);
                navigate(SimScreenName.DIRECT_DELIVERY_ASN_LIST_SCREEN);
                return true;
            }
            if (deliveryVOs.size() == 1 && RConfirmUtility.confirm("Apply Open ASN?", DirectDeliveryMessageText.ASN_SINGLE_OPEN_QUESTION)) {
                //Single open ASN delivery exists for purchase order
                DirectDeliveryVO deliveryVO = deliveryVOs.get(0);
                //Do not open delivery if lock not obtained
                if (!model.obtainLock(deliveryVO)) {
                    return false;
                }
                delivery = model.prepareAsnForPurchaseOrder(deliveryVO.getId());
            }
        }
        if (delivery == null) {
            if (!model.isCreateDeliveryWithoutAsnAllowed()) {
                //Not allowed to create delivery without an ASN
                throw new BusinessException(DirectDeliveryMessageText.DELIVERY_CREATE_NOT_ALLOWED);
            }
            //Create new delivery for purchase order
            delivery = model.createDeliveryForPurchaseOrder();
        }
        delivery.markInProgress();
        model.storeDelivery(delivery);
        navigate(SimScreenName.DIRECT_DELIVERY_DETAIL_SCREEN);
        return true;
    }

    /****************************************************************************************************
     * Helper Methods
     ***************************************************************************************************/

    private void populateScreen() throws Exception {
        PurchaseOrder purchaseOrder = model.getPurchaseOrder();
        supplierEditor.setData(purchaseOrder.getSupplier());
        purchaseOrderEditor.setData(purchaseOrder.getExternalId());
        statusEditor.setData(purchaseOrder.getStatus());
        createDateEditor.setData(purchaseOrder.getCreateDate());
        notBeforeDateEditor.setData(purchaseOrder.getNotBeforeDate());
        notAfterDateEditor.setData(purchaseOrder.getNotAfterDate());
        customerOrderEditor.setData(purchaseOrder.getCustomerOrderId());
        fulfillmentOrderEditor.setData(purchaseOrder.getFulfillmentOrderExternalId());
        expectedCasesEditor.setData(purchaseOrder.getNumberOfCasesExpected().intValue());
        receivedCasesEditor.setData(purchaseOrder.getNumberOfCasesReceived().intValue());
        orderedCasesEditor.setData(purchaseOrder.getNumberOfCasesOrdered().intValue());
        commentsEditor.setText(purchaseOrder.getComments());

        lineItemTable.setRows(model.getLineItemWrappers());
    }

    /****************************************************************************************************
     * PURCHASE ORDER LINE ITEM TABLE DEFINITION
     ***************************************************************************************************/

    private class PurchaseOrderItemDefinition extends SimTableDefinition {
        public Class<?> getDataClass() {
            return PurchaseOrderLineItemWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>();
            attributes.add(new SimTableAttribute("Item", PurchaseOrderProperty.SUPPLIER_ITEM, new AttributeDisplayer(PurchaseOrderProperty.ITEM_ID)));
            attributes.add(new SimTableAttribute("Item Description", PurchaseOrderProperty.STOCK_ITEM_DESCRIPTION, false));
            attributes.add(new SimTableAttribute("UOM", PurchaseOrderProperty.UOM_MODE, new UomModeDisplayer(), new UomModeTableEditor(true)));
            attributes.add(new SimTableAttribute("Pack Size", PurchaseOrderProperty.CASE_SIZE, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("On Order", PurchaseOrderProperty.QUANTITY_ORDERED_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("Expected", PurchaseOrderProperty.QUANTITY_EXPECTED_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("Received", PurchaseOrderProperty.QUANTITY_RECEIVED_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            if (SimConfigManager.getBoolean(SimConfigManager.DISPLAY_UNIT_COST_FOR_DIRECT_DELIVERIES)) {
                attributes.add(new SimTableAttribute("Unit Cost", PurchaseOrderProperty.UNIT_COST, new SimMoneyDisplayer(), new SimMoneyTableEditor(true)));
            }
            return attributes;
        }
    }
}
