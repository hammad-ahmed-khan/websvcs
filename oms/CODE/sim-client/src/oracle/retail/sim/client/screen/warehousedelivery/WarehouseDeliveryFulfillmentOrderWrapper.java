package oracle.retail.sim.client.screen.warehousedelivery;

import oracle.retail.sim.common.business.Wrapper;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderStatus;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class WarehouseDeliveryFulfillmentOrderWrapper extends Wrapper {
    private String customerOrderId;
    private String fulfillmentOrderExternalId;
    private String cartonExternalId;
    private FulfillmentOrderStatus status;
    private Integer numberOfLineItems;
    private String comments;

    public String getCustomerOrderId() {
        return customerOrderId;
    }

    public void setCustomerOrderId(String customerOrderId) {
        this.customerOrderId = customerOrderId;
    }

    public String getFulfillmentOrderExternalId() {
        return fulfillmentOrderExternalId;
    }

    public void setFulfillmentOrderExternalId(String fulfillmentOrderExternalId) {
        this.fulfillmentOrderExternalId = fulfillmentOrderExternalId;
    }

    public String getCartonExternalId() {
        return cartonExternalId;
    }

    public void setCartonExternalId(String cartonExternalId) {
        this.cartonExternalId = cartonExternalId;
    }

    public FulfillmentOrderStatus getStatus() {
        return status;
    }

    public void setStatus(FulfillmentOrderStatus status) {
        this.status = status;
    }

    public Integer getNumberOfLineItems() {
        return numberOfLineItems;
    }

    public void setNumberOfLineItems(Integer numberOfLineItems) {
        this.numberOfLineItems = numberOfLineItems;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
