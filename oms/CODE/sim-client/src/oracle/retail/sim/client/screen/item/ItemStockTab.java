package oracle.retail.sim.client.screen.item;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.EstimatedQuantityDisplayer;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
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
import oracle.retail.sim.common.item.StoreItemStockVO;
import oracle.retail.sim.common.lineitem.UOMConstants;

/********************************************************************************************************
 * Item Stock Tab
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemStockTab extends SimTab {
    private static final long serialVersionUID = 2898931148303768741L;

    private ItemStockTabModel model = new ItemStockTabModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RLongTextFieldEditor itemDescEditor = new RLongTextFieldEditor("Item Description");
    private RCheckBoxEditor rangedEditor = new RCheckBoxEditor("Ranged");
    private RDisplayLabelEditor upcEditor = new RDisplayLabelEditor("Primary UPC");
    private RDisplayLabelEditor vpnEditor = new RDisplayLabelEditor("VPN");
    private RDisplayLabelEditor uomEditor = new RDisplayLabelEditor("UOM");

    private EstimatedQuantityDisplayer estimatedQuantityDisplayer = new EstimatedQuantityDisplayer(false);

    private SimTable stockTable = new SimTable(new StockLocatorDefinition());
    private SimTablePane stockPane = new SimTablePane(stockTable);

    /****************************************************************************************************
     * Build Tab
     ***************************************************************************************************/

    public ItemStockTab() {
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

        REditorPanel supplierPanel = new REditorPanel(3);
        supplierPanel.add(upcEditor);
        supplierPanel.add(vpnEditor);
        supplierPanel.add(uomEditor);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);

        setLayout(new GridBagLayout());
        add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        add(supplierPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        add(divider, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        add(stockPane, GridTool.constraints(0, 3, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
    }

    /****************************************************************************************************
     * Load Tab
     ***************************************************************************************************/

    protected void loadTab(ItemDetailVO itemDetailVO) throws Exception {
        if (model.setItemDetail(itemDetailVO)) {
            itemEditor.setData(itemDetailVO.getId());

            if (SimConfigManager.isItemShortDescription()) {
                itemDescEditor.setText(itemDetailVO.getShortDescription());
            } else {
                itemDescEditor.setText(itemDetailVO.getLongDescription());
            }

            rangedEditor.setSelected(itemDetailVO.isRanged());
            upcEditor.setData(itemDetailVO.getUPC());
            vpnEditor.setData(itemDetailVO.getVPN());

            if (UOMConstants.EACHES.equals(itemDetailVO.getUnitOfMeasure())) {
                uomEditor.setData(Translator.getText("Units"));
            } else {
                uomEditor.setData(Translator.getText(itemDetailVO.getUnitOfMeasure()));
            }

            estimatedQuantityDisplayer.setIsEstimatedQuantity(itemDetailVO.getStockItem().isInventoryEstimated());

            stockTable.setRows(model.findAvailableStock());
        }
    }

    /****************************************************************************************************
     * Stock Locator Table Definition
     ***************************************************************************************************/

    private class StockLocatorDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return StoreItemStockVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> sortAttributes = new ArrayList<>(2);
            sortAttributes.add(new SimTableSortAttribute("buddyStore", false));
            sortAttributes.add(new SimTableSortAttribute("availableStockOnHand", false));
            return sortAttributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(4);
            attributes.add(new SimTableAttribute("Store", "storeDescription"));
            attributes.add(new SimTableAttribute("Available SOH", "availableStockOnHand", estimatedQuantityDisplayer));
            attributes.add(new SimTableAttribute("Received Today", "receivedToday"));
            attributes.add(new SimTableAttribute("Buddy Store", "buddyStore", new BooleanDisplayer(), null));
            return attributes;
        }
    }
}
