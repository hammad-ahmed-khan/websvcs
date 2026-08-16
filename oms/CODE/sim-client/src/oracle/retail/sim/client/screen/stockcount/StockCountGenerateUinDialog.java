package oracle.retail.sim.client.screen.stockcount;

import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * Stock Count Auto Generate UIN Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountGenerateUinDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -2237262839313778935L;

    private RIntegerFieldEditor valueEditor = new RIntegerFieldEditor("Number of AGSNs");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public StockCountGenerateUinDialog(RDialog dialog) {
        super(dialog);
        setStatusBarVisible(false);
        setTitle("Auto Generation");
        setSize(300, 100);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        valueEditor.setIdentifier(SimName.STOCK_COUNT_AGSN_QTY);
        valueEditor.setMinimumValue(0);
        valueEditor.setMaximumValue(100);

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(cancelButton);

        REditorPanel mainPanel = new REditorPanel(1, 1);
        mainPanel.add(valueEditor);

        setContentPane(mainPanel);
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
     * Save Information
     ***************************************************************************************************/

    private void doApply() throws Exception {
        Integer numberToGenerate = valueEditor.getIntegerOrNull();
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.AGSN_TO_GENERATE, numberToGenerate));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Window
     ***************************************************************************************************/

    private void doCancel() {
        closeWindow();
    }
}
