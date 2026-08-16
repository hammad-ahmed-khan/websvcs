package oracle.retail.sim.client.screen.item;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.EstimatedQuantityDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.lineitem.UOMConstants;

/********************************************************************************************************
 * Component Item Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockLocatorDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 8169978117362599919L;

    private StockLocatorModel model = new StockLocatorModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RLongTextFieldEditor itemDescEditor = new RLongTextFieldEditor("Item Description");
    private RCheckBoxEditor rangedEditor = new RCheckBoxEditor("Ranged");
    private RDisplayLabelEditor upcEditor = new RDisplayLabelEditor("Primary UPC");
    private RDisplayLabelEditor vpnEditor = new RDisplayLabelEditor("VPN");
    private RDisplayLabelEditor uomEditor = new RDisplayLabelEditor("UOM");

    private EstimatedQuantityDisplayer estimatedQuantityDisplayer = new EstimatedQuantityDisplayer(false);

    private SimTable stockTable = new SimTable(new StockLocatorDefinition());
    private SimTablePane stockPane = new SimTablePane(stockTable);

    private RButton closeButton = new RButton(SimNavigation.DIALOG_CLOSE);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/

    public StockLocatorDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Stock Locator");
        setSize(750, 400);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        itemDescEditor.setEnabled(true, false);
        rangedEditor.setEnabled(true, false);

        closeButton.registerAction(this, SimNavigation.DIALOG_CLOSE);

        stockTable.setColumnSize("availableStockOnHand", EditorConstants.COLUMN_LABEL_WIDTH);
        stockTable.setColumnSize("receivedToday", EditorConstants.COLUMN_LABEL_WIDTH);
        stockTable.setColumnSize("buddyStore", EditorConstants.COLUMN_LABEL_WIDTH);
    }

    private void layoutContent() {
        addButton(closeButton);

        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.setLineBorder(1);
        headerPanel.add(itemEditor, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 5, 0, 5));
        headerPanel.add(itemDescEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 0, 5, 0, 5));
        headerPanel.add(rangedEditor, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 3, 0, 5, 0, 5));
        headerPanel.add(upcEditor, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 3, 0, 5, 0, 5));
        headerPanel.add(vpnEditor, GridTool.constraints(1, 1, 1, 1, 0, 0, 0, 3, 0, 5, 0, 5));
        headerPanel.add(uomEditor, GridTool.constraints(2, 1, 1, 1, 0, 0, 0, 3, 0, 5, 0, 5));

        LayoutUtility.alignEditorsInGridBag(headerPanel);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 5, 5, 5));
        mainPanel.add(stockPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Load The Dialog
     ***************************************************************************************************/

    public void setItem(ItemDetailVO itemDetailVO) throws Exception {
        if (itemDetailVO == null) {
            return;
        }

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

        stockTable.setRows(model.findAvailableStock(itemDetailVO));
    }

    public void stopEditing() {
        stockTable.stopEditing();
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_CLOSE)) {
                doClose();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Done Action
     ***************************************************************************************************/
    private void doClose() {
        closeWindow();
    }

    /****************************************************************************************************
     * Stock Locator Table Definition
     ***************************************************************************************************/

    private class StockLocatorDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return StockLocatorWrapper.class;
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
