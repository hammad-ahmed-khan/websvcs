package oracle.retail.sim.client.screen.itemprice;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.displayer.SimMoneyDisplayer;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.MediumDateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.ObjectDisplayer;
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
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.itemprice.ItemPriceMessageText;
import oracle.retail.sim.common.itemprice.ItemPriceVO;
import oracle.retail.sim.common.itemticket.ItemTicketMessageText;

/********************************************************************************************************
 * Price Change List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemPriceListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -7040008235588478463L;

    private ItemPriceListModel model = new ItemPriceListModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    private SimTable itemPriceTable = new SimTable(new ItemPriceDefinition());
    private SimTablePane itemPricePane = new SimTablePane(itemPriceTable);

    private ItemPriceFilterDialog filterDialog = new ItemPriceFilterDialog();

    private static final String FILTER_SELECTED = "Filter.selected";

    public ItemPriceListPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        filterEditor.registerAction(this, FILTER_SELECTED);

        filterDialog.addREventListener(this);

        itemPriceTable.setTableEditable(false);
        itemPriceTable.registerDoubleClickAction(this, SimClientStateKey.PRICE_CHANGE_DETAIL);
    }

    private void layoutScreen() {
        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.add(filterEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(itemPricePane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return itemPriceTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        doItemPriceFilterSelected();
    }

    /****************************************************************************************************
     * Handle Create
     ***************************************************************************************************/

    public void handleCreate() {
        model.storeNewItemPrice();
    }

    /****************************************************************************************************
     * Handle Shelf Labels
     ***************************************************************************************************/

    public void handleShelfLabels() throws Exception {
        if (itemPriceTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        if (RConfirmUtility.confirm("Shelf Label Confirmation", ItemPriceMessageText.TICKET_SHELF_LABEL_CONFIRM)) {
            model.createShelfLabels(itemPriceTable.getAllSelectedRowData());
            refreshScreen();
        }
    }

    /****************************************************************************************************
     * Handle Item Tickets Labels
     ***************************************************************************************************/

    public void handleItemTickets() throws Exception {
        if (itemPriceTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        if (RConfirmUtility.confirm("Item Ticket Confirmation", ItemTicketMessageText.CREATE_CONFIRM)) {
            model.createItemTickets(itemPriceTable.getAllSelectedRowData());
            refreshScreen();
        }
    }

    /****************************************************************************************************
     * Handle Panel Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimClientStateKey.PRICE_CHANGE_DETAIL)) {
                doItemPriceSelected();
            } else if (command.equals(FILTER_SELECTED)) {
                doItemPriceFilterSelected();
            } else if (command.equals(SimClientStateKey.PRICE_CHANGE_FILTER_MODIFIED)) {
                refreshScreen();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doItemPriceSelected() throws Exception {
        model.storeSelectedItemPrice((ItemPriceVO) itemPriceTable.getSelectedRowData());
        navigate(SimScreenName.PRICE_CHANGE_DETAIL_SCREEN);
    }

    private void doItemPriceFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    public void refreshScreen() throws Exception {
        filterEditor.setText(model.getDescriptionMap());
        itemPriceTable.setRows(model.findItemPriceVOs());
    }

    /****************************************************************************************************
     * Price Change Table Definition
     ***************************************************************************************************/

    private class ItemPriceDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return ItemPriceVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("effectiveDate"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(15);
            attributes.add(new SimTableAttribute("Effective Date", "effectiveDate", new MediumDateTimeDisplayer()));
            attributes.add(new SimTableAttribute("End Date", "endDate", new MediumDateTimeDisplayer()));
            attributes.add(new SimTableAttribute("Item", "itemId"));
            if (SimConfigManager.isItemShortDescription()) {
                attributes.add(new SimTableAttribute("Description", "shortDescription"));
            } else {
                attributes.add(new SimTableAttribute("Description", "longDescription"));
            }
            attributes.add(new SimTableAttribute("Current Price", "currentPrice", new SimMoneyDisplayer()));
            attributes.add(new SimTableAttribute("Current Selling UOM", "currentSellingUOM"));
            attributes.add(new SimTableAttribute("New Price", "price", new SimMoneyDisplayer()));
            attributes.add(new SimTableAttribute("New Selling UOM", "sellingUOM"));
            attributes.add(new SimTableAttribute("Multi Unit Price", "multiUnitPrice", new SimMoneyDisplayer()));
            attributes.add(new SimTableAttribute("Multi Unit UOM", "multiUnitUOM"));

            attributes.add(new SimTableAttribute("Multi Unit Price Change", "multiUnitPriceChange", new BooleanDisplayer()));
            attributes.add(new SimTableAttribute("Status", "status", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Promotion ID", "promotionId", new ObjectDisplayer()));
            attributes.add(new SimTableAttribute("Price Change Desc", "priceChangeDesc", new TranslatedObjectDisplayer()));
            return attributes;
        }
    }
}
