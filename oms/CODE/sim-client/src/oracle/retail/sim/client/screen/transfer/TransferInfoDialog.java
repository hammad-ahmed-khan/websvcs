package oracle.retail.sim.client.screen.transfer;

import java.awt.GridBagLayout;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.transfer.Transfer;

public class TransferInfoDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -1310294277972386835L;

    private TransferInfoDialogModel model = new TransferInfoDialogModel();

    private RDisplayLabelEditor submitUserEditor = new RDisplayLabelEditor("Submit User");
    private RDisplayLabelEditor submitDateEditor = new RDisplayLabelEditor("Submit Date");
    private RDisplayLabelEditor customerOrderEditor = new RDisplayLabelEditor("Customer Order");
    private RDisplayLabelEditor fulfillmentOrderEditor = new RDisplayLabelEditor("Fulfillment Order");

    private RButton closeButton = new RButton(SimNavigation.DIALOG_CLOSE);

    public TransferInfoDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Transfer Info");
        setSize(550, 210);
        initializeWidgets();
        layoutContent();
        centerWindow();

    }

    private void initializeWidgets() {
        closeButton.registerAction(this, SimNavigation.DIALOG_CLOSE);
    }

    public void setTransfer(Transfer transfer) {
        model.setTransfer(transfer);
        submitUserEditor.setData(model.getSubmitUser());
        submitDateEditor.setData(model.getSubmitDate());
        customerOrderEditor.setData(model.getCustomerOrderExternalId());
        fulfillmentOrderEditor.setData(model.getFulfillOrderExternalId());
    }

    private void layoutContent() {
        addButton(closeButton);

        REditorPanel submitPanel = new REditorPanel(2, 1);
        submitPanel.setTitleBorder("Pre-shipment submit");
        submitPanel.add(submitUserEditor);
        submitPanel.add(submitDateEditor);

        REditorPanel fulfillOrderPanel = new REditorPanel(2, 1);
        fulfillOrderPanel.setTitleBorder("Customer Fulfillment Order");
        fulfillOrderPanel.add(customerOrderEditor);
        fulfillOrderPanel.add(fulfillmentOrderEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.setLineBorder(1);

        mainPanel.add(submitPanel, GridTool.constraints(0, 0, 2, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(fulfillOrderPanel, GridTool.constraints(0, 1, 2, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        setContentPane(mainPanel);

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

}
