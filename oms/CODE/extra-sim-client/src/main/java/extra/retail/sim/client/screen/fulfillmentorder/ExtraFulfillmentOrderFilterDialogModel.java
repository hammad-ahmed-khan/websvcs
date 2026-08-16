package extra.retail.sim.client.screen.fulfillmentorder;

import java.util.List;

import extra.retail.sim.common.baselv.BaseLV;
import extra.retail.sim.common.business.ExtraBOFactory;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderQueryFilter;
import extra.retail.sim.service.core.ExtraClientServiceFactory;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderStatus;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderType;
import oracle.retail.sim.common.item.ItemVO;

/********************************************************************************************************
 * Fulfillment Order Filter Dialog Model
 * <p>
 * Provides logic to the Customer Order Filter Dialog.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraFulfillmentOrderFilterDialogModel extends SimScreenModel {
    private ExtraFulfillmentOrderQueryFilter filter;

    /**
     * Sets the current FulfillmentOrderQueryFilter to the input filter.
     * @param filter A FulfillmentOrderQueryFilter to set.
     */
    public void setFilter(ExtraFulfillmentOrderQueryFilter filter) {
        this.filter = filter;
    }

    /**
     * Returns the current FulfillmentOrderQueryFilter.
     * @return The current FulfillmnetOrderQueryFilter.
     */
    public ExtraFulfillmentOrderQueryFilter getFilter() {
        return filter;
    }

    /**
     * Creates a FulfillmentOrderQueryFilter with default values.
     * @return A new FulfillmentOrderQueryFilter with default values.
     */
    public ExtraFulfillmentOrderQueryFilter resetFilter() throws BusinessException {
        filter = ExtraBOFactory.createFulfillmentOrderQueryFilter();
        filter.setStatus(FulfillmentOrderStatus.ACTIVE);
        filter.setStoreId(getStoreId());
        return filter;
    }

    /**
     * Returns an ItemVO representing the item for which to search FulfillmentOrders for.
     * @return An ItemVO representing an item by which to search FulfillmentOrders for.
     */
    public ItemVO loadItem() {
        return (ItemVO) RepositoryManager.getStateObject(SimClientStateKey.FULFILLMENT_ORDER_FILTER_ITEM_VO);
    }

    /**
     * Returns a List of all FulfillmentOrderTypes.
     * @return A List of all FulfillmentOrderTypes.
     */
    public List<FulfillmentOrderType> findOrderTypes() throws Exception {
        return SimEnumUtility.findAllFulfillmentOrderTypes();
    }

    /**
     * Returns a List containing all FulfillmentOrderStatus values.
     * @return A List containing all FulfillmentOrderStatus values.
     */
    public List<FulfillmentOrderStatus> findCustomerOrderStatus() {
        return SimEnumUtility.findAllCustomerOrderStatus();
    }

	public List<BaseLV> getDeliveryModes() throws Exception {
		return ExtraClientServiceFactory.getBaseLVServices().findBaseListOfValues("DELIVERY_MODE");
	}
}
