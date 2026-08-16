package oracle.retail.sim.client.screen.supplier;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.RHeaderPanel;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.person.AddressType;
import oracle.retail.sim.common.source.Supplier;

/********************************************************************************************************
 * Supplier Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SupplierDetailPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -2715493474630454793L;

    private SupplierDetailModel model = new SupplierDetailModel();

    private RDisplayLabelEditor supplierEditor = new RDisplayLabelEditor("Supplier ID");
    private RDisplayLabelEditor supplierNameEditor = new RDisplayLabelEditor("Supplier Name");
    private RDisplayLabelEditor statusEditor = new RDisplayLabelEditor("Status");
    private RDisplayLabelEditor returnAllowEditor = new RDisplayLabelEditor("Returns Allowed");
    private RDisplayLabelEditor returnAuthEditor = new RDisplayLabelEditor("Return Authorization Required");
    private RDisplayLabelEditor deliveryDiscrepancyEditor = new RDisplayLabelEditor("Delivery Discrepancy");

    private RComboBoxEditor addressTypeEditor = new RComboBoxEditor("Address Type");

    private AddressPanel returnAddressPanel = new AddressPanel();
    private AddressPanel hqAddressPanel = new AddressPanel();

    public SupplierDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        addressTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        addressTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        addressTypeEditor.setSortEnabled(false);
        addressTypeEditor.setSelectionRequired(true);
        addressTypeEditor.setItems(model.findAddressQueryTypes());
        addressTypeEditor.setSelectedItem(model.getDefaultAddressQueryType());
        addressTypeEditor.registerAction(this, SimClientStateKey.SUPPLIER_FILTER_MODIFIED);

        returnAddressPanel.setTitleBorder("Return Address");
        hqAddressPanel.setTitleBorder("HQ Address");
        returnAllowEditor.setDisplayer(new BooleanDisplayer());
        returnAuthEditor.setDisplayer(new BooleanDisplayer());
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        deliveryDiscrepancyEditor.setDisplayer(new TranslatedObjectDisplayer());
        supplierNameEditor.setSizeType(EditorConstants.LARGE);
    }

    private void layoutScreen() {
        RHeaderPanel headerPanel = new RHeaderPanel(1, 2);
        headerPanel.add(supplierEditor);
        headerPanel.add(supplierNameEditor);

        REditorPanel footerPanel = new REditorPanel(1, 4);
        footerPanel.add(statusEditor);
        footerPanel.add(returnAllowEditor);
        footerPanel.add(returnAuthEditor);
        footerPanel.add(deliveryDiscrepancyEditor);

        REditorPanel topPanel = new REditorPanel(1, 3);
        topPanel.add(addressTypeEditor);
        topPanel.add(new JPanel());
        topPanel.add(new JPanel());

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel,        GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 5, 5, 5));
        mainPanel.add(topPanel,           GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(returnAddressPanel, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(hqAddressPanel,     GridTool.constraints(0, 3, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(footerPanel,        GridTool.constraints(0, 4, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(new RLabel(),       GridTool.constraints(0, 5, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return null;
    }

    public void start() throws Exception {
        addressTypeEditor.setActionsEnabled(false);
        addressTypeEditor.setSelectedItem(model.getDefaultAddressQueryType());
        addressTypeEditor.setActionsEnabled(true);

        Supplier supplier = (Supplier) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_SUPPLIER);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_SUPPLIER);

        model.setSupplier(supplier);

        if (supplier == null) {
            clearScreen();
            return;
        }

        supplierEditor.setData(supplier.getId());
        supplierNameEditor.setData(supplier.getName());

        returnAddressPanel.setContactInfo(model.getContactInfo(AddressType.RETURNS));
        hqAddressPanel.setContactInfo(model.getContactInfo(AddressType.BUSINESS));

        statusEditor.setData(supplier.getStatus());
        returnAllowEditor.setData(supplier.isReturnsAllowed());
        returnAuthEditor.setData(supplier.isAuthorizationRequired());
        deliveryDiscrepancyEditor.setData(supplier.getDeliveryDiscrepancy());
    }

    private void clearScreen() {
        supplierEditor.clear();
        supplierNameEditor.clear();
        statusEditor.clear();
        returnAllowEditor.clear();
        returnAuthEditor.clear();
        deliveryDiscrepancyEditor.clear();
        returnAddressPanel.clearAddress();
        hqAddressPanel.clearAddress();
    }

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimClientStateKey.SUPPLIER_FILTER_MODIFIED)) {
                AddressType addressType = (AddressType) addressTypeEditor.getSelectedItem();
                returnAddressPanel.setContactInfo(model.getContactInfo(addressType));
                returnAddressPanel.setTitleBorder(addressType.toString());
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }
}
