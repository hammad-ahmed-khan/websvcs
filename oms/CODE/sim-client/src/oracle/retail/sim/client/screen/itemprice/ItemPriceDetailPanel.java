package oracle.retail.sim.client.screen.itemprice;

import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.SimMoneyDisplayer;
import oracle.retail.sim.client.displayer.StoreDisplayer;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.editor.SimMoneyFieldEditor;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateRangeEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RMatrixPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.currency.SimMoneyUtility;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.RetailItem;
import oracle.retail.sim.common.itemprice.FuturePriceVO;
import oracle.retail.sim.common.itemprice.ItemPrice;
import oracle.retail.sim.common.itemprice.ItemPriceMessageText;
import oracle.retail.sim.common.itemprice.ItemPriceProperty;
import oracle.retail.sim.common.itemprice.PriceType;

/********************************************************************************************************
 * Price Change Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemPriceDetailPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 6574215139384737243L;

    private ItemPriceDetailModel model = new ItemPriceDetailModel();

    private RDisplayLabelEditor storeEditor = new RDisplayLabelEditor("Store");
    private RSearchFieldEditor itemEditor = SimEditorFactory.createRetailItemSearchFieldEditor(true);
    private RDisplayLabelEditor promoItemEditor = new RDisplayLabelEditor();
    private RDisplayLabelEditor promoIdEditor = new RDisplayLabelEditor("Promotion ID");
    private RDisplayLabelEditor statusEditor = new RDisplayLabelEditor("Status");
    private RComboBoxEditor priceTypeEditor = new RComboBoxEditor("Price Change Type");

    private RDateRangeEditor effectiveDateEditor = new RDateRangeEditor("", "Start Date", "End Date");

    private RDisplayLabelEditor currentPriceEditor = new RDisplayLabelEditor("Current Price");
    private SimMoneyFieldEditor newPriceEditor = new SimMoneyFieldEditor("New Price");
    private RDisplayLabelEditor effectivePriceEditor = new RDisplayLabelEditor("Price on Effective Date");
    private RDisplayLabelEditor sellingUOMEditor = new RDisplayLabelEditor("Selling UOM");
    private RDisplayLabelEditor effectUOMDateEditor = new RDisplayLabelEditor("UOM on Effective Date");

    private RCheckBoxEditor multiUnitPriceChgIndEditor = new RCheckBoxEditor("Multi Unit Price Change");
    private RDisplayLabelEditor multiUnitPriceEditor = new RDisplayLabelEditor("Multi Unit Price");
    private RDisplayLabelEditor multiUnitQtyEditor = new RDisplayLabelEditor("Multi Unit Quantity");
    private RDisplayLabelEditor multiUnitUOM = new RDisplayLabelEditor("Multi Unit UOM");

    private static final String ITEM_MODIFIED = "Item.modified";
    private static final String DESC_MODIFIED = "Description.modified";
    private static final String DATE_MODIFIED = "Date.modified";

    public ItemPriceDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        newPriceEditor.setIdentifier(SimName.ITEM_PRICE);

        storeEditor.setDisplayer(new StoreDisplayer());
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        priceTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        currentPriceEditor.setDisplayer(new SimMoneyDisplayer());
        effectivePriceEditor.setDisplayer(new SimMoneyDisplayer());
        multiUnitPriceChgIndEditor.setEnabled(false);
        multiUnitPriceEditor.setDisplayer(new SimMoneyDisplayer());
        multiUnitQtyEditor.setDisplayer(new QuantityDisplayer());

        itemEditor.registerAction(this, ITEM_MODIFIED);
        itemEditor.setSearchListener(buildItemSearchListener());
        priceTypeEditor.registerAction(this, DESC_MODIFIED);
        effectiveDateEditor.registerAction(this, DATE_MODIFIED);
    }

    private void layoutScreen() {
        REditorPanel itemPanel = new REditorPanel(6);
        itemPanel.setTitleBorder("Item");
        itemPanel.add(storeEditor);
        itemPanel.add(itemEditor);
        itemPanel.add(promoIdEditor);
        itemPanel.add(promoItemEditor);
        itemPanel.add(statusEditor);
        itemPanel.add(priceTypeEditor);

        REditorPanel datePanel = new REditorPanel(1, 2);
        datePanel.setTitleBorder("Date");
        datePanel.add(effectiveDateEditor);

        REditorPanel pricePanel = new REditorPanel(5, 2);
        pricePanel.setTitleBorder("Price");
        pricePanel.add(currentPriceEditor);
        pricePanel.add(effectivePriceEditor);
        pricePanel.add(newPriceEditor);

        pricePanel.add(multiUnitPriceChgIndEditor);
        pricePanel.add(multiUnitPriceEditor);

        pricePanel.add(sellingUOMEditor);
        pricePanel.add(effectUOMDateEditor);
        pricePanel.add(multiUnitQtyEditor);
        pricePanel.add(multiUnitUOM);

        RMatrixPanel mainPanel = new RMatrixPanel(3);
        mainPanel.add(itemPanel, true, false);
        mainPanel.add(datePanel, true, false);
        mainPanel.add(pricePanel, true, false);

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
        model.loadItemPrice();
        if (model.obtainItemPriceLock()) {
            populateScreen();
        }
    }

    public void resume() {
        assignFocusInScreen();
    }

    public void assignFocusInScreen() {
        assignFocusInScreen(itemEditor);
    }

    /****************************************************************************************************
     * State Methods
     ***************************************************************************************************/

    public boolean isViewOnly() {
        return model.isViewOnly();
    }

    /****************************************************************************************************
     * Populate Screen
     ***************************************************************************************************/

    private void populateScreen() throws UIException {
        ItemPrice itemPrice = model.getItemPrice();
        setActionsEnabled(false);
        displayItemPriceItem(itemPrice);
        displayEffectiveDateInfo(itemPrice);
        displayPricingInfo(itemPrice);
        setActionsEnabled(true);
    }

    private void displayItemPriceItem(ItemPrice itemPrice) {
        RetailItem retailItem = itemPrice.getRetailItem();
        if (retailItem != null) {
            itemEditor.setData(retailItem);
        }
        itemEditor.setEnabled(itemPrice.isPropertyModifiable(ItemPriceProperty.RETAIL_ITEM));

        storeEditor.setData(model.getStore());
        promoIdEditor.setData(itemPrice.getPromotionId());
        statusEditor.setData(itemPrice.getStatus());
        priceTypeEditor.setItems(model.findPriceDescriptions());
        priceTypeEditor.setSelectedItem(itemPrice.getPriceType());
    }

    private void displayEffectiveDateInfo(ItemPrice itemPrice) throws UIException {
        Date today = SimDateUtil.getCurrentDateAtStartOfDay(model.getTimeZone());
        if (itemPrice.getId() == null) {
            effectiveDateEditor.setStartDate(SimDateUtil.getTomorrowAtStartOfDay(model.getTimeZone()));
        } else {
            Date startDate = itemPrice.getEffectiveDate();
            Date endDate = itemPrice.getEndDate();
            Calendar startDateCalendar = Calendar.getInstance(model.getTimeZone());
            startDateCalendar.setTime(startDate);
            effectiveDateEditor.setStartDate(startDateCalendar.getTime());
            if (endDate != null) {
                Calendar endDateCalendar = Calendar.getInstance(model.getTimeZone());
                endDateCalendar.setTime(endDate);
                effectiveDateEditor.setEndDate(endDateCalendar.getTime());
            }
        }
        effectiveDateEditor.setValidStartDate(today);

        effectiveDateEditor.setStartDateEnabled(itemPrice.isPropertyModifiable(ItemPriceProperty.EFFECTIVE_DATE));
        effectiveDateEditor.setEndDateEnabled(itemPrice.isPropertyModifiable(ItemPriceProperty.END_DATE));
    }

    private void displayPricingInfo(ItemPrice itemPrice) throws UIException {
        RetailItem retailItem = itemPrice.getRetailItem();

        if (retailItem != null) {
            if (itemPrice.getPriceUom() == null) {
                sellingUOMEditor.setData(retailItem.getSellingUom());
            } else {
                sellingUOMEditor.setData(itemPrice.getPriceUom());
            }
            SimMoney retailPrice = retailItem.getRetailPrice();
            if (retailPrice == null) {
                retailPrice = SimMoneyUtility.getZeroMoney(model.getStore().getCurrencyCode());
            }
            currentPriceEditor.setData(retailPrice);
        }

        if (itemPrice.getPrice() != null) {
            newPriceEditor.setMoney(itemPrice.getPrice());
        } else {
            newPriceEditor.setMoney(null);
            newPriceEditor.setCurrency(Currency.getInstance(model.getStore().getCurrencyCode()));
        }

        if (retailItem != null) {
            priceTypeEditor.setEnabled(itemPrice.isPropertyModifiable(ItemPriceProperty.DESCRIPTION));
            newPriceEditor.setEnabled(itemPrice.isPropertyModifiable(ItemPriceProperty.PRICE));
        } else {
            priceTypeEditor.setEnabled(false);
            newPriceEditor.setEnabled(false);
        }

        if (!itemPrice.isNew()) {
            effectUOMDateEditor.setData(itemPrice.getPriceUom());
            effectivePriceEditor.setData(itemPrice.getPrice());
        }

        effectivePriceEditor.setVisible(itemPrice.isEditable());
        effectUOMDateEditor.setVisible(itemPrice.isEditable());
        multiUnitPriceChgIndEditor.setSelected(itemPrice.isMultiPriceChange());
        multiUnitPriceEditor.setData(itemPrice.getMultiUnitPrice());
        multiUnitQtyEditor.setData(itemPrice.getMultiUnits());
        multiUnitUOM.setData(itemPrice.getMultiUnitUom());
    }

    /****************************************************************************************************
     * Handle Cancel
     ***************************************************************************************************/

    public void stop() {
        storeEditor.clear();
        itemEditor.clear();
        promoItemEditor.clear();
        promoIdEditor.clear();
        statusEditor.clear();
        priceTypeEditor.setEmptySelection();
        effectiveDateEditor.clear();
        currentPriceEditor.clear();
        newPriceEditor.clear();
        effectivePriceEditor.clear();
        sellingUOMEditor.clear();
        effectUOMDateEditor.clear();
        multiUnitPriceChgIndEditor.setSelected(false);

        multiUnitPriceEditor.clear();
        multiUnitQtyEditor.clear();
        multiUnitUOM.clear();

        try {
            model.clearItemPrice();
        } catch (Throwable e) {
            displayException(e);
        }
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public boolean handleSave() throws Exception {
        ItemPrice itemPrice = model.getItemPrice();

        RetailItem retailItem = itemPrice.getRetailItem();
        if (retailItem == null) {
            throw new BusinessException(ItemPriceMessageText.MISSING_ITEM);
        }

        SimMoney newPrice = newPriceEditor.getMoney();
        if (newPrice == null) {
            throw new BusinessException(ItemPriceMessageText.MISSING_PRICE);
        }

        if (!model.validateNewPrice(newPrice)) {
            if (!RConfirmUtility.confirm("Confirmation", ItemPriceMessageText.REDUCE_PRICE_CONFIRM)) {
                return false;
            }
        }
        MessageText message = itemPrice.isNew() ? ItemPriceMessageText.CREATE_CONFIRM : ItemPriceMessageText.MODIFY_CONFIRM;
        if (RConfirmUtility.confirm("Confirmation", message)) {
            Date startDate = effectiveDateEditor.getStartDate();
            if (startDate == null) {
                throw new BusinessException(ItemPriceMessageText.START_DATE_INVALID);
            }
            itemPrice.setEffectiveDate(startDate, model.getTimeZone());
            itemPrice.setPrice(newPriceEditor.getMoney());

            PriceType type = itemPrice.getPriceType();
            if (type != null && !type.equals(PriceType.PERMANENT)) {
                Date endDate = effectiveDateEditor.getEndDate();
                itemPrice.setEndDate(endDate);
            }
            try {
                itemPrice.isCoherent();
            } catch (BusinessException ex) {
                displayException(ex);
                return false;
            }
            try {
                model.requestItemPrice();
            } catch (BusinessException be) {
                displayError(be.getPrimaryMessageText(), be.getPrimaryMessageValues());
                return false;
            } catch (Throwable t) {
                displayError(ItemPriceMessageText.REQUEST_FAILED, t.getLocalizedMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    /****************************************************************************************************
     * Handle Panel Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(ITEM_MODIFIED)) {
            doItemModified();
        } else if (command.equals(DESC_MODIFIED)) {
            doDescriptionModified();
        } else if (command.equals(DATE_MODIFIED)) {
            doDateModified();
        }
    }

    private void doItemModified() {
        RetailItem retailItem = (RetailItem) itemEditor.getData();

        if (retailItem == null) {
            clearItemInformation();
            return;
        }

        ItemPrice itemPrice = model.getItemPrice();

        if (itemPrice != null) {
            try {
                itemPrice.setRetailItem(retailItem);
                refreshItemInformation();
            } catch (Throwable exception) {
                displayException(exception);
                itemEditor.clear();
            }
        }
    }

    private void clearItemInformation() {
        currentPriceEditor.clear();
        newPriceEditor.clear();
        effectivePriceEditor.clear();
        sellingUOMEditor.clear();
        effectUOMDateEditor.clear();
        multiUnitPriceChgIndEditor.setSelected(false);
        validateEnabledState();
    }

    private void refreshItemInformation() {
        ItemPrice itemPrice = model.getItemPrice();
        RetailItem retailItem = itemPrice.getRetailItem();

        if (StringUtility.isNullOrEmpty(itemPrice.getPriceUom())) {
            itemPrice.doSetPriceUom(retailItem.getSellingUom());
        }
        sellingUOMEditor.setData(itemPrice.getPriceUom());
        priceTypeEditor.setSelectedItem(itemPrice.getPriceType());

        SimMoney retailPrice = retailItem.getRetailPrice();
        if (retailPrice == null) {
            retailPrice = SimMoneyUtility.getZeroMoney(model.getStore().getCurrencyCode());
        }
        currentPriceEditor.setData(retailPrice);

        validateEnabledState();
        doDateModified();
    }

    private void validateEnabledState() {
        ItemPrice itemPrice = model.getItemPrice();

        priceTypeEditor.setEnabled(itemPrice.isPropertyModifiable(ItemPriceProperty.DESCRIPTION));
        itemEditor.setEnabled(itemPrice.isPropertyModifiable(ItemPriceProperty.EFFECTIVE_DATE));
        effectiveDateEditor.setStartDateEnabled(itemPrice.isPropertyModifiable(ItemPriceProperty.EFFECTIVE_DATE));
        effectiveDateEditor.setEndDateEnabled(itemPrice.isPropertyModifiable(ItemPriceProperty.END_DATE));
        newPriceEditor.setEnabled(itemPrice.isPropertyModifiable(ItemPriceProperty.PRICE));
    }

    private void doDescriptionModified() {
        PriceType priceType = (PriceType) priceTypeEditor.getSelectedItem();

        if (priceType != null) {
            ItemPrice itemPrice = model.getItemPrice();
            try {
                itemPrice.setPriceType(priceType);
            } catch (BusinessException exception) {
                displayException(exception);
            }
            effectiveDateEditor.setEndDateEnabled(itemPrice.isPropertyModifiable(ItemPriceProperty.END_DATE));
            effectiveDateEditor.setEndDate(itemPrice.getEndDate());
        }
    }

    private void doDateModified() {
        effectUOMDateEditor.clear();
        effectivePriceEditor.clear();
        try {
            if (effectiveDateEditor.getStartDate() != null) {
                FuturePriceVO futurePrice = model.getFuturePrice(effectiveDateEditor.getStartDate());

                if (futurePrice != null) {
                    effectUOMDateEditor.setData(futurePrice.getPriceUom());
                    effectivePriceEditor.setData(futurePrice.getPrice());
                } else {
                    effectUOMDateEditor.clear();
                    effectivePriceEditor.clear();
                }
            }
        } catch (BusinessException be) {
            displayError(be.getPrimaryMessageText(), be.getPrimaryMessageValues());

        } catch (Throwable t) {
            displayException(t);

        }
    }

    /****************************************************************************************************
     * Item Search Listener - pops open the item lookup dialog
     ***************************************************************************************************/

    private ItemSearchListener buildItemSearchListener() {
        return new ItemSearchListener() {
            public void assignItem(ItemVO itemVO) {
                if (itemVO != null) {
                    itemEditor.setData(itemVO);
                    try {
                        populateScreen();
                    } catch (Exception exception) {
                        displayException(exception);
                    }
                    assignFocusInScreen();
                }
            }
        };
    }
}
