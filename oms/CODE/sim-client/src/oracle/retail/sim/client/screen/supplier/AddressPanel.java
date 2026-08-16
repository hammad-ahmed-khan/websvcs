package oracle.retail.sim.client.screen.supplier;

import java.util.Locale;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.common.format.PhoneMaskFactory;
import oracle.retail.sim.common.person.ContactInfo;
import oracle.retail.sim.common.person.PostalAddress;

/********************************************************************************************************
 * Address Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class AddressPanel extends REditorPanel {
    private static final long serialVersionUID = -8000523718460530564L;

    private RDisplayLabelEditor address1Editor = new RDisplayLabelEditor("Address 1");
    private RDisplayLabelEditor address2Editor = new RDisplayLabelEditor("Address 2");
    private RDisplayLabelEditor cityEditor = new RDisplayLabelEditor("City");
    private RDisplayLabelEditor stateEditor = new RDisplayLabelEditor("State");
    private RDisplayLabelEditor zipCodeEditor = new RDisplayLabelEditor("Zip Code");
    private RDisplayLabelEditor phoneEditor = new RDisplayLabelEditor("Phone #");
    private RDisplayLabelEditor contactEditor = new RDisplayLabelEditor("Contact");
    private RDisplayLabelEditor emailEditor = new RDisplayLabelEditor("Email Address");
    private RDisplayLabelEditor faxEditor = new RDisplayLabelEditor("Fax #");

    public AddressPanel() {
        super(5, 2);
        add(address1Editor);
        add(address2Editor);
        add(cityEditor);
        add(stateEditor);
        add(zipCodeEditor);
        add(phoneEditor);
        add(contactEditor);
        add(emailEditor);
        add(faxEditor);
    }

    public void clearAddress() {
        address1Editor.clear();
        address2Editor.clear();
        cityEditor.clear();
        stateEditor.clear();
        zipCodeEditor.clear();
        phoneEditor.clear();
        contactEditor.clear();
        emailEditor.clear();
        faxEditor.clear();
    }

    public void setContactInfo(ContactInfo contactInfo) {
        clearAddress();

        if (contactInfo != null) {
            Locale locale = LocaleManager.getLanguageLocale();

            phoneEditor.getDisplayLabel().setMask(PhoneMaskFactory.createPhoneMask(locale));
            faxEditor.getDisplayLabel().setMask(PhoneMaskFactory.createPhoneMask(locale));

            PostalAddress address = contactInfo.getAddress();

            if (address != null) {
                address1Editor.setData(address.getAddressLine1());
                address2Editor.setData(address.getAddressLine2());
                cityEditor.setData(address.getCity());
                stateEditor.setData(address.getState());
                zipCodeEditor.setData(address.getPostalCode());
            }

            contactEditor.setData(contactInfo.getContact());

            if (contactInfo.getTelephone() != null) {
                phoneEditor.setData(contactInfo.getTelephone());
            }
            if (contactInfo.getFax() != null) {
                faxEditor.setData(contactInfo.getFax());
            }
            emailEditor.setData(contactInfo.getEmail());
        }
    }
}
