package oracle.retail.sim.client.screen.warehousedelivery;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.GenericIdDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryProperty;

/********************************************************************************************************
 * Warehouse Delivery Fulfillment Order Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class WarehouseDeliveryFulfillmentOrderPanel extends ScreenPanel {
    private static final long serialVersionUID = -8690357014702172476L;

    private WarehouseDeliveryFulfillmentOrderModel model = new WarehouseDeliveryFulfillmentOrderModel();

    private SimTable deliveryOrderTable = new SimTable(new WarehouseDeliveryFulfillmentOrderDefinition());
    private SimTablePane deliveryOrderPane = new SimTablePane(deliveryOrderTable);

    public WarehouseDeliveryFulfillmentOrderPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        deliveryOrderTable.setTableEditable(false);
    }

    private void layoutScreen() {
        setContentPane(deliveryOrderPane);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return deliveryOrderTable;
    }

    public void start() {
        try {
            populateScreen();
        } catch (Throwable t) {
            displayException(t);
        }
    }

    private void populateScreen() {
        deliveryOrderTable.setRows(model.getWrappers());
    }

    /****************************************************************************************************
     * Warehouse Delivery Fulfillment Order Definition
     ***************************************************************************************************/

    private class WarehouseDeliveryFulfillmentOrderDefinition extends SimTableDefinition {
        public Class<?> getDataClass() {
            return WarehouseDeliveryFulfillmentOrderWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> attributes = new ArrayList<SimTableSortAttribute>();
            attributes.add(new SimTableSortAttribute(WarehouseDeliveryProperty.CUSTOMER_ORDER_ID));
            attributes.add(new SimTableSortAttribute(WarehouseDeliveryProperty.FULFILLMENT_ORDER_EXTERNAL_ID));
            attributes.add(new SimTableSortAttribute(WarehouseDeliveryProperty.CARTON_EXTERNAL_ID));
            return attributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>();
            attributes.add(new SimTableAttribute("Customer Order", WarehouseDeliveryProperty.CUSTOMER_ORDER_ID, new GenericIdDisplayer()));
            attributes.add(new SimTableAttribute("Fulfillment Order", WarehouseDeliveryProperty.FULFILLMENT_ORDER_EXTERNAL_ID));
            attributes.add(new SimTableAttribute("Container ID", WarehouseDeliveryProperty.CARTON_EXTERNAL_ID));
            attributes.add(new SimTableAttribute("Status", WarehouseDeliveryProperty.STATUS, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Total SKUs", WarehouseDeliveryProperty.NUMBER_OF_LINE_ITEMS));
            attributes.add(new SimTableAttribute("Comments", WarehouseDeliveryProperty.COMMENTS));
            return attributes;
        }
    }
}
