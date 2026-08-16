package oracle.retail.sim.client.screen.carton;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCarton;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCartonVO;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryLineItem;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class CartonDetailModel extends SimScreenModel {
    public WarehouseDeliveryCarton getWarehouseDeliveryCarton() {
        return (WarehouseDeliveryCarton) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_CARTON);
    }

    public WarehouseDeliveryCartonVO getWarehouseDeliveryCartonVO() {
        return (WarehouseDeliveryCartonVO) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_CARTON_VO);
    }

    public void clearState() {
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_CARTON);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_CARTON_VO);
    }

    public List<CartonLineItemWrapper> getLineItemWrappers(WarehouseDeliveryCarton carton) {
        List<WarehouseDeliveryLineItem> lineItems = carton.getLineItems();
        List<CartonLineItemWrapper> wrappers = new ArrayList<CartonLineItemWrapper>(lineItems.size());
        for (WarehouseDeliveryLineItem lineItem : lineItems) {
            wrappers.add(ClientWrapperFactory.createCartonLineItemWrapper(lineItem));
        }
        return wrappers;
    }
}
