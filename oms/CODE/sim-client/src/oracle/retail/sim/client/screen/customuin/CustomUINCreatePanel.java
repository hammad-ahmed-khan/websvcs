package oracle.retail.sim.client.screen.customuin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.core.ClientTableFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.IntegerDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDef;
import oracle.retail.sim.client.swing.table.SimTableEditorEvent;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.tableeditor.PopupTableEditorListener;
import oracle.retail.sim.client.uom.StockItemTableEditor;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.item.StockItem;

/********************************************************************************************************
 * UIN Create Panel - Contains the visual elements of the UIN Create screen.
 * <p>
 * This is future work-in-progress code for future SIM 14.0 or later release.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CustomUINCreatePanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 7667002106326748099L;

    private CustomUINCreateModel model = new CustomUINCreateModel();

    private StockItemTableEditor stockItemTableEditor = new StockItemTableEditor();

    private SimTable stockItemTable = null;
    private SimTablePane stockItemPane = null;

    /****************************************************************************************************
     * Initialize Panel
     ***************************************************************************************************/

    public CustomUINCreatePanel() {
        initializeTables();
        initializePanel();
        layoutPanel();
    }

    private void initializeTables() {
        SimTableDef tableDef = createTableDef(CustomUINCreateWrapper.class);
        tableDef.addNotEditableAttribute(CustomUINCreateWrapper.UIN_COUNT_PROPERTY);

        tableDef.addTableAttribute(new SimTableAttribute("Item", CustomUINCreateWrapper.STOCK_ITEM_PROPERTY, new AttributeDisplayer("id"), stockItemTableEditor));
        tableDef.addTableAttribute(new SimTableAttribute("Item Description", CustomUINCreateWrapper.DESCRIPTION_PROPERTY));
        tableDef.addTableAttribute(new SimTableAttribute("UIN Qty", CustomUINCreateWrapper.UIN_COUNT_PROPERTY, new IntegerDisplayer(), new CustomUINCountTableEditor(new UINPopupListener())));

        stockItemTable = ClientTableFactory.createCustomUinTable(tableDef);
        stockItemPane = new SimTablePane(stockItemTable);
    }

    private void initializePanel() {
        stockItemTableEditor.addTableEditorListener(buildStockItemListener());

        stockItemTable.setColumnSize("serialNumberCount", SimTable.LABEL_WIDTH);
    }

    private void layoutPanel() {
        setContentPane(stockItemPane);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return stockItemTable;
    }

    public boolean isStartable() {
        return true;
    }

    public void start() throws Throwable {
        stockItemTable.addRow(new CustomUINCreateWrapper());
    }

    public void stop() {
    }

    public void performActionEvent(RActionEvent event) {
    }

    /****************************************************************************************************
     * Handle Add Item Action
     ***************************************************************************************************/

    public void doAddItem() {
        List<CustomUINCreateWrapper> wrappers = stockItemTable.getAllRowData();
        for (CustomUINCreateWrapper wrapper : wrappers) {
            if (wrapper.getStockItem() == null) {
                return;
            }
        }
        stockItemTable.addRow(new CustomUINCreateWrapper());
    }

    /****************************************************************************************************
     * Handle Delete Item Action
     ***************************************************************************************************/

    public void doRemoveItem() {
        List<CustomUINCreateWrapper> wrappers = stockItemTable.getAllSelectedRowData();
        for (CustomUINCreateWrapper wrapper : wrappers) {
            stockItemTable.removeRow(wrapper);
        }
    }

    /****************************************************************************************************
     * Handle Done Action
     ***************************************************************************************************/

    public void doHandleDone() throws Exception {
        List<CustomUINCreateWrapper> wrappers = stockItemTable.getAllRowData();
        model.saveSerialNumbers(wrappers);
    }

    /****************************************************************************************************
     * Stock Item Listener - Accessed when stock item is added to table of items.
     ***************************************************************************************************/

    private SimTableEditorListener buildStockItemListener() {
        return new SimTableEditorListener() {
            public void performTableEditorEvent(SimTableEditorEvent event) {
                Set<StockItem> stockItems = new HashSet<>();
                List<CustomUINCreateWrapper> wrappers = stockItemTable.getAllSelectedRowData();
                for (CustomUINCreateWrapper wrapper : wrappers) {
                    if (stockItems.contains(wrapper.getStockItem())) {
                        displayError(CommonMessageText.ITEM_ALREADY_EXISTS);
                        stockItemTable.removeRow(wrapper);
                        return;
                    }
                    stockItems.add(wrapper.getStockItem());
                }
            }
        };
    }

    /****************************************************************************************************
     * UIN POPUP LISTENER - Pops up when UIN column is double-clicked.
     ***************************************************************************************************/

    private class UINPopupListener implements PopupTableEditorListener {
        public void popupDialog(Object lineItem) {
            CustomUinCreateDialog dialog = new CustomUinCreateDialog();
            dialog.setWrapper((CustomUINCreateWrapper) lineItem);
            dialog.setVisible(true);
            stockItemTable.setRows(stockItemTable.getAllRowData());
        }
    }
}
