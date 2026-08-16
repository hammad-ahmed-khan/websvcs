package oracle.retail.sim.client.screen.directdelivery;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
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
import oracle.retail.sim.common.directdelivery.DirectDeliveryVO;
import oracle.retail.sim.common.directdelivery.PurchaseOrder;
import oracle.retail.sim.common.directdelivery.PurchaseOrderProperty;
import oracle.retail.sim.common.directdelivery.PurchaseOrderVO;

/********************************************************************************************************
 * Purchase Order Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class PurchaseOrderListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -2379047144798106483L;

    private static final String PURCHASE_ORDER_SELECTED = "PurchaseOrder.selected";
    private static final String PURCHASE_ORDER_FILTER_SELECTED = "PurchaseOrder.filterSelected";

    private PurchaseOrderListModel model = new PurchaseOrderListModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    private PurchaseOrderFilterDialog filterDialog = new PurchaseOrderFilterDialog();

    private SimTable purchaseOrderTable = new SimTable(new PurchaseOrderListDefinition());
    private SimTablePane purchaseOrderPane = new SimTablePane(purchaseOrderTable);

    public PurchaseOrderListPanel() {
        initializeTable();
        layoutScreen();
    }

    private void initializeTable() {
        filterEditor.registerAction(this, PURCHASE_ORDER_FILTER_SELECTED);

        filterDialog.addREventListener(this);

        purchaseOrderTable.setTableEditable(false);
        purchaseOrderTable.setSingleRowSelectionMode();
        purchaseOrderTable.registerDoubleClickAction(this, PURCHASE_ORDER_SELECTED);
    }

    private void layoutScreen() {
        REditorPanel topPanel = new REditorPanel(1);
        topPanel.add(filterEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(topPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(purchaseOrderPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return purchaseOrderTable;
    }

    /****************************************************************************************************
     * State Methods for Screen
     ***************************************************************************************************/

    public boolean isCreateDeliveryAllowed() {
        return model.isCreateDeliveryAllowed();
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        populateScreen();
    }

    public void resume() throws Exception {
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

    public void handleCreateDelivery() throws Exception {
        PurchaseOrderVO purchaseOrderVO = (PurchaseOrderVO) purchaseOrderTable.getSelectedRowData();
        if (purchaseOrderVO == null) {
            throw new BusinessException(DirectDeliveryMessageText.PO_NONE_SELECTED);
        }
        if (model.isPurchaseOrderClosed(purchaseOrderVO)) {
            throw new BusinessException(DirectDeliveryMessageText.PO_INVALID_ENTRY);
        }

		if (!model.obtainLock(purchaseOrderVO)) {
			return;
		}

        //Prompt user for confirmation to receive against inactive suppliers
        if (purchaseOrderVO.getSupplier().isInactive()) {
            if (!RConfirmUtility.confirm("Inactive Supplier", CommonMessageText.SUPPLIER_INACTIVE_CONFIRM)) {
                return;
            }
        }
        DirectDelivery delivery = null;
        if (model.isCreateDeliveryWithAsnAllowed()) {
            List<DirectDeliveryVO> deliveryVOs = model.findOpenAsnDirectDeliveryVOs(purchaseOrderVO.getId());
            if (deliveryVOs.size() > 1 && RConfirmUtility.confirm("Apply Open ASN?", DirectDeliveryMessageText.ASN_MULTIPLE_OPEN_QUESTION)) {
                //Multiple open ASN deliveries exist for purchase order
                model.storeOpenAsnsDeliveryVOs(deliveryVOs);
                navigate(SimScreenName.DIRECT_DELIVERY_ASN_LIST_SCREEN);
                return;
            }
            if (deliveryVOs.size() == 1 && RConfirmUtility.confirm("Apply Open ASN?", DirectDeliveryMessageText.ASN_SINGLE_OPEN_QUESTION)) {
                //Single open ASN delivery exists for purchase order
                DirectDeliveryVO deliveryVO = deliveryVOs.get(0);
                //Do not open delivery if lock not obtained
                if (!model.obtainLock(deliveryVO)) {
                    return;
                }
                delivery = model.prepareAsnForPurchaseOrder(deliveryVO.getId());
            }
        }
        if (delivery == null) {
            if (!model.isCreateDeliveryWithoutAsnAllowed()) {
                //Not allowed to create delivery without an ASN
                throw new BusinessException(DirectDeliveryMessageText.PO_NO_OPEN_ASNS);
            }
            //Create new delivery for purchase order
            delivery = model.createDeliveryForPurchaseOrder(purchaseOrderVO.getId());
        }
        delivery.markInProgress();
        model.storeDelivery(delivery);
        navigate(SimScreenName.DIRECT_DELIVERY_DETAIL_SCREEN);
    }

    /****************************************************************************************************
     * Helper Methods
     ***************************************************************************************************/

    private void clearScreen() {
        purchaseOrderTable.clearRows();
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(PURCHASE_ORDER_SELECTED)) {
                doPurchaseOrderSelected();
            } else if (command.equals(PURCHASE_ORDER_FILTER_SELECTED)) {
                doPurchaseOrderFilterSelected();
            } else if (command.equals(SimClientStateKey.PURCHASE_ORDER_FILTER_MODIFIED)) {
                populateScreen();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    private void doPurchaseOrderSelected() throws Exception {
        PurchaseOrderVO purchaseOrderVO = (PurchaseOrderVO) purchaseOrderTable.getSelectedRowData();
        if (purchaseOrderTable.getSelectedRowCount() != 1 || purchaseOrderVO == null) {
            throw new BusinessException(CommonMessageText.NO_SINGLE_ROW_SELECTED);
        }
        PurchaseOrder purchaseOrder = model.getPurchaseOrder(purchaseOrderVO.getId());
        model.storePurchaseOrder(purchaseOrder);
        navigate(SimScreenName.PURCHASE_ORDER_DETAIL_SCREEN);
    }

    private void doPurchaseOrderFilterSelected() {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    private void populateScreen() throws Exception {
        filterEditor.setText(model.getDescriptionMap());
        purchaseOrderTable.setRows(model.getPurchaseOrders());
    }

    /****************************************************************************************************
     * PURCHASE ORDER TABLE DEFINITION
     ***************************************************************************************************/

    private class PurchaseOrderListDefinition extends SimTableDefinition {
        public Class<?> getDataClass() {
            return PurchaseOrderVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> attributes = new ArrayList<SimTableSortAttribute>();
            attributes.add(new SimTableSortAttribute(PurchaseOrderProperty.EXTERNAL_ID));
            return attributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>();
            attributes.add(new SimTableAttribute("ID", PurchaseOrderProperty.EXTERNAL_ID));
            attributes.add(new SimTableAttribute("Supplier", PurchaseOrderProperty.SUPPLIER, new IdNameDisplayer()));
            attributes.add(new SimTableAttribute("Create Date", PurchaseOrderProperty.CREATE_DATE));
            attributes.add(new SimTableAttribute("Status", PurchaseOrderProperty.STATUS, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Customer Order", PurchaseOrderProperty.FULFILLMENT_ORDER_RELATED, new BooleanDisplayer()));
            return attributes;
        }
    }
}
