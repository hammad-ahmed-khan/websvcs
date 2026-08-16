package oracle.retail.sim.client.screen.itemticket;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.DirectDeliveryVODisplayer;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.directdelivery.DirectDeliveryMessageText;
import oracle.retail.sim.common.directdelivery.DirectDeliveryStatus;
import oracle.retail.sim.common.directdelivery.DirectDeliveryVO;

/********************************************************************************************************
 * Purchase Order Add Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class PurchaseOrderAddPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -2302843086455691858L;

    private PurchaseOrderAddModel model = new PurchaseOrderAddModel();

    private RTextFieldEditor purchaseOrderExternalIdEditor = new RTextFieldEditor("PO #", true);
    private RComboBoxEditor deliveryEditor = new RComboBoxEditor("Shipment ID", true);

    private static final String PO_MODIFIED = "PurchaseOrder.modified";

    public PurchaseOrderAddPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        purchaseOrderExternalIdEditor.setIdentifier(SimName.PURCHASE_ORDER_EXTERNAL_ID);
        purchaseOrderExternalIdEditor.setSizeType(EditorConstants.MEDIUM);
        deliveryEditor.setSizeType(EditorConstants.MEDIUM);
        purchaseOrderExternalIdEditor.registerAction(this, PO_MODIFIED);
        deliveryEditor.setDisplayer(new DirectDeliveryVODisplayer());
    }

    private void layoutScreen() {
        REditorPanel mainPanel = new REditorPanel(2);
        mainPanel.add(purchaseOrderExternalIdEditor);
        mainPanel.add(deliveryEditor);

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return null;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() {
    }

    public void assignFocusInScreen() {
        assignFocusInScreen(purchaseOrderExternalIdEditor);
    }

    public void stop() {
        purchaseOrderExternalIdEditor.clear();
        deliveryEditor.clear();
        purchaseOrderExternalIdEditor.setEnabled(true);
        deliveryEditor.setEnabled(false);
    }

    /****************************************************************************************************
     * Handle Cancel/Apply
     ***************************************************************************************************/

    public boolean handleSave() throws Exception {
        if (purchaseOrderExternalIdEditor.isEmpty()) {
            displayError(DirectDeliveryMessageText.PO_NUM_REQUIRED);
            return false;
        }
        if (deliveryEditor.isEmptySelection()) {
            displayError(DirectDeliveryMessageText.ID_REQUIRED);
            return false;
        }
        if (!RConfirmUtility.confirm("Apply Confirmation", DirectDeliveryMessageText.APPLY_ITEM_CONFIRM)) {
            return false;
        }

        DirectDeliveryVO deliveryVO = (DirectDeliveryVO) deliveryEditor.getSelectedItem();
        model.applyDelivery(purchaseOrderExternalIdEditor.getText(), deliveryVO);
        return true;
    }

    /****************************************************************************************************
     * Handle Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(PO_MODIFIED)) {
                doPoModified();
            }
        } catch (Throwable e) {
            displayException(e);
        }
    }

    private void doPoModified() throws Exception {
        List<DirectDeliveryVO> deliveryVOs = model.findDeliveries(purchaseOrderExternalIdEditor.getText());
        if (deliveryVOs.isEmpty()) {
            displayError(DirectDeliveryMessageText.PO_INVALID_ENTRY);
            purchaseOrderExternalIdEditor.clear();
            assignFocusInScreen();
            return;
        }
        List<DirectDeliveryVO> deliveries = new ArrayList<DirectDeliveryVO>();
        for (DirectDeliveryVO deliveryVO : deliveryVOs) {
            if (StringHelper.isNullOrEmpty(deliveryVO.getAsnId()) && deliveryVO.getStatus() == DirectDeliveryStatus.RECEIVED) {
                deliveries.add(deliveryVO);
            }
        }
        if (deliveries.isEmpty()) {
            displayError(DirectDeliveryMessageText.PO_NOT_RECEIVED);
            purchaseOrderExternalIdEditor.clear();
            assignFocusInScreen();
            return;
        }
        deliveryEditor.setItems(deliveries);
        deliveryEditor.setEnabled(!deliveryEditor.isEmpty());
    }
}
