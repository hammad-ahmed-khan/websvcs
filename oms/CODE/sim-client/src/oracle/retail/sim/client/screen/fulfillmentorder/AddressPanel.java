package oracle.retail.sim.client.screen.fulfillmentorder;

import oracle.retail.sim.client.displayer.CountryDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.common.fulfillmentorder.CustomerAddress;
import oracle.retail.sim.common.person.PostalAddress;

/********************************************************************************************************
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class AddressPanel extends REditorPanel {

    private static final long serialVersionUID = 5634731360073286365L;
    
    private RDisplayLabelEditor firstNameEditor = new RDisplayLabelEditor("First Name");
    private RDisplayLabelEditor lastNameEditor = new RDisplayLabelEditor("Last Name");
    private RDisplayLabelEditor phoneticFirstNameEditor = new RDisplayLabelEditor("Phonetic First Name");
    private RDisplayLabelEditor phoneticLastNameEditor = new RDisplayLabelEditor("Phonetic Last Name");
    private RDisplayLabelEditor preferredNameEditor = new RDisplayLabelEditor("Preferred Name");
    private RDisplayLabelEditor companyNameEditor = new RDisplayLabelEditor("Company Name");
    private RDisplayLabelEditor address1Editor = new RDisplayLabelEditor("Address 1");
    private RDisplayLabelEditor address2Editor = new RDisplayLabelEditor("Address 2");
    private RDisplayLabelEditor address3Editor = new RDisplayLabelEditor("Address 3");
    private RDisplayLabelEditor countyEditor = new RDisplayLabelEditor("County");
    private RDisplayLabelEditor cityEditor = new RDisplayLabelEditor("City");
    private RDisplayLabelEditor stateEditor = new RDisplayLabelEditor("State");
    private RDisplayLabelEditor countryEditor = new RDisplayLabelEditor("Country");
    private RDisplayLabelEditor zipCodeEditor = new RDisplayLabelEditor("Zip Code");
    private RDisplayLabelEditor phoneEditor = new RDisplayLabelEditor("Phone");

    public AddressPanel() {
        super(5, 3);

        countryEditor.setDisplayer(new CountryDisplayer());

        add(firstNameEditor);
        add(lastNameEditor);
        add(phoneticFirstNameEditor);
        add(phoneticLastNameEditor);
        add(preferredNameEditor);
        add(companyNameEditor);
        add(address1Editor);
        add(address2Editor);
        add(address3Editor);
        add(countyEditor);
        add(cityEditor);
        add(stateEditor);
        add(zipCodeEditor);
        add(countryEditor);
        add(phoneEditor);
    }

    public void clearAddress() {
        firstNameEditor.clear();
        lastNameEditor.clear();
        phoneticFirstNameEditor.clear();
        phoneticLastNameEditor.clear();
        preferredNameEditor.clear();
        companyNameEditor.clear();
        address1Editor.clear();
        address2Editor.clear();
        address3Editor.clear();
        countyEditor.clear();
        cityEditor.clear();
        stateEditor.clear();
        zipCodeEditor.clear();
        countryEditor.clear();
        phoneEditor.clear();
    }

    public void setAddress(CustomerAddress address) {
        clearAddress();

        if (address != null) {
            PostalAddress postalAddress = address.getPostalAddress();
            firstNameEditor.setData(address.getFirstName());
            lastNameEditor.setData(address.getLastName());
            phoneticFirstNameEditor.setData(address.getPhoneticFirstName());
            phoneticLastNameEditor.setData(address.getPhoneticLastName());
            preferredNameEditor.setData(address.getPreferredName());
            companyNameEditor.setData(address.getCompanyName());
            address1Editor.setData(postalAddress.getAddressLine1());
            address2Editor.setData(postalAddress.getAddressLine2());
            address3Editor.setData(postalAddress.getAddressLine3());
            countyEditor.setData(postalAddress.getCounty());
            cityEditor.setData(postalAddress.getCity());
            stateEditor.setData(postalAddress.getState());
            zipCodeEditor.setData(postalAddress.getPostalCode());
            countryEditor.setData(postalAddress.getCountry());
            if (address.getPhone() != null) {
                phoneEditor.setData(address.getPhone().getTelephoneNumber());
            }
        }
    }
}
