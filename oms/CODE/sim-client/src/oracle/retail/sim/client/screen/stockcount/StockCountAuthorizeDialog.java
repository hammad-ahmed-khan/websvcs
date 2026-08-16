package oracle.retail.sim.client.screen.stockcount;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.displayer.ItemIdDescriptionDisplayer;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;

/********************************************************************************************************
 * Stock Count Multiple Location Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountAuthorizeDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -1587165282763630770L;

    private StockCountAuthorizeDialogModel model = new StockCountAuthorizeDialogModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RDisplayLabelEditor totalEditor = new RDisplayLabelEditor("Total Counted Qty");
    private RDisplayLabelEditor stockOnHandEditor = new RDisplayLabelEditor("SOH");
    private RDisplayLabelEditor authorizedEditor = new RDisplayLabelEditor("Total Authorized Qty");

    private SimTable locationTable = new SimTable(new MultiLineItemDefinition());
    private SimTablePane locationPane = new SimTablePane(locationTable);

    private RButton closeButton = new RButton(SimNavigation.DIALOG_CLOSE);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public StockCountAuthorizeDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Authorization Detail");
        setSize(750, 550);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        itemEditor.setDisplayer(new ItemIdDescriptionDisplayer());
        closeButton.registerAction(this, SimNavigation.DIALOG_CLOSE);
    }

    private void layoutContent() {
        addButton(closeButton);

        REditorPanel headerPanel = new REditorPanel(2, 2);
        headerPanel.add(itemEditor);
        headerPanel.add(totalEditor);
        headerPanel.add(stockOnHandEditor);
        headerPanel.add(authorizedEditor);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(divider, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(locationPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Assign Values
     ***************************************************************************************************/

    public void setStockCount(StockCountWrapper stockCount) {
        model.setStockCount(stockCount);
    }

    public void setLineItem(StockCountLineItemWrapper lineItem) throws Exception {
        model.setLineItem(lineItem);

        List<StockCountLineItemAuthWrapper> lineItems = model.findDetailLineItems();

        itemEditor.setData(model.getStockCountItem());
        totalEditor.setData(model.getTotalCountDisplayValue(lineItems));
        stockOnHandEditor.setData(model.getStockOnHandDisplayValue());
        authorizedEditor.setData(model.getAuthorizedQuantityDisplayValue());

        resetLineItemTable();

        locationTable.setRows(lineItems);
    }

    private void resetLineItemTable() {
        locationTable = new SimTable(new MultiLineItemDefinition());
        locationTable.setColumnSize("stockCountedBasedOnUOM", EditorConstants.COLUMN_LABEL_WIDTH);
        if (model.isRecountRequired()) {
            locationTable.setColumnSize("stockRecountedBasedOnUOM", EditorConstants.COLUMN_LABEL_WIDTH);
        }
        locationPane.setTable(locationTable);
    }
    
    public void stopEditing() {
        locationTable.stopEditing();
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
     * Stock Count Line Item Display Definition
     ***************************************************************************************************/

    private class MultiLineItemDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return StockCountLineItemAuthWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("location"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(5);
            attributes.add(new SimTableAttribute("Location", "location"));
            attributes.add(new SimTableAttribute("UOM", "unitOfMeasureMode", new UomModeDisplayer(), new UomModeTableEditor()));
            attributes.add(new SimTableAttribute("Count Qty", "stockCountedBasedOnUOM", false));
            if (model.isRecountRequired()) {
                attributes.add(new SimTableAttribute("Re-Count Qty", "stockRecountedBasedOnUOM", false));
            }
            return attributes;
        }
    }
}
