package oracle.retail.sim.client.screen.fulfillmentorderpick;

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
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickProperty;

/********************************************************************************************************
 * This dialog handles displaying multiple line items to choose from for fulfillment order pick.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FulfillmentOrderPickLineItemDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 3115909499749587812L;

    private RDisplayLabelEditor directionsLabel = new RDisplayLabelEditor();
    
    private SimTable lineItemTable = new SimTable(new FopLineItemTableDefinition());
    private SimTablePane lineItemPane = new SimTablePane(lineItemTable);

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    private FulfillmentOrderPickLineItemWrapper selectedWrapper = null;

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public FulfillmentOrderPickLineItemDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Select Customer Order Pick Item");
        setSize(450, 200);
        initializeWidgets();
        layoutContent();
        centerWindow();
    }

    private void initializeWidgets() {
        directionsLabel.setData(FulfillmentOrderMessageText.SELECT_PICK_ITEM.getCode());
        
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

    public void setLineItems(List<FulfillmentOrderPickLineItemWrapper> lineItems) {
        lineItemTable.setTableEditable(false);
        lineItemTable.setSingleRowSelectionMode();
        lineItemTable.setRows(lineItems);

        selectedWrapper = null;
    }

    public FulfillmentOrderPickLineItemWrapper getSelectedLineItem() {
        return selectedWrapper;
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
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
        selectedWrapper = (FulfillmentOrderPickLineItemWrapper) lineItemTable.getSelectedRowData();
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

    private class FopLineItemTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return FulfillmentOrderPickLineItemWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute(FulfillmentOrderPickProperty.BIN_ID));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(3);
            attributes.add(new SimTableAttribute("Item", FulfillmentOrderPickProperty.ITEM_ID, new GenericIdDisplayer()));
            attributes.add(new SimTableAttribute("Description", FulfillmentOrderPickProperty.ITEM_DESCRIPTION, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Bin ID", FulfillmentOrderPickProperty.BIN_ID));
            return attributes;
        }
    }
}
