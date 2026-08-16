package oracle.retail.sim.client.screen.item;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.RHeaderPanel;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.widget.SimTab;
import oracle.retail.sim.common.item.ItemDetailVO;

/********************************************************************************************************
 * Item Non-Sellable Tab
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemNonSellableTab extends SimTab {
    private static final long serialVersionUID = 7909599849991591063L;

    private ItemNonSellableTabModel model = new ItemNonSellableTabModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RDisplayLabelEditor itemDescEditor = new RDisplayLabelEditor("Item Description");
    private RDisplayLabelEditor nonsellableEditor = new RDisplayLabelEditor("Nonsellable");
    private RDisplayLabelEditor unitOfMeasureEditor = new RDisplayLabelEditor("UOM");

    private SimTable quantityTable = new SimTable(new NonSellableQuantityTableDefinition());
    private SimTablePane quantityPane = new SimTablePane(quantityTable);

    /****************************************************************************************************
     * Build Tab
     ***************************************************************************************************/

    public ItemNonSellableTab() {
        initializeTab();
        layoutTab();
    }

    private void initializeTab() {
        nonsellableEditor.setDataType(DataTypeConstants.QUANTITY);
    }

    private void layoutTab() {
        RHeaderPanel headerPanel = new RHeaderPanel(1, 4);
        headerPanel.add(itemEditor);
        headerPanel.add(itemDescEditor);
        headerPanel.add(nonsellableEditor);
        headerPanel.add(unitOfMeasureEditor);

        setLayout(new BorderLayout(0, 10));
        add(headerPanel, BorderLayout.NORTH);
        add(quantityPane, BorderLayout.CENTER);
    }

    /****************************************************************************************************
     * Load Tab
     ***************************************************************************************************/

    public void loadTab(ItemDetailVO item) throws Exception {
        if (model.setItemDetail(item)) {
            itemEditor.setData(model.getItemId());
            itemDescEditor.setData(model.getItemDescription());
            nonsellableEditor.setData(model.getNonSellableQuantity());
            unitOfMeasureEditor.setData(model.getUnitOfMeasure());
            quantityTable.setRows(model.getQuantityWrappers());
        }
    }

    /****************************************************************************************************
     * Nonsellable Quantity Table Definition
     ***************************************************************************************************/

    private class NonSellableQuantityTableDefinition extends SimTableDefinition {
        public Class getDataClass() {
            return NonSellableQuantityWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("quantityType"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(2);
            attributes.add(new SimTableAttribute("Sub-bucket", "quantityType", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Quantity", "quantity", new QuantityDisplayer()));
            return attributes;
        }
    }
}
