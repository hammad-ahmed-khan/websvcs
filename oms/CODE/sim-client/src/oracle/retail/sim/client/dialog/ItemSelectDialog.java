package oracle.retail.sim.client.dialog;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemDescription;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.OrderItem;
import oracle.retail.sim.common.item.ProductGroupItem;
import oracle.retail.sim.common.item.RetailItem;
import oracle.retail.sim.common.item.StockItem;

/********************************************************************************************************
 * Item Selection Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemSelectDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -2870549306630153665L;

    private SimTable itemTable = new SimTable(new ItemSelectDefinition());
    private SimTablePane itemPane = new SimTablePane(itemTable);

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/

    public ItemSelectDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Select Item");
        setSize(550, 200);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        itemTable.setSingleRowSelectionMode();
    }

    private void layoutContent() {
        addButton(applyButton);
        setContentPane(itemPane);
    }

    /****************************************************************************************************
     * Load The Dialog
     ***************************************************************************************************/

    public void setStockItems(List<StockItem> items) {
        itemTable.setRows(items);
    }

    public void setItemVOs(List<ItemVO> items) {
        itemTable.setRows(items);
    }

    public void setProductGroupItems(List<ProductGroupItem> items) {
        itemTable.setRows(items);
    }

    public void setRetailItems(List<RetailItem> items) {
        itemTable.setRows(items);
    }

    public void setOrderItems(List<OrderItem> items) {
        itemTable.setRows(items);
    }

    public Object getSelectedItem() {
        return itemTable.getSelectedRowData();
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doDone();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doDone() {
        if (itemTable.getSelectedRowCount() == 0) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED);
        } else {
            dispose();
        }
    }

    public void closeWindow() {
        displayWarning(CommonMessageText.NO_ROWS_SELECTED);
    }

    /****************************************************************************************************
     * Component Item Table Definition
     ***************************************************************************************************/

    private class ItemSelectDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return ItemDescription.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(2);
            attributes.add(new SimTableAttribute("ID", "id"));
            if (SimConfigManager.isItemShortDescription()) {
                attributes.add(new SimTableAttribute("Description", "shortDescription"));
            } else {
                attributes.add(new SimTableAttribute("Description", "longDescription"));
            }
            return attributes;
        }
    }
}
