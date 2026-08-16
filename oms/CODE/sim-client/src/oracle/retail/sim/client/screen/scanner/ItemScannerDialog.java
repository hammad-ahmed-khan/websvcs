package oracle.retail.sim.client.screen.scanner;

import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.UIManager;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.dialog.ItemSelectDialog;
import oracle.retail.sim.client.displayer.SimMoneyDisplayer;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RRadioButtonEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.EnterKeyAdapter;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RTextArea;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.item.BarcodeInfo;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.StockItem;

/********************************************************************************************************
 * This dialog handles entering advanced item scanning.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class ItemScannerDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -5757513658874523106L;

    private ItemScannerModel model = new ItemScannerModel();

    private RTextFieldEditor barcodeEditor = new RTextFieldEditor();
    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RDisplayLabelEditor descriptionEditor = new RDisplayLabelEditor("Description");
    private RDisplayLabelEditor quantityEditor = new RDisplayLabelEditor("Quantity");
    private RDisplayLabelEditor priceEditor = new RDisplayLabelEditor("Price");
    private RDisplayLabelEditor serialNumberEditor = new RDisplayLabelEditor("UIN");
    private RRadioButtonEditor receivedDamagedEditor = new RRadioButtonEditor("Quantity Field");
    private RCheckBoxEditor autoApplyEditor = new RCheckBoxEditor("Auto Apply Items");

    private static String RECEIVED = "Received";
    private static String DAMAGED = "Damaged";
    private static String[] radioHeaders = new String[] { RECEIVED, DAMAGED };

    private RLabel statusIcon = new RLabel();
    private RTextArea statusArea = new RTextArea();

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);

    private static String ITEM_ENTERED = "Item.entered";
    private static String AUTO_APPLY_MODIFIED = "AutoApply.modified";

    private ItemScannerListener itemProcessor = null;

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public ItemScannerDialog() {
        super(Application.getFrame(), false);
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        setStatusBarVisible(false);
        setTitle("Advanced Item Entry");
        setSize(300, 325);
        initializeWidgets();
        layoutContent();
        centerWindow();
    }

    private void initializeWidgets() {
        barcodeEditor.setLength(SimName.ITEM_FULL_ENTRY);
        barcodeEditor.registerAction(this, ITEM_ENTERED);
        barcodeEditor.addKeyListener(createScannerKeyListener());
        quantityEditor.setDataType(DataTypeConstants.QUANTITY);
        priceEditor.setDisplayer(new SimMoneyDisplayer());
        autoApplyEditor.setSelected(model.isAutoApply());
        autoApplyEditor.registerAction(this, AUTO_APPLY_MODIFIED);
        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        statusArea.setFont(UIManager.getFont(UIThemeName.OPTIONPANE_FONT));
        statusArea.setDisabledColorSchemeActive(false);
        statusArea.setEnabled(false);
        serialNumberEditor.setVisible(model.isUinProcessingEnabled());
        receivedDamagedEditor.setEnabled(false);
        receivedDamagedEditor.setVisible(false);
        receivedDamagedEditor.setRadioButtons(radioHeaders, 1, 2);
        receivedDamagedEditor.setMinimumWidth(1);
        receivedDamagedEditor.setSelected(RECEIVED, true);
        doAutoAppliedModified();
    }

    private void layoutContent() {
        addButton(applyButton);

        RDivider divider1 = new RDivider(RDivider.HORIZONTAL);
        RDivider divider2 = new RDivider(RDivider.HORIZONTAL);
        RDivider divider3 = new RDivider(RDivider.HORIZONTAL);

        REditorPanel displayPanel = new REditorPanel(6, 1);
        displayPanel.add(itemEditor);
        displayPanel.add(descriptionEditor);
        displayPanel.add(quantityEditor);
        displayPanel.add(priceEditor);
        displayPanel.add(serialNumberEditor);
        displayPanel.add(receivedDamagedEditor);

        RPanel statusPanel = new RPanel(new GridBagLayout());
        statusPanel.add(statusIcon, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 10, 0, 0));
        statusPanel.add(statusArea, GridTool.constraints(1, 0, 1, 1, 1, 1, 0, 3, 0, 10, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(barcodeEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 10, 0, 0));
        mainPanel.add(divider1, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(displayPanel, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(divider2, GridTool.constraints(0, 3, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(statusPanel, GridTool.constraints(0, 4, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        mainPanel.add(divider3, GridTool.constraints(0, 5, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(autoApplyEditor, GridTool.constraints(0, 6, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);

        LayoutUtility.alignEditorsInGridBag(mainPanel);
    }

    public void setItemProcessor(ItemScannerListener itemProcessor) {
        this.itemProcessor = itemProcessor;
    }

    public void activateReceiving() {
        receivedDamagedEditor.setEnabled(true);
        receivedDamagedEditor.setVisible(true);
    }

    /****************************************************************************************************
     * Handle Action Event
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(AUTO_APPLY_MODIFIED)) {
                doAutoAppliedModified();
            } else if (command.equals(ITEM_ENTERED)) {
                doClearDetails();
                doItemEntered();
            } else if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doClearStatus();
                doApply();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Handle Auto Apply Checkbox Change
     ***************************************************************************************************/

    private void doAutoAppliedModified() {
        applyButton.setEnabled(!autoApplyEditor.isSelected());
    }

    /****************************************************************************************************
     * Handle Item Modified In Scanner Field Action
     ***************************************************************************************************/

    private void doItemEntered() throws Exception {
        String barcode = barcodeEditor.getTextOrNull();
        if (barcode == null) {
            return;
        }

        BarcodeItem barcodeItem = model.findExistingBarcodeItem(barcode);
        if (barcodeItem != null) {
            doProcessBarcodeItem(barcode, barcodeItem);
            return;
        }

        BarcodeInfo barcodeInfo = model.findBarcodeItems(barcode);
        List<BarcodeItem> barcodeItems = barcodeInfo.getBarcodeItems();
        if (barcodeItems.isEmpty()) {
            displayErrorStatus(ItemMessageText.ITEM_NOT_FOUND);
            doClearDetails();
            doResetBarcode();
            return;
        }
        doClearStatus();

        if (barcodeItems.size() == 1) {
            doProcessBarcodeItem(barcode, barcodeItems.get(0));
            return;
        }

        ItemSelectDialog dialog = new ItemSelectDialog();
        dialog.setStockItems(model.convertToStockItems(barcodeItems));
        dialog.setVisible(true);

        BarcodeItem selectedItem = model.convertToBarcodeItem(barcodeItems, (StockItem) dialog.getSelectedItem());
        if (selectedItem == null) {
            displayErrorStatus(ItemMessageText.ITEM_NOT_FOUND);
            doClearDetails();
            doResetBarcode();
            return;
        }

        doProcessBarcodeItem(barcode, selectedItem);
    }

    private void doProcessBarcodeItem(String barcode, BarcodeItem barcodeItem) throws Exception {
        model.setSelectedItem(barcode, barcodeItem);

        itemEditor.setData(barcodeItem.getId());
        descriptionEditor.setData(barcodeItem.getStockItem().getItemDescription());
        serialNumberEditor.setData(barcodeItem.getUin());
        if (barcodeItem.isPriceSupported()) {
            priceEditor.setData(barcodeItem.getPrice());
        }
        quantityEditor.setData(barcodeItem.getQuantity());

        if (autoApplyEditor.isSelected()) {
            doApply();
        }
    }

    /****************************************************************************************************
     * Handle Apply Action
     ***************************************************************************************************/

    private void doApply() throws Exception {
        BarcodeItem barcodeItem = model.getSelectedItem();
        if (barcodeItem == null || itemProcessor == null) {
            doClearDetails();
            doResetBarcode();
            return;
        }
        if (validateBarcodeItem(barcodeItem)) {
            doApplyItem(barcodeItem);
        }
        doResetBarcode();
    }

    /****************************************************************************************************
     * Validate the barcode item
     ***************************************************************************************************/
    abstract boolean validateBarcodeItem(BarcodeItem barcodeItem) throws Exception;

    /****************************************************************************************************
     * Handle Applying The Actual Barcode Item To The Item Processor
     ***************************************************************************************************/

    private void doApplyItem(BarcodeItem barcodeItem) {
        try {
            if (receivedDamagedEditor.isEnabled()) {
                if (receivedDamagedEditor.isSelected(DAMAGED)) {
                    barcodeItem.setDamaged();
                } else {
                    barcodeItem.setReceived();
                }
            }
            itemProcessor.processBarcodeItem(barcodeItem);
            model.clearSelectedItem();
            if (autoApplyEditor.isSelected()) {
                return;
            }
            doClearDetails();
        } catch (Exception exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Returns true if dialog allows non-ranged items.
     ***************************************************************************************************/
    boolean isAllowNonRangedItems() {
        return model.isAllowNonRangedItems();
    }

    /****************************************************************************************************
     * Reset The Barcode Field
     ***************************************************************************************************/

    void doResetBarcode() {
        barcodeEditor.clear();
        barcodeEditor.requestFocusInWindow();
    }

    /****************************************************************************************************
     * Clears the Status Area
     ***************************************************************************************************/

    void doClearStatus() {
        statusIcon.setIcon(null);
        statusArea.clear();
    }

    /****************************************************************************************************
     * Clears the Detail Area Of The Window
     ***************************************************************************************************/

    void doClearDetails() {
        itemEditor.clear();
        descriptionEditor.clear();
        quantityEditor.clear();
        priceEditor.clear();
        serialNumberEditor.clear();
    }

    /****************************************************************************************************
     * Closes the window.
     ***************************************************************************************************/
    public void closeWindow() {
        doClearStatus();
        doClearDetails();
        barcodeEditor.clear();
        super.closeWindow();
    }

    /****************************************************************************************************
     * Creates key listener for scanner field.
     ***************************************************************************************************/
    private KeyListener createScannerKeyListener() {
        return new EnterKeyAdapter() {
            public void enterKeyPressed(KeyEvent event) {
                performActionEvent(new RActionEvent(this, ITEM_ENTERED));
            }
        };
    }

    /****************************************************************************************************
     * Handles display exceptions
     ***************************************************************************************************/

    public void displayException(Exception exception) {
        if (exception instanceof BusinessException) {
            BusinessException be = (BusinessException) exception;
            displayErrorStatus(be.getPrimaryMessageText(), be.getPrimaryMessageValues());
            return;
        }
        if (exception instanceof UIException) {
            UIException ie = (UIException) exception;
            if (ie.getSeverity() == RErrorSeverity.FATAL) {
                UIStatusUtility.displayException(this, exception);
            } else if (ie.getSeverity() == RErrorSeverity.ERROR) {
                displayErrorStatus(ie.getPrimaryMessageText(), ie.getPrimaryMessageValues());
            } else {
                displayWarningStatus(ie.getPrimaryMessageText(), ie.getPrimaryMessageValues());
            }
            return;
        }
        UIStatusUtility.displayException(this, exception);
    }

    /****************************************************************************************************
     * Warning Helper Methods
     ***************************************************************************************************/

    void displayWarningStatus(MessageText message) {
        statusIcon.setIcon(UIManager.getIcon(UIThemeName.OPTIONPANE_WARNING_ICON));
        statusArea.setText(Translator.getMessage(message.getText()));
    }

    void displayWarningStatus(MessageText message, Object[] values) {
        statusIcon.setIcon(UIManager.getIcon(UIThemeName.OPTIONPANE_WARNING_ICON));
        statusArea.setText(Translator.getMessage(message.getText(), values));
    }

    /****************************************************************************************************
     * Error Helper Methods
     ***************************************************************************************************/

    void displayErrorStatus(MessageText message) {
        statusIcon.setIcon(UIManager.getIcon(UIThemeName.OPTIONPANE_ERROR_ICON));
        statusArea.setText(Translator.getMessage(message.getText()));
    }

    void displayErrorStatus(MessageText message, Object[] values) {
        statusIcon.setIcon(UIManager.getIcon(UIThemeName.OPTIONPANE_ERROR_ICON));
        statusArea.setText(Translator.getMessage(message.getText(), values));
    }
}
