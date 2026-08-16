package oracle.retail.sim.client.screen.item;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.MediumDateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
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
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.itemprice.PriceInfo;

/********************************************************************************************************
 * Component Item Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class PriceInformationDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -2780352389494715857L;

    private PriceInformationModel model = new PriceInformationModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RLongTextFieldEditor itemDescEditor = new RLongTextFieldEditor("Item Description");
    private RCheckBoxEditor rangedEditor = new RCheckBoxEditor("Ranged");

    private SimTable priceTable = new SimTable(new PriceInformationDefinition());
    private SimTablePane pricePane = new SimTablePane(priceTable);

    private RButton closeButton = new RButton(SimNavigation.DIALOG_CLOSE);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/

    public PriceInformationDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Price Information");
        setSize(850, 400);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        itemDescEditor.setEnabled(true, false);
        rangedEditor.setEnabled(true, false);
        closeButton.registerAction(this, SimNavigation.DIALOG_CLOSE);

        priceTable.setColumnSize("multiUnitType", EditorConstants.COLUMN_LABEL_WIDTH);
        priceTable.setColumnSize("multiUnitRetail", EditorConstants.COLUMN_LABEL_WIDTH);
        priceTable.setColumnSize("multiUnits", EditorConstants.COLUMN_LABEL_WIDTH);
        priceTable.setColumnSize("multiSellingUOM", EditorConstants.COLUMN_LABEL_WIDTH);
    }

    private void layoutContent() {
        addButton(closeButton);

        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.setLineBorder(1);
        headerPanel.add(itemEditor, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 5, 0, 5));
        headerPanel.add(itemDescEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 0, 5, 0, 5));
        headerPanel.add(rangedEditor, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 3, 0, 5, 0, 5));

        RPanel mainPanel = new RPanel(new BorderLayout(0, 10));
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(pricePane, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Load Items
     ***************************************************************************************************/

    public void setItem(ItemDetailVO itemDetailVO) throws Exception {
        itemEditor.setData(itemDetailVO.getId());
        if (SimConfigManager.isItemShortDescription()) {
            itemDescEditor.setText(itemDetailVO.getShortDescription());
        } else {
            itemDescEditor.setText(itemDetailVO.getLongDescription());
        }
        rangedEditor.setSelected(itemDetailVO.isRanged());
        priceTable.setRows(model.getPriceHistory(itemDetailVO));
    }
    
    public void stopEditing() {
        priceTable.stopEditing();
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
     * Price Information Table Definition
     ***************************************************************************************************/

    private class PriceInformationDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return PriceInfo.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("effectiveDate", false));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(7);
            attributes.add(new SimTableAttribute("Price", "price"));
            attributes.add(new SimTableAttribute("Effective Date", "effectiveDate", new MediumDateTimeDisplayer(), null));
            //  attributes.add(new SimTableAttribute("End Date", "endDate", new MediumDateTimeDisplayer(), null));
            attributes.add(new SimTableAttribute("Pricing Type", "priceType", new TranslatedObjectDisplayer(), null));
            attributes.add(new SimTableAttribute("Multi Unit Price Change", "multiUnitType", new BooleanDisplayer(), null));
            attributes.add(new SimTableAttribute("Multi Unit Price", "multiUnitPrice"));
            attributes.add(new SimTableAttribute("Multi Unit Quantity", "multiUnits", new QuantityDisplayer(), null));
            attributes.add(new SimTableAttribute("Multi Unit UOM", "multiSellingUOM", new TranslatedObjectDisplayer(), null));
            attributes.add(new SimTableAttribute("Update Date", "updateDate", new MediumDateTimeDisplayer(), null));

            return attributes;
        }
    }
}
