package oracle.retail.sim.client.screen.itemticket;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.CountryDisplayer;
import oracle.retail.sim.client.displayer.PricePerUomDisplayer;
import oracle.retail.sim.client.displayer.SimMoneyDisplayer;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.editor.SimMoneyFieldEditor;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.screen.scanner.ItemScannerListener;
import oracle.retail.sim.client.screen.scanner.StockItemScannerDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.util.NoPrinterDefinedException;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.RetailItem;
import oracle.retail.sim.common.itemticket.ItemTicket;
import oracle.retail.sim.common.itemticket.ItemTicketMessageText;
import oracle.retail.sim.common.itemticket.TicketType;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;
import oracle.retail.sim.common.itemticket.TicketTypeId;
import oracle.retail.sim.common.report.ReportMessageText;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.StorePrinter;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;

/********************************************************************************************************
 * Item Ticket Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemTicketDetailPanel extends ScreenPanel implements REventListener, ItemScannerListener {
    private static final long serialVersionUID = -8032370002710207337L;

    private final ItemTicketDetailModel model = new ItemTicketDetailModel();

    private final RDisplayLabelEditor suggestedTicketTypeEditor = new RDisplayLabelEditor("Ticket Type");
    private final RDisplayLabelEditor effectiveDateEditor = new RDisplayLabelEditor("Effective Date");
    private final RDisplayLabelEditor userEditor = new RDisplayLabelEditor("User");

    private final PricePerUomDisplayer pricePerUomDisplayer = new PricePerUomDisplayer();
    private final RSearchFieldEditor retailItemEditor = SimEditorFactory.createRetailItemSearchFieldEditor(false);

    private final RComboBoxEditor labelTypeEditor = new RComboBoxEditor("Label Type");
    private final RComboBoxEditor formatTypeEditor = new RComboBoxEditor("Format");
    private final RComboBoxEditor countryMfrEditor = new RComboBoxEditor("Country of Manufacture");
    private final RDisplayLabelEditor labelPriceEditor = new RDisplayLabelEditor("Label Price");
    private final SimMoneyFieldEditor overridePriceEditor = new SimMoneyFieldEditor("Override Ticket Price");
    private final RIntegerFieldEditor numberTicketsEditor = new RIntegerFieldEditor("Quantity");

    private final RDisplayLabelEditor promotionIdEditor = new RDisplayLabelEditor("Promotion ID");
    private final RCheckBoxEditor multiUnitPriceChgIndEditor = new RCheckBoxEditor("Multi Unit Price Change");
    private final RCheckBoxEditor agsnEditor = new RCheckBoxEditor("Auto Generate");
    private final RDisplayLabelEditor multiUnitPriceEditor = new RDisplayLabelEditor("Multi Unit Price");
    private final RDisplayLabelEditor multiUnitQtyEditor = new RDisplayLabelEditor("Multi Unit Quantity");
    private final RDisplayLabelEditor pricePerUomEditor = new RDisplayLabelEditor("Price per UOM");

    private final ItemTicketSelectUinDialog serialNumberDialog = new ItemTicketSelectUinDialog();

    private final SimTable uinTable = new SimTable(new SelectUINTableDefinition());
    private final SimTablePane uinTablePane = new SimTablePane(uinTable);

    private StockItemScannerDialog scannerDialog;

    private static final String ITEM_MODIFIED = "Item.modified";
    private static final String LABEL_TYPE_MODIFIED = "LabelType.modified";
    private static final String FORMAT_MODIFIED = "Format.modified";
    private static final String COUNTRY_MODIFIED = "Country.modified";
    private static final String PRICE_MODIFIED = "Price.modified";
    private static final String QUANTITY_MODIFIED = "Quantity.modified";

    /****************************************************************************************************
     * Build Screen
     ***************************************************************************************************/

    public ItemTicketDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        uinTable.setMultipleRowSelectionMode();
        overridePriceEditor.setIdentifier(SimName.ITEM_TICKET_OVERRIDE_PRICE);
        numberTicketsEditor.setIdentifier(SimName.ITEM_TICKET_QUANTITY);

        overridePriceEditor.setSizeType(EditorConstants.SMALL);
        numberTicketsEditor.setSizeType(EditorConstants.SMALL);

        labelPriceEditor.setDisplayer(new SimMoneyDisplayer());
        effectiveDateEditor.setDataType(DataTypeConstants.DATE_SHORT);
        labelTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        formatTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        countryMfrEditor.setDisplayer(new CountryDisplayer());
        multiUnitPriceEditor.setDisplayer(new SimMoneyDisplayer());

        retailItemEditor.registerAction(this, ITEM_MODIFIED);
        labelTypeEditor.registerAction(this, LABEL_TYPE_MODIFIED);
        formatTypeEditor.registerAction(this, FORMAT_MODIFIED);
        countryMfrEditor.registerAction(this, COUNTRY_MODIFIED);
        overridePriceEditor.registerAction(this, PRICE_MODIFIED);
        numberTicketsEditor.registerAction(this, QUANTITY_MODIFIED);

        serialNumberDialog.addREventListener(this);

        retailItemEditor.setSearchListener(buildItemSearchListener());
    }

    private void layoutScreen() {
        REditorPanel leftPanel = new REditorPanel(15, 1);
        leftPanel.setTitleBorder("Price");
        leftPanel.add(suggestedTicketTypeEditor);
        leftPanel.add(labelTypeEditor);
        leftPanel.add(formatTypeEditor);
        leftPanel.add(labelPriceEditor);
        leftPanel.add(agsnEditor);
        leftPanel.add(numberTicketsEditor);
        leftPanel.add(overridePriceEditor);
        leftPanel.add(countryMfrEditor);
        leftPanel.add(pricePerUomEditor);
        leftPanel.add(userEditor);
        leftPanel.add(promotionIdEditor);
        leftPanel.add(effectiveDateEditor);
        leftPanel.add(multiUnitPriceChgIndEditor);
        leftPanel.add(multiUnitPriceEditor);
        leftPanel.add(multiUnitQtyEditor);

        RPanel rightPanel = new RPanel(new BorderLayout());
        if (model.isSerialNumberProcessingEnabled() && model.hasPermission(PermissionKey.PC_PRINT_UIN_AGSN_TICKET)) {
            rightPanel.setTitleBorder("Selected UINs");
            rightPanel.add(uinTablePane, BorderLayout.CENTER);
        } else {
            rightPanel.add(new JLabel(), BorderLayout.CENTER);
            agsnEditor.setVisible(false);
        }

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(retailItemEditor, GridTool.constraints(0, 0, 2, 1, 1, 0, 0, 3, 10, 0, 10, 0));
        mainPanel.add(leftPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 10, 0));
        mainPanel.add(rightPanel, GridTool.constraints(1, 1, 1, 1, 2, 0, 0, 3, 0, 0, 10, 0));
        mainPanel.add(new JLabel(), GridTool.constraints(0, 2, 2, 1, 1, 1, 0, 3, 0, 0, 10, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return uinTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadItemTicket();
        model.loadPricePerUom();
        if (!model.isItemTicketUnmodifiable() && !model.obtainItemTicketLock()) {
            model.setLockNotBroken(true);
        }
        populateScreen();
        launchScanner();
    }

    public void resume() {
        assignFocusInScreen(overridePriceEditor);
    }

    public void assignFocusInScreen() {
        if (model.getItemTicket() == null) {
            assignFocusInScreen(retailItemEditor);
        } else {
            assignFocusInScreen(overridePriceEditor);
        }
    }

    private void displayRetailItem(ItemVO itemVO) {
        try {
            RetailItem retailItem = model.getRetailItem(itemVO);

            if (retailItem.isSellable()) {
                retailItemEditor.setData(retailItem);
            } else {
                displayError(ItemMessageText.ITEM_NON_SELLABLE_PACK_ERROR);
                retailItemEditor.setData(null);
            }
        } catch (Throwable exception) {
            displayError(ItemMessageText.ITEM_NON_SELLABLE_PACK_ERROR);
            retailItemEditor.setData(null);
        }
    }

    public boolean isItemTicketUnmodifiable() {
        return model.isItemTicketUnmodifiable();
    }

    public boolean isUINProcessDisabled() {
        return !model.isSerialNumberProcessingEnabled();
    }

    /****************************************************************************************************
     * Scanner Methods
     ***************************************************************************************************/

    public boolean isScannerAvailable() {
        return model.isScannerAvailable();
    }

    private void launchScanner() {
        if (model.isScannerAvailable()) {
            if (model.isScannerAutoDisplay()) {
                displayScanner();
            }
        }
    }

    private void displayScanner() {
        if (model.isScannerAvailable()) {
            if (scannerDialog == null) {
                scannerDialog = new StockItemScannerDialog();
                scannerDialog.setItemProcessor(this);
            }
            scannerDialog.setVisible(true);
        }
    }

    private void shutdownScanner() {
        if (scannerDialog != null) {
            scannerDialog.setVisible(false);
            scannerDialog = null;
        }
    }

    public void processBarcodeItem(BarcodeItem barcodeItem) {
        retailItemEditor.setText(barcodeItem.getId());
    }

    /****************************************************************************************************
     * Populate Screen
     ***************************************************************************************************/

    private void populateScreen() throws Exception {
        ItemTicket itemTicket = model.getItemTicket();

        if (itemTicket == null) {
            clearEditorInformation();
            return;
        }

        retailItemEditor.setEnabled(model.isItemEditable());

        if (model.isLabelAndFormatEditable()) {
            labelTypeEditor.setEnabled(true);
            formatTypeEditor.setEnabled(true);
            countryMfrEditor.setEnabled(model.isCountryModifiable());
        } else {
            labelTypeEditor.setEnabled(false);
            formatTypeEditor.setEnabled(false);
            countryMfrEditor.setEnabled(false);
        }

        labelTypeEditor.setActionsEnabled(false);
        labelTypeEditor.removeEmptySelection();
        labelTypeEditor.setActionsEnabled(true);

        //on labeltypeEditor modify action will populate the formatTypeEditor
        formatTypeEditor.setActionsEnabled(false);
        formatTypeEditor.removeEmptySelection();
        formatTypeEditor.setActionsEnabled(true);

        agsnEditor.setEnabled(false);
        multiUnitPriceChgIndEditor.setEnabled(false);
        pricePerUomEditor.setEnabled(false);

        loadEditorInformation(itemTicket);

        if (isItemTicketUnmodifiable()) {
            retailItemEditor.setEnabled(false);
            labelTypeEditor.setEnabled(false);
            formatTypeEditor.setEnabled(false);
            countryMfrEditor.setEnabled(false);
            agsnEditor.setEnabled(false);
            multiUnitPriceChgIndEditor.setEnabled(false);
            overridePriceEditor.setEnabled(false);
            numberTicketsEditor.setEnabled(false);
        }
        if (itemTicket.getRetailItem().isAGSNEnabled()) {
            agsnEditor.setEnabled(true);
        }
    }

    /****************************************************************************************************
     * Populate Editors
     ***************************************************************************************************/

    private void loadEditorInformation(ItemTicket itemTicket) throws Exception {
        RetailItem retailItem = itemTicket.getRetailItem();
        if (retailItemEditor.getData() == null) {
            retailItemEditor.setData(retailItem);
        }
        RetailItem oldRetailItem = (RetailItem) retailItemEditor.getData();
        if (!oldRetailItem.getId().equals(retailItem.getId())) {
            retailItemEditor.setData(retailItem);
        }

        labelTypeEditor.setItems(model.loadLabelTypes());
        TicketTypeFormat typeFormat = itemTicket.getTicketTypeFormat();

        if (typeFormat != null && typeFormat.getTicketType() != null) {
            labelTypeEditor.setSelectedItem(typeFormat.getTicketType());

            if (typeFormat.getTicketType().getId().equals(TicketTypeId.SHELF_LABEL_ID)) {
                suggestedTicketTypeEditor.clear();
            } else {
                suggestedTicketTypeEditor.setData(retailItem.getSuggestedTicketTypeCode());
            }

            formatTypeEditor.setItems(model.findTicketTypeFormats(typeFormat.getTicketType().getId()));
            formatTypeEditor.setSelectedItem(typeFormat);
        } else {
            suggestedTicketTypeEditor.setData(retailItem.getSuggestedTicketTypeCode());

            formatTypeEditor.setItems(model.findTicketTypeFormats(TicketTypeId.ITEM_TICKET_ID));
        }

        String country = itemTicket.getCountryManufacture();
        countryMfrEditor.setItems(model.getCountriesOfManufacture());
        countryMfrEditor.setSelectedItem(country);

        effectiveDateEditor.setData(itemTicket.getEffectiveDate());
        promotionIdEditor.setData(itemTicket.getPromotionId());

        if (itemTicket.getLabelPrice() != null) {
            labelPriceEditor.setData(itemTicket.getLabelPrice());
        } else {
            labelPriceEditor.setData(retailItem.getRetailPrice());
        }

        userEditor.setData(itemTicket.getUserId());

        if (itemTicket.getOverridePrice() != null) {
            overridePriceEditor.setMoney(itemTicket.getOverridePrice());
        } else {
            overridePriceEditor.clear();
        }
        overridePriceEditor.setEnabled(model.isOverridePriceModifiable());

        if (itemTicket.getQuantity() > 0) {
            numberTicketsEditor.setInteger(itemTicket.getQuantity().intValue());
        } else {
            numberTicketsEditor.clear();
        }
        numberTicketsEditor.setEnabled(model.isQuantityModifiable());

        multiUnitPriceChgIndEditor.setSelected(itemTicket.isMultiUnitPriceChange());
        multiUnitPriceEditor.setData(itemTicket.getMultiUnitLabelPrice());
        multiUnitQtyEditor.setData(itemTicket.getMultiUnits());

        String pricePerUom = pricePerUomDisplayer.getDisplayText(itemTicket.getPricePerUom(), itemTicket);
        pricePerUomEditor.setData(pricePerUom);

        if (model.isAGSNEnabled()) {
            uinTable.setRows(itemTicket.getSerialNumbers());
        }
    }

    /****************************************************************************************************
     * Clear Editors
     ***************************************************************************************************/

    private void clearEditorInformation() {
        setActionsEnabled(false);
        retailItemEditor.clear();
        suggestedTicketTypeEditor.clear();
        formatTypeEditor.setEmptySelection();
        countryMfrEditor.setEmptySelection();
        labelTypeEditor.setEmptySelection();
        effectiveDateEditor.clear();
        promotionIdEditor.clear();
        labelPriceEditor.clear();
        overridePriceEditor.clear();
        numberTicketsEditor.clear();
        userEditor.setData(model.getUserName());
        agsnEditor.setSelected(false);
        multiUnitPriceChgIndEditor.setSelected(false);
        multiUnitPriceEditor.clear();
        multiUnitQtyEditor.clear();
        pricePerUomEditor.clear();
        uinTable.clearRows();

        setActionsEnabled(true);
        validateEnabledState();
    }

    public boolean isTicketCancelled() {
        if (model.getItemTicket() == null) {
            return false;
        }
        return model.isTicketCancelled();
    }

    /****************************************************************************************************
     * Handle STOP
     ***************************************************************************************************/

    public void stop() {
        try {
            model.releaseTicketLock();
        } catch (Throwable e) {
            displayException(e);
        }
        shutdownScanner();
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public void handleSave() throws Exception {
        if (model.isAGSNEnabled()) {
            model.getItemTicket().setQuantity(numberTicketsEditor.getInteger());
        }

        if (model.isTicketValidForUpdate()) {
            if (model.isNewTicket()) {
                model.updateTicket((TicketType) labelTypeEditor.getSelectedItem());
                return;
            }
            if (model.isTicketCancelled() || model.isTicketPrinted()) {
                return;
            }
            if (model.checkTicketLock()) {
                model.updateTicket((TicketType) labelTypeEditor.getSelectedItem());
                model.releaseTicketLock();
            } else {
                displayError(CommonMessageText.LOCK_TAKEN_OVER);
            }
        }
    }

    /****************************************************************************************************
     * Handle Uin lookup
     ***************************************************************************************************/

    public void handleUinLookup() throws Exception {
        if (retailItemEditor.isEmpty()) {
            throw new BusinessException(ItemMessageText.ITEM_MISSING);
        }
        if (agsnEditor.isSelected()) {
            displayError(UINMessageText.AGSN_CHECK_ERROR);
            return;
        }
        // RetailItem retailItem = (RetailItem) retailItemEditor.getData();

        if (!model.isAGSNEnabled()) {
            displayError(UINMessageText.ITEM_NOT_UIN_ENABLED);
            return;
        }
        agsnEditor.setEnabled(false);
        numberTicketsEditor.setText("1");

        serialNumberDialog.setItemTicketLineItemWrapper(model.buildNewLineItemWrapper(model.getItemTicket()));
        serialNumberDialog.setVisible(true);
    }

    /****************************************************************************************************
     * Handle Removal
     ***************************************************************************************************/

    public void handleUinRemove() throws Exception {
        if (retailItemEditor.isEmpty()) {
            throw new BusinessException(ItemMessageText.ITEM_MISSING);
        }
        if (!model.isAGSNEnabled()) {
            displayError(UINMessageText.ITEM_NOT_UIN_ENABLED);
            return;
        }
        if (uinTable.getSelectedRowCount() < 1) {
            displayError(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        List<Object> objects = uinTable.getAllSelectedRowData();
        for (Object object : objects) {
            uinTable.removeRow(object);
            SerialNumberValue value = (SerialNumberValue) object;
            model.getItemTicket().removeSerialNumber(value.getUin());
        }
        agsnEditor.setEnabled(uinTable.getRowCount() < 1);
    }

    /****************************************************************************************************
     * Handle Scanner
     ***************************************************************************************************/

    public void handleScanner() throws Exception {
        displayScanner();
    }

    /****************************************************************************************************
     * Handle Print
     ***************************************************************************************************/

    public void handlePrintTickets() throws Exception {
        model.setSerialNumbers(uinTable.getAllRowData());
        handleSave();

        StorePrinter printerSelected = null;
        try {
            printerSelected = model.selectPrinter(model.getItemTicket());
        } catch (NoPrinterDefinedException noPrinterException) {
            displayError(ReportMessageText.NO_STORE_PRINTERS);
        }
        if (printerSelected == null) {
            return;
        }

        boolean generateUIN = model.isAGSNEnabled() && agsnEditor.isSelected();
        ReportResponse response = ItemTicketPrintUtility.createSuccessResponse();

        Map<String, List<String>> itemUins = new HashMap<>();

        if (model.isAGSNFormatType() && model.isAGSNEnabled()) {
            if (generateUIN) {
                //generte and save item ticket uin
                Map<String, List<SerialNumberValue>> itemAgsns = model.generateAGSNs();
                itemUins = model.getItemTicketUinsByValue(itemAgsns, printerSelected);

            } else {
                itemUins = model.getItemTicketUins();
            }
            if (!validateUin(itemUins)) {
                return;
            }
            response = model.printUINs(itemUins, printerSelected);
        } else {
            if (!validateUin(itemUins)) {
                return;
            }
            response = model.printTicket(printerSelected);
        }
        if (isPrintResponseSuccess(printerSelected, response)) {
            model.markTicketSentPrint();
            displayMessage(ItemTicketMessageText.ITEM_TICKET_PRINTED, printerSelected.getDescription());
            return;
        }

    }

    private boolean validateUin(Map<String, List<String>> itemUins) {
        if (model.isAGSNFormatType()) {
            if (itemUins == null || itemUins.size() == 0) {
                displayError(CommonMessageText.UIN_REQUIRED, model.getTicktTypeDescription());
                return false;
            }
        }
        return true;

    }

    private boolean isPrintResponseSuccess(StorePrinter printerSelected, ReportResponse response) {
        if (response == null) {
            displayError(ReportMessageText.PRINTING_ERROR, printerSelected.getDescription());
            return false;
        } else if (response != null && response.isFailedState()) {
            if (response.getMessage() != null) {
                displayError(response.getMessage(), response.getMessageValue());
            } else if (response.getPrintResponse() != null) {
                displayException(new Exception(response.getPrintResponse()));
            }
            return false;
        }
        return true;
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(LABEL_TYPE_MODIFIED)) {
                doLabelTypeModified();
            } else if (command.equals(FORMAT_MODIFIED)) {
                doFormatModified();
            } else if (command.equals(PRICE_MODIFIED)) {
                doPriceModified();
            } else if (command.equals(QUANTITY_MODIFIED)) {
                doQuantityModified();
            } else if (command.equals(ITEM_MODIFIED)) {
                doItemModified();
            } else if (command.equals(COUNTRY_MODIFIED)) {
                doCountryModified();
            } else if (command.equals(SimClientStateKey.ITEM_TICKET_APPLY_SELECTED_UIN)) {
                doApplySelectedSerialNumbers(event.getEventData());
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doApplySelectedSerialNumbers(Object eventData) {
        uinTable.setRows((List<SerialNumberValue>) eventData);
    }

    private void doLabelTypeModified() throws Exception {
        Long ticketTypeId;
        TicketType labelType = (TicketType) labelTypeEditor.getSelectedItem();
        if (labelType == null) {
            ticketTypeId = TicketTypeId.ITEM_TICKET_ID;
        } else {
            ticketTypeId = labelType.getId();
        }

        List<TicketTypeFormat> validTicketTypes = model.findTicketTypeFormats(ticketTypeId);

        formatTypeEditor.setActionsEnabled(false);
        formatTypeEditor.setItems(validTicketTypes);
        formatTypeEditor.setActionsEnabled(true);
        if (ticketTypeId == TicketTypeId.ITEM_TICKET_ID) {
            suggestedTicketTypeEditor.setData(model.getItemTicket().getRetailItem().getSuggestedTicketTypeCode());
        } else {
            suggestedTicketTypeEditor.clear();
        }
        TicketTypeFormat oldTypeFormat = model.getTicketTypeFormat();
        if (validTicketTypes.contains(oldTypeFormat)) {
            formatTypeEditor.setSelectedItem(oldTypeFormat);
        } else {
            formatTypeEditor.setEmptySelection();
        }
    }

    private void doFormatModified() {
        try {
            TicketTypeFormat format = (TicketTypeFormat) formatTypeEditor.getSelectedItem();
            if (format != null) {
                model.getItemTicket().setTicketTypeFormat(format);
            }
        } catch (BusinessException exception) {
            String displayMessage = Translator.getMessage(exception.getPrimaryMessageText().getText(), exception.getPrimaryMessageValues());
            formatTypeEditor.setErrorState(true, displayMessage);
            displayException(exception);
        }
    }

    private void doPriceModified() {
        try {
            ItemTicket itemTicket = model.getItemTicket();
            itemTicket.setOverridePrice(overridePriceEditor.getMoney());
            if (itemTicket.getOverridePrice() != null) {
                itemTicket.setLabelPrice(itemTicket.getOverridePrice());
            }

        } catch (BusinessException exception) {
            String displayMessage = Translator.getMessage(exception.getPrimaryMessageText().getText(), exception.getPrimaryMessageValues());
            overridePriceEditor.setErrorState(true, displayMessage);
            displayException(exception);
            assignFocusInScreen(overridePriceEditor);
        } catch (UIException uiException) {
            String displayMessage = Translator.getMessage(uiException.getPrimaryMessageText().getText(), uiException.getPrimaryMessageValues());
            overridePriceEditor.setErrorState(true, displayMessage);
            displayException(uiException);
            assignFocusInScreen(overridePriceEditor);
        }
    }

    private void doQuantityModified() {
        try {
            model.getItemTicket().setQuantity(numberTicketsEditor.getInteger());
        } catch (BusinessException exception) {
            String displayMessage = Translator.getMessage(exception.getPrimaryMessageText().getText(), exception.getPrimaryMessageValues());
            numberTicketsEditor.setErrorState(true, displayMessage);
            displayException(exception);
            assignFocusInScreen(numberTicketsEditor);
        } catch (UIException uiException) {
            String displayMessage = Translator.getMessage(uiException.getPrimaryMessageText().getText(), uiException.getPrimaryMessageValues());
            numberTicketsEditor.setErrorState(true, displayMessage);
            displayException(uiException);
            assignFocusInScreen(numberTicketsEditor);
        }
    }

    private void doItemModified() throws Exception {
        RetailItem retailItem = (RetailItem) retailItemEditor.getData();
        if (retailItem == null) {
            model.clearItemTicket();
            clearEditorInformation();
            return;
        }
        if (!retailItem.isSellable()) {
            displayError(ItemMessageText.ITEM_NON_SELLABLE_PACK_ERROR);
            retailItemEditor.clear();
            return;
        }

        model.createItemTicket(retailItem);

        labelPriceEditor.setData(model.getItemTicket().getRetailItem().getRetailPrice());
        labelTypeEditor.setItems(model.loadLabelTypes());
        labelTypeEditor.removeEmptySelection();

        if (!model.isCountryModifiable()) {
            countryMfrEditor.setEnabled(false);
        } else {
            countryMfrEditor.setItems(model.getCountriesOfManufacture());
            countryMfrEditor.setSelectedItem(model.getDefaultCountry());
        }

        doLabelTypeModified();
        doFormatModified();

        model.loadPricePerUom();

        ItemTicket itemTicket = model.getItemTicket();

        pricePerUomEditor.setData(pricePerUomDisplayer.getDisplayText(itemTicket.getPricePerUom(), itemTicket));
        uinTable.clearRows();
        validateEnabledState();
    }

    private void doCountryModified() throws Exception {
        try {
            String country = (String) countryMfrEditor.getSelectedItem();
            model.getItemTicket().setCountryManufacture(country);
        } catch (BusinessException exception) {
            countryMfrEditor.setErrorState(true, exception.getLocalizedMessage());
            displayException(exception);
        }
    }

    private void validateEnabledState() {
        boolean isEditable = !retailItemEditor.isEmpty();
        formatTypeEditor.setEnabled(isEditable);
        labelTypeEditor.setEnabled(isEditable);
        countryMfrEditor.setEnabled(isEditable);
        countryMfrEditor.setEnabled(isEditable && model.isCountryModifiable());
        numberTicketsEditor.setEnabled(isEditable);
        overridePriceEditor.setEnabled(isEditable);
        multiUnitPriceChgIndEditor.setEnabled(false);
        agsnEditor.setEnabled(isEditable && model.isAGSNEnabled());
    }

    /****************************************************************************************************
     * Item Search Listener - pops open the item lookup dialog
     ***************************************************************************************************/

    private ItemSearchListener buildItemSearchListener() {
        return new ItemSearchListener() {
            public void assignItem(ItemVO itemVo) {
                try {
                    displayRetailItem(itemVo);
                    populateScreen();
                } catch (Exception exception) {
                    displayException(exception);
                }
                assignFocusInScreen(overridePriceEditor);
            }
        };
    }

    /****************************************************************************************************
     * Select UIN Table Definition
     ***************************************************************************************************/

    private class SelectUINTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return SerialNumberValue.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(1);
            attributes.add(new SimTableAttribute("UIN", "uin"));
            return attributes;
        }
    }
}
