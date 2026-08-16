package oracle.retail.sim.client.screen.finisher;

import java.awt.GridBagLayout;
import java.util.Locale;
import javax.swing.JPanel;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.screen.supplier.AddressPanel;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
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
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.format.PhoneMaskFactory;
import oracle.retail.sim.common.person.AddressType;
import oracle.retail.sim.common.source.Finisher;

/********************************************************************************************************
 * Finisher Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FinisherDetailPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 491434700213302589L;

    private FinisherDetailModel model = new FinisherDetailModel();

    private RDisplayLabelEditor identifierEditor = new RDisplayLabelEditor("Finisher ID");
    private RDisplayLabelEditor nameEditor = new RDisplayLabelEditor("Finisher Name");
    private RDisplayLabelEditor countryEditor = new RDisplayLabelEditor("Principal Country");
    private RDisplayLabelEditor currencyEditor = new RDisplayLabelEditor("Currency");
    private RDisplayLabelEditor languageEditor = new RDisplayLabelEditor("Language");
    private RDisplayLabelEditor paymentTermsEditor = new RDisplayLabelEditor("Payment Terms");
    private RDisplayLabelEditor contactPersonEditor = new RDisplayLabelEditor("Contact Person");
    private RDisplayLabelEditor phoneEditor = new RDisplayLabelEditor("Phone");
    private RDisplayLabelEditor faxEditor = new RDisplayLabelEditor("Fax");
    private RDisplayLabelEditor telexEditor = new RDisplayLabelEditor("Telex");
    private RDisplayLabelEditor emailEditor = new RDisplayLabelEditor("Email");
    private RDisplayLabelEditor statusEditor = new RDisplayLabelEditor("Status");
    private RDisplayLabelEditor vatRegionEditor = new RDisplayLabelEditor("VAT Region");
    private RDisplayLabelEditor transferEditor = new RDisplayLabelEditor("Transfer Entity ID");
    private RDisplayLabelEditor orgUnitEditor = new RDisplayLabelEditor("Org Unit Id");

    private RComboBoxEditor addressTypeEditor = new RComboBoxEditor("Select Address Type");

    private AddressPanel returnAddressPanel = new AddressPanel();
    private AddressPanel mainAddressPanel = new AddressPanel();

    /********************************************************************************************************
     * Build Panel
     *******************************************************************************************************/

    public FinisherDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        addressTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        addressTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        addressTypeEditor.setSortEnabled(false);
        addressTypeEditor.setSelectionRequired(true);
        addressTypeEditor.registerAction(this, SimClientStateKey.FINISHER_FILTER_MODIFIED);

        returnAddressPanel.setTitleBorder("Return Address");
        mainAddressPanel.setTitleBorder("HQ Address");
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        
        Locale locale = LocaleManager.getLanguageLocale();

        phoneEditor.getDisplayLabel().setMask(PhoneMaskFactory.createPhoneMask(locale));
        faxEditor.getDisplayLabel().setMask(PhoneMaskFactory.createPhoneMask(locale));
        telexEditor.getDisplayLabel().setMask(PhoneMaskFactory.createPhoneMask(locale));
    }

    private void layoutScreen() {
        REditorPanel headerPanel = new REditorPanel(2, 3);
        headerPanel.setTitleBorder(StringConstants.EMPTY);
        headerPanel.add(identifierEditor);
        headerPanel.add(nameEditor);
        headerPanel.add(countryEditor);
        headerPanel.add(currencyEditor);
        headerPanel.add(languageEditor);
        headerPanel.add(paymentTermsEditor);

        REditorPanel contactPanel = new REditorPanel(2, 3);
        contactPanel.setTitleBorder(StringConstants.EMPTY);
        contactPanel.add(contactPersonEditor);
        contactPanel.add(phoneEditor);
        contactPanel.add(faxEditor);
        contactPanel.add(telexEditor);
        contactPanel.add(emailEditor);

        REditorPanel addressTypePanel = new REditorPanel(1, 3);
        addressTypePanel.add(addressTypeEditor);
        addressTypePanel.add(new JPanel());
        addressTypePanel.add(new JPanel());

        REditorPanel footerPanel = new REditorPanel(1, 4);
        footerPanel.setTitleBorder(StringConstants.EMPTY);
        footerPanel.add(statusEditor);
        footerPanel.add(vatRegionEditor);
        footerPanel.add(transferEditor);
        footerPanel.add(orgUnitEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(contactPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(addressTypePanel, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(returnAddressPanel, GridTool.constraints(0, 3, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(mainAddressPanel, GridTool.constraints(0, 4, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(footerPanel, GridTool.constraints(0, 5, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(new RLabel(), GridTool.constraints(0, 6, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return null;
    }

    /********************************************************************************************************
     * START
     *******************************************************************************************************/

    public void start() throws Exception {
        model.loadFinisher();

        if (model.getFinisher() == null) {
            clearScreen();
            return;
        }
        populateScreen();
    }

    private void populateScreen() {
        Finisher finisher = model.getFinisher();

        addressTypeEditor.setActionsEnabled(false);
        addressTypeEditor.setItems(model.getAddressQueryTypes());
        addressTypeEditor.setSelectedItem(model.getDefaultAddressQueryType());
        addressTypeEditor.setActionsEnabled(true);

        identifierEditor.setData(finisher.getId());
        nameEditor.setData(finisher.getName());
        currencyEditor.setData(finisher.getCurrencyCode());
        countryEditor.setData(finisher.getPrincipalCountry());
        languageEditor.setData(finisher.getLanguage());
        paymentTermsEditor.setData(finisher.getPaymentTerms());
        contactPersonEditor.setData(finisher.getDefaultContact().getContact());
        phoneEditor.setData(finisher.getDefaultContact().getTelephone());
        faxEditor.setData(finisher.getDefaultContact().getFax());
        emailEditor.setData(finisher.getDefaultContact().getEmail());
        telexEditor.setData(finisher.getDefaultContact().getTelex());

        returnAddressPanel.setContactInfo(model.getContactInfo(AddressType.RETURNS));
        mainAddressPanel.setContactInfo(model.getContactInfo(AddressType.BUSINESS));

        statusEditor.setData(finisher.getStatus());
        orgUnitEditor.setData(finisher.getOrgUnitId());
    }

    private void clearScreen() {
        identifierEditor.clear();
        nameEditor.clear();
        countryEditor.clear();
        currencyEditor.clear();
        languageEditor.clear();
        paymentTermsEditor.clear();
        contactPersonEditor.clear();
        phoneEditor.clear();
        faxEditor.clear();
        telexEditor.clear();
        emailEditor.clear();
        statusEditor.clear();
        vatRegionEditor.clear();
        transferEditor.clear();
        orgUnitEditor.clear();
        returnAddressPanel.clearAddress();
        mainAddressPanel.clearAddress();
    }

    /********************************************************************************************************
     * HANDLE ACTIONS
     *******************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimClientStateKey.FINISHER_FILTER_MODIFIED)) {
                doAddressTypeModified();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doAddressTypeModified() {
        AddressType addressType = (AddressType) addressTypeEditor.getSelectedItem();
        returnAddressPanel.setContactInfo(model.getContactInfo(addressType));
        returnAddressPanel.setTitleBorder(addressType.toString());
    }
}
