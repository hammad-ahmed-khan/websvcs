package oracle.retail.sim.client.screen.item;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.displayer.SimMoneyDisplayer;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.DateDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.widget.SimTab;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.itemprice.PriceInfo;

/********************************************************************************************************
 * Item Price Tab
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemPriceTab extends SimTab {
    private static final long serialVersionUID = -4285452046029137468L;

    private ItemPriceTabModel model = new ItemPriceTabModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RLongTextFieldEditor itemDescEditor = new RLongTextFieldEditor("Item Description");
    private RCheckBoxEditor rangedEditor = new RCheckBoxEditor("Ranged");

    private SimTable priceTable = new SimTable(new PriceInformationDefinition());
    private SimTablePane pricePane = new SimTablePane(priceTable);

    /****************************************************************************************************
     * Build Tab
     ***************************************************************************************************/

    public ItemPriceTab() {
        initializeTab();
        layoutTab();
    }

    private void initializeTab() {
        rangedEditor.setEnabled(false);
    }

    private void layoutTab() {
        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.setLineBorder(1);
        headerPanel.add(itemEditor, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 5, 0, 0));
        headerPanel.add(itemDescEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 0, 5, 0, 0));
        headerPanel.add(rangedEditor, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 3, 0, 5, 0, 5));

        setLayout(new BorderLayout(0, 10));
        add(headerPanel, BorderLayout.NORTH);
        add(pricePane, BorderLayout.CENTER);
    }

    /****************************************************************************************************
     * Load Tab
     ***************************************************************************************************/

    public void loadTab(ItemDetailVO item) throws Exception {
        if (model.setItemDetail(item)) {
            itemEditor.setData(item.getId());
            if (SimConfigManager.isItemShortDescription()) {
                itemDescEditor.setText(item.getShortDescription());
            } else {
                itemDescEditor.setText(item.getLongDescription());
            }
            rangedEditor.setSelected(item.isRanged());
            priceTable.setRows(model.getPriceHistory());
        }
    }

    /****************************************************************************************************
     * Price Information Table Definition
     ***************************************************************************************************/

    private class PriceInformationDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return PriceInfo.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("effectiveDate", false));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(7);
            attributes.add(new SimTableAttribute("Price", "price", new SimMoneyDisplayer(), null));
            attributes.add(new SimTableAttribute("Effective Date", "effectiveDate", new DateDisplayer(), null));
            attributes.add(new SimTableAttribute("Pricing Type", "priceType", new TranslatedObjectDisplayer(), null));
            attributes.add(new SimTableAttribute("Multi Unit Price Change", "multiUnitType", new BooleanDisplayer(), null));
            attributes.add(new SimTableAttribute("Multi Unit Price", "multiUnitPrice", new SimMoneyDisplayer(), null));
            attributes.add(new SimTableAttribute("Multi Unit Quantity", "multiUnits", new QuantityDisplayer(), null));
            attributes.add(new SimTableAttribute("Multi Unit UOM", "multiSellingUOM", new TranslatedObjectDisplayer(), null));

            return attributes;
        }
    }
}
