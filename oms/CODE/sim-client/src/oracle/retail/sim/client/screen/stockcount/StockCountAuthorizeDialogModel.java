package oracle.retail.sim.client.screen.stockcount;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.item.StockCountItem;
import oracle.retail.sim.common.lineitem.UOMConstants;
import oracle.retail.sim.common.stockcount.StockCountLineItemAreaBreakdownVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Stock Count Authorization Multiple Location Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountAuthorizeDialogModel extends SimScreenModel {
    private StockCountWrapper stockCountWrapper;
    private StockCountLineItemWrapper lineItemWrapper;
    private QuantityDisplayer quantityDisplayer = new QuantityDisplayer();

    public void setStockCount(StockCountWrapper stockCount) {
        stockCountWrapper = stockCount;
    }

    public void setLineItem(StockCountLineItemWrapper lineItem) {
        lineItemWrapper = lineItem;
    }

    public boolean isRecountRequired() {
        if (stockCountWrapper != null) {
            return stockCountWrapper.isRecountRequired();
        }
        return false;
    }

    public List<StockCountLineItemAuthWrapper> findDetailLineItems() throws Exception {
        List<StockCountLineItemAuthWrapper> wrappers = new ArrayList<>();
        List<StockCountLineItemAreaBreakdownVO> breakdownVOs = loadDetails(stockCountWrapper.getStoreId(), stockCountWrapper.getId(), lineItemWrapper.getItemId());
        for (StockCountLineItemAreaBreakdownVO vo : breakdownVOs) {
            wrappers.add(ClientWrapperFactory.createStockCountLineItemAuthWrapper(vo));
        }
        return wrappers;
    }

    private List<StockCountLineItemAreaBreakdownVO> loadDetails(Long storeId, Long stockCountId, String itemId) throws Exception {
        return ClientServiceFactory.getStockCountLineItemServices().findLineItemSequencedAreaBreakdownDetails(storeId, stockCountId, itemId);
    }

    public StockCountItem getStockCountItem() {
        return lineItemWrapper.getLineItem().getStockCountItem();
    }

    public String getTotalCountDisplayValue(List<StockCountLineItemAuthWrapper> lineItems) {
        Quantity totalCountQuantity = Quantity.ZERO;
        for (StockCountLineItemAuthWrapper tmpLineItem : lineItems) {
            Quantity countedQty = tmpLineItem.getStockRecounted();
            if (countedQty == null) {
                countedQty = tmpLineItem.getStockCounted();
            }
            if (countedQty != null) {
                totalCountQuantity = totalCountQuantity.add(countedQty);
            }
        }
        return formatUnitOfMeasureCount(totalCountQuantity);
    }

    public String getStockOnHandDisplayValue() {
        return formatUnitOfMeasureCount(lineItemWrapper.getSnapshot());
    }

    public String getAuthorizedQuantityDisplayValue() {
        return formatUnitOfMeasureCount(lineItemWrapper.getStockApproved());
    }

    private String formatUnitOfMeasureCount(Quantity quantity) {
        String uom = getStockCountItem().getUnitOfMeasure();
        if (UOMConstants.EACHES.equals(uom)) {
            uom = Translator.getText(UOMConstants.UNITS);
        }
        if (quantity == null) {
            quantity = Quantity.ZERO;
        }
        return quantityDisplayer.getDisplayText(quantity) + " " + uom;
    }
}
