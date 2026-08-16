package oracle.retail.sim.client.screen.billoflading;

import java.util.List;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.common.person.AddressType;
import oracle.retail.sim.common.person.PostalAddress;

/********************************************************************************************************
 * Bill Of Lading Address Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class BillOfLadingAddressPanel extends REditorPanel {
    private static final long serialVersionUID = -7571396395666423339L;

    private RComboBoxEditor addressTypeEditor = new RComboBoxEditor("Address Type");
    private RDisplayLabelEditor nameEditor = new RDisplayLabelEditor("Name");
    private RDisplayLabelEditor phoneticNameEditor = new RDisplayLabelEditor("Phonetic Name");
    private RDisplayLabelEditor address1Editor = new RDisplayLabelEditor("Address 1");
    private RDisplayLabelEditor address2Editor = new RDisplayLabelEditor("Address 2");
    private RDisplayLabelEditor address3Editor = new RDisplayLabelEditor("Address 3");
    private RDisplayLabelEditor countyEditor = new RDisplayLabelEditor("County");
    private RDisplayLabelEditor cityEditor = new RDisplayLabelEditor("City");
    private RDisplayLabelEditor stateEditor = new RDisplayLabelEditor("State");
    private RDisplayLabelEditor zipCodeEditor = new RDisplayLabelEditor("Zip Code");
    private RDisplayLabelEditor countryEditor = new RDisplayLabelEditor("Country");

    public BillOfLadingAddressPanel() {
        super(11, 1);
        addressTypeEditor.setDisplayer(new TranslatedObjectDisplayer());

        add(addressTypeEditor);
        add(nameEditor);
        add(phoneticNameEditor);
        add(address1Editor);
        add(address2Editor);
        add(address3Editor);
        add(countyEditor);
        add(cityEditor);
        add(stateEditor);
        add(zipCodeEditor);
        add(countryEditor);
    }

    public RComboBoxEditor getAddressTypeEditor() {
        return addressTypeEditor;
    }

    public void setAddressTypeVisible(boolean visible) {
        addressTypeEditor.setVisible(visible);
    }

    public void registerAddressTypeAction(BillOfLadingDetailPanel panel, String command) {
        addressTypeEditor.registerAction(panel, command);
    }

    public void setAddressTypes(List<AddressType> addressTypes) {
        addressTypeEditor.setItems(addressTypes);
        addressTypeEditor.removeEmptySelection();
    }

    public void setAddressType(AddressType addressType) {
        addressTypeEditor.setSelectedItem(addressType);
    }

    public void setAddressTypeEnabled(boolean isEnabled) {
        addressTypeEditor.setEnabled(isEnabled);
    }

    public AddressType getSelectedAddressType() {
        return (AddressType) addressTypeEditor.getSelectedItem();
    }

    public void setAddressLine3Visible(boolean visible) {
        address3Editor.setVisible(visible);
    }

    public void setAddressName(String name) {
        nameEditor.setData(name);
    }

    public void setAddressPhoneticName(String phoneticName) {
        phoneticNameEditor.setData(phoneticName);
    }

    public void setAddressPhoneticNameVisible(boolean visible) {
        phoneticNameEditor.setVisible(visible);
    }

    public void setCountryVisible(boolean visible) {
        countryEditor.setVisible(visible);
    }

    public void setCountyVisible(boolean visible) {
        countyEditor.setVisible(visible);
    }

    public void setPostalAddress(PostalAddress address) {
        if (address == null) {
            clearAddress();
            return;
        }
        address1Editor.setData(address.getAddressLine1());
        address2Editor.setData(address.getAddressLine2());
        address3Editor.setData(address.getAddressLine3());
        countyEditor.setData(address.getCounty());
        cityEditor.setData(address.getCity());
        stateEditor.setData(address.getState());
        zipCodeEditor.setData(address.getPostalCode());
        countryEditor.setData(address.getCountry());
    }

    public void clearAddress() {
        address1Editor.clear();
        address2Editor.clear();
        address3Editor.clear();
        countyEditor.clear();
        cityEditor.clear();
        stateEditor.clear();
        zipCodeEditor.clear();
        countryEditor.clear();
    }
}
