package oracle.retail.sim.client.screen.item;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.PackHeaderVO;

/********************************************************************************************************
 * Component Item Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class PackHeaderDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 4549094270763602263L;

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RDisplayLabelEditor itemDescEditor = new RDisplayLabelEditor("Item Description");
    private RCheckBoxEditor rangedEditor = new RCheckBoxEditor("Ranged");

    private SimTable packHeaderTable = new SimTable(new PackHeaderDefinition());
    private SimTablePane packHeaderPane = new SimTablePane(packHeaderTable);

    private RButton closeButton = new RButton(SimNavigation.DIALOG_CLOSE);

    public PackHeaderDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Pack Info");
        setSize(900, 400);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        rangedEditor.setEnabled(true, false);

        closeButton.registerAction(this, SimNavigation.DIALOG_CLOSE);

        packHeaderTable.setColumnSize("componentCount", SimTable.LABEL_WIDTH);
        packHeaderTable.setColumnSize("orderedQty", SimTable.LABEL_WIDTH);
        packHeaderTable.setColumnSize("inTransitQty", SimTable.LABEL_WIDTH);
        packHeaderTable.setColumnSize("transferReservedQty", SimTable.LABEL_WIDTH);
        packHeaderTable.setColumnSize("vendorReturnQty", SimTable.LABEL_WIDTH);
        packHeaderTable.setColumnSize("customerReservedQty", SimTable.LABEL_WIDTH);
    }

    private void layoutContent() {
        addButton(closeButton);

        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.setLineBorder(1);
        headerPanel.add(itemEditor, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 5, 0, 5));
        headerPanel.add(itemDescEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 0, 5, 0, 5));
        headerPanel.add(rangedEditor, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 3, 0, 5, 0, 5));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 5, 5, 5));
        mainPanel.add(packHeaderPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Load The Dialog
     ***************************************************************************************************/

    public void setItem(ItemDetailVO detailVO) {
        itemEditor.setData(detailVO.getId());
        if (SimConfigManager.isItemShortDescription()) {
            itemDescEditor.setData(detailVO.getShortDescription());
        } else {
            itemDescEditor.setData(detailVO.getLongDescription());
        }
        rangedEditor.setSelected(true);
    }

    public void setPackHeaders(List<PackHeaderVO> packHeaders) {
        packHeaderTable.setRows(packHeaders);
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
     * Close Action
     ***************************************************************************************************/
    private void doClose() {
        closeWindow();
    }

    /****************************************************************************************************
     * Component Item Table Definition
     ***************************************************************************************************/

    private class PackHeaderDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return PackHeaderVO.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(9);
            attributes.add(new SimTableAttribute("Pack Item ID", "itemId"));
            if (SimConfigManager.isItemShortDescription()) {
                attributes.add(new SimTableAttribute("Pack Item Description", "shortDescription"));
            } else {
                attributes.add(new SimTableAttribute("Pack Item Description", "longDescription"));
            }
            attributes.add(new SimTableAttribute("Type", "itemTypeDescription", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Qty in Pack", "componentCount"));
            attributes.add(new SimTableAttribute("Ordered Qty", "orderedQty"));
            attributes.add(new SimTableAttribute("In Transit", "inTransitQty"));
            attributes.add(new SimTableAttribute("Transfer Reserved", "transferReservedQty"));
            attributes.add(new SimTableAttribute("RTV Reserved", "vendorReturnQty"));
            attributes.add(new SimTableAttribute("Customer Order", "customerReservedQty"));
            return attributes;
        }
    }
}
