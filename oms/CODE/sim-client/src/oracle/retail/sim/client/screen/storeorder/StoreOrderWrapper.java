package oracle.retail.sim.client.screen.storeorder;

import java.util.Date;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.source.Source;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.common.storeorder.StoreOrder;

/********************************************************************************************************
 * Store Order Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreOrderWrapper {

    private StoreOrder storeOrder;

    public StoreOrderWrapper(StoreOrder storeOrder) {
        if (storeOrder == null) {
            throw new IllegalArgumentException("Store order can't be null.");
        }
        this.storeOrder = storeOrder;
    }

    public StoreOrder getStoreOrder() {
        return storeOrder;
    }

    public String getApprovalUser() {
        return storeOrder.getApprovalUser();
    }

    public Date getCreationDate() {
        return storeOrder.getCreationDate();
    }

    public String getCreationUser() {
        return storeOrder.getCreationUser();
    }

    public String getCurrencyCode() {
        return storeOrder.getCurrencyCode();
    }

    public Store getToLocation() {
        return storeOrder.getToLocation();
    }

    public Date getNotAfterDate() {
        return storeOrder.getNotAfterDate();
    }

    public Date getNotBeforeDate() {
        return storeOrder.getNotBeforeDate();
    }

    public Source getFromLocation() {
        return storeOrder.getFromLocation();
    }

    public int getStatus() {
        return storeOrder.getStatus().getCode();
    }

    public String getStatusDescription() {
        return storeOrder.getStatusDescription();
    }

    public String getStoreOrderNumber() {
        return storeOrder.getStoreOrderNumber();
    }

    public String getShortDescription() {
        return storeOrder.getShortDescription();
    }

    public Quantity getQuantity() {
        return storeOrder.getQuantity();
    }
}
