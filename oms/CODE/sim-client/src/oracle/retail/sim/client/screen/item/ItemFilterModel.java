package oracle.retail.sim.client.screen.item;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.invadjustment.InventoryStatus;
import oracle.retail.sim.common.item.NonSellableQtyType;
import oracle.retail.sim.common.source.Finisher;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.source.Warehouse;
import oracle.retail.sim.common.uda.UDADetail;
import oracle.retail.sim.common.uda.UDAType;
import oracle.retail.sim.common.uda.UDAValue;

/********************************************************************************************************
 * Item Filter Panel Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemFilterModel extends SimScreenModel {

    public Supplier getSupplier() {
        return (Supplier) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_SUPPLIER);
    }

    public Warehouse getWarehouse() {
        return (Warehouse) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_WAREHOUSE);
    }

    public Finisher getFinisher() {
        return (Finisher) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_FINISHER);
    }

    public List<UDADetail> findAllTextUdaTypes() throws Exception {
        return findUdaDetails(UDAType.TEXT);
    }

    public List<UDADetail> findAllValueUdaTypes() throws Exception {
        return findUdaDetails(UDAType.VALUE);
    }

    public List<UDADetail> findAllDateUdaTypes() throws Exception {
        return findUdaDetails(UDAType.DATE);
    }

    public List<Warehouse> findAllWarehouses() throws Exception {
        return new ArrayList<Warehouse>(ClientDataCacheUtility.getAllWarehouses().values());
    }

    public List<InventoryStatus> findAllInventoryStatus() {
        return InventoryStatus.getList();
    }

    public List<NonSellableQtyType> findAllNonSellableQtyTypes() throws Exception {
        return ClientDataCacheUtility.getNonSellableQtyTypes();
    }

    private List<UDADetail> findUdaDetails(UDAType type) throws Exception {
        List<UDADetail> detailsForType = new ArrayList<>();
        for (UDADetail udaDetail : ClientDataCacheUtility.getUDADetails()) {
            if (udaDetail.getType() == type) {
                detailsForType.add(udaDetail);
            }
        }
        return detailsForType;
    }

    public List<UDAValue> findUDAValues(UDADetail udaDetail) throws Exception {
        return ClientDataCacheUtility.getUDAValues(udaDetail);
    }
}
