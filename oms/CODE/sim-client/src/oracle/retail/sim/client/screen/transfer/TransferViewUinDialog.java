package oracle.retail.sim.client.screen.transfer;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableCheckBoxRenderer;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.transfer.TransferSerialNumber;
import oracle.retail.sim.common.uin.UINType;

/********************************************************************************************************
 * This dialog handles entering UIN information for transaction line items.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferViewUinDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 334600726345424800L;

    private TransferViewUinDialogModel model = new TransferViewUinDialogModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RDisplayLabelEditor itemDescEditor = new RDisplayLabelEditor("Item Description");

    private SimTable uinTable = new SimTable(new TransferViewTableDefinition());
    private SimTablePane uinTablePane = new SimTablePane(uinTable);

    private RButton closeButton = new RButton(SimNavigation.DIALOG_CLOSE);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public TransferViewUinDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("UIN");
        setSize(550, 400);
        initializeWidgets();
        layoutContent();
        centerWindow();
    }

    private void initializeWidgets() {
        uinTable.setColumnSize("shipped", SimTable.LABEL_WIDTH);
        uinTable.setColumnSize("received", SimTable.LABEL_WIDTH);
        uinTable.setColumnSize("damaged", SimTable.LABEL_WIDTH);

        closeButton.registerAction(this, SimNavigation.DIALOG_CLOSE);
    }

    private void layoutContent() {
        addButton(closeButton);

        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.add(itemEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 3, 0, 0, 10));
        headerPanel.add(itemDescEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 3, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(uinTablePane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public void setLineItemWrapper(TransferLineItemWrapper wrapper) throws Exception {
        model.setLineItemWrapper(wrapper);
        itemEditor.setData(wrapper.getStockItem().getId());
        itemDescEditor.setData(wrapper.getDescription());
        uinTable.setTableEditable(false);
        uinTable.setRows(model.findTransferSerialNumbers());
    }
    
    public void stopEditing() {
        uinTable.stopEditing();
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_CLOSE)) {
                closeWindow();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * UIN Table Definition
     ***************************************************************************************************/

    private class TransferViewTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return TransferSerialNumber.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(4);
            attributes.add(new SimTableAttribute(UINType.SERIAL.toString(), "uin"));
            attributes.add(new SimTableAttribute("Dispatched", "shipped", new SimTableCheckBoxRenderer()));
            attributes.add(new SimTableAttribute("Received", "received", new SimTableCheckBoxRenderer()));
            attributes.add(new SimTableAttribute("Damaged", "damaged", new SimTableCheckBoxRenderer()));
            return attributes;
        }
    }
}
