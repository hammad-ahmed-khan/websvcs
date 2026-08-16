package oracle.retail.sim.client.screen.storeorder;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.RHeaderPanel;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.displayer.DecimalDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.common.storeorder.ItemSale;

/********************************************************************************************************
 * Item Sales Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemSalesPanel extends ScreenPanel {
    private static final long serialVersionUID = 6064185108715997943L;

    private ItemSalesModel model = new ItemSalesModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private SimTable itemSalesTable = new SimTable(new ItemSalesDefinition());
    private SimTablePane itemSalesPane = new SimTablePane(itemSalesTable);

    public ItemSalesPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        itemSalesTable.setTableEditable(false);
        itemSalesTable.setSingleRowSelectionMode();
    }

    private void layoutScreen() {
        RHeaderPanel headerPanel = new RHeaderPanel(1);
        headerPanel.add(itemEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(itemSalesPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return itemSalesTable;
    }

    public void start() {
        try {
            model.loadLineItem();
            itemEditor.setData(model.getLineItem().getOrderItem().getId());
            itemSalesTable.setRows(model.findStoreSales());
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Item Sales Table Definition
     ***************************************************************************************************/

    private class ItemSalesDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return ItemSale.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("endOfWeekDate"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>();
            attributes.add(new SimTableAttribute("End of Week Date", "endOfWeekDate"));
            attributes.add(new SimTableAttribute("Qty", "quantity", new DecimalDisplayer(2)));
            attributes.add(new SimTableAttribute("Sales Value", "salesValue"));
            attributes.add(new SimTableAttribute("Sales Type", "saleTypeDescription"));
            return attributes;
        }
    }
}
