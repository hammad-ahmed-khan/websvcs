package oracle.retail.sim.client.screen.stockcount;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.displayer.ItemIdDescriptionDisplayer;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
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

/********************************************************************************************************
 * Stock Count Component Detail Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountComponentDetailDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -6002540053901528580L;

    private StockCountComponentDetailModel model = new StockCountComponentDetailModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RDisplayLabelEditor totalEditor = new RDisplayLabelEditor("Total Counted Qty");
    private RDisplayLabelEditor stockOnHandEditor = new RDisplayLabelEditor("SOH");
    private RDisplayLabelEditor authorizedEditor = new RDisplayLabelEditor("Total Authorized Qty");

    private SimTable detailTable = new SimTable(new ComponentLineItemDefinition());
    private SimTablePane detailPane = new SimTablePane(detailTable);

    private RButton closeButton = new RButton(SimNavigation.DIALOG_CLOSE);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public StockCountComponentDetailDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Pack Item and Component Item Count Detail");
        setSize(775, 550);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        itemEditor.setDisplayer(new ItemIdDescriptionDisplayer());

        detailTable.setColumnSize("stockCounted", EditorConstants.COLUMN_LABEL_WIDTH);
        detailTable.setColumnSize("stockRecounted", EditorConstants.COLUMN_LABEL_WIDTH);

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
        mainPanel.add(detailPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

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

        itemEditor.setData(model.getStockCountItem());
        totalEditor.setData(model.getTotalCountDisplayValue());
        stockOnHandEditor.setData(model.getStockOnHandDisplayValue());
        authorizedEditor.setData(model.getAuthorizedQuantityDisplayValue());

        detailTable.setRows(model.findDetailLineItems());
    }

    public void stopEditing() {
        detailTable.stopEditing();
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

    private class ComponentLineItemDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return StockCountComponentDetailWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("itemId"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(7);
            attributes.add(new SimTableAttribute("Item", "itemId"));
            attributes.add(new SimTableAttribute("Description", "itemDescription"));
            attributes.add(new SimTableAttribute("Type", "itemType", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("# of components in Pack", "componentCount"));
            attributes.add(new SimTableAttribute("UOM", "unitOfMeasure"));
            attributes.add(new SimTableAttribute("Count Qty", "stockCounted"));
            attributes.add(new SimTableAttribute("Re-Count Qty", "stockRecounted"));
            return attributes;
        }
    }
}
