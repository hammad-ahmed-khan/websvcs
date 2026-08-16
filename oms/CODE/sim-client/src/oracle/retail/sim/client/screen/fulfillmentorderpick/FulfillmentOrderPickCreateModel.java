package oracle.retail.sim.client.screen.fulfillmentorderpick;

import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.fulfillmentorderpick.DefaultCustomerOrderPickingMethodOptions;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPick;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickType;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPossiblePickVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Fulfillment Order Pick Create Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderPickCreateModel extends SimScreenModel {

    /**
     * Returns the store's default picking type based on store settings.
     * @return The current store's default picking type.
     */
    public FulfillmentOrderPickType getDefaultType() {
        String description = getStoreString(StoreConfigKeys.DEFAULT_CUSTOMER_ORDER_PICKING_METHOD);
        DefaultCustomerOrderPickingMethodOptions pickMethod = DefaultCustomerOrderPickingMethodOptions.toValue(description);
        if (pickMethod == DefaultCustomerOrderPickingMethodOptions.BIN) {
            return FulfillmentOrderPickType.BIN;
        }
        return FulfillmentOrderPickType.ORDER;
    }

    /**
     * Returns whether or not the user is allowed to overwrite the default number of Bins for a pick.
     * @return True if the user is allowed to overwrite the default number of bins, otherwise false.
     */
    public boolean isBinQtyEnabled() {
        return getStoreBoolean(StoreConfigKeys.OVERRIDE_BIN_QUANTITY);
    }

    /**
     * Returns the default number of Bins for a pick.
     * @return The default number of Bins for a pick based on store settings.
     */
    public Integer getDefaultBinQty() {
        return getStoreInteger(StoreConfigKeys.DEFAULT_NUMBER_OF_BINS);
    }

    /**
     * Returns a List of FulfillmentOrderPossiblePickVO representing fulfillment orders that can be picked.
     * @return A List of FulfillmentOrderPossiblePickVO representing fulfillment orders that can be picked.
     */
    public List<FulfillmentOrderPossiblePickVO> getFulfillmentOrderPossiblePickVOs() throws Exception {
        //If this flag is true, return an empty list
        if (getStoreBoolean(StoreConfigKeys.RESERVE_CUSTOMER_ORDER_INVENTORY_UPON_RECEIVING)) {
            return Collections.emptyList();
        }
        return ClientServiceFactory.getFulfillmentOrderServices().readFulfillmentOrdersForPicking(getStoreId());
    }

    /**
     * Creates a Fulfillment Order Pick for the Fulfillment Order corresponding to the input SIM Customer Order ID.
     * @param fulfillmentOrderId The SIM Customer Order ID of the Fulfillment Order for which to create a Pick.
     */
    public void createPick(Long fulfillmentOrderId) throws Exception {
        FulfillmentOrderPick pick = ClientServiceFactory.getFulfillmentOrderPickServices().createFulfillmentOrderPickForFulfillmentOrder(fulfillmentOrderId);
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER_PICK, pick);
        RepositoryManager.addStateObject(SimClientStateKey.CUSTOMER_ORDER_PICK_MODIFIED, Boolean.TRUE);
    }

    /**
     * Creates a Fulfillment Order Pick for the specific number of Bins, each representing one Fulfillment Order.
     * @param numberOfBins The number of Bins to add onto a newly created Pick. Each Bin represents one Fulfillment Order.
     */
    public void createPick(Integer numberOfBins) throws Exception {
        FulfillmentOrderPick pick = ClientServiceFactory.getFulfillmentOrderPickServices().createFulfillmentOrderPickByBins(numberOfBins, getStoreId());
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER_PICK, pick);
        RepositoryManager.addStateObject(SimClientStateKey.CUSTOMER_ORDER_PICK_MODIFIED, Boolean.TRUE);
    }
}
