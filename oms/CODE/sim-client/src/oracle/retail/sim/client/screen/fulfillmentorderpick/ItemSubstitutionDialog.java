package oracle.retail.sim.client.screen.fulfillmentorderpick;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.item.StockItemSearchListener;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTableEditorEvent;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.tableeditor.QuantityTableEditor;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.OrderItemEstimatedQuantityDisplayer;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickProperty;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.StockItem;

/********************************************************************************************************
 * Item Substitution Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class ItemSubstitutionDialog extends RDialog implements REventListener {

    private static final long serialVersionUID = -4578785532735355657L;

    private RDisplayLabelEditor itemInfoEditor = new RDisplayLabelEditor();
    private RDisplayLabelEditor pickQtyEditor = new RDisplayLabelEditor("Pick Quantity");
    private RDisplayLabelEditor totalQtyEditor = new RDisplayLabelEditor("Total Actual Pick Quantity");
    private RDisplayLabelEditor uomEditor = new RDisplayLabelEditor("Standard UOM");
    private RSearchFieldEditor itemEditor = SimEditorFactory.createItemSearchFieldEditor(null);

    private QuantityTableEditor actualQtyEditor = new QuantityTableEditor();

    private SimTable substituteTable = new SimTable(new CustomerOrderSubstituteItemDefinition());
    private SimTablePane substitutePane = new SimTablePane(substituteTable);

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);
    private RButton addItemButton = new RButton(SimNavigation.DIALOG_ADD_ITEM);

    private ItemSubstitutionDialogModel model = new ItemSubstitutionDialogModel();

    public ItemSubstitutionDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Item Substitution");
        setSize(850, 450);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        actualQtyEditor.addTableEditorListener(buildActualPickQuantityListener());

        addItemButton.registerAction(this, SimNavigation.DIALOG_ADD_ITEM);
        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);

        pickQtyEditor.setDisplayer(new QuantityDisplayer());
        totalQtyEditor.setDisplayer(new QuantityDisplayer());

        itemEditor.setSearchListener(buildItemSearchListener());

        //If the user cannot enter any item, hide item search editor
        if (!model.isStoreDiscretionSubstitution()) {
            itemEditor.setVisible(false);
            addItemButton.setVisible(false);
        }
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(cancelButton);

        REditorPanel itemPanel = new REditorPanel(4);
        itemPanel.add(itemInfoEditor);
        itemPanel.add(pickQtyEditor);
        itemPanel.add(totalQtyEditor);
        itemPanel.add(uomEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(itemPanel, GridTool.constraints(0, 1, 2, 1, 1, 0, 0, 1, 0, 5, 5, 5));
        mainPanel.add(itemEditor, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 1, 0, 5, 5, 5));
        mainPanel.add(addItemButton, GridTool.constraints(1, 2, 1, 1, 0, 0, 1, 0, 0, 5, 5, 5));
        mainPanel.add(substitutePane, GridTool.constraints(0, 3, 2, 1, 1, 1, 0, 3, 0, 0, 5, 0));

        setContentPane(mainPanel);
    }

    public void setWrapper(FulfillmentOrderPickLineItemWrapper wrapper) throws Exception {
        model.setWrapper(wrapper);

        pickQtyEditor.setData(wrapper.getSuggestedQuantity());
        itemInfoEditor.setData(wrapper.getItemId() + " - " + wrapper.getItemDescription());
        uomEditor.setData(wrapper.getStandardUnitOfMeasure());

        substituteTable.addRows(model.loadSubstitutes());

        refreshTotalQty();

        if (model.isViewOnly()) {
            substituteTable.setEnabled(false);
            applyButton.setVisible(false);
        } else {
            substituteTable.setEnabled(true);
            applyButton.setVisible(true);
        }
    }

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_ADD_ITEM)) {
                doAddItem();
            } else if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doAddItem() throws Exception {
        StockItem stockItem = (StockItem) itemEditor.getData();
        if (stockItem == null) {
            throw new BusinessException(ItemMessageText.ITEM_BLANK_ERROR);
        }

        //Make sure this item isn't already on the list of available substitutes
        boolean subAlreadyExists = false;
        for (ItemSubstitutionWrapper wrapper : getAllWrappers()) {
            if (wrapper.getItemId().equals(stockItem.getId())) {
                subAlreadyExists = true;
                break;
            }
        }

        //Add it to the list of available substitutes if it was not found
        if (!subAlreadyExists) {
            ItemSubstitutionWrapper wrapper = model.getItemSubstitutionWrapper(stockItem);
            model.verifyUom(wrapper);
            substituteTable.addRow(wrapper);
        }
    }

    private void doApply() throws Exception {
        model.addApplySubstituteItems(getAllWrappers());
        closeWindow();
    }

    private void doCancel() {
        closeWindow();
    }

    /****************************************************************************************************
     * Helper methods
     ***************************************************************************************************/

    private List<ItemSubstitutionWrapper> getAllWrappers() {
        return (List<ItemSubstitutionWrapper>) substituteTable.getAllRowData();
    }

    private void refreshTotalQty() {
        Quantity totalQty = Quantity.ZERO;
        for (ItemSubstitutionWrapper wrapper : getAllWrappers()) {
            totalQty = totalQty.add(wrapper.getActualQuantity());
        }
        totalQtyEditor.setData(totalQty);
    }

    /****************************************************************************************************
     * Item Search Listener - pops open the item lookup dialog
     ***************************************************************************************************/

    private StockItemSearchListener buildItemSearchListener() {
        return new StockItemSearchListener() {
            public void assignStockItem(StockItem stockItem) {
                if (stockItem != null) {
                    itemEditor.setData(stockItem);
                }
            }
        };
    }

    /****************************************************************************************************
     * Actual Pick Qty Field Listener - updates the Actual Pick Qty field at the top of the dialog
     ***************************************************************************************************/

    private SimTableEditorListener buildActualPickQuantityListener() {
        return new SimTableEditorListener() {
            public void performTableEditorEvent(SimTableEditorEvent event) {
                refreshTotalQty();
            }
        };
    }

    /****************************************************************************************************
     * ITEM DIFF TABLE DEFINITION
     ***************************************************************************************************/

    private class CustomerOrderSubstituteItemDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return ItemSubstitutionWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(10);
            attributes.add(new SimTableAttribute("Item", FulfillmentOrderPickProperty.ITEM_ID));
            attributes.add(new SimTableAttribute("Description", FulfillmentOrderPickProperty.ITEM_DESCRIPTION, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Diff1", FulfillmentOrderPickProperty.DIFF1));
            attributes.add(new SimTableAttribute("Diff2", FulfillmentOrderPickProperty.DIFF2));
            attributes.add(new SimTableAttribute("Diff3", FulfillmentOrderPickProperty.DIFF3));
            attributes.add(new SimTableAttribute("Diff4", FulfillmentOrderPickProperty.DIFF4));
            attributes.add(new SimTableAttribute("UOM", FulfillmentOrderPickProperty.UOM_MODE, new UomModeDisplayer(), new UomModeTableEditor(true)));
            attributes.add(new SimTableAttribute("Pack Size", FulfillmentOrderPickProperty.CASE_SIZE, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("Avail SOH", FulfillmentOrderPickProperty.AVAIL_SOH, new OrderItemEstimatedQuantityDisplayer()));
            attributes.add(new SimTableAttribute("Actual Pick Qty", FulfillmentOrderPickProperty.ACTUAL_QTY_UOM, new QuantityDisplayer(), actualQtyEditor));
            return attributes;
        }
    }

}
