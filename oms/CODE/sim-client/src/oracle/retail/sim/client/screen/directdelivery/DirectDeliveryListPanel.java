package oracle.retail.sim.client.screen.directdelivery;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.displayer.PurchaseOrderIdDisplayer;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.directdelivery.DirectDelivery;
import oracle.retail.sim.common.directdelivery.DirectDeliveryMessageText;
import oracle.retail.sim.common.directdelivery.DirectDeliveryProperty;
import oracle.retail.sim.common.directdelivery.DirectDeliveryVO;
import oracle.retail.sim.common.report.ReportMessageText;

/********************************************************************************************************
 * Direct Delivery Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class DirectDeliveryListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -8391950937940994799L;

    private static final String DELIVERY_SELECTED = "Delivery.selected";
    private static final String DIRECT_DELIVERY_FILTER_SELECTED = "Delivery.filterSelected";

    private DirectDeliveryListModel model = new DirectDeliveryListModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    private DirectDeliveryFilterDialog filterDialog = new DirectDeliveryFilterDialog();

    private SimTable deliveryTable = new SimTable(new DeliveryListDefinition());
    private SimTablePane deliveryPane = new SimTablePane(deliveryTable);

    public DirectDeliveryListPanel() {
        initializeTable();
        layoutScreen();
    }

    private void initializeTable() {
        filterEditor.registerAction(this, DIRECT_DELIVERY_FILTER_SELECTED);

        filterDialog.addREventListener(this);

        deliveryTable.setTableEditable(false);
        deliveryTable.registerDoubleClickAction(this, DELIVERY_SELECTED);
    }

    private void layoutScreen() {
        REditorPanel topPanel = new REditorPanel(1);
        topPanel.add(filterEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(topPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(deliveryPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return deliveryTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        populateScreen();
    }

    public void resume() throws Exception {
        if (model.isCanceled()) {
            return;
        }
        populateScreen();
    }

    public void stop() {
        model.clearState();
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/

    public void handleRefresh() throws Exception {
        clearScreen();
        populateScreen();
    }

    public void handlePrint() throws Exception {
        List<DirectDeliveryVO> deliveryVOs = deliveryTable.getAllSelectedRowData();
        if (deliveryVOs.isEmpty()) {
            throw new BusinessException(ReportMessageText.NO_ROWS_SELECTED_PRINT);
        }
        model.printDeliveries(deliveryVOs);
    }

    /****************************************************************************************************
     * Helper Methods
     ***************************************************************************************************/

    private void clearScreen() {
        deliveryTable.clearRows();
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(DELIVERY_SELECTED)) {
                doDeliverySelected();
            } else if (command.equals(DIRECT_DELIVERY_FILTER_SELECTED)) {
                doDeliveryFilterSelected();
            } else if (command.equals(SimClientStateKey.DIRECT_DELIVERY_FILTER_MODIFIED)) {
                populateScreen();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    private void doDeliverySelected() throws Exception {
        DirectDeliveryVO deliveryVO = (DirectDeliveryVO) deliveryTable.getSelectedRowData();
        if (deliveryTable.getSelectedRowCount() != 1 || deliveryVO == null) {
            throw new BusinessException(CommonMessageText.NO_SINGLE_ROW_SELECTED);
        }
        DirectDelivery selectedDelivery = model.getDelivery(deliveryVO.getId());
        if(model.isDeliveryNew(selectedDelivery)) {
            DirectDelivery delivery = null;
            if (model.isCreateDeliveryWithAsnAllowed()) {
                List<DirectDeliveryVO> deliveryVOs = model.findOpenAsnDirectDeliveryVOs(deliveryVO.getPurchaseOrder().getId());
                if (deliveryVOs.size() > 1 && RConfirmUtility.confirm("Apply Open ASN?", DirectDeliveryMessageText.ASN_MULTIPLE_OPEN_QUESTION)) {
                    //Multiple open ASN deliveries exist for purchase order
                    model.storeOpenAsnsDeliveryVOs(deliveryVOs);
                    navigate(SimScreenName.DIRECT_DELIVERY_ASN_LIST_SCREEN);
                    return;
                }
                if (deliveryVOs.size() == 1 && RConfirmUtility.confirm("Apply Open ASN?", DirectDeliveryMessageText.ASN_SINGLE_OPEN_QUESTION)) {
                    //Single open ASN delivery exists for purchase order
                    DirectDeliveryVO singleDeliveryVO = deliveryVOs.get(0);
                    //Do not open delivery if lock not obtained
                    if (!model.obtainLock(singleDeliveryVO)) {
                        return;
                    }
                    delivery = model.prepareAsnForPurchaseOrder(singleDeliveryVO.getId());
                }
            }
            if (delivery == null) {
                if (!model.isCreateDeliveryWithoutAsnAllowed()) {
                    //Not allowed to create delivery without an ASN
                    throw new BusinessException(DirectDeliveryMessageText.PO_NO_OPEN_ASNS);
                }
                //Create new delivery for purchase order
                delivery = model.createDeliveryForPurchaseOrder(deliveryVO.getPurchaseOrder().getId());
            }
            delivery.markInProgress();
            model.storeDelivery(delivery);
            if(delivery.getPurchaseOrder() != null)
            {
                model.obtainLock(delivery.getPurchaseOrder());
            }
            navigate(SimScreenName.DIRECT_DELIVERY_DETAIL_SCREEN);
            return;
        }
        if (!model.isDeliveryEditAllowed() || !model.isDeliveryClosed(selectedDelivery) && !model.obtainLock(selectedDelivery)) {
            model.storeViewOnly();
        } else {
            model.markNewInProgress(selectedDelivery);
        }
        model.storeDelivery(selectedDelivery);
        if(selectedDelivery.getPurchaseOrder() != null)
        {
            model.obtainLock(selectedDelivery.getPurchaseOrder());
        }
        navigate(SimScreenName.DIRECT_DELIVERY_DETAIL_SCREEN);
    }

    private void doDeliveryFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    private void populateScreen() throws Exception {
        filterEditor.setText(model.getDescriptionMap());
        deliveryTable.setRows(model.getDeliveries());
    }

    /****************************************************************************************************
     * DIRECT DELIVERY TABLE DEFINITION
     ***************************************************************************************************/

    private class DeliveryListDefinition extends SimTableDefinition {
        public Class<?> getDataClass() {
            return DirectDeliveryVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> attributes = new ArrayList<SimTableSortAttribute>();
            attributes.add(new SimTableSortAttribute(DirectDeliveryProperty.PURCHASE_ORDER));
            attributes.add(new SimTableSortAttribute(DirectDeliveryProperty.ID));
            return attributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>();
            attributes.add(new SimTableAttribute("ID", DirectDeliveryProperty.ID));
            attributes.add(new SimTableAttribute("PO", DirectDeliveryProperty.PURCHASE_ORDER, new PurchaseOrderIdDisplayer()));
            if (model.isInvoiceEntryEnabled()) {
                attributes.add(new SimTableAttribute("Invoice Number", DirectDeliveryProperty.INVOICE_NUMBER));
            }
            attributes.add(new SimTableAttribute("Supplier", DirectDeliveryProperty.SUPPLIER, new IdNameDisplayer()));
            attributes.add(new SimTableAttribute("Create Date", DirectDeliveryProperty.CREATE_DATE));
            attributes.add(new SimTableAttribute("Status", DirectDeliveryProperty.STATUS, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("SKUs Received", DirectDeliveryProperty.NUMBER_OF_LINE_ITEMS_RECEIVED));
            attributes.add(new SimTableAttribute("Customer Order", DirectDeliveryProperty.FULFILLMENT_ORDER_RELATED, new BooleanDisplayer()));
            attributes.add(new SimTableAttribute("User", DirectDeliveryProperty.USER_ID));
            return attributes;
        }
    }
}
