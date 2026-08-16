package oracle.retail.sim.client.screen.scanner;

import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.ItemStatus;
import oracle.retail.sim.common.item.ItemType;
import oracle.retail.sim.common.item.OrderItem;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * This dialog handles entering advanced item scanning (for OrderItem based areas).
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class OrderItemScannerDialog extends ItemScannerDialog {
    private static final long serialVersionUID = -86143033425533264L;

    private OrderItem orderItem = null;

    public OrderItem getOrderItem() {
        return orderItem;
    }

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
        if (!stockItem.isRanged() || stockItem.getStatus() == ItemStatus.AUTO_STOCKABLE) {
            displayErrorStatus(ItemMessageText.ITEM_NOT_RANGED_ERROR);
            doClearDetails();
            doResetBarcode();
            return false;
        }

        orderItem = ClientServiceFactory.getItemServices().readOrderItem(barcodeItem.getId(), barcodeItem.getStockItem().getStoreId(), true);

        if (orderItem == null || !orderItem.isOrderable()) {
            displayErrorStatus(CommonMessageText.NON_ORDERABLE_ITEM_ERROR);
            doClearDetails();
            doResetBarcode();
            return false;
        }
        return true;
    }
}
