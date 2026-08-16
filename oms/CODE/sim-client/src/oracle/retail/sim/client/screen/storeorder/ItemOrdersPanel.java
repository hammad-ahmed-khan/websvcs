package oracle.retail.sim.client.screen.storeorder;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.RHeaderPanel;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;

/********************************************************************************************************
 * Item Orders Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemOrdersPanel extends ScreenPanel {
    private static final long serialVersionUID = -4347872774253769641L;

    private ItemOrdersModel model = new ItemOrdersModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private SimTable itemOrdersTable = new SimTable(new ItemOrdersDefinition());
    private SimTablePane itemOrdersPane = new SimTablePane(itemOrdersTable);

    public ItemOrdersPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        itemOrdersTable.setTableEditable(false);
        itemOrdersTable.setSingleRowSelectionMode();
    }

    private void layoutScreen() {
        RHeaderPanel headerPanel = new RHeaderPanel(1);
        headerPanel.add(itemEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(itemOrdersPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return itemOrdersTable;
    }

    public void start() {
        try {
            model.loadLineItem();
            itemEditor.setData(model.getLineItem().getOrderItem().getId());
            itemOrdersTable.setRows(model.findStoreOrders());
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Pack Item Table Definition
     ***************************************************************************************************/

    private class ItemOrdersDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return StoreOrderWrapper.class;
        }

        public List getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("notBeforeDate"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>();
            attributes.add(new SimTableAttribute("Order ID", "storeOrderNumber"));
            attributes.add(new SimTableAttribute("Source", "fromLocation.name"));
            attributes.add(new SimTableAttribute("Date", "creationDate"));
            attributes.add(new SimTableAttribute("Status", "statusDescription"));
            attributes.add(new SimTableAttribute("Not Before Date", "notBeforeDate"));
            attributes.add(new SimTableAttribute("Not After Date", "notAfterDate"));
            attributes.add(new SimTableAttribute("Qty", "quantity"));
            return attributes;
        }
    }
}
