package oracle.retail.sim.client.screen.stockcount;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.item.StockCountItem;
import oracle.retail.sim.common.lineitem.UOMConstants;
import oracle.retail.sim.common.stockcount.StockCountLineItemCompBreakdownVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Stock Count Component Detail Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountComponentDetailModel extends SimScreenModel {
    private StockCountWrapper stockCountWrapper;
    private StockCountLineItemWrapper lineItemWrapper;

    private QuantityDisplayer quantityDisplayer = new QuantityDisplayer();

    public void setStockCount(StockCountWrapper stockCount) {
        stockCountWrapper = stockCount;
    }

    public void setLineItem(StockCountLineItemWrapper lineItem) {
        lineItemWrapper = lineItem;
    }

    public List<StockCountComponentDetailWrapper> findDetailLineItems() throws Exception {
        List<StockCountComponentDetailWrapper> wrappers = new ArrayList<>();
        List<StockCountLineItemCompBreakdownVO> breakdownVOs = loadDetails(stockCountWrapper.getStoreId(), stockCountWrapper.getId(), lineItemWrapper.getItemId());
        for (StockCountLineItemCompBreakdownVO vo : breakdownVOs) {
            wrappers.add(ClientWrapperFactory.createStockCountComponentDetailWrapper(vo));
        }
        return wrappers;
    }

    private List<StockCountLineItemCompBreakdownVO> loadDetails(Long storeId, Long stockCountId, String itemId) throws Exception {
        return ClientServiceFactory.getStockCountLineItemServices().findLineItemComponentCountBreakdownDetails(storeId, stockCountId, itemId);
    }

    private StockCountComponentDetailWrapper buildComponentLineItem() {
        StockCountLineItemCompBreakdownVO breakdownVO = BOFactory.createStockCountLineItemCompBreakdownVO();
        breakdownVO.doSetItemId(lineItemWrapper.getItemId());
        breakdownVO.doSetShortDescription(lineItemWrapper.getShortDescription());
        breakdownVO.doSetLongDescription(lineItemWrapper.getLongDescription());
        breakdownVO.doSetUnitOfMeasure(lineItemWrapper.getLineItem().getUnitOfMeasure());
        breakdownVO.doSetItemType(lineItemWrapper.getLineItem().getStockCountItem().getItemType());
        breakdownVO.doSetStockCounted(lineItemWrapper.getStockCounted());
        breakdownVO.doSetStockRecounted(lineItemWrapper.getStockRecounted());

        return ClientWrapperFactory.createStockCountComponentDetailWrapper(breakdownVO);
    }

    public StockCountItem getStockCountItem() {
        return lineItemWrapper.getLineItem().getStockCountItem();
    }

    public String getTotalCountDisplayValue() {
        if (lineItemWrapper.getStockRecountedTotal() != null) {
            return formatUnitOfMeasureCount(lineItemWrapper.getStockRecountedTotal());
        }
        return formatUnitOfMeasureCount(lineItemWrapper.getStockCountedTotal());
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
