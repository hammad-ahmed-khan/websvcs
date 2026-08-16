package oracle.retail.sim.client.screen.fulfillmentorderpick;

import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickQueryFilter;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickStatus;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickType;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Fulfillment Order Pick Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderPickFilterDialogModel extends SimScreenModel {

    private FulfillmentOrderPickQueryFilter filter;

    /**
     * Sets a FulfillmentOrderPickQueryFilter to the Customer Order Pick Filter Dialog.
     * @param filter The Filter to set to the Customer Order Pick Filter Dialog.
     */
    public void setFilter(FulfillmentOrderPickQueryFilter filter) {
        this.filter = filter;
    }

    /**
     * Returns the FulfillmentOrderPickQueryFilter currently in use by the 
     * Customer Order Pick Filter Dialog.
     * @return The filter currently in use by the Customer Order Pick Query Filter Dialog.
     */
    public FulfillmentOrderPickQueryFilter getFilter() {
        return filter;
    }

    /**
     * Returns a new Fulfillment Order Pick Query Filter with default settings.
     * @return A new Fulfillment Order Pick Query Filter with default settings.
     */
    public FulfillmentOrderPickQueryFilter resetFilter() throws BusinessException {
        filter = BOFactory.createFulfillmentOrderPickQueryFilter();
        filter.setStatus(FulfillmentOrderPickStatus.ACTIVE);
        filter.setStoreId(getStoreId());
        return filter;
    }

    /**
     * Returns the ItemVO currently selected by the Customer Order Pick Query Filter dialog.
     * @return The ItemVO currently selected.
     */
    public ItemVO loadItem() {
        return (ItemVO) RepositoryManager.getStateObject(SimClientStateKey.FULFILLMENT_ORDER_PICK_FILTER_ITEM_VO);
    }

    /**
     * Returns a List of all values for FulfillmentOrderPickStatus.
     * @return A List of all values for FulfillmentOrderPickStatus.
     */
    public List<FulfillmentOrderPickStatus> findFulfillmentOrderPickStatus() {
        return SimEnumUtility.findAllFulfillmentOrderPickStatus();
    }

    /**
     * Returns a List of all FulfillmentOrderPickTypes.
     * @return A List of all FulfillmentOrderPickTypes.
     */
    public List<FulfillmentOrderPickType> findFulfillmentOrderPickTypes() {
        return SimEnumUtility.findAllFulfillmentOrderPickTypes();
    }

    /**
     * Returns a List of usernames attached to FulfillmentOrderPicks.
     * @return A List of usernames attached to FulfillmentOrderPicks.
     */
    public List<String> findUsernames() throws Exception {
        return ClientServiceFactory.getFulfillmentOrderPickServices().findFulfillmentOrderPickUsernames(getStoreId());
    }
}
