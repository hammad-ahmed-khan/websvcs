package oracle.retail.sim.client.screen.scanner;

import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.ItemType;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * This dialog handles entering advanced item scanning (for StockItem based areas).
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class StockItemScannerDialog extends ItemScannerDialog {
    private static final long serialVersionUID = 2609707232448043260L;

    boolean validateBarcodeItem(BarcodeItem barcodeItem) throws Exception {
        ItemType itemType = barcodeItem.getItemType();
        if (itemType == ItemType.CONSIGNMENT) {
            displayErrorStatus(CommonMessageText.CONSIGNMENT_ITEM_ERROR);
            doClearDetails();
            doResetBarcode();
            return false;
        }
        if (itemType == ItemType.CONCESSION) {
            displayErrorStatus(CommonMessageText.CONCESSION_ITEM_ERROR);
            doClearDetails();
            doResetBarcode();
            return false;
        }
        if (itemType == ItemType.NON_INVENTORY) {
            displayErrorStatus(CommonMessageText.NON_INVENTORY_ITEM_ERROR);
            doClearDetails();
            doResetBarcode();
            return false;
        }

        StockItem stockItem = barcodeItem.getStockItem();
        if (stockItem.isRanged()) {
            return true;
        }
        if (!isAllowNonRangedItems()) {
            displayErrorStatus(ItemMessageText.ITEM_NOT_RANGED_ERROR);
            doClearDetails();
            doResetBarcode();
            return false;
        }
        if (!RConfirmUtility.confirm("Non-Ranged Item Confirmation", ItemMessageText.ITEM_ADD_NON_RANGED)) {
            return false;
        }
        stockItem = ClientServiceFactory.getItemServices().readStockItemOrCreate(stockItem.getId(), SimRepository.getStoreId());
        if (stockItem == null) {
            return false;
        }
        barcodeItem.doSetStockItem(stockItem);
        return true;
    }
}
