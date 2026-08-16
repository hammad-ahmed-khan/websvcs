package extra.retail.sim.client.screen.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.fulfillmentorder.ItemFulfillmentOrderVO;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.ItemImage;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.PackHeaderVO;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.reportrequest.ItemReportRequest;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.uda.ItemUDAVO;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.source.SourceServices;

/********************************************************************************************************
 * Item Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraItemDetailModel extends SimScreenModel {
    private ItemDetailVO itemDetailVO;
    private List<PackHeaderVO> packHeaderVOs;
    private List<ImageIcon> imageIcons;

    public void loadItem() throws Exception {
        Object data = RepositoryManager.getStateObject(SimClientStateKey.SELECTED_ITEM);
        if (data instanceof StockItem) {
            StockItem stockItem = (StockItem) data;
            itemDetailVO = ClientServiceFactory.getItemServices().readItemDetailVO(stockItem.getId(), getStoreId());
            packHeaderVOs = null;
            imageIcons = null;
        } else if (data instanceof ItemVO) {
            ItemVO itemVO = (ItemVO) data;
            itemDetailVO = ClientServiceFactory.getItemServices().readItemDetailVO(itemVO.getId(), getStoreId());
            packHeaderVOs = null;
            imageIcons = null;
        } else if (data instanceof ItemDetailVO) {
            itemDetailVO = (ItemDetailVO) data;
            packHeaderVOs = null;
            imageIcons = null;
        }
    }

    public ItemDetailVO getItem() {
        return itemDetailVO;
    }

    public List<PackHeaderVO> getPackItems() throws Exception {
        if (packHeaderVOs == null) {
            packHeaderVOs = ClientServiceFactory.getItemServices().findPackHeaderVOsContainingItem(itemDetailVO.getId(), getStoreId());
        }
        return packHeaderVOs;
    }

    public void storeItem() {
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_ITEM, itemDetailVO);
    }

    public Supplier getSupplier() throws Exception {
        return ClientServiceFactory.getSourceServices().readSupplier(itemDetailVO.getSupplierVO().getId(), getStoreId());
    }

    public boolean isDisplaySequenceActive() {
        return getStoreBoolean(StoreConfigKeys.DISPLAY_SEQUENCE_FIELDS);
    }

    public boolean isDisplayDeliveryBayActive() {
        return getStoreBoolean(StoreConfigKeys.REPLENISHMENT_DELIVERY_BAY_INVENTORY);
    }

    public List<ItemUDAVO> getUDADetail() throws Exception {
        List<ItemUDAVO> itemUDOVOs = ClientServiceFactory.getUDAServices().findItemUDAVOs(itemDetailVO.getId());
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_ITEM, itemDetailVO);
        RepositoryManager.addStateObject(SimClientStateKey.ITEM_UDA_VOS, itemUDOVOs);
        return itemUDOVOs;
    }

    public List<Supplier> findAdditionalSuppliers() throws Exception {
        if (itemDetailVO.getSupplierVO() == null) {
            return Collections.emptyList();
        }
        SourceServices sourceServices = ClientServiceFactory.getSourceServices();
        List<String> supplierIds = sourceServices.findAdditionalSupplierIds(itemDetailVO.getId(), getStoreId(), itemDetailVO.getSupplierVO().getId());
        if (!supplierIds.isEmpty()) {
            return sourceServices.readSuppliers(supplierIds, getStoreId());
        }
        return Collections.emptyList();
    }

    public boolean hasAdditionalSuppliers() throws Exception {
        if (itemDetailVO.getSupplierVO() == null) {
            return false;
        }
        List<String> supplierIds = ClientServiceFactory.getSourceServices().findAdditionalSupplierIds(itemDetailVO.getId(), getStoreId(), itemDetailVO.getSupplierVO().getId());
        if (supplierIds.isEmpty()) {
            return false;
        }
        RepositoryManager.addStateObject(SimClientStateKey.ITEM_ADDITIONAL_SUPPLIERS, supplierIds);
        return true;
    }

    public boolean hasCustomerOrders() throws Exception {
        List<ItemFulfillmentOrderVO> itemOrders = ClientServiceFactory.getFulfillmentOrderServices().findItemFulfillmentOrderVOs(itemDetailVO.getId(), getStoreId());
        if (itemOrders.isEmpty()) {
            return false;
        }
        RepositoryManager.addStateObject(SimClientStateKey.ITEM_CUSTOMER_ORDERS, itemOrders);
        return true;
    }

    public void printItemDetail() throws Exception {
        if (itemDetailVO != null) {
            List<RetailStoreFormatPrinter> formatPrinters = SimClientPrintUtility.selectFormatPrinter(getStoreId(), ReportFormat.ITEM_DETAIL);
            if (formatPrinters != null && formatPrinters.size() > 0) {
                ItemReportRequest reportRequest = BOFactory.createItemReportRequest(itemDetailVO.getId());
                reportRequest.setStoreId(itemDetailVO.getStoreId());

                SimClientPrintUtility.printReportRequest(reportRequest, formatPrinters, ItemMessageText.ITEM_REPORT_PRINTED);
            }
        }
    }

    public boolean isUINDetailAvailable() throws Exception {
        return isSerialNumberProcessingEnabled() && itemDetailVO.getStockItem().isSerialNumberRequired();
    }

    public boolean isStockLocatorAvailable() {
        return hasPermission(PermissionKey.PC_DISPLAY_STOCK_LOCATOR);
    }

    public boolean isSupplierLookUpAvailable() {
        return hasPermission(PermissionKey.PC_ACCESS_SUPPLIER_LOOKUP);
    }
    
    public boolean isMultipleDeliveryAllowed() {
        if (itemDetailVO != null) {
            if (itemDetailVO.isStoreOrderReplenishmentType() && itemDetailVO.isMultipleDeliveryAllowed()) {
                return getStoreBoolean(StoreConfigKeys.DISPLAY_ITEM_REQUEST_DELIVERY_TIMESLOT);
            }
        }
        return false;
    }

    public boolean isPackInfoAvailable() {
        return itemDetailVO.isComponent();
    }

    public boolean isComponentInfoAvailable() {
        return itemDetailVO.isPack();
    }

    public List<ImageIcon> findItemImages() throws Exception {
        if (imageIcons == null) {
            imageIcons = new ArrayList<>();

            List<ItemImage> itemImages = ClientServiceFactory.getItemServices().findItemImages(itemDetailVO.getId());
            for (ItemImage itemImage : itemImages) {
                imageIcons.add(itemImage.getImage());
            }
        }
        return imageIcons;
    }
}
