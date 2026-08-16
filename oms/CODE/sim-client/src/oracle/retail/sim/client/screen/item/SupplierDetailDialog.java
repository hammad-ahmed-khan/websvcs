package oracle.retail.sim.client.screen.item;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.RHeaderPanel;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.screen.supplier.AddressPanel;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.person.AddressType;
import oracle.retail.sim.common.person.ContactInfo;
import oracle.retail.sim.common.person.SupplierContactInfo;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Supplier Lookup Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SupplierDetailDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 3629355852669058153L;

    private final RDisplayLabelEditor supplierEditor = new RDisplayLabelEditor("Supplier ID");
    private final RDisplayLabelEditor supplierNameEditor = new RDisplayLabelEditor("Supplier Name");
    private final RDisplayLabelEditor statusEditor = new RDisplayLabelEditor("Status");
    private final RDisplayLabelEditor returnAllowEditor = new RDisplayLabelEditor("Returns Allowed");
    private final RDisplayLabelEditor returnAuthEditor = new RDisplayLabelEditor("Return Authorization Required");
    private final RComboBoxEditor addressTypeEditor = new RComboBoxEditor("Address Type");

    private final AddressPanel returnAddressPanel = new AddressPanel();
    private final AddressPanel hqAddressPanel = new AddressPanel();

    private final SimTable supplierTable = new SimTable(new AdditionalSupplierDefinition());
    private final SimTablePane supplierPane = new SimTablePane(supplierTable);

    private final RButton closeButton = new RButton(SimNavigation.DIALOG_CLOSE);

    private List<SupplierContactInfo> contactList;

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public SupplierDetailDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Supplier Detail");
        setSize(700, 550);
        initContent();
        layoutContent();
        centerWindow();
    }

    private void initContent() {
        returnAddressPanel.setTitleBorder("Return Address");
        hqAddressPanel.setTitleBorder("HQ Address");
        returnAllowEditor.setDisplayer(new BooleanDisplayer());
        returnAuthEditor.setDisplayer(new BooleanDisplayer());
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        supplierNameEditor.setSizeType(EditorConstants.LARGE);
        supplierPane.setTitleBorder("Additional Suppliers");
        closeButton.registerAction(this, SimNavigation.DIALOG_CLOSE);

        addressTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        addressTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        addressTypeEditor.setSortEnabled(false);
        addressTypeEditor.setSelectionRequired(true);
        addressTypeEditor.setItems(findAddressQueryTypes());
        addressTypeEditor.setSelectedItem(AddressType.RETURNS);
        addressTypeEditor.registerAction(this, SimClientStateKey.SUPPLIER_FILTER_MODIFIED);
    }

    private void layoutContent() {
        addButton(closeButton);

        RHeaderPanel headerPanel = new RHeaderPanel(1, 2);
        headerPanel.add(supplierEditor);
        headerPanel.add(supplierNameEditor);

        REditorPanel footerPanel = new REditorPanel(1, 3);
        footerPanel.add(statusEditor);
        footerPanel.add(returnAllowEditor);
        footerPanel.add(returnAuthEditor);

        REditorPanel addressTypetopPanel = new REditorPanel(1, 3);
        addressTypetopPanel.add(addressTypeEditor);
        addressTypetopPanel.add(new JPanel());
        addressTypetopPanel.add(new JPanel());

        RPanel topPanel = new RPanel(new GridBagLayout());
        topPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        topPanel.add(addressTypetopPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        topPanel.add(returnAddressPanel, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        topPanel.add(hqAddressPanel, GridTool.constraints(0, 3, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        topPanel.add(footerPanel, GridTool.constraints(0, 4, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(topPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(supplierPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Populate Dialog
     ***************************************************************************************************/

    public void setSupplier(Supplier supplier, List<Supplier> additionalSuppliers) throws Exception {
        contactList = ClientServiceFactory.getSourceServices().readSupplierContactInfo(supplier.getId());

        supplierEditor.setData(supplier.getId());
        supplierNameEditor.setData(supplier.getName());
        statusEditor.setData(supplier.getStatus().toString());
        returnAllowEditor.setData(supplier.isReturnsAllowed());
        returnAuthEditor.setData(supplier.isAuthorizationRequired());
        hqAddressPanel.setContactInfo(getContactInfo(AddressType.BUSINESS));
        returnAddressPanel.setContactInfo(getContactInfo(AddressType.RETURNS));
        supplierTable.setRows(additionalSuppliers);
    }

    public void stopEditing() {
        supplierTable.stopEditing();
    }

    /****************************************************************************************************
     * Get and Set Dialog Properties
     ***************************************************************************************************/
    private Set<AddressType> findAddressQueryTypes() {
        return AddressType.getQuerySet();
    }

    private ContactInfo getContactInfo(AddressType addressType) {
        for (SupplierContactInfo supplierContactInfo : contactList) {
            if (supplierContactInfo.getAddrType() == addressType) {
                return supplierContactInfo.getContactInfo();
            }
        }
        return null;
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            stopEditing();
            if (command.equals(SimNavigation.DIALOG_CLOSE)) {
                doDone();
            } else if (command.equals(SimClientStateKey.SUPPLIER_FILTER_MODIFIED)) {
                doSupplierFilterModified();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doSupplierFilterModified() {
        AddressType addressType = (AddressType) addressTypeEditor.getSelectedItem();
        returnAddressPanel.setContactInfo(getContactInfo(addressType));
        returnAddressPanel.setTitleBorder(addressType.toString());
    }

    private void doDone() {
        closeWindow();
    }

    /****************************************************************************************************
     * Additional Supplier Table Definition
     ***************************************************************************************************/

    private class AdditionalSupplierDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return Supplier.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(2);
            attributes.add(new SimTableAttribute("Supplier ID", "id"));
            attributes.add(new SimTableAttribute("Supplier Name", "name"));
            return attributes;
        }
    }
}
