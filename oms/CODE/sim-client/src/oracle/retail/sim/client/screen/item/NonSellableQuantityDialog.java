package oracle.retail.sim.client.screen.item;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.EstimatedQuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.item.ItemDetailVO;

/********************************************************************************************************
 * This dialog handles entering displaying the non sellable quantities of an item
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class NonSellableQuantityDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 1307422880513116690L;

    private NonSellableQuantityDialogModel model = new NonSellableQuantityDialogModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RDisplayLabelEditor itemDescEditor = new RDisplayLabelEditor("Item Description");
    private RDisplayLabelEditor nonsellableEditor = new RDisplayLabelEditor("Nonsellable");
    private RDisplayLabelEditor unitOfMeasureEditor = new RDisplayLabelEditor("UOM");

    private EstimatedQuantityDisplayer tableQuantityDisplayer = new EstimatedQuantityDisplayer(false);

    private SimTable quantityTable = new SimTable(new NonSellableQuantityTableDefinition());
    private SimTablePane quantityPane = new SimTablePane(quantityTable);

    private RButton closeButton = new RButton(SimNavigation.DIALOG_CLOSE);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public NonSellableQuantityDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Nonsellable Inventory");
        setSize(550, 420);
        initializeWidgets();
        layoutContent();
        centerWindow();
    }

    private void initializeWidgets() {
        nonsellableEditor.setDataType(DataTypeConstants.QUANTITY);
        closeButton.registerAction(this, SimNavigation.DIALOG_CLOSE);
    }

    private void layoutContent() {
        addButton(closeButton);

        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.add(itemEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 3, 0, 0, 10));
        headerPanel.add(itemDescEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 3, 0, 0, 0));
        headerPanel.add(nonsellableEditor, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 3, 0, 0, 0));
        headerPanel.add(unitOfMeasureEditor, GridTool.constraints(1, 1, 1, 1, 1, 0, 0, 3, 3, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(quantityPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);

        LayoutUtility.alignEditorsInGridBag(headerPanel);
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void setItem(ItemDetailVO item) throws Exception {
        model.setItem(item);

        nonsellableEditor.setDisplayer(new EstimatedQuantityDisplayer(model.isDisplayAsEstimate()));
        tableQuantityDisplayer.setIsEstimatedQuantity(model.isDisplayAsEstimate());

        itemEditor.setData(model.getItemId());
        itemDescEditor.setData(model.getItemDescription());
        nonsellableEditor.setData(model.getNonSellableQuantity());
        unitOfMeasureEditor.setData(model.getUnitOfMeasure());

        quantityTable.setRows(model.getQuantityWrappers());
    }

    public void stopEditing() {
        quantityTable.stopEditing();
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(SimNavigation.DIALOG_CLOSE)) {
            closeWindow();
        }
    }

    /****************************************************************************************************
     * Nonsellable Quantity Table Definition
     ***************************************************************************************************/

    private class NonSellableQuantityTableDefinition extends SimTableDefinition {
        public Class getDataClass() {
            return NonSellableQuantityWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("quantityType"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(2);
            attributes.add(new SimTableAttribute("Sub-bucket", "quantityType", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Quantity", "quantity", tableQuantityDisplayer));
            return attributes;
        }
    }
}
