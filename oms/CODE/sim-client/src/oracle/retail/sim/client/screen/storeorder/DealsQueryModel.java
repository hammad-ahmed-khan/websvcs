package oracle.retail.sim.client.screen.storeorder;

import java.util.Date;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.deals.Deal;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.storeorder.StoreOrderLineItem;
import oracle.retail.sim.common.storeorder.StoreOrderMessageText;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Deals Query Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DealsQueryModel extends SimScreenModel {
    private Supplier supplier;
    private StoreOrderLineItem lineItem;
    private Date notBeforeDate;

    public void loadStateInformation() {
        supplier = (Supplier) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_SUPPLIER);
        lineItem = (StoreOrderLineItem) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_ITEM);
        notBeforeDate = (Date) RepositoryManager.getStateObject(SimClientStateKey.STORE_ORDER_NOT_BEFORE_DATE);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_SUPPLIER);
        RepositoryManager.removeStateObject(SimClientStateKey.STORE_ORDER_NOT_BEFORE_DATE);
    }

    public List findDealsQuery() throws Exception {
        if (supplier == null) {
            throw new BusinessException(StoreOrderMessageText.STORE_ORDER_NO_SUPPLIER);
        }
        String itemId = lineItem.getOrderItem().getId();
        String supplierId = supplier.getId();

        List<Deal> deals = ClientServiceFactory.getDealServices().findDeals(getStoreId(), supplierId, itemId, notBeforeDate);
        if (deals.isEmpty()) {
            throw new BusinessException(StoreOrderMessageText.MISSING_DEALS);
        }
        return deals;
    }
}
