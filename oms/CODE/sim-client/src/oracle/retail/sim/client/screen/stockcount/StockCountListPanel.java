package oracle.retail.sim.client.screen.stockcount;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ListSelectionModel;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.DateDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.stockcount.StockCountMessageText;

/********************************************************************************************************
 * Stock Count List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -2273376766655058091L;

    private StockCountListModel model = new StockCountListModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();
    private RDisplayLabelEditor totalItemEditor = new RDisplayLabelEditor("Total Items");
    private SimTable stockCountTable = new SimTable(new StockCountDefinition());
    private SimTablePane stockCountPane = new SimTablePane(stockCountTable);

    private static final String STOCK_COUNT_FILTER_SELECTED = "StockCount.filterSelected";
    private static final String STOCK_COUNT_SELECTED = "StockCount.selected";

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public StockCountListPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        filterEditor.registerAction(this, STOCK_COUNT_FILTER_SELECTED);

        totalItemEditor.setDataType(DataTypeConstants.INTEGER_LEFT);
        totalItemEditor.setSizeType(EditorConstants.SMALL);

        stockCountTable.setTableEditable(false);
        stockCountTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        stockCountTable.setColumnSize("id", SimTable.LABEL_WIDTH);
        stockCountTable.setColumnSize("itemsLeftToCount", SimTable.LABEL_WIDTH);
        stockCountTable.registerDoubleClickAction(this, STOCK_COUNT_SELECTED);
    }

    private void layoutScreen() {
        RPanel filterPanel = new RPanel(new GridBagLayout());
        filterPanel.add(filterEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        filterPanel.add(totalItemEditor, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(filterPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(stockCountPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return stockCountTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        doStockCountFilterModified();
    }

    /****************************************************************************************************
     * Handle Print Stock Count
     ***************************************************************************************************/

    public void handlePrint() throws Exception {
        if (stockCountTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        List<RetailStoreFormatPrinter> formatPrinters = model.getFormatPrinters();
        if (formatPrinters == null || formatPrinters.size() == 0) {
            return;
        }
        List<StockCountWrapper> selectedStockCounts = stockCountTable.getAllSelectedRowData();
        for (StockCountWrapper stockCount : selectedStockCounts) {
            ReportResponse response = model.printStockCount(stockCount, formatPrinters);
            if (response != null) {
                if (response.getMessage() != null) {
                    displayError(response.getMessage(), response.getMessageValue());
                } else if (response.getPrintResponse() != null) {
                    displayException(new Exception(response.getPrintResponse()));
                }
                break;
            }
        }
    }

    /****************************************************************************************************
     * Handle Refresh
     ***************************************************************************************************/

    public void handleRefresh() throws Exception {
        doStockCountFilterModified();
    }

    /****************************************************************************************************
     * Handle Delete
     ***************************************************************************************************/

    public void handleDelete() throws Exception {
        if (stockCountTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        List<StockCountWrapper> selectedStockCounts = stockCountTable.getAllSelectedRowData();
        List<StockCountWrapper> stockCountsToDelete = new ArrayList<>();
        boolean invalidCountsExist = false;
        for (StockCountWrapper stockCountWrapper : selectedStockCounts) {
            if (stockCountWrapper.isStockCountComplete()) {
                invalidCountsExist = true;
            } else if (stockCountWrapper.isStockCountDeletable()) {
                stockCountsToDelete.add(stockCountWrapper);
            } else {
                invalidCountsExist = true;
            }
        }
        if (invalidCountsExist) {
            displayWarning(StockCountMessageText.DELETE_WARNING);
        }
        if (stockCountsToDelete.isEmpty()) {
            return;
        }
        if (RConfirmUtility.confirm("Delete Confirmation", model.getDeleteConfirmMessage(stockCountsToDelete))) {
            showScreenBusy(true);
            try {
                deleteStockCounts(stockCountsToDelete);
            } finally {
                showScreenBusy(false);
                doStockCountFilterModified();
            }
        }
    }

    // Do not need to gain lock to delete stock count
    private void deleteStockCounts(List<StockCountWrapper> stockCounts) throws Exception {
        try {
            for (StockCountWrapper stockCount : stockCounts) {
                model.cancelStockCount(stockCount);
            }
        } catch (BusinessException businessException) {
            displayException(businessException);
        }
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(STOCK_COUNT_SELECTED)) {
                doStockCountSelected(event);
            } else if (command.equals(STOCK_COUNT_FILTER_SELECTED)) {
                doStockCountFilterSelected();
            } else if (command.equals(SimClientStateKey.STOCK_COUNT_FILTER_MODIFIED)) {
                doStockCountFilterModified();
            }
        } catch (Throwable exception) {
            displayException(exception);
        } finally {
            showScreenBusy(false);
        }
    }

    private void doStockCountSelected(RActionEvent event) {
        model.storeStockCount((StockCountWrapper) stockCountTable.getSelectedRowData());
        navigate(SimScreenName.STOCK_COUNT_LOCATION_SCREEN);
    }

    private void doStockCountFilterSelected() throws Exception {
        StockCountFilterDialog dialog = new StockCountFilterDialog();
        dialog.addREventListener(this);
        dialog.setFilter(model.getFilter());
        dialog.setVisible(true);
    }

    private void doStockCountFilterModified() throws Exception {
        List<StockCountWrapper> wrappers = model.findStockCounts();
        filterEditor.setText(model.getDescriptionMap());
        totalItemEditor.setData(model.calculateTotalItems(wrappers));
        stockCountTable.setRows(wrappers);
    }

    /****************************************************************************************************
     * Stock Count Table Definition
     ***************************************************************************************************/

    private class StockCountDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return StockCountWrapper.class;
        }

        public List getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("scheduledDate"));
        }

        public List getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(7);
            attributes.add(new SimTableAttribute("Count ID", "id"));
            attributes.add(new SimTableAttribute("Count Description", "countDescription"));
            attributes.add(new SimTableAttribute("Count Group", "groupDescription"));
            attributes.add(new SimTableAttribute("Date", "scheduledDate", new DateDisplayer(SimDateUtil.getGMTTimeZone())));
            attributes.add(new SimTableAttribute("Type", "phase", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Status", "displayStatus", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Items Left To Count", "itemsLeftToCount"));
            return attributes;
        }
    }
}
