package oracle.retail.sim.client.screen.stockcount;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.item.StockItemSearchListener;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.screen.uin.SerialNumberTableDisplayer;
import oracle.retail.sim.client.screen.uin.SerialNumberTableEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.tableeditor.LongTextTableEditor;
import oracle.retail.sim.client.swing.tableeditor.PopupTableEditorListener;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.uom.StockItemTableEditor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.stockcount.StockCountMessageText;
import oracle.retail.sim.common.stockcount.StockCountRejectedLineItem;
import oracle.retail.sim.common.stockcount.StockCountRejectedStatus;

/********************************************************************************************************
 * Stock Count Rejected Items Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountRejectedItemsPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -3038667450566432309L;

    private StockCountRejectedItemsModel model = new StockCountRejectedItemsModel();

    private RDisplayLabelEditor descriptionEditor = new RDisplayLabelEditor("Rejected Items For");
    private RSearchFieldEditor stockItemEditor = SimEditorFactory.createItemSearchFieldEditor("Item");

    private static final String ASSIGN_ALL = "Assign To All";
    private RButton assignButton = new RButton(ASSIGN_ALL);

    private StockItemTableEditor stockItemTableEditor = new StockItemTableEditor();
    private SimTable rejectedItemTable = new SimTable(new RejectedItemTableDefinition());
    private SimTablePane rejectedItemPane = new SimTablePane(rejectedItemTable);

    public StockCountRejectedItemsPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        assignButton.registerAction(this, ASSIGN_ALL);
        stockItemEditor.setSearchListener(buildItemSearchListener());
        stockItemTableEditor.setSearchListener(buildItemTableSearchListener());

        if (model.isSerialNumberProcessingEnabled()) {
            rejectedItemTable.setColumnSize("serialNumberTotal", SimTable.LABEL_WIDTH);
        }
    }

    private void layoutScreen() {
        REditorPanel topPanel = new REditorPanel(1, 3);
        topPanel.setEmptyBorder(5);
        topPanel.add(descriptionEditor);
        topPanel.add(stockItemEditor);
        topPanel.add(assignButton);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(topPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(divider, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(rejectedItemPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return rejectedItemTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() {
        descriptionEditor.setData(model.getScheduleDescription());
        rejectedItemTable.setTableEditable(model.isRejectedLineItemsEditable());
        rejectedItemTable.setRows(model.getRejectedLineItems());
    }

    /****************************************************************************************************
     * Handle Print
     ***************************************************************************************************/

    public void handlePrintAll() throws Exception {
        if (RConfirmUtility.confirm("Print All Not On File Items Confirmation", StockCountMessageText.NOF_PRINT_CONFIRM)) {
            List<RetailStoreFormatPrinter> formatPrinters = SimClientPrintUtility.selectFormatPrinter(model.getStoreId(), ReportFormat.STOCK_COUNT_NOF);
            if (formatPrinters != null && formatPrinters.size() > 0) {
                ReportResponse response = model.printRejectedLineItems(formatPrinters);
                if (response == null) {
                    return;
                }
                if (response.getMessage() != null) {
                    displayError(response.getMessage(), response.getMessageValue());
                } else if (response.getPrintResponse() != null) {
                    displayException(new Exception(response.getPrintResponse()));
                }
            }
        }
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public boolean handleSave() throws Exception {
        List<StockCountRejectedLineItem> lineItems = rejectedItemTable.getAllRowData();
        List<String> unprocessedItemIds = model.updateRejectedLineItems(lineItems);
        if (unprocessedItemIds.isEmpty()) {
            return true;
        }
        displayException(new BusinessException(StockCountMessageText.REJECTED_STATUS_ERROR, unprocessedItemIds));
        rejectedItemTable.setRows(model.reloadRejectedItems());
        return false;
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(ASSIGN_ALL)) {
                doAssignAll();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doAssignAll() throws Exception {
        StockItem stockItem = (StockItem) stockItemEditor.getData();
        if (stockItem == null) {
            return;
        }
        List<StockCountRejectedLineItem> lineItemsToProcess = new ArrayList<>();
        List<StockCountRejectedLineItem> lineItems = rejectedItemTable.getAllRowData();
        for (StockCountRejectedLineItem lineItem : lineItems) {
            if (lineItem.getStockItem() == null) {
                if (lineItem.getStatus() != StockCountRejectedStatus.ITEM_NOT_ON_COUNT) {
                    lineItemsToProcess.add(lineItem);
                }
            }
        }
        if (lineItemsToProcess.isEmpty()) {
            displayWarning(StockCountMessageText.REJECT_ALL_WARNING);
            return;
        }
        for (StockCountRejectedLineItem lineItem : lineItemsToProcess) {
            lineItem.setStockItem(stockItem);
        }
        rejectedItemTable.setRows(lineItems);
        stockItemTableEditor.setEnabled(false);
    }

    /****************************************************************************************************
     * Item Search Listener
     ***************************************************************************************************/

    private StockItemSearchListener buildItemSearchListener() {
        return new StockItemSearchListener() {
            public void assignStockItem(StockItem stockItem) {
                if (validateStockItem(stockItem)) {
                    stockItemEditor.setData(stockItem);
                }
            }
        };
    }

    /****************************************************************************************************
     * Item Table Search Listener
     ***************************************************************************************************/

    private StockItemSearchListener buildItemTableSearchListener() {
        return new StockItemSearchListener() {
            public void assignStockItem(StockItem stockItem) {
                if (validateStockItem(stockItem)) {
                    stockItemTableEditor.setData(stockItem);
                }
            }
        };
    }

    /****************************************************************************************************
     * Validate Stock Item Exists On Product Group
     ***************************************************************************************************/
    private boolean validateStockItem(StockItem stockItem) {
        try {
            if (model.isValidStockItem(stockItem)) {
                return true;
            }
            displayWarning(StockCountMessageText.REJECTED_DEPT_ERROR);
            return false;
        } catch (Throwable exception) {
            displayException(exception);
            return false;
        }
    }

    /****************************************************************************************************
     * Rejected Item Table Definition
     ***************************************************************************************************/

    private class RejectedItemTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return StockCountRejectedLineItem.class;
        }

        public List<String> getOverrideEditableAttributes() {
            return Collections.singletonList("serialNumberTotal");
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>();
            attributes.add(new SimTableAttribute("SIM Item Id", "stockItem", new AttributeDisplayer("id"), stockItemTableEditor));
            attributes.add(new SimTableAttribute("Item Description", "shortDescription"));
            attributes.add(new SimTableAttribute("Rejected Item Id", "itemId"));
            attributes.add(new SimTableAttribute("Rejected UIN", "serialNumber"));
            attributes.add(new SimTableAttribute("Status", "status", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Count Quantity", "quantity"));
            attributes.add(new SimTableAttribute("Count Location", "areaNumber"));
            attributes.add(new SimTableAttribute("Comments", "comments", new LongTextTableEditor(SimName.STOCK_COUNT_REJECTED_COMMENT)));
            attributes.add(new SimTableAttribute("User ID", "createdUser"));
            if (model.isSerialNumberProcessingEnabled()) {
                attributes.add(new SimTableAttribute("UIN Qty", "serialNumberTotal", new SerialNumberTableDisplayer(), new SerialNumberTableEditor(new UINPopupListener())));
            }
            return attributes;
        }
    }

    /****************************************************************************************************
     * UIN POPUP LISTENER
     ***************************************************************************************************/

    private class UINPopupListener implements PopupTableEditorListener {

        public void popupDialog(Object object) {
            try {
                StockCountRejectedUinDialog dialog = new StockCountRejectedUinDialog();
                dialog.setLineItemWrapper((StockCountRejectedLineItem) object);
                dialog.setVisible(true);
            } catch (Throwable exception) {
                displayException(exception);
            }
        }
    }
}
