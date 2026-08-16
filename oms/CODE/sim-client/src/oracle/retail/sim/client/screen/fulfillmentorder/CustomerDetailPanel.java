package oracle.retail.sim.client.screen.fulfillmentorder;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import oracle.retail.sim.client.core.RHeaderPanel;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.util.GridTool;

/********************************************************************************************************
 * Customer Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class CustomerDetailPanel extends ScreenPanel {
    private static final long serialVersionUID = -820030916153927511L;

    private CustomerDetailModel model = new CustomerDetailModel();

    private RDisplayLabelEditor customerEditor = new RDisplayLabelEditor("Customer ID");

    private AddressPanel deliveryAddressPanel = new AddressPanel();
    private AddressPanel billingAddressPanel = new AddressPanel();

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public CustomerDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        deliveryAddressPanel.setTitleBorder("Delivery Address");
        billingAddressPanel.setTitleBorder("Billing Address");
    }

    private void layoutScreen() {
        RHeaderPanel headerPanel = new RHeaderPanel(1);
        headerPanel.add(customerEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 5, 5, 5));
        mainPanel.add(deliveryAddressPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(billingAddressPanel, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(new JLabel(), GridTool.constraints(0, 3, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Initialize Panel
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadAddresses();
        populateScreen();
    }

    private void populateScreen() throws Exception {
        customerEditor.setData(model.getCustomerId());
        billingAddressPanel.setAddress(model.getBillingAddress());
        deliveryAddressPanel.setAddress(model.getDeliveryAddress());
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return null;
    }
}
