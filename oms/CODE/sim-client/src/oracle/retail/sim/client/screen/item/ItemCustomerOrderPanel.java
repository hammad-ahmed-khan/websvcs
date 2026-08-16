package oracle.retail.sim.client.screen.item;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.displayer.GenericIdDisplayer;
import oracle.retail.sim.client.swing.displayer.MediumDateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.uom.UomDisplayer;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderProperty;
import oracle.retail.sim.common.fulfillmentorder.ItemFulfillmentOrderVO;
import oracle.retail.sim.common.item.ItemMessageText;

/********************************************************************************************************
 * Item Customer Order Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemCustomerOrderPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 7286012643798636929L;

    private static final String CUST_ORDER_SELECTED = "CustomerOrder.selected";

    private ItemCustomerOrderModel model = new ItemCustomerOrderModel();

    private SimTable itemOrderTable = new SimTable(new ItemCustomerOrderTableDefinition());
    private SimTablePane itemOrderPane = new SimTablePane(itemOrderTable);

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public ItemCustomerOrderPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        itemOrderTable.setTableEditable(false);
        itemOrderTable.setSingleRowSelectionMode();
        itemOrderTable.registerDoubleClickAction(this, CUST_ORDER_SELECTED);
        itemOrderTable.setColumnSize(FulfillmentOrderProperty.PENDING_QTY, EditorConstants.COLUMN_LABEL_WIDTH);
    }

    private void layoutScreen() {
        RPanel mainPanel = new RPanel(new BorderLayout(0, 10));
        mainPanel.add(itemOrderPane, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return itemOrderTable;
    }

    /****************************************************************************************************
     * Initialize Panel
     ***************************************************************************************************/

    public void start() {
        model.loadCustomerOrders();
        try {
            populateScreen();
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    public void resume() {
        model.removeOrigin();
    }

    private void populateScreen() {
        itemOrderTable.setRows(model.getItemOrderVOs());
        if (itemOrderTable.getRowCount() < 1) {
            displayError(ItemMessageText.NO_CUSTOMER_ORDERS_FOUND);
            navigate(SimNavigation.BACK);
        }
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(CUST_ORDER_SELECTED)) {
                doCustomerOrderSelected();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doCustomerOrderSelected() throws Exception {
        ItemFulfillmentOrderVO itemOrderVO = (ItemFulfillmentOrderVO) itemOrderTable.getSelectedRowData();
        if (itemOrderVO != null) {
            model.storeCustomerOrder(itemOrderVO);
            model.storeOrigin();
            navigate(SimScreenName.FULFILLMENT_ORDER_DETAIL_SCREEN);
        }
    }

    /****************************************************************************************************
     * Item Customer Order Table Definition
     ***************************************************************************************************/

    private class ItemCustomerOrderTableDefinition extends SimTableDefinition {
        public Class<?> getDataClass() {
            return ItemFulfillmentOrderVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> sortAttributes = new ArrayList<>(2);
            sortAttributes.add(new SimTableSortAttribute(FulfillmentOrderProperty.RELEASE_DATE, true));
            sortAttributes.add(new SimTableSortAttribute(FulfillmentOrderProperty.CUSTOMER_ORDER_ID, true));
            return sortAttributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(11);
            attributes.add(new SimTableAttribute("SIM Cust Order", FulfillmentOrderProperty.FULFILL_ORDER_ID, new GenericIdDisplayer()));
            attributes.add(new SimTableAttribute("Customer Order", FulfillmentOrderProperty.CUSTOMER_ORDER_ID, new GenericIdDisplayer()));
            attributes.add(new SimTableAttribute("Fulfillment Order", FulfillmentOrderProperty.FULFILL_ORDER_EXTERNAL_ID));
            attributes.add(new SimTableAttribute("Type", FulfillmentOrderProperty.ORDER_TYPE, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Item", FulfillmentOrderProperty.ITEM_ID));
            attributes.add(new SimTableAttribute("Item Description", FulfillmentOrderProperty.ITEM_DESCRIPTION));
            attributes.add(new SimTableAttribute("UOM", FulfillmentOrderProperty.UNIT_OF_MEASURE, new UomDisplayer()));
            attributes.add(new SimTableAttribute("Quantity", FulfillmentOrderProperty.PENDING_QTY, new QuantityDisplayer()));
            attributes.add(new SimTableAttribute("Create Date", FulfillmentOrderProperty.CREATE_DATE, new MediumDateTimeDisplayer()));
            attributes.add(new SimTableAttribute("Release Date", FulfillmentOrderProperty.RELEASE_DATE, new MediumDateTimeDisplayer()));
            attributes.add(new SimTableAttribute("Comments", FulfillmentOrderProperty.COMMENTS));
            return attributes;
        }
    }
}
