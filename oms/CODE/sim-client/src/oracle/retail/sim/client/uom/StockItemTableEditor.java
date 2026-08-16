package oracle.retail.sim.client.uom;

import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JComponent;
import oracle.retail.sim.client.core.SimFatalManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.dialog.ItemSelectDialog;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.tableeditor.SimSearchTableEditor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.ItemType;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * A table editor for choosing a ranged only stock item from a search style component regardless of
 * whether or not the system config settings allow non ranged item. Displays only the ID of the item in
 * the field. This is a very unusual table editor that violates many of the design rules of the UI.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockItemTableEditor extends SimSearchTableEditor implements SimTableEditor {
    private static final long serialVersionUID = -5785954896641807069L;

    private final ItemMessageText addNonRangedItemMessage = ItemMessageText.ITEM_ADD_NON_RANGED;

    private final SimTableEditorEventAdaptor eventAdaptor;
    protected String lastCheckedId = StringConstants.EMPTY;

    private FunctionalArea functionalArea = null;
    private boolean allowNonInventoryItems = false;
    private final boolean isNullable = true;

    public StockItemTableEditor() {
        this(null);
    }

    public StockItemTableEditor(FunctionalArea functionalArea) {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        setDisplayer(new AttributeDisplayer("id"));
        setIdentifier(SimName.ITEM_ID);
        this.functionalArea = functionalArea;
    }

    public void setAllowNonInventoryItems(boolean allowItems) {
        allowNonInventoryItems = allowItems;
    }

    public Class getValueClass() {
        return StockItem.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignore
    }

    public void setModel(Object model) {
        // Ignore
    }

    public Object getValue() {
        if (isErrorState()) {
            return null;
        }
        return getSearchData();
    }

    public void setData(Object value) {
        if (value instanceof StockItem) {
            setSearchData(value);
            checkValue();
            eventAdaptor.fireTypeEditorEvent();
            return;
        }
        if (value == null && isNullable) {
            setSearchData(null);
            return;
        }
        throw new IllegalArgumentException("StockItemTableEditor only edits StockItem!");
    }

    public void setValue(Object value) {
        if (value instanceof String) {
            getTextField().setText((String) value);
            checkValue();
        } else {
            setSearchData(value);
        }
        eventAdaptor.fireTypeEditorEvent();
    }

    public JComponent getComponent() {
        return this;
    }

    /****************************************************************************************************
     * Validates the stockable information in the table editor. It will find the stock item and verify
     * its isRanged value. If non-ranged are allowed, then a window will ask the user if they would like
     * to range the item.
     ***************************************************************************************************/
    public boolean checkValue() {
        StockItem stockItem = (StockItem) getSearchData();

        String enteredText = getText();

        setErrorState(false);

        if (StringUtility.isNullOrEmpty(enteredText)) {
            return checkEmptyItemId(stockItem);
        }

        try {
            // Check If Value Has Not Been Modified
            // This used to return false. It now returns true meaning any focus lost event will assume data is valid if it hasnt changed.
            // This could be an issue if the data is in error.
            if (stockItem != null && enteredText.equals(stockItem.getId()) && enteredText.equals(lastCheckedId)) {
                return true;
            }
            if (!StringUtility.isNullOrEmpty(enteredText)) {
                List<StockItem> stockItems = ClientServiceFactory.getItemServices().findStockItems(enteredText, SimRepository.getStoreId());
                if (stockItems.isEmpty()) {
                    stockItem = null;
                } else {
                    stockItem = determineStockItem(stockItems);
                }
            }
            if (stockItem == null) {
                displayErrorWithReset(ItemMessageText.ITEM_NOT_FOUND);
                return false;
            }
            if (!allowNonInventoryItems) {
                if (stockItem.getItemType() == ItemType.CONSIGNMENT) {
                    displayErrorWithClear(CommonMessageText.CONSIGNMENT_ITEM_ERROR);
                    return false;
                }
                if (stockItem.getItemType() == ItemType.CONCESSION) {
                    displayErrorWithClear(CommonMessageText.CONCESSION_ITEM_ERROR);
                    return false;
                }
                if (stockItem.getItemType() == ItemType.NON_INVENTORY) {
                    displayErrorWithClear(CommonMessageText.NON_INVENTORY_ITEM_ERROR);
                    return false;
                }
            }
            if (functionalArea != null) {
                if (functionalArea.isDirectDelivery() && !stockItem.isOrderable()) {
                    displayErrorWithClear(CommonMessageText.NON_ORDERABLE_ITEM_ERROR);
                    return false;
                }
                if (functionalArea.isReturn() && stockItem.isPack() && !stockItem.isOrderable()) {
                    displayErrorWithClear(CommonMessageText.NON_ORDERABLE_ITEM_ERROR);
                    return false;
                }
            }
            if (stockItem.isRanged()) {
                setSearchData(stockItem);
                lastCheckedId = stockItem.getId();
                return true;
            }
            if (!SimConfigManager.getBoolean(SimConfigManager.ALLOW_NON_RANGE_ITEM)) {
                displayErrorWithClear(ItemMessageText.ITEM_NOT_RANGED_ERROR);
                return false;
            }
            if (RConfirmUtility.confirm("Non-Ranged Item Confirmation", addNonRangedItemMessage)) {
                stockItem = ClientServiceFactory.getItemServices().readStockItemOrCreate(stockItem.getId(), SimRepository.getStoreId());
                setSearchData(stockItem);
                lastCheckedId = stockItem.getId();
                return true;
            }
        } catch (BusinessException ruleException) {
            displayError(ruleException);
        } catch (Throwable exception) {
            SimFatalManager.resetApplication(getClass(), exception);
        }
        lastCheckedId = "";
        setSearchData(null);
        return false;
    }

    private StockItem determineStockItem(List<StockItem> items) {
        if (items.size() == 1) {
            return items.get(0);
        }
        ItemSelectDialog dialog = new ItemSelectDialog();
        dialog.setStockItems(items);
        dialog.setVisible(true);
        return (StockItem) dialog.getSelectedItem();
    }

    /****************************************************************************************************
     * Validates empty stockable id and clear out the information. If nullable is not allowed, it display
     * an error.
     ***************************************************************************************************/
    protected boolean checkEmptyItemId(StockItem stockItem) {
        if (isNullable) {
            setSearchData(null);
            return true;
        }
        if (stockItem == null) {
            return false;
        }
        displayErrorWithReset(ItemMessageText.ITEM_BLANK_ERROR);
        return true;
    }

    public void addTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.addTableEditorListener(listener);
    }

    public void removeTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.removeTableEditorListener(listener);
    }

    /****************************************************************************************************
     * Id Value IS Modified
     ***************************************************************************************************/
    protected boolean doValueModified() {
        if (checkValue()) {
            eventAdaptor.fireTypeEditorEvent();
            return true;
        }
        return false;
    }

    /****************************************************************************************************
     * Determines if keystroke is invalid for input into the field.
     ***************************************************************************************************/
    public boolean isInvalidKeystroke(KeyEvent event) {
        return false;
    }
}
