package oracle.retail.sim.client.screen.item;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
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
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.uda.ItemUDAVO;

/********************************************************************************************************
 * Component Item Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemUdaDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -5752045099196226075L;

    private ItemUdaModel model = new ItemUdaModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RLongTextFieldEditor itemDescEditor = new RLongTextFieldEditor("Item Description");
    private RCheckBoxEditor rangedEditor = new RCheckBoxEditor("Ranged");

    private SimTable udaDetailTable = new SimTable(new UDATableDefinition());
    private SimTablePane udaDetailPane = new SimTablePane(udaDetailTable);

    private RButton closeButton = new RButton(SimNavigation.DIALOG_CLOSE);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/

    public ItemUdaDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("UDA Detail");
        setSize(750, 400);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        itemDescEditor.setEnabled(true, false);
        rangedEditor.setEnabled(true, false);

        closeButton.registerAction(this, SimNavigation.DIALOG_CLOSE);

        udaDetailTable.setSingleRowSelectionMode();
        udaDetailTable.setTableEditable(false);
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
        mainPanel.add(udaDetailPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Load Dialog
     ***************************************************************************************************/

    public void setItem(ItemDetailVO itemDetailVO) {
        itemEditor.setData(itemDetailVO.getId());
        if (SimConfigManager.isItemShortDescription()) {
            itemDescEditor.setText(itemDetailVO.getShortDescription());
        } else {
            itemDescEditor.setText(itemDetailVO.getLongDescription());
        }
        rangedEditor.setSelected(itemDetailVO.isRanged());
    }

    public void setUDADetail(List<ItemUDAVO> itemUDAVOs) {
        udaDetailTable.setRows(model.getUserDefinedAttributes());
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
     * Item UDA Table Definition
     ***************************************************************************************************/

    private class UDATableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return ItemUDAWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("description", true));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(2);
            attributes.add(new SimTableAttribute("UDA", "description"));
            attributes.add(new SimTableAttribute("Value", "value"));
            return attributes;
        }
    }
}
