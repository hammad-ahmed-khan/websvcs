package oracle.retail.sim.client.screen.carton;

import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCarton;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCartonQueryFilter;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCartonVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Carton Lookup Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class CartonLookupModel extends SimScreenModel {
    public void storeWarehouseDeliveryCarton(WarehouseDeliveryCartonVO cartonVO) throws Exception {
        WarehouseDeliveryCarton carton = ClientServiceFactory.getWarehouseDeliveryServices().readWarehouseDeliveryCarton(cartonVO.getId());
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_CARTON, carton);
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_CARTON_VO, cartonVO);
    }

    public void clearState() {
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_CARTON);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_CARTON_VO);
    }

    public List<WarehouseDeliveryCartonVO> findWarehouseDeliveryCartonVOs(WarehouseDeliveryCartonQueryFilter filter) throws Exception {
        filter.doSetStoreId(getStoreId());
        return ClientServiceFactory.getWarehouseDeliveryServices().findWarehouseDeliveryCartonVOs(filter);
    }

    public Integer getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_CONTAINER_LOOKUP);
    }
}
