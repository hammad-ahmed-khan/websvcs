package oracle.retail.sim.client.screen.fulfillmentorderpick;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.screen.fulfillmentorder.IdReleaseDateDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickType;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPossiblePickVO;

/********************************************************************************************************
 * Fulfillment Order Pick Create Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderPickCreatePanel extends ScreenPanel implements REventListener {

    private static final long serialVersionUID = 8848088461685219253L;

    private FulfillmentOrderPickCreateModel model = new FulfillmentOrderPickCreateModel();

    private REditorPanel createPanel = new REditorPanel(3);

    private RComboBoxEditor pickTypeEditor = new RComboBoxEditor("Pick Type");
    private RComboBoxEditor orderEditor = new RComboBoxEditor("SIM Customer Order ID");
    private RIntegerFieldEditor binEditor = new RIntegerFieldEditor("Bin Quantity");

    private static final String PICK_TYPE_MODIFIED = "PickType.modified";

    public FulfillmentOrderPickCreatePanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        createPanel.setTitleBorder("Create Pick", 5);

        pickTypeEditor.setSizeType(EditorConstants.LARGE);
        pickTypeEditor.registerAction(this, PICK_TYPE_MODIFIED);
        pickTypeEditor.setItems(SimEnumUtility.findAllFulfillmentOrderPickTypes());
        pickTypeEditor.setSelectionRequired(true);
        pickTypeEditor.setSelectedItem(model.getDefaultType());

        orderEditor.setSizeType(EditorConstants.LARGE);
        orderEditor.setDisplayer(new IdReleaseDateDisplayer());
        orderEditor.setSortEnabled(false);

        binEditor.setIdentifier(SimName.CUSTOMER_ORDER_BIN_QTY);
        binEditor.setInteger(model.getDefaultBinQty());
        binEditor.setEnabled(model.isBinQtyEnabled());
        binEditor.setMinimumValue(1);
        binEditor.setMaximumValue(999);
    }

    private void layoutPanel() {
        createPanel.add(pickTypeEditor);
        createPanel.add(orderEditor);
        createPanel.add(binEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(createPanel, GridTool.constraints(0, 0, 1, 1, 0, 0, 1, 0, 0, 10, 10, 0));
        mainPanel.add(new JLabel(), GridTool.constraints(0, 3, 2, 1, 1, 1, 1, 3, 0, 10, 10, 0));

        setContentPane(mainPanel);
    }

    public void start() throws Exception {
        setEditorState();

        orderEditor.setItems(model.getFulfillmentOrderPossiblePickVOs());
    }

    public boolean handleSave() throws Exception {
        FulfillmentOrderPickType pickType = (FulfillmentOrderPickType) pickTypeEditor.getSelectedItem();
        if (pickType == FulfillmentOrderPickType.ORDER) {
            FulfillmentOrderPossiblePickVO orderVO = (FulfillmentOrderPossiblePickVO) orderEditor.getSelectedItem();
            if (orderVO == null) {
                return false;
            }
            model.createPick(orderVO.getId());
        } else {
            if (binEditor.isEmpty()) {
                UIStatusUtility.displayWarning(this, FulfillmentOrderMessageText.EMPTY_BIN_QUANTITY);
                return false;
            }
            model.createPick(binEditor.getInteger());
        }
        return true;
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return null;
    }

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(PICK_TYPE_MODIFIED)) {
                doPickTypeModified();
            }
        } catch (Throwable e) {
            displayException(e);
        }
    }

    private void doPickTypeModified() {
        setEditorState();
    }

    private void setEditorState() {
        FulfillmentOrderPickType pickType = (FulfillmentOrderPickType) pickTypeEditor.getSelectedItem();
        if (pickType == FulfillmentOrderPickType.BIN) {
            orderEditor.setVisible(false);
            binEditor.setVisible(true);
        } else if (pickType == FulfillmentOrderPickType.ORDER) {
            orderEditor.setVisible(true);
            binEditor.setVisible(false);
        }
    }
}
