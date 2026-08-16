package oracle.retail.sim.client.screen.warehousedelivery;

import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.business.Wrapper;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCarton;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryStatus;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class WarehouseDeliveryCartonWrapper extends Wrapper {
    private WarehouseDeliveryCarton carton;

    public WarehouseDeliveryCartonWrapper(WarehouseDeliveryCarton carton) {
        this.carton = carton;
    }

    public WarehouseDeliveryCarton getCarton() {
        return carton;
    }

    public Long getId() {
        return carton.getId();
    }

    public String getExternalId() {
        return carton.getExternalId();
    }

    public WarehouseDeliveryStatus getStatus() {
        return carton.getStatus();
    }

    public Quantity getNumberOfCasesExpected() {
        return carton.getNumberOfCasesExpected();
    }

    public boolean isSerialNumberRequired() {
        return carton.isSerialNumberRequired();
    }

    public boolean isFulfillmentOrderRelated() {
        return carton.isFulfillmentOrderRelated();
    }

    public boolean isOnlyFulfillmentOrder() {
        return carton.isOnlyFulfillmentOrder();
    }

    public WarehouseDeliveryFulfillmentOrderIndicator getFulfillmentOrderIndicator() {
        if (!isFulfillmentOrderRelated()) {
            return WarehouseDeliveryFulfillmentOrderIndicator.NO;
        }
        if (isOnlyFulfillmentOrder()) {
            return WarehouseDeliveryFulfillmentOrderIndicator.YES;
        }
        return WarehouseDeliveryFulfillmentOrderIndicator.MIXED;
    }

    public boolean isPropertyModifiable(String property) throws Exception {
        return false;
    }
}
