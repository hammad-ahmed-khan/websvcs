package oracle.retail.sim.client.screen.stockcount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.productgroup.ProductGroup;
import oracle.retail.sim.common.productgroup.ProductGroupHierarchy;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.report.StorePrinter;
import oracle.retail.sim.common.stockcount.StockCountRejectedLineItem;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Stock Count Rejected Items Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountRejectedItemsModel extends SimScreenModel {
    private StockCountWrapper stockCountWrapper;
    private ProductGroup productGroup;

    public String getScheduleDescription() {
        if (stockCountWrapper == null) {
            stockCountWrapper = (StockCountWrapper) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_STOCK_COUNT);
        }
        return stockCountWrapper.getGroupDescription();
    }

    public List<StockCountRejectedLineItem> getRejectedLineItems() {
        return (List<StockCountRejectedLineItem>) RepositoryManager.getStateObject(SimClientStateKey.STOCK_COUNT_REJECTED_ITEMS);
    }

    public boolean isRejectedLineItemsEditable() {
        Boolean value = (Boolean) RepositoryManager.getStateObject(SimClientStateKey.STOCK_COUNT_REJECTED_ITEMS_EDITABLE);
        if (value == null) {
            value = Boolean.FALSE;
        }
        return value;
    }

    public List<StockCountRejectedLineItem> reloadRejectedItems() throws Exception {
        return ClientServiceFactory.getStockCountLineItemServices().findRejectedLineItems(stockCountWrapper.getStoreId(), stockCountWrapper.getId());
    }

    public ReportResponse printRejectedLineItems(StorePrinter printer) throws Exception {
        return StockCountPrintUtility.printRejectedLineItems(getStoreId(), printer);
    }

    public ReportResponse printRejectedLineItems(List<RetailStoreFormatPrinter> formatPrinters) throws Exception {
        return StockCountPrintUtility.printRejectedLineItems(getStoreId(), formatPrinters);
    }

    public List<String> updateRejectedLineItems(List<StockCountRejectedLineItem> lineItems) throws Exception {
        List<StockCountRejectedLineItem> lineItemsToProcess = new ArrayList<>();
        for (StockCountRejectedLineItem lineItem : lineItems) {
            if (lineItem.getStockItem() != null && !lineItem.isResolved()) {
                lineItemsToProcess.add(lineItem);
            }
        }
        if (lineItemsToProcess.isEmpty()) {
            return Collections.emptyList();
        }
        return ClientServiceFactory.getStockCountLineItemServices().updateRejectedLineItems(stockCountWrapper.getStockCount().getId(), lineItemsToProcess);
    }

    /*
     * Checks that stock item exists on the product group of the current stock count. This is not
     * entirely valid since the product group may have been modified, but its close enough.
     */

    public boolean isValidStockItem(StockItem stockItem) throws Exception {
        if (productGroup == null) {
            productGroup = ClientServiceFactory.getProductGroupServices().readProductGroup(stockCountWrapper.getStockCount().getGroupId());
        }
        if (productGroup.isAllItems()) {
            return true;
        }
        for (String itemId : productGroup.getSingleItemIds()) {
            if (itemId.equals(stockItem.getId())) {
                return true;
            }
        }
        for (ProductGroupHierarchy hierarchy : productGroup.getHierarchies()) {
            Long departmentId = hierarchy.getDepartmentId();
            Long classId = hierarchy.getClassId();
            Long subclassId = hierarchy.getSubclassId();

            if (departmentId.equals(stockItem.getDepartmentId())) {
                if (classId == null) {
                    return true;
                }
                if (classId.equals(stockItem.getClassId())) {
                    if (subclassId == null) {
                        return true;
                    }
                    return subclassId.equals(stockItem.getSubclassId());
                }
            }
        }
        return false;
    }
}
