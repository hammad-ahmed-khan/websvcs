package oracle.retail.sim.client.screen.fulfillmentorderreversepick;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.displayer.GenericIdDisplayer;
import oracle.retail.sim.client.swing.dialog.RDialog;
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
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderProperty;

/********************************************************************************************************
 * This dialog handles displaying multiple line items to choose from for fulfillment order.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FulfillmentOrderReversePickLineItemDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -2304892090689190752L;

    private RDisplayLabelEditor directionsLabel = new RDisplayLabelEditor();

    private SimTable lineItemTable = new SimTable(new FodLineItemTableDefinition());
    private SimTablePane lineItemPane = new SimTablePane(lineItemTable);

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    private FulfillmentOrderReversePickLineItemWrapper selectedWrapper = null;

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public FulfillmentOrderReversePickLineItemDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Select Customer Order Reverse Pick Item");
        setSize(450, 200);
        initializeWidgets();
        layoutContent();
        centerWindow();
    }

    private void initializeWidgets() {
        directionsLabel.setData(FulfillmentOrderMessageText.SELECT_DELIVERY_ITEM.getCode());

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(cancelButton);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(directionsLabel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(lineItemPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void setLineItems(List<FulfillmentOrderReversePickLineItemWrapper> lineItems) {
        lineItemTable.setTableEditable(false);
        lineItemTable.setSingleRowSelectionMode();
        lineItemTable.setRows(lineItems);

        selectedWrapper = null;
    }

    public FulfillmentOrderReversePickLineItemWrapper getSelectedLineItem() {
        return selectedWrapper;
    }
    
    public void stopEditing() {
        lineItemTable.stopEditing();
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            stopEditing();
            if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
      * Apply Action
      ***************************************************************************************************/
    private void doApply() throws Exception {
        selectedWrapper = (FulfillmentOrderReversePickLineItemWrapper) lineItemTable.getSelectedRowData();
        if (selectedWrapper == null) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED);
        }
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        selectedWrapper = null;
        closeWindow();
    }

    /****************************************************************************************************
     * Table Definition
     ***************************************************************************************************/

    private class FodLineItemTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return FulfillmentOrderReversePickLineItemWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute(FulfillmentOrderProperty.SUBSTITUTE_ID));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(3);
            attributes.add(new SimTableAttribute("Item", FulfillmentOrderProperty.ITEM_ID, new GenericIdDisplayer()));
            attributes.add(new SimTableAttribute("Description", FulfillmentOrderProperty.ITEM_DESCRIPTION, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Substitute", FulfillmentOrderProperty.SUBSTITUTE_ID));
            return attributes;
        }
    }
}
