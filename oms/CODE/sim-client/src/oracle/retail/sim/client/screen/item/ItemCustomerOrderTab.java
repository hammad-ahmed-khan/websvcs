package oracle.retail.sim.client.screen.item;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.displayer.GenericIdDisplayer;
import oracle.retail.sim.client.swing.displayer.MediumDateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.widget.SimTab;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderProperty;
import oracle.retail.sim.common.fulfillmentorder.ItemFulfillmentOrderVO;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.ItemMessageText;

/********************************************************************************************************
 * Item Customer Order Tab
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemCustomerOrderTab extends SimTab {
    private static final long serialVersionUID = -5523058434441877914L;

    private ItemCustomerOrderTabModel model = new ItemCustomerOrderTabModel();

    private SimTable itemOrderTable = new SimTable(new ItemCustomerOrderTabTableDefinition());
    private SimTablePane itemOrderPane = new SimTablePane(itemOrderTable);

    /****************************************************************************************************
     * Build Tab
     ***************************************************************************************************/

    public ItemCustomerOrderTab() {
        initializeTab();
        layoutTab();
    }

    private void initializeTab() {
        itemOrderTable.setSingleRowSelectionMode();
        itemOrderTable.setTableEditable(false);
    }

    private void layoutTab() {
        setLayout(new BorderLayout(0, 10));
        add(itemOrderPane, BorderLayout.CENTER);
    }

    /****************************************************************************************************
     * Load Tab
     ***************************************************************************************************/

    protected void loadTab(ItemDetailVO itemVO) throws Exception {
        model.setItemDetail(itemVO);
        List<ItemFulfillmentOrderVO> itemOrders = model.getItemCustomerOrderVOs();
        if (itemOrders.isEmpty()) {
            displayWarning(ItemMessageText.NO_CUSTOMER_ORDERS_FOUND);
            return;
        }
        itemOrderTable.setRows(itemOrders);
    }

    /****************************************************************************************************
     * Item Customer Order Tab Table Definition
     ***************************************************************************************************/

    private class ItemCustomerOrderTabTableDefinition extends SimTableDefinition {
        public Class getDataClass() {
            return ItemFulfillmentOrderVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute(FulfillmentOrderProperty.CUSTOMER_ORDER_ID));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(8);
            attributes.add(new SimTableAttribute("Customer Order ID", FulfillmentOrderProperty.CUSTOMER_ORDER_ID, new GenericIdDisplayer()));
            attributes.add(new SimTableAttribute("Type", FulfillmentOrderProperty.ORDER_TYPE, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Item", FulfillmentOrderProperty.ITEM_ID));
            attributes.add(new SimTableAttribute("Item Description", FulfillmentOrderProperty.ITEM_DESCRIPTION));
            attributes.add(new SimTableAttribute("UOM", FulfillmentOrderProperty.UNIT_OF_MEASURE, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Quantity", FulfillmentOrderProperty.PENDING_QTY, new QuantityDisplayer()));
            attributes.add(new SimTableAttribute("Create Date", FulfillmentOrderProperty.CREATE_DATE, new MediumDateTimeDisplayer()));
            attributes.add(new SimTableAttribute("Comments", FulfillmentOrderProperty.COMMENTS));

            return attributes;
        }
    }
}
