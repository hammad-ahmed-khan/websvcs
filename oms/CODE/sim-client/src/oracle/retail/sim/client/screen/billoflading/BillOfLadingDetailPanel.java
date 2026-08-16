package oracle.retail.sim.client.screen.billoflading;

import java.awt.GridBagLayout;
import javax.swing.JComponent;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.GenericIdDisplayer;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.editor.RQuantityFieldEditor;
import oracle.retail.sim.client.swing.editor.RRadioButtonEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.person.AddressType;
import oracle.retail.sim.common.shipment.BillOfLadingMotive;
import oracle.retail.sim.common.shipment.ShipmentCarrier;
import oracle.retail.sim.common.shipment.ShipmentCarrierRole;
import oracle.retail.sim.common.shipment.ShipmentCarrierService;
import oracle.retail.sim.common.shipment.ShipmentCartonType;
import oracle.retail.sim.common.shipment.ShipmentMessageText;

/********************************************************************************************************
 * Bill Of Lading Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class BillOfLadingDetailPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 5487238295211605775L;

    private BillOfLadingDetailModel model = new BillOfLadingDetailModel();

    private RDisplayLabelEditor shipmentEditor = new RDisplayLabelEditor("Return ID");
    private RDisplayLabelEditor billOfLadingEditor = new RDisplayLabelEditor("BOL ID");
    private RDisplayLabelEditor createDateEditor = new RDisplayLabelEditor("Create Date");
    private RDisplayLabelEditor dispatchDateEditor = new RDisplayLabelEditor("Dispatch Date");

    private BillOfLadingAddressPanel shipFromAddressPanel = new BillOfLadingAddressPanel();
    private BillOfLadingAddressPanel shipToAddressPanel = new BillOfLadingAddressPanel();
    private REditorPanel servicePanel = new REditorPanel(1, 1);
    private REditorPanel supplierTaxIdPanel = new REditorPanel(1, 1);

    private RComboBoxEditor billOfLadingMotiveEditor = new RComboBoxEditor();
    private RTextFieldEditor supplierTaxIdEditor = new RTextFieldEditor();

    private RRadioButtonEditor carrierRoleEditor = new RRadioButtonEditor();
    private static String SENDER = "Sender";
    private static String RECEIVER = "Receiver";
    private static String THIRD_PARTY = "Third Party";
    private static String[] radioHeaders = new String[] { SENDER, RECEIVER, THIRD_PARTY };
    private RComboBoxEditor carrierEditor = new RComboBoxEditor();
    private RTextFieldEditor carrierNameEditor = new RTextFieldEditor("Name");
    private RLongTextFieldEditor carrierAddressEditor = new RLongTextFieldEditor("Address");

    private RComboBoxEditor serviceEditor = new RComboBoxEditor();

    private RLongTextFieldEditor altShipToAddressEditor = new RLongTextFieldEditor();
    private RDateFieldEditor pickupDateEditor = new RDateFieldEditor();

    private REditorPanel trackingPanel = new REditorPanel(1, 1);
    private RTextFieldEditor trackingEditor = new RTextFieldEditor();

    private RQuantityFieldEditor weightEditor = new RQuantityFieldEditor();
    private RComboBoxEditor packageTypeEditor = new RComboBoxEditor();

    // Screen Actions
    private static final String CARRIER_MODIFIED = "Carrier.modified";
    private static final String SERVICE_MODIFIED = "Service.modified";
    private static final String CARRIER_TYPE_MODIFIED = "CarrierType.modified";
    private static final String ADDRESS_TYPE_MODIFIED = "AddressType.modified";

    /****************************************************************************************************
     * Initialize Screen
     ***************************************************************************************************/

    public BillOfLadingDetailPanel() {
        initializePanel();
    }

    private void initializePanel() {
        carrierAddressEditor.setIdentifier(SimName.BILL_OF_LADING_CARRIER_ADDRESS);
        carrierNameEditor.setIdentifier(SimName.BILL_OF_LADING_CARRIER_NAME);
        carrierRoleEditor.setIdentifier(SimName.BILL_OF_LADING_CARRIER_TYPE);
        altShipToAddressEditor.setIdentifier(SimName.BILL_OF_LADING_DESTINATION_ADDRESS);
        supplierTaxIdEditor.setIdentifier(SimName.BILL_OF_LADING_TAX_ID);
        trackingEditor.setIdentifier(SimName.BILL_OF_LADING_TRACKING);
        weightEditor.setIdentifier(SimName.BILL_OF_LADING_WEIGHT);

        shipmentEditor.setDisplayer(new GenericIdDisplayer());
        billOfLadingEditor.setDisplayer(new GenericIdDisplayer());
        createDateEditor.setDataType(DataTypeConstants.DATE_SHORT);
        dispatchDateEditor.setDataType(DataTypeConstants.DATE_SHORT);

        shipFromAddressPanel.setTitleBorder("Ship From");
        shipFromAddressPanel.setAddressPhoneticNameVisible(false);
        shipFromAddressPanel.setAddressLine3Visible(false);
        shipFromAddressPanel.setCountryVisible(false);
        shipFromAddressPanel.setCountyVisible(false);
        shipToAddressPanel.setTitleBorder("Ship To");

        carrierRoleEditor.setRadioButtons(radioHeaders, 1, 3);
        carrierRoleEditor.setTitle("Type");
        carrierRoleEditor.setMinimumWidth(1);
        carrierEditor.setDisplayer(new TranslatedObjectDisplayer());

        servicePanel.setTitleBorder("Service");
        serviceEditor.setDisplayer(new TranslatedObjectDisplayer());

        billOfLadingMotiveEditor.setRequired(true);
        billOfLadingMotiveEditor.setDisplayer(new TranslatedObjectDisplayer());

        pickupDateEditor.setValidStartDate(SimDateUtil.getCurrentDate());

        trackingPanel.setTitleBorder("Tracking ID");

        serviceEditor.registerAction(this, SERVICE_MODIFIED);
        carrierEditor.registerAction(this, CARRIER_MODIFIED);
        carrierRoleEditor.registerAction(this, CARRIER_TYPE_MODIFIED);
        shipFromAddressPanel.registerAddressTypeAction(this, ADDRESS_TYPE_MODIFIED);

        weightEditor.setSizeType(EditorConstants.SMALL);
        packageTypeEditor.setTitle("Package Type");
    }

    private void layoutPanel() {
        REditorPanel headerPanel = new REditorPanel(1, 4);
        headerPanel.setTitleBorder("Delivery Summary");
        headerPanel.add(shipmentEditor);
        headerPanel.add(billOfLadingEditor);
        headerPanel.add(createDateEditor);
        headerPanel.add(dispatchDateEditor);

        REditorPanel motivePanel = new REditorPanel(1, 1);
        motivePanel.setTitleBorder("Motive");
        motivePanel.add(billOfLadingMotiveEditor);

        supplierTaxIdPanel.setTitleBorder("Tax ID");
        supplierTaxIdPanel.add(supplierTaxIdEditor);

        REditorPanel carrierTypePanel = new REditorPanel(4, 1);
        carrierTypePanel.setTitleBorder("Carrier");
        carrierTypePanel.add(carrierRoleEditor);
        carrierTypePanel.add(carrierEditor);
        carrierTypePanel.add(carrierNameEditor);
        carrierTypePanel.add(carrierAddressEditor);

        servicePanel.setTitleBorder("Service");
        servicePanel.add(serviceEditor);

        trackingPanel.add(trackingEditor);

        REditorPanel packageInfoPanel = new REditorPanel(2, 1);
        packageInfoPanel.setTitleBorder("Package Info");
        packageInfoPanel.add(weightEditor);
        packageInfoPanel.add(packageTypeEditor);
        REditorPanel destinationPanel = new REditorPanel(1, 1);
        destinationPanel.setTitleBorder("Alternate Destination Address");
        if (!model.isOrderDelivery()) {
            destinationPanel.add(altShipToAddressEditor);
        }

        REditorPanel pickupDatePanel = new REditorPanel(1, 1);
        pickupDatePanel.setTitleBorder("Requested Pickup Date");
        pickupDatePanel.add(pickupDateEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 2, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(shipFromAddressPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(shipToAddressPanel, GridTool.constraints(1, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(motivePanel, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(supplierTaxIdPanel, GridTool.constraints(1, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(carrierTypePanel, GridTool.constraints(0, 3, 1, 2, 1, 0, 0, 3, 0, 0, 5, 0));
        if (!model.isOrderDelivery()) {
            mainPanel.add(destinationPanel, GridTool.constraints(1, 3, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
            mainPanel.add(pickupDatePanel, GridTool.constraints(1, 4, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
            mainPanel.add(trackingPanel, GridTool.constraints(1, 5, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        } else {
            mainPanel.add(pickupDatePanel, GridTool.constraints(1, 3, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
            mainPanel.add(trackingPanel, GridTool.constraints(1, 4, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        }
        mainPanel.add(servicePanel, GridTool.constraints(0, 5, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));

        mainPanel.add(packageInfoPanel, GridTool.constraints(0, 6, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(new RLabel(), GridTool.constraints(0, 7, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return null;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadShipment();
        populateScreen();
        layoutPanel();
    }

    private void populateScreen() throws Exception {
        shipmentEditor.setTitle(model.getShipmentIdTitle());
        shipmentEditor.setData(model.getShipmentId());

        billOfLadingEditor.setData(model.getBillOfLadingId());
        createDateEditor.setData(model.getCreateDate());
        dispatchDateEditor.setData(model.getDispatchDate());

        shipFromAddressPanel.setAddressTypeVisible(false);
        shipFromAddressPanel.setAddressName(model.getShipFromAddressName());
        shipFromAddressPanel.setPostalAddress(model.getShipFromAddress());

        shipToAddressPanel.setAddressTypes(model.getShipToAddressTypes());
        shipToAddressPanel.setAddressType(model.getShipToAddressType());
        shipToAddressPanel.setAddressTypeVisible(true);
        shipToAddressPanel.setAddressName(model.getShipToAddressName());
        shipToAddressPanel.setAddressPhoneticName(model.getShipToAddressPhoneticName());
        shipToAddressPanel.setPostalAddress(model.getShipToAddress(shipToAddressPanel.getSelectedAddressType()));

        billOfLadingMotiveEditor.setActionsEnabled(false);
        billOfLadingMotiveEditor.setItems(model.getAvailableMotives());
        billOfLadingMotiveEditor.removeEmptySelection();
        billOfLadingMotiveEditor.setSelectedItem(model.getBillOfLadingMotive());
        billOfLadingMotiveEditor.setActionsEnabled(true);

        ShipmentCarrierRole carrierType = model.getCarrierType();

        if (carrierType == ShipmentCarrierRole.SENDER) {
            carrierRoleEditor.setSelected(SENDER, true);
        } else if (carrierType == ShipmentCarrierRole.RECEIVER) {
            carrierRoleEditor.setSelected(RECEIVER, true);
        } else if (carrierType == ShipmentCarrierRole.THIRD_PARTY) {
            carrierRoleEditor.setSelected(THIRD_PARTY, true);
        }

        carrierEditor.setActionsEnabled(false);
        carrierEditor.setItems(model.getAvailableCarriers());
        carrierEditor.removeEmptySelection();
        carrierEditor.setSelectedItem(model.getCarrier());
        carrierEditor.setActionsEnabled(true);

        carrierNameEditor.setText(model.getAlternameCarrierName());
        carrierAddressEditor.setText(model.getAlternateCarrierAddress());

        serviceEditor.setActionsEnabled(false);
        serviceEditor.setItems(model.getCarrierServices(model.getCarrier()));
        serviceEditor.setSelectedItem(model.getCarrierService());
        serviceEditor.setActionsEnabled(true);

        supplierTaxIdEditor.setText(model.getBillOfLadingTaxId());

        altShipToAddressEditor.setText(model.getAlternateShipToAddress());
        pickupDateEditor.setDate(model.getRequestedPickupDate());

        trackingEditor.setText(model.getTrackingNumber());

        weightEditor.setQuantity(model.getWeight());

        packageTypeEditor.setItems(model.getCartonTypes());
        packageTypeEditor.setSelectedItem(model.getCartonType());

        //This requires info from loaded data, so must be here
        weightEditor.setTitle(getWeightEditorText());

        if (model.isReturn()) {
            validateEditorStateForReturn();
        } else if (model.isTransfer()) {
            validateEditorStateForTransfer();
        } else {
            validateEditorStateForDelivery();
        }
    }

    private void validateEditorStateForReturn() throws Exception {
        ShipmentCarrierRole carrierType = model.getCarrierType();
        boolean isReturnEditable = model.isReturnEditable();

        shipToAddressPanel.setAddressTypeVisible(isReturnEditable);
        shipToAddressPanel.setAddressLine3Visible(false);
        shipToAddressPanel.setAddressPhoneticNameVisible(false);
        shipToAddressPanel.setCountyVisible(false);
        shipToAddressPanel.setCountryVisible(false);

        if (model.isWarehouseReturn()) {
            shipToAddressPanel.setAddressTypeEnabled(false);
        } else {
            shipToAddressPanel.setAddressTypeEnabled(isReturnEditable);
        }

        billOfLadingMotiveEditor.setEnabled(isReturnEditable);

        carrierRoleEditor.setEnabled(isReturnEditable);
        carrierEditor.setEnabled(isReturnEditable && carrierType == ShipmentCarrierRole.THIRD_PARTY);
        carrierEditor.setVisible(true);
        carrierNameEditor.setEnabled(isReturnEditable && carrierType == ShipmentCarrierRole.THIRD_PARTY && model.isOtherCarrier(model.getCarrier()));
        carrierAddressEditor.setEnabled(isReturnEditable && carrierType == ShipmentCarrierRole.THIRD_PARTY && model.isOtherCarrier(model.getCarrier()));

        servicePanel.setVisible(true);
        serviceEditor.setEnabled(isReturnEditable && carrierType == ShipmentCarrierRole.THIRD_PARTY);

        supplierTaxIdPanel.setEnabled(isReturnEditable);
        supplierTaxIdPanel.setVisible(isReturnEditable);
        supplierTaxIdEditor.setEnabled(model.isEditableTaxId());

        altShipToAddressEditor.setEnabled(isReturnEditable);
        pickupDateEditor.setEnabled(isReturnEditable);

        weightEditor.setEnabled(isReturnEditable);
        packageTypeEditor.setEnabled(isReturnEditable);
        trackingEditor.setEnabled(isReturnEditable);
    }

    private void validateEditorStateForTransfer() {
        ShipmentCarrierRole carrierType = model.getCarrierType();
        boolean isTransferEditable = model.isTransferEditable();

        shipToAddressPanel.setAddressTypeVisible(false);
        shipToAddressPanel.setAddressLine3Visible(false);
        shipToAddressPanel.setAddressPhoneticNameVisible(false);
        shipToAddressPanel.setCountyVisible(false);
        shipToAddressPanel.setCountryVisible(false);
        billOfLadingMotiveEditor.setEnabled(isTransferEditable);

        carrierRoleEditor.setEnabled(isTransferEditable);
        carrierEditor.setEnabled(isTransferEditable && carrierType == ShipmentCarrierRole.THIRD_PARTY);
        carrierNameEditor.setEnabled(isTransferEditable && carrierType == ShipmentCarrierRole.THIRD_PARTY && model.isOtherCarrier(model.getCarrier()));
        carrierAddressEditor.setEnabled(isTransferEditable && carrierType == ShipmentCarrierRole.THIRD_PARTY && model.isOtherCarrier(model.getCarrier()));

        altShipToAddressEditor.setEnabled(isTransferEditable);
        pickupDateEditor.setEnabled(isTransferEditable);
        serviceEditor.setEnabled(isTransferEditable && carrierType == ShipmentCarrierRole.THIRD_PARTY);

        supplierTaxIdPanel.setEnabled(false);
        supplierTaxIdPanel.setVisible(false);
        supplierTaxIdEditor.setEnabled(false);

        weightEditor.setEnabled(isTransferEditable);
        packageTypeEditor.setEnabled(isTransferEditable);

        trackingEditor.setEnabled(isTransferEditable);
    }

    private void validateEditorStateForDelivery() {
        ShipmentCarrierRole carrierType = model.getCarrierType();
        boolean isDeliveryEditable = model.isDeliveryEditable();

        altShipToAddressEditor.setEnabled(isDeliveryEditable);
        shipToAddressPanel.setAddressTypeEnabled(false);
        shipToAddressPanel.setAddressTypeVisible(false);
        billOfLadingMotiveEditor.setEnabled(isDeliveryEditable);
        carrierRoleEditor.setEnabled(isDeliveryEditable);
        carrierEditor.setEnabled(isDeliveryEditable && carrierType == ShipmentCarrierRole.THIRD_PARTY);
        carrierNameEditor.setEnabled(isDeliveryEditable && model.isOtherCarrier(model.getCarrier()));
        carrierAddressEditor.setEnabled(isDeliveryEditable && model.isOtherCarrier(model.getCarrier()));
        pickupDateEditor.setEnabled(isDeliveryEditable);
        serviceEditor.setEnabled(isDeliveryEditable && carrierType == ShipmentCarrierRole.THIRD_PARTY);
        supplierTaxIdPanel.setEnabled(false);
        supplierTaxIdEditor.setEnabled(false);
        weightEditor.setEnabled(isDeliveryEditable);
        packageTypeEditor.setEnabled(isDeliveryEditable);
        trackingEditor.setEnabled(isDeliveryEditable);
    }

    public boolean isEditable() {
        if (model.isReturn()) {
            return model.isReturnEditable();
        } else if (model.isTransfer()) {
            return model.isTransferEditable();
        }
        return model.isDeliveryEditable();
    }

    /****************************************************************************************************
     * Handle Cancel
     ***************************************************************************************************/

    public void handleCancel() throws Exception {
        model.storeShipment();
    }

    /****************************************************************************************************
     * Handle Done
     ***************************************************************************************************/

    public boolean handleSave() throws Exception {
        if (!isEditableState()) {
            return true;
        }
        JComponent focusEditor = billOfLadingMotiveEditor;
        try {
            // Bill Of Lading Motive
            focusEditor = billOfLadingMotiveEditor;
            model.setBillOfLadingMotive((BillOfLadingMotive) billOfLadingMotiveEditor.getSelectedItem());

            focusEditor = carrierRoleEditor;
            setCarrierRoleType();

            focusEditor = carrierEditor;
            model.setCarrier((ShipmentCarrier) carrierEditor.getSelectedItem());

            if (model.getCarrierType() == ShipmentCarrierRole.THIRD_PARTY) {
                focusEditor = carrierNameEditor;
                model.setAlternateCarrierName(carrierNameEditor.getTextOrNull());
                focusEditor = carrierAddressEditor;
                model.setAlternateCarrierAddress(carrierAddressEditor.getTextOrNull());
            } else {
                focusEditor = carrierNameEditor;
                model.setAlternateCarrierName(null);
                focusEditor = carrierAddressEditor;
                model.setAlternateCarrierAddress(null);
            }

            focusEditor = serviceEditor;
            model.setCarrierService((ShipmentCarrierService) serviceEditor.getSelectedItem());

            focusEditor = packageTypeEditor;
            model.setCartonType((ShipmentCartonType) packageTypeEditor.getSelectedItem());

            focusEditor = trackingEditor;
            model.setTrackingNumber(trackingEditor.getText());

            focusEditor = weightEditor;
            model.setWeight(weightEditor.getQuantity());

            focusEditor = pickupDateEditor;
            model.setRequestedPickupDate(pickupDateEditor.getDate());

            if (model.isReturn() && !model.isWarehouseReturn()) {
                focusEditor = shipToAddressPanel.getAddressTypeEditor();
                model.setShipToAddressType(shipToAddressPanel.getSelectedAddressType());
            }

            focusEditor = altShipToAddressEditor;
            model.setAlternateShipToAddress(altShipToAddressEditor.getTextOrNull());

            if (model.isReturn() && model.isReturnEditable()) {
                focusEditor = supplierTaxIdEditor;
                model.setSupplierTaxId(supplierTaxIdEditor.getTextOrNull());
            }

            if (validateThirdPartyInfo() && validateCarrierServiceInfo()) {
                model.storeShipment();
                return true;
            }

        } catch (Throwable e) {
            displayException(e, focusEditor);
        }
        return false;
    }

    private boolean validateThirdPartyInfo() {
        ShipmentCarrierRole carrierType = model.getCarrierType();
        if (carrierType == ShipmentCarrierRole.THIRD_PARTY && model.getCarrier() == null) {
            displayWarning(CommonMessageText.CARRIER_TYPE_REQUIRED);
            return false;
        }
        return true;
    }

    private boolean validateCarrierServiceInfo() {
        ShipmentCarrierService carrierService = model.getCarrierService();
        if (carrierService == null) {
            return true;
        }
        if (carrierService.isCartonTypeRequired() && model.getCartonType() == null) {
            displayWarning(ShipmentMessageText.CARTON_TYPE_REQUIRED);
            return false;
        }
        if (carrierService.isWeightRequired() && model.getWeight() == null) {
            displayWarning(ShipmentMessageText.WEIGHT_REQUIRED);
            return false;
        }
        return true;
    }

    private void setCarrierRoleType() throws BusinessException {
        if (carrierRoleEditor.isSelected(RECEIVER)) {
            model.setCarrierType(ShipmentCarrierRole.RECEIVER);
        } else if (carrierRoleEditor.isSelected(THIRD_PARTY)) {
            model.setCarrierType(ShipmentCarrierRole.THIRD_PARTY);
        } else {
            model.setCarrierType(ShipmentCarrierRole.SENDER);
        }
    }

    private boolean isEditableState() {
        if (model.isReturn()) {
            return model.isReturnEditable();
        } else if (model.isTransfer()) {
            return model.isTransferEditable();
        } else {
            return model.isDeliveryEditable();
        }
    }

    private String getWeightEditorText() {
        return Translator.getText("Weight") + StringConstants.SPACE + "(" + Translator.getText(model.getWeightUomLabel()) + ")";
    }

    private void displayException(Throwable exception, JComponent editor) {
        displayException(exception);
        assignFocusInScreen(editor);
    }

    /****************************************************************************************************
     * Screen Events
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(ADDRESS_TYPE_MODIFIED)) {
            doAddressTypeModified();
        } else if (command.equals(CARRIER_TYPE_MODIFIED)) {
            doCarrierTypeModified();
        } else if (command.equals(CARRIER_MODIFIED)) {
            doCarrierModified();
        }
    }

    private void doAddressTypeModified() {
        AddressType addressType = shipToAddressPanel.getSelectedAddressType();
        try {
            shipToAddressPanel.setPostalAddress(model.getShipToAddress(addressType));
        } catch (Throwable exception) {
            displayException(exception);
            shipToAddressPanel.setAddressType(model.getShipToAddressType());
            assignFocusInScreen(shipToAddressPanel.getAddressTypeEditor());
        }
    }

    private void doCarrierTypeModified() {
        try {
            ShipmentCarrierRole carrierType = ShipmentCarrierRole.SENDER;
            if (carrierRoleEditor.isSelected(RECEIVER)) {
                carrierType = ShipmentCarrierRole.RECEIVER;
            } else if (carrierRoleEditor.isSelected(THIRD_PARTY)) {
                carrierType = ShipmentCarrierRole.THIRD_PARTY;
            }

            if (carrierType == ShipmentCarrierRole.THIRD_PARTY) {
                carrierEditor.setEnabled(true);
                serviceEditor.setEnabled(true);
                if (model.getCarrier() == null) {
                    ShipmentCarrier otherCarrier = model.getOtherCarrier();
                    model.setCarrier(otherCarrier);
                    serviceEditor.setItems(model.getCarrierServices(otherCarrier));
                }
                carrierEditor.setSelectedItem(model.getCarrier());
                serviceEditor.setSelectedItem(model.getCarrierService());
                if (model.isOtherCarrier(model.getCarrier())) {
                    carrierNameEditor.setEnabled(true);
                    carrierAddressEditor.setEnabled(true);
                }
            } else {
                carrierEditor.setSelectedItem(null);
                carrierEditor.setEnabled(false);
                serviceEditor.setSelectedItem(null);
                serviceEditor.setEnabled(false);
                carrierNameEditor.setText(null);
                carrierAddressEditor.setText(null);
                carrierNameEditor.setEnabled(false);
                carrierAddressEditor.setEnabled(false);
            }
        } catch (Exception exception) {
            displayException(exception);
            assignFocusInScreen(carrierRoleEditor);
        }
    }

    private void doCarrierModified() {
        try {
            ShipmentCarrier carrier = (ShipmentCarrier) carrierEditor.getSelectedItem();
            if (carrier == null) {
                return;
            }
            serviceEditor.setItems(model.getCarrierServices(carrier));
            if (model.isOtherCarrier(carrier)) {
                carrierNameEditor.setText(model.getAlternameCarrierName());
                carrierAddressEditor.setText(model.getAlternateCarrierAddress());
                carrierNameEditor.setEnabled(true);
                carrierAddressEditor.setEnabled(true);
            } else {
                carrierNameEditor.setText(null);
                carrierAddressEditor.setText(null);
                carrierNameEditor.setEnabled(false);
                carrierAddressEditor.setEnabled(false);
            }
        } catch (Exception exception) {
            displayException(exception);
            assignFocusInScreen(carrierEditor);
        }
    }
}