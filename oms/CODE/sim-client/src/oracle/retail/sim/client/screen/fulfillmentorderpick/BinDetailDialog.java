package oracle.retail.sim.client.screen.fulfillmentorderpick;

import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.tableeditor.StringTableEditor;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPick;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickProperty;
import oracle.retail.sim.common.report.ReportMessageText;

/********************************************************************************************************
 * Bin Detail Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class BinDetailDialog extends RDialog implements REventListener {

    private static final long serialVersionUID = 8616782719610132354L;

    BinDetailDialogModel model = new BinDetailDialogModel();

    private SimTable binTable = new SimTable(new CustomerOrderBinDefinition());
    private SimTablePane binPane = new SimTablePane(binTable);

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton closeButton = new RButton(SimNavigation.DIALOG_CLOSE);
    private RButton printButton = new RButton(SimNavigation.DIALOG_PRINT);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    public BinDetailDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Bin Detail");
        setSize(450, 400);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        closeButton.registerAction(this, SimNavigation.DIALOG_CLOSE);
        printButton.registerAction(this, SimNavigation.DIALOG_PRINT);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);

        binTable.setMultipleRowSelectionMode();
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(closeButton);
        addButton(cancelButton);
        addButton(printButton);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(binPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));
        mainPanel.add(new JLabel(), GridTool.constraints(0, 2, 2, 1, 0, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public void setPick(FulfillmentOrderPick pick) {
        model.setPick(pick);

        if (model.isViewOnly()) {
            binTable.setRows(model.getBinMap(false).values());
            applyButton.setVisible(false);
            applyButton.setEnabled(false);
            cancelButton.setVisible(false);
            cancelButton.setEnabled(false);
            closeButton.setVisible(true);
            closeButton.setEnabled(true);
        } else {
            binTable.setRows(model.getBinMap(true).values());

            if (!model.isBinsCaptured()) {
                applyButton.setVisible(true);
                applyButton.setEnabled(true);
                cancelButton.setVisible(false);
                cancelButton.setEnabled(false);
                closeButton.setVisible(false);
                closeButton.setEnabled(false);
            } else {
                cancelButton.setVisible(true);
                cancelButton.setEnabled(true);
                closeButton.setVisible(false);
                closeButton.setEnabled(false);
                applyButton.setVisible(true);
                applyButton.setEnabled(true);
            }
        }
    }

    public void stopEditing() {
        binTable.stopEditing();
    }

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            stopEditing();
            if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.DIALOG_PRINT)) {
                doPrint();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            } else if (command.equals(SimNavigation.DIALOG_CLOSE)) {
                doCancel();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doApply() throws Exception {
        if (!model.isViewOnly()) {
            model.saveBins((List<FulfillmentOrderBinWrapper>) binTable.getAllRowData());
        }
        closeWindow();
    }

    private void doPrint() throws Exception {
        List<FulfillmentOrderBinWrapper> wrappers = (List<FulfillmentOrderBinWrapper>) binTable.getAllSelectedRowData();
        if (wrappers.isEmpty()) {
            throw new BusinessException(ReportMessageText.NO_ROWS_SELECTED_PRINT);
        }
        model.printLabels(wrappers);
    }

    private void doCancel() throws Exception {
        closeWindow();
    }

    // Override window closing to only allow closing if bins are captured (when they are required)
    public void windowClosing(WindowEvent event) {
        if (!model.isBinsManuallyEntered() || model.isBinsCaptured()) {
            super.windowClosing(event);
        }
    }

    /****************************************************************************************************
     * BIN TABLE DEFINITION
     ***************************************************************************************************/

    private class CustomerOrderBinDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return FulfillmentOrderBinWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(1);
            attributes.add(new SimTableAttribute("Bin ID", FulfillmentOrderPickProperty.BIN_ID, new TranslatedObjectDisplayer(), new StringTableEditor(SimName.CUSTOMER_ORDER_BIN_ID)));
            return attributes;
        }
    }
}
