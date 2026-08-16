package oracle.retail.sim.client.screen.itemticket;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.ListSelectionModel;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.MediumDateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.util.NoPrinterDefinedException;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.itemticket.ItemTicket;
import oracle.retail.sim.common.itemticket.ItemTicketMessageText;
import oracle.retail.sim.common.itemticket.ItemTicketQueryFilter;
import oracle.retail.sim.common.itemticket.ItemTicketStatus;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;
import oracle.retail.sim.common.itemticket.TicketTypeId;
import oracle.retail.sim.common.report.ReportMessageText;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.StorePrinter;

/********************************************************************************************************
 * Item Ticket List Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemTicketListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 1966860846084292469L;

    private ItemTicketListModel model = new ItemTicketListModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    private SimTable itemTicketTable = new SimTable(new ItemTicketDefinition());
    private SimTablePane itemTicketPane = new SimTablePane(itemTicketTable);

    private ItemTicketFilterDialog filterDialog = new ItemTicketFilterDialog();

    private static final String ITEM_TICKET_FILTER_SELECTED = "ItemTicket.filterSelected";

    public ItemTicketListPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        filterEditor.registerAction(this, ITEM_TICKET_FILTER_SELECTED);
        filterDialog.addREventListener(this);

        itemTicketTable.setTableEditable(false);
        itemTicketTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        itemTicketTable.registerDoubleClickAction(this, SimNavigation.CREATE);
    }

    private void layoutPanel() {
        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.add(filterEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(itemTicketPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return itemTicketTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        doItemTicketFilterSelected();
    }

    public void resume() throws Exception {
        populateScreen();
    }

    /****************************************************************************************************
     * Handle Create
     ***************************************************************************************************/

    public void handleCreate() {
        RepositoryManager.removeStateObject(SimClientStateKey.ITEM_TICKET_DETAIL);
    }

    /****************************************************************************************************
     * Handle Print Tickets
     ***************************************************************************************************/

    public void handlePrintTickets() throws Exception {
        if (itemTicketTable.getSelectedRowCount() == 0) {
            displayError(ReportMessageText.NO_ROWS_SELECTED_PRINT);
            return;
        }

        Set<String> ticketTypeSet = getTicketTypeSet();
        if (ticketTypeSet.isEmpty()) {
            displayError(ItemMessageText.ITEM_TICKET_NO_FORMAT);
            return;
        }

        if (ticketTypeSet.size() == 1) {
            printTickets();
            return;
        }

        if (RConfirmUtility.confirm("Item Ticket Print Confirmation", ReportMessageText.ITEM_TICKET_DIFFERENT_TYPES)) {
            //all tickets selected will be sent to the same printer
            printTickets();
        }
    }

    private Set<String> getTicketTypeSet() {
        Set<String> ticketTypeSet = new HashSet<>();
        List<ItemTicketWrapper> wrappers = itemTicketTable.getAllSelectedRowData();
        for (ItemTicketWrapper wrapper : wrappers) {
            TicketTypeFormat typeFormat = wrapper.getItemTicket().getTicketTypeFormat();

            if (typeFormat != null && !StringUtility.isNullOrEmpty(typeFormat.getId())) {
                ticketTypeSet.add(typeFormat.getId());
            }
        }
        return ticketTypeSet;
    }

    private void printTickets() throws Exception {
        try {
            List<ItemTicketWrapper> wrappers = itemTicketTable.getAllSelectedRowData();
            //all selected tickets will be sent to the selected printer
            StorePrinter printerSelected = null;
            try {
                printerSelected = model.selectPrinter(wrappers.get(0).getItemTicket());
            } catch (NoPrinterDefinedException noPrinterException) {
                displayError(ReportMessageText.NO_STORE_PRINTERS);
            }
            if (printerSelected == null) {
                return;
            }
            ReportResponse response = model.printTickets(wrappers, printerSelected);

            if (isPrintResponseSuccess(printerSelected, response)) {
                model.markTicketSentPrint(wrappers);
                displayMessage(ItemTicketMessageText.ITEM_TICKET_PRINTED, printerSelected.getDescription());
                return;
            }
        } finally {
            itemTicketTable.setRows(model.findItemTickets());
        }
    }

    private boolean isPrintResponseSuccess(StorePrinter printerSelected, ReportResponse response) {
        if (response == null) {
            displayError(ReportMessageText.PRINTING_ERROR, printerSelected.getDescription());
            return false;
        } else if (response != null && response.isFailedState()) {
            if (response.getMessage() != null) {
                displayError(response.getMessage(), response.getMessageValue());
            } else if (response.getPrintResponse() != null) {
                displayException(new Exception(response.getPrintResponse()));
            }
            return false;
        }
        return true;
    }

    /****************************************************************************************************
     * Handle Stock On Hand
     ***************************************************************************************************/

    public void handleUpdateStockOnHand() throws Exception {
        if (itemTicketTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }

        String title = "Item Ticket Stock On Hand Update Confirmation";
        if (!RConfirmUtility.confirm(title, ItemTicketMessageText.UPDATE_QTY_CONFIRM)) {
            return;
        }

        List<ItemTicketWrapper> wrappers = itemTicketTable.getAllSelectedRowData();

        for (ItemTicketWrapper wrapper : wrappers) {
            ItemTicket itemTicket = wrapper.getItemTicket();
            if (itemTicket.getExternalPoId() != null) {
                displayError(ItemTicketMessageText.ATTACHED_PO_ERROR);
                return;
            }
            TicketTypeFormat typeFormat = itemTicket.getTicketTypeFormat();
            if (typeFormat != null && !typeFormat.getTicketType().getId().equals(TicketTypeId.ITEM_TICKET_ID)) {
                displayError(ItemTicketMessageText.INVALID_FORMAT);
                return;
            }
            if (itemTicket.getStatus() != ItemTicketStatus.PENDING) {
                displayError(ItemTicketMessageText.INVALID_STATUS_SOH_UPDATE);
                return;
            }
        }

        for (ItemTicketWrapper wrapper : wrappers) {
            try {
                model.updateStockOnHand(wrapper.getItemTicket());
            } catch (BusinessException exception) {
                displayException(exception);
            }
        }

        itemTicketTable.refreshTable();
    }

    /****************************************************************************************************
     * Handle Delete
     ***************************************************************************************************/

    public void handleDelete() throws Exception {
        if (itemTicketTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }
        if (!RConfirmUtility.confirm("Delete Confirmation", ItemMessageText.ITEM_DELETE_CONFIRM)) {
            return;
        }

        List<ItemTicketWrapper> wrappers = itemTicketTable.getAllSelectedRowData();
        for (ItemTicketWrapper wrapper : wrappers) {
            if (!wrapper.getStatus().equals(ItemTicketStatus.PENDING)) {
                displayError(ItemTicketMessageText.INVALID_STATUS_FOR_DELETE);
                return;
            }
        }

        if (model.deleteTickets(itemTicketTable.getAllSelectedRowData())) {
            itemTicketTable.setRows(model.findItemTickets());
            return;
        }
        displayError(CommonMessageText.LOCK_TAKEN_OVER);
        RepositoryManager.removeStateObject(SimClientStateKey.ITEM_TICKET_FILTER);
    }

    /****************************************************************************************************
     * Handle Panel Events
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.CREATE)) {
                doItemTicketSelected();
            } else if (command.equals(ITEM_TICKET_FILTER_SELECTED)) {
                doItemTicketFilterSelected();
            } else if (command.equals(SimClientStateKey.ITEM_TICKET_FILTER_MODIFIED)) {
                populateScreen();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doItemTicketSelected() throws Exception {
        model.storeItemTicket((ItemTicketWrapper) itemTicketTable.getSelectedRowData());
        navigate(SimScreenName.ITEM_TICKET_DETAIL_SCREEN);
    }

    private void doItemTicketFilterSelected() throws Exception {
        ItemTicketQueryFilter filter = model.getFilter();
        filterDialog.setFilter(filter);
        filterDialog.setVisible(true);
    }

    private void populateScreen() throws Exception {
        filterEditor.setText(model.getDescriptionMap());
        itemTicketTable.setRows(model.findItemTickets());
    }

    /****************************************************************************************************
     * Item Ticket Definition
     ***************************************************************************************************/

    private class ItemTicketDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return ItemTicketWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("itemId"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(11);
            attributes.add(new SimTableAttribute("Item", "itemId"));
            attributes.add(new SimTableAttribute("Description", "itemDescription"));
            attributes.add(new SimTableAttribute("Label Type", "labelType", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Qty", "quantity"));
            attributes.add(new SimTableAttribute("Price", "price"));
            attributes.add(new SimTableAttribute("Multi Unit Price Change", "multiUnitPriceChange", new BooleanDisplayer()));
            attributes.add(new SimTableAttribute("Status", "status", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Format", "format", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Promotion ID", "promotionId"));
            attributes.add(new SimTableAttribute("Effective Date", "effectiveDate", new MediumDateTimeDisplayer()));
            attributes.add(new SimTableAttribute("User", "userId"));
            attributes.add(new SimTableAttribute("PO #", "externalPurchaseOrderId"));
            return attributes;
        }
    }
}
