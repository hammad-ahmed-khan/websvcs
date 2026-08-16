package oracle.retail.sim.client.screen.fulfillmentorder;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.fulfillmentorder.CustomerAddress;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderContactInfo;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Customer Detail Model
 * <p>
 * Provides business logic to the Customer Detail Screen.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class CustomerDetailModel extends SimScreenModel {

    private FulfillmentOrder fulfillmentOrder;
    private FulfillmentOrderContactInfo orderContactInfo;

    /**
     * Loads addresses of the customer attached to the current customer order.
     */
    public void loadAddresses() throws Exception {
        fulfillmentOrder = (FulfillmentOrder) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER);
        orderContactInfo = ClientServiceFactory.getFulfillmentOrderServices().readFulfillmentOrderContactInfo(fulfillmentOrder.getId());
    }

    /**
     * Returns the id of the customer attached to the current customer order.
     * @return The unique identifier of the customer attached to the current customer order.
     */
    public String getCustomerId() {
        return fulfillmentOrder.getCustomerId();
    }

    /**
     * Returns the billing address from the list of loaded customer addresses.
     * @return The billing address from the list of loaded customer addresses.
     */
    public CustomerAddress getBillingAddress() {
        return orderContactInfo.getBillingAddress();
    }

    /**
     * Returns the delivery address from the list of loaded customer addresses.
     * @return The delivery address from the list of loaded customer addresses.
     */
    public CustomerAddress getDeliveryAddress() {
        return orderContactInfo.getDeliveryAddress();
    }
}
