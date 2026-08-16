package oracle.retail.sim.client.screen.scanner;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.item.BarcodeInfo;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * General Advanced Item Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemScannerModel extends SimScreenModel {

    private String selectedBarcode = null;
    private BarcodeItem selectedBarcodeItem = null;

    public void setSelectedItem(String barcode, BarcodeItem barcodeItem) {
        selectedBarcode = barcode;
        selectedBarcodeItem = barcodeItem;
    }

    public BarcodeItem getSelectedItem() {
        return selectedBarcodeItem;
    }

    public void clearSelectedItem() {
        selectedBarcode = null;
        selectedBarcodeItem = null;
    }

    public BarcodeItem findExistingBarcodeItem(String barcode) {
        if (StringUtility.isNullOrEmpty(barcode)) {
            return null;
        }
        return barcode.equals(selectedBarcode) ? selectedBarcodeItem : null;
    }

    public BarcodeInfo findBarcodeItems(String barcode) throws Exception {
        return ClientServiceFactory.getItemServices().findBarcodeInfoForItemScan(barcode, SimRepository.getStoreId());
    }

    public List<StockItem> convertToStockItems(List<BarcodeItem> barcodeItems) {
        List<StockItem> stockItems = new ArrayList<>(barcodeItems.size());
        for (BarcodeItem barcodeItem : barcodeItems) {
            stockItems.add(barcodeItem.getStockItem());
        }
        return stockItems;
    }

    public BarcodeItem convertToBarcodeItem(List<BarcodeItem> barcodeItems, StockItem stockItem) {
        for (BarcodeItem barcodeItem : barcodeItems) {
            if (barcodeItem.getId().equals(stockItem.getId())) {
                return barcodeItem;
            }
        }
        return null;
    }

    public boolean isAutoApply() {
        return getStoreBoolean(StoreConfigKeys.AUTO_APPLY_ADVANCED_ITEM_ENTRY);
    }

    public boolean isAllowNonRangedItems() {
        return SimConfigManager.getBoolean(SimConfigManager.ALLOW_NON_RANGE_ITEM);
    }

    public boolean isUinProcessingEnabled() {
        return getStoreBoolean(StoreConfigKeys.UIN_PROCESSING_ENABLED);
    }
}
