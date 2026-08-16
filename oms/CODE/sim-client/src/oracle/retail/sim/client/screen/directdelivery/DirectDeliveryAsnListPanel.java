package oracle.retail.sim.client.screen.directdelivery;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.displayer.PurchaseOrderIdDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.directdelivery.DirectDeliveryMessageText;
import oracle.retail.sim.common.directdelivery.DirectDeliveryProperty;
import oracle.retail.sim.common.directdelivery.DirectDeliveryVO;

/********************************************************************************************************
 * Direct Delivery ASN List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class DirectDeliveryAsnListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 8413366238067192888L;

    private static final String ASN_SELECTED = "Asn.selected";

    private DirectDeliveryAsnListModel model = new DirectDeliveryAsnListModel();

    private SimTable deliveryAsnTable = new SimTable(new DirectDeliveryAsnListDefinition());
    private SimTablePane deliveryAsnPane = new SimTablePane(deliveryAsnTable);

    public DirectDeliveryAsnListPanel() {
        initializeTable();
        layoutScreen();
    }

    private void initializeTable() {
        deliveryAsnTable.setTableEditable(false);
        deliveryAsnTable.setSingleRowSelectionMode();
        deliveryAsnTable.registerDoubleClickAction(this, ASN_SELECTED);
    }

    private void layoutScreen() {
        setContentPane(deliveryAsnPane);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return deliveryAsnTable;
    }

    public void start() {
        try {
            populateScreen();
        } catch (Throwable e) {
            displayException(e);
        }
    }

    public void stop() {
        model.clearState();
    }

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(ASN_SELECTED)) {
                handleUseAsn();
            }
        } catch (Throwable e) {
            displayException(e);
        }
    }

    public void handleCancel() {
        model.storeCanceled();
    }

    private void populateScreen() {
        deliveryAsnTable.setRows(model.getOpenAsnDeliveryVOs());
    }

    public boolean handleUseAsn() throws Exception {
        DirectDeliveryVO deliveryVO = (DirectDeliveryVO) deliveryAsnTable.getSelectedRowData();
        if (deliveryVO == null) {
            throw new BusinessException(DirectDeliveryMessageText.ASN_NOT_SELECTED);
        }
        return model.selectAsnDelivery(deliveryVO.getId());
    }

    /****************************************************************************************************
     * ASN LIST TABLE DEFINITION
     ***************************************************************************************************/

    private class DirectDeliveryAsnListDefinition extends SimTableDefinition {
        public Class getDataClass() {
            return DirectDeliveryVO.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>();
            attributes.add(new SimTableAttribute("Purchase Order", DirectDeliveryProperty.PURCHASE_ORDER, new PurchaseOrderIdDisplayer()));
            attributes.add(new SimTableAttribute("ASN", DirectDeliveryProperty.ASN_ID));
            attributes.add(new SimTableAttribute("Supplier", DirectDeliveryProperty.SUPPLIER, new IdNameDisplayer()));
            attributes.add(new SimTableAttribute("Expected Delivery Date", DirectDeliveryProperty.EXPECTED_ARRIVAL_DATE));
            attributes.add(new SimTableAttribute("Total SKUs", DirectDeliveryProperty.NUMBER_OF_LINE_ITEMS));
            return attributes;
        }
    }
}
