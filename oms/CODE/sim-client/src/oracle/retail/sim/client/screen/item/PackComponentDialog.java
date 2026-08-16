package oracle.retail.sim.client.screen.item;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
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
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemDetailVO;

/********************************************************************************************************
 * Component Item Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class PackComponentDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 1672711275253438159L;

    private PackComponentModel model = new PackComponentModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RLongTextFieldEditor itemDescEditor = new RLongTextFieldEditor("Item Description");
    private RDisplayLabelEditor packTypeEditor = new RDisplayLabelEditor("Type");
    private RCheckBoxEditor rangedEditor = new RCheckBoxEditor("Ranged");

    private SimTable packComponentTable = new SimTable(new PackComponentDefinition());
    private SimTablePane packComponentPane = new SimTablePane(packComponentTable);

    private RButton closeButton = new RButton(SimNavigation.DIALOG_CLOSE);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/

    public PackComponentDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Component Info");
        setSize(750, 400);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        packTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        itemDescEditor.setEnabled(true, false);
        rangedEditor.setEnabled(true, false);

        closeButton.registerAction(this, SimNavigation.DIALOG_CLOSE);

        packComponentTable.setColumnSize("availableStockOnHand", EditorConstants.COLUMN_LABEL_WIDTH);
        packComponentTable.setColumnSize("componentQuantity", EditorConstants.COLUMN_LABEL_WIDTH);
    }

    private void layoutContent() {
        addButton(closeButton);

        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.setLineBorder(1);
        headerPanel.add(itemEditor, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 5, 0, 5));
        headerPanel.add(itemDescEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 0, 5, 0, 5));
        headerPanel.add(packTypeEditor, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 3, 0, 5, 0, 5));
        headerPanel.add(rangedEditor, GridTool.constraints(3, 0, 1, 1, 0, 0, 0, 3, 0, 5, 0, 5));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 5, 5, 5));
        mainPanel.add(packComponentPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Load The Dialog
     ***************************************************************************************************/

    public void setItem(ItemDetailVO itemDetailVO) throws Exception {
        itemEditor.setData(itemDetailVO.getId());
        if (SimConfigManager.isItemShortDescription()) {
            itemDescEditor.setText(itemDetailVO.getShortDescription());
        } else {
            itemDescEditor.setText(itemDetailVO.getLongDescription());
        }
        rangedEditor.setSelected(itemDetailVO.isRanged());
        packTypeEditor.setData(itemDetailVO.getItemType().toString());
        packComponentTable.setRows(model.findStockItems(itemDetailVO));
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
     * Pack Item Table Definition
     ***************************************************************************************************/

    private class PackComponentDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return PackComponentWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(5);
            attributes.add(new SimTableAttribute("Item ID", "id"));
            attributes.add(new SimTableAttribute("Description", "description"));
            attributes.add(new SimTableAttribute("UPC", "uPC"));
            attributes.add(new SimTableAttribute("Avail SOH", "availableStockOnHand"));
            attributes.add(new SimTableAttribute("# of items in pack", "componentQuantity"));
            return attributes;
        }
    }
}
