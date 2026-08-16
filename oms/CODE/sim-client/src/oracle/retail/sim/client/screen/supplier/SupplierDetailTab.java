package oracle.retail.sim.client.screen.supplier;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;
import oracle.retail.sim.client.core.RHeaderPanel;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.client.widget.SimTab;
import oracle.retail.sim.common.person.AddressType;
import oracle.retail.sim.common.person.ContactInfo;
import oracle.retail.sim.common.person.SupplierContactInfo;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Supplier Detail Tab
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SupplierDetailTab extends SimTab implements REventListener {
    private static final long serialVersionUID = -409460529660427691L;

    private RDisplayLabelEditor supplierEditor = new RDisplayLabelEditor("Supplier ID");
    private RDisplayLabelEditor supplierNameEditor = new RDisplayLabelEditor("Supplier Name");
    private RDisplayLabelEditor statusEditor = new RDisplayLabelEditor("Status");
    private RDisplayLabelEditor returnAllowEditor = new RDisplayLabelEditor("Returns Allowed");
    private RDisplayLabelEditor returnAuthEditor = new RDisplayLabelEditor("Return Authorization Required");

    private RComboBoxEditor queryTypeEditor = new RComboBoxEditor("Select Address Type");

    private AddressPanel returnAddressPanel = new AddressPanel();
    private AddressPanel hqAddressPanel = new AddressPanel();

    private List<SupplierContactInfo> contactList = new ArrayList<>();

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public SupplierDetailTab() {
        setTitle("Detail");
        setSize(800, 600);
        initializeTab();
        layoutTab();
    }

    private void initializeTab() {
        queryTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        queryTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        queryTypeEditor.setSortEnabled(false);
        queryTypeEditor.setSelectionRequired(true);
        queryTypeEditor.setItems(getAddressQueryTypes());
        queryTypeEditor.setSelectedItem(AddressType.RETURNS);
        queryTypeEditor.registerAction(this, SimClientStateKey.SUPPLIER_FILTER_MODIFIED);

        returnAddressPanel.setTitleBorder("Return Address");
        hqAddressPanel.setTitleBorder("HQ Address");
        returnAllowEditor.setDisplayer(new BooleanDisplayer());
        returnAuthEditor.setDisplayer(new BooleanDisplayer());
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        supplierNameEditor.setSizeType(EditorConstants.LARGE);
    }

    private void layoutTab() {
        RHeaderPanel headerPanel = new RHeaderPanel(1, 2);
        headerPanel.add(supplierEditor);
        headerPanel.add(supplierNameEditor);

        REditorPanel footerPanel = new REditorPanel(1, 3);
        footerPanel.add(statusEditor);
        footerPanel.add(returnAllowEditor);
        footerPanel.add(returnAuthEditor);

        REditorPanel topPanel = new REditorPanel(1, 3);
        topPanel.add(queryTypeEditor);
        topPanel.add(new JPanel());
        topPanel.add(new JPanel());

        setLayout(new GridBagLayout());
        add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        add(topPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        add(returnAddressPanel, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        add(hqAddressPanel, GridTool.constraints(0, 3, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        add(footerPanel, GridTool.constraints(0, 4, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        add(new RLabel(), GridTool.constraints(0, 5, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));
    }

    /****************************************************************************************************
     * Get and Set Dialog Properties
     ***************************************************************************************************/

    public void loadTab(Supplier supplier) throws Exception {
        supplierEditor.setData(supplier.getId());
        supplierNameEditor.setData(supplier.getName());
        statusEditor.setData(supplier.getStatus());
        returnAllowEditor.setData(supplier.isReturnsAllowed());
        returnAuthEditor.setData(supplier.isAuthorizationRequired());

        contactList = ClientServiceFactory.getSourceServices().readSupplierContactInfo(supplier.getId());

        returnAddressPanel.setContactInfo(getContactInfo(AddressType.RETURNS));
        hqAddressPanel.setContactInfo(getContactInfo(AddressType.BUSINESS));
    }

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimClientStateKey.SUPPLIER_FILTER_MODIFIED)) {
                AddressType addressType = (AddressType) queryTypeEditor.getSelectedItem();
                returnAddressPanel.setContactInfo(getContactInfo(addressType));
                returnAddressPanel.setTitleBorder(addressType.toString());
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private Set<AddressType> getAddressQueryTypes() {
        return AddressType.getQuerySet();
    }

    private ContactInfo getContactInfo(AddressType addressType) {
        for (SupplierContactInfo contactInfo : contactList) {
            if (contactInfo.getAddrType() == addressType) {
                return contactInfo.getContactInfo();
            }
        }
        return null;
    }
}
